package com.project.adverstir.ui.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.adverstir.R;

import com.project.adverstir.utils.Constants;

public class OnboardingStateAdapter extends FragmentPagerAdapter {

    public OnboardingStateAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        StoryFragment frag = new StoryFragment();
        Bundle bundle = new Bundle();
//        Log.e("STATE","page number "+position);
        Constants.pageNumber = position;
        if (position==0) {
            bundle.putInt("image", R.drawable.new_on1);
            bundle.putInt("message", R.string.story1);
            bundle.putInt("title", R.string.storyTitle1);
        }
        else if (position==1) {
            bundle.putInt("image", R.drawable.new_on2);
            bundle.putInt("message", R.string.story2);
            bundle.putInt("title", R.string.storyTitle2);
        }
        else if (position==2) {
            bundle.putInt("image", R.drawable.new_on3);
            bundle.putInt("message", R.string.story3);
            bundle.putInt("title", R.string.storyTitle3);
        }
        else if (position==3) {
            bundle.putInt("image", R.drawable.new_on4);
            bundle.putInt("message", R.string.story4);
            bundle.putInt("title", R.string.storyTitle4);
        }
        bundle.putInt("pos",position);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
