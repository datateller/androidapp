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
		
		//初始化控件
		webview = (WebView) findViewById(R.id.webview);
		collect_button=(Button)findViewById(R.id.collect_button);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webview.getSettings().setSupportZoom(true);
		webview.setScrollBarStyle(0);
		webview.getSettings().setDefaultTextEncodingName("UTF-8");
		
		//获取MainActivity传递过来的参数
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
            		builder.setTitle("提示");
            		builder.setMessage("您还没有登录，请您登录后在收藏您喜欢的知识");
            		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							Intent intent=new Intent();
		                	intent.setClass(ShowKnowledgeActivity.this, LoginActivity.class);
		                    startActivity(intent);
						}
            		});
            		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
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
					UserHelper.showDialog(ShowKnowledgeActivity.this, "无法收藏，请插入SD卡");
				    return;
				}
				String path = Environment.getExternalStorageDirectory()+"/"+APPNAME+"/"+COLLECT;
				File fileDir=new File(path);
				if(!fileDir.exists()){
					fileDir.mkdirs();
				}
				String filename=BASICKNOWLEDGE+".xml";
				File file=new File(path,filename);
    //			TODO 需要修改icon的值，其值应该为icon的当前的目录
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
					UserHelper.showDialog(ShowKnowledgeActivity.this, "收藏失败，您已经收藏该条知识");
			        return;	
				}
//				在collect目录下面保存string
				String collectKnowledgeFilename=String.valueOf(knowledgeId)+".html";
				service.writeKnowledgeToFile(path,collectKnowledgeFilename,collectKnowledge);
				UserHelper.showDialog(ShowKnowledgeActivity.this, "恭喜您，您成功的收藏您喜欢的知识");
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
				ShowKnowledgeActivity.this, "请稍等", "正在获取数据...", true, true);
				handler = new Handler() {
					@Override
					// TODO 网络连接过长也是登陆失败的一种
					public void handleMessage(Message msg) {
						Bundle bundle = msg.getData();
						String knowledge=bundle.getString("knowledge");
						myDialog.dismiss();
						collectKnowledge=knowledge;
						if(filename==null) {
							UserHelper.showDialog(ShowKnowledgeActivity.this, "获取知识失败");
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
						//TODO 此处的Abstract以及icon字段的值，暂时设为test
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
