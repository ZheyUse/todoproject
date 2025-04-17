package com.example.todolistapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todolistapp.R
import com.example.todolistapp.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentSigninBinding

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            // Create a NavOptions object for popup transition animations
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setEnterAnim(R.anim.popup_enter)  // Pop-in effect when entering the destination
                .setExitAnim(R.anim.popup_exit)    // Pop-out effect when exiting the current screen
                .setPopEnterAnim(R.anim.popup_enter)  // Pop-in effect when popping back to previous fragment
                .setPopExitAnim(R.anim.popup_exit)    // Pop-out effect when popping the current fragment
                .build()

            // Check if the user is logged in
            if (auth.currentUser != null) {
                // If logged in, navigate to HomeFragment with the popup transition
                navController.navigate(
                    R.id.action_splashFragment_to_homeFragment,
                    null,
                    navOptions
                )
            } else {
                // If not logged in, navigate to SigninFragment with the popup transition
                navController.navigate(
                    R.id.action_splashFragment_to_signinFragment,
                    null,
                    navOptions
                )
            }
        }, 2000) // Delay of 2 seconds before navigating

    }

}