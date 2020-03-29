package com.example.rapportv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.R.style.Theme_Holo_Dialog_MinWidth;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextInputEditText Name, Job, City, PhoneNumber,Date,Uni ,Sama, CurrentCity;
    private DatePickerDialog.OnDateSetListener  mDatePickerDialog;
    private Dialog mPopUpDialog;
    private Button UMM, UMY1,UMY2,UMT,UMMG, SaveBtn;

    private TextView CancelTv;

    private ProgressDialog loadingBar;


    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    private String CurrentUid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        CurrentUid = mAuth.getCurrentUser().getUid();

        initialization();
        settingToolbar();
        DatePicking();

        Uni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUpDialog();
                mPopUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                mPopUpDialog.show();
            }
        });


        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   SaveAccountInformationButton();
            }
        });




    }

    private void SaveAccountInformationButton()
    {
        String sName, sJob, sCity,sPhoneNum, sSama,sUni,sCurCity,sDateOfGraduation;


        sName = Name.getText().toString();
        sJob = Job.getText().toString();
        sCity = City.getText().toString();
        sPhoneNum = PhoneNumber.getText().toString();

        sSama = Sama.getText().toString();
        sUni = Uni.getText().toString();
        sCurCity = CurrentCity.getText().toString();
        sDateOfGraduation = Date.getText().toString();


        if(       TextUtils.isEmpty(sName)
                ||TextUtils.isEmpty(sJob)
                ||TextUtils.isEmpty(sCity)
                ||TextUtils.isEmpty(sPhoneNum)
                ||TextUtils.isEmpty(sSama)
                ||TextUtils.isEmpty(sUni)
                ||TextUtils.isEmpty(sCurCity)
                ||TextUtils.isEmpty(sDateOfGraduation))
        {
            Toast.makeText(this, "Please Enter all Fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();


            HashMap userMap = new HashMap();
            userMap.put("A_name",sName);
            userMap.put("B_job",sJob);
            userMap.put("C_city",sCity);
            userMap.put("Phone_Number",sPhoneNum);


            userMap.put("D_samaNumber",sSama);
            userMap.put("E_university",sUni);
            userMap.put("Current_City",sCurCity);
            userMap.put("F_dateOfGraduation",sDateOfGraduation);


            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this, "Account has been created successfully...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().toString();
                        Toast.makeText(SetupActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }


            });


        }

    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void PopUpDialog()
    {
        mPopUpDialog = new Dialog(SetupActivity.this);
        mPopUpDialog.setContentView(R.layout.popup_uni);

        UMM = mPopUpDialog.findViewById(R.id.UMM);
        UMY1 = mPopUpDialog.findViewById(R.id.UMY1);
        UMY2 = mPopUpDialog.findViewById(R.id.UMY2);
        UMT = mPopUpDialog.findViewById(R.id.UMT);
        UMMG = mPopUpDialog.findViewById(R.id.UMMG);

        UMM.setOnClickListener(this);
        UMY1.setOnClickListener(this);
        UMY2.setOnClickListener(this);
        UMT.setOnClickListener(this);
        UMMG.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.UMM:
                Uni.setText("University of Medicine, Mandalay");
                mPopUpDialog.dismiss();
                break;
            case R.id.UMY1:
                Uni.setText("University of Medicine 1, Yangon");
                mPopUpDialog.dismiss();
                break;
            case R.id.UMY2:
                Uni.setText("University of Medicine 2, Yangon");
                mPopUpDialog.dismiss();
                break;
            case R.id.UMT:
                Uni.setText("University of Medicine, Taungyi");
                mPopUpDialog.dismiss();
                break;
            case R.id.UMMG:
                Uni.setText("University of Medicine, Magway");
                mPopUpDialog.dismiss();
                break;

        }

    }



    private void DatePicking() {



        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SetupActivity.this,
                        Theme_Holo_Dialog_MinWidth,
                        mDatePickerDialog,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });



        mDatePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                 c.set(Calendar.YEAR,year);
                 c.set(Calendar.MONTH,month);
                 c.set(Calendar.DAY_OF_MONTH,day);
                String date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());
                Date.setText(date);
            }
        };
    }

    private void initialization() {

        Date = findViewById(R.id.setup_date);
        Uni = findViewById(R.id.setup_university);

        Name = findViewById(R.id.setup_name);
        Job = findViewById(R.id.setup_occupation);
        City = findViewById(R.id.setup_city);
        PhoneNumber = findViewById(R.id.setup_phone_number);


        Sama = findViewById(R.id.setup_sama);
        CurrentCity = findViewById(R.id.setup_current_city);


        loadingBar = new ProgressDialog(this);

        CancelTv = findViewById(R.id.setup_cancel_tv);
        SaveBtn = findViewById(R.id.setup_save_btn);


        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUid);



    }

    private void settingToolbar()
    {
        mToolbar = (Toolbar) findViewById(R.id.setup_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile Setup");

    }



}
