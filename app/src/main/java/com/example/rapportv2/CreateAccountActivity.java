package com.example.rapportv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText RegisterEmail, RegisterPassword, RegisterConfirmPassword;
    private Button CreateBtn;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        Initialization();

        CreateingAccount();



    }

    private void CreateingAccount()
    {
       CreateBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               String email = RegisterEmail.getText().toString();
               String password = RegisterPassword.getText().toString();
               String confirm = RegisterConfirmPassword.getText().toString();

               if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm))
               {
                   Toast.makeText(CreateAccountActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
               }
               else
               {

                   if(!password.equals(confirm))
                   {
                       Toast.makeText(CreateAccountActivity.this, "Password do not match with confirm password...", Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       loadingBar.setTitle("Creating New Account");
                       loadingBar.setMessage("Please wait, while your account is being created...");
                       loadingBar.show();
                       loadingBar.setCanceledOnTouchOutside(true);
                       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful())
                               {
                                   sendUserToSetupActivity();
                                   Toast.makeText(CreateAccountActivity.this, "your account has been created successfully...", Toast.LENGTH_SHORT).show();
                                   loadingBar.dismiss();
                               }
                               else
                               {
                                   String message = task.getException().getMessage();
                                   Toast.makeText(CreateAccountActivity.this, "Error Occurred : " + message, Toast.LENGTH_SHORT).show();
                                   loadingBar.dismiss();
                               }
                           }
                       });
                   }

               }
           }
       });

    }

    private void sendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(CreateAccountActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            SendUserToMainActivity();
        }
    }



    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(CreateAccountActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void Initialization()
    {
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        RegisterEmail = findViewById(R.id.register_email);
        RegisterPassword = findViewById(R.id.register_password);
        RegisterConfirmPassword = findViewById(R.id.register_confirm_password);
        CreateBtn = findViewById(R.id.register_create_btn);
    }
}
