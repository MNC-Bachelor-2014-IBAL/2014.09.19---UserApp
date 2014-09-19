package mnc.beacon.ar;

import mnc.beacon.R;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.maps.MapActivity;

public class ARActivity extends MapActivity {
	/** Called when the activity is first created. */
	private ARPreview mCameraPreview;
	private AROverlayView mOverlayView = null;
	private String floor = "FIRST";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Toast.makeText(this, "Data download...", Toast.LENGTH_SHORT).show();
		overridePendingTransition(android.R.anim.slide_in_left, 0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// 카메라 프리뷰와 카메라 프리뷰 위에 보여줄 카메라 오버레이뷰 생성
		// create camera Preview and camera Overlay View 
		mCameraPreview = new ARPreview(this);
		mOverlayView = new AROverlayView(this);

		Display display = ((WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int height = display.getHeight();

		// set camera screen rate(2:3)
		// 2:3 비율로 카메라 화면을 설정(보통의 카메라 사진 크기)
		mOverlayView.setOverlaySize((int) (height * 1.5), height);

		setContentView(mCameraPreview, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		addContentView(mOverlayView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}

	// 액티비티가 소멸될때  오버레이 뷰의 자원을 해제
	// free Overlay View when destroying Activity
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mOverlayView.viewDestory();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		menu.setQwertyMode(true);
		SubMenu sub = menu.addSubMenu("Select Floor");
		sub.add(0, 1, 0, "First Floor");
		sub.add(0, 2, 0, "Second Floor");
		sub.add(0, 3, 0, "Third Floor");
		sub.add(0, 4, 0, "Fourth Floor");
		sub.add(0, 5, 0, "Fifth Floor");
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 1:
        	floor = "FIRST";
            return true;
        case 2: 
        	floor = "SECOND";
            return true;
        case 3:
        	floor = "THIRD";
            return true;
        case 4:
        	floor = "FOURTH";
            return true;   
        case 5:
        	floor = "FIFTH";
        	return true;
        }
        return false;
    } 

	public void onBackPressed() {
		this.finish();
		overridePendingTransition(0, android.R.anim.slide_out_right);
	}
	
	public String getFloor() {
		return floor;
	}
}
