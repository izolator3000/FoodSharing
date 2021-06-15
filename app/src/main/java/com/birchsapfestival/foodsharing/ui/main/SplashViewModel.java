package com.birchsapfestival.foodsharing.ui.main;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.birchsapfestival.foodsharing.model.User;
import com.birchsapfestival.foodsharing.repository.DatabaseProvider;
import com.birchsapfestival.foodsharing.repository.Repository;

public class SplashViewModel extends ViewModel {
    private final MutableLiveData<SplashViewState> viewStateLiveData = new MutableLiveData(SplashViewState.EMPTY);

    private final DatabaseProvider repository = new Repository();
   {
       requestUser();
   }

    void requestUser(){
       if(viewStateLiveData.getValue()!=SplashViewState.EMPTY) {
           User currentUser = repository.getCurrentUser();
           if (currentUser != null) {
               viewStateLiveData.setValue(SplashViewState.AUTH);
               Log.d(getClass().getSimpleName(), currentUser.toString());
           }
       }
    }

    void setViewState(SplashViewState viewState){
       viewStateLiveData.setValue(viewState);
    }
    LiveData<SplashViewState> observeViewState(){
       return viewStateLiveData;
    }
}
