package com.example.foodsharing.ui.request;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.foodsharing.model.FoodModel;
import com.example.foodsharing.repository.Provider;
import com.example.foodsharing.repository.Repository;

public class CreateRequestViewModel extends ViewModel {

    private Provider provider = new Repository();

    public void pushRequest(FoodModel model) {
        provider.pushRequest(model);
        Log.e(getClass().getSimpleName(), model.toString());
    }
}
