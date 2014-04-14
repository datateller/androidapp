package cn.com.datateller.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.com.datateller.util.DateUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ImageService {

	private static final String APPNAME="yangwabao";
	
	public Bitmap getImageFromServer(String urlString) {
		// TODO Auto-generated method stub
		InputStream in = getImageStream(urlString);
		if(in==null) return null;
		Bitmap bitmap = BitmapFactory.decodeStream(in);
		return bitmap;
	}

	public boolean SaveBitMap(Bitmap bitmap, String picFilename) {
		// TODO Auto-generated method stub
		String currentDay=DateUtil.getCurrentDay();
		String path = Environment.getExternalStorageDirectory()+"/"+APPNAME+"/"+currentDay;
		File file = new File(path, picFilename);
		if (file.exists())
			return true;
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public InputStream getImageStream(String path) {

		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return conn.getInputStream();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
