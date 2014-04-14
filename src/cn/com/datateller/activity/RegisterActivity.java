package cn.com.datateller.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import cn.com.datateller.R;
import cn.com.datateller.model.User;
import cn.com.datateller.service.UserService;
import cn.com.datateller.util.SpinnerAdapter;
import cn.com.datateller.util.UserHelper;

public class RegisterActivity extends Activity {

	private static final String TAG="RegisterActivity";
	
	private EditText etuserName;
	private EditText etpassword;
	private EditText etconfirmpassword;
	private Button nextButton;
	private String username;
	private String password;
	private String confirmpassword;
	private Handler handler;
	private Spinner spinner;
	private List<String> list=new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		etuserName=(EditText)findViewById(R.id.etuserName);
		etpassword=(EditText)findViewById(R.id.etpassword);
		etconfirmpassword=(EditText)findViewById(R.id.etconfirmpassword);
		nextButton=(Button)findViewById(R.id.nextButton);
		spinner=(Spinner)findViewById(R.id.register_activity_navigate_spinner);
		list.add("注册");
		list.add("知识");
		list.add("收藏");
		list.add("设置");
		list.add("登陆");
		
		SpinnerAdapter adapter=new SpinnerAdapter(RegisterActivity.this, list);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				String str=parent.getItemAtPosition(position).toString();
			    if(str.equals("知识")){
			    	intent.setClass(RegisterActivity.this, MainActivity.class);
			    	startActivity(intent);
			    }
			    else if(str.equals("收藏")){
			    	intent.setClass(RegisterActivity.this, CollectKnowledgeActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("设置")){
			    	intent.setClass(RegisterActivity.this, SetupActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("登陆")){
			    	intent.setClass(RegisterActivity.this, LoginActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("注册")){
			    	
			    }
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}});
		
		
		nextButton.setOnClickListener(new ButtonClick());
		
		
	}

	private class ButtonClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			username=etuserName.getText().toString();
			password=etpassword.getText().toString();
			confirmpassword=etconfirmpassword.getText().toString();
			
			if(username.trim().length()==0){
				UserHelper.showDialog(RegisterActivity.this, "用户名不能为空");
				return;
			}
			
			if(password.trim().length()==0){
				UserHelper.showDialog(RegisterActivity.this, "密码不能为空");
				return;
			}
			
			if(confirmpassword.trim().length()==0){
				UserHelper.showDialog(RegisterActivity.this, "确认密码不能为空");
				return;
			}
			
			if(!password.equals(confirmpassword)){
				UserHelper.showDialog(RegisterActivity.this, "密码与确认密码不一致");
				return;
			}
		
			final Intent intent=new Intent();
			switch (v.getId()) {
			case R.id.nextButton:
				intent.setClass(RegisterActivity.this, RegisterWithBabyInforActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				RegisterActivity.this.startActivity(intent);
				break;

			default:
				break;
			}		
		}	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
