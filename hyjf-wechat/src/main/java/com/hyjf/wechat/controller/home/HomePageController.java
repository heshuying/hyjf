/**
 * 首页控制器
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.wechat.controller.home;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.ContentQualify;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.app.AppAdsCustomize;
import com.hyjf.mybatis.model.customize.app.AppModuleCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebHomePageStatisticsCustomize;
import com.hyjf.mybatis.model.customize.wechat.WechatHomeProjectListCustomize;
import com.hyjf.wechat.BaseResultBeanFrontEnd;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.controller.hjh.WxHjhPlanService;
import com.hyjf.wechat.controller.project.ProjectDefine;
import com.hyjf.wechat.service.find.operationalData.PlatDataStatisticsService;
import com.hyjf.wechat.service.home.HomePageService;
import com.hyjf.wechat.util.ResultEnum;

/**
 * 微信首页controller
 *
 * @author jijun
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月27日
 * @see 上午09：12
 */
@Controller
@RequestMapping(value = HomePageDefine.REQUEST_MAPPING)
public class HomePageController extends BaseController {

	@Autowired
	private HomePageService homePageService;

	@Autowired
	private WxHjhPlanService hjhPlanService;

	@Autowired
	private PlatDataStatisticsService platDataStatisticsService;

	/** 图片地址 */
	private static final String HOST_URL = PropUtils.getSystem("file.domain.url");
	/** 发布地址 */
	private static final String HOST = PropUtils.getSystem("hyjf.web.host");

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

	/**
	 * 根据参数数据获取相应的首页数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.INDEX_DATA_ACTION, method = RequestMethod.GET)
	public BaseResultBean searchProjectList(HttpServletRequest request, HttpServletResponse response) {
		HomePageResultVo vo = new HomePageResultVo();
		vo = getHomePage(request, vo);

		return vo;
	}

	/**
	 * 异步获取标的列表projectList add by jijun 20180412
	 * 微信首页各种标(新手，智投，散标..)
	 * @param request
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.GET_HOME_PROJECT_LIST_ACTION, method = RequestMethod.GET)
	public  BaseResultBean getHomeProjectList(HttpServletRequest request,
											  @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
											  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
											  @RequestParam(value = "showPlanFlag") String showPlanFlag) {
		if(currentPage<=0){
			currentPage=1;
		}
		if(pageSize<=0){
			pageSize=10;
		}
		HomePageResultVo vo = new HomePageResultVo();
		vo.setCurrentPage(currentPage);
		vo.setPageSize(pageSize);
		vo=homePageService.getProjectListAsyn(vo,currentPage,pageSize,showPlanFlag);//加缓存 异步获取标的列表

		if(currentPage==1){
			//获取用户id
			Integer userId = requestUtil.getRequestUserId(request);
			//判断用户是否登录
			if (userId == null||userId <= 0  ) {
				//获取新手标
				vo.setHomeXshProjectList(this.createProjectNewPage(userId, HOST));
			} else {
				//查询用户是否开户
				Integer userType = homePageService.getUserTypeByUserId(userId);
				if (userType == 0) {//未开户
					//获取新手标
					vo.setHomeXshProjectList(this.createProjectNewPage(userId, HOST));
				} else if (userType == 1) {//已开户
					//获取用户累计出借条数
					Integer count = homePageService.selectInvestCount(userId);
					if (count == null || count <= 0) {
						//获取新手标
						vo.setHomeXshProjectList(this.createProjectNewPage(userId, HOST));
					}
				}
			}
		}


		return vo;
	}

	/**
	 *
	 * 获取新手标数据
	 * @author hsy
	 * @param info
	 */
	private List<WechatHomeProjectListCustomize> createProjectNewPage(Integer userId, String HOST) {
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		List<WechatHomeProjectListCustomize> list= new ArrayList<WechatHomeProjectListCustomize>();
		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("host", HOST + HomePageDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING
				+ ProjectDefine.PROJECT_DETAIL_ACTION);
		projectMap.put("userId", userId);
		List<AppProjectListCustomize> projectList = homePageService.searchProjectNewList(projectMap);//加缓存
		boolean isNewUser = homePageService.checkNewUser(userId);
		if(isNewUser){
			if(projectList != null && !projectList.isEmpty()&&projectList.size()!=0){
				if(list.size()==0&&projectList.size()!=0){
					AppProjectListCustomize project=projectList.get(0);
					WechatHomeProjectListCustomize customize=new WechatHomeProjectListCustomize();
					customize.setBorrowNid(project.getBorrowNid());
					customize.setBorrowName(project.getBorrowName());
					customize.setBorrowType(project.getBorrowType());
					customize.setBorrowApr(project.getBorrowApr());
					customize.setBorrowPeriod(project.getBorrowPeriodInt()+"");
					customize.setBorrowPeriodType(project.getBorrowPeriodType());
					customize.setBorrowExtraYield(project.getBorrowExtraYield());
					if("0".equals(project.getOnTime())||"".equals(project.getOnTime())){
						switch (project.getStatus()) {
							case "10":
								customize.setOnTime(project.getOnTime());
								break;
							case "11":
								customize.setOnTime("立即出借");
								break;
							case "12":
								customize.setOnTime("复审中");
								break;
							case "13":
								customize.setOnTime("还款中");
								break;
							case "14":
								customize.setOnTime("已退出");
								break;
						}

					}else{
						customize.setOnTime(project.getOnTime());
					}
					customize.setStatus(project.getStatus());
					customize.setAccountWait(df.format(new BigDecimal(project.getBorrowAccountWait())));
					list.add(customize);
				}
			}
		}
		return list;
	}



