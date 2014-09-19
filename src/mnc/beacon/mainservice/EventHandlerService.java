/*
 * Copyright (C) 2013 The An\droid Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mnc.beacon.mainservice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import mnc.beacon.*;
import mnc.beacon.beacon.*;
import mnc.beacon.mapview.MapView;
import mnc.beacon.server.Http;
import mnc.beacon.survey.calweight;
import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import android.view.Window;
import android.widget.*;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class EventHandlerService extends Service {

	private BluetoothAdapter mBluetoothAdapter;
	private BeaconPacket beacon;
	private BeaconManager beaconManager;
	private boolean mScanning;
	private Handler mHandler;
	private BeaconEventFlag beaconEventFlag;
	Object obj;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final long SCAN_PERIOD = 2000;
	private ToastClass toastClass;
	Context context;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 
		// TODO Auto-generated method stub
		mHandler = new Handler();
		toastClass = ToastClass.instance();
		beaconEventFlag = BeaconEventFlag.instance();
		context = getApplicationContext();
		checkEvent(true);

		return super.onStartCommand(intent, flags, startId);
	}

	private void checkEvent(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);

					if(beaconEventFlag.firstMapdown == true && beaconEventFlag.startService == true ) {
						Intent intent2 = new Intent(EventHandlerService.this,CalWeightService.class);
						startService(intent2);
					}
					if(beaconEventFlag.backGround == true && !beaconEventFlag.doService && beaconEventFlag.startService ==true && isScreenOn(context) == false ) {
						Intent intent = new Intent(EventHandlerService.this , CancelPopupActivity.class);
						beaconEventFlag.doService = true;
						
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
						
						startActivity(intent);
					}
						else
					checkEvent(true);
				}
			}, SCAN_PERIOD);

		}

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub

		return null;
	}
	public static boolean isScreenOn(Context context) {
		return ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
	}

}