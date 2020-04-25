package com.hyjf.admin.finance.bankaccountmanage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BankAccountCheckUtil {

	/**
	 * 转换后的字符格式 yyyy-MM-dd
	 */
	public static final int DATEFORMAT_TYPE1 = 1;//yyyy-MM-dd
	/**
	 * 转换后的字符格式 yyyyMMdd
	 */
	public static final int DATEFORMAT_TYPE2 = 2;//yyyyMMdd
	/**
	 * add by cwyang 时间转换工具类
	 * 根据type的类型进行转换
	 * 转换成 对应格式的字符串时间
	 * 1:yyyy-MM-dd 2:yyyyMMdd
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date,int type){
		String dateStr = "";
		if (date != null && type > 0) {
			SimpleDateFormat format = new SimpleDateFormat();
			if (DATEFORMAT_TYPE1 == type) {
				format.applyPattern("yyyy-MM-dd");
			}else if(DATEFORMAT_TYPE2 == type){
				format.applyPattern("yyyyMMdd");
			}else{
				return "";
			}
			dateStr = format.format(date);
		}
		return dateStr;
	}

	/**
	 * add by cwyang
	 * 根据yyyy-mm-dd 格式的字符串转换成 date类型的日期
	 * @param date
	 * @return
	 */
	 public static Date getDateByString(String date){
		Date dated = null;
		if (date!=null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dated = format.parse(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dated;
	}
	/**
	 * add by cwyang 
	 * 获得指定月份的最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	 public static int getMonthLastDay(int year, int month){  
	    Calendar a = Calendar.getInstance();  
	    a.set(Calendar.YEAR, year);  
	    a.set(Calendar.MONTH, month-1);  
	    a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
	    a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
	    int maxDate = a.get(Calendar.DATE);  
	    return maxDate;  
	}

/**
	 * 通过现有时间推断一段时间后的时间
	 * 
	 * @param date 当前计算时间
	 * @param cmoth 需要计算的月份区间 正数为向后计算,负数向前计算
	 * @param cday 需要计算的日期区间 正数为向后计算,负数向前计算
	 * @return 计算之后的时间
	 */
	 public static Date getNoticeDate(Date date,int cmonth,int cday) {
		Date noticeDate = null;
		if (date!=null) {
			String datestr  = getDateString(date,DATEFORMAT_TYPE1);
			int year = Integer.parseInt(datestr.split("-")[0]);
			int month = Integer.parseInt(datestr.split("-")[1]);
			int day = Integer.parseInt(datestr.split("-")[2]);
			int naticeMonth = month + cmonth;
			int naticeDay = 0;
			if (naticeMonth>12) {//计算月份后日期转至下一年
				naticeMonth = naticeMonth - 12;
				year++;//年份+1
			}else if (naticeMonth<=0) {//计算月份后日期转至上一年
				naticeMonth = 12 - Math.abs(naticeMonth);
				year--;//年份减1
			}
			int lastdate = getMonthLastDay(year, naticeMonth);
			if (day+cday<=0) {//提前天数月份提前至上一个月
				int value = Math.abs(day+cday);
				if (naticeMonth==1) {//提前天数年份转至上一年
					naticeMonth = 12;
					year--;//年份-1
				}else{
					naticeMonth--;
				}
				//当前月份最后一天
				int maxdate = getMonthLastDay(year, naticeMonth);
				naticeDay = maxdate - value;
				
			}else if (day+cday>lastdate) {//延后天数后往后一个月
				if (naticeMonth==12) {
					naticeMonth = 1;
					year++;
				}else{
					naticeMonth++;
				}
				naticeDay = day+cday-lastdate;
			}else{
				naticeDay = day + cday;
			}
		    
		    String noticeDateStr = year + "-" + naticeMonth + "-" + naticeDay;
		    System.out.println(noticeDateStr);
		    noticeDate = getDateByString(noticeDateStr);
		    System.out.println(getDateString(noticeDate,DATEFORMAT_TYPE1));
		}
		return noticeDate;
	}
}
