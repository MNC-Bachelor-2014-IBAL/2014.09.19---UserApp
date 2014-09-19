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

package mnc.beacon.ble;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import mnc.beacon.*;
import mnc.beacon.beacon.*;
import mnc.beacon.mainservice.*;
import mnc.beacon.server.Http;
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
public class DeviceScanService extends Service {

	private BluetoothAdapter mBluetoothAdapter;
	private BeaconPacket beacon;
	private BeaconManager beaconManager;
	private boolean mScanning;
	private Handler mHandler;
	private BeaconPacketParser IbeaconData;
	private static final int REQUEST_ENABLE_BT = 1;
	private static final long SCAN_PERIOD = 99999999;

	String byteToString;
	
	byte[] StringTobyte;	
	byte[] test = {'a','b','c'};

	int count = 0;

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mHandler = new Handler();

		// Use this check to determine whether BLE is supported on the device.
		// Then you can
		// selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
					.show();

			// finish();
		}

		// Initializes a Bluetooth adapter. For API level 18 and above, get a
		// reference to
		// BluetoothAdapter through BluetoothManager.
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// Checks if Bluetooth is supported on the device.
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, R.string.error_bluetooth_not_supported,
					Toast.LENGTH_SHORT).show();
			// finish();

		}

		scanLeDevice(true);

		return super.onStartCommand(intent, flags, startId);
	}

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);

				}
			}, SCAN_PERIOD);

			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}

	}

	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				final byte[] scanRecord) {
			
			beaconManager = BeaconManager.instance();	
			String scandata = new String(scanRecord, 0, scanRecord.length);

			IbeaconData = new BeaconPacketParser(scanRecord);

			beacon = new BeaconPacket(IbeaconData.getUUID(),
					IbeaconData.getMajor(), IbeaconData.getMinor(), rssi,
					IbeaconData.getCalibratedTxPower());
			
			if (!beaconManager.checkDouble(beacon)) {
				beaconManager.beaconList.add(beacon);
			}
			beaconManager.reduceTimer();
			
			
		}

	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub

		return null;
	}

	public static String BinToHex(byte[] buf) {
		String res = "";
		String token = "";
		for (int ix = 0; ix < buf.length; ix++) {
			token = Integer.toHexString(buf[ix]);
			// CommonUtil.println("[" + ix + "] token value : " + token +
			// " len : " + token.length());
			if (token.length() >= 2)
				token = token.substring(token.length() - 2);
			else {
				for (int jx = 0; jx < 2 - token.length(); jx++)
					token = "0" + token;
			}
			res += " " + token;
		}

		return res.toUpperCase();
	}
	
	public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}