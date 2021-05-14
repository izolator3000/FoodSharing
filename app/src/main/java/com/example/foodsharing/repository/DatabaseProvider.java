package com.example.foodsharing.repository;

import com.example.foodsharing.model.FoodModel;

import java.util.List;
import java.util.Map;

import kotlinx.coroutines.flow.Flow;

public interface DatabaseProvider {
    Flow<List<FoodModel>> observeFoods();
    void getDataFromFirebase();
    void pushRequest(FoodModel model);
}
