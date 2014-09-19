package mnc.beacon.survey;

public class CalDistance {
	public static double dist;
	public static double caldist(double Mpower, double Mrssi){
		
		double ratio = Mrssi / Mpower;
		double rssiCorrection = 0.96D +Math.pow(Math.abs(Mrssi),3.0D) % 10.0D/150.0D;
				
		return (0.103D + 0.89978D*Math.pow(ratio, 7.71D))*rssiCorrection;
	}
	

}
