package com.birchsapfestival.foodsharing.ui.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.birchsapfestival.foodsharing.repository.DatabaseProvider
import com.birchsapfestival.foodsharing.repository.Repository
import com.birchsapfestival.foodsharing.ui.food.FoodsViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MapsViewModel : ViewModel() {

    private val repository: DatabaseProvider = Repository()

    private val foodsLiveData = MutableLiveData<FoodsViewState>()

    fun getData() {
        repository.observeFoods().onEach {
            foodsLiveData.value =
                if (it.isEmpty()) FoodsViewState.EMPTY else FoodsViewState.Value(it)
        }.launchIn(viewModelScope)
    }

    fun observeData(): LiveData<FoodsViewState> = foodsLiveData
}