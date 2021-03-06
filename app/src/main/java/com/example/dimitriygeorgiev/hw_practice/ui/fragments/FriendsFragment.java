package com.example.dimitriygeorgiev.hw_practice.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dimitriygeorgiev.hw_practice.adapters.SecondFirebaseAdapter;
import com.example.dimitriygeorgiev.hw_practice.adapters.ThirdFirebaseAdapter;
import com.example.dimitriygeorgiev.hw_practice.models.CityModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FriendsFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseRecyclerAdapter adapter;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("shared");
        FirebaseRecyclerOptions<CityModel> options =
                new FirebaseRecyclerOptions.Builder<CityModel>()
                        .setQuery(query, CityModel.class)
                        .build();
        adapter = new ThirdFirebaseAdapter(options, user);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void setUser(FirebaseUser firebaseUser) {
        this.user = firebaseUser;
    }
}
