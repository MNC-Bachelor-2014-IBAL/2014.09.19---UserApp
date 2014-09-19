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
	private RectF mFirstRect;
	private RectF mSecondRect;
	private RectF mThirdRect;
	private RectF mFourthRect;
	private RectF mFifthRect;
	private Paint mPopPaint;
	private int mVisibleDistance = 3;
	private float mTouchedY;
	private float mTouchedX;
	private boolean mScreenTouched = false;
	private int mCounter = 0;
	private Paint mTouchEffectPaint;
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
		// 비트맵, 센서, 페인트, 핸들러 초기화
		// initialize bitmap, sensor, paint, Handler
		initBitmap();
		initSensor(context);
		initPaints();
		initHandler();
	}
	
	// onSensorChanged에서 센서값 TYPE_ORIENTATION이 일정한 시간마다 INVALIDATE되어 실행됨
	// 정보를 계속 그리면서 표현, data를 해석하면서 아이템을 그림
	public void onDraw(Canvas canvas) {
		canvas.save();
		// 안드로이드 2.1이하에서는 카메라 화면이 오른쪽으로 90도 돌아간 화면으로 나옴
		// 화면을 돌리기 위하여 사용\
		canvas.rotate(270, mWidth / 2, mHeight / 2);
		// initialize Rect
		initRectFs();
		// initialize Handler
		initHandler();
		// 아이템이 터치된 상태일때 팝업을 그림
		if (mTouched == true) {
			drawPopup(canvas);
		}
		// data를 읽어들이고, drawGrid를 실행시켜 마커들을 그림
		interpret(canvas);
		// 회전된 카메라를 원상복귀함
		canvas.restore();
		// 스크린이 터치되었을때 효과를 그림
		if (mScreenTouched == true && mCounter < 15) {
			drawTouchEffect(canvas);
			mCounter++;
		} else {
			mScreenTouched = false;
			mCounter = 0;
		}
	}

	// 스크린이 터치될때의 효과를 그림 원 3개를 물결처럼 그림
	private void drawTouchEffect(Canvas pCanvas) {
		// TODO Auto-generated method stub
		pCanvas.drawCircle(mTouchedX, mTouchedY, mCounter * 1,
				mTouchEffectPaint);
		pCanvas.drawCircle(mTouchedX, mTouchedY, mCounter * 2,
				mTouchEffectPaint);
		pCanvas.drawCircle(mTouchedX, mTouchedY, mCounter * 3,
				mTouchEffectPaint);
	}

	// 스크린이 터치되었을때 좌표를 해석하고 처리
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		// 화면이 회전되었기에 좌표도 변환함
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
		
		// 팝업을 터치시 처리
		// 터치시 정보 페이지로 이동
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

		// 아이템을 터치시 처리
		// 터치시 플래그, 터치된 아이템 번호 설정
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

	// 센서 초기화
	// TYPE_ORIENTATION 사용할 수 있게 설정
	private void initSensor(Context context) {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		mOriSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mSensorManager.registerListener(this, mOriSensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	// Handler 초기화
	// 처음에는 First floor 읽음
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

	// 페인트 초기화
	// 그려질 여러 메뉴, 아이템의 페인트 설정
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
		mPopPaint.setColor(Color.rgb(131, 139, 131));

		mTouchEffectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTouchEffectPaint.setColor(Color.rgb(205, 92, 92));
		mTouchEffectPaint.setStrokeWidth(5);
		mTouchEffectPaint.setStyle(Paint.Style.STROKE);
	}

	// 사각형 초기화
	// 보여질 여러 사각형 좌표, 페인트 설정
	private void initRectFs() {
		// TODO Auto-generated method stub
		int themeRectWidth = (mHeight - (mHeight / 20 * 2)) / 6;

		mFirstRect = new RectF((float) ((mWidth - mHeight) / 2) + mHeight / 20
				+ (themeRectWidth * 0), (float) (-(mWidth - mHeight) / 2)
				+ mHeight / 20, (float) ((mWidth - mHeight) / 2) + mHeight / 20
				+ (themeRectWidth * 1), (float) -(mHeight / 20));

		mSecondRect = new RectF((float) ((mWidth - mHeight) / 2) + mHeight
				/ 20 + (themeRectWidth * 1), (float) (-(mWidth - mHeight) / 2)
				+ mHeight / 20, (float) ((mWidth - mHeight) / 2) + mHeight / 20
				+ (themeRectWidth * 2), (float) -(mHeight / 20));

		mThirdRect = new RectF((float) ((mWidth - mHeight) / 2) + mHeight
				/ 20 + (themeRectWidth * 2), (float) (-(mWidth - mHeight) / 2)
				+ mHeight / 20, (float) ((mWidth - mHeight) / 2) + mHeight / 20
				+ (themeRectWidth * 3), (float) -(mHeight / 20));

		mFourthRect = new RectF((float) ((mWidth - mHeight) / 2) + mHeight
				/ 20 + (themeRectWidth * 3), (float) (-(mWidth - mHeight) / 2)
				+ mHeight / 20, (float) ((mWidth - mHeight) / 2) + mHeight / 20
				+ (themeRectWidth * 4), (float) -(mHeight / 20));

		mFifthRect = new RectF((float) ((mWidth - mHeight) / 2) + mHeight / 20
				+ (themeRectWidth * 4), (float) (-(mWidth - mHeight) / 2)
				+ mHeight / 20, (float) ((mWidth - mHeight) / 2) + mHeight / 20
				+ (themeRectWidth * 5), (float) -(mHeight / 20));

		mPopRect = new RectF((float) ((mWidth - mHeight) / 2) + mHeight / 20,
				(float) ((mHeight / 5) * 4), (float) mWidth
						- ((mWidth - mHeight) / 2) - mHeight / 20,
				(float) mHeight + ((mHeight / 5)) - 30);			
	}

	// data 터치시 나타나는 popup 그림
	private void drawPopup(Canvas pCanvas) {
		// TODO Auto-generated method stub
		pCanvas.drawRoundRect(mPopRect, 20, 20, mPopPaint);

		int xMargin = 20;
		int yMargin = 0;

		// 터치된 아이템을 이용하여 이름이 무엇인지 알아내고 보여줌
		String tName = mPointHashMap.get(mTouchedItem);
		pCanvas.drawText(tName, ((mWidth - mHeight) / 2) + mHeight / 20
				+ xMargin + mShadowXMargin, ((mHeight / 5) * 4 + 40) + yMargin
				+ mShadowYMargin, mShadowPaint);

		pCanvas.drawText(tName, ((mWidth - mHeight) / 2) + mHeight / 20
				+ xMargin, ((mHeight / 5) * 4 + 40) + yMargin, mPaint);
	}

	// 선택된 아이콘의 마커를 그림
	// 현재의 위치정보와 마커의 위치정보를 이용하여 두 위치간의 각도를 계산하고,
	// 현재 기기의 방향이 동쪽 기준 각도가 몇인지를 참고로
	// 기기 화면에 계속 새로고침됨
	// 두 위치간의 거리 또한 표시
	// 정면이 90도라하였을때 75도에서 105도 사이 30도가 시야각
	private PointF drawGrid(double tAx, double tAy, double tBx, double tBy,
			Canvas pCanvas, Paint pPaint, String name, String theme) {
		// TODO Auto-generated method stub

		// 현재 위치와 마커의 위치를 계산하는 공식
		double mXDegree = (double) (Math.atan((double) (tBy - tAy)
				/ (double) (tBx - tAx)) * 180.0 / Math.PI);
		float mYDegree = mYCompassDegree; // 기기의 기울임각도

		// 4/4분면을 고려하여 0~360도가 나오게 설정
		if (tBx > tAx && tBy > tAy) {
			;
		} else if (tBx < tAx && tBy > tAy) {
			mXDegree += 180;
		} else if (tBx < tAx && tBy < tAy) {
			mXDegree += 180;
		} else if (tBx > tAx && tBy < tAy) {
			mXDegree += 360;
		}

		// 두 위치간의 각도에 현재 스마트폰이 동쪽기준 바라보고 있는 방향 만큼 더해줌
		// 360도(한바퀴)가 넘었으면 한바퀴 회전한것이기에 360를 빼줌
		if (mXDegree + mXCompassDegree < 360) {
			mXDegree += mXCompassDegree;
		} else if (mXDegree + mXCompassDegree >= 360) {
			mXDegree = mXDegree + mXCompassDegree - 360;
		}

		// 계산된 각도 만큼 기기 정중앙 화면 기준 어디에 나타날지 계산함
		// 정중앙은 90도, 시야각은 30도로 75 ~ 105 사이일때만 화면에 나타남
		float mX = 0;
		float mY = 0;

		if (mXDegree > 75 && mXDegree < 105) {
			if (mYDegree > -105 && mYDegree < -75) {
				mX = (float) mWidth
						- (float) ((mXDegree - 75) * ((float) mWidth / 30));
				mYDegree = -(mYDegree);
				mY = (float) (mYDegree * ((float) mHeight / 180));
			}
		}

		// 두 위치간의 거리를 계산함
		Location locationA = new Location("Point A");
		Location locationB = new Location("Point B");

		locationA.setLongitude(tAx);
		locationA.setLatitude(tAy);

		locationB.setLongitude(tBx);
		locationB.setLatitude(tBy);

		int distance = 50;

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

		// 마커에 해당하는 아이콘과 이름, 거리를 출력
		// 거리는 1000미터 이하
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
		// 현재의 회전되기전의 좌표를 회전된 좌표로 변환한후 반환함
		PointF tPoint = new PointF();
		tPoint.set(mX - mWidth / 2, mY - mHeight / 2);
		return tPoint;
	}

	// 좌표를 읽어
	// 그리는 함수를 호출
	private void interpret(Canvas pCanvas) {
		// TODO Auto-generated method stub
		double tAx, tAy, tBx, tBy;
		beaconEventOccur = BeaconEventFlag.instance();
		
		tAx=beaconEventOccur.getcurXloc();
		tAy=beaconEventOccur.getcurYloc();
		
	//	tAx = 50;
	//	tAy = 50;
		
		mPointFList = new ArrayList<PointF>();
		mPointHashMap = new HashMap<Integer, String>();

		String tName;
		PointF tPoint;
		String tFloor;
		ARData tData;

		// 좌표를 하나씩 읽어 마커를 화면에 그리는 함수 호출
		mDataList = mARHandler.getmDataList();
		Iterator<ARData> DataIterator = mDataList.iterator();
		for (int i = 0; i < mDataList.size(); i++) {
			tData = DataIterator.next();
			if (tData != null) {
				tName = tData.getName();
				tBx = tData.getx();
				tBy = tData.gety();
				tFloor = tData.getFloor();

				// 화면에 그림
				tPoint = drawGrid(tAx, tAy, tBx, tBy, pCanvas, mPaint, tName,
						tFloor);

				// 마커의 화면 위치를 리스트로 저장
				// 해시맵으로 마커의 번호와 이름을 저장
				// 마커가 터치되었을때 어떤 마커가 터치 되었는지 확인하기 위함
				mPointFList.add(tPoint);
				mPointHashMap.put(i, tName);
			}
		}
	}

	// 센서가 바뀔때마다 실행됨
	// 기기의 방향중 X, Y값을 저장하고 오버레이 화면을 다시 그리게 함
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

	// 카메라 액티비티가 소멸될때 센서 리스너를 해제
	// free sensor listener when destroying camera activity
	public void viewDestory() {
		mSensorManager.unregisterListener(this);
	}

	// 카메라 액티비티에서 오버레이 화면 크기를 설정함
	// set Overlay screen size in camera activity
	public void setOverlaySize(int width, int height) {
		// TODO Auto-generated method stub
		mWidth = width;
		mHeight = height;
	}
}
