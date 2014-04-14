package cn.com.datateller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import cn.com.datateller.R;
import cn.com.datateller.service.KnowledgeService;

public class CollectDetailKnowledgeActivity extends Activity {

	private WebView webview;
	private static final String TAG = "CollectDetailKnowledgeActivity";
	private static final String APPNAME="yangwabao";
	private static final String COLLECT="collect";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect_detail_knowledge);
		
		int knowledgeId=getIntent().getIntExtra("knowledgeId", 0);
		
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webview.getSettings().setSupportZoom(true);
		webview.setScrollBarStyle(0);
		webview.getSettings().setDefaultTextEncodingName("UTF-8");
		
		String path = Environment.getExternalStorageDirectory()+"/"+APPNAME+"/"+COLLECT;
		String filename=String.valueOf(knowledgeId)+".html";
		KnowledgeService service=new KnowledgeService();
		String knowledge=service.readKnowledgeFromFile(path,filename);
		webview.loadDataWithBaseURL(null, knowledge, "text/html", "UTF-8", null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collect_detail_knowledge, menu);
		return true;
	}

}
