package com.example.textbookmarketplace.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.textbookmarketplace.util.ThemeHelper;

public class ThemeViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> themeMode;

    public ThemeViewModel(@NonNull Application application) {
        super(application);
        themeMode = new MutableLiveData<>(ThemeHelper.SYSTEM);
    }

    public LiveData<Integer> getThemeMode() {
        return themeMode;
    }

    public void setThemeMode(int mode) {
        themeMode.setValue(mode);
    }
}