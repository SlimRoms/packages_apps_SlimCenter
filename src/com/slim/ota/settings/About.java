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
 *  DESCRIPTION: SlimOTA keeps our rom up to date
 *
 *=========================================================================
 */

package com.slim.ota.settings;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.slim.center.SlimCenter;
import com.slim.ota.R;

public class About extends Activity {
    @SuppressWarnings("unused")
    private static final String TAG = "About";

    private static final int ID_HOME_URL = R.id.about_home;
    private static final int ID_CONTACT = R.id.about_contact;
    private static final int ID_LICENSE = R.id.about_license_url;

    private TextView mHomeURL;
    private TextView mContact;
    private TextView mLicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        mHomeURL = (TextView) findViewById(ID_HOME_URL);
        mContact = (TextView) findViewById(ID_CONTACT);
        mLicense = (TextView) findViewById(ID_LICENSE);

        mHomeURL.setMovementMethod(LinkMovementMethod.getInstance());
        mContact.setMovementMethod(LinkMovementMethod.getInstance());
        mLicense.setMovementMethod(LinkMovementMethod.getInstance());

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
