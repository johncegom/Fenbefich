package com.example.firebasegogo;

public class Food {
    private String Price;
    private String Name;
    private String Image;
    public Food() {
        // empty default constructor, necessary for Firebase to be able to deserialize users
    }

    public String getName() {
        return Name;
    }
    public String getPrice() {
        return Price;
    }
    public String getImage() {
        return Image;
    }
}
