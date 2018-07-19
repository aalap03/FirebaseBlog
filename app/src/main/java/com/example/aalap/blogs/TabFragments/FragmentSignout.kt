package com.example.aalap.blogs.TabFragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.aalap.blogs.LoginRegister
import com.example.aalap.blogs.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_out.*

val TAG = "FragmentSignout:"

class FragmentSignout : Fragment() {

    lateinit var firebaseAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth ->

            Toast.makeText(context, "Signing out.....", Toast.LENGTH_SHORT).show()
            if (firebaseAuth.currentUser == null) {
                Toast.makeText(context, "Signed Out", Toast.LENGTH_SHORT).show()
                startActivity(Intent(context, LoginRegister::class.java))
                activity?.finish()
            } else {
                Toast.makeText(context, "Could not sign out", Toast.LENGTH_SHORT).show()
            }
        }

        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuthStateListener }

        return inflater.inflate(R.layout.fragment_sign_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sign_out.setOnClickListener {
            progress_bar.visibility = View.VISIBLE

            FirebaseAuth.getInstance().signOut()
        }
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener { firebaseAuthStateListener }
    }
}