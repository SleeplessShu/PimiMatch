package com.sleeplessdog.matchthewords.dictionary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.ui.theme.DarkTextDefault
import com.sleeplessdog.matchthewords.ui.theme.Gray05
import com.sleeplessdog.matchthewords.ui.theme.textSize16SemiBold

@Composable
fun GroupActionsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(12.dp),
        containerColor = Gray05,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
    ) {
        ActionMenuItem(
            icon = R.drawable.icon_edit,
            titleId = R.string.button_rename,
            onClick = {
                onDismiss()
                onRename()
            }
        )

        ActionMenuItem(
            icon = R.drawable.icon_delete,
            titleId = R.string.button_delete,
            onClick = {
                onDismiss()
                onDelete()
            }
        )
    }
}

@Composable
private fun ActionMenuItem(
    icon: Int,
    titleId: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = DarkTextDefault,
            modifier = Modifier.size(20.dp)
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = stringResource(titleId),
            style = textSize16SemiBold,
            color = DarkTextDefault
        )
    }
}