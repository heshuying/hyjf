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
package com.hyjf.app.hjhasset;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.bank.user.openaccount.OpenAccountDefine;
import com.hyjf.app.user.credit.AppTenderCreditService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.AsteriskProcessUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetJumpCommand;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.app.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = HjhProjectDefine.REQUEST_MAPPING)
public class HjhProjectController extends BaseController {

	@Autowired
	private HjhProjectService hjhProjectService;

	/** 债权转让Service */
	@Autowired
	private AppTenderCreditService appTenderCreditService;

	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

	/**
	 * 查询相应的项目详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = HjhProjectDefine.PROJECT_DETAIL_ACTION)
	public ModelAndView searchProjectDetail(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_DETAIL_ACTION);
		ModelAndView modelAndView = new ModelAndView(HjhProjectDefine.PROJECT_DETAIL_PTAH);
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
		HjhProjectDetailBean projectDeatil = new HjhProjectDetailBean();
		AppProjectDetailCustomize borrow = this.hjhProjectService.selectProjectDetail(borrowNid);
		if (borrow == null) {
			modelAndView = new ModelAndView(HjhProjectDefine.ERROR_PTAH);
			return modelAndView;
		} else {
			// 去最小值 最大可投和 项目可投
			BeanUtils.copyProperties(borrow, projectDeatil);
			if (projectDeatil.getType().equals("9")) {// 如果项目为汇资产项目
				// 3添加相应的url
				// projectDeatil.setTabOneName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_PROJECTINFO);//项目信息
				projectDeatil.setTabOneName(HjhProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_RISKCONTROLL);// 风控信息
				projectDeatil.setTabOneUrl(HOST_URL + HjhProjectDefine.REQUEST_HOME + HjhProjectDefine.REQUEST_MAPPING + HjhProjectDefine.PROJECT_INFO_ACTION);
				projectDeatil.setTabTwoName(HjhProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_DISPOSALPLAN); // 处置预案
				projectDeatil.setTabTwoUrl(HOST_URL + HjhProjectDefine.REQUEST_HOME + HjhProjectDefine.REQUEST_MAPPING + HjhProjectDefine.DISPOSAL_PLAN_ACTION);
				projectDeatil.setTabThreeName(HjhProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_FILES); // 相关文件
				projectDeatil.setTabThreeUrl(HOST_URL + HjhProjectDefine.REQUEST_HOME + HjhProjectDefine.REQUEST_MAPPING + HjhProjectDefine.FILES_ACTION);
				projectDeatil.setTabFourName(HjhProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_INVEST);// 出借记录
				projectDeatil.setTabFourUrl(HOST_URL + HjhProjectDefine.REQUEST_HOME + HjhProjectDefine.REQUEST_MAPPING + HjhProjectDefine.PROJECT_INVEST_ACTION);
				projectDeatil.setTabFiveName(HjhProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_QUESTION);// 常见问题
				// projectDeatil.setTabFiveUrl(ProjectDefine.);
				
				// 用户未登录
				if (StringUtils.isEmpty(token)) {
					projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + HjhProjectDefine.USER_LOGIN_URL);
					modelAndView.addObject("loginFlag", 0);
				} else {// 用户已经登陆
					modelAndView.addObject("loginFlag", 1);
					Integer userId = SecretUtil.getUserId(sign);
					Users user = this.hjhProjectService.searchLoginUser(userId);
					modelAndView.addObject("isSetPassword", user.getIsSetPassword());
					if (user.getBankOpenAccount() == 0) {//modify by cwyang 2017-5-8 将汇付开户标示改为江西银行开户标示
						String mobile = user.getMobile();
						String url = HOST_URL + HjhProjectDefine.REQUEST_HOME + OpenAccountDefine.REQUEST_MAPPING + OpenAccountDefine.BANKOPEN_OPEN_ACTION;
						modelAndView.addObject("openAccountUrl", url);
						modelAndView.addObject("mobile", mobile);
						projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + HjhProjectDefine.USER_OPEN_URL);
					} else {
						projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + HjhProjectDefine.USER_INVEST_URL);
					}
					// add APP1.3.5改版 未登录用户或已登录用户但未出借过项目的用户不能看到相关文件 by zhangjp
					// start
					if (StringUtils.equals("13", borrow.getStatus()) && !this.hjhProjectService.checkTenderByUser(userId, borrowNid)) {
						// 项目状态为还款中并且用户未出借过该项目，则相关文件不显示
						modelAndView.addObject("fileVisible", 0);
					}
					// add APP1.3.5改版 未登录用户或已登录用户但未出借过项目的用户不能看到相关文件 by zhangjp
					// end

					if(this.hjhProjectService.checkTenderByUser(userId, borrowNid)){
						modelAndView.addObject("investFlag", 1);
					}else{
						modelAndView.addObject("investFlag", 0);
					}
				}
				// 添加相应的项目详情信息
				modelAndView.addObject("projectDeatil", projectDeatil);

				// 4查询相应的汇资产的首页信息
				AppHzcProjectDetailCustomize borrowInfo = this.hjhProjectService.searchHzcProjectDetail(borrowNid);
				modelAndView.addObject("borrowInfo", borrowInfo);

				// 5查询相应的还款计划
				List<HjhProjectRepayPlanBean> repayPlanList = this.hjhProjectService.getRepayPlan(borrowNid);
				modelAndView.addObject("repayPlanList", repayPlanList);
				// 6获取处置预案
				AppHzcDisposalPlanCustomize disposalPlan = this.hjhProjectService.searchDisposalPlan(borrowNid);
				modelAndView.addObject("disposalPlan", disposalPlan);
				
				List<HjhProjectFileBean> files = this.hjhProjectService.searchProjectFiles(borrowNid, HOST_URL);
				modelAndView.addObject("fileList", files);
			} else {// 项目为非汇资产项目
				// 3添加相应的url
				// projectDeatil.setTabOneName(ProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_PROJECTINFO);//项目信息
				projectDeatil.setTabOneName(HjhProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_RISKCONTROLL);// 风控信息
				projectDeatil.setTabOneUrl(HjhProjectDefine.REQUEST_HOME + HjhProjectDefine.REQUEST_MAPPING + HjhProjectDefine.PROJECT_INFO_ACTION);
				projectDeatil.setTabTwoName(HjhProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_RISKCONTROLL); // 处置预案
				projectDeatil.setTabTwoUrl(HjhProjectDefine.REQUEST_HOME + HjhProjectDefine.REQUEST_MAPPING + HjhProjectDefine.RISK_CONTROL_ACTION);
				projectDeatil.setTabThreeName(HjhProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_FILES); // 相关文件
				projectDeatil.setTabThreeUrl(HjhProjectDefine.REQUEST_HOME + HjhProjectDefine.REQUEST_MAPPING + HjhProjectDefine.FILES_ACTION);
				projectDeatil.setTabFourName(HjhProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_INVEST);// 出借记录
				projectDeatil.setTabFourUrl(HjhProjectDefine.REQUEST_HOME + HjhProjectDefine.REQUEST_MAPPING + HjhProjectDefine.PROJECT_INVEST_ACTION);
				projectDeatil.setTabFiveName(HjhProjectDefine.PROJECT_DETAIL_CONSTANT_TABNAME_QUESTION);// 常见问题
				// projectDeatil.setTabFiveUrl(ProjectDefine.);
				if (projectDeatil.getProjectType().equals("HXF")) {
					modelAndView.addObject("consumeUrl", HOST_URL + HjhProjectDefine.REQUEST_HOME + HjhProjectDefine.REQUEST_MAPPING + HjhProjectDefine.PROJECT_CONSUME_ACTION);
				}
				// 用户未登录
				if (StringUtils.isEmpty(token)) {
					projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + HjhProjectDefine.USER_LOGIN_URL);
					modelAndView.addObject("loginFlag", 0);
				} else {// 用户已经登陆
					modelAndView.addObject("loginFlag", 1);
					Integer userId = SecretUtil.getUserId(sign);
					Users user = this.hjhProjectService.searchLoginUser(userId);
					modelAndView.addObject("isSetPassword", user.getIsSetPassword());
					if (user.getBankOpenAccount() == 0) {//modify by cwyang 2017-5-8 将汇付开户标示改为江西银行开户标示
						String mobile = user.getMobile();
						String url = HOST_URL + HjhProjectDefine.REQUEST_HOME + OpenAccountDefine.REQUEST_MAPPING + OpenAccountDefine.BANKOPEN_OPEN_ACTION;
						modelAndView.addObject("openAccountUrl", url);
						modelAndView.addObject("mobile", mobile);
						projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + HjhProjectDefine.USER_OPEN_URL);
					} else {
						projectDeatil.setInvestUrl(GetJumpCommand.getLinkJumpPrefix(request, version) + HjhProjectDefine.USER_INVEST_URL);
					}
					// add APP1.3.5改版 未登录用户或项目状态为还款中且未出借过项目的用户不能看到相关文件 by
					// zhangjp start
					if (StringUtils.equals("13", borrow.getStatus()) && !this.hjhProjectService.checkTenderByUser(userId, borrowNid)) {
						// 项目状态为还款中并且用户未出借过该项目，则相关文件不显示
						modelAndView.addObject("fileVisible", 0);
					}
					// add APP1.3.5改版 未登录用户或项目状态为还款中且未出借过项目的用户不能看到相关文件 by
					// zhangjp end
					if(this.hjhProjectService.checkTenderByUser(userId, borrowNid)){
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
					AppProjectCompanyDetailCustomize borrowInfo = hjhProjectService.searchProjectCompanyDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				} else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
					// 查询相应的汇直投个人项目详情
					AppProjectPersonDetailCustomize borrowInfo = hjhProjectService.searchProjectPersonDetail(borrowNid);
					modelAndView.addObject("borrowInfo", borrowInfo);
				}
				// 5查询相应的认证信息
				List<AppProjectAuthenInfoCustomize> authenList = hjhProjectService.searchProjectAuthenInfos(borrowNid);
				modelAndView.addObject("authenList", authenList);
				// 6查询相应的还款计划
				List<HjhProjectRepayPlanBean> repayPlanList = this.hjhProjectService.getRepayPlan(borrowNid);
				modelAndView.addObject("repayPlanList", repayPlanList);
				// 7查询风控信息
				AppRiskControlCustomize riskControl = this.hjhProjectService.selectRiskControl(borrowNid);
				riskControl.setControlMeasures(riskControl.getControlMeasures().replace("\r\n", ""));
//				riskControl.setControlMort(riskControl.getControlMort().replace("\r\n", ""));
				modelAndView.addObject("riskControl", riskControl);
				// 8添加相应的房产信息
				List<AppMortgageCustomize> mortgageList = this.hjhProjectService.selectMortgageList(borrowNid);
				modelAndView.addObject("mortgageList", mortgageList);
				// 9添加相应的汽车抵押信息
				List<BorrowCarinfo> vehiclePledgeList = this.hjhProjectService.selectBorrowCarInfo(borrowNid);
				modelAndView.addObject("vehiclePledgeList", vehiclePledgeList);
				for(BorrowCarinfo carInfo : vehiclePledgeList){
				    carInfo.setNumber(AsteriskProcessUtil.getAsteriskedValue(carInfo.getNumber(), 2, 4));
				    carInfo.setVin(AsteriskProcessUtil.getAsteriskedValue(carInfo.getVin(), 4, 5));
				}
				
				
				List<HjhProjectFileBean> files = this.hjhProjectService.searchProjectFiles(borrowNid, HOST_URL);
				modelAndView.addObject("fileList", files);
			}

			LogUtil.endLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_DETAIL_ACTION);
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
	@RequestMapping(value = HjhProjectDefine.PROJECT_INFO_ACTION, produces = "application/json; charset=utf-8")
	public String searchProjectInfo(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_DETAIL_ACTION);
		JSONObject info = new JSONObject();
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		// 2.根据项目标号获取相应的项目信息
		AppProjectDetailCustomize borrow = this.hjhProjectService.selectProjectDetail(borrowNid);
		if (borrow.getType().equals("9")) {// 如果项目为汇资产项目
			// 4查询相应的汇资产的首页信息
			AppHzcProjectDetailCustomize borrowInfo = this.hjhProjectService.searchHzcProjectDetail(borrowNid);
			info.put("borrowInfo", borrowInfo);
			// 5查询相应的还款计划
			List<HjhProjectRepayPlanBean> repayPlanList = this.hjhProjectService.getRepayPlan(borrowNid);
			info.put("repayPlanList", repayPlanList);
		} else {// 项目为非汇资产项目
			// 4查询非汇资产项目的项目信息
			if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("1")) {
				// 查询相应的企业项目详情
				AppProjectCompanyDetailCustomize borrowInfo = hjhProjectService.searchProjectCompanyDetail(borrowNid);
				info.put("borrowInfo", borrowInfo);
			} else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
				// 查询相应的汇直投个人项目详情
				AppProjectPersonDetailCustomize borrowInfo = hjhProjectService.searchProjectPersonDetail(borrowNid);
				info.put("borrowInfo", borrowInfo);
			}
			// 5查询相应的认证信息
			List<AppProjectAuthenInfoCustomize> authenList = hjhProjectService.searchProjectAuthenInfos(borrowNid);
			info.put("authenList", authenList);
			// 6查询相应的还款计划
			List<HjhProjectRepayPlanBean> repayPlanList = this.hjhProjectService.getRepayPlan(borrowNid);
			info.put("repayPlanList", repayPlanList);
		}
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_DETAIL_ACTION);
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
	@RequestMapping(value = HjhProjectDefine.DISPOSAL_PLAN_ACTION, produces = "application/json; charset=utf-8")
	public String searchDisposalPlan(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_DETAIL_ACTION);
		JSONObject info = new JSONObject();
		// 1.获取项目标号
		String borrowNid = request.getParameter("borrowNid");
		AppHzcDisposalPlanCustomize disposalPlan = this.hjhProjectService.searchDisposalPlan(borrowNid);
		info.put("disposalPlan", disposalPlan);
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_DETAIL_ACTION);
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
	@RequestMapping(value = HjhProjectDefine.RISK_CONTROL_ACTION, produces = "application/json; charset=utf-8")
	public String searchRiskControl(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_DETAIL_ACTION);
		String borrowNid = request.getParameter("borrowNid");
		// 2.根据项目标号获取相应的项目信息
		AppProjectDetailCustomize borrow = this.hjhProjectService.selectProjectDetail(borrowNid);
		JSONObject info = new JSONObject();
		AppRiskControlCustomize riskControl = this.hjhProjectService.selectRiskControl(borrowNid);
		riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
		riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
		// 添加风控信息
		info.put("riskControl", riskControl);
		List<AppMortgageCustomize> mortgageList = this.hjhProjectService.selectMortgageList(borrowNid);
		// 添加相应的房产信息
		info.put("mortgageList", mortgageList);
		List<AppVehiclePledgeCustomize> vehiclePledgeList = this.hjhProjectService.selectVehiclePledgeList(borrowNid);
		// 添加相应的汽车抵押信息
		info.put("vehiclePledgeList", vehiclePledgeList);
		if (borrow.getProjectType().equals("HXF")) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("borrowNid", borrowNid);
			int consumeTotal = this.hjhProjectService.countProjectConsumeListRecordTotal(params);
			if (consumeTotal > 0) {
				// 查询相应的汇直投列表数据
				HjhProjectConsumeBean form = new HjhProjectConsumeBean();
				int limit = form.getPageSize();
				int page = form.getPage();
				int offSet = (page - 1) * limit;
				if (offSet == 0 || offSet > 0) {
					params.put("limitStart", offSet);
				}
				if (limit > 0) {
					params.put("limitEnd", limit);
				}
				List<AppProjectConsumeCustomize> consumeList = this.hjhProjectService.selectProjectConsumeList(params);
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
		LogUtil.endLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_DETAIL_ACTION);
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
	@RequestMapping(value = HjhProjectDefine.PROJECT_CONSUME_ACTION, produces = "application/json; charset=utf-8")
	public String searchProjectConsumeList(@ModelAttribute HjhProjectConsumeBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_CONSUME_ACTION);
		JSONObject info = new JSONObject();
		this.createProjectConsumePage(info, form);
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_CONSUME_ACTION);
		return JSONObject.toJSONString(info, true);
	}

	/**
	 * 创建相应的汇消费项目的打包信息
	 * 
	 * @param info
	 * @param form
	 */

