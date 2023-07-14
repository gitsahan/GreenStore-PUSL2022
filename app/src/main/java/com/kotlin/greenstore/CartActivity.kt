package com.kotlin.greenstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlin.greenstore.adapter.MyCartAdapter
import com.kotlin.greenstore.adapter.MyDrinkAdapter
import com.kotlin.greenstore.listener.ICartListener
import com.kotlin.greenstore.listener.IDrinkListener
import com.kotlin.greenstore.model.CartModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.layout_drink_item.*

class CartActivity : AppCompatActivity(), ICartListener  {

    var cartLoadListner:ICartListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        init()
        loadCartFromFireBase()

    }

    private fun loadCartFromFireBase()
    {
        val cartModels : MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("Unique_User_ID")
            .addListenerForSingleValueEvent(object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    for(cartSnapshot in snapshot.children)
                    {
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListner!!.onCartSuccess(cartModels)
                }
                override fun onCancelled(error: DatabaseError)
                {
                    cartLoadListner!!.onCartFailed(error.message)
                }
            })
    }

    private fun init()
    {
        cartLoadListner = this

        val layoutManager = LinearLayoutManager(this)
        recycler_cart!!.layoutManager = layoutManager
        recycler_cart!!.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        btnBack_01!!.setOnClickListener {finish()}
    }

    override fun onCartSuccess(cartModelList: List<CartModel>)
    {
        var sum = 0.0
        for (cartModel in cartModelList!!)
        {
            sum += cartModel!!.totalPrice
        }
        txtTotal.text = StringBuilder("Rs").append(sum)
        val adapter = MyCartAdapter(this, cartModelList)
        recycler_cart!!.adapter = adapter
    }

    override fun onCartFailed(message: String?) {
        Snackbar.make(imageView,message!!, Snackbar.LENGTH_LONG).show()
    }
}