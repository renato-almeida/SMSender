package com.example.sendsms;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class OptionsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.settings);
    }
    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
    		String key) {
    	Log.i("OptionsActivity", key);
    	
    }
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onResume() {
    	getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    	super.onResume();
    }
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onPause() {
    	getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    	super.onPause();
    }
    
    @Override
    public void onBackPressed() {
    	Log.d("OptionsActivity", "Back Pressed.");
    	setResult(RESULT_OK);
    	super.onBackPressed();
    }
}
