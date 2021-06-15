package com.birchsapfestival.foodsharing.ui.request;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.birchsapfestival.foodsharing.model.FoodModel;
import com.birchsapfestival.foodsharing.repository.DatabaseProvider;
import com.birchsapfestival.foodsharing.repository.Repository;

public class CreateRequestViewModel extends ViewModel {

    private DatabaseProvider provider = new Repository();

    public void pushRequest(FoodModel model) {
        provider.pushRequest(model);
        Log.e(getClass().getSimpleName(), model.toString());
    }

    public String getEmail() {
        return provider.getCurrentUser().getEmail();
    }
}
