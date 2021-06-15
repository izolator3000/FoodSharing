package com.birchsapfestival.foodsharing.ui.profile.my_food

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.birchsapfestival.foodsharing.R
import com.birchsapfestival.foodsharing.model.FoodModel
import com.birchsapfestival.foodsharing.ui.food.FoodsViewState
import com.birchsapfestival.foodsharing.ui.food.adapter.FoodAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class MyFoodActivity : AppCompatActivity() {
    private lateinit var myFoodViewModel: MyFoodViewModel
    private lateinit var listOfFood: RecyclerView
    private val foodAdapter = FoodAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_food)
        myFoodViewModel = ViewModelProvider(this).get(MyFoodViewModel::class.java)
        myFoodViewModel.getData();

        myFoodViewModel.observeData().observe(this)
        { data ->
            when (data) {
                FoodsViewState.EMPTY -> Log.d(javaClass.simpleName, "Empty list")
                is FoodsViewState.Value -> {
                    Log.d(javaClass.simpleName, "Not Empty list")
                    val reversedFood = mutableListOf<FoodModel>()
                    data.foods.forEach { food ->
                        if (food.email == myFoodViewModel.getEmail()) {
                            reversedFood.add(food)
                        }
                    }
                    foodAdapter.setData(reversedFood.reversed())
                }
            }
        }

        foodAdapter.attachListener {
            createDialog(it)
        }
        listOfFood = findViewById(R.id.list_of_food)
        listOfFood.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        listOfFood.setAdapter(foodAdapter)
        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(
            Objects.requireNonNull(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.divider,
                    null
                )
            )!!
        )
        listOfFood.addItemDecoration(dividerItemDecoration)
    }

    private fun createDialog(food: FoodModel) {

        MaterialAlertDialogBuilder(this).setTitle("Удалить заявку?")
            .setNegativeButton("NO", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Toast.makeText(this@MyFoodActivity, "NO", Toast.LENGTH_SHORT).show()
                }
            }
            )
            .setPositiveButton("YES", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    myFoodViewModel.deleteFood(food)
                }
            }
            ).show()
    }
}