package ui;



import android.os.Bundle;

import android.text.Editable;

import android.text.TextWatcher;

import android.widget.EditText;

import android.widget.LinearLayout;

import android.widget.TextView;



import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;



import com.example.textbookmarkertplace.R;

import adapter.ListingAdapter;

import manager.ListingManager;

import model.Listing;



import java.util.ArrayList;

import java.util.List;



public class SearchActivity extends AppCompatActivity {



    private EditText etSearchInput;

    private RecyclerView recyclerViewSearchResults;

    private TextView tvResultCount;

    private LinearLayout layoutNoResults;



    private ListingAdapter adapter;

    private List<Listing> fullListings;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);



        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setTitle("Search Textbooks");

        }



        initViews();

        loadInitialData();

        setupSearchListener();

    }



    private void initViews() {

        // Note: If your activity_search.xml uses a SearchView instead of EditText,

        // you would change the type here. Assuming standard EditText based on common setups.

        etSearchInput = findViewById(R.id.searchInputLayout);

        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);

        tvResultCount = findViewById(R.id.tvResultCount);

        layoutNoResults = findViewById(R.id.tvNoResults); // ID from your XML snippet



        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));

    }



    private void loadInitialData() {

        // Fetch the master list from the Singleton Manager

        fullListings = ListingManager.getInstance().getActiveListings();



        // Set up adapter (reuse the same adapter as MainActivity)

        adapter = new ListingAdapter(new ArrayList<>(fullListings), listing -> {

            // Reusing the same click listener logic to open Details

            startActivity(ListingDetailActivity.newIntent(this, listing.getListingId()));

        });

        recyclerViewSearchResults.setAdapter(adapter);



        updateResultCount(fullListings.size());

    }



    private void setupSearchListener() {

        // TextWatcher listens to every letter typed in real-time

        etSearchInput.addTextChangedListener(new TextWatcher() {

            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}



            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                performSearch(s.toString());

            }



            @Override

            public void afterTextChanged(Editable s) {}

        });

    }



    // ==========================================

    // OOP INTERFACE / POLYMORPHISM BLOCK

    // ==========================================

    private void performSearch(String query) {

        List<Listing> filteredList = new ArrayList<>();



        if (query == null || query.trim().isEmpty()) {

            filteredList.addAll(fullListings);

        } else {

            String lowerCaseQuery = query.toLowerCase().trim();



            // Loop through the list and treat each item as a 'Searchable' object

            for (Listing listing : fullListings) {



                // Because Listing implements the Searchable interface, we can call this method.

                // This is POLYMORPHISM: The SearchActivity doesn't care HOW it searches,

                // it just knows the Listing HAS A method called matchesQuery().

                if (listing.matchesQuery(lowerCaseQuery)) {

                    filteredList.add(listing);

                }

            }

        }



        // Update the UI

        adapter.updateList(filteredList); // You will need to add this method to your Adapter

        updateResultCount(filteredList.size());



        if (filteredList.isEmpty() && !query.trim().isEmpty()) {

            layoutNoResults.setVisibility(LinearLayout.VISIBLE);

            recyclerViewSearchResults.setVisibility(RecyclerView.GONE);

        } else {

            layoutNoResults.setVisibility(LinearLayout.GONE);

            recyclerViewSearchResults.setVisibility(RecyclerView.VISIBLE);

        }

    }



    private void updateResultCount(int count) {

        if (tvResultCount != null) {

            tvResultCount.setText(count + " result(s) found");

        }

    }



    @Override

    public boolean onSupportNavigateUp() {

        finish();

        return true;

    }

}