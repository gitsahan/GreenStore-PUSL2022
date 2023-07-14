package com.kotlin.greenstore.listener

import com.kotlin.greenstore.model.DrinkModel

interface IDrinkListener
{
    fun onDrinkLoadSuccess(drinkModelList: List<DrinkModel>?)
    fun onDrinkLoadFailed(message:String?)
}