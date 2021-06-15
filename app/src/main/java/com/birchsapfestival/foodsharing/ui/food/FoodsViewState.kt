package com.birchsapfestival.foodsharing.ui.food

import com.birchsapfestival.foodsharing.model.FoodModel

sealed class FoodsViewState {
    data class Value(val foods: List<FoodModel>) : FoodsViewState()
    object EMPTY : FoodsViewState()
}