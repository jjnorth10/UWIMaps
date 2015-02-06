package com.example.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.uwimaps.LocationImageFragment;
import com.example.uwimaps.MainActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

public class Communication {
	public static final String TAG = Communication.class.getSimpleName();
	public String URL;// = "http://192.168.0.6:9000/location/list";
	JSONArray array;
	JSONObject object;
	int statusCode;
	String errorString = "Error cannot connect";
	
	ProgressDialog progressDialog;
	ProgressDialog progressDialog2;
	Context context;
	public Communication(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	
	public void send (final int communicationId,final CommunicationResponse communicationResponse,String apiUrl,String path,String queryParams){
		StringBuilder builder = new StringBuilder();
		builder.append(apiUrl);
		builder.append(path);
		builder.append(queryParams);
		URL = builder.toString();
		Log.d(TAG, "Url: "+URL);
		
		AsyncTask<String,Object,String> async = new AsyncTask<String,Object,String>(){
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Loading...");
				progressDialog.show();
			}


			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				String json;
				try{
					StrictMode.ThreadPolicy policy = new StrictMode.
					ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy); 
					
					//java.net.URL url = new java.net.URL(URL);
					//HttpURLConnection con =(HttpURLConnection) url.openConnection();
					
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(arg0[0]);
					HttpResponse httpResponse = httpClient.execute(httpGet);
					statusCode=httpResponse.getStatusLine().getStatusCode();
					HttpEntity entity = httpResponse.getEntity();
					InputStream is = entity.getContent();
					//return is;
					try {
						 
						InputStreamReader isReader = new InputStreamReader(is);
						BufferedReader reader = new BufferedReader(isReader);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while((line = reader.readLine())!=null){
							sb.append(line+"\n");
							
						}
						is.close();
						json = sb.toString();
						Log.d(TAG, "json string: "+json);
						return json;
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						Log.d(TAG, "ERROR",e);
						e.printStackTrace();
						return errorString;
					}catch(IOException e){
						Log.d(TAG, "ERROR",e);
						e.printStackTrace();
						return errorString;
					}catch(Exception e){
						Log.d(TAG, "ERROR",e);
						e.printStackTrace();
						return errorString;
						
					}
				
				}catch(Exception e){
					Log.d(TAG, "ERROR",e);
					e.printStackTrace();
					return  errorString;
					
				}
			}
			;
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				//progressDialog.dismiss();
				try {
			        if ((progressDialog != null) && progressDialog.isShowing()) {
			            progressDialog.dismiss();
			        }
			    } catch (final IllegalArgumentException e) {
			        // Handle or log or ignore
			    } catch (final Exception e) {
			        // Handle or log or ignore
			    } finally {
			        progressDialog = null;
			    }  
				
				if(result== errorString){
					Toast.makeText(context, result, Toast.LENGTH_LONG);
					Log.d(TAG, result);
				}else{
				try {
					//array=new JSONArray(result);
					object = new JSONObject(result);
					//Log.d(TAG, "result object: "+object);
					Response response = new Response(object);
					if(response.isSuccess()){
						communicationResponse.onSuccess(communicationId,object);
					}else{
						communicationResponse.onError(communicationId,response.getMessage());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d(TAG, "ERROR",e);
					e.printStackTrace();
				}
				}
				
			}
		};
		async.execute(URL);
	}
	
	public void sendForImage (final MainActivity activity,String apiUrl,String path,String queryParams){
		StringBuilder builder = new StringBuilder();
		builder.append(apiUrl);
		builder.append(path);
		builder.append(queryParams);
		URL = builder.toString();
		Log.d(TAG, "Url: "+URL);
		
		AsyncTask<String,Object,Bitmap> async = new AsyncTask<String,Object,Bitmap>(){
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				progressDialog2 = new ProgressDialog(context);
				progressDialog2.setMessage("Loading Image...");
				progressDialog2.show();
			}


			@Override
			protected Bitmap doInBackground(String... arg0) {
				// TODO Auto-generated method stub
			
				try{
					StrictMode.ThreadPolicy policy = new StrictMode.
					ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy); 
					
					//java.net.URL url = new java.net.URL(URL);
					//HttpURLConnection con =(HttpURLConnection) url.openConnection();
					
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(arg0[0]);
					HttpResponse httpResponse = httpClient.execute(httpGet);
					statusCode=httpResponse.getStatusLine().getStatusCode();
					HttpEntity entity = httpResponse.getEntity();
					InputStream is = entity.getContent();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					return bitmap;
					
				
				}catch(Exception e){
					Log.d(TAG, "ERROR",e);
					e.printStackTrace();
					return null;
					
					
				}
			}
			
			@Override
			protected void onPostExecute(Bitmap bitmap) {
				// TODO Auto-generated method stub
				try {
			        if ((progressDialog2 != null) && progressDialog2.isShowing()) {
			            progressDialog2.dismiss();
			        }
			    } catch (final IllegalArgumentException e) {
			        // Handle or log or ignore
			    } catch (final Exception e) {
			        // Handle or log or ignore
			    } finally {
			        progressDialog2 = null;
			    }  
				if(bitmap == null){
					
				}else{
					try {
					
						activity.setImage(bitmap);
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.d(TAG, "ERROR",e);
						e.printStackTrace();
					}
				}
			}
				
			
		};
		async.execute(URL);
	}
	
	public JSONArray getResult(){
		return this.array;
	}
	
	public static String getQueryString(HashMap<String,Object> map){
		String query="";
		
		
		for(Map.Entry<String, Object> entry: map.entrySet()){
			if(query==""){
				query=query+"?"+entry.getKey()+"="+entry.getValue();
			}else{
				query=query+"&"+entry.getKey()+"="+entry.getValue();
			}
			
		}
		return query;
	}
		
	

}
