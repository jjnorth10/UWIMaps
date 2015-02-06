package com.example.uwimaps;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class UWIMapFragment extends Fragment implements LocationListener, DBConstants {
	public static final String TAG = UWIMapFragment.class.getSimpleName();
	private GoogleMap map;
	private SupportMapFragment mSupportMapFragment;
	private MapData mapData;
	public UWIMapFragment() {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mapData = new MapData(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.map_fragment, container,false);
		
		loadMap();
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		try {
            
            //map.setMyLocationEnabled(true);
            //map.getUiSettings().setMyLocationButtonEnabled(true);



        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void loadMap(){
		mSupportMapFragment= (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
		if (mSupportMapFragment == null) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			mSupportMapFragment = SupportMapFragment.newInstance();
			fragmentTransaction.replace(R.id.map, mSupportMapFragment).commit();
	    }
			map = mSupportMapFragment.getMap();
			if(map!=null){
				LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
				String provider = locationManager.getBestProvider(new Criteria(), true);
				 // getting GPS status
		        boolean isGPSEnabled = locationManager
		                .isProviderEnabled(LocationManager.GPS_PROVIDER);

		        // getting network status
		        boolean isNetworkEnabled = locationManager
		                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		        if (!isGPSEnabled && !isNetworkEnabled) {
		            // no network provider is enabled
		        	Toast.makeText(this.getActivity().getApplicationContext(),
		        	        "No network provider is enabled", Toast.LENGTH_SHORT).show();
		        }else{
		        	if(isNetworkEnabled){
		        		locationManager.requestLocationUpdates(
		        				LocationManager.NETWORK_PROVIDER,
		        				0,
		        				0, this);
		        		 Log.d("Network", "Network Enabled");
		        		 if (locationManager != null) {
		        			 Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		     				if (location != null){
		     					double latitude = location.getLatitude();
		     					double longitude = location.getLongitude();
		     					LatLng latLng = new LatLng(latitude, longitude);
		     					map.addMarker(new MarkerOptions()
		     										.title("You Are Here")
		     			                            .position(latLng)
		     			                            .draggable(true)
		     			                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		     			        );
		     					addLocationMarkers(map);
		     			        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
		     				}
		        			 
		        		 }
		        	}
		        	if (isGPSEnabled) {
		                  locationManager.requestLocationUpdates(
		                              LocationManager.GPS_PROVIDER,
		                              0,
		                              0, this);
		                      Log.d("GPS", "GPS Enabled");
		                      if (locationManager != null) {
				        			 Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				     				if (location != null){
				     					double latitude = location.getLatitude();
				     					double longitude = location.getLongitude();
				     					LatLng latLng = new LatLng(latitude, longitude);
				     					map.addMarker(new MarkerOptions()
				     										.title("You Are Here")
				     			                            .position(latLng)
				     			                            .draggable(true)
				     			                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
				     			        );
				     					addLocationMarkers(map);
				     			        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
				     				}
				        			 
				        		 }
		        	}
		        }

				/*Location location = locationManager.getLastKnownLocation(provider);
				if (location != null){
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					LatLng latLng = new LatLng(latitude, longitude);
					map.addMarker(new MarkerOptions()
			                            .position(latLng)
			                            .draggable(true)
			                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
			        );
			        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
				}*/
			
			}else{
			Toast.makeText(this.getActivity().getApplicationContext(),
	        "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
	    }
		
		
	}
	
	public void addLocationMarkers(GoogleMap map){
		Cursor cursor = mapData.getAllLocations();
		if(cursor.getCount()>0){
			while(cursor.moveToNext()){
				double latitude = cursor.getDouble(cursor.getColumnIndex(LOCATION_LATITUDE));
				double longitude = cursor.getDouble(cursor.getColumnIndex(LOCATION_LONGITUDE));
				LatLng latLng = new LatLng(latitude, longitude);
			
				map.addMarker(new MarkerOptions()
								.position(latLng)
								.draggable(true)
								.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			}
		}else{
			Log.d(TAG, "Error no records found");
			Toast.makeText(getActivity(), "Error no records found", Toast.LENGTH_LONG);
			
		}
			
			
	}
		
	


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//loadMap();
	}



	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
