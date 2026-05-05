package adapter;



import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.LinearLayout;

import android.widget.TextView;



import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;



import com.example.textbookmarkertplace.R;

import model.Listing;



import java.util.List;



public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {



    // 1. Data source

    private List<Listing> listings;



    // 2. Interface for click events (OOP Concept: Polymorphism via Interface)

    public interface OnItemClickListener {

        void onItemClick(Listing listing);

    }



    private OnItemClickListener listener;



    // 3. Constructor

    public ListingAdapter(List<Listing> listings, OnItemClickListener listener) {

        this.listings = listings;

        this.listener = listener;

    }



    // 4. Creates the visual view for the row (inflates item_listing.xml)

    @NonNull

    @Override

    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())

                .inflate(R.layout.item_listing, parent, false);

        return new ListingViewHolder(view);

    }



    // 5. Binds the actual data to the UI elements in the row

    @Override

    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {

        Listing listing = listings.get(position);



        // Book Info

        holder.tvBookTitle.setText(listing.getTextbook().getTitle());

        holder.tvAuthor.setText("by " + listing.getTextbook().getAuthor());



        // Format and set price

        String formattedPrice = String.format("R%.2f", listing.getSellingPrice());

        holder.tvPrice.setText(formattedPrice);



        // Course Code (Hide if it is null or empty)

        String courseCode = listing.getTextbook().getCourseCode();

        if (courseCode != null && !courseCode.trim().isEmpty()) {

            holder.tvCourseCode.setText(courseCode);

            holder.tvCourseCode.setVisibility(View.VISIBLE);

        } else {

            holder.tvCourseCode.setVisibility(View.GONE);

        }



        // Condition

        String condition = listing.getTextbook().getCondition();

        if (condition != null && !condition.trim().isEmpty()) {

            holder.tvCondition.setText(condition);

            holder.tvCondition.setVisibility(View.VISIBLE);

        } else {

            holder.tvCondition.setVisibility(View.GONE);

        }



        // Copies

        holder.tvCopies.setText(listing.getAvailableCopies() + " copies");



        // Seller Info

        holder.tvSellerName.setText("Listed by " + listing.getSeller().getName());



        // Date

        holder.tvListingDate.setText("Listed: " + listing.getListingDate());



        // 6. Set the Click Listener on the whole card

        holder.cardListing.setOnClickListener(v -> {

            if (listener != null) {

                listener.onItemClick(listing);

            }

        });

    }



    // 7. Tells Android how many items are in the list

    @Override

    public int getItemCount() {

        return listings.size();

    }



    // 8. ViewHolder class (Encapsulation - holds the UI references)

    public static class ListingViewHolder extends RecyclerView.ViewHolder {



        androidx.cardview.widget.CardView cardListing;

        TextView tvBookTitle, tvPrice, tvAuthor;

        TextView tvCourseCode, tvCondition, tvCopies;

        TextView tvSellerName, tvListingDate;



        public ListingViewHolder(@NonNull View itemView) {

            super(itemView);



            // Link Java variables to XML IDs

            cardListing = itemView.findViewById(R.id.cardListing);

            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);

            tvPrice = itemView.findViewById(R.id.tvPrice);

            tvAuthor = itemView.findViewById(R.id.tvAuthor);

            tvCourseCode = itemView.findViewById(R.id.tvCourseCode);

            tvCondition = itemView.findViewById(R.id.tvCondition);

            tvCopies = itemView.findViewById(R.id.tvCopies);

            tvSellerName = itemView.findViewById(R.id.tvSellerName);

            tvListingDate = itemView.findViewById(R.id.tvListingDate);

        }

    }
    public void updateList(List<Listing> newListings) {

        this.listings = newListings;

        notifyDataSetChanged();

    }

}
