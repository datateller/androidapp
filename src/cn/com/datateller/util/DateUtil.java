package cn.com.datateller.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	//���ַ�������ʽ���ص�ǰ����
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
			return "����Ƿ������";
		if(intmonth>12||intmonth<0)
			return "����Ƿ����·�";
		if(intday>31||intday<0)
			return "����Ƿ�����";
		return "true";
		
	}
    
	
	
}
