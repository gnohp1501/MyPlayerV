package com.example.myplayerv.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myplayerv.fragments.PlaylistTab;
import com.example.myplayerv.fragments.NewFavoritesTab;
import com.example.myplayerv.fragments.VideoFolderListTab;
import com.example.myplayerv.fragments.VideoListTab;

public class ParentFragmentPagerAdapter extends FragmentStateAdapter {
    public ParentFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new VideoListTab();
            case 1:
                return new VideoFolderListTab();
            case 2:
                return new NewFavoritesTab();
            case 3:
                return new PlaylistTab();
            default:
                return new VideoListTab();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
