package com.example.textbookmarketplace.ui.add;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.databinding.FragmentAddTextbookBinding;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.repository.FileStorageRepository;
import com.example.textbookmarketplace.util.FilePickerHelper;
import com.example.textbookmarketplace.viewmodel.BookViewModel;
import java.util.Objects;

public class AddTextbookFragment extends Fragment {
    private FragmentAddTextbookBinding binding;
    private BookViewModel viewModel;
    private Textbook textbook;
    private String editBookId;
    private String currentUserId;

    private Uri selectedImageUri;
    private Uri selectedFileUri;
    private String selectedFileType;

    private final ActivityResultLauncher<Intent> imagePicker =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        binding.ivCoverPreview.setImageURI(selectedImageUri);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> filePicker =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedFileUri = result.getData().getData();
                    if (selectedFileUri != null) {
                        selectedFileType = requireContext().getContentResolver().getType(selectedFileUri);
                        String fileName = FilePickerHelper.getFileNameFromUri(requireActivity(), selectedFileUri);
                        binding.tvFileName.setText(fileName);
                        binding.tvFileName.setVisibility(View.VISIBLE);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddTextbookBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserId = "demo_user_123";

        viewModel = new ViewModelProvider(this).get(BookViewModel.class);
        textbook = new Textbook();
        textbook.setSellerId(currentUserId);

        setupFormFields();
        setupFilePickers();
        setupActions();
        checkEditMode();
    }

    private void setupFormFields() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.categories, R.layout.dropdown_item);
        binding.etCategory.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.conditions, R.layout.dropdown_item);
        binding.etCondition.setAdapter(conditionAdapter);
    }

    private void setupFilePickers() {
        binding.btnPickCover.setOnClickListener(v ->
                FilePickerHelper.pickImage(this, FilePickerHelper.REQUEST_IMAGE));

        binding.btnPickDigital.setOnClickListener(v ->
                FilePickerHelper.pickDocument(this, FilePickerHelper.REQUEST_DOCUMENT,
                        new String[]{"application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}));
    }

    private void setupActions() {
        binding.btnSave.setOnClickListener(v -> validateAndSave());
        binding.btnCancel.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());
    }

    private void checkEditMode() {
        Bundle args = getArguments();
        if (args != null) {
            editBookId = args.getString("edit_book_id");
        }

        if (editBookId != null) {
            binding.tvTitle.setText(R.string.edit_title);
            loadBookForEdit(editBookId);
        }
    }

    private void loadBookForEdit(String bookId) {
        viewModel.getBookById(bookId).observe(getViewLifecycleOwner(), book -> {
            if (book != null) {
                textbook = book;
                populateForm(book);
            }
        });
    }

    private void populateForm(Textbook book) {
        binding.etTitle.setText(book.getTitle());
        binding.etAuthor.setText(book.getAuthor());
        binding.etIsbn.setText(book.getIsbn());
        binding.etDescription.setText(book.getDescription());
        binding.etPrice.setText(String.valueOf(book.getPrice()));
        binding.etCategory.setText(book.getCategory(), false);
        binding.etCondition.setText(book.getCondition(), false);

        if (book.isDigital()) {
            binding.tvFileName.setText(book.getDigitalFileName());
            binding.tvFileName.setVisibility(View.VISIBLE);
        }
    }

    private void validateAndSave() {
        String title = binding.etTitle.getText().toString().trim();
        String author = binding.etAuthor.getText().toString().trim();
        String priceStr = binding.etPrice.getText().toString().trim();

        if (title.isEmpty()) { binding.etTitle.setError(getString(R.string.error_required)); return; }
        if (author.isEmpty()) { binding.etAuthor.setError(getString(R.string.error_required)); return; }
        if (priceStr.isEmpty()) { binding.etPrice.setError(getString(R.string.error_required)); return; }

        double price;
        try { price = Double.parseDouble(priceStr); }
        catch (NumberFormatException e) { binding.etPrice.setError(getString(R.string.error_invalid_price)); return; }

        textbook.setTitle(title);
        textbook.setAuthor(author);
        textbook.setIsbn(binding.etIsbn.getText().toString().trim());
        textbook.setDescription(binding.etDescription.getText().toString().trim());
        textbook.setPrice(price);
        textbook.setCategory(Objects.requireNonNull(binding.etCategory.getText()).toString());
        textbook.setCondition(Objects.requireNonNull(binding.etCondition.getText()).toString());
        textbook.setSellerEmail("user@example.com");
        textbook.setSellerName("Demo User");

        if (selectedImageUri != null) { uploadCoverImage(); }
        else if (selectedFileUri != null) { uploadDigitalFile(); }
        else { saveToDatabase(); }
    }

    private void uploadCoverImage() {
        viewModel.uploadCoverImage(selectedImageUri, currentUserId)
                .observe(getViewLifecycleOwner(), result -> {
                    if (result.isSuccess()) {
                        textbook.setCoverImageUrl(result.url);
                        if (selectedFileUri != null) { uploadDigitalFile(); }
                        else { saveToDatabase(); }
                    } else {
                        Toast.makeText(getContext(), "Image upload failed: " + result.error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadDigitalFile() {
        viewModel.uploadDigitalFile(selectedFileUri, currentUserId, selectedFileType)
                .observe(getViewLifecycleOwner(), result -> {
                    if (result.isSuccess()) {
                        textbook.setDigitalFileUrl(result.url);
                        textbook.setDigitalFileName(result.fileName);
                        textbook.setFileType(result.mimeType.contains("pdf") ? "pdf" : "docx");
                        textbook.setDigital(true);
                        saveToDatabase();
                    } else {
                        Toast.makeText(getContext(), "File upload failed: " + result.error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveToDatabase() {
        if (editBookId != null) {
            viewModel.update(textbook);
            Toast.makeText(getContext(), "Textbook updated!", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.insert(textbook);
            Toast.makeText(getContext(), "Textbook listed!", Toast.LENGTH_SHORT).show();
        }
        NavHostFragment.findNavController(this).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}