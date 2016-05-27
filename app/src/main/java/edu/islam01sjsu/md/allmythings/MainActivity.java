package edu.islam01sjsu.md.allmythings;

import android.annotation.TargetApi;


import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;



public class MainActivity extends AppCompatActivity {


    private EditText mLoginEmail;
    private EditText mLoginPassword;
    private Button mLogInButton;
    private TextView mForgotPasswordClickable;
    private TextView mSignUpText;
    private TextInputLayout mEmailWrapper;
    private TextInputLayout mPasswordWrapper;




    Firebase FBref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        FBref = new Firebase("https://allmythings2016.firebaseio.com/");
        mForgotPasswordClickable = (TextView)findViewById(R.id.forgotPassword_clickable_text);
        mLogInButton = (Button)findViewById(R.id.loginButton);
        mLoginEmail = (EditText)findViewById(R.id.login_email);
        mLoginPassword = (EditText)findViewById(R.id.login_Password);

        mEmailWrapper = (TextInputLayout) findViewById(R.id.emailFieldWrapper_activity_main);
        mPasswordWrapper = (TextInputLayout) findViewById(R.id.password_field_wrapper_main_activity);
        mEmailWrapper.setHint("Email");
        mPasswordWrapper.setHint("Password");



        //action for signup text which opens another intent
        mSignUpText = (TextView) findViewById(R.id.signUP_clickable_text);
        mSignUpText.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
//                Intent startSignUp = new Intent(MainActivity.this, signup.class);
//
//
//                startActivity(startSignUp);
                //might need an onstart menu here
                //+++++++++++++++++++++++++//

//                SignUpFragment fragment = new SignUpFragment();
//                FragmentManager manager = fragment.getChildFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.add(R.id.signUp_linear_layout, fragment, "SignUpFragment");
//                transaction.commit();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.login_activity_frameLayout, new SignUpFragment(), "first_fragment");
                ft.addToBackStack("back_to_main_activity").commit();




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

    @Override
    protected void onStart() {
        super.onStart();
        Firebase.setAndroidContext(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Firebase.setAndroidContext(this);
    }
}
