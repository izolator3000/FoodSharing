package com.birchsapfestival.foodsharing.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.birchsapfestival.foodsharing.R;

public class SplashActivity extends BaseActivity {

    private SplashViewModel splashViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashViewModel =
                new ViewModelProvider(this).get(SplashViewModel.class);
        splashViewModel.observeViewState().observe(this, new Observer<SplashViewState>() {
            @Override
            public void onChanged(SplashViewState splashViewState) {
                if (splashViewState == SplashViewState.AUTH) {
                    renderData();
                } else if (splashViewState == SplashViewState.ERROR) {
                    renderError();
                } else if (splashViewState == SplashViewState.EMPTY) {
                    startLoginActivity();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != RC_SIGN_IN) {
            return;
        } else if (resultCode != RESULT_OK) {
            finish();
        } else if (resultCode == RESULT_OK) {
            renderData();
        }
    }
}
