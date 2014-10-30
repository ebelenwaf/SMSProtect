package com.android.smsprotect;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver
{
	static SmsMessage[] msgs ;
	static String decryptString = null;
	static String str_base_64 = "";
	@Override
	public void onReceive(Context context, Intent intent) 
	{
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
         msgs = null;
        String str = "";    
        
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
                str += "SMS from " + msgs[i].getOriginatingAddress();                     
                str += " :";
                
               // try {
                	Log.d("hello", RsaCrypto.getPrivateKey().toString() );
					str_base_64 += msgs[i].getMessageBody().toString();
					
					//str = str+ str_base_64;
				/*			
							//RsaCrypto.Decrypt(msgs[i].getMessageBody().toString(),RsaCrypto.getPrivateKey() ) ;
				} catch (InvalidKeyException | ShortBufferException
						| IllegalBlockSizeException | BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				*/
               str += "\n";        
            }
            
            
            
            
            
			try {
				decryptString = RsaCrypto.Decrypt(str_base_64, RsaCrypto.getPrivateKey());
			} catch (InvalidKeyException | ShortBufferException
					| IllegalBlockSizeException | BadPaddingException
					| NoSuchAlgorithmException | NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			str = str + decryptString;
			
			
			Intent recieveIntent =  new Intent(context, ReceiveMessageActivity.class);
			 
			recieveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 context.startActivity(recieveIntent);
			 
			 
			
			Log.d("decrypted message", str_base_64);
			
			
            //---display the new SMS message---
            //Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }                 		
	}
	
	public static String getencryptedMessage()
	{
		return "SMS from " + msgs[0].getOriginatingAddress()+"\n\n"+str_base_64;
	}
	
	public static String getdecryptedMessage(){
		
		return "Decrypted Content: \n\n"+ decryptString; 
	}
}