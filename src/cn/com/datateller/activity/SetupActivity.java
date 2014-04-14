package cn.com.datateller.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import cn.com.datateller.R;
import cn.com.datateller.util.SpinnerAdapter;
import cn.com.datateller.util.UserHelper;

public class SetupActivity extends Activity {

	private static final String TAG="SetupActivity";
	private Button updateInforButton;
	private Button logoutButton;
	private Button locationButton;
	private Spinner navigate_spinner;
	private List<String> list=new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		
		updateInforButton = (Button) findViewById(R.id.updateUserInfoActivityButton);
		logoutButton = (Button) findViewById(R.id.logoutButton);

		navigate_spinner=(Spinner)findViewById(R.id.navigate_spinner);
		list.add("设置");
		list.add("注册");
		list.add("知识");
		list.add("收藏");
		list.add("登陆");
		
		SpinnerAdapter adapter=new SpinnerAdapter(SetupActivity.this, list);
		navigate_spinner.setAdapter(adapter);
		navigate_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				String str=parent.getItemAtPosition(position).toString();
			    if(str.equals("设置")){
			    	
			    }
			    else if(str.equals("收藏")){
			    	intent.setClass(SetupActivity.this, CollectKnowledgeActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("知识")){
			    	intent.setClass(SetupActivity.this, MainActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("登陆")){
			    	intent.setClass(SetupActivity.this, LoginActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("注册")){
			    	intent.setClass(SetupActivity.this, RegisterActivity.class);
			    	startActivity(intent);
			    }
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}});
		updateInforButton.setOnClickListener(new ButtonClick());
		logoutButton.setOnClickListener(new ButtonClick());
		locationButton.setOnClickListener(new ButtonClick());
		
	}

	private class ButtonClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.updateUserInfoActivityButton:
				// TODO更新个人信息
				intent.setClass(SetupActivity.this,
						UpdateUserInforActivity.class);
				SetupActivity.this.startActivity(intent);
				break;
			case R.id.logoutButton:
				UserHelper.deleteUserInfo(SetupActivity.this);
				SetupActivity.this.finish();
				intent.setClass(SetupActivity.this, WelcomeActivity.class);
				SetupActivity.this.startActivity(intent);
				break;

			default:
				break;
			}
		}

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup, menu);
		return true;
	}

}
