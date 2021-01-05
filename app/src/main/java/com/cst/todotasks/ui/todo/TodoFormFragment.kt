package com.cst.todotasks.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.cst.todotasks.R
import com.cst.todotasks.data.Todo
import com.cst.todotasks.data.TodoRepository
import com.cst.todotasks.ui.viewModel.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class TodoFormFragment : Fragment() {

    private lateinit var todoTitle: EditText
    private lateinit var todoDescription: EditText

    private var todo: Todo? = null

    private val todoViewModel: TodoViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.todo_form, container, false)

        todoTitle = view.findViewById(R.id.todo_form_title)
        todoDescription = view.findViewById(R.id.todo_form_description)

        todoViewModel.todoLiveData.observe(viewLifecycleOwner, {
            with(it) {
                todo = this
                if (todo == null) {
                    todoTitle.text = null
                    todoDescription.text = null
                } else {
                    (activity as AppCompatActivity).title = "Edit Task"
                    todoTitle.setText(title)
                    todoDescription.setText(description)
                }
            }
        })

        view.findViewById<FloatingActionButton>(R.id.save_todo).setOnClickListener {
            if (todoTitle.text.isEmpty() || todoTitle.text.isBlank()) {
                todoTitle.error = "Title is Required"

                return@setOnClickListener
            }
            val notification: Int
            if (todo == null) {
                todo = Todo(todoTitle.text.toString(), todoDescription.text.toString())
                context?.let { c -> TodoRepository.insertTodo(c, todo!!) }
                notification = R.string.todo_added
            } else {
                todo!!.title = todoTitle.text.toString()
                todo!!.description = todoDescription.text.toString()
                context?.let { c -> TodoRepository.updateTodo(c, todo!!) }
                notification = R.string.todo_edited
            }

            findNavController().navigate(R.id.action_todo_form_to_fragment_todo_list)
            Snackbar.make(it, getText(notification), Snackbar.LENGTH_SHORT).show()
        }

        return view
    }
}