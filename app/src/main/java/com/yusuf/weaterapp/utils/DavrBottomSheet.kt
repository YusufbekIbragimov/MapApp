package com.yusuf.weaterapp.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yusuf.weaterapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    containerColor: Color = Color.White,
    title: String = "",
    contentIcon: Painter? = null,
    titleContentColor: Color? = null,
    titleHeight: Dp = 76.dp,
    titleColor: Color = Color.Black,
    titleStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    textAlign: TextAlign = TextAlign.Start,
    isShowHeader: Boolean = true,
    shouldDismissOnBackPress: Boolean = true,
    horizontalPadding: Dp = 16.dp,
    maxLines: Int = 1,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    sheetContent: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = {
            scope.launch { sheetState.hide() }
                .invokeOnCompletion { onDismiss() }
        },
        sheetState = sheetState,
        shape = shape,
        containerColor = containerColor,
        properties = ModalBottomSheetDefaults.properties(shouldDismissOnBackPress = shouldDismissOnBackPress),
        dragHandle = {
            if (isShowHeader) {
                Column(
                    modifier = Modifier
                        .background(titleContentColor ?: containerColor)
                        .fillMaxWidth()
                        .height(titleHeight),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(4.dp)
                            .width(32.dp)
                            .background(
                                Color.White,
                                RoundedCornerShape(CornerSize(2.dp))
                            )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPadding),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (contentIcon != null) {
                            Row(
                                horizontalArrangement = horizontalArrangement,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Image(
                                    painter = contentIcon,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = title,
                                    color = titleColor,
                                    style = titleStyle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        } else {
                            if (textAlign == TextAlign.Center) {
                                Spacer(modifier = Modifier.width(48.dp))
                            }
                            Text(
                                modifier = Modifier.weight(1f),
                                text = title,
                                color = titleColor,
                                style = titleStyle,
                                textAlign = textAlign,
                                maxLines = maxLines,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        if (titleContentColor != null) {
                            Image(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(
                                        shape = CircleShape,
                                        color = Color.White.copy(0.12f)
                                    )
                                    .clickable {
                                        scope
                                            .launch { sheetState.hide() }
                                            .invokeOnCompletion { onDismiss() }
                                    }
                                    .padding(6.dp),
                                painter = painterResource(id = R.drawable.ic_cancel),
                                colorFilter = ColorFilter.tint(Color.White),
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        scope
                                            .launch { sheetState.hide() }
                                            .invokeOnCompletion { onDismiss() }
                                    },
                                painter = painterResource(id = R.drawable.ic_cancel),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }) {
        sheetContent()
    }
}