package mnc.beacon.beacon;

public class CurrentBeacon {
	public static CurrentBeacon currentBeacon ;
	public static String UUID;
	public static int mMajor;
	public static int mMinor;
	public static int mPower;
	public static double mRssi;

	public CurrentBeacon(){
		
	}
	
	public CurrentBeacon(String UUID, int mMajor, int mMinor, double mRssi,int mPower) {
		this.UUID=UUID;
		this.mMajor=mMajor;
		this.mMinor=mMinor;
		this.mRssi=mRssi;
		this.mPower=mPower;
	}
	
	public static CurrentBeacon instance() {
		if( currentBeacon == null) {
			currentBeacon = new CurrentBeacon();
			return currentBeacon;
		}
		else
			return currentBeacon;
	}
}
