package com.example.textbookmarketplace.repository;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.UUID;

public class FileStorageRepository {

    public LiveData<UploadResult> uploadImage(Uri imageUri, String userId) {
        MutableLiveData<UploadResult> result = new MutableLiveData<>();
        new Thread(() -> {
            try {
                Thread.sleep(500);
                String fakeUrl = "https://example.com/images/" + userId + "/" + UUID.randomUUID().toString() + ".jpg";
                result.postValue(UploadResult.success(fakeUrl));
            } catch (InterruptedException e) {
                result.postValue(UploadResult.failure(e.getMessage()));
            }
        }).start();
        return result;
    }

    public LiveData<UploadResult> uploadFile(Uri fileUri, String userId, String mimeType) {
        MutableLiveData<UploadResult> result = new MutableLiveData<>();
        new Thread(() -> {
            try {
                Thread.sleep(500);
                String fakeUrl = "https://example.com/files/" + userId + "/" + UUID.randomUUID().toString();
                String fileName = "document_" + System.currentTimeMillis();
                result.postValue(UploadResult.success(fakeUrl, fileName, mimeType));
            } catch (InterruptedException e) {
                result.postValue(UploadResult.failure(e.getMessage()));
            }
        }).start();
        return result;
    }
}