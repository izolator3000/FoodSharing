package com.example.foodsharing.ui.food

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
import com.example.foodsharing.model.FoodModel
import com.example.foodsharing.ui.food.adapter.FoodAdapter
import java.util.*

class FoodFragmentKt :Fragment(){
    private lateinit var foodViewModel: FoodViewModelKt

    private lateinit var listOfFood: RecyclerView
    private val foodAdapter = FoodAdapter()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        foodViewModel = ViewModelProvider(this).get(FoodViewModelKt::class.java)
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         foodViewModel.getData();

        foodViewModel.observeData().observe(viewLifecycleOwner){
            when(it){
                FoodsViewState.EMPTY -> Log.d(javaClass.simpleName,"Empty list")
                is FoodsViewState.Value -> {
                    Log.d(javaClass.simpleName,"Not Empty list")
                    foodAdapter.setData(it.foods.reversed())}
            }
        }
//        foodAdapter.setData(ArrayList(Arrays.asList(FoodModel("TitleOne", "SubtitleOne", "DateOne"), FoodModel("TitleTwo", "SubtitleTwo", "DateTwo"),
//                FoodModel("TitleThree", "SubtitleThree", "DateThree"),
//                FoodModel("TitleFour", "SubtitleFour", "DateFour"),
//                FoodModel("TitleFive", "SubtitleOFive", "DateOFive"),
//                FoodModel("TitleSix", "SubtitleSix", "DateSix"),
//                FoodModel("TitleSeven", "SubtitleSeven", "DateSeven"),
//                FoodModel("TitleEight", "SubtitleEight", "DateEight"),
//                FoodModel("TitleNine", "SubtitleNine", "DateNine"),
//                FoodModel("TitleTen", "SubtitleTen", "DateTen"))))
        listOfFood = view.findViewById(R.id.list_of_food)
        listOfFood.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
        listOfFood.setAdapter(foodAdapter)
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(resources, R.drawable.divider, null))!!)
        listOfFood.addItemDecoration(dividerItemDecoration)
    }
}