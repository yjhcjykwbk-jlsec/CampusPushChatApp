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
		if (msg.what==0) { //ȡ��
			thread.interrupt();
			Toast.makeText(activity, "�û� "+userID+" ��ֹ�ύ", Toast.LENGTH_SHORT).show();
		}else if (msg.what==1) { //�ɹ�
			dialogProgress.dismiss();
			Toast.makeText(activity, "�û� "+userID+" �ύ�ɹ�", Toast.LENGTH_LONG).show();
		}else if (msg.what==2) { //ʧ��
			dialogProgress.dismiss();
			Toast.makeText(activity, "�û� "+userID+" �ύʧ��", Toast.LENGTH_LONG).show();
		}
	}
	
}
