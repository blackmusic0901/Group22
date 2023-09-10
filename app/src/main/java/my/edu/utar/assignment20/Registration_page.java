package my.edu.utar.assignment20;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class Registration_page extends AppCompatActivity {

    EditText editTextEmail, editTextPass, editTextPassConfirm, editTextName, editTextDate;
    Button btReg;
    Calendar calendar;
    FirebaseAuth mAuth;
    String email, password,passwordConfirm,name,dateBirth;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(Registration_page.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.etEmail);
        editTextName = findViewById(R.id.etUserName);
        editTextDate = findViewById(R.id.etDate);
        editTextPass = findViewById(R.id.etPassword);
        editTextPassConfirm = findViewById(R.id.etPasswordConfirm);
        progressBar=findViewById(R.id.progressBar);

        btReg = findViewById(R.id.RNext);

        calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DATE,date);

                updateCalendar();
            }
            private void updateCalendar(){
                String Format="dd/MM/yyyy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(Format, Locale.CHINESE);

                editTextDate.setText(dateFormat.format(calendar.getTime()));
            }
        };
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Registration_page.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE)).show();
            }
        });


        btReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                name=editTextName.getText().toString();
                password = editTextPass.getText().toString();
                passwordConfirm = editTextPassConfirm.getText().toString();
                dateBirth=editTextDate.getText().toString();
                String userId=email.replace(".",",");

                database=FirebaseDatabase.getInstance();
                reference=database.getReference("Users");

                int compare=password.compareTo(passwordConfirm);
                progressBar.setVisibility(View.VISIBLE);

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Registration_page.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Registration_page.this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(Registration_page.this,"Enter name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Registration_page.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(compare!=0){
                    Toast.makeText(Registration_page.this,"Password Not Match",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(name)&&compare==0){
                    Toast.makeText(Registration_page.this,"SignUp Successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Registration_page.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    loginHelper helper=new loginHelper(name,email,password,dateBirth);
                                    reference.child(userId).setValue(helper);
                                    Toast.makeText(Registration_page.this, "Authentication completed.", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(Registration_page.this,ProfileSetup_page.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user
                                    Toast.makeText(Registration_page.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
    }
}