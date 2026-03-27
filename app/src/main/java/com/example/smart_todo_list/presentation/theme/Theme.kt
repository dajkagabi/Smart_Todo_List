package com.example.smart_todo_list.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun Smart_Todo_ListTheme(
    content: @Composable () -> Unit
) {
    /**
     * Material 2 Theme for Wear OS.
     */
    MaterialTheme(
        content = content
    )
}
