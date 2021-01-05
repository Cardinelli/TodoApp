package com.cst.todotasks.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cst.todotasks.data.Todo
import com.cst.todotasks.data.TodoRepository

class TodoViewModel : ViewModel() {

    private val _todosLiveData = MutableLiveData<List<Todo>>()
    val todosLiveData: LiveData<List<Todo>>
        get() = _todosLiveData

    private val _todoLiveData = MutableLiveData<Todo>()
    val todoLiveData: LiveData<Todo>
        get() = _todoLiveData

    fun postTodo(todo: Todo?) {
        _todoLiveData.postValue(todo)
    }

    fun postTodos(todos: List<Todo>?) {
        _todosLiveData.postValue(todos)
    }

    fun getTodos(context: Context) {
        postTodos(TodoRepository.getTodos(context))
    }

}