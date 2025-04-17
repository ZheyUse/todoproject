package com.example.todolistapp.fragments

import android.os.Bundle
import android.renderscript.Sampler.Value
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistapp.R
import com.example.todolistapp.databinding.FragmentHomeBinding
import com.example.todolistapp.utils.ToDoAdapter
import com.example.todolistapp.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(), addTodoPopupFragment.DialogNextBtnClickLister,
    ToDoAdapter.ToDoAdapterClickInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dbref: DatabaseReference
    private var popupfragment: addTodoPopupFragment? = null

    private lateinit var adapter: ToDoAdapter
    private lateinit var mList:MutableList<ToDoData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFireBase()
        logoutEvent()
        addEvent()
    }


    private fun addEvent() {
        binding.addbtn.setOnClickListener {
            if (popupfragment != null)
                childFragmentManager.beginTransaction().remove(popupfragment!!).commit()
            popupfragment = addTodoPopupFragment()
            popupfragment!!.setListener(this)
            popupfragment!!.show(childFragmentManager, addTodoPopupFragment.TAG)
        }
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        dbref = FirebaseDatabase.getInstance().reference.child("Tasks").child(auth.currentUser?.uid.toString())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = ToDoAdapter(mList, requireContext())

        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }
    private fun getDataFromFireBase(){
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for(taskSnapshot in snapshot.children){
                    val todoTask = taskSnapshot.key?.let{
                        ToDoData(it, taskSnapshot.value.toString())
                    }
                    if(todoTask != null){
                        mList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun logoutEvent() {
        binding.logoutbtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(context, "Logged Out Successfully!", Toast.LENGTH_SHORT).show()
            navControl.navigate(R.id.action_homeFragment_to_splashFragment)
        }
    }




    override fun onSaveTask(todo: String, taskInput: TextInputEditText) {
        if (todo.isNotEmpty()) {
            dbref.push().setValue(todo).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "To do list added!", Toast.LENGTH_SHORT).show()
                    taskInput.text = null  // Clear the input after saving
                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
                // Dismiss the popup once the task is complete (success or failure)
                popupfragment!!.dismiss()
            }
        } else {
            Toast.makeText(requireContext(), "Please enter a task", Toast.LENGTH_SHORT).show()
            // Dismiss the popup if there's no input
            popupfragment!!.dismiss()
        }
    }

    override fun updateTask(toDoData: ToDoData, taskInput: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[toDoData.taskId] = toDoData.task
        dbref.updateChildren(map).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "List has been updated!", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            taskInput.text = null
            popupfragment!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClick(toDoData: ToDoData) {
        dbref.child(toDoData.taskId).removeValue().addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "List has been removed", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTaskBtnClick(toDoData: ToDoData) {
        if (popupfragment != null)
            childFragmentManager.beginTransaction().remove(popupfragment!!).commit()

        popupfragment = addTodoPopupFragment.newInstance(toDoData.taskId, toDoData.task)
        popupfragment!!.setListener(this)
        popupfragment!!.show(childFragmentManager, addTodoPopupFragment.TAG)
    }

}
