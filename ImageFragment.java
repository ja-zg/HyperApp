package hr.android.jsonparsing;

import com.androidhive.jsonparsing.R;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends Fragment {	
	ImageView image;	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		View view = inflater.inflate(R.layout.fragment_layout, container, false);		
		final ImageFragment imageFragment = (ImageFragment)getFragmentManager().findFragmentById(R.id.fragment_Image);
		
		image = (ImageView)view.findViewById(R.id.imageView_Fragment_Pic);	
		image.setClickable(true);
		image.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				FragmentTransaction fr = getFragmentManager().beginTransaction();
		        fr.hide(imageFragment);
		        fr.commit();				
			}
		});
						
		return view;		
	}
	
	public void changeImage(Bitmap bitmap) {
		image.setImageBitmap(bitmap);
	}

	

}
