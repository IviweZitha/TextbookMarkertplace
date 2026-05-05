package com.example.textbookmarketplace.util;

import android.app.Activity;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    public static void applyTheme(String mode) {
        int nightMode = switch (mode) {
            case "light" -> AppCompatDelegate.MODE_NIGHT_NO;
            case "dark" -> AppCompatDelegate.MODE_NIGHT_YES;
            default -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        };
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    public static void animateTransition(Activity activity) {
        activity.overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
    }
}