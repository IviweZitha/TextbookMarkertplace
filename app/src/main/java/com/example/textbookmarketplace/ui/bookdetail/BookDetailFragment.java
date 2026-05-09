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
import com.bumptech.glide.Glide;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

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

        if (book.getCoverImageUrl() != null && !book.getCoverImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(book.getCoverImageUrl())
                    .placeholder(com.example.textbookmarketplace.R.color.surface_variant)
                    .into(binding.ivCover);
        }

        if (book.isDigital() && book.getDigitalFileName() != null) {
            binding.tvDigitalFile.setVisibility(View.VISIBLE);
            binding.tvDigitalFile.setText(getString(com.example.textbookmarketplace.R.string.digital_file_info, book.getDigitalFileName()));
            binding.btnReadFile.setVisibility(View.VISIBLE);
        } else {
            binding.tvDigitalFile.setVisibility(View.GONE);
            binding.btnReadFile.setVisibility(View.GONE);
        }
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        
        binding.btnContactSeller.setOnClickListener(v -> {
            viewModel.getBookById(bookId).observe(getViewLifecycleOwner(), book -> {
                if (book != null && book.getSellerEmail() != null) {
                    com.example.textbookmarketplace.util.GmailIntentHelper.composeSellerEmail(
                            requireContext(), book.getSellerEmail(), book.getTitle());
                } else {
                    Toast.makeText(getContext(), "Seller email not available", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.btnReadFile.setOnClickListener(v -> {
            viewModel.getBookById(bookId).observe(getViewLifecycleOwner(), book -> {
                if (book != null && book.getDigitalFileUrl() != null) {
                    openFile(book.getDigitalFileUrl(), book.getFileType());
                }
            });
        });
    }

    private void openFile(String fileUrl, String fileType) {
        Uri uri = Uri.parse(fileUrl);
        String mimeType = "application/pdf";
        if ("docx".equalsIgnoreCase(fileType)) {
            mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "No app found to open this file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}