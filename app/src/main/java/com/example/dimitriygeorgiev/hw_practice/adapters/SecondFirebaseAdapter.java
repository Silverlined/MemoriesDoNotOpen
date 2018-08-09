package com.example.dimitriygeorgiev.hw_practice.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class SecondFirebaseAdapter extends FirebaseRecyclerAdapter<CityModel, MemoryViewHolder> {
    private DatabaseReference allCitiesReference;
    private DatabaseReference favouriteCitiesReference;
    private DatabaseReference sharedReference;
    private FirebaseUser firebaseUser;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     * @param user
     */
    public SecondFirebaseAdapter(@NonNull FirebaseRecyclerOptions<CityModel> options, FirebaseUser user) {
        super(options);
        this.firebaseUser = user;
    }

    @NonNull
    @Override
    public MemoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_city_layout, parent, false);

        return new MemoryViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MemoryViewHolder holder, int position, @NonNull CityModel model) {
        holder.setImg_city(model.getImageUrl());
    }

    /*
    private void onUnlikeClicked(ImageButton v, CityModel model) {
        if (model.getState() != 1 && model.getState() != 0) {
            throw new IllegalArgumentException();
        }
        v.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        allCitiesReference = FirebaseDatabase.getInstance().getReference().child("places").child(firebaseUser.getUid());
        favouriteCitiesReference = FirebaseDatabase.getInstance().getReference().child("favourite").child(firebaseUser.getUid());
        sharedReference = FirebaseDatabase.getInstance().getReference().child("shared");
        favouriteCitiesReference.child(model.getCity() + model.getTimeStamp()).setValue(null, (databaseError, databaseReference) -> updateDB(model));
        sharedReference.child(firebaseUser.getUid() + model.getTimeStamp()).setValue(null);
    }

    private void updateDB(CityModel model) {
        model.setState(0);
        Map<String, Object> update = new HashMap<>();
        update.put("state", model.getState());
        allCitiesReference.child(model.getCity() + model.getTimeStamp()).updateChildren(update);
        notifyDataSetChanged();
    }
    */
}
