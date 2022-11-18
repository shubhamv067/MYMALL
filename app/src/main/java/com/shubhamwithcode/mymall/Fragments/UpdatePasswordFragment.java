package com.shubhamwithcode.mymall.Fragments;

import android.app.Dialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shubhamwithcode.mymall.R;


public class UpdatePasswordFragment extends Fragment {



    public UpdatePasswordFragment() {
        // Required empty public constructor
    }
    private EditText oldPassword,newPassword,ConfirmNewPassword;
    private Button updateBtn;
    private String email;
    private Dialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_update_password, container, false);

        oldPassword = view.findViewById(R.id.old_password);
        newPassword = view.findViewById(R.id.new_password);
        ConfirmNewPassword = view.findViewById(R.id.confirm_new_password);
        updateBtn = view.findViewById(R.id.update_password_btn);

        /////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_bg));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        /////loading dialog


        email = getArguments().getString("Email");


        oldPassword.addTextChangedListener(new TextWatcher() {
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
        newPassword.addTextChangedListener(new TextWatcher() {
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
        ConfirmNewPassword.addTextChangedListener(new TextWatcher() {
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

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailAndPassword();
            }
        });
        return view;
    }

    private  void checkInputs(){
        if (!TextUtils.isEmpty(oldPassword.getText())){
            if (!TextUtils.isEmpty(newPassword.getText())){
                if (!TextUtils.isEmpty(ConfirmNewPassword.getText()) && ConfirmNewPassword.length() >= 8){

                        updateBtn.setEnabled(true);
                    updateBtn.setTextColor(Color.rgb(255,255,255));

                }else{
                    updateBtn.setEnabled(false);
                    updateBtn.setTextColor(Color.argb(50,255,255,255));
                }
            }else{
                updateBtn.setEnabled(false);
                updateBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            updateBtn.setEnabled(false);
            updateBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void CheckEmailAndPassword(){
        Drawable CustomErrorIcon = getResources().getDrawable(R.drawable.attention);
        CustomErrorIcon.setBounds(0,0,CustomErrorIcon.getIntrinsicWidth(),CustomErrorIcon.getIntrinsicHeight());

            if (newPassword.getText().toString().equals(ConfirmNewPassword.getText().toString())){

                loadingDialog.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                AuthCredential credential = EmailAuthProvider.getCredential(email,oldPassword.getText().toString());

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                               oldPassword.setText(null);
                                               newPassword.setText(null);
                                               ConfirmNewPassword.setText(null);
                                               getActivity().finish();
                                                Toast.makeText(getContext(), "PasswordUpdated SuccessFully!", Toast.LENGTH_SHORT).show();
                                            }else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                            }
                                            loadingDialog.dismiss();
                                        }
                                    });
                                }else {
                                    loadingDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                                }else {
                ConfirmNewPassword.setError("Password doesn't matched!",CustomErrorIcon);
            }
    }


}