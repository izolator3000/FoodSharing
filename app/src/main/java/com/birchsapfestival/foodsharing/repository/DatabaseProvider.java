package com.birchsapfestival.foodsharing.repository;

import com.birchsapfestival.foodsharing.model.FoodModel;
import com.birchsapfestival.foodsharing.model.User;

import java.util.List;

import kotlinx.coroutines.flow.Flow;

public interface DatabaseProvider {
    Flow<List<FoodModel>> observeFoods();

    void getDataFromFirebase();

    void  pushRequest(FoodModel model);

    void deleteRequest(Long id);

    User getCurrentUser();
}
