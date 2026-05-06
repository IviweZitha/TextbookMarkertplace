package com.example.textbookmarketplace.ui.mylistings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.adapter.MyListingAdapter;
import com.example.textbookmarketplace.databinding.FragmentMyListingsBinding;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.viewmodel.BookViewModel;
import java.util.List;

public class MyListingsFragment extends Fragment {

    private FragmentMyListingsBinding binding;
    private BookViewModel viewModel;
    private MyListingAdapter adapter;
    private String currentUserId = "demo_user_123";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyListingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);
        setupRecyclerView();
        observeListings();
    }

    private void setupRecyclerView() {
        adapter = new MyListingAdapter(
                book -> {
                    Bundle args = new Bundle();
                    args.putString("edit_book_id", book.getId());
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_mylistings_to_add, args);
                },
                book -> showDeleteConfirmation(book)
        );
        binding.recyclerMyListings.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerMyListings.setAdapter(adapter);
    }

    private void observeListings() {
        viewModel.getUserListings(currentUserId).observe(getViewLifecycleOwner(), listings -> {
            if (listings == null || listings.isEmpty()) {
                showEmptyState();
            } else {
                hideEmptyState();
                adapter.submitList(listings);
            }
        });
    }

    private void showDeleteConfirmation(Textbook book) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_msg)
                .setPositiveButton(R.string.btn_confirm, (d, w) -> viewModel.deleteById(book.getId()))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showEmptyState() {
        binding.recyclerMyListings.setVisibility(View.GONE);
        if (binding.emptyState != null) {
            binding.emptyState.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyState() {
        binding.recyclerMyListings.setVisibility(View.VISIBLE);
        if (binding.emptyState != null) {
            binding.emptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}