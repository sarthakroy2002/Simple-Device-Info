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
import android.content.pm.ConfigurationInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
            builder.setMessage("It is a simple Android app created by Sarthak Roy to display few device information.");
            builder.setTitle("About the App");
            builder.setNegativeButton("Done", (dialog, which) -> dialog.cancel());
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        builder.setMessage("Do you want to exit the app?");
        builder.setTitle("Warning!");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, which) -> finish());
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        builder.show();
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

        ListView listView= findViewById(R.id.listView);
        ArrayList<SysView> arrayList = new ArrayList<>();

        arrayList.add(new SysView("Device Manufacturer", String.valueOf(Build.MANUFACTURER)));
        arrayList.add(new SysView("Device Model", String.valueOf(Build.MODEL)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !Objects.equals(Build.SKU, "unknown")) {
            arrayList.add(new SysView("SKU", Build.SKU));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !Objects.equals(Build.ODM_SKU, "unknown")) {
            arrayList.add(new SysView("ODM", Build.ODM_SKU));
        }
        arrayList.add(new SysView("Hardware", Build.HARDWARE));
        if(!Objects.equals(Build.BOOTLOADER, "unknown")) {
            arrayList.add(new SysView("Bootloader", Build.BOOTLOADER));
        }
        if(!Objects.equals(Build.BOARD, "unknown")) {
            arrayList.add(new SysView("Board", Build.BOARD));
        }
        if(Build.VERSION.SDK_INT >= 31) {
            if (!Objects.equals(Build.SOC_MANUFACTURER, "unknown")){
                arrayList.add(new SysView("SoC Manufacturer", Build.SOC_MANUFACTURER));
            }
            if (!Objects.equals(Build.SOC_MODEL, "unknown")) {
                arrayList.add(new SysView("SoC Model", Build.SOC_MODEL));
            }
        }
        arrayList.add(new SysView("Android version", Build.VERSION.RELEASE));
        arrayList.add(new SysView("Security Patch", Build.VERSION.SECURITY_PATCH));
        arrayList.add(new SysView("SDK version", String.valueOf(Build.VERSION.SDK_INT)));
        arrayList.add(new SysView("Build Fingerprint", Build.FINGERPRINT));
        arrayList.add(new SysView("Build ID", Build.ID));
        arrayList.add(new SysView("Build Type", Build.TYPE));
        arrayList.add(new SysView("Build Host", Build.HOST));
        arrayList.add(new SysView("Build Time", String.valueOf(Build.TIME)));
        arrayList.add(new SysView("Supported ABIs", Arrays.toString(Build.SUPPORTED_ABIS)));
        arrayList.add(new SysView("RAM", totalMemory + "MB"));
        arrayList.add(new SysView("Battery Status", (int)batteryPct + "%"));

        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        arrayList.add(new SysView("OpenGL ES Version", configurationInfo.getGlEsVersion()));

        SysViewAdapter sysViewAdapter = new SysViewAdapter(this, arrayList);
        listView.setDivider(null);
        listView.setSmoothScrollbarEnabled(true);
        listView.setAdapter(sysViewAdapter);

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
