package com.example.recipeapp;

public class FoodData {
    private String itemName;
    private String itemDescription;
    private String itemTime;
    private String itemImage;
    private String key;

    public FoodData(){
        //Specify a defualt constructor to avoid error in getValue in Main activity
    }

    public FoodData(String itemName, String itemDescription, String itemTime, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemTime = itemTime;
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemTime() {
        return itemTime;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
