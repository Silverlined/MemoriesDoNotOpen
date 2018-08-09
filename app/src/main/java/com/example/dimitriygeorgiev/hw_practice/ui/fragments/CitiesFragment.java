package com.example.dimitriygeorgiev.hw_practice.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dimitriygeorgiev.hw_practice.R;
import com.example.dimitriygeorgiev.hw_practice.adapters.FirebaseAdapter;
import com.example.dimitriygeorgiev.hw_practice.models.CityModel;
import com.example.dimitriygeorgiev.hw_practice.ui.activities.AddCityActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CitiesFragment extends Fragment {
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter adapter;
    FirebaseUser user;
    FloatingActionButton floatingButton;
    private static final String USER_ID = String.valueOf(R.string.user_id);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_cities_layout, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        floatingButton = view.findViewById(R.id.btn_floating);
        setUpRecyclerView();
        floatingButton.setOnClickListener(v -> onFloatingButtonClick());
        return view;
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("places")
                .child(user.getUid());
        FirebaseRecyclerOptions<CityModel> options =
                new FirebaseRecyclerOptions.Builder<CityModel>()
                        .setQuery(query, CityModel.class)
                        .build();
        adapter = new FirebaseAdapter(options, user);
        recyclerView.setAdapter(adapter);
    }

    private void onFloatingButtonClick() {
        Intent intent = new Intent(getActivity(), AddCityActivity.class);
        intent.putExtra(USER_ID, user.getUid());
        startActivity(intent);
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
