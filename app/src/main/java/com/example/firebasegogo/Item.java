package com.example.firebasegogo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Item implements Serializable {
    private String Price;
    private String Name;
    private String Image;
    private String Type;
    private Integer Quantity;
    public Item() {
        // empty default constructor, necessary for Firebase to be able to deserialize users
    }
    public Item(String Name, String Price, String Image, String Type){
        this.Name = Name;
        this.Price = Price;
        this.Image = Image;
        this.Quantity = 0;
        this.Type = Type;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", Name);
        result.put("Price", Price);
        result.put("Image", Image);
        result.put("Type", Type);
        return result;
    }
    public Map<String, Object> toMap1() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", Name);
        result.put("Price", Price);
        result.put("Quantity", Quantity);
        result.put("Type", Type);
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
    public String getType() {
        return Type;
    }
    public Integer getQuantity() {
        return Quantity;
    }
    public void setImage(String Image){
        this.Image = Image;
    }
    public void setPrice(String Price) {
        this.Price = Price;
    }
    public void setName(String name) {
        Name = name;
    }
    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }
    public void setType(String Type) {
        this.Type = Type;
    }

}
