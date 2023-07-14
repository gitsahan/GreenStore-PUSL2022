package com.kotlin.greenstore

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.kotlin.greenstore.databinding.ActivitySignupBinding

class SignUp_Activity : AppCompatActivity()
{
    private lateinit var binding: ActivitySignupBinding
    private lateinit var actionBar: ActionBar
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private var email=""
    private var password=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar=supportActionBar!!
        actionBar.title="Create Account."
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setMessage("Creating an account...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth= FirebaseAuth.getInstance()
        binding.SignUpBtn.setOnClickListener {
            validateData()
        }
    }

    private fun validateData()
    {
     email=binding.emailEt.text.toString().trim()
     password=binding.passwordEt.text.toString().trim()

     if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
     {
         binding.emailEt.error="Invalid email format."
     }
     else if (TextUtils.isEmpty(password))
     {
         binding.passwordEt.error="Please enter password."
     }
     else if (password.length<6)
     {
         binding.passwordEt.error="Password should be more than 6 characters long."
     }
     else
     {
         firebaseSignUp()
     }
    }

    private fun firebaseSignUp()
    {
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser=firebaseAuth.currentUser
                val email=firebaseUser!!.email
                Toast.makeText(this, "Account created with $email", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this,Profile_Activity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"SignUp failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}