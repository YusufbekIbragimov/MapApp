package com.yusuf.weaterapp.presentation.dashboard.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yusuf.weaterapp.R
import com.yusuf.weaterapp.utils.Constants.Companion.tashkentCameraPosition
import com.yusuf.weaterapp.utils.EditContent
import com.yusuf.weaterapp.utils.ModalBottomSheet
import com.yusuf.weaterapp.utils.SingleEventEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    showBottomSheet: (Boolean) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val editNameSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    val mapView = remember { MapView(context) }

    val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    val listener = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            viewModel.onResult(response.collection.children.mapNotNull { it.obj })
        }

        override fun onSearchError(p0: Error) {}
    }

    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    UserLocationFinder(
        viewModel.findUserLocation,
        viewModel::setUserLocation
    )

    LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
        if (bottomSheetState.bottomSheetState.currentValue != SheetValue.Expanded) {
            focusManager.clearFocus()
            showBottomSheet(true)
        } else showBottomSheet(false)
    }

    LaunchedEffect(Unit) {
        MapKitFactory.getInstance().onStart()
    }

    DisposableEffect(Unit) {
        onDispose {
            showBottomSheet(true)
            MapKitFactory.getInstance().onStop()
        }
    }

    MapEventsHandler(
        viewModel = viewModel,
        bottomSheetState = bottomSheetState,
        showBottomSheet = showBottomSheet,
        context = context,
        mapView = mapView,
        searchManager = searchManager,
        listener = listener
    )

    LocationInfoBottomSheet(uiState, viewModel)
    SaveLocationBottomSheet(uiState, viewModel, editNameSheetState, focusRequester, scope)

    BottomSheetScaffold(
        scaffoldState = bottomSheetState,
        modifier = Modifier.imePadding(),
        sheetContainerColor = Color.Transparent,
        containerColor = MaterialTheme.colorScheme.background,
        sheetDragHandle = {},
        sheetTonalElevation = 0.dp,
        sheetSwipeEnabled = true,
        sheetShadowElevation = 0.dp,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            SearchBottomSheet(
                sheetState = bottomSheetState,
                query = uiState.query,
                places = uiState.places,
                onQueryChange = viewModel::onQueryChange,
                showLocation = viewModel::showLocation,
                trailingIcon = { ClearIcon(viewModel::onQueryChange, focusManager::clearFocus) },
                focusRequester = focusRequester
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = { FloatActionButton(viewModel::onClickFloat) }
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        start = it.calculateStartPadding(LayoutDirection.Ltr),
                        end = it.calculateStartPadding(LayoutDirection.Ltr)
                    )
                    .fillMaxSize()
            ) {
                MapBlock(mapView)

                AnimatedVisibility(
                    bottomSheetState.bottomSheetState.currentValue != SheetValue.Expanded
                ) {
                    SearchBlock(openSearchSheet = viewModel::openSearchSheet)
                }
            }
        }
    }
}

@Composable
private fun ClearIcon(onQueryChange: (String) -> Unit, clearFocus: () -> Unit) {
    Icon(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(
                onClick = {
                    onQueryChange("")
                    clearFocus()
                }),
        imageVector = Icons.Default.Clear,
        contentDescription = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapEventsHandler(
    viewModel: MapViewModel,
    bottomSheetState: BottomSheetScaffoldState,
    showBottomSheet: (Boolean) -> Unit,
    context: Context,
    mapView: MapView,
    searchManager: SearchManager,
    listener: Session.SearchListener
) {
    SingleEventEffect(viewModel.openSearchSheet) { isOpen ->
        if (isOpen) {
            bottomSheetState.bottomSheetState.expand()
            showBottomSheet(false)
        } else {
            bottomSheetState.bottomSheetState.hide()
            showBottomSheet(true)
        }
    }

    SingleEventEffect(viewModel.showToast) { textToast ->
        Toast.makeText(context, textToast, Toast.LENGTH_SHORT).show()
    }

    SingleEventEffect(viewModel.userLocation) { userLocation ->
        mapView.mapWindow.map.move(
            CameraPosition(Point(userLocation.first, userLocation.second), 14f, 0f, 0f),
            com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 0.5f),
            null
        )
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(userLocation.first, userLocation.second)
            setIcon(
                ImageProvider.fromResource(context, R.drawable.ic_marker_loc),
                IconStyle().setScale(0.5f)
            )
        }
    }

    SingleEventEffect(viewModel.searchChannel) { searchText ->
        if (searchText.isNotEmpty()) {
            searchManager.submit(
                searchText,
                VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
                SearchOptions(),
                listener
            )
        }
    }
}

