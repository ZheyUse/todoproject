package com.example.todolistapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todolistapp.utils.ToDoData

import com.example.todolistapp.R
import com.example.todolistapp.databinding.FragmentAddTodoPopupBinding
import com.example.todolistapp.databinding.FragmentSigninBinding
import com.google.android.material.textfield.TextInputEditText


class addTodoPopupFragment : DialogFragment() {

    private lateinit var binding: FragmentAddTodoPopupBinding
    private var listener: DialogNextBtnClickLister? = null
    private var toDoData: ToDoData? = null

    fun setListener(listerner: DialogNextBtnClickLister){
        this.listener = listerner
    }

    companion object {
        const val TAG = "DialogFragment"
        @JvmStatic
        fun newInstance(taskId: String, task: String) =
            addTodoPopupFragment().apply {
                arguments = Bundle().apply {
                    putString("taskId", taskId)
                    putString("task", task)
                }
            }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTodoPopupBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if arguments are passed (i.e., we're editing an existing task)
        if (arguments != null) {
            toDoData = ToDoData(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString()
            )
            binding.taskInput.setText(toDoData?.task)
            binding.createButton.text = "Update" // 👈 Change button text to "Update"
            binding.taskInputLayout.hint = "Update Task"
        } else {
            binding.createButton.text = "Create" // 👈 Default to "Create"
            binding.taskInputLayout.hint = "Type task here"
        }

        addEvent()
    }

    private fun addEvent(){
        binding.createButton.setOnClickListener{
            val todoTask = binding.taskInput.text.toString()
            if (todoTask.isNotEmpty()){
                if (toDoData == null){
                    listener?.onSaveTask(todoTask, binding.taskInput)
                }
                else{
                    toDoData!!.task = todoTask
                    listener?.updateTask(toDoData!!, binding.taskInput)
                }

            }
            else{
                Toast.makeText(context, "Invalid Input" , Toast.LENGTH_SHORT).show()
            }
        }
        binding.closeButton.setOnClickListener{
            dismiss()
        }
    }

    //This code make the popup fully transparent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar)
    }

    override fun onResume() {
        super.onResume()
        // Set the dialog size to match parent width and wrap content height
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt() // 90% of screen width
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    interface DialogNextBtnClickLister {
        fun onSaveTask(todo: String, taskInput: TextInputEditText)
        fun updateTask(toDoData: ToDoData , todoEdit:TextInputEditText)
    }

}