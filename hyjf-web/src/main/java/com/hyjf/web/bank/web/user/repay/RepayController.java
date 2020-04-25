/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
package com.hyjf.web.bank.web.user.repay;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.bank.service.user.credit.CreditAssignedBean;
import com.hyjf.bank.service.user.credit.CreditService;
import com.hyjf.bank.service.user.repay.ProjectBean;
import com.hyjf.bank.service.user.repay.RepayBean;
import com.hyjf.bank.service.user.repay.RepayProjectListBean;
import com.hyjf.bank.service.user.repay.RepayService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.common.zip.ZIPGenerator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
//import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
//import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserRepayProjectListCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.agreement.CreateAgreementController;
import com.hyjf.web.bank.web.borrow.batchloan.BatchLoanDefine;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.login.LoginService;
import com.hyjf.web.user.mytender.MyTenderDefine;
import com.hyjf.web.user.mytender.MyTenderService;
import com.hyjf.web.user.mytender.ProjectRepayListBean;
import com.hyjf.web.user.mytender.UserInvestListBean;
import com.hyjf.web.user.planinfo.PlanInfoService;
import com.hyjf.web.user.repay.InvestListBean;
import com.hyjf.web.util.WebUtils;


@Controller
@RequestMapping(value = RepayDefine.REQUEST_MAPPING)
public class RepayController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(RepayController.class);

	@Autowired
	private RepayService repayService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private MyTenderService mytenderService;

	@Autowired
	private PlanInfoService planInfoService;
	
	@Autowired
	private CreditService tenderCreditService;
	
	@Autowired
    private FddGenerateContractService fddGenerateContractService;
	
	@Autowired
	private CreateAgreementController createAgreementController;
	@Autowired
    private AuthService authService;

	public static JedisPool pool = RedisUtils.getPool();

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	/** 用户ID */
	private static final String VAL_USERID = "userId";
	/**总还款数量*/
	private static final String VAL_ALLCOUNT = "allCount";
	/**成功数量*/
	private static final String VAL_SUCCESSCOUNT = "successCount";
	/**失败数量*/
	private static final String VAL_FAILCOUNT = "failCount";

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	/**
	 * 
	 * 查看协议
	 * 
	 * @author renxingchen
	 * @param request 
	 * @param borrowNid
	 * @return
	 */
	@RequestMapping(value = RepayDefine.GET_AGREEMENT_ACTION)
	public ModelAndView getAgreement(HttpServletRequest request, String borrowNid) {
		ModelAndView modelAndView = new ModelAndView();
		WebViewUser user = WebUtils.getUser(request);
		Borrow borrow = this.repayService.searchRepayProject(user.getUserId(), user.getUsername(), user.getRoleId(), borrowNid);
		if (null != borrow.getBorrowFullTime()) {
			modelAndView.addObject("borrowFullTime", new Date(borrow.getBorrowFullTime() * 1000L));
		} else {
			modelAndView.addObject("borrowFullTime", new Date((Integer.parseInt(borrow.getVerifyTime()) + borrow.getBorrowValidTime() * 24 * 60 * 60) * 1000L));
		}
		modelAndView.addObject("username", user.getUsername());
		modelAndView.addObject("borrowNid", borrowNid);
		// 查询用户项目的出借情况
		List<WebUserInvestListCustomize> investlist = repayService.selectUserInvestList(borrowNid, -1, -1);
		modelAndView.addObject("investlist", investlist);
		if (borrow.getProjectType() == 8) {
			modelAndView.setViewName(RepayDefine.AGREEMENT_CONTRACT_HXF_PAGE);
		} else {
			modelAndView.setViewName(RepayDefine.AGREEMENT_CONTRACT_PAGE);
		}
		return modelAndView;
	}

	/**
	 * 
	 * 检测还款密码
	 * 
	 * @author renxingchen
	 * @param request
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = RepayDefine.CHECK_PASSWORD_ACTION)
	public boolean checkPassword(HttpServletRequest request, String password) {
		WebViewUser user = WebUtils.getUser(request);
		if (null == user) {
			return true;
		} else {
			if (loginService.queryPasswordAction2(user.getUsername(), password) < 0) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 
	 * 用户还款页面
	 * 
	 * @author renxingchen
	 * @param request
	 * @return
	 */
	@RequestMapping(value = RepayDefine.USER_REPAY_PAGE_ACTION)
	public ModelAndView userRepayPage(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(RepayDefine.USER_REPAY_PAGE);
		// 查询该用户的未还款金额
		WebViewUser user = WebUtils.getUser(request);
		String tab = request.getParameter("tab")==null?"":request.getParameter("tab");// 加载显示的标签
		if (user != null) {
			// 根据RoleId 判断用户为借款人
			if ("2".equals(user.getRoleId())) {
				Account account = repayService.getAccount(user.getUserId());
				// 1.待还款总额
				BigDecimal repay = account.getBankWaitCapital().add(account.getBankWaitInterest());
				BigDecimal repayMangeFee = repayService.getRepayMangeFee(user.getRoleId(),user.getUserId());
				repay = repay.add(repayMangeFee);
				// 2.待还垫付总额 bank_wait_repay_org
				BigDecimal advancedMoney = account.getBankWaitRepayOrg();
				modelAndView.addObject("repayMoney", CustomConstants.DF_FOR_VIEW.format(repay));
				modelAndView.addObject("repayAdvancedMoney", CustomConstants.DF_FOR_VIEW.format(advancedMoney));
				HjhUserAuth hjhUserAuth = this.authService.getHjhUserAuthByUserId(user.getUserId());
				modelAndView.addObject("repayAuthStatus",hjhUserAuth==null?"":hjhUserAuth.getAutoRepayStatus());
			}
			// 根据RoleId 判断用户为垫付机构
			else if ("3".equals(user.getRoleId())) {
				// 1.待垫付总额
				BigDecimal repay = repayService.getRepayOrgRepaywait(user.getUserId());
				BigDecimal repayMangeFee = repayService.getRepayMangeFee(user.getRoleId(),user.getUserId());
				repay = repay.add(repayMangeFee);
				modelAndView.addObject("repayMoney", CustomConstants.DF_FOR_VIEW.format(repay));
				// 2.待收垫付总额 bank_await_org
				BigDecimal uncollectedRepay = repayService.getUncollectedRepayOrgRepaywait(user.getUserId());
				modelAndView.addObject("uncollectedRepay", CustomConstants.DF_FOR_VIEW.format(uncollectedRepay));
				String showFlag = request.getParameter("showFlag"); 
				if (StringUtils.isNotBlank(showFlag)) {
					modelAndView.addObject("showFlag", showFlag);
				}
				// 垫付机构不用校验还款授权
				modelAndView.addObject("repayAuthStatus","");
			}
			modelAndView.addObject("roleId", user.getRoleId());
			modelAndView.addObject("userId",user.getUserId());

			modelAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
			modelAndView.addObject("paymentAuthStatus",user.getPaymentAuthStatus());
			// modelAndView.addObject("repayAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_REPAYMENT_AUTH).getEnabledStatus());
			// 还款授权不需要校验  张宁说的
			modelAndView.addObject("repayAuthOn", "0");

		}
		modelAndView.addObject("tab",tab);
		return modelAndView;
	}

	/**
	 * 用户代还标的,债转详情.
	 * @param request
	 * @return
	 * @Author : huanghui
	 */
	@RequestMapping(value = RepayDefine.USER_REPAY_DETAIL)
	public ModelAndView userRepayDetail(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(RepayDefine.USER_REPAY_DETAIL_PAGE);

		String borrowNid = request.getParameter("borrowNid");
		WebViewUser userInfo = WebUtils.getUser(request);

		/** 当前用户已登录并且标的NID不为空 */
		String verificationFlag = null;
		if (userInfo != null && StringUtils.isNotBlank(borrowNid)){
			WebUserTransferBorrowInfoCustomize borrowInfo = this.repayService.getBorrowInfo(borrowNid);
			// 单纯的作为验证标识.

			if (borrowInfo.getPlanNid() != null){
				verificationFlag = borrowInfo.getPlanNid();
			}else {
				verificationFlag = null;
			}

			//居间协议
			Integer fddStatus = 0;
			List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(borrowNid);
			if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
				TenderAgreement tenderAgreement = tenderAgreementsNid.get(0);
				fddStatus = tenderAgreement.getStatus();
				//法大大协议生成状态：0:初始,1:成功,2:失败，3下载成功
				if(fddStatus.equals(3)){
					fddStatus = 1;
				}else {
					//隐藏下载按钮
					fddStatus = 0;
				}
			}else {
				//下载老版本协议
				fddStatus = 1;
			}

			// 计算到账金额
			BigDecimal oldYesAccount = borrowInfo.getSucSmount();
			borrowInfo.setSucSmount(oldYesAccount.subtract(borrowInfo.getServiceFee()));

			modelAndView.addObject("verificationFlag", verificationFlag);
			modelAndView.addObject("borrowInfo", borrowInfo);
			modelAndView.addObject("fddStatus", fddStatus);
		}
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = RepayDefine.USER_REPAY_DETAIL_AJAX, produces = "application/json; charset=utf-8")
	public RepayListAjaxResult userRepayDetailAjax(HttpServletRequest request, HttpServletResponse response){
		RepayListAjaxResult results = new RepayListAjaxResult();

		this.createTransferListPage(request, results, request.getParameter("borrowNid"), request.getParameter("verificationFlag"));

		results.setHost(RepayDefine.HOST);
		return results;
	}

	private void createTransferListPage(HttpServletRequest request, RepayListAjaxResult results, String borrowNid, String verificationFlag) {
		// 查询承接条数(标的发生过债转,并且已有人承接)

		int listCount = repayService.selectUserRepayTransferDetailListTotal(borrowNid, verificationFlag);
		if (listCount > 0){

			String thisPaginatorPage = request.getParameter("paginatorPage");
			int intThisPaginatorPage = Integer.valueOf(thisPaginatorPage).intValue();
			Paginator paginator = null;
			if (intThisPaginatorPage > 1){
				paginator = new Paginator(intThisPaginatorPage, listCount, results.getPageSize());
			}else {
				paginator = new Paginator(results.getPaginatorPage(), listCount, results.getPageSize());
			}


			List<WebUserRepayTransferCustomize> repayList = repayService.selectUserRepayTransferDetailList(borrowNid, verificationFlag, paginator.getOffset(), paginator.getLimit());

			// 数据格式化的格式 10,000.00
			DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
			// 遍历列表, 给承接人和转让人用户名加密
			for (WebUserRepayTransferCustomize re: repayList) {
				re.setCreditUserName(repayService.usernameEncryption(re.getCreditUserName()));
				re.setUndertakerUserName(repayService.usernameEncryption(re.getUndertakerUserName()));
				re.setAssignCapitalString(decimalFormat.format(re.getAssignCapital()));
			}

			results.success();
			results.setPaginator(paginator);
			results.setTransferList(repayList);
		}else {
			Paginator paginator = new Paginator(results.getPaginatorPage(), listCount, results.getPageSize());
			results.success();
			results.setPaginator(paginator);
		}
	}
	/**
	 * 获取用户的借款项目
	 *
	 * @param userRepay
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = RepayDefine.REPAY_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public RepayListAjaxResult searchUserRepayList(@ModelAttribute RepayProjectListBean userRepay, HttpServletRequest request, HttpServletResponse response) {

		RepayListAjaxResult result = new RepayListAjaxResult();
		// 用户ID
		WebViewUser user = WebUtils.getUser(request);
		if (user != null) {
			userRepay.setUserId(user.getUserId().toString());
			userRepay.setRoleId(user.getRoleId());// 角色分借款人、垫付机构
			result.setRoleId(user.getRoleId());
			this.createUserRepayListPage(request, result, userRepay);
		}
		return result;
	}

	/**
	 * TODO 获取用户本期还款金额
	 *
	 * @param userRepay
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = RepayDefine.GET_REPAY_MONEY_TOTAL_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public JSONObject getRepayMoneyTotalAction(@ModelAttribute RepayProjectListBean userRepay, HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(RepayController.class.toString(), RepayDefine.GET_REPAY_MONEY_TOTAL_ACTION);
		JSONObject json=new JSONObject();
		BigDecimal repayMoneyTotal=new BigDecimal(0);
		int repayMoneyNum=0;
		// 用户ID
		WebViewUser user = WebUtils.getUser(request);
		String borrowNid=request.getParameter("borrowNid").trim();
		String startDate=request.getParameter("repayStartDateSrch");
		String endDate=request.getParameter("repayEndDateSrch");
		if (user != null) {
			userRepay.setUserId(user.getUserId().toString());
			userRepay.setRoleId(user.getRoleId());// 角色分借款人、垫付机构
			userRepay.setStatus("0");
			userRepay.setRepayStatus("0");
			userRepay.setBorrowNid(borrowNid);//项目名称
			userRepay.setEndDate(endDate);//应还的截止时间
			userRepay.setStartDate(startDate);//应还的开始时间
//			int recordTotal = this.repayService.countUserRepayRecordTotal(form);
			int recordTotal = this.repayService.countUserRepayRecordTotal(userRepay);// 查询记录总数（个人和机构）
			if (recordTotal > 0) {
				Paginator paginator = new Paginator(userRepay.getPaginatorPage(), recordTotal, userRepay.getPageSize());
				// 获取借款人用户待还款（或已还款 ）by role ,status的项目列表
				// 获取垫付机构用户待垫付（或已垫付 ）by role ,status的项目列表      ---->应该这个字段realAccountYes
				List<WebUserRepayProjectListCustomize> recordList = repayService.searchUserRepayList(userRepay, 0, recordTotal);
				if(!CollectionUtils.isEmpty(recordList)){
					repayMoneyNum=recordTotal;
					log.info("recordList:{}",recordList.size() );
					for (WebUserRepayProjectListCustomize customize : recordList) {
							repayMoneyTotal=repayMoneyTotal.add(new BigDecimal(customize.getRealAccountYes()));
					}

				}
			}
		}
		json.put("repayMoneyTotal",repayMoneyTotal);
		json.put("repayMoneyNum",repayMoneyNum);
		LogUtil.endLog(RepayDefine.THIS_CLASS, RepayDefine.GET_REPAY_MONEY_TOTAL_ACTION+">>>>>>>>>>>>>>>>");
		return json;
	}
	/**
	 * 垫付机构已垫付列表
	 * 
	 * @param userRepay
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = RepayDefine.ORG_REPAY_LIST_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public RepayListAjaxResult searchOrgRepayList(@ModelAttribute RepayProjectListBean userRepay, HttpServletRequest request, HttpServletResponse response) {
		RepayListAjaxResult result = new RepayListAjaxResult();
		// 用户ID
		WebViewUser user = WebUtils.getUser(request);
		if (user != null) {
			userRepay.setUserId(user.getUserId().toString());
			userRepay.setRoleId(user.getRoleId());// 角色分借款人、垫付机构
			result.setRoleId(user.getRoleId());
			this.createOrgRepayListPage(request, result, userRepay);
		}
		return result;
	}

	/**
	 * 垫付机构已垫付项目
	 * 
	 * @param request
	 * @param result
	 * @param form
	 */
	private void createOrgRepayListPage(HttpServletRequest request, RepayListAjaxResult result, RepayProjectListBean form) {
		int recordTotal = this.repayService.countOrgRepayRecordTotal(form);// 查询记录总数(垫付机构已垫付还款项目)
		if(recordTotal > 0){
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			List<WebUserRepayProjectListCustomize> recordList = repayService.searchOrgRepayList(form, paginator.getOffset(), paginator.getLimit());
			if(recordList!=null && recordList.size()>0){
                for (WebUserRepayProjectListCustomize customize : recordList) {
                 // 查询用户项目的出借情况
                    List<WebUserInvestListCustomize> investlist = repayService.selectUserInvestList(customize.getBorrowNid(), -1, -1);
                    //遍历每一个出借
                    for (WebUserInvestListCustomize webUserInvestListCustomize : investlist) {
                        List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(webUserInvestListCustomize.getNid());//居间协议
                        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
                            TenderAgreement tenderAgreement = tenderAgreementsNid.get(0);
                            Integer fddStatus = tenderAgreement.getStatus();
                            //法大大协议生成状态：0:初始,1:成功,2:失败，3下载成功
//                            System.out.println("******************1法大大协议状态："+tenderAgreement.getStatus());
                            if(fddStatus.equals(3)){
                                customize.setFddStatus(1);
                            }else {
                                //隐藏下载按钮
//                                System.out.println("******************2法大大协议状态：0");
                                customize.setFddStatus(0);
                            }
                        }else {
                            //下载老版本协议
//                            System.out.println("******************3法大大协议状态：2");
                            customize.setFddStatus(1);
                        }
                    }
                   
                }
            }
			result.success();
			result.setPaginator(paginator);
			result.setRepayList(recordList);
			result.setNowdate(new java.util.Date());
		}else{
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			result.success();
			result.setPaginator(paginator);
		}
	}

	/**
	 * 获取用户还款项目分页信息
	 * 
	 * @param request
	 * @param result
	 * @param form
	 */
	private void createUserRepayListPage(HttpServletRequest request, RepayListAjaxResult result, RepayProjectListBean form) {

		int recordTotal = this.repayService.countUserRepayRecordTotal(form);// 查询记录总数（个人和机构）
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			// 获取借款人用户待还款（或已还款 ）by role ,status的项目列表
			// 获取垫付机构用户待垫付（或已垫付 ）by role ,status的项目列表
			List<WebUserRepayProjectListCustomize> recordList = repayService.searchUserRepayList(form, paginator.getOffset(), paginator.getLimit());
			if(recordList!=null && recordList.size()>0){
			    for (WebUserRepayProjectListCustomize customize : recordList) {

                    // 查询承接条数(标的发生过债转,并且已有人承接)
                    int listCount = repayService.selectUserRepayTransferDetailListTotal(customize.getBorrowNid(), customize.getPlanNid());
                    customize.setListCount(listCount);
			     // 查询用户项目的出借情况
			        List<WebUserInvestListCustomize> investlist = repayService.selectUserInvestList(customize.getBorrowNid(), -1, -1);
			        //遍历每一个出借
			        for (WebUserInvestListCustomize webUserInvestListCustomize : investlist) {
			            List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(webUserInvestListCustomize.getNid());//居间协议
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
			            }
			      }
			}
			result.success();
			result.setPaginator(paginator);
			result.setRepayList(recordList);
			result.setNowdate(new java.util.Date());
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			result.success();
			result.setPaginator(paginator);
		}
	}

	/**
	 * 获取用户借款项目的用户出借详情列表
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = RepayDefine.INVEST_LIST_ACTION, produces = "application/json; charset=utf-8")
	public String searchUserInvestList(@ModelAttribute InvestListBean form, HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(RepayDefine.THIS_CLASS, RepayDefine.INVEST_LIST_ACTION);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		createUserInvestPage(request, info, form);
		ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
		ret.put(CustomConstants.DATA, info);
		ret.put(CustomConstants.MSG, "");
		LogUtil.endLog(RepayDefine.THIS_CLASS, RepayDefine.INVEST_LIST_ACTION);
		return JSONObject.toJSONString(ret, true);
	}

	/**
	 * 创建用户借款项目的用户出借详情分页信息
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createUserInvestPage(HttpServletRequest request, JSONObject info, InvestListBean form) {

		int recordTotal = this.repayService.countUserInvestRecordTotal(form.getBorrowNid());
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			// 查询用户项目的出借情况
			List<WebUserInvestListCustomize> recordList = repayService.selectUserInvestList(form.getBorrowNid(), paginator.getOffset(), paginator.getLimit());
			info.put("paginator", paginator);
			info.put("userinvestlist", recordList);
		} else {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
			info.put("paginator", paginator);
			info.put("userinvestlist", "");
		}
	}
	
	/**
	 * 获取用户借款项目还款详情(需要计算还款总额----第一遍) 下拉箭头
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 * @throws NumberFormatException
	 */
	@RequestMapping(value = RepayDefine.REPAY_DETAIL_ACTION)
	public ModelAndView searchRepayProjectDetail(@ModelAttribute ProjectBean form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView(RepayDefine.REPAY_DETAIL_PAGE);
		WebViewUser user = WebUtils.getUser(request);
		if (user != null) {
			form.setUserId(user.getUserId().toString());
			form.setUsername(user.getUsername());
			form.setRoleId(user.getRoleId());
		}
		// 是否一次性还款
		String pAllRepay = request.getParameter("isAllRepay");
		boolean isAllRepay = false;
		if(pAllRepay != null && "1".equals(pAllRepay)) {
			isAllRepay = true;
		}
		// 查询用户的出借详情
		// 服务费授权和还款授权校验
		Users users = this.repayService.getUsers(user.getUserId());
        modelAndView.addObject("paymentAuthStatus", users.getPaymentAuthStatus());
        //还款授权
       HjhUserAuth hjhUserAuth=repayService.getHjhUserAuthByUserId(user.getUserId());
        if(hjhUserAuth==null){
            modelAndView.addObject("repayAuthStatus", "0");
        }else{
			modelAndView.addObject("repayAuthStatus", hjhUserAuth.getAutoRepayStatus());
        }
        // 是否开启服务费授权 0未开启  1已开启
        modelAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
		modelAndView.addObject("repayAuthOn","0");
        // modelAndView.addObject("repayAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_REPAYMENT_AUTH).getEnabledStatus());
        modelAndView.addObject("roleId", user.getRoleId());
		ProjectBean repayProject = repayService.searchRepayProjectDetail(form,isAllRepay);
		modelAndView.addObject("repayProject", repayProject);
		if("1".equals(repayProject.getOnlyAllRepay())) {
			pAllRepay ="1";
		}
		boolean isFreeze = !repayService.checkRepayInfo(form.getBorrowNid());
		modelAndView.addObject("isFreeze", isFreeze);
		modelAndView.addObject("isAllRepay", pAllRepay);
		return modelAndView;
	}

	/***
	 * 用户还款
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = RepayDefine.REPAY_ACTION)
	public ModelAndView repayUserBorrowProject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView(RepayDefine.REPAY_ERROR_PAGE);
		JSONObject info = new JSONObject();
		WebViewUser user = WebUtils.getUser(request);
		if (user == null) {
			modelAndView.addObject("message", "登录超时，请重新登录！");
			return modelAndView;
		}
		Integer userId = user.getUserId();
		String roleId = user.getRoleId();
		String userName = user.getUsername();
		String password = request.getParameter("password");
		String borrowNid = request.getParameter("borrowNid");
		// 对分期标的，是否一次全部还款
		String pAllRepay = request.getParameter("isAllRepay");
		boolean isAllRepay = false;
		if(pAllRepay != null && "1".equals(pAllRepay)) {
			isAllRepay = true;
		}
		
		// 获取用户在银行的客户号
		BankOpenAccount userBankOpenAccount = this.repayService.getBankOpenAccount(userId);
		if (userBankOpenAccount == null || StringUtils.isEmpty(userBankOpenAccount.getAccount())) {
			modelAndView.addObject("message", "用户在银行未开户");
			return modelAndView;
		}
		modelAndView.addObject("borrowNid", borrowNid);
		Borrow borrow = this.repayService.searchRepayProject(userId, userName, roleId, borrowNid);
		/** redis 锁 */
		boolean isRepay = RedisUtils.tranactionSet("repay_borrow_nid" + borrowNid, 60);
		if(!isRepay){
			modelAndView.addObject("message", "项目正在还款中...");
			return modelAndView;
		}
		// 校验用户/垫付机构的还款
		RepayBean repay = null;
		if (StringUtils.isNotEmpty(roleId) && "3".equals(roleId)) {// 垫付机构还款校验
			Integer repayUserId = borrow.getUserId();
			repay = this.validatorFieldCheckRepay_org(info, userId, password, borrow, repayUserId,0,isAllRepay,true);
		} else { // 借款人还款校验
			repay = this.validatorFieldCheckRepay(info, userId, password, borrow, isAllRepay);
		}
		
		if (!ValidatorCheckUtil.hasValidateError(info) && repay != null) {
			//防止汇计划还款时正在发生债转操作
			int errflag = repay.getFlag();
			if (1 == errflag) {
				modelAndView.addObject("message", repay.getMessage());
				return modelAndView;
			}
			
			String ip = GetCilentIP.getIpAddr(request);
			repay.setIp(ip);
			BigDecimal repayTotal = repay.getRepayAccountAll();
			String account = userBankOpenAccount.getAccount();
            // 借款人还款
			try {
				if(StringUtils.isNotEmpty(roleId) && "3".equals(roleId)){// 垫付机构还款
					String txDate = GetOrderIdUtils.getTxDate();// 交易日期
					String txTime = GetOrderIdUtils.getTxTime();// 交易时间
					String seqNo = GetOrderIdUtils.getSeqNo(6);// 交易流水号
					String orderId = txDate + txTime + seqNo;// 交易日期+交易时间+交易流水号
					//add by cwyang 2017-07-25 还款去重
					boolean result = repayService.checkRepayInfo(borrowNid);
					if (!result) {
						modelAndView.addObject("message", "项目正在还款中...");
						return modelAndView;
					}
					//插入垫付机构冻结信息日志表 add by wgx 2018-09-11
					repayService.insertRepayOrgFreezeLof(userId, orderId, account, borrow, repay, userName, isAllRepay);
					return getBankRefinanceFreezePage(userId, userName, ip, orderId, borrowNid, repayTotal, account);
				} else {
					String orderId = GetOrderIdUtils.getOrderId2(userId);
					//add by cwyang 2017-07-25 还款去重
					boolean result = repayService.checkRepayInfo(borrowNid);
					if (!result) {
						modelAndView.addObject("message", "项目正在还款中...");
						return modelAndView;
					}
					//插入冻结信息日志表 add by cwyang 2017-07-08
					repayService.insertRepayFreezeLof(userId, orderId, account, borrowNid, repayTotal, userName);
					// 调用江西银行还款申请冻结资金
					return getBalanceFreeze(user, borrow, repay, orderId, account, isAllRepay);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			modelAndView.addObject("message", "还款失败，请稍后再试！");
			return modelAndView;

		} else {
			modelAndView.addObject("message", info.getString(RepayDefine.REPAY_ERROR));
			return modelAndView;
		}
	}

	/**
	 * 还款申请冻结资金
	 * @param user
	 * @param borrow
	 * @param repay
	 * @param orderId
	 * @param account
	 * @param isAllRepay
	 * @return
	 */
	private ModelAndView getBalanceFreeze(WebViewUser user, Borrow borrow, RepayBean repay, String orderId, String account,boolean isAllRepay) {
		Integer userId = user.getUserId();
		String roleId = user.getRoleId();
		String userName = user.getUsername();
		String ip = repay.getIp();
		BigDecimal repayTotal = repay.getRepayAccountAll();
		String borrowNid = borrow.getBorrowNid();
		ModelAndView modelAndView = new ModelAndView(RepayDefine.REPAY_ERROR_PAGE);
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_FREEZE);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(account);// 电子账号
		bean.setOrderId(orderId); // 订单号
		bean.setTxAmount(String.valueOf(repayTotal));// 交易金额
		bean.setProductId(borrowNid);
		bean.setFrzType("0");
		bean.setLogOrderId(orderId);// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogUserName(userName);
		bean.setLogClient(0);
		bean.setLogIp(ip);
		bean.setProductId(borrowNid);
		modelAndView.addObject("borrowNid", borrowNid);
		try {
			BankCallBean callBackBean = BankCallUtils.callApiBg(bean);
			String respCode = callBackBean == null ? "" : callBackBean.getRetCode();
			// 申请冻结资金失败
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
				if (!"".equals(respCode)) {
					repayService.deleteFreezeTempLogs(orderId);
				}
				log.info("调用还款申请冻结资金接口失败:" + callBackBean.getRetMsg() + "订单号:" + callBackBean.getOrderId());
				modelAndView.addObject("message", "还款失败，请稍后再试！");
				return modelAndView;
			}
			// 还款后变更数据
			boolean flag = this.repayService.updateRepayMoney(repay, callBackBean, Integer.valueOf(roleId), userId, userName, isAllRepay);
			if (flag) {
				// 如果有正在出让的债权,先去把出让状态停止
				this.repayService.updateBorrowCreditStautus(borrow);
				modelAndView.addObject("borrowName", borrow.getName());
				modelAndView.setViewName(RepayDefine.REPAY_SUCCESS_PAGE);
				return modelAndView;
			} else {
				modelAndView.addObject("message", "还款失败，请稍后再试！");
				return modelAndView;
			}
		} catch (Exception e) {
			e.printStackTrace();
			modelAndView.addObject("message", "还款失败，请稍后再试！");
			return modelAndView;
		}
	}

	/**
	 * 校验用户还款信息(计算结果形成中间结果值)
	 * 
	 * @param info
	 * @param userId
	 * @param password
	 * @param borrow
	 * @throws ParseException
	 */
	private RepayBean validatorFieldCheckRepay(JSONObject info, int userId, String password, Borrow borrow,boolean isAllRepay) throws Exception {

		RepayBean repayByTerm = null;
		// 获取当前用户
		Users user = this.repayService.searchRepayUser(userId);
		// 检查用户是否存在
		if (user == null) {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR1);
		}
		
		// 服务费授权和还款授权校验
        boolean isPaymentAuth = this.authService.checkPaymentAuthStatus(userId);
        if (!isPaymentAuth) {
            ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR12, RepayDefine.REPAY_ERROR12);
        }
        // 张宁说的  不校验还款授权
       /* boolean isRepayAuth = this.authService.checkRepayAuthStatus(userId);
        if (!isRepayAuth) {
            ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR13, RepayDefine.REPAY_ERROR13);
        }*/
		String sort = user.getSalt();
		String mdPassword = MD5.toMD5Code(password + sort);
		// 检查用户输入的密码信息同当前的用户密码信息是否对应
		if (!mdPassword.equals(user.getPassword())) {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR2);
		}
		// 获取用户的账户余额信息
		Account account = this.repayService.searchRepayUserAccount(userId);
		// 查询用户的账户余额信息
		if (account == null) {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR3);
		}
		// 获取用户的余额
		BigDecimal balance = account.getBankBalance();
		// 获取当前的用户还款的项目
		borrow = this.repayService.searchRepayProject(userId, user.getUsername(), "2", borrow.getBorrowNid());
		// 判断用户当前还款的项目是否存在
		if (borrow == null) {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR4);
		}
		// 获取项目还款方式
		String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
		// 获取历史年回报率
		BigDecimal borrowApr = borrow.getBorrowApr();
		// 判断项目的还款方式是否为空
		if (StringUtils.isEmpty(borrowStyle)) {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR5);
		}
		//设置防债转并发锁
		boolean tranactionSetFlag = RedisUtils.tranactionSet(RedisConstants.HJH_DEBT_SWAPING + borrow.getBorrowNid(),300);
		if (!tranactionSetFlag) {//设置失败
			repayByTerm = new RepayBean();
			repayByTerm.setFlag(1);//校验失败
			repayByTerm.setMessage("系统繁忙,请5分钟后重试.......");
			return repayByTerm;
		}
		// 一次性还款
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
			repayByTerm = this.repayService.searchRepayTotalV2(userId, borrow);
			if (repayByTerm.getRepayAccountAll().compareTo(balance) == 0 || repayByTerm.getRepayAccountAll().compareTo(balance) == -1) {
				// ** 用户符合还款条件，可以还款 *//*
				// 查询用户在银行电子账户的余额
				BankOpenAccount accountChinapnr = this.repayService.getBankOpenAccount(userId);
				BigDecimal userBankBalance = this.repayService.getBankBalance(userId, accountChinapnr.getAccount());
				if (repayByTerm.getRepayAccountAll().compareTo(userBankBalance) == 0 || repayByTerm.getRepayAccountAll().compareTo(userBankBalance) == -1) {
					// ** 用户符合还款条件，可以还款 *//*
				} else {
					ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR10);
					repayByTerm = null;
				}
			} else {
				ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR8);
				repayByTerm = null;
			}
		} // 分期还款
		else {
			
			if(isAllRepay) {
				repayByTerm = repayAllPlan(info, userId, borrow, balance);
				
			}else {
				repayByTerm = repayCurPlan(info, userId, borrow, balance, borrowStyle, borrowApr);
			}
		}


		return repayByTerm;
	}

	//当前还款
	private RepayBean repayCurPlan(JSONObject info, int userId, Borrow borrow, BigDecimal balance, String borrowStyle, BigDecimal borrowApr) throws Exception {
		// 这里查询，算出逻辑
		RepayBean repayByTerm = this.repayService.searchRepayByTermTotalV2(userId, borrow, borrowApr, borrowStyle, borrow.getBorrowPeriod());
		
		if (repayByTerm.getRepayAccountAll().compareTo(balance) == 0 || repayByTerm.getRepayAccountAll().compareTo(balance) == -1) {
			BankOpenAccount accountChinapnr = this.repayService.getBankOpenAccount(userId);
			// 查询用户在银行电子账户的可用余额
			BigDecimal userBalance = this.repayService.getBankBalance(userId, accountChinapnr.getAccount());
			if (repayByTerm.getRepayAccountAll().compareTo(userBalance) == 0 || repayByTerm.getRepayAccountAll().compareTo(userBalance) == -1) {
				// ** 用户符合还款条件，可以还款 *//*
				return repayByTerm;
			} else {
				ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR10);
				return null;
			}
		} else {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR8);
			return null;
		}
	}

	// 一次性还款
	private RepayBean repayAllPlan(JSONObject info, int userId, Borrow borrow, BigDecimal balance) throws Exception {
		
		// 这里查询，算出逻辑
		RepayBean repayByTerm = this.repayService.searchRepayPlanTotal(userId, borrow);
		if (repayByTerm.getRepayAccountAll().compareTo(balance) == 0 || repayByTerm.getRepayAccountAll().compareTo(balance) == -1) {
			BankOpenAccount accountChinapnr = this.repayService.getBankOpenAccount(userId);
			// 查询用户在银行电子账户的可用余额
			BigDecimal userBalance = this.repayService.getBankBalance(userId, accountChinapnr.getAccount());
			if (repayByTerm.getRepayAccountAll().compareTo(userBalance) == 0 || repayByTerm.getRepayAccountAll().compareTo(userBalance) == -1) {
				// ** 用户符合还款条件，可以还款 *//*
				return repayByTerm;
			} else {
				ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR10);
				return null;
			}
		} else {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR8);
			return null;
		}
	}

	/**
	 * 校验垫付机构还款信息(计算结果形成中间结果值)
	 * 
	 * @param borrow
	 * @param info
	 * @param userId
	 *            垫付机构id
	 * @param password
	 * @param borrow
	 * @param repayUserId
	 *            借款人id
	 * @param flag
	 * @param isAllRepay
	 * @throws ParseException
	 */
	private RepayBean validatorFieldCheckRepay_org(JSONObject info, int userId, String password, Borrow borrow, Integer repayUserId, int flag, boolean isAllRepay,boolean checkPassword) throws Exception {
		RepayBean repayByTerm = null;
		// 获取当前垫付机构
		Users user = this.repayService.searchRepayUser(userId);
		// 检查垫付机构是否存在
		if (user == null) {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR1);
		}
		// 服务费授权和还款授权校验
        boolean isPaymentAuth = this.authService.checkPaymentAuthStatus(userId);
        if (!isPaymentAuth) {
            ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR12, RepayDefine.REPAY_ERROR12);
        }
        // 垫付机构不校验还款授权
       /* boolean isRepayAuth = this.autoService.checkRepayAuthStatus(userId);
        if (!isRepayAuth) {
            ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR13, RepayDefine.REPAY_ERROR13);
        }*/
       if(checkPassword) {
		   String sort = user.getSalt();
		   String mdPassword = MD5.toMD5Code(password + sort);
		   // 检查垫付机构输入的密码信息同当前的用户密码信息是否对应
		   if (!mdPassword.equals(user.getPassword())) {
			   ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR2);
		   }
	   }
		// 获取垫付机构的账户余额信息
		Account account = this.repayService.searchRepayUserAccount(userId);
		// 查询垫付机构的账户余额信息
		if (account == null) {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR3);
		}
		// 获取用户在平台的账户余额
		BigDecimal balance = account.getBankBalance();
		// 获取当前垫付机构要还款的项目
		borrow = this.repayService.searchRepayProject(repayUserId, null, null, borrow.getBorrowNid());
		// 判断用户当前还款的项目是否存在
		if (borrow == null) {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR4);
		}
		// 获取项目还款方式
		String borrowStyle = StringUtils.isNotEmpty(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
		// 获取历史年回报率
		BigDecimal borrowApr = borrow.getBorrowApr();
		// 判断项目的还款方式是否为空
		if (StringUtils.isEmpty(borrowStyle)) {
			ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR5);
		}
		boolean tranactionSetFlag = RedisUtils.tranactionSet(RedisConstants.HJH_DEBT_SWAPING + borrow.getBorrowNid(),300);
		if (!tranactionSetFlag) {//设置失败
			repayByTerm = new RepayBean();
			repayByTerm.setFlag(1);//校验失败
			repayByTerm.setMessage("系统繁忙,请5分钟后重试.......");
			return repayByTerm;
		}
		// 一次性还款
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
			repayByTerm = this.repayService.searchRepayTotalV2(repayUserId, borrow);
			if (repayByTerm.getRepayAccountAll().compareTo(balance) == 0 || repayByTerm.getRepayAccountAll().compareTo(balance) == -1) {
				// ** 垫付机构符合还款条件，可以还款 *//*
				// 查询用户在银行的电子账户
				BankOpenAccount bankOpenAccount = this.repayService.getBankOpenAccount(userId);
				// 获取用户在银行的电子账户余额
				if (flag == 1) {//垫付机构批量还款
					repayByTerm.setRepayUserId(userId);// 垫付机构id
				}else{
					BigDecimal userBankBalance = this.repayService.getBankBalance(userId, bankOpenAccount.getAccount());
					if (repayByTerm.getRepayAccountAll().compareTo(userBankBalance) == 0 || repayByTerm.getRepayAccountAll().compareTo(userBankBalance) == -1) {
						// ** 垫付机构符合还款条件，可以还款 *//*
						repayByTerm.setRepayUserId(userId);// 垫付机构id
					} else {
						ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR10);
						repayByTerm = null;
					}
				}

			} else {
				ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR8);
				repayByTerm = null;
			}
		} // 分期还款
		else {


			if(isAllRepay) {
				 repayByTerm = this.repayService.searchRepayPlanTotal(repayUserId, borrow);
			}else {
				 repayByTerm = this.repayService.searchRepayByTermTotalV2(repayUserId, borrow, borrowApr, borrowStyle, borrow.getBorrowPeriod());
			}
			repayByTerm.setRepayUserId(userId);// 垫付机构id

			if (flag ==1) {
				//垫付机构批量还款 ，不验证总额
				;
			}else {

				if (repayByTerm.getRepayAccountAll().compareTo(balance) == 0 || repayByTerm.getRepayAccountAll().compareTo(balance) == -1) {
					// ** 垫付机构符合还款条件，可以还款 *//*
					// 查询用户在银行的电子账户
					BankOpenAccount bankOpenAccount = this.repayService.getBankOpenAccount(userId);
					// 获取用户在银行的电子账户余额
					BigDecimal userBankBalance = this.repayService.getBankBalance(userId, bankOpenAccount.getAccount());
					if (repayByTerm.getRepayAccountAll().compareTo(userBankBalance) == 0 || repayByTerm.getRepayAccountAll().compareTo(userBankBalance) == -1) {
						// ** 用户符合还款条件，可以还款 *//*
						;
					} else {
						ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR10);
						repayByTerm = null;
					}
				} else {
					ValidatorCheckUtil.validateSpecialError(info, RepayDefine.REPAY_ERROR, RepayDefine.REPAY_ERROR8);
					repayByTerm = null;
				}


			}


		}
		// 如果有正在出让的债权,先去把出让状态停止
		this.repayService.updateBorrowCreditStautus(borrow);
		return repayByTerm;
	}

	/**
	 * 
	 * 下载借款用户的协议
	 * 
	 * @author renxingchen
	 */
	@RequestMapping(value = RepayDefine.DOWNLOAD_BORROWER_PDF_ACTION)
	public void downloadBorrowerPdf(HttpServletRequest request, HttpServletResponse response, String borrowNid, String tType) {
		List<File> files = new ArrayList<File>();
		
		Integer currentUserId = WebUtils.getUserId(request);
		
		//CreateAgreementController createAgreementController = new CreateAgreementController();
		// 查询用户项目的出借情况
		List<WebUserInvestListCustomize> investlist;
		if ("1".equals(tType)) {
			// 原汇添金(已经弃用)
			investlist = repayService.selectUserDebtInvestList(borrowNid, null, -1, -1);
		} else {
			investlist = repayService.selectUserInvestList(borrowNid, -1, -1);
		}
		UserInvestListBean form;
		String flag = "1";
		for (WebUserInvestListCustomize tmp : investlist) {
			form = new UserInvestListBean();
			form.setBorrowNid(borrowNid);
			form.setNid(tmp.getNid());//huiyingdai_borrow_tender--nid(取自银行)字段
			form.setFlag(flag);
			File file;
			if ("1".equals(tType)) {
				//原汇添金
				file = createDebtAgreementPDFFile(request, response, form, tmp.getUserId(), tmp.getOrderId());
			} else {
				//居间服务于借款协议时展示标的维度的借款方与出借方的关系的，出借方来自于 huiyingdai_borrow_tender
				//原居间协议(注掉) file = createAgreementPDFFile(request, response, form, tmp.getUserId());
				//(1)调用新作的居间借款协议
				//file = createAgreementController.createAgreementPDFFile(request, response, form);
			    file = createAgreementController.createAgreementPDFFileRepay(request, response, form);
			}
			if (file != null) {
				files.add(file);
			}
		}
		ZIPGenerator.generateZip(response, files, borrowNid);
		return;
		
		/*************************************借款人借款列表先不下载债转**********************************/
		/*// 散标进计划--》建标时打上计划的标签，so散标债转只能转成散标，计划中的标的债转也只能转成计划中的标的
		// 一个标的一旦开始被出借要么是散标中中的标的，要么是计划中用的标的
		
		// (2.1)散标的债转协议(原)
		Borrow borrow = this.repayService.getBorrowByNid(borrowNid);
		if( borrow != null ){
			if(StringUtils.isNotEmpty(borrow.getPlanNid())){
				//计划中的标的
				List<HjhDebtCreditTender> hjhCreditTenderList = this.repayService.selectHjhCreditTenderList(borrowNid);//hyjf_hjh_debt_credit_tender
				for (HjhDebtCreditTender hjhCreditTender : hjhCreditTenderList) {
					//调用下载计划债转协议的方法
					CreditAssignedBean tenderCreditAssignedBean  = new CreditAssignedBean();
					tenderCreditAssignedBean.setBidNid(hjhCreditTender.getBorrowNid());// 标号
					tenderCreditAssignedBean.setCreditNid(hjhCreditTender.getCreditNid());// 债转编号
					tenderCreditAssignedBean.setCreditTenderNid(hjhCreditTender.getInvestOrderId());//原始出借订单号
					tenderCreditAssignedBean.setAssignNid(hjhCreditTender.getAssignOrderId());//债转后的新的"出借"订单号
					if(currentUserId != null){
						tenderCreditAssignedBean.setCurrentUserId(currentUserId);
					}
					// 模板参数对象(查新表)
					Map<String, Object> creditContract = tenderCreditService.selectHJHUserCreditContract(tenderCreditAssignedBean);
					try {
						File file = PdfGenerator.generatePdfFile(request, response, ((HjhDebtCreditTender) creditContract.get("creditTender")).getAssignOrderId() + ".pdf", CustomConstants.HJH_CREDIT_CONTRACT, creditContract);
						if(file!=null){
							files.add(file);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			} else {
				//散标中的标的
				// 根据标号判断标的是否有承接
				List<CreditTender> creditTenderList = this.repayService.selectCreditTenderList(borrowNid);//huiyingdai_credit_tender
				// 债转协议
				if(creditTenderList!= null && creditTenderList.size()>0){
					for (CreditTender creditTender : creditTenderList) {
						Map<String,String> param = new HashMap<String,String>();
						// 标号
						param.put("bidNid", creditTender.getBidNid());
						// 债转编号
						param.put("creditNid", creditTender.getCreditNid());
						// 债转出借订单号
						param.put("creditTenderNid", creditTender.getCreditTenderNid());
						// 承接订单号
						param.put("assignNid", creditTender.getAssignNid());
						// 当前登陆者
						param.put("currentUserId", currentUserId.toString());
						
						// 模板参数对象
						Map<String, Object> creditContract = this.repayService.selectUserCreditContract(param);
						try {
							File file = PdfGenerator.generatePdfFile(request, response, String.valueOf(creditTender.getAssignNid()) + ".pdf", CustomConstants.CREDIT_CONTRACT, creditContract);
							if(file!=null){
								files.add(file);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}	
			}
		}
		ZIPGenerator.generateZip(response, files, borrowNid);*/
	}

	/**
     * 
     * 下载借款用户的协议-计划居间和债转
     * 
     * @author zhadaojian
     */
    @RequestMapping(value = RepayDefine.DOWNLOAD_INTERMEDIARY_PDF_ACTION)
    public void downloadIntermediaryPdf(HttpServletRequest request, HttpServletResponse response, String flag,String borrowNid, String nid, String assignNid) {
        List<File> files = new ArrayList<File>();
        Integer currentUserId = WebUtils.getUserId(request);
        // 散标进计划--》建标时打上计划的标签，so散标债转只能转成散标，计划中的标的债转也只能转成计划中的标的
           // 一个标的一旦开始被出借要么是散标中中的标的，要么是计划中用的标的
           
           // (2.1)债转协议
           Borrow borrow = this.repayService.getBorrowByNid(borrowNid);
           if( borrow != null ){
               if(StringUtils.isNotEmpty(borrow.getPlanNid())){
                   TenderAgreement tenderAgreement = new TenderAgreement();
                   List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(assignNid);//居间
                   List<TenderAgreement> tenderAgreementsAss= fddGenerateContractService.selectByExample(nid);//债转协议
                    if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
                       //下载法大大协议--债转
                       /*if(tenderAgreementsAss!=null && tenderAgreementsAss.size()>0){
                           tenderAgreement = tenderAgreementsAss.get(0);
                           try {
                               if(StringUtils.isNotBlank(tenderAgreement.getDownloadUrl())){
                                   File filePdf= FileUtil.getFile(request,response,tenderAgreement.getDownloadUrl(),assignNid+".pdf");//债转协议
                                   if(filePdf!=null){
                                       files.add(filePdf);
                                   }
                               }
                              
                            } catch (IOException e) {
                                LogUtil.infoLog(this.getClass().getName(), "downloadIntermediaryPdf", "下载失败，请稍后重试。。。。");
                                return;
                            }
                       }
                       //下载法大大协议--居间
                       if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
                           tenderAgreement = tenderAgreementsNid.get(0);
                           if(tenderAgreement!=null){
                            // 查询借款人用户名
                               BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
                               // 借款编码
                               borrowCommonCustomize.setBorrowNidSrch(borrowNid);
                               files = createAgreementController.createFaddPDFImgFile(files,tenderAgreement);//下载脱敏
                           }
                       }*/

                      //计划中的标的---法大大
                        while (true) {
                            List<HjhDebtCreditTender> hjhCreditTenderList = this.repayService.selectHjhCreditTenderListByassignOrderId(nid);//hyjf_hjh_debt_credit_tender
                            if(hjhCreditTenderList!=null && hjhCreditTenderList.size()>0){
                                HjhDebtCreditTender hjhCreditTender = hjhCreditTenderList.get(0);
                                    logger.info("调用下载计划债转协议的方法 ---------------------:"+assignNid);
                                    tenderAgreementsAss= fddGenerateContractService.selectByExample(hjhCreditTender.getAssignOrderId());//债转协议
                                    //下载法大大协议--债转
                                    if(tenderAgreementsAss!=null && tenderAgreementsAss.size()>0){
                                        tenderAgreement = tenderAgreementsAss.get(0);
                                        files = createAgreementController.createFaddPDFImgFile(files,tenderAgreement);//下载脱敏
                                        /*try {
                                            if(StringUtils.isNotBlank(tenderAgreement.getDownloadUrl())){
                                                File filePdf= FileUtil.getFile(request,response,tenderAgreement.getDownloadUrl(),assignNid+".pdf");//债转协议
                                                if(filePdf!=null){
                                                    files.add(filePdf);
                                                }
                                            }

                                         } catch (IOException e) {
                                             LogUtil.infoLog(this.getClass().getName(), "downloadIntermediaryPdf", "下载失败，请稍后重试。。。。");
                                             return;
                                         }*/
                                    }
                                    nid = hjhCreditTender.getSellOrderId();
                                    if(nid.equals(hjhCreditTender.getInvestOrderId())){
                                      //下载法大大协议--居间
                                        tenderAgreementsNid= fddGenerateContractService.selectByExample(nid);//居间
                                        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
                                            tenderAgreement = tenderAgreementsNid.get(0);
                                            if(tenderAgreement!=null){
                                             // 查询借款人用户名
                                                BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
                                                // 借款编码
                                                borrowCommonCustomize.setBorrowNidSrch(borrowNid);
                                                files = createAgreementController.createFaddPDFImgFile(files,tenderAgreement);//下载脱敏
                                            }
                                        }
                                        break;
                                    }
                            }else {
                             break;
                         }
                      }
                   }else{
                   
                       //计划中的标的
                       while (true) {
                           List<HjhDebtCreditTender> hjhCreditTenderList = this.repayService.selectHjhCreditTenderListByassignOrderId(assignNid);//hyjf_hjh_debt_credit_tender
                           if(hjhCreditTenderList!=null && hjhCreditTenderList.size()>0){
                               HjhDebtCreditTender hjhCreditTender = hjhCreditTenderList.get(0);
                                   System.out.println("调用下载计划债转协议的方法 ---------------------:"+assignNid);
                                   //调用下载计划债转协议的方法 
                                   CreditAssignedBean tenderCreditAssignedBean  = new CreditAssignedBean();
                                   Map<String, Object> creditContract = null;
                                   tenderCreditAssignedBean.setBidNid(hjhCreditTender.getBorrowNid());// 标号
                                   tenderCreditAssignedBean.setCreditNid(hjhCreditTender.getCreditNid());// 债转编号
                                   tenderCreditAssignedBean.setCreditTenderNid(hjhCreditTender.getInvestOrderId());//原始出借订单号
                                   tenderCreditAssignedBean.setAssignNid(hjhCreditTender.getAssignOrderId());//债转后的新的"出借"订单号
                                   if(currentUserId != null){
                                       tenderCreditAssignedBean.setCurrentUserId(currentUserId);
                                   }
                                   // 模板参数对象(查新表)
                                   creditContract = tenderCreditService.selectHJHUserCreditContract(tenderCreditAssignedBean);
                                   if(creditContract!=null){
                                       try {
                                           File filetender = PdfGenerator.generatePdfFile(request, response, ((HjhDebtCreditTender) creditContract.get("creditTender")).getAssignOrderId() + ".pdf", CustomConstants.HJH_CREDIT_CONTRACT, creditContract);
                                           if(filetender!=null){
                                               files.add(filetender);
                                           }
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }  
                                       
                                   }
                                   nid = hjhCreditTender.getSellOrderId();
                                   if(nid.equals(hjhCreditTender.getInvestOrderId())){
                                       //原始标的居间协议
                                       UserInvestListBean form;
                                       form = new UserInvestListBean();
                                       form.setBorrowNid(borrowNid);
                                       form.setNid(nid);//huiyingdai_borrow_tender--nid(取自银行)字段
                                       form.setFlag("1");
                                       File file;
                                       //居间服务于借款协议时展示标的维度的借款方与出借方的关系的，出借方来自于 huiyingdai_borrow_tender
                                       //原居间协议(注掉) file = createAgreementPDFFile(request, response, form, tmp.getUserId());
                                       //(1)调用新作的居间借款协议
                                       file = createAgreementController.createAgreementPDFFile(request, response, form);
                                       if (file != null) {
                                           files.add(file);
                                       }
                                       break;
                                   }
                           }else {
                            break;
                        }
                       }
                   }
               }
           }
           if(files!=null && files.size()>0){
               ZIPGenerator.generateZip(response, files, borrowNid);
           }
    }
	/**
	 * 
	 * 生成汇添金专属表的PDF文件
	 * 
	 * @author renxingchen
	 * @param request
	 * @param response
	 * @param form
	 * @param userId
	 * @return
	 */
	private File createDebtAgreementPDFFile(HttpServletRequest request, HttpServletResponse response, UserInvestListBean form, String userId, String orderId) {
		LogUtil.startLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF 生成PDF文件");
		if (form.getBorrowNid() == null || "".equals(form.getBorrowNid().trim())) {
			return null;
		}
		// 查询借款人用户名
		DebtBorrowCommonCustomize borrowCommonCustomize = new DebtBorrowCommonCustomize();
		// 借款编码
		borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
		List<DebtBorrowCustomize> recordList = mytenderService.selectDebtBorrowList(borrowCommonCustomize);
		DebtBorrowCustomize borrowCustomize;
		if (recordList.size() != 1) {
			return null;
		} else {
			borrowCustomize = recordList.get(0);
		}
		Map<String, Object> contents = new HashMap<String, Object>();
		contents.put("record", recordList.get(0));
		contents.put("borrowNid", form.getBorrowNid());
		contents.put("nid", form.getNid());
		// 借款人用户名
		int userIds = recordList.get(0).getUserId();
        UsersInfo userInfo = planInfoService.getUsersInfoByUserId(userIds);
         contents.put("idCard", userInfo.getIdcard());
         contents.put("borrowUsername", recordList.get(0).getUsername());
		if (StringUtils.isNotBlank(borrowCustomize.getRecoverLastTime())) {
			// 最后一笔的放款完成时间 (协议签订日期)
			contents.put("recoverTime", borrowCustomize.getRecoverLastTime());
		} else {
			// 设置为满标时间
			contents.put("recoverTime", borrowCustomize.getReverifyTime());
		}
		// 用户ID
		form.setUserId(userId);
		// 用户出借列表
		List<WebUserInvestListCustomize> tzList = repayService.selectUserDebtInvestList(form.getBorrowNid(), orderId, -1, -1);
		if (tzList != null && tzList.size() > 0) {
			contents.put("userInvest", tzList.get(0));
		}

		// 如果是分期还款，查询分期信息
		String borrowStyle = borrowCustomize.getBorrowStyle();// 还款模式
		if (borrowStyle != null) {
		    //计算历史回报
            BigDecimal earnings = new BigDecimal("0");
            // 收益率
            
            String borrowAprString = StringUtils.isEmpty(recordList.get(0).getBorrowApr())?"0.00":recordList.get(0).getBorrowApr().replace("%", "");
            BigDecimal borrowApr = new BigDecimal(borrowAprString);
            //出借金额
            String accountString = StringUtils.isEmpty(recordList.get(0).getAccount())?"0.00":recordList.get(0).getAccount().replace(",", "");
            BigDecimal account = new BigDecimal(accountString);
           // 周期
            String borrowPeriodString = StringUtils.isEmpty(recordList.get(0).getBorrowPeriod())?"0":recordList.get(0).getBorrowPeriod();
            String regEx="[^0-9]";   
            Pattern p = Pattern.compile(regEx);   
            Matcher m = p.matcher(borrowPeriodString); 
            borrowPeriodString = m.replaceAll("").trim();
            Integer borrowPeriod = Integer.valueOf(borrowPeriodString);
            if (StringUtils.equals("endday", borrowStyle)){
                // 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷365*锁定期；
                earnings = DuePrincipalAndInterestUtils.getDayInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
            } else {
                // 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
                earnings = DuePrincipalAndInterestUtils.getMonthInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);

            }
            contents.put("earnings", earnings);
			if ("month".equals(borrowStyle) || "principal".equals(borrowStyle) || "endmonth".equals(borrowStyle)) {
				ProjectRepayListBean bean = new ProjectRepayListBean();
				bean.setUserId(userId);
				bean.setBorrowNid(form.getBorrowNid());
				bean.setNid(form.getNid());
				int recordTotal = this.planInfoService.countProjectRepayPlanRecordTotal(bean);
				if (recordTotal > 0) {
					Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
					List<WebProjectRepayListCustomize> fqList = this.planInfoService.selectProjectRepayPlanList(bean, paginator.getOffset(), paginator.getLimit());
					contents.put("paginator", paginator);
					contents.put("repayList", fqList);
				} else {
					Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
					contents.put("paginator", paginator);
					contents.put("repayList", "");
				}
			}
		}

		File file = null;
		try {
		    String borrowNid = form.getBorrowNid();
            Borrow borrow = mytenderService.getBorrowByNid(borrowNid);
            if(borrow.getPlanNid() != null){//该标的与计划关联，应发计划的居间
               log.info("计划的居间协议");
               file = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + orderId + ".pdf", CustomConstants.TENDER_CONTRACT, contents);
            } else {
                file = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + orderId + ".pdf", CustomConstants.TENDER_CONTRACT, contents);
                log.info("散标的居间协议");
               // pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.TENDER_CONTRACT, contents);
               // file = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + orderId + ".pdf", CustomConstants.NEW_TENDER_CONTRACT_FTL_NAME, contents);
               
            }
			//file = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + orderId + ".pdf", CustomConstants.TENDER_CONTRACT, contents);
		} catch (Exception e) {
			LogUtil.errorLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF  生成PDF文件", e);
			e.printStackTrace();
		}

		LogUtil.endLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF 生成PDF文件");
		return file;
	}

	/**
	 * 生成PDF文件
	 * 
	 * @param request
	 * @param response
	 * @param form
	 */
	/*private File createAgreementPDFFile(HttpServletRequest request, HttpServletResponse response, UserInvestListBean form, String userId) {
		LogUtil.startLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF 生成PDF文件");
		if (form.getBorrowNid() == null || "".equals(form.getBorrowNid().trim())) {
			return null;
		}
		// 查询借款人用户名
		BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
		// 借款编码
		borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
		List<BorrowCustomize> recordList = mytenderService.selectBorrowList(borrowCommonCustomize);
		if (recordList.size() != 1) {
			return null;
		}
		Map<String, Object> contents = new HashMap<String, Object>();
		contents.put("borrowNid", form.getBorrowNid());
		// 借款人用户名
		UsersInfo userInfo=planInfoService.getUsersInfoByUserId(recordList.get(0).getUserId());
		contents.put("borrowUsername", userInfo.getTruename());
		contents.put("idCard", userInfo.getIdcard());
		if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
			// 最后一笔的放款完成时间 (协议签订日期)
			contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
		} else {
			// 设置为满标时间
			contents.put("recoverTime", recordList.get(0).getReverifyTime());
		}
		// 用户ID
		form.setUserId(userId);
		// 用户出借列表
		List<WebUserInvestListCustomize> tzList = mytenderService.selectUserInvestList(form, 0, 100);
		if (tzList != null && tzList.size() > 0) {
			contents.put("userInvest", tzList.get(0));
		}

		// 如果是分期还款，查询分期信息
		String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
		if (borrowStyle != null) {
			if ("month".equals(borrowStyle) || "principal".equals(borrowStyle) || "endmonth".equals(borrowStyle)) {
				ProjectRepayListBean bean = new ProjectRepayListBean();
				bean.setUserId(userId);
				bean.setBorrowNid(form.getBorrowNid());
				bean.setNid(form.getNid());
				int recordTotal = this.mytenderService.countProjectRepayPlanRecordTotal(bean);
				if (recordTotal > 0) {
					Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
					List<WebProjectRepayListCustomize> fqList = mytenderService.selectProjectRepayPlanList(bean, paginator.getOffset(), paginator.getLimit());
					contents.put("paginator", paginator);
					contents.put("repayList", fqList);
				} else {
					Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
					contents.put("paginator", paginator);
					contents.put("repayList", "");
				}
			}
		}

		File file = null;
		try {
		    String borrowNid = form.getBorrowNid();
            Borrow borrow = mytenderService.getBorrowByNid(borrowNid);
            if(borrow.getPlanNid() != null){
               //该标的与计划关联，应发计划的居间(汇计划二期后，只用一套居间借款协议)
               log.info("计划的居间协议"+CustomConstants.TENDER_CONTRACT);
               file = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf", CustomConstants.TENDER_CONTRACT, contents);
            } else {
                log.info("散标的居间协议"+CustomConstants.NEW_TENDER_CONTRACT_FTL_NAME);
               // pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.TENDER_CONTRACT, contents);
                file = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf", CustomConstants.NEW_TENDER_CONTRACT_FTL_NAME, contents);
               
            }
			//file = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf", CustomConstants.TENDER_CONTRACT, contents);
		} catch (Exception e) {
			LogUtil.errorLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF  生成PDF文件", e);
			e.printStackTrace();
		}
		LogUtil.endLog(MyTenderDefine.THIS_CLASS, "createAgreementPDF 生成PDF文件");
		return file;
	}*/

	/**
	 * 收到报文后对合法性检查后的异步回调
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(RepayDefine.REPAY_VERIFY_RETURN_ACTION)
	public String repayVerifyReturnAction(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) throws Exception {

		log.info("批次还款请求,收到报文后对合法性检查后的异步回调开始");
		BankCallResult result = new BankCallResult();
		bean.convert();
		String respCode = StringUtils.isBlank(bean.getRetCode()) ? null : bean.getRetCode();// 返回码
		if (StringUtils.isBlank(respCode)) {
			log.info("放款校验回调，返回码为空！");
			return JSONObject.toJSONString(result, true);
		}
		String txDate = bean.getTxDate();
		String txTime = bean.getTxTime();
		String seqNo = bean.getSeqNo();
		String bankSeqNo = txDate + txTime + seqNo;
		BorrowApicron apicron = this.repayService.selectBorrowApicron(bankSeqNo);
		if (Validator.isNull(apicron)) {
			log.info("还款校验回调，未查询到放款请求记录！银行唯一订单号：" + bankSeqNo);
			// 更新相应的放款请求校验失败
			return JSONObject.toJSONString(result, true);
		}
		// 当前批次放款状态
		int repayStatus = apicron.getStatus();
		if (repayStatus == CustomConstants.BANK_BATCH_STATUS_SENDED) {
			String borrowNid = apicron.getBorrowNid();
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
				log.info("批次还款请求,收到报文后,数据合法性异常");
				String retMsg = bean.getRetMsg();
				log.info("放款校验回调失败！银行返回信息：" + retMsg);
				apicron.setData(retMsg);
				apicron.setFailTimes(apicron.getFailTimes() + 1);
				// 更新任务API状态为放款校验失败
				boolean apicronResultFlag = repayService.updateBorrowApicron(apicron, BatchLoanDefine.STATUS_VERIFY_FAIL);
				if (!apicronResultFlag) {
					throw new Exception("更新放款任务为放款请求失败失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				// 更新相应的放款请求校验失败
				return JSONObject.toJSONString(result, true);
			}
			// 更新相应的放款请求校验成功
			boolean apicronResultFlag = repayService.updateBorrowApicron(apicron, BatchLoanDefine.STATUS_VERIFY_SUCCESS);
			if (!apicronResultFlag) {
				throw new Exception("更新放款任务为放款请求成功失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
			}
		}
		result.setStatus(true);
		log.info("批次还款请求,收到报文后对合法性检查后的异步回调结束");
		return JSONObject.toJSONString(result, true);

	}

	/**
	 * 业务处理结果的异步回调
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(RepayDefine.REPAY_RESULT_RETURN_ACTION)
	public String repayResultReturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) throws Exception {

		log.info("批次还款请求,业务处理结果的异步回调开始");
		BankCallResult result = new BankCallResult();
		bean.convert();
		String respCode = StringUtils.isBlank(bean.getRetCode()) ? null : bean.getRetCode();// 返回码
		if (StringUtils.isBlank(respCode)) {
			log.info("放款结果回调，返回码为空！");
			return JSONObject.toJSONString(result, true);
		}
		String txDate = bean.getTxDate();
		String txTime = bean.getTxTime();
		String seqNo = bean.getSeqNo();
		String bankSeqNo = txDate + txTime + seqNo;
		BorrowApicron apicron = this.repayService.selectBorrowApicron(bankSeqNo);
		if (Validator.isNull(apicron)) {
			log.info("放款结果回调，未查询到放款请求记录！银行唯一订单号：" + bankSeqNo);
			// 更新相应的放款请求校验失败
			return JSONObject.toJSONString(result, true);
		}
		// 当前批次放款状态
		int repayStatus = apicron.getStatus();
		String borrowNid = apicron.getBorrowNid();// 項目编号
		int borrowUserId = apicron.getUserId();// 放款用户
		if (repayStatus == CustomConstants.BANK_BATCH_STATUS_VERIFY_SUCCESS) {
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
				String retMsg = bean.getRetMsg();
				log.info("放款结果回调失败！银行返回信息：" + retMsg);
				apicron.setData(retMsg);
				apicron.setFailTimes(apicron.getFailTimes() + 1);
				// 更新任务API状态为放款校验失败
				boolean apicronResultFlag = repayService.updateBorrowApicron(apicron, BatchLoanDefine.STATUS_LOAN_FAIL);
				if (!apicronResultFlag) {
					throw new Exception("更新放款任务为放款结果失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
				// 更新相应的放款请求校验失败
				return JSONObject.toJSONString(result, true);
			} else {
				// 查询批次放款状态
				BankCallBean batchResult = this.repayService.batchQuery(apicron);
				if (Validator.isNotNull(batchResult)) {
					// 批次放款返回码
					String retCode = StringUtils.isNotBlank(batchResult.getRetCode()) ? batchResult.getRetCode() : "";
					if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
						// 批次放款状态
						String batchState = batchResult.getBatchState();
						if (StringUtils.isNotBlank(batchState)) {
							// 如果是批次处理失败
							if (batchState.equals(BankCallConstant.BATCHSTATE_TYPE_FAIL)) {
								String failMsg = batchResult.getFailMsg();// 失败原因
								if (StringUtils.isNotBlank(failMsg)) {
									apicron.setData(failMsg);
									apicron.setFailTimes(apicron.getFailTimes() + 1);
									// 更新任务API状态
									boolean apicronResultFlag = this.repayService.updateBorrowApicron(apicron, CustomConstants.BANK_BATCH_STATUS_FAIL);
									if (apicronResultFlag) {
										result.setStatus(true);
										return JSONObject.toJSONString(result, true);
									} else {
										throw new Exception("更新状态为（放款请求失败）失败。[用户ID：" + borrowUserId + "]," + "[借款编号：" + borrowNid + "]");
									}
								} 
//								else {
//									// 查询批次交易明细，进行后续操作
//									boolean batchDetailFlag = this.repayService.batchDetailsQuery(apicron);
//									// 进行后续失败的放款的放款请求
//									if (batchDetailFlag) {
//										result.setStatus(true);
//										return JSONObject.toJSONString(result, true);
//									} else {
//										throw new Exception("放款失败后，查询放款明细失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
//									}
//								}
							}
//							// 如果是批次处理成功
//							else if (batchState.equals(BankCallConstant.BATCHSTATE_TYPE_SUCCESS)) {
//								// 查询批次交易明细，进行后续操作
//								boolean batchDetailFlag = this.repayService.batchDetailsQuery(apicron);
//								if (batchDetailFlag) {
//									result.setStatus(true);
//									return JSONObject.toJSONString(result, true);
//								} else {
//									throw new Exception("放款成功后，查询放款明细失败。[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
//								}
//							} else {
//								result.setStatus(true);
//								return JSONObject.toJSONString(result, true);
//							}
						} else {
							throw new Exception("放款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
						}
					} else {
						String retMsg = batchResult.getRetMsg();
						throw new Exception("放款状态查询失败！银行返回信息：" + retMsg + ",[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
					}
				} else {
					throw new Exception("放款状态查询失败！[银行唯一订单号：" + bankSeqNo + "]," + "[借款编号：" + borrowNid + "]");
				}
			}
		} else {
			result.setStatus(true);
			return JSONObject.toJSONString(result, true);
		}
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);
	}
	
	
	/**
	 * 
	 * 担保机构批量还款页面
	 * 
	 * @author cwyang
	 * @param request
	 * @return
	 */
	@RequestMapping(RepayDefine.ORG_USER_BATCH_REPAY_PAGE_ACTION)
	public ModelAndView orgUserBatchRepayPage(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(RepayDefine.USER_ORG_BATCH_REPAY_PAGE);
		// 查询该用户的未还款金额
		String userId = request.getParameter("userId");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		if (StringUtils.isNotBlank(userId)) {
			modelAndView.addObject("orgUserId", userId);
			modelAndView.addObject("startDate", startDate);
			modelAndView.addObject("endDate", endDate);
			if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
				int endTime = Integer.parseInt(GetDate.get10Time(endDate));
				int startTime = Integer.parseInt(GetDate.get10Time(startDate));
				if (endTime < startTime) {
					modelAndView.addObject("errCode", "900");//结束时间大于开始时间
					return modelAndView;
				}
				int lastTime = endTime - startTime;
				if (lastTime > 60*60*24*7) {
					modelAndView.addObject("errCode", "901");//时间差不能大于七天
					return modelAndView;
				}
			}else{
				startDate  = GetDate.formatDate(new java.util.Date());
				endDate = GetDate.formatDate(new java.util.Date());
				modelAndView.addObject("orgUserId", userId);
				modelAndView.addObject("startDate", startDate);
				modelAndView.addObject("endDate", endDate);
			}

			//查询垫付机构代垫付列表
			RepayProjectListBean form = new RepayProjectListBean();
			form.setUserId(userId);
			form.setRoleId("3");
			form.setStartDate(startDate);
			form.setEndDate(endDate);
			form.setStatus("0");
			form.setRepayStatus("0");
			List<WebUserRepayProjectListCustomize> oreUserRepayList = repayService.searchUserRepayList(form , 0, 0);
			if (oreUserRepayList == null) {
				oreUserRepayList = new ArrayList<WebUserRepayProjectListCustomize>();
			}
			getOrgRepayInfo(oreUserRepayList,modelAndView,userId);
			
			// 缴费授权
	        Users users = this.repayService.getUsers(Integer.parseInt(userId));
	        modelAndView.addObject("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
            modelAndView.addObject("paymentAuthStatus",users.getPaymentAuthStatus());
		}
		return modelAndView;
	}

	
	/**
	 * 获取垫付机构还款信息
	 * @param list
	 * @param modelAndView
	 * @param userId 
	 */
	private void getOrgRepayInfo(List<WebUserRepayProjectListCustomize> list, ModelAndView modelAndView, String userId) {
		ProjectBean form = new ProjectBean();
		form.setUserId(userId);
		form.setRoleId("3");
		//垫付机构总的还款信息
		ProjectBean repayProjectInfo = new ProjectBean();
		BigDecimal repayAccount = new BigDecimal(0);
		BigDecimal repayCapital = new BigDecimal(0);
		BigDecimal repayInterest = new BigDecimal(0);
		BigDecimal repayMangee = new BigDecimal(0);
		BigDecimal chargeInterest = new BigDecimal(0);//提前减息
		BigDecimal delayInterest = new BigDecimal(0);//延期利息
		BigDecimal lateInterest = new BigDecimal(0);//逾期利息
		try {
			for (int i = 0; i < list.size(); i++) {
				form.setBorrowNid(list.get(i).getBorrowNid());
				ProjectBean repayProject =  this.repayService.searchRepayProjectDetail(form,false);
				repayAccount = repayAccount.add(new BigDecimal(repayProject.getRepayAccount())).add(new BigDecimal(repayProject.getManageFee()));
				repayCapital = repayCapital.add(new BigDecimal(repayProject.getRepayCapital()));
				repayInterest = repayInterest.add(new BigDecimal(repayProject.getShouldInterest()));
				repayMangee = repayMangee.add(new BigDecimal(repayProject.getManageFee()));
				chargeInterest = chargeInterest.add(new BigDecimal(repayProject.getChargeInterest() == null ? "0" : repayProject.getChargeInterest()));
				delayInterest = delayInterest.add(new BigDecimal(repayProject.getDelayInterest() == null ? "0" : repayProject.getDelayInterest()));
				lateInterest = lateInterest.add(new BigDecimal(repayProject.getLateInterest() == null ? "0" : repayProject.getLateInterest()));
			}
			repayProjectInfo.setRepayAccount(repayAccount.toString());
			repayProjectInfo.setRepayCapital(repayCapital.toString());
			repayProjectInfo.setRepayInterest(repayInterest.toString());
			repayProjectInfo.setManageFee(repayMangee.toString());
			repayProjectInfo.setChargeInterest(chargeInterest.toString());
			repayProjectInfo.setDelayInterest(delayInterest.toString());
			repayProjectInfo.setLateInterest(lateInterest.toString());
			//返回应收笔数
			repayProjectInfo.setRepayNum(String.valueOf(list.size()));
			modelAndView.addObject("repayInfo",repayProjectInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 担保机构批量还款处理中页面
	 * 
	 * @author cwyang
	 * @param request
	 * @return
	 */
	@RequestMapping(RepayDefine.ORG_USER_BATCH_REPAY_ACTION)
	public ModelAndView orgUserBatchRepay(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView(RepayDefine.ORG_USER_REPAY_SUCCESS_PAGE);
		// 查询该用户的未还款金额
		WebViewUser user = WebUtils.getUser(request);
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String password = request.getParameter("password");
		String repayTotal = request.getParameter("repayTotal");
		Integer userId = user.getUserId();
		//TODO 查询该时间段的所有担保户的待还款记录并进行还款
		if (user != null) {
			modelAndView.addObject("orgUserId", userId);
			modelAndView.addObject("startDate",startDate);
			modelAndView.addObject("endDate",endDate);
			modelAndView.addObject("password",password);
			modelAndView.addObject("repayTotal", repayTotal);
		}
		// 查询该时间段的所有担保户的待还款记录并进行还款
		if (user != null) {
			// 还款方法10分钟只能一次
			boolean result = RedisUtils.tranactionSet("batchOrgRepayUserid_" + userId, 600);
			if(result){
				BankOpenAccount userBankOpenAccount = this.repayService.getBankOpenAccount(userId);
				return startOrgRepay(startDate,endDate,userId,password,request,userBankOpenAccount);
			}
			log.info("==============垫付机构:" + userId + "批量还款失败,项目正在还款中!");
			modelAndView.addObject(RepayDefine.MESSAGE,"系统正在处理还款，请10分钟后再试!");
		}
		return modelAndView;
	}
	
	/**
	 * 
	 * 担保机构开始校验批量还款
	 * 
	 * @author cwyang
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(RepayDefine.ORG_USER_START_BATCH_REPAY_CHECK_ACTION)
	public String orgUserStartBatchRepayCheck(HttpServletRequest request) {
		// 查询该用户的未还款金额
		WebViewUser user = WebUtils.getUser(request);
		String repayTotal = request.getParameter("repayTotal");
		Integer userId = user.getUserId();
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String msg = "";
		if (user != null) {
			BankOpenAccount userBankOpenAccount = this.repayService.getBankOpenAccount(userId);
			if (userBankOpenAccount == null || StringUtils.isEmpty(userBankOpenAccount.getAccount())) {
				msg = "998";
				log.info("==============垫付机构:" + userId + "批量还款失败,用户未开户!");
				return msg;
			}
			boolean isBalance = comperToOrgUserBalance(userBankOpenAccount,new BigDecimal(repayTotal));
			if (!isBalance) {//余额不足
				msg = "997";
				log.info("==============垫付机构:" + userId + "批量还款失败,用户银行可用余额不足!");
				return msg;
			}
			boolean reslut = RedisUtils.exists("batchOrgRepayUserid_" + userId);
			if(reslut){
				msg = "999";
				log.info("==============垫付机构:" + userId + "校验处->批量还款失败,项目正在还款中!");
				return msg;
			}
			boolean isTime = companyRepayTime(startDate,endDate,userId);
			if(isTime){
				msg = "996";
				log.info("==============垫付机构:" + userId + "校验处->批量还款失败,还款区间大于28天!");
				return msg;
			}
		}
		return msg;
	}

	/**
	 * 查询是否存在提前28天以上的还款标的
	 * @param endDate
	 * @param userId
	 * @return
	 */
	private boolean companyRepayTime(String startDate,String endDate, Integer userId) {
		RepayProjectListBean form = new RepayProjectListBean();
		form.setUserId(userId + "");
		form.setRoleId("3");
		form.setStartDate(startDate);
		form.setEndDate(endDate);
		form.setStatus("0");
		form.setRepayStatus("0");
		form.setRepayOrder("1");
		List<WebUserRepayProjectListCustomize> list = repayService.searchUserRepayList(form , 0, 1);
		if(list != null && list.size() > 0){
			WebUserRepayProjectListCustomize customize = list.get(0);
			String repayTime = customize.getRepayTime();
			try {
				int day = GetDate.daysBetween(GetDate.getDayStart(new java.util.Date()), GetDate.getDayStart(repayTime));
				if(day >= 28){
					return  true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				log.info("==================校验还款区间是否大于28天报错===================");
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * 担保机构开始批量还款
	 * 
	 * @author cwyang
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(RepayDefine.ORG_USER_START_BATCH_REPAY_ACTION)
	public String orgUserStartBatchRepay(HttpServletRequest request) {
		// 查询该用户的未还款金额
		WebViewUser user = WebUtils.getUser(request);
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String password = request.getParameter("password");
		Integer userId = user.getUserId();
		String msg = "";
		//TODO 查询该时间段的所有担保户的待还款记录并进行还款
		if (user != null) {
			// 还款方法10分钟只能一次
			boolean result = RedisUtils.tranactionSet("batchOrgRepayUserid_" + userId, 600);
			if(!result){
				msg = "999";
				log.info("==============垫付机构:" + userId + "批量还款失败,项目正在还款中!");
				return msg;
			}
			BankOpenAccount userBankOpenAccount = this.repayService.getBankOpenAccount(userId);
			startOrgRepay(startDate,endDate,userId,password,request,userBankOpenAccount);
		}
		
		return msg;
	}
	
	private boolean comperToOrgUserBalance(BankOpenAccount bankOpenAccount, BigDecimal repayTotal) {
		BigDecimal userBankBalance = this.repayService.getBankBalance(bankOpenAccount.getUserId(), bankOpenAccount.getAccount());
		if (repayTotal.compareTo(userBankBalance) == 0 || repayTotal.compareTo(userBankBalance) == -1) {
			return true;
		}
		return false;
	}

	/**
	 * 开始批量还款
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @param password 
	 * @param request 
	 * @param userBankOpenAccount
	 */
	private ModelAndView startOrgRepay(String startDate, String endDate, Integer userId, String password, HttpServletRequest request, BankOpenAccount userBankOpenAccount) {
		RepayProjectListBean form = new RepayProjectListBean();
		form.setUserId(userId + "");
		form.setRoleId("3");
		form.setStartDate(startDate);
		form.setEndDate(endDate);
		form.setStatus("0");
		form.setRepayStatus("0");
		Users user = this.repayService.getUsersByUserId(userId);
		List<WebUserRepayProjectListCustomize> list = repayService.searchUserRepayList(form , 0, 0);
		String ip = GetCilentIP.getIpAddr(request);
		String account = userBankOpenAccount.getAccount();
		String txDate = GetOrderIdUtils.getTxDate();// 交易日期
		String txTime = GetOrderIdUtils.getTxTime();// 交易时间
		String seqNo = GetOrderIdUtils.getSeqNo(6);// 交易流水号
		String orderId = txDate + txTime + seqNo;// 交易日期+交易时间+交易流水号
		BigDecimal repayTotal = BigDecimal.ZERO;
		if (list != null && list.size() > 0) {
			int allRepaySize = list.size();//所有还款标的数目
			log.info("=================cwyang 总还款笔数:" + allRepaySize + ",还款id:" + userId);
			for (int i = 0; i < list.size(); i++) {
				JSONObject info = new JSONObject();
				WebUserRepayProjectListCustomize repayInfo = list.get(i);
				String borrowNid = repayInfo.getBorrowNid();
				try {
					BorrowWithBLOBs borrow = repayService.getBorrowByNid(borrowNid);
					Integer repayUserId = borrow.getUserId();
					int isOrg = 1;//垫付机构不校验单笔标的的冻结余额
					RepayBean repay = this.validatorFieldCheckRepay_org(info, userId, password, borrow, repayUserId,isOrg, false,true);
					if (!ValidatorCheckUtil.hasValidateError(info) && repay != null) {

						//防止汇计划还款时正在发生债转操作
						int errflag = repay.getFlag();
						if (1 == errflag) {
							throw new RuntimeException("标的号:" + borrowNid + ",存在正在债转的操作,无法还款........");
						}
						//还款去重
						boolean result = repayService.checkRepayInfo(borrowNid);
						if (!result) {
							log.info("标的号:{},项目正在还款中...",borrowNid);
							continue;
						}
						// 用户还款
						try {
							//插入垫付机构冻结信息日志表 add by wgx 2018-09-11
							repayService.insertRepayOrgFreezeLof(userId, orderId, account, borrow, repay, user.getUsername(), false);
							BigDecimal total = repay.getRepayAccountAll();
							repayTotal = repayTotal.add(total);
						} catch (Exception e) {
							log.info("==============垫付机构:" + userId + "批量还款调用银行接口异常,标的号:" + borrowNid);
							e.printStackTrace();
						}

					} else {
						log.info("==============垫付机构:" + userId + "还款计算失败,标的号:" + borrowNid + ",失败原因:" + info.getString(RepayDefine.REPAY_ERROR));
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("==============垫付机构:" + userId + "批量还款存在失败标的,标的号:" + borrowNid);
				}

			}
			try {
				if(repayTotal.compareTo(BigDecimal.ZERO) == 1) {// 可正常还款金额大于0
					// 调用江西银行还款申请冻结资金
					return getBankRefinanceFreezePage(userId, user.getUsername(), ip, orderId, "", repayTotal, account);
				}
			} catch (Exception e) {
				log.info("==============垫付机构:" + userId + "批量还款调用银行接口异常,订单号:" + orderId);
				e.printStackTrace();
			}
		}
		ModelAndView modelAndView = new ModelAndView(RepayDefine.ORG_USER_REPAY_SUCCESS_PAGE);
		log.info("==============垫付机构:" + userId + "批量还款金额为0,订单号:" + orderId);
		return modelAndView;

	}
	
	/**
	 * 推送消息
	 * 
	 * @param userid
	 * @author Administrator
	 * @param successRepaySize,int userid 
	 * @param allRepaySize 
	 */
	private void sendMessage(int allRepaySize, int successRepaySize,int userid) {
		Map<String, String> msg = new HashMap<String, String>();
		msg.put(VAL_ALLCOUNT, allRepaySize + "");// 所有还款项目
		msg.put(VAL_SUCCESSCOUNT, successRepaySize + "");//成功还款项目
		msg.put(VAL_FAILCOUNT, (allRepaySize - successRepaySize) + "");//成功还款项目
		msg.put(VAL_USERID, String.valueOf(userid));
		if (Validator.isNotNull(msg.get(VAL_USERID))) {
			Users users = repayService.getUsersByUserId(Integer.valueOf(msg.get(VAL_USERID)));
			if (users == null) {
				return;
			} else {
				if (allRepaySize == successRepaySize) {//全部成功
					SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.JYTZ_PLAN_REPAYALL_SUCCESS,
							CustomConstants.CHANNEL_TYPE_NORMAL);
					smsProcesser.gather(smsMessage);
				}else{// 改批次代偿，失败不发短信 2018-9-13 wgx
					//SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(VAL_USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null, CustomConstants.JYTZ_PLAN_REPAYPART_SUCCESS,
					//		CustomConstants.CHANNEL_TYPE_NORMAL);
					//smsProcesser.gather(smsMessage);
				}
			}
		}
	}

	/**
	 * 批次结束债权收到报文后对合法性检查后的异步回调
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(RepayDefine.BATCHCREDITEND_VERIFY_ACTION)
	public String batchCreditEndVerify(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) throws Exception {

		BankCallResult result = new BankCallResult();
		log.info("批次结束债权,合法性检查异步回调开始 "+bean.getRetCode() + "  "+ bean.getBatchNo());
		
		if (StringUtils.isBlank(bean.getRetCode()) || StringUtils.isBlank(bean.getBatchNo())) {
			log.info(" 合法性校验 为空！");
			return JSONObject.toJSONString(result, true);
		}
		
		// 跟据批次号查询当批次所有数据，更新所有
		int resultCnt = this.repayService.updateBatchCreditEndCheck(bean);
		
		log.info(bean.getBatchNo()+" 批次结束债权,合法性检查的异步回调结束 : "+resultCnt);
		
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);

	}

	/**
	 * 批次结束债权业务处理结果的异步回调
	 * 
	 * @param request
	 * @param response
	 * @param bean
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(RepayDefine.BATCHCREDITEND_RESULT_RETURN_ACTION)
	public String batchCreditEndResultReturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) throws Exception {

		BankCallResult result = new BankCallResult();
		log.info("批次结束债权,业务结果异步回调开始 "+bean.getRetCode() + "  "+ bean.getBatchNo());
		
		if (StringUtils.isBlank(bean.getRetCode()) || StringUtils.isBlank(bean.getBatchNo())) {
			log.info(" 业务结果异步回调 为空！");
			return JSONObject.toJSONString(result, true);
		}
		
		// 跟据批次号查询当批次所有数据，更新所有
		int resultCnt = this.repayService.updateBatchCreditEnd(bean);
		
		log.info(bean.getBatchNo()+" 批次结束债权,业务结果异步回调结束 : "+resultCnt);
		
		result.setStatus(true);
		return JSONObject.toJSONString(result, true);

	}

	/**
	 * 代偿冻结（合规要求）
	 * @param userId
	 * @param userName
	 * @param ip
	 * @param orderId
	 * @param borrowNid
	 * @param repayTotal
	 * @param account
	 * @return
	 */
	private ModelAndView getBankRefinanceFreezePage(Integer userId, String userName,String ip, String orderId, String borrowNid, BigDecimal repayTotal, String account) {
		ModelAndView modelAndView = new ModelAndView(RepayDefine.REPAY_ERROR_PAGE);
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxCode(BankCallMethodConstant.TXCODE_REFINANCE_FREEZE_PAGE);// 交易代码
		bean.setTxDate(orderId.substring(0,8));
		bean.setTxTime(orderId.substring(8,14));
		bean.setSeqNo(orderId.substring(14));
		bean.setOrderId(orderId);// 订单号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(account);// 电子账号
		bean.setTxAmount(String.valueOf(repayTotal));// 交易金额
		bean.setProductId(borrowNid);
		bean.setFrzType("0");
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserName(userName);
		bean.setLogClient(0);
		bean.setLogIp(ip);
		bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_REFINANCE_FREEZE_PAGE);
		// 同步调用路径
		String retUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + RepayDefine.REQUEST_MAPPING
				+ RepayDefine.REPAY_SYNC_RETURN_ACTION + ".do";
		// 异步调用路
		String bgRetUrl = PropUtils.getSystem(CustomConstants.HYJF_WEB_URL) + RepayDefine.REQUEST_MAPPING
				+ RepayDefine.REPAY_ASYNC_RETURN_ACTION + ".do";
		bean.setRetUrl(retUrl);// 页面同步返回 URL
		bean.setSuccessfulUrl(retUrl + "?orderId=" + orderId + "&isBatchRepay=" + StringUtils.isBlank(borrowNid));
		bean.setNotifyUrl(bgRetUrl + "?orderId=" + orderId + "&isBatchRepay=" + StringUtils.isBlank(borrowNid));// 页面异步返回URL(必须)
		try {
			modelAndView = BankCallUtils.callApi(bean);
			log.info("【代偿冻结】调用还款申请冻结资金接口成功,订单号:{}", orderId);
		} catch (Exception e) {
			log.info("【代偿冻结】调用还款申请冻结资金接口失败,订单号:{}", orderId);
			modelAndView.addObject("message", "还款失败，请稍后再试！");
			LogUtil.errorLog(RepayController.class.toString(), "getBankRefinanceFreezePage", e);
			return modelAndView;
		}
		return modelAndView;
	}

	/**
	 * 单笔还款申请冻结查询
	 * @param userId
	 * @param userName
	 * @param orderId
	 * @param account
	 * @return
	 */
	public boolean queryBalanceFreeze(Integer userId, String userName, String orderId, String account) {
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_BALANCE_FREEZE_QUERY);
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(account);// 电子账号
		bean.setOrgOrderId(orderId);// 原订单号
		// 操作者ID
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(userId));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserName(userName);
		bean.setLogClient(0);
		try {
			BankCallBean callBackBean = BankCallUtils.callApiBg(bean);
			String respCode = callBackBean == null ? "" : callBackBean.getRetCode();
			// 单笔还款申请冻结查询失败
			if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
				log.info("【冻结查询】调用单笔还款申请冻结查询接口失败:{},订单号:{}", callBackBean.getRetMsg(), callBackBean.getOrderId());
				return false;
			} else {
				// 单笔还款申请冻结查询非正常
				if (!BankCallConstant.STATUS_SUCCESS.equals(callBackBean.getState())) {
					repayService.deleteOrgFreezeTempLogs(orderId, null);
					RedisUtils.del("batchOrgRepayUserid_" + userId);
					log.info("【冻结查询】单笔还款申请冻结未成功，订单号:{}", callBackBean.getOrderId());
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 机构垫付还款同步回调
	 *
	 * @param request
	 * @param callBackBean
	 * @return
	 */
	@RequestMapping(RepayDefine.REPAY_SYNC_RETURN_ACTION)
	public ModelAndView repayReturn(HttpServletRequest request, @ModelAttribute BankCallBean callBackBean) {
		log.debug("代偿冻结同步回调开始");
		String orderId = request.getParameter("orderId");
		boolean isBatchRepay = Boolean.parseBoolean(request.getParameter("isBatchRepay"));// 是否批量还款
		ModelAndView modelAndView = new ModelAndView(isBatchRepay ? RepayDefine.ORG_USER_REPAY_SUCCESS_PAGE : RepayDefine.ORG_REPAY_SUCCESS_PAGE);
		if (StringUtils.isBlank(orderId)) {
			if (!isBatchRepay) {
				modelAndView.setViewName(RepayDefine.ORG_REPAY_ERROR_PAGE);
			}
			modelAndView.addObject("message", "还款失败,失败原因：" + repayService.getBankRetMsg(callBackBean.getRetCode()));
			log.info("【代偿冻结同步回调】垫付机构还款失败,错误码：{}", callBackBean.getRetCode());
			return modelAndView;
		}
		log.info("【代偿冻结同步回调】垫付机构还款申请成功,订单号:{}", orderId);
		return modelAndView;
	}

	/**
	 * 机构垫付还款异步回调
	 *
	 * @param request
	 * @param response
	 * @param callBackBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(RepayDefine.REPAY_ASYNC_RETURN_ACTION)
	public BankCallResult repayAsyncReturn(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean callBackBean) throws IOException {
		BankCallResult result = new BankCallResult();
		result.setStatus(false);
		String orderId = request.getParameter("orderId");
		boolean isBatchRepay = Boolean.parseBoolean(request.getParameter("isBatchRepay"));// 是否批量还款
		if (StringUtils.isBlank(orderId)) {
			log.info("【代偿冻结异步回调】获取冻结订单号失败！");
			return result;
		}
		List<BankRepayOrgFreezeLog> orgLogList = repayService.getBankRepayOrgFreezeLogList(null, orderId);
		if (orgLogList == null || orgLogList.size() == 0) {
			log.info("【代偿冻结异步回调】还款申请资金已解冻,订单号：{}", orderId);
			return result;
		}
		log.info("【代偿冻结异步回调】{}还款冻结开始处理，订单号：{}", isBatchRepay ? "批量" : "", orderId);
		String accountId = callBackBean.getAccountId();//电子账号
		Integer userId = orgLogList.get(0).getRepayUserId();
		String userName = orgLogList.get(0).getRepayUserName();
		// 垫付机构还款冻结状态查询
		boolean hasFreeze = queryBalanceFreeze(Integer.valueOf(userId), userName, orderId, accountId);
		if (hasFreeze) {
			for (BankRepayOrgFreezeLog orgFreezeLog : orgLogList) {
				String borrowNid = orgFreezeLog.getBorrowNid();
				boolean isAllRepay = orgFreezeLog.getAllRepayFlag() == 1;
				try {
					Borrow borrow = this.repayService.searchRepayProject(userId, userName, "3", borrowNid);
					// 垫付机构的还款
					RepayBean repay = this.getRepayBean(userId, borrow, isAllRepay);
					if (repay != null) {
						// 还款后变更数据
						callBackBean.setOrderId(orderId);
						boolean flag = repayService.updateRepayMoney(repay, callBackBean, 3, Integer.valueOf(userId), userName, isAllRepay);
						if (flag) {
							// 如果有正在出让的债权,先去把出让状态停止
							repayService.deleteOrgFreezeTempLogs(orderId, borrowNid);
							repayService.updateBorrowCreditStautus(borrow);
							log.info("【代偿冻结异步回调】垫付机构:" + userId + "还款申请成功,标的号:{},订单号:{}", borrowNid, orderId);
						} else {
							log.error("【代偿冻结异步回调】垫付机构:" + userId + "还款更新数据失败,标的号:{},订单号:{}", borrowNid, orderId);
							if (!isBatchRepay) {
								return result;
							}
						}
					} else {
						log.info("【代偿冻结异步回调】获取垫付机构:" + userId + "还款信息失败,标的号:{},订单号:{}", borrowNid, orderId);
						if (!isBatchRepay) {
							return result;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("【代偿冻结异步回调】垫付机构:" + userId + "还款调用银行接口异常,标的号:{},订单号:{}", borrowNid, orderId);
					if (!isBatchRepay) {
						return result;
					}
				}
				if (!isBatchRepay) {
					break;
				}
			}
			log.info("【代偿冻结异步回调】垫付机构:{}还款申请处理完成,订单号:{}", userId, orderId);
			if (isBatchRepay) {
				sendMessage(orgLogList.size(), orgLogList.size(), userId);
				// RedisUtils.del("batchOrgRepayUserid_" + userId); 昌卫建议不删除
			}
		} else {
			log.info("【代偿冻结异步回调】垫付机构还款未冻结,订单号:{}", orderId);
		}
		result.setStatus(true);
		return result;
	}
	/**
	 * 获取垫付机构还款信息
	 * @param userId
	 * @param borrow
	 * @param isAllRepay
	 * @return
	 * @throws Exception
	 */
	private RepayBean getRepayBean(int userId, Borrow borrow, boolean isAllRepay) throws Exception {
		RepayBean repayByTerm;
		String borrowStyle = StringUtils.isNotBlank(borrow.getBorrowStyle()) ? borrow.getBorrowStyle() : null;
		BigDecimal borrowApr = borrow.getBorrowApr();
		Integer repayUserId = borrow.getUserId();
		// 一次性还款
		if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle) || CustomConstants.BORROW_STYLE_END.equals(borrowStyle)) {
			repayByTerm = this.repayService.searchRepayTotalV2(repayUserId, borrow);
			repayByTerm.setRepayUserId(userId);// 垫付机构id
		} else {// 分期还款
			if (isAllRepay) {
				repayByTerm = this.repayService.searchRepayPlanTotal(repayUserId, borrow);
			} else {
				repayByTerm = this.repayService.searchRepayByTermTotalV2(repayUserId, borrow, borrowApr, borrowStyle, borrow.getBorrowPeriod());
			}
			repayByTerm.setRepayUserId(userId);// 垫付机构id
		}
		return repayByTerm;
	}


}
