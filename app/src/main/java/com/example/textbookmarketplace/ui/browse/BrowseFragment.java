package com.example.textbookmarketplace.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.adapter.TextbookAdapter;
import com.example.textbookmarketplace.databinding.FragmentBrowseBinding;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.viewmodel.BookViewModel;
import java.util.List;

public class BrowseFragment extends Fragment {

    private FragmentBrowseBinding binding;
    private BookViewModel viewModel;
    private TextbookAdapter adapter;
    private String currentCategory = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBrowseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);
        setupRecyclerView();
        setupSearch();
        setupFilters();
        observeData();
    }

    private void setupRecyclerView() {
        adapter = new TextbookAdapter(book -> {
            Bundle args = new Bundle();
            args.putString("book_id", book.getId());
            NavHostFragment.findNavController(BrowseFragment.this)
                    .navigate(R.id.action_browse_to_detail, args);
        });
        if (getContext() != null) {
            binding.recyclerBrowse.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        binding.recyclerBrowse.setAdapter(adapter);
    }

    private void observeData() {
        viewModel.getAllAvailableBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null && !books.isEmpty()) {
                hideEmptyState();
                adapter.submitList(books);
            } else {
                showEmptyState();
            }
        });
    }

    private void setupSearch() {
        binding.searchInput.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.trim().isEmpty()) {
                    viewModel.searchBooks(query.trim()).observe(getViewLifecycleOwner(), results -> {
                        handleSearchResults(results);
                    });
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.trim().isEmpty()) {
                    observeData();
                }
                return true;
            }
        });
    }

    private void handleSearchResults(List<Textbook> results) {
        if (results == null || results.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
            adapter.submitList(results);
        }
    }

    private void showEmptyState() {
        binding.recyclerBrowse.setVisibility(View.GONE);
        if (binding.emptyState != null) {
            binding.emptyState.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyState() {
        binding.recyclerBrowse.setVisibility(View.VISIBLE);
        if (binding.emptyState != null) {
            binding.emptyState.setVisibility(View.GONE);
        }
    }

    private void setupFilters() {
        ChipGroup chipGroup = binding.chipGroup;
        if (chipGroup == null) return;
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            final Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setOnClickListener(v -> {
                if (chip.isChecked()) {
                    currentCategory = String.valueOf(chip.getText());
                    if ("All".equalsIgnoreCase(currentCategory)) {
                        observeData();
                    } else {
                        filterByCategory(currentCategory);
                    }
                }
            });
        }
    }

    private void filterByCategory(String category) {
        viewModel.getBooksByCategory(category).observe(getViewLifecycleOwner(), books -> {
            if (books != null && !books.isEmpty()) {
                hideEmptyState();
                adapter.submitList(books);
            } else {
                showEmptyState();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}