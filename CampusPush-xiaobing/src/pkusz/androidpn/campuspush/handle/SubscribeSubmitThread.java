package pkusz.androidpn.campuspush.handle;

import pkusz.androidpn.campuspush.SubscribeActivity;
import pkusz.androidpn.campuspush.data.ServerInfo;
import pkusz.androidpn.campuspush.util.GetPostUtil;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SubscribeSubmitThread extends Thread {

	SubscribeActivity activity;
	String userID;
	String subscriptions;

	public SubscribeSubmitThread(SubscribeActivity activity, String userID,
			String subscriptions) {
		this.activity = activity;
		this.userID = userID;
		this.subscriptions = subscriptions;
	}

	@Override
	public void run() {

		StringBuilder parameter = new StringBuilder();
		parameter.append("action=get");
		parameter.append("&subscriber=");
		parameter.append(userID);
		parameter.append("&subscriptions=");
		parameter.append(subscriptions);

		String resp = GetPostUtil.send("POST", ServerInfo.SERVER_IP
				+ "notification.do", parameter);
		Log.i("xiaobingo", "¶©ÔÄÏìÓ¦£º" + resp);
		int what;
		if (resp.contains("subscribe:success")) { // ¶©ÔÄ³É¹¦
			what = 1;
		} else { // ¶©ÔÄÊ§°Ü
			what = 2;
		}
		Message message = Message.obtain(activity.getHandler(), what);
		message.sendToTarget();
		activity.finish();
	}
}
