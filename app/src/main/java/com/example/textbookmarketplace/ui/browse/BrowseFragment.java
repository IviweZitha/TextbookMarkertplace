package com.example.textbookmarketplace.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.adapter.TextbookAdapter;
import com.example.textbookmarketplace.databinding.FragmentBrowseBinding;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.util.GmailIntentHelper;
import com.example.textbookmarketplace.viewmodel.BookViewModel;
import java.util.ArrayList;
import java.util.List;

public class BrowseFragment extends Fragment {

    private FragmentBrowseBinding binding;
    private BookViewModel viewModel;
    private TextbookAdapter adapter;
    private String currentCategory = null;
    private String currentSearchQuery = null;

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

        // Get search query from arguments (if coming from HomeFragment)
        if (getArguments() != null) {
            currentSearchQuery = getArguments().getString("search_query", null);
            if (currentSearchQuery != null && binding.searchInput != null) {
                binding.searchInput.setText(currentSearchQuery);
            }
        }

        setupRecyclerView();
        setupSearch();
        setupFilters();
        setupFAB();
        observeData();
    }

    private void setupRecyclerView() {
        adapter = new TextbookAdapter(book -> {
            Bundle args = new Bundle();
            args.putString("book_id", book.getId());
            try {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_browse_to_detail, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (getContext() != null) {
            binding.recyclerBrowse.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        binding.recyclerBrowse.setAdapter(adapter);
    }

    private void setupSearch() {
        binding.searchInput.setOnEditorActionListener((v, actionId, event) -> {
            String query = binding.searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                currentSearchQuery = query;
                performSearch(query);
            }
            return true;
        });
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            // If query is empty, show all books
            viewModel.getAllAvailableBooks().observe(getViewLifecycleOwner(), books -> {
                updateList(books);
            });
        } else {
            // Search by title, author, or category
            viewModel.searchBooks(query).observe(getViewLifecycleOwner(), books -> {
                updateList(books);
            });
        }
    }

    private void setupFilters() {
        ChipGroup chipGroup = binding.chipGroup;

        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            final Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setOnClickListener(v -> {
                if (chip.isChecked()) {
                    String selectedChip = chip.getText().toString();

                    if ("All".equals(selectedChip)) {
                        currentCategory = null;
                        // Show all books
                        viewModel.getAllAvailableBooks().observe(getViewLifecycleOwner(), books -> {
                            updateList(books);
                        });
                    } else {
                        currentCategory = selectedChip;
                        // Filter by category
                        viewModel.searchBooks(currentCategory).observe(getViewLifecycleOwner(), books -> {
                            updateList(books);
                        });
                    }
                }
            });
        }
    }

    private void setupFAB() {
        binding.fabAdd.setOnClickListener(v -> {
            try {
                Navigation.findNavController(v)
                        .navigate(R.id.action_browse_to_add);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void observeData() {
        // Initial load - show all books
        viewModel.getAllAvailableBooks().observe(getViewLifecycleOwner(), books -> {
            // If there's a search query from arguments, perform search instead
            if (currentSearchQuery != null && !currentSearchQuery.isEmpty()) {
                performSearch(currentSearchQuery);
            } else {
                updateList(books);
            }
        });
    }

    private void updateList(List<Textbook> books) {
        if (books == null || books.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
            adapter.submitList(books);
        }
    }

    private void showEmptyState() {
        binding.recyclerBrowse.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.VISIBLE);
        binding.progressLoading.setVisibility(View.GONE);

        if (currentSearchQuery != null && !currentSearchQuery.isEmpty()) {
            binding.tvEmptyMsg.setText("No results for \"" + currentSearchQuery + "\"");
            binding.btnWebSearch.setVisibility(View.VISIBLE);
            binding.btnWebSearch.setOnClickListener(v -> {
                GmailIntentHelper.openGoogleBooksSearch(requireContext(), currentSearchQuery);
            });
        } else {
            binding.tvEmptyMsg.setText("No textbooks available");
            binding.btnWebSearch.setVisibility(View.GONE);
        }
    }

    private void hideEmptyState() {
        binding.recyclerBrowse.setVisibility(View.VISIBLE);
        binding.emptyState.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}