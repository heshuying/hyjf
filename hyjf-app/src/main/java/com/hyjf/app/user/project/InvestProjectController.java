/**
 * Description:我的出借控制器
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.app.user.project;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.project.ProjectDefine;
import com.hyjf.app.project.ProjectService;
import com.hyjf.app.user.credit.AppTenderCreditDefine;
import com.hyjf.app.user.credit.AppTenderCreditService;
import com.hyjf.app.user.credit.MyProjectResponse;
import com.hyjf.app.user.credit.MyProjectVo;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.user.assetmanage.AssetManageService;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.app.AppAlreadyRepayListCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectContractDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectContractRecoverPlanCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayListCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayPlanListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRepayPlanListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditListCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldObligatoryRightListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;

@Controller
@RequestMapping(value = InvestProjectDefine.REQUEST_MAPPING)
public class InvestProjectController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(InvestProjectController.class);
	@Autowired
	private ProjectService projectService;
	@Autowired
	private InvestProjectService investProjectService;
	@Autowired
	private AppTenderCreditService appTenderCreditService;
	@Autowired
	private AssetManageService assetManageService;

	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

	/**
	 * 获取我的债权
	 */
	@ResponseBody
	@RequestMapping(value = InvestProjectDefine.MYPROJECT_LIST_ACTION, produces = "application/json; charset=utf-8")
	public MyProjectResponse searchMyProjectList(HttpServletRequest request) {
		LogUtil.startLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.MYPROJECT_LIST_ACTION);
		MyProjectResponse response = new MyProjectResponse(InvestProjectDefine.MYPROJECT_LIST_REQUEST);
		response.setStatus(CustomConstants.APP_STATUS_SUCCESS);
		response.setStatusDesc(CustomConstants.APP_STATUS_DESC_SUCCESS);

		// 状态：1为当前持有，2为已回款，3为转让记录
		String type = request.getParameter("type");
		// 唯一标识
		String sign = request.getParameter("sign");
		String version = request.getParameter("version");

		// 检查参数正确性
		if (Validator.isNull(sign) || Validator.isNull(type) || !Arrays.asList("1", "2", "3").contains(type)) {
			response.setStatus(CustomConstants.APP_STATUS_FAIL);
			response.setStatusDesc("参数非法");
			return response;
		}
		Integer userId = SecretUtil.getUserId(sign);
		//Integer userId = Integer.valueOf(request.getParameter("userId"));
		// 构建查询条件
		Map<String, Object> params = buildQueryParameter(request);
		params.put("userId", userId);

		// 这2个不用了，在返回的时候拼接
		params.put("host", "");
		params.put("sign", sign);

		Account account = investProjectService.getAccount(userId);
		// 散标待收总额
		if (account != null) {
			response.setMoney(CommonUtils.formatAmount(version, account.getBankAwait()));
		}

		int count = 0;
		switch (type) {
		// 当前持有
		case "1":
			// 查询我的出借中债权总数
			count = this.assetManageService.selectCurrentHoldObligatoryRightListTotal(params);
			if (count > 0) {
				// 获取用户当前持有债权列表
				List<CurrentHoldObligatoryRightListCustomize> recordList =
						assetManageService.selectCurrentHoldObligatoryRightList(params);
				response.setProjectList(
						convertAppInvestListCustomizeToMyProjectVo(recordList, request, userId));
			} else {
				response.setProjectList(new ArrayList<MyProjectVo>());
			}
			break;

		case "2":
			// 查询我的回款总数
			count = investProjectService.countAlreadyRepayListRecordTotal(params);
			if (count > 0) {
				List<AppAlreadyRepayListCustomize> appAlreadyRepayListCustomizes = investProjectService
						.selectAlreadyRepayList(params);
				response.setProjectList(
						convertappAlreadyRepayListToMyProjectVo(appAlreadyRepayListCustomizes, request));
			} else {
				response.setProjectList(new ArrayList<MyProjectVo>());
			}
			break;

		case "3":
			// 查询我的债转记录总数
			params.put("countStatus", "2");
			count = appTenderCreditService.countCreditRecord(params);
			if (count > 0) {
				List<AppTenderCreditRecordListCustomize> project = appTenderCreditService
						.searchCreditRecordList(params);
				response.setProjectList(converCreditToMyProject(project, request));
			} else {
				response.setProjectList(new ArrayList<MyProjectVo>());
			}
			break;
		default:
			logger.error("传入参数type错误");
			response.setStatus(CustomConstants.APP_STATUS_FAIL);
			response.setStatusDesc(CustomConstants.APP_STATUS_DESC_FAIL);
		}
		response.setProjectTotal(count);
		return response;
	}


	/**
	 * 查询用户的出借项目列表还款中
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestProjectDefine.REPAY_LIST_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject searchRepayList(@ModelAttribute RepayListBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.REPAY_LIST_ACTION);
		JSONObject info = new JSONObject();
		// 唯一标识
		String sign = request.getParameter("sign");
		// 验证请求参数
		if (Validator.isNull(sign)) {
			info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			info.put(CustomConstants.APP_STATUS_DESC, "请求参数非法");
			info.put(CustomConstants.APP_REQUEST, InvestProjectDefine.REQUEST_HOME
					+ InvestProjectDefine.REQUEST_MAPPING + InvestProjectDefine.REPAY_LIST_ACTION);
			return info;
		}

		// 用户id
		Integer userId = SecretUtil.getUserId(sign);
		String host = HOST_URL + InvestProjectDefine.REQUEST_HOME + InvestProjectDefine.REQUEST_MAPPING
				+ InvestProjectDefine.REPAY_PROJECT_DETAIL_ACTION;
		String hostContact = HOST_URL + InvestProjectDefine.REQUEST_HOME + InvestProjectDefine.REQUEST_MAPPING
				+ InvestProjectDefine.REPAY_CONTACT_ACTION;

		// 调用接口获取数据
		String resultStr = investProjectService.getRepayData(String.valueOf(userId), host, hostContact,
				String.valueOf(form.getPage()), String.valueOf(form.getPageSize()));
		JSONObject resultJson = JSONObject.parseObject(resultStr);
		if (resultJson.getString("status").equals("0")) {
			info.put("projectList", resultJson.get("data"));
			info.put("projectTotal", resultJson.get("projectTotal"));
		} else {
			info.put("projectList", new ArrayList<AppRepayListCustomize>());
			info.put("projectTotal", "0");
		}

		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		info.put(CustomConstants.APP_REQUEST, InvestProjectDefine.REQUEST_HOME + InvestProjectDefine.REQUEST_MAPPING
				+ InvestProjectDefine.REPAY_LIST_ACTION);

		LogUtil.endLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.REPAY_LIST_ACTION);
		return info;
	}

	/**
	 * 查询用户的出借项目列表出借中
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestProjectDefine.INVEST_LIST_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject searchInvestList(@ModelAttribute InvestListBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.INVEST_LIST_ACTION);
		JSONObject info = new JSONObject();
		// 唯一标识
		String sign = request.getParameter("sign");

		// 验证请求参数
		if (Validator.isNull(sign)) {
			info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			info.put(CustomConstants.APP_STATUS_DESC, "请求参数非法");
			info.put(CustomConstants.APP_REQUEST, InvestProjectDefine.REQUEST_HOME
					+ InvestProjectDefine.REQUEST_MAPPING + InvestProjectDefine.INVEST_LIST_ACTION);
			return info;
		}

		// 用户id
		Integer userId = SecretUtil.getUserId(sign);
		String host = HOST_URL + InvestProjectDefine.REQUEST_HOME + InvestProjectDefine.REQUEST_MAPPING
				+ InvestProjectDefine.INVEST_PROJECT_DETAIL_ACTION;

		// 调用接口获取数据
		String resultStr = investProjectService.getInvestingData(String.valueOf(userId), host,
				String.valueOf(form.getPage()), String.valueOf(form.getPageSize()));
		JSONObject resultJson = JSONObject.parseObject(resultStr);
		if (resultJson.getString("status").equals("0")) {
			info.put("projectList", resultJson.get("data"));
			info.put("projectTotal", resultJson.get("projectTotal"));
		} else {
			info.put("projectList", new ArrayList<AppRepayListCustomize>());
			info.put("projectTotal", "0");
		}

		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		info.put(CustomConstants.APP_REQUEST, InvestProjectDefine.REQUEST_HOME + InvestProjectDefine.REQUEST_MAPPING
				+ InvestProjectDefine.INVEST_LIST_ACTION);
		LogUtil.endLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.INVEST_LIST_ACTION);
		return info;
	}

	/**
	 * 查询用户的出借项目列表已回款
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestProjectDefine.ALREADY_REPAY_LIST_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject searchAlreadyRepayList(@ModelAttribute AlreadyRepayListBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.ALREADY_REPAY_LIST_ACTION);
		JSONObject info = new JSONObject();
		// 唯一标识
		String sign = request.getParameter("sign");

		// 验证请求参数
		if (Validator.isNull(sign)) {
			info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			info.put(CustomConstants.APP_STATUS_DESC, "请求参数非法");
			info.put(CustomConstants.APP_REQUEST, InvestProjectDefine.REQUEST_HOME
					+ InvestProjectDefine.REQUEST_MAPPING + InvestProjectDefine.ALREADY_REPAY_LIST_ACTION);
			return info;
		}

		// 用户id
		Integer userId = SecretUtil.getUserId(sign);
		String host = HOST_URL + InvestProjectDefine.REQUEST_HOME + InvestProjectDefine.REQUEST_MAPPING
				+ InvestProjectDefine.REPAYED_PROJECT_DETAIL_ACTION;

		// 调用接口获取数据
		String resultStr = investProjectService.getRepayedData(String.valueOf(userId), host,
				String.valueOf(form.getPage()), String.valueOf(form.getPageSize()));
		JSONObject resultJson = JSONObject.parseObject(resultStr);
		if (resultJson.getString("status").equals("0")) {
			info.put("projectList", resultJson.get("data"));
			info.put("projectTotal", resultJson.get("projectTotal"));
		} else {
			info.put("projectList", new ArrayList<AppRepayListCustomize>());
			info.put("projectTotal", "0");
		}

		info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
		info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
		info.put(CustomConstants.APP_REQUEST, InvestProjectDefine.REQUEST_HOME + InvestProjectDefine.REQUEST_MAPPING
				+ InvestProjectDefine.ALREADY_REPAY_LIST_ACTION);
		LogUtil.endLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.ALREADY_REPAY_LIST_ACTION);
		return info;
	}


	/**
	 * 分期项目查看相应的还款信息
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = InvestProjectDefine.REPAY_PLAN_LIST_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject searchRepayPlanList(@ModelAttribute RepayPlanListBean form, HttpServletRequest request,
			HttpServletResponse response) {
		LogUtil.startLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.REPAY_PLAN_LIST_ACTION);
		JSONObject info = new JSONObject();
		// 唯一标识
		String sign = request.getParameter("sign");

		// add by hesy 优惠券相关 start
		String isCouponTender = request.getParameter("isCouponTender");
		LogUtil.infoLog(this.getClass().getName(), "searchRepayPlanList", "还款计划isCouponTender: " + isCouponTender);
		if (StringUtils.isEmpty(isCouponTender)) {
			isCouponTender = "0";
		}

		CouponConfig couponConfig = null;
		if (isCouponTender.equals("1")) {
			couponConfig = investProjectService.getCouponConfigByOrderId(form.getOrderId());
		}

		// add by hesy 优惠券相关 end

		// 用户id
		Integer userId = SecretUtil.getUserId(sign);
		form.setUserId(userId.toString());
		// add liuyang 债转还款计划 start
		if (StringUtils.isNotEmpty(form.getInvestType()) && "HZR".equals(form.getInvestType())) {
			info = this.searchCreditRepayPlanList(form, request, response);
			return info;
		}
		// add liuyang 债转还款计划 end
		Borrow borrow = this.investProjectService.selectBorrowByBorrowNid(form.getBorrowNid());
		if (borrow == null) {
			info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			info.put(CustomConstants.APP_STATUS_DESC, "未查询到相应的项目的信息");
			info.put(CustomConstants.APP_REQUEST, InvestProjectDefine.REQUEST_HOME
					+ InvestProjectDefine.REQUEST_MAPPING + InvestProjectDefine.REPAY_PLAN_LIST_ACTION);
			return info;
		} else {
			String borrowStyleStr = borrow.getBorrowStyle();
			info.put("projectName", borrow.getName());
			info.put("borrowName", borrow.getBorrowAssetNumber());
			info.put("projectType", form.getBorrowNid().substring(0, 3));
			BorrowStyle borrowStyle = this.investProjectService.selectBorrowStyleByStyle(borrowStyleStr);
			if (borrowStyle == null) {
				info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
				info.put(CustomConstants.APP_STATUS_DESC, "未查询到相应的项目的还款方式");
				info.put(CustomConstants.APP_REQUEST, InvestProjectDefine.REQUEST_HOME
						+ InvestProjectDefine.REQUEST_MAPPING + InvestProjectDefine.REPAY_PLAN_LIST_ACTION);
				return info;
			} else {
				if (isCouponTender.equals("1")) {
				    if(couponConfig == null){
				        info.put("repayStyle", borrowStyle.getName());
				    }else if(couponConfig.getCouponType() == 1){
				        info.put("repayStyle", "按天计息，到期还本还息");
				    }
				} else {
					info.put("repayStyle", borrowStyle.getName());
				}
				// modify by hesy 优惠券相关 start
				if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyleStr)
						|| CustomConstants.BORROW_STYLE_END.equals(borrowStyleStr)) {
					if (isCouponTender.equals("1")) {
						// 优惠券出借回款计划
						createCouponRepayRecoverListPage(info, form);
					} else {
						// 普通出借回款计划
						createRepayRecoverListPage(info, form);
					}
				} else {
					if (isCouponTender.equals("1")) {
						// 优惠券出借回款计划
						createCouponRepayRecoverListPage(info, form);
					} else {
						// 普通出借回款计划
						createRepayRecoverPlanListPage(info, form, borrowStyleStr);
					}
				}
				// modify by hesy 优惠券相关 end

				info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
				info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
				info.put(CustomConstants.APP_REQUEST, InvestProjectDefine.REQUEST_HOME
						+ InvestProjectDefine.REQUEST_MAPPING + InvestProjectDefine.REPAY_PLAN_LIST_ACTION);
				LogUtil.endLog(InvestProjectDefine.THIS_CLASS, InvestProjectDefine.REPAY_PLAN_LIST_ACTION);
				return info;
			}

		}

	}

	/**
	 * 创建相应的还款信息分页
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createRepayRecoverListPage(JSONObject info, RepayPlanListBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", form.getUserId());
		params.put("borrowNid", form.getBorrowNid());
		params.put("orderId", form.getOrderId());
		int recordTotal = this.investProjectService.countRepayRecoverListRecordTotal(params);
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
			List<AppRepayPlanListCustomize> recordList = investProjectService.selectRepayRecoverList(params);
			info.put("repayList", recordList);
			info.put("repayTotal", String.valueOf(recordTotal));
		} else {
			info.put("repayList", new ArrayList<AppRepayPlanListCustomize>());
			info.put("repayTotal", "0");
		}
		info.put("borrowNid", form.getBorrowNid());
	}

	/**
	 * 创建相应的还款信息分页
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createRepayRecoverPlanListPage(JSONObject info, RepayPlanListBean form, String borrowStyleStr) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", form.getUserId());
		params.put("borrowNid", form.getBorrowNid());
		params.put("orderId", form.getOrderId());
		params.put("borrowStyle", borrowStyleStr);
		int recordTotal = this.investProjectService.countRepayPlanListRecordTotal(params);
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
			List<AppRepayPlanListCustomize> recordList = investProjectService.selectRepayPlanList(params);
			info.put("repayList", recordList);
			info.put("repayTotal", String.valueOf(recordTotal));
		} else {
			info.put("repayList", new ArrayList<AppRepayPlanListCustomize>());
			info.put("repayTotal", "0");
		}
		info.put("borrowNid", form.getBorrowNid());
	}

	// 优惠券还款计划
	private void createCouponRepayRecoverListPage(JSONObject info, RepayPlanListBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", form.getUserId());
		params.put("borrowNid", form.getBorrowNid());
		params.put("orderId", form.getOrderId());
		int recordTotal = this.investProjectService.countCouponRepayRecoverListRecordTotal(params);
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
			String receivedInterest = investProjectService.selectReceivedInterest(params);
			LogUtil.errorLog(this.getClass().getName(), "createCouponRepayRecoverListPage", "优惠券还款计划已得收益："
					+ receivedInterest, null);
			List<AppRepayPlanListCustomize> recordList = investProjectService.selectCouponRepayRecoverList(params);
			info.put("money", receivedInterest);
			info.put("repayList", recordList);
			info.put("repayTotal", String.valueOf(recordTotal));
		} else {
			info.put("money", "0");
			info.put("repayList", new ArrayList<AppRepayPlanListCustomize>());
			info.put("repayTotal", "0");
		}
		info.put("borrowNid", form.getBorrowNid());
	}

	/**
	 * 查询相应的项目信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = InvestProjectDefine.REPAY_CONTACT_ACTION)
	public ModelAndView searchProjectInfo(@ModelAttribute ProjectContractDetailBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(ProjectDefine.THIS_CLASS, InvestProjectDefine.REPAY_CONTACT_ACTION);
		ModelAndView modelAndView = null;

		String sign = request.getParameter("sign"); // 随机字符串
		// 用户id
		Integer userId = SecretUtil.getUserId(sign);
		form.setUserId(userId.toString());
		// 1.获取项目标号
		String borrowNid = form.getBorrowNid();
		// 获取相应的orderId
		String orderId = form.getOrderId();
		// 2.根据项目标号获取相应的项目信息
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", borrowNid);
		params.put("orderId", orderId);
		params.put("userId", userId.toString());
		AppProjectContractDetailCustomize borrow = this.investProjectService.selectProjectContractDetail(params);
		if (borrow.getProjectType().equals("HXF")) {// 如果项目为汇消费项目
			modelAndView = new ModelAndView(InvestProjectDefine.PROJECT_HXF_CONTRACT_PTAH);
		} else if (borrow.getProjectType().equals("RTB")) {
			AppProjectDetailCustomize borrow1 = this.projectService.selectProjectDetail(form.getBorrowNid());
			if (borrow != null && borrow1.getBorrowPublisher() != null && borrow1.getBorrowPublisher().equals("中商储")) {
				modelAndView = new ModelAndView(InvestProjectDefine.PROJECT_RTB_CONTRACT_ZSC_PTAH);
			} else {
				modelAndView = new ModelAndView(InvestProjectDefine.PROJECT_RTB_CONTRACT_PTAH);
			}

			UsersInfo userinfo = investProjectService.getUsersInfoByUserId(userId);

			List<WebUserInvestListCustomize> invest = investProjectService.selectUserInvestList(borrowNid, userId,
					orderId, 0, 10);
			if (invest != null && invest.size() > 0) {
				modelAndView.addObject("investDeatil", invest.get(0));
			}
			if (borrow1 != null) {
				borrow1.setBorrowPeriod(borrow1.getBorrowPeriod().substring(0, borrow1.getBorrowPeriod().length() - 1));
			}
			modelAndView.addObject("projectDeatil", borrow1);
			modelAndView.addObject("truename", userinfo.getTruename());
			modelAndView.addObject("idcard", userinfo.getIdcard());
			modelAndView.addObject("borrowNid", form.getBorrowNid());// 标的号
			modelAndView.addObject("assetNumber", borrow1.getBorrowAssetNumber());// 资产编号
			modelAndView.addObject("projectType", form.getProjectType());// 项目类型
		} else {
			modelAndView = new ModelAndView(InvestProjectDefine.PROJECT_HZT_CONTRACT_PTAH);
		}
		modelAndView.addObject("borrow", borrow);
		String repayType = borrow.getRepayType();
		if (CustomConstants.BORROW_STYLE_MONTH.equals(repayType)
				|| CustomConstants.BORROW_STYLE_PRINCIPAL.equals(repayType)
				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(repayType)) {
			List<AppProjectContractRecoverPlanCustomize> repayPlans = this.investProjectService
					.selectProjectContractRecoverPlan(params);
			modelAndView.addObject("repayPlans", repayPlans);
		}
		return modelAndView;

	}

	// add liuyang 获取债转的还款计划 start
	/**
	 * 
	 * 用户承接债转的还款计划
	 * 
	 * @author liuyang
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public JSONObject searchCreditRepayPlanList(@ModelAttribute RepayPlanListBean form, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject ret = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		// 债转编号
		String creditNid = request.getParameter("borrowNid");
		// 债转投标单号
		String assignNid = request.getParameter("orderId");

		String sign = request.getParameter("sign");
		// 用户已登录
		Integer userId = SecretUtil.getUserId(sign);

		form.setUserId(String.valueOf(userId));
		params.put("userId", userId);
		if (StringUtils.isEmpty(assignNid) || StringUtils.isEmpty(creditNid)) {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, null);
			ret.put(CustomConstants.MSG, "无法获取债转编号与承接编号");
			LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AppTenderCreditDefine.CREDIT_REPAY_PLAN_LIST);
			return ret;
		}
		params.put("creditNid", creditNid);
		params.put("assignNid", assignNid);
		// 根据债转债转编号获取原标NID
		BorrowCredit borrowCredit = this.appTenderCreditService.selectCreditTenderByCreditNid(creditNid);
		if (borrowCredit == null) {
			// 债转获取失败
			ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
			ret.put(CustomConstants.APP_STATUS_DESC, "未查询到相应的项目的信息");
			ret.put(CustomConstants.APP_REQUEST, AppTenderCreditDefine.REQUEST_HOME
					+ AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_REPAY_PLAN_LIST);
			return ret;
		} else {
			// 获取原标标号
			String bidNid = borrowCredit.getBidNid();
			ret.put("borrowNid", bidNid);
			Borrow borrow = this.appTenderCreditService.selectBorrowByBorrowNid(bidNid);
			if (borrow == null) {
				// 债转获取失败
				ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
				ret.put(CustomConstants.APP_STATUS_DESC, "获取原标的信息失败");
				ret.put(CustomConstants.APP_REQUEST, AppTenderCreditDefine.REQUEST_HOME
						+ AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_REPAY_PLAN_LIST);
				return ret;
			}
			// 借款方式
			String borrowStyleStr = borrow.getBorrowStyle();
			BorrowStyle borrowStyle = this.appTenderCreditService.selectBorrowStyleByStyle(borrowStyleStr);
			if (borrowStyle == null) {
				// 债转获取失败
				ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
				ret.put(CustomConstants.APP_STATUS_DESC, "获取原标的借款方式失败");
				ret.put(CustomConstants.APP_REQUEST, AppTenderCreditDefine.REQUEST_HOME
						+ AppTenderCreditDefine.REQUEST_MAPPING + AppTenderCreditDefine.CREDIT_REPAY_PLAN_LIST);
				return ret;
			}
			// 还款方式
			ret.put("repayStyle", borrowStyle.getName());
			if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyleStr)
					|| CustomConstants.BORROW_STYLE_END.equals(borrowStyleStr)) {
				// 按月计息到期还本还息,按天计息，到期还本还息 不分期标获取还款计划
				this.createCreditRepayRecoverListPage(ret, form);
			} else {
				// 分期标,获取还款计划
				this.createCreditRepayRecoverPlanListPage(ret, form, borrowStyleStr);
			}

			ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
			ret.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
			ret.put(CustomConstants.APP_REQUEST, InvestProjectDefine.REQUEST_HOME + InvestProjectDefine.REQUEST_MAPPING
					+ InvestProjectDefine.REPAY_PLAN_LIST_ACTION);
		}
		return ret;
	}

	/**
	 * 
	 * 不分期债转对应的 创建相应的还款信息分页
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createCreditRepayRecoverListPage(JSONObject info, RepayPlanListBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", form.getUserId());
		params.put("creditNid", form.getBorrowNid());
		params.put("assignNid", form.getOrderId());
		int recordTotal = this.appTenderCreditService.countRepayRecoverListRecordTotal(params);
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
			// 查询不分期债转的还款计划
			List<AppTenderCreditRepayPlanListCustomize> recordList = this.appTenderCreditService
					.selectRepayRecoverList(params);
			info.put("repayList", recordList);
			info.put("repayTotal", String.valueOf(recordTotal));
		} else {
			info.put("repayList", new ArrayList<AppRepayPlanListCustomize>());
			info.put("repayTotal", "0");
		}
	}

	/**
	 * 
	 * 分期债转对应的 创建相应的还款信息分页
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createCreditRepayRecoverPlanListPage(JSONObject info, RepayPlanListBean form, String borrowStyleStr) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", form.getUserId());
		params.put("creditNid", form.getBorrowNid());
		params.put("assignNid", form.getOrderId());
		params.put("borrowStyle", borrowStyleStr);
		int recordTotal = this.appTenderCreditService.countRepayPlanListRecordTotal(params);
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
			// 获取分期债转的还款计划
			List<AppTenderCreditRepayPlanListCustomize> recordList = this.appTenderCreditService
					.selectRepayRecoverPlanList(params);
			info.put("repayList", recordList);
			info.put("repayTotal", String.valueOf(recordTotal));
		} else {
			info.put("repayList", new ArrayList<AppRepayPlanListCustomize>());
			info.put("repayTotal", "0");
		}
	}

	// add liuyang 获取债转的还款计划 end

	/**
	 * 查询回款中的项目详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = InvestProjectDefine.REPAY_PROJECT_DETAIL_ACTION)
	public ModelAndView searchRepayProjectDetail(InvestProjectBean form, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(InvestProjectDefine.REPAY_PROJECT_DETAIL_PATH);
		Map<String, Object> params = new HashMap<String, Object>();
		String sign = request.getParameter("sign");
		// 用户已登录
		Integer userId = SecretUtil.getUserId(sign);
		modelAndView.addObject("investType", form.getInvestType());
		if (StringUtils.isEmpty(form.getCouponCode())) {
			if (StringUtils.isNotEmpty(form.getBorrowNid()) && StringUtils.isNotEmpty(form.getTenderNid())) {
				params.put("borrowNid", form.getBorrowNid());
				params.put("tenderNid", form.getTenderNid());
				params.put("investType", form.getInvestType());
				AppRepayDetailCustomize appRepayDetailCustomize = this.investProjectService.selectRepayDetail(params);
				if (appRepayDetailCustomize != null) {
					modelAndView.addObject("repayDetail", appRepayDetailCustomize);
				} else {
					modelAndView = new ModelAndView(InvestProjectDefine.ERROR_PTAH);
					return modelAndView;
				}
			} else {
				modelAndView = new ModelAndView(InvestProjectDefine.ERROR_PTAH);
				return modelAndView;
			}
		} else {
			// 优惠券编号
			params.put("couponCode", form.getCouponCode());
			params.put("userId", userId);
			// 优惠券出借
			AppRepayDetailCustomize appCouponRepayDetailCustomize = this.investProjectService
					.selectCouponRepayDetail(params);
			if (appCouponRepayDetailCustomize != null) {
				modelAndView.addObject("repayDetail", appCouponRepayDetailCustomize);
			} else {
				modelAndView = new ModelAndView(InvestProjectDefine.ERROR_PTAH);
				return modelAndView;
			}
		}
		return modelAndView;
	}

	/**
	 * 查询出借中项目详情
	 * 
	 * @author liuyang
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = InvestProjectDefine.INVEST_PROJECT_DETAIL_ACTION)
	public ModelAndView searchInvestProjectDetail(InvestProjectBean form, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(InvestProjectDefine.INVEST_PROJECT_DETAIL_PATH);
		Map<String, Object> params = new HashMap<String, Object>();
		String version = request.getParameter("version");
		String sign = request.getParameter("sign");
		// 用户已登录
		Integer userId = SecretUtil.getUserId(sign);
		modelAndView.addObject("investType", form.getInvestType());
		params.put("userId", userId);
		if (StringUtils.isEmpty(form.getCouponCode())) {
			// 未使用优惠券出借
			if (StringUtils.isNotEmpty(form.getBorrowNid()) && StringUtils.isNotEmpty(form.getTenderNid())) {
				params.put("borrowNid", form.getBorrowNid());
				params.put("tenderNid", form.getTenderNid());
				params.put("investType", form.getInvestType());
				AppRepayDetailCustomize appInvestDetailCustomize = this.investProjectService
						.selectInvestProjectDetail(params);
				if (appInvestDetailCustomize != null) {
					DecimalFormat df = null;
					if (StringUtils.contains(version, CustomConstants.APP_VERSION_NUM)) {
						df = CustomConstants.DF_FOR_VIEW_V1;
					} else {
						df = CustomConstants.DF_FOR_VIEW;
					}
					String borrowStyle = appInvestDetailCustomize.getBorrowStyle();
					// 收益率
					BigDecimal borrowApr = BigDecimal.ZERO;
					if (form.getInvestType() != null && form.getInvestType().equals("3")) {
						borrowApr = appInvestDetailCustomize.getBorrowExtraYield();
					} else {
						// 收益率
						borrowApr = new BigDecimal(appInvestDetailCustomize.getBorrowApr());

					}
					/*
					 * // 融通宝收益叠加 if (appInvestDetailCustomize.getProjectType()
					 * == 13 && appInvestDetailCustomize.getBorrowExtraYield()
					 * != null &&
					 * appInvestDetailCustomize.getBorrowExtraYield().
					 * compareTo(BigDecimal.ZERO) > 0) { borrowApr =
					 * borrowApr.add
					 * (appInvestDetailCustomize.getBorrowExtraYield()); }
					 */
					// 周期
					Integer borrowPeriod = Integer.parseInt(appInvestDetailCustomize.getBorrowPeriod());
					BigDecimal earnings = new BigDecimal("0");
					df.setRoundingMode(RoundingMode.FLOOR);
					switch (borrowStyle) {
					case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
						// 计算历史回报
						earnings = DuePrincipalAndInterestUtils.getMonthInterest(
								appInvestDetailCustomize.getAccountNum(), borrowApr.divide(new BigDecimal("100")),
								borrowPeriod);
						appInvestDetailCustomize.setAccountAll(df.format(appInvestDetailCustomize.getAccountNum().add(
								earnings)));
						appInvestDetailCustomize.setInterest(df.format(earnings));
						break;
					case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
						earnings = DuePrincipalAndInterestUtils.getDayInterest(
								appInvestDetailCustomize.getAccountNum(), borrowApr.divide(new BigDecimal("100")),
								borrowPeriod);
						appInvestDetailCustomize.setAccountAll(df.format(appInvestDetailCustomize.getAccountNum().add(
								earnings)));
						appInvestDetailCustomize.setInterest(df.format(earnings));
						break;
					case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
						earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(
								appInvestDetailCustomize.getAccountNum(), borrowApr.divide(new BigDecimal("100")),
								borrowPeriod, borrowPeriod);
						appInvestDetailCustomize.setAccountAll(df.format(appInvestDetailCustomize.getAccountNum().add(
								earnings)));
						appInvestDetailCustomize.setInterest(df.format(earnings));
						break;
					case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
						earnings = AverageCapitalPlusInterestUtils.getInterestCount(
								appInvestDetailCustomize.getAccountNum(), borrowApr.divide(new BigDecimal("100")),
								borrowPeriod);
						appInvestDetailCustomize.setAccountAll(df.format(appInvestDetailCustomize.getAccountNum().add(
								earnings)));
						appInvestDetailCustomize.setInterest(df.format(earnings));
						break;
					default:
						appInvestDetailCustomize.setAccountAll("");
						appInvestDetailCustomize.setInterest("");
						break;
					}
					modelAndView.addObject("repayDetail", appInvestDetailCustomize);
				} else {
					logger.info("----borrowNid----" + form.getBorrowNid());
					logger.info("----tenderNid----" + form.getTenderNid());
					modelAndView = new ModelAndView(InvestProjectDefine.ERROR_PTAH);
					return modelAndView;
				}
			} else {
				logger.info("borrowNid或tenderNid为空");
				modelAndView = new ModelAndView(InvestProjectDefine.ERROR_PTAH);
				return modelAndView;
			}
		} else {
			// 优惠券出借项目详情
			// 优惠券编号
			params.put("couponCode", form.getCouponCode());
			// 优惠券出借
			AppRepayDetailCustomize appCouponRepayDetailCustomize = this.investProjectService
					.selectCouponInvestProjectDetail(params);
			if (appCouponRepayDetailCustomize != null) {
				DecimalFormat df = null;
				if (StringUtils.contains(version, CustomConstants.APP_VERSION_NUM)) {
					df = CustomConstants.DF_FOR_VIEW_V1;
				} else {
					df = CustomConstants.DF_FOR_VIEW;
				}
				String borrowStyle = appCouponRepayDetailCustomize.getBorrowStyle();
				// 收益率
				BigDecimal borrowApr = null;
				if (!appCouponRepayDetailCustomize.getCouponType().equals("2")) {
					borrowApr = new BigDecimal(appCouponRepayDetailCustomize.getBorrowApr());
				} else {
					borrowApr = new BigDecimal(appCouponRepayDetailCustomize.getCouponQuota());
				}
				// 周期
				Integer borrowPeriod = Integer.parseInt(appCouponRepayDetailCustomize.getBorrowPeriod());
				BigDecimal earnings = new BigDecimal("0");
				df.setRoundingMode(RoundingMode.FLOOR);
				switch (borrowStyle) {
				case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
					// 计算历史回报
					earnings = DuePrincipalAndInterestUtils.getMonthInterest(
							appCouponRepayDetailCustomize.getAccountNum(), borrowApr.divide(new BigDecimal("100")),
							borrowPeriod);
					appCouponRepayDetailCustomize.setAccountAll(df.format(appCouponRepayDetailCustomize.getAccountNum()
							.add(earnings)));
					appCouponRepayDetailCustomize.setInterest(df.format(earnings));
					break;
				case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷360*天数；
					earnings = DuePrincipalAndInterestUtils.getDayInterest(
							appCouponRepayDetailCustomize.getAccountNum(), borrowApr.divide(new BigDecimal("100")),
							borrowPeriod);
					appCouponRepayDetailCustomize.setAccountAll(df.format(appCouponRepayDetailCustomize.getAccountNum()
							.add(earnings)));
					appCouponRepayDetailCustomize.setInterest(df.format(earnings));
					break;
				case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：历史回报=出借金额*年化收益÷12*月数；
					earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(
							appCouponRepayDetailCustomize.getAccountNum(), borrowApr.divide(new BigDecimal("100")),
							borrowPeriod, borrowPeriod);
					appCouponRepayDetailCustomize.setAccountAll(df.format(appCouponRepayDetailCustomize.getAccountNum()
							.add(earnings)));
					appCouponRepayDetailCustomize.setInterest(df.format(earnings));
					break;
				case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：历史回报=出借金额*年化收益÷12*月数；
					earnings = AverageCapitalPlusInterestUtils.getInterestCount(
							appCouponRepayDetailCustomize.getAccountNum(), borrowApr.divide(new BigDecimal("100")),
							borrowPeriod);
					appCouponRepayDetailCustomize.setAccountAll(df.format(appCouponRepayDetailCustomize.getAccountNum()
							.add(earnings)));
					appCouponRepayDetailCustomize.setInterest(df.format(earnings));
					break;
				default:
					appCouponRepayDetailCustomize.setAccountAll("");
					appCouponRepayDetailCustomize.setInterest("");
					break;
				}

				if (appCouponRepayDetailCustomize.getCouponType().equals("1")) {
					earnings = DuePrincipalAndInterestUtils.getDayInterestExperience(
							appCouponRepayDetailCustomize.getAccountNum(), borrowApr.divide(new BigDecimal("100")),
							Integer.parseInt(appCouponRepayDetailCustomize.getCouponProfitTime()));
					appCouponRepayDetailCustomize.setAccountAll(df.format(earnings));
					appCouponRepayDetailCustomize.setInterest(df.format(earnings));
				}

			}
			if (appCouponRepayDetailCustomize != null) {
				modelAndView.addObject("repayDetail", appCouponRepayDetailCustomize);
			} else {
				logger.info("----borrowNid----" + form.getCouponCode());
				modelAndView = new ModelAndView(InvestProjectDefine.ERROR_PTAH);
				return modelAndView;
			}
		}
		return modelAndView;
	}

	/**
	 * 
	 * 获取已回款项目详情
	 * 
	 * @author liuyang
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = InvestProjectDefine.REPAYED_PROJECT_DETAIL_ACTION)
	public ModelAndView searchRepayedProjectDetail(InvestProjectBean form, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(InvestProjectDefine.REPAYED_PROJECT_DETAIL_PATH);
		Map<String, Object> params = new HashMap<String, Object>();
		String sign = request.getParameter("sign");
		// 用户已登录
		Integer userId = SecretUtil.getUserId(sign);
		modelAndView.addObject("investType", form.getInvestType());
		// 用户Id
		params.put("userId", userId);
		if (StringUtils.isEmpty(form.getCouponCode())) {
			// 未使用优惠券的出借
			if (StringUtils.isNotEmpty(form.getBorrowNid()) && StringUtils.isNotEmpty(form.getTenderNid())) {
				// 借款编号
				params.put("borrowNid", form.getBorrowNid());
				// 出借单号
				params.put("tenderNid", form.getTenderNid());
				params.put("investType", form.getInvestType());
				AppRepayDetailCustomize appRepayedDetailCustomize = this.investProjectService
						.selectRepayedProjectDetail(params);
				if (appRepayedDetailCustomize != null) {
					modelAndView.addObject("repayDetail", appRepayedDetailCustomize);
				} else {
					logger.info("----borrowNid----" + form.getBorrowNid());
					logger.info("----tenderNid----" + form.getTenderNid());
					modelAndView = new ModelAndView(InvestProjectDefine.ERROR_PTAH);
					return modelAndView;
				}
			} else {
				logger.info("borrowNid或tenderNid为空");
				modelAndView = new ModelAndView(InvestProjectDefine.ERROR_PTAH);
				return modelAndView;
			}
		} else {
			// 优惠券出借
			params.put("couponCode", form.getCouponCode());
			AppRepayDetailCustomize appRepayedDetailCustomize = this.investProjectService
					.selectCouponRepayedProjectDetail(params);
			if (appRepayedDetailCustomize != null) {
				modelAndView.addObject("repayDetail", appRepayedDetailCustomize);
			} else {
				logger.info("----------获取详情失败:couponCode----------" + form.getCouponCode());
				modelAndView = new ModelAndView(InvestProjectDefine.ERROR_PTAH);
				return modelAndView;
			}
		}
		return modelAndView;
	}


	/**
	 * 适应客户端数据返回 - 已回款
	 * 
	 * @param appAlreadyRepayListCustomizes
	 * @return
	 */
	private List<MyProjectVo> convertappAlreadyRepayListToMyProjectVo(
			List<AppAlreadyRepayListCustomize> appAlreadyRepayListCustomizes, HttpServletRequest request) {
		List<MyProjectVo> vos = new ArrayList<>();
		MyProjectVo vo = null;
		if (CollectionUtils.isEmpty(appAlreadyRepayListCustomizes))
			return vos;
		for (AppAlreadyRepayListCustomize entity : appAlreadyRepayListCustomizes) {
			vo = new MyProjectVo();
			BeanUtils.copyProperties(entity, vo);
			vo.setBorrowTheFirst(entity.getAccount() + "元");
			vo.setBorrowTheFirstDesc("出借金额");
			vo.setBorrowTheSecond(entity.getPeriod());
			vo.setBorrowTheSecondDesc("项目期限");
			vo.setBorrowTheThird(GetDate.times10toStrYYYYMMDD(Integer.valueOf(entity.getRecoverTime())));
			vo.setBorrowTheThirdDesc("回款时间");
			vo.setType("2");
			vo.setIsDisplay("0");

			String assignNid = "";
			if ("HZR".equals(entity.getProjectType())) {
				assignNid = entity.getOrderId();
				logger.info("已回款状态，债转编号:{}", assignNid);
			}

			String borrowUrl = this.concatInvestDetailUrl(entity.getBorrowNid(), entity.getOrderId(),
					request.getParameter("type"), entity.getCouponType(), assignNid, "已还款");
			//vo.setBorrowUrl(CommonUtils.concatReturnUrl(request, borrowUrl));
			// 产品加息
            if ("3".equals(entity.getInvestType())) {
                borrowUrl += "&isIncrease=1&isCalendar=1";
                vo.setCouponType("4");
            }
			vo.setBorrowUrl(borrowUrl);
			CommonUtils.convertNullToEmptyString(vo);
			vos.add(vo);
		}
		return vos;
	}

	/**
	 * 适应客户端数据返回 - 出借中
	 * 
	 * @param appInvestListCustomizes
	 * @return
	 */
	private List<MyProjectVo> convertAppInvestListCustomizeToMyProjectVo(
			List<CurrentHoldObligatoryRightListCustomize> customizes, HttpServletRequest request, Integer userId) {
		List<MyProjectVo> vos = new ArrayList<>();
		if (CollectionUtils.isEmpty(customizes)){
			return vos;
		}
		String investStatusDesc = "";
		for (CurrentHoldObligatoryRightListCustomize entity : customizes) {
		    MyProjectVo vo = new MyProjectVo();
			BeanUtils.copyProperties(entity, vo);
			vo.setBorrowNid(entity.getBorrowNid());
            
			String couponType = entity.getCouponType();
			switch (couponType) {
			case "1":
				vo.setLabel("体验金");
				investStatusDesc = "未回款";
				break;
			case "2":
				vo.setLabel("加息券");
				investStatusDesc = "未回款";
				break;
			case "3":
				vo.setLabel("代金券");
				investStatusDesc = "未回款";
				break;
			default:
				if("2".equals(vo.getType())){
					vo.setLabel("承接债转");
				} else {
					vo.setLabel("");
				}
				investStatusDesc = "现金出借".equals(entity.getData()) ? "还款中" : entity.getData();
			}
			
			// 加息收益
            if("4".equals(entity.getType())){
                vo.setLabel(entity.getData());
                investStatusDesc = "未回款";
                vo.setCouponType("4");
            }
            
			vo.setBorrowName(entity.getBorrowNid());
			vo.setBorrowTheFirst(CommonUtils.formatAmount(entity.getCapital()) + "元");
			vo.setBorrowTheFirstDesc("出借金额");
			vo.setBorrowTheSecond(entity.getBorrowPeriod());
			vo.setBorrowTheSecondDesc("项目期限");
			vo.setBorrowTheThird(entity.getAddtime());
			vo.setBorrowTheThirdDesc("出借时间");
			vo.setType("1");
			// 出借订单号
			String assignNid = "";
			String nid = entity.getNid();
			if (!StringUtils.isBlank(entity.getCreditTenderNid())) {
				assignNid = entity.getNid();
				logger.info("债转编号: {}", assignNid);
			}

			String borrowUrl = this.concatInvestDetailUrl(entity.getBorrowNid(),  nid,
					request.getParameter("type"), entity.getCouponType(), assignNid, investStatusDesc);
			//vo.setBorrowUrl(CommonUtils.concatReturnUrl(request, borrowUrl));
			// 如果是产品加息
            if ("4".equals(entity.getType())) {
                borrowUrl += "&isIncrease=1";
            }
			vo.setBorrowUrl(borrowUrl);

			// 判断债权能否债转
			if (canDoTransfer(entity.getBorrowNid(), nid, userId)) {
				vo.setIsDisplay("1");
				String url = InvestProjectDefine.TO_CREDIT_PAGE_URL + "?borrowId=" + entity.getBorrowNid()
						+ "&tenderId=" + nid;
				//vo.setUrl(CommonUtils.concatReturnUrl(request, url));
				vo.setUrl(url);
			} else {
				vo.setIsDisplay("0");
			}
			CommonUtils.convertNullToEmptyString(vo);
			vos.add(vo);
		}
		return vos;
	}


	/**
	 * 判断用户某一出借是否满足债转条件
	 * 
	 * @param borrowNid
	 * @param tenderNid
	 * @param userId
	 * @return
	 */
	private boolean canDoTransfer(String borrowNid, String tenderNid, Integer userId) {

		if(!investProjectService.isAllowChannelAttorn(userId)){
			// 判断用户所处渠道不允许债转
			return false;
		}

		Map<String, Object> params = new HashMap<>();
		params.put("borrowNid", borrowNid);
		params.put("tenderNid", tenderNid);
		params.put("userId", userId);
		params.put("nowTime", (int) (System.currentTimeMillis() / 1000));
		params.put("limitStart", 0);
		params.put("limitEnd", 1);
		List<AppTenderToCreditListCustomize> list = appTenderCreditService.selectTenderToCreditList(params);
		if (!CollectionUtils.isEmpty(list)) {
			return true;
		}
		return false;
	}

	/**
	 * 拼接出借详情url
	 * @param borrowNid
	 * @param orderId
	 * @param type
	 * @param couponType
	 * @param assignNid
	 * @param investStatusDesc  出借状态
	 * @return
	 */
	private String concatInvestDetailUrl(String borrowNid, String orderId, String type, String couponType, String assignNid, String investStatusDesc) {
		if (StringUtils.isEmpty(couponType)){
			couponType = "0";
		}
		String url = InvestProjectDefine.MY_BORROW_PAGE_URL + "/" + borrowNid + "?orderId=" + orderId + "&type=" + type
				+ "&couponType=" + couponType + "&assignNid=" + assignNid +"&investStatusDesc=" + investStatusDesc;
		return url;
	}

	/**
	 * 适应客户端数据返回 - 债转
	 * 
	 * @param projectList
	 * @return
	 */
	private List<MyProjectVo> converCreditToMyProject(List<AppTenderCreditRecordListCustomize> projectList,
			HttpServletRequest request) {
		List<MyProjectVo> vos = new ArrayList<>();
		MyProjectVo vo = null;
		if (CollectionUtils.isEmpty(projectList))
			return vos;
		for (AppTenderCreditRecordListCustomize customize : projectList) {
			vo = new MyProjectVo();
			vo.setBorrowTheFirst(customize.getCreditCapital() + "元");
			vo.setBorrowTheFirstDesc("原始本金");
			vo.setBorrowTheSecond(customize.getCreditCapitalAssigned() + "元");
			vo.setBorrowTheSecondDesc("已转让金额");
			vo.setBorrowTheThird(customize.getCreditStatusDesc());
			vo.setStatusName(customize.getCreditStatusDesc());
			vo.setType("3");
			vo.setIsDisplay("0");
			vo.setBorrowName(customize.getCreditNid());

			String borrowUrl = InvestProjectDefine.MY_CREDIT_PAGE_URL + "/" + customize.getRealCreditNid();
			//vo.setBorrowUrl(CommonUtils.concatReturnUrl(request, borrowUrl));
			vo.setBorrowUrl(borrowUrl);
			CommonUtils.convertNullToEmptyString(vo);
			vos.add(vo);
		}
		return vos;
	}


	/**
	 * 构造查询参数map
	 * @param request
	 * @return
	 */
	private Map<String,Object> buildQueryParameter(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
		params.put("limitStart", (page - 1) * pageSize);
		params.put("limitEnd", pageSize);

		return params;
	}

}
