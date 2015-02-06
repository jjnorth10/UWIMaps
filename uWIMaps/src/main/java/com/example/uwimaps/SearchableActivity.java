package com.example.uwimaps;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ListView;

public class SearchableActivity extends ListActivity implements DBConstants{
	ListView listView;
	MapData mapData;
	SimpleCursorAdapter adapter;
	public static final String TAG = SearchableActivity.class.getSimpleName();
	public SearchableActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchable_activity);
		listView = getListView();
		mapData = new MapData(this);
		
		Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      performSearch(query);
	    }
	}
	
	public void performSearch(String query){
		int[] to=new int[]{R.id.list_item_title,R.id.list_item_desc};
		String[] columns = new String[]{LOCATION_NAME,LOCATION_DESCRIPTION};
		Cursor cursor = mapData.getLocationByFTS(query);
		if(cursor.getCount()>0){
			adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, columns, to,0);
		}else{
			Log.d(TAG, "Course cursor null");
		}
		listView.setAdapter(adapter);
		//Log.d(TAG, "search query: "+query);
		
	}

}
