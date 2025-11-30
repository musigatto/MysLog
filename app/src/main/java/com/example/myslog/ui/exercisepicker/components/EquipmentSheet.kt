package com.example.myslog.ui.exercisepicker.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myslog.R

import com.example.myslog.ui.exercisepicker.ExerciseEvent
import com.example.myslog.utils.Event

@Composable
fun EquipmentSheet(
  selectedEquipment: List<String>,
  allEquipment: List<String>,
  onEvent: (Event) -> Unit
) {
  Sheet(
    items = allEquipment.sorted(),
    selectedItems = selectedEquipment,
    title = stringResource(R.string.filter_by_equipment),
    onSelect = { onEvent(ExerciseEvent.SelectEquipment(it)) }
  ) {
    onEvent(ExerciseEvent.DeselectEquipment)
  }
}