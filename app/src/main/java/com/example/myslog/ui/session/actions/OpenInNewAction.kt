package com.example.myslog.ui.session.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myslog.R

@Composable
fun OpenInNewAction(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.ZoomIn,
            contentDescription = stringResource(R.string.open_exercise_guide)
        )
    }
}