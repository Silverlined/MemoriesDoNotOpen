package com.example.dimitriygeorgiev.hw_practice.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dimitriygeorgiev.hw_practice.R;
import com.example.dimitriygeorgiev.hw_practice.models.CityModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCityActivity extends AppCompatActivity {

    private CityModel mCityModel;
    private String mUserId;
    private static final int RC_PHOTO_PICKER = 2;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mImagesStorageReference;
    private Uri uploadedPhotoURI;
    private Uri selectedImgUri;

    @BindView(R.id.img_add_picture)
    ImageView imgAddPicture;
    @BindView(R.id.edt_city)
    EditText edtCity;
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.btn_done)
    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        ButterKnife.bind(this);
        mUserId = getIntent().getStringExtra(String.valueOf(R.string.user_id));
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("places").child(mUserId);
        mImagesStorageReference = mFirebaseStorage.getReference().child(mUserId);
        imgAddPicture.setOnClickListener(v -> addPictureAsync());
        btnDone.setOnClickListener(v -> onDoneClick());
    }

    private void onDoneClick() {
        String city = String.valueOf(edtCity.getText());
        String description = String.valueOf(edtDescription.getText());
        if (description != null && city != null && selectedImgUri != null) {
            StorageReference photoRef = mImagesStorageReference.child(selectedImgUri.getLastPathSegment());
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (bmp.getAllocationByteCount() < 25000000) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            } else {
                bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos);
            }
            byte[] data = baos.toByteArray();
            photoRef.putBytes(data).addOnSuccessListener(this,
                    taskSnapshot -> getImageUri(city, description, photoRef));
        } else {
            Toast.makeText(this, "Please, fill everything necessary first", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private Task<Uri> getImageUri(String city, String description, StorageReference photoRef) {
        return photoRef.getDownloadUrl().addOnSuccessListener(uri -> uploadNewModel(city, description, uri));
    }

    private void uploadNewModel(String city, String description, Uri uri) {
        uploadedPhotoURI = uri;
        mCityModel = new CityModel(uploadedPhotoURI.toString(), city, description);
        mDatabaseReference.child(city + mCityModel.getTimeStamp()).setValue(mCityModel);
        finish();
    }

    private void addPictureAsync() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            selectedImgUri = data.getData();
            if (selectedImgUri != null) {
                Picasso.get().load(selectedImgUri).resize(240, 150).centerCrop().into(imgAddPicture);
            }
        }

    }
}
