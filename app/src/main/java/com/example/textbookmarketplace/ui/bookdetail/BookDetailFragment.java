package com.example.textbookmarketplace.ui.bookdetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.databinding.FragmentBookDetailBinding;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.util.GmailIntentHelper;
import com.example.textbookmarketplace.viewmodel.BookViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookDetailFragment extends Fragment {
    private FragmentBookDetailBinding binding;
    private BookViewModel viewModel;
    private Textbook currentBook;

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

        String bookId = BookDetailFragmentArgs.fromArgs(getArguments()).getBookId();
        if (bookId != null) { loadBookDetails(bookId); }
        setupClickListeners();
    }

    private void loadBookDetails(String bookId) {
        viewModel.getBookById(bookId).observe(getViewLifecycleOwner(), book -> {
            if (book != null) {
                currentBook = book;
                bindData(book);
            }
        });
    }

    private void bindData(Textbook book) {
        if (book.getCoverImageUrl() != null) {
            Glide.with(this).load(book.getCoverImageUrl())
                    .centerCrop().placeholder(R.drawable.ic_launcher_monochrome).into(binding.ivCover);
        }

        binding.tvTitle.setText(book.getTitle());
        binding.tvAuthor.setText("by " + book.getAuthor());
        binding.tvPrice.setText(String.format(getString(R.string.detail_price), book.getPrice()));
        binding.tvCategory.setText(book.getCategory());
        binding.tvCondition.setText(String.format(getString(R.string.detail_condition), book.getCondition()));
        binding.tvIsbn.setText(book.getIsbn() != null ? book.getIsbn() : "N/A");
        binding.tvDescription.setText(book.getDescription());
        binding.tvSellerName.setText(book.getSellerName());
        binding.tvSellerEmail.setText(book.getSellerEmail());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        binding.tvDate.setText(sdf.format(new Date(book.getTimestamp())));

        if (book.isDigital() && book.getDigitalFileUrl() != null) {
            binding.chipDigital.setVisibility(View.VISIBLE);
            binding.btnViewDigital.setVisibility(View.VISIBLE);
        }
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        binding.btnContactSeller.setOnClickListener(v -> {
            if (currentBook != null && currentBook.getSellerEmail() != null) {
                GmailIntentHelper.composeSellerEmail(
                        requireContext(), currentBook.getSellerEmail(), currentBook.getTitle());
            }
        });

        binding.btnViewDigital.setOnClickListener(v -> {
            if (currentBook != null && currentBook.getDigitalFileUrl() != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(currentBook.getDigitalFileUrl()), currentBook.getFileType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, "Open with"));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}