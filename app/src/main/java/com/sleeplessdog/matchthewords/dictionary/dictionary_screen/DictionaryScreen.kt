package com.sleeplessdog.matchthewords.dictionary

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sleeplessdog.matchthewords.R
import com.sleeplessdog.matchthewords.backend.domain.models.GroupUiDictionary
import com.sleeplessdog.matchthewords.dictionary.dictionary_screen.DictionaryViewModel
import com.sleeplessdog.matchthewords.dictionary.group_screen.DictionaryWordGroups
import com.sleeplessdog.matchthewords.ui.theme.BlackPrimary
import com.sleeplessdog.matchthewords.ui.theme.DarkTextDefault
import com.sleeplessdog.matchthewords.ui.theme.DarkTextUsed
import com.sleeplessdog.matchthewords.ui.theme.Gray03
import com.sleeplessdog.matchthewords.ui.theme.Gray05
import com.sleeplessdog.matchthewords.ui.theme.Gray07
import com.sleeplessdog.matchthewords.ui.theme.textSize14SemiBold
import com.sleeplessdog.matchthewords.ui.theme.textSize16Bold
import com.sleeplessdog.matchthewords.ui.theme.textSize16SemiBold
import com.sleeplessdog.matchthewords.ui.theme.textSize20Medium
import com.sleeplessdog.matchthewords.ui.theme.textSize24Bold
import com.sleeplessdog.matchthewords.ui.theme.textSize24Medium

@Composable
fun DictionaryUi(
    viewModel: DictionaryViewModel,
    onNavigateToUserGroup: (String, String) -> Unit,
    onNavigateToGlobalGroup: (String, String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    DictionaryScreen(
        userGroups = state.userGroups,
        standardGroups = state.globalGroups,
        onNavigateToUserGroup = onNavigateToUserGroup,
        onNavigateToGlobalGroup = onNavigateToGlobalGroup,
        addNewUserGroup = viewModel::addNewUserGroup
    )
}

@Composable
fun DictionaryScreen(
    userGroups: List<GroupUiDictionary>,
    standardGroups: List<GroupUiDictionary>,
    onNavigateToUserGroup: (String, String) -> Unit,
    onNavigateToGlobalGroup: (String, String) -> Unit,
    addNewUserGroup: (String) -> Unit,
) {
    var groupState by remember { mutableStateOf(DictionaryWordGroups.MIXED) }
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(BlackPrimary)
            .verticalScroll(scrollState)
    ) {

        HeaderDictionary()

        Spacer(modifier = Modifier.height(8.dp))

        UserGroupsHeader(
            showAll = groupState == DictionaryWordGroups.MIXED || groupState == DictionaryWordGroups.USERS,
            onClick = {
                groupState = if (groupState == DictionaryWordGroups.USERS) {
                    DictionaryWordGroups.MIXED
                } else {
                    DictionaryWordGroups.USERS
                }
            },
            groupState = groupState
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (groupState == DictionaryWordGroups.MIXED || groupState == DictionaryWordGroups.USERS) {
            UserGroupsTable(
                userGroups,
                expanded = groupState == DictionaryWordGroups.USERS,
                onNavigateToUserGroup = onNavigateToUserGroup,
                onShowDialog = { showDialog = true },
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        if (showDialog) {
            NewCategoryDialog(onDismiss = { showDialog = false }, onSave = {
                addNewUserGroup(it)
                showDialog = false
            })
        }

        StandardGroupsHeader(
            showAll = groupState == DictionaryWordGroups.MIXED || groupState == DictionaryWordGroups.GLOBAL,
            onClick = {
                groupState = if (groupState == DictionaryWordGroups.GLOBAL) {
                    DictionaryWordGroups.MIXED
                } else {
                    DictionaryWordGroups.GLOBAL
                }
            },
            groupState = groupState
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (groupState == DictionaryWordGroups.MIXED || groupState == DictionaryWordGroups.GLOBAL) {
            Spacer(modifier = Modifier.height(10.dp))

            StandardGroupsTable(
                standardGroups,
                expanded = groupState == DictionaryWordGroups.GLOBAL,
                onNavigateToGlobalGroup = onNavigateToGlobalGroup
            )

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun HeaderDictionary() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.pimiss),
                contentDescription = "Левая иконка",
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.dictionary),
                style = textSize24Medium,
                color = DarkTextDefault,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.icon_park_outline_search),
                contentDescription = "Правая иконка",
                tint = DarkTextDefault,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable {
                        Toast.makeText(context, "Лупа нажата", Toast.LENGTH_SHORT).show()
                    })
        }
    }
}

@Composable
fun UserGroupsHeader(
    onClick: () -> Unit,
    showAll: Boolean,
    groupState: DictionaryWordGroups,
) {
    val textColor = if (groupState == DictionaryWordGroups.USERS) DarkTextDefault else Gray03
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.user_groups),
            style = textSize20Medium,
            color = DarkTextDefault
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(
                    id = if (showAll) R.drawable.icon_visibility
                    else R.drawable.icon_hidden
                ),
                tint = if (showAll) DarkTextDefault else Gray03,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onClick() },
                contentDescription = "Иконка все"
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                style = textSize16SemiBold,
                color = textColor,
                text = "Все",
                modifier = Modifier.clickable { onClick() })
        }
    }
}

