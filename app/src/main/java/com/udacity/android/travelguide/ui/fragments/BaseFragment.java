package com.udacity.android.travelguide.ui.fragments;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    protected void log(String msg) {
        Log.d(TAG, msg);
    }

    public Context getContext() {
        return getActivity();
    }

    public android.support.v7.app.ActionBar getActionBar() {
        AppCompatActivity ac = getAppCompatActivity();
        return ac.getSupportActionBar();
    }

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    protected void snack(View view, String msg) {
        Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG)
                .show();
    }

}
