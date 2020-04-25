package com.hyjf.web.platdatastatistics;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mongo.operationreport.dao.OperationMongDao;
import com.hyjf.mongo.operationreport.dao.OperationMongoGroupDao;
import com.hyjf.mongo.operationreport.entity.OperationMongoGroupEntity;
import com.hyjf.mongo.operationreport.entity.OperationReportEntity;
import com.hyjf.mongo.operationreport.entity.SubEntity;
import com.hyjf.mybatis.model.auto.BorrowUserStatistic;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.web.BaseController;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 平台数据统计
 *
 * @author liuyang
 */
@Controller
@RequestMapping(PlatDataStatisticsDefine.REQUEST_MAPPING)
public class PlatDataStatisticsController extends BaseController {

	@Autowired
	private PlatDataStatisticsService platDataStatisticsService;
	@Autowired
	private OperationMongoGroupDao operationMongoGroupDao;
	@Autowired
	private OperationMongDao operationMongDao;
	@Autowired
	private PlatDataStatisticsService statisticsService;
	/**
	 * 获取临时数据，之后正式接口上线将删除 累计出借 + 累计收益
	 *
	 * @return
	 */
	@RequestMapping("/getTempData")
	public JSONObject getTempData() {
		JSONObject result = new JSONObject();
		result.put("status", "000");
		result.put("statusDesc", "成功");

		DecimalFormat df = new DecimalFormat("#,##0");
		CalculateInvestInterest calculateInvestInterest = this.platDataStatisticsService
				.selectCalculateInvestInterest();
		if (calculateInvestInterest != null) {
			// 累计出借
			result.put("investTotal",
					df.format(calculateInvestInterest.getTenderSum().setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			// 累计收益
			result.put("interestTotal",
					df.format(calculateInvestInterest.getInterestSum().setScale(0, BigDecimal.ROUND_HALF_DOWN)));
		}
		return result;
	}

	/**
	 * 画面初始化
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(PlatDataStatisticsDefine.INIT_ACTION)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(PlatDataStatisticsDefine.INIT_PATH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("#,##0");
		Calendar cal=Calendar.getInstance();
		Query query = new Query();
		query.limit(1);
		query.with(new Sort(Sort.Direction.DESC, "statisticsMonth"));
		OperationReportEntity oe = operationMongDao.findOne(query);
		// 累计出借
		modelAndView.addObject("investTotal", df.format(statisticsService.selectTotalInvest().setScale(0, BigDecimal.ROUND_DOWN)));
		// 累计收益
		modelAndView.addObject("interestTotal",
				df.format(statisticsService.selectTotalInterest().setScale(0, BigDecimal.ROUND_DOWN)));
		// 累计交易笔数
		modelAndView.addObject("tenderCounts",  statisticsService.selectTotalTradeSum());
		// 累计人均出借金额
		modelAndView.addObject("perCapitaInvestment", df.format(oe.getPerInvest()));
		// 借贷余额（原借款人待还）
		modelAndView.addObject("totalRepayAwait", df.format(oe.getWillPayMoney()));
		// 借贷笔数
		modelAndView.addObject("totalRepayCounts", oe.getLoanNum());
		// 出借人总数
		modelAndView.addObject("registerCounts", oe.getTenderCount());

		query = new Query();
		query.limit(1);
		query.with(new Sort(Sort.Direction.DESC, "statisticsMonth"));
		OperationMongoGroupEntity oegroup = operationMongoGroupDao.findOne(query);
		Map<Integer, Integer> ageMap = oegroup.getInvestorAgeMap();
		int r1 = ageMap.get(OperationMongoGroupEntity.ageRange1);
		int r2 = ageMap.get(OperationMongoGroupEntity.ageRange2);
		int r3 = ageMap.get(OperationMongoGroupEntity.ageRange3);
		int r4 = ageMap.get(OperationMongoGroupEntity.ageRange4);
		int total = r1 + r2 + r3 + r4;

		// 年龄
		String ageData = "[ {value:" + r1 + ", name:'25岁以下:　　　" + oegroup.formatDate((float) r1 * 100 / total)
				+ "%　',icon:'circle',}," + "{value:" + r2 + ", name:'25至35岁:　　　"
				+ oegroup.formatDate((float) r2 * 100 / total) + "%　',icon:'circle',}," + "{value:" + r3
				+ ", name:'36至50岁:　　　" + oegroup.formatDate((float) r3 * 100 / total) + "%　' ,icon:'circle',},"
				+ "{value:" + r4 + ", name:'50岁以上:　　　" + oegroup.formatDate((float) r4 * 100 / total)
				+ "%　',icon:'circle',}]";

		List<PlatDataAgeDataBean> ageDataBeanList = new ArrayList<PlatDataAgeDataBean>();
		PlatDataAgeDataBean bean = new PlatDataAgeDataBean();
		bean.setName("25岁以下:");
		bean.setRateValue(oegroup.formatDate((float) r1 * 100 / total) + "%");
		bean.setStyleClass("background:rgb(254,216,125)");

		PlatDataAgeDataBean bean1 = new PlatDataAgeDataBean();
		bean1.setName("25至35岁:");
		bean1.setRateValue(oegroup.formatDate((float) r2 * 100 / total) + "%");
		bean1.setStyleClass("background:rgb(211,73,86)");

		PlatDataAgeDataBean bean2 = new PlatDataAgeDataBean();
		bean2.setName("36至50岁:");
		bean2.setRateValue(oegroup.formatDate((float) r3 * 100 / total) + "%");
		bean2.setStyleClass("background:rgb(22,81,122)");

		PlatDataAgeDataBean bean3 = new PlatDataAgeDataBean();
		bean3.setName("50岁以上:");
		bean3.setRateValue(oegroup.formatDate((float) r4 * 100 / total) + "%");
		bean3.setStyleClass("background:rgb(103,195,203)");
		ageDataBeanList.add(bean);
		ageDataBeanList.add(bean1);
		ageDataBeanList.add(bean2);
		ageDataBeanList.add(bean3);

		// 性别

		Map<Integer, Integer> sexMap = oegroup.getInvestorSexMap();
		int maleCount = sexMap.get(OperationMongoGroupEntity.MALE);
		int femaleCount = sexMap.get(OperationMongoGroupEntity.FEMALE);
		float malePer = (float) maleCount * 100 / (maleCount + femaleCount);
		float femalePer = (float) femaleCount * 100 / (maleCount + femaleCount);

		List<PlatDataAgeDataBean> sexDataBeanList = new ArrayList<PlatDataAgeDataBean>();
		PlatDataAgeDataBean bean4 = new PlatDataAgeDataBean();
		bean4.setName("男:");
		bean4.setRateValue(oegroup.formatDate(malePer) + "%");
		bean4.setStyleClass("background:rgb(28,90,132)");

		PlatDataAgeDataBean bean5 = new PlatDataAgeDataBean();
		bean5.setName("女:");
		bean5.setRateValue(oegroup.formatDate(femalePer) + "%");
		bean5.setStyleClass("background:rgb(211,73,86)");
		sexDataBeanList.add(bean4);
		sexDataBeanList.add(bean5);
		modelAndView.addObject("sexDataBeanList", sexDataBeanList);

		String sexData = "[ {value:" + maleCount + ", name:'男:　　　" + oegroup.formatDate(malePer)
				+ "%　',icon:'circle',}," + "{value:" + femaleCount + ", name:'女:　　　" + oegroup.formatDate(femalePer)
				+ "%　',icon:'circle',}]";
		modelAndView.addObject("sexData", sexData);
		modelAndView.addObject("ageData", ageData);

		// 区域
		query = new Query();
		query.limit(1);
		query.with(new Sort(Sort.Direction.DESC, "statisticsMonth"));
		OperationMongoGroupEntity region = operationMongoGroupDao.findOne(query);

		Map<Integer, String> cityMap = region.getInvestorRegionMap();
		List<SubEntity> list = region.orgnizeData(cityMap);
		List<SubEntity> sublist = region.formatList(list);

		StringBuffer regionData = new StringBuffer();
		regionData.append("[");
		regionData.append("{name: '北京',value: " + getRegionValue(sublist, "北京") + " },");
		regionData.append("{name: '天津',value: " + getRegionValue(sublist, "天津") + " },");
		regionData.append("{name: '上海',value: " + getRegionValue(sublist, "上海") + " },");
		regionData.append("{name: '重庆',value: " + getRegionValue(sublist, "重庆") + " },");
		regionData.append("{name: '河北',value: " + getRegionValue(sublist, "河北") + " },");
		regionData.append("{name: '河南',value: " + getRegionValue(sublist, "河南") + " },");
		regionData.append("{name: '云南',value: " + getRegionValue(sublist, "云南") + " },");
		regionData.append("{name: '辽宁',value: " + getRegionValue(sublist, "辽宁") + " },");
		regionData.append("{name: '黑龙江',value: " + getRegionValue(sublist, "黑龙江") + " },");
		regionData.append("{name: '湖南',value: " + getRegionValue(sublist, "湖南") + " },");
		regionData.append("{name: '安徽',value: " + getRegionValue(sublist, "安徽") + " },");
		regionData.append("{name: '山东',value: " + getRegionValue(sublist, "山东") + " },");
		regionData.append("{name: '新疆',value: " + getRegionValue(sublist, "新疆") + " },");
		regionData.append("{name: '江苏',value: " + getRegionValue(sublist, "江苏") + " },");
		regionData.append("{name: '浙江',value: " + getRegionValue(sublist, "浙江") + " },");
		regionData.append("{name: '江西',value: " + getRegionValue(sublist, "江西") + " },");
		regionData.append("{name: '湖北',value: " + getRegionValue(sublist, "湖北") + " },");
		regionData.append("{name: '广西',value: " + getRegionValue(sublist, "广西") + " },");
		regionData.append("{name: '甘肃',value: " + getRegionValue(sublist, "甘肃") + " },");
		regionData.append("{name: '山西',value: " + getRegionValue(sublist, "山西") + " },");
		regionData.append("{name: '内蒙古',value: " + getRegionValue(sublist, "内蒙古") + " },");
		regionData.append("{name: '陕西',value: " + getRegionValue(sublist, "陕西") + " },");
		regionData.append("{name: '吉林',value: " + getRegionValue(sublist, "吉林") + " },");
		regionData.append("{name: '福建',value: " + getRegionValue(sublist, "福建") + " },");
		regionData.append("{name: '贵州',value: " + getRegionValue(sublist, "贵州") + " },");
		regionData.append("{name: '广东',value: " + getRegionValue(sublist, "广东") + " },");
		regionData.append("{name: '青海',value: " + getRegionValue(sublist, "青海") + " },");
		regionData.append("{name: '西藏', value: " + getRegionValue(sublist, "西藏") + " },");
		regionData.append("{name: '四川',value: " + getRegionValue(sublist, "四川") + " },");
		regionData.append("{name: '宁夏',value: " + getRegionValue(sublist, "宁夏") + " },");
		regionData.append("{name: '海南',value: " + getRegionValue(sublist, "海南") + " },");
		regionData.append("{name: '台湾',value: " + getRegionValue(sublist, "台湾") + " },");
		regionData.append("{name: '香港',value: " + getRegionValue(sublist, "香港") + " },");
		regionData.append("{name: '澳门',value: " + getRegionValue(sublist, "澳门") + " },");
		regionData.append("{name: '南海诸岛',value: " + getRegionValue(sublist, "南海诸岛") + " },");
		regionData.append(" ]");

		// 城市排名
		List<PlatDataAgeDataBean> regionDataList = getTop(sublist);

		// 获取12个月的数据
		DBObject dbObject = new BasicDBObject();
		BasicDBObject fieldsObject = new BasicDBObject();
		// 指定返回的字段
		fieldsObject.put("statisticsMonth", true);
		fieldsObject.put("accountMonth", true);
		fieldsObject.put("tradeCountMonth", true);
		query = new BasicQuery(dbObject, fieldsObject);
		query.limit(12);
		query.with(new Sort(Sort.Direction.DESC, "statisticsMonth"));
		List<OperationReportEntity> slist = operationMongDao.find(query);
		// 每月交易总额
		List<String> monthlyTenderTitle = new ArrayList<String>();
		List<String> monthlyTenderData = new ArrayList<String>();
		// 每月交易笔数
		List<String> monthlyTenderCountData = new ArrayList<String>();
		for (int i = slist.size() - 1; i >= 0; i--) {
			monthlyTenderTitle.add(oe.formatnew(String.valueOf(slist.get(i).getStatisticsMonth())));
			monthlyTenderData.add(trim(slist.get(i).getAccountMonth().intValue(), 100000000));
			monthlyTenderCountData.add(trim(slist.get(i).getTradeCountMonth(), 10000));
		}

		CalculateInvestInterest calculateInvestInterest = this.platDataStatisticsService
				.selectCalculateInvestInterest();
		if (calculateInvestInterest != null) {

			// 每月交易总额
			// List<String> monthlyTenderTitle = new ArrayList<String>();
			// monthlyTenderTitle.add("'2017-02'");
			// monthlyTenderTitle.add("'2017-03'");
			// monthlyTenderTitle.add("'2017-04'");
			// monthlyTenderTitle.add("'2017-05'");
			// monthlyTenderTitle.add("'2017-06'");
			// monthlyTenderTitle.add("'2017-07'");
			// monthlyTenderTitle.add("'2017-08'");
			// monthlyTenderTitle.add("'2017-09'");
			// monthlyTenderTitle.add("'2017-10'");
			// monthlyTenderTitle.add("'2017-11'");
			// monthlyTenderTitle.add("'2017-12'");
			// monthlyTenderTitle.add("'2018-01'");
			//
			// List<String> monthlyTenderData = new ArrayList<String>();
			// ;
			// monthlyTenderData.add("4.66");
			// monthlyTenderData.add("6.30");
			// monthlyTenderData.add("6.29");
			// monthlyTenderData.add("6.73");
			// monthlyTenderData.add("7.87");
			// monthlyTenderData.add("7.74");
			// monthlyTenderData.add("8.41");
			// monthlyTenderData.add("9.98");
			// monthlyTenderData.add("6.62");
			// monthlyTenderData.add("9.13");
			// monthlyTenderData.add("9.01");
			// monthlyTenderData.add("6.50");
			//
			// // 每月交易笔数
			// List<String> monthlyTenderCountData = new ArrayList<String>();
			// monthlyTenderCountData.add("1.84");
			// monthlyTenderCountData.add("2.03");
			// monthlyTenderCountData.add("2.69");
			// monthlyTenderCountData.add("2.82");
			// monthlyTenderCountData.add("2.25");
			// monthlyTenderCountData.add("2.60");
			// monthlyTenderCountData.add("2.80");
			// monthlyTenderCountData.add("3.23");
			// monthlyTenderCountData.add("2.31");
			// monthlyTenderCountData.add("3.68");
			// monthlyTenderCountData.add("3.10");
			// monthlyTenderCountData.add("2.71");

			// modelAndView.addObject("monthlyTenderTitle", monthlyTenderTitle);
			// modelAndView.addObject("monthlyTenderData", monthlyTenderData);
			// modelAndView.addObject("monthlyTenderCountData",
			// monthlyTenderCountData);
			//
			// modelAndView.addObject("regionData", regionData);
			// modelAndView.addObject("top10RegionData", regionDataList);
			// modelAndView.addObject("ageDataBeanList", ageDataBeanList);
			// modelAndView.addObject("deadline", "2018年01月31日");
			// // 满标时间
			// modelAndView.addObject("hour", "2");
			// modelAndView.addObject("min", "18");
			// modelAndView.addObject("sec", "46");
		}
		modelAndView.addObject("monthlyTenderTitle", monthlyTenderTitle);
		modelAndView.addObject("monthlyTenderData", monthlyTenderData);
		modelAndView.addObject("monthlyTenderCountData", monthlyTenderCountData);

		modelAndView.addObject("regionData", regionData);
		modelAndView.addObject("top10RegionData", regionDataList);
		modelAndView.addObject("ageDataBeanList", ageDataBeanList);
//		modelAndView.addObject("deadline", "2018年01月31日");
		// 满标时间
		float time = oe.getFullBillTimeCurrentMonth();
		modelAndView.addObject("hour", oe.getHour(time));
		modelAndView.addObject("min", oe.getMinutes(time));
		modelAndView.addObject("sec", oe.getSeconds(time));
		
		modelAndView.addObject("deadline",transferIntToDate(oe.getStatisticsMonth()));
		
		//借款人相关数据统计：
		BorrowUserStatistic borrowUserData = platDataStatisticsService.selectBorrowUserStatistic();
		BigDecimal borrowuserMoneyTopone = BigDecimal.ZERO;
		BigDecimal borrowuserMoneyTopten = BigDecimal.ZERO;
//		if(borrowUserData.getBorrowuserMoneyTotal() != null && borrowUserData.getBorrowuserMoneyTotal().compareTo(BigDecimal.ZERO) > 0){
			//borrowuserMoneyTopone = borrowUserData.getBorrowuserMoneyTopone().divide(borrowUserData.getBorrowuserMoneyTotal(), BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
			//borrowuserMoneyTopten = borrowUserData.getBorrowuserMoneyTopten().divide(borrowUserData.getBorrowuserMoneyTotal(), BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
//			borrowuserMoneyTopone = borrowUserData.getBorrowuserMoneyTopone().divide(borrowUserData.getBorrowuserMoneyTotal(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP);
//			borrowuserMoneyTopten = borrowUserData.getBorrowuserMoneyTopten().divide(borrowUserData.getBorrowuserMoneyTotal(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP);

//		}

		modelAndView.addObject("borrowuserCountTotal", oe.getBorrowuserCountTotal());
		modelAndView.addObject("borrowuserCountCurrent", oe.getBorrowuserCountCurrent());
		modelAndView.addObject("tenderuserCountCurrent", oe.getTenderuserCountCurrent());
		modelAndView.addObject("borrowuserMoneyTopone", oe.getBorrowuserMoneyTopone().setScale(2, BigDecimal.ROUND_DOWN));
		modelAndView.addObject("borrowuserMoneyTopten", oe.getBorrowuserMoneyTopten().setScale(2, BigDecimal.ROUND_DOWN));


		return modelAndView;
	}

	public int getRegionValue(List<SubEntity> list, String cityName) {
		for (int i = 0; i < list.size(); i++) {
			SubEntity sub = list.get(i);
			if (sub.getName().contains(cityName)) {
				return sub.getValue();
			}
		}
		return 0;
	}

	public List<PlatDataAgeDataBean> getTop(List<SubEntity> list) {
		List<PlatDataAgeDataBean> regionDataList = new ArrayList<PlatDataAgeDataBean>();
		for (int i = 0; i <= 9; i++) {
			PlatDataAgeDataBean regionData = new PlatDataAgeDataBean();
			int j = i + 1;
			if (j < 10) {
				regionData.setAreaNumber("0" + j);
			} else {
				regionData.setAreaNumber(j + "");
			}
			if (list.get(i).getName().contains("新疆")) {
				regionData.setName("新疆");
			} else {
				regionData.setName(list.get(i).getName().replace("省", ""));
			}

			regionData.setRateValue(list.get(i).getPercent());
			regionDataList.add(regionData);
		}
		return regionDataList;

	}

	public String trim(float input, int fenzi) {
		return new BigDecimal((float) input / fenzi).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	public  String transferIntToDate(int date ) {
		Calendar cl=Calendar.getInstance();
		String str=String.valueOf(date);
		int year=Integer.valueOf(str.substring(0,4));
		int month=Integer.valueOf(str.substring(4,6))-1;
		
		cl.set(Calendar.YEAR,year);
		cl.set(Calendar.MONTH,month);
		int lastDay = cl.getActualMaximum(Calendar.DAY_OF_MONTH);  
        cl.set(Calendar.DAY_OF_MONTH, lastDay); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		return sdf.format(cl.getTime());
	}

}
