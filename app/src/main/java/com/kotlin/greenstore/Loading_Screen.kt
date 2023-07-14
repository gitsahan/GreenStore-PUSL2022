package com.kotlin.greenstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Loading_Screen : AppCompatActivity()
{
    private val LOADING_TIME : Long = 3000

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)

        Handler().postDelayed( {
         startActivity (Intent (this, Login_Activity::class.java))
         finish()
        }, LOADING_TIME)
    }
}