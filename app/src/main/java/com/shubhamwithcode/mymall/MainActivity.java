package com.shubhamwithcode.mymall;

import static com.shubhamwithcode.mymall.Register_Activity.setSignUpFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shubhamwithcode.mymall.Fragments.signin_Fragment;
import com.shubhamwithcode.mymall.Fragments.signup_Fragment;
import com.shubhamwithcode.mymall.ui.MyMallFragment;
import com.shubhamwithcode.mymall.ui.accountFragment;
import com.shubhamwithcode.mymall.ui.myCartFragment;
import com.shubhamwithcode.mymall.ui.myOrdersFragment;
import com.shubhamwithcode.mymall.ui.myRewardsFragment;
import com.shubhamwithcode.mymall.ui.wishlistFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private static final int MY_MALL_FRAGMENT = 0;
    private static final int MY_CART_FRAGMENT = 1;
    private static final int MY_ORDER_FRAGMENT = 2;
    private static final int MY_WISHLIST_FRAGMENT = 3;
    private static final int MY_REWARD_FRAGMENT = 4;
    private static final int MY_ACCOUNT_FRAGMENT = 5;
    public static Boolean showCart = false;
    public static Activity mainActivity;
    public static boolean resetMainActivity;

    private AppBarConfiguration mAppBarConfiguration;
    private int currentFragment = -1;
    private ImageView ActionBar_logo;
    public static DrawerLayout drawer;
    Dialog signInDialog;
    Toolbar toolbar;
    NavigationView navigationView;
    FrameLayout frameLayout;
    Window window;
    private FirebaseUser currentUser;
    private TextView badgeCount;
    private int scrollFlags;
    private AppBarLayout.LayoutParams params;
    private CircleImageView profileView;
    private TextView fullname,email;
    private ImageView addProfileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBar_logo = findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        frameLayout = findViewById(R.id.nav_host_fragment_content_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);


        frameLayout = findViewById(R.id.nav_host_fragment_content_main);

        profileView = navigationView.getHeaderView(0).findViewById(R.id.main_profile_image);
        fullname = navigationView.getHeaderView(0).findViewById(R.id.main_fullname);
        email = navigationView.getHeaderView(0).findViewById(R.id.main_email);
        addProfileIcon = navigationView.getHeaderView(0).findViewById(R.id.add_profile_icon);

        if (showCart) {
            mainActivity = this;
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart", new myCartFragment(), -2);
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.open_nav, R.string.close_nav);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            setfragment(new MyMallFragment(), MY_MALL_FRAGMENT);
        }


        signInDialog = new Dialog(MainActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialogue);
        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        Button dialogSignInBtn = signInDialog.findViewById(R.id.Quantity_Cancel_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.Quantity_OK_btn);
        final Intent registerIntent = new Intent(MainActivity.this,Register_Activity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin_Fragment.disableCloseBtn = true;
                signup_Fragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin_Fragment.disableCloseBtn = true;
                signup_Fragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawer.closeDrawer(GravityCompat.START);
                if (currentUser != null) {
                    drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                        @Override
                        public void onDrawerClosed(View drawerView) {
                            super.onDrawerClosed(drawerView);
                            int id = menuItem.getItemId();
                            if (id == R.id.nav_mymall) {

                                ActionBar_logo.setVisibility(View.VISIBLE);
                                invalidateOptionsMenu();
                                setfragment(new MyMallFragment(), MY_MALL_FRAGMENT);
                            } else if (id == R.id.nav_orders) {

                                gotoFragment("My Order", new myOrdersFragment(), MY_ORDER_FRAGMENT);
                            } else if (id == R.id.nav_Rewards) {

                                gotoFragment(" My Rewards", new myRewardsFragment(), MY_REWARD_FRAGMENT);
                            } else if (id == R.id.nav_Cart) {

                                gotoFragment("My Cart", new myCartFragment(), MY_CART_FRAGMENT);
                            } else if (id == R.id.nav_Wishlist) {

                                gotoFragment(" My Wishlist", new wishlistFragment(), MY_WISHLIST_FRAGMENT);
                            } else if (id == R.id.nav_Account) {

                                gotoFragment(" My Account", new accountFragment(), MY_ACCOUNT_FRAGMENT);
                            }else if (id == R.id.nav_SignOut){
                                FirebaseAuth.getInstance().signOut();
                                DBqueries.clearData();
                                Intent registerIntent = new Intent(MainActivity.this,Register_Activity.class);
                                startActivity(registerIntent);
                                finish();
                            }
                            drawer.removeDrawerListener(this);
                        }
                    });
                    return true;
                }else {
                    signInDialog.show();
                    return false;

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null){
            navigationView.getMenu().getItem(navigationView.getMenu().size() -1).setEnabled(false);
        }else {
            if (DBqueries.email == null) {
                FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DBqueries.fullName = task.getResult().getString("fullName");
                            DBqueries.email = task.getResult().getString("email");
                            DBqueries.profile = task.getResult().getString("profile");

                            fullname.setText(DBqueries.fullName);
                            email.setText(DBqueries.email);
                            if (DBqueries.profile.equals("")) {
                                addProfileIcon.setVisibility(View.VISIBLE);
                            } else {
                                addProfileIcon.setVisibility(View.INVISIBLE);
                                Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.avtar)).into(profileView);
                            }

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                fullname.setText(DBqueries.fullName);
                email.setText(DBqueries.email);
                if (DBqueries.profile.equals("")) {
                    profileView.setImageResource(R.drawable.avtar);
                    addProfileIcon.setVisibility(View.VISIBLE);
                } else {
                    addProfileIcon.setVisibility(View.INVISIBLE);
                    Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.avtar)).into(profileView);
                }

            }
            navigationView.getMenu().getItem(navigationView.getMenu().size() -1).setEnabled(true);

        }
        if (resetMainActivity){
            ActionBar_logo.setVisibility(View.VISIBLE);
            resetMainActivity = false;
            setfragment(new MyMallFragment(),MY_MALL_FRAGMENT);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DBqueries.checkNotifications(true,null);
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);

        }else {
            if (currentFragment == MY_MALL_FRAGMENT){
                currentFragment = -1;
                super.onBackPressed();
            }else {
                if (showCart){
                    mainActivity = null;
                    showCart = false;
                    finish();
                }else {
                    ActionBar_logo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setfragment(new MyMallFragment(), MY_MALL_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == MY_MALL_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem cartItem = menu.findItem(R.id.main_my_cart);

                cartItem.setActionView(R.layout.badge_layout);
                ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
                badgeIcon.setImageResource(R.drawable.cart);
                badgeIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
                if (currentUser != null){
                    if (DBqueries.cartList.size() == 0) {
                        DBqueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this), false,badgeCount,new TextView(MainActivity.this));
                    }else {
                            badgeCount.setVisibility(View.VISIBLE);
                            if (DBqueries.cartList.size() < 99) {
                            badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                        }else {
                            badgeCount.setText("99");
                        }
                    }
                }
                cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentUser == null) {
                            signInDialog.show();
                        }else {
                            gotoFragment("My Cart", new myCartFragment(), MY_CART_FRAGMENT);
                        }
                    }
                });

            MenuItem notifyItem = menu.findItem(R.id.main_notification);

            notifyItem.setActionView(R.layout.badge_layout);
            ImageView notifyIcon = notifyItem.getActionView().findViewById(R.id.badge_icon);
            notifyIcon.setImageResource(R.drawable.notification);
            badgeIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            TextView notifyCount = notifyItem.getActionView().findViewById(R.id.badge_count);
            if (currentUser != null){
                DBqueries.checkNotifications(false,notifyCount);
            }
            notifyItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent notificationIntent = new Intent(MainActivity.this,NotificationActivity.class);
                    startActivity(notificationIntent);
                }
            });
        }

        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_search) {
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (id == R.id.main_notification) {
            Intent notificationIntent = new Intent(this,NotificationActivity.class);
            startActivity(notificationIntent);
            return true;
        } else if (id == R.id.main_my_cart) {

            if (currentUser == null) {
                signInDialog.show();
            }else {
                gotoFragment("My Cart", new myCartFragment(), MY_CART_FRAGMENT);
            }

            return true;
        }else if (id == android.R.id.home){
            if (showCart){
                mainActivity = null;
                showCart = false;
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title,Fragment fragmentt,int FragmentNo) {
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        ActionBar_logo.setVisibility(View.GONE);
        setfragment(fragmentt, FragmentNo);
        if (FragmentNo == MY_CART_FRAGMENT || showCart){
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        }else {
            params.setScrollFlags(scrollFlags);
        }

    }

    private void setfragment(Fragment fragment, int fragmentNo) {
        if (currentFragment != fragmentNo) {
            if (fragmentNo == MY_REWARD_FRAGMENT){
                window.setStatusBarColor(Color.parseColor("#5B04B1"));
                toolbar.setBackgroundColor(Color.parseColor("#5B04B1"));
            }else {
                window.setStatusBarColor(getResources().getColor(R.color.colour_primary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colour_primary));
            }
            currentFragment = fragmentNo;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(frameLayout.getId(), fragment);
            transaction.commit();
        }
    }
}
