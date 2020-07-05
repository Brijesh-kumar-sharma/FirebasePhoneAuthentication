package com.example.otpdesignlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private Button SentOtp,VerifyOtp;
    private EditText GetPhoneNumber,GetOtp;
    FirebaseAuth mAuth;
    private String CodeSent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        SentOtp=findViewById(R.id.sentOtp);
        VerifyOtp=findViewById(R.id.verifyOtp);
        GetPhoneNumber=findViewById(R.id.getPhoneNumber);
        GetOtp=findViewById(R.id.getOtp);

        SentOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();

            }
        });

        VerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });








    }
    private void verifyOtp()
    {
        String code=GetOtp.getText().toString().trim();
        if(code.isEmpty())
        {
            GetOtp.setError("Enter Otp");
            GetOtp.requestFocus();
            return;
        }
        else {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent, code);
            signInWithPhoneAuthCredential(credential);
        }


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here opens new activity
                            Toast.makeText(MainActivity.this, "Login Sucess", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,Second.class);
                            startActivity(intent);



                        } else {

                           if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                               Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_LONG).show();
                           }
                        }
                    }
                });
    }



    private void sendVerificationCode() {

        String Phone = GetPhoneNumber.getText().toString().trim();
        if (Phone.isEmpty()) {
            GetPhoneNumber.setError("Please Enter Phone Number");
            GetPhoneNumber.requestFocus();
            return;
        } else if (Phone.length() < 10 || Phone.length() > 10)
        {
            GetPhoneNumber.setError("Please Enter Correct Number");
            GetPhoneNumber.requestFocus();
            return;
        }
        else
        {
            String phoneNumber = "+91" +Phone ;


            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks


        }
    }
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(MainActivity.this,"sent",Toast.LENGTH_LONG).show();
                CodeSent=s;
            }
        };





    }
