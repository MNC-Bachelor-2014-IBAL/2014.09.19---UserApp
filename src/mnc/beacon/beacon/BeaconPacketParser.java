package mnc.beacon.beacon;

import java.util.Arrays;


public final class BeaconPacketParser {

	private final byte[] mData;
	private final int mCalibratedTxPower;
	private final int mCompanyIdentidier;
	private final int mIBeaconAdvertisment;
	private final int mMajor;
	private final int mMinor;
	private final String mUUID;

	
	public BeaconPacketParser(byte[] data) {

		mData = data;

		mCompanyIdentidier = BeaconByteUtils.getIntFrom2ByteArray(BeaconByteUtils
				.invertArray(Arrays.copyOfRange(mData, 5, 7)));

		mIBeaconAdvertisment = BeaconByteUtils.getIntFrom2ByteArray(Arrays
				.copyOfRange(mData, 2, 4));
		mUUID = calculateUUIDString(Arrays.copyOfRange(mData, 4, 20));
		mMajor = BeaconByteUtils.getIntFrom2ByteArray(Arrays.copyOfRange(mData, 25,
				27));
		mMinor = BeaconByteUtils.getIntFrom2ByteArray(Arrays.copyOfRange(mData, 27,
				29));
		mCalibratedTxPower = data[24];
	}

	public int getCalibratedTxPower() {
		return mCalibratedTxPower;
	}

	public int getCompanyIdentifier() {
		return mCompanyIdentidier;
	}

	public int getIBeaconAdvertisement() {
		return mIBeaconAdvertisment;
	}

	public int getMajor() {
		return mMajor;
	}

	public int getMinor() {
		return mMinor;
	}

	public String getUUID() {
		return mUUID;
	}

	private static String calculateUUIDString(final byte[] uuid) {
		final StringBuffer sb = new StringBuffer();

		for (int i = 0; i < uuid.length; i++) {
			if (i == 4) {
				sb.append('-');
			}
			if (i == 6) {
				sb.append('-');
			}
			if (i == 8) {
				sb.append('-');
			}
			if (i == 10) {
				sb.append('-');
			}

			sb.append(Integer.toHexString(BeaconByteUtils.getIntFromByte(uuid[i])));
		}

		return sb.toString();
	}
}
