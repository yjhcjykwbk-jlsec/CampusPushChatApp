package pkusz.androidpn.campuspush;

import pkusz.androidpn.campuspush.data.Constants;
import pkusz.androidpn.campuspush.handle.SubscribeHandler;
import pkusz.androidpn.campuspush.util.GetPostUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.view.View.OnClickListener;

public class SubscribeActivity extends Activity {
	private SharedPreferences originSharedPrefs;
	private CheckBox cb_all;
	private CheckBox cb_news;
	private CheckBox cb_notification;
	private CheckBox cb_cieVideo;
	private CheckBox cb_hsbcVideo;
	private CheckBox cb_stlVideo;
	private CheckBox cb_renwenVideo;
	private CheckBox cb_schoolVideo;
	private CheckBox cb_leisureVideo;
	private Button btn_subsub;
	private SubscribeHandler handler;

	private String SubAll = "all";
	private String SubLeisureVideo = "video_leisurevideo";
	private String SubSchoolVideo = "video_schoolvideo";
	private String SubCieVideo = "video_cievideo";
	private String SubHsbcVideo = "video_hsbcvideo";
	private String SubStlVideo = "video_stlvideo";
	private String SubRenwenVideo = "video_renwenvideo";
	private String SubNews = "news_yaowen";
	private String SubNotification = "pkusz_notification";
	private String userID = "";
	private String subscriptions = "";

