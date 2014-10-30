package com.android.smsprotect;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMS extends Activity  
{
	Button btnEncryptSend;
	EditText txtPhoneNo;
	EditText txtMessage;
	Button buttonSend;
	String phoneNo;
	String message;
	
	ArrayList<String> messageArray = new ArrayList<String>();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        btnEncryptSend = (Button) findViewById(R.id.btnSendSMS);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
       // buttonSend = (Button) findViewById(R.id.btnEsend);
         //static final boolean bool = false;
        
        /*
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "Content of the SMS goes here..."); 
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
        */
        
        /*
        
        buttonSend.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {            	
            	phoneNo = txtPhoneNo.getText().toString();
           	 	message = txtMessage.getText().toString();
           	 	
           	 if (phoneNo.length()>0 && message.length()>0)               
		             sendSMS(phoneNo, message);                
		         else
		         	Toast.makeText(getBaseContext(), 
		                 "Please enter both phone number and message.", 
		                 Toast.LENGTH_SHORT).show();
           	   //  bool = true;
            }});
        */
                
        btnEncryptSend.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {            	
            	 phoneNo = txtPhoneNo.getText().toString();
            	 message = txtMessage.getText().toString();
            	 int end = 0;
            	 int messageLength = 0;
            	 
            	// message = "dcojsflsdjlksdjflksjdflkjsdlfkjdsckvsflhkvlfdafhjkdshfksjha;ljhldsjhdsfkhslkdfslkdhflkhfdsklhfdlfskhfdlkshdlfkdasifhlsdhsdlkhfldsjhfljhlfsdjdahslfjhsdljfhsjfhjkshfkjhsdfkj";
            	try {
            	
        			RsaCrypto encryptString;
        			
        				encryptString = new RsaCrypto();
        				
        				
        				
        				
        				message = encryptString.Encrypt(message);
        				Log.d("message", message);
        				
		            	} catch (Exception e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
        			
            	   
            	
						        					
							if (phoneNo.length()>0 && message.length()>0)               
	  				             sendSMS(phoneNo, message);                
	  				         else
	  				         	Toast.makeText(getBaseContext(), 
	  				                 "Please enter both phone number and message.", 
	  				                 Toast.LENGTH_SHORT).show();
						        					
						 
            	
            }
        });        
    }
    
    //---sends a SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {      
    	
    	
    	String SENT = "SMS_SENT";
    	String DELIVERED = "SMS_DELIVERED";
    	
       PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
               new Intent(SENT), 0);
        
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
    	
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(getBaseContext(), "SMS sent", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					    Toast.makeText(getBaseContext(), "Generic failure", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NO_SERVICE:
					    Toast.makeText(getBaseContext(), "No service", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NULL_PDU:
					    Toast.makeText(getBaseContext(), "Null PDU", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_RADIO_OFF:
					    Toast.makeText(getBaseContext(), "Radio off", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				}
			}
        }, new IntentFilter(SENT));
        
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(getBaseContext(), "SMS delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case Activity.RESULT_CANCELED:
					    Toast.makeText(getBaseContext(), "SMS not delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;					    
				}
			}
        }, new IntentFilter(DELIVERED));        
    	
        SmsManager sms = SmsManager.getDefault();
        
        ArrayList<String> parts = sms.divideMessage(message);
        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
        for (int number = 0; number < parts.size(); number++) { 
                sentIntents.add(sentPI);
                deliveryIntents.add(deliveredPI);

           // .sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);               
    }
        sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);

	
    }
}