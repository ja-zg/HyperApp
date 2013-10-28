package hr.android.jsonparsing;

import com.androidhive.jsonparsing.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {
	static int SPLASH_TIME_OUT = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);        
	    ImageView splash = (ImageView) findViewById(R.id.imageView_Logo);
	    Animation seq = AnimationUtils.loadAnimation(this, R.anim.blink);
	    splash.startAnimation(seq);
	    
	    new Handler().postDelayed(new Runnable() {
	    	@Override
	        public void run() {                
	            Intent i = new Intent(SplashActivity.this, HypeactiveAppActivity.class);
	            startActivity(i);                
	            finish();
	        }
	    }, SPLASH_TIME_OUT);
	}
}
