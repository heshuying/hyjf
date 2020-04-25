package com.hyjf.batch.statistics.operationresport;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatisticsOperationReportTask {
	Logger _log = LoggerFactory.getLogger(StatisticsOperationReportTask.class);

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	public StatisticsOperationReportService statisticsOperationReportService;

	@Autowired
	private  StatisticsOperationReportInfoService statisticsOperationReportInfoService;

	// @Autowired
	// private OperationDataMongoService operationDataMongoService;
	public synchronized void run() {
		if (isOver) {
			try {
				isOver = false;

				statisticsDay();
			} finally {
				isOver = true;
			}
		}
	}

	/**
	 * 统计报表<br>
	 * 根据当前时间要获取到上个月的数据
	 * 
	 * 
	 */
	private void statisticsDay() {
		_log.info("开始 从数据库获取运营报告的数据...");

        boolean flag = RedisUtils.tranactionSet("StatisticsOperationReportTask");
        if(!flag){
            return;
        }

		Calendar cal = Calendar.getInstance();
		try {
			// 插入性别，性别 ，区域的统计信息
//			cal.add(Calendar.MONTH, 1);
			statisticsOperationReportService.insertOperationGroupData(cal);
		} catch (org.springframework.dao.DuplicateKeyException e) {
			_log.info("重复插入，可忽略");
		} catch (com.mongodb.DuplicateKeyException e) {
			_log.info("重复插入，可忽略");
		} catch (Exception e) {
			_log.info("重复插入，可忽略");
		}
		// 插入出借类的信息
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			cal = Calendar.getInstance();
//			cal.add(Calendar.MONTH, 1);
			statisticsOperationReportService.insertOperationData(cal);

		} catch (org.springframework.dao.DuplicateKeyException e) {
			_log.info("重复插入，可忽略");
		} catch (com.mongodb.DuplicateKeyException e) {
			_log.info("重复插入，可忽略");
		} catch (Exception e) {
			_log.info("重复插入，可忽略");
		}

		// 插入前台界面的运营报告(月，季，半年，全年)
		try {
			String year = String.valueOf(GetDate.getYear());
			String month = GetDate.getMonth();

			//输出上个月的日期
			int lastMonth = getLastMonth();

			//每个月月初的1号，自动统计出上一个月的数据，统计顺序依次是：
			//1月，2月，第一季度，4月，5月，上半年，7月，8月，第三季度，10月，11月，年度报告
			if(lastMonth == 12){
				statisticsOperationReportInfoService.setYearReport(year,month);
			} else if(lastMonth == 6 ){
				statisticsOperationReportInfoService.setHalfYearReport(year,month);
			}else if(lastMonth == 3 || lastMonth == 9 ){
				statisticsOperationReportInfoService.setQuarterReport(year,month);
			}else{
				statisticsOperationReportInfoService.setMonthReport(year,month);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

        RedisUtils.del("StatisticsOperationReportTask");

		_log.info("完成 插入统计数据到mongodb...");
	}

	/**
	 * 获得当前月份的上个月日期
	 * @return
	 */
	public static int getLastMonth(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		Calendar calendar = Calendar.getInstance();//日历对象
		calendar.setTime(new Date());//设置当前日期
		calendar.add(Calendar.MONTH, -1);//月份减一
		//输出上个月的日期
		int lastMonth = Integer.valueOf(sdf.format( calendar.getTime()));
		return lastMonth;
	}

	// /**
	// * 通过输入的日期，获取这个日期所在月的第一天
	// *
	// * @param cal
	// * @return
	// */
	// public static Date getFirstDay(Calendar cal) {
	// cal.set(Calendar.HOUR, 0);
	// cal.set(Calendar.MINUTE, 0);
	// cal.set(Calendar.SECOND, 0);
	// return GetDate.getYUECHU(cal.getTime());
	// }
	//
	// /**
	// * 通过输入的日期，获取这个日期所在月份的最后一天
	// *
	// * @param cal
	// * @return
	// */
	// public static Date getLastDay(Calendar cal) {
	// cal.set(Calendar.HOUR, 23);
	// cal.set(Calendar.MINUTE, 59);
	// cal.set(Calendar.SECOND, 59);
	// return GetDate.getYUEMO(cal.getTime());
	// }
	// public String get(float f){
	// DecimalFormat df = new DecimalFormat("##.##");
	// return df.format(f);
	// }
	// public int getDate(Calendar cl){
	// SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
	// String c=sdf.format(cl.getTime());
	// int date = Integer.parseInt(c);
	// return date;
	// }
}
