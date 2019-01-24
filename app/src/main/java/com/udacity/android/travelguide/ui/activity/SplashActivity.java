package com.udacity.android.travelguide.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udacity.android.travelguide.util.FirebaseAuthUtil;

import static com.udacity.android.travelguide.util.Constants.ENABLE_SMART_LOCK;

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleFirebaseAuth();
    }

    private void handleFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "User logged, starting HomeActivity");
                    Intent mainIntent = createIntentFor(HomeActivity.class);
                    mainIntent.putExtra("user", user.getDisplayName());
                    startActivity(mainIntent);
                } else {
                    // User is signed out
                    Log.d(TAG, "User not logged, starting FirebaseUI");
                    startActivityForResult(
                            FirebaseAuthUtil.createAuthIntent(ENABLE_SMART_LOCK),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

}


