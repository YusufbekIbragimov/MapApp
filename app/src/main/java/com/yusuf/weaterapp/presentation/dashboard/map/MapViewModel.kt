package com.yusuf.weaterapp.presentation.dashboard.map

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.GeoObject
import com.yusuf.weaterapp.R
import com.yusuf.weaterapp.domain.model.GeoModel
import com.yusuf.weaterapp.domain.usecases.SaveGeoUseCase
import com.yusuf.weaterapp.utils.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val query: String = "",
    val openSaveAlert: Boolean = false,
    val selectedGeoObject: GeoObject? = null,
    val selectedGeoSaveName: String = "",
    val places: List<GeoObject> = emptyList()
)

@OptIn(FlowPreview::class)
@HiltViewModel
class MapViewModel @Inject constructor(
    private val saveGeoUseCase: SaveGeoUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    private val _openSearchSheet = Channel<Boolean>()
    val openSearchSheet = _openSearchSheet.receiveAsFlow()

    private val _showToast = Channel<String>()
    val showToast = _showToast.receiveAsFlow()

    private val _searchChannel = Channel<String>()
    val searchChannel = _searchChannel.receiveAsFlow()

    private val _findUserLocation = Channel<Unit>()
    val findUserLocation = _findUserLocation.receiveAsFlow()

    private val _userLocation = Channel<Pair<Double, Double>>()
    val userLocation = _userLocation.receiveAsFlow()

    private val _query = MutableStateFlow("")
    private val _openSaveAlert = MutableStateFlow(false)
    private val _selectedGeoSaveName = MutableStateFlow("")
    private val _result = MutableStateFlow<List<GeoObject>>(emptyList())
    private val _selectedGeoObject = MutableStateFlow<GeoObject?>(null)

    init {
        viewModelScope.launch {
            combine(
                _query,
                _result,
                _openSaveAlert,
                _selectedGeoObject,
                _selectedGeoSaveName
            ) { query, result, openSaveAlert, geoObject, selectedGeoSaveName ->
                _uiState.update {
                    it.copy(
                        query = query,
                        places = result,
                        openSaveAlert = openSaveAlert,
                        selectedGeoObject = geoObject,
                        selectedGeoSaveName = selectedGeoSaveName
                    )
                }
            }.collect()
        }
    }

    init {
        viewModelScope.launch {
            _query.debounce(500L)
                .distinctUntilChanged()
                .collectLatest { searchText ->
                    _searchChannel.send(searchText)
                }
        }
    }

    fun onQueryChange(query: String) {
        viewModelScope.launch {
            _query.emit(query)
            if (query.isEmpty()) {
                _result.emit(emptyList())
            }
        }
    }

    fun openSearchSheet() {
        viewModelScope.launch {
            _openSearchSheet.send(true)
        }
    }

    fun onResult(list: List<GeoObject>) {
        viewModelScope.launch {
            _result.emit(list)
        }
    }

    fun showLocation(geoObject: GeoObject?) {
        viewModelScope.launch {
            _selectedGeoObject.emit(geoObject)
            _selectedGeoSaveName.emit(geoObject?.name ?: "")
        }
    }

    fun openSaveAlert() {
        viewModelScope.launch {
            _openSaveAlert.emit(true)
        }
    }

    fun openEditCard(state: Boolean) {
        viewModelScope.launch {
            _openSaveAlert.emit(state)
        }
    }

    fun onValueChange(textFieldValue: TextFieldValue) {
        viewModelScope.launch {
            if (uiState.value.selectedGeoObject != null) {
                _selectedGeoSaveName.emit(textFieldValue.text)
            }
        }
    }

    fun saveGeoWithNewName() {
        viewModelScope.launch {
            saveGeoUseCase(
                geoModel = GeoModel(
                    name = uiState.value.selectedGeoSaveName,
                    address = uiState.value.selectedGeoObject?.descriptionText ?: "",
                    lat = uiState.value.selectedGeoObject?.geometry?.first()?.point?.latitude ?: 0.0,
                    lon = uiState.value.selectedGeoObject?.geometry?.first()?.point?.longitude ?: 0.0,
                )
            )

            _openSearchSheet.send(false)
            _openSaveAlert.emit(false)
            _query.emit("")
            _result.emit(emptyList())
            _selectedGeoSaveName.emit("")
            _selectedGeoObject.emit(null)
            _showToast.send(resourceProvider.getString(R.string.saved))
        }
    }

    fun onClickFloat() {
        viewModelScope.launch {
            _findUserLocation.send(Unit)
        }
    }

    fun setUserLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _userLocation.send(Pair(latitude, longitude))
        }
    }

}