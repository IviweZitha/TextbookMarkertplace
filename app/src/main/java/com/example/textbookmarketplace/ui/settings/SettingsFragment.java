package com.example.textbookmarketplace.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.textbookmarketplace.BuildConfig;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.databinding.FragmentSettingsBinding;
import com.example.textbookmarketplace.util.ThemeHelper;
import com.example.textbookmarketplace.viewmodel.ThemeViewModel;
import com.google.firebase.appcheck.BuildConfig;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private ThemeViewModel themeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        themeViewModel = new ViewModelProvider(this).get(ThemeViewModel.class);

        setupVersion();
        setupThemeToggle();
        setupActions();
        observeTheme();
    }

    private void setupVersion() {
        String version = String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME);
        binding.tvVersion.setText(version);
    }

    private void setupThemeToggle() {
        binding.themeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String mode;
                if (checkedId == R.id.theme_system) {
                    mode = "system";
                } else if (checkedId == R.id.theme_light) {
                    mode = "light";
                } else if (checkedId == R.id.theme_dark) {
                    mode = "dark";
                } else {
                    mode = "system";
                }
                themeViewModel.setThemeMode(mode);
            }
        });
    }

    private void setupActions() {
        binding.btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themeViewModel.clearCache();
                Toast.makeText(getContext(), "Cache cleared", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Privacy Policy coming soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeTheme() {
        themeViewModel.getThemeMode().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(String mode) {
                if ("system".equals(mode)) {
                    binding.themeSystem.setChecked(true);
                } else if ("light".equals(mode)) {
                    binding.themeLight.setChecked(true);
                } else if ("dark".equals(mode)) {
                    binding.themeDark.setChecked(true);
                }
                ThemeHelper.animateTransition(requireActivity());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}