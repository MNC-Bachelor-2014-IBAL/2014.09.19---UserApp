package mnc.beacon.ar;

public class ARData {
	private String floor;
	private String name;
	private double aX;
	private double aY;
	private String location;
	
	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getx() {
		return aX;
	}

	public void setx(double x) {
		this.aX = x;
	}

	public double gety() {
		return aY;
	}

	public void sety(double y) {
		this.aY = y;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}