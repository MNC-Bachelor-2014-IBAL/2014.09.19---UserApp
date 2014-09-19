package mnc.beacon.mainservice;

import mnc.beacon.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CancelPopupActivity extends Activity {
	private static final int DIALOG_YES_NO_MESSAGE = 0;
	BeaconEventFlag beaconEventFalg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.popup);
	    beaconEventFalg = BeaconEventFlag.instance();
	    beaconEventFalg.backGround = false;
	   
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | 
	    		WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | 
	    		WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
	    
	    showDialog(DIALOG_YES_NO_MESSAGE);
	}
	
	@Override
	protected Dialog onCreateDialog(int id){
		
		switch(id) {
		case DIALOG_YES_NO_MESSAGE:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("find Beacon")
			.setMessage("애플리케이션을 실행하시겠습니까?")
			.setCancelable(false)
			.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {		
					Intent intent = new Intent(CancelPopupActivity.this, MainActivity.class);
					beaconEventFalg.doService = false;
					
					startActivity(intent);
					finish();
				}
			})
			.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton){
					dialog.cancel();
					finish();
				}
			});
			AlertDialog alert = builder.create();
			return alert;
		}
		return null;
	}
}
