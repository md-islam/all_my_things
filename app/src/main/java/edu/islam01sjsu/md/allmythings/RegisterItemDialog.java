package edu.islam01sjsu.md.allmythings;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by MD on 3/16/2016.
 */
public class RegisterItemDialog extends DialogFragment {

    private EditText mItemName;
    private EditText mItemDescription;
    private Button mCancelButton;
    private Button mSaveButton;
    private Button mTakePictureButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register_item, container, false);
//        mItemName = (EditText) rootView.findViewById(R.id.item_name);
//        mItemDescription = (EditText) rootView.findViewById(R.id.item_description);
        mCancelButton = (Button) rootView.findViewById(R.id.dialogCancelButton);
//      mTakePictureButton = (Button) rootView.findViewById(R.id.dialogTakePictureButton);
//      mSaveButton = (Button) rootView.findViewById(R.id.dialogSaveButton);
        getDialog().setTitle("Register new Item");

        //cancel button action
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }


}
