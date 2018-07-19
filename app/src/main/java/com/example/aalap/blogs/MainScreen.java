package com.example.aalap.blogs;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.aalap.blogs.TabFragments.FragmentPostItem;
import com.example.aalap.blogs.TabFragments.FragmentSavedItems;
import com.example.aalap.blogs.TabFragments.FragmentSearch;
import com.example.aalap.blogs.TabFragments.FragmentSignout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainScreen extends AppCompatActivity {

    TextView textView;
    private DatabaseReference mDatabase;
    private static final String TAG = "MainScreen:";
    TabAdapter tabPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tablayout);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.DB_REFERENCE);
        Log.d(TAG, "onCreate: " + mDatabase);

        tabPagerAdapter = new TabAdapter(getSupportFragmentManager());
        setupViewPager();
    }

    private void setupViewPager() {
        tabPagerAdapter.addFragment(new FragmentSearch());
        tabPagerAdapter.addFragment(new FragmentSavedItems());
        tabPagerAdapter.addFragment(new FragmentPostItem());
        tabPagerAdapter.addFragment(new FragmentSignout());

        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
