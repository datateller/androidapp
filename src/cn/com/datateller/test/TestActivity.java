package cn.com.datateller.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.text.format.DateFormat;
import android.util.Log;
import cn.com.datateller.model.Baby;
import cn.com.datateller.model.BasicInformation;
import cn.com.datateller.model.User;
import cn.com.datateller.service.KnowledgeService;
import cn.com.datateller.service.UserService;
import cn.com.datateller.util.DateUtil;
import cn.com.datateller.util.SexEnum;

public class TestActivity extends AndroidTestCase{

	private static final String TAG="TestActivity";
	private static final String NAME="Knowledge";
	private static final String APPNAME="yangwabao";
	private static final String COLLECT="collect";
	private static final String BASICKNOWLEDGE="basicKnowlege";
	
/*	public void testUserLogin() {

		User user = new User();
		user.setUserName("shentest");
		user.setPassword("shentest");
		long startTime = System.currentTimeMillis();
		boolean result = new UserService().userLogin(user.getUserName(),user.getPassword());
//		Assert.assertEquals(result, true);
		System.out.println("The login result is " + result);
		System.out.println(System.currentTimeMillis() - startTime);
	}*/
	
	public void testGetBasicKnowledgesFromServer(){
		User user=new User();
		user.setUserName("anonymous");
		user.setPassword("wjbb123");
		int age=2;
		KnowledgeService service=new KnowledgeService();
		Log.d(TAG, user.getUserName());
		List<BasicInformation> basicKnowledge=service.getBasicKnowledgesFromServer(user, age);
		Log.d(TAG, basicKnowledge.toString());
		System.out.println(basicKnowledge);
	}
	
	/*public void testWriteBasicKnowledgeToFile(){
		User user=new User();
		user.setUserName("a");
		user.setPassword("a");
		int age=2;
		KnowledgeService service=new KnowledgeService();
		Log.d(TAG, user.getUserName());
		List<BasicKnowledge> basicKnowledge=service.getBasicKnowledgesFromServer(user, age);
		service.writeBasicKnowledgeToFile(Environment.getExternalStorageDirectory() + "/", "20140311.xml", basicKnowledge);
//		Log.d(TAG, service.readBasicKnowledgeFromFile(Environment.getExternalStorageDirectory() + "/", "20140311.xml");
	}*/
	
/*	public void testGetKnowledgeById(){
		User user=new User();
		user.setUserName("a");
		user.setPassword("a");
		KnowledgeService service=new KnowledgeService();
		String knowledge=service.getInformationById(44121, user, "knowledge");
		String currentDay = DateUtil.getCurrentDay();
		String filename = NAME + String.valueOf(21739) + ".html";
		String path = Environment.getExternalStorageDirectory()+"/"+APPNAME+"/"+currentDay;
		service.writeKnowledgeToFile(path, filename, knowledge);
		String result=service.readKnowledgeFromFile(path, filename);
		Log.d(TAG, knowledge.toString());
	}*/
	
/*	public void testaddBasicKnowledgeToFile(){
		String path = Environment.getExternalStorageDirectory()+"/"+APPNAME+"/"+COLLECT;
		String filename=BASICKNOWLEDGE+".xml";
		BasicKnowledge basicKnowledge=new BasicKnowledge();
		basicKnowledge.setKnowledgeId(22036);
		KnowledgeService service=new KnowledgeService();
		boolean result=service.addBasicKnowledgeToFile(path, filename, basicKnowledge);
		System.out.println(result);
	}*/
	
/*	public void testgetBasicKnowledgesFromServer(){
		User user=new User();
		user.setUserName("anonymous");
		user.setPassword("datateller");
		KnowledgeService service=new KnowledgeService();
		List<BasicKnowledge> list=service.getBasicKnowledgesFromServer(user, 0);
		System.out.println(list);
	}*/
	
	/*public void testUpdateInfo() throws ParseException{
		UserService service=new UserService();
		User user=new User();
		user.setUserName("a");
		user.setPassword("a");
		Baby baby=new Baby();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Date date=format.parse("2013-10-29");
		baby.setBirthday(date);
		baby.setChildname("Test");
		baby.setFamilyAddress("");
		baby.setSchoolAddress("");
		baby.setSex(SexEnum.BOY);
		baby.setWeight(50);
		baby.setHeight(25.0f);
		String result=service.UpdateInfo(user, baby);
		if(result==null){
			System.out.println(result);	
		}
//		Log.d(TAG, result);
		System.out.println(result);	
	}*/
	
	
/*	public void testregister(){
		UserService service=new UserService();
		User user=new User();
		user.setUserName("a");
		user.setPassword("a");
		String result=service.register(user);
		System.out.println(result);
	}*/
	
	
}
