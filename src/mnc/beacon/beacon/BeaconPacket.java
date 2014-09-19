package mnc.beacon.beacon;

import java.util.*;

import mnc.beacon.survey.kalman;

public class BeaconPacket {
	private String UUID;
	private int mMajor;
	private int mMinor;
	private int mPower;
	private int timer=100;
	private int count=0;
	
	private kalman kal;
	
	private double mRssi;
	private double init[];
	private double updat[];
	
	
	public BeaconPacket(String UUID, int mMajor, int mMinor, double mRssi,
			int mPower) {
		this.UUID = UUID;
		this.mMajor = mMajor;
		this.mMinor = mMinor;
		this.mRssi = mRssi;
		this.mPower = mPower;
		init=new double[2];
		updat=new double[2];
		kal= new kalman();
	}

	public void BeaconUpdate(double mRssi, int mPower)
	{	
		
		if (count == 0) {
			init[0] = (double) mRssi;
			init[1] = (double) mRssi;

			kal.init(init, 2);

		}
		++count;

		updat[0] = (double)mRssi;
		updat[1] = (double)mRssi;
		kal.update(updat, 3);
	
		this.mRssi =(double)kal.getCorrectedValues().clone()[0];
		
		//this.mRssi=mRssi;  
		this.mPower = mPower;
		
	}
	
	public String getUUID() {
		return UUID;
	}

	public int getMajor() {
		return mMajor;
	}

	public int getMinor() {
		return mMinor;
	}

	public double getRssi() {
		return mRssi;
	}

	public int getPower() {
		return mPower;
	}
	
	public int getTimer(){
		return timer;
	}
	
	public void addTimer(){
		timer= 100;
	}
	public void reduceTimer(){
		timer= timer -1;
	}

}
