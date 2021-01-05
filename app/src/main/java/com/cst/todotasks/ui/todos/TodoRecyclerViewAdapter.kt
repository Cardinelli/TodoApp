package com.cst.todotasks.ui.todos

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cst.todotasks.R
import com.cst.todotasks.data.Todo

class TodoRecyclerViewAdapter(
    private val todoList: List<Todo>,
    private val todoCheckedListener: TodoCheckedListener,
    private val todoClickListener: TodoClickListener,
) : RecyclerView.Adapter<TodoRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todoList[position]
        holder.todoTitle.text = todo.title

        if (todo.completed) {
            holder.todoCheckbox.isChecked = true
            holder.todoTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        holder.todoTitle.setOnClickListener {
            todoClickListener.onTodoClickListener(todo)
        }

        holder.todoCheckbox.setOnCheckedChangeListener { _: CompoundButton, checked: Boolean ->
            if (checked) {
                holder.todoCheckbox.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.todoCheckbox.paintFlags = Paint.ANTI_ALIAS_FLAG
            }
            todoCheckedListener.onTodoCheckedListener(todo, checked)
        }
    }

    override fun getItemCount(): Int = todoList.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoCheckbox: CheckBox = view.findViewById(R.id.todo_item_checkbox)
        val todoTitle: TextView = view.findViewById(R.id.todo_item_title)
    }

    interface TodoClickListener {
        fun onTodoClickListener(todo: Todo)
    }

    interface TodoCheckedListener {
        fun onTodoCheckedListener(todo: Todo, completed: Boolean)
    }

}

