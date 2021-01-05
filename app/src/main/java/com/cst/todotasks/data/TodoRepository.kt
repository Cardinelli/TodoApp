package com.cst.todotasks.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TodoRepository {

    companion object {
        var database: TodoDatabase? = null

        fun getTodos(context: Context): List<Todo>? {
            database = TodoDatabase.getDatabase(context)

            return database!!.getTodoDao().getAll()
        }

        fun insertTodo(context: Context, todo: Todo) {
            database = TodoDatabase.getDatabase(context)

            CoroutineScope(IO).launch {
                database!!.getTodoDao().insertTodo(todo)
            }
        }

        fun updateTodo(context: Context, todo: Todo) {
            database = TodoDatabase.getDatabase(context)

            CoroutineScope(IO).launch {
                database!!.getTodoDao().updateTodo(todo)
            }
        }

        fun deleteTodo(context: Context, todo: Todo) {
            database = TodoDatabase.getDatabase(context)
            database!!.getTodoDao().deleteTodo(todo)
        }

        fun deleteCompletedTodos(context: Context) {
            database = TodoDatabase.getDatabase(context)
            database!!.getTodoDao().deleteCompleted()
        }
    }
}