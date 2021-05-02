package com.example.foodsharing.ui.food;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodsharing.repository.Provider;
import com.example.foodsharing.repository.Repository;

import java.util.List;
import java.util.Map;

public class FoodViewModel extends ViewModel {

    private final Provider repository = new Repository();

    private final MutableLiveData<List<Map<String, Object>>> dataFromFirebase = new MutableLiveData<>();

    void getData() {
        List<Map<String, Object>> data = repository.getDataFromFireBase();
        dataFromFirebase.postValue(data);
    }

    MutableLiveData<List<Map<String, Object>>> observeData() {
        return dataFromFirebase;
    }
}