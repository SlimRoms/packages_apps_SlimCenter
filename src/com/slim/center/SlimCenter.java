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

import com.slim.ota.R;
import com.slim.ota.SlimOTA;
import com.slim.sizer.SlimSizer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class SlimCenter extends TabActivity{

    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
            setContentView(R.layout.slim_center);
            TabHost TabHost = getTabHost();
            Intent intent;
            intent=new Intent().setClass(this, SlimOTA.class);
            TabHost.addTab(TabHost.newTabSpec("tab1").setIndicator("SlimOTA").setContent(intent));
            intent=new Intent().setClass(this, SlimSizer.class);
            TabHost.addTab(TabHost.newTabSpec("tab2").setIndicator("SlimSizer").setContent(intent));
            TabHost.setCurrentTab(0);
    }
}
