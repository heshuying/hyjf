/**
 * 首页控制器
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.app.home;

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

import com.hyjf.mybatis.model.customize.EvalationCustomize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.hjhplan.HjhPlanDefine;
import com.hyjf.app.project.ProjectDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AppPushManage;
import com.hyjf.mybatis.model.auto.ContentQualify;
import com.hyjf.mybatis.model.customize.app.AppAdsCustomize;
import com.hyjf.mybatis.model.customize.app.AppBorrowImageCustomize;
import com.hyjf.mybatis.model.customize.app.AppHomePageCustomize;
import com.hyjf.mybatis.model.customize.app.AppHomePageRecommendProject;
import com.hyjf.mybatis.model.customize.app.AppModuleCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.web.WebHomePageStatisticsCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;

@Controller
@RequestMapping(value = HomePageDefine.REQUEST_MAPPING)
public class HomePageController extends BaseController {

	@Autowired
	private HomePageService homePageService;

//	@Autowired
//	private OperationReportCustomizeMapper operationReportCustomizeMapper;

	/** 图片地址 */
	private static final String HOST_URL = PropUtils.getSystem("file.domain.url");
	/** 发布地址 */
	/* 版本强更临时处理
    private static final String HOST = PropUtils.getSystem("hyjf.web.host");
    */
	/** 首页module地址 */
	private static final String MODULE_URL = HomePageDefine.MODULE_URL;
	/** 首页汇计划显示条数*/
	private final int INDEX_HJH_SHOW_SIZE = 1;

	/** 首页汇计划显示条数*/
	private final int INDEX_HJH_EXTENSION_SHOW_SIZE = 3;

	private static final String H5_URL = "https://www.baidu.com/";

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

	Logger _log = LoggerFactory.getLogger(HomePageController.class);
	/**
	 * 根据参数数据获取相应的首页数据
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.PROJECT_LIST_ACTION, method = RequestMethod.POST ,produces = "application/json; charset=utf-8")
	public JSONObject searchProjectList(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HomePageDefine.THIS_CLASS, HomePageDefine.PROJECT_LIST_ACTION);
		JSONObject info = new JSONObject();
		String platform = request.getParameter("realPlatform");
		String sign = request.getParameter("sign");
		String version = request.getParameter("version");
		String uniqueIdentifier = request.getParameter("UniqueIdentifier");
		String token = request.getParameter("token");
		/*boolean isNeedUPdate = UpdateNoticeUtils.checkForAppUpdate(version, "此版本暂不可用，请更新至最新版本。", HomePageDefine.REQUEST_HOME + HomePageDefine.REQUEST_MAPPING + HomePageDefine.PROJECT_LIST_ACTION, info);
        if(isNeedUPdate){
            return info;
        }
*/
		_log.info("首页----------sign:" + sign + ", version:" + version + ", token:" +token);
		if (StringUtils.isBlank(platform)) {
			platform = request.getParameter("platform");
		}
		String HOST = "";
		//新旧版本HOST获取
        if ("3.0.5".compareTo(version) <= 0) {
        	//logger.info("-----------------新版app请求地址获取Https------------------------");
        	HOST = PropUtils.getSystem("hyjf.web.host");
        } else {
        	HOST = PropUtils.getSystem("hyjf.web.host.old");
        }
        _log.info("首页-------------HOST:" + HOST);
        //首页展示的项目集合
        List<AppProjectListCustomize> list = new ArrayList<>();
		boolean compareToVersion = compareToVersion(version, "3.0.8");//判断是否为 3.0.8以上版本
        //获取用户id
	    Integer userId = SecretUtil.getUserIdNoException(sign);
		info.put("warning", "市场有风险 出借需谨慎");

		// 合规自查 - 注册协议及隐私政策弹窗 add by huanghui 20181123 start
