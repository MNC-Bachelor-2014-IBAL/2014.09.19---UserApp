package mnc.beacon.beacon;

import java.nio.ByteBuffer;

public class BeaconByteUtils {

	private static final String HEXES = "0123456789ABCDEF";

	public static String byteArrayToHexString(final byte[] array) {
		final StringBuffer sb = new StringBuffer();
		boolean firstEntry = true;
		sb.append('[');

		for (final byte b : array) {
			if (!firstEntry) {
				sb.append(", ");
			}
			sb.append(HEXES.charAt((b & 0xF0) >> 4));
			sb.append(HEXES.charAt((b & 0x0F)));
			firstEntry = false;
		}

		sb.append(']');
		return sb.toString();
	}

	public static boolean doesArrayBeginWith(byte[] array, byte[] prefix) {
		if (array.length < prefix.length) {
			return false;
		}

		for (int i = 0; i < prefix.length; i++) {
			if (array[i] != prefix[i]) {
				return false;
			}
		}

		return true;
	}

	public static int getIntFrom2ByteArray(byte[] input) {
		final byte[] result = new byte[4];

		result[0] = 0;
		result[1] = 0;
		result[2] = input[0];
		result[3] = input[1];

		return BeaconByteUtils.getIntFromByteArray(result);
	}

	public static int getIntFromByte(final byte bite) {
		return Integer.valueOf(bite & 0xFF);
	}

		public static int getIntFromByteArray(final byte[] bytes) {
		return ByteBuffer.wrap(bytes).getInt();
	}

	public static long getLongFromByteArray(final byte[] bytes) {
		return ByteBuffer.wrap(bytes).getLong();
	}

	public static byte[] invertArray(byte[] array) {
		final int size = array.length;
		byte temp;

		for (int i = 0; i < size / 2; i++) {
			temp = array[i];
			array[i] = array[size - 1 - i];
			array[size - 1 - i] = temp;
		}

		return array;
	}
}
