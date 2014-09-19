package mnc.beacon.ar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mnc.beacon.mainservice.BeaconEventFlag;
import mnc.beacon.server.Http;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.content.Context;
import android.os.StrictMode;

public class ARHandler {
	private List<ARData> tRecordList;
	private String mFloor;
	private Object mContext;

	public static final String FIRST = "FIRST";
	public static final String SECOND = "SECOND";
	public static final String THIRD = "THIRD";
	public static final String FOURTH = "FOURTH";
	public static final String FIFTH = "FIFTH";
	BeaconEventFlag beaconEventOccur;
	public ARHandler(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void readData() {
		JSONObject beaconObj = new JSONObject();
		JSONArray jArray = new JSONArray(); // 배열
		JSONParser parser = new JSONParser();
		Object obj = null;
		JSONObject j;
		
	beaconEventOccur = BeaconEventFlag.instance();
	
		
		tRecordList = new ArrayList<ARData>();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Http q3 = new Http();
		Map data5 = new HashMap();
		data5.put("abc", "abc");
		String result = q3.get("http://164.125.34.173:8080/ARdata.jsp",
				data5);
		try {
			obj = parser.parse(result);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		beaconObj = (JSONObject) obj;
		jArray = (JSONArray) beaconObj.get("ARdata");
		j = new JSONObject();
		
		for (int i = 0; i < jArray.size(); i++) {
			j = (JSONObject) jArray.get(i);
			ARData Record = new ARData();
				Record.setFloor(j.get("floor").toString());
			if(mFloor.equals(Record.getFloor())) {
				Record.setFloor(j.get("floor").toString());
				Record.setName(j.get("name").toString());
				Record.setx(Double.parseDouble(j.get("x").toString()));
				Record.sety(Double.parseDouble(j.get("y").toString()));
				Record.setLocation(j.get("location").toString());
				tRecordList.add(Record);
			}
			else{
			
				if(j.get("name").toString().equals(Integer.toString(beaconEventOccur.getCurrentLocation()))){
					beaconEventOccur.setcurXYloc(Double.parseDouble(j.get("x").toString()), Double.parseDouble(j.get("y").toString()));
				}
			}
		}
	}
	
	// return record list
	public List<ARData> getmDataList() {
		return tRecordList;
	}

	public void setFloor(String Floor) {
		// TODO Auto-generated method stub
		mFloor = Floor;
	}

	public String getFloor() {
		// TODO Auto-generated method stub
		return mFloor;
	}
}
