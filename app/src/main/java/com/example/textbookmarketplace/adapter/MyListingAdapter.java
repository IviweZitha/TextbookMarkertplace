package com.example.textbookmarketplace.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.textbookmarketplace.databinding.ItemMyListingBinding;
import com.example.textbookmarketplace.model.Textbook;
import java.util.Locale;

public class MyListingAdapter extends ListAdapter<Textbook, MyListingAdapter.ViewHolder> {

    private final OnEditClickListener editListener;
    private final OnDeleteClickListener deleteListener;

    public interface OnEditClickListener {
        void onEdit(Textbook book);
    }

    public interface OnDeleteClickListener {
        void onDelete(Textbook book);
    }

    public MyListingAdapter(OnEditClickListener editListener, OnDeleteClickListener deleteListener) {
        super(new DiffUtil.ItemCallback<Textbook>() {
            @Override
            public boolean areItemsTheSame(@NonNull Textbook oldItem, @NonNull Textbook newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Textbook oldItem, @NonNull Textbook newItem) {
                return oldItem.getTitle().equals(newItem.getTitle());
            }
        });
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyListingBinding binding = ItemMyListingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // REMOVED 'static' keyword here
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMyListingBinding binding;

        public ViewHolder(ItemMyListingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Textbook book) {
            binding.tvTitle.setText(book.getTitle());
            binding.tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", book.getPrice()));

            if (book.getCoverImageUrl() != null && !book.getCoverImageUrl().isEmpty()) {
                Glide.with(binding.ivCover.getContext())
                        .load(book.getCoverImageUrl())
                        .centerCrop()
                        .into(binding.ivCover);
            }

            binding.tvStatus.setText(book.isAvailable() ? "Available" : "Sold");
            binding.tvStatus.setTextColor(book.isAvailable()
                    ? android.graphics.Color.GREEN
                    : android.graphics.Color.RED);

            binding.btnEdit.setOnClickListener(v -> editListener.onEdit(book));
            binding.btnDelete.setOnClickListener(v -> deleteListener.onDelete(book));
        }
    }
}