package com.example.todolistapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todolistapp.R
import com.example.todolistapp.databinding.FragmentSigninBinding
import com.example.todolistapp.databinding.FragmentSignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.ContextCompat



class SigninFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSigninBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        loginEvents()
        dontHave()
//        forgotPass()

        // For login button press effect
        binding.loginbtn.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    // Change to pressed state image when button is pressed
                    binding.loginbtn.setImageResource(R.drawable.loginbtnpressed)
                }
                android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                    // Change back to normal state image when button is released or touch is canceled
                    binding.loginbtn.setImageResource(R.drawable.loginbtn)
                }
            }
            false // Return false to allow the click event to propagate
        }
    }

    private fun forgotPass() {
        binding.forgotpass.setOnTouchListener { v, event ->
            if (v.id == binding.forgotpass.id) {
                when (event.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        binding.forgotpass.setTextColor(resources.getColor(R.color.blue2, null))
                    }
                    android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                        binding.forgotpass.setTextColor(resources.getColor(R.color.blue1, null))
                    }
                }
            }
            false
        }
    }

    private fun dontHave() {
        binding.donthaveaccount.setOnTouchListener { v, event ->
            if (v.id == binding.donthaveaccount.id) {
                when (event.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        binding.donthaveaccount.setTextColor(resources.getColor(R.color.blue2, null))
                    }
                    android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                        binding.donthaveaccount.setTextColor(resources.getColor(R.color.blue1, null))
                    }
                }
            }
            false
        }
    }





    private fun init (view:View){
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }
    private fun loginEvents() {

        // donthaveaccount to SignupFragment with animation
        binding.donthaveaccount.setOnClickListener {
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build()

            navControl.navigate(
                R.id.action_signinFragment_to_signupFragment,

                null,
                navOptions
            )
        }

        // login button to HomeFragment with animation
        binding.loginbtn.setOnClickListener {
            val username = binding.txtuser.text.toString().trim()
            val pass = binding.txtpass.text.toString().trim()

            if (username.isNotEmpty() && pass.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE

                auth.signInWithEmailAndPassword(username, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Logged In Successfully!", Toast.LENGTH_SHORT).show()

                        val navOptions = androidx.navigation.NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_in_right)
                            .setExitAnim(R.anim.slide_out_left)
                            .setPopEnterAnim(R.anim.slide_in_left)
                            .setPopExitAnim(R.anim.slide_out_right)
                            .build()

                        navControl.navigate(
                            R.id.action_signinFragment_to_homeFragment,
                            null,
                            navOptions
                        )
                    } else {
                        Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                    binding.progressBar.visibility = View.GONE
                }

            } else {
                Toast.makeText(context, "Error, Please Try Again!", Toast.LENGTH_SHORT).show()
            }
        }
    }



}