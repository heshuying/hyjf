/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
package com.hyjf.web.home;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.BorrowService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.AddressUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.HtmlUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.UtmVisit;
import com.hyjf.mybatis.model.auto.Version;
import com.hyjf.mybatis.model.customize.web.WebHomePageStatisticsCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.contentArticle.ContentArticleService;
import com.hyjf.web.hjhplan.HjhPlanService;
import com.hyjf.web.interceptor.InterceptorDefine;
import com.hyjf.web.plan.PlanService;
import com.hyjf.web.platdata.PlatDataService;
import com.hyjf.web.platdatastatistics.PlatDataStatisticsService;
import com.hyjf.web.util.WebUtils;

/**
 * 
 * 前台文章类数据控制器
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月11日
 * @see 上午11:10:39
 */
@Controller("homePageController")
@RequestMapping(value = HomePageDefine.REQUEST_MAPPING)
public class HomePageController extends BaseController {

	@Autowired
	private HomePageService homePageService;

	@Autowired
	private ContentArticleService contentArticleService;
	
	@Autowired
    private BorrowService borrowService;
	
	@Autowired
    private PlanService planService;

	@Autowired
	private HjhPlanService hjhPlanService;
	
	@Autowired
    private PlatDataService platDataService;

	@Autowired
	private PlatDataStatisticsService statisticsService;

	/** 平台上线时间 */
	private static final String PUT_ONLINE_TIME = "2013-12";

	/**
	 * 
	 * 计算器页面跳转
	 * 
	 * @author renxingchen
	 * @return
	 */
	@RequestMapping(value = HomePageDefine.CALC_ACTION)
	public ModelAndView calc() {
		ModelAndView modelAndView = new ModelAndView("/calc");
		return modelAndView;
	}

