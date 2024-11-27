package com.yusuf.weaterapp.presentation.dashboard.map

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.yandex.mapkit.GeoObject
import com.yusuf.weaterapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBottomSheet(
    sheetState: BottomSheetScaffoldState,
    places: List<GeoObject>,
    query: String,
    onQueryChange: (String) -> Unit,
    showLocation: (GeoObject) -> Unit,
    placeholder: @Composable (() -> Unit)? = { Text(stringResource(R.string.search)) },
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(
            Icons.Default.Search,
            contentDescription = null
        )
    },
    trailingIcon: @Composable (() -> Unit)? = null,
    focusRequester: FocusRequester,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    LaunchedEffect(sheetState.bottomSheetState.currentValue) {
        if (sheetState.bottomSheetState.currentValue == SheetValue.Expanded) {
            focusRequester.requestFocus()
        }
    }

    Column(modifier = Modifier) {
        Spacer(modifier = Modifier.height(140.dp))
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .height(5.dp)
                            .width(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = TextFieldValue(query, selection = TextRange(query.length)),
                    onValueChange = { onQueryChange(it.text) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                        .focusRequester(focusRequester)
                        .animateContentSize(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    placeholder = placeholder,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        leadingIcon?.invoke()
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            trailingIcon?.invoke()
                        }
                    },
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn {
                    items(places) { place ->
                        LocationCard(
                            place,
                            onClick = {
                                showLocation(place)
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(WindowInsets.navigationBars.getBottom(LocalDensity.current).dp))
    }
}

@Composable
private fun LocationCard(geoObject: GeoObject, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                tint = Color.Gray,
                contentDescription = "Location Icon",
                modifier = Modifier.size(32.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = geoObject.name ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = geoObject.descriptionText ?: "",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }
}