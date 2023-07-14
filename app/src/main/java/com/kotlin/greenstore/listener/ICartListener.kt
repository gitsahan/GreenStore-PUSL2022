package com.kotlin.greenstore.listener

import com.kotlin.greenstore.model.CartModel

interface ICartListener
{
    fun onCartSuccess (cartModelList: List<CartModel>)
    fun onCartFailed (message:String?)
}