	/**
	 *
	 * 五周年庆宣传页
	 *
	 * @author wq
	 * @return
	 */
	@RequestMapping(value = HomePageDefine.FIVEFESTIVAL_ACTION)
	public ModelAndView fiveFestival() {
		ModelAndView modelAndView = new ModelAndView("/activity/active201808/fivefestival");
		return modelAndView;
	}
	/**
	 * 
	 * 首页面跳转
	 * 
	 * @author Michael
	 * @return
	 */
	@RequestMapping(value = HomePageDefine.HOME_ACTION)
	public ModelAndView home(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/home/home");
		// 环境类型  0测试   1预生产  99正式
		// modify by libin PC端图片应获取CDN中的图片 20190109，之前取值错误
		modelAndView.addObject("onlineType", CustomConstants.HYJF_ONLINE_TYPE);
		// modify by libin PC端图片应获取CDN中的图片 20190109
		WebViewUser wuser = WebUtils.getUser(request);
		if (wuser == null) {
			modelAndView.addObject("loginFlag", "0");//未登陆
		}else{
			modelAndView.addObject("loginFlag", "1");//已登陆
			//获取用户优惠券数量
			int couponCount = this.homePageService.getUserCouponCount(wuser.getUserId(), "0");
			modelAndView.addObject("couponCount", couponCount);
			if(wuser.isBankOpenAccount()){ //开户
				modelAndView.addObject("openFlag", "1"); //已开户
				Account account = this.homePageService.getAccount(wuser.getUserId());
				if(account != null){
					modelAndView.addObject("userInterest", account.getBankInterestSum()); //用户收益
				}else{
					modelAndView.addObject("userInterest", "0.00"); 
				}
				
				//欢迎语
				if(StringUtils.isNotEmpty(wuser.getTruename())){
					StringBuffer helloWord = new StringBuffer();
					int helloFlag = 0;
					int currentHour = GetDate.getHour(new Date()); 
					if(currentHour >= 6 && currentHour < 12){
						helloWord.append("上午好，");
						helloFlag = 0;
					}else if(currentHour >= 12 && currentHour < 18){
						helloWord.append("下午好，");
						helloFlag = 1;
					}else{
						helloWord.append("晚上好，");
						helloFlag = 2;
					}
					helloWord.append(wuser.getTruename().substring(0, 1));
					if(wuser.getSex() == 2){ //女
						helloWord.append("女士");
					}else{
						helloWord.append("先生");
					}
					modelAndView.addObject("helloWord", helloWord.toString());
					modelAndView.addObject("helloFlag", helloFlag);
				}
			}else{
				modelAndView.addObject("openFlag", "0"); //未开户
			}
		}
		//统计数据
//		CalculateInvestInterest calculateInvestInterest = this.homePageService.getTenderSum();
//		if(calculateInvestInterest != null){
			//出借总额(亿元)
			modelAndView.addObject("tenderSum", statisticsService.selectTotalInvest().divide(new BigDecimal("100000000")).setScale(0, BigDecimal.ROUND_DOWN).toString());
			//收益总额(亿元)
			modelAndView.addObject("interestSum", statisticsService.selectTotalInterest().divide(new BigDecimal("100000000")).setScale(0, BigDecimal.ROUND_DOWN).toString());
//		}else{
//			//出借总额(亿元)
//			modelAndView.addObject("tenderSum", "0.00");
//			//收益总额(亿元)
//			modelAndView.addObject("interestSum", "0.00");
//		}
		//累计上线年数
		Integer yearSum = GetDate.getYearFromDate(PUT_ONLINE_TIME);
		//上线年数
		modelAndView.addObject("yearSum", yearSum);

		List<Ads> recordList = homePageService.searchHomeAdsList(6, new Short("0"), 0, 4);
		modelAndView.addObject("bannerList", recordList);
		// 公司公告列表
		List<ContentArticle> noticeList = this.contentArticleService.searchNoticeList("2", 0, 1);
		if (noticeList != null && noticeList.size() > 0) {
			ContentArticle contentArticle = noticeList.get(0);
			modelAndView.addObject("noticeInfo", contentArticle);
		}
		/***********************网站改版首页数据接口修改  pcc  start***************************/
		//获取新手专区项目信息
		Map<String, Object> paramsNew = new HashMap<String, Object>();
		paramsNew.put("projectType", "HZT");
		paramsNew.put("borrowClass", "NEW");
		paramsNew.put("limitStart", 0);
		paramsNew.put("limitEnd", 1);
        List<WebProjectListCustomize> newProjectList = borrowService.searchProjectListNew(paramsNew);
		// add by nxl 判断是否为产品加息 start
		if(null!=newProjectList&&newProjectList.size()>0){
			WebProjectListCustomize webProjectListCustomize = newProjectList.get(0);
			int intFlg = Integer.parseInt(StringUtils.isNotBlank(webProjectListCustomize.getIncreaseInterestFlag())?webProjectListCustomize.getIncreaseInterestFlag():"0");
			BigDecimal dbYield = new BigDecimal(StringUtils.isNotBlank(webProjectListCustomize.getBorrowExtraYield())?webProjectListCustomize.getBorrowExtraYield():"0");
			boolean booleanVal = Validator.isIncrease(intFlg,dbYield);
			webProjectListCustomize.setIsIncrease(String.valueOf(booleanVal));
		}
		// add by nxl 判断是否为产品加息 end
        modelAndView.addObject("newProjectList", newProjectList);
        
        //获取债权专区项目信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projectType", "HZT");
//        paramsNew.put("borrowClass", "");
        params.put("limitStart", 0);
        params.put("limitEnd", 4);
        List<WebProjectListCustomize> projectList = borrowService.searchProjectListNew(params);
		// add by nxl 判断是否为产品加息 start
		if(null!=projectList&&projectList.size()>0){
			for(WebProjectListCustomize webProjectListCustomize:projectList){
				int intFlg = Integer.parseInt(StringUtils.isNotBlank(webProjectListCustomize.getIncreaseInterestFlag())?webProjectListCustomize.getIncreaseInterestFlag():"0");
				BigDecimal dbYield = new BigDecimal(StringUtils.isNotBlank(webProjectListCustomize.getBorrowExtraYield())?webProjectListCustomize.getBorrowExtraYield():"0");
				boolean booleanVal = Validator.isIncrease(intFlg,dbYield);
				webProjectListCustomize.setIsIncrease(String.valueOf(booleanVal));
			}
		}
		// add by nxl 判断是否为产品加息 end
        modelAndView.addObject("projectList", projectList);
        
        //获取汇计划列表
        //Map<String, Object> planParams = new HashMap<String, Object>();
        //planParams.put("limitStart", 0);
        //planParams.put("limitEnd", 4);
        //List<DebtPlanCustomize> planList = planService.searchDebtPlanList(planParams);
        //modelAndView.addObject("planList", planList);

		//获取HJH计划列表
		Map<String, Object> hjhPlanParams = new HashMap<String, Object>();
		hjhPlanParams.put("limitStart", 0);
		hjhPlanParams.put("limitEnd", 4);
		hjhPlanParams.put("isHome",1);
		//修改首页开放额度添加小数位
		List<HjhPlanCustomize> hjhPlanList = hjhPlanService.searchHjhPlanList(hjhPlanParams);
		modelAndView.addObject("hjhPlanList", hjhPlanList);

        //公司动态
        List<ContentArticle> companyDynamicsList = homePageService.searchHomeNoticeList( HomePageDefine.NOTICE_TYPE_COMPANY_DYNAMICS, 0, 4);
		if(companyDynamicsList != null && companyDynamicsList.size() > 0){
			int thisIndex = 0;
			List<ContentArticle> companyDynamicsListSon = new ArrayList<>();
			for (ContentArticle companyDynamics : companyDynamicsList) {
				++thisIndex;
				if (companyDynamics.getContent().contains("../../../..")) {
					companyDynamics.setContent(companyDynamics.getContent().replaceAll("../../../..", HomePageDefine.HOST));
				} else if (companyDynamics.getContent().contains("src=\"/")) {
					companyDynamics.setContent(companyDynamics.getContent().replaceAll("src=\"/","src=\"" + HomePageDefine.HOST)+ "//");
				}

				if (thisIndex > 1){
					companyDynamicsListSon.add(companyDynamics);
				}else {
					//去除html标签 <div 等标签
					companyDynamics.setContent(HtmlUtil.getTextFromHtml(companyDynamics.getContent()));
					// 避免页面上出现未转换的HTML特殊字符
					companyDynamics.setContent(StringEscapeUtils.unescapeHtml4(companyDynamics.getContent()));
				}
			}

			modelAndView.addObject("companyArticle", companyDynamicsList.get(0));
			modelAndView.addObject("companyDynamicsList", companyDynamicsListSon);
		} else {
			modelAndView.addObject("companyArticle", null);
			modelAndView.addObject("companyDynamicsList", null);
		}

        //系统当前时间
        modelAndView.addObject("nowTime", GetDate.getNowTime10());
        
        /***********************网站改版首页数据接口修改  pcc  end***************************/
		// 加入推广session
		if (StringUtils.isNotEmpty(request.getParameter("utm_id"))) {
			if (Validator.isNumber(request.getParameter("utm_id"))) {
				UtmVisit visit = new UtmVisit();
				String address = "";
				String ip = GetCilentIP.getIpAddr(request);
				try {
					address = new AddressUtil().getAddresses("ip=" + ip, "utf-8");
				} catch (Exception e) {
					// e.printStackTrace();
				}
				try {
					String utm_id = request.getParameter("utm_id");
					String browser = GetCilentIP.getBrowserName(request);
					visit.setUtmId(Integer.parseInt(utm_id));
					visit.setBrowser(browser);
					visit.setIp(ip);
					visit.setOs("windows");// 暂时不知道怎么获取
					visit.setLogin(0);
					visit.setCreateTime(GetDate.getMyTimeInMillis());
					if (StringUtils.isNotEmpty(address)) {
						String[] adds = address.split(",");
						if (adds.length == 3) {
							visit.setCountry(adds[0]);
							visit.setProvince(adds[1]);
							visit.setCity(adds[2]);
						} else {
							visit.setCountry("");
							visit.setProvince("");
							visit.setCity("");
						}
					} else {
						visit.setCountry("");
						visit.setProvince("");
						visit.setCity("");
					}
					homePageService.saveUtmVisit(visit);
					WebUtils.addCookie(request, response, "utm_id", utm_id, null,
							InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
		return modelAndView;
	}


	/**
	 * 用户注册初始化画面数据保存（保存到session）
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.INVEST_STATISTICS_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchInvestStatistics(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HomePageDefine.THIS_CLASS, HomePageDefine.INVEST_STATISTICS_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		/*
		 * WebHomePageStatisticsCustomize homeStatistics =
		 * homePageService.searchTotalStatistics(); String
		 * dataFlagInfoToView=RedisUtils.get("dataFlagInfoToView"); JSONObject
		 * js=JSON.parseObject(dataFlagInfoToView);
		 */
		WebHomePageStatisticsCustomize homeStatistics = new WebHomePageStatisticsCustomize();
		Map<String, Object> data = platDataService.selectData();
		DecimalFormat df = new DecimalFormat("#,##0");
		if (data != null) {
			homeStatistics.setBailTotal(df.format(new BigDecimal(data.get("bail_money") + "").setScale(0,
					BigDecimal.ROUND_HALF_DOWN)));
			homeStatistics.setTotalSum(df.format(new BigDecimal(data.get("tender_sum") + "").setScale(0,
					BigDecimal.ROUND_HALF_DOWN)));
			homeStatistics.setTotalInterest(df.format(new BigDecimal(data.get("interest_sum") + "").setScale(0,
					BigDecimal.ROUND_HALF_DOWN)));
		}

		info.put("homeStatistics", homeStatistics);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, info);
		ret.put(CustomConstants.MSG, "");
		LogUtil.endLog(HomePageDefine.THIS_CLASS, HomePageDefine.INVEST_STATISTICS_ACTION);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 
	 * 信息系统安全等级保护备案证明
	 * @author pcc
	 * @param request
	 * @return
	 */
    @RequestMapping(value = HomePageDefine.SYSTEM_SAFETY_LEVEL_PROTECTION_RECORD_INIT)
    public ModelAndView systemSafetyLevelProtectionRecordInit(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(HomePageDefine.SYSTEM_SAFETY_LEVEL_PROTECTION_RECORD_PATH);
        return modelAndView;
    }
    
	/**
	 * 查询最新更新地址(下载APP链接)
	 * new added by libin
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.APP_DL_LASTEST_URL)
	public void searchLastestUrl(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(HomePageDefine.THIS_CLASS, HomePageDefine.APP_DL_LASTEST_URL);
		String url = "";
		Version version = this.homePageService.getVersion();
		if( version.getUrl() != null ){
			url = version.getUrl().trim();
		}
		LogUtil.endLog(HomePageDefine.THIS_CLASS, HomePageDefine.APP_DL_LASTEST_URL);
		try {
			response.sendRedirect(url);
		} catch (IOException e) {		
			e.printStackTrace();
		}
	}
}
