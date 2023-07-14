package com.kotlin.greenstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.kotlin.greenstore.databinding.ActivityProfileBinding
import kotlinx.android.synthetic.main.activity_cart.*

class Profile_Activity : AppCompatActivity()
{
    private lateinit var binding: ActivityProfileBinding
    private lateinit var actionBar: ActionBar
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar=supportActionBar!!
        actionBar.title="Profile Manager"

        firebaseAuth=FirebaseAuth.getInstance()
        checkUser()

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()


        }

    }

    private fun checkUser()
    {
        val firebaseUser=firebaseAuth.currentUser
        if (firebaseUser != null)
        {
            val email = firebaseUser.email
            binding.emailTv.text = email
        }
        else
        {
            startActivity(Intent(this, Login_Activity::class.java))
            finish()
        }
    }
}