	private void createProjectConsumePage(JSONObject info, HjhProjectConsumeBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		int recordTotal = this.hjhProjectService.countProjectConsumeRecordTotal(params);
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
			List<AppProjectConsumeCustomize> recordList = hjhProjectService.searchProjectConsumeList(params);
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
	@RequestMapping(value = HjhProjectDefine.PROJECT_INVEST_ACTION, produces = "application/json; charset=utf-8")
	public String searchProjectInvestList(@ModelAttribute HjhProjectInvestBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_INVEST_ACTION);
		JSONObject info = new JSONObject();
		this.createProjectInvestPage(info, form);
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.PROJECT_INVEST_ACTION);
		return JSONObject.toJSONString(info, true);
	}

	/**
	 * 创建相应的项目的用户出借分页信息
	 * 
	 * @param info
	 * @param form
	 */

	private void createProjectInvestPage(JSONObject info, HjhProjectInvestBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		int recordTotal = this.hjhProjectService.countProjectInvestRecordTotal(params);
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
			List<AppProjectInvestListCustomize> recordList = hjhProjectService.searchProjectInvestList(params);
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
	@RequestMapping(value = HjhProjectDefine.FILES_ACTION, produces = "application/json; charset=utf-8")
	public String searchFilesList(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.FILES_ACTION);
		JSONObject info = new JSONObject();
		String borrowNid = request.getParameter("borrowNid");
		List<HjhProjectFileBean> files = this.hjhProjectService.searchProjectFiles(borrowNid, HOST_URL);
		info.put("fileList", files);
		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		LogUtil.endLog(HjhProjectDefine.THIS_CLASS, HjhProjectDefine.FILES_ACTION);
		return JSONObject.toJSONString(info, true);
	}
}
