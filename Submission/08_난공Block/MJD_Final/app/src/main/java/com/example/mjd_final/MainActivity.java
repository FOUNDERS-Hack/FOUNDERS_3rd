package com.example.mjd_final;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mjd_final.contract.IntroContract;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private MapFragment mapFragment = new MapFragment();
    private ReviewFragment reviewFragment = new ReviewFragment();
    private AccountFragment accountFragment = new AccountFragment();

    private static final String TAG = MainActivity.class.getSimpleName();
    private IntroContract.Presenter contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout,mapFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()){
                    case R.id.navigation_map:{
                        transaction.replace(R.id.frame_layout,
                                mapFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_reviews:{
                        transaction.replace(R.id.frame_layout,
                                reviewFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_account:{
                        transaction.replace(R.id.frame_layout,
                                accountFragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }
}
