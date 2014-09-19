package mnc.beacon.survey;

public class FingerTest {
	public static FingerTest fingerTest;
	
	
	public static FingerTest instance() {
		if( fingerTest == null) {
			fingerTest = new FingerTest();
			return fingerTest;
		}
		else
			return fingerTest;
	}
	
	
	
	

}
