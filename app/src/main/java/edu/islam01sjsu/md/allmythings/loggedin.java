package edu.islam01sjsu.md.allmythings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class loggedin extends AppCompatActivity {

    private TextView mUserEmailView;
    private Button mGetAuthChecker;
    private Button mLogOutButton;
    private FloatingActionButton mFab;
    private ImageView mImageView;

    private Firebase FBref;
    private AuthData authData;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);
        mUserEmailView = (TextView)findViewById(R.id.emailView);
        mGetAuthChecker = (Button)findViewById(R.id.authChecker);
        mLogOutButton = (Button)findViewById(R.id .logOutButton);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mImageView = (ImageView) findViewById(R.id.testImageView);


        Intent recieverIntent = getIntent();
        final String firebaseURL = recieverIntent.getStringExtra("firebaseURL");
        String userEmail = recieverIntent.getStringExtra("userEmail");
        mUserEmailView.setText(userEmail);
        FBref = new Firebase(firebaseURL);
        authData = FBref.getAuth();
        String USERuid = authData.getUid();
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FBref.unauth();
                Toast.makeText(getApplicationContext(), "LOGGED OUT !!!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        mGetAuthChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), authData.getUid(), Toast.LENGTH_LONG).show();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);





            }
        });
    }


    private File getFile(){
        File folder = new File("sdcard/camera_app");
        if(!folder.exists()){
            folder.mkdir();
        }
        File image_File = new File(folder, "cam_image.jpg");
        return image_File;
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/camera_app/cam_image.jpg";
        onResume();
        testBitmapToString();

        //        mImageView.setImageDrawable(Drawable.createFromPath(path));

        }



    public void testBitmapToString(){

        Bitmap bm = BitmapFactory.decodeFile("sdcard/camera_app/cam_image.jpg");
        int nh = (int) ( bm.getHeight() * (512.0 / bm.getWidth()) );
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
        Map<String, Object> mapImage = new HashMap<String, Object>();
        mapImage.put("users/"+authData.getUid()+"/imageString",imageEncoded);
        FBref.updateChildren(mapImage);

    }
}
