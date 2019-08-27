package com.bibinetalkole.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bibinetalkole.app.fragments.FragmentGlobal;
import com.bibinetalkole.app.fragments.FragmentShow;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;

    public boolean isFirstStart;
    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Intro App Initialize SharedPreferences
                SharedPreferences getSharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                isFirstStart = getSharedPreferences.getBoolean("firstStart", true);

                //  Check either activity or app is open very first time or not and do action
                if (isFirstStart) {

                    //  Launch application introduction screen
                    Intent i = new Intent(MainActivity.this, MyIntro.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getSharedPreferences.edit();
                    e.putBoolean("firstStart", false);
                    e.apply();
                }
            }
        });
        t.start();

        //Bottom Navigation Views
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.inflateMenu(R.menu.menu_bottom_navigation);

        fragmentManager = getSupportFragmentManager();
        // define your fragments here
        final Fragment fragment1 = new FragmentGlobal();
        final Fragment fragmentShow = new FragmentShow();
        final Fragment fragment3 = new FragmentGlobal();

        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_feeds:
                        fragment = fragment1;
                        Toast.makeText(MainActivity.this, "Feeds", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_shows:
                        fragment = fragmentShow;
                        Toast.makeText(MainActivity.this, "Shows -2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_about:
                        fragment = fragment3;
                        Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_albums:
                        fragment = fragment3;
                        Toast.makeText(MainActivity.this, "Albums", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_search:
                        fragment = fragment3;
                        Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                        break;
                    default: return true;
                }

                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_shows);
    }
}