	/**
	 * 获取首页数据
	 * @param request
	 * @return
	 */
	private HomePageResultVo getHomePage(HttpServletRequest request, HomePageResultVo vo) {

		LogUtil.startLog(HomePageDefine.THIS_CLASS, HomePageDefine.INDEX_DATA_ACTION);
		// sign 就是 userId

		// 获取用户id
		Integer userId = requestUtil.getRequestUserId(request);
		vo.setWarning("市场有风险 出借需谨慎");

		// 判断用户是否登录
		if (userId == null || userId <= 0) {
			vo.setTotalAssets("0.00");
			vo.setAvailableBalance("0.00");
			vo.setAccumulatedEarnings("0.00");
			// 创建首页广告
			String type = "0"; // 未注册
			// 首页顶端轮播图
			createAdPic(vo, type);
			vo.setAdDesc("立即注册");

			// 获取首页项目列表
			//this.createProjectListPage(vo, list);
		} else {
			// 查询用户是否开户
			Integer userType = homePageService.getUserTypeByUserId(userId);
			Users loginUser = homePageService.getUsers(userId);
			vo.setIsOpenAccount(loginUser.getBankOpenAccount());
			vo.setIsSetPassword(loginUser.getIsSetPassword());
			vo.setIsCheckUserRole(Boolean.parseBoolean(PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN)));
			
			vo.setPaymentAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
			vo.setInvesAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH).getEnabledStatus());
			vo.setCreditAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_CREDIT_AUTH).getEnabledStatus());
			UsersInfo usersInfo=homePageService.getUsersInfoByUserId(userId);
			vo.setRoleId(usersInfo.getRoleId());
