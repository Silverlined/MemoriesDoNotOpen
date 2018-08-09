package com.example.dimitriygeorgiev.hw_practice.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.dimitriygeorgiev.hw_practice.ui.fragments.CitiesFragment;
import com.example.dimitriygeorgiev.hw_practice.ui.fragments.FriendsFragment;
import com.example.dimitriygeorgiev.hw_practice.ui.fragments.MyCitiesFragment;
import com.google.firebase.auth.FirebaseUser;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;
    private FirebaseUser firebaseUser;

    public PagerAdapter(FragmentManager fm, int numberOfTabs, FirebaseUser firebaseUser) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.firebaseUser = firebaseUser;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FriendsFragment friendsFragment = new FriendsFragment();
                friendsFragment.setUser(firebaseUser);
                return friendsFragment;
            case 1:
                CitiesFragment citiesFragment = new CitiesFragment();
                citiesFragment.setUser(firebaseUser);
                return citiesFragment;
            case 2:
                MyCitiesFragment myCitiesFragment = new MyCitiesFragment();
                myCitiesFragment.setUser(firebaseUser);
                return myCitiesFragment;
                    default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
