package pkusz.androidpn.campuspush.handle;

import pkusz.androidpn.campuspush.MainActivity;
import pkusz.androidpn.campuspush.R;
import pkusz.androidpn.campuspush.push.ServiceManager;
import android.os.Handler;
import android.util.Log;

public class MainActivityHandler extends Handler{
	private static final String TAG = "MainActivityHandler";
	MainActivity activity;
	ServiceManager serviceManager;

	public MainActivityHandler(MainActivity activity){
		this.activity = activity;
	}
	
	public void startNotificationService(){
		Log.d(TAG, "the service is starting");
		serviceManager = new ServiceManager(activity);
		serviceManager.setNotificationIcon(R.drawable.notification);
		serviceManager.startService();
	}
	
	public void stopNotificationService(){
		serviceManager.stopService();
	}
}