//		String randomString = request.getParameter("randomString");
		String privacyAgreementContent = "【审慎阅读】在您注册成为汇盈金服用户的过程中，您需要完成我们的注册流程，并通过点击同意的形式在线签署以下的协议，请您务必仔细阅读、充分理解协议中的条款内容后，再点击同意：\n" +
				"《注册协议》\n" +
				"《平台隐私条款》\n" +
				"\n" +
				"【请您注意】如果您不同意上述协议或者其中任何条款约定，请您停止注册。如您按照注册流程提示填写信息、阅读点击并同意上述协议且完成全部注册流程后，即表示您已充分阅读、理解并接受协议的全部内容，并表明您也同意，汇盈金服可以依据以上的隐私政策内容来处理您的个人信息。\n" +
				"如您对以上协议内容有任何疑问，您可随时与汇盈金服联系，在线客服电话：400-900-7878";
		info.put("registerProtocolsDesc", privacyAgreementContent);

		// 拼接 协议 Url
		String urlSplice = "platform=" + platform + "&sign=" + sign + "&version=" + version;
		// 平台注册协议
		Map<String, Object> registrationAgreement = new HashMap<>();
		// 平台隐私条款
		Map<String, Object> registrationAgreement2 = new HashMap<>();

		// 协议数组
		List<Object> arrList = new ArrayList<>();
		registrationAgreement.put("name", "《注册协议》");
		registrationAgreement.put("url", HOST + "/agreement/RegisterRuleAgreement?" + urlSplice);
		registrationAgreement2.put("name", "《平台隐私条款》");
		registrationAgreement2.put("url", HOST + "/agreement/privacyClauseAgreement?" + urlSplice);
		arrList.add(registrationAgreement);
		arrList.add(registrationAgreement2);
		info.put("registerProtocols", arrList);
		// 合规自查 - 注册协议及隐私政策弹窗 add by huanghui 20181123 end

		//判断用户是否登录
		if (userId == null||userId <= 0 || token == null || token == "" ) {
			info.put("userType", "0");
			info.put("totalAssets", "0.00");
			info.put("availableBalance", "0.00");
			info.put("accumulatedEarnings", "0.00");
			info.put("coupons", "0");
			/*if (platform.equals("2")){

            } else if (platform.equals("3")) {

            }
			info.put("adPicUrl", "");
            info.put("adClickPicUrl", "");*/
			//创建首页广告
			String type = "0";  //未注册
			createAdPic(info, platform, type, HOST);
			info.put("adDesc", "立即注册");
			//获取新手标
			this.createProjectNewPage(info , list, HOST);

            //获取首页项目列表
            /*this.createProjectListPage(info, version, list, HOST);*/
			if(compareToVersion){
				this.creatNoSignProjectListPage(info,list,HOST);
			}else{
				this.createHjhExtensionProjectListPage(info, list, HOST,version);
			}

        } else {
            //查询用户是否开户
            Integer userType = homePageService.getUserTypeByUserId(userId);
            if (userType == 0) {//未开户
				info.put("userType", "1");
				info.put("totalAssets", "0.00");
				info.put("availableBalance", "0.00");
				info.put("accumulatedEarnings", "0.00");
				info.put("coupons", "0");
				String type = "1";//未开户
				createAdPic(info, platform, type, HOST);
//				info.put("adPicUrl", "");
//				info.put("adClickPicUrl", "");
				info.put("adDesc", "立即开户");

                //获取新手标
                this.createProjectNewPage(info, list, HOST);

                //获取首页项目列表
                /*this.createProjectListPage(info, version, list, HOST);*/
				if(compareToVersion){
					this.creatNoSignProjectListPage(info,list,HOST);
				}else{
					this.createHjhExtensionProjectListPage(info, list, HOST,version);
				}
            } else if (userType == 1) {//已开户
				info.put("userType", "2");

				boolean isnew =false;
				//获取用户累计出借条数
                Integer count = homePageService.selectInvestCount(userId);
                if (count <= 0 || count == null) {
                    //获取新手标
                    this.createProjectNewPage(info, list, HOST);
                    isnew = true;
                }

                //获取首页项目列表
                /*this.createProjectListPage(info, version, list, HOST);*/

				if(compareToVersion){
					if(isnew){
						this.creatNoSignProjectListPage(info,list,HOST);
					}else{
						this.createHjhExtensionProjectListPage_new(info, list, HOST,version);
					}
				}else{
					this.createHjhExtensionProjectListPage(info, list, HOST,version);
				}
                //获取资产总额
                String totalAssets = homePageService.getTotalAssets(userId);
                info.put("totalAssets", StringUtils.isBlank(totalAssets)?"0.00":DF_FOR_VIEW.format(new BigDecimal(totalAssets)));
                //获取可用余额
                String availableBalance = homePageService.getaVailableBalance(userId);
                info.put("availableBalance",StringUtils.isBlank(availableBalance)?"0.00":DF_FOR_VIEW.format(new BigDecimal(availableBalance)));
                //获取累计收益
                String accumulatedEarnings = homePageService.getaccumulatedEarnings(userId);
                info.put("accumulatedEarnings", StringUtils.isBlank(accumulatedEarnings)?"0.00":accumulatedEarnings);
                //获取优惠券数量
                String coupons = homePageService.getCoupons(userId);
                info.put("coupons", StringUtils.isBlank(coupons)?"0":coupons);
                info.put("adPicUrl", "");
                info.put("adClickPicUrl", "");
            }
        }

        //获取累计出借金额
        String totalInvestmentAmount = homePageService.getTotalInvestmentAmount(userId);
        String totalInvest = homePageService.selectTotalInvest().toString();
        String totalInvestStr = formatTotalInvest(totalInvest);
        info.put("totalInvestmentAmount", totalInvestStr);
        info.put("moduleTotal", "4");
		List<AppModuleCustomize> moduleList = new ArrayList<>();

		//platform 2.安卓 3.ios

		//添加首页module
        //资金存管
        createModule(moduleList, platform, "android_module1", "ios_module1" ,HOST);
		//美国上市
        createModule(moduleList, platform, "android_module2", "ios_module2" ,HOST);
		if(compareToVersion){//新版本
			AppAdsCustomize appAdsCustomize = getOperationalDataUrl(platform, "android_module3", "ios_module3", HOST);
			String url = "";
			if(appAdsCustomize != null){
				url = appAdsCustomize.getUrl();
			}
			info.put("operationalDataURL",url);
		}else{
			//运营数据
			createModule(moduleList, platform, "android_module3", "ios_module3" ,HOST);
		}
	    //关于我们
        createModule(moduleList, platform, "android_module4", "ios_module4" ,HOST);


		info.put("moduleList", moduleList);
		//add by yangchangwei 2018-06-26 app3.0.9 增加协议相关参数
		String agreementUrl = PropUtils.getSystem("hyjf.app.regist.agreement.url");
		Map protocalInfo = new HashMap();
		protocalInfo.put("name","《相关协议》");
		protocalInfo.put("URL",HOST + agreementUrl);
		info.put("registerProtocol",protocalInfo);
		//增加公告内容
		this.getAnnouncements(info,HOST);
		Integer days = GetDate.countDate(GetDate.stringToDate("2013-12-23 00:00:00"), new Date());
		info.put("survivalDays",days);//安全运营天数
		//end

		//添加顶部活动图片总数和顶部活动图片数组
		this.createBannerPage(info,platform,request,HOST);
		this.createBannerlittlePage(info);
		this.createPopImgPage(info, uniqueIdentifier);
//		this.createTypePage(info, version, sign);
		//获取新手标
//		this.createProjectNewPage(info, list);
		//获取首页项目列表
