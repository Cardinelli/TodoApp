package com.cst.todotasks.data

import androidx.room.*

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo")
    fun getAll(): List<Todo>

    @Insert
    fun insertTodo(todo: Todo)

    @Update
    fun updateTodo(todo: Todo)

    @Delete
    fun deleteTodo(todo: Todo)

    @Query("DELETE FROM todo WHERE completed = 1")
    fun deleteCompleted()
}