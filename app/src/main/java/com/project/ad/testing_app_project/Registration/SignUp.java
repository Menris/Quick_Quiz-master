package com.project.ad.testing_app_project.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ad.testing_app_project.R;
import com.project.ad.testing_app_project.Starting;

import java.util.Objects;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    //edit text fields for inputing information
    private EditText inputName, inputEmail, inputPassword, inputGroup;
    //layout for wrong input (showing error text)
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutGroup, inputLayoutRadio;
    //button for registration current user
    private Button btnSignUp;
    private ProgressDialog progressDialog;
    private RadioButton radioButton_studnent, radioButton_teacher;
    private RadioGroup radioGroup;
    String Name, Email, Password;
    private String role, groupName;
    private Integer radioID = -1;
    //check if its needed to enter group name for student or teacher
    private Boolean validateGroupName = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutGroup = (TextInputLayout) findViewById(R.id.input_layout_group);
        inputLayoutRadio = (TextInputLayout) findViewById(R.id.input_layout_radioGroup);

        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputGroup = (EditText) findViewById(R.id.input_group);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioID = radioGroup.getCheckedRadioButtonId();

        btnSignUp = (Button) findViewById(R.id.btn_signup);

        progressDialog = new ProgressDialog(this);

        inputName.addTextChangedListener(new SignUp.MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new SignUp.MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new SignUp.MyTextWatcher(inputPassword));
        inputGroup.addTextChangedListener(new SignUp.MyTextWatcher(inputGroup));
        btnSignUp.setOnClickListener(this);
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (validateGroupName) {
            if (!validateGroup()) {
                return;
            }
        }
        if (!validateRadio()) {
            return;
        }

        Email = inputEmail.getText().toString().trim();
        Password = inputPassword.getText().toString().trim();
        Name = inputName.getText().toString().trim();
        groupName = inputGroup.getText().toString().trim();

        if (TextUtils.isEmpty(Email)) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(Password)) {
            //email is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        //PROGRESS krujochek
        progressDialog.setMessage("Registering User...");
        progressDialog.show();


        //EBAT CHTO ETO
        firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            saveUserInformation();

                            //user is succesfully registered and logged in
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Starting.class));
                                    //start the profile activity here
                                }
                            }, 1000);


                        } else {
                            Toast.makeText(SignUp.this, "FAIL, try again", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }


    public void saveUserInformation() {

        //Getting values from database


        //creating a userinformation object
        SignUp_userInfo userInformation = new SignUp_userInfo();

        userInformation.setName(Name);
        userInformation.setRole(role);
        userInformation.setEmail(Email);
        userInformation.setGroup(groupName);

        //getting the current logged in user
        user = firebaseAuth.getCurrentUser();
        if (Objects.equals(role, "students")) {
            Log.w("Amir", "user is student");
            databaseReference.child("userInformation").child(user.getUid()).setValue(userInformation);
        } else if (Objects.equals(role, "teachers")) {
            userInformation.setGroup("teachers");
            databaseReference.child("userInformation").child(user.getUid()).setValue(userInformation);
        }
        //displaying a success toast
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
    }


    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Enter the password 6 characters long");
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validateGroup() {
        if (inputGroup.getText().toString().trim().isEmpty()) {
            inputLayoutGroup.setError("Enter you group number");
            requestFocus(inputGroup);
            return false;
        } else {
            inputLayoutGroup.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateRadio() {
        if (radioID == -1) {
            inputLayoutRadio.setError("Choose your role");
            requestFocus(radioGroup);
            return false;
        } else {
            inputLayoutRadio.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String Email) {
        return !TextUtils.isEmpty(Email) && android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == btnSignUp) {
            submitForm();
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
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_group:
                    validatePassword();
                    break;
                case R.id.radioGroup:
                    validateRadio();
                    break;
            }
        }
    }


    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        //creating a userinformation object


        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.student_radio:
                if (checked) {
                    //groupNumber.setVisibility(View.VISIBLE);
                    //check if user going to join student database
                    Log.w("Amir", " ===  students");
                    role = "students";
                    inputGroup.setVisibility(View.VISIBLE);
                    radioID = 1;
                    validateGroupName = true;
                    break;
                }
            case R.id.teacher_radio:
                if (checked) {
                    // groupNumber.setVisibility(View.GONE);
                    //check if user going to join teacher database
                    Log.w("Amir", " ===  teachers");
                    role = "teachers";
                    radioID = 1;
                    inputGroup.setVisibility(View.GONE);
                    validateGroupName = false;
                    break;
                }
        }
    }
}
