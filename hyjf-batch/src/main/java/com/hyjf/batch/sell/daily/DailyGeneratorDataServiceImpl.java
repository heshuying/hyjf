package com.hyjf.batch.sell.daily;

import static com.hyjf.common.util.GetDate.getLastDayOnMonth;

import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.auto.HolidaysConfigNewMapper;
import com.hyjf.mybatis.mapper.auto.SellDailyMapper;
import com.hyjf.mybatis.mapper.customize.SellDailyCustomizeMapper;
import com.hyjf.mybatis.model.auto.HolidaysConfigNew;
import com.hyjf.mybatis.model.auto.HolidaysConfigNewExample;
import com.hyjf.mybatis.model.auto.SellDaily;
import com.hyjf.mybatis.model.auto.SellDailyExample;

/**
 * @author xiasq
 * @version DailyGeneratorDataServiceImpl, v0.1 2018/8/2 9:54
 */
@Service
public class DailyGeneratorDataServiceImpl implements DailyGeneratorDataService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SellDailyMapper sellDailyMapper;
	@Autowired
	private SellDailyCustomizeMapper sellDailyCustomizeMapper;
	@Autowired
	private HolidaysConfigNewMapper holidaysConfigNewMapper;
	@Autowired
	private DailyAutoSendService dailyAutoSendService;

	private static final String NMZX_DIVISION_NAME = "纳觅咨询";
	private static final String QGR_DIVISION_NAME = "裕峰瑞";
	private static final String DTHJ_DIVISION_NAME = "大唐汇金";
	private static final String SHRJ_DIVISION_NAME = "上海嵘具";
	private static final String YYZX_DIVISION_NAME = "运营中心";
	private static final String HZSW_DIVISION_NAME = "惠众";

	private static final int DRAW_ORDER_LEVEL1 = 1;
	private static final int DRAW_ORDER_LEVEL2 = 2;
	private static final int DRAW_ORDER_LEVEL3 = 3;
	private static final int DRAW_ORDER_LEVEL4 = 4;

	/** 查询所有分部 */
	private static final Integer QUERY_ALL_DIVISION_TYPE = 1;
	/** 上海运营中心-网络运营部 id:327 */
	private static final Integer QUERY_OC_THREE_DIVISION_TYPE = 2;
	/** 查询APP推广 */
	private static final Integer QUERY_APP_TYPE = 3;
	/** 不需要显示的网点 */
	private static final List NMZX_IGNORE_TWODIVISION_LIST = Arrays.asList("胶州分部");
	private static final List DTHJ_IGNORE_TWODIVISION_LIST = Arrays.asList("樟树分部", "东莞分部", "西安分部");

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void initDepartment() {
		List<SellDaily> list = new ArrayList<>();
		List<String> twoDivisionList1 = sellDailyCustomizeMapper.selectTwoDivisionByPrimaryDivision(NMZX_DIVISION_NAME);
		for (String twoDivision : twoDivisionList1) {
			if (NMZX_IGNORE_TWODIVISION_LIST.contains(twoDivision)) {
				continue;
			}
			list.add(this.constructionSellDaily(NMZX_DIVISION_NAME, twoDivision, DRAW_ORDER_LEVEL1,
					getStoreNum(NMZX_DIVISION_NAME, twoDivision)));
		}

		list.add(this.constructionSellDaily(YYZX_DIVISION_NAME, "网络运营部", DRAW_ORDER_LEVEL2, 0));
		list.add(this.constructionSellDaily(YYZX_DIVISION_NAME, "无主单", DRAW_ORDER_LEVEL2, 0));
		list.add(this.constructionSellDaily("其中：", "APP推广", DRAW_ORDER_LEVEL2, 0));
		list.add(this.constructionSellDaily(HZSW_DIVISION_NAME, "其它", DRAW_ORDER_LEVEL2, 0));

		List<String> twoDivisionList3 = sellDailyCustomizeMapper.selectTwoDivisionByPrimaryDivision(QGR_DIVISION_NAME);
		for (String twoDivision : twoDivisionList3) {
			list.add(this.constructionSellDaily(QGR_DIVISION_NAME, twoDivision, DRAW_ORDER_LEVEL3,
					getStoreNum(QGR_DIVISION_NAME, twoDivision)));
		}

		List<String> twoDivisionList4 = sellDailyCustomizeMapper.selectTwoDivisionByPrimaryDivision(DTHJ_DIVISION_NAME);
		List<String> twoDivisionList5 = sellDailyCustomizeMapper.selectTwoDivisionByPrimaryDivision(SHRJ_DIVISION_NAME);
		for (String twoDivision : twoDivisionList4) {
			if (DTHJ_IGNORE_TWODIVISION_LIST.contains(twoDivision)) {
				continue;
			}
			list.add(this.constructionSellDaily(DTHJ_DIVISION_NAME, twoDivision, DRAW_ORDER_LEVEL4,
					getStoreNum(DTHJ_DIVISION_NAME, twoDivision)));
		}
		for (String twoDivision : twoDivisionList5) {
			list.add(this.constructionSellDaily(SHRJ_DIVISION_NAME, twoDivision, DRAW_ORDER_LEVEL4,
					getStoreNum(SHRJ_DIVISION_NAME, twoDivision)));
		}
		sellDailyCustomizeMapper.batchInsert(list);
	}

	/**
	 * 生成销售日报数据
	 * 
	 * @param date
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void generatorSellDaily(Date currentDate) {

		// 昨日结束日期（昨日统计数据的结束日期）
		Date yesterdayEndTime;
		// 本月累计列计算的开始时间
		Date totalMonthStartTime;
		// 本月累计列计算的结束时间
		Date totalMonthEndTime;
		// 上月累计结束日期
		Date totalPreviousMonthEndTime;
		// 上月累计开始时间
		Date totalPreviousMonthStartTime;
		// 昨日开始日期 节假日向前退推移
		Date yesterdayStartTime;
		// 第十七列: 查询当日待还（工作日计算当天， 如果工作日次日是节假日，那么计算当天到节假日过后第一个工作日）
		Date todayEndTime;

		// 当月第一个工作日，查询上个月数据
		if (isMonthFirstDay(currentDate)) {
			yesterdayEndTime = GetDate.getYesterdayEndTime(currentDate);
			totalMonthStartTime = GetDate.getFirstDayOnMonth(GetDate.getYesterdayEndTime(getFirstDayOnMonth(currentDate)));
			totalMonthEndTime = getLastDayOnMonth(totalMonthStartTime);
			totalPreviousMonthEndTime = getPreviousMonth(totalMonthEndTime);
			totalPreviousMonthStartTime = GetDate.getFirstDayOnMonth(totalPreviousMonthEndTime);
			yesterdayStartTime = this.getYesterdayStartTime(currentDate);
			todayEndTime = getNextWorkDate(currentDate);

		} else {
			yesterdayEndTime = GetDate.getYesterdayEndTime(currentDate);
			totalMonthStartTime = GetDate.getFirstDayOnMonth(yesterdayEndTime);
			totalMonthEndTime = yesterdayEndTime;
			totalPreviousMonthEndTime = getPreviousMonth(yesterdayEndTime);
			totalPreviousMonthStartTime = GetDate.getFirstDayOnMonth(totalPreviousMonthEndTime);
			yesterdayStartTime = this.getYesterdayStartTime(currentDate);
			todayEndTime = getNextWorkDate(currentDate);
		}


		// 第一列: 查询本月累计规模业绩
		this.insertSomeColumn(totalMonthStartTime, totalMonthEndTime, 1);

		// 第三列: 查询上月累计规模业绩
		this.insertSomeColumn(totalPreviousMonthStartTime, totalPreviousMonthEndTime, 3);

		// 第二列: 查询本月累计已还款
		this.insertSomeColumn(totalMonthStartTime, totalMonthEndTime, 2);

		// 第五列: 查询本月累计提现
		this.insertSomeColumn(totalMonthStartTime, totalMonthEndTime, 5);

		// 第七列: 查询本月累计充值
		this.insertSomeColumn(totalMonthStartTime, totalMonthEndTime, 7);

		// 第八列: 计算本月累计年化业绩
		this.insertSomeColumn(totalMonthStartTime, totalMonthEndTime, 8);
		// 第九列: 计算上月累计年化业绩
		this.insertSomeColumn(totalPreviousMonthStartTime, totalPreviousMonthEndTime, 9);

		// 第十一列: 查询昨日规模业绩
		this.insertSomeColumn(yesterdayStartTime, yesterdayEndTime, 11);
		// 第十二列: 查询昨日还款
		this.insertSomeColumn(yesterdayStartTime, yesterdayEndTime, 12);
		// 第十三列: 查询昨日年化业绩
		this.insertSomeColumn(yesterdayStartTime, yesterdayEndTime, 13);
		// 第十四列: 查询昨日提现
		this.insertSomeColumn(yesterdayStartTime, yesterdayEndTime, 14);
		// 第十五列: 查询昨日充值
		this.insertSomeColumn(yesterdayStartTime, yesterdayEndTime, 15);

		// 统一计算第四、六、十列速率,第十六列净资金流
		// 第四列: 计算环比增速
		// 第六列: 计算提现占比
		// 第十列: 计算环比增速
		// 第十六列: 计算昨日净资金流（充值-提现）
		sellDailyCustomizeMapper.calculateRate();

		// 第十七列: 查询当日待还（工作日计算当天， 如果工作日次日是节假日，那么计算当天到节假日过后第一个工作日）
		this.insertSomeColumn(GetDate.getSomeDayStart(currentDate), GetDate.getSomeDayEnd(todayEndTime), 17);

		// 第十八列: 查询昨日注册数
		this.insertSomeColumn(yesterdayStartTime, yesterdayEndTime, 18);

		// 第十九列: 查询其中充值≥3000人数
		this.insertSomeColumn(yesterdayStartTime, yesterdayEndTime, 19);
		// 第二十列: 查询出借≥3000人数
		this.insertSomeColumn(yesterdayStartTime, yesterdayEndTime, 20);
		// 第二十一列: 查询本月累计出借3000以上新客户数
		this.insertSomeColumn(totalMonthStartTime, yesterdayEndTime, 21);
	}

	/**
	 * 判断是否是当前月份第一天
	 *
	 * @param currentDate
	 * @return
	 */
	private boolean isMonthFirstDay(Date currentDate) {
		HolidaysConfigNewExample example = new HolidaysConfigNewExample();
		HolidaysConfigNewExample.Criteria criteria = example.createCriteria();

		criteria.andDayTimeBetween(getFirstDayOnMonth(currentDate), getLastDayOnMonth(currentDate));

		criteria.andHolidayFlagEqualTo(0);
		example.setOrderByClause("day_time asc");
		List<HolidaysConfigNew> list = holidaysConfigNewMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			throw new RuntimeException("日历配置错误...");
		}
		return DateUtils.isSameDay(list.get(0).getDayTime(), currentDate);
	}

	/**
	 * 获取某日期的月第一天
	 *
	 * @param currentDate
	 * @return
	 */
	private Date getFirstDayOnMonth(Date currentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 当前日期是否已经生成销售日报
	 * 
	 * @return
	 */
	@Override
	public boolean hasGeneratorDataToday() {
		SellDailyExample example = new SellDailyExample();
		SellDailyExample.Criteria criteria = example.createCriteria();
		criteria.andDateStrEqualTo(GetDate.getFormatDateStr());
		List<SellDaily> list = sellDailyMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取指定时间上月
	 * 
	 * @param date
	 * @return
	 */
	private Date getPreviousMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}

	/**
	 * 插入本月累计规模、上月累计规模
	 *
	 * @param startTime
	 * @param endTime
	 * @param column
	 *            1-本月累计规模 2-本月累计已还款 3-上月累计规模 5-本月累计提现 7-本月累计充值 8-本月累计年化业绩
	 *            9-上月累计年化业绩 11-昨日规模业绩 12-昨日还款 13-昨日年化业绩 14-昨日提现 15-昨日充值 17-当日待还
	 */
	private void insertSomeColumn(Date startTime, Date endTime, int column) {
		logger.info("startTime :" + GetDate.formatTime(startTime) + ", endTime is :" + GetDate.formatTime(endTime)
				+ ", column:" + column);
		// 1. 按照一级分部，二级分部分组查询统计数据
		List<SellDaily> list = null;
		List<SellDaily> ocSellDailyList = null;
		List<SellDaily> appSellDailyList = null;
		// 上海运营中心-网络运营部要单独查询 (id:327)
		SellDaily shOCSellDaily = constructionSellDaily(null, null);
		// app推广单独查询
		SellDaily appSellDaily = null;
		long timeStart = System.currentTimeMillis();
		switch (column) {
		case 1:
			list = sellDailyCustomizeMapper.countTotalInvestOnMonth(startTime, endTime, QUERY_ALL_DIVISION_TYPE);

			ocSellDailyList = sellDailyCustomizeMapper.countTotalInvestOnMonth(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
			if(!CollectionUtils.isEmpty(ocSellDailyList)){
				BigDecimal investTotalMonth = BigDecimal.ZERO;
				for(SellDaily sl: ocSellDailyList){
					investTotalMonth = investTotalMonth.add(sl.getInvestTotalMonth());
				}
				shOCSellDaily.setInvestTotalMonth(investTotalMonth);
			}

			appSellDailyList = sellDailyCustomizeMapper.countTotalInvestOnMonth(startTime, endTime, QUERY_APP_TYPE);
			appSellDaily = CollectionUtils.isEmpty(appSellDailyList) ? constructionSellDaily(null, null)
					: appSellDailyList.get(0);
			break;
		case 2:
			list = sellDailyCustomizeMapper.countTotalRepayOnMonth(startTime, endTime, QUERY_ALL_DIVISION_TYPE);

			ocSellDailyList = sellDailyCustomizeMapper.countTotalRepayOnMonth(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
			if(!CollectionUtils.isEmpty(ocSellDailyList)){
				BigDecimal repaymentTotalMonth = BigDecimal.ZERO;
				for(SellDaily sl: ocSellDailyList){
					repaymentTotalMonth = repaymentTotalMonth.add(sl.getRepaymentTotalMonth());
				}
				shOCSellDaily.setRepaymentTotalMonth(repaymentTotalMonth);
			}

			break;
		case 3:
			list = sellDailyCustomizeMapper.countTotalInvestOnPreviousMonth(startTime, endTime,
					QUERY_ALL_DIVISION_TYPE);

			ocSellDailyList = sellDailyCustomizeMapper.countTotalInvestOnPreviousMonth(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
			if(!CollectionUtils.isEmpty(ocSellDailyList)){
				BigDecimal investTotalPreviousMonth = BigDecimal.ZERO;
				for(SellDaily sl: ocSellDailyList){
					investTotalPreviousMonth = investTotalPreviousMonth.add(sl.getInvestTotalPreviousMonth());
				}
				shOCSellDaily.setInvestTotalPreviousMonth(investTotalPreviousMonth);
			}

			appSellDailyList = sellDailyCustomizeMapper.countTotalInvestOnPreviousMonth(startTime, endTime,
					QUERY_APP_TYPE);
			appSellDaily = CollectionUtils.isEmpty(appSellDailyList) ? constructionSellDaily(null, null)
					: appSellDailyList.get(0);
			break;
		case 5:
			list = sellDailyCustomizeMapper.countTotalWithdrawOnMonth(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countTotalWithdrawOnMonth(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
			if(!CollectionUtils.isEmpty(ocSellDailyList)){
				BigDecimal withdrawTotalMonth = BigDecimal.ZERO;
				for(SellDaily sl: ocSellDailyList){
                    withdrawTotalMonth = withdrawTotalMonth.add(sl.getWithdrawTotalMonth());
				}
				shOCSellDaily.setWithdrawTotalMonth(withdrawTotalMonth);
			}
			break;
		case 7:
			list = sellDailyCustomizeMapper.countTotalRechargeOnMonth(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countTotalRechargeOnMonth(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                BigDecimal rechargeTotalMonth = BigDecimal.ZERO;
                for(SellDaily sl: ocSellDailyList){
                    rechargeTotalMonth = rechargeTotalMonth.add(sl.getRechargeTotalMonth());
                }
                shOCSellDaily.setRechargeTotalMonth(rechargeTotalMonth);
            }
			break;
		case 8:
			list = sellDailyCustomizeMapper.countTotalAnnualInvestOnMonth(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countTotalAnnualInvestOnMonth(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                BigDecimal investAnnualTotalMonth = BigDecimal.ZERO;
                for(SellDaily sl: ocSellDailyList){
                    investAnnualTotalMonth = investAnnualTotalMonth.add(sl.getInvestAnnualTotalMonth());
                }
                shOCSellDaily.setInvestAnnualTotalMonth(investAnnualTotalMonth);
            }

			appSellDailyList = sellDailyCustomizeMapper.countTotalAnnualInvestOnMonth(startTime, endTime,
					QUERY_APP_TYPE);
			appSellDaily = CollectionUtils.isEmpty(appSellDailyList) ? constructionSellDaily(null, null)
					: appSellDailyList.get(0);
			break;
		case 9:
			list = sellDailyCustomizeMapper.countTotalAnnualInvestOnPreviousMonth(startTime, endTime,
					QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countTotalAnnualInvestOnPreviousMonth(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                BigDecimal investAnnualTotalPreviousMonth = BigDecimal.ZERO;
                for(SellDaily sl: ocSellDailyList){
                    investAnnualTotalPreviousMonth = investAnnualTotalPreviousMonth.add(sl.getInvestAnnualTotalPreviousMonth());
                }
                shOCSellDaily.setInvestAnnualTotalPreviousMonth(investAnnualTotalPreviousMonth);
            }
			appSellDailyList = sellDailyCustomizeMapper.countTotalAnnualInvestOnPreviousMonth(startTime, endTime,
					QUERY_APP_TYPE);
			appSellDaily = CollectionUtils.isEmpty(appSellDailyList) ? constructionSellDaily(null, null)
					: appSellDailyList.get(0);
			break;
		case 11:
			list = sellDailyCustomizeMapper.countTotalTenderYesterday(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countTotalTenderYesterday(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                BigDecimal invest_total_yesterday = BigDecimal.ZERO;
                for(SellDaily sl: ocSellDailyList){
                    invest_total_yesterday = invest_total_yesterday.add(sl.getInvestTotalYesterday());
                }
                shOCSellDaily.setInvestTotalYesterday(invest_total_yesterday);
            }
			appSellDailyList = sellDailyCustomizeMapper.countTotalTenderYesterday(startTime, endTime, QUERY_APP_TYPE);
			appSellDaily = CollectionUtils.isEmpty(appSellDailyList) ? constructionSellDaily(null, null)
					: appSellDailyList.get(0);
			break;
		case 12:
			list = sellDailyCustomizeMapper.countTotalRepayYesterday(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countTotalRepayYesterday(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                BigDecimal repaymentTotalYesterday = BigDecimal.ZERO;
                for(SellDaily sl: ocSellDailyList){
                    repaymentTotalYesterday = repaymentTotalYesterday.add(sl.getRepaymentTotalYesterday());
                }
                shOCSellDaily.setRepaymentTotalYesterday(repaymentTotalYesterday);
            }
			break;
		case 13:
			list = sellDailyCustomizeMapper.countTotalAnnualInvestYesterday(startTime, endTime,
					QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countTotalAnnualInvestYesterday(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                BigDecimal investAnnualTotalYesterday = BigDecimal.ZERO;
                for(SellDaily sl: ocSellDailyList){
                    investAnnualTotalYesterday = investAnnualTotalYesterday.add(sl.getInvestAnnualTotalYesterday());
                }
                shOCSellDaily.setInvestAnnualTotalYesterday(investAnnualTotalYesterday);
            }

			appSellDailyList = sellDailyCustomizeMapper.countTotalAnnualInvestYesterday(startTime, endTime,
					QUERY_APP_TYPE);
			appSellDaily = CollectionUtils.isEmpty(appSellDailyList) ? constructionSellDaily(null, null)
					: appSellDailyList.get(0);
			break;
		case 14:
			list = sellDailyCustomizeMapper.countTotalWithdrawYesterday(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countTotalWithdrawYesterday(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                BigDecimal withdrawTotalYesterday = BigDecimal.ZERO;
                for(SellDaily sl: ocSellDailyList){
                    withdrawTotalYesterday = withdrawTotalYesterday.add(sl.getWithdrawTotalYesterday());
                }
                shOCSellDaily.setWithdrawTotalYesterday(withdrawTotalYesterday);
            }
			break;
		case 15:
			list = sellDailyCustomizeMapper.countTotalRechargeYesterday(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countTotalRechargeYesterday(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                BigDecimal rechargeTotalYesterday = BigDecimal.ZERO;
                for(SellDaily sl: ocSellDailyList){
                    rechargeTotalYesterday = rechargeTotalYesterday.add(sl.getRechargeTotalYesterday());
                }
                shOCSellDaily.setRechargeTotalYesterday(rechargeTotalYesterday);
            }
			break;
		case 17:
			list = sellDailyCustomizeMapper.countNoneRepayToday(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countNoneRepayToday(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                BigDecimal nonRepaymentToday = BigDecimal.ZERO;
                for(SellDaily sl: ocSellDailyList){
                    nonRepaymentToday = nonRepaymentToday.add(sl.getNonRepaymentToday());
                }
                shOCSellDaily.setNonRepaymentToday(nonRepaymentToday);
            }
			break;
		case 18:
			list = sellDailyCustomizeMapper.countRegisterTotalYesterday(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countRegisterTotalYesterday(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                int registerTotalYesterday = 0;
                for(SellDaily sl: ocSellDailyList){
                    registerTotalYesterday += sl.getRegisterTotalYesterday().intValue();
                }
                shOCSellDaily.setRegisterTotalYesterday(registerTotalYesterday);
            }
			break;
		case 19:
			list = sellDailyCustomizeMapper.countRechargeGt3000UserNum(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countRechargeGt3000UserNum(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                int rechargeGt3000UserNum = 0;
                for(SellDaily sl: ocSellDailyList){
                    rechargeGt3000UserNum += sl.getRechargeGt3000UserNum().intValue();
                }
                shOCSellDaily.setRechargeGt3000UserNum(rechargeGt3000UserNum);
            }
			break;
		case 20:
			list = sellDailyCustomizeMapper.countInvestGt3000UserNum(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countInvestGt3000UserNum(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                int investGt3000UserNum = 0;
                for(SellDaily sl: ocSellDailyList){
                    investGt3000UserNum += sl.getInvestGt3000UserNum().intValue();
                }
                shOCSellDaily.setInvestGt3000UserNum(investGt3000UserNum);
            }
			break;
		case 21:
			list = sellDailyCustomizeMapper.countInvestGt3000MonthUserNum(startTime, endTime, QUERY_ALL_DIVISION_TYPE);
			ocSellDailyList = sellDailyCustomizeMapper.countInvestGt3000MonthUserNum(startTime, endTime,
					QUERY_OC_THREE_DIVISION_TYPE);
            if(!CollectionUtils.isEmpty(ocSellDailyList)){
                int investGt3000MonthUserNum = 0;
                for(SellDaily sl: ocSellDailyList){
                    investGt3000MonthUserNum += sl.getInvestGt3000MonthUserNum().intValue();
                }
                shOCSellDaily.setInvestGt3000MonthUserNum(investGt3000MonthUserNum);
            }
			break;
		}

		// 2. 处理drawOrder=2特殊分部的数据
		// 运营中心无主单 - 月累计出借
		BigDecimal noneRefferTotalTmp = BigDecimal.ZERO;
		BigDecimal hzTotalTmp = BigDecimal.ZERO;
		for (SellDaily entity : list) {
			if (StringUtils.isEmpty(entity.getPrimaryDivision()) || "杭州分公司".equals(entity.getPrimaryDivision())
					|| "特殊一级分部（勿动）".equals(entity.getPrimaryDivision())) {
				noneRefferTotalTmp = this.addValue(noneRefferTotalTmp, column, entity);
			}

			if ("惠众商务".equals(entity.getPrimaryDivision())) {
				hzTotalTmp = this.addValue(hzTotalTmp, column, entity);
			}
		}

		// 2.1 网络运营部特指：上海运营中心-网络运营部 单独查询
		list.add(this.constructionSellDaily(shOCSellDaily, YYZX_DIVISION_NAME, "网络运营部", 2, 0));

		// 2.2 无主单包含： 部门空 + 杭州分部 + 特殊一级分部（勿动）
		SellDaily noneRefferRecord = this.constructionSellDaily(YYZX_DIVISION_NAME, "无主单");
		noneRefferRecord = this.setValue(noneRefferTotalTmp, column, noneRefferRecord,
				this.constructionSellDaily("", ""));
		list.add(noneRefferRecord);

		// 2.3 app推广计算app渠道出借， 只显示 本月累计规模业绩 上月对应累计规模业绩 环比增速 本月累计年化业绩 上月累计年化业绩 环比增速
		// 昨日规模业绩
		// 昨日年化业绩 昨日注册数 其中充值≥3000人数 其中出借≥3000人数 本月累计出借3000以上新客户数
		if (Arrays.asList(1, 3, 8, 9, 11, 13).contains(column)) {
			list.add(this.constructionSellDaily(appSellDaily, "其中：", "APP推广", 2, 0));
		}

		// 2.4 惠众-其它 排除 上海运营中心-网络运营部
		SellDaily hzRecord = this.constructionSellDaily(HZSW_DIVISION_NAME, "其它");
		hzRecord = this.setValue(hzTotalTmp, column, hzRecord, shOCSellDaily);
		list.add(hzRecord);

		// 3. 批量更新
		long timeTmp = System.currentTimeMillis();
		logger.info("填充数据耗时: " + (timeTmp - timeStart) + "ms, 批量更新开始，column: " + column);
		sellDailyCustomizeMapper.batchUpdate(list);
		long timeEnd = System.currentTimeMillis();
		logger.info("批量更新结束， column: " + column + ", 耗时: " + (timeEnd - timeTmp) + "ms");
	}

	/**
	 * 特殊部门赋值
	 * 
	 * @param tmp
	 *            临时值
	 * @param column
	 *            第几列
	 * @param sellDaily
	 * @param sellDaily
	 *            扣减值
	 * @return
	 */
	private SellDaily setValue(BigDecimal tmp, int column, SellDaily sellDaily, SellDaily reduceSellDaily) {
		switch (column) {
		case 1:
			sellDaily.setInvestTotalMonth(tmp.subtract(reduceSellDaily.getInvestTotalMonth()));
			break;
		case 2:
			sellDaily.setRepaymentTotalMonth(tmp.subtract(reduceSellDaily.getRepaymentTotalMonth()));
			break;
		case 3:
			sellDaily.setInvestTotalPreviousMonth(tmp.subtract(reduceSellDaily.getInvestTotalPreviousMonth()));
			break;
		case 5:
			sellDaily.setWithdrawTotalMonth(tmp.subtract(reduceSellDaily.getWithdrawTotalMonth()));
			break;
		case 7:
			sellDaily.setRechargeTotalMonth(tmp.subtract(reduceSellDaily.getRechargeTotalMonth()));
			break;
		case 8:
			sellDaily.setInvestAnnualTotalMonth(tmp.subtract(reduceSellDaily.getInvestAnnualTotalMonth()));
			break;
		case 9:
			sellDaily.setInvestAnnualTotalPreviousMonth(
					tmp.subtract(reduceSellDaily.getInvestAnnualTotalPreviousMonth()));
			break;
		case 11:
			sellDaily.setInvestTotalYesterday(tmp.subtract(reduceSellDaily.getInvestTotalYesterday()));
			break;
		case 12:
			sellDaily.setRepaymentTotalYesterday(tmp.subtract(reduceSellDaily.getRepaymentTotalYesterday()));
			break;
		case 13:
			sellDaily.setInvestAnnualTotalYesterday(tmp.subtract(reduceSellDaily.getInvestAnnualTotalYesterday()));
			break;
		case 14:
			sellDaily.setWithdrawTotalYesterday(tmp.subtract(reduceSellDaily.getWithdrawTotalYesterday()));
			break;
		case 15:
			sellDaily.setRechargeTotalYesterday(tmp.subtract(reduceSellDaily.getRechargeTotalYesterday()));
			break;
		case 17:
			sellDaily.setNonRepaymentToday(tmp.subtract(reduceSellDaily.getNonRepaymentToday()));
			break;
		case 18:
			sellDaily.setRegisterTotalYesterday(tmp.intValue() - reduceSellDaily.getRegisterTotalYesterday());
			break;
		case 19:
			sellDaily.setRechargeGt3000UserNum(tmp.intValue() - reduceSellDaily.getRechargeGt3000UserNum());
			break;
		case 20:
			sellDaily.setInvestGt3000UserNum(tmp.intValue() - reduceSellDaily.getInvestGt3000UserNum());
			break;
		case 21:
			sellDaily.setInvestGt3000MonthUserNum(tmp.intValue() - reduceSellDaily.getInvestGt3000MonthUserNum());
			break;
		}

		return sellDaily;
	}

	/**
	 * drawOrder=2特殊分部的数值累加
	 * 
	 * @param tmp
	 * @param column
	 * @param entity
	 * @return
	 */
	private BigDecimal addValue(BigDecimal tmp, int column, SellDaily entity) {
		BigDecimal result = BigDecimal.ZERO;
		switch (column) {
		case 1:
			result = tmp.add(entity.getInvestTotalMonth());
			break;
		case 2:
			result = tmp.add(entity.getRepaymentTotalMonth());
			break;
		case 3:
			result = tmp.add(entity.getInvestTotalPreviousMonth());
			break;
		case 5:
			result = tmp.add(entity.getWithdrawTotalMonth());
			break;
		case 7:
			result = tmp.add(entity.getRechargeTotalMonth());
			break;
		case 8:
			result = tmp.add(entity.getInvestAnnualTotalMonth());
			break;
		case 9:
			result = tmp.add(entity.getInvestAnnualTotalPreviousMonth());
			break;
		case 11:
			result = tmp.add(entity.getInvestTotalYesterday());
			break;
		case 12:
			result = tmp.add(entity.getRepaymentTotalYesterday());
			break;
		case 13:
			result = tmp.add(entity.getInvestAnnualTotalYesterday());
			break;
		case 14:
			result = tmp.add(entity.getWithdrawTotalYesterday());
			break;
		case 15:
			result = tmp.add(entity.getRechargeTotalYesterday());
			break;
		case 17:
			result = tmp.add(entity.getNonRepaymentToday());
			break;
		case 18:
			result = new BigDecimal(tmp.intValue() + entity.getRegisterTotalYesterday());
			break;
		case 19:
			result = new BigDecimal(tmp.intValue() + entity.getRechargeGt3000UserNum());
			break;
		case 20:
			result = new BigDecimal(tmp.intValue() + entity.getInvestGt3000UserNum());
			break;
		case 21:
			result = new BigDecimal(tmp.intValue() + entity.getInvestGt3000MonthUserNum());
			break;
		}

		return result;
	}

	/**
	 * 获取下一个工作日前节假日结束时间
	 * @param currentDate
	 * @return
	 */
	private Date getNextWorkDate(Date currentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date torrowDate = calendar.getTime();

		// 昨天非工作日， 前推到第一个工作日
		if (!dailyAutoSendService.isWorkdateOnSomeDay(torrowDate)) {
			torrowDate = getFirstWorkdateAfterSomeDate(torrowDate);
			// 取昨日
			calendar.setTime(torrowDate);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			currentDate = calendar.getTime();
		}

		return currentDate;
	}

	/**
	 * 昨日开始时间 昨日节假日往前推直到第一个工作日
	 *
	 * @param currentDate
	 * @return
	 */
	private Date getYesterdayStartTime(Date currentDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date yesterday = calendar.getTime();

		// 昨天非工作日， 前推到第一个工作日
		if (!dailyAutoSendService.isWorkdateOnSomeDay(yesterday)) {
			yesterday = getFirstWorkdateBeforeSomeDate(yesterday);
		}

		return GetDate.getSomeDayStart(yesterday);
	}

	/**
	 * 取昨日开始时间
	 *
	 * @param date
	 * @return
	 */
	private Date getFirstWorkdateBeforeSomeDate(Date date) {
		HolidaysConfigNewExample example = new HolidaysConfigNewExample();
		HolidaysConfigNewExample.Criteria criteria = example.createCriteria();
		criteria.andDayTimeLessThan(date);
		criteria.andHolidayFlagEqualTo(0);
		example.setOrderByClause("day_time desc");
		List<HolidaysConfigNew> list = holidaysConfigNewMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			throw new RuntimeException("日历配置错误...");
		}
		return list.get(0).getDayTime();
	}

	/**
	 * 取明日时间 -- 第一个工作日
	 *
	 * @param date
	 * @return
	 */
	private Date getFirstWorkdateAfterSomeDate(Date date) {
		HolidaysConfigNewExample example = new HolidaysConfigNewExample();
		HolidaysConfigNewExample.Criteria criteria = example.createCriteria();
		criteria.andDayTimeGreaterThan(date);
		criteria.andHolidayFlagEqualTo(0);
		example.setOrderByClause("day_time asc");
		List<HolidaysConfigNew> list = holidaysConfigNewMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			throw new RuntimeException("日历配置错误...");
		}
		return list.get(0).getDayTime();
	}

	/**
	 * 初始化部门 日期 一级部门 二级部门 绘制顺序 门店数量
	 */

	private static final List<String> NM_NUM_0 = Arrays.asList("威海中信分部", "东营一分部", "青岛开发区分部", "济南分部", "青岛市南分部", "天津分部",
			"乳山分部", "郑州一分部", "荣成分部", "上海一分部", "青岛清江路社区", "上海运营中心", "邹平分部", "吉林分部", "漯河分部", "上海二分部", "成都分公司", "深圳分部",
			"郑州二分部", "青岛红岛社区", "阜阳分部", "成都分部", "青岛崂山社区");
	private static final List<String> NM_NUM_1 = Arrays.asList("莱州分部", "即墨二分部", "常州分部", "威海经区分部", "北京分部", "烟台开发区分部",
			"黄岛分部", "招远分部", "青岛市北分部", "即墨一分部");
	private static final List<String> NM_NUM_2 = Arrays.asList("文登分部", "烟台一分部", "淄博分部", "青岛城阳分部", "上海三分部");
	private static final List<String> NM_NUM_3 = Arrays.asList("牡丹江分部", "潍坊分部");
	private static final List<String> QRF_NUM_0 = Arrays.asList("苏州分部", "合肥分部", "威海文登分部", "威海中信分部", "青岛红岛社区", "石家庄分部",
			"通辽分部");
	private static final List<String> QRF_NUM_1 = Arrays.asList("青岛开发区分部", "即墨二分部");
	private static final List<String> QRF_NUM_2 = Arrays.asList("青岛市南分部");

	private Integer getStoreNum(String primaryDivision, String twoDivision) {
		Integer num = 0;
		if (NMZX_DIVISION_NAME.equals(primaryDivision)) {
			if (NM_NUM_0.contains(twoDivision)) {
				num = 0;
			} else if (NM_NUM_1.contains(twoDivision)) {
				num = 1;
			} else if (NM_NUM_2.contains(twoDivision)) {
				num = 2;
			} else if (NM_NUM_3.contains(twoDivision)) {
				num = 3;
			}
		}
		if (QGR_DIVISION_NAME.equals(primaryDivision)) {
			if (QRF_NUM_0.contains(twoDivision)) {
				num = 0;
			} else if (QRF_NUM_1.contains(twoDivision)) {
				num = 1;
			} else if (QRF_NUM_2.contains(twoDivision)) {
				num = 2;
			}
		}
		if (DTHJ_DIVISION_NAME.equals(primaryDivision) || SHRJ_DIVISION_NAME.equals(primaryDivision)) {
			num = 0;
		}

		return num;
	}

	/**
	 * SellDaily 是mybatis自动生成工具生成，无法添加符合的构造函数， 此方法可以替换
	 *
	 * @param primaryDivision
	 *            一级部门
	 * @param twoDivision
	 *            二级部门
	 * @param drawOrder
	 *            绘制顺序
	 * @param storeNum
	 *            门店数量
	 * @return
	 */
	private SellDaily constructionSellDaily(String primaryDivision, String twoDivision, int drawOrder, int storeNum) {
		SellDaily record = new SellDaily();
		record.setDateStr(GetDate.getFormatDateStr());
		record.setPrimaryDivision(primaryDivision);
		record.setTwoDivision(twoDivision);
		record.setDrawOrder(drawOrder);
		record.setStoreNum(storeNum);
		record.setInvestTotalMonth(BigDecimal.ZERO);
		record.setInvestTotalPreviousMonth(BigDecimal.ZERO);
		record.setRepaymentTotalMonth(BigDecimal.ZERO);
		record.setRepaymentTotalYesterday(BigDecimal.ZERO);
		record.setNonRepaymentToday(BigDecimal.ZERO);
		record.setInvestAnnualTotalMonth(BigDecimal.ZERO);
		record.setInvestAnnualTotalPreviousMonth(BigDecimal.ZERO);
		record.setInvestTotalPreviousMonth(BigDecimal.ZERO);
		record.setInvestAnnualTotalYesterday(BigDecimal.ZERO);
		record.setInvestTotalYesterday(BigDecimal.ZERO);
		record.setNonRepaymentToday(BigDecimal.ZERO);
		record.setRechargeTotalMonth(BigDecimal.ZERO);
		record.setRechargeTotalYesterday(BigDecimal.ZERO);
		record.setNetCapitalInflowYesterday(BigDecimal.ZERO);
		record.setWithdrawTotalMonth(BigDecimal.ZERO);
		record.setWithdrawTotalYesterday(BigDecimal.ZERO);
		record.setInvestAnnularRatioGrowth("");
		record.setInvestRatioGrowth("");
		record.setWithdrawRate("");
		record.setRegisterTotalYesterday(0);
		record.setInvestGt3000MonthUserNum(0);
		record.setInvestGt3000UserNum(0);
		record.setRechargeGt3000UserNum(0);
		return record;
	}

	/**
	 * 重载方法
	 * 
	 * @param ocSellDaily
	 * @param primaryDivision
	 * @param twoDivision
	 * @param drawOrder
	 * @param storeNum
	 * @return
	 */
	private SellDaily constructionSellDaily(SellDaily ocSellDaily, String primaryDivision, String twoDivision,
                                            int drawOrder, int storeNum) {
		ocSellDaily.setDateStr(GetDate.getFormatDateStr());
		ocSellDaily.setPrimaryDivision(primaryDivision);
		ocSellDaily.setTwoDivision(twoDivision);
		ocSellDaily.setDrawOrder(drawOrder);
		ocSellDaily.setStoreNum(storeNum);
		return ocSellDaily;
	}

	/**
	 * 重载方法
	 * 
	 * @param primaryDivision
	 * @param twoDivision
	 * @return
	 */
	private SellDaily constructionSellDaily(String primaryDivision, String twoDivision) {
		return this.constructionSellDaily(primaryDivision, twoDivision, 0, 0);
	}
}
