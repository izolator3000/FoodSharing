package com.birchsapfestival.foodsharing.ui.food

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.birchsapfestival.foodsharing.R
import com.birchsapfestival.foodsharing.ui.food.adapter.FoodAdapter
import com.birchsapfestival.foodsharing.ui.food.maps.FoodsOnMapsActivity
import java.util.*

class FoodFragmentKt : Fragment() {
    private lateinit var menuItem: MenuItem
    private lateinit var foodViewModel: FoodViewModelKt
    private lateinit var listOfFood: RecyclerView
    private val foodAdapter = FoodAdapter()

    companion object {
        const val EXTRA_FOOD = "EXTRA_FOOD"
        const val EXTRA_OPEN_MAPS_WITH_ALL_FOODS = "EXTRA_OPEN_MAPS_WITH_ALL_FOODS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
                Intent(context, FoodsOnMapsActivity::class.java).apply { putExtra(EXTRA_FOOD, it) }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_maps -> {
                val intent = Intent(context, FoodsOnMapsActivity::class.java).apply {
                    putExtra(EXTRA_OPEN_MAPS_WITH_ALL_FOODS, "")
                }
                startActivity(intent)
            }
            else -> return false
        }
        return true
    }
}