//			vo.setIsEvaluationFlag(loginUser.getIsEvaluationFlag());
			try {
				if(loginUser.getIsEvaluationFlag()==1 && null != loginUser.getEvaluationExpiredTime()){
					//测评到期日
					Long lCreate = loginUser.getEvaluationExpiredTime().getTime();
					//当前日期
					Long lNow = System.currentTimeMillis();
					if (lCreate <= lNow) {
						//已过期需要重新评测
						vo.setIsEvaluationFlag(2);
					} else {
						//未到一年有效期
						vo.setIsEvaluationFlag(1);
					}
				}else{
					vo.setIsEvaluationFlag(0);
				}
				// modify by liuyang 20180411 用户是否完成风险测评标识 end
			} catch (Exception e) {
				logger.error("查询用户是否完成风险测评标识出错....", e);
				vo.setIsEvaluationFlag(0);
			}
			vo.setPaymentAuthStatus(loginUser.getPaymentAuthStatus());//是否缴费授权
			vo.setUserStatus(loginUser.getStatus());
			HjhUserAuth hjhUserAuth = hjhPlanService.getHjhUserAuthByUserId(userId);
			if(hjhUserAuth!=null){
				//自动投标授权状态
				vo.setAutoInvesStatus(hjhUserAuth.getAutoInvesStatus());
				//自动债转授权状态
				vo.setAutoCreditStatus(hjhUserAuth.getAutoCreditStatus());
				//缴费授权状态
				vo.setPaymentAuthStatus(hjhUserAuth.getAutoPaymentStatus());
			}else{
				//自动投标授权状态
				vo.setAutoInvesStatus(0);
				//自动债转授权状态
				vo.setAutoCreditStatus(0);
				//缴费授权状态
				vo.setPaymentAuthStatus(0);
			}

			if (userType == 0) {// 未开户
				vo.setTotalAssets("0.00");
				vo.setAvailableBalance("0.00");
				vo.setAccumulatedEarnings("0.00");
				String type = "1";// 未开户
				createAdPic(vo, type);
				vo.setAdDesc("立即开户");
				// 获取首页项目列表
				//this.createProjectListPage(vo, list);
			} else if (userType == 1) {// 已开户
				// 获取首页项目列表
				//this.createProjectListPage(vo, list);

				// 获取资产总额
				String totalAssets = homePageService.getTotalAssets(userId);
				vo.setTotalAssets(
						StringUtils.isBlank(totalAssets) ? "0.00" : DF_FOR_VIEW.format(new BigDecimal(totalAssets)));
				// 获取可用余额
				String availableBalance = homePageService.getaVailableBalance(userId);
				vo.setAvailableBalance(StringUtils.isBlank(availableBalance) ? "0.00"
						: DF_FOR_VIEW.format(new BigDecimal(availableBalance)));
				// 获取累计收益
				String accumulatedEarnings = homePageService.getaccumulatedEarnings(userId);
				vo.setAccumulatedEarnings(StringUtils.isBlank(accumulatedEarnings) ? "0.00" : accumulatedEarnings);

			}
		}

		// 获取累计出借金额
		// String totalInvestmentAmount = homePageService.getTotalInvestmentAmount(userId);
		vo.setTotalInvestmentAmount(DF_FOR_VIEW.format(platDataStatisticsService.selectTotalInvest()));
		vo.setModuleTotal("4");

		List<AppModuleCustomize> moduleList = new ArrayList<>();

		// 添加首页大栏目
		// 微信安全保障
		this.createModule(moduleList, "wechat_module1");
		// 微信信息披露
		this.createModule(moduleList, "wechat_module4");
		// 微信运营数据
		this.createModule(moduleList, "wechat_module2");
		// 微信关于我们
		this.createModule(moduleList, "wechat_module3");

		vo.setModuleList(moduleList);

		// 添加顶部活动图片总数和顶部活动图片数组
		this.createBannerPage(vo);

		vo.setStatus(ResultEnum.SUCCESS.getStatus());
		vo.setStatusDesc(ResultEnum.SUCCESS.getStatusDesc());
		vo.setRequest(
				HomePageDefine.REQUEST_HOME + HomePageDefine.REQUEST_MAPPING + HomePageDefine.INDEX_DATA_ACTION);

		CommonUtils.convertNullToEmptyString(vo);
		LogUtil.endLog(HomePageDefine.THIS_CLASS, HomePageDefine.INDEX_DATA_ACTION);
		return vo;
	}

	/**
	 * 创建首页module
	 *
	 * @param info
	 * @param platform
	 * @param type
	 */
	private void createModule(List<AppModuleCustomize> moduleList, String module) {
		Map<String, Object> ads = new HashMap<String, Object>();
		ads.put("limitStart", HomePageDefine.BANNER_SIZE_LIMIT_START);
		ads.put("limitEnd", HomePageDefine.BANNER_SIZE_LIMIT_END);
		ads.put("host", HOST);
		ads.put("code", module);
		ads.put("platformType", "3");
		List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存 首页banner
		if (picList != null && picList.size() > 0) {
			AppModuleCustomize appModule = new AppModuleCustomize();
			appModule.setModuleUrl(picList.get(0).getImage()==null?"":picList.get(0).getImage());
			appModule.setModuleH5Url(picList.get(0).getUrl()==null?"":picList.get(0).getUrl());
			appModule.setModuleTitle(picList.get(0).getBannerName()==null?"":picList.get(0).getBannerName());
			moduleList.add(appModule);
		} else {
			AppModuleCustomize appModule = new AppModuleCustomize();
			appModule.setModuleUrl("");
			appModule.setModuleH5Url("");
			appModule.setModuleTitle("");
			moduleList.add(appModule);
		}

	}

	/**
	 * 创建首页广告
	 */
	private void createAdPic(HomePageResultVo vo, String type) {
		Map<String, Object> ads = new HashMap<String, Object>();
		ads.put("limitStart", HomePageDefine.BANNER_SIZE_LIMIT_START);
		ads.put("limitEnd", HomePageDefine.BANNER_SIZE_LIMIT_END);
		ads.put("host", HOST);
		String code = "";

		if (type.equals("0")) {// 未注册
			code = "wechat_regist_888";
		} else if (type.equals("1")) {
			code = "wechat_open_888";
		}

		ads.put("code", code);
		ads.put("platformType", "3");
		List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存 首页banner
		if (picList != null && picList.size() > 0) {
			vo.setAdPicUrl(picList.get(0).getImage());
			vo.setAdClickPicUrl(picList.get(0).getUrl());
		} else {
			vo.setAdPicUrl("");
			vo.setAdClickPicUrl("");
		}
	}

	/**
	 * 查询首页banner图
	 *
	 * @param info
	 */
	private void createBannerPage(HomePageResultVo vo) {
		Map<String, Object> ads = new HashMap<String, Object>();
		ads.put("limitStart", HomePageDefine.BANNER_SIZE_LIMIT_START);
		ads.put("limitEnd", HomePageDefine.BANNER_SIZE_LIMIT_END);
		//去掉host前缀
		ads.put("host", "");
		String code = "wechat_banner";
		ads.put("code", code);
		ads.put("platformType", "3");
		List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存 首页banner
		if (picList != null && picList.size() > 0) {
			for (AppAdsCustomize appAdsCustomize : picList) {
				appAdsCustomize.setPicUrl(appAdsCustomize.getImage());
				appAdsCustomize.setPicH5Url(appAdsCustomize.getUrl());
			}
			vo.setPicList(picList);
			vo.setPicTotal(String.valueOf(picList.size()));
		} else {
			vo.setPicList(new ArrayList<AppAdsCustomize>());
			vo.setPicTotal("0");
		}
	}
	/**
	 * 获取首页统计数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.TOTALSTATICS_ACTION)
	public JSONObject totalStaticsAction(HttpServletRequest request, HttpServletResponse response) {
		JSONObject map = new JSONObject();
		map.put("request", HOST_URL + HomePageDefine.REQUEST_MAPPING + HomePageDefine.TOTALSTATICS_ACTION);
		map.put("status", "0");
		map.put("statusDesc", "请求成功");
		// 检查参数this.homePageService.searchTotalStatistics();
		WebHomePageStatisticsCustomize homePageStatisticsCustomize = new WebHomePageStatisticsCustomize();
		Map<String, Object> data = homePageService.selectData();
		DecimalFormat df = new DecimalFormat("#,##0");
		if (data != null) {
			homePageStatisticsCustomize.setBailTotal(
					df.format(new BigDecimal(data.get("bail_money") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			homePageStatisticsCustomize.setTotalSum(
					df.format(new BigDecimal(data.get("tender_sum") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			homePageStatisticsCustomize.setTotalInterest(
					df.format(new BigDecimal(data.get("interest_sum") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
		}
		// 系统累计出借金额
		if (homePageStatisticsCustomize.getTotalSum() != null) {
			map.put("totalSum", DF_FOR_VIEW.format(new BigDecimal(homePageStatisticsCustomize.getTotalSum())));
		} else {
			map.put("totalSum", "0");
		}
		// 风险保证金
		if (homePageStatisticsCustomize.getBailTotal() != null) {
			map.put("bailTotal", homePageStatisticsCustomize.getBailTotal());
		} else {
			map.put("bailTotal", "0");
		}
		// 累计创造收益
		if (homePageStatisticsCustomize.getTotalInterest() != null) {
			map.put("totalInterest", homePageStatisticsCustomize.getTotalInterest());
		} else {
			map.put("totalInterest", "0");
		}
		return map;
	}

	/**
	 * 获取起始页banner
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.START_PAGE_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject getStartPage(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(HomePageDefine.THIS_CLASS, HomePageDefine.START_PAGE_ACTION);
		JSONObject result = new JSONObject();
		result.put(CustomConstants.APP_REQUEST,
				HomePageDefine.REQUEST_HOME + HomePageDefine.REQUEST_MAPPING + HomePageDefine.START_PAGE_ACTION);
		try {
			Map<String, Object> ads = new HashMap<String, Object>();
			ads.put("limitStart", 0);
			ads.put("limitEnd", 1);
			ads.put("host", HOST_URL);
			ads.put("code", "startpage");
			ads.put("platformType","3");
			List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存 首页banner
			if (picList == null || picList.size() == 0) {
				result.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
				result.put(CustomConstants.APP_STATUS_DESC, "获取banner失败,暂无数据");
				return result;
			}
			result.put("picUrl", picList.get(0).getImage());
			if (StringUtils.isNotEmpty(picList.get(0).getUrl())) {
				result.put("actionUrl", picList.get(0).getUrl());
			} else {
				result.put("actionUrl", "");
			}
			result.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
			result.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			result.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			result.put(CustomConstants.APP_STATUS_DESC, "获取banner出现异常");
			return result;
		}
		LogUtil.endLog(HomePageDefine.THIS_CLASS, HomePageDefine.START_PAGE_ACTION);
		return result;
	}

	/**
	 * 关于我们——资质文件
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.CONTENT_QUALIFY_ACTION)
	public JSONObject getCompanyQualify(HttpServletRequest request, HttpServletResponse response) {
		JSONObject map = new JSONObject();
		map.put("request", HOST_URL + HomePageDefine.REQUEST_MAPPING + HomePageDefine.CONTENT_QUALIFY_ACTION);
		map.put("status", "0");
		map.put("statusDesc", "请求成功");
		// 资质文件数据
		List<ContentQualify> list = this.homePageService.getCompanyQualify();
		map.put("contentQualifyList", list);
		// 文件地址
		map.put("host", HOST_URL);
		return map;
	}

	/**
	 * 关于我们——平台数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.PLATFORM_DATA_ACTION)
	public JSONObject getPlatformData(HttpServletRequest request, HttpServletResponse response) {
		JSONObject js = new JSONObject();
		js.put("request", HOST_URL + HomePageDefine.REQUEST_MAPPING + HomePageDefine.PLATFORM_DATA_ACTION);
		js.put("status", "0");
		js.put("statusDesc", "请求成功");
		// 平台数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("#,##0");
		Map<String, Object> data = homePageService.selectData();
		if (data != null) {
			js.put("tenderMoney", df.format(new BigDecimal(data.get("tender_sum") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			js.put("recoverInterest", df.format(new BigDecimal(data.get("interest_sum") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			js.put("tenderMoney7", df.format(new BigDecimal(data.get("seven_day_tender_sum") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			js.put("recoverInterest7", df.format(new BigDecimal(data.get("seven_day_interest_sum") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			// 融资期限分布
			Map<String, Object> periodInfo = new HashMap<String, Object>();
			// 出借金额占比
			Map<String, Object> TendMoneyInfo = new HashMap<String, Object>();
			js.put("newdate", sdf.format(data.get("update_time")));
			periodInfo.put("zeroone", data.get("borrow_zero_one"));
			periodInfo.put("onethree", data.get("borrow_one_three"));
			periodInfo.put("threesex", data.get("borrow_three_six"));
			periodInfo.put("sextw", data.get("borrow_six_twelve"));
			periodInfo.put("tw", data.get("borrow_twelve_up"));

			TendMoneyInfo.put("zeroone", data.get("invest_one_down"));
			TendMoneyInfo.put("onefive", data.get("invest_one_five"));
			TendMoneyInfo.put("fiveten", data.get("invest_five_ten"));
			TendMoneyInfo.put("tenfive", data.get("invest_ten_fifth"));
			TendMoneyInfo.put("five", data.get("invest_fifth_up"));
			js.put("periodInfo", periodInfo);
			js.put("TendMoneyInfo", TendMoneyInfo);
		}
		List<Map<String, Object>> tenderlist = homePageService.selectTenderListMap(0);
		List<Map<String, Object>> creditlist = homePageService.selectTenderListMap(1);
		List<Map<String, Object>> htjlist = homePageService.selectTenderListMap(2);
		// 开始先把日期都放集合里 然后排序，
		// 然后 循环对比集合里是否有此日期的数据 么有的话放0，有的话放入
		// 最后生成两个一样大小的有序集合
		List<String> timelist = new ArrayList<String>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.addAll(tenderlist);
		list.addAll(creditlist);
		list.addAll(htjlist);
		for (int i = 0; i < list.size(); i++) {
			timelist.add(list.get(i).get("time") + "");
		}
		HashSet<String> h = new HashSet<String>(timelist);
		timelist.clear();
		timelist.addAll(h);
		Collections.sort(timelist);
		List<Map<String, Object>> newtenderlist = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> newcreditlist = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> newhtjlist = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (int j = 0; j < tenderlist.size(); j++) {

			for (; i < timelist.size(); i++) {
				if (timelist.get(i).compareTo(tenderlist.get(j).get("time") + "") != 0) {
					Map<String, Object> tmpmap = new HashMap<String, Object>();
					tmpmap.put("money", "0");
					tmpmap.put("time", timelist.get(i));
					newtenderlist.add(tmpmap);
				} else {
					newtenderlist.add(tenderlist.get(j));
					if (j != (tenderlist.size() - 1)) {
						i++;
						break;
					}

				}
			}
		}
		i = 0;
		for (int j = 0; j < creditlist.size(); j++) {
			for (; i < timelist.size(); i++) {
				if (timelist.get(i).compareTo(creditlist.get(j).get("time") + "") != 0) {
					Map<String, Object> tmpmap = new HashMap<String, Object>();
					tmpmap.put("money", "0");
					tmpmap.put("time", timelist.get(i));
					newcreditlist.add(tmpmap);
				} else {
					newcreditlist.add(creditlist.get(j));
					if (j != (creditlist.size() - 1)) {
						i++;
						break;
					}
				}

			}
		}
		i = 0;
		for (int j = 0; j < htjlist.size(); j++) {
			for (; i < timelist.size(); i++) {
				if (timelist.get(i).compareTo(htjlist.get(j).get("time") + "") != 0) {
					Map<String, Object> tmpmap = new HashMap<String, Object>();
					tmpmap.put("money", "0");
					tmpmap.put("time", timelist.get(i));
					newhtjlist.add(tmpmap);
				} else {
					newhtjlist.add(htjlist.get(j));
					if (j != (htjlist.size() - 1)) {
						i++;
						break;
					}
				}

			}
		}
		// 为前台报表js不能写循环？ 制造数据
		for (int j = 0; j < timelist.size(); j++) {
			timelist.set(j, timelist.get(j));
		}
		List<String> tendermoneylist = new ArrayList<String>();
		List<String> creditmoneylist = new ArrayList<String>();
		List<String> htjmoneylist = new ArrayList<String>();
		for (int j = 0; j < newtenderlist.size(); j++) {
			tendermoneylist.add(newtenderlist.get(j).get("money") + "");
			if (newcreditlist != null && newcreditlist.size() > 0) {
				creditmoneylist.add(newcreditlist.get(j).get("money") + "");
			}
			if (newhtjlist != null && newhtjlist.size() > 0) {
				htjmoneylist.add(newhtjlist.get(j).get("money") + "");
			}
		}
		js.put("tendermoneylist", tendermoneylist);
		js.put("creditmoneylist", creditmoneylist);
		js.put("htjmoneylist", htjmoneylist);
		js.put("timelist", timelist);
		js.put("timeSize", timelist.size());

		return js;
	}

	/**
	 * 客服电话
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.SERVICE_PHONE_NUMBER_ACTION)
	public JSONObject getServicePhoneNumber(HttpServletRequest request, HttpServletResponse response) {
		JSONObject map = new JSONObject();
		map.put("request", HOST_URL + HomePageDefine.REQUEST_MAPPING + HomePageDefine.SERVICE_PHONE_NUMBER_ACTION);
		map.put("status", "0");
		map.put("statusDesc", "请求成功");
		// 400客服电话
		String phoneNumber = this.homePageService.getServicePhoneNumber();
		map.put("servicePhoneNumber", phoneNumber);
		return map;
	}

	/**
	 * 获取JumpCommend
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.GET_JUMP_COMMEND_ACTION)
	public BaseResultBeanFrontEnd getJumpCommend(HttpServletRequest request, HttpServletResponse response) {
		BaseResultBeanFrontEnd baseResultBeanFrontEnd = new BaseResultBeanFrontEnd();
		baseResultBeanFrontEnd.setStatus(BaseResultBeanFrontEnd.SUCCESS);
		baseResultBeanFrontEnd.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
		return baseResultBeanFrontEnd;
	}

}
