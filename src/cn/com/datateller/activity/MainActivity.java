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
//		��ʼ���ؼ�
		final Long lday=getIntent().getLongExtra("day", 0);
		birthdayString=getIntent().getStringExtra("birthday");
		final int day=Integer.valueOf(String.valueOf(lday));
		Log.d(TAG, String.valueOf(day));
		
//		�趨�����û���������
		if(UserHelper.readUserName(MainActivity.this)==""){
			UserHelper.saveUserInfo(MainActivity.this,"anonymous", "wjbb123");
		}
        
		titleText=(TextView)findViewById(R.id.titleText);
		navigate_spinner=(Spinner)findViewById(R.id.navigate_spinner);
//		��������Դ
		list.add("֪ʶ");
		list.add("�ղ�");
		list.add("����");
		list.add("��½");
		list.add("ע��");
//		��ʼ��adapter
		SpinnerAdapter adapter=new SpinnerAdapter(MainActivity.this, list);
//		��adapter��spinner
		navigate_spinner.setAdapter(adapter);
//		�ӷ�������ȡ֪ʶ��չʾ��ҳ����
		showBasicKnowledge(day);
		
		navigate_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				String str=parent.getItemAtPosition(position).toString();
			    if(str.equals("֪ʶ")){
			    	
			    }
			    else if(str.equals("�ղ�")){
			    	intent.setClass(MainActivity.this, CollectKnowledgeActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("����")){
			    	intent.setClass(MainActivity.this, SetupActivity.class);
			    	startActivity(intent);
			    	
			    }else if(str.equals("��½")){
			    	intent.setClass(MainActivity.this, LoginActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("ע��")){
			    	intent.setClass(MainActivity.this, RegisterActivity.class);
			    	startActivity(intent);
			    }
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}});
	}
    /*  չʾ֪ʶ
     *    ����ֻ��Ƿ����sd��
     *    ��sd��û�в��룬��ֱ�Ӵӷ������л�ȡ���ݣ�����������listview��չʾ
     *    ��sd�����ڣ����һ������Ե��ս�β��xml�ļ��Ƿ���ڣ���������ֱ�Ӵ��ļ��ж�ȡ���������ڸ��ļ���ӷ������л�ȡ���ݣ�����
     *    ��ȡ��֪ʶд���Ե��ս�β��xml�ļ��С�
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
//		������ļ��в����ڣ��򴴽����ļ��У������Ĳ����������ļ����н���
		if(!fileDir.exists()){
			fileDir.mkdirs();
			//TODO ɾ��ϵͳ�д��ڵ�ʧЧ�ļ�
		}
		   
		String filename = NAME+birthdayString+".xml";
		String path = Environment.getExternalStorageDirectory() +"/"+APPNAME+"/"+currentDay;
		boolean isExists = new File(path, filename).exists();
		if (isExists) {
//          ֱ�Ӵ��ļ��ж�ȡbasicknowledge����չʾ����
			KnowledgeService service=new KnowledgeService();
			List<BasicInformation> basicKnowledgeList=service.readBasicKnowledgeFromFile(path,filename);
			show(basicKnowledgeList);
			return;
		} else {
			getBasicKnowledgeFromServerAndWriteToFile(day,path,filename);
		}
	}

	//�÷����ӷ������л�ȡ֪ʶ��������Ĳ���filename�Լ�pathΪ�գ����ʾ�ֻ�û��sd�������ֱ��
	//�ӷ������л�ȡ֪ʶ����չʾ���ɣ�������չʾ֮ǰ���Ƚ�֪ʶд���Ե��ս�β��xml�ļ���
	//�÷���ʹ�����첽ͨ�ŵĲ���
	private void getBasicKnowledgeFromServerAndWriteToFile(
			final int day, final String path, final String filename) {
		// TODO Auto-generated method stub
		final ProgressDialog myDialog = ProgressDialog.show(
				MainActivity.this, "���Ե�", "���ڻ�ȡ����...", true, true);
		handler = new Handler() {
			@Override
			// TODO �������ӹ���Ҳ�ǵ�½ʧ�ܵ�һ��
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				ArrayList basicKnowledge = bundle
						.getParcelableArrayList("basicKnowledge");
				myDialog.dismiss();
				KnowledgeService service=new KnowledgeService();
				if(basicKnowledge==null) {
					UserHelper.showDialog(MainActivity.this, "��ȡ֪ʶʧ��");
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
	
//	���ӷ����������ļ��л�ȡ��֪ʶ�б���listview�ķ�ʽ����չʾ
	private void show(final List<BasicInformation> basicKnowledgeList) {
		// TODO Auto-generated method stub
		ListViewHelper helper=new ListViewHelper();
		ListViewHelper listHelper=new ListViewHelper();
		List<Map<String, Object>> data=helper.getData(basicKnowledgeList);
		listview=(ListView)findViewById(R.id.basic_knowledge_content);
//		��ʼ��������
		MyListViewAdapter adapter=new MyListViewAdapter(MainActivity.this,data);
//		����������listview��
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
