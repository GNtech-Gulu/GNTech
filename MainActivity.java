package com.example.tal.alert;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // This is links main interface mainactivity class to phone class
        final TextView ph = (TextView)findViewById(R.id.textView1);

        ph.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, Phone.class));
            }

        });



    }

}
