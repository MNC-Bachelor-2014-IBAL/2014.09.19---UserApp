package mnc.beacon.mainservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.xml.sax.Parser;

import mnc.beacon.*;
import mnc.beacon.ar.ARActivity;
import mnc.beacon.beacon.*;
import mnc.beacon.ble.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import mnc.beacon.beacon.*;
import mnc.beacon.mapview.*;
import mnc.beacon.server.Http;
import mnc.beacon.survey.CollectFinger;
import mnc.beacon.survey.kalman;

public class MainActivity extends Activity {
	Button beaconNumBtn, mapBtn, mapBtn1, mapBtn2, fingerbutton, requestBtn;
	EditText coldat, fingertext;
	TextView beaconNumText;
	BeaconPacket beacon;
	BeaconManager beaconManager;
	BeaconEventFlag beaconEventOccur;
	private int checkCount;
	private Intent intent;
	private Intent intent1;
	private Intent intent2;
	private Intent intent3;
	private ToastClass toastClass;


	public double sum1, sum2, sum3;
	public int count = 0;
	public double updat[] = new double[1];
	public double resuldat[] = new double[1];
	double init[] = new double[1];


	private int beaconnumber=6;
	private int cellnumber=8;
	
	Object obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.first_activity);
		beaconNumBtn = (Button) findViewById(R.id.beaconNumBtn);
		mapBtn = (Button) findViewById(R.id.mapBtn);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		beaconNumText = (TextView) findViewById(R.id.beaconNumText);
		beaconManager = BeaconManager.instance();
		beaconEventOccur = BeaconEventFlag.instance();

		intent = new Intent(this, DeviceScanService.class);
		intent1 = new Intent(this, BeaconListForwardService.class);
		intent2 = new Intent(this, EventCheckExecuteService.class);
		intent3 = new Intent(this, EventHandlerService.class);
		
		
		
		Intent intent4 = new Intent(this, CalWeightService.class);
		
		
		startService(intent4);
		
		
		
		startService(intent);
		startService(intent1);
		startService(intent2);
		startService(intent3);
	}
	public void onStop() {
		beaconEventOccur.backGround = true;
		super.onStop();
		
	}
	public void onPause() {
		beaconEventOccur.backGround = true;
		super.onPause();
	}
	public void onRestart() {
		beaconEventOccur.backGround = false;
		super.onRestart();
	}

	public void mOnClick(View v) throws IOException {

		switch (v.getId()) {
		case R.id.beaconNumBtn:
			
			int num = beaconManager.beaconList.size();
			beaconNumText.setText("Beacon : " + num + "개");
			break;
			
		case R.id.mapBtn:
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
			.permitAll().build();
	StrictMode.setThreadPolicy(policy);
	
			Intent intent = new Intent(this, MapView.class);
			//Intent intent1 = new Intent(this, CalWeightService.class);
			
			startActivity(intent);
			//startService(intent1);
			break;
			
		}
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		menu.setQwertyMode(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ar: {
			Intent intent = new Intent(MainActivity.this, ARActivity.class);
			startActivity(intent);
			return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			String alertTitle = getResources().getString(R.string.app_name);
			String buttonMessage = getResources().getString(R.string.alert_msg_exit);
			String buttonYes = getResources().getString(R.string.button_yes);
			String buttonNo = getResources().getString(R.string.button_no);

			new AlertDialog.Builder(MainActivity.this)
			.setTitle(alertTitle)
			.setMessage(buttonMessage)
			.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					moveTaskToBack(true);
					finish();
				}
			})
			.setNegativeButton(buttonNo, null)
			.show();
			return true;
		}
		return false;
	}
}
