package com.yusuf.weaterapp.presentation.dashboard.saved

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yusuf.weaterapp.R
import com.yusuf.weaterapp.domain.model.GeoModel
import com.yusuf.weaterapp.utils.TopBar

@Composable
fun SavedScreen(viewModel: SavedViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {}

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Scaffold(
        topBar = { TopBar(R.string.my_addresses) }
    ) { paddings ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            contentPadding = paddings
        ) {
            items(uiState.savedLocations) { geoModel ->
                SavedItem(geoModel, viewModel::openDeleteDialog)
            }
        }
    }

    if (uiState.showDeleteDialog) {
        DeleteItemDialog(
            onDelete = viewModel::deleteLocation,
            onDismiss = viewModel::dismissDeleteDialog
        )
    }
}

@Composable
private fun DeleteItemDialog(
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 16.dp),
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(R.string.confirm_delete)) },
        text = { Text(text = stringResource(R.string.are_you_sure_delete)) },
        confirmButton = {
            TextButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Red),
                onClick = {
                    onDelete()
                    onDismiss()
                }
            ) {
                Text(
                    stringResource(R.string.delete),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismiss() }
            ) {
                Text(
                    stringResource(R.string.cancel),
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}

@Composable
private fun SavedItem(geoModel: GeoModel, onDeleteItem: (GeoModel) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = { isExpanded = !isExpanded },
        shadowElevation = 2.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = geoModel.name,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = geoModel.address,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_love_location),
                    contentDescription = "Location Icon",
                    modifier = Modifier.size(32.dp)
                )
            }

            AnimatedVisibility(isExpanded) {
                Button(
                    onClick = { onDeleteItem(geoModel) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(76.dp)
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

    }
}

