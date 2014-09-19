package mnc.beacon.survey;

import android.util.Log;

public class Triangulation {

	public Double AB;
	public Double BC;
	public Double Userx;
	public Double Usery;
	public Double Usery2;

	public void CalcTri(double Ix1, double Iy1, double Ix2, double Iy2,
			double Ix3, double Iy3, double dist1, double dist2, double dist3) {

		if (((Ix2 - Ix1) * (Iy3 - Iy2) - (Ix3 - Ix2) * (Iy2 - Iy1)) != 0
				&& (Iy2 - Iy1) != 0 && (Iy3 - Iy2) != 0) {
			AB = (dist1 * dist1) - (dist2 * dist2) - (Ix1 * Ix1) - (Iy1 * Iy1)
					+ (Ix2 * Ix2) + (Iy2 * Iy2);
			BC = (dist2 * dist2) - (dist3 * dist3) - (Ix2 * Ix2) - (Iy2 * Iy2)
					+ (Ix3 * Ix3) + (Iy3 * Iy3);
			Userx = ((AB * (Iy3 - Iy2)) - (BC * (Iy2 - Iy1)))
					/ (2 * ((Ix2 - Ix1) * (Iy3 - Iy2) - (Ix3 - Ix2)
							* (Iy2 - Iy1)));
			Usery = (AB - (2 * Userx * (Ix2 - Ix1))) / (2 * (Iy2 - Iy1));
			Usery2 = (BC - 2 * Userx * (Ix3 - Ix2)) / (2 * (Iy3 - Iy2));
			Usery = (Usery + Usery2) / 2;
		}

		else if (((Ix3 - Ix2) * (Iy1 - Iy3) - (Ix1 - Ix3) * (Iy3 - Iy2)) != 0
				&& (Iy3 - Iy2) != 0 && (Iy1 - Iy3) != 0) {
			AB = (dist2 * dist2) - (dist3 * dist3) - (Ix2 * Ix2) - (Iy2 * Iy2)
					+ (Ix3 * Ix3) + (Iy3 * Iy3);
			BC = (dist3 * dist3) - (dist1 * dist1) - (Ix3 * Ix3) - (Iy3 * Iy3)
					+ (Ix1 * Ix1) + (Iy1 * Iy1);
			Userx = ((AB * (Iy1 - Iy3)) - (BC * (Iy3 - Iy2)))
					/ (2 * ((Ix3 - Ix2) * (Iy1 - Iy3) - (Ix1 - Ix3)
							* (Iy3 - Iy2)));
			Usery = (AB - (2 * Userx * (Ix3 - Ix2))) / (2 * (Iy3 - Iy2));
			Usery2 = (BC - 2 * Userx * (Ix1 - Ix3)) / (2 * (Iy1 - Iy3));
			Usery = (Usery + Usery2) / 2;
		}

		else {
			AB = (dist3 * dist3) - (dist2 * dist2) - (Ix3 * Ix3) - (Iy3 * Iy3)
					+ (Ix2 * Ix2) + (Iy2 * Iy2);
			BC = (dist2 * dist2) - (dist1 * dist1) - (Ix2 * Ix2) - (Iy2 * Iy2)
					+ (Ix1 * Ix1) + (Iy1 * Iy1);
			Userx = ((AB * (Iy1 - Iy2)) - (BC * (Iy2 - Iy3)))
					/ (2 * ((Ix2 - Ix3) * (Iy1 - Iy2) - (Ix1 - Ix2)
							* (Iy2 - Iy3)));
			Usery = (AB - (2 * Userx * (Ix2 - Ix3))) / (2 * (Iy2 - Iy3));
			Usery2 = (BC - 2 * Userx * (Ix1 - Ix2)) / (2 * (Iy1 - Iy2));
			Usery = (Usery + Usery2) / 2;
		}
	}

	public Double getx() {

		return Userx;
	}

	public Double gety() {

		return Usery;
	}

}
