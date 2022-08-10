package com.example.apprute.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.apprute.Fragment.NaturalTourismFragment;
import com.example.apprute.Fragment.SocialCultureFragment;

public class AdapterLayout extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public AdapterLayout(Context myContext, FragmentManager fragmentManager,int totalTabs) {
        super(fragmentManager);
        this.myContext = myContext;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NaturalTourismFragment naturalTourismFragment = new NaturalTourismFragment();
                return naturalTourismFragment;
            case 1:
                SocialCultureFragment socialCultureFragment = new SocialCultureFragment();
                return socialCultureFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
