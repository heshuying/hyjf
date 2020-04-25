/**
 * Description:获取指定的项目类型的项目列表
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.bank.wechat.borrow;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.BorrowFileCustomBean;
import com.hyjf.bank.service.borrow.BorrowRepayPlanCustomBean;
import com.hyjf.bank.service.borrow.BorrowService;
import com.hyjf.common.calculate.*;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.AsteriskProcessUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.customize.web.*;
import com.hyjf.web.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Controller(BorrowDefine.CONTROLLER_NAME)
@RequestMapping(value = BorrowDefine.REQUEST_MAPPING)
public class BorrowController extends BaseController {

	@Autowired
	private BorrowService borrowService;

	/** 发布地址 */
	private static String HOST_URL = PropUtils.getSystem("hyjf.web.host");

	/**
	 * 获取汇直投列表
	 * 
	 * @param hzt
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BorrowDefine.BORROW_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchBorrowList(@ModelAttribute BorrowBean hzt, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_LIST_ACTION);
		BorrowListAJaxBean result = new BorrowListAJaxBean();
		this.createProjectListPage(result, hzt);
		LogUtil.endLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_LIST_ACTION);
		return JSONObject.toJSONString(result, true);
	}

	/**
	 * 创建相应的汇直投相应的列表分页
	 * @param result
	 * @param form
	 */
	private void createProjectListPage(BorrowListAJaxBean result, BorrowBean form) {

		String status = form.getStatus();
		List<BorrowProjectType> borrowTypes = this.borrowService.getProjectTypeList();
		String projectType = form.getProjectType();// 项目类型
		String borrowClass = form.getBorrowClass();// 项目子类型
		if (StringUtils.isBlank(projectType)) {
			result.setError(1);
			result.setErrorDesc("请求参数错误");
			return;
		}
		if(borrowTypes == null || borrowTypes.size() == 0){
			result.setError(1);
			result.setErrorDesc("系统获取项目类型失败");
			return;
		}
		// 校验相应的项目类型
		boolean typeFlag = false;
		boolean classFlag = false;
		for (BorrowProjectType borrowType : borrowTypes) {
			String type = borrowType.getBorrowProjectType();
			if (type.equals(projectType)) {
				typeFlag = true;
			}
			if (StringUtils.isNotBlank(borrowClass)) {
				String classType = borrowType.getBorrowClass();
				if (classType.equals(borrowClass)) {
					classFlag = true;
				}
			} else {
				classFlag = true;
			}
		}
		if(!typeFlag || !classFlag){
			result.setError(1);
			result.setErrorDesc("请求参数非法，参数不存在");
			return;
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("projectType", projectType);
		params.put("borrowClass", borrowClass);
		params.put("status", status);

		// 汇盈金服微信列表定向标过滤
		params.put("publishInstCode", CustomConstants.HYJF_INST_CODE);

		// 统计相应的汇直投的数据记录数
		int projecTotal = this.borrowService.countProjectListRecordTotalNew(params);

		if (projecTotal > 0) {
			//add by cwyang 项目列表显示2页
			int pageNum = 2;
			if(projecTotal > form.getPageSize() * pageNum){
				projecTotal = form.getPageSize() * pageNum;
			}
			Paginator paginator = new Paginator(form.getPaginatorPage(), projecTotal, form.getPageSize());
			// 查询相应的汇直投列表数据
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<WebProjectListCustomize> projectList = borrowService.searchProjectListNew(params);
			result.setProjectList(projectList);
			result.setPaginator(paginator);
			result.setError(0);
			result.setErrorDesc("成功");
		} else {
			result.setProjectList(new ArrayList<WebProjectListCustomize>());
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
		}
	}

	/**
	 * 微信查询相应的项目详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BorrowDefine.BORROW_DETAIL_ACTION,method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String searchProjectDetail(HttpServletRequest request, HttpServletResponse response) {
		logger.info("请求微信端查询项目详情IP为："+GetCilentIP.getIpAddr(request));
		String borrowNid = request.getParameter("borrowNid");
		BorrowDetailAJaxBean result = new BorrowDetailAJaxBean();
		LogUtil.startLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_DETAIL_ACTION);
		// 2.根据项目标号获取相应的项目信息
		WebProjectDetailCustomize borrow = this.borrowService.selectProjectDetail(borrowNid);
		BorrowRepay borrowRepay = borrowService.getBorrowRepay(borrowNid);
		
		if (borrow == null) {
			result.setError(1);
			result.setErrorDesc("根据id查询借款失败");
			return JSONObject.toJSONString(result, true);
		} else {
			String nowTime = String.valueOf(GetDate.getNowTime10());
			result.setNowTime(nowTime);
			String borrowStyle = borrow.getBorrowStyle();
			BigDecimal borrowInterest = new BigDecimal(0);
			switch (borrowStyle) {
			case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
				// 计算历史回报
				borrowInterest = DuePrincipalAndInterestUtils
						.getMonthInterest(new BigDecimal(borrow.getBorrowAccount()), new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
						.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“
				borrowInterest = DuePrincipalAndInterestUtils
						.getDayInterest(new BigDecimal(borrow.getBorrowAccount()), new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
						.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：；
				borrowInterest = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")),
						Integer.parseInt(borrow.getBorrowPeriod()), Integer.parseInt(borrow.getBorrowPeriod())).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：；
				borrowInterest = AverageCapitalPlusInterestUtils
						.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
						.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
				borrowInterest = AverageCapitalUtils
						.getInterestCount(new BigDecimal(borrow.getBorrowAccount()), new BigDecimal(borrow.getBorrowApr()).divide(new BigDecimal("100")), Integer.parseInt(borrow.getBorrowPeriod()))
						.divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
				break;
			default:
				break;
			}
			borrow.setBorrowInterest(borrowInterest.toString());
			if (borrow.getType().equals("9")) {// 如果项目为汇资产项目
				// 添加相应的项目详情信息
				result.setProjectDeatil(borrow);
				// 4查询相应的汇资产的首页信息
				WebHzcProjectDetailCustomize borrowInfo = this.borrowService.searchHzcProjectDetail(borrowNid);
				result.setBorrowInfo(borrowInfo);
				// 处置预案
				WebHzcDisposalPlanCustomize disposalPlan = this.borrowService.searchDisposalPlan(borrowNid);
				result.setDisposalPlan(disposalPlan);
				// 5查询相应的还款计划
				List<BorrowRepayPlanCustomBean> repayPlanList = this.borrowService.getRepayPlan(borrowNid);
				result.setRepayPlanList(repayPlanList);
				// 相关文件
				List<BorrowFileCustomBean> files = this.borrowService.searchProjectFiles(borrowNid, HOST_URL);
				result.setFiles(files);
			} else {// 项目为非汇资产项目
				// 添加相应的项目详情信息
				result.setProjectDeatil(borrow);
				// 4查询非汇资产项目的项目信息
				if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("1")) {
					// 查询相应的企业项目详情
					WebProjectCompanyDetailCustomize companyDetail = borrowService.searchProjectCompanyDetail(borrowNid);
					//数据脱敏
					companyDetail.setSocialCreditCode(AsteriskProcessUtil.getAsteriskedValue(companyDetail.getSocialCreditCode(), 4, 10));
					companyDetail.setRegistCode(AsteriskProcessUtil.getAsteriskedValue(companyDetail.getRegistCode(), 4, 10));
					companyDetail.setLegalPerson(AsteriskProcessUtil.getAsteriskedValue(companyDetail.getLegalPerson(), 1, 2));
					companyDetail.setCompanyName(AsteriskProcessUtil.getAsteriskedValue(companyDetail.getCompanyName(),companyDetail.getCompanyName().length()-2));					result.setCompanyDetail(companyDetail);
					
					// 信批需求新增(放款后才显示)
					if(borrow.getStatusOrginal()>=4 && borrowRepay != null){
						Map<String,Object> otherInfo = new HashMap<String,Object>();
						otherInfo.put("isFunds", companyDetail.getIsFunds());
						otherInfo.put("isManaged", companyDetail.getIsManaged());
						otherInfo.put("isAbility", companyDetail.getIsAbility());
						otherInfo.put("isOverdue", companyDetail.getIsOverdue());
						otherInfo.put("isComplaint", companyDetail.getIsComplaint());
						otherInfo.put("isPunished", companyDetail.getIsPunished());
						otherInfo.put("updateTime", getUpdateTime(Integer.parseInt(borrowRepay.getAddtime()),  StringUtils.isBlank(borrowRepay.getRepayYestime())?null:Integer.parseInt(borrowRepay.getRepayYestime())));
						result.setOtherInfo(otherInfo);
					}
				} else if (StringUtils.isNotEmpty(borrow.getComOrPer()) && borrow.getComOrPer().equals("2")) {
					// 查询相应的汇直投个人项目详情
					WebProjectPersonDetailCustomize personDetail = borrowService.searchProjectPersonDetail(borrowNid);
					//数据脱敏
					personDetail.setTrueName(AsteriskProcessUtil.getAsteriskedValue(personDetail.getTrueName(), 1, 2));
					personDetail.setCardNo(AsteriskProcessUtil.getAsteriskedValue(personDetail.getCardNo(), 4, 10));
					result.setPersonDetail(personDetail);
					
					// 信批需求新增(放款后才显示)
					if(borrow.getStatusOrginal()>=4){
						Map<String,Object> otherInfo = new HashMap<String,Object>();
						otherInfo.put("isFunds", personDetail.getIsFunds());
						otherInfo.put("isManaged", personDetail.getIsManaged());
						otherInfo.put("isAbility", personDetail.getIsAbility());
						otherInfo.put("isOverdue", personDetail.getIsOverdue());
						otherInfo.put("isComplaint", personDetail.getIsComplaint());
						otherInfo.put("isPunished", personDetail.getIsPunished());
						otherInfo.put("updateTime", getUpdateTime(Integer.parseInt(borrowRepay.getAddtime()),  StringUtils.isBlank(borrowRepay.getRepayYestime())?null:Integer.parseInt(borrowRepay.getRepayYestime())));
						result.setOtherInfo(otherInfo);
					}
				}

				// 风控信息
				WebRiskControlCustomize riskControl = this.borrowService.selectRiskControl(borrowNid);
				riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
				riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
				// 添加风控信息
				result.setRiskControl(riskControl);
				List<WebMortgageCustomize> mortgageList = this.borrowService.selectMortgageList(borrowNid);
				// 添加相应的房产信息
				result.setMortgageList(mortgageList);
				List<WebVehiclePledgeCustomize> vehiclePledgeList = this.borrowService.selectVehiclePledgeList(borrowNid);
				// 添加相应的汽车抵押信息
				result.setVehiclePledgeList(vehiclePledgeList);
				// 5查询相应的认证信息
				List<WebProjectAuthenInfoCustomize> authenList = borrowService.searchProjectAuthenInfos(borrowNid);
				result.setAuthenList(authenList);
				// 6查询相应的还款计划
				List<BorrowRepayPlanCustomBean> repayPlanList = this.borrowService.getRepayPlan(borrowNid);
				result.setRepayPlanList(repayPlanList);
				// 7 相关文件
				List<BorrowFileCustomBean> files = this.borrowService.searchProjectFiles(borrowNid, HOST_URL);
				result.setFiles(files);
			}

			LogUtil.endLog(BorrowDefine.THIS_CLASS, BorrowDefine.BORROW_DETAIL_ACTION);
			result.setError(0);
			result.setErrorDesc("请求成功！");
			return JSONObject.toJSONString(result, true);
		}

	}
	
	/**
	 * 计算更新时间
	 * @param timeLoan
	 * @param timeRepay
	 * @return
	 */
	public static String getUpdateTime(Integer timeLoan, Integer timeRepay){
		if(timeLoan == null){
			return "";
		}
		
		Integer timeCurr = GetDate.getNowTime10();
		if(timeRepay != null && timeCurr > timeRepay){
			timeCurr = timeRepay;
		}
		
		Integer timeDiff = timeCurr - timeLoan;
		Integer timeDiffMonth = timeDiff/(60*60*24*31);
		
		Calendar timeLoanCal = Calendar.getInstance();
		timeLoanCal.setTimeInMillis(timeLoan * 1000L);
		
		if(timeDiffMonth >= 1){
			timeLoanCal.add(Calendar.MONTH, timeDiffMonth);
		}
		
		return GetDate.formatDate(timeLoanCal);
	}
	
}
