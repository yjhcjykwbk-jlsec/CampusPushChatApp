package pkusz.androidpn.campuspush.handle;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pkusz.androidpn.campuspush.LoginActivity;
import pkusz.androidpn.campuspush.data.Constants;
import pkusz.androidpn.campuspush.data.ServerInfo;
import pkusz.androidpn.campuspush.util.GetPostUtil;

import pkusz.androidpn.campuspush.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Message;
import android.util.Log;

public class LoginThread extends Thread {
	private final static String TAG = "LoginThread";
	SharedPreferences sharedPrefs;
	LoginActivity activity;
	private String userName, passWd;
	String encryptedPW;

	public LoginThread(LoginActivity activity, String userName, String passWd) {
		this.activity = activity;
		this.userName = userName;
		this.passWd = passWd;
	}

	@Override
	public void run() {
		// Abstracted with MD5

		try {
			encryptedPW = toMD5((passWd).getBytes("GBK"));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Log.d(TAG, "MD5加密后的密码：" + encryptedPW);

		String response = testUserPassWd();

		Log.d(TAG, "response:" + response);

		Message msg = null;
		// 验证用户名密码成功
		if (response.contains("check:success")) {
			sharedPrefs = activity.getSharedPreferences(
	                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = sharedPrefs.edit();
			editor.putString(Constants.XMPP_USERNAME, userName);
			editor.putString(Constants.XMPP_PASSWORD, encryptedPW);
			editor.commit();
			msg = Message.obtain(activity.getHandler(), R.id.check_success);
		}
		// 密码错误
		else if (response.contains("check:password failure")) {
			msg = Message.obtain(activity.getHandler(),R.id.password_failure);
		}
		// 用户不存在
		else if (response.contains("check:not exist")) {
			msg = Message.obtain(activity.getHandler(),R.id.check_not_exist);
		}
		
		msg.sendToTarget();
	}

	/*
	 * Abstract the password with MD5.
	 */
	protected String toMD5(byte[] pwd) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(pwd);
			StringBuffer sb = new StringBuffer();
			for (byte b : md.digest()) {
				sb.append(String.format("%02x", b & 0xff));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
			return null;
		}
	}

	/*
	 * test the username and the password
	 */
	private String testUserPassWd() {

		// 用POST方式发送
		/*--拼接POST字符串--*/
		StringBuilder parameter = new StringBuilder();
		parameter.append("action=checkUser"); 
		parameter.append("&androidName=");
		parameter.append(userName);
		parameter.append("&androidPwd=");
		parameter.append(encryptedPW);
		/*--End--*/
		String resp = GetPostUtil.send("POST",
				ServerInfo.SERVER_IP + "user.do", parameter);

		return resp;
	}

}
