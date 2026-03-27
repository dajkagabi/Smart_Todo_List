package com.example.smart_todo_list.presentation.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_todo_list.data.TodoDatabase
import com.example.smart_todo_list.data.TodoItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * A ViewModel felel az üzleti logikáért és az adatok UI felé közvetítéséért.
 */
class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = TodoDatabase.getDatabase(application).todoDao()

    // Az összes teendő lekérése Flow-ként, ami automatikusan frissül
    val todoItems: StateFlow<List<TodoItem>> = dao.getAllItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Új teendő hozzáadása kategóriával.
     */
    fun addTodo(title: String, category: String = "Általános") {
        viewModelScope.launch {
            dao.insertItem(TodoItem(title = title, category = category))
        }
    }

    /**
     * Teendő frissítése (pl. pipa vagy név módosítása).
     */
    fun toggleTodo(item: TodoItem) {
        viewModelScope.launch {
            dao.updateItem(item)
        }
    }

    /**
     * Teendő törlése.
     */
    fun deleteTodo(item: TodoItem) {
        viewModelScope.launch {
            dao.deleteItem(item)
        }
    }
}
