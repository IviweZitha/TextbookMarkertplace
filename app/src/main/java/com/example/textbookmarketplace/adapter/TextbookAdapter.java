package com.example.textbookmarketplace.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.textbookmarketplace.R;
import com.example.textbookmarketplace.databinding.ItemTextbookBinding;
import com.example.textbookmarketplace.model.Textbook;

public class TextbookAdapter extends ListAdapter<Textbook, TextbookAdapter.ViewHolder> {
    private final OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(Textbook book);
    }

    public TextbookAdapter(OnBookClickListener listener) {
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
                    return oldItem.getTitle().equals(newItem.getTitle()) &&
                            oldItem.getPrice() == newItem.getPrice();
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTextbookBinding binding = ItemTextbookBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Textbook book = getItem(position);
        holder.bind(book);
        holder.binding.getRoot().setOnClickListener(v -> listener.onBookClick(book));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemTextbookBinding binding;

        ViewHolder(ItemTextbookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Textbook book) {
            binding.tvTitle.setText(book.getTitle());
            binding.tvAuthor.setText(book.getAuthor());
            binding.tvPrice.setText(String.format("$%.2f", book.getPrice()));
            binding.tvCategory.setText(book.getCategory());

            if (book.getCoverImageUrl() != null) {
                Glide.with(binding.ivCover.getContext())
                        .load(book.getCoverImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_monochrome)
                        .into(binding.ivCover);
            }

            binding.ivDigitalBadge.setVisibility(book.isDigital() ?
                    android.view.View.VISIBLE : android.view.View.GONE);
        }
    }
}