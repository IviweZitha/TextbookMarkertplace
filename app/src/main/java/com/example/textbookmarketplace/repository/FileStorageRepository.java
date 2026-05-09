package com.example.textbookmarketplace.repository;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class FileStorageRepository {
    private static final String TAG = "FileStorageRepository";
    private final Context context;

    public FileStorageRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public LiveData<UploadResult> uploadImage(Uri imageUri, String userId) {
        MutableLiveData<UploadResult> result = new MutableLiveData<>();
        
        try {
            File storageDir = new File(context.getFilesDir(), "images/" + userId);
            if (!storageDir.exists() && !storageDir.mkdirs()) {
                result.postValue(UploadResult.failure("Failed to create directory"));
                return result;
            }

            String fileName = UUID.randomUUID().toString() + ".jpg";
            File destFile = new File(storageDir, fileName);

            copyUriToFile(imageUri, destFile);
            
            Uri contentUri = FileProvider.getUriForFile(context, 
                    context.getPackageName() + ".fileprovider", destFile);
            
            result.postValue(UploadResult.success(contentUri.toString()));
        } catch (Exception e) {
            Log.e(TAG, "Error saving image locally", e);
            result.postValue(UploadResult.failure(e.getMessage()));
        }
        
        return result;
    }

    public LiveData<UploadResult> uploadFile(Uri fileUri, String userId, String mimeType) {
        MutableLiveData<UploadResult> result = new MutableLiveData<>();

        try {
            File storageDir = new File(context.getFilesDir(), "files/" + userId);
            if (!storageDir.exists() && !storageDir.mkdirs()) {
                result.postValue(UploadResult.failure("Failed to create directory"));
                return result;
            }

            String extension = mimeType != null && mimeType.contains("pdf") ? ".pdf" : ".docx";
            String originalFileName = "document_" + System.currentTimeMillis() + extension;
            String fileName = UUID.randomUUID().toString() + extension;
            File destFile = new File(storageDir, fileName);

            copyUriToFile(fileUri, destFile);
            
            Uri contentUri = FileProvider.getUriForFile(context, 
                    context.getPackageName() + ".fileprovider", destFile);
            
            result.postValue(UploadResult.success(contentUri.toString(), originalFileName, mimeType));
        } catch (Exception e) {
            Log.e(TAG, "Error saving file locally", e);
            result.postValue(UploadResult.failure(e.getMessage()));
        }

        return result;
    }

    private void copyUriToFile(Uri uri, File destFile) throws Exception {
        try (InputStream in = context.getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(destFile)) {
            if (in == null) throw new Exception("Could not open input stream");
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
    }
}