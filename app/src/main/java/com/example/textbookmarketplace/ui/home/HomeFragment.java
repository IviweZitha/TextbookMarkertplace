package com.example.textbookmarketplace.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.adapter.TextbookAdapter;
import com.example.textbookmarketplace.databinding.FragmentHomeBinding;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.viewmodel.BookViewModel;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private BookViewModel viewModel;
    private TextbookAdapter featuredAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        setupSearch();
        setupQuickActions();
        setupFeaturedList();
        observeFeaturedBooks();
    }

    private void setupSearch() {
        binding.searchInput.setOnEditorActionListener((v, actionId, event) -> {
            String query = binding.searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                Bundle args = new Bundle();
                args.putString("search_query", query);
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_home_to_browse, args);
            }
            return true;
        });
    }

    private void setupQuickActions() {
        binding.btnBrowse.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_home_to_browse));

        binding.btnAdd.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_home_to_add));
    }

    private void setupFeaturedList() {
        featuredAdapter = new TextbookAdapter(book -> {
            Bundle args = new Bundle();
            args.putString("book_id", book.getId());
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_home_to_detail, args);
        });

        binding.recyclerFeatured.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerFeatured.setAdapter(featuredAdapter);
    }

    private void observeFeaturedBooks() {
        viewModel.getAllAvailableBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null && !books.isEmpty()) {
                List<Textbook> featured = books.subList(0, Math.min(10, books.size()));
                featuredAdapter.submitList(featured);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}