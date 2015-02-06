package com.example.request;

import com.example.communication.APIEndPoints;

public class LocationListRequest {

	public LocationListRequest() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getPathUrl(){
		return APIEndPoints.location_list;
	}
	
	public String getMethod(){
		return "get";
	}

}
