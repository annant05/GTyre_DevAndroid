package com.developer.annant.gopaltyres;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.developer.annant.gopaltyres.drawer_activities.AboutDeveloperDrawerActivity;
import com.developer.annant.gopaltyres.drawer_activities.AddTyreInDbActivity;
import com.developer.annant.gopaltyres.drawer_activities.FeedbackDrawerActivity;
import com.developer.annant.gopaltyres.drawer_activities.ShopImageDrawerActivity;
import com.developer.annant.gopaltyres.drawer_activities.ShopInfoDrawerActivity;
import com.developer.annant.gopaltyres.fragments.LoadDataBaseFragment;
import com.developer.annant.gopaltyres.fragments.LoadDataFromSqliteDb;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {//Need TO write a method to solve error

    //Above are Variable and Object Declaration
    public static final int RC_SIGN_IN = 1;
    String TAG = "MAIN_ACTIVITY";
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    AdView mAdView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String URL = "https://api.myjson.com/bins/15nk89";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // modified Drawer Code
        toolbar = findViewById(R.id.toolbar);
        //  toolbar.collapseActionView();
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.activity_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);  //Need TO write a method to solve error


        //Modified Code Ends
        // startActivity(new Intent(MainActivity.this,WebData.class));


        //Start Tab Layout And AdMob Code
        viewPager = findViewById(R.id.view_pagerid);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));

        tabLayout = findViewById(R.id.tabs_viewid);
        tabLayout.setupWithViewPager(viewPager);

        //Start AdView Code
        mAdView = findViewById(R.id.adView);
//        mAdView.setAdSize(AdSize.SMART_BANNER);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.test_deviceid))
                .build();        // All emulators
        //.addTestDevice("8C8485E8FF86CDBA3F9E16C49523FAAA")  // An example device ID
        //.build();

        mAdView.loadAd(request);


        //End TabLayout and AdMob code

        firebaseAuthentication();

    }  // End Of onCreate Method


    private void firebaseAuthentication() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is signed in
                    Toast.makeText(MainActivity.this, "You are signed in. Welcome!", Toast.LENGTH_SHORT).show();


                } else {
                    //user is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                            )
                                    )
                                    .build(),
                            RC_SIGN_IN);

                }
            }
        };


    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.activity_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }// You Do understand the Use of above method


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    //Below is the method we were going to use for correcting the errors//  Important

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        // Enter All your drawer calling code here
        // For example Fragments and Activity


        switch (item.getItemId()) {

           /* case R.id.nav_home:
                startActivity(new Intent(MainActivity.this, HomeDrawerActivity.class));
                break;

                */
            case R.id.nav_feedback:
                startActivity(new Intent(MainActivity.this, FeedbackDrawerActivity.class));
                break;
            case R.id.nav_addTyre:
                startActivity(new Intent(MainActivity.this, AddTyreInDbActivity.class));
                break;
            case R.id.nav_shop_info:
                startActivity(new Intent(MainActivity.this, ShopInfoDrawerActivity.class));
                break;
            case R.id.nav_shop_image:
                startActivity(new Intent(MainActivity.this, ShopImageDrawerActivity.class));
                break;

            case R.id.about_dev:
                startActivity(new Intent(MainActivity.this, AboutDeveloperDrawerActivity.class));
                break;

            default:
                //  startActivity(new Intent(MainActivity.this, HomeDrawerActivity.class));
                break;
        }


        DrawerLayout drawer = findViewById(R.id.activity_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }


    //Drawer Code Ends Here
////////////////////////////////////////////////////////////////////////////////////////////////////


    /// Start AdmoB functionss
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // if (mAuthListener != null) {
        //   mAuth.removeAuthStateListener(mAuthListener);
        // }
    }


    // Start TabLayout Code

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    ////// End AdMob And TabLayout code Function in main

    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments[] = {"Firebase", "Sql DB"}; //, "Car", "Truck", "Tractor", "MiniTruck"};

        CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LoadDataBaseFragment();
                case 1:
                    return new LoadDataFromSqliteDb();

               /* case 2:
                    return new TruckFragment();
                case 3:
                    return new LoadDataBaseFragment();
                case 4:
                    return new MiniTruckFragment();
                */
                default:
                    return null;//new FragmentFirst();
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }

    }

}





