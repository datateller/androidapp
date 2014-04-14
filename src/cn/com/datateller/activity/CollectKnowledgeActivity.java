package cn.com.datateller.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.datateller.R;
import cn.com.datateller.R.layout;
import cn.com.datateller.R.menu;
import cn.com.datateller.model.BasicInformation;
import cn.com.datateller.service.KnowledgeService;
import cn.com.datateller.util.ListViewHelper;
import cn.com.datateller.util.MyListViewAdapter;
import cn.com.datateller.util.SpinnerAdapter;
import cn.com.datateller.util.UserHelper;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CollectKnowledgeActivity extends Activity {

	private static final String TAG="CollectKnowledgeActivity";
	private static final String COLLECT="collect";
	private static final String NAME="BasicKnowledge";
	private static final String BASICKNOWLEDGE="basicKnowlege";
	private static final String APPNAME="yangwabao";
	private TextView collect_titleText;
	private Spinner collect_navigate_spinner;
	private List<String> list=new ArrayList<String>();
	private ListView listview;
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect_knowledge);
		
		collect_titleText=(TextView)findViewById(R.id.collect_titleText);
		collect_navigate_spinner=(Spinner)findViewById(R.id.collect_navigate_spinner);
		
		list.add("�ղ�");
		list.add("֪ʶ");
		list.add("����");
		list.add("��½");
		list.add("ע��");
		
		SpinnerAdapter adapter=new SpinnerAdapter(CollectKnowledgeActivity.this, list);
		collect_navigate_spinner.setAdapter(adapter);
		
		showCollectKnowledge();
		
		collect_navigate_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				String str=parent.getItemAtPosition(position).toString();
			    if(str.equals("�ղ�")){
			    	
			    }
			    else if(str.equals("֪ʶ")){
			    	intent.setClass(CollectKnowledgeActivity.this, MainActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("����")){
			    	intent.setClass(CollectKnowledgeActivity.this, SetupActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("��½")){
			    	intent.setClass(CollectKnowledgeActivity.this, LoginActivity.class);
			    	startActivity(intent);
			    }else if(str.equals("ע��")){
			    	intent.setClass(CollectKnowledgeActivity.this, RegisterActivity.class);
			    	startActivity(intent);
			    }
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}});
	}
		
	private void showCollectKnowledge() {
		// TODO Auto-generated method stub
		String path = Environment.getExternalStorageDirectory()+"/"+APPNAME+"/"+COLLECT;
		File fileDir=new File(path);
		if(!fileDir.exists()){
			UserHelper.showDialog(CollectKnowledgeActivity.this, "�Բ�������û���ղ�֪ʶ");
			return;
		}
		String filename=BASICKNOWLEDGE+".xml";
		File file=new File(path,filename);
		if(!file.exists()){
			UserHelper.showDialog(CollectKnowledgeActivity.this, "�Բ�������û���ղ�֪ʶ");
			return;
		}
		KnowledgeService service=new KnowledgeService();
		List<BasicInformation> basicKnowledgeList=service.readBasicKnowledgeFromFile(path,filename);
		show(basicKnowledgeList);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collect, menu);
		return true;
	}

	private void show(final List<BasicInformation> basicKnowledgeList) {
		// TODO Auto-generated method stub
		ListViewHelper helper=new ListViewHelper();
		ListViewHelper listHelper=new ListViewHelper();
		List<Map<String, Object>> data=helper.getData(basicKnowledgeList);
		listview=(ListView)findViewById(R.id.collect_basic_knowledge_content);
//		��ʼ��������
		MyListViewAdapter adapter=new MyListViewAdapter(CollectKnowledgeActivity.this,data);
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
				Intent intent = new Intent();
				intent.putExtra("knowledgeId", knowledgeId);
				intent.setClass(CollectKnowledgeActivity.this, CollectDetailKnowledgeActivity.class);
				CollectKnowledgeActivity.this.startActivity(intent);
			}
		});
	}
	
}
