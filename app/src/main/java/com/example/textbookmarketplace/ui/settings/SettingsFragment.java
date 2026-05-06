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
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.databinding.FragmentSettingsBinding;
import com.example.textbookmarketplace.util.ThemeHelper;
import com.example.textbookmarketplace.viewmodel.ThemeViewModel;

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

        themeViewModel = new ViewModelProvider(requireActivity()).get(ThemeViewModel.class);

        binding.themeRadioGroup.setOnCheckedChangeListener((@NonNull RadioGroup group, int checkedId) -> {
            if (checkedId == R.id.radioLight) {
                ThemeHelper.applyTheme(ThemeHelper.LIGHT);
                themeViewModel.setThemeMode(ThemeHelper.LIGHT);
            } else if (checkedId == R.id.radioDark) {
                ThemeHelper.applyTheme(ThemeHelper.DARK);
                themeViewModel.setThemeMode(ThemeHelper.DARK);
            } else if (checkedId == R.id.radioSystem) {
                ThemeHelper.applyTheme(ThemeHelper.SYSTEM);
                themeViewModel.setThemeMode(ThemeHelper.SYSTEM);
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Settings saved", Toast.LENGTH_SHORT).show();
        });

        binding.btnLogout.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}