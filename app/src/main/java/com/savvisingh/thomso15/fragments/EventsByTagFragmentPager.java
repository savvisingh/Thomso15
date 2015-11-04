package com.savvisingh.thomso15.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savvisingh.thomso15.R;

/**
 * Created by Savvi Singh on 10/7/2015.
 */
public class EventsByTagFragmentPager extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);


        pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());


        //initializing tab layout
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(View.VISIBLE);
        //initializing ViewPager
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        //giving viewPager reference to tablayout so that the viewPager changes when tab is clicked
        tabLayout.setupWithViewPager(viewPager);
        //giving tablayout reference to viewPager so that the tablayout changes when viewPager is scrolled
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        return view;
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        Fragment fragment = null;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Based upon the position you can call the fragment you need here
            //here i have called the same fragment for all the instances
            fragment = null;
            switch (position) {
                case 0:
                    fragment = FragmentsByTag.newInstance("PRONITES");
                    break;

                case 1:
                    fragment = FragmentsByTag.newInstance("FORMALS");
                    break;

                case 2:
                    fragment = FragmentsByTag.newInstance("INFORMALS");
                    break;

            }
            return fragment;


        }


        @Override
        public int getCount() {
            // Returns the number of tabs (If you need 4 tabs change it to 4)
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //this is where you set texts to your tabs based upon the position
            //positions starts from 0


            switch (position){
                case 0:
                    return "Pronite";
                case 1:
                    return "Formal";
                case 2:
                    return "Informal";

            }
            return null;
        }
    }
}
