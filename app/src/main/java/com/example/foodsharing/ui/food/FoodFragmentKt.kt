package com.example.foodsharing.ui.food

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodsharing.R
import com.example.foodsharing.ui.food.adapter.FoodAdapter
import com.example.foodsharing.ui.request.MapsActivity
import java.util.*

class FoodFragmentKt : Fragment() {
    private lateinit var foodViewModel: FoodViewModelKt


    private lateinit var listOfFood: RecyclerView
    private val foodAdapter = FoodAdapter()

    companion object {
        const val EXTRA_FOOD = "EXTRA_FOOD"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        foodViewModel = ViewModelProvider(this).get(FoodViewModelKt::class.java)
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        foodViewModel.getData();

        foodViewModel.observeData().observe(viewLifecycleOwner) {
            when (it) {
                FoodsViewState.EMPTY -> Log.d(javaClass.simpleName, "Empty list")
                is FoodsViewState.Value -> {
                    Log.d(javaClass.simpleName, "Not Empty list")
                    foodAdapter.setData(it.foods.reversed())
                }
            }
        }
        foodAdapter.attachListener {
            val intent =
                Intent(context, MapsActivity::class.java).apply { putExtra(EXTRA_FOOD, it) }
            startActivity(intent)
        }
        listOfFood = view.findViewById(R.id.list_of_food)
        listOfFood.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        listOfFood.setAdapter(foodAdapter)
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
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
}