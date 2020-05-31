package com.example.firebasegogo;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String Price;
    private String Name;
    private String Image;
    public Item() {
        // empty default constructor, necessary for Firebase to be able to deserialize users
    }
    public Item(String Name, String Price, String Image){
        this.Name = Name;
        this.Price = Price;
        this.Image = Image;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", Name);
        result.put("Price", Price);
        result.put("Image", Image);
        return result;
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
    public void setImage(String Image){
        this.Image = Image;
    }
}
