package edu.islam01sjsu.md.allmythings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mRepeatPassword;
    private Button mSignUpButton;
    private Firebase FBref;



    private String firstNameInput;
    private String lastNameInput;
    private String usernameInput;
    private String emailInput;
    private String passwordInput;
    private String repeatPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Firebase.setAndroidContext(this);
        FBref = new Firebase("https://allmythings2016.firebaseio.com");

        //initializing view items
        mFirstName = (EditText)findViewById(R.id.firstNameField);
        mLastName = (EditText)findViewById(R.id.lastNameField);
        mUsername = (EditText)findViewById(R.id.userNameField);
        mEmail = (EditText)findViewById(R.id.signUpEmailField);
        mPassword = (EditText)findViewById(R.id.password_field);
        mRepeatPassword = (EditText)findViewById(R.id.repeatPassword_field);
        mSignUpButton = (Button)findViewById(R.id.loginButton);



        //needs to be unique in the database



        //need to handle email validation
        //needs to be unique in the database



        //need to handle password matches repeated password (created checker method)





        //need to check for uniqueness, equality, regex on sign up in actionListener.



        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"SIGN UP Button is clicked", Toast.LENGTH_LONG).show();

                firstNameInput = mFirstName.getText().toString();
                lastNameInput = mLastName.getText().toString();
                usernameInput = mUsername.getText().toString();
                emailInput = mEmail.getText().toString();
                passwordInput = mPassword.getText().toString();
                repeatPasswordInput = mRepeatPassword.getText().toString();

                final User newUser = new User();
                newUser.setFirstName(firstNameInput);
                newUser.setLastName(lastNameInput);
                newUser.setUserName(usernameInput);


                if(isValidEmail(emailInput)){
                    Log.d("emailcheckERRRR TAG!!", "your email pattern worked my man!!");
                }
                if(checkPassWordAndConfirmPassword(passwordInput, repeatPasswordInput)){
                    Log.d("passwordChecker TAG","your password matched too bro");
                }
                newUser.setEmailAddress(emailInput);
                newUser.setPassWord(passwordInput);
                // firebase create user
                // must add more attributes of user, don't think there's a necessity to sign up

                FBref.createUser(newUser.getEmailAddress(), newUser.getPassWord(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        Toast.makeText(getApplicationContext(), "account created", Toast.LENGTH_LONG).show();
                        Log.d("THIS IS A TAG!!!!!!!", "Successfully created user account with uid: " + result.get("uid"));
                        Map<String, String> newUserMap = new HashMap<String, String>();
                        newUserMap.put("firstName", newUser.getFirstName());
                        newUserMap.put("lastName", newUser.getLastName());
                        newUserMap.put("userName", newUser.getUserName());
                        newUserMap.put("emailAddress", newUser.getEmailAddress());
                        FBref.child("users").child((String)result.get("uid")).setValue(newUserMap);


                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        Log.e("ERROR TAG","didnt work but got through firebase reference!!!: "+emailInput);

                    }
                });

                //after signup done, authenticate users with




            }
        });

        // ACTION LISTENER take in all items, handle duplicate values in database and check if password and repeat
        // password are the same [could be taken care of at sign up field];


    }


    /**
     * To check email confirming email pattern
     * @param email
     * @return boolean checking email validation
     */
    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    /**
     * To check if user enters and confirms his/her password
     * @param password
     * @param repeatPassword
     * @return boolean checking both passwords match for confirmation
     */
    public boolean checkPassWordAndConfirmPassword(String password,String repeatPassword)
    {
        boolean pstatus = false;
        if (repeatPassword != null && password != null)
        {
            if (password.equals(repeatPassword))
            {
                pstatus = true;
            }
        }
        return pstatus;
    }
}
