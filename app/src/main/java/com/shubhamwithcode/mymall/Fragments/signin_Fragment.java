package com.shubhamwithcode.mymall.Fragments;

import static com.shubhamwithcode.mymall.Register_Activity.onResetPasswordFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shubhamwithcode.mymall.MainActivity;
import com.shubhamwithcode.mymall.R;


public class signin_Fragment extends Fragment {



    public signin_Fragment() {
        // Required empty public constructor
    }

    TextView dontHaveAnAccount;
    FrameLayout perantFrameLayout;
    FirebaseAuth auth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn;


    ImageView Crossbtn;
    EditText EmailSignIn,PasswordSignIn;
    Button SignIn;
    TextView forgot_password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_signin_, container, false);
        dontHaveAnAccount = view.findViewById(R.id.Registernow);
        perantFrameLayout = getActivity().findViewById(R.id.Register_framLayout);
        auth = FirebaseAuth.getInstance();

        Crossbtn = view.findViewById(R.id.signinCross);
        EmailSignIn = view.findViewById(R.id.etEmailSignIn);
        PasswordSignIn = view.findViewById(R.id.etPasswordSignIn);
        SignIn = view.findViewById(R.id.btnSignIn);
        forgot_password = view.findViewById(R.id.tvForgotPassword);

        if (disableCloseBtn){
            Crossbtn.setVisibility(View.GONE);
        }else {
            Crossbtn.setVisibility(View.VISIBLE);

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onResetPasswordFragment = true;
                setFragment(new forgot_Password());
            }
        });

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new signup_Fragment());
            }
        });

        Crossbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainIntent();
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailAndPassword();
            }
        });

        EmailSignIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        PasswordSignIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
     // transaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        transaction.replace(perantFrameLayout.getId(),fragment);
        transaction.commit();
    }

    private void checkInputs(){
        if (!TextUtils.isEmpty(EmailSignIn.getText())){
            if (!TextUtils.isEmpty(PasswordSignIn.getText())){
                SignIn.setEnabled(true);
                SignIn.setTextColor(Color.rgb(255,255,255));
            }else{
                SignIn.setEnabled(false);
                SignIn.setTextColor(Color.argb(50,255,255,255));
            }

        }else{
            SignIn.setEnabled(false);
            SignIn.setTextColor(Color.argb(50,255,255,255));
        }

    }

    private  void CheckEmailAndPassword(){
        if (EmailSignIn.getText().toString().matches(emailPattern)){
            if (PasswordSignIn.length() >= 8){

                SignIn.setEnabled(false);
                SignIn.setTextColor(Color.argb(50,255,255,255));

                auth.signInWithEmailAndPassword(EmailSignIn.getText().toString(),PasswordSignIn.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                   MainIntent();
                                }else {
                                    SignIn.setEnabled(true);
                                    SignIn.setTextColor(Color.rgb(255,255,255));
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else {
                Toast.makeText(getActivity(), "incorrect password", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "incorrect Email", Toast.LENGTH_SHORT).show();
        }

    }
    private void MainIntent(){
        if (disableCloseBtn){
            disableCloseBtn = false;
        }else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
        getActivity().finish();
    }
}