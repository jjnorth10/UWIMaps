package com.example.uwimaps;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.communication.APIEndPoints;
import com.example.communication.Communication;
import com.example.communication.CommunicationResponse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class LocationImageFragment extends Fragment implements CommunicationResponse {
	Communication comm;
	ImageView image;
	public static final String TAG = LocationImageFragment.class.getSimpleName();
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		image = (ImageView) getActivity().findViewById(R.id.locationImage);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		comm = new Communication(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.location_image_fragment, container, false);
		return v;
	}

	public LocationImageFragment() {
		// TODO Auto-generated constructor stub
	}
	
	/*public void setLocationImage(long id){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		String query=Communication.getQueryString(map);
		Log.d(TAG, "query: "+query);
		comm.sendForImage(this, APIEndPoints.apiUrl, APIEndPoints.location_picture, query);
	}*/
	
	public void setImage(Bitmap bitmap){
		
		 image.setImageBitmap(bitmap);
		
	}

	/*@Override
	public void onSuccess(JSONObject object) {
		// TODO Auto-generated method stub
		
		try {
			String[] byteString = object.getString("data").split("/");
			int len = byteString.length;
		    byte[] byteArray = new byte[len];
		    for (int i = 0; i < len; ++i) {
		    	byteArray[i] = (byte)Integer.parseInt(byteString[i],16);
		    }
		   // Log.d(TAG, ""+byteArray);
		    InputStream is = new ByteArrayInputStream(byteArray);
		    BufferedInputStream bis = new BufferedInputStream(is);
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    //options.inJustDecodeBounds = true;
		   // Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,options);
		    Bitmap bmp = BitmapFactory.decodeStream(bis);
		    image.setImageBitmap(bmp);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onError(String message) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void onSuccess(int communicationId, JSONObject object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int communicationId, String message) {
		// TODO Auto-generated method stub
		
	}

}
