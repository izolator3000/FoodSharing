package com.example.foodsharing.repository;

import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.model.User;

import java.util.List;

import kotlinx.coroutines.flow.Flow;

public interface DatabaseProvider {
    Flow<List<FoodModel>> observeFoods();
    void getDataFromFirebase();
    void pushRequest(FoodModel model);
    User getCurrentUser();
}
