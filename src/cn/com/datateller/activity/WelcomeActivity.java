package cn.com.datateller.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.datateller.R;
import cn.com.datateller.util.UserHelper;

public class WelcomeActivity extends Activity {

	private Button experienceButton;
	private EditText birthdayEdittext;
	private TextView welcomeTextview;
//	private Integer age;
	private String birthdayString;
	private static final String TAG="WelcomeActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		experienceButton = (Button) findViewById(R.id.experience_button);
		birthdayEdittext = (EditText) findViewById(R.id.welcome_activity_babyage_edittext);
		welcomeTextview = (TextView) findViewById(R.id.welcome_activity_textview);

		experienceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String birthdayString = birthdayEdittext.getText().toString();
				Long year=0L;
				Long day=0L;
				if (birthdayString.trim().length() == 0) {
					UserHelper.showDialog(WelcomeActivity.this, "请输入您家宝贝的出生日期");
					return;
				}
				try {
//					age = Integer.valueOf(ageStr);
					String pat= "\\d{4}\\d{2}\\d{2}";
					Pattern pattern = Pattern.compile(pat);
					Matcher macher = pattern.matcher(birthdayString) ;
					if(!macher.matches()){
						UserHelper.showDialog(WelcomeActivity.this, "请输入正确的日期格式");
						return ;
					}
					SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
					Date birthDate=sdf.parse(birthdayString);
					Date today=new Date();
					long time=today.getTime()-birthDate.getTime();
					if(time<0) {
						UserHelper.showDialog(WelcomeActivity.this, "请输入正确的日期");
					    return;	
					}
					day=time/1000/3600/24;
					long month=day/30;
					year=month/12;
					
				} catch (NumberFormatException e) {
					// TODO: handle exception
					UserHelper.showDialog(WelcomeActivity.this, "请输入您家宝贝的出生日期");
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d(TAG,String.valueOf(day));
                Intent intent=new Intent();
                intent.putExtra("day", day);
                intent.putExtra("birthday", birthdayString);
                intent.setClass(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
}
