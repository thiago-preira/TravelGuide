package com.udacity.android.travelguide.util;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.udacity.android.travelguide.R;

import java.util.ArrayList;
import java.util.List;

public abstract class FirebaseAuthUtil {

    public static Intent createAuthIntent(boolean smartLockEnabled){
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(getProviderList())
                .setIsSmartLockEnabled(smartLockEnabled)
                .setAlwaysShowSignInMethodScreen(true)
                .setLogo(R.drawable.logo)
                .setTheme(R.style.LoginTheme)
                .build();
    }

    private static  List<AuthUI.IdpConfig> getProviderList() {
        List<AuthUI.IdpConfig> providers = new ArrayList<>();
        providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        providers.add(new AuthUI.IdpConfig.EmailBuilder().build());
        return providers;
    }

}
