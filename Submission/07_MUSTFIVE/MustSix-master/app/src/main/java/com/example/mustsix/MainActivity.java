package com.example.mustsix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mustsix.navigation.MyDogsFragment;
import com.example.mustsix.navigation.CardsFragment;
import com.example.mustsix.navigation.HomeFragment;
import com.example.mustsix.navigation.LiveFeedFragment;
import com.example.mustsix.navigation.MyHavenFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    HomeFragment homeFragment = new HomeFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_myhaven:
                MyHavenFragment myHavenFragment = new MyHavenFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, myHavenFragment).commit();
                return true;
            case R.id.action_livefeed:
                LiveFeedFragment liveFeedFragment = new LiveFeedFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, liveFeedFragment).commit();
                return true;
            case R.id.action_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, homeFragment).commit();
                return true;
            case R.id.action_mydogs:
                MyDogsFragment myDogsFragment = new MyDogsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, myDogsFragment).commit();
                return true;
            case R.id.action_cards:
                CardsFragment cardsFragment = new CardsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, cardsFragment).commit();
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, homeFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

}