@Composable
private fun UserLocationFinder(
    findUserLocation: Flow<Unit>,
    setUserLocation: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    val applicationContext = context.applicationContext
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { activityResult ->
        if (activityResult) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@rememberLauncherForActivityResult
            }
            fusedLocationClient.lastLocation.addOnSuccessListener {
                setUserLocation(it.latitude, it.longitude)
            }
        }
    }

    SingleEventEffect(findUserLocation) {
        if (
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    setUserLocation(it.latitude, it.longitude)
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    setUserLocation(it.latitude, it.longitude)
                }
            }
        }
    }
}

@Composable
private fun FloatActionButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        color = Color.White,
        shape = CircleShape,
        shadowElevation = 16.dp
    ) {
        Icon(
            modifier = Modifier.padding(12.dp),
            painter = painterResource(R.drawable.ic_go_navigate),
            contentDescription = null
        )
    }
}

@Composable
private fun MapBlock(mapView: MapView) {
    val context = LocalContext.current

    if (locationPermitted(context)) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                mapView.also {
                    it.map.move(tashkentCameraPosition)
                }
            }
        )
    }
}

@Composable
private fun BoxScope.SearchBlock(openSearchSheet: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { openSearchSheet() }
            .align(Alignment.TopCenter),
        shadowElevation = 0.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(start = 16.dp),
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(R.string.search)
            )
        }
    }
}

@Composable
private fun HotelCard(
    address: String,
    rating: Int = 4,
    reviewsCount: Int,
    openSaveAlert: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = address,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RatingBar(rating = rating)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.rate, reviewsCount),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Button(
            onClick = openSaveAlert,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5BC250))
        ) {
            Text(
                text = stringResource(R.string.add_saved),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun RatingBar(
    rating: Int,
    maxRating: Int = 5
) {
    Row {
        for (i in 1..maxRating) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp),
                tint = if (i <= rating) Color.Yellow else Color.Gray,
                painter = painterResource(if (i <= rating) R.drawable.ic_star_active else R.drawable.ic_star_inactive),
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SaveLocationBottomSheet(
    uiState: MapUiState,
    viewModel: MapViewModel,
    editNameSheetState: SheetState,
    focusRequester: FocusRequester,
    scope: CoroutineScope
) {
    if (uiState.openSaveAlert) {
        ModalBottomSheet(
            onDismiss = { viewModel.openEditCard(false) },
            sheetState = editNameSheetState,
            title = stringResource(id = R.string.add_address_saved),
            textAlign = TextAlign.Start
        ) {
            EditContent(
                geoNameValue = uiState.selectedGeoSaveName,
                onValueChange = viewModel::onValueChange,
                focusRequester = focusRequester,
                label = stringResource(R.string.add_saved),
                btnText = stringResource(id = R.string.add_saved),
                onClick = {
                    scope.launch { editNameSheetState.hide() }
                        .invokeOnCompletion {
                            viewModel.saveGeoWithNewName()
                        }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationInfoBottomSheet(uiState: MapUiState, viewModel: MapViewModel) {
    uiState.selectedGeoObject?.let {
        ModalBottomSheet(
            onDismiss = { viewModel.showLocation(null) },
            title = it.name ?: ""
        ) {
            HotelCard(
                address = it.descriptionText ?: "",
                reviewsCount = it.attributionMap.size,
                openSaveAlert = viewModel::openSaveAlert
            )
        }
    }
}

private fun locationPermitted(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}