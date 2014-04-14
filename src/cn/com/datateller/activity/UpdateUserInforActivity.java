package cn.com.datateller.activity;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import cn.com.datateller.R;
import cn.com.datateller.R.layout;
import cn.com.datateller.R.menu;
import cn.com.datateller.model.Baby;
import cn.com.datateller.model.User;
import cn.com.datateller.service.UserService;
import cn.com.datateller.util.DateUtil;
import cn.com.datateller.util.SexEnum;
import cn.com.datateller.util.UserHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class UpdateUserInforActivity extends Activity {

	private static final String TAG = "UpdateUserInforActivity";
	private RadioGroup rgchildsex;
	private EditText etchildname;
	private EditText etchildheight;
	private EditText etchildweight;
	private EditText etbirthyear;
    private EditText etbirthmonth;
    private EditText etbirthday;
	private EditText etfamilyAddress;
	private EditText etschoolAddress;
    private Handler handler;
    private Button updateBabyInforButton;;
    private SexEnum sex=SexEnum.BOY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_user_infor);
//		TODO 只有登陆的用户才能修改自己的个人信息
		rgchildsex=(RadioGroup)findViewById(R.id.childsexRadioGroup);
		etchildheight=(EditText)findViewById(R.id.etchildheight);
		etchildweight=(EditText)findViewById(R.id.etchildweight);
		updateBabyInforButton=(Button)findViewById(R.id.updateBabyInforButton);
	    etbirthday=(EditText)findViewById(R.id.etbirthday);
	    etbirthmonth=(EditText)findViewById(R.id.etbirthmonth);
	    etbirthyear=(EditText)findViewById(R.id.etbirthyear);
	    etchildname=(EditText)findViewById(R.id.etchildrname);
		
	    final String username=UserHelper.readUserName(this);
	    final String password=UserHelper.readPassword(this);
	    
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
		
        updateBabyInforButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String childname=etchildname.getText().toString();
				Log.d(TAG, childname);
				if(childname==""){
					UserHelper.showDialog(UpdateUserInforActivity.this, "Baby 的姓名不能为空");
				}
				int childheight=0;
				if(etchildheight.getText().toString()!="") 
					childheight=Integer.valueOf(etchildheight.getText().toString());
				Log.d(TAG, childheight+"");
				float childweight=0.0f;
				if(etchildweight.getText().toString()!="")
					childweight=Float.valueOf(etchildweight.getText().toString());
				Log.d(TAG, etchildweight+"");
				Date birthday=getChildBirthday();
//				TODO 无效的出生日期需要重新判断
				if(birthday.getTime()>System.currentTimeMillis()){
					UserHelper.showDialog(UpdateUserInforActivity.this, "Baby出生日期输入错误");
				}
				String familyAddress=null;
				if(etfamilyAddress.getText().toString()!=""){
					familyAddress=etfamilyAddress.getText().toString();
				}
				String schoolAddress=null;
				if(etschoolAddress.getText().toString()!=""){
					schoolAddress=etschoolAddress.getText().toString();
				}
				Log.d(TAG, birthday.toString());
				final User user=new User();
				final Baby child=new Baby();
				user.setUserName(username);
				user.setPassword(password);
				child.setWeight(childweight);
				child.setHeight(childheight);
				child.setBirthday(birthday);
				child.setSex(sex);
				child.setFamilyAddress(familyAddress);
				child.setSchoolAddress(schoolAddress);
				System.out.println(sex.getIndex());
				final ProgressDialog myDialog=ProgressDialog.show(UpdateUserInforActivity.this, "正在更新", "连接中，请稍后...",true,true);
				handler=new Handler(){
					@Override
					public void handleMessage(Message msg){
						Bundle bundle=msg.getData();
						String result=bundle.getString("result");
						System.out.println("In the handler");
						myDialog.dismiss();
						if(result.equals("True")){
							UserHelper.showDialog(UpdateUserInforActivity.this, "Baby信息更新成功");
						}else if(result.equals("False")){
							UserHelper.showDialog(UpdateUserInforActivity.this, "Baby信息更新失败");
						}
					}
				};
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String result=new UserService().UpdateInfo(user, child);
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

	protected Date getChildBirthday() {
		// TODO Auto-generated method stub
		String year=etbirthyear.getText().toString();
		String month=etbirthmonth.getText().toString();
		String day=etbirthday.getText().toString();
		if(year==""|month==""||month==""){
			UserHelper.showDialog(UpdateUserInforActivity.this, "Baby出生日期不能为空");
		}
		String result=DateUtil.CheckBabyBirthday(year, month, day);
		if(!(result.equals("true"))){
			UserHelper.showDialog(UpdateUserInforActivity.this, result);
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
		getMenuInflater().inflate(R.menu.update_user_infor, menu);
		return true;
	}

}
