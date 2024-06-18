package dev.red.demo3b.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.red.demo3b.MainActivity
import dev.red.demo3b.R
import dev.red.demo3b.databinding.FragmentAddTaskBinding
import dev.red.demo3b.model.Task
import dev.red.demo3b.viewmodel.TaskViewModel

class AddTaskFragment : Fragment(R.layout.fragment_add_task), MenuProvider {

    private var addTaskBinding : FragmentAddTaskBinding? = null
    private val binding get() = addTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var addTaskView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addTaskBinding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        addTaskView = view
    }

    private fun saveTAsk(view: View) {
        val taskTitle = binding.addTaskTitle.text.toString().trim()
        val taskDesc  = binding.addTaskDesc.text.toString().trim()

        if (taskTitle.isNotEmpty()) {
            val task = Task(0, taskTitle, taskDesc)
            taskViewModel.addTask(task)

            Snackbar.make(addTaskView, "Tarea Guardada.", Snackbar.LENGTH_LONG)
                .show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Snackbar.make(addTaskView, "Favor de agregar el titulo de la Tarea.", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

        menuInflater.inflate(R.menu.menu_add_task, menu)

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId) {
            R.id.saveMenu -> {
                saveTAsk(addTaskView)
                true
            } else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addTaskBinding = null
    }


}