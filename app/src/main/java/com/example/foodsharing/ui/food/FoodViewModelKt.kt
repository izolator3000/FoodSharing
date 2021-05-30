package com.example.foodsharing.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodsharing.repository.DatabaseProvider
import com.example.foodsharing.repository.Repository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FoodViewModelKt : ViewModel() {
    private val repository: DatabaseProvider = Repository()

    private val foodsLiveData = MutableLiveData<FoodsViewState>()

    fun getData() {
        //List<Map<String, Object>> data = repository.getDataFromFireBase();
        // dataFromFirebase.postValue(data);
        repository.observeFoods().onEach {
            foodsLiveData.value =
                    if (it.isEmpty()) FoodsViewState.EMPTY else FoodsViewState.Value(it)
        }.launchIn(viewModelScope)
    }

    fun observeData(): LiveData<FoodsViewState> = foodsLiveData
}