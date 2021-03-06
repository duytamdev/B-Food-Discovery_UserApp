package com.fpoly.pro1121.userapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fpoly.pro1121.userapp.fragment.AccountFragment;
import com.fpoly.pro1121.userapp.fragment.CartFragment;
import com.fpoly.pro1121.userapp.fragment.HomeFragment;

public class PagerMainAdapter extends FragmentStateAdapter {
    public PagerMainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new CartFragment();
            case 2:
                return new AccountFragment();
            case 0:
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // 3 fragment
    }
}
