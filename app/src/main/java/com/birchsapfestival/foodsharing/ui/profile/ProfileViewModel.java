package com.birchsapfestival.foodsharing.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.birchsapfestival.foodsharing.model.User;
import com.birchsapfestival.foodsharing.repository.DatabaseProvider;
import com.birchsapfestival.foodsharing.repository.Repository;

public class ProfileViewModel extends ViewModel {
    private final DatabaseProvider repository = new Repository();
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