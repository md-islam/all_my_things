package edu.islam01sjsu.md.allmythings;

import android.graphics.Bitmap;

/**
 * Created by MD on 2/28/2016.
 */
public class Belonging {
    String imageString;
    String description;
    // Bitmap itemBitmap;
    // String itemName;
    // date bought
    // date taken
    // lent
    //
    public Belonging(){

    }


    public Belonging(String description, String imageString){
        this.description = description;
        this.imageString = imageString;
    }


    public String getImageString(){
        return imageString;
    }
    public String getDescription(){
        return description;
    }





}
