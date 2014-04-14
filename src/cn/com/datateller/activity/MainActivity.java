package cn.com.datateller.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import cn.com.datateller.R;
import cn.com.datateller.model.BasicInformation;
import cn.com.datateller.model.User;
import cn.com.datateller.service.KnowledgeService;
import cn.com.datateller.util.DateUtil;
import cn.com.datateller.util.ListViewHelper;
import cn.com.datateller.util.MyListViewAdapter;
import cn.com.datateller.util.SpinnerAdapter;
import cn.com.datateller.util.UserHelper;

public class MainActivity extends Activity {
	
	private static final String TAG="MainActivity";
	private static final String NAME="BasicKnowledge";
	private static final String APPNAME="yangwabao";
	private TextView titleText;
	private Spinner navigate_spinner;
	private List<String> list=new ArrayList<String>();
	private ListView listview;
	private Handler handler;
	private String birthdayString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		初始化控件
		final Long lday=getIntent().getLongExtra("day", 0);
		birthdayString=getIntent().getStringExtra("birthday");
		final int day=Integer.valueOf(String.valueOf(lday));
		Log.d(TAG, String.valueOf(day));
		
//		设定匿名用户名和密码
		if(UserHelper.readUserName(MainActivity.this)==""){
			UserHelper.saveUserInfo(MainActivity.this,"anonymous", "wjbb123");
		}
        
		titleText=(TextView)findViewById(R.id.titleText);
		navigate_spinner=(Spinner)findViewById(R.id.navigate_spinner);
//		建立数据源
		list.add("知识");
		list.add("收藏");
		list.add("设置");
		list.add("登陆");
		list.add("注册");
//		初始化adapter
		SpinnerAdapter adapter=new SpinnerAdapter(MainActivity.this, list);
//		绑定adapter到spinner
		navigate_spinner.setAdapter(adapter);
//		从服务器获取知识并展示在页面上
		showBasicKnowledge(day);
		
		navigate_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				String str=parent.getItemAtPosition(position).toString();
			    if(str.equals("知识")){
			    	
			    }
			    else if(str.equals("收藏")){
			    	intent.setClass(MainActivity.this, CollectKnowledgeActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("设置")){
			    	intent.setClass(MainActivity.this, SetupActivity.class);
			    	startActivity(intent);
			    	
			    }else if(str.equals("登陆")){
			    	intent.setClass(MainActivity.this, LoginActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("注册")){
			    	intent.setClass(MainActivity.this, RegisterActivity.class);
			    	startActivity(intent);
			    }
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}});
	}
    /*  展示知识
     *    检查手机是否插入sd卡
     *    若sd卡没有插入，则直接从服务器中获取数据，并将数据在listview中展示
     *    若sd卡存在，则进一步检查以当日结尾的xml文件是否存在，若存在则直接从文件中读取，若不存在该文件则从服务器中获取数据，并将
     *    获取的知识写入以当日结尾的xml文件中。
     * */
	private void showBasicKnowledge(int day) {
		// TODO Auto-generated method stub
		boolean sdcardIsmount = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if(sdcardIsmount==false){
			getBasicKnowledgeFromServerAndWriteToFile(0,null,null);
			return;
		}
		
		String currentDay = DateUtil.getCurrentDay();
		File fileDir=new File(Environment.getExternalStorageDirectory()+"/"+APPNAME+"/"+currentDay);
//		如果该文件夹不存在，则创建该文件夹，后续的操作都将在文件夹中进行
		if(!fileDir.exists()){
			fileDir.mkdirs();
			//TODO 删除系统中存在的失效文件
		}
		   
		String filename = NAME+birthdayString+".xml";
		String path = Environment.getExternalStorageDirectory() +"/"+APPNAME+"/"+currentDay;
		boolean isExists = new File(path, filename).exists();
		if (isExists) {
//          直接从文件中读取basicknowledge，并展示返回
			KnowledgeService service=new KnowledgeService();
			List<BasicInformation> basicKnowledgeList=service.readBasicKnowledgeFromFile(path,filename);
			show(basicKnowledgeList);
			return;
		} else {
			getBasicKnowledgeFromServerAndWriteToFile(day,path,filename);
		}
	}

	//该方法从服务器中获取知识，若传入的参数filename以及path为空，则表示手机没有sd卡，因此直接
	//从服务器中获取知识，并展示即可，否则在展示之前，先将知识写入以当日结尾的xml文件中
	//该方法使用了异步通信的策略
	private void getBasicKnowledgeFromServerAndWriteToFile(
			final int day, final String path, final String filename) {
		// TODO Auto-generated method stub
		final ProgressDialog myDialog = ProgressDialog.show(
				MainActivity.this, "请稍等", "正在获取数据...", true, true);
		handler = new Handler() {
			@Override
			// TODO 网络连接过长也是登陆失败的一种
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				ArrayList basicKnowledge = bundle
						.getParcelableArrayList("basicKnowledge");
				myDialog.dismiss();
				KnowledgeService service=new KnowledgeService();
				if(basicKnowledge==null) {
					UserHelper.showDialog(MainActivity.this, "获取知识失败");
					return;
				}
				if(path!=null||filename!=null) service.writeBasicKnowledgeToFile(path, filename,basicKnowledge);
				show(basicKnowledge);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				KnowledgeService service = new KnowledgeService();
				User user = new User();
				user.setUserName(UserHelper
						.readUserName(MainActivity.this));
				user.setPassword(UserHelper
						.readPassword(MainActivity.this));
				ArrayList<BasicInformation> basicKnowledgelist = service
						.getBasicKnowledgesFromServer(user,day);
				if(basicKnowledgelist!=null){
					basicKnowledgelist = service.getIconByUrl(basicKnowledgelist);
				}
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("basicKnowledge",
						(ArrayList<? extends Parcelable>) basicKnowledgelist);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}).start();
	}
	
