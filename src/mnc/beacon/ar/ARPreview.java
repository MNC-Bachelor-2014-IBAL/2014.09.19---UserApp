package mnc.beacon.ar;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ARPreview extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder mSurfaceHolder;
	private Camera mCamera;

	// set Holder that managed Surface view
	public ARPreview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mSurfaceHolder = getHolder();
		// register callback methods
		mSurfaceHolder.addCallback(this);
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	// execute when creating surface view
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// open camera
		mCamera = Camera.open();

		try {
			// keep camera screen
			mSurfaceHolder.setKeepScreenOn(true);
			mCamera.setPreviewDisplay(mSurfaceHolder);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			mCamera.release();
			mCamera = null;
			e.printStackTrace();
		}
	}

	// execute when changing surface view size
	// execute when starting surface view
	// start camera preview 
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		mCamera.startPreview();
	}

	// execute when destroying surface view
	// stop camera and return resource
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}
}
