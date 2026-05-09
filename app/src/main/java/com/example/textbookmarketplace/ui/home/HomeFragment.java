package com.example.textbookmarketplace.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.adapter.TextbookAdapter;
import com.example.textbookmarketplace.databinding.FragmentHomeBinding;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.viewmodel.BookViewModel;
import java.util.ArrayList;
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

        setupButtons();
        setupSearch();
        setupFeaturedList();
        observeFeaturedBooks();
    }

    private void setupButtons() {
        // Browse Textbooks Button
        binding.btnBrowse.setOnClickListener(v -> {
            try {
                Navigation.findNavController(v).navigate(R.id.action_home_to_browse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Add Textbook Button
        binding.btnAdd.setOnClickListener(v -> navigateToAdd(v));

        // FAB Add Button
        binding.fabAdd.setOnClickListener(v -> navigateToAdd(v));
    }

    private void navigateToAdd(View v) {
        try {
            Navigation.findNavController(v).navigate(R.id.action_home_to_add);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSearch() {
        binding.searchInput.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) &&
                    (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                String query = binding.searchInput.getText().toString().trim();
                if (!query.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("search_query", query);
                    try {
                        Navigation.findNavController(v).navigate(R.id.action_home_to_browse, bundle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
            return false;
        });
    }

    private void setupFeaturedList() {
        featuredAdapter = new TextbookAdapter(book -> {
            Bundle args = new Bundle();
            args.putString("book_id", book.getId());
            try {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_home_to_detail, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.recyclerFeatured.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerFeatured.setAdapter(featuredAdapter);
    }

    private void observeFeaturedBooks() {
        viewModel.getAllAvailableBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null && !books.isEmpty()) {
                // Show first 10 books as featured
                List<Textbook> featured = new ArrayList<>();
                for (int i = 0; i < Math.min(10, books.size()); i++) {
                    featured.add(books.get(i));
                }
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