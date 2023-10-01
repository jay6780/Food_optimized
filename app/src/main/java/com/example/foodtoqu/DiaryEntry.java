package com.example.foodtoqu;

public class DiaryEntry {
    private String name;
    private String imageUrl;

    // Default constructor (required for Firebase)
    public DiaryEntry() {
    }

    public DiaryEntry(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
