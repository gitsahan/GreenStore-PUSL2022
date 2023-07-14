package com.kotlin.greenstore.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.kotlin.greenstore.R
import com.kotlin.greenstore.eventbus.UpdateCartEvent
import com.kotlin.greenstore.model.CartModel
import org.greenrobot.eventbus.EventBus

class MyCartAdapter
    (private val context:Context,
     private val cartModelList:List<CartModel>
    ): RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder>()
{
    class MyCartViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var btnMinus:ImageView? = null
        var btnPlus:ImageView? = null
        var imageView:ImageView? = null
        var btnDelete:ImageView? = null
        var txtName:TextView? = null
        var txtPrice:TextView? = null
        var txtQuantity:TextView? = null

        init
        {
            btnMinus = itemView.findViewById(R.id.btnMinus) as ImageView
            btnPlus = itemView.findViewById(R.id.btnPlus) as ImageView
            imageView = itemView.findViewById(R.id.imageView) as ImageView
            btnDelete = itemView.findViewById(R.id.bntDelete) as ImageView
            txtName = itemView.findViewById(R.id.txtName) as TextView
            txtPrice = itemView.findViewById(R.id.txtPrice) as TextView
            txtQuantity = itemView.findViewById(R.id.txtQuantity) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        return MyCartViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.layout_cart_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int)
    {
        Glide.with(context)
            .load(cartModelList[position].image)
            .into(holder.imageView!!)
        holder.txtName!!.text=StringBuilder().append(cartModelList[position].name)
        holder.txtPrice!!.text=StringBuilder("Rs.").append(cartModelList[position].price)
        holder.txtQuantity!!.text=StringBuilder("").append(cartModelList[position].quantity)

        holder.btnMinus!!.setOnClickListener {_ -> minusCartItem(holder, cartModelList[position])}
        holder.btnPlus!!.setOnClickListener {_ -> plusCartItem(holder, cartModelList[position])}
        holder.btnDelete!!.setOnClickListener {_ ->
            val dialog = AlertDialog.Builder(context)
                .setTitle("Detele Item.")
                .setMessage("Do You really want to delete this item ?")
                .setNegativeButton("CANCEL") {dialog,_ -> dialog.dismiss()}
                .setPositiveButton("DELETE") {dialog,_ -> dialog.dismiss()

                        notifyItemRemoved(position)
                        FirebaseDatabase.getInstance()
                            .getReference("Cart")
                            .child("Unique_User_ID")
                            .child(cartModelList[position].key!!)
                            .removeValue()
                            .addOnFailureListener {EventBus.getDefault().postSticky(UpdateCartEvent())}
                }
                .create()
            dialog.show()
        }
    }

    private fun plusCartItem (holder: MyCartAdapter.MyCartViewHolder, cartModel: CartModel)
    {
        cartModel.quantity += 1
        cartModel.totalPrice = cartModel.quantity * cartModel.price!!.toFloat()
        holder.txtQuantity!!.text = StringBuilder("").append(cartModel.quantity)
        updateFireBase(cartModel)
    }

    private fun minusCartItem (holder: MyCartViewHolder, cartModel: CartModel)
    {
        if (cartModel.quantity > 1)
        {
            cartModel.quantity -= 1
            cartModel.totalPrice = cartModel.quantity * cartModel.price!!.toFloat()
            holder.txtQuantity!!.text = StringBuilder("").append(cartModel.quantity)
            updateFireBase(cartModel)
        }
    }

    private fun updateFireBase(cartModel: CartModel)
    {
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("Unique_User_ID")
            .child(cartModel.key!!)
            .setValue(cartModel)
            .addOnSuccessListener {EventBus.getDefault().postSticky(UpdateCartEvent())}
    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }

}