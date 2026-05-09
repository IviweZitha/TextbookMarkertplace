package com.example.textbookmarketplace.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.textbookmarketplace.R;

public class GmailIntentHelper {
    public static void openGoogleBooksSearch(Context context, String query) {
        String url = "https://www.google.com/search?tbm=bks&q=" + Uri.encode(query);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void composeSellerEmail(Context context, String sellerEmail, String bookTitle) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{sellerEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT,
                "Hello,\n\nI'm interested in purchasing: " + bookTitle +
                        "\n\nPlease let me know:\n- Current condition\n- Availability for pickup/delivery\n- Preferred payment method\n\nBest regards");

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, "Send email via"));
        }
    }
}