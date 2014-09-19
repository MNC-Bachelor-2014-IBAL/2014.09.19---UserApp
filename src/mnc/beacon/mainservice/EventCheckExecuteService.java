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
import mnc.beacon.server.Http;
import mnc.beacon.survey.calweight;
import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import android.widget.*;
/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class EventCheckExecuteService extends Service {
	private BluetoothAdapter mBluetoothAdapter;
	private BeaconPacket beacon;
	private BeaconManager beaconManager;
	private boolean mScanning;
	private Handler mHandler;
	Object obj;
	private static final int REQUEST_ENABLE_BT = 1;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 2000;
	private ToastClass toastClass;
	private BeaconEventFlag beaconEventFlag;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mHandler = new Handler();
		toastClass = ToastClass.instance();
		beaconEventFlag = BeaconEventFlag.instance();
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
					
					JSONObject beaconObj = new JSONObject();
					JSONArray jArray = new JSONArray(); // 배열
					JSONParser parser = new JSONParser();
					Http q3 = new Http();
					Map data5 = new HashMap();
					data5.put("abc", "abc");
					String eventid = null;
					String eventvalue = null;
					String result = null;
					String resul1t1 = q3.get("http://164.125.34.173:8080/event.jsp",
							data5);
					// String resul1t1 =
					// q3.get("http://192.168.1.81:8080/event.jsp",data5);
					// resul1t1="ddddd";
					try {
						obj = parser.parse(resul1t1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					beaconObj = (JSONObject) obj;

					jArray = (JSONArray) beaconObj.get("eventArray");

					JSONObject j = new JSONObject();
					for (int i = 0; i < jArray.size(); i++) {
						j = (JSONObject) jArray.get(i);

						eventid = j.get("eventid").toString();
						//beaconEventFlag.addEvent(eventid);
						eventvalue = j.get("eventvalue").toString();
						beaconEventFlag.addEvent(eventvalue);
						result +=  eventid + " " + eventvalue + " ";
					//	Log.i("testflag", eventid + eventvalue);

					}
					beaconEventFlag.setEventFlag();
					toastClass.setString(result);
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
}