package pkusz.androidpn.campuspush;

import pkusz.androidpn.campuspush.handle.LoginHandler;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LoginActivity extends Activity {

	private final static String TAG = "LoginActivity";
	private LinearLayout loginLayout, centerLayout;
	private Button btn_login = null;
	private EditText userName = null;
	private EditText passWord = null;
	private ImageView logo;
	private InputMethodManager imm;
	private LoginHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		handler = null;
		setDisplay();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public LoginHandler getHandler(){
		return handler;
	}
	

	/*
	 * Set the display of the components in LoginActivity.
	 */
	public void setDisplay() {
		setContentView(R.layout.login);
		userName = (EditText) findViewById(R.id.username);
		passWord = (EditText) findViewById(R.id.password);
		btn_login = (Button) findViewById(R.id.btn_login);
		logo = (ImageView) findViewById(R.id.logo);
		loginLayout = (LinearLayout) findViewById(R.id.login_layout);
		centerLayout = (LinearLayout) findViewById(R.id.centerlayout);

		setCenterDisplay();

		setSoftKeyboard();

	}

	/*
	 * set the display of the center block
	 */
	@SuppressWarnings("deprecation")
	private void setCenterDisplay() {
		LinearLayout.LayoutParams centerlayout_param = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		centerlayout_param.width = getWindowManager().getDefaultDisplay()
				.getWidth() * 5 / 6;
		centerlayout_param.gravity = Gravity.CENTER_HORIZONTAL;

		centerLayout.setLayoutParams(centerlayout_param);
	}

	/*
	 * set the display of the soft keyboard
	 */
	private void setSoftKeyboard() {

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		loginLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				loginLayout.requestFocus();
				hideKeyboard();
				return false;
			}
		});

		loginLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					public void onGlobalLayout() {
						// TODO Auto-generated method stub
						Rect r = new Rect();

						loginLayout.getWindowVisibleDisplayFrame(r);
						int heightDiff = loginLayout.getRootView().getHeight()
								- r.height();
						if (heightDiff > 100) {
							logo.setVisibility(View.GONE);
						} else {
							logo.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	/*
	 * hide the soft keyboard
	 */
	private void hideKeyboard() {
		imm.hideSoftInputFromWindow(loginLayout.getWindowToken(), 0);
		logo.setVisibility(View.VISIBLE);
	}

	/*
	 * when click the login button
	 */
	@SuppressLint("NewApi")
	public void onSubmit(View v) {

		hideKeyboard();
		
		String theUserName = userName.getText().toString().trim();
		String thePassWord = passWord.getText().toString().trim();

		if (theUserName.equals("")) {
			userName.setHint("用户名不得为空");
		}
		if (thePassWord.equals("")) {
			passWord.setHint("密码不得为空");
		}
		if (!theUserName.isEmpty() && !thePassWord.isEmpty()) {
			setContentView(R.layout.transition);
			handler = new LoginHandler(this,theUserName,thePassWord);
		}
	}

}
