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
 *  DESCRIPTION: SlimSizer: manage your apps
 *
 *=========================================================================
 */
package com.slim.sizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.slim.ota.R;

public class SlimSizer extends Activity {
    private final int STARTUP_DIALOG = 1;
    private final int DELETE_DIALOG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slim_sizer);
        final Button delButton = (Button) findViewById(R.id.btn_delete);
        final Button profileButton = (Button) findViewById(R.id.btn_profile);

        // create arraylist of apps not to be removed
        final ArrayList<String> safetyList = new ArrayList<String>();
        safetyList.add("BackupRestoreConfirmation.apk");
        safetyList.add("CertInstaller.apk");
        safetyList.add("Contacts.apk");
        safetyList.add("ContactsProvider.apk");
        safetyList.add("DefaultContainerService.apk");
        safetyList.add("DownloadProvider.apk");
        safetyList.add("DrmProvider.apk");
        safetyList.add("MediaProvider.apk");
        safetyList.add("Mms.apk");
        safetyList.add("PackageInstaller.apk");
        safetyList.add("Phone.apk");
        safetyList.add("Settings.apk");
        safetyList.add("SettingsProvider.apk");
        safetyList.add("Superuser.apk");
        safetyList.add("SystemUI.apk");
        safetyList.add("TelephonyProvider.apk");

        // create arraylist from /system/app content
        final String path = "/system/app";
        File system = new File(path);
        String[] sysappArray = system.list();
        final ArrayList<String> sysApp = new ArrayList<String>(
                Arrays.asList(sysappArray));

        // remove "apps not to be removed" from list and sort list
        sysApp.removeAll(safetyList);
        Collections.sort(sysApp);

        // populate listview via arrayadapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, sysApp);

        // startup dialog
        showDialog(STARTUP_DIALOG, null, adapter);

        final ListView lv = (ListView) findViewById(R.string.listsystem);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(adapter);

        // longclick an entry
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    final int arg2, long arg3) {
                // create deletion dialog
                String item = lv.getAdapter().getItem(arg2).toString();
                showDialog(DELETE_DIALOG, item, adapter);
                return false;
            }
        });
        // click button delete
        delButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // check which items are selected
                String item = null;
                int len = lv.getCount();
                SparseBooleanArray checked = lv.getCheckedItemPositions();
                for (int i = len - 1; i > 0; i--) {
                    if (checked.get(i)) {
                        item = sysApp.get(i);
                    }
                }
                if (item == null) {
                    toast(getResources().getString(
                            R.string.sizer_message_noselect));
                    return;
                } else {
                    showDialog(DELETE_DIALOG, item, adapter);
                }
                for (int i = len - 1; i > 0; i--) {
                    if (checked.get(i)) {
                        item = sysApp.get(i);
                            // call delete
                            boolean successDel = delete(item);
                            if (successDel == true) {
                                // remove list entry
                                lv.setItemChecked(i, false);
                                adapter.remove(item);
                                adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        // click button profile
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call select dialog
                selectDialog(sysApp, adapter);
            }
        });
    }

    private void showDialog(int id, final String item,
            final ArrayAdapter<String> adapter) {
        // startup dialog
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        if (id == STARTUP_DIALOG) {
            // create warning dialog
            alert.setMessage(R.string.sizer_message_startup)
                    .setTitle(R.string.caution)
                    .setCancelable(true)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int id) {
                                    // action for ok
                                    try {
                                        Runtime.getRuntime().exec("su");
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    dialog.cancel();
                                }
                            });
            // delete dialog
        } else if (id == DELETE_DIALOG) {
            alert.setMessage(R.string.sizer_message_delete)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int id) {
                                    // action for ok
                                    // call delete
                                    boolean successDel = delete(item);
                                    if (successDel == true) {
                                        // remove list entry
                                        adapter.remove(item);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                        int id) {
                                    // action for cancel
                                    dialog.cancel();
                                }
                            });
        }
        // show warning dialog
        alert.show();
    }

    // profile select dialog
    private void selectDialog(final ArrayList<String> sysAppProfile,
            final ArrayAdapter<String> adapter) {
        AlertDialog.Builder select = new AlertDialog.Builder(this);
        select.setItems(R.array.slimsizer_profile_array,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        short state = sdAvailable();
                        File path = new File(Environment
                                .getExternalStorageDirectory() + "/Slim");
                        File savefile = new File(path + "/slimsizer.stf");
                        if (which == 0) {
                            // load profile action
                            if (state >= 1) {
                                String profile;
                                try {
                                    // read savefile and create arraylist
                                    profile = new Scanner(savefile, "UTF-8")
                                            .useDelimiter("\\A").next();
                                    ArrayList<String> profileState = new ArrayList<String>(
                                            Arrays.asList(profile.split(", ")));
                                    // create arraylist of unique entries in
                                    // sysAppProfile (currently installed apps)
                                    ArrayList<String> deleteList = new ArrayList<String>();
                                    for (String item : sysAppProfile) {
                                        if (!profileState.contains(item)) {
                                            deleteList.add(item);
                                        }
                                    }
                                    // delete all entries in deleteList
                                    int len = deleteList.size();
                                    for (int i = len - 1; i > 0; i--) {
                                        String item = deleteList.get(i);
                                        // call delete
                                        boolean successDel = delete(item);
                                        if (successDel == true) {
                                            // remove list entry
                                            adapter.remove(item);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else {
                                toast(getResources().getString(
                                        R.string.sizer_message_sdnoread));
                            }
                        } else if (which == 1) {
                            // save profile action
                            if (state == 2) {
                                try {
                                    // create directory if it doesnt exist
                                    if (!path.exists()) {
                                        path.mkdirs();
                                    }
                                    // create string from arraylists
                                    String lists = sysAppProfile.toString();
                                    lists = lists.replace("][", ",");
                                    lists = lists.replace("[", "");
                                    lists = lists.replace("]", "");
                                    // delete savefile if it exists (overwrite)
                                    if (savefile.exists()) {
                                        savefile.delete();
                                    }
                                    // create savefile and output lists to it
                                    FileWriter outstream = new FileWriter(
                                            savefile);
                                    BufferedWriter save = new BufferedWriter(
                                            outstream);
                                    save.write(lists);
                                    save.close();
                                    // check for success
                                    if (savefile.exists()) {
                                        toast(getResources()
                                                .getString(
                                                        R.string.sizer_message_filesuccess));
                                    } else {
                                        toast(getResources()
                                                .getString(
                                                        R.string.sizer_message_filefail));
                                    }
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else {
                                toast(getResources().getString(
                                        R.string.sizer_message_sdnowrite));
                            }
                        }
                    }
                });
        select.show();
    }

    public void toast(String text) {
        // easy toasts for all!
        Toast toast = Toast.makeText(getApplicationContext(), text,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private short sdAvailable() {
        // check if sdcard is available
        // taken from developer.android.com
        short mExternalStorageAvailable = 0;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = 2;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = 1;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = 0;
        }
        return mExternalStorageAvailable;
    }

    private boolean delete(String appname) {
        String item = appname;
        final String path = "/system/app";
        File app = new File(path + "/" + item);

        try {
            // check for rw permissions
            if (app.canWrite() == true) {
                Runtime.getRuntime().exec("su && rm -rf " + app);
                Thread.sleep(1500);
            } else {
                Runtime.getRuntime().exec(
                        "su && mount -o remount,rw /system && rm -rf " + app);
                Thread.sleep(1500);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // check if app was deleted
        if (app.exists() == true) {
            toast(getResources().getString(R.string.delete_fail) + item);
            return false;
        } else {
            toast(getResources().getString(R.string.delete_success) + item);
            return true;
        }
    }

    // mount /system as ro on close
    protected void onStop(Bundle savedInstanceState) throws IOException {
        Runtime.getRuntime().exec("su && mount -o remount,ro /system");
    }
}