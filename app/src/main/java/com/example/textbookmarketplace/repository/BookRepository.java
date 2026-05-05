package com.example.textbookmarketplace.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.textbookmarketplace.dao.TextbookDao;
import com.example.textbookmarketplace.database.TextbookDatabase;
import com.example.textbookmarketplace.model.Textbook;
import java.util.List;

public class BookRepository {
    private final TextbookDao textbookDao;
    private final LiveData<List<Textbook>> allAvailableBooks;

    public BookRepository(Application application) {
        TextbookDatabase db = TextbookDatabase.getDatabase(application);
        textbookDao = db.textbookDao();
        allAvailableBooks = textbookDao.getAllAvailable();
    }

    public LiveData<List<Textbook>> getAllAvailableBooks() {
        return allAvailableBooks;
    }

    public LiveData<List<Textbook>> getUserListings(String userId) {
        return textbookDao.getBySeller(userId);
    }

    public LiveData<Textbook> getBookById(String bookId) {
        return textbookDao.getById(bookId);
    }

    public LiveData<List<Textbook>> searchBooks(String query) {
        return textbookDao.search("%" + query + "%");
    }

    public void insert(Textbook textbook) {
        TextbookDatabase.databaseWriteExecutor.execute(() -> textbookDao.insert(textbook));
    }

    public void update(Textbook textbook) {
        TextbookDatabase.databaseWriteExecutor.execute(() -> textbookDao.update(textbook));
    }

    public void delete(Textbook textbook) {
        TextbookDatabase.databaseWriteExecutor.execute(() -> textbookDao.delete(textbook));
    }

    public void deleteById(String bookId) {
        TextbookDatabase.databaseWriteExecutor.execute(() -> textbookDao.deleteById(bookId));
    }
}