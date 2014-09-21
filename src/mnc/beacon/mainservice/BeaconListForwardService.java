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
public class BeaconListForwardService extends Service {

	// private LeDeviceListAdapter mLeDeviceListAdapter;
	private BluetoothAdapter mBluetoothAdapter;
	private BeaconPacket beacon;
	private BeaconManager beaconManager;
	private boolean mScanning;
	private Handler mHandler;

	private int numArray[] = new int[4];
	private BeaconPacketParser IbeaconData;
	private static final int REQUEST_ENABLE_BT = 1;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 3000;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//mHandler = new Handler();
		Thread thread = new Thread(runService);
		thread.start();
		//checkEvent(true);
		return super.onStartCommand(intent, flags, startId);
	}
	
	Runnable runService = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			Http qq = new Http();

			Map data1 = new HashMap();
			int count = 0;
			
			while (true) {
				beaconManager = BeaconManager.instance();
				JSONObject sendObject = new JSONObject();
				JSONArray objArray = new JSONArray();
				Iterator<BeaconPacket> iterator = beaconManager.beaconList
						.iterator();

				while (iterator.hasNext()) {
					JSONObject obj = new JSONObject();
					BeaconPacket beacon = iterator.next();
					obj.put("TIMESTAMP", System.currentTimeMillis());
					obj.put("UUID", beacon.getUUID());
					obj.put("MAJOR", beacon.getMajor());
					obj.put("MINOR", beacon.getMinor());
					obj.put("TXPOWER", beacon.getPower());
					obj.put("RSSI", beacon.getRssi());
					objArray.add(obj);

				}

				sendObject.put("sendData", objArray);
				data1.put("abc", sendObject);
				String resul1t = qq.get("http://164.125.34.173:8080/test.jsp",
						data1);
				count++;
				//toastClass.setString(""+count);
				Log.i("kang",count+" " + resul1t );
				
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