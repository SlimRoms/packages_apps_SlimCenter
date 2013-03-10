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

package com.slim.ota;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceFragment;
import android.text.style.ForegroundColorSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.Toast;

import com.slim.ota.updater.UpdateChecker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ShortCutFragment extends PreferenceFragment {
    private static final String TAG = "ShortCutFragment";

    private static final String KEY_DOWNLOAD = "short_cut_download";
    private static final String KEY_DOWNLOAD_GAPPS = "short_cut_download_gapps";

    private static final String KEY_CHANGELOG = "short_cut_changelog";
    private static final String KEY_FAQ = "short_cut_faq";
    private static final String KEY_NEWS = "short_cut_news";

    private Preference mDownload;
    private Preference mDownloadGapps;
    private Preference mChangelog;
    private Preference mFAQ;
    private Preference mNews;

    private String mStrFileNameNew;
    private String mStrFileURLNew;
    private String mStrCurFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createCustomView();
    }

    private PreferenceScreen createCustomView() {
        PreferenceScreen prefs = getPreferenceScreen();
        if (prefs != null) {
            prefs.removeAll();
        }

        addPreferencesFromResource(R.xml.short_cut_fragment);
        prefs = getPreferenceScreen();

        mDownload = (Preference) findPreference(KEY_DOWNLOAD);
        mDownloadGapps = (Preference) findPreference(KEY_DOWNLOAD_GAPPS);
        mChangelog = (Preference) findPreference(KEY_CHANGELOG);
        mFAQ = (Preference) findPreference(KEY_FAQ);
        mNews = (Preference) findPreference(KEY_NEWS);

        try {
            FileInputStream fstream = new FileInputStream("/system/build.prop");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String[] line = strLine.split("=");
                if (line[0].equals("ro.modversion")) {
                    mStrCurFile = line[1];
                }
            }
            in.close();
        } catch (Exception e) {
            Toast.makeText(getActivity().getBaseContext(), getString(R.string.system_prop_error),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        SharedPreferences shPrefs = getActivity().getSharedPreferences("UpdateChecker", 0);
        mStrFileNameNew = shPrefs.getString("Filename", "");
        mStrFileURLNew = shPrefs.getString("DownloadUrl", "");

        updateView();

        return prefs;
     }

    public void updateView() {
        if (!UpdateChecker.connectivityAvailable(getActivity().getBaseContext()) || mStrFileNameNew.equals("")) {
            mDownload.setEnabled(false);
            mDownloadGapps.setEnabled(false);
            mChangelog.setEnabled(false);
            mFAQ.setEnabled(false);
            mNews.setEnabled(false);
        } else if (mStrFileNameNew.compareToIgnoreCase(mStrCurFile)<=0) {
            mDownload.setEnabled(true);
            mDownloadGapps.setEnabled(true);
            mChangelog.setEnabled(true);
            mFAQ.setEnabled(true);
            mNews.setEnabled(true);
        } else {
            mDownload.setEnabled(true);
            mDownloadGapps.setEnabled(true);
            mChangelog.setEnabled(true);
            mFAQ.setEnabled(true);
            mNews.setEnabled(true);
            mChangelog.setSummary(getString(R.string.short_cut_changelog_summary_update_available));
            mDownload.setSummary(getString(R.string.short_cut_download_summary_update_available));
            setPreferenceColor(mDownload, Color.GREEN);
            setPreferenceColor(mChangelog, Color.GREEN);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mChangelog) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.changelog_url)));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } else if (preference == mDownload) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mStrFileURLNew));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } else if (preference == mDownloadGapps) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.gapps_url)));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } else if (preference == mFAQ) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.faq_url)));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } else if (preference == mNews) {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.news_url)));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public void setPreferenceColor(Preference preference, int color) {
        Spannable textToColor = new SpannableString(preference.getSummary());
        textToColor.setSpan( new ForegroundColorSpan(color), 0, textToColor.length(), 0 );
        preference.setSummary(textToColor);
        textToColor = new SpannableString(preference.getTitle());
        textToColor.setSpan( new ForegroundColorSpan(color), 0, textToColor.length(), 0 );
        preference.setTitle(textToColor);
    }
}
