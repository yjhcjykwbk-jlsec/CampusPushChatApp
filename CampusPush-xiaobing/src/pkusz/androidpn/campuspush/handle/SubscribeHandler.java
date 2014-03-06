package pkusz.androidpn.campuspush.handle;

import pkusz.androidpn.campuspush.SubscribeActivity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SubscribeHandler extends Handler{
	SubscribeActivity activity;
	SubscribeSubmitThread thread;
	String userID;
	String subscribe;
	private ProgressDialog dialogProgress;
	
	public SubscribeHandler(SubscribeActivity activity,String userID,String subscribe){
		this.activity = activity;
		this.userID = userID;
		this.subscribe = subscribe;
		dialogProgress = new ProgressDialog(activity);
		thread = new SubscribeSubmitThread(this.activity,this.userID,this.subscribe);
		thread.start();
	}
	
	@Override
	public void handleMessage(Message msg) {
		if (msg.what==0) { //取消
			thread.interrupt();
			Toast.makeText(activity, "用户 "+userID+" 中止提交", Toast.LENGTH_SHORT).show();
		}else if (msg.what==1) { //成功
			dialogProgress.dismiss();
			Toast.makeText(activity, "用户 "+userID+" 提交成功", Toast.LENGTH_LONG).show();
		}else if (msg.what==2) { //失败
			dialogProgress.dismiss();
			Toast.makeText(activity, "用户 "+userID+" 提交失败", Toast.LENGTH_LONG).show();
		}
	}
	
}
