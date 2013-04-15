/*=========================================================================
 *
 *  PROJECT:  SlimRoms
 *            Team Slimroms (http://www.slimroms.net)
 *
 *  COPYRIGHT Copyright (C) 2013 Slimroms http://www.slimroms.net
 *            All rights reserved
 *
 *  LICENSE   http://www.gnu.org/licenses/gpl-2.0.html GNU/GPL
 *
 *  AUTHORS:     fronti90, mnazim, tchaari, kufikugel
 *  DESCRIPTION: SlimCenter: manage your ROM
 *
 *=========================================================================
 */
package com.slim.center;

import java.util.List;
import java.util.Locale;

import com.slim.ota.R;
import com.slim.ota.SlimOTA;
import com.slim.ota.settings.About;
import com.slim.sizer.SlimSizer;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class SlimCenter extends FragmentActivity implements
ActionBar.TabListener{
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slim_center);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
            FragmentTransaction fragmentTransaction) {
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
            case 0:
                return getString(R.string.ota_title).toUpperCase(l);
            case 1:
                return getString(R.string.sizer_title).toUpperCase(l);
            case 2:
                return getString(R.string.aboutslim_title).toUpperCase(l);
            }
            return null;
        }

        public android.support.v4.app.Fragment getItem(int page) {
            switch (page) {
            case 0:
                return new SlimOTA();
            case 1:
                return new SlimSizer();
            case 2:
                return new AboutSlim();
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.slim_center, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.ab_about:
                Intent intentAbout = new Intent(this, About.class);
                startActivity(intentAbout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
