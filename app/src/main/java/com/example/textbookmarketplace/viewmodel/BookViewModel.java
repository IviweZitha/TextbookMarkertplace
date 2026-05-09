package com.example.textbookmarketplace.viewmodel;

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.repository.BookRepository;
import com.example.textbookmarketplace.repository.FileStorageRepository;
import com.example.textbookmarketplace.repository.UploadResult;
import java.util.List;

public class BookViewModel extends AndroidViewModel {

    private final BookRepository bookRepository;
    private final FileStorageRepository fileStorageRepository;
    private final LiveData<List<Textbook>> allAvailableBooks;

    public BookViewModel(@NonNull Application application) {
        super(application);
        bookRepository = new BookRepository(application);
        fileStorageRepository = new FileStorageRepository(application);
        allAvailableBooks = bookRepository.getAllAvailableBooks();
    }

    public LiveData<List<Textbook>> getAllAvailableBooks() {
        return allAvailableBooks;
    }

    public LiveData<List<Textbook>> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public LiveData<Textbook> getBookById(String bookId) {
        return bookRepository.getBookById(bookId);
    }

    public LiveData<List<Textbook>> getUserListings(String userId) {
        return bookRepository.getUserListings(userId);
    }

    public LiveData<List<Textbook>> searchBooks(String query) {
        return bookRepository.searchBooks(query);
    }

    public LiveData<List<Textbook>> getBooksByCategory(String category) {
        return bookRepository.getBooksByCategory(category);
    }

    public LiveData<UploadResult> uploadCoverImage(Uri imageUri, String userId) {
        return fileStorageRepository.uploadImage(imageUri, userId);
    }

    public LiveData<UploadResult> uploadDigitalFile(Uri fileUri, String userId, String mimeType) {
        return fileStorageRepository.uploadFile(fileUri, userId, mimeType);
    }

    public void insert(Textbook book) {
        bookRepository.insert(book);
    }

    public void update(Textbook book) {
        bookRepository.update(book);
    }

    public void delete(Textbook book) {
        bookRepository.delete(book);
    }

    public void deleteById(String bookId) {
        bookRepository.deleteById(bookId);
    }
}