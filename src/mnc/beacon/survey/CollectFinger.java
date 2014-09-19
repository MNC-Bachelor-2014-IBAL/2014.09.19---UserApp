package mnc.beacon.survey;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class CollectFinger {
	public static File McurrentDirectory;
	public static String sdPath;
	public static FileOutputStream fos;
	
	public CollectFinger(String filename) throws IOException{
		//sdPath=  Environment.getExternalStorageDirectory().getAbsolutePath();
		//File dir = new File(sdPath,"testing"); 
		//dir.mkdir();
		
		McurrentDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		File file = new File(McurrentDirectory, filename+"txt");
		fos = new FileOutputStream(file);
	//	fos.write("dd".toString().getBytes());
		fos.close();
		// fos = new FileOutputStream(McurrentDirectory+"/"+ filename);
	}
	
	public static void writeintdata(int data) throws IOException{
		Integer a;
		a=data;
		fos.write(a.toString().getBytes());
		fos.close();
	}
	
	
}
