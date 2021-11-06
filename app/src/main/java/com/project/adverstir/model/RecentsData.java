package com.project.adverstir.model;

public class RecentsData {

    String placeName;
    String countryName;
    String price;
    Integer imageUrl;
    float starRating;

    // Right click -> Generate -> Constructor
    public RecentsData(String placeName, String countryName, String price, Integer imageUrl, float starRating) {
        this.placeName = placeName;
        this.countryName = countryName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.starRating = starRating;
    }

    // Right click -> Generate -> Getter and Setter
    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getStarRating() {
        return starRating;
    }

    public void setStarRating(float starRating) {
        this.starRating = starRating;
    }
}

