package pkusz.androidpn.campuspush;

import java.util.ArrayList;
import java.util.HashMap;

import pkusz.androidpn.campuspush.data.Constants;
import pkusz.androidpn.campuspush.data.UserInfo;
import pkusz.androidpn.campuspush.handle.MainActivityHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	public static final int MENU_ITEM0 = Menu.FIRST;
	public static final int MENU_ITEM1 = Menu.FIRST + 1;
	ListView listView;
	UserInfo userInfo;
	WakeLock wakelock;
	MainActivityHandler handler;
	private SharedPreferences sharedPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		sharedPrefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
				Context.MODE_PRIVATE);

		setDisplay();

		handler = new MainActivityHandler(this);

		userInfo = (UserInfo) getApplication();
		userInfo.initUserInfo();

		handler.startNotificationService();

	}

	@Override
	protected void onResume() {

		super.onResume();

		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
		listItem = userInfo.getMyNotifier();

		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,
				R.layout.list, new String[] { "ItemTitle", "ItemMessage",
						"ItemUri" }, new int[] { R.id.ItemTitle,
						R.id.ItemMessage, R.id.ItemUri });
		listView.setAdapter(listItemAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ITEM0, 0, "退出登陆");
		menu.add(0, MENU_ITEM1, 0, "清空列表");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ITEM0: {
			finish();
			break;
		}
		case MENU_ITEM1: {
			break;
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 按下back键,最小化
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goHome(MainActivity.this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 最小化，回到桌面
	 * 
	 */
	public static void goHome(Activity activity) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory("android.intent.category.HOME");
		activity.startActivity(intent);
	}

	@Override
	public void onStop() {
		Log.d(TAG, "this activity is stopped");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		handler.stopNotificationService();
		super.onDestroy();
	}

	/*
	 * set the display of the main activity
	 */
	public void setDisplay() {
		setContentView(R.layout.main);

		listView = (ListView) findViewById(R.id.myList);
	}

	/*
	 * set the listener when click the btn_center
	 */
	public void clickCenter(View view) {
		Intent itent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://push.pkusz.edu.cn"));
		startActivity(itent);
	}

	/*
	 * set the listener when click the btn_subscribe
	 */
	public void clickSubscribe(View view) {
		Intent subIntent = new Intent(this, SubscribeActivity.class);
		Bundle bd = new Bundle();
		bd.putString("userID",
				sharedPrefs.getString(Constants.XMPP_USERNAME, "未知用户"));
		subIntent.putExtras(bd);
		startActivity(subIntent);
	}

	/*
	 * set the listener when click the btn_setting
	 */
	public void clickSetting(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	/*
	 * set the listener when click the btn_myUpload
	 */
	public void clickUpload(View view){
		Intent upIntent = new Intent(this, UploadActivity.class);
		Bundle bd = new Bundle();
		bd.putString("userID", sharedPrefs.getString(Constants.XMPP_USERNAME, "未知用户"));
		upIntent.putExtras(bd);
		startActivity(upIntent);
	}
}