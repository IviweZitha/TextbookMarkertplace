package com.example.textbookmarketplace.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.textbookmarketplace.dao.TextbookDao;
import com.example.textbookmarketplace.model.Textbook;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Textbook.class}, version = 2, exportSchema = false)
public abstract class TextbookDatabase extends RoomDatabase {
    public abstract TextbookDao textbookDao();

    private static volatile TextbookDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TextbookDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TextbookDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    TextbookDatabase.class,
                                    "textbook_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                }
            };
}