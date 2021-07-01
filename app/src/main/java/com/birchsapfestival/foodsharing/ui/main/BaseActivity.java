package com.birchsapfestival.foodsharing.ui.main;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.birchsapfestival.foodsharing.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class BaseActivity extends AppCompatActivity {
    static final int RC_SIGN_IN = 458;

    protected void startLoginActivity() {
        ArrayList<AuthUI.IdpConfig> providers =
                new ArrayList(Arrays.asList(
                        new AuthUI.IdpConfig.PhoneBuilder()
                                .setDefaultNumber("ru", "23456789")
                                .setWhitelistedCountries(new ArrayList<>(Arrays.asList("ru", "ua", "by")))
                                .build()));

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setLogo(R.drawable.ic_launcher_foreground).setTheme(R.style.LoginStyle)
                .setAvailableProviders(providers).build(), RC_SIGN_IN);
    }


    protected void renderData() {
        startMainActivity();
    }

    private void startMainActivity() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }

    protected void renderError() {
        startLoginActivity();
        Toast.makeText(this, "Error! May be wrong password?", Toast.LENGTH_SHORT).show();
    }
}