//	将从服务器或者文件中获取的知识列表以listview的方式进行展示
	private void show(final List<BasicInformation> basicKnowledgeList) {
		// TODO Auto-generated method stub
		ListViewHelper helper=new ListViewHelper();
		ListViewHelper listHelper=new ListViewHelper();
		List<Map<String, Object>> data=helper.getData(basicKnowledgeList);
		listview=(ListView)findViewById(R.id.basic_knowledge_content);
//		初始化适配器
		MyListViewAdapter adapter=new MyListViewAdapter(MainActivity.this,data);
//		绑定适配器到listview上
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				ListView lview = (ListView) parent;
				int index = (Integer) lview.getItemAtPosition(position);
				int knowledgeId = Integer.valueOf(basicKnowledgeList.get(index)
						.getId());
				String knowledgePicUrl = basicKnowledgeList.get(index).getPic();
				String catetoryTag=null;
				String link=basicKnowledgeList.get(index).getLink();
				if(link.indexOf("knowledge")>0)  
					catetoryTag="knowledge";
				else if(link.indexOf("consumption")>0)
					catetoryTag="consumption";
				else if(link.indexOf("shop")>0)
					catetoryTag="shop";
				Intent intent = new Intent();
				intent.putExtra("knowledgeId", knowledgeId);
				intent.putExtra("title", basicKnowledgeList.get(index).getTitle());
				intent.putExtra("Abstract", basicKnowledgeList.get(index).getAbstract());
				intent.putExtra("knowledgeIconUrl", basicKnowledgeList.get(index).getIcon());
				intent.putExtra("link", basicKnowledgeList.get(index).getLink());
				intent.putExtra("address", basicKnowledgeList.get(index).getAddress());
				intent.putExtra("pic", basicKnowledgeList.get(index).getPic());
				intent.putExtra("catetoryTag", catetoryTag);
				intent.setClass(MainActivity.this, ShowKnowledgeActivity.class);
                MainActivity.this.startActivity(intent);				
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
