package com.yusuf.weaterapp.presentation.dashboard.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.weaterapp.domain.model.GeoModel
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
    val isLoading: Boolean = false
)

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getGeosListUseCase: GetGeosListUseCase,
    private val navigator: AppNavigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedUiState())
    val uiState = _uiState.asStateFlow()

    private val _loading = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            combine(
                getGeosListUseCase(),
                _loading
            ) { list , loading ->
                _uiState.update {
                    it.copy(
                        savedLocations = list,
                        isLoading = loading
                    )
                }
            }.collect()
        }
    }

}