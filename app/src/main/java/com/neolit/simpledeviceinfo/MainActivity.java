/*
 * Copyright (C) 2023 Sarthak Roy
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.neolit.simpledeviceinfo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("It is a simple Android app created by Sarthak Roy to display few device information.");
            builder.setTitle("About the App");
            builder.setNegativeButton("Done", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to exit the app?");
        builder.setTitle("Warning!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, which) -> finish());
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long totalMemory = memoryInfo.totalMem/(1024*1024);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level * 100 / (float)scale;

        listView= findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.lists, arrayList);
        listView.setAdapter(adapter);

        arrayList.add("Device Manufacturer: "+ Build.MANUFACTURER);
        arrayList.add("Device Model: "+ Build.MODEL);
        arrayList.add("Hardware: "+ Build.HARDWARE);
        if(!Objects.equals(Build.BOOTLOADER, "unknown")) {
            arrayList.add("Bootloader: " + Build.BOOTLOADER);
        }
        if(!Objects.equals(Build.BOARD, "unknown")) {
            arrayList.add("Board: " + Build.BOARD);
        }
        if(Build.VERSION.SDK_INT >= 31) {
            if (!Objects.equals(Build.SOC_MANUFACTURER, "unknown")){
                arrayList.add("SoC Manufacturer: " + Build.SOC_MANUFACTURER);
            }
            if (!Objects.equals(Build.SOC_MODEL, "unknown")) {
                arrayList.add("SoC Model: " + Build.SOC_MODEL);
            }
        }
        arrayList.add("Android version: Android "+ Build.VERSION.RELEASE);
        arrayList.add("Security Patch: "+ (Build.VERSION.SECURITY_PATCH));
        arrayList.add("SDK version: "+ Build.VERSION.SDK_INT);
        arrayList.add("Build Fingerprint: "+ Build.FINGERPRINT);
        arrayList.add("Build ID: "+ Build.ID);
        arrayList.add("Build Type: "+ Build.TYPE);
        arrayList.add("RAM: "+ totalMemory + "MB");
        arrayList.add("Battery Status: "+ (int)batteryPct + "%");
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            int i;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                i=i+1;
                if (i>=5 && i%10==0) {
                    Toast.makeText(MainActivity.this, "Don't be a fool to tap continuously!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

}
