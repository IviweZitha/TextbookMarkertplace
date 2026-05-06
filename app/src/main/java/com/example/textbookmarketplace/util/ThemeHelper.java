package com.example.textbookmarketplace.util;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {

    public static final int LIGHT = AppCompatDelegate.MODE_NIGHT_NO;
    public static final int DARK = AppCompatDelegate.MODE_NIGHT_YES;
    public static final int SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

    public static void applyTheme(int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
    }
}