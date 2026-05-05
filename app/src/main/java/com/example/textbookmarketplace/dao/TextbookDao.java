package com.example.textbookmarketplace.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.textbookmarketplace.model.Textbook;
import java.util.List;

@Dao
public interface TextbookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Textbook textbook);

    @Update
    void update(Textbook textbook);

    @Delete
    void delete(Textbook textbook);

    @Query("DELETE FROM textbooks WHERE id = :bookId")
    void deleteById(String bookId);

    @Query("SELECT * FROM textbooks WHERE isAvailable = 1 ORDER BY timestamp DESC")
    LiveData<List<Textbook>> getAllAvailable();

    @Query("SELECT * FROM textbooks WHERE sellerId = :userId ORDER BY timestamp DESC")
    LiveData<List<Textbook>> getBySeller(String userId);

    @Query("SELECT * FROM textbooks WHERE id = :bookId")
    LiveData<Textbook> getById(String bookId);

    @Query("SELECT * FROM textbooks WHERE title LIKE :query OR author LIKE :query OR category LIKE :query")
    LiveData<List<Textbook>> search(String query);
}