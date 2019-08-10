package com.example.adventuremessenger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import android.util.Log
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class LoginActivity:  AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener{

       performLogin()

        }
        login_button_login.setOnClickListener {
            performLogin()
        }

        backToReg_editText.setOnClickListener{
            finish()
        }

    }

    private fun performLogin(){

        val email = EmailAddress_logn_editText.text.toString()
        val password = password_edittext_login.text.toString()

        Log.d("LoginActivity", "Attempt login with email/pw: $email/***")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(){

            if (!it.isSuccessful) return@addOnCompleteListener

            Log.d("Login", "Successfully logged in: ${it.result?.user!!.uid}")

            val intent = Intent(this, LatestMessagesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
            .addOnFailureListener{

                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()

            }

    }

}
