package com.neolit.simpledeviceinfo;

import static java.lang.System.getProperty;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long totalMemory = memoryInfo.totalMem/(1024*1024);

        listView=(ListView)findViewById(R.id.listView);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
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
        arrayList.add("SDK version: "+ String.valueOf(Build.VERSION.SDK_INT));
        arrayList.add("Build Fingerprint: "+ Build.FINGERPRINT);
        arrayList.add("Build ID: "+ Build.ID);
        arrayList.add("Build Type: "+ Build.TYPE);
        if (!Objects.equals(getProperty("ro.treble.enabled"), "true")) {
            arrayList.add("Project Treble: true");
        }else{
            arrayList.add("Project Treble: false");
        }
        if (Objects.equals(getProperty("ro.virtual_ab.enabled"), "true") && Objects.equals(getProperty("ro.virtual_ab.retrofit"), "false")) {
            arrayList.add("Virtual A/B Partition Scheme: true");
        }
        else if (Objects.equals(getProperty("ro.build.ab_update"), "true")) {
            arrayList.add("A/B Partition Scheme: true");
        }
        else if (Objects.equals(getProperty("ro.virtual_ab.retrofit"), "true")) {
            arrayList.add("Virtual A/B Retrofit Partition Scheme: true");
        }
        else{
            arrayList.add("A-only Partition Scheme: true");
        }
        arrayList.add("RAM: "+ totalMemory + "MB");

        adapter.notifyDataSetChanged();
    }
}