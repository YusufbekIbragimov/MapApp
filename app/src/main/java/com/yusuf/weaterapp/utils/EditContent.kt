package com.yusuf.weaterapp.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.yusuf.weaterapp.R

@Composable
fun EditContent(
    geoNameValue: String,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    btnText: String,
    onClick: () -> Unit,
    focusRequester: FocusRequester,
    errorMsg: String = ""
) {
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth(),
            value = TextFieldValue(geoNameValue, selection = TextRange(geoNameValue.length)),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            label = {
                Text(text = label)
            },
            trailingIcon = {
                Icon(painter = painterResource(R.drawable.ic_edit), contentDescription = null)
            },
            isError = errorMsg.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            enabled = geoNameValue.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5BC250)),
            content = {
                Text(text = btnText, color = Color.White, style = MaterialTheme.typography.titleMedium)
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}