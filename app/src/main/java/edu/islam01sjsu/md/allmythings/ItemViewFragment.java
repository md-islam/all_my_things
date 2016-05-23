package edu.islam01sjsu.md.allmythings;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.utilities.Base64;

/**
 * Created by MD on 4/11/2016.
 */
public class ItemViewFragment extends android.support.v4.app.Fragment {

    TextView item_name_value;
    TextView item_description_value;
    TextView item_price_value;
    TextView item_date_value;
    ImageView item_image_value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_itemview , container , false);
        item_name_value = (TextView) rootView.findViewById(R.id.fragment_item_text_value);
        item_description_value = (TextView) rootView.findViewById(R.id.fragment_description_text_value);
        item_price_value = (TextView) rootView.findViewById(R.id.fragment_price_text_value);
        //item_price_value = (TextView) rootView.findViewById(R.id.fragment_price_text_value);
        item_date_value = (TextView) rootView.findViewById(R.id.fragment_datePurchasedView_text_value);
        item_image_value = (ImageView) rootView.findViewById(R.id.fragment_image_value);


        return rootView;
    }

    public void setUpItemToView(Belonging belonging){

        item_name_value.setText(belonging.getItemName());
        item_description_value.setText(belonging.getDescription());
        item_price_value.setText(Double.toString(belonging.getPrice()));
        item_date_value.setText(belonging.getDatePurchased());
        byte[] decodedString = android.util.Base64.decode(belonging.imageString, android.util.Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        item_image_value.setImageBitmap(decodedByte);


    }

}
