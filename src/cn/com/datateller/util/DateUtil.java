package cn.com.datateller.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	//以字符串的形式返回当前日期
	public static String getCurrentDay() {
		// TODO Auto-generated method stub
		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}

	public static String CheckBabyBirthday(String year,String month,String day){
		int intyear=Integer.valueOf(year);
		int intmonth=Integer.valueOf(month);
		int intday=Integer.valueOf(day);
		
		if(intyear>2100&&intyear<1900) 
			return "输入非法的年份";
		if(intmonth>12||intmonth<0)
			return "输入非法的月份";
		if(intday>31||intday<0)
			return "输入非法日期";
		return "true";
		
	}
    
	
	
}
