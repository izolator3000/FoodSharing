package com.example.foodsharing.repository;

import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.model.User;

import java.util.List;
import java.util.Map;

public interface Provider {
    List<Map<String, Object>> getDataFromFireBase();
    void pushRequest(FoodModel foodModel);
    User getCurrentUser();
}
