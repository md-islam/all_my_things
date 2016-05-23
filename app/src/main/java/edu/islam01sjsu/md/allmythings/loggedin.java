package edu.islam01sjsu.md.allmythings;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class loggedin extends AppCompatActivity {
    FirebaseRecyclerAdapter<Belonging, ItemViewHolder> fAdapter;

    //private List<Belonging> belongings;
    RecyclerView mRecyclerView;    // Declaring RecyclerView
    Firebase FBref = new Firebase("https://allmythings2016.firebaseio.com/");
    RecyclerView.LayoutManager mLayoutManager;  // Declaring Layout Manager as a linear layout manager
    CoordinatorLayout mCoordinatorLayout;
    String userID = FBref.getAuth().getUid();
    private FloatingActionButton mFab;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    //**dialog fragment components;
    private Button mSaveButton;
    Calendar calendar = Calendar.getInstance();
    private Button mTakePictureButton;
    private EditText mItemEdit;
    private EditText mItemDescription;
    private EditText mItemPriceEdit;
    private ImageView mItemImage;
    private EditText mDatePicker;
    private String userNameString;

    @Override
    protected void onStart() {
        super.onStart();
        Firebase.setAndroidContext(this);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        //************************//
        //initialize belongings
        // belongings = new ArrayList<>();
        userID = FBref.getAuth().getUid();
        Firebase userNameRef = FBref.child("users").child((String)userID).child("userName");

        //YOUR TREE
        //Firebase

        userNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userNameString = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Toast.makeText(getApplicationContext(),userNameString+": UserName", Toast.LENGTH_SHORT).show();



        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        fAdapter = new FirebaseRecyclerAdapter<Belonging, ItemViewHolder>(Belonging.class,
                R.layout.card_layout, ItemViewHolder.class, FBref.child("photos").child(userID)) {
            @Override
            protected void populateViewHolder(ItemViewHolder ivh, Belonging belonging, int i) {

                ivh.mItemDescription.setText(belonging.itemName);
                byte[] decodedString = android.util.Base64.decode(belonging.imageString, android.util.Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivh.mItemImage.setImageBitmap(decodedByte);
            }

        };


        mRecyclerView.setAdapter(fAdapter);         // Setting the adapter to RecyclerView
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Movie movie = movieList.get(position);
                //Toast.makeText(getApplicationContext(), position + " is selected!= " +
                //        fAdapter.getRef(position).toString(), Toast.LENGTH_SHORT).show();
                Belonging touchedBelonging = fAdapter.getItem(position);
                Toast.makeText(getApplicationContext(),touchedBelonging.getDescription(), Toast.LENGTH_SHORT).show();

//                FragmentManager fman = getSupportFragmentManager();
//                ItemViewFragment ivf = (ItemViewFragment) fman.findFragmentById(R.id.fragment_itemview_table);
//                ivf.setUpItemToView(touchedBelonging);
            }

            @Override
            public void onLongClick(View view, int position) {

                Toast.makeText(getApplicationContext(), "" + fAdapter.getItemId(position) + " " + fAdapter.getItem(position).getItemName(), Toast.LENGTH_SHORT).show();
                //fAdapter.getRef(position).removeValue();
            }
        }));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        //***********************//
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                File file = getFile();
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                showItemRegistrationDialog();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);
        Firebase.setAndroidContext(getApplicationContext());
    }

    public void showItemRegistrationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        RegisterItemDialog rid = new RegisterItemDialog();
        rid.show(fm, "fragment_register_item");
        fm.executePendingTransactions();
        final Dialog dialog = rid.getDialog();
        mTakePictureButton = (Button) dialog.findViewById(R.id.dialogTakePictureButton);
        mItemEdit = (EditText) dialog.findViewById(R.id.item_name);
        mItemDescription = (EditText) dialog.findViewById(R.id.item_description);
        mItemPriceEdit = (EditText) dialog.findViewById(R.id.item_price);
        mSaveButton = (Button) dialog.findViewById(R.id.dialogSaveButton);
        mItemImage = (ImageView) dialog.findViewById(R.id.dialog_item_image);
        mDatePicker = (EditText) dialog.findViewById(R.id.dialog_date_purchased);
        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDatePicker.setText(monthOfYear + 1 + "/" + dayOfMonth + "/" + year);
            }
        };
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(loggedin.this, listener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();

            }
        });

        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }


        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = mItemEdit.getText().toString();
                String itemDescription = mItemDescription.getText().toString();
                double itemPrice = 0;
                String datePurchased = mDatePicker.getText().toString();
                Bitmap imageFromPreview = null;

                if (!(mItemPriceEdit.getText().toString().equals(""))) {
                    itemPrice = Double.parseDouble(mItemPriceEdit.getText().toString());
                }
                if (mItemImage.getDrawable() != null) {
                    imageFromPreview = ((BitmapDrawable) mItemImage.getDrawable()).getBitmap();
                } else {
                    //code to show a toast
                }
                if (!(mItemPriceEdit.getText().toString().equals("")) && !(itemName.equals("")) &&
                        !(itemDescription.equals("")) && imageFromPreview != null && !(datePurchased.equals(""))) {
                    //encode this shit to firebase
                    encodeToFirebase(itemName, itemDescription, itemPrice, imageFromPreview, datePurchased);
                    dialog.dismiss();


                } else {
                    Toast.makeText(getApplicationContext(), "All data including image must be provided",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bitmap bm = getImageBitmap();
        if (bm != null && mItemImage != null) {
            mItemImage.setImageBitmap(bm);
        }
    }


    private File getFile() {
        File folder = new File("sdcard/camera_app");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File image_File = new File(folder, "cam_image.jpg");
        return image_File;
    }


    // needs refining considering app activity lifecycle
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/camera_app/cam_image.jpg";
        onResume();

        //picture not taken handler and back button is hit
        if (resultCode != RESULT_OK) {
            Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "Item not added to inventory", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            //encodeToFirebase();
            deleteTemporaryStorage();
        }


    }


    public void encodeToFirebase(String itemName, String itemDescription,
                                 double itemPrice, Bitmap itemImage, String datePurchased) {
        //  Bitmap bm = BitmapFactory.decodeFile("sdcard/camera_app/cam_image.jpg");
        Bitmap bm = itemImage;
        int nh = (int) (bm.getHeight() * (512.0 / bm.getWidth()));
        Bitmap bmap = Bitmap.createScaledBitmap(bm, 512, nh, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String imageEncoded = com.firebase.client.utilities.Base64.encodeBytes(b);
        Toast.makeText(getApplicationContext(), "created image for current user", Toast.LENGTH_LONG).show();
        Map<String, String> photoMap = new HashMap<String, String>();
        photoMap.put("description", itemDescription);
        photoMap.put("imageString", imageEncoded);
        photoMap.put("itemName", itemName);
        photoMap.put("price", Double.toString(itemPrice));
        photoMap.put("datePurchased", datePurchased);
        FBref.child("photos").child(userID).push().setValue(photoMap);
    }

    public Bitmap getImageBitmap() {
        Bitmap bm = BitmapFactory.decodeFile("sdcard/camera_app/cam_image.jpg");
        Bitmap bmap = null;
        if (bm != null) {
            int nh = (int) (bm.getHeight() * (512.0 / bm.getWidth()));
            bmap = Bitmap.createScaledBitmap(bm, 512, nh, true);
        }
        return bmap;
    }

    public void deleteTemporaryStorage() {
        String path = "sdcard/camera_app/cam_image.jpg";
        File deleteFile = new File(path);
        deleteFile.delete();

    }


//FOR ITERATING VALUES:
//    public void decodeFromFirebase() {
//        FBref.child("photos").child(userID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
//                    String imageDescription = (String) messageSnapshot.child("description").getValue();
//                    String encodedImage = (String) messageSnapshot.child("imageString").getValue();
//
//                    //decode encoded image to BITMAP
//                    if (encodedImage != null) {
//                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        belongings.add(new Belonging(imageDescription, encodedImage));
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.loginmenu, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FBref.unauth();
                Toast.makeText(getApplicationContext(), "LOGGED OUT !!!", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            case R.id.action_getauth:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(getApplicationContext(), userID, Toast.LENGTH_SHORT).show();
                return true;


            case R.id.action_takePicture:

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    ////// this is recycler view action part


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private loggedin.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final loggedin.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    ///// END recycler view actionLISTENER PART part


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView mItemDescription;
        ImageView mItemImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            mItemDescription = (TextView) itemView.findViewById(R.id.item_description);
            mItemImage = (ImageView) itemView.findViewById(R.id.item_photo);
        }
    }


    //SWIPE ACTIONS: DELETE, ARcHIVE(PENDing implementation)
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            //setting backupItem and URL.

            //URL
            String backuptoRestoreLocation = fAdapter.getRef(position).toString();

            //Belonging object from firebase on recyclerView
            final Belonging temporaryBackupItem = fAdapter.getItem(position);
            final Firebase targetDeleteLocation = new Firebase(backuptoRestoreLocation);


            fAdapter.getRef(position).removeValue();

            //Snackbar <code>
            Snackbar snackbar = Snackbar
                    .make(mCoordinatorLayout, "Image is deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            targetDeleteLocation.setValue(temporaryBackupItem);
                            Snackbar snackbar1 = Snackbar.make(mCoordinatorLayout, "Image is restored!", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    });

            snackbar.show();
        }
    };
}

