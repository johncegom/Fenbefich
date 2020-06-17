package com.example.firebasegogo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
    private String Price;
    private String Name;
    private String Type;
    private Integer Quantity;
    public Cart() {
        // empty default constructor, necessary for Firebase to be able to deserialize users
    }
    public Cart(String Name, String Price, Integer Quantity, String Type){
        this.Name = Name;
        this.Price = Price;
        this.Quantity = 0;
        this.Type = Type;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", Name);
        result.put("Price", Price);
        result.put("Quantity", Quantity);
        return result;
    }

    public String getName() {
        return Name;
    }
    public String getPrice() {
        return Price;
    }
    public Integer getQuantity() {
        return Quantity;
    }
    public String getType() {
        return Type;
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

