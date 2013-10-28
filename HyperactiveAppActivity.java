package hr.android.jsonparsing;

import com.androidhive.jsonparsing.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class HypeactiveAppActivity extends ListActivity {	
	private static final String DEBUG_TAG = "downloadingUsers";
	
	JSONArray users = null;
	ArrayList<HashMap<String, String>> usersList = new ArrayList<HashMap<String, String>>();	
	DownloadUsers downloadUsers;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		downloadUsers = new DownloadUsers();
		downloadUsers.execute();					
	}
	
	private class DownloadUsers extends AsyncTask<Object, String, Boolean> {
		
		ProgressDialog pleaseWaitDialog;
		
		@Override
        protected void onCancelled() {
            Log.i(DEBUG_TAG, "onCancelled");
            pleaseWaitDialog.dismiss();
        }
		
		@Override
        protected void onPreExecute() {
            pleaseWaitDialog = ProgressDialog.show(HypeactiveAppActivity.this, 
            		getResources().getString(R.string.app_name), 
            		getResources().getString(R.string.downloading_info), true, true);
            pleaseWaitDialog.setOnCancelListener(new OnCancelListener() {
            	
                public void onCancel(DialogInterface dialog) {
                    DownloadUsers.this.cancel(true);
                }
            });
        }

		@Override
		protected Boolean doInBackground(Object... params) {
			getJSONData();			
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {			
			Log.i(DEBUG_TAG, "onPostExecute");
		    pleaseWaitDialog.dismiss();
			doOnPostExecute();
		}						
	}
	 
	private void doOnPostExecute() {		
		/**
		 * Updating parsed JSON data into ListView
		 * */
		ListAdapter adapter = new SimpleAdapter(getApplicationContext(), usersList,
				R.layout.list_item, new String[] { HyperactiveApp.NAME, 
			HyperactiveApp.LAST_NAME, HyperactiveApp.NUMBER, HyperactiveApp.IMAGE }, new int[] {
						R.id.textView_Name, R.id.textView_LastName, R.id.textView_Number,
						R.id.textView_Image});
		
		setListAdapter(adapter);
		
		// selecting single ListView item
		ListView lv = getListView();
	
		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final String name = ((TextView) view.findViewById(R.id.textView_Name)).getText().toString();
				final String lastName = ((TextView) view.findViewById(R.id.textView_LastName)).getText().toString();
				final String number = ((TextView) view.findViewById(R.id.textView_Number)).getText().toString();
				//my little hack to simplify the thing
				final String image = ((TextView) view.findViewById(R.id.textView_Image)).getText().toString();
				final Handler handler = new Handler(getMainLooper());
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
						in.putExtra(HyperactiveApp.NAME, name);
						in.putExtra(HyperactiveApp.LAST_NAME, lastName);
						in.putExtra(HyperactiveApp.NUMBER, number);
						in.putExtra(HyperactiveApp.IMAGE, image);
						startActivity(in);
					}
				});		
			}
		});		   
	}
	
	private void getJSONData() {
		try {				
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpGet request = new HttpGet(HyperactiveApp.URL);
			HttpResponse response = httpclient.execute(request);
			HttpEntity resEntity = response.getEntity();
			String _response = EntityUtils.toString(resEntity); // content will be consume only once
			Log.i(getResources().getString(R.string.downloading_info),_response);
			JSONObject json = new JSONObject(_response);
			users = json.getJSONArray(HyperactiveApp.USERS);	
			
			for(int i = 0; i < users.length(); i++) {
				JSONObject jobj;
				
				jobj = users.getJSONObject(i);				
				
				String name = jobj.getString(HyperactiveApp.NAME);
				String lastName = jobj.getString(HyperactiveApp.LAST_NAME);
				String imageUrl = jobj.getString(HyperactiveApp.IMAGE);
				String phNum = jobj.getString(HyperactiveApp.NUMBER);
					
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(HyperactiveApp.NAME, name);
				map.put(HyperactiveApp.LAST_NAME, lastName);
				map.put(HyperactiveApp.IMAGE, imageUrl);
				map.put(HyperactiveApp.NUMBER, phNum);
				
				usersList.add(map);					
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
		
	}

}
