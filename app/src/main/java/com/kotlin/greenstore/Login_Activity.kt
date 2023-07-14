package com.kotlin.greenstore

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.kotlin.greenstore.databinding.ActivityLoginBinding

class Login_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var actionBar: ActionBar
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "GreenStore Login"

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setMessage("Logging...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, SignUp_Activity::class.java))
        }

        binding.loginBtn.setOnClickListener{
            validateData()
        }
    }

    private fun validateData()
    {
        email=binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.emailEt.error = "Invalid email format."
        }
        else if (TextUtils.isEmpty(password))
        {
            binding.passwordEt.error="Please enter your password."
        }
        else
        {
            firebaseLogin()
        }
    }

    private fun firebaseLogin()
    {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener{
                progressDialog.dismiss()
                val firebaseUser=firebaseAuth.currentUser
                val email=firebaseUser!!.email
                Toast.makeText(this,"Logged in as $email", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, Products_Activity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"Logn failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser()
    {
        val firebaseUser=firebaseAuth.currentUser
        if (firebaseUser != null)
        {
            startActivity(Intent(this, Profile_Activity::class.java))
            finish()
        }
    }
}