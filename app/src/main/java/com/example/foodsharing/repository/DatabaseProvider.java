package com.example.foodsharing.repository;

import com.example.foodsharing.model.FoodModel;

import java.util.List;
import java.util.Map;

public interface DatabaseProvider {
    List<Map<String, Object>> getDataFromFirebase();
    void pushRequest(FoodModel model);
}
