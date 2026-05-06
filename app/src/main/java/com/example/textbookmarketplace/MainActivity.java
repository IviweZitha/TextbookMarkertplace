package com.example.textbookmarketplace;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.textbookmarketplace.util.ThemeHelper;
import com.example.textbookmarketplace.viewmodel.ThemeViewModel;

public class MainActivity extends AppCompatActivity {

    private ThemeViewModel themeViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        themeViewModel = new ViewModelProvider(this).get(ThemeViewModel.class);
        themeViewModel.getThemeMode().observe(this, ThemeHelper::applyTheme);

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHost != null) {
            NavController navController = navHost.getNavController();
            BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
            NavigationUI.setupWithNavController(bottomNav, navController);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Integer mode = themeViewModel.getThemeMode().getValue();
        if (mode != null) {
            ThemeHelper.applyTheme(mode);
        }
    }
}