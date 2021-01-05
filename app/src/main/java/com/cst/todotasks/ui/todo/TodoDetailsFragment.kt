package com.cst.todotasks.ui.todo

import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.cst.todotasks.R
import com.cst.todotasks.data.Todo
import com.cst.todotasks.data.TodoRepository
import com.cst.todotasks.ui.viewModel.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class TodoDetailsFragment : Fragment() {
    private lateinit var todoViewTitle: CheckBox
    private lateinit var todoViewText: TextView

    private var todo: Todo? = null
    private val todoViewModel: TodoViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.todo_detailed_view, container, false)

        todoViewTitle = view.findViewById(R.id.todo_detailed_checkbox)
        todoViewText = view.findViewById(R.id.todo_detailed_description)

        todoViewModel.todoLiveData.observe(viewLifecycleOwner, {
            with(it) {
                todo = this
                todoViewTitle.text = title
                todoViewTitle.isChecked = completed
                todoViewText.text = description

                todoViewTitle.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
                    todo?.let { td ->
                        run {
                            td.completed = isChecked
                            context?.let { d -> TodoRepository.updateTodo(d, td) }
                            Snackbar.make(
                                view,
                                getText(if (isChecked) R.string.set_completed else R.string.mark_as_active),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
        })

        view.findViewById<FloatingActionButton>(R.id.edit_todo).setOnClickListener{
            todo?.let { todo -> todoViewModel.postTodo(todo)}
            it.findNavController().navigate(R.id.action_todo_detailed_view_to_todo_form)
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}