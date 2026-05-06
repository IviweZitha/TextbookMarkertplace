package com.example.textbookmarketplace.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.textbookmarketplace.model.Textbook;
import java.util.List;

@Dao
public interface TextbookDao {

    @Query("SELECT * FROM textbooks WHERE isAvailable = 1 ORDER BY title ASC")
    LiveData<List<Textbook>> getAllAvailableBooks();

    @Query("SELECT * FROM textbooks ORDER BY title ASC")
    LiveData<List<Textbook>> getAllBooks();

    @Query("SELECT * FROM textbooks WHERE id = :bookId LIMIT 1")
    LiveData<Textbook> getBookById(String bookId);

    @Query("SELECT * FROM textbooks WHERE sellerId = :userId ORDER BY timestamp DESC")
    LiveData<List<Textbook>> getBySeller(String userId);

    @Query("SELECT * FROM textbooks WHERE title LIKE :query OR author LIKE :query OR category LIKE :query")
    LiveData<List<Textbook>> search(String query);

    @Query("SELECT * FROM textbooks WHERE category = :category AND isAvailable = 1")
    LiveData<List<Textbook>> getBooksByCategory(String category);

    @Query("DELETE FROM textbooks WHERE id = :bookId")
    void deleteById(String bookId);

    @Insert
    void insert(Textbook book);

    @Update
    void update(Textbook book);

    @Delete
    void delete(Textbook book);
}