package com.example.textbookmarketplace.viewmodel;

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.textbookmarketplace.model.Textbook;
import com.example.textbookmarketplace.repository.BookRepository;
import com.example.textbookmarketplace.repository.FileStorageRepository;
import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private final BookRepository repository;
    private final FileStorageRepository fileStorage;
    private final LiveData<List<Textbook>> allAvailableBooks;

    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
        fileStorage = new FileStorageRepository();
        allAvailableBooks = repository.getAllAvailableBooks();
    }

    public LiveData<List<Textbook>> getAllAvailableBooks() { return allAvailableBooks; }
    public LiveData<List<Textbook>> getUserListings(String userId) {
        return repository.getUserListings(userId);
    }
    public LiveData<Textbook> getBookById(String bookId) {
        return repository.getBookById(bookId);
    }
    public LiveData<List<Textbook>> searchBooks(String query) {
        return repository.searchBooks(query);
    }

    public void insert(Textbook textbook) { repository.insert(textbook); }
    public void update(Textbook textbook) { repository.update(textbook); }
    public void delete(Textbook textbook) { repository.delete(textbook); }
    public void deleteById(String bookId) { repository.deleteById(bookId); }

    public MutableLiveData<FileStorageRepository.UploadResult> uploadCoverImage(Uri uri, String userId) {
        return fileStorage.uploadImage(uri, userId);
    }

    public MutableLiveData<FileStorageRepository.UploadResult> uploadDigitalFile(Uri uri, String userId, String mimeType) {
        return fileStorage.uploadDigitalFile(uri, userId, mimeType);
    }
}