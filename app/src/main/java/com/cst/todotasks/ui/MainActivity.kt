package com.cst.todotasks.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.cst.todotasks.R
import com.cst.todotasks.extensions.replaceFragment
import com.cst.todotasks.ui.todos.TaskListFragment


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findNavController(R.id.navigation_fragment).addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.setDisplayHomeAsUpEnabled(R.id.task_list_fragment != destination.id)
            supportActionBar?.setDisplayHomeAsUpEnabled(R.id.task_list_fragment != destination.id)

            title = when (destination.id) {
                R.id.fragment_todo_form -> getText(R.string.new_task)
                R.id.fragment_todo_details -> getText(R.string.task_details)
                else -> getText(R.string.app_name)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}