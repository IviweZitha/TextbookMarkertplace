package com.example.textbookmarketplace.repository;

import android.net.Uri;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.UUID;

public class FileStorageRepository {
    private final FirebaseStorage storage;

    public FileStorageRepository() {
        storage = FirebaseStorage.getInstance();
    }

    public MutableLiveData<UploadResult> uploadImage(Uri imageUri, String userId) {
        MutableLiveData<UploadResult> result = new MutableLiveData<>();
        String fileName = "covers/" + userId + "/" + UUID.randomUUID() + ".jpg";
        StorageReference ref = storage.getReference().child(fileName);

        ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl().addOnSuccessListener(uri ->
                                result.postValue(new UploadResult(uri.toString(), "image/jpeg", "Cover Image"))))
                .addOnFailureListener(e ->
                        result.postValue(new UploadResult(null, null, e.getMessage())));

        return result;
    }

    public MutableLiveData<UploadResult> uploadDigitalFile(Uri fileUri, String userId, String mimeType) {
        MutableLiveData<UploadResult> result = new MutableLiveData<>();
        String ext = mimeType.contains("pdf") ? "pdf" : "docx";
        String fileName = "digital/" + userId + "/" + UUID.randomUUID() + "." + ext;
        StorageReference ref = storage.getReference().child(fileName);

        ref.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl().addOnSuccessListener(uri ->
                                result.postValue(new UploadResult(uri.toString(), mimeType, fileName))))
                .addOnFailureListener(e ->
                        result.postValue(new UploadResult(null, null, e.getMessage())));

        return result;
    }

    public static class UploadResult {
        public final String url;
        public final String mimeType;
        public final String fileName;
        public final String error;

        public UploadResult(String url, String mimeType, String fileName) {
            this.url = url;
            this.mimeType = mimeType;
            this.fileName = fileName;
            this.error = null;
        }

        public boolean isSuccess() { return error == null; }
    }
}