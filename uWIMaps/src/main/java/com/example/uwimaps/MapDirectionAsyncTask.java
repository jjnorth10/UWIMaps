package com.example.uwimaps;

import java.util.ArrayList;
import java.util.Map;

import org.w3c.dom.Document;

import com.example.mapdirection.MapDirection;
import com.google.android.gms.maps.model.LatLng;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class MapDirectionAsyncTask extends AsyncTask<Map<String,String>, Object, ArrayList> {
	public static final String USER_CURRENT_LAT = "user_current_lat";
    public static final String USER_CURRENT_LONG = "user_current_long";
    public static final String DESTINATION_LAT = "destination_lat";
    public static final String DESTINATION_LONG = "destination_long";
    public static final String DIRECTIONS_MODE = "directions_mode";
    private MainActivity activity;
    private Exception exception;
    private ProgressDialog progressDialog;
    
    public MapDirectionAsyncTask(MainActivity activity){
    	super();
    	this.activity = activity;
    	
    }
    
    
 

	@Override
	protected void onPostExecute(ArrayList result) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		if(exception == null){
			activity.handleGetDirectionsResult(result);
		}else{
			processException();
		}
	}




	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage("Calculating directions");
		progressDialog.show();
	}




	@Override
	protected ArrayList doInBackground(Map<String, String>... params) {
		// TODO Auto-generated method stub
		Map<String,String> paramMap = params[0];
		try{
			LatLng fromPosition = new LatLng(Double.valueOf(paramMap.get(USER_CURRENT_LAT)),Double.valueOf(paramMap.get(USER_CURRENT_LONG)));
			LatLng toPosition = new LatLng(Double.valueOf(paramMap.get(DESTINATION_LAT)) , Double.valueOf(paramMap.get(DESTINATION_LONG)));
			MapDirection mapDir = new MapDirection();
			Document doc = mapDir.getDocument(fromPosition, toPosition, paramMap.get(DIRECTIONS_MODE));
			ArrayList directionPoints = mapDir.getDirection(doc);
			return directionPoints;
			 
			
		}catch(Exception e){
			exception = e;
			return null;
			
		}
	}
	
	private void processException(){
		Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG);
	}

}
