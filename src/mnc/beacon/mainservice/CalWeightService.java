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
public class CalWeightService extends Service {

	private int beaconnumber = 6;
	private int cellnumber = 8;

	// private LeDeviceListAdapter mLeDeviceListAdapter;
	private BluetoothAdapter mBluetoothAdapter;
	private BeaconPacket beacon;
	private BeaconManager beaconManager;
	private boolean mScanning;
	private Handler mHandler;
	private int cellnum = 12;
	// private int startcellnum;

	private BeaconPacketParser IbeaconData;

	private static final int REQUEST_ENABLE_BT = 1;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 3000;
	BeaconEventFlag beaconEventOccur;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mHandler = new Handler();
		Log.i("okokok", "okokok");
		beaconEventOccur = BeaconEventFlag.instance();
		Thread thread = new Thread(runService);
		thread.start();

		return super.onStartCommand(intent, flags, startId);
	}

	Runnable runService = new Runnable() {
		@Override
		public void run() {
			Object obj = null;
			Http requesthttp = new Http();
			Map requestdata = new HashMap();
			JSONParser resultParser = new JSONParser();
			String result = "";
			String result1 = "";

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			while (true) {

				requestdata.put("request", "data");

				String fingerresult = requesthttp.get(
						"http://164.125.34.173:8080/calLocation.jsp",
						requestdata);
				// 위에서는 kalweight DB에 각 좌표의 weight 저장, currentlocation도 DB에
				// 따로 저장.

				requestdata.put("request", "data");
				String fingerresult1 = requesthttp.get(
						"http://164.125.34.173:8080/returnLocation.jsp",
						requestdata);

				try {
					obj = resultParser.parse(fingerresult1);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				JSONObject object = (JSONObject) obj;
				result = object.get("index").toString();
				result1 = object.get("rssi").toString();

				for (int i = 0; i < cellnum; i++) {
					beaconEventOccur.setWeight(i, Integer.parseInt(object.get(
							"calcell" + Integer.toString(i)).toString()));

				}
				beaconEventOccur.setCurrentLocation(Integer.parseInt(result));
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub

		return null;
	}
}