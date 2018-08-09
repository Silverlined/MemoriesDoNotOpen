package com.example.dimitriygeorgiev.hw_practice.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.dimitriygeorgiev.hw_practice.R;
import com.example.dimitriygeorgiev.hw_practice.models.CityModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ThirdFirebaseAdapter extends FirebaseRecyclerAdapter<CityModel, CityViewHolder> {
    private DatabaseReference allMemoriesReference;
    private DatabaseReference sharedMemoriesReference;
    private FirebaseUser firebaseUser;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link com.firebase.ui.database.FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     * @param user
     */
    public ThirdFirebaseAdapter(@NonNull FirebaseRecyclerOptions<CityModel> options, FirebaseUser user) {
        super(options);
        this.firebaseUser = user;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shared_memory_layout, parent, false);

        return new CityViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull CityViewHolder holder, int position, @NonNull CityModel model) {
        holder.setImg_city(model.getImageUrl());
        holder.setTxt_description(model.getDescription());
        holder.setIconOfImageButtonSharedMemories(model, firebaseUser.getUid());
        holder.setTxt_likes(model.getLikes());
        holder.btnLike.setOnClickListener(v -> onLikeClicked(model, (ImageButton) v));
    }

    private void onLikeClicked(CityModel model, ImageButton btnLike) {
        sharedMemoriesReference = FirebaseDatabase.getInstance().getReference().child("shared")
                .child(firebaseUser.getUid() + model.getTimeStamp());
        allMemoriesReference = FirebaseDatabase.getInstance().getReference().child("places")
                .child(firebaseUser.getUid()).child(model.getCity() + model.getTimeStamp());
        boolean result = model.incrementLikes(firebaseUser.getUid());
        if (result) {
            btnLike.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            btnLike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        Map<String, Object> update = new HashMap<>();
        update.put("likes", model.getLikes());
        Map<String, Object> updateUsers = new HashMap<>();
        update.put("users", model.getUsers());
        sharedMemoriesReference.updateChildren(update);
        sharedMemoriesReference.updateChildren(updateUsers);
        allMemoriesReference.updateChildren(update);
        allMemoriesReference.updateChildren(updateUsers);
        notifyDataSetChanged();
    }
}