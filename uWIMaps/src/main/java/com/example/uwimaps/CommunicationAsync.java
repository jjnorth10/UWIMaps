package com.example.uwimaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class CommunicationAsync extends AsyncTask<InputStream, Object, String> {
	public static final String TAG = CommunicationAsync.class.getSimpleName();
	ProgressDialog progressDialog;
	MainActivity activity;
	

    public CommunicationAsync(MainActivity activity){
    	super();
    	this.activity = activity;
    	
    }
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage("Calculating directions");
		progressDialog.show();
	}

	@Override
	protected String doInBackground(InputStream... is) {
		InputStreamReader isReader;
		try {
			isReader = new InputStreamReader(is[0]);
			BufferedReader reader = new BufferedReader(isReader);
			String s = reader.readLine();
			return s;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "ERROR",e);
			e.printStackTrace();
			return null;
		}catch(IOException e){
			Log.d(TAG, "ERROR",e);
			e.printStackTrace();
			return null;
		}catch(Exception e){
			Log.d(TAG, "ERROR",e);
			e.printStackTrace();
			return null;
			
		}
		
	}
	
	@Override
	protected void onPostExecute(String s) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		if(s!=null){
		Log.d(TAG, "Data: "+s);
		Toast.makeText(activity, s, Toast.LENGTH_LONG);
		}
	}

}
