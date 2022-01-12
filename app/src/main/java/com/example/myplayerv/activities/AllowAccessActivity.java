package com.example.myplayerv.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.myplayerv.R;
import com.example.myplayerv.databinding.ActivityAllowAccessBinding;

import java.lang.ref.PhantomReference;

public class AllowAccessActivity extends AppCompatActivity {

    private ActivityAllowAccessBinding binding;
    private static final int STORAGE_PERMISSION = 1;
    public static final int REQUEST_PERMISSION_SETTING = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllowAccessBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //
        SharedPreferences preferences = getSharedPreferences("AllowAccess",MODE_PRIVATE);
        String value = preferences.getString("Allow","");
        //
        if(value.equals("OK")){
            startActivity(new Intent(AllowAccessActivity.this, MainActivity.class));
            finish();
        }else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Allow","OK");
            editor.apply();
        }
        //
        checkPermission();
    }

    public void checkPermission() {
        binding.btnAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(AllowAccessActivity.this, MainActivity.class));
                    finish();
                }else {
                    ActivityCompat.requestPermissions(AllowAccessActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== STORAGE_PERMISSION){
            for(int i = 0;i<permissions.length;i++){
                String per = permissions[i];
                if(grantResults[i]==PackageManager.PERMISSION_DENIED){
                    boolean showRationale = shouldShowRequestPermissionRationale(per);
                    if(!showRationale){
                        /**
                         * User clicked never ask again
                         */
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("App Permission")
                                .setMessage("For playing video, you must allow access"
                                +"\n\n"+"Now follow the below steps"
                                +"\n\n"+"Open settings from below button"
                                +"\n"+"Click on Permission"
                                +"\n"+"Allow access for storage")
                                .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package",getPackageName(),null);
                                        intent.setData(uri);
                                        startActivityForResult(intent,REQUEST_PERMISSION_SETTING);
                                    }
                                }).create().show();

                    }else {
                        ActivityCompat.requestPermissions(AllowAccessActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION);
                    }
                }else{
                    startActivity(new Intent(AllowAccessActivity.this, MainActivity.class));
                    finish();
                }
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(AllowAccessActivity.this, MainActivity.class));
            finish();
        }
    }
}