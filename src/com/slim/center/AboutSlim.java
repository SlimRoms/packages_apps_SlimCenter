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
import android.widget.TextView;
import android.widget.Toast;

public class AboutSlim extends Fragment{

    private static Intent IRC_INTENT = new Intent(Intent.ACTION_VIEW, Uri.parse("ccircslim:1"));

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slim_about, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //set TextViews
        TextView website = (TextView) getView().findViewById(R.id.slim_website);
        TextView websiteSum = (TextView) getView().findViewById(R.id.slim_website_sum);
        TextView source = (TextView) getView().findViewById(R.id.slim_source);
        TextView sourceSum = (TextView) getView().findViewById(R.id.slim_source_sum);
        TextView donate = (TextView) getView().findViewById(R.id.donate);
        TextView donateSum = (TextView) getView().findViewById(R.id.donate_sum);
        TextView irc = (TextView) getView().findViewById(R.id.irc);
        TextView ircSum = (TextView) getView().findViewById(R.id.irc_sum);
        //set onClickListener on every TextView
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUrl("http://slimroms.net/");
            }
        });
        websiteSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUrl("http://slimroms.net/");
            }
        });
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUrl("http://github.com/SlimRoms");
            }
        });
        sourceSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUrl("http://github.com/SlimRoms");
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUrl("http://www.slimroms.net/index.php/donations");
            }
        });
        donateSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUrl("http://www.slimroms.net/index.php/donations");
            }
        });
        irc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCallable(IRC_INTENT)){
                    startActivity(IRC_INTENT);
                } else {
                    toast(getResources().getString(R.string.no_irc));
                }
            }
        });
        ircSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCallable(IRC_INTENT)){
                    startActivity(IRC_INTENT);
                } else {
                    toast(getResources().getString(R.string.no_irc));
                }
            }
        });
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
