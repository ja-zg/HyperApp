package hr.android.jsonparsing;

import com.androidhive.jsonparsing.R;
import hr.android.imageloader.ImageLoader;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {
	private static final String DEBUG_TAG = "onClick";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
        
        Intent intent = getIntent();
        int loader = R.drawable.loader;
        
        String name = intent.getStringExtra(HyperactiveApp.NAME);
        String lastName = intent.getStringExtra(HyperactiveApp.LAST_NAME);
        String number = intent.getStringExtra(HyperactiveApp.NUMBER);
        String imageUrl = intent.getStringExtra(HyperactiveApp.IMAGE);
        
        TextView lblName = (TextView) findViewById(R.id.textView_Single_Name);
        TextView lblLastName = (TextView) findViewById(R.id.textView_Single_LastName);
        TextView lblNumber = (TextView) findViewById(R.id.textView_Single_Number);
        ImageView image = (ImageView) findViewById(R.id.imageView_Single_Pic);
        
        lblName.setText(name);
        lblLastName.setText(lastName);
        lblNumber.setText(number);        
        
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
        imgLoader.DisplayImage(imageUrl, loader, image);        
    }
	
	public void onClick(View v) {
		final ImageView image = (ImageView) findViewById(R.id.imageView_Single_Pic);
		final Handler handler = new Handler(getMainLooper());
		
		image.buildDrawingCache();
		Bitmap bitmap = image.getDrawingCache();
		
		Log.i(DEBUG_TAG, getResources().getString(R.string.onclick_press));
		
		switch(v.getId()) {
		case R.id.imageView_Single_Pic:
			ImageFragment imageFragment = (ImageFragment)getFragmentManager().findFragmentById(R.id.fragment_Image);			
			imageFragment.changeImage(bitmap);
			FragmentTransaction fr = getFragmentManager().beginTransaction();			
	        fr.show(imageFragment);	        
	        fr.commit();
	        break;
	        
		case R.id.textView_Single_Name:
			handler.post(new Runnable() {				
				@Override
				public void run() {
					startDialActivity();
				}
			});
			break;
			
		case R.id.textView_Single_LastName:
			handler.post(new Runnable() {				
				@Override
				public void run() {
					startDialActivity();
				}
			});
			break;
		}
	}
	
	private void startDialActivity(){
		String tel = "tel:";
		
		try {
			Uri number = Uri.parse(tel + HyperactiveApp.NUMBER);
		    Intent intent = new Intent(Intent.ACTION_CALL);
		    intent.setData(number);
		    startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Log.e(getResources().getString(R.string.app_name), getResources().getString(R.string.dial_error), e);
		}
	}		
}
