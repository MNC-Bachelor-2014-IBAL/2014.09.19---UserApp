package mnc.beacon.mapview;

//import andexam.ver4_1.*;
import java.util.*;

import mnc.beacon.*;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.util.Log;
import android.view.*;
import mnc.beacon.beacon.BeaconManager;
import mnc.beacon.mainservice.BeaconEventFlag;
import mnc.beacon.server.*;

import java.util.HashMap;
import java.util.Map;

public class MapView extends Activity {

	// private double[] xLocation = {300, 560, 760, 960, 1160, 560, 760, 560,
	// 760};
	// private double[] yLocation = {125, 125, 125, 125, 125, 325, 325, 525,
	// 525};

	// private double[] xLocation = {560, 760, 560, 760};
	// private double[] yLocation = {325, 325, 525, 525};

	private double[] xLocation = { 1700, 1450, 1200, 950, 700, 850, 1000, 850, 600, 350, 100 };
	private double[] yLocation = { 480, 480, 480, 480, 140, 50, 140, 300,480, 480, 480 };

	static int weight1;
	static int weight2;

	private int distance1;
	private int distance2;
	private int drawCondition;
	private int rssi;
	MyView vw;
	public String TAG = "a2";
	Bitmap bm;
	private BeaconManager beaconManager;
	private BeaconEventFlag beaconEventOccur;
	private int[] currentWeight = new int[12];

	
	private int cellnum=12;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vw = new MyView(this);
		setContentView(vw);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		beaconManager = BeaconManager.instance();

		Http p = new Http();
		Image img = new Image();
		/*
		 * Map data = new HashMap(); data.put("id", "1"); String result =
		 * p.get("http://164.125.34.173:8080/NewFile.jsp", data); //bm =
		 * img.DownloadImg(p.getURL());
		 */
		bm = img.DownloadImg("http://164.125.34.173:8080/map.png");
		MapviewThread thread = new MapviewThread(mHandler);
		thread.setDaemon(true);
		thread.start();
	}

	public void onResume() {
		super.onResume();

	}

	class MyView extends View {
		public MyView(Context context) {
			super(context);
		}

		public void onDraw(Canvas canvas) {
			Log.i(TAG, "onDraw");
			canvas.drawColor(Color.LTGRAY);
			Paint Pnt = new Paint();
			Paint textPnt = new Paint();
			textPnt.setColor(Color.BLACK);
			textPnt.setTextSize(40);
			textPnt.setStrokeWidth(10);

			Resources res1 = getResources();
			BitmapDrawable firstfloor = (BitmapDrawable) res1
					.getDrawable(R.drawable.floor2);
			Bitmap firstfl = firstfloor.getBitmap();

			BitmapDrawable mylocation = (BitmapDrawable) res1
					.getDrawable(R.drawable.location2);
			Bitmap myloc = mylocation.getBitmap();

			BitmapDrawable bd1 = (BitmapDrawable) res1
					.getDrawable(R.drawable.location2);
			Bitmap bit1 = bd1.getBitmap();
			Rect dest = new Rect(0, 0, getWidth(), getHeight());

			canvas.drawBitmap(bm, null, dest, null);
		
		

				if (drawCondition == 1) {
					canvas.drawBitmap(bit1, (float) xLocation[0],
							(float) yLocation[0], null);
				} else if (drawCondition == 2) {
					canvas.drawBitmap(bit1, (float) xLocation[1],
							(float) yLocation[1], null);
				} else if (drawCondition == 3) {
					canvas.drawBitmap(bit1, (float) xLocation[2],
							(float) yLocation[2], null);
				} else if (drawCondition == 4) {
					canvas.drawBitmap(bit1, (float) xLocation[3],
							(float) yLocation[3], null);
				}
			

				if (drawCondition == 5) {
					canvas.drawBitmap(bit1, (float) xLocation[4],
							(float) yLocation[4], null);
				} else if (drawCondition == 6) {
					canvas.drawBitmap(bit1, (float) xLocation[5],
							(float) yLocation[5], null);
				} else if (drawCondition == 7) {
					canvas.drawBitmap(bit1, (float) xLocation[6],
							(float) yLocation[6], null);
				} else if (drawCondition == 8) {
					canvas.drawBitmap(bit1, (float) xLocation[7],
							(float) yLocation[7], null);
				}
			

				if (drawCondition == 9) {
					canvas.drawBitmap(bit1, (float) xLocation[8],
							(float) yLocation[8], null);
				} else if (drawCondition == 10) {
					canvas.drawBitmap(bit1, (float) xLocation[9],
							(float) yLocation[9], null);
				} else if (drawCondition == 11) {
					canvas.drawBitmap(bit1, (float) xLocation[10],
							(float) yLocation[10], null);
				} 
			
			for (int i = 0; i < cellnum; ++i) {
				canvas.drawText("" + currentWeight[i - 1],
						(float) xLocation[i - 1] + 30,
						(float) yLocation[i - 1] + 30, textPnt);
			}

		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int caseNum = BeaconEventFlag.getCurrentLocation();
			beaconEventOccur = (BeaconEventFlag) msg.obj;
			currentWeight = beaconEventOccur.getWeight();

			switch (caseNum) {
			case 1:
				drawCondition = 1;
				break;
			case 2:
				drawCondition = 2;
				break;
			case 3:
				drawCondition = 3;
				break;
			case 4:
				drawCondition = 4;
				break;
			case 5:
				drawCondition = 5;
				break;
			case 6:
				drawCondition = 6;
				break;
			case 7:
				drawCondition = 7;
				break;
			case 8:
				drawCondition = 8;
				break;
			case 9:
				drawCondition = 9;
				break;
			case 10:
				drawCondition = 10;
				break;
			case 11:
				drawCondition =11;
				break;

			}
			vw.invalidate();

		}
	};
}