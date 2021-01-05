package com.cst.todotasks.ui.todos

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.cst.todotasks.R
import com.cst.todotasks.data.Todo
import com.cst.todotasks.data.TodoRepository
import com.cst.todotasks.ui.viewModel.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * Created by nikolozakhvlediani on 12/24/20.
 */
class TaskListFragment : Fragment(),
    TodoRecyclerViewAdapter.TodoCheckedListener,
    TodoRecyclerViewAdapter.TodoClickListener {

    companion object {
        private const val SHOW_ALL = 0
        private const val SHOW_ACTIVE = 1
        private const val SHOW_COMPLETED = 2
    }

    private lateinit var fragmentView: View
    private lateinit var recycleViewTodos: RecyclerView
    private lateinit var allTodos: TextView
    private lateinit var emptyImg: ImageView
    private lateinit var emptyText: TextView

    private var todosFiltered: Boolean = false
    private var todos: List<Todo> = emptyList()

    private val todoViewModel: TodoViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        fragmentView = inflater.inflate(R.layout.fragment_task_list, container, false)

        recycleViewTodos = fragmentView.findViewById(R.id.recycle_view_todos)
        allTodos = fragmentView.findViewById(R.id.all_todos)
        emptyImg = fragmentView.findViewById(R.id.empty_img)
        emptyText = fragmentView.findViewById(R.id.empty_text)

        todoViewModel.todosLiveData.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                emptyTodos()
            } else {
                emptyTodos(false)

                if (!todosFiltered) {
                    todos = it
                }

                todosFiltered = false
                recycleViewTodos.adapter = TodoRecyclerViewAdapter(
                    it,
                    this@TaskListFragment,
                    this@TaskListFragment
                )
            }
        })

        context?.let { todoViewModel.getTodos(it) }

        fragmentView.findViewById<FloatingActionButton>(R.id.add_task).setOnClickListener {
            todoViewModel.postTodo(null)
            it.findNavController().navigate(R.id.action_fragment_todo_list_to_todo_form)
        }
        return fragmentView
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_clear -> {
                context?.let {
                    TodoRepository.deleteCompletedTodos(it)
                    todoViewModel.getTodos(it)
                    Snackbar.make(
                        fragmentView,
                        R.string.menu_clear,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                true
            }
            R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }

    private fun showFilteringPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.active -> {
                        filterTodos(SHOW_ACTIVE)
                    }
                    R.id.completed -> {
                        filterTodos(SHOW_COMPLETED)
                    }
                    else -> {
                        filterTodos(SHOW_ALL)
                    }
                }
                true
            }
            show()
        }
    }

    override fun onTodoCheckedListener(todo: Todo, completed: Boolean) {
        todo.completed = completed
        context?.let { TodoRepository.updateTodo(it, todo) }
    }

    override fun onTodoClickListener(todo: Todo) {
        Log.d("5555", "unda shemovides")
        todoViewModel.postTodo(todo)
        fragmentView.findNavController()
            .navigate(R.id.action_fragment_todo_list_to_todo_detailed_view)
    }


    private fun filterTodos(criteria: Int) {
        val filteredTodos = when (criteria) {
            SHOW_ACTIVE -> todos.filter { !it.completed }
            SHOW_COMPLETED -> todos.filter { it.completed }
            else -> todos
        }

        todosFiltered = true
        todoViewModel.postTodos(filteredTodos)
    }

    private fun emptyTodos(empty: Boolean = true) {
        emptyImg.visibility = if (empty) View.VISIBLE else View.GONE
        emptyText.visibility = if (empty) View.VISIBLE else View.GONE
        allTodos.visibility = if (empty) View.GONE else View.VISIBLE
        recycleViewTodos.visibility = if (empty) View.GONE else View.VISIBLE
    }

}