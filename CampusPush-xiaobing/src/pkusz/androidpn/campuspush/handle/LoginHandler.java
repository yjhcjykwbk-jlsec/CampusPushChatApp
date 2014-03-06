package pkusz.androidpn.campuspush.handle;

import pkusz.androidpn.campuspush.LoginActivity;
import pkusz.androidpn.campuspush.MainActivity;
import pkusz.androidpn.campuspush.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class LoginHandler extends Handler{
	private final static String TAG = "LoginHandler";
	LoginActivity activity;
	String userName,passWd;
	LoginThread loginThread;

	public LoginHandler(LoginActivity activity,String userName,String passWd){
		this.activity = activity;
		this.userName = userName;
		this.passWd = passWd;
		
		
		loginThread = new LoginThread(this.activity,userName,passWd);
		loginThread.start();
	}
	
	@Override
	public void handleMessage(Message message){
	
		Log.d(TAG, "handle the message");
		switch(message.what){
		case R.id.check_success:
			Intent intent = new Intent(activity,MainActivity.class);
			activity.startActivity(intent);
			activity.finish();
			break;
		case R.id.password_failure:
			activity.setDisplay();
			Toast.makeText(activity, "the password is not right!", Toast.LENGTH_LONG).show();
			break;
		case R.id.check_not_exist:
			activity.setDisplay();
			Toast.makeText(activity, "the username is not existed!", Toast.LENGTH_LONG).show();
		}
	}
}
