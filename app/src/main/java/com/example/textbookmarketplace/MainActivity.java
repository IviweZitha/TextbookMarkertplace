package com.iviwezitha.textbookmarketplace;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.iviwezitha.textbookmarketplace.util.ThemeHelper;
import com.iviwezitha.textbookmarketplace.viewmodel.ThemeViewModel;

public class MainActivity extends AppCompatActivity {
    private ThemeViewModel themeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup theme
        themeViewModel = new ViewModelProvider(this).get(ThemeViewModel.class);
        themeViewModel.getThemeMode().observe(this, ThemeHelper::applyTheme);

        // Setup navigation
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHost.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-apply theme in case system changed
        themeViewModel.getThemeMode().getValue();
    }
}