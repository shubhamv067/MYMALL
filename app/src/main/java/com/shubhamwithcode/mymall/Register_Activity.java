package com.shubhamwithcode.mymall;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.shubhamwithcode.mymall.Fragments.signin_Fragment;
import com.shubhamwithcode.mymall.Fragments.signup_Fragment;

public class Register_Activity extends AppCompatActivity {

    FrameLayout frameLayout;
    public static boolean onResetPasswordFragment = false;
    public static boolean setSignUpFragment = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        frameLayout = findViewById(R.id.Register_framLayout);
      getSupportActionBar().hide();

      if (setSignUpFragment){
       setSignUpFragment =false;
       setDefultefragment(new signup_Fragment());
      }else {
          setDefultefragment(new signin_Fragment());
      }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            signup_Fragment.disableCloseBtn = false;
            signin_Fragment.disableCloseBtn = false;
            if (onResetPasswordFragment){
                    setfragment(new signin_Fragment());
                    onResetPasswordFragment = false;
                  return false;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setDefultefragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(),fragment);
        transaction.commit();
    }

    private void setfragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(),fragment);
        transaction.commit();
    }
}