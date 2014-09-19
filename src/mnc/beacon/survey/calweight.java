package mnc.beacon.survey;

public class calweight {

	public static int retweight(double rssiave, double currssi) {

		double result;
		if(rssiave<currssi){
			result=currssi-rssiave; 
		}
		else
			result=rssiave-currssi;
	
		if ((result) < 1 ) {
				return 7;
		} else if ((result) < 2 ) {
				return 6;
		} else if ((result) < 3 ) {
				return 5;
		} else if ((result) < 4) {
				return 4;
		} else if ((result) < 6) {
				return 3;
		} else if ((result) < 8 ) {
				return 2;
		} else if ((result) < 10) {
				return 1;
		}

		return 0;
	}

}
