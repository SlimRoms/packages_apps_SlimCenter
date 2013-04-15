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
 *  AUTHORS:     fronti90
 *  DESCRIPTION: SlimCenter: manage your ROM
 *
 *=========================================================================
 */
package com.slim.center;

import com.slim.ota.R;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AboutSlim extends Fragment{

    private LinearLayout website;
    private LinearLayout source;
    private LinearLayout donate;
    private LinearLayout irc;
    private static Intent IRC_INTENT = new Intent(Intent.ACTION_VIEW, Uri.parse("ccircslim:1"));

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slim_about, container, false);
        return view;
    }

    private final View.OnClickListener mActionLayouts = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == website) {
                launchUrl("http://slimroms.net/");
            } else if (v == source) {
                launchUrl("http://github.com/SlimRoms");
            } else if (v == donate) {
                launchUrl("http://www.slimroms.net/index.php/donations");
            } else if (v == irc) {
                if (isCallable(IRC_INTENT)){
                    startActivity(IRC_INTENT);
                } else {
                    toast(getResources().getString(R.string.no_irc));
                }
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set LinearLayouts and onClickListeners

        website = (LinearLayout) getView().findViewById(R.id.slim_website);
        website.setOnClickListener(mActionLayouts);

        source = (LinearLayout) getView().findViewById(R.id.slim_source);
        source.setOnClickListener(mActionLayouts);

        donate = (LinearLayout) getView().findViewById(R.id.slim_donate);
        donate.setOnClickListener(mActionLayouts);

        irc = (LinearLayout) getView().findViewById(R.id.slim_irc);
        irc.setOnClickListener(mActionLayouts);
    }

    private void launchUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent donate = new Intent(Intent.ACTION_VIEW, uriUrl);
        getActivity().startActivity(donate);
    }

    private void toast(String text) {
        // easy toasts for all!
        Toast toast = Toast.makeText(getView().getContext(), text,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
