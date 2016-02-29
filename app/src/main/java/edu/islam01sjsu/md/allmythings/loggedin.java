package edu.islam01sjsu.md.allmythings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.Toast;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class loggedin extends AppCompatActivity {

    private List<Belonging> belongings;

    private Toolbar toolbar;       // Declaring the Toolbar Object
    RecyclerView mRecyclerView;    // Declaring RecyclerView
    RecyclerView.Adapter mAdapter; // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;  // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;          // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;// Declaring Action Bar Drawer Toggle
    String userID;

    private Firebase FBref;
    private AuthData authData;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);

        Intent recieverIntent = getIntent();
        final String firebaseURL = recieverIntent.getStringExtra("firebaseURL");
        String userEmail = recieverIntent.getStringExtra("userEmail");


        //************************//
        //initialize belongings
        belongings = new ArrayList<>();
        FBref = new Firebase(firebaseURL);
        authData = FBref.getAuth();
        userID = authData.getUid();
        decodeFromFirebase();


        //initialize belongings


        //
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new MyAdapter(belongings,this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        //***********************//




    }


    private File getFile() {
        File folder = new File("sdcard/camera_app");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File image_File = new File(folder, "cam_image.jpg");
        return image_File;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/camera_app/cam_image.jpg";
        onResume();

        encodeToFirebase();

        //        mImageView.setImageDrawable(Drawable.createFromPath(path));

    }


    public void encodeToFirebase() {

        Bitmap bm = BitmapFactory.decodeFile("sdcard/camera_app/cam_image.jpg");
        int nh = (int) (bm.getHeight() * (512.0 / bm.getWidth()));
        Bitmap bmap = Bitmap.createScaledBitmap(bm, 512, nh, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 90, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        String imageEncoded = com.firebase.client.utilities.Base64.encodeBytes(b);
        Toast.makeText(getApplicationContext(), "created image for current user", Toast.LENGTH_LONG).show();
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("ImageString",imageEncoded);
//        FBref.child("users").child(authData.getUid()).child("user's images").setValue(map);

        Map<String, String> photoMap = new HashMap<String, String>();
        photoMap.put("description", "example Description ++");
        photoMap.put("image-string", imageEncoded);
        FBref.child("photos").child(authData.getUid()).push().setValue(photoMap);

    }


    public void decodeFromFirebase(){
        FBref.child("photos").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String imageDescription = (String) messageSnapshot.child("description").getValue();
                    String encodedImage = (String) messageSnapshot.child("image-string").getValue();

                    //decode encoded image to BITMAP
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    belongings.add(new Belonging(imageDescription, decodedByte));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



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
                Toast.makeText(getApplicationContext(), authData.getUid(), Toast.LENGTH_SHORT).show();
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
}

