package mnc.beacon.mapview;

import mnc.beacon.beacon.*;
import mnc.beacon.mainservice.*;
import mnc.beacon.survey.*;
import android.os.*;

public class MapviewThread extends Thread {
	Handler handler;
	BeaconEventFlag beaconEventOccur;
	BeaconPacket beacon1, beacon2;
	BeaconManager beaconManager;


	public MapviewThread(Handler handler) {
		this.handler = handler;

	}

	public void run() {
		beaconManager = BeaconManager.instance();
		while (true) {
			
			Message msg = new Message();
			
			msg.obj = (BeaconEventFlag)BeaconEventFlag.instance();
			msg.what = 1;
			handler.sendMessage(msg);
			
			
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
