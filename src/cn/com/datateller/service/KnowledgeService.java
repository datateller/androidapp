package cn.com.datateller.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import cn.com.datateller.model.BasicInformation;
import cn.com.datateller.model.User;
import cn.com.datateller.util.DateUtil;
import cn.com.datateller.util.FileUtils;
import cn.com.datateller.util.HttpConnection;
import cn.com.datateller.util.UserServiceUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class KnowledgeService {

	private static final String HOST = "http://168.63.219.187:80/";
	private static final String TAG = "KnowledgeService";
	private static final String APPNAME = "yangwabao";

	public KnowledgeService() {
		// TODO Auto-generated constructor stub
	}

	// 从指定的文件名中获取知识，并将知识存入list中，最后返回list
	public List<BasicInformation> readBasicKnowledgeFromFile(String path,
			String filename) {
		// TODO Auto-generated method stub
		ArrayList<BasicInformation> list = null;
		try {
			File file = new File(path, filename);
			FileInputStream is = new FileInputStream(file);
			list = new ArrayList<BasicInformation>();
			SAXReader reader = new SAXReader();
			Document doc = reader.read(is);
			Element eleRoot = doc.getRootElement();
			Iterator<Element> iter = eleRoot.elementIterator();
			while (iter.hasNext()) {
				Element eleIdTitle = iter.next();
				if ("BasicInformation".equals(eleIdTitle.getName())) {
					BasicInformation knowIdTitile = new BasicInformation();
					Iterator<Element> innerIter = eleIdTitle.elementIterator();
					while (innerIter.hasNext()) {
						Element ele = innerIter.next();
						if ("id".equals(ele.getName())) {
							knowIdTitile.setId(Integer.valueOf(ele
									.getText()));
						} else if ("title".equals(ele.getName())) {
							knowIdTitile.setTitle(ele.getText());
						} else if ("icon".equals(ele.getName())) {
							knowIdTitile.setIcon(ele.getText());
						} else if ("abstract".equals(ele.getName())) {
							knowIdTitile.setAbstract(ele.getText());
						} else if("link".equals(ele.getName())){
							knowIdTitile.setLink(ele.getText());
						} else if("address".equals(ele.getName())){
							knowIdTitile.setAddress(ele.getText());
						} else if("pic".equals(ele.getName())){
							knowIdTitile.setPic(ele.getText());
						}
					}
					list.add(knowIdTitile);
					knowIdTitile = null;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<BasicInformation> getBasicKnowledgesFromServer(User user,
			int age) {
		// TODO Auto-generated method stub
		String urlString = HOST + "mobile/getdata/";
		Log.d(TAG, user.getUserName());
		List<NameValuePair> userlist = UserServiceUtil
				.initUserInforNameValuePair(user);
	    NameValuePair agePair = new BasicNameValuePair("age",String.valueOf(age));
	    NameValuePair knumber=new BasicNameValuePair("knumber", String.valueOf(5));
	    NameValuePair snumber=new BasicNameValuePair("snumber", String.valueOf(2));
	    NameValuePair cnumber=new BasicNameValuePair("cnumber", String.valueOf(2));
	    userlist.add(agePair);
		userlist.add(knumber);
		userlist.add(snumber);
		userlist.add(cnumber);
		
		InputStream stream = HttpConnection.communicateWithServer(urlString,
				userlist);
		if (stream == null)
			return null;
		String input = HttpConnection.InputStreamToString(stream);
		Log.d(TAG, input);
		if (input.equals("AUTH_FAILED"))
			return null;
		ArrayList<BasicInformation> list;
		list = AnalysisBasicKnowledge(input);
		return list;
	}

	private ArrayList<BasicInformation> AnalysisBasicKnowledge(String input) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<BasicInformation>>() {
		}.getType();
		return gson.fromJson(input, type);
	}

	// 将list列表的数据写入文件
	public void writeBasicKnowledgeToFile(String path, String filename,
			List<BasicInformation> basicKnowledge) {
		// TODO Auto-generated method stub
		StringWriter xmlWriter = new StringWriter();
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("utf-8");
		Element elementRoot = doc.addElement("BasifInformations");
		int length = basicKnowledge.size();
		for (int i = 0; i < length; i++) {
			Element eleIdTitle = elementRoot.addElement("BasicInformation");
			Element eleId = eleIdTitle.addElement("id");
			eleId.addText(basicKnowledge.get(i).getId() + "");
			Element eleLink=eleIdTitle.addElement("link");
			eleLink.addText(basicKnowledge.get(i).getLink());
			Element eleAddress=eleIdTitle.addElement("address");
			eleAddress.addText(basicKnowledge.get(i).getAddress());
			Element eleTitle = eleIdTitle.addElement("title");
			eleTitle.addText(basicKnowledge.get(i).getTitle());
			Element elepic = eleIdTitle.addElement("pic");
			elepic.addText(basicKnowledge.get(i).getPic());
			Element eleicon = eleIdTitle.addElement("icon");
			eleicon.addText(basicKnowledge.get(i).getIcon());
			Element eleabstract = eleIdTitle.addElement("abstract");
			eleabstract.addText(basicKnowledge.get(i).getAbstract());
		}
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		outputFormat.setEncoding("utf-8");
		outputFormat.setIndent(false);
		outputFormat.setNewlines(true);
		outputFormat.setTrimText(true);
		XMLWriter output = new XMLWriter(xmlWriter, outputFormat);
		try {
			output.write(doc);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileUtils.SaveXml(path, filename, xmlWriter.toString());
	}

	public ArrayList<BasicInformation> getIconByUrl(
			ArrayList<BasicInformation> basicKnowledgelist) {
		// TODO Auto-generated method stub
		ImageService service = new ImageService();
		for (BasicInformation basicKnowledge : basicKnowledgelist) {
			Bitmap bitmap = service
					.getImageFromServer(basicKnowledge.getIcon());
			String currentDay = DateUtil.getCurrentDay();
			if (bitmap != null) {
				String iconFilename = String
						.valueOf(System.currentTimeMillis()) + ".gif";
				service.SaveBitMap(bitmap, iconFilename);
				basicKnowledge.setIcon(Environment
						.getExternalStorageDirectory()
						+ "/"
						+ APPNAME
						+ "/"
						+ currentDay + "/" + iconFilename);
			} else {
				basicKnowledge.setIcon(Environment
						.getExternalStorageDirectory()
						+ "/"
						+ APPNAME
						+ "/"
						+ currentDay + "/" + "default.gif");
			}
		}
		return basicKnowledgelist;
	}


	/*
	 * 1、从服务器获取html文件
	 */
	public String getInformationById(int id, User user,String tag) {
		System.out.println(tag);
		String urlString = HOST + tag+"/webview/" + String.valueOf(id);
		String input = HttpConnection.communicateServerWithGetMethod(urlString);
		return input;
	}


	public String readKnowledgeFromFile(String path, String filename) {
		// TODO Auto-generated method stub
		File file = new File(path, filename);
		InputStreamReader inputReader = null;
		BufferedReader bufferReader = null;
		StringBuffer strBuffer = new StringBuffer();
		try {
			InputStream inputStream = new FileInputStream(file);
			inputReader = new InputStreamReader(inputStream);
			bufferReader = new BufferedReader(inputReader);
			String line = null;
			while ((line = bufferReader.readLine()) != null) {
				strBuffer.append(line);
			}
		} catch (Exception e) {
            e.printStackTrace();
		}
		return strBuffer.toString();
	}

	public void writeKnowledgeToFile(String path, String filename,
			String knowledge) {
		// TODO Auto-generated method stub
		File file = new File(path, filename);
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			os.write(knowledge.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	/*
	 * 检查sd卡是否以插入手机 以当前日期为结尾的文件是否存在，若不存在，若不存在则创建新的文件，将知识写入文件，否则直接从文件中读取内容
	 * 若文件结尾的日期为当前文件则这直接从文件中读取，若文件不存在则直接从服务器端获取数据，写入以日期结尾的xml文件中。
	 */
	// TODO 删除不在使用的以日期结尾的xml文件

	public boolean saveBasicKnowlegeToFile(String path, String filename,
			BasicInformation basicKnowledge) {
		// TODO Auto-generated method stub
		File file=new File(path,filename);
		if(!file.exists()){
			createFile(path,filename,basicKnowledge);
			return true;
		}
		return addBasicKnowledgeToFile(path,filename,basicKnowledge);
	}

	public  boolean addBasicKnowledgeToFile(String path, String filename,
			BasicInformation basicKnowledge) {
		// TODO Auto-generated method stub
		List<BasicInformation> basicKnowledgeList=readBasicKnowledgeFromFile(path, filename);
		int bk1=basicKnowledge.getId();
		for (BasicInformation basicKnowledge2 : basicKnowledgeList) {
			int bk2=basicKnowledge2.getId();
			if(bk1==bk2) return false;
		}
		basicKnowledgeList.add(basicKnowledge);
		writeBasicKnowledgeToFile(path, filename, basicKnowledgeList);
		return true;
	}

	private void createFile(String path, String filename,
			BasicInformation basicKnowledge) {
		// TODO Auto-generated method stub
		StringWriter xmlWriter = new StringWriter();
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("utf-8");
		Element elementRoot = doc.addElement("BasicInformations");
		Element eleIdTitle = elementRoot.addElement("BasicInformation");
		Element eleId = eleIdTitle.addElement("id");
		eleId.addText(basicKnowledge.getId() + "");
		Element eleTitle = eleIdTitle.addElement("title");
		eleTitle.addText(basicKnowledge.getTitle());
		Element elepic = eleIdTitle.addElement("pic");
		elepic.addText(basicKnowledge.getPic());
		Element eleicon = eleIdTitle.addElement("icon");
		eleicon.addText(basicKnowledge.getIcon());
		Element eleabstract = eleIdTitle.addElement("abstract");
		eleabstract.addText(basicKnowledge.getAbstract());
		Element elelink = eleIdTitle.addElement("link");
		elelink.addText(basicKnowledge.getLink());
		Element eleaddress = eleIdTitle.addElement("address");
		eleaddress.addText(basicKnowledge.getAddress());
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		outputFormat.setEncoding("utf-8");
		outputFormat.setIndent(false);
		outputFormat.setNewlines(true);
		outputFormat.setTrimText(true);
		XMLWriter output = new XMLWriter(xmlWriter, outputFormat);
		try {
			output.write(doc);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileUtils.SaveXml(path, filename, xmlWriter.toString());
	}



}
