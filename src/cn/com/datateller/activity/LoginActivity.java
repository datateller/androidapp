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
//		��������Դ
		list.add("��½");
		list.add("֪ʶ");
		list.add("�ղ�");
		list.add("����");
		list.add("ע��");
//		��ʼ��adapter
		SpinnerAdapter adapter=new SpinnerAdapter(LoginActivity.this, list);
//		��adapter��spinner
		navigate_spinner.setAdapter(adapter);
//		�ӷ�������ȡ֪ʶ��չʾ��ҳ����
		navigate_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent();
				String str=parent.getItemAtPosition(position).toString();
			    if(str.equals("��¼")){
			    	
			    }
			    else if(str.equals("�ղ�")){
			    	intent.setClass(LoginActivity.this, CollectKnowledgeActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("����")){
			    	intent.setClass(LoginActivity.this, SetupActivity.class);
			    	startActivity(intent);
			    	
			    }else if(str.equals("֪ʶ")){
			    	intent.setClass(LoginActivity.this, MainActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("ע��")){
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

				// ��ȡ�û���������
				username = etuserName.getText().toString();
				password = etpassword.getText().toString();
				Log.d(TAG, "The user name is " + username);
				Log.d(TAG, "The password is " + password);
				// �û���Ϊ����ֱ�ӷ���
				if (username.trim().length() == 0) {
					Log.d(TAG, "�򿪶Ի���");
					UserHelper.showDialog(LoginActivity.this, "�û�������Ϊ��");
					return;
				}
				// ����Ϊ����ֱ�ӷ���
				if (password.trim().length() == 0) {
					UserHelper.showDialog(LoginActivity.this, "���벻��Ϊ��");
					return;
				}
				// ��ҳ������ʾԲ�ν�����
				final ProgressDialog myDialog = ProgressDialog.show(
						LoginActivity.this, "���ڵ�½", "�����У����Ժ�...", true, true);

				// �첽�ύ�û���������
				handler = new Handler() {
					@Override
					// TODO �������ӹ���Ҳ�ǵ�½ʧ�ܵ�һ��
					public void handleMessage(Message msg) {
						Bundle bundle = msg.getData();
						// ��ȡ���������صĽ��
						boolean result = bundle.getBoolean("result");
						Log.d(TAG, String.valueOf(result));
						myDialog.dismiss();
						// ��½�ɹ��������MainActivity
						if (result == true) {
							Intent intent = new Intent();
							// ���û��������뻺����userInformation�ļ��У��ٴν����ʱ�����ظ������û���������
							UserHelper.deleteUserInfo(LoginActivity.this);
							System.out.println(username);
							System.out.println(password);
							UserHelper.saveUserInfo(LoginActivity.this,
									username, password);
							intent.setClass(LoginActivity.this,
									MainActivity.class);
							startActivity(intent);
						} else {
							// ��½ʧ�ܣ�����ʾ��½ʧ�ܵĶԻ���
							UserHelper.showDialog(LoginActivity.this, "��½ʧ��");
						}
					}
				};
				// �¿���һ���̣߳���������ύ��½��Ϣ
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
						// ʹ��handler����message
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
