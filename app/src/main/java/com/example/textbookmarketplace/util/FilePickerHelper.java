package com.example.textbookmarketplace.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.Fragment;

public class FilePickerHelper {
    public static final int REQUEST_IMAGE = 1001;
    public static final int REQUEST_DOCUMENT = 1002;

    public static void pickImage(Fragment fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void pickDocument(Fragment fragment, int requestCode, String[] mimeTypes) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static String getFileNameFromUri(Activity activity, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            android.database.Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    result = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) result = result.substring(cut + 1);
        }
        return result;
    }
}