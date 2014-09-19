package mnc.beacon.mainservice;

import java.util.*;

import mnc.beacon.beacon.*;

public class BeaconEventFlag {

	public String[] value = new String[10];
	private static int count = 0;
	public static boolean firstMapdown = false;
	public static boolean startService = false;
	public static boolean startService1 = false;
	public static boolean startService2 = false;
	public static boolean startService3 = false;
	public static boolean Event3Occur = false;
	public static boolean Event4Occur = false;
	public static boolean Event5Occur = false;
	public static boolean Event6Occur = false;
	public static boolean backGround = false;
	public static boolean doService = false;
	public ArrayList<String> str = new ArrayList<String>();
	
	private static int currentLocation;
	private static double currentYloc;
	private static double currentXloc;
	private static int cellnum;
	private static int startcellnum;
	
	private static int[] weight = new int[12];

	public static BeaconEventFlag eventOccur;

	public static BeaconEventFlag instance() {
		if (eventOccur == null) {
			eventOccur = new BeaconEventFlag();
		}
		return eventOccur;
		
	}

	public static int getCurrentLocation() {
		return currentLocation;
	}

	public static int[] getWeight() {
		return weight;
	}

	public void setCurrentLocation(int cur) {
		this.currentLocation = cur;
	}

	public void setWeight(int index, int value) {
		weight[index] = value;
	}

	public void addEvent(String eventValue) {
		value[count++] = eventValue;
	}

	public void setEventFlag() {

		if (value[0].equalsIgnoreCase("true")) {
			firstMapdown = true;
			
		} else
			firstMapdown = false;
		
		if (value[1].equalsIgnoreCase("true")) {
			startService = true;
		} else
			startService = false;
		
		if (value[2].equalsIgnoreCase("true")) {
			startService1 = true;
			startService2 =false;
			startService3 = false;
		} else
			startService1 = false;
		
		
		if (value[3].equalsIgnoreCase("true")) {
			startService2 = true;
			startService1 =false;
			startService3 = false;
		} else
			startService2 = false;
		
		
		if (value[4].equalsIgnoreCase("true")) {
			startService3 = true;
			startService1 =false;
			startService2 = false;
		} else
			startService3 = false;
		
		
		
		
		count =0;

	}
	
	public void setcurXYloc(double x, double y){
		this.currentXloc=x;
		this.currentYloc=y;
	}
	
	public double getcurXloc(){
		
		return  this.currentXloc;
	}

	public double getcurYloc(){
		
		return  this.currentYloc;
	}
	
	
	public int getcellnum(){
		
		return  this.cellnum;
	}

	public int getstartcellnum(){
		
		return  this.startcellnum;
	}
	
	public void setcellnum(int cellnum){
		
		this.cellnum=cellnum;
	}

	public void setstartcellnum(int startcellnum){
		
		this.startcellnum=startcellnum;
	}

}
