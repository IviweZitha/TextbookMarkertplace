package com.example.textbookmarketplace.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.databinding.ItemMyListingBinding;
import com.example.textbookmarketplace.model.Textbook;

public class MyListingAdapter extends ListAdapter<Textbook, MyListingAdapter.ViewHolder> {
    private final OnActionListener listener;

    public interface OnActionListener {
        void onEdit(Textbook book);
        void onDelete(Textbook book);
        void onBookClick(Textbook book);
    }

    public MyListingAdapter(OnActionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Textbook> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Textbook>() {
                @Override
                public boolean areItemsTheSame(@NonNull Textbook oldItem, @NonNull Textbook newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull Textbook oldItem, @NonNull Textbook newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle());
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyListingBinding binding = ItemMyListingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Textbook book = getItem(position);
        holder.bind(book);

        holder.binding.getRoot().setOnClickListener(v -> listener.onBookClick(book));
        holder.binding.btnEdit.setOnClickListener(v -> listener.onEdit(book));
        holder.binding.btnDelete.setOnClickListener(v -> listener.onDelete(book));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMyListingBinding binding;

        ViewHolder(ItemMyListingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Textbook book) {
            binding.tvTitle.setText(book.getTitle());
            binding.tvPrice.setText(String.format("$%.2f", book.getPrice()));
            binding.tvStatus.setText(book.isAvailable() ? "Available" : "Sold");

            if (book.getCoverImageUrl() != null) {
                Glide.with(binding.ivCover.getContext())
                        .load(book.getCoverImageUrl())
                        .centerCrop()
                        .into(binding.ivCover);
            }
        }
    }
}