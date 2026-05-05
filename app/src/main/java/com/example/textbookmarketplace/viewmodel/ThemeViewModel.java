package com.example.textbookmarketplace.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ThemeViewModel extends AndroidViewModel {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME = "color_mode";

    private final SharedPreferences prefs;
    private final MutableLiveData<String> themeMode = new MutableLiveData<>();

    public ThemeViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences(PREFS_NAME, Application.MODE_PRIVATE);
        loadTheme();
    }

    private void loadTheme() {
        String mode = prefs.getString(KEY_THEME, "system");
        themeMode.postValue(mode);
    }

    public MutableLiveData<String> getThemeMode() { return themeMode; }

    public void setThemeMode(String mode) {
        prefs.edit().putString(KEY_THEME, mode).apply();
        themeMode.postValue(mode);
    }

    public void clearCache() {
        prefs.edit().clear().apply();
    }
}