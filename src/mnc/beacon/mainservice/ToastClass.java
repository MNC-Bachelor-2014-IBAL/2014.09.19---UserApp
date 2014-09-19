package mnc.beacon.mainservice;

public class ToastClass {
	String str = null;
	public static ToastClass toastClass;
	
	public ToastClass() {
		
	}
	public static ToastClass instance() {
		if(toastClass == null ) {
			toastClass = new ToastClass();
			return toastClass;
		}
		return toastClass;
	}
	
	public void setString(String str) {
		this.str = str;
	}
	
	public String getString() {	
		return str;	
	}

}
