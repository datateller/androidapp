package cn.com.datateller.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.com.datateller.R;
import cn.com.datateller.model.Baby;
import cn.com.datateller.model.User;
import cn.com.datateller.service.UserService;
import cn.com.datateller.util.DateUtil;
import cn.com.datateller.util.SexEnum;
import cn.com.datateller.util.UserHelper;

public class RegisterWithBabyInforActivity extends Activity {

	private static final String TAG="RegisterWithChildInfoActivity"; 
	private EditText etchildname;
	private RadioGroup rgchildsex;
	private EditText etchildheight;
	private EditText etchildweight;
	private EditText etfamilyAddress;
	private EditText etschoolAddress;
	private Button finshButton;
	private SexEnum sex=SexEnum.BOY;
	private EditText etbirthyear;
    private EditText etbirthmonth;
    private EditText etbirthday;
    private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_with_baby_infor);
		
		final String username=getIntent().getStringExtra("username");
		final String password=getIntent().getStringExtra("password");

		etchildname=(EditText)findViewById(R.id.etchildrname);
		rgchildsex=(RadioGroup)findViewById(R.id.childsexRadioGroup);
		etchildheight=(EditText)findViewById(R.id.etchildheight);
		etchildweight=(EditText)findViewById(R.id.etchildweight);
		finshButton=(Button)findViewById(R.id.finishButton);
	    etbirthday=(EditText)findViewById(R.id.etbirthday);
	    etbirthmonth=(EditText)findViewById(R.id.etbirthmonth);
	    etbirthyear=(EditText)findViewById(R.id.etbirthyear);
	    etfamilyAddress=(EditText)findViewById(R.id.etfamilyaddress);
	    etschoolAddress=(EditText)findViewById(R.id.etschooladdress);
	    
        rgchildsex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkId) {
				// TODO Auto-generated method stub
				if(checkId==R.id.boyButton){
					sex.setIndex(1);
				}else{
					sex.setIndex(2);
				}
			}
		});
        
        finshButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//初始化childname、childheight、childweight
				String childname=etchildname.getText().toString();
				Log.d(TAG, childname);
				if(childname==""){
					UserHelper.showDialog(RegisterWithBabyInforActivity.this, "Baby 的姓名不能为空");
					return;
				}
			    float childheight=0.0f;
				if(etchildheight.getText().toString()!="") 
					childheight=Float.valueOf(etchildheight.getText().toString());
				Log.d(TAG, childheight+"");
				float childweight=0.0f;
				if(etchildweight.getText().toString()!="")
					childweight=Float.valueOf(etchildweight.getText().toString());
				Log.d(TAG, etchildweight+"");
				Date birthday=getChildBirthday();
//				TODO 无效的出生日期需要重新判断
				if(birthday.getTime()>System.currentTimeMillis()){
					UserHelper.showDialog(RegisterWithBabyInforActivity.this, "Baby出生日期输入错误");
					return;
				}
				String familyAddress=null;
				if(etfamilyAddress.getText().toString()==""){
					UserHelper.showDialog(RegisterWithBabyInforActivity.this, "为了能向您提供更好的个性化服务，请输入家庭地址信息，谢谢！");
				}
				familyAddress=etfamilyAddress.getText().toString();
				
				String schoolAddress=null;
				if(etschoolAddress.getText().toString()!=""){
					schoolAddress=etschoolAddress.getText().toString();
				}
				Log.d(TAG, birthday.toString());
				final User user=new User();
				final Baby child=new Baby();
				user.setUserName(username);
				user.setPassword(password);
				child.setChildname(childname);
				child.setWeight(childweight);
				child.setHeight(childheight);
				child.setBirthday(birthday);
				child.setSex(sex);
				child.setFamilyAddress(familyAddress);
				child.setSchoolAddress(schoolAddress);
				System.out.println(sex.getIndex());
				final ProgressDialog myDialog=ProgressDialog.show(RegisterWithBabyInforActivity.this, "正在注册", "连接中，请稍后...",true,true);
				final Intent intent=new Intent();
				handler=new Handler(){
					@Override
					public void handleMessage(Message msg){
						Bundle bundle=msg.getData();
						String result=bundle.getString("result");
						System.out.println("In the handler");
						myDialog.dismiss();
						if(result.equals("True")){
							UserHelper.deleteUserInfo(RegisterWithBabyInforActivity.this);
							UserHelper.saveUserInfo(RegisterWithBabyInforActivity.this, username, password);
							intent.putExtra("birthday",etbirthyear.getText().toString()+etbirthmonth.getText().toString()+etbirthday.getText().toString());
							intent.setClass(RegisterWithBabyInforActivity.this, MainActivity.class);
							startActivity(intent);
						}else if(result.equals("DuplicateName")){
							UserHelper.showDialog(RegisterWithBabyInforActivity.this, "用户名已存在，请重新注册");
						}
					}
				};
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						UserService service=new UserService();
						String result=service.Register(user, child);
						Message msg=new Message();
						Bundle bundle=new Bundle();
						bundle.putString("result", result);
						msg.setData(bundle);
						//使用handler发送message
						handler.sendMessage(msg);
					}
				}).start();	
			}
		});
		
	}

	private Date getChildBirthday() {
		// TODO Auto-generated method stub
		String year=etbirthyear.getText().toString();
		String month=etbirthmonth.getText().toString();
		String day=etbirthday.getText().toString();
		if(year==""|month==""||month==""){
			UserHelper.showDialog(RegisterWithBabyInforActivity.this, "Baby出生日期不能为空");
			return null;
		}
		String result=DateUtil.CheckBabyBirthday(year, month, day);
		if(!(result.equals("true"))){
			UserHelper.showDialog(RegisterWithBabyInforActivity.this, result);
		}
		if(Integer.valueOf(month)<10) month="0"+month;
		if(Integer.valueOf(day)<10) day="0"+day;
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Date date=new Date();
		try {
			date=format.parse(year+"-"+month+"-"+day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_with_baby_infor, menu);
		return true;
	}

}
