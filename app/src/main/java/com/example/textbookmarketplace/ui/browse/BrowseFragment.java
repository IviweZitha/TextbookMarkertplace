package com.example.textbookmarketplace.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.adapter.TextbookAdapter;
import com.example.textbookmarketplace.databinding.FragmentBrowseBinding;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.util.GmailIntentHelper;
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
        adapter = new TextbookAdapter(new TextbookAdapter.OnBookClickListener() {
            @Override
            public void onBookClick(Textbook book) {
                Bundle args = new Bundle();
                args.putString("book_id", book.getId());
                NavHostFragment.findNavController(BrowseFragment.this)
                        .navigate(R.id.action_browse_to_detail, args);
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
                viewModel.searchBooks(query).observe(getViewLifecycleOwner(), new Observer<List<Textbook>>() {
                    @Override
                    public void onChanged(List<Textbook> results) {
                        handleSearchResults(results, query);
                    }
                });
            }
            return true;
        });
    }

    private void handleSearchResults(List<Textbook> results, String query) {
        if (results == null || results.isEmpty()) {
            showEmptyState("No results for \"" + query + "\"", query);
        } else {
            hideEmptyState();
            adapter.submitList(results);
        }
    }

    private void setupFilters() {
        ChipGroup chipGroup = binding.chipGroup;
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            final Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chip.isChecked()) {
                        currentCategory = chip.getText().toString();
                        if ("All".equals(currentCategory));}}}}}}