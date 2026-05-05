package ui;

import android.os.Bundle;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.textbookmarkertplace.R;

import manager.ListingManager;

import model.Listing;

import util.Validator;

import java.util.Optional;

public class ListingDetailActivity extends AppCompatActivity {

    public static final String EXTRA_LISTING_ID = "listing_id";

    private TextView tvTitle, tvAuthor, tvIsbn, tvEdition, tvCondition;

    private TextView tvFaculty, tvCourseCode, tvPrice, tvCopies;

    private TextView tvSellerName, tvSellerEmail, tvListingDate;

    private TextView tvPaymentInfo;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listing_detail);

        initViews();

        loadListingData();

    }
    private void initViews() {

        tvTitle = findViewById(R.id.tvDetailTitle);

        tvAuthor = findViewById(R.id.tvDetailAuthor);

        tvIsbn = findViewById(R.id.tvDetailIsbn);

        tvEdition = findViewById(R.id.tvDetailEdition);

        tvCondition = findViewById(R.id.tvDetailCondition);

        tvFaculty = findViewById(R.id.tvDetailFaculty);

        tvCourseCode = findViewById(R.id.tvDetailCourseCode);

        tvPrice = findViewById(R.id.tvDetailPrice);

        tvCopies = findViewById(R.id.tvDetailCopies);

        tvSellerName = findViewById(R.id.tvDetailSellerName);

        tvSellerEmail = findViewById(R.id.tvDetailSellerEmail);

        tvListingDate = findViewById(R.id.tvDetailListingDate);

        tvPaymentInfo = findViewById(R.id.tvPaymentInfo);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

    }
    private void loadListingData() {

        String listingId = getIntent().getStringExtra(EXTRA_LISTING_ID);



        if (listingId == null) {

            finish();

            return;

        }

        ListingManager manager = ListingManager.getInstance();

        Optional<Listing> listingOpt = manager.getActiveListings().stream()

                .filter(l -> l.getListingId().equals(listingId))

                .findFirst();

        if (!listingOpt.isPresent()) {

            finish();

            return;

        }
        Listing listing = listingOpt.get();

        tvTitle.setText(listing.getTextbook().getTitle());

        tvAuthor.setText("by " + listing.getTextbook().getAuthor());

        tvIsbn.setText(listing.getTextbook().getIsbn());

        tvEdition.setText(listing.getTextbook().getEdition());

        tvCondition.setText(listing.getTextbook().getCondition());

        tvFaculty.setText(listing.getTextbook().getFaculty());

        tvCourseCode.setText(listing.getTextbook().getCourseCode());

        tvPrice.setText(Validator.formatPrice(listing.getSellingPrice()));

        tvCopies.setText(listing.getAvailableCopies() + " copies available");

        tvSellerName.setText(listing.getSeller().getName());

        tvSellerEmail.setText(listing.getSeller().getEmail());

        tvListingDate.setText("Listed: " + listing.getListingDate());

        tvPaymentInfo.setText(listing.getSeller().getMaskedBankingInfo());

    }

}
