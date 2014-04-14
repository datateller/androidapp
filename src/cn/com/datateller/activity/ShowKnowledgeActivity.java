package cn.com.datateller.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import cn.com.datateller.R;
import cn.com.datateller.model.BasicInformation;
import cn.com.datateller.model.User;
import cn.com.datateller.service.KnowledgeService;
import cn.com.datateller.util.DateUtil;
import cn.com.datateller.util.UserHelper;


public class ShowKnowledgeActivity extends Activity {

	private WebView webview;
	private Button collect_button;
	private Handler handler;
	private static final String TAG = "ShowKnowledgeActivity";
	private static final String NAME="Knowledge";
	private static final String APPNAME="yangwabao";
	private static final String BASICKNOWLEDGE="basicKnowlege";
	private static final String COLLECT="collect";
	private String collectKnowledge;
	private String categoryTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_knowledge);
		
		//��ʼ���ؼ�
		webview = (WebView) findViewById(R.id.webview);
		collect_button=(Button)findViewById(R.id.collect_button);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webview.getSettings().setSupportZoom(true);
		webview.setScrollBarStyle(0);
		webview.getSettings().setDefaultTextEncodingName("UTF-8");
		
		//��ȡMainActivity���ݹ����Ĳ���
		final int knowledgeId=getIntent().getIntExtra("knowledgeId",0);
		final String title=getIntent().getStringExtra("title");
		final String Abstract=getIntent().getStringExtra("Abstract");
		final String knowledgeIconUrl=getIntent().getStringExtra("knowledgeIconUrl");
		final String link=getIntent().getStringExtra("link");
		final String address=getIntent().getStringExtra("address");
		final String pic=getIntent().getStringExtra("pic");
		categoryTag=getIntent().getStringExtra("catetoryTag");
		
		
		ShowKnowledge(knowledgeId);
		
		collect_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                String username=UserHelper.readUserName(ShowKnowledgeActivity.this);
                String userPassword=UserHelper.readPassword(ShowKnowledgeActivity.this);
                if(username.equals("anonymous")){
                	AlertDialog.Builder builder=new AlertDialog.Builder(ShowKnowledgeActivity.this);
            		builder.setTitle("��ʾ");
            		builder.setMessage("����û�е�¼��������¼�����ղ���ϲ����֪ʶ");
            		builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							Intent intent=new Intent();
		                	intent.setClass(ShowKnowledgeActivity.this, LoginActivity.class);
		                    startActivity(intent);
						}
            		});
            		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
						}
					});
            		builder.show();	
            		return;
                }
				boolean sdcardIsmount = Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED);
				if(!sdcardIsmount){
					UserHelper.showDialog(ShowKnowledgeActivity.this, "�޷��ղأ������SD��");
				    return;
				}
				String path = Environment.getExternalStorageDirectory()+"/"+APPNAME+"/"+COLLECT;
				File fileDir=new File(path);
				if(!fileDir.exists()){
					fileDir.mkdirs();
				}
				String filename=BASICKNOWLEDGE+".xml";
				File file=new File(path,filename);
    //			TODO ��Ҫ�޸�icon��ֵ����ֵӦ��Ϊicon�ĵ�ǰ��Ŀ¼
				KnowledgeService service=new KnowledgeService();
				BasicInformation basicKnowledge=new BasicInformation();
				basicKnowledge.setAbstract(Abstract);
				basicKnowledge.setTitle(title);
				basicKnowledge.setId(knowledgeId);
				basicKnowledge.setIcon(knowledgeIconUrl);
				basicKnowledge.setPic(pic);
				basicKnowledge.setLink(link);
				basicKnowledge.setAddress(address);
				if(service.saveBasicKnowlegeToFile(path,filename,basicKnowledge)==false){
					UserHelper.showDialog(ShowKnowledgeActivity.this, "�ղ�ʧ�ܣ����Ѿ��ղظ���֪ʶ");
			        return;	
				}
//				��collectĿ¼���汣��string
				String collectKnowledgeFilename=String.valueOf(knowledgeId)+".html";
				service.writeKnowledgeToFile(path,collectKnowledgeFilename,collectKnowledge);
				UserHelper.showDialog(ShowKnowledgeActivity.this, "��ϲ�������ɹ����ղ���ϲ����֪ʶ");
			}
		});
	}

	private void ShowKnowledge(int knowledgeId) {
		// TODO Auto-generated method stub
		boolean sdcardIsmount = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if(sdcardIsmount==false){
			getKnowledgeFromServerAndWriteToFile(knowledgeId,null,null);
			return;
		}
		String currentDay = DateUtil.getCurrentDay();
		String filename = NAME + String.valueOf(knowledgeId) + ".html";
		String path = Environment.getExternalStorageDirectory()+"/"+APPNAME+"/"+currentDay;
		boolean isExists = new File(path, filename).exists();
		if (isExists){
			KnowledgeService service=new KnowledgeService();
			String knowledge;
			knowledge=service.readKnowledgeFromFile(path,filename);
			collectKnowledge=knowledge;
			show(knowledge);
		}else{
			getKnowledgeFromServerAndWriteToFile(knowledgeId,path,filename);
		}
	}


	private void getKnowledgeFromServerAndWriteToFile(final int knowledgeId,
			final String path, final String filename) {
		// TODO Auto-generated method stub
		final ProgressDialog myDialog = ProgressDialog.show(
				ShowKnowledgeActivity.this, "���Ե�", "���ڻ�ȡ����...", true, true);
				handler = new Handler() {
					@Override
					// TODO �������ӹ���Ҳ�ǵ�½ʧ�ܵ�һ��
					public void handleMessage(Message msg) {
						Bundle bundle = msg.getData();
						String knowledge=bundle.getString("knowledge");
						myDialog.dismiss();
						collectKnowledge=knowledge;
						if(filename==null) {
							UserHelper.showDialog(ShowKnowledgeActivity.this, "��ȡ֪ʶʧ��");
							return;
						}
						KnowledgeService service=new KnowledgeService();
						if(path!=null||filename!=null) service.writeKnowledgeToFile(path, filename,knowledge);
						show(knowledge);
					}
				};
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						KnowledgeService service = new KnowledgeService();
						User user = new User();
						user.setUserName(UserHelper
								.readUserName(ShowKnowledgeActivity.this));
						user.setPassword(UserHelper
								.readPassword(ShowKnowledgeActivity.this));
						String knowledge=service.getInformationById(knowledgeId, user,categoryTag);
						
						/*Knowledge knowledge=service.getKnowledgeById(knowledgeId, user);
						//TODO �˴���Abstract�Լ�icon�ֶε�ֵ����ʱ��Ϊtest
						knowledge.setAbstract("test");
						knowledge.setIcon("test");*/
						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("knowledge", knowledge);
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
				}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_knowledge, menu);
		return true;
	}

	private void show(String knowledge) {

		webview.loadDataWithBaseURL(null, knowledge, "text/html", "UTF-8", null);
	}
 
}
