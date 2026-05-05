package ui;



import android.os.Bundle;

import android.view.View;

import android.widget.Toast;



import androidx.appcompat.app.AppCompatActivity;



import com.example.textbookmarkertplace.R;

import exception.DuplicateListingException;

import exception.InsufficientCopiesException;

import exception.InvalidBankingInfoException;

import exception.InvalidListingException;

import manager.ListingManager;

import model.BankingInfo;

import model.Listing;

import model.Seller;

import model.Textbook;

import util.Validator;

import com.google.android.material.button.MaterialButton;

import com.google.android.material.textfield.TextInputEditText;



public class AddListingActivity extends AppCompatActivity {



    // Book UI Elements

    private TextInputEditText etBookTitle, etAuthor, etIsbn, etEdition, etCondition;

    private TextInputEditText etFaculty, etCourseCode, etCopies, etSellingPrice;



    // Seller UI Elements

    private TextInputEditText etSellerName, etSellerEmail, etSellerPhone;


    private TextInputEditText etBankName, etAccountNumber, etAccountHolder, etBranchCode;



    // Buttons

    private MaterialButton btnSubmit, btnClear;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_listing);



        // Enable Back Button in Toolbar

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setTitle("Add Listing");

        }



        initViews();

        setupListeners();

    }



    private void initViews() {

        // Linking XML to Java (Encapsulation of UI components)

        etBookTitle = findViewById(R.id.etBookTitle);

        etAuthor = findViewById(R.id.etAuthor);

        etIsbn = findViewById(R.id.etIsbn);

        etEdition = findViewById(R.id.etEdition);

        etCondition = findViewById(R.id.etCondition);

        etFaculty = findViewById(R.id.etFaculty);

        etCourseCode = findViewById(R.id.etCourseCode);

        etCopies = findViewById(R.id.etCopies);

        etSellingPrice = findViewById(R.id.etSellingPrice);



        etSellerName = findViewById(R.id.etSellerName);

        etSellerEmail = findViewById(R.id.etSellerEmail);

        etSellerPhone = findViewById(R.id.etSellerPhone);



        etBankName = findViewById(R.id.etBankName);

        etAccountNumber = findViewById(R.id.etAccountNumber);

        etAccountHolder = findViewById(R.id.etAccountHolder);

        etBranchCode = findViewById(R.id.etBranchCode);



        btnSubmit = findViewById(R.id.btnSubmit);

        btnClear = findViewById(R.id.btnClear);

    }



    private void setupListeners() {

        btnClear.setOnClickListener(v -> clearForm());



        btnSubmit.setOnClickListener(v -> {

            try {

                submitListing();

            } catch (Exception e) {

                // The specific catch blocks inside submitListing() will handle the toasts

            }

        });

    }



    private void submitListing() {

        // 1. Get Text Safely (Prevents NullPointerException)

        String title = getTextFromString(etBookTitle);

        String author = getTextFromString(etAuthor);

        String isbn = getTextFromString(etIsbn);

        String edition = getTextFromString(etEdition);

        String condition = getTextFromString(etCondition);

        String faculty = getTextFromString(etFaculty);

        String courseCode = getTextFromString(etCourseCode);



        String sellerName = getTextFromString(etSellerName);

        String sellerEmail = getTextFromString(etSellerEmail);

        String sellerPhone = getTextFromString(etSellerPhone);



        String bankName = getTextFromString(etBankName);

        String accountNumber = getTextFromString(etAccountNumber);

        String accountHolder = getTextFromString(etAccountHolder);

        String branchCode = getTextFromString(etBranchCode);



        // ==========================================

        // EXCEPTION HANDLING BLOCK (Assignment Req.)

        // ==========================================



        try {

            // Validate Required Fields

            if (title.isEmpty() || author.isEmpty() || sellerName.isEmpty()) {

                throw new InvalidListingException("Book title, author, and your name are required.");

            }



            // Validate Copies

            int copies = 0;

            try {

                copies = Integer.parseInt(getTextFromString(etCopies));

                if (copies <= 0) {

                    throw new InsufficientCopiesException("You must list at least 1 copy.");

                }

            } catch (NumberFormatException e) {

                throw new InvalidListingException("Copies must be a valid number.");

            }



            // Validate Price

            double price = 0.0;

            try {

                price = Double.parseDouble(getTextFromString(etSellingPrice));

                if (price <= 0) {

                    throw new InvalidListingException("Selling price must be greater than 0.");

                }

            } catch (NumberFormatException e) {

                throw new InvalidListingException("Selling price must be a valid number (e.g., 250.50).");

            }



            // Validate Banking Info (Using the Validator Utility Class)

            if (bankName.isEmpty() || accountNumber.isEmpty() || accountHolder.isEmpty() || branchCode.isEmpty()) {

                throw new InvalidBankingInfoException("All banking details are required for payment.");

            }

            if (!Validator.isValidBranchCode(branchCode)) {

                throw new InvalidBankingInfoException("Branch code must be exactly 6 digits.");

            }



            // ==========================================

            // OOP COMPOSITION BLOCK

            // ==========================================



            // Create Objects bottom-up (Composition: "Has-A" relationship)

            // A Listing HAS-A Textbook. A Listing HAS-A Seller. A Seller HAS-A BankingInfo.



            BankingInfo bankingInfo = new BankingInfo(bankName, accountNumber, accountHolder, branchCode);

            Seller seller = new Seller(sellerName, sellerEmail, sellerPhone, bankingInfo);



            // Textbook extends abstract ListItem (Inheritance: "Is-A" relationship)

            Textbook textbook = new Textbook(title, author, isbn, edition, condition, faculty, courseCode);



            Listing newListing = new Listing(textbook, seller, price, copies);



            // ==========================================

            // MANAGER / SINGLETON BLOCK

            // ==========================================

            ListingManager.getInstance().addListing(newListing);



            // Success

            Toast.makeText(this, "Listing added successfully!", Toast.LENGTH_SHORT).show();

            finish(); // Close this screen and go back to MainActivity



        } catch (InvalidListingException | InsufficientCopiesException e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        } catch (InvalidBankingInfoException e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        } catch (DuplicateListingException e) {

            // This is thrown by ListingManager if the exact same book & seller combo exists

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {

            Toast.makeText(this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();

            e.printStackTrace();

        }

    }



    private void clearForm() {

        // Simple loop to clear all TextInputEditTexts on the screen

        View currentView = findViewById(android.R.id.content).getRootView();

        if (currentView instanceof ViewGroup) {

            clearEditTexts((ViewGroup) currentView);

        }

    }



    private void clearEditTexts(ViewGroup group) {

        for (int i = 0; i < group.getChildCount(); i++) {

            View child = group.getChildAt(i);

            if (child instanceof TextInputEditText) {

                ((TextInputEditText) child).setText("");

            } else if (child instanceof ViewGroup) {

                clearEditTexts((ViewGroup) child);

            }

        }

    }



    // Helper method to prevent NullPointerException when getting text

    private String getTextFromString(TextInputEditText editText) {

        return editText.getText() != null ? editText.getText().toString().trim() : "";

    }



    @Override

    public boolean onSupportNavigateUp() {

        finish(); // Handle back button click

        return true;

    }

}