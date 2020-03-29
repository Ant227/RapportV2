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
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private EditText LoginEmail, LoginPassword;
    private Button LoginSignin;
    private TextView LoginCreateNewAccount;
    private FirebaseAuth mAuth;

    private ProgressDialog LoadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Initialization();
        SignInButton();
        CreateAccount();


    }

    private void CreateAccount()
    {
        LoginCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            SendUserToMainActivity();
        }
    }





    private void SignInButton()
    {
        LoginSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });
    }

    private void AllowUserToLogin()
    {
        String user_email = LoginEmail.getText().toString();
        String user_password = LoginPassword.getText().toString();

        if(TextUtils.isEmpty(user_email) || TextUtils.isEmpty(user_password))
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            LoadingBar.setTitle("Login");
            LoadingBar.setMessage("Logging In...");
            LoadingBar.setCanceledOnTouchOutside(true);
            LoadingBar.show();

                mAuth.signInWithEmailAndPassword(user_email,user_password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    SendUserToMainActivity();
                                    Toast.makeText(LoginActivity.this, "Logged in successfully...", Toast.LENGTH_SHORT).show();
                                    LoadingBar.dismiss();
                                }
                                else
                                {
                                    String error_message = task.getException().toString();
                                    Toast.makeText(LoginActivity.this, "Error : "+ error_message, Toast.LENGTH_SHORT).show();
                                   LoadingBar.dismiss();

                                }
                            }
                        });
        }
    }

    private void SendUserToMainActivity()
    {
        Intent ToMainActivity = new Intent(LoginActivity.this,MainActivity.class);
        ToMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ToMainActivity);
    }

    private void Initialization()
    {
        LoginEmail = findViewById(R.id.login_email);
        LoginPassword = findViewById(R.id.login_password);
        LoginSignin = findViewById(R.id.login_signInBtn);
        LoginCreateNewAccount = findViewById(R.id.login_create_account_btn);



        mAuth = FirebaseAuth.getInstance();
        LoadingBar = new ProgressDialog(this);
    }

}
