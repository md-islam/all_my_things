package edu.islam01sjsu.md.allmythings;

import android.graphics.Bitmap;

/**
 * Created by MD on 2/28/2016.
 */
public class Belonging {
    String imageString;
    String description;
    String itemName;
    double price;




    // Bitmap itemBitmap;
    // String itemName;
    // String itemDescription
    // date taken
    // lent
    //
    public Belonging(){

    }


    public Belonging(String description, String imageString, String itemName, Double price){
        this.description = description;
        this.imageString = imageString;
        this.itemName = itemName;
        this.price = price;
    }


    public String getImageString(){
        return imageString;
    }
    public String getDescription(){
        return description;
    }
    public Double getPrice() {
        return price;
    }
    public String getItemName() {
        return itemName;
    }






}
