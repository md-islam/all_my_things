package edu.islam01sjsu.md.allmythings;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private Boolean methodBooleanExisting = false;


    private String firstNameInput;
    private String lastNameInput;
    private String usernameInput;
    private String emailInput;
    private String passwordInput;
    private String repeatPasswordInput;
    private List<String> userNames = new ArrayList<String>();
    private Boolean isExistingUsername;

    private TextInputLayout mFirstNameWrapper;
    private TextInputLayout mLastNameWrapper;
    private TextInputLayout mEmailWrapper;
    private TextInputLayout mUsernameWrapper;
    private TextInputLayout mPasswordWrapper;
    private TextInputLayout mRepeatPasswordWrapper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Firebase.setAndroidContext(this);
        FBref = new Firebase("https://allmythings2016.firebaseio.com");

        //initializing view items
        mFirstName = (EditText) findViewById(R.id.firstNameField);
        mLastName = (EditText) findViewById(R.id.lastNameField);
        mUsername = (EditText) findViewById(R.id.userNameField);
        mEmail = (EditText) findViewById(R.id.signUpEmailField);
        mPassword = (EditText) findViewById(R.id.password_field);
        mRepeatPassword = (EditText) findViewById(R.id.repeatPassword_field);
        mSignUpButton = (Button) findViewById(R.id.signUp_Button_signup_activity);

        //initializing the wrappers for animation
        mFirstNameWrapper = (TextInputLayout) findViewById(R.id.firstName_wrapper_signup_activity);
        mLastNameWrapper = (TextInputLayout) findViewById(R.id.lastName_wrapper_signUpactivity);
        mEmailWrapper = (TextInputLayout) findViewById(R.id.email_field_wrapper_signUp_activity);
        mUsernameWrapper = (TextInputLayout) findViewById(R.id.userNameField_wrapper_signUpActivity);
        mPasswordWrapper = (TextInputLayout) findViewById(R.id.password_field_wrapper_signUp_activity);
        mRepeatPasswordWrapper = (TextInputLayout) findViewById(R.id.repeatPassword_field_wrapper_signUpActivity);

        passwordInput = mPassword.getText().toString();
        repeatPasswordInput = mRepeatPassword.getText().toString();

        mFirstNameWrapper.setHint("First name");
        mLastNameWrapper.setHint("Last name");
        mUsernameWrapper.setHint("Username");
        mEmailWrapper.setHint("Email");
        mPasswordWrapper.setHint("Password");
        mRepeatPasswordWrapper.setHint("Repeat password");


        mFirstName.addTextChangedListener(new MyTextWatcher(mFirstName));
        mLastName.addTextChangedListener(new MyTextWatcher(mLastName));
        mEmail.addTextChangedListener(new MyTextWatcher(mEmail));
        mRepeatPassword.addTextChangedListener(new MyTextWatcher(mRepeatPassword));



        fillUpUsernamesList();
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "SIGN UP Button is clicked", Toast.LENGTH_LONG).show();
                Boolean isExistingUsername = false;
                firstNameInput = mFirstName.getText().toString();
                lastNameInput = mLastName.getText().toString();
                usernameInput = mUsername.getText().toString();
                emailInput = mEmail.getText().toString();
                passwordInput = mPassword.getText().toString();
                repeatPasswordInput = mRepeatPassword.getText().toString();

                final User newUser = new User();
                newUser.setFirstName(firstNameInput);
                newUser.setLastName(lastNameInput);


                //checking for an already registered user with the same username
                Log.d("going to call method", "1...2....3...GO");

                if (userNames.contains(usernameInput)) {
                    isExistingUsername = true;
                }


                newUser.setUserName(usernameInput);


                Log.d("BOOLEAN TAG USENAME", isExistingUsername.toString());
                if (isExistingUsername) {
                    Log.d("TAG!!!!", "username exists in firebase");
                } else {
                    Log.d("TAG!!!", "username does not exist");
                }
                if (isValidEmail(emailInput)) {
                    Log.d("emailcheckERRRR TAG!!", "your email pattern worked my man!!");
                }
                if (checkPassWordAndConfirmPassword(passwordInput, repeatPasswordInput)) {
                    Log.d("passwordChecker TAG", "your password matched too bro");
                }
                newUser.setEmailAddress(emailInput);
                newUser.setPassWord(passwordInput);
                // firebase create user
                // must add more attributes of user, don't think there's a necessity to sign up


                //adding the user in the firebase json format
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
                        FBref.child("users").child((String) result.get("uid")).setValue(newUserMap);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        Log.e("ERROR TAG", "didnt work but got through firebase reference!!!: " + emailInput);

                    }
                });
                //redo userNamesList();
                fillUpUsernamesList();
            }

        });

        // ACTION LISTENER take in all items, handle duplicate values in database and check if password and repeat
        // password are the same [could be taken care of at sign up field];


    }





    /**
     * To check if user enters and confirms his/her password
     *
     * @param password
     * @param repeatPassword
     * @return boolean checking both passwords match for confirmation
     */
    public boolean checkPassWordAndConfirmPassword(String password, String repeatPassword) {
        boolean pstatus = false;
        if (repeatPassword != null && password != null) {
            if (password.equals(repeatPassword)) {
                pstatus = true;
            }
        }
        return pstatus;
    }


    private void fillUpUsernamesList() {
        userNames.clear();
        FBref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String existingUsername = (String) userSnapshot.child("userName").getValue();
                    //Log.d("EXISTING USERNAMES", existingUsername);
                    userNames.add(existingUsername);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //some error thrown here
            }
        });
    }


    private boolean validateFirstName() {
        if (mFirstName.getText().toString().trim().isEmpty()) {
            mFirstNameWrapper.setError(getString(R.string.error_hint_first_name));
            requestFocus(mFirstName);
            return false;
        } else {
            mFirstNameWrapper.setErrorEnabled(false);
            mFirstNameWrapper.setError(null);
        }
        return true;
    }

    private boolean validateLastName() {
        if (mLastName.getText().toString().trim().isEmpty()) {
            mLastNameWrapper.setError(getString(R.string.error_hint_first_name));
            requestFocus(mLastName);
            return false;
        } else {
            mLastNameWrapper.setErrorEnabled(false);
            mLastNameWrapper.setError(null);
        }
        return true;
    }

    private boolean validateEmailAddress(){
        String email = mEmail.getText().toString().trim();
        if(email.isEmpty()|| !isValidEmail(email)){
            mEmailWrapper.setError(getString(R.string.error_hint_email));
            requestFocus(mEmail);
            return false;
        }
        else{
            mEmailWrapper.setErrorEnabled(false);
            mEmailWrapper.setError(null);
        }
        return true;
    }

    private boolean validatePassword(){
        passwordInput = mPassword.getText().toString();
        repeatPasswordInput = mRepeatPassword.getText().toString();
        if(!checkPassWordAndConfirmPassword(passwordInput, repeatPasswordInput)||repeatPasswordInput.isEmpty()) {
            mRepeatPasswordWrapper.setError(getString(R.string.error_hint_repeat_password));
            requestFocus(mRepeatPassword);
            return false;
        }
        else{
            mRepeatPasswordWrapper.setErrorEnabled(false);
            mRepeatPasswordWrapper.setError(null);
        }
        return true;
        }







    /**
     * To check email confirming email pattern
     *
     * @param email
     * @return boolean checking email validation
     */
    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.firstNameField:
                    validateFirstName();
                    break;
                case R.id.lastNameField:
                    validateLastName();
                    break;
                case R.id.signUpEmailField:
                    validateEmailAddress();
                    break;
                case R.id.repeatPassword_field:
                    validatePassword();
                    break;
//            if(view.getId()==R.id.firstNameField){
//                validateFirstName();
//            }
//            else if(view.getId() == R.id.lastNameField){
//                validateLastName();
//            }
//            else if(view.getId() == R.id.signUpEmailField){
//                validateEmailAddress();
//            }
            }
        }
    }
}



