package com.example.dimitriygeorgiev.hw_practice.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.dimitriygeorgiev.hw_practice.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemoryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.img_city)
    ImageView imageView;

    public MemoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setImg_city(String imgUrl) {
        Picasso.get().load(imgUrl).into(imageView);
    }
}
