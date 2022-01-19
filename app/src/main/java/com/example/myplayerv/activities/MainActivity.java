package com.example.myplayerv.activities;

import static com.example.myplayerv.activities.AllowAccessActivity.REQUEST_PERMISSION_SETTING;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myplayerv.AppConstant;
import com.example.myplayerv.R;
import com.example.myplayerv.adapters.ParentFragmentPagerAdapter;
import com.example.myplayerv.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ParentFragmentPagerAdapter myPageAdapter;
    private ActivityMainBinding binding;
    private FragmentRefreshListener fragmentRefreshListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Click on permission", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
        }

        myPageAdapter = new ParentFragmentPagerAdapter(this);
        binding.viewPager.setAdapter(myPageAdapter);
        new TabLayoutMediator(binding.bottomTab, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.ic_home_video);
                    break;
                case 1:
                    tab.setIcon(R.drawable.ic_home_folder);
                    break;
                case 2:
                    tab.setIcon(R.drawable.ic_starss);
                    break;
                case 3:
                    tab.setIcon(R.drawable.ic_home_playlist);
                    break;
            }
        }).attach();
        binding.viewPager.registerOnPageChangeCallback(pageListener);
        //
        //
    }

    private final ViewPager2.OnPageChangeCallback pageListener = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    binding.tvTitle.setText(R.string.video);
                    binding.ivMenuFolder.setVisibility(View.GONE);
                    binding.ivMenuVideo.setVisibility(View.VISIBLE);
                    SharedPreferences preferences = getSharedPreferences(AppConstant.MY_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    binding.ivMenuVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu pm = new PopupMenu(MainActivity.this, v);
                            pm.getMenuInflater().inflate(R.menu.video_menu, pm.getMenu());
                            pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.sortName:
                                            editor.putString("sort", "sortName");
                                            editor.apply();
                                            if(getFragmentRefreshListener()!= null){
                                                getFragmentRefreshListener().onRefresh();
                                            }
                                            break;
                                        case R.id.sortSize:
                                            editor.putString("sort", "sortSize");
                                            editor.apply();
                                            if(getFragmentRefreshListener()!= null){
                                                getFragmentRefreshListener().onRefresh();
                                            }
                                            break;
                                        case R.id.sortDate:
                                            editor.putString("sort", "sortDate");
                                            editor.apply();
                                            if(getFragmentRefreshListener()!= null){
                                                getFragmentRefreshListener().onRefresh();
                                            }
                                            break;
                                        case R.id.sortLength:
                                            editor.putString("sort", "sortLength");
                                            editor.apply();
                                            if(getFragmentRefreshListener()!= null){
                                                getFragmentRefreshListener().onRefresh();
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                    return true;
                                }
                            });
                            pm.show();
                        }
                    });
                    break;


                case 1:
                    binding.tvTitle.setText("Folder");
                    binding.ivMenuFolder.setVisibility(View.VISIBLE);
                    binding.ivMenuVideo.setVisibility(View.GONE);
                    binding.ivMenuFolder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu pm = new PopupMenu(MainActivity.this, v);
                            pm.getMenuInflater().inflate(R.menu.folder_menu, pm.getMenu());
                            pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.rateus:
                                            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id="
                                                    + getApplicationContext().getPackageName());
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                            break;
                                        case R.id.refresh:
//                                            finish();
//                                            startActivity(getIntent());
                                            break;
                                        case R.id.share_app:
                                            Intent share_intent = new Intent();
                                            share_intent.setAction(Intent.ACTION_SEND);
                                            share_intent.putExtra(Intent.EXTRA_TEXT, "CHECK THIS APP VIA\n"
                                                    + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                                            share_intent.setType("text/plain");
                                            startActivity(Intent.createChooser(share_intent, "Share app via"));
                                            break;
                                        default:
                                            break;
                                    }
                                    return true;
                                }
                            });
                            pm.show();
                        }
                    });
                    break;
                case 2:
                    binding.tvTitle.setText("Star");
                    binding.ivMenuFolder.setVisibility(View.GONE);
                    binding.ivMenuVideo.setVisibility(View.GONE);
                    break;
                case 3:
                    binding.tvTitle.setText(R.string.playlist);
                    binding.ivMenuFolder.setVisibility(View.GONE);
                    binding.ivMenuVideo.setVisibility(View.GONE);
                    break;
            }
            super.onPageSelected(position);
        }
    };
    //
    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public interface FragmentRefreshListener{
        void onRefresh();
    }
    //

}