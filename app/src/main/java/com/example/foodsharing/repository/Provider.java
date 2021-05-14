package com.example.foodsharing.repository;

import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.model.User;

import java.util.List;
import java.util.Map;

import kotlinx.coroutines.flow.Flow;

public interface Provider {
  //  void getDataFromFireBase();
    Flow<List<FoodModel>> observeFoods();
    void pushRequest(FoodModel foodModel);
    User getCurrentUser();
}
