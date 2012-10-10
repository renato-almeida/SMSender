package com.example.sendsms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    
    TextView tv;
    PowerManager.WakeLock wl;
    
    private String phoneNumber;
    private long timeout;
    private int smsQuantity;
    private SharedPreferences preferenceManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this);
        phoneNumber = preferenceManager.getString("phonenumber", null);
        
        String tmpTimeout = preferenceManager.getString("timeout", "20000");
        timeout = Long.parseLong(tmpTimeout);
        
        String tmpSMSQuantity = preferenceManager.getString("smsnumber", "245");
        smsQuantity = Integer.parseInt(tmpSMSQuantity);
        
        if (phoneNumber == null) {
        	Log.e("MainActivity", "phone number not defined.");
        	Intent settings = new Intent(this, OptionsActivity.class);
			startActivityForResult(settings, 0);
        }
        
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                                        | PowerManager.ON_AFTER_RELEASE, "My Tag");
        wl.acquire();
        
        Button b = (Button) findViewById(R.id.button1);
        
        b.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new Cenas().execute();
                
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	
    	if (resultCode == RESULT_OK) {
    		phoneNumber = preferenceManager.getString("phonenumber", null);
            //timeout = preferenceManager.getLong("timeout", 20000);
    		
    		String tmpTimeout = preferenceManager.getString("timeout", "20000");
            timeout = Long.parseLong(tmpTimeout);
            
            String tmpSMSQuantity = preferenceManager.getString("smsnumber", "245");
            smsQuantity = Integer.parseInt(tmpSMSQuantity);
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	
    		case R.id.menu_settings: {
    			Intent settings = new Intent(this, OptionsActivity.class);
    			startActivityForResult(settings, 1);
    			return true;
    		}
    	
    		default: return super.onOptionsItemSelected(item);
    	}
    }
    
    private class Cenas extends AsyncTask<Void, Integer, Void>{
        
        @Override
        protected Void doInBackground(Void... params) {
            
            for(int i=0; i<smsQuantity; i++){
                
                publishProgress(i);
                
                sendSMS(phoneNumber, "sms " + i);
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            return null;
        }
        
        @Override
        public void onProgressUpdate( Integer ... params){
            
            tv = (TextView) findViewById(R.id.textView1);
            tv.setText("Sending "+params[0]+"...");
        }
        
        @Override
        protected void onPostExecute( Void result ) {
            
            super.onPostExecute(result);
            wl.release();
        }
        
        
    }
    
    private void sendSMS(String phoneNumber, String message)
    {
        Log.v("phoneNumber",phoneNumber);
        Log.v("MEssage",message);
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, X.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);
    }
    
    class X {
        
    }
}
