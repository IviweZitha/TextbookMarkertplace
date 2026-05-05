package com.example.textbookmarketplace.ui.mylistings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyListingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserId = "demo_user_123"; // Replace with FirebaseAuth.getInstance().getCurrentUser().getUid()

        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        setupRecyclerView();
        setupSwipeToDelete();
        observeListings();
        setupEmptyState();
    }

    private void setupRecyclerView() {
        adapter = new MyListingAdapter(new MyListingAdapter.OnActionListener() {
            @Override public void onEdit(Textbook book) {
                Bundle args = new Bundle();
                args.putString("edit_book_id", book.getId());
                NavHostFragment.findNavController(MyListingsFragment.this)
                        .navigate(R.id.action_myListings_to_add, args);
            }
            @Override public void onDelete(Textbook book) { showDeleteConfirmation(book); }
            @Override public void onBookClick(Textbook book) {
                Bundle args = new Bundle();
                args.putString("book_id", book.getId());
                NavHostFragment.findNavController(MyListingsFragment.this)
                        .navigate(R.id.action_myListings_to_detail, args);
            }
        });

        binding.recyclerMyListings.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerMyListings.setAdapter(adapter);
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override public boolean onMove(@NonNull RecyclerView recyclerView,
                                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                                    @NonNull RecyclerView.ViewHolder target) { return false; }
                    @Override public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Textbook book = adapter.getCurrentList().get(position);
                        showDeleteConfirmation(book);
                    }
                };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.recyclerMyListings);
    }

    private void showDeleteConfirmation(Textbook book) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_msg)
                .setPositiveButton(R.string.btn_confirm, (d, w) -> viewModel.deleteById(book.getId()))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void observeListings() {
        viewModel.getUserListings(currentUserId).observe(getViewLifecycleOwner(), listings -> {
            if (listings == null || listings.isEmpty()) { showEmptyState(); }
            else { hideEmptyState(); adapter.submitList(listings); }
        });
    }

    private void setupEmptyState() {
        binding.btnAddFirst.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_myListings_to_add));
    }

    private void showEmptyState() {
        binding.recyclerMyListings.setVisibility(View.GONE);
        binding.emptyState.setVisibility(View.VISIBLE);
    }

    private void hideEmptyState() {
        binding.recyclerMyListings.setVisibility(View.VISIBLE);
        binding.emptyState.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}