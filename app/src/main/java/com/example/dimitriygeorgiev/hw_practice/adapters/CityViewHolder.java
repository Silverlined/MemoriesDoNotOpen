package com.example.dimitriygeorgiev.hw_practice.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dimitriygeorgiev.hw_practice.R;
import com.example.dimitriygeorgiev.hw_practice.models.CityModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.img_city)
    ImageView imageView;
    @BindView(R.id.txt_description)
    TextView txtDescription;
    @BindView(R.id.btn_like)
    ImageButton btnLike;
    @BindView(R.id.txt_number_likes)
    TextView txtLikes;

    public CityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setImg_city(String imgUrl) {
        Picasso.get().load(imgUrl).into(imageView);
    }

    public void setTxt_description(String txt_description) {
        txtDescription.setText(txt_description);
    }

    public void setIconOfImageButton(CityModel model) {
        if (model.getState() == 0) {
            btnLike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else if (model.getState() == 1) {
            btnLike.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setIconOfImageButtonSharedMemories(CityModel model, String user) {
        if (model.getUsers().contains(user)) {
            btnLike.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            btnLike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    public void setTxt_likes(int likes) {
        txtLikes.setText(String.valueOf(likes));
    }
}
