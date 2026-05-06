package com.example.textbookmarketplace.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.textbookmarketplace.dao.TextbookDao;
import com.example.textbookmarketplace.database.AppDatabase;
import com.example.textbookmarketplace.model.Textbook;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookRepository {

    private final TextbookDao textbookDao;
    private final LiveData<List<Textbook>> allAvailableBooks;
    private final ExecutorService executor;

    public BookRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        textbookDao = database.textbookDao();
        allAvailableBooks = textbookDao.getAllAvailableBooks();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Textbook>> getAllAvailableBooks() {
        return allAvailableBooks;
    }

    public LiveData<List<Textbook>> getAllBooks() {
        return textbookDao.getAllBooks();
    }

    public LiveData<Textbook> getBookById(String bookId) {
        return textbookDao.getBookById(bookId);
    }

    public LiveData<List<Textbook>> getUserListings(String userId) {
        return textbookDao.getBySeller(userId);
    }

    public LiveData<List<Textbook>> searchBooks(String query) {
        return textbookDao.search("%" + query + "%");
    }

    public LiveData<List<Textbook>> getBooksByCategory(String category) {
        return textbookDao.getBooksByCategory(category);
    }

    public void insert(Textbook book) {
        executor.execute(() -> textbookDao.insert(book));
    }

    public void update(Textbook book) {
        executor.execute(() -> textbookDao.update(book));
    }

    public void delete(Textbook book) {
        executor.execute(() -> textbookDao.delete(book));
    }

    public void deleteById(String bookId) {
        executor.execute(() -> textbookDao.deleteById(bookId));
    }
}