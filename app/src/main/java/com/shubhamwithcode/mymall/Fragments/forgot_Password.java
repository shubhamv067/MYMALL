package com.shubhamwithcode.mymall.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.TransitionManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.shubhamwithcode.mymall.R;


public class forgot_Password extends Fragment {



    public forgot_Password() {
        // Required empty public constructor
    }


    TextView goBack;
    EditText Email_forgot_password;
    Button Reset_Password_btn;
    FrameLayout perantFrameLayout;
    FirebaseAuth auth;
    ViewGroup EmailIconContenur;
    ImageView EmailIcon;
    TextView EmailIconText;
    ProgressBar progressBar;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot__password, container, false);

        goBack = view.findViewById(R.id.goBack_forgotPassword);
        Email_forgot_password = view.findViewById(R.id.et_forgot_password_email);
        Reset_Password_btn = view.findViewById(R.id.tv_reset_password_btn);
        perantFrameLayout = getActivity().findViewById(R.id.Register_framLayout);
        auth = FirebaseAuth.getInstance();

         EmailIconContenur = view.findViewById(R.id.forgot_password_email_icon_contenur);
         EmailIcon = view.findViewById(R.id.forgot_password_email_icon);
         EmailIconText = view.findViewById(R.id.forgot_password_email_icon_text);
         progressBar = view.findViewById(R.id.progressBar);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Reset_Password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(EmailIconContenur);
                EmailIconText.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(EmailIconContenur);
                EmailIcon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                Reset_Password_btn.setEnabled(false);
                Reset_Password_btn.setTextColor(Color.argb(50,255,255,255));

               auth.sendPasswordResetEmail(Email_forgot_password.getText().toString())
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){

                                   ScaleAnimation scaleAnimation = new ScaleAnimation(1,0,1,0,EmailIconText.getWidth()/2,EmailIconText.getHeight()/2);//EmailIconText
                                   scaleAnimation.setDuration(100);
                                   scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                   scaleAnimation.setRepeatMode(Animation.REVERSE);
                                   scaleAnimation.setRepeatCount(1);

                                   scaleAnimation.setAnimationListener(new Animation.AnimationListener(){
                                       @Override
                                       public void onAnimationStart(Animation animation) {

                                       }

                                       @Override
                                       public void onAnimationEnd(Animation animation) {
                                           EmailIconText.setText("Recovery email sent successfully ! check your inbox");
                                           EmailIconText.setTextColor(getResources().getColor(R.color.successGreen));

                                           TransitionManager.beginDelayedTransition(EmailIconContenur);
                                           EmailIconText.setVisibility(View.VISIBLE);//EmailIconText
                                       }

                                       @Override
                                       public void onAnimationRepeat(Animation animation) {
                                           EmailIcon.setImageResource(R.drawable.greenmail);//EmailIconText

                                       }

                                   });

                                   EmailIconText.startAnimation(scaleAnimation);//EmailIconText
                                   progressBar.setVisibility(View.GONE);

                               }else {
                                   String error = task.getException().getMessage();
                                   progressBar.setVisibility(View.GONE);



                                   EmailIconText.setText(error);

                                   EmailIconText.setTextColor(getResources().getColor(R.color.colour_primary));
                                   TransitionManager.beginDelayedTransition(EmailIconContenur);
                                   EmailIconText.setVisibility(View.VISIBLE);

                                   Reset_Password_btn.setEnabled(true);
                                   Reset_Password_btn.setTextColor(Color.rgb(255,255,255));
                                }
                              // EmailIcon.setVisibility(View.GONE);

                           }
                       });
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new signin_Fragment());
            }
        });

        Email_forgot_password.addTextChangedListener(new TextWatcher() {
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
        if (TextUtils.isEmpty(Email_forgot_password.getText())){
            Reset_Password_btn.setEnabled(false);
            Reset_Password_btn.setTextColor(Color.argb(50,255,255,255));

        }else {
            Reset_Password_btn.setEnabled(true);
            Reset_Password_btn.setTextColor(Color.rgb(255,255,255));
        }
    }
}