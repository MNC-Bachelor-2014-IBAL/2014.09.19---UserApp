package mnc.beacon.ar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import mnc.beacon.R;
import mnc.beacon.mainservice.BeaconEventFlag;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class AROverlayView extends View implements SensorEventListener {
	private static float mXCompassDegree;
	private static float mYCompassDegree;
	private SensorManager mSensorManager;
	private Sensor mOriSensor;
	private int mWidth;
	private int mHeight;
	private Paint mPaint;
	private List<PointF> mPointFList = null;
	private HashMap<Integer, String> mPointHashMap;
	private boolean mTouched = false;
	private int mTouchedItem;
	private ARActivity mContext;
	private List<ARData> mDataList;
	private ARHandler mARHandler;
	private RectF mPopRect;
	private Paint mPopPaint;
	private int mVisibleDistance = 3;
	private float mTouchedY;
	private float mTouchedX;
	private boolean mScreenTouched = false;
	private int mCounter = 0;
	private Paint mTouchEffectPaint;
	private Bitmap mIconBitmap;
	private Bitmap mFirstIconBitmap;
	private Bitmap mSecondIconBitmap;
	private Bitmap mThirdIconBitmap;
	private Bitmap mFourthIconBitmap;
	private Bitmap mFifthIconBitmap;
	private Paint mShadowPaint;
	private int mShadowXMargin;
	private int mShadowYMargin;
	public static final String FIRST = "FIRST";
	public static final String SECOND = "SECOND";
	public static final String THIRD = "THIRD";
	public static final String FOURTH = "FOURTH";
	public static final String FIFTH = "FIFTH";	
	BeaconEventFlag beaconEventOccur;

	public AROverlayView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = (ARActivity) context;
		// initialize bitmap, sensor, paint, Handler
		initBitmap();
		initSensor(context);
		initPaints();
		initHandler();
	}
	
	// execute onSensorChanged when invalidating sensor value TYPE_ORIENTATION 
	// present information, interpret data and draw marker
	public void onDraw(Canvas canvas) {
		canvas.save();
		// rotate screen
		canvas.rotate(270, mWidth / 2, mHeight / 2);
		// initialize Rect
		initRectFs();
		// initialize Handler
		initHandler();

		// draw popup when item touched
		if (mTouched == true) {
			drawPopup(canvas);
		}

		// read data, execute drawGrid and draw marker
		interpret(canvas);
		// restore rotated camera
		canvas.restore();
		// draw effect when screen touched
		if (mScreenTouched == true && mCounter < 15) {
			drawTouchEffect(canvas);
			mCounter++;
		} else {
			mScreenTouched = false;
			mCounter = 0;
		}
	}

	// draw effect when screen touched
	private void drawTouchEffect(Canvas pCanvas) {
		// TODO Auto-generated method stub
		pCanvas.drawCircle(mTouchedX, mTouchedY, mCounter * 1,
				mTouchEffectPaint);
		pCanvas.drawCircle(mTouchedX, mTouchedY, mCounter * 2,
				mTouchEffectPaint);
		pCanvas.drawCircle(mTouchedX, mTouchedY, mCounter * 3,
				mTouchEffectPaint);
	}

	// handling and interpret coordinate when screen touched
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		// transfer coordinate 
		float convertedX, convertedY, temp;
		convertedX = event.getX();
		convertedY = event.getY();
		convertedX = convertedX - mWidth / 2;
		convertedY = convertedY - mHeight / 2;
		temp = convertedX;
		convertedX = -convertedY;
		convertedY = temp;

		mTouchedX = event.getX();
		mTouchedY = event.getY();

		mScreenTouched = true;
		
		// handling when touching Pop up
		// touch = true -> go to the web site
		if (mTouched == true) {
			if (convertedX > mPopRect.left - mWidth / 2
					&& convertedX < mPopRect.right - mWidth / 2
					&& convertedY > mPopRect.top - mHeight / 2
					&& convertedY < mPopRect.bottom - mHeight / 2) {

				Toast.makeText(mContext, "go to the information page", Toast.LENGTH_SHORT)
						.show();
				
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mobile.pusan.ac.kr/"));
				mContext.startActivity(intent);
			}
		}

		// handle item when touched
		// set touched item number, touch flag
		mTouched = false;
		PointF tPoint = new PointF();
		Iterator<PointF> pointIterator = mPointFList.iterator();
		for (int i = 0; i < mPointFList.size(); i++) {
			tPoint = pointIterator.next();

			if (convertedX > tPoint.x - (mFirstIconBitmap.getWidth() / 2)
					&& convertedX < tPoint.x
							+ (mFirstIconBitmap.getWidth() / 2)
					&& convertedY > tPoint.y
							- (mFirstIconBitmap.getHeight() / 2)
					&& convertedY < tPoint.y
							+ (mFirstIconBitmap.getHeight() / 2)) {
				mTouched = true;
				mTouchedItem = i;
			}
		}
		return super.onTouchEvent(event);
	}

	private void initBitmap() {
		// TODO Auto-generated method stub
		mIconBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.arrow);
		mIconBitmap = Bitmap.createScaledBitmap(mIconBitmap, 130,
				130, true);
		
		mFirstIconBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);
		mFirstIconBitmap = Bitmap.createScaledBitmap(mFirstIconBitmap, 130,
				130, true);
		
		mSecondIconBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);
		mSecondIconBitmap = Bitmap.createScaledBitmap(mSecondIconBitmap, 100,
				100, true);

		mThirdIconBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);
		mThirdIconBitmap = Bitmap.createScaledBitmap(mThirdIconBitmap, 100, 
				100, true);

		mFourthIconBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);
		mFourthIconBitmap = Bitmap.createScaledBitmap(mFourthIconBitmap, 100,
				100, true);

		mFifthIconBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon);
		mFifthIconBitmap = Bitmap.createScaledBitmap(mFifthIconBitmap, 100, 
				100, true);
	}

	// sensor initialize
	// set TYPE_ORIENTATION
	private void initSensor(Context context) {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		mOriSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mSensorManager.registerListener(this, mOriSensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	// initialize Handler
	// read First floor
	private void initHandler() {
		// TODO Auto-generated method stub
		if(FIRST.equals(mContext.getFloor())) {
			mARHandler = new ARHandler(mContext);
			mARHandler.setFloor(ARHandler.FIRST);
			mARHandler.readData();
		}
		else if(SECOND.equals(mContext.getFloor())) {
			mARHandler = new ARHandler(mContext);
			mARHandler.setFloor(ARHandler.SECOND);
			mARHandler.readData();
		}
		else if(THIRD.equals(mContext.getFloor())) {
			mARHandler = new ARHandler(mContext);
			mARHandler.setFloor(ARHandler.THIRD);
			mARHandler.readData();
		}
		else if(FOURTH.equals(mContext.getFloor())) {
			mARHandler = new ARHandler(mContext);
			mARHandler.setFloor(ARHandler.FOURTH);
			mARHandler.readData();
		}
		else if(FIFTH.equals(mContext.getFloor())) {
			mARHandler = new ARHandler(mContext);
			mARHandler.setFloor(ARHandler.FIFTH);
			mARHandler.readData();
		}
	}

	// initialize paint
	// set paint of marker, item
	private void initPaints() {
		// TODO Auto-generated method stub
		mShadowXMargin = 2;
		mShadowYMargin = 2;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.rgb(238, 229, 222));
		mPaint.setTextSize(25);

		mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mShadowPaint.setColor(Color.BLACK);
		mShadowPaint.setTextSize(25);

		mPopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPopPaint.setColor(Color.rgb(250, 250, 210));

		mTouchEffectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTouchEffectPaint.setColor(Color.rgb(205, 92, 92));
		mTouchEffectPaint.setStrokeWidth(5);
		mTouchEffectPaint.setStyle(Paint.Style.STROKE);
	}

	// initialize Rect
	// set paint, Rect coordinate
	private void initRectFs() {
		// TODO Auto-generated method stub
		mPopRect = new RectF((float) ((mWidth - mHeight) / 2) + mHeight / 20,
				(float) ((mHeight / 5) * 4), (float) mWidth
						- ((mWidth - mHeight) / 2) - mHeight / 20,
				(float) mHeight + ((mHeight / 5)) - 30);			
	}

	// draw Popup when touching marker
	private void drawPopup(Canvas pCanvas) {
		// TODO Auto-generated method stub
		pCanvas.drawRoundRect(mPopRect, 20, 20, mPopPaint);

		int xMargin = 20;
		int yMargin = 0;

		// show name of touched marker
		String tName = mPointHashMap.get(mTouchedItem);
		pCanvas.drawText(tName, ((mWidth - mHeight) / 2) + mHeight / 20
				+ xMargin + mShadowXMargin, ((mHeight / 5) * 4 + 40) + yMargin
				+ mShadowYMargin, mShadowPaint);

		pCanvas.drawText(tName, ((mWidth - mHeight) / 2) + mHeight / 20
				+ xMargin, ((mHeight / 5) * 4 + 40) + yMargin, mPaint);
	}

	// draw marker
	// check current position and marker position, calculate angle between two location.
	// reference angle by direction of current phone,
	// refresh screen
	// draw distance between two location
	// when the front angle is 90, between 75~105 viewing angle
	private PointF drawGrid(double tAx, double tAy, double tBx, double tBy,
			Canvas pCanvas, Paint pPaint, String name, String theme) {
		// TODO Auto-generated method stub

		// calculate current position and marker position
		double mXDegree = (double) (Math.atan((double) (tBy - tAy)
				/ (double) (tBx - tAx)) * 180.0 / Math.PI);
		float mYDegree = mYCompassDegree; // phone gradient angle

		// consider quadrant, set 0~360 angle 
		if (tBx > tAx && tBy > tAy) {
			;
		} else if (tBx < tAx && tBy > tAy) {
			mXDegree += 180;
		} else if (tBx < tAx && tBy < tAy) {
			mXDegree += 180;
		} else if (tBx > tAx && tBy < tAy) {
			mXDegree += 360;
		}
		
		// if angle more than 360, then -360
		if (mXDegree + mXCompassDegree < 360) {
			mXDegree += mXCompassDegree;
		} else if (mXDegree + mXCompassDegree >= 360) {
			mXDegree = mXDegree + mXCompassDegree - 360;
		}

		// calculate marker position angle
		// middle angle 90, viewing angle is 30(75~105)
		float mX = 0;
		float mY = 0;

		if (mXDegree > 75 && mXDegree < 105) {
			if (mYDegree > -105 && mYDegree < -80) {
				mX = (float) mWidth
						- (float) ((mXDegree - 75) * ((float) mWidth / 30));
				mYDegree = -(mYDegree);
				mY = (float) (mYDegree * ((float) mHeight / 180));
			}
		}
		
		if (mXDegree > 75 && mXDegree < 105) {
			if (mYDegree > -50 && mYDegree < -0) {
				mX = (float) mWidth
						- (float) ((mXDegree - 75) * ((float) mWidth / 30));
				mYDegree = -(mYDegree);
				mY = (float) (mYDegree * ((float) mHeight / 180));
				
				// calculate distance
				int distance = 50;
				Bitmap tIconBitmap = null;
				if (theme.equals("FIRST")) {
					tIconBitmap = mIconBitmap;
				}
				
				int iconWidth, iconHeight;
				iconWidth = tIconBitmap.getWidth();
				iconHeight = tIconBitmap.getHeight();

				// draw marker(icon, name, distance)
				// distance < 1000m
				if (distance <= mVisibleDistance * 1000) {
					if (distance < 1000) {
						pCanvas.drawBitmap(tIconBitmap, mX - (iconWidth / 2), mY
								- (iconHeight / 2), pPaint);
					} 
				}
				
				// return transfered coordinate
				PointF tPoint = new PointF();
				tPoint.set(mX - mWidth / 2, mY - mHeight / 2);
				return tPoint;
			}
		}

		// calculate distance
		Location locationA = new Location("Point A");
		Location locationB = new Location("Point B");

		locationA.setLongitude(tAx);
		locationA.setLatitude(tAy);

		locationB.setLongitude(tBx);
		locationB.setLatitude(tBy);

		int distance = (int) locationA.distanceTo(locationB);

		Bitmap tIconBitmap = null;
		if (theme.equals("FIRST")) {
			tIconBitmap = mFirstIconBitmap;
		} else if (theme.equals("SECOND")) {
			tIconBitmap = mSecondIconBitmap;
		} else if (theme.equals("THIRD")) {
			tIconBitmap = mThirdIconBitmap;
		} else if (theme.equals("FOURTH")) {
			tIconBitmap = mFourthIconBitmap;
		} else if (theme.equals("FIFTH")) {
			tIconBitmap = mFifthIconBitmap;
		}

		int iconWidth, iconHeight;
		iconWidth = tIconBitmap.getWidth();
		iconHeight = tIconBitmap.getHeight();

		// draw marker(icon, name, distance)
		// distance < 1000m
		if (distance <= mVisibleDistance * 1000) {
			if (distance < 1000) {
				pCanvas.drawBitmap(tIconBitmap, mX - (iconWidth / 2), mY
						- (iconHeight / 2), pPaint);

				pCanvas.drawText(name, mX - pPaint.measureText(name) / 2
						+ mShadowXMargin, mY + iconHeight / 2 + 30
						+ mShadowYMargin, mShadowPaint);

				pCanvas.drawText(name, mX - pPaint.measureText(name) / 2, mY
						+ iconHeight / 2 + 30, pPaint);

				pCanvas.drawText(distance + "m",
						mX - pPaint.measureText(distance + "m") / 2
								+ mShadowXMargin, mY + iconHeight / 2 + 60
								+ mShadowYMargin, mShadowPaint);

				pCanvas.drawText(distance + "m",
						mX - pPaint.measureText(distance + "m") / 2, mY
								+ iconHeight / 2 + 60, pPaint);

			} 
		}
		// return transfered coordinate
		PointF tPoint = new PointF();
		tPoint.set(mX - mWidth / 2, mY - mHeight / 2);
		return tPoint;
	}

	// read coordinate,
	// call draw function
	private void interpret(Canvas pCanvas) {
		// TODO Auto-generated method stub
		double tAx, tAy, tBx, tBy;
		//beaconEventOccur = BeaconEventFlag.instance();
		
		//tAx=beaconEventOccur.getcurXloc();
		//tAy=beaconEventOccur.getcurYloc();
		
		tAx = 35.2348984;
		tAy = 129.0827668;
		
		mPointFList = new ArrayList<PointF>();
		mPointHashMap = new HashMap<Integer, String>();

		String tName;
		PointF tPoint;
		String tFloor;
		ARData tData;

		// call marker drawing function
		mDataList = mARHandler.getmDataList();
		Iterator<ARData> DataIterator = mDataList.iterator();
		for (int i = 0; i < mDataList.size(); i++) {
			tData = DataIterator.next();
			if (tData != null) {
				tName = tData.getName();
				tBx = tData.getx();
				tBy = tData.gety();
				tFloor = tData.getFloor();

				// draw marker
				tPoint = drawGrid(tAx, tAy, tBx, tBy, pCanvas, mPaint, tName,
						tFloor);

				// store screen position of marker to list
				// store marker number and name to HashMap
				// check touched marker
				mPointFList.add(tPoint);
				mPointHashMap.put(i, tName);
			}
		}
	}

	// execute when sensor changed
	// store X, Y direction(phone), redraw overlay screen
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			mXCompassDegree = event.values[0];
			mYCompassDegree = event.values[1];
			this.invalidate();
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	// free sensor listener when destroying camera activity
	public void viewDestory() {
		mSensorManager.unregisterListener(this);
	}

	// set Overlay screen size in camera activity
	public void setOverlaySize(int width, int height) {
		// TODO Auto-generated method stub
		mWidth = width;
		mHeight = height;
	}
}
