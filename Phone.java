package com.example.tal.alert;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Phone extends Activity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);


        // This is opendatabase SQLite database and table creaction
        try {
            db = SQLiteDatabase.openDatabase("sdcard/phonenumber.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table phonenum(" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + " Tel TEXT)");
        } catch (Exception e) {
        }


        final TextView Contact = (TextView)findViewById(R.id.contact);
        final Button sub =(Button)findViewById(R.id.sendDatax);


        // phone number inserting into database
        sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(Contact.getText().toString().length()==10) {
                    //insert general data
                    db.execSQL("insert into phonenum (Tel) values ('" + Contact.getText().toString() + "');");

                        Toast.makeText(Phone.this, "Phone Number Inserted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Phone.this, Phone.class));

                } else {
                    Toast.makeText(Phone.this, "Enter Phone Number or Number entered is less", Toast.LENGTH_LONG).show();
                }

            }
        });



//selecting and vieweing phone number submitted to the database
        final Button view_sms = (Button) findViewById(R.id.phonenu);
        view_sms.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Cursor res = db.rawQuery("select * from phonenum",null);
                        if (res.getCount() == 0) {
                            //show message
                            showMessage("Error", "Nothing Found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("TELEPHONE :" + res.getString(1) + "\n");

                        }
                        //Show all data

                        showMessage("View Data", buffer.toString());
                    }
                }
        );





// deleting phone number inserted into the database
        final Button deletex = (Button) findViewById(R.id.deletex);
        deletex.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.execSQL("delete from phonenum");
                    }
                }
        );










    }


    public void  showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }



}