	private Button.OnClickListener clickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribe);
		// ��ӵ�activitylist��������ͳһ�˳�

		handler = null;
		Log.i("xiaobingo", "���붩�Ľ���");
		Bundle bundle = this.getIntent().getExtras();
		userID = bundle.getString("userID");

		cb_all = (CheckBox) findViewById(R.id.subAll);
		cb_news = (CheckBox) findViewById(R.id.subNews);
		cb_notification = (CheckBox) findViewById(R.id.subNotification);
		cb_cieVideo = (CheckBox) findViewById(R.id.subCieVideo);
		cb_hsbcVideo = (CheckBox) findViewById(R.id.subHsbcVideo);
		cb_stlVideo = (CheckBox) findViewById(R.id.subStlVideo);
		cb_renwenVideo = (CheckBox) findViewById(R.id.subRenwenVideo);
		cb_schoolVideo = (CheckBox) findViewById(R.id.subSchoolVideo);
		cb_leisureVideo = (CheckBox) findViewById(R.id.subLeisureVideo);
		btn_subsub = (Button) findViewById(R.id.btn_subsub);

		clickListener = new Button.OnClickListener() {

			@Override
			public void onClick(View view) {
				int buttonID = view.getId();
				CheckBox v = (CheckBox)view;
				switch (buttonID) {
				case R.id.subAll:
					selectAll(cb_all.isChecked());
					break;
				case R.id.subCieVideo:
					cb_all.setChecked(false);
					SubCieVideo = v.isChecked() ? null : "video_cievideo";
					break;
				case R.id.subHsbcVideo:
					cb_all.setChecked(false);
					SubHsbcVideo = v.isChecked() ? null : "video_hsbcvideo";
					break;
				case R.id.subStlVideo:
					cb_all.setChecked(false);
					SubStlVideo = v.isChecked() ? null : "video_stlvideo";
					break;
				case R.id.subRenwenVideo:
					cb_all.setChecked(false);
					SubRenwenVideo = v.isChecked() ? null : "video_renwenvideo";
					break;
				case R.id.subSchoolVideo:
					cb_all.setChecked(false);
					SubSchoolVideo = v.isChecked() ? null : "video_schoolvideo";
					break;
				case R.id.subLeisureVideo:
					cb_all.setChecked(false);
					SubLeisureVideo = v.isChecked() ? null : "video_leisurevideo";
					break;
				case R.id.subNews:
					cb_all.setChecked(false);
					SubNews = v.isChecked() ? null : "news_yaowen";
					break;
				case R.id.subNotification:
					cb_all.setChecked(false);
					SubNotification = v.isChecked() ? null : "pkusz_notification";
					break;
				}
			}
		};

		// ��������
		cb_all.setOnClickListener(clickListener);

		// ������ϢѧԺ��Ƶ
		cb_cieVideo.setOnClickListener(clickListener);

		// ���Ļ����ѧԺ��Ƶ
		cb_hsbcVideo.setOnClickListener(clickListener);

		// ���Ĺ��ʷ�ѧԺ��Ƶ
		cb_stlVideo.setOnClickListener(clickListener);

		// ��������ѧԺ��Ƶ
		cb_renwenVideo.setOnClickListener(clickListener);

		// ����ѧУ��Ƶ
		cb_schoolVideo.setOnClickListener(clickListener);

		// ����������Ƶ
		cb_leisureVideo.setOnClickListener(clickListener);

		// ��������Ҫ��
		cb_news.setOnClickListener(clickListener);

		// ����֪ͨ����
		cb_notification.setOnClickListener(clickListener);

		// �ύ���Ļ���ȡ������
		btn_subsub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// �ж�����
				// IsNetworkConn isConn = new
				// IsNetworkConn(SubscribeActivity.this);
				// if (!isConn.isConnected) {
				// Toast.makeText(SubscribeActivity.this, "δ��������������~",
				// Toast.LENGTH_LONG).show();
				// return;
				// }
				subscriptions = SubAll + ";" + SubNews + ";" + SubNotification
						+ ";" + SubSchoolVideo + ";" + SubCieVideo + ";"
						+ SubHsbcVideo + ";" + SubStlVideo + ";"
						+ SubRenwenVideo + ";" + SubLeisureVideo;
				Log.i("xiaobingo", "���ĵ��У�" + subscriptions);
				handler = new SubscribeHandler(SubscribeActivity.this, userID,
						subscriptions);
				// ���ĸı��ˣ��޸ı��ض��ļ�¼
				Editor editor = originSharedPrefs.edit();
				editor.putString(Constants.USER_SUBSCRIPTION, subscriptions);
				editor.commit(); // ���涩��
			}
		});
	}

	// ��onresume������ÿһ�λָ�����activityʱ����Ҫ�жϼ����û��Ѷ��ĵ�����
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		originSharedPrefs = this.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (originSharedPrefs.contains(Constants.USER_SUBSCRIPTION)) {
			String thisSubscriptions = originSharedPrefs.getString(
					Constants.USER_SUBSCRIPTION, null); // ��ȡ������û����ļ�¼
			if (thisSubscriptions.contains("all")) {
				cb_all.setChecked(true);
			}
			if (thisSubscriptions.contains("video_leisurevideo")) {
				cb_leisureVideo.setChecked(true);
			}
			if (thisSubscriptions.contains("video_schoolvideo")) {
				cb_schoolVideo.setChecked(true);
			}
			if (thisSubscriptions.contains("video_cievideo")) {
				cb_cieVideo.setChecked(true);
			}
			if (thisSubscriptions.contains("video_hsbcvideo")) {
				cb_hsbcVideo.setChecked(true);
			}
			if (thisSubscriptions.contains("video_stlvideo")) {
				cb_stlVideo.setChecked(true);
			}
			if (thisSubscriptions.contains("video_renwenvideo")) {
				cb_renwenVideo.setChecked(true);
			}
			if (thisSubscriptions.contains("news_yaowen")) {
				cb_news.setChecked(true);
			}
			if (thisSubscriptions.contains("pkusz_notification")) {
				cb_notification.setChecked(true);
			}
		}

	}

	public Handler getHandler() {
		return handler;
	}

	private void selectAll(boolean isChecked) {
		if (!isChecked) {
			SubAll = null;
			cb_news.setChecked(false);
			cb_notification.setChecked(false);
			cb_cieVideo.setChecked(false);
			cb_hsbcVideo.setChecked(false);
			cb_stlVideo.setChecked(false);
			cb_renwenVideo.setChecked(false);
			cb_schoolVideo.setChecked(false);
			cb_leisureVideo.setChecked(false);
		} else {
			SubAll = "all";
			SubLeisureVideo = "video_leisurevideo";
			SubSchoolVideo = "video_schoolvideo";
			SubCieVideo = "video_cievideo";
			SubHsbcVideo = "video_hsbcvideo";
			SubStlVideo = "video_stlvideo";
			SubRenwenVideo = "video_renwenvideo";
			SubNews = "news_yaowen";
			SubNotification = "pkusz_notification";
			cb_all.setChecked(true);
			cb_news.setChecked(true);
			cb_notification.setChecked(true);
			cb_cieVideo.setChecked(true);
			cb_hsbcVideo.setChecked(true);
			cb_stlVideo.setChecked(true);
			cb_renwenVideo.setChecked(true);
			cb_schoolVideo.setChecked(true);
			cb_leisureVideo.setChecked(true);
		}
	}
}
