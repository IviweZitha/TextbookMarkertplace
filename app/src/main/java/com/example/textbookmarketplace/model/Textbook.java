package com.example.textbookmarketplace.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "textbooks")
public class Textbook {
    @PrimaryKey
    @NonNull
    private String id;

    private String title;
    private String author;
    private String isbn;
    private String description;
    private double price;
    private String category;
    private String condition;

    // File references
    private String coverImageUrl;
    private String digitalFileUrl;
    private String digitalFileName;
    private String fileType; // "pdf", "docx", or null
    private boolean isDigital;

    // Seller info
    private String sellerEmail;
    private String sellerId;
    private String sellerName;

    private long timestamp;
    private boolean isAvailable;

    public Textbook() {
        this.id = java.util.UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.isAvailable = true;
    }

    // Getters and Setters
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getDigitalFileUrl() { return digitalFileUrl; }
    public void setDigitalFileUrl(String digitalFileUrl) { this.digitalFileUrl = digitalFileUrl; }

    public String getDigitalFileName() { return digitalFileName; }
    public void setDigitalFileName(String digitalFileName) { this.digitalFileName = digitalFileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public boolean isDigital() { return isDigital; }
    public void setDigital(boolean digital) { isDigital = digital; }

    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}