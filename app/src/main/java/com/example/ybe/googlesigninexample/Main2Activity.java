package com.example.ybe.googlesigninexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Button logout;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final String TAG = "MainActivity3";
    public static final String ANONYMOUS = "anonymous";

    private GoogleApiClient mGoogleApiClient;

    String mPhotoUrl, mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        logout = findViewById(R.id.logout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        /*
//          The {@link android.support.v4.view.PagerAdapter} that will provide
//          fragments for each of the sections. We use a
//          {@link FragmentPagerAdapter} derivative, which will keep every
//          loaded fragment in memory. If this becomes too memory intensive, it
//          may be best to switch to a
//          {@link android.support.v4.app.FragmentStatePagerAdapter}.
//        *
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();


        if (mUser == null)  {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            mUsername = mUser.getDisplayName();
            if (mUser.getPhotoUrl() != null) {
                mPhotoUrl = mUser.getPhotoUrl().toString();
            }
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main2, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FirebaseUser mUser = mAuth.getCurrentUser();
            Bundle bundle = new Bundle();
            bundle.putString("userUid", mUser.getUid());

            switch (position)  {
                case 0: {
                        //"architecture" fragment.
                        Tab1Architecture fragment = new Tab1Architecture();
                        fragment.setArguments(bundle);
                        return fragment;
                    }
                    case 1: {
                        //"literature" fragment.
                        Tab2Literature fragment = new Tab2Literature();
                        fragment.setArguments(bundle);
                        return fragment;
                    }
                    case 2: {
                        //"music" fragment.
                        Tab3Music fragment = new Tab3Music();
                        fragment.setArguments(bundle);
                        return fragment;
                    }
                    case 3: {
                        //"painting" fragment.
                        Tab4Painting fragment = new Tab4Painting();
                        fragment.setArguments(bundle);
                        return fragment;
                    }
                    case 4: {
                        //"sculpture" fragment.
                        Tab5Sculpture fragment = new Tab5Sculpture();
                        fragment.setArguments(bundle);
                        return fragment;
                    }
                    case 5: {
                        //"theater" fragment.
                        Tab6Theater fragment = new Tab6Theater();
                        fragment.setArguments(bundle);
                        return fragment;
                    }

                    case 6: {
                        //"last page" fragment.

                    }
                    default: {
                        return new Tab1Architecture();
                    }
                }
            }

        @Override
        public int getCount() {
            //Shows 7 Pages
            return 7;
        }
    }
    private void signOut() {
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mUsername = ANONYMOUS;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
