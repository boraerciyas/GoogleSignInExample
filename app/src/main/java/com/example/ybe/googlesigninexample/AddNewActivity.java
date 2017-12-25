package com.example.ybe.googlesigninexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewActivity extends AppCompatActivity {

    public Button submit_button;
    TextView edit_name, edit_artist, edit_photo_url, edit_description, edit_year;

    private static final String TAG = "AddNewActivity";

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userUid = getIntent().getExtras().getString("userUid");

        //Views
        submit_button = findViewById(R.id.submit_button);
        edit_name = findViewById(R.id.edit_name);
        edit_artist = findViewById(R.id.edit_artist);
        edit_description = findViewById(R.id.edit_description);
        edit_photo_url = findViewById(R.id.edit_photo_url);
        edit_year = findViewById(R.id.edit_year);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference rootRef = database.getReference("addedFromUser");

                int year = Integer.parseInt(edit_year.getText().toString());
                Art art = new Art(edit_artist.getText().toString(), edit_description.getText().toString(), edit_name.getText().toString(), edit_photo_url.getText().toString(), year);

                rootRef.setValue(art);

                finish();
            }
        });
    }
}