@Composable
fun UserGroupsTable(
    groups: List<GroupUiDictionary>,
    expanded: Boolean,
    onNavigateToUserGroup: (String, String) -> Unit,
    onShowDialog: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {

        Divider(color = BlackPrimary, thickness = 1.dp)
        val groupsToShow = if (expanded) groups else groups.take(2)
        groupsToShow.forEachIndexed { index, group ->
            UserGroupTableRow(
                title = group.title,
                iconKey = group.iconRes,
                wordsCount = group.wordsInGroup,
                rowIndex = index,
                onClick = { onNavigateToUserGroup(group.key, group.title) }
            )
            if (index != groups.lastIndex) {
                Divider(color = BlackPrimary, thickness = 1.dp)
            }
        }
        Divider(color = BlackPrimary, thickness = 1.dp)
        UserGroupTableRow(
            title = stringResource(R.string.create_group),
            rowIndex = -2,
            iconKey = R.drawable.icon_add,
            onClick = { onShowDialog(true) }

        )
    }
}

@Composable
fun UserGroupTableRow(
    rowIndex: Int,
    title: String,
    iconKey: Int,
    wordsCount: Int? = null,
    onClick: () -> Unit,
) {
    val context = LocalContext.current

    val clickableIconPainter = painterResource(id = R.drawable.icon_dots_three_outline_vertical)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .background(Gray05)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconKey),
            tint = DarkTextDefault,
            contentDescription = "Icon for row $rowIndex",
            modifier = Modifier
                .size(36.dp)
                .padding(start = 12.dp),

            )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title, style = textSize16Bold, color = DarkTextDefault
            )

            wordsCount?.let {
                Text(
                    text = pluralStringResource(
                        R.plurals.words_count, wordsCount, wordsCount
                    ),
                    style = textSize14SemiBold,
                    color = DarkTextDefault.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        if (rowIndex != -2) {
            Icon(
                painter = clickableIconPainter,
                tint = DarkTextDefault,
                contentDescription = "Clickable icon for row $rowIndex",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(24.dp)
                    .clickable {
                        Toast.makeText(context, "Три точки нажаты", Toast.LENGTH_SHORT).show()
                    })
        }
    }
}


@Composable
fun StandardGroupsHeader(
    showAll: Boolean,
    onClick: () -> Unit,
    groupState: DictionaryWordGroups,
) {
    val textColor = if (groupState == DictionaryWordGroups.GLOBAL) DarkTextDefault else Gray03
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 18.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.global_groups),
            style = textSize20Medium,
            color = DarkTextDefault
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(
                    id = if (showAll) R.drawable.icon_visibility else R.drawable.icon_hidden
                ),
                tint = if (showAll) DarkTextDefault else Gray03,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onClick() },
                contentDescription = if (showAll) "Иконка все" else "Иконка скрыто"
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Все",
                style = textSize16SemiBold,
                color = textColor,
                modifier = Modifier.clickable { onClick() })
        }
    }
}

@Composable
fun StandardGroupsTable(
    groups: List<GroupUiDictionary>,
    expanded: Boolean,
    onNavigateToGlobalGroup: (String, String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .offset(y = (-6).dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        val groupsToShow = if (expanded) groups else groups.take(3)
        groupsToShow.forEachIndexed { index, group ->

            StandardGroupTableRow(
                wordsCount = group.wordsInGroup,
                iconKey = group.iconRes,
                title = group.title,
                onClick = { onNavigateToGlobalGroup(group.key, group.title) },
            )

            if (index != groups.lastIndex) {
                Divider(color = BlackPrimary, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun StandardGroupTableRow(
    wordsCount: Int,
    iconKey: Int,
    title: String,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val iconPainter = painterResource(id = iconKey)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray05)
            .height(68.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = null,
            tint = DarkTextDefault,
            modifier = Modifier
                .size(36.dp)
                .padding(start = 12.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                style = textSize16Bold,
                color = DarkTextDefault,
                text = title,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pluralStringResource(
                    R.plurals.words_count, wordsCount, wordsCount
                ),
                style = textSize14SemiBold,
                color = DarkTextDefault.copy(alpha = 0.6f),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.icon_play_circle),
            tint = DarkTextDefault,
            contentDescription = "Кнопка действия",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    Toast.makeText(context, "Плей нажат", Toast.LENGTH_SHORT).show()
                })
        Spacer(modifier = Modifier.padding(end = 12.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCategoryDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        )
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Gray05)
                    .padding(16.dp), verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = stringResource(R.string.new_group),
                    style = textSize24Bold,
                    color = DarkTextDefault,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_group_name),
                            style = textSize20Medium,
                            color = Gray07,
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = textSize16SemiBold.copy(color = DarkTextDefault),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = DarkTextDefault
                    )
                )
                Divider(
                    thickness = 1.dp, color = DarkTextDefault
                )
                Spacer(modifier = Modifier.height(36.dp))
                ButtonsCancelAndSave(onDismiss = onDismiss, onSave = {
                    if (text.isNotBlank()) {
                        onSave(text)
                        onDismiss()
                    }
                })
            }
        }
    }
}

@Composable
fun ButtonsCancelAndSave(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Box(modifier = Modifier.padding(start = 10.dp)) {
            TextButton(
                modifier = Modifier
                    .padding(start = 0.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkTextUsed)
                    .height(41.dp),
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(R.string.button_cancel), color = DarkTextDefault
                )
            }
        }
        Box(modifier = Modifier.padding(end = 10.dp)) {
            TextButton(
                modifier = Modifier
                    .padding(end = 0.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkTextUsed)
                    .height(41.dp),
                onClick = onSave,
            ) {
                Text(
                    text = stringResource(R.string.button_save), color = DarkTextDefault
                )
            }
        }
    }
}