package com.example.ybe.googlesigninexample;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by root on 25.12.2017.
 */

public class Tab7Final extends Fragment {

    Button share_button, library_button, add_new_button;

    String userUid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_architecture, container, false);

        //Passed Parameter from Main2Activity.
        Bundle args = this.getArguments();
        userUid = args.getString("userUid");

        //Views
        share_button = rootView.findViewById(R.id.share_button);
        library_button = rootView.findViewById(R.id.library_button);
        add_new_button = rootView.findViewById(R.id.add_new_button);

        library_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("userUid", userUid);
                Intent intent = new Intent(getActivity(), Library.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        return rootView;
    }
}
