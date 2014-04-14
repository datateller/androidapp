package cn.com.datateller.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import cn.com.datateller.R;
import cn.com.datateller.service.UserService;
import cn.com.datateller.util.SpinnerAdapter;
import cn.com.datateller.util.UserHelper;

public class LoginActivity extends Activity {

	private static final String TAG = "LoginActivity";
	private EditText etuserName;
	private EditText etpassword;
	private Button registerButton;
	private Button loginButton;
	private String username;
	private String password;
	private Handler handler;
	private Spinner navigate_spinner;
	private List<String> list=new ArrayList<String>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		username = UserHelper.readUserName(this);
		password = UserHelper.readPassword(this);

		etuserName = (EditText) findViewById(R.id.etuserName);
		etpassword = (EditText) findViewById(R.id.etpassword);
		registerButton = (Button) findViewById(R.id.registerButton);
		loginButton = (Button) findViewById(R.id.loginButton);

		navigate_spinner=(Spinner)findViewById(R.id.navigate_spinner);
//		建立数据源
		list.add("登陆");
		list.add("知识");
		list.add("收藏");
		list.add("设置");
		list.add("注册");
//		初始化adapter
		SpinnerAdapter adapter=new SpinnerAdapter(LoginActivity.this, list);
//		绑定adapter到spinner
		navigate_spinner.setAdapter(adapter);
//		从服务器获取知识并展示在页面上
		navigate_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent();
				String str=parent.getItemAtPosition(position).toString();
			    if(str.equals("登录")){
			    	
			    }
			    else if(str.equals("收藏")){
			    	intent.setClass(LoginActivity.this, CollectKnowledgeActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("设置")){
			    	intent.setClass(LoginActivity.this, SetupActivity.class);
			    	startActivity(intent);
			    	
			    }else if(str.equals("知识")){
			    	intent.setClass(LoginActivity.this, MainActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("注册")){
			    	intent.setClass(LoginActivity.this, RegisterActivity.class);
			    	startActivity(intent);
			    }
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}});
		
		registerButton.setOnClickListener(new ButtonClick());
		loginButton.setOnClickListener(new ButtonClick());

	}

	private class ButtonClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.loginButton: {

				// 获取用户名和密码
				username = etuserName.getText().toString();
				password = etpassword.getText().toString();
				Log.d(TAG, "The user name is " + username);
				Log.d(TAG, "The password is " + password);
				// 用户名为空则直接返回
				if (username.trim().length() == 0) {
					Log.d(TAG, "打开对话框");
					UserHelper.showDialog(LoginActivity.this, "用户名不能为空");
					return;
				}
				// 密码为空则直接返回
				if (password.trim().length() == 0) {
					UserHelper.showDialog(LoginActivity.this, "密码不能为空");
					return;
				}
				// 在页面中显示圆形进度条
				final ProgressDialog myDialog = ProgressDialog.show(
						LoginActivity.this, "正在登陆", "连接中，请稍后...", true, true);

				// 异步提交用户名和密码
				handler = new Handler() {
					@Override
					// TODO 网络连接过长也是登陆失败的一种
					public void handleMessage(Message msg) {
						Bundle bundle = msg.getData();
						// 获取服务器返回的结果
						boolean result = bundle.getBoolean("result");
						Log.d(TAG, String.valueOf(result));
						myDialog.dismiss();
						// 登陆成功，则调用MainActivity
						if (result == true) {
							Intent intent = new Intent();
							// 将用户名和密码缓存入userInformation文件中，再次进入的时候不用重复输入用户名和密码
							UserHelper.deleteUserInfo(LoginActivity.this);
							System.out.println(username);
							System.out.println(password);
							UserHelper.saveUserInfo(LoginActivity.this,
									username, password);
							intent.setClass(LoginActivity.this,
									MainActivity.class);
							startActivity(intent);
						} else {
							// 登陆失败，这显示登陆失败的对话框
							UserHelper.showDialog(LoginActivity.this, "登陆失败");
						}
					}
				};
				// 新开启一个线程，向服务器提交登陆信息
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						UserService userService = new UserService();
						boolean result = userService.userLogin(username,
								password);
						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putBoolean("result", result);
						msg.setData(bundle);
						// 使用handler发送message
						handler.sendMessage(msg);
					}
				}).start();
			}
				break;
			case R.id.registerButton: {
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
}
