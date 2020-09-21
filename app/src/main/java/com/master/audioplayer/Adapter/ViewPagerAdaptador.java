package com.master.audioplayer.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdaptador extends FragmentPagerAdapter {

    private ArrayList <Fragment> fragments;
    private ArrayList<String> titulos;
    public ViewPagerAdaptador(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList <>();
        this.titulos = new ArrayList <>();
    }

    public void addFragment(Fragment fragment, String titulo){
        fragments.add(fragment);
        titulos.add(titulo);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titulos.get(position);
    }
}
