/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.demoapp;

import java.util.ArrayList;
import java.util.HashMap;
import org.androidpn.client.Constants;
import org.androidpn.client.NotificationDetailsActivity;
import org.androidpn.client.ServiceManager;
import org.androidpn.util.ActivityUtil;
import org.androidpn.util.IsNetworkConn;
import org.androidpn.util.RTMPConnectionUtil;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener; 

/**
 * This is an androidpn client demo application.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class DemoAppActivity extends Activity {

	public static final int MENU_ITEM0 = Menu.FIRST;
	public static final int MENU_ITEM1 = Menu.FIRST+1;
	private SharedPreferences originSharedPrefs;
	private TextView welcomeUser;
	private TextView info;
	private TextView itemUri;
	private Button btn_center;
	private Button btn_subscribe;
	private Button btn_settings;
	private Button btn_myVideo;
	private Button btn_myUpload;
	ListView listView;
    UserInfo userInfo;
    WakeLock wakelock;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("xiaobingo", "onResume...");
		//生成动态数组，加入数据
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String,String>>();
        listItem = userInfo.getMyNotifier();  
        Log.i("xiaobingo", "listItem的大小是："+listItem.size()); 
        //生成适配器的Item和动态数组对应的元素 
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.list,
        		new String[]{"ItemTitle","ItemMessage","ItemUri"},
        		new int[]{R.id.ItemTitle,R.id.ItemMessage,R.id.ItemUri}
        		);
		listView.setAdapter(listItemAdapter);
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("xiaobingo", "onCreate()...");

        super.onCreate(savedInstanceState); 
        setContentView(R.layout.main);
        //添加到activitylist里，方便最后统一退出
        ActivityUtil.getInstance().addActivity(this);
        
        welcomeUser = (TextView)findViewById(R.id.user);		
		itemUri = (TextView)findViewById(R.id.ItemUri);
        listView = (ListView)findViewById(R.id.myList); 
        info = (TextView)findViewById(R.id.info);

        originSharedPrefs = this.getSharedPreferences(
                Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        welcomeUser.setText("Welcome，"+originSharedPrefs.getString(Constants.XMPP_USERNAME, "未知用户"));
        
		userInfo=(UserInfo)getApplication();		
		//初始化通知列表，在程序启动时候加载该列表。保证不为空
		userInfo.initUserInfo();		
        
        IsNetworkConn isConn = new IsNetworkConn(DemoAppActivity.this);
		        if (!isConn.isConnected) { //如果没联网
					info.setText("未联网，请先联网~");
				}else {
					//预先连接red5直播
					/*
		      		new Thread() {
		      			public void run () {
		      				RTMPConnectionUtil.ConnectRed5(DemoAppActivity.this);
		      			}
		      		}.start();
		      		*/
					//连androidpn server获取用户已订阅的栏目,并保存	
					StringBuilder parameter = new StringBuilder();
					parameter.append("action=getSubscription"); 
					parameter.append("&userName=");
					parameter.append(originSharedPrefs.getString(Constants.XMPP_USERNAME, "未知用户"));
					String responseSubscription = GetPostUtil.send("POST", "http://219.223.195.133:8080/user.do", parameter);
					Editor editor = originSharedPrefs.edit();
					editor.putString(Constants.USER_SUBSCRIPTION, responseSubscription);
					editor.commit(); //保存订阅
				}

		
		
        //item点击事件
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//点击进入详细通知页面
				ListView listView=(ListView)arg0;
				String getItemString = listView.getItemAtPosition(arg2).toString();
				//获取点击项的标题和信息
				String getItemTitle = getItemString.substring(getItemString.indexOf("ItemTitle")+10, getItemString.indexOf("ItemUri")-2);
				String getItemUri = getItemString.substring(getItemString.indexOf("ItemUri")+8, getItemString.indexOf("ItemMessage")-2); 
				String getItemMessage = getItemString.substring(getItemString.indexOf("ItemMessage")+12, getItemString.length()-1); 
				Log.i("xiaobingo", "标题是："+getItemTitle);
				Log.i("xiaobingo", "信息是："+getItemMessage);
				Log.i("xiaobingo", "链接是："+getItemUri);
				//activity跳转
				Intent it = new Intent(DemoAppActivity.this, NotificationDetailsActivity.class);
				it.putExtra("ItemTitle", getItemTitle);
				it.putExtra("ItemMessage", getItemMessage);
				it.putExtra("ItemUri", getItemUri);
				startActivity(it);
				
			}
        	
		}); 
		
		
		// Settings
        //Button btn_subscribe = (Button)findViewById(R.id.btn_subscribe);
       // Button btn_unsubscribe = (Button)findViewById(R.id.btn_unsubscribe);
        //final EditText subscription = (EditText)findViewById(R.id.subs);
        btn_settings = (Button) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ServiceManager.viewNotificationSettings(DemoAppActivity.this);
            }
        });
        
        //订阅
        btn_subscribe = (Button)findViewById(R.id.btn_subscribe);
        btn_subscribe.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent subIntent = new Intent(DemoAppActivity.this, SubscribeActivity.class);
				Bundle bd = new Bundle();
				bd.putString("userID", originSharedPrefs.getString(Constants.XMPP_USERNAME, "未知用户"));
				subIntent.putExtras(bd);
				DemoAppActivity.this.startActivity(subIntent);
			}
		});
        
        //资源中心
        btn_center = (Button)findViewById(R.id.btn_center);
        btn_center.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent itent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://push.pkusz.edu.cn"));
				DemoAppActivity.this.startActivity(itent);
			}
		});
        
        //我要上传
        btn_myUpload = (Button)findViewById(R.id.btn_myUpload);
        btn_myUpload.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent upIntent = new Intent(DemoAppActivity.this, UploadActivity.class);
				Bundle bd = new Bundle();
				bd.putString("userID", originSharedPrefs.getString(Constants.XMPP_USERNAME, "未知用户"));
				upIntent.putExtras(bd);
				DemoAppActivity.this.startActivity(upIntent);
			}
		});
        
        
        //我的视频
        btn_myVideo = (Button)findViewById(R.id.btn_myVideo);
        btn_myVideo.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent subIntent = new Intent(DemoAppActivity.this, MyVideoActivity.class);
				Bundle bd = new Bundle();
				bd.putString("userID", originSharedPrefs.getString(Constants.XMPP_USERNAME, "未知用户"));
				subIntent.putExtras(bd);
				DemoAppActivity.this.startActivity(subIntent);
			}
		});     
        
     
		
    }

	//创建menu键事件，退出登录
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ITEM0, 0, "退出登陆");
		menu.add(0, MENU_ITEM1, 0, "清空列表");
		return true;
	}

	//退出登陆事件和清空记录事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ITEM0:
		{	//注销用户名。密码
			originSharedPrefs = this.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = originSharedPrefs.edit();
			editor.remove(Constants.XMPP_USERNAME);
			editor.remove(Constants.XMPP_PASSWORD);
			editor.commit(); //保存订阅
			//调用全局serviceManager，来停止服务，客户端就彻底掉线
			Constants.serviceManager.stopService();
			//结束所有activity,完全退出
			ActivityUtil.getInstance().exit();
			break;
		}
		case MENU_ITEM1:
		{	//清空消息list，但保留默认数据
			userInfo = (UserInfo)getApplication();
			ArrayList<HashMap<String, String>> emptyList= new ArrayList<HashMap<String,String>>();
			userInfo.setMyNotifier(emptyList);
			userInfo.initUserInfo();
			ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String,String>>();
	        listItem = userInfo.getMyNotifier();  
	        Log.i("xiaobingo", "listItem的大小是："+listItem.size()); 
	        //生成适配器的Item和动态数组对应的元素 
	        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.list,
	        		new String[]{"ItemTitle","ItemMessage","ItemUri"},
	        		new int[]{R.id.ItemTitle,R.id.ItemMessage,R.id.ItemUri}
	        		);
			listView.setAdapter(listItemAdapter);
			break;
		}
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	  //按下back键,最小化
  	@Override
  	public boolean onKeyDown(int keyCode, KeyEvent event) {
  		
  		if (keyCode == KeyEvent.KEYCODE_BACK) {  					
  					goHome(DemoAppActivity.this);
  			return true;
  		} else {
  			return super.onKeyDown(keyCode, event);
  		}
  	}

//  	/** 
//  	  * 最小化，回到桌面 
//  	  *  
//  	  */ 
  	 public static void goHome(Activity activity) {  
  	     Intent intent = new Intent();
  	     intent.setAction("android.intent.action.MAIN");
  	     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  	     intent.addCategory("android.intent.category.HOME");
  	     activity.startActivity(intent);
  	 }  
  	 
  	 @Override
  	 public void onStop(){
  		 Log.d("demoappactivity", "this activity is stopped");
//  		 wakelock.release();
  		 super.onStop();
  	 }
  	 
}