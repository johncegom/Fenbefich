package com.example.firebasegogo;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String ID;
    private String Name;
    private String Password;
    private String Image;
    private String Email;
    public User() {
        // empty default constructor, necessary for Firebase to be able to deserialize users
    }
    public User(String ID, String Name, String Password, String Image, String Email){
        this.ID = ID;
        this.Name = Name;
        this.Password = Password;
        this.Image = Image;
        this.Email = Email;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", ID);
        result.put("Name", Name);
        result.put("Password", Password);
        result.put("Image", Image);
        result.put("Email", Email);
        return result;
    }
    public String getID() {
        return ID;
    }
    public String getName() {
        return Name;
    }
    public String getPassword() {
        return Password;
    }
    public String getImage() {
        return Image;
    }
    public String getEmail() {
        return Email;
    }
    public void setImage(String Image){ this.Image = Image; }
    public void setID(String ID) { this.ID = ID; }
    public void setPassword(String Password) { this.Password = Password; }
    public void setName(String Name) { this.Name = Name; }
    public void setEmail(String Email) { this.Email = Email; }
}
