package com.example.firebasegogo;

public class Item {
    private String Price;
    private String Name;
    private String Image;
    public Item() {
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
