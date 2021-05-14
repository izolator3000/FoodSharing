package com.example.foodsharing.ui.food

import com.example.foodsharing.model.FoodModel

sealed class FoodsViewState {
    data class Value(val foods: List<FoodModel>) : FoodsViewState()
    object EMPTY : FoodsViewState()
}