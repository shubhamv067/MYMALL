package com.shubhamwithcode.mymall.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shubhamwithcode.mymall.MainActivity;
import com.shubhamwithcode.mymall.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class signup_Fragment extends Fragment {



    public signup_Fragment() {
        // Required empty public constructor
    }
    TextView AlreadyHaveAccount;
    FrameLayout perantFrameLayout;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    public static boolean disableCloseBtn;

    ImageView crossButton;
    Button Signupbtn;
    EditText FullName,SignupEmail,SignupPassword,SignupConfirmPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_, container, false);

        AlreadyHaveAccount = view.findViewById(R.id.SignIn);
        perantFrameLayout = getActivity().findViewById(R.id.Register_framLayout);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        crossButton =view.findViewById(R.id.signinCross);
        Signupbtn = view.findViewById(R.id.btnSignUp);
        SignupEmail = view.findViewById(R.id.etEmailSignUp);
        FullName = view.findViewById(R.id.etFullNameSignUp);
        SignupPassword = view.findViewById(R.id.etpasswordSignUp);
        SignupConfirmPassword = view.findViewById(R.id.etConformPasswordSignUp);

        if (disableCloseBtn){
            crossButton.setVisibility(View.GONE);
        }else {
            crossButton.setVisibility(View.VISIBLE);

        }


        return  view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              setFragment(new signin_Fragment());
            }
        });

        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainIntent();
            }
        });

        SignupEmail.addTextChangedListener(new TextWatcher() {
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
        FullName.addTextChangedListener(new TextWatcher() {
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
        SignupPassword.addTextChangedListener(new TextWatcher() {
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
        SignupConfirmPassword.addTextChangedListener(new TextWatcher() {
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



        Signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailAndPassword();
            }
        });

    }
    private void setFragment(Fragment fragment){

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
             // transaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
                transaction.replace(perantFrameLayout.getId(),fragment);
                transaction.commit();
            }

    private  void checkInputs(){
        if (!TextUtils.isEmpty(SignupEmail.getText())){
            if (!TextUtils.isEmpty(FullName.getText())){
                if (!TextUtils.isEmpty(SignupPassword.getText()) && SignupPassword.length() >= 8){
                    if (!TextUtils.isEmpty(SignupConfirmPassword.getText())){
                        Signupbtn.setEnabled(true);
                        Signupbtn.setTextColor(Color.rgb(255,255,255));
                    }else{
                        Signupbtn.setEnabled(false);
                        Signupbtn.setTextColor(Color.argb(50,255,255,255));
                    }
                }else{
                    Signupbtn.setEnabled(false);
                    Signupbtn.setTextColor(Color.argb(50,255,255,255));
                }
            }else{
                Signupbtn.setEnabled(false);
                Signupbtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            Signupbtn.setEnabled(false);
            Signupbtn.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void  CheckEmailAndPassword(){
        Drawable CustomErrorIcon = getResources().getDrawable(R.drawable.attention);
        CustomErrorIcon.setBounds(0,0,CustomErrorIcon.getIntrinsicWidth(),CustomErrorIcon.getIntrinsicHeight());
    if (SignupEmail.getText().toString().matches(emailPattern)){
        if (SignupPassword.getText().toString().equals(SignupConfirmPassword.getText().toString())){

            Signupbtn.setEnabled(false);
            Signupbtn.setTextColor(Color.argb(50,255,255,255));


            auth.createUserWithEmailAndPassword(SignupEmail.getText().toString(),SignupPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Map<String,Object> userData = new HashMap<>();
                            userData.put("fullName",FullName.getText().toString());
                            userData.put("email",SignupEmail.getText().toString());
                            userData.put("profile","");

                            firebaseFirestore.collection("USERS").document(auth.getUid()).set(userData)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()){

                                               CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(auth.getUid()).collection("USER_DATA");
                                               /// MAPS
                                               Map<String,Object> wishlistMap = new HashMap<>();
                                               wishlistMap.put("list_size",(long) 0);

                                               Map<String,Object> ratingsMap = new HashMap<>();
                                               ratingsMap.put("list_size",(long) 0);

                                               Map<String,Object> cartMap = new HashMap<>();
                                               cartMap.put("list_size",(long) 0);

                                               Map<String,Object> myAddressesMap = new HashMap<>();
                                               myAddressesMap.put("list_size",(long) 0);

                                               Map<String,Object> notificationsMap = new HashMap<>();
                                               notificationsMap.put("list_size",(long) 0);
                                               /// MAPS

                                               List<String> documentName = new ArrayList<>();
                                               documentName.add("MY_WISHLIST");
                                               documentName.add("MY_RATINGS");
                                               documentName.add("MY_CART");
                                               documentName.add("MY_ADDRESSES");
                                               documentName.add("MY_NOTIFICATIONS");

                                               List<Map<String,Object>> documentField = new ArrayList<>();
                                               documentField.add(wishlistMap);
                                               documentField.add(ratingsMap);
                                               documentField.add(cartMap);
                                               documentField.add(myAddressesMap);
                                               documentField.add(notificationsMap);

                                               for (int x = 0; x< documentName.size(); x++){
                                                   int finalX = x;
                                                   userDataReference.document(documentName.get(x)).set(documentField.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()){
                                                               if (finalX == documentName.size() -1){
                                                                   MainIntent();
                                                               }
                                                           }else {
                                                               Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                               Signupbtn.setEnabled(true);
                                                               Signupbtn.setTextColor(Color.rgb(255,255,255));
                                                           }
                                                       }
                                                   });
                                               }
                                           }else {
                                               Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                        }else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Signupbtn.setEnabled(true);
                            Signupbtn.setTextColor(Color.argb(50,255,255,255));
                        }
                        }
                    });
        }else {
                SignupConfirmPassword.setError("Password doesn't matched!",CustomErrorIcon);
        }
    }else {
            SignupEmail.setError("Invalid Email!",CustomErrorIcon);
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
