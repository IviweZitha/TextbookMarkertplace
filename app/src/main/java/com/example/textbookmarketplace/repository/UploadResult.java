package com.example.textbookmarketplace.repository;

public class UploadResult {
    public final boolean success;
    public final String url;
    public final String error;
    public final String fileName;
    public final String mimeType;

    public UploadResult(boolean success, String url, String error, String fileName, String mimeType) {
        this.success = success;
        this.url = url;
        this.error = error;
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public boolean isSuccess() { return success; }

    public static UploadResult success(String url) {
        return new UploadResult(true, url, null, null, null);
    }

    public static UploadResult success(String url, String fileName, String mimeType) {
        return new UploadResult(true, url, null, fileName, mimeType);
    }

    public static UploadResult failure(String error) {
        return new UploadResult(false, null, error, null, null);
    }
}