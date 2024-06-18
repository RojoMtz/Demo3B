package dev.red.demo3b.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import dev.red.demo3b.MainActivity
import dev.red.demo3b.R
import dev.red.demo3b.databinding.FragmentEditTaskBinding
import dev.red.demo3b.model.Task
import dev.red.demo3b.viewmodel.TaskViewModel


class EditTaskFragment : Fragment(R.layout.fragment_edit_task), MenuProvider {

    private var editTaskBinding : FragmentEditTaskBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var currentTask: Task

    private val args : EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editTaskBinding = FragmentEditTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!


        binding.editTaskTitle.setText(currentTask.taskTitle)
        binding.editTaskDesc.setText(currentTask.taskDescription)
        if (currentTask.isFavorite) binding.checkFinish.isChecked = true

        binding.editTaskFab.setOnClickListener {
            val taskTitle = binding.editTaskTitle.text.toString().trim()
            val taskDesc = binding.editTaskDesc.text.toString().trim()
            val taskFinish = binding.checkFinish.isChecked

            if (taskTitle.isNotEmpty()) {
                val task = Task(currentTask.id, taskTitle, taskDesc, taskFinish)
                taskViewModel.updateTask(task)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Favor de agrear un titulo.", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun deleteTask() {
        AlertDialog.Builder(requireActivity()).apply {
            setTitle("Borrando Tarea")
            setMessage("Deseas borrar esta tarea?")
            setPositiveButton("Borrar") {_,_ ->
                taskViewModel.deleteTask(currentTask)
                Toast.makeText(context, "Tarea Eliminada.", Toast.LENGTH_LONG).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancelar", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_task, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
       return when(menuItem.itemId) {
           R.id.deleteMenu -> {
               deleteTask()
               true
           } else -> false
       }
    }

    override fun onDestroy() {
        super.onDestroy()
        editTaskBinding = null
    }

}