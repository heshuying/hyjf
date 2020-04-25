package com.hyjf.batch.statistics.operationresport;

import com.hyjf.mongo.operationreport.dao.OperationMongDao;
import com.hyjf.mongo.operationreport.dao.OperationMongoGroupDao;
import com.hyjf.mongo.operationreport.entity.OperationMongoGroupEntity;
import com.hyjf.mongo.operationreport.entity.OperationReportEntity;
import com.hyjf.mybatis.mapper.auto.BorrowUserStatisticMapper;
import com.hyjf.mybatis.mapper.customize.OperationReportCustomizeMapper;
import com.hyjf.mybatis.model.auto.BorrowUserStatistic;
import com.hyjf.mybatis.model.auto.BorrowUserStatisticExample;
import com.hyjf.mybatis.model.customize.TenderCityCount;
import com.hyjf.mybatis.model.customize.TenderSexCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StatisticsOperationReportServiceImpl implements StatisticsOperationReportService {

	@Autowired
	private OperationReportCustomizeMapper operationReportCustomizeMapper;

	@Autowired
	private OperationMongoGroupDao operationMongoGroupDao;
	@Autowired
	private OperationMongDao operationMongDao;

	@Autowired
	private BorrowUserStatisticMapper borrowUserStatisticMapper;

	private Logger log = LoggerFactory.getLogger(StatisticsOperationReportServiceImpl.class);

	@Override
	public void insertOperationGroupData(Calendar cal) {
		OperationMongoGroupEntity oegroup = new OperationMongoGroupEntity();
		// 插入统计日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		oegroup.setInsertDate(transferDateToInt(cal, sdf));

		// 要统计前一个月的数据，所以月份要减一
		cal.add(Calendar.MONTH, -1);
		sdf = new SimpleDateFormat("yyyyMM");
		oegroup.setStatisticsMonth(transferDateToInt(cal, sdf));

		// 出借人按照地域分布
		List<TenderCityCount> cityGroup = operationReportCustomizeMapper.getTenderCityGroupBy(getLastDay(cal));
		Map<Integer, String> cityMap = cityGrouptoMap(cityGroup);
		oegroup.setInvestorRegionMap(cityMap);
		// Gson gson=new Gson();
		// String json = gson.toJson(cityGroup);
		// List<com.hyjf.mongo.operationreport.entity.TenderCityCount>
		// list=gson.fromJson(json, new
		// TypeToken<List<com.hyjf.mongo.operationreport.entity.TenderCityCount>>(){}.getType()
		// );

		// 出借人按照性别分布
		List<TenderSexCount> sexGroup = operationReportCustomizeMapper.getTenderSexGroupBy(getLastDay(cal));
		Map<Integer, Integer> sexMap = sexGrouptoMap(sexGroup);
		oegroup.setInvestorSexMap(sexMap);

		// 出借人按照年龄分布
		Map<Integer, Integer> ageMap = new HashMap<Integer, Integer>();
		int age = operationReportCustomizeMapper.getTenderAgeByRange(getLastDay(cal), 0,
				OperationMongoGroupEntity.ageRange1);
		ageMap.put(OperationMongoGroupEntity.ageRange1, age);
		age = operationReportCustomizeMapper.getTenderAgeByRange(getLastDay(cal), OperationMongoGroupEntity.ageRange1,
				OperationMongoGroupEntity.ageRange2);
		ageMap.put(OperationMongoGroupEntity.ageRange2, age);
		age = operationReportCustomizeMapper.getTenderAgeByRange(getLastDay(cal), OperationMongoGroupEntity.ageRange2,
				OperationMongoGroupEntity.ageRange3);
		ageMap.put(OperationMongoGroupEntity.ageRange3, age);
		age = operationReportCustomizeMapper.getTenderAgeByRange(getLastDay(cal), OperationMongoGroupEntity.ageRange3,
				OperationMongoGroupEntity.ageRange4);
		ageMap.put(OperationMongoGroupEntity.ageRange4, age);

		oegroup.setInvestorAgeMap(ageMap);

		operationMongoGroupDao.insert(oegroup);

	}

	@Override
	public Calendar insertOperationData(Calendar cal) {
		// 插入统计日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// 要统计前一个月的数据，所以月份要减一
		cal.add(Calendar.MONTH, -1);
		sdf = new SimpleDateFormat("yyyyMM");

		Query query = new Query();
		Criteria criteria = Criteria.where("statisticsMonth").is(transferDateToInt(cal, sdf));
		query.addCriteria(criteria);
		OperationReportEntity oe = operationMongDao.findOne(query);
		if (oe == null) {
			oe = new OperationReportEntity();
		}
		oe.setInsertDate(transferDateToInt(cal, sdf));

		oe.setStatisticsMonth(transferDateToInt(cal, sdf));
//		System.out.println(sdf.format(cal.getTime()));
		// 月交易金额
		oe.setAccountMonth(operationReportCustomizeMapper.getAccountByMonth(getFirstDay(cal), getLastDay(cal)));
//		System.out.println(sdf.format(cal.getTime()));
		// 月交易笔数
		oe.setTradeCountMonth(operationReportCustomizeMapper.getTradeCountByMonth(getFirstDay(cal), getLastDay(cal)));
//		System.out.println(sdf.format(cal.getTime()));
		// 累计交易笔数
//		oe.setTradeCount(operationReportCustomizeMapper.getTradeCount());
//		System.out.println(sdf.format(cal.getTime()));
		//累计交易金额
//		oe.setTotalCount(operationReportCustomizeMapper.getTotalCount());
		//累计收益
//		oe.setTotalInterest(operationReportCustomizeMapper.getIotalInterest());
		//借贷笔数
		oe.setLoanNum(operationReportCustomizeMapper.getLoanNum(getLastDay(cal)));
		//人均出借金额
		double bb=operationReportCustomizeMapper.getInvestLastDate(getLastDay(cal));
		int aa=operationReportCustomizeMapper.getTenderCount(getLastDay(cal));
//		System.out.println(bb);
//		System.out.println(aa);
//		System.out.println(bb/aa);
		oe.setPerInvest((int)Math.ceil(bb/aa));
		//平均满标时间
		oe.setFullBillTimeCurrentMonth(operationReportCustomizeMapper.getFullBillAverageTime(getLastDay(cal)));
//		System.out.println(sdf.format(cal.getTime()));
		//出借人总数
		oe.setTenderCount(operationReportCustomizeMapper.getTenderCount(getLastDay(cal)));
//		System.out.println(sdf.format(cal.getTime()));
		//代偿金额
		oe.setWillPayMoney(operationReportCustomizeMapper.getRepayTotal(getLastDay(cal)));

		BorrowUserStatistic borrowUserStatistic = this.selectBorrowUserStatistic();
		// 累计借款人
		oe.setBorrowuserCountTotal(borrowUserStatistic.getBorrowuserCountTotal());
		// 当前出借人
		oe.setBorrowuserCountCurrent(borrowUserStatistic.getBorrowuserCountCurrent());
		// 当前出借人
		oe.setTenderuserCountCurrent(borrowUserStatistic.getTenderuserCountCurrent());
		// 最大单一借款人待还金额占比
		oe.setBorrowuserMoneyTopone(borrowUserStatistic.getBorrowuserMoneyTopone().divide(borrowUserStatistic.getBorrowuserMoneyTotal(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP));
		// 前十大借款人待还金额占比
		oe.setBorrowuserMoneyTopten(borrowUserStatistic.getBorrowuserMoneyTopten().divide(borrowUserStatistic.getBorrowuserMoneyTotal(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP));

		log.info("借款数据....  oe is :{}", oe);

		operationMongDao.save(oe);
//		System.out.println(transferDateToInt(cal, sdf));
		return cal;
	}

	@Override
	public BorrowUserStatistic selectBorrowUserStatistic() {
		BorrowUserStatisticExample example = new BorrowUserStatisticExample();
		List<BorrowUserStatistic> list = borrowUserStatisticMapper.selectByExample(example);

		if(list == null || list.isEmpty()){
			return null;
		}

		return list.get(0);
	}

	private Map<Integer, Integer> sexGrouptoMap(List<TenderSexCount> list) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		Iterator<TenderSexCount> iter = list.iterator();
		while (iter.hasNext()) {
			TenderSexCount sex = iter.next();
			map.put(sex.getSex(), sex.getCount());
		}
		return map;
	}

	private Map<Integer, String> cityGrouptoMap(List<TenderCityCount> list) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		Iterator<TenderCityCount> iter = list.iterator();
		while (iter.hasNext()) {
			TenderCityCount tcity = iter.next();
			map.put(tcity.getCount(), tcity.getCount() + ":" + tcity.getName());
		}
		return map;
	}

	/**
	 * 通过输入的日期，获取这个日期所在月的第一天
	 * 
	 * @param cal
	 * @return
	 */
	public static Date getFirstDay(Calendar cal) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		System.out.println(sdf.format(cal.getTime()));

		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
//		System.out.println(sdf.format(cal.getTime()));
		return cal.getTime();
//		return GetDate.getYUECHU(cal.getTime());
	}

	/**
	 * 通过输入的日期，获取这个日期所在月份的最后一天
	 * 
	 * @param cal
	 * @return
	 */
	public static Date getLastDay(Calendar cal) {
//		12小时显示方式
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
//		24小时显示方式
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
//		System.out.println(sdf.format(cal.getTime()));
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//		System.out.println(sdf.format(cal.getTime()));
//		cal.set(Calendar.AM_PM, Calendar.PM);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
//		System.out.println(sdf.format(cal.getTime()));
		return cal.getTime();
//		return GetDate.getYUEMO(cal.getTime());
	}

	public int transferDateToInt(Calendar cl, SimpleDateFormat sdf) {
		// SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String c = sdf.format(cl.getTime());
		int date = Integer.parseInt(c);
		return date;
	}

}
