package com.example.dimitriygeorgiev.hw_practice.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dimitriygeorgiev.hw_practice.R;
import com.example.dimitriygeorgiev.hw_practice.adapters.PagerAdapter;
import com.example.dimitriygeorgiev.hw_practice.models.CityModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private static final int RC_SIGN_IN = 123;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    FirebaseUser firebaseUser;

    private static final byte MAIN_TAB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("You&I"));
        tabLayout.addTab(tabLayout.newTab().setText("Places"));
        tabLayout.addTab(tabLayout.newTab().setText("Fav"));


        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // already signed in
                    onSignInInitialized(user);
                } else {
                    // not signed in
                    onSignOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setTheme(R.style.GreenTheme)
                                    .setLogo(R.drawable.story)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void setUpFragmentsUi() {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), firebaseUser);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(MAIN_TAB);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void onSignOutCleanUp() {
    }

    private void onSignInInitialized(FirebaseUser user) {
        firebaseUser = user;
        setUpFragmentsUi();
    }

//    private void populateDatabase() {
//        DatabaseReference databaseReference;
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("cities");
//        List<CityModel> cities = Arrays.asList((new CityModel[]{
//                new CityModel("http://www.varna.bg/files/images/slideshow/custom/faa19d9fca6aeabbf04e8b042b4071d4.jpg",
//                        "Varna"),
//                new CityModel("http://www.bestourism.com/img/items/big/7091/Sofia_Amazing-landscape_9072.jpg",
//                        "Sofia"),
//                new CityModel("https://mir-s3-cdn-cf.behance.net/project_modules/disp/11b5017211335.56028ecfe5954.jpg",
//                        "Plovdiv"),
//                new CityModel("https://s-media-cache-ak0.pinimg.com/originals/15/ef/93/15ef93b44bee29cc8afe5fb120d1ef40.jpg",
//                        "Ruse"),
//                new CityModel("https://itravel-bg.com/wp-content/uploads/2017/12/800px-Vratsa-740x431.jpg",
//                        "Vratsa")
//        }));
//        for (int i = 0; i < cities.size(); i++) {
//            switch (i) {
//                case 0:
//                    databaseReference.child("Varna").setValue(cities.get(i));
//                    break;
//                case 1:
//                    databaseReference.child("Sofia").setValue(cities.get(i));
//                    break;
//                case 2:
//                    databaseReference.child("Plovdiv").setValue(cities.get(i));
//                    break;
//                case 3:
//                    databaseReference.child("Ruse").setValue(cities.get(i));
//                    break;
//                case 4:
//                    databaseReference.child("Vratsa").setValue(cities.get(i));
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed out!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_sign_out:
                //sign out
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}