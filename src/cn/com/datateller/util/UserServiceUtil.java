package cn.com.datateller.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Base64;
import cn.com.datateller.model.User;

public class UserServiceUtil {
	public static  List<NameValuePair> initUserInforNameValuePair(User user){
		user.setUserName(Base64.encodeToString(user.getUserName().getBytes(), Base64.DEFAULT));
		user.setPassword(Base64.encodeToString(user.getPassword().getBytes(), Base64.DEFAULT));
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		NameValuePair userNamePair = new BasicNameValuePair("username",
				user.getUserName());
		NameValuePair passwordPair = new BasicNameValuePair("password",
				user.getPassword());
		list.add(userNamePair);
		list.add(passwordPair);
		return list;
	}

}
