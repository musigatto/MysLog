package com.example.myslog.ui.session.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myslog.R

@Composable
fun DeletionAlertDialog(
  onDismiss: () -> Unit,
  onDelete: () -> Unit,
  title: @Composable () -> Unit,
  text: @Composable () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(onClick = onDelete) {
        Text(text = stringResource(R.string.delete))
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
          Text(text = stringResource(R.string.cancel))
      }
    },
    title = title,
    text = text
  )
}