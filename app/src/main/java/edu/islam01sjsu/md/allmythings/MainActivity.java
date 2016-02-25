package edu.islam01sjsu.md.allmythings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private TextView mLoginEmail;
    private TextView mLoginPassword;
    private Button mLogInButton;
    private TextView mForgotPasswordClickable;
    private TextView mSignUpText;




    Firebase FBref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        FBref = new Firebase("https://allmythings2016.firebaseio.com/");
        mForgotPasswordClickable = (TextView)findViewById(R.id.forgotPassword_clickable_text);
        mLogInButton = (Button)findViewById(R.id.loginButton);
        mLoginEmail = (TextView)findViewById(R.id.login_email);
        mLoginPassword = (TextView)findViewById(R.id.login_Password);



        //action for signup text which opens another intent
        mSignUpText = (TextView) findViewById(R.id.signUP_clickable_text);
        mSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startSignUp = new Intent(MainActivity.this, signup.class);


                startActivity(startSignUp);
                //might need an onstart menu here
                //+++++++++++++++++++++++++//

            }
        });


        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mLoginEmail.getText().toString();
                String password = mLoginPassword.getText().toString();

                //copied from firebase
                FBref.authWithPassword(email, password, new Firebase.AuthResultHandler() {

                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Log.d("LOGIN TAG!!!!!!!", "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        Toast.makeText(getApplicationContext(), "LOG IN SUCCESSFUL!!!!", Toast.LENGTH_LONG).show();

                        //storing user data
//                        Map<String, String> map = new HashMap<String, String>();
//                        map.put("provider", authData.getProvider());
//                        if (authData.getProviderData().containsKey("displayName")) {
//                            map.put("displayName", authData.getProviderData().get("displayName").toString());
//                        }
//                        FBref.child("users").child(authData.getUid()).setValue(map);
                        //

                        Intent loginPage = new Intent(MainActivity.this, loggedin.class);
                        loginPage.putExtra("firebaseURL", "https://allmythings2016.firebaseio.com/");
                        loginPage.putExtra("userEmail", email);
                        startActivity(loginPage);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // there was an error
                        Toast.makeText(getApplicationContext(), "LOG IN UNSUCCESSFUL!!!!", Toast.LENGTH_LONG).show();
                        Log.e("ERROR TAG", "didnt work but got through firebase reference!!!: ");
                    }
                });
                //copied from firebase


            }
        });









    }
}
