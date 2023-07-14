package com.kotlin.greenstore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.kotlin.greenstore.R
import com.kotlin.greenstore.adapter.MyCartAdapter
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.layout_drink_item.*

class SummaryActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<Model>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        userRecyclerview = findViewById(R.id.recycler_cart)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<Model>()
        getUserData()

    }

    private fun getUserData() {

        dbref = FirebaseDatabase.getInstance().getReference("Unique_User_ID")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children)
                    {
                        val Model = userSnapshot.getValue(Model::class.java)
                        userArrayList.add(Model!!)

                    }

                    recycler_cart.adapter = MySumrAdapter(Model)


                }

            }

            override fun onCancelled(error: DatabaseError)
            {
                TODO("Not yet implemented")
            }


        })



    }
}