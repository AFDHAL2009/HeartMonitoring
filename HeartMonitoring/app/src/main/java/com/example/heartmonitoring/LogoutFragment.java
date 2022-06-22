package com.example.heartmonitoring;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Created by velmmuru on 1/6/2018.
 */

public class LogoutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences userDetails =getActivity().getSharedPreferences("test", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = userDetails.edit();
        edit.clear();
        edit.putString("email", "");
        edit.commit();
        //call home activity
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(getActivity(),Home.class);
        getActivity().startActivity(myIntent);

        return null;

    }
}