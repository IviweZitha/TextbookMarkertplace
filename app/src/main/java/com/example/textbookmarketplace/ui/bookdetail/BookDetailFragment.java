package com.example.textbookmarketplace.ui.bookdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.textbookmarketplace.databinding.FragmentBookDetailBinding;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.viewmodel.BookViewModel;

public class BookDetailFragment extends Fragment {

    private FragmentBookDetailBinding binding;
    private BookViewModel viewModel;
    private String bookId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        Bundle args = getArguments();
        if (args != null) {
            bookId = args.getString("book_id");
        }

        if (bookId != null) {
            loadBookDetails(bookId);
        }
        setupClickListeners();
    }

    private void loadBookDetails(String id) {
        viewModel.getBookById(id).observe(getViewLifecycleOwner(), book -> {
            if (book != null) {
                populateUI(book);
            }
        });
    }

    private void populateUI(Textbook book) {
        binding.tvTitle.setText(book.getTitle());
        binding.tvAuthor.setText(book.getAuthor());
        binding.tvPrice.setText(String.format("$%.2f", book.getPrice()));
        binding.tvDescription.setText(book.getDescription());
        binding.tvCategory.setText(book.getCategory());
        binding.tvCondition.setText(book.getCondition());
        binding.tvSellerName.setText(book.getSellerName());
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        binding.btnContactSeller.setOnClickListener(v -> {
            // TODO: Open email intent
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}