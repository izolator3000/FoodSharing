package com.example.foodsharing.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodsharing.model.User;
import com.example.foodsharing.repository.Provider;
import com.example.foodsharing.repository.Repository;

public class ProfileViewModel extends ViewModel {
    private final Provider repository = new Repository();
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    void setName(){
       User currentUser = repository.getCurrentUser();
       if(currentUser!=null){
           userLiveData.postValue(currentUser);
       }
    }

    public LiveData<User> observeUser(){
        return userLiveData;
    }
}