/**
 * Description:获取指定的项目类型的项目列表
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.app.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.bank.user.openaccount.OpenAccountDefine;
import com.hyjf.app.hjhplan.HjhPlanDefine;
import com.hyjf.app.hjhplan.HjhPlanService;
import com.hyjf.app.home.HomePageDefine;
import com.hyjf.app.user.credit.AppTenderCreditDefine;
import com.hyjf.app.user.credit.AppTenderCreditService;
import com.hyjf.app.user.transfer.AppTransferDefine;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.AsteriskProcessUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetJumpCommand;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.UpdateNoticeUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.app.AppHzcDisposalPlanCustomize;
import com.hyjf.mybatis.model.customize.app.AppHzcProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppMortgageCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectAuthenInfoCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectCompanyDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectConsumeCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectPersonDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectTypeCustomize;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppVehiclePledgeCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;

@Controller
@RequestMapping(value = ProjectDefine.REQUEST_MAPPING)
public class ProjectController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectService projectService;

	@Autowired
	private HjhPlanService hjhPlanService;

	/** 债权转让Service */
	@Autowired
	private AppTenderCreditService appTenderCreditService;

	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

	private static String HOST = PropUtils.getSystem("hyjf.web.host");

	/**
	 * 获取指定类型的项目的列表
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ProjectDefine.PROJECT_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject searchProjectList(@ModelAttribute ProjectBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_LIST_ACTION);
		JSONObject info = new JSONObject();
		// form.setProjectType("HZR");
		
		String version = request.getParameter("version");

        boolean isNeedUPdate = UpdateNoticeUtils.checkForAppUpdate(version, "此版本暂不可用，请更新至最新版本。", ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.PROJECT_LIST_ACTION, info);
        if(isNeedUPdate){
            return info;
        }

        String projectType = request.getParameter("projectType");
//		String projectType = form.getProjectType();
        if (projectType.equals("HZT")){
			info = this.createProjectListPage(form, request);//加缓存
		}else if (projectType.equals("HJH")){
			info = this.createHJHProjectListPage(form, request);//加缓存
		}else if (projectType.equals("HZR")){
			info = getCreditTenderList(form, request);//加缓存
		}

        // modify by xiashuqing 20171108 begin 增加app汇计划查询
		//info = this.createProjectListPage(form); 原来的逻辑
		/*if("HJH".equals(form.getProjectType())){
			info = this.createHJHProjectListPage(form);
		} else {
			info = this.createProjectListPage(form);
		}*/
		// modify by xiashuqing 20171108 end 增加app汇计划查询
        info.put("page", form.getPage());
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		info.put(CustomConstants.APP_REQUEST, ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.PROJECT_LIST_ACTION);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_LIST_ACTION);
		return info;
	}

	/**
	 * 查询汇计划分页列表
	 * @param form
	 * @return
	 */
	private JSONObject createHJHProjectListPage(ProjectBean form, HttpServletRequest request) {
		JSONObject info = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();

		// 查询总数
		Integer count = projectService.countHjhPlan(params);
		logger.info("count is : {}", count);

		// 合规审批需求  add by huanghui 20181123 start
		info.put("riskWarningHint", "智投服务是平台根据出借人授权，帮助出借人分散投标、循环出借的服务。到期后退出时效以实际债权转让完成用时为准 。");
		info.put("riskWarningContent", " $智投介绍$ " +
				"\n" +
				"智投服务是汇盈金服为您提供的本金自动循环出借及到期自动转让退出的自动投标服务，自动投标授权服务期限自授权出借之日起至退出完成。出借范围仅限于平台发布的借款标的或服务中被转让债权，您可随时查看持有的债权标的列表及标的详情。" +
				"\n\n" +
				" $出借人适当性管理告知$ " +
				"\n" +
				"作为网络借贷的出借人，应当具备出借风险意识，风险识别能力，请您在出借前，确保了解借款项目的主要风险，谨慎出借。\n" +
				"汇盈金服展示的参考回报不代表对实际回报的承诺；您的出借本金及对应回报可能无法按时收回。服务回报期限届满，系统对尚未结清标的自动发起债权转让。退出完成需债权标的全部结清，并且债权转让全部完成。您所持债权转让完成的具体时间，视债权转让市场交易情况而定。");
		// 合规审批需求  add by huanghui 20181123 end

		if (count != null && count > 0) {
			info.put("projecTotal", count);

			// 构造分页
			Integer pageSize = form.getPageSize();
			Integer page = form.getPage();
			params.put("limitStart", pageSize * (page - 1));
			params.put("limitEnd", pageSize);

			List<HjhPlanCustomize> planList = projectService.searchHjhPlanList(params);
			List<AppProjectListCustomize> list = convertToAppProjectList(planList);
			List<AppProjectTypeCustomize> appProjectTypes = convertToAppProjectHJHType(list, request);
			logger.info("list is : {}", CollectionUtils.isEmpty(appProjectTypes));

			if (!CollectionUtils.isEmpty(list)) {
				info.put("projectList", appProjectTypes);
			} else {
				info.put("projectList", new ArrayList<AppProjectListCustomize>());
			}
		} else {
			info.put("projecTotal", "0");
			info.put("projectList", new ArrayList<AppProjectListCustomize>());
		}

//		info.put("projectType", form.getProjectType());
		return info;
	}

	/**
	 * 适应客户端返回数据格式
	 * @param list
	 * @return
	 */
	private List<AppProjectTypeCustomize> convertToAppProjectHJHType(List<AppProjectListCustomize> list, HttpServletRequest request) {
		List<AppProjectTypeCustomize> appProjectTypes = new ArrayList<>();
		for(AppProjectListCustomize listCustomize : list){
			AppProjectTypeCustomize appProjectType = new AppProjectTypeCustomize();
			appProjectType.setBorrowNid(listCustomize.getBorrowNid());
			appProjectType.setBorrowName(listCustomize.getBorrowName());
			appProjectType.setBorrowDesc(listCustomize.getBorrowDesc());
			appProjectType.setBorrowTheFirst(listCustomize.getBorrowApr() + "%");
			// mod by nxl 智投服务修改历史年回报率->参考年回报率
//			appProjectType.setBorrowTheFirstDesc("历史年回报率");
			appProjectType.setBorrowTheFirstDesc("参考年回报率");
			appProjectType.setBorrowTheSecond(listCustomize.getBorrowPeriod());
			// mod by nxl 智投服务修改锁定期限->服务回报期限
//			appProjectType.setBorrowTheSecondDesc("锁定期限");
			appProjectType.setBorrowTheSecondDesc("服务回报期限");
			String status = listCustomize.getStatus();
			/*if (status.equals("10")){
				//剩余发标时间
				String timeLeft = projectService.getTimeLeft(listCustomize.getBorrowNid());
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				String timeFormat = formatter.format(Long.valueOf(timeLeft));
				appProjectType.setStatusName(timeFormat);
				//可投金额
				String borrowAccountWait = projectService.getBorrowAccountWait(listCustomize.getBorrowNid());
				appProjectType.setStatusNameDesc("剩余" + borrowAccountWait);
			}else if(status.equals("11")){
				appProjectType.setStatusName("立即加入");
				//可投金额
				String borrowAccountWait = projectService.getBorrowAccountWait(listCustomize.getBorrowNid());
				appProjectType.setStatusNameDesc("剩余" + borrowAccountWait);
			}else if (status.equals("12")){
				appProjectType.setStatusName("复审中");
			}else if (status.equals("13")){
				appProjectType.setStatusName("还款中");
			}else if (status.equals("14")){
				appProjectType.setStatusName("已还款");
			}*/
			if (listCustomize.getStatusName().equals("稍后开启")){    //1.启用  2.关闭
			    // 20.立即加入  21.稍后开启
				appProjectType.setStatus("21");
				appProjectType.setStatusName("稍后开启");
			}
			/*else if(listCustomize.getStatusName().equals("立即加入")){  //1.启用  2.关闭
				appProjectType.setStatus("20");
				appProjectType.setStatusName("立即加入");*/
			//mod by nxl 智投服务 修改立即加入->授权服务
			else if(listCustomize.getStatusName().equals("授权服务")){
				appProjectType.setStatus("20");
				appProjectType.setStatusName("授权服务");
                // 根据计划编号获取相应的计划详情信息
				DebtPlanDetailCustomize debtPlanDetailCustomize = hjhPlanService.selectDebtPlanDetail(listCustomize.getBorrowNid());
                String availableInvestAccount = debtPlanDetailCustomize.getAvailableInvestAccount();
                availableInvestAccount = CommonUtils.formatAmount(availableInvestAccount);
                    appProjectType.setStatusNameDesc(StringUtils.isBlank(availableInvestAccount)?"":"额度" + availableInvestAccount);
			}
			//appProjectType.setBorrowUrl(CommonUtils.concatReturnUrl(request, HOST_URL + HjhPlanDefine.REQUEST_MAPPING + "/" + listCustomize.getBorrowNid()));
			appProjectType.setBorrowUrl(HOST_URL + HjhPlanDefine.REQUEST_MAPPING + "/" + listCustomize.getBorrowNid());
			appProjectType.setOnTime(listCustomize.getOnTime());

//			String projectType = projectService.getProjectType(listCustomize.getBorrowType());
			if ("ZXH".equals(listCustomize.getBorrowType())){
				appProjectType.setMark("尊享");
			}else if("RTB".equals(listCustomize.getBorrowType())){
				appProjectType.setMark("优选");
			}
			appProjectType.setBorrowType(listCustomize.getBorrowType());
			CommonUtils.convertNullToEmptyString(appProjectType);
			appProjectTypes.add(appProjectType);
		}
		return appProjectTypes;
	}

	/**
	 * 适应客户端返回数据格式
	 * @param planList
	 * @return
	 */
	private List<AppProjectListCustomize> convertToAppProjectList(List<HjhPlanCustomize> planList) {
		List<AppProjectListCustomize> appProjectList = new ArrayList<>();
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
				appProjectListCustomize.setBorrowAccountWait(entity.getAvailableInvestAccount());
				appProjectListCustomize.setStatusName(entity.getStatusName());
				appProjectListCustomize.setBorrowNid(entity.getPlanNid());
				appProjectListCustomize.setBorrowAccountWait(entity.getAvailableInvestAccount());
				String couponEnable = entity.getCouponEnable();
				if (org.apache.commons.lang.StringUtils.isEmpty(couponEnable) || "0".equals(couponEnable)) {
					couponEnable = "0";
				} else {
					couponEnable = "1";
				}
				appProjectListCustomize.setCouponEnable(couponEnable);
				// 项目详情url
				url = HOST + HomePageDefine.REQUEST_HOME + HjhPlanDefine.REQUEST_MAPPING + "/"
						+ entity.getPlanNid() ;
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
	 * 查询相应的项目分页列表
	 * @param form
	 * @return
	 */
	private JSONObject createProjectListPage(ProjectBean form, HttpServletRequest request) {
		JSONObject info = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();

		// 合规审批需求  add by huanghui 20181123 start
		info.put("riskWarningHint", "散标是经过汇盈金服对借款人进行信息搜集和资信评估后在汇盈金服平台发布的借款标的统称。请您充分了解标的信息，谨慎出借。");
		info.put("riskWarningContent", " $散标介绍$ " +
				"\n" +
				"散标是经过汇盈金服对具有借款需求的借款人进行信息搜集和资信评估后在汇盈金服平台发布的借款标的统称，包括但不限于实物抵押标、第三方保证标以及汇盈金服平台不时增加和发布的其他类型借款标。" +
				"\n\n" +
				" $出借人适当性管理告知$ " +
				"\n" +
				"作为网络借贷的出借人，应当具备出借风险意识，风险识别能力，拥有一定的金融产品出借经验并熟悉互联网金融。请您在出借前，确保了解借款项目的主要风险，同时确认具有相应的风险认知和承受能力，并自行承担出借可能产生的相关损失。");

		// 合规审批需求  add by huanghui 20181123 end

		if (form.getProjectType().equals("XSH")) {
            params.put("projectType", "HZT");
            params.put("type", "4");
        } else if (form.getProjectType().equals("ZXH")) {
            params.put("projectType", "HZT");
            params.put("type", "11");
        }else if("HZT".equals(form.getProjectType())){
		    params.put("projectType", "CFH");
		}else if ("HZR".equals(form.getProjectType())) {
			// 汇转让出借列表
			info = this.getCreditTenderList(form, request);
			return info;
		}else if ("RTB".equals(form.getProjectType()) || "HJLC".equals(form.getProjectType())) { // 融通宝
			params.put("projectType", "HZT");
			params.put("type", "13");
		}else if("ZQ".equals(form.getProjectType())) {
		    // 新的债权列表（包含汇直投、尊享汇、融通宝）
		    params.put("projectType", "ZQ");
		}else if("XS".equals(form.getProjectType())) {
		    // 新手汇
		    params.put("projectType", "HZT");
            params.put("type", "4");
        }else {
			params.put("projectType", form.getProjectType());
		}
		params.put("host", HOST_URL + ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.PROJECT_DETAIL_ACTION);

		// 汇盈金服app列表定向标过滤
		params.put("publishInstCode", CustomConstants.HYJF_INST_CODE);

        // 统计相应的汇直投的数据记录数
		int projecTotal = this.projectService.countProjectListRecordTotal(params);
//		if (projecTotal > ProjectDefine.PROJEC_TOTAL) {
//			projecTotal = ProjectDefine.PROJEC_TOTAL;
//		}
		// 李深强修改 by明举 所有产品列表只查2页之内的数据
		if(projecTotal > form.getPageSize()){
			projecTotal = form.getPageSize();
		}
		if (projecTotal > 0) {
			// 查询相应的汇直投列表数据
			int limit = form.getPageSize();
			int page = form.getPage();
			int offSet = (page - 1) * limit;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<AppProjectListCustomize> list = projectService.searchProjectList(params);
			//add by fuqiang app改版 start
			List<AppProjectTypeCustomize> projectList = this.convertToAppProjectType(list);
			//add by fuqiang app改版 end

			info.put("projectList", CommonUtils.convertNullToEmptyString(projectList));
//			info.put("projectType", form.getProjectType());
			info.put("projecTotal", String.valueOf(projecTotal));
		} else {
			info.put("projectList", new ArrayList<AppProjectListCustomize>());
//			info.put("projectType", form.getProjectType());
			info.put("projecTotal", "0");
		}
		return info;
	}

	/**
	 * 适应客户端数据返回
	 * @param list
	 * @return
	 */
	private List<AppProjectTypeCustomize> convertToAppProjectType(List<AppProjectListCustomize> list) {
		List<AppProjectTypeCustomize> appProjectTypes = new ArrayList<>();
		for(AppProjectListCustomize listCustomize : list){
			AppProjectTypeCustomize appProjectType = new AppProjectTypeCustomize();
			appProjectType.setBorrowNid(listCustomize.getBorrowNid());
			appProjectType.setBorrowName(listCustomize.getBorrowNid());
			appProjectType.setBorrowDesc(listCustomize.getBorrowDesc());
			appProjectType.setBorrowTheFirst(listCustomize.getBorrowApr() + "%");
			appProjectType.setBorrowTheFirstDesc("历史年回报率");
			appProjectType.setBorrowTheSecond(listCustomize.getBorrowPeriod());
			appProjectType.setBorrowTheSecondDesc("项目期限");
			String status = listCustomize.getStatus();
			// 设置产品加息 显示收益率
	        if (Validator.isIncrease(listCustomize.getIncreaseInterestFlag(), listCustomize.getBorrowExtraYieldOld())) {
	            appProjectType.setBorrowExtraYield(listCustomize.getBorrowExtraYield());
	        }else{
	            appProjectType.setBorrowExtraYield("");
	        }
			if (status.equals("10")){
				//剩余发标时间
				//String timeLeft = projectService.getTimeLeft(listCustomize.getBorrowNid());
				//SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				//String timeFormat = formatter.format(Long.valueOf(timeLeft));
				// 不显示倒计时，换开标时间
				appProjectType.setStatusName(listCustomize.getOnTime());
				//可投金额
				String borrowAccountWait = projectService.getBorrowAccountWait(listCustomize.getBorrowNid());
                borrowAccountWait = CommonUtils.formatAmount(borrowAccountWait);
                    appProjectType.setStatusNameDesc(StringUtils.isBlank(borrowAccountWait)?"":"剩余" + borrowAccountWait);
			}else if(status.equals("11")){
				appProjectType.setStatusName("立即出借");
				//可投金额
				String borrowAccountWait = projectService.getBorrowAccountWait(listCustomize.getBorrowNid());
                borrowAccountWait = CommonUtils.formatAmount(borrowAccountWait);
                    appProjectType.setStatusNameDesc(StringUtils.isBlank(borrowAccountWait)?"":"剩余" + borrowAccountWait);
			}else if (status.equals("12")){
				appProjectType.setStatusName("复审中");
			}else if (status.equals("13")){
				appProjectType.setStatusName("还款中");
			}else if (status.equals("14")){
				appProjectType.setStatusName("已还款");
			}
			appProjectType.setBorrowUrl(HOST_URL + BorrowProjectDefine.REQUEST_MAPPING + "/" + listCustomize.getBorrowNid());
			appProjectType.setStatus(listCustomize.getStatus());
			appProjectType.setOnTime(listCustomize.getOnTime());

//			String projectType = projectService.getProjectType(listCustomize.getBorrowType());
			if ("ZXH".equals(listCustomize.getBorrowType())){
				appProjectType.setMark("尊享");
			}else if("RTB".equals(listCustomize.getBorrowType())){
				appProjectType.setMark("优选");
			}
			appProjectType.setBorrowType(listCustomize.getBorrowType());
			// 应客户端要求，返回空串
			CommonUtils.convertNullToEmptyString(appProjectType);
			appProjectTypes.add(appProjectType);
		}
		return appProjectTypes;
	}

	/**
	 * 
	 * 获取汇转让出借列表
	 * 
	 * @author liuyang
	 * @param form
	 * @return
	 */
	private JSONObject getCreditTenderList(ProjectBean form, HttpServletRequest request) {
		JSONObject info = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();

		// 合规审批需求  add by huanghui 20181123 start
		info.put("riskWarningHint", "债权转让是债权持有人在汇盈金服平台将债权挂出并将所持有的债权转让给受让人的操作。请您充分了解标的信息，谨慎出借。");
		info.put("riskWarningContent", " $债权转让介绍$ " +
				"\n" +
				"债权持有人通过汇盈金服平台债权转让系统将债权挂出且与承接人签订债权转让协议，将所持有的债权转让给承接人的操作。" +
				"\n\n" +
				" $出借人适当性管理告知$ " +
				"\n" +
				"作为网络借贷的出借人，应当具备出借风险意识，风险识别能力，拥有一定的金融产品出借经验并熟悉互联网金融。请您在出借前，确保了解借款项目的主要风险，同时确认具有相应的风险认知和承受能力，并自行承担出借可能产生的相关损失。");
		// 合规审批需求  add by huanghui 20181123 end

		// 债转详情
		params.put("host", HOST_URL + AppTenderCreditDefine.REQUEST_HOME + AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.PROJECT_DETAIL_ACTION);
		// 统计汇转让出借列表数据
		int projectTotal = this.appTenderCreditService.countTenderCreditListRecordCount(params);
		// 周小帅修改 by明举 所有产品列表只查5页之内的数据
		// modify by liuyang 期限债转列表只显示两页限制 松超提成 start
//		int pageNum = 5;
//		if(projectTotal > form.getPageSize() * pageNum){
//			projectTotal = form.getPageSize() * pageNum;
//		}
		// modify by liuyang 期限债转列表只显示两页限制 松超提成 start
		
		if (projectTotal > 0) {
			// 查询相应的汇转让列表
			int limit = form.getPageSize();
			int page = form.getPage();
			int offSet = (page - 1) * limit;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			// 债转状态
			params.put("creditStatus", "0");
			// 查询债转列表
			List<AppProjectListCustomize> resultList = this.appTenderCreditService.searchTenderCreditList(params);

			List<AppProjectTypeCustomize> list = convertToAppProjectHZRType(resultList, request);
			info.put("projectList", list);
			info.put("projecTotal", String.valueOf(projectTotal));
		} else {
			info.put("projectList", new ArrayList<AppProjectListCustomize>());
			info.put("projecTotal", "0");
		}

//		info.put("projectType", "HZR");
		return info;
	}

	/**
	 * 适应客户端返回数据
	 * @param resultList
	 * @return
	 */
	private List<AppProjectTypeCustomize> convertToAppProjectHZRType(List<AppProjectListCustomize> resultList, HttpServletRequest request) {
		List<AppProjectTypeCustomize> appProjectTypes = new ArrayList<>();
		for(AppProjectListCustomize listCustomize : resultList){
			AppProjectTypeCustomize appProjectType = new AppProjectTypeCustomize();
			appProjectType.setBorrowNid(listCustomize.getBorrowNid());
			appProjectType.setBorrowName(listCustomize.getBorrowNid());
			appProjectType.setBorrowDesc(listCustomize.getBorrowDesc());
			appProjectType.setBorrowTheFirst(listCustomize.getBorrowApr() + "%");
			appProjectType.setBorrowTheFirstDesc("历史年回报率");
			String borrowNid = listCustomize.getBorrowNid();
			String creditNid = borrowNid.substring(3);
			BorrowCredit creditByBorrowNid = appTenderCreditService.selectCreditTenderByCreditNid(creditNid);
			AppTenderToCreditDetailCustomize tender = appTenderCreditService.selectCreditTenderDetail(creditNid);
			if (tender != null) {
					appProjectType.setBorrowTheSecond(String.valueOf(tender.getCreditDiscount())  + "%");
			}
			appProjectType.setBorrowTheSecondDesc("折让率");
			appProjectType.setBorrowTheThird(listCustomize.getBorrowPeriod() + "天");
			appProjectType.setBorrowTheThirdDesc("项目期限");
			String status = listCustomize.getStatus();
			if (status.equals("11")) {
				appProjectType.setStatusName("立即承接");
				//可投金额
				String borrowAccountWait = String.valueOf(creditByBorrowNid.getCreditCapital().subtract(creditByBorrowNid.getCreditCapitalAssigned()));
//				appProjectType.setStatusNameDesc(StringUtils.isBlank(borrowAccountWait)?"":"剩余" + insertComma(borrowAccountWait, 2));
                borrowAccountWait = CommonUtils.formatAmount(borrowAccountWait);
                    appProjectType.setStatusNameDesc(StringUtils.isBlank(borrowAccountWait)?"":"剩余" + borrowAccountWait);
			}
			/*if (status.equals("10")){
				//剩余发标时间
				String timeLeft = projectService.getTimeLeft(listCustomize.getBorrowNid());
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				String timeFormat = formatter.format(Long.valueOf(timeLeft));
				appProjectType.setStatusName(timeFormat);
				//可投金额
				String borrowAccountWait = projectService.getBorrowAccountWait(listCustomize.getBorrowNid());
				appProjectType.setStatusNameDesc("剩余" + borrowAccountWait);
			}else if(status.equals("11")){
				appProjectType.setStatusName("立即出借");
				//可投金额
				String borrowAccountWait = projectService.getBorrowAccountWait(listCustomize.getBorrowNid());
				appProjectType.setStatusNameDesc("剩余" + borrowAccountWait);
			}else if (status.equals("12")){
				appProjectType.setStatusName("复审中");
			}else if (status.equals("13")){
				appProjectType.setStatusName("还款中");
			}else if (status.equals("14")){
				appProjectType.setStatusName("已还款");
			}*/
			//appProjectType.setBorrowUrl(CommonUtils.concatReturnUrl(request, HOST_URL + AppTransferDefine.REQUEST_MAPPING + "/" + creditNid));
			appProjectType.setBorrowUrl(HOST_URL + AppTransferDefine.REQUEST_MAPPING + "/" + creditNid);
			appProjectType.setStatus(listCustomize.getStatus());
			appProjectType.setOnTime(listCustomize.getOnTime());

//			String projectType = projectService.getProjectType(listCustomize.getBorrowType());
			if ("ZXH".equals(listCustomize.getBorrowType())){
				appProjectType.setMark("尊享");
			}else if("RTB".equals(listCustomize.getBorrowType())){
				appProjectType.setMark("优选");
			}
			appProjectType.setBorrowType(listCustomize.getBorrowType());
			CommonUtils.convertNullToEmptyString(appProjectType);
			appProjectTypes.add(appProjectType);
		}
		return appProjectTypes;
	}

	/**
	 * 查询相应的项目详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ProjectDefine.PROJECT_DETAIL_ACTION)
	public ModelAndView searchProjectDetail(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(ProjectDefine.PROJECT_DETAIL_PTAH);
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		String platform = request.getParameter("realPlatform");
		String token = request.getParameter("token");
		String sign = request.getParameter("sign");
		String randomString = request.getParameter("randomString");
		String order = request.getParameter("order");
		String version = request.getParameter("version");
		System.out.println("version: " + version);
		if (StringUtils.isBlank(platform)) {
			platform = request.getParameter("platform");
		}
		modelAndView.addObject("platform", platform);
		if (StringUtils.isNotEmpty(token)) {
			modelAndView.addObject("token", strEncode(token));
		}
		modelAndView.addObject("sign", sign);
		modelAndView.addObject("randomString", randomString);
		modelAndView.addObject("order", strEncode(order));
		// 2.根据项目标号获取相应的项目信息
		ProjectDetailBean projectDeatil = new ProjectDetailBean();
		AppProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);
		if (borrow == null) {
			modelAndView = new ModelAndView(ProjectDefine.ERROR_PTAH);
			return modelAndView;
		} else {
			// 去最小值 最大可投和 项目可投
			BeanUtils.copyProperties(borrow, projectDeatil);
			if (projectDeatil.getType().equals("9")) {// 如果项目为汇资产项目
				// 3添加相应的url
				// projectDeatil.setTabOneName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_PROJECTINFO);//项目信息
				projectDeatil.setTabOneName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_RISKCONTROLL);// 风控信息
				projectDeatil.setTabOneUrl(HOST_URL + ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.PROJECT_INFO_ACTION);
				projectDeatil.setTabTwoName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_DISPOSALPLAN); // 处置预案
				projectDeatil.setTabTwoUrl(HOST_URL + ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.DISPOSAL_PLAN_ACTION);
				projectDeatil.setTabThreeName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_FILES); // 相关文件
				projectDeatil.setTabThreeUrl(HOST_URL + ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.FILES_ACTION);
				projectDeatil.setTabFourName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_INVEST);// 出借记录
				projectDeatil.setTabFourUrl(HOST_URL + ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.PROJECT_INVEST_ACTION);
				projectDeatil.setTabFiveName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_QUESTION);// 常见问题
				// projectDeatil.setTabFiveUrl(ProjectDefine.);
				
				// 用户未登录
				if (StringUtils.isEmpty(token)) {
					projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + ProjectDefine.USER_LOGIN_URL);
					modelAndView.addObject("loginFlag", 0);
				} else {// 用户已经登陆
					modelAndView.addObject("loginFlag", 1);
					Integer userId = SecretUtil.getUserId(sign);
					Users user = this.projectService.searchLoginUser(userId);
					modelAndView.addObject("isSetPassword", user.getIsSetPassword());
					if (user.getBankOpenAccount() == 0) {//modify by cwyang 2017-5-8 将汇付开户标示改为江西银行开户标示
						String mobile = user.getMobile();
						String url = HOST_URL + ProjectDefine.REQUEST_HOME + OpenAccountDefine.REQUEST_MAPPING + OpenAccountDefine.BANKOPEN_OPEN_ACTION;
						modelAndView.addObject("openAccountUrl", url);
						modelAndView.addObject("mobile", mobile);
						projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + ProjectDefine.USER_OPEN_URL);
					} else {
						projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + ProjectDefine.USER_INVEST_URL);
					}
					// add APP1.3.5改版 未登录用户或已登录用户但未出借过项目的用户不能看到相关文件 by zhangjp
					// start
					if (StringUtils.equals("13", borrow.getStatus()) && !this.projectService.checkTenderByUser(userId, borrowNid)) {
						// 项目状态为还款中并且用户未出借过该项目，则相关文件不显示
						modelAndView.addObject("fileVisible", 0);
					}
					// add APP1.3.5改版 未登录用户或已登录用户但未出借过项目的用户不能看到相关文件 by zhangjp
					// end

					if(this.projectService.checkTenderByUser(userId, borrowNid)){
						modelAndView.addObject("investFlag", 1);
					}else{
						modelAndView.addObject("investFlag", 0);
					}
				}
				// 添加相应的项目详情信息
				modelAndView.addObject("projectDeatil", projectDeatil);

				// 4查询相应的汇资产的首页信息
				AppHzcProjectDetailCustomize borrowInfo = this.projectService.searchHzcProjectDetail(borrowNid);
				modelAndView.addObject("borrowInfo", borrowInfo);

				// 5查询相应的还款计划
				List<RepayPlanBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
				modelAndView.addObject("repayPlanList", repayPlanList);
				// 6获取处置预案
				AppHzcDisposalPlanCustomize disposalPlan = this.projectService.searchDisposalPlan(borrowNid);
				modelAndView.addObject("disposalPlan", disposalPlan);
				
				List<ProjectFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
				modelAndView.addObject("fileList", files);
			} else {// 项目为非汇资产项目
				// 3添加相应的url
				// projectDeatil.setTabOneName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_PROJECTINFO);//项目信息
				projectDeatil.setTabOneName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_RISKCONTROLL);// 风控信息
				projectDeatil.setTabOneUrl(ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.PROJECT_INFO_ACTION);
				projectDeatil.setTabTwoName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_RISKCONTROLL); // 处置预案
				projectDeatil.setTabTwoUrl(ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.RISK_CONTROL_ACTION);
				projectDeatil.setTabThreeName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_FILES); // 相关文件
				projectDeatil.setTabThreeUrl(ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.FILES_ACTION);
				projectDeatil.setTabFourName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_INVEST);// 出借记录
				projectDeatil.setTabFourUrl(ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.PROJECT_INVEST_ACTION);
				projectDeatil.setTabFiveName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_QUESTION);// 常见问题
				// projectDeatil.setTabFiveUrl(ProjectDefine.);
				if (projectDeatil.getProjectType().equals("HXF")) {
					modelAndView.addObject("consumeUrl", HOST_URL + ProjectDefine.REQUEST_HOME + ProjectDefine.REQUEST_MAPPING + ProjectDefine.PROJECT_CONSUME_ACTION);
				}
				// 用户未登录
				if (StringUtils.isEmpty(token)) {
					projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + ProjectDefine.USER_LOGIN_URL);
					modelAndView.addObject("loginFlag", 0);
				} else {// 用户已经登陆
					modelAndView.addObject("loginFlag", 1);
					Integer userId = SecretUtil.getUserId(sign);
					Users user = this.projectService.searchLoginUser(userId);
					modelAndView.addObject("isSetPassword", user.getIsSetPassword());
					if (user.getBankOpenAccount() == 0) {//modify by cwyang 2017-5-8 将汇付开户标示改为江西银行开户标示
						String mobile = user.getMobile();
						String url = HOST_URL + ProjectDefine.REQUEST_HOME + OpenAccountDefine.REQUEST_MAPPING + OpenAccountDefine.BANKOPEN_OPEN_ACTION;
						modelAndView.addObject("openAccountUrl", url);
						modelAndView.addObject("mobile", mobile);
						projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + ProjectDefine.USER_OPEN_URL);
					} else {
						projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + ProjectDefine.USER_INVEST_URL);
					}
					// add APP1.3.5改版 未登录用户或项目状态为还款中且未出借过项目的用户不能看到相关文件 by
					// zhangjp start
					if (StringUtils.equals("13", borrow.getStatus()) && !this.projectService.checkTenderByUser(userId, borrowNid)) {
						// 项目状态为还款中并且用户未出借过该项目，则相关文件不显示
						modelAndView.addObject("fileVisible", 0);
					}
					// add APP1.3.5改版 未登录用户或项目状态为还款中且未出借过项目的用户不能看到相关文件 by
					// zhangjp end
					if(this.projectService.checkTenderByUser(userId, borrowNid)){
						modelAndView.addObject("investFlag", 1);
					}else{
						modelAndView.addObject("investFlag", 0);
					}
				}
				// 添加相应的项目详情信息
				modelAndView.addObject("projectDeatil", projectDeatil);
				// 4查询非汇资产项目的项目信息
				if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("1")) {
					// 查询相应的企业项目详情
					AppProjectCompanyDetailCustomize borrowInfo = projectService.searchProjectCompanyDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				} else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
					// 查询相应的汇直投个人项目详情
					AppProjectPersonDetailCustomize borrowInfo = projectService.searchProjectPersonDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				}
				// 5查询相应的认证信息
				List<AppProjectAuthenInfoCustomize> authenList = projectService.searchProjectAuthenInfos(borrowNid);
				modelAndView.addObject("authenList", authenList);
				// 6查询相应的还款计划
				List<RepayPlanBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
				modelAndView.addObject("repayPlanList", repayPlanList);
				// 7查询风控信息
				AppRiskControlCustomize riskControl = this.projectService.selectRiskControl(borrowNid);
				riskControl.setControlMeasures(riskControl.getControlMeasures().replace("\r\n", ""));
				riskControl.setControlMort(riskControl.getControlMort().replace("\r\n", ""));
				modelAndView.addObject("riskControl", riskControl);
				// 8添加相应的房产信息
				List<AppMortgageCustomize> mortgageList = this.projectService.selectMortgageList(borrowNid);
				modelAndView.addObject("mortgageList", mortgageList);
				// 9添加相应的汽车抵押信息
				List<BorrowCarinfo> vehiclePledgeList = this.projectService.selectBorrowCarInfo(borrowNid);
				modelAndView.addObject("vehiclePledgeList", vehiclePledgeList);
				for(BorrowCarinfo carInfo : vehiclePledgeList){
				    carInfo.setNumber(AsteriskProcessUtil.getAsteriskedValue(carInfo.getNumber(), 2, 4));
				    carInfo.setVin(AsteriskProcessUtil.getAsteriskedValue(carInfo.getVin(), 4, 5));
				}
				
				
				List<ProjectFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
				modelAndView.addObject("fileList", files);
			}

			LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_DETAIL_ACTION);
			return modelAndView;
		}

	}

	/**
	 * 查询相应的项目信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ProjectDefine.PROJECT_INFO_ACTION, produces = "application/json; charset=utf-8")
	public String searchProjectInfo(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_DETAIL_ACTION);
		JSONObject info = new JSONObject();
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 2.根据项目标号获取相应的项目信息
		AppProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);
		if (borrow.getType().equals("9")) {// 如果项目为汇资产项目
			// 4查询相应的汇资产的首页信息
			AppHzcProjectDetailCustomize borrowInfo = this.projectService.searchHzcProjectDetail(borrowNid);
			info.put("borrowInfo", borrowInfo);
			// 5查询相应的还款计划
			List<RepayPlanBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
			info.put("repayPlanList", repayPlanList);
		} else {// 项目为非汇资产项目
			// 4查询非汇资产项目的项目信息
			if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("1")) {
				// 查询相应的企业项目详情
				AppProjectCompanyDetailCustomize borrowInfo = projectService.searchProjectCompanyDetail(borrowNid);
				info.put("borrowInfo", borrowInfo);
			} else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
				// 查询相应的汇直投个人项目详情
				AppProjectPersonDetailCustomize borrowInfo = projectService.searchProjectPersonDetail(borrowNid);
				info.put("borrowInfo", borrowInfo);
			}
			// 5查询相应的认证信息
			List<AppProjectAuthenInfoCustomize> authenList = projectService.searchProjectAuthenInfos(borrowNid);
			info.put("authenList", authenList);
			// 6查询相应的还款计划
			List<RepayPlanBean> repayPlanList = this.projectService.getRepayPlan(borrowNid);
			info.put("repayPlanList", repayPlanList);
		}
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_DETAIL_ACTION);
		return JSONObject.toJSONString(info, true);

	}

	/**
	 * 查询相应的处置预案
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ProjectDefine.DISPOSAL_PLAN_ACTION, produces = "application/json; charset=utf-8")
	public String searchDisposalPlan(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_DETAIL_ACTION);
		JSONObject info = new JSONObject();
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		AppHzcDisposalPlanCustomize disposalPlan = this.projectService.searchDisposalPlan(borrowNid);
		info.put("disposalPlan", disposalPlan);
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_DETAIL_ACTION);
		return JSONObject.toJSONString(info, true);

	}

	/**
	 * 查询相应的风控信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = ProjectDefine.RISK_CONTROL_ACTION, produces = "application/json; charset=utf-8")
	public String searchRiskControl(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_DETAIL_ACTION);
		String borrowNid = request.getParameter("borrowNid");
		// 2.根据项目标号获取相应的项目信息
		AppProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);
		JSONObject info = new JSONObject();
		AppRiskControlCustomize riskControl = this.projectService.selectRiskControl(borrowNid);
		riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
		riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
		// 添加风控信息
		info.put("riskControl", riskControl);
		List<AppMortgageCustomize> mortgageList = this.projectService.selectMortgageList(borrowNid);
		// 添加相应的房产信息
		info.put("mortgageList", mortgageList);
		List<AppVehiclePledgeCustomize> vehiclePledgeList = this.projectService.selectVehiclePledgeList(borrowNid);
		// 添加相应的汽车抵押信息
		info.put("vehiclePledgeList", vehiclePledgeList);
		if (borrow.getProjectType().equals("HXF")) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("borrowNid", borrowNid);
			int consumeTotal = this.projectService.countProjectConsumeListRecordTotal(params);
			if (consumeTotal > 0) {
				// 查询相应的汇直投列表数据
				ProjectConsumeBean form = new ProjectConsumeBean();
				int limit = form.getPageSize();
				int page = form.getPage();
				int offSet = (page - 1) * limit;
				if (offSet == 0 || offSet > 0) {
					params.put("limitStart", offSet);
				}
				if (limit > 0) {
					params.put("limitEnd", limit);
				}
				List<AppProjectConsumeCustomize> consumeList = this.projectService.selectProjectConsumeList(params);
				// 添加相应的汇消费债券信息
				info.put("consumeList", consumeList);
				// 添加相应的汇消费债券信息
				info.put("consumeTotal", String.valueOf(consumeTotal));
			} else {
				// 添加相应的汇消费债券信息
				info.put("consumeTotal", "0");
				// 添加相应的汇消费债券信息
				info.put("consumeList", new ArrayList<AppProjectConsumeCustomize>());
			}
		}
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_DETAIL_ACTION);
		return JSONObject.toJSONString(info, true);

	}

	/***
	 * 查询相应的汇消费项目的打包数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = ProjectDefine.PROJECT_CONSUME_ACTION, produces = "application/json; charset=utf-8")
	public String searchProjectConsumeList(@ModelAttribute ProjectConsumeBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_CONSUME_ACTION);
		JSONObject info = new JSONObject();
		this.createProjectConsumePage(info, form);
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_CONSUME_ACTION);
		return JSONObject.toJSONString(info, true);
	}

	/**
	 * 创建相应的汇消费项目的打包信息
	 * 
	 * @param info
	 * @param form
	 */

	private void createProjectConsumePage(JSONObject info, ProjectConsumeBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		int recordTotal = this.projectService.countProjectConsumeRecordTotal(params);
		if (recordTotal > 0) {
			// 查询相应的汇直投列表数据
			int limit = form.getPageSize();
			int page = form.getPage();
			int offSet = (page - 1) * limit;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<AppProjectConsumeCustomize> recordList = projectService.searchProjectConsumeList(params);
			info.put("consumeList", recordList);
			info.put("consumeTotal", String.valueOf(recordTotal));
		} else {
			// 添加相应的汇消费债券信息
			info.put("consumeTotal", new ArrayList<AppProjectConsumeCustomize>());
			// 添加相应的汇消费债券信息
			info.put("consumeList", "");
		}
	}

	/**
	 * 查询相应的项目的出借列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = ProjectDefine.PROJECT_INVEST_ACTION, produces = "application/json; charset=utf-8")
	public String searchProjectInvestList(@ModelAttribute ProjectInvestBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_INVEST_ACTION);
		JSONObject info = new JSONObject();
		this.createProjectInvestPage(info, form);
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.PROJECT_INVEST_ACTION);
		return JSONObject.toJSONString(info, true);
	}

	/**
	 * 创建相应的项目的用户出借分页信息
	 * 
	 * @param info
	 * @param form
	 */

	private void createProjectInvestPage(JSONObject info, ProjectInvestBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		int recordTotal = this.projectService.countProjectInvestRecordTotal(params);
		if (recordTotal > 0) { // 查询相应的汇直投列表数据
			int limit = form.getPageSize();
			int page = form.getPage();
			int offSet = (page - 1) * limit;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<AppProjectInvestListCustomize> recordList = projectService.searchProjectInvestList(params);
			info.put("investList", recordList);
			info.put("investListTotal", String.valueOf(recordTotal));
		} else {
			info.put("investList", new ArrayList<AppProjectInvestListCustomize>());
			info.put("investListTotal", "0");
		}
	}

	/**
	 * 查询相应的项目的出借列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = ProjectDefine.FILES_ACTION, produces = "application/json; charset=utf-8")
	public String searchFilesList(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, ProjectDefine.FILES_ACTION);
		JSONObject info = new JSONObject();
		String borrowNid = request.getParameter("borrowNid");
		List<ProjectFileBean> files = this.projectService.searchProjectFiles(borrowNid, HOST_URL);
		info.put("fileList", files);
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(ProjectDefine.THIS_CLASS, ProjectDefine.FILES_ACTION);
		return JSONObject.toJSONString(info, true);
	}
}
