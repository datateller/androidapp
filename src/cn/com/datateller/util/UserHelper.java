package cn.com.datateller.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class UserHelper {

	private static final String TAG="UserHelper";
	private static final String USERINFOR="userInformation";
	
	public static String readUserName(Context context){
		SharedPreferences sharedPreferences=context.getSharedPreferences(USERINFOR, Activity.MODE_PRIVATE);
		return sharedPreferences.getString("username", "");
	}
	
	public static String readPassword(Context context){
		SharedPreferences sharedPreferences=context.getSharedPreferences(USERINFOR, Activity.MODE_PRIVATE);
		return sharedPreferences.getString("password", "");
	}
	
	public static void showDialog(Context context,String message){
		Log.d(TAG, "In the function showDialog of the ActivityUtil");
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle("提示");
		builder.setMessage(message);
		builder.setPositiveButton("确认", new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	public static boolean deleteUserInfo(Context context){
		SharedPreferences sharedPreferences=context.getSharedPreferences(USERINFOR, Activity.MODE_PRIVATE);
		return sharedPreferences.edit().clear().commit();
	}
	
	public static boolean saveUserInfo(Context context,String username,String password){
		SharedPreferences sharedPreferences=context.getSharedPreferences(USERINFOR, Activity.MODE_PRIVATE);
		System.out.println(sharedPreferences);
		Editor editor=sharedPreferences.edit();
		editor.putString("username", username);
		editor.putString("password", password);
		return editor.commit();
	}
}
