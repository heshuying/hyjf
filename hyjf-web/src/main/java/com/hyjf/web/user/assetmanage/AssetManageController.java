/**
 * Description:出借控制器
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:32:36
 * Modification History:
 * Modified by :
 */
package com.hyjf.web.user.assetmanage;

import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.bank.service.user.assetmanage.*;
import com.hyjf.bank.service.user.credit.CreditAssignedBean;
import com.hyjf.bank.service.user.credit.CreditService;
import com.hyjf.bank.service.user.credit.DebtConfigService;
import com.hyjf.bank.service.user.credit.MyCreditDetailAJaxBean;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;
import com.hyjf.mybatis.model.customize.web.*;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistListCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.agreement.AgreementService;
import com.hyjf.web.bank.web.borrow.BorrowDefine;
import com.hyjf.web.bank.web.user.credit.CreditController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.hjhplan.HjhPlanService;
import com.hyjf.web.user.planinfo.PlanInfoService;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资产管理
 *
 * @author HBZ
 */
@Controller
@RequestMapping(value = AssetManageDefine.REQUEST_MAPPING)
public class AssetManageController extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = AssetManageController.class.getName();

	@Autowired
	private AssetManageService assetManageService;

	@Autowired
	private CreditService tenderCreditService;
	@Autowired
	private DebtConfigService debtConfigService;
	
	//法大大
	@Autowired
    private FddGenerateContractService fddGenerateContractService;
	//汇添加
	@Autowired
	private PlanInfoService planInfoService;
	//汇计划
	@Autowired
    HjhPlanService hjhPlanService;
	//协议模版
	@Autowired
	private AgreementService agreementService;

	/**
	 * 资产管理页面初始化
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(AssetManageDefine.INIT_ACTION)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, AssetManageDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(AssetManageDefine.ASSET_MANAGE_INIT_PATH);
		Integer userId = WebUtils.getUserId(request);
		String currentTab = request.getParameter("currentTab");
		Account account = assetManageService.getAccount(userId);
		modelAndView.addObject("account", account);
		modelAndView.addObject("currentTab", currentTab);
		List<DebtConfig> config = debtConfigService.selectDebtConfigList();
		if(!CollectionUtils.isEmpty(config)){
			modelAndView.addObject("toggle",config.get(0).getToggle());
			modelAndView.addObject("closeDes",config.get(0).getCloseDes());
		}
		LogUtil.endLog(THIS_CLASS, AssetManageDefine.INIT_ACTION);
		return modelAndView;
	}

	/**
	 * 获取用户当前持有债权页面json数据
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AssetManageDefine.GET_CURRENT_HOLD_OBLIGATORY_RIGHT_ACTION, produces = "application/json;charset=utf-8")
	public ObligatoryRightAjaxBean getCurrentHoldObligatoryRight(@ModelAttribute AssetManageBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, AssetManageDefine.GET_CURRENT_HOLD_OBLIGATORY_RIGHT_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		form.setUserId(userId.toString());
		ObligatoryRightAjaxBean result = new ObligatoryRightAjaxBean();
		this.createCurrentHoldObligatoryRightListPage(result, form);
		LogUtil.endLog(THIS_CLASS, AssetManageDefine.GET_CURRENT_HOLD_OBLIGATORY_RIGHT_ACTION);
		return result;
	}

	/**
	 * 获取用户当前持有债权列表分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createCurrentHoldObligatoryRightListPage(ObligatoryRightAjaxBean result, AssetManageBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		params.put("userId", userId);
		params.put("startDate", form.getStartDate());
		params.put("endDate", form.getEndDate());
		params.put("orderByFlag", form.getOrderByFlag());
		params.put("sortBy", form.getSortBy());
		// 查询标签页显示数量
        searchListCount(result, params);
		// 获取用户当前持有债权总数
		int recordTotal = result.getCurrentHoldObligatoryRightCount();
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			// 获取用户当前持有债权列表
			List<CurrentHoldObligatoryRightListCustomize> recordList = assetManageService.selectCurrentHoldObligatoryRightList(params);
			if(recordList!=null && recordList.size()>0){
			    for (CurrentHoldObligatoryRightListCustomize currentHoldObligatoryRightListCustomize : recordList) {
			        String nid = currentHoldObligatoryRightListCustomize.getNid();
			        //法大大居间服务协议（type=2时候，为债转协议）
			        List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(nid);//居间协议
			        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
			            TenderAgreement tenderAgreement = tenderAgreementsNid.get(0);
			            Integer fddStatus = tenderAgreement.getStatus();
			            //法大大协议生成状态：0:初始,1:成功,2:失败，3下载成功
                        //System.out.println("******************1法大大协议状态："+tenderAgreement.getStatus());
			            if(fddStatus.equals(3)){
                            currentHoldObligatoryRightListCustomize.setFddStatus(1);
			            }else {
			                //隐藏下载按钮
			                //System.out.println("******************2法大大协议状态：0");
			                currentHoldObligatoryRightListCustomize.setFddStatus(0);
                        }
			        }else {
			            //下载老版本协议
			            //System.out.println("******************3法大大协议状态：2");
                        currentHoldObligatoryRightListCustomize.setFddStatus(1);
                    }
			    }
			}
			result.setPaginator(paginator);
			result.setCurrentHoldObligatoryRightList(recordList);
			result.setCurrentHoldObligatoryRightCount(recordTotal);
		} else {
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			result.setCurrentHoldObligatoryRightList(new ArrayList<CurrentHoldObligatoryRightListCustomize>());
			result.setCurrentHoldObligatoryRightCount(0);
		}
		
		result.success();
	}

	/**
	 * 获取用户已回款债权页面json数据
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AssetManageDefine.GET_REPAY_MENT_ACTION, produces = "application/json;charset=utf-8")
	public ObligatoryRightAjaxBean getRepayMent(@ModelAttribute AssetManageBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, AssetManageDefine.GET_REPAY_MENT_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		// Integer userId = 22401220;
		form.setUserId(userId.toString());
		ObligatoryRightAjaxBean result = new ObligatoryRightAjaxBean();
		this.createRepayMentListPage(result, form);
		LogUtil.endLog(THIS_CLASS, AssetManageDefine.GET_REPAY_MENT_ACTION);
		return result;
	}

	/**
	 * 获取用户已回款债权列表分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createRepayMentListPage(ObligatoryRightAjaxBean result, AssetManageBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		params.put("userId", userId);
		params.put("startDate", form.getStartDate());
		params.put("endDate", form.getEndDate());
		params.put("orderByFlag", form.getOrderByFlag());
		params.put("sortBy", form.getSortBy());
		// 查询标签页显示数量
        searchListCount(result, params);
		// 获取用户已回款债权总数
		int recordTotal = result.getRepayMentCount();
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			// 获取用户已回款债权列表
			List<RepayMentListCustomize> recordList = assetManageService.selectRepaymentList(params);
			result.setPaginator(paginator);
			result.setRepayMentList(recordList);
			result.setRepayMentCount(recordTotal);
		} else {
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			result.setRepayMentList(new ArrayList<RepayMentListCustomize>());
			result.setRepayMentCount(0);
		}
		result.success();
	}

	/**
	 * 获取用户转让记录债权页面json数据
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AssetManageDefine.GET_CREDIT_RECORD_LIST_ACTION, produces = "application/json;charset=utf-8")
	public ObligatoryRightAjaxBean getCreditRecordList(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, AssetManageDefine.GET_CREDIT_RECORD_LIST_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		ObligatoryRightAjaxBean result = new ObligatoryRightAjaxBean();

		try {
			if (userId != null && userId.intValue() != 0) {
				int paginatorPage = StringUtils.isNotEmpty(request.getParameter("paginatorPage")) ? Integer.parseInt(request.getParameter("paginatorPage")) : 1;
				int pageSize = StringUtils.isNotEmpty(request.getParameter("pageSize")) ? Integer.parseInt(request.getParameter("pageSize")) : 8;

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userId", userId);
				// 查询标签页显示数量
		        searchListCount(result, params);
				// 查询相应的汇消费列表的总数
				int recordTotal = result.getTenderCreditDetailCount();
				if (recordTotal > 0) {
					Paginator paginator = new Paginator(paginatorPage, recordTotal, pageSize);
					// 查询汇消费列表数据
					params.put("limitStart", paginator.getOffset());
					params.put("limitEnd", paginator.getLimit());
					List<TenderCreditDetailCustomize> recordList = tenderCreditService.selectCreditRecordList(params);
					result.setPaginator(paginator);
					result.setCreditRecordList(recordList);
				} else {
					result.setPaginator(new Paginator(paginatorPage, 0));
					result.setCreditRecordList(new ArrayList<TenderCreditDetailCustomize>());
				}
				result.success();
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), AssetManageDefine.GET_CREDIT_RECORD_LIST_ACTION, "系统异常");
			result.setPaginator(new Paginator(1, 0));
			result.setCreditRecordList(new ArrayList<TenderCreditDetailCustomize>());
		}

		LogUtil.endLog(THIS_CLASS, AssetManageDefine.GET_CREDIT_RECORD_LIST_ACTION);
		return result;
	}

	/**
	 * 转让明细
	 * 
	 * @param request
	 * @param response
	 * @param tenderCreditAssignedBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AssetManageDefine.GET_MYCREDIT_ASSIGN_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public MyCreditDetailAJaxBean searchCreditTenderAssignList(HttpServletRequest request, HttpServletResponse response, CreditAssignedBean tenderCreditAssignedBean) {
		LogUtil.startLog(CreditController.class.toString(), AssetManageDefine.GET_MYCREDIT_ASSIGN_ACTION);
		MyCreditDetailAJaxBean resultBean = new MyCreditDetailAJaxBean();

		Map<String, Object> params = new HashMap<String, Object>();
		String creditNid = request.getParameter("creditNid");

		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				params.put("creditNid", creditNid);
				// 查询相应的汇消费列表的总数
				int recordTotal = this.tenderCreditService.countCreditAssigned(params);
				if (recordTotal > 0) {
					// 查询汇消费列表数据
					long timestamp = System.currentTimeMillis() / 1000;
					params.put("timestamp", String.valueOf(timestamp));
					List<TenderCreditAssignedCustomize> recordList = tenderCreditService.selectCreditAssigned(params);
					
					resultBean.setRecordList(recordList);
				} else {
					resultBean.setRecordList(new ArrayList<TenderCreditAssignedCustomize>());
				}

				// 债转承接记录统计
				List<TenderCreditAssignedStatisticCustomize> statisticList = tenderCreditService.selectCreditTenderAssignedStatistic(params);
				if (statisticList != null & !statisticList.isEmpty()) {
					resultBean.setAssignedStatistic(statisticList.get(0));
				}
				
				BorrowCredit borrowCredit = tenderCreditService.getBorrowCredit(creditNid);
				resultBean.setBorrowCredit(borrowCredit);
				
				resultBean.success();
				LogUtil.endLog(CreditController.class.toString(), AssetManageDefine.GET_MYCREDIT_ASSIGN_ACTION);
			}
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "getMyCreditAssignDetail", "系统异常");
			resultBean.setRecordList(new ArrayList<TenderCreditAssignedCustomize>());
		}
		return resultBean;
	}

	private void searchListCount(ObligatoryRightAjaxBean result, Map<String, Object> params) {
		// 获取用户当前持有债权总数
		int currentHoldObligatoryRightCount = this.assetManageService.selectCurrentHoldObligatoryRightListTotal(params);
		result.setCurrentHoldObligatoryRightCount(currentHoldObligatoryRightCount);
		// 获取用户已回款债权列表总数
		//修改已回款债权总数 mod by nxl
//		int repaymentCount = this.assetManageService.selectRepaymentListTotal(params);
		int repaymentCount = this.assetManageService.selectRepaymentListTotalWeb(params);
		result.setRepayMentCount(repaymentCount);
		// 获取用户转让记录总数
		int tenderCreditDetailCount = this.tenderCreditService.countCreditRecordTotal(params);
		result.setTenderCreditDetailCount(tenderCreditDetailCount);
	}

	/**
	 * 获取用户当前持有计划页面json数据
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AssetManageDefine.GET_CURRENT_HOLD_PLAN_ACTION, produces = "application/json;charset=utf-8")
	public PlanAjaxBean getCurrentHoldPlan(@ModelAttribute AssetManageBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, AssetManageDefine.GET_CURRENT_HOLD_PLAN_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		PlanAjaxBean result = new PlanAjaxBean();
		if(userId == null){
			return result;
		}
		form.setUserId(userId.toString());
		this.createCurrentHoldPlanListPage(result, form);
		LogUtil.endLog(THIS_CLASS, AssetManageDefine.GET_CURRENT_HOLD_PLAN_ACTION);
		return result;
	}

	/**
	 * 获取用户当前持有计划列表分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createCurrentHoldPlanListPage(PlanAjaxBean result, AssetManageBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		params.put("userId", userId);
		params.put("startDate", form.getStartDate());
		params.put("endDate", form.getEndDate());
		params.put("orderByFlag", form.getOrderByFlag());
		params.put("sortBy", form.getSortBy());
		// 获取用户当前持有计划记录总数
		int recordTotal = this.assetManageService.countCurrentHoldPlanTotal(params);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			// 获取用户当前持有计划记录列表
			List<CurrentHoldPlanListCustomize> recordList = assetManageService.selectCurrentHoldPlanList(params);
			result.setPaginator(paginator);
			result.setCurrentHoldPlanList(recordList);
			result.setCurrentHoldPlanCount(recordTotal);
		} else {
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			result.setCurrentHoldPlanList(new ArrayList<CurrentHoldPlanListCustomize>());
			result.setCurrentHoldPlanCount(0);
		}
		result.setRepayMentPlanCount(this.assetManageService.countRepayMentPlanTotal(params));
		result.success();
	}

	/**
	 * 获取用户已回款计划页面json数据
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AssetManageDefine.GET_REPAY_MENT_PLAN_ACTION, produces = "application/json;charset=utf-8")
	public PlanAjaxBean getRepayMentPlan(@ModelAttribute AssetManageBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(THIS_CLASS, AssetManageDefine.GET_REPAY_MENT_PLAN_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		form.setUserId(userId.toString());
		PlanAjaxBean result = new PlanAjaxBean();
		this.createRepayMentPlanListPage(result, form);
		LogUtil.endLog(THIS_CLASS, AssetManageDefine.GET_REPAY_MENT_PLAN_ACTION);
		return result;
	}

	/**
	 * 获取用户已回款计划列表分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createRepayMentPlanListPage(PlanAjaxBean result, AssetManageBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		params.put("userId", userId);
		params.put("startDate", form.getStartDate());
		params.put("endDate", form.getEndDate());
		params.put("orderByFlag", form.getOrderByFlag());
		params.put("sortBy", form.getSortBy());
		// 获取用户当前持有计划记录总数
		int recordTotal = this.assetManageService.countRepayMentPlanTotal(params);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			// 获取用户当前持有计划记录列表
			List<RepayMentPlanListCustomize> recordList = assetManageService.selectRepayMentPlanList(params);
			//计算实际收益
			for(int i=0;i<recordList.size();i++){
				if(!"2".equals(recordList.get(i).getType()) && recordList.get(i).getRepayAccountYes()!=null&&recordList.get(i).getAccedeAccount()!=null){
	    			BigDecimal receivedTotal=new BigDecimal(recordList.get(i).getRepayAccountYes().replaceAll(",", "").trim());
	    			BigDecimal accedeAccount=new BigDecimal(recordList.get(i).getAccedeAccount().replaceAll(",", "").trim());
	    			BigDecimal userHjhInvistDetail=receivedTotal.subtract(accedeAccount);
	    			int account=userHjhInvistDetail.compareTo(BigDecimal.ZERO); 
	    			if(account==-1){
	    				recordList.get(i).setRepayInterestYes(BigDecimal.ZERO.toString());
	    				LogUtil.infoLog(this.getClass().getName(), AssetManageDefine.TO_MY_HJH_PLAN_INFO_DETAIL_PAGE_ACTION, "实际收益userHjhInvistDetail为负数");
	    			}else{
	    				recordList.get(i).setRepayInterestYes(userHjhInvistDetail.toString());
	    			}
	        	}
			}
			result.setPaginator(paginator);
			result.setRepayMentPlanList(recordList);
			result.setRepayMentPlanCount(recordTotal);
		} else {
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			result.setRepayMentPlanList(new ArrayList<RepayMentPlanListCustomize>());
			result.setCurrentHoldPlanCount(0);
		}
		result.setCurrentHoldPlanCount(this.assetManageService.countCurrentHoldPlanTotal(params));
		result.success();
	}

	/**
	 * 进入我的计划详情页面
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = AssetManageDefine.TO_MY_PLAN_INFO_DETAIL_PAGE_ACTION)
	public ModelAndView toMyPlanInfoDetailPage(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, AssetManageDefine.TO_MY_PLAN_INFO_DETAIL_PAGE_ACTION);

		ModelAndView modelAndView = new ModelAndView();

		WebViewUser wuser = WebUtils.getUser(request);
		if (wuser == null) {
			modelAndView.addObject("message", "用户信息失效，请您重新登录。");
			return new ModelAndView("error/systemerror");
		}
		String accedeOrderId = request.getParameter("accedeOrderId");
		// 页面固定传值0是出借中 1是锁定中 2是已回款
		String type = request.getParameter("type");
		Integer userId = wuser.getUserId();
		// 1基本信息
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accedeOrderId", accedeOrderId);
		params.put("userId", userId);
		List<PlanLockCustomize> recordList = planInfoService.selectUserProjectListCapital(params);
		if (recordList != null && recordList.size() > 0) {
			PlanLockCustomize planinfo = recordList.get(0);
			modelAndView.addObject("planinfo", planinfo);
			BigDecimal accedeAccount = new BigDecimal(planinfo.getAccedeAccount());
			BigDecimal lockPeriod = new BigDecimal(planinfo.getDebtLockPeriod());
			BigDecimal expectApr = new BigDecimal(planinfo.getExpectApr()).divide(new BigDecimal("100"));
			BigDecimal repayAccountYes = new BigDecimal(planinfo.getRepayAccountYes());
			// 2资产统计
			HashMap<String, Object> map = planInfoService.selectPlanInfoSum(accedeOrderId);
			BigDecimal investSum = BigDecimal.ZERO;
			if (map != null) {
				// 当前持有资产总计
				investSum = new BigDecimal(map.get("investSum") + "");
				modelAndView.addObject("investSum", investSum);
			}
			// 预计到期收益 加入计划金额*计划期限*计划收益率/12；
			BigDecimal expectIntrest = accedeAccount.multiply(lockPeriod).multiply(expectApr).divide(new BigDecimal("12"), 2, BigDecimal.ROUND_DOWN);
			modelAndView.addObject("expectIntrest", expectIntrest);
			// 回款总金额
			modelAndView.addObject("repayAccountYes", repayAccountYes);
			modelAndView.addObject("factIntrest", planinfo.getRepayInterestYes());
			Map<String, Object> params1 = new HashMap<String, Object>();
			params1.put("planOrderId", accedeOrderId);
			modelAndView.addObject("type", type);
			// params1.put("type", 1);
			// 3持有项目列表
			if (type != null && "1".equals(type)) {
				// 锁定中
				// TODO 不要分页 查两次 合并
				List<PlanInvestCustomize> debtInvestList = planInfoService.selectInvestCreditList(params1);
				List<PlanInvestCustomize> debtCreditList = planInfoService.selectCreditCreditList(params1);
				List<PlanInvestCustomize> tmpList = new ArrayList<PlanInvestCustomize>();
				if (debtInvestList != null) {
					tmpList.addAll(debtInvestList);
				}
				if (debtCreditList != null) {
					tmpList.addAll(debtCreditList);
				}

				modelAndView.addObject("debtInvestList", tmpList);
				modelAndView.setViewName(AssetManageDefine.TO_MY_PLAN_INFO_DETAIL_PAGE_PATH);

			} else if (type != null && "2".equals(type)) {
				params1.put("status", "11");
				// 已退出
				modelAndView.setViewName(AssetManageDefine.TO_MY_PLAN_INFO_DETAIL_PAGE_PATH);
				List<PlanInvestCustomize> debtInvestList = planInfoService.selectInvestCreditList(params1);
				List<PlanInvestCustomize> debtCreditList = planInfoService.selectCreditCreditList(params1);
				List<PlanInvestCustomize> tmpList = new ArrayList<PlanInvestCustomize>();
				if (debtInvestList != null) {
					tmpList.addAll(debtInvestList);
				}
				if (debtCreditList != null) {
					tmpList.addAll(debtCreditList);
				}
				modelAndView.addObject("debtInvestList", tmpList);
			} else {
				// 申购中
				modelAndView.setViewName(AssetManageDefine.TO_MY_PLAN_INFO_DETAIL_PAGE_PATH);
			}
		}
		LogUtil.endLog(THIS_CLASS, AssetManageDefine.TO_MY_PLAN_INFO_DETAIL_PAGE_ACTION);
		return modelAndView;
	}


	/**
	 * 进入我的汇计划详情页面
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = AssetManageDefine.TO_MY_HJH_PLAN_INFO_DETAIL_PAGE_ACTION)
    public ModelAndView toMyHjhPlanInfoDetailPage(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(THIS_CLASS, AssetManageDefine.TO_MY_HJH_PLAN_INFO_DETAIL_PAGE_ACTION);

        ModelAndView modelAndView = new ModelAndView();

        WebViewUser wuser = WebUtils.getUser(request);
        if (wuser == null) {
            modelAndView.addObject("message", "用户信息失效，请您重新登录。");
            return new ModelAndView("error/systemerror");
        }
        String accedeOrderId = request.getParameter("accedeOrderId");
        // 页面固定传值0是出借中 1是锁定中 2是已回款
        String type = request.getParameter("type");
        Integer userId = wuser.getUserId();
        // 1基本信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accedeOrderId", accedeOrderId);
        params.put("userId", userId);
        UserHjhInvistDetailCustomize customize = hjhPlanService.selectUserHjhInvistDetail(params);
        if (customize != null)  {
        	// add by nxl 智投服务：格式化参考回报Start
			if(customize.getWaitInterest().equals("0.00")){
				customize.setWaitInterest("--");
			}
			// add by nxl 智投服务：格式化参考回报End
			// add by nxl 图标授权时间格式化
			if(StringUtils.isNotBlank(customize.getAddTime())){
				customize.setAddTimeFormat(customize.getAddTime().substring(0,10));
			}
        	//计算实际收益
        	if(type != null && "2".equals(type)){
        		if(customize.getAccedeAccount()!=null&&customize.getReceivedTotal()!=null){
        			BigDecimal receivedTotal=new BigDecimal(customize.getReceivedTotal().replaceAll(",", "").trim());
        			BigDecimal accedeAccount=new BigDecimal(customize.getAccedeAccount().replaceAll(",", "").trim());
        			BigDecimal userHjhInvistDetail=receivedTotal.subtract(accedeAccount);
        			int i=userHjhInvistDetail.compareTo(BigDecimal.ZERO); 
        			if(i==-1){
        				customize.setReceivedInterest(BigDecimal.ZERO.toString());
        				LogUtil.infoLog(this.getClass().getName(), AssetManageDefine.TO_MY_HJH_PLAN_INFO_DETAIL_PAGE_ACTION, "实际收益userHjhInvistDetail为负数");
        			}else{
        				customize.setReceivedInterest(userHjhInvistDetail.toString());
        			}
            	}
        	}
        	List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(accedeOrderId);//居间协议
        	if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
                TenderAgreement tenderAgreement = tenderAgreementsNid.get(0);
                Integer fddStatus = tenderAgreement.getStatus();
                //法大大协议生成状态：0:初始,1:成功,2:失败，3下载成功
                //System.out.println("******************1法大大协议状态："+tenderAgreement.getStatus());
                if(fddStatus.equals(3)){
                    customize.setFddStatus(1);
                }else {
                    //隐藏下载按钮
                    //System.out.println("******************2法大大协议状态：0");
                    customize.setFddStatus(0);
                }
            }else {
                //下载老版本协议
                //System.out.println("******************3法大大协议状态：2");
                customize.setFddStatus(1);
            }
        	// add 汇计划二期前端优化 持有中计划详情修改锁定期和实际退出时间 nxl 20180419 start
			SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
			Date datePeriod = null;
			if (StringUtils.isNotEmpty(customize.getCountInterestTime())&&!customize.getCountInterestTime().equals("待确认")) {
				try {
					Date dateAddTime = smp.parse(customize.getCountInterestTime());
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(dateAddTime);
					if (customize.getPlanPeriod().contains("天")) {
						String days = customize.getPlanPeriod().split("天")[0];
						int intD = Integer.parseInt(days);
						calendar.add(Calendar.DAY_OF_MONTH, +intD);
						datePeriod = calendar.getTime();
					}
					if (customize.getPlanPeriod().contains("个月")) {
						String days = customize.getPlanPeriod().split("个月")[0];
						int intD = Integer.parseInt(days);
						calendar.add(Calendar.MONTH, +intD);
						datePeriod = calendar.getTime();
					}
					if (datePeriod != null) {
						String endStrDate = smp.format(datePeriod);
						String startStrDate = customize.getAddTime().substring(0, 10);
						// mod by nxl 智投服务，修改服务期限的显示格式
//						customize.setPlanPeriod(startStrDate + "~" + endStrDate);
						// add by nxl 20180903 智投服务添加计息结束日
						customize.setEndInterestTime(endStrDate);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else {
				// mod by nxl 智投服务，修改服务期限的显示格式
//				customize.setPlanPeriod("— —");
				//add by nxl 如果开始计息时间为空或为待确认是,将开始计息时间设置为空
				customize.setCountInterestTime(null);
				customize.setEndInterestTime(null);
			}
			// add by nxl 智投服务：计划状态为退出中显示开始退出时间 start
			// 计划状态为5 代表退出中(如果不是退出中并且是持有中的时候，将开始推出时间设置为空）
			if(!customize.getOrderStatus().equals("5")&&type.equals("1")){
				customize.setEndInterestTime(null);
			}
			// add by nxl 智投服务：计划状态为退出中显示开始退出时间 end
			// 实际退出时间
			/*if(StringUtils.isEmpty(customize.getRepayActualTime())) {
				customize.setRepayActualTime("— —");
			}*/
			// add 汇计划二期前端优化 持有中计划详情修改锁定期和实际退出时间 nxl 20180419 end
            modelAndView.addObject("userHjhInvistDetail", customize);
            modelAndView.addObject("type", type);
            // params1.put("type", 1);
			//协议名称 动态获得
			List<ProtocolTemplate> list = agreementService.getdisplayNameDynamic();
			if(org.apache.commons.collections.CollectionUtils.isNotEmpty(list)){
				//是否在枚举中有定义
				for (ProtocolTemplate p : list) {
					String protocolType = p.getProtocolType();
					String alia = ProtocolEnum.getAlias(protocolType);
					if (alia != null){
						modelAndView.addObject(alia, p.getDisplayName());
					}
				}
			}
            // 3持有项目列表
            if (type != null && "1".equals(type)) {
                // 锁定中
                List<UserHjhInvistListCustomize> userHjhInvistBorrowList = hjhPlanService.selectUserHjhInvistBorrowList(params);
                List<UserHjhInvistListCustomize> tmpList = new ArrayList<UserHjhInvistListCustomize>();
                if (userHjhInvistBorrowList != null) {
					// add by cwyang 2018-3-27 计划持有项目显示协议下载按钮
					for (int i = 0; i < userHjhInvistBorrowList.size(); i++) {
						UserHjhInvistListCustomize userHjhInvistListCustomize = userHjhInvistBorrowList.get(i);
						String nid = userHjhInvistListCustomize.getNid();
						List<TenderAgreement> tenderAgreements= fddGenerateContractService.selectByExample(nid);
						if(tenderAgreements!=null && tenderAgreements.size()>0){
			                TenderAgreement tenderAgreement = tenderAgreements.get(0);
			                Integer fddStatus = tenderAgreement.getStatus();
			                //法大大协议生成状态：0:初始,1:成功,2:失败，3下载成功
			                //System.out.println("******************1法大大协议状态："+tenderAgreement.getStatus());
			                if(fddStatus.equals(3)){
			                    userHjhInvistListCustomize.setFddStatus(1);
			                }else {
			                    //隐藏下载按钮
			                    //System.out.println("******************2法大大协议状态：0");
			                    userHjhInvistListCustomize.setFddStatus(0);
			                }
			            }else {
			                //下载老版本协议
			                //System.out.println("******************3法大大协议状态：2");
			                userHjhInvistListCustomize.setFddStatus(1);
			            }
					}
                    tmpList.addAll(userHjhInvistBorrowList);
                }
                modelAndView.addObject("investList", tmpList);
                modelAndView.setViewName(AssetManageDefine.TO_MY_HJH_PLAN_INFO_DETAIL_PAGE_PATH);

            } else if (type != null && "2".equals(type)) {
                // 已退出
                modelAndView.setViewName(AssetManageDefine.TO_MY_HJH_PLAN_INFO_DETAIL_PAGE_PATH);
                List<UserHjhInvistListCustomize> debtInvestList = hjhPlanService.selectUserHjhInvistBorrowList(params);
                List<UserHjhInvistListCustomize> tmpList = new ArrayList<UserHjhInvistListCustomize>();
                if (debtInvestList != null) {
                    tmpList.addAll(debtInvestList);
                }
                modelAndView.addObject("investList", tmpList);
            } else {
                // 申购中
                modelAndView.setViewName(AssetManageDefine.TO_MY_HJH_PLAN_INFO_DETAIL_PAGE_PATH);
            }
        }else{
            modelAndView.addObject("message", "用户信息失效，请您重新查询。");
            return new ModelAndView("error/systemerror");
        }
        
        LogUtil.endLog(THIS_CLASS, AssetManageDefine.TO_MY_HJH_PLAN_INFO_DETAIL_PAGE_ACTION);
        return modelAndView;
    }

    /**
     * 获取还款计划json数据
     * 
     * @param project
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AssetManageDefine.GET_REPAYMENT_PLAN_ACTION, produces = "application/json;charset=utf-8")
    public RepaymentPlanAjaxBean getRepaymentPlan(
            HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(THIS_CLASS, AssetManageDefine.GET_REPAYMENT_PLAN_ACTION);
        String borrowNid=request.getParameter("borrowNid");
        String nid=request.getParameter("nid");
        String type=request.getParameter("typeStr");
        RepaymentPlanAjaxBean result = new RepaymentPlanAjaxBean();

        this.createRepaymentPlanPage(result, borrowNid,nid,type);
        LogUtil.endLog(THIS_CLASS, AssetManageDefine.GET_REPAYMENT_PLAN_ACTION);
        return result;
    }


    /**
     * 获取还款计划列表分页数据
     * 
     * @param request
     * @param info
     * @param form
     */
    private void createRepaymentPlanPage(RepaymentPlanAjaxBean result, String borrowNid,String nid,String type) {
        BorrowWithBLOBs borrow=null;
        //type 出借记录类型  0现金出借，1部分债转，2债权承接，3优惠券出借，4 融通宝产品加息
        switch (type) {
        case "0":
            borrow=assetManageService.getBorrowByNid(borrowNid);
            if("endday".equals(borrow.getBorrowStyle())||"end".equals(borrow.getBorrowStyle())){
                //真实出借不分期还款计划查询
                assetManageService.realInvestmentRepaymentPlanNoStages(result,borrowNid,nid);
            } else {
                //真实出借分期还款计划查询
                assetManageService.realInvestmentRepaymentPlanStages(result,borrowNid,nid);
            }
            break;
        case "1":
            borrow=assetManageService.getBorrowByNid(borrowNid);
            if("endday".equals(borrow.getBorrowStyle())||"end".equals(borrow.getBorrowStyle())){
                //部分债转不分期还款计划查询
                assetManageService.assignRepaymentPlanNoStages(result,borrowNid,nid);
            } else {
                //部分债转分期还款计划查询
                assetManageService.assignRepaymentPlanStages(result,borrowNid,nid);
            }
            
            break;
        case "2": 
            //债权承接还款计划查询
            assetManageService.creditRepaymentPlan(result,borrowNid,nid);
            break;
        case "3":
            //优惠券还款计划查询
            assetManageService.couponRepaymentPlan(result,borrowNid,nid);
            break;
        case "4":
            
            borrow=assetManageService.getBorrowByNid(borrowNid);
            if("endday".equals(borrow.getBorrowStyle())||"end".equals(borrow.getBorrowStyle())){
                //融通宝不分期还款计划查询
                assetManageService.rtbRepaymentPlanNoStages(result,borrowNid,nid);
            } else {
                //融通宝分期还款计划查询
                assetManageService.rtbRepaymentPlanStages(result,borrowNid,nid);
            }
            break;
        }
    }
    
    
    /**
     * 获取指定类型的项目的列表
     * 
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AssetManageDefine.BORROW_LIST_ACTION, produces = "application/json; charset=utf-8")
    public HjhBorrowListAJaxBean searchBorrowList(@ModelAttribute HjhBorrowBean form, HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(THIS_CLASS, AssetManageDefine.BORROW_LIST_ACTION);
        WebViewUser wuser = WebUtils.getUser(request);
        form.setUserId(wuser.getUserId());
        HjhBorrowListAJaxBean result = new HjhBorrowListAJaxBean();
        this.createBorrowListPage(result, form);
        result.success();
        result.setHost(BorrowDefine.HOST);
        LogUtil.endLog(THIS_CLASS, AssetManageDefine.BORROW_LIST_ACTION);
        return result;
    }

    /**
     * 查询相应的项目分页列表
     * 
     * @param info
     * @param form
     */
    private void createBorrowListPage(HjhBorrowListAJaxBean result, HjhBorrowBean form) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accedeOrderId", form.getAccedeOrderId());
        params.put("userId", form.getUserId());
        
        
        int projecTotal = this.hjhPlanService.countUserHjhInvistBorrowListTotal(params);
        
        if (projecTotal > 0) {
            
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
            List<UserHjhInvistListCustomize> tmpList = hjhPlanService.selectUserHjhInvistBorrowList(params);
            if(tmpList!=null && tmpList.size()>0){
                for (UserHjhInvistListCustomize userHjhInvistListCustomize : tmpList) {
                    String nid = userHjhInvistListCustomize.getNid();//居间
                    String investOrderId = userHjhInvistListCustomize.getInvestOrderId();//债转
                    //法大大居间服务协议（type=2时候，为债转协议）
                    List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(nid);//居间协议
                    List<TenderAgreement> tenderAgreementsOrderId= fddGenerateContractService.selectByExample(investOrderId);//债转协议
                    if("0".equals(userHjhInvistListCustomize.getType())){//居间协议
                        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
                            TenderAgreement tenderAgreement = tenderAgreementsNid.get(0);
                            Integer fddStatus = tenderAgreement.getStatus();
                            //法大大协议生成状态：0:初始,1:成功,2:失败，3下载成功
                            //System.out.println("******************1法大大协议状态："+tenderAgreement.getStatus());
                            if(fddStatus.equals(3)){
                                userHjhInvistListCustomize.setFddStatus(1);
                            }else {
                                //隐藏下载按钮
                                //System.out.println("******************2法大大协议状态：0");
                                userHjhInvistListCustomize.setFddStatus(0);
                            }
                        }else {
                            //下载老版本协议
                            //System.out.println("******************3法大大协议状态：2");
                            userHjhInvistListCustomize.setFddStatus(1);
                        }
                    }else{//债转协议
                        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
                            TenderAgreement tenderAgreement = tenderAgreementsNid.get(0);
                            Integer fddStatus = tenderAgreement.getStatus();
                            //法大大协议生成状态：0:初始,1:成功,2:失败，3下载成功
                            //System.out.println("******************1法大大协议状态："+tenderAgreement.getStatus());
                            if(fddStatus.equals(3)){
                                userHjhInvistListCustomize.setFddStatus(1);
                            }else {
                                //隐藏下载按钮
                                //System.out.println("******************2法大大协议状态：0");
                                userHjhInvistListCustomize.setFddStatus(0);
                            }
                        }else {
                            //下载老版本协议
                            //System.out.println("******************3法大大协议状态：2");
                            userHjhInvistListCustomize.setFddStatus(1);
                        }
                    }
                    
                }
            }
            result.setProjectList(tmpList);
            result.setPaginator(paginator);
            int nowTime = GetDate.getNowTime10();
            result.setNowTime(nowTime);
        }
    }
    
    
}
