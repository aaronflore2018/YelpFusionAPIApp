package com.example.finalproject;

public class Business {
    private String name;
    private String id;
    private String address;
    private String phone;
    private String rating;
    private String price;

    public Business(){};

    public void setName(String n){this.name = n;}

    public void setId(String id){this.id = id;}

    public void setAddress(String a){this.address = a;}

    public void setPhone(String p){this.phone = p;}

    public void setRating(String r){this.rating = r;}

    public void setPrice(String p){this.price = p;}

    public String getName(){return this.name;}

    public String getId(){return this.id;}

    public String getAddress(){return this.address;}

    public String getPhone(){return this.phone;}

    public String getRating(){return this.rating;};

    public String getPrice(){return this.price;}
}
