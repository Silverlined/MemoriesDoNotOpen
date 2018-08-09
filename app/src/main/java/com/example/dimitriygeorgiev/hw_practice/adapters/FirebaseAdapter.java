package com.example.dimitriygeorgiev.hw_practice.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseAdapter extends FirebaseRecyclerAdapter<CityModel, CityViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private DatabaseReference favouriteCitiesReference;
    private DatabaseReference allCitiesReference;
    private DatabaseReference sharedReference;
    private FirebaseUser firebaseUser;

    public FirebaseAdapter(@NonNull FirebaseRecyclerOptions<CityModel> options, FirebaseUser user) {
        super(options);
        this.firebaseUser = user;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_card_view_layout, parent, false);

        return new CityViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull CityViewHolder holder, int position, @NonNull CityModel model) {
        holder.setImg_city(model.getImageUrl());
        holder.setTxt_description(model.getDescription());
        holder.setIconOfImageButton(model);
        holder.btnLike.setOnClickListener(v -> onLikeClicked((ImageButton) v, model, position));
    }

    private void onLikeClicked(ImageButton v, CityModel model, int position) {
        favouriteCitiesReference = FirebaseDatabase.getInstance().getReference().child("favourite").child(firebaseUser.getUid());
        allCitiesReference = FirebaseDatabase.getInstance().getReference().child("places").child(firebaseUser.getUid());
        sharedReference = FirebaseDatabase.getInstance().getReference().child("shared");

        if (model.getState() != 1 && model.getState() != 0) {
            throw new IllegalArgumentException();
        } else if (model.getState() == 0) {
            model.setState(1);
            v.setImageResource(R.drawable.ic_favorite_black_24dp);
            Map<String, Object> update = new HashMap<>();
            update.put("state", model.getState());
            allCitiesReference.child(model.getCity() + model.getTimeStamp()).updateChildren(update, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    favouriteCitiesReference.child(model.getCity() + model.getTimeStamp()).setValue(model);
                    sharedReference.child(firebaseUser.getUid() + model.getTimeStamp()).setValue(model);
                    notifyDataSetChanged();
                }
            });
        } else if (model.getState() == 1) {
            model.setState(0);
            Map<String, Object> update = new HashMap<>();
            update.put("state", model.getState());
            allCitiesReference.child(model.getCity() + model.getTimeStamp()).updateChildren(update, (databaseError, databaseReference) -> updateDb(model));
            v.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void updateDb(CityModel model) {
        favouriteCitiesReference.child(model.getCity() + model.getTimeStamp()).setValue(null);
        sharedReference.child(firebaseUser.getUid() + model.getTimeStamp()).setValue(null);
        notifyDataSetChanged();
    }
}
