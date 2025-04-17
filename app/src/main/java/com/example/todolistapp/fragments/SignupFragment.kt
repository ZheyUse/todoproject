package com.example.todolistapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import com.example.todolistapp.R
import com.example.todolistapp.databinding.FragmentSignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.ContextCompat




class SignupFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var navControl:NavController
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
        binding.haveanaccount.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    binding.haveanaccount.setTextColor(resources.getColor(R.color.blue2, null))
                }
                android.view.MotionEvent.ACTION_UP,
                android.view.MotionEvent.ACTION_CANCEL -> {
                    binding.haveanaccount.setTextColor(resources.getColor(R.color.blue1, null))
                }
            }
            false
        }

        binding.signupbtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    // Change to pressed state image when button is pressed
                    binding.signupbtn.setImageResource(R.drawable.signupbtnpressed)
                }
                android.view.MotionEvent.ACTION_UP,
                android.view.MotionEvent.ACTION_CANCEL -> {
                    // Change back to normal state image when button is released or touch is canceled
                    binding.signupbtn.setImageResource(R.drawable.signupbtn)
                }
            }
            false // Return true to indicate that the touch event was handled
        }

    }

    private fun init (view:View){
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }
    private fun registerEvents() {
        // Navigate to SigninFragment from SignupFragment with slide-in-left animation
        binding.haveanaccount.setOnClickListener {
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_left)   // Slide in from left
                .setExitAnim(R.anim.slide_out_right)  // Slide out to the right
                .setPopEnterAnim(R.anim.slide_in_right) // Pop enter will slide in from right
                .setPopExitAnim(R.anim.slide_out_left)  // Pop exit will slide out to the left
                .build()

            navControl.navigate(
                R.id.action_signupFragment_to_signinFragment2,
                null,
                navOptions
            )
        }

        // Signup button logic to create a new user
        binding.signupbtn.setOnClickListener {
            val username = binding.txtuser1.text.toString().trim()
            val pass = binding.txtpass1.text.toString().trim()
            val conpass = binding.txtconpass.text.toString().trim()

            if (username.isNotEmpty() && pass.isNotEmpty() && conpass.isNotEmpty()) {

                if (pass == conpass) {
                    binding.progressBar1.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(username, pass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "You have now Registered!", Toast.LENGTH_SHORT).show()

                            // Navigate to SigninFragment with animation
                            val navOptions = androidx.navigation.NavOptions.Builder()
                                .setEnterAnim(R.anim.slide_in_left)   // Slide in from left
                                .setExitAnim(R.anim.slide_out_right)  // Slide out to the right
                                .setPopEnterAnim(R.anim.slide_in_right) // Pop enter will slide in from right
                                .setPopExitAnim(R.anim.slide_out_left)  // Pop exit will slide out to the left
                                .build()

                            navControl.navigate(
                                R.id.action_signupFragment_to_signinFragment2,
                                null,
                                navOptions
                            )
                        } else {
                            Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                        binding.progressBar1.visibility = View.GONE
                    }
                } else {
                    Toast.makeText(context, "Password do not match, Please try again!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Error, Please Try Again!", Toast.LENGTH_SHORT).show()
            }
        }
    }


}