//		this.createProjectListPage(info, version);

		//this.createNoticeInfo(info,platform, version);
		this.createForceUpdateInfo(info,platform, version,HOST);
		//增加测评说明
		//评分标准（闫文说评分标准单独做接口了不加到这里了）
		//List<Evalation> evalationList = homePageService.getEvalationRecord();
		//info.put("evalationList",evalationList);

		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		info.put(CustomConstants.APP_REQUEST,
				HomePageDefine.REQUEST_HOME + HomePageDefine.REQUEST_MAPPING + HomePageDefine.PROJECT_LIST_ACTION);
		CommonUtils.convertNullToEmptyString(info);
		LogUtil.endLog(HomePageDefine.THIS_CLASS, HomePageDefine.PROJECT_LIST_ACTION);
		return info;
	}

    /**
     * 格式化金额
     * @param totalInvest
     */
    private String formatTotalInvest(String totalInvest) {

        String[] split = totalInvest.split("\\.");
        String money = split[0];
        String substring = money.substring(0, money.length() - 8);
        totalInvest = substring + "亿元";
        return totalInvest;
    }

    /**
	 * TODO 3.0.9 版本重新填充首页推荐标的
	 * @param info
	 * @param list
	 * @param host
	 */
	private void createHjhExtensionProjectListPage_new(JSONObject info, List<AppProjectListCustomize> list, String host,String version) {

		List<AppHomePageCustomize> homeHjhPageCustomizes=convertToAppHomePageHJHCustomize(this.createAppExtensionListInfoPage_new(info,version,host),host,version);//计划
		AppHomePageCustomize appHomePageCustomize = new AppHomePageCustomize();
		if(homeHjhPageCustomizes != null && homeHjhPageCustomizes.size() > 0){
			appHomePageCustomize = homeHjhPageCustomizes.get(0);
		}
		// mod by nxl 智投服务->推荐服务和推荐产品区分开
//		appHomePageCustomize.setTitle("推荐产品");
		CommonUtils.convertNullToEmptyString(appHomePageCustomize);
		AppHomePageRecommendProject recommendProject = convertToAppHomePageRecommendProject(appHomePageCustomize);
		info.put("recommendProject", recommendProject);
	}

	/**
	 * app3.0.9 组装推荐项目 add by cwyang
	 * @param appHomePageCustomize
	 * @return
	 */
	private AppHomePageRecommendProject convertToAppHomePageRecommendProject(AppHomePageCustomize appHomePageCustomize) {
		AppHomePageRecommendProject project = new AppHomePageRecommendProject();
		project.setTitle(appHomePageCustomize.getTitle());
		project.setTag(appHomePageCustomize.getTag());
		project.setBorrowNid(appHomePageCustomize.getBorrowNid());
		project.setStatus(appHomePageCustomize.getStatus());
		project.setBorrowUrl(appHomePageCustomize.getBorrowUrl());
		project.setBorrowName(appHomePageCustomize.getBorrowName());
		project.setBorrowType(appHomePageCustomize.getBorrowType());
		project.setBorrowApr(appHomePageCustomize.getBorrowApr());
		project.setBorrowPeriod(appHomePageCustomize.getBorrowTheSecondDesc() + appHomePageCustomize.getBorrowTheSecond());
		project.setBorrowDesc(appHomePageCustomize.getBorrowDesc());
		project.setButtonText(appHomePageCustomize.getStatusName());
		//add by nxl 智投服务,添加历史
		project.setBorrowTheFirstDesc(appHomePageCustomize.getBorrowTheFirstDesc());
		// 产品加息
		project.setBorrowExtraYield(appHomePageCustomize.getBorrowExtraYield());
		if("13".equals(project.getStatus()) || "12".equals(project.getStatus())){
		    project.setButtonText("查看详情");
        }
		return project;
	}

	/**
	 * 填充新手标信息
	 * @param info
	 * @param list
	 * @param host
	 */
	private void creatNoSignProjectListPage(JSONObject info, List<AppProjectListCustomize> list, String host) {
		List<AppHomePageCustomize> homePageCustomizes = convertToAppHomePageCustomize(list,host);//新手标
		AppHomePageCustomize pageCustomize = homePageCustomizes.get(0);
		pageCustomize.setTitle("新手专享");
		pageCustomize.setTag("新手限投1次");
		pageCustomize.setBorrowDesc("100起投，最高出借5000");
		CommonUtils.convertNullToEmptyString(pageCustomize);
		AppHomePageRecommendProject recommendProject = convertToAppHomePageRecommendProject(pageCustomize);
		info.put("recommendProject", recommendProject);
	}

	/**
	 * 获取有效的公告内容
	 * @param info
	 * @param HOST
	 */
	private void getAnnouncements(JSONObject info, String HOST) {

		List<AppPushManage> manageInfoList = this.homePageService.getAnnouncenments();
		if(manageInfoList != null){

            List<Map> announMap = new ArrayList<>();
            for (AppPushManage manager : manageInfoList) {
				Map cements = new HashMap();
            	String title = manager.getTitle();//标题
				String jumpUrl = manager.getJumpUrl();//跳转URL
				Integer jumpType = manager.getJumpType();//跳转类型
				Integer jumpContent = manager.getJumpContent();//内容类型
				if(2 == jumpContent){//跳转H5页面
					jumpUrl = HOST + PropUtils.getSystem("hyjf.app.push.manager.url");
					jumpUrl = jumpUrl + manager.getId();
				}
				cements.put("title",title);
				cements.put("URL", jumpUrl);
				announMap.add(cements);
			}
			info.put("announcements",announMap);
		}else{
		    _log.info("-------------获取首页列表时，未获得有效公告！---------");
        }
	}

	/**
	 * 创建首页module
	 * @param moduleList
	 * @param platform
	 * @param android
	 * @param ios
	 */
	private void createModule(List<AppModuleCustomize> moduleList, String platform, String android, String ios, String HOST) {
		Map<String, Object> ads = new HashMap<String, Object>();
		ads.put("limitStart", HomePageDefine.BANNER_SIZE_LIMIT_START);
		ads.put("limitEnd", HomePageDefine.BANNER_SIZE_LIMIT_END);
		ads.put("host", HOST);
		String code = "";
		if (platform.equals("2")) {
			code = android;
			ads.put("platformType","1");
		} else if (platform.equals("3")) {
			code = ios;
			ads.put("platformType","2");
		}
		ads.put("code", code);
		List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存
		if(picList!=null&&picList.size()>0){
			AppModuleCustomize appModule = new AppModuleCustomize();
			appModule.setModuleUrl(picList.get(0).getImage());
			appModule.setModuleH5Url(picList.get(0).getUrl());
			moduleList.add(appModule);
		}else{
			AppModuleCustomize appModule = new AppModuleCustomize();
			appModule.setModuleUrl("");
			appModule.setModuleH5Url("");
			moduleList.add(appModule);
		}

	}

	/**
	 * 获取运营数据连接
	 * @param platform
	 * @param android
	 * @param ios
	 */
	private AppAdsCustomize getOperationalDataUrl(String platform, String android, String ios, String HOST) {
		Map<String, Object> ads = new HashMap<String, Object>();
		ads.put("limitStart", HomePageDefine.BANNER_SIZE_LIMIT_START);
		ads.put("limitEnd", HomePageDefine.BANNER_SIZE_LIMIT_END);
		ads.put("host", HOST);
		String code = "";
		if (platform.equals("2")) {
			code = android;
		} else if (platform.equals("3")) {
			code = ios;
		}
		ads.put("code", code);
		List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存
		if(picList!=null&&picList.size()>0){
			return picList.get(0);
		}
		return null;

	}

    /**
     * 创建首页广告
     * @param info
     * @param platform
     */
    private void createAdPic(JSONObject info, String platform, String type, String HOST) {
        Map<String, Object> ads = new HashMap<String, Object>();
        ads.put("limitStart", HomePageDefine.BANNER_SIZE_LIMIT_START);
        ads.put("limitEnd", HomePageDefine.BANNER_SIZE_LIMIT_END);
        ads.put("host", HOST);
        String code = "";
        if (platform.equals("2")) {
            if (type.equals("0")) {//未注册
                code = "android_regist_888";
            } else if (type.equals("1")) {
                code = "android_open_888";
            }
        } else if (platform.equals("3")) {
            if (type.equals("0")) {//未注册
                code = "ios_regist_888";
            } else if (type.equals("1")) {
                code = "ios_open_888";
            }
        }
        ads.put("code", code);
        List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存
        if (picList != null && picList.size() > 0) {
            info.put("adPicUrl", picList.get(0).getImage());
            info.put("adClickPicUrl", picList.get(0).getUrl());
        } else {
            info.put("adPicUrl", "");
            info.put("adClickPicUrl", "");
        }
    }

	/**
	 * 生成moudle图片
	 * @param moduleList
	 * @param s
	 * @param h5Url
	 */
	private void createMoudle(List<AppModuleCustomize> moduleList, String s, String h5Url) {
		AppModuleCustomize customize = new AppModuleCustomize();
		customize.setModuleUrl(s);
		customize.setModuleH5Url(h5Url);
		moduleList.add(customize);
	}

	/**
	 * 首页汇计划列表
	 */
	private List<AppProjectListCustomize> createHjhListInfoPage(String HOST) {

		// 构造分页信息 首页只取第一条
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limitStart", 0);
		params.put("limitEnd", INDEX_HJH_SHOW_SIZE);
		List<HjhPlanCustomize> planList = homePageService.searchIndexHjhPlanList(params);

		List<AppProjectListCustomize> projectListCustomizes = convertToAppProjectList(planList,HOST);
		return projectListCustomizes;
	}

	/**
	 * 首页汇计划列表
	 */
	private List<AppProjectListCustomize> createHjhExtensionListInfoPage(String HOST) {

		// 构造分页信息 首页只取第一条
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("limitStart", 0);
		params.put("limitEnd", INDEX_HJH_EXTENSION_SHOW_SIZE);
		List<HjhPlanCustomize> planList = homePageService.searchIndexHjhExtensionPlanList(params);
		boolean hasInvestment = false;
		//判断是否有可出借计划
		for (HjhPlanCustomize hjhPlanCustomize:planList){
			// mod by nxl 立即加入->授权服务 20180903 start
			/*if("立即加入".equals(hjhPlanCustomize.getStatusName())){
				hasInvestment = true;
				break;
			}*/
			if("授权服务".equals(hjhPlanCustomize.getStatusName())){
				hasInvestment = true;
				break;
			}
			// mod by nxl 立即加入->授权服务 20180903 end
		}
		//无可投计划
		if(!hasInvestment){
			planList = homePageService.selectIndexHjhExtensionPlanListByLockTime(params);
		}
        List<AppProjectListCustomize> projectListCustomizes = convertToAppProjectList(planList,HOST);
        return projectListCustomizes;
    }

    /**
     * 首页推荐标的 3.0.9
     */
    private List<AppProjectListCustomize> createAppExtensionListInfoPage_new(JSONObject info,String version,String HOST) {

        // 构造分页信息 首页只取第一条
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("limitStart", 0);
        params.put("limitEnd", INDEX_HJH_SHOW_SIZE);
        List<HjhPlanCustomize> planList = homePageService.searchIndexHjhExtensionPlanList(params);
        boolean hasInvestment = false;
		//判断是否有可出借计划
        for (HjhPlanCustomize hjhPlanCustomize:planList){
			// mod by nxl 立即加入->授权服务 20180903 start
			/*if("立即加入".equals(hjhPlanCustomize.getStatusName())){
				hasInvestment = true;
				break;
			}*/
			if("授权服务".equals(hjhPlanCustomize.getStatusName())){
				hasInvestment = true;
				break;
			}
			// mod by nxl 立即加入->授权服务 20180903 end

		}
		//无可投计划
		if(!hasInvestment){
			List<AppProjectListCustomize> projectList = this.createAppOldUserProject(info, version, HOST);
			if(projectList.size() > 0){
				List<AppProjectListCustomize> list = new ArrayList<AppProjectListCustomize>();
				AppProjectListCustomize customize = projectList.get(0);
				customize.setTag("优质资产");
				customize.setBorrowDesc("项目剩余" + customize.getBorrowAccountWait());
				customize.setStatusName("立即出借");
				list.add(customize);
				return  list;
			}else{
				planList = homePageService.selectIndexHjhExtensionPlanListByLockTime(params);
			}

		}
        List<AppProjectListCustomize> projectListCustomizes = convertToAppProjectList(planList,HOST);
		for (AppProjectListCustomize appInfo :
				projectListCustomizes) {
			appInfo.setTag("稳健回报");
			appInfo.setBorrowDesc("开放额度" + appInfo.getAvailableInvestAccountNew());
		}
        return projectListCustomizes;
    }

	/**
	 * 查询已登录出借用户的项目详情
	 * @param info
	 * @param version
	 * @param HOST
	 */
	private List<AppProjectListCustomize> createAppOldUserProject(JSONObject info, String version, String HOST) {
		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("host", HOST + HomePageDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING
				+ ProjectDefine.PROJECT_DETAIL_ACTION);
		projectMap.put("userId", info.getString("userId"));
		projectMap.put("version", StringUtils.isEmpty(version)?"":version);

		// 汇盈金服app首页定向标过滤
		projectMap.put("publishInstCode", CustomConstants.HYJF_INST_CODE);

		List<AppProjectListCustomize> projectList = new ArrayList<AppProjectListCustomize>();
		List<AppProjectListCustomize> list = homePageService.searchProjectList_new(projectMap);
		return list;
	}

	/**
	 * 适应客户端返回数据格式
	 * @param planList
	 * @return
	 */
	private List<AppProjectListCustomize> convertToAppProjectList(List<HjhPlanCustomize> planList, String HOST) {
		List<AppProjectListCustomize> appProjectList = null;
		String url = "";
		AppProjectListCustomize appProjectListCustomize;
		if (!CollectionUtils.isEmpty(planList)) {
			appProjectList = new ArrayList<AppProjectListCustomize>();
			for (HjhPlanCustomize entity : planList) {
				appProjectListCustomize = new AppProjectListCustomize();
				appProjectListCustomize.setStatus(entity.getStatus());
				appProjectListCustomize.setBorrowName(entity.getPlanName());
				appProjectListCustomize.setPlanApr(entity.getPlanApr());
				appProjectListCustomize.setBorrowApr(entity.getPlanApr());
				appProjectListCustomize.setPlanPeriod(entity.getPlanPeriod());
				appProjectListCustomize.setBorrowPeriod(entity.getPlanPeriod());
				appProjectListCustomize.setAvailableInvestAccount(entity.getAvailableInvestAccount());
				appProjectListCustomize.setAvailableInvestAccountNew(entity.getAvailableInvestAccountNew());
				appProjectListCustomize.setStatusName(entity.getStatusName());
				appProjectListCustomize.setBorrowNid(entity.getPlanNid());
				String couponEnable = entity.getCouponEnable();
				if (org.apache.commons.lang.StringUtils.isEmpty(couponEnable) || "0".equals(couponEnable)) {
					couponEnable = "0";
				} else {
					couponEnable = "1";
				}
				appProjectListCustomize.setCouponEnable(couponEnable);
				// 项目详情url
				url = HOST + HomePageDefine.REQUEST_HOME + HjhPlanDefine.REQUEST_MAPPING
						+ HjhPlanDefine.HJH_PLAN_DETAIL_ACTION + "?planNid='" + entity.getPlanNid() + "'";
				appProjectListCustomize.setBorrowUrl(url);
				appProjectListCustomize.setProjectType("HJH");
				appProjectListCustomize.setBorrowType("HJH");

				// 应客户端要求，返回空串
				CommonUtils.convertNullToEmptyString(appProjectListCustomize);
				appProjectList.add(appProjectListCustomize);
			}
		}
		return appProjectList;
	}

	/**
	 * 查询首页banner图
	 *
	 * @param info
	 */
	private void createBannerPage(JSONObject info, String platform, HttpServletRequest request, String HOST) {
		Map<String, Object> ads = new HashMap<String, Object>();
		ads.put("limitStart", HomePageDefine.BANNER_SIZE_LIMIT_START);
		ads.put("limitEnd", HomePageDefine.BANNER_SIZE_LIMIT_END);
		ads.put("host", HOST);
		String code = "";
		if (platform.equals("2")) {
			code = "android_banner";
			ads.put("platformType","1");
		} else if (platform.equals("3")) {
			code = "ios_banner";
			ads.put("platformType","2");
		}
		ads.put("code", code);
		List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存
		if (picList != null && picList.size() > 0) {
			for(AppAdsCustomize appAdsCustomize : picList){
				appAdsCustomize.setPicUrl(appAdsCustomize.getImage());
				// 安卓需加sign和token
				if ("2".equals(platform)) {
					String picH5Url = appAdsCustomize.getUrl();
					appAdsCustomize.setPicH5Url(appAdsCustomize.getUrl());
				} else {
					appAdsCustomize.setPicH5Url(appAdsCustomize.getUrl());
				}
			}
			info.put("picList", picList);
			info.put("picTotal", String.valueOf(picList.size()));
		} else {
			info.put("picList", new ArrayList<AppAdsCustomize>());
			info.put("picTotal", "0");
		}
	}

	/**
	 *
	 * 首页弹窗广告图片
	 * @author hsy
	 * @param info
	 */
	private void createPopImgPage(JSONObject info, String uniqueIdentifier) {
		Map<String, Object> ads = new HashMap<String, Object>();
		ads.put("limitStart", HomePageDefine.BANNER_SIZE_LIMIT_START);
		ads.put("limitEnd", HomePageDefine.BANNER_SIZE_LIMIT_END);
		ads.put("host", HOST_URL);
		ads.put("code", "popup");
		Integer userId = info.get("userId") == null? null: (Integer)info.get("userId");
		List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存
		if (picList != null && picList.size() > 0) {
			AppAdsCustomize appads = picList.get(0);
			info.put("imageUrl", appads.getImage());
			info.put("imageUrlOperation", appads.getUrl());

			int requestTimes = homePageService.updateCurrentDayRequestTimes(uniqueIdentifier, userId);
			if(appads.getNewuserShow().equals("2") || (appads.getNewuserShow().equals("1") && userId == null)){
				if(requestTimes <= 1){
					// 如果是今天第一次请求则显示
					info.put("imageUrlExist", "1");
				}else{
					// 不是今天第一次请求则不显示
					info.put("imageUrlExist", "0");
				}

			}else{
				info.put("imageUrlExist", "0");
			}
		} else {

			info.put("imageUrlExist", "0");
			info.put("imageUrl", "");
			info.put("imageUrlOperation", "");
		}
	}

	/**
	 *
	 * 首页横幅广告
	 * @author hsy
	 * @param info
	 */
	private void createBannerlittlePage(JSONObject info) {
		Map<String, Object> ads = new HashMap<String, Object>();
		ads.put("limitStart", HomePageDefine.BANNER_SIZE_LIMIT_START);
		ads.put("limitEnd", HomePageDefine.BANNER_SIZE_LIMIT_END);
		ads.put("host", HOST_URL);
		ads.put("code", "bannerlittle");
		Object userId = info.get("userId");
		List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存
		if (picList != null && picList.size() > 0) {
			AppAdsCustomize appads = picList.get(0);
			System.out.println("adImageUrl: " + appads.getImage());
			System.out.println("adImageUrlOperation: " + appads.getUrl());
			info.put("adImageUrl", appads.getImage());
			info.put("adImageUrlOperation", appads.getUrl());

			boolean isNewUser = homePageService.checkNewUser(userId);
			if(appads.getNewuserShow().equals("2") || (appads.getNewuserShow().equals("1") && isNewUser)){
				info.put("adExist", "1");
			}else{
				info.put("adExist", "0");
			}
		} else {

			info.put("adExist", "0");
			info.put("adImageUrl", "");
			info.put("adImageUrlOperation", "");
		}
		//System.out.println("jsonInfoBannerLittle: " + info.toJSONString());
	}


	/**
	 * 查询首页的项目类型的列表
	 * @param info
	 * @param version
	 * @param sign
	 */
	private void createTypePage(JSONObject info, String version, String sign) {
		Map<String, Object> projectType = new HashMap<String, Object>();
		projectType.put("host", HOST_URL);
		projectType.put("version", version);
		projectType.put("sign", sign);
		List<AppBorrowImageCustomize> typeList = homePageService.searchProjectTypeList(projectType);
		if (typeList != null && typeList.size() > 0) {
			info.put("typeList", typeList);
			info.put("typeTotal", String.valueOf(typeList.size()));
		} else {
			info.put("typeList", new ArrayList<AppBorrowImageCustomize>());
			info.put("typeTotal", "0");
		}

	}

	/**
	 * 查询首页项目列表
	 * @param info
	 * @param version
	 */
	private void createProjectListPage(JSONObject info, String version,List<AppProjectListCustomize> appProjectListCustomizeList, String HOST) {
		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("host", HOST + HomePageDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING
				+ ProjectDefine.PROJECT_DETAIL_ACTION);
		projectMap.put("userId", info.getString("userId"));
		projectMap.put("version", StringUtils.isEmpty(version)?"":version);

		// 汇盈金服app首页定向标过滤
		projectMap.put("publishInstCode", CustomConstants.HYJF_INST_CODE);

		List<AppProjectListCustomize> projectList = new ArrayList<AppProjectListCustomize>();
		List<AppProjectListCustomize> list = homePageService.searchProjectList(projectMap);
		if (appProjectListCustomizeList.size() == 0){
			//如果没有新手标,则首页展示两条散标
			AppProjectListCustomize project1 = list.get(0);
			AppProjectListCustomize project2 = list.get(1);
			appProjectListCustomizeList.add(project1);
			appProjectListCustomizeList.add(project2);
		}else {
			AppProjectListCustomize project1 =  list.get(0);
			appProjectListCustomizeList.add(project1);
		}

		List<AppHomePageCustomize> homePageCustomizes = convertToAppHomePageCustomize(appProjectListCustomizeList,HOST);

		// 2.0.7及以下，不返回汇计划
		if (compareToVersion(version, "2.0.7")) {
			homePageCustomizes.addAll(convertToAppHomePageHJHCustomize(this.createHjhListInfoPage(HOST),HOST,version));
		}
		CommonUtils.convertNullToEmptyString(homePageCustomizes);
		info.put("projectTotal",homePageCustomizes.size());
		info.put("projectList", homePageCustomizes);
	}

	/**
     * 查询首页项目列表
     * @param info
     * @param version
     */
    private void createHjhExtensionProjectListPage(JSONObject info,List<AppProjectListCustomize> appProjectListCustomizeList, String HOST,String version) {

        List<AppHomePageCustomize> homePageCustomizes = convertToAppHomePageCustomize(appProjectListCustomizeList,HOST);//新手标
        List<AppHomePageCustomize> homeHjhPageCustomizes=convertToAppHomePageHJHCustomize(this.createHjhExtensionListInfoPage(HOST),HOST,version);//计划
        if (appProjectListCustomizeList.size() == 0){
             homePageCustomizes.addAll(homeHjhPageCustomizes);
        }else {
            if(homeHjhPageCustomizes!=null&&homeHjhPageCustomizes.size()==3){
                homePageCustomizes.add(homeHjhPageCustomizes.get(0));
                homePageCustomizes.add(homeHjhPageCustomizes.get(1));
            } else {
                homePageCustomizes.addAll(homeHjhPageCustomizes);
            }
            
        }
        CommonUtils.convertNullToEmptyString(homePageCustomizes);
        info.put("projectTotal",homePageCustomizes.size());
        info.put("projectList", homePageCustomizes);
    }

	/**
	 * 返回version 是否大于 基准版本compareVersion
	 * @param version
	 * @param compareVersion
	 * @return
	 */
	private boolean compareToVersion(String version, String compareVersion) {
		if (StringUtils.isEmpty(version) || StringUtils.isEmpty(compareVersion))
			return false;

		if (version.length() >= 5) {
			version = version.substring(0, 5);
		}
		if (version.compareTo(compareVersion) > 0) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * 获取新手标数据
	 * @author hsy
	 * @param info
	 */
	private void createProjectNewPage(JSONObject info, List<AppProjectListCustomize> list, String HOST) {

		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("host", HOST + HomePageDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING
				+ ProjectDefine.PROJECT_DETAIL_ACTION);
		projectMap.put("userId", info.getString("userId"));
		List<AppProjectListCustomize> projectList = homePageService.searchProjectNewList(projectMap);//加缓存
		Object userId = info.get("userId");
		boolean isNewUser = homePageService.checkNewUser(userId);
		if(isNewUser){
			if(projectList != null && !projectList.isEmpty()){
				AppProjectListCustomize project = projectList.get(0);
				if (list.size() == 0) {
					list.add(project);
				}
				info.put("sprogExist", "1");
				info.put("sprogBorrowApr", project.getBorrowApr());
				info.put("sprogBorrowPeriod", project.getBorrowPeriod());
				String balance = project.getBorrowAccountWait();//add by cwyang 根据borrowNid 获取项目可投金额
				info.put("sprogBorrowMoney", balance);//新手标取得是可投余额不是出借总额 add by cwyang
				info.put("sprogBorrowNid", project.getBorrowNid());
				info.put("sprogBorrowUrl", project.getBorrowUrl());
				info.put("sprogTime", project.getOnTime());
				if(!Validator.isIncrease(project.getIncreaseInterestFlag(), project.getBorrowExtraYieldOld())){
					project.setBorrowExtraYield("");
				}
				info.put("borrowExtraYield", project.getBorrowExtraYield());
			}else {
				info.put("sprogExist", "0");
				info.put("sprogBorrowApr", "");
				info.put("sprogBorrowPeriod", "");
				info.put("sprogBorrowMoney", "");
				info.put("sprogBorrowNid", "");
				info.put("sprogBorrowUrl", "");
				info.put("sprogTime", "");
				info.put("borrowExtraYield", "");
			}
		}else {
			info.put("sprogExist", "0");
			info.put("sprogBorrowApr", "");
			info.put("sprogBorrowPeriod", "");
			info.put("sprogBorrowMoney", "");
			info.put("sprogBorrowNid", "");
			info.put("sprogBorrowUrl", "");
			info.put("sprogTime", "");
			info.put("borrowExtraYield", "");
		}
	}

	/**
	 * 适应客户端数据返回
	 * @param list
	 * @return
	 */
	private List<AppHomePageCustomize> convertToAppHomePageCustomize(List<AppProjectListCustomize> list, String HOST) {
		List<AppHomePageCustomize> homePageCustomizes = new ArrayList<>();
		for (AppProjectListCustomize listCustomize : list) {
			AppHomePageCustomize homePageCustomize = new AppHomePageCustomize();
			homePageCustomize.setBorrowNid(listCustomize.getBorrowNid());
			if (listCustomize.getBorrowNid().startsWith("NEW")) {
				homePageCustomize.setBorrowName( listCustomize.getBorrowNid());
				homePageCustomize.setBorrowDesc("新手");
			} else {
				homePageCustomize.setBorrowName(listCustomize.getBorrowNid());
				homePageCustomize.setBorrowDesc("散标");
			}
			// 产品加息

            String borrowExtraYield = listCustomize.getBorrowExtraYield();
//            if(StringUtils.isNotBlank(borrowExtraYield)){
//                borrowExtraYield = borrowExtraYield.substring(1,borrowExtraYield.length());
//            }
            homePageCustomize.setBorrowExtraYield(borrowExtraYield);
			homePageCustomize.setBorrowType(listCustomize.getBorrowType());
			homePageCustomize.setBorrowTheFirst(listCustomize.getBorrowApr() + "%");
			homePageCustomize.setBorrowTheFirstDesc("历史年回报率");
			homePageCustomize.setBorrowTheSecond(listCustomize.getBorrowPeriod());
			homePageCustomize.setBorrowTheSecondDesc("项目期限");

			//定时开标
			String onTime = listCustomize.getOnTime();
			if(!("0".equals(onTime)||"".equals(onTime))){
				homePageCustomize.setStatusName(onTime);
			} else {
				homePageCustomize.setStatusName("立即出借");
			}

			String statusNameDesc = homePageService.getBorrowAccountWait(listCustomize.getBorrowNid());
			homePageCustomize.setStatusNameDesc("剩余" + DF_FOR_VIEW.format(new BigDecimal(statusNameDesc)));
			homePageCustomize.setBorrowUrl(HOST + HomePageDefine.BORROW + listCustomize.getBorrowNid());
			homePageCustomize.setBorrowApr(listCustomize.getBorrowApr() + "%");
			homePageCustomize.setBorrowPeriod(listCustomize.getBorrowPeriod());
			homePageCustomize.setStatus(listCustomize.getStatus());
			homePageCustomize.setOnTime(listCustomize.getOnTime());
			homePageCustomize.setBorrowSchedule(listCustomize.getBorrowSchedule());
			homePageCustomize.setBorrowTotalMoney(StringUtils.isBlank(listCustomize.getBorrowTotalMoney())?"0":listCustomize.getBorrowTotalMoney());
			homePageCustomizes.add(homePageCustomize);
		}
		return homePageCustomizes;
	}

	/**
	 * 适应客户端数据返回HJH
	 * @param list
	 * @return
	 */
	private List<AppHomePageCustomize> convertToAppHomePageHJHCustomize(List<AppProjectListCustomize> list, String HOST,String version) {
		List<AppHomePageCustomize> homePageCustomizes = new ArrayList<>();
		for (AppProjectListCustomize listCustomize : list) {
			AppHomePageCustomize homePageCustomize = new AppHomePageCustomize();
			homePageCustomize.setBorrowNid(listCustomize.getBorrowNid());
			homePageCustomize.setBorrowName( listCustomize.getBorrowName());
			if(compareToVersion(version,"3.0.8")){
				homePageCustomize.setBorrowDesc(listCustomize.getBorrowDesc());
			}else{
				homePageCustomize.setBorrowDesc("计划");
			}
			homePageCustomize.setBorrowType(listCustomize.getBorrowType());
			homePageCustomize.setBorrowTheFirst(listCustomize.getBorrowApr() + "%");
			// mod by nxl 智投服务 计划的历史回报率->参考年回报率
//			homePageCustomize.setBorrowTheFirstDesc("历史年回报率");
			homePageCustomize.setBorrowTheFirstDesc("参考年回报率");
			homePageCustomize.setBorrowTheSecond(listCustomize.getBorrowPeriod());
			// mod by nxl 智投服务 修改显示 start
//			homePageCustomize.setBorrowTheSecondDesc("锁定期限");
			homePageCustomize.setBorrowTheSecondDesc("服务回报期限");
			// mod by nxl 智投服务 修改显示 end
			String statusNameDesc = homePageService.getHJHAccountWait(listCustomize.getBorrowNid());
			if(StringUtils.isNotBlank(statusNameDesc)){
				BigDecimal openAmount = new BigDecimal(statusNameDesc);
				if(openAmount.compareTo(BigDecimal.ZERO) > 0){
					homePageCustomize.setStatusNameDesc("额度" + DF_FOR_VIEW.format(openAmount));
				} else {
					homePageCustomize.setStatusNameDesc("");
				}
			}

			homePageCustomize.setBorrowUrl(HOST + HomePageDefine.PLAN + listCustomize.getBorrowNid());
			homePageCustomize.setBorrowApr(listCustomize.getBorrowApr() + "%");
			homePageCustomize.setBorrowPeriod(listCustomize.getBorrowPeriod());
			String status = listCustomize.getStatus();
			// add by nxl 智投服务->添加推荐服务标题
			homePageCustomize.setTitle("推荐服务");
			if ("稍后开启".equals(listCustomize.getStatusName())){    //1.启用  2.关闭
				// 20.立即加入  21.稍后开启
				homePageCustomize.setStatus("21");
				homePageCustomize.setStatusName("稍后开启");
				homePageCustomize.setStatusNameDesc("");
			}/*else if(listCustomize.getStatusName().equals("立即加入")){  //1.启用  2.关闭
				homePageCustomize.setStatus("20");
				homePageCustomize.setStatusName("立即加入");
			}*/else if(listCustomize.getStatusName().equals("授权服务")){  //1.启用  2.关闭
				homePageCustomize.setStatus("20");
				homePageCustomize.setStatusName("授权服务");
			}else if(listCustomize.getStatusName().equals("立即出借")){
				//add by nxl 智投服务->添加推荐服务标题
				homePageCustomize.setTitle("推荐产品");
				homePageCustomize.setStatus(listCustomize.getStatus());
				homePageCustomize.setStatusName("立即出借");
				homePageCustomize.setBorrowTheSecondDesc("项目期限");
				// add by nxl 智投服务 推荐产品显示历史年回报率
				homePageCustomize.setBorrowTheFirstDesc("历史年回报率");
				homePageCustomize.setBorrowUrl(HOST + HomePageDefine.BORROW + listCustomize.getBorrowNid());
                String borrowExtraYield = listCustomize.getBorrowExtraYield();
                if(StringUtils.isNotBlank(borrowExtraYield)){
                    borrowExtraYield = borrowExtraYield.substring(1,borrowExtraYield.length());
                }
                homePageCustomize.setBorrowExtraYield(borrowExtraYield);
			}else {
				homePageCustomize.setStatus("21");
				homePageCustomize.setStatusName("稍后开启");
				homePageCustomize.setStatusNameDesc("");
			}
			homePageCustomize.setOnTime(listCustomize.getOnTime());
			homePageCustomize.setBorrowSchedule(listCustomize.getBorrowSchedule());
			homePageCustomize.setBorrowTotalMoney(StringUtils.isBlank(listCustomize.getBorrowTotalMoney())?"0":listCustomize.getBorrowTotalMoney());
			homePageCustomize.setTag(listCustomize.getTag());
			homePageCustomizes.add(homePageCustomize);
		}
		CommonUtils.convertNullToEmptyString(homePageCustomizes);
		return homePageCustomizes;
	}

	/**
	 *
	 * 获取IOS强制更新
	 * @author hsy
	 * @param info
	 */
	private void createForceUpdateInfo(JSONObject info, String platform, String version, String HOST){
		// 从配置文件中加载配置信息
		String noticeStatus = PropUtils.getSystem("hyjf.notice.status");
		String noticeUrlIOS = PropUtils.getSystem("hyjf.notice.requesturl.ios");
		String iosVersion = PropUtils.getSystem("hyjf.notice.ios.version");
		noticeUrlIOS = HOST + HomePageDefine.REQUEST_HOME + "/" + noticeUrlIOS+"?version="+version;

		boolean isNeedUpdate = false;
		if(StringUtils.isEmpty(version)){
			isNeedUpdate = true;
		}

		if(version.length()>=5){
			version = version.substring(0, 5);
		}
		_log.info("version===="+version+"-----------iosVersion"+iosVersion);
		if(version.compareTo(iosVersion)<0){
			isNeedUpdate = true;
		}

//       System.out.println("noticeStatus: " + noticeStatus);
		// 是否需要收到通知
		if(noticeStatus.equals("true") && StringUtils.isNotBlank(platform) && platform.equals("3") && isNeedUpdate){
			info.put("needForcedToUpdate", "1");
			info.put("forcedToUpdateUrl", noticeUrlIOS);
		}else {
			info.put("needForcedToUpdate", "0");
			info.put("forcedToUpdateUrl", "");
		}

	}

	/**
	 *
	 * 升级维护页
	 * @author hsy
	 * @param info
	 */
	private void createNoticeInfo(JSONObject info, String platform, String version){
		// 从配置文件中加载配置信息
		String HOST = PropUtils.getSystem("hyjf.web.host");
		String noticeStatus = PropUtils.getSystem("hyjf.notice.status");
		String noticeRange = PropUtils.getSystem("hyjf.notice.timerange");
		String noticeUrl = PropUtils.getSystem("hyjf.notice.requesturl");
//       String noticeUrlIOS = PropUtils.getSystem("hyjf.notice.requesturl.ios");
		noticeUrl = HOST + HomePageDefine.REQUEST_HOME + "/" + noticeUrl+"?";
//       noticeUrlIOS = HOST + HomePageDefine.REQUEST_HOME + "/" + noticeUrlIOS+"?version="+version;
		if(noticeUrl.indexOf("?")==-1){
			noticeUrl=noticeUrl+"?";
//           noticeUrlIOS=noticeUrlIOS+"?";
		}


		Integer timeStart = Integer.parseInt(noticeRange.split(",")[0]);
		Integer timeEnd = Integer.parseInt(noticeRange.split(",")[1]);
		Integer nowTime = GetDate.getNowTime10();

		// 是否需要收到通知
		if(timeStart <= nowTime && timeEnd >= nowTime && noticeStatus.equals("true")){
			info.put("needForcedToUpdate", "1");
			info.put("forcedToUpdateUrl", noticeUrl);
		}else {
			info.put("needForcedToUpdate", "0");
			info.put("forcedToUpdateUrl", "");
		}

	}


	/**
	 * 获取首页统计数据
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
		Map<String, Object> data=homePageService.selectData();
		DecimalFormat df = new DecimalFormat("#,##0");
		if (data!=null) {
			homePageStatisticsCustomize.setBailTotal( df.format(new BigDecimal(data.get("bail_money")+"").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			homePageStatisticsCustomize.setTotalSum( df.format(new BigDecimal(data.get("tender_sum")+"").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			homePageStatisticsCustomize.setTotalInterest(df.format(new BigDecimal(data.get("interest_sum")+"").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
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
	 * 获取起始页banner(app首先请求画面)
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.START_PAGE_ACTION, method = RequestMethod.POST ,produces = "application/json; charset=utf-8")
	public JSONObject getStartPage(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(HomePageDefine.THIS_CLASS, HomePageDefine.START_PAGE_ACTION);
		JSONObject result = new JSONObject();
		String platform = request.getParameter("realPlatform");
		if (StringUtils.isBlank(platform)) {
			platform = request.getParameter("platform");
		}
		result.put(CustomConstants.APP_REQUEST,HomePageDefine.REQUEST_HOME + HomePageDefine.REQUEST_MAPPING + HomePageDefine.START_PAGE_ACTION);
		try {
			Map<String, Object> ads = new HashMap<String, Object>();
			ads.put("limitStart",0 );
			ads.put("limitEnd", 1);
			ads.put("host", HOST_URL);
			ads.put("code", "startpage");
			if (platform.equals("2")) {
				ads.put("platformType","1");
			} else if (platform.equals("3")) {
				ads.put("platformType","2");
			}
			List<AppAdsCustomize> picList = homePageService.searchBannerList(ads);//加缓存
			if(picList == null || picList.size() == 0){
				result.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
				result.put(CustomConstants.APP_STATUS_DESC, "获取banner失败,暂无数据");
				return result;
			}
			_log.info("platform======"+platform);
			_log.info("picUrl======"+picList.get(0).getImage());
			_log.info("actionUrl======"+picList.get(0).getUrl());
			result.put("picUrl",picList.get(0).getImage());
			if(StringUtils.isNotEmpty(picList.get(0).getUrl())){
				result.put("actionUrl", picList.get(0).getUrl());
			}else{
				result.put("actionUrl","");
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
		//资质文件数据
		List<ContentQualify> list = this.homePageService.getCompanyQualify();
		map.put("contentQualifyList", list);
		//文件地址
		map.put("host", HOST_URL);
		return map;
	}

	/**
	 * 关于我们——平台数据
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.PLATFORM_DATA_ACTION)
	public JSONObject getPlatformData(HttpServletRequest request, HttpServletResponse response) {
		JSONObject js=new JSONObject();
		js.put("request", HOST_URL + HomePageDefine.REQUEST_MAPPING + HomePageDefine.PLATFORM_DATA_ACTION);
		js.put("status", "0");
		js.put("statusDesc", "请求成功");
		//平台数据
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("#,##0");
		Map<String, Object> data=homePageService.selectData();
		if (data!=null) {
			js.put("tenderMoney", df.format(new BigDecimal(data.get("tender_sum")+"").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			js.put("recoverInterest",df.format(new BigDecimal(data.get("interest_sum")+"").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			js.put("tenderMoney7", df.format(new BigDecimal(data.get("seven_day_tender_sum")+"").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			js.put("recoverInterest7", df.format(new BigDecimal(data.get("seven_day_interest_sum")+"").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			//融资期限分布
			Map<String, Object> periodInfo=new HashMap<String, Object>();
			//出借金额占比
			Map<String, Object> TendMoneyInfo=new HashMap<String, Object>();
			js.put("newdate",sdf.format(data.get("update_time")));
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
		//开始先把日期都放集合里 然后排序，
		//然后 循环对比集合里是否有此日期的数据 么有的话放0，有的话放入
		//最后生成两个一样大小的有序集合
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
				if (timelist.get(i).compareTo(
						tenderlist.get(j).get("time") + "") != 0) {
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
				if (timelist.get(i).compareTo(
						creditlist.get(j).get("time") + "") != 0) {
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
				if (timelist.get(i).compareTo(
						htjlist.get(j).get("time") + "") != 0) {
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
		//为前台报表js不能写循环？ 制造数据
		for (int j = 0; j < timelist.size(); j++) {
			timelist.set(j, timelist.get(j));
		}
		List<String> tendermoneylist = new ArrayList<String>();
		List<String> creditmoneylist = new ArrayList<String>();
		List<String> htjmoneylist = new ArrayList<String>();
		for (int j = 0; j < newtenderlist.size(); j++) {
			tendermoneylist.add(newtenderlist.get(j).get("money")+"");
			if (newcreditlist!=null&&newcreditlist.size()>0) {
				creditmoneylist.add(newcreditlist.get(j).get("money")+"");
			}
			if (newhtjlist!=null&&newhtjlist.size()>0) {
				htjmoneylist.add(newhtjlist.get(j).get("money")+"");
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
		//400客服电话
		String phoneNumber = this.homePageService.getServicePhoneNumber();
		map.put("servicePhoneNumber", phoneNumber);
		return map;
	}

	/**
	 * 获取JumpCommend
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = HomePageDefine.GET_JUMP_COMMEND_ACTION)
	public BaseResultBeanFrontEnd getJumpCommend(HttpServletRequest request, HttpServletResponse response) {
		BaseResultBeanFrontEnd baseResultBeanFrontEnd=new BaseResultBeanFrontEnd();
		baseResultBeanFrontEnd.setStatus(BaseResultBeanFrontEnd.SUCCESS);
		baseResultBeanFrontEnd.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
		return baseResultBeanFrontEnd;
	}

	/**
	 * 评分标准接口
	 *
	 * */

	@ResponseBody
	@RequestMapping(value = HomePageDefine.RESULT_STANDARD, method = RequestMethod.POST)
	public JSONObject gradingStandardResult(HttpServletRequest request, HttpServletResponse response) {
		JSONObject map = new JSONObject();
		List<EvalationCustomize> evalationCustomizeList = homePageService.getEvalationRecord();
		map.put("status", "000");
		map.put("statusDesc", "请求成功");
		map.put("evalationList", evalationCustomizeList);
		return map;
	}

}
