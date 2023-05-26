package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.woofer.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class LandingPage extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.add:
                    replaceFragment(new AddFragment());
                    break;

                case R.id.search:
                    replaceFragment(new SearchFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransition = fragmentManager.beginTransaction();
        fragmentTransition.replace(R.id.frame_layout, fragment);
        fragmentTransition.commit();
    }
}