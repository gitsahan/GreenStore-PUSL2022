package com.kotlin.greenstore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.greenstore.R
import java.nio.file.Files.size

class MySumrAdapter(private val useList: ArrayList<MySumrAdapter>):RecyclerView.Adapter<MySumrAdapter.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySumrAdapter.MyViewHolder{

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row,
            parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        val currentitem =useList [position]

        holder.key.text = currentitem.toString()
        holder.name.text = currentitem.toString()
        holder.price.text = currentitem.toString()
        holder.quantity.text = currentitem.toString()
        holder.totalprice.text =currentitem.toString()
        holder.image.text = currentitem.toString()
    }

    override fun getItemCount(): Int {


        return useList.size

    }
  class MyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){

      val key : TextView = itemView.findViewById(R.id.key)
      val name : TextView = itemView.findViewById(R.id.name)
      val price : TextView = itemView.findViewById(R.id.price)
      val quantity : TextView= itemView.findViewById(R.id.quantity)
      val totalprice : TextView = itemView.findViewById(R.id.totalprice)
      val image : TextView =itemView.findViewById(R.id.img)

  }

}