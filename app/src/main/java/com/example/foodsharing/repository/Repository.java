package com.example.foodsharing.repository;

import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.model.User;
import com.example.foodsharing.repository.remote.FirebaseDataProviderKt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;

import kotlinx.coroutines.flow.Flow;

public class Repository implements Provider {
    FirebaseDataProviderKt fbDataProvider = new FirebaseDataProviderKt();

    @Override
    public Flow<List<FoodModel>> observeFoods() {
        return fbDataProvider.observeFoods();
    }

    @Override
    public void pushRequest(FoodModel foodModel) {
        fbDataProvider.pushRequest(foodModel);
    }

    @Override
    public User getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        User currentUser = new User();
        if (user != null) {
            currentUser.setName(user.getDisplayName());
            currentUser.setEmail(user.getEmail());
            currentUser.setEmailVerified(user.isEmailVerified());
            currentUser.setUid(user.getUid());
            currentUser.setUrl(user.getPhotoUrl());
        }
        return currentUser;
    }
}
