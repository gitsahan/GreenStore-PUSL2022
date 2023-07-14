package com.kotlin.greenstore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kotlin.greenstore.adapter.MyDrinkAdapter
import com.kotlin.greenstore.databinding.ActivityProfileBinding
import com.kotlin.greenstore.eventbus.UpdateCartEvent
import com.kotlin.greenstore.listener.ICartListener
import com.kotlin.greenstore.listener.IDrinkListener
import com.kotlin.greenstore.model.CartModel
import com.kotlin.greenstore.model.DrinkModel
import com.kotlin.greenstore.utils.SpaceItemDecoration
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.layout_drink_item.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class Products_Activity : AppCompatActivity(), IDrinkListener, ICartListener {

    lateinit var drinkLoadListener:IDrinkListener
    lateinit var cartLoadListener: ICartListener

    override fun onStart()
    {
        super.onStart()
        EventBus.getDefault().register(this,)
    }

    override fun onStop()
    {
        super.onStop()
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun onUpdateCartEvent(event: UpdateCartEvent)
    {
        countCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        init()
        loadDrinkFromFirebase()
        countCartFromFirebase()
    }

    private fun countCartFromFirebase() {
        val cartModels : MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("Unique_User_ID")
            .addListenerForSingleValueEvent(object :ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    for(cartSnapshot in snapshot.children)
                    {
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onCartSuccess(cartModels)
                }
                override fun onCancelled(error: DatabaseError)
                {
                    cartLoadListener.onCartFailed(error.message)
                }
            })
    }

    private fun loadDrinkFromFirebase()
    {
        val drinkModels:MutableList<DrinkModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Drink")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        for (drinkSnapshot in snapshot.children)
                        {
                            val drinkModel = drinkSnapshot.getValue(DrinkModel::class.java)
                            drinkModel!!.key = drinkSnapshot.key
                            drinkModels.add(drinkModel)
                        }
                        drinkLoadListener.onDrinkLoadSuccess(drinkModels)
                    }
                    else
                    {
                        drinkLoadListener.onDrinkLoadFailed("The grocery item not available.")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    drinkLoadListener.onDrinkLoadFailed(error.message)
                }

            })
    }
    private fun init()
    {
        drinkLoadListener = this
        cartLoadListener = this

        val gridLayoutManager = GridLayoutManager(this, 2)
        recycler_drink.layoutManager = gridLayoutManager
        recycler_drink.addItemDecoration(SpaceItemDecoration())

        btnCart.setOnClickListener {startActivity(Intent(this, CartActivity::class.java))}
    }

    override fun onDrinkLoadSuccess(drinkModelList: List <DrinkModel>?)
    {
        val adapter = MyDrinkAdapter (this, drinkModelList!!, cartLoadListener)
        recycler_drink.adapter = adapter
    }

    override fun onDrinkLoadFailed(message: String?) {
        Snackbar.make(imageView, message!!,Snackbar.LENGTH_LONG).show()
    }

    override fun onCartSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
            for (cartModel in cartModelList!!) cartSum+=cartModel!!.quantity
            badge!!.setNumber(cartSum)
    }

    override fun onCartFailed(message: String?) {
        Snackbar.make(imageView, message!!,Snackbar.LENGTH_LONG).show()
    }

}