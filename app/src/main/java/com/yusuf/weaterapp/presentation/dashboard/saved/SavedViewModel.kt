package com.yusuf.weaterapp.presentation.dashboard.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.weaterapp.domain.model.GeoModel
import com.yusuf.weaterapp.domain.usecases.DeleteGeoUseCase
import com.yusuf.weaterapp.domain.usecases.GetGeosListUseCase
import com.yusuf.weaterapp.presentation.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SavedUiState(
    val savedLocations: List<GeoModel> = emptyList(),
    val selectedLocation: GeoModel? = null,
    val showDeleteDialog: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getGeosListUseCase: GetGeosListUseCase,
    private val deleteLocationUseCase: DeleteGeoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedUiState())
    val uiState = _uiState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    private val _openDeleteDialog = MutableStateFlow(false)
    private val _selectedLocation = MutableStateFlow<GeoModel?>(null)

    init {
        viewModelScope.launch {
            combine(
                getGeosListUseCase(),
                _loading,
                _openDeleteDialog,
                _selectedLocation
            ) { list , loading, deleteDialog, location ->
                _uiState.update {
                    it.copy(
                        savedLocations = list,
                        isLoading = loading,
                        showDeleteDialog = deleteDialog,
                        selectedLocation = location
                    )
                }
            }.collect()
        }
    }

    fun openDeleteDialog(geoModel: GeoModel) {
        viewModelScope.launch {
            _selectedLocation.emit(geoModel)
            _openDeleteDialog.emit(true)
        }
    }

    fun deleteLocation() {
        viewModelScope.launch {
            uiState.value.selectedLocation?.let {
                deleteLocationUseCase(it)
                _openDeleteDialog.emit(false)
                _selectedLocation.emit(null)
            }
        }
    }

    fun dismissDeleteDialog() {
        viewModelScope.launch {
            _openDeleteDialog.emit(false)
            _selectedLocation.emit(null)
        }
    }

}