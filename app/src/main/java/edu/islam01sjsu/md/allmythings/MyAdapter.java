package edu.islam01sjsu.md.allmythings;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hp1 on 28-12-2014.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {
     private List<Belonging> belongings;
     LayoutInflater inflater;

    MyAdapter(List<Belonging> belongings, Context context){
        this.belongings = belongings;
        inflater = LayoutInflater.from(context);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView mItemDescription;
        ImageView mItemImage;


        public ItemViewHolder(View itemView) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
            //initialize cardview components
            cv = (CardView)itemView.findViewById(R.id.cv);
            mItemDescription = (TextView)itemView.findViewById(R.id.item_description);
            mItemImage =(ImageView)itemView.findViewById(R.id.item_photo);
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
        }


    }

    @Override
    public int getItemCount() {
        //return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
        return belongings.size();
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder ivh, int i){
       // ivh.mItemImage.setImageBitmap();
        ivh.mItemDescription.setText(belongings.get(i).description);
        ivh.mItemImage.setImageBitmap(belongings.get(i).itemBitmap);
    }
}