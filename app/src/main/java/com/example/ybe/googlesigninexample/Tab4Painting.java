package com.example.ybe.googlesigninexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 18.12.2017.
 */

public class Tab4Painting extends Fragment {

    public TextView art_name, artist_name, description;
    public ImageView art_image;
    public ImageButton like_button, unlike_button;
    public ProgressBar like_bar;
    public LinearLayout like_buttons;

    private static final String TAG = "Tab4Painting";

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    String userUid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4_painting, container, false);
        //Passed Parameter from Main2Activity.
        Bundle args = this.getArguments();
        userUid = args.getString("userUid");

        //Views
        art_name = rootView.findViewById(R.id.art_name);
        artist_name = rootView.findViewById(R.id.artist_name);
        description = rootView.findViewById(R.id.description);
        art_image = rootView.findViewById(R.id.art_image);
        like_button = rootView.findViewById(R.id.like_button);
        unlike_button = rootView.findViewById(R.id.unlike_button);
        like_buttons = rootView.findViewById(R.id.like_buttons);
        like_bar = rootView.findViewById(R.id.like_bar);

        //Policy for retrieving image from URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DatabaseReference dateRef = database.getReference("Date/" + getDate());

        Log.i(TAG, "DatabaseReference of Daily, dateRef is: " + dateRef.toString());

        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final DailyPlan tempDaily = dataSnapshot.getValue(DailyPlan.class);
                Log.i(TAG, tempDaily.toString());

                DatabaseReference artRef = database.getReference("ArtWorks/ArtType/painting/" + tempDaily.getPainting() + "/");
                Log.i(TAG, "DatabaseReference of Art, artRef is: " + artRef.toString());

                artRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Art art = dataSnapshot.getValue(Art.class);
                        Bitmap bitmap = loadImageFromWebOperations(art.getUrl());
                        art_name.setText(tempDaily.getPainting());
                        artist_name.setText(art.getArtist());
                        description.setText(art.getDescription());
                        art_image.setImageBitmap(bitmap);

                        if (art.likes.containsKey(userUid) || art.unlikes.containsKey(userUid)) {
                            showBar(art.likeCount, art.unlikeCount);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "artRef has been cancelled due to database error code of: " + databaseError.getCode());
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "dateRef Can not retrieve the data bc of " + databaseError.getCode());
            }
        });

        like_button.setOnClickListener(mButtonListener);
        unlike_button.setOnClickListener(mButtonListener);

        return rootView;
    }

    //Gets the current date with format "ddMMyy".
    public String getDate()    {

        return new SimpleDateFormat("ddMMyy").format(new Date());
    }
    public static Bitmap loadImageFromWebOperations(String url) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());

            Log.i(TAG, "Image is turning into Bitmap from URL: " + url);
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "Image From URL can not be loaded.");
            e.printStackTrace();
            return null;
        }
    }

    private View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Log.i(TAG, art_name.getText().toString());

            switch (view.getId())
            {
                case R.id.like_button:  {
                    Log.w(TAG, "Pressed on Like Button with id " + R.id.like_button);

                    DatabaseReference dateRef = database.getReference("Date/" + getDate());
                    dateRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final DailyPlan tempDaily = dataSnapshot.getValue(DailyPlan.class);
                            DatabaseReference likeRef = database.getReference("ArtWorks/ArtType/painting/" + tempDaily.getPainting());

                            likeRef.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    Art art = mutableData.getValue(Art.class);
                                    if (art == null)    {
                                        return Transaction.success(mutableData);
                                    }

                                    if (art.likes.containsKey(userUid))   {
                                        //If user likes it already, then show the percentage bar.
                                        showBar(art.likeCount, art.unlikeCount);
                                    }
                                    else    {
                                        //Like the post.
                                        art.likeCount = art.likeCount + 1;
                                        art.likes.put(userUid, true);
                                        //If user likes it already, then show the percentage bar.
                                        showBar(art.likeCount, art.unlikeCount);
                                    }
                                    // Set value and report transaction success
                                    mutableData.setValue(art);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                    // Transaction completed
                                    Log.d(TAG, "likeTransaction:onComplete:" + databaseError);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "loadDailyPlan:onCancelled", databaseError.toException());
                        }
                    });
                    break;
                }
                case R.id.unlike_button:    {
                    Log.w(TAG, "Pressed on Unlike Button");
                    DatabaseReference dateRef = database.getReference("Date/" + getDate());
                    dateRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final DailyPlan tempDaily = dataSnapshot.getValue(DailyPlan.class);
                            DatabaseReference likeRef = database.getReference("ArtWorks/ArtType/painting/" + tempDaily.getPainting());

                            likeRef.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    Art art = mutableData.getValue(Art.class);
                                    if (art == null)    {
                                        return Transaction.success(mutableData);
                                    }

                                    if (art.unlikes.containsKey(userUid))   {
                                        //If user likes it already, then show the percentage bar.
                                        showBar(art.likeCount, art.unlikeCount);
                                    }
                                    else    {
                                        //Like the post.
                                        art.unlikeCount = art.unlikeCount + 1;
                                        art.unlikes.put(userUid, true);
                                        //If user likes it already, then show the percentage bar.
                                        showBar(art.likeCount, art.unlikeCount);
                                    }
                                    // Set value and report transaction success
                                    mutableData.setValue(art);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                    // Transaction completed
                                    Log.d(TAG, "unlikeTransaction:onComplete:" + databaseError);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "loadDailyPlan:onCancelled", databaseError.toException());
                        }
                    });
                    break;
                }
            }
        }
    };

    private void showBar(int likeCount, int unlikeCount) {
        Double tempPercentage = (double)likeCount/( (double)likeCount + (double) unlikeCount) * 100;
        final int percentage = tempPercentage.intValue();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                like_button.setVisibility(View.GONE);
                unlike_button.setVisibility(View.GONE);
                like_bar.setVisibility(View.VISIBLE);
                Log.i(TAG, "" + percentage);
                like_bar.setProgress(percentage);
            }
        });
    }
}