package com.birchsapfestival.foodsharing.ui.chat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ChatViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("В разработке. Используйте номер телефона для связи");
    }

    public LiveData<String> getText() {
        return mText;
    }
}