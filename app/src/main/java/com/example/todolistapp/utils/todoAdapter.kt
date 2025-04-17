package com.example.todolistapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R
import com.example.todolistapp.databinding.TodoeachBinding
import com.example.todolistapp.utils.ToDoData

class ToDoAdapter(private val list: MutableList<ToDoData>, private val context: Context) : RecyclerView.Adapter<ToDoAdapter.TodoViewHolder>() {
    private var listener: ToDoAdapterClickInterface? = null

    fun setListener(listener: ToDoAdapterClickInterface) {
        this.listener = listener
    }

    inner class TodoViewHolder(val binding: TodoeachBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoeachBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.todoTask.text = this.task

                // Check if it's the first login using SharedPreferences
                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val isFirstLogin = sharedPreferences.getBoolean("isFirstLogin", true)

                if (isFirstLogin) {
                    // Set up the animation for each item
                    val animation = AnimationUtils.loadAnimation(context, R.anim.popup_enter)

                    // Start the animation with a delay for each item
                    holder.itemView.visibility = View.INVISIBLE
                    holder.itemView.postDelayed({
                        holder.itemView.startAnimation(animation)
                        holder.itemView.visibility = View.VISIBLE
                    }, position * 150L)  // Delay each item’s animation for a sequential effect
                }

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteTaskBtnClick(this)
                }

                binding.editTask.setOnClickListener {
                    listener?.onEditTaskBtnClick(this)
                }
            }
        }
    }

    interface ToDoAdapterClickInterface {
        fun onDeleteTaskBtnClick(toDoData: ToDoData)
        fun onEditTaskBtnClick(toDoData: ToDoData)
    }
}
