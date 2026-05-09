package com.example.textbookmarketplace.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.example.textbookmarketplace.BuildConfig;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME = "color_mode";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupVersion();
        loadCurrentTheme();
        setupThemeToggle();
        setupActions();
    }

    private void setupVersion() {
        String version = String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME);
        binding.tvVersion.setText(version);
    }

    private void loadCurrentTheme() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, requireActivity().MODE_PRIVATE);
        String currentTheme = prefs.getString(KEY_THEME, "system");

        switch (currentTheme) {
            case "light":
                binding.themeLight.setChecked(true);
                break;
            case "dark":
                binding.themeDark.setChecked(true);
                break;
            default:
                binding.themeSystem.setChecked(true);
                break;
        }
    }

    private void setupThemeToggle() {
        View.OnClickListener themeClickListener = v -> {
            String selectedTheme;
            int id = v.getId();
            
            if (id == R.id.themeLight) {
                selectedTheme = "light";
            } else if (id == R.id.themeDark) {
                selectedTheme = "dark";
            } else {
                selectedTheme = "system";
            }

            SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, requireActivity().MODE_PRIVATE);
            String currentTheme = prefs.getString(KEY_THEME, "system");

            if (!selectedTheme.equals(currentTheme)) {
                prefs.edit().putString(KEY_THEME, selectedTheme).apply();
                applyTheme(selectedTheme);
                Toast.makeText(getContext(), "Theme saved: " + selectedTheme, Toast.LENGTH_SHORT).show();
            }
        };

        binding.themeLight.setOnClickListener(themeClickListener);
        binding.themeDark.setOnClickListener(themeClickListener);
        binding.themeSystem.setOnClickListener(themeClickListener);
    }

    private void applyTheme(String mode) {
        int nightMode;
        switch (mode) {
            case "light":
                nightMode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case "dark":
                nightMode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            default:
                nightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                break;
        }
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    private void setupActions() {
        binding.btnClearCache.setOnClickListener(v -> {
            // Clear cache logic
            requireActivity().getCacheDir().delete();
            Toast.makeText(getContext(), "Cache cleared", Toast.LENGTH_SHORT).show();
        });

        binding.btnPrivacy.setOnClickListener(v -> {
            // TODO: Open privacy policy URL
            Toast.makeText(getContext(), "Privacy Policy coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}