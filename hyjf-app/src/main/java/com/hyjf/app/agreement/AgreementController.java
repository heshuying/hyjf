package com.hyjf.app.agreement;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.project.ProjectService;
import com.hyjf.app.user.credit.AppTenderCreditDefine;
import com.hyjf.app.user.credit.CreditResultBean;
import com.hyjf.app.user.project.InvestProjectDefine;
import com.hyjf.app.user.project.InvestProjectService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网站协议集合
 *
 * @author hyjf
 * @version hyjf 1.0
 * @see 17:27:14
 * @since hyjf 1.0 2017年6月5日
 */
@Controller
@RequestMapping(value = AgreementDefine.REQUEST_MAPPING)
public class AgreementController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(AgreementController.class);

    @Autowired
    private AgreementService agreementService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private InvestProjectService investProjectService;

    /**
     * 汇盈金服互联网金融服务平台居间服务协议
     *
     * @param request
     * @param response
     * @param tenderCreditAssignedBean
     * @return
     */
    @RequestMapping(AgreementDefine.INTERMEDIARY_SERVICES_ACTION)
    public ModelAndView intermediaryServices(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CreditAssignedBean tenderCreditAssignedBean) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.INTERMEDIARY_SERVICES_ACTION);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.INTERMEDIARY_SERVICES_PATH);
        CreditResultBean creditResultBean = new CreditResultBean();
        String sign = request.getParameter("sign");
        Integer userId = null;
        try {
            /*userId = WebUtils.getUserId(request); */// 用户ID
            userId = SecretUtil.getUserId(sign);
            if (userId != null && userId.intValue() != 0) {
                if (StringUtils.isEmpty(tenderCreditAssignedBean.getBidNid())
                        || StringUtils.isEmpty(tenderCreditAssignedBean.getCreditNid())
                        || StringUtils.isEmpty(tenderCreditAssignedBean.getCreditTenderNid())
                        || StringUtils.isEmpty(tenderCreditAssignedBean.getAssignNid())) {
                    creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
                    creditResultBean.setMsg("查看失败,参数不符");
                    creditResultBean.setData(null);
                    LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.INTERMEDIARY_SERVICES_ACTION);
                    modelAndView.addObject("creditResult", creditResultBean);
                    return modelAndView;
                }
                Map<String, Object> creditContract = agreementService.selectUserCreditContract(tenderCreditAssignedBean);
                creditResultBean.setResultFlag(CustomConstants.RESULT_SUCCESS);
                creditResultBean.setMsg("");
                creditResultBean.setData(creditContract);
            } else {
                LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "用户未登录");
                creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
                creditResultBean.setMsg("用户未登录");
                creditResultBean.setData(null);
            }
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "系统异常");
            creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
            creditResultBean.setMsg("系统异常");
            creditResultBean.setData(null);
        }
        modelAndView.addObject("creditResult", creditResultBean);
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.INTERMEDIARY_SERVICES_ACTION);
        return modelAndView;
    }


    /**
     * 资产管理-计划-计划协议-汇计划出借计划服务协议（查看协议）
     *
     * @param request
     * @return
     */
    @RequestMapping(value = AgreementDefine.HJH_INFO_AGREEMENT)
    public ModelAndView hjhInfo(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.HJH_INFO_AGREEMENT_PATH);
        String accedeOrderId = request.getParameter("accedeOrderId");
        String sign = request.getParameter("sign");
        if (StringUtils.isNotEmpty(accedeOrderId)) {
            logger.info("get sign is: {}", sign);
            Integer userId = SecretUtil.getUserId(sign);
            logger.info("get userId is: {}", userId);
            // 1基本信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("accedeOrderId", accedeOrderId);
            params.put("userId", userId);
            UsersInfo userInfo = agreementService.getUsersInfoByUserId(userId);
            modelAndView.addObject("userInfo", userInfo);
            UserHjhInvistDetailCustomize userHjhInvistDetailCustomize = agreementService
                    .selectUserHjhInvistDetail(params);
            modelAndView.addObject("userHjhInvistDetail", userHjhInvistDetailCustomize);
        } else {
            modelAndView.addObject("userHjhInvistDetail", null);
        }

        return modelAndView;
    }

    /**
     * 散标风险揭示书页面
     *
     * @return
     * @author sss
     */
    @RequestMapping(AgreementDefine.CONFIRMATION_OF_INVESTMENT_RISK_ACTION)
    public ModelAndView confirmationOfInvestmentRisk() {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.CONFIRMATION_OF_INVESTMENT_RISK_ACTION);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.CONFIRMATION_OF_INVESTMENT_RISK_PATH);
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.CONFIRMATION_OF_INVESTMENT_RISK_ACTION);
        return modelAndView;
    }


    /**
     * app 居间服务协议
     *
     * @param request
     * @return
     */
    @RequestMapping(value = AgreementDefine.HJH_MEDIACY_CONTRACT)
    public ModelAndView getServcieContract(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.INTERMEDIARY_SERVICES_PATH);
        String borrowNid = request.getParameter("borrowNid");
        String sign = request.getParameter("sign");
        logger.info("borrowNid is: {}", borrowNid);

        if (StringUtils.isNotEmpty(borrowNid)
                && StringUtils.isNotEmpty(sign)) {

            Integer userId = SecretUtil.getUserId(sign);

            // 出借人
            UsersInfo userInfo = agreementService.getUsersInfoByUserId(Integer.valueOf(userId));
            modelAndView.addObject("userInfo", userInfo);
            modelAndView.addObject("borrowNid", borrowNid);

            // 借款人
            Integer borrowerUserId = agreementService.selectBorrowerByBorrowNid(borrowNid);
            UsersInfo borrowerUserInfo = agreementService.getUsersInfoByUserId(borrowerUserId);
            modelAndView.addObject("borrowerUserInfo", borrowerUserInfo);

            BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
            borrowCommonCustomize.setBorrowNidSrch(borrowNid);
            List<BorrowCustomize> recordList = agreementService.selectBorrowList(borrowCommonCustomize);
            if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
                // 最后一笔的放款完成时间 (协议签订日期)
                modelAndView.addObject("recoverTime", recordList.get(0).getRecoverLastTime());
            } else {
                // 设置为满标时间
                modelAndView.addObject("recoverTime", recordList.get(0).getReverifyTime());
            }

        } else {
            modelAndView.addObject("userHjhInvistDetail", null);
        }
        return modelAndView;
    }


    /**
     * 出借协议《借款协议》--查询有填充值
     *
     * @return
     * @throws Exception
     */

    @RequestMapping(value = AgreementDefine.HJH_DIARY_AGREEMENT)
    public ModelAndView hjhDiaryAgreement(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.DIARY_SERVICES_PATH);
        String borrowNid = request.getParameter("borrowNid");
        String accedeOrderId = request.getParameter("accedeOrderId");
        // 出借金额
//        String account = request.getParameter("account");
        String sign = request.getParameter("sign");

        // 检查参数正确性
        if (Validator.isNull(sign)) {
            logger.error("sign非空校验失败....");
            return modelAndView;
        }

        Integer userId = SecretUtil.getUserId(sign);

        logger.info("userId is: {}", userId);

//        if (StringUtils.isNotEmpty(borrowNid) && StringUtils.isNotEmpty(account)) {
        if (StringUtils.isNotEmpty(borrowNid) && StringUtils.isNotEmpty(accedeOrderId)) {
            // 出借人
            UsersInfo userInfo = agreementService.getUsersInfoByUserId(Integer.valueOf(userId));
            modelAndView.addObject("userInfo", userInfo);
            modelAndView.addObject("borrowNid", borrowNid);


            // 借款人
            Integer borrowerUserId = agreementService.selectBorrowerByBorrowNid(borrowNid);
            UsersInfo borrowerUserInfo = agreementService.getUsersInfoByUserId(borrowerUserId);
            modelAndView.addObject("borrowerUserInfo", borrowerUserInfo);

            BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
            borrowCommonCustomize.setBorrowNidSrch(borrowNid);
            List<BorrowCustomize> recordList = agreementService.selectBorrowList(borrowCommonCustomize);

            modelAndView.addObject("recoverAccount", agreementService.getAccedeAccount(accedeOrderId));
            if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
                // 最后一笔的放款完成时间 (协议签订日期)
                modelAndView.addObject("recoverTime", recordList.get(0).getRecoverLastTime());
            } else {
                // 设置为满标时间
                modelAndView.addObject("recoverTime", recordList.get(0).getReverifyTime());
            }
            modelAndView.addObject("record", recordList.get(0));

        } else {
            modelAndView.addObject("userHjhInvistDetail", null);
            modelAndView.addObject("record", null);
            modelAndView.addObject("recoverTime", null);
        }
        return modelAndView;
    }

    /**
     * 协议列表查询有填充值
     *
     * @return
     * @throws Exception
     */

    @RequestMapping(value = AgreementDefine.HJH_LIST_AGREEMENT)
    public ModelAndView hjhListAgreement(HttpServletRequest request, HttpServletResponse response) {
        String url2 = request.getScheme() + "://" + request.getServerName();//+request.getRequestURI();
        System.out.println("协议名：//域名=" + url2);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.LIST_SERVICES_PATH);
        String accedeOrderId = request.getParameter("accedeOrderId");
        String borrowNid = request.getParameter("borrowNid");
        String userId = request.getParameter("userId");
        String sign = request.getParameter("sign");
        String account = request.getParameter("account");
        logger.info("userId is: {},accedeOrderId is :{} ", userId, accedeOrderId);
        if (StringUtils.isNotEmpty(accedeOrderId) && StringUtils.isNotEmpty(borrowNid)
                && StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(sign)) {
            // 出借人

            modelAndView.addObject("accedeOrderId", accedeOrderId);
            modelAndView.addObject("borrowNid", borrowNid);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("sign", sign);
            modelAndView.addObject("recoverAccount", account);

        } else {

        }
        modelAndView.addObject("hostAppUrl", url2);
        return modelAndView;
    }

    /**
     * 融通宝的链接 嘉融的地址
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = AgreementDefine.RONG_TONG_BAO_JIA)
    public ModelAndView rongtongbao(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView modelAndView = null;
        String orderId = request.getParameter("orderId");
        String borrowNid = request.getParameter("borrowNid");
//	    int userId = Integer.valueOf(request.getParameter("userId"));

        String sign = request.getParameter("sign"); // 随机字符串


        // 用户id
        Integer userId = SecretUtil.getUserId(sign);

//	    int userId=3788;
        // 2.根据项目标号获取相应的项目信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("orderId", orderId);
        params.put("userId", userId);
//		AppProjectContractDetailCustomize borrow = this.investProjectService.selectProjectContractDetail(params);

        AppProjectDetailCustomize borrow1 = this.projectService.selectProjectDetail(borrowNid);
//		if (borrow != null && borrow1.getBorrowPublisher() != null && borrow1.getBorrowPublisher().equals("中商储")) {
//			modelAndView = new ModelAndView(InvestProjectDefine.PROJECT_RTB_CONTRACT_ZSC_PTAH);
//		} else {
        modelAndView = new ModelAndView(InvestProjectDefine.PROJECT_RTB_CONTRACT_PTAH);
//		}

        UsersInfo userinfo = investProjectService.getUsersInfoByUserId(userId);

        List<WebUserInvestListCustomize> invest = investProjectService.selectUserInvestList(borrowNid, userId,
                orderId, 0, 10);
        if (invest != null && invest.size() > 0) {
            modelAndView.addObject("investDeatil", invest.get(0));
        }
//		if (borrow1 != null) {
//			borrow1.setBorrowPeriod(borrow1.getBorrowPeriod().substring(0, borrow1.getBorrowPeriod().length() - 1));
//		}
        modelAndView.addObject("projectDeatil", borrow1);
        modelAndView.addObject("truename", userinfo.getTruename());
        modelAndView.addObject("idcard", userinfo.getIdcard());
        modelAndView.addObject("borrowNid", borrowNid);// 标的号
        modelAndView.addObject("assetNumber", borrow1.getBorrowAssetNumber());// 资产编号
        modelAndView.addObject("projectType", "RTB");// 项目类型

//		modelAndView.addObject("borrow", borrow);
//		String repayType = borrow.getRepayType();
//		if (CustomConstants.BORROW_STYLE_MONTH.equals(repayType)
//				|| CustomConstants.BORROW_STYLE_PRINCIPAL.equals(repayType)
//				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(repayType)) {
//			List<AppProjectContractRecoverPlanCustomize> repayPlans = this.investProjectService
//					.selectProjectContractRecoverPlan(params);
//			modelAndView.addObject("repayPlans", repayPlans);
//		}
        return modelAndView;
    }

    /**
     * 融通宝的链接 中商储的地址
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = AgreementDefine.RONG_TONG_BAO_ZHONG)
    public ModelAndView rongtongbaoZhong(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView modelAndView = null;
        String orderId = request.getParameter("orderId");
        String borrowNid = request.getParameter("borrowNid");
//	    int userId = Integer.valueOf(request.getParameter("userId"));

        String sign = request.getParameter("sign"); // 随机字符串
        // 用户id
        Integer userId = SecretUtil.getUserId(sign);
//		form.setUserId(userId.toString());
//		 int userId=3788;
        // 2.根据项目标号获取相应的项目信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", borrowNid);
        params.put("orderId", orderId);
        params.put("userId", userId);
//		AppProjectContractDetailCustomize borrow = this.investProjectService.selectProjectContractDetail(params);

        AppProjectDetailCustomize borrow1 = this.projectService.selectProjectDetail(borrowNid);
//		if (borrow != null && borrow1.getBorrowPublisher() != null && borrow1.getBorrowPublisher().equals("中商储")) {
        modelAndView = new ModelAndView(InvestProjectDefine.PROJECT_RTB_CONTRACT_ZSC_PTAH);
//		} else {
//			modelAndView = new ModelAndView(InvestProjectDefine.PROJECT_RTB_CONTRACT_PTAH);
//		}

        UsersInfo userinfo = investProjectService.getUsersInfoByUserId(userId);

        List<WebUserInvestListCustomize> invest = investProjectService.selectUserInvestList(borrowNid, userId,
                orderId, 0, 10);
        if (invest != null && invest.size() > 0) {
            modelAndView.addObject("investDeatil", invest.get(0));
        }
//		if (borrow1 != null) {
//			borrow1.setBorrowPeriod(borrow1.getBorrowPeriod().substring(0, borrow1.getBorrowPeriod().length() - 1));
//		}
        modelAndView.addObject("projectDeatil", borrow1);
        modelAndView.addObject("truename", userinfo.getTruename());
        modelAndView.addObject("idcard", userinfo.getIdcard());
        modelAndView.addObject("borrowNid", borrowNid);// 标的号
        modelAndView.addObject("assetNumber", borrow1.getBorrowAssetNumber());// 资产编号
        modelAndView.addObject("projectType", "RTB");// 项目类型

//		modelAndView.addObject("borrow", borrow);
//		String repayType = borrow.getRepayType();
//		if (CustomConstants.BORROW_STYLE_MONTH.equals(repayType)
//				|| CustomConstants.BORROW_STYLE_PRINCIPAL.equals(repayType)
//				|| CustomConstants.BORROW_STYLE_ENDMONTH.equals(repayType)) {
//			List<AppProjectContractRecoverPlanCustomize> repayPlans = this.investProjectService
//					.selectProjectContractRecoverPlan(params);
//			modelAndView.addObject("repayPlans", repayPlans);
//		}
        return modelAndView;
    }

    /**
     * app 我的计划-计划详情-资产列表-协议（转让）
     *
     * @param request
     * @return
     */
    @RequestMapping(value = AgreementDefine.PLAN_CREDIT_CONTRACT)
    public ModelAndView userCreditContract(HttpServletRequest request) {
        LogUtil.startLog(AppTenderCreditDefine.THIS_CLASS, AgreementDefine.PLAN_CREDIT_CONTRACT);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.PLAN_CREDIT_CONTRACT_PATH);
        String nid = request.getParameter("nid");
        logger.info("我的计划-计划详情-资产列表-协议，债转id :{}", nid);
        // 债转承接信息
        HjhDebtCreditTender hjhDebtCreditTender = this.agreementService.getHjhDebtCreditTender(Integer.parseInt(nid));
        modelAndView.addObject("hjhDebtCreditTender", hjhDebtCreditTender);

        if (hjhDebtCreditTender != null) {
            // 转让人信息
            UsersInfo creditUserInfo = this.agreementService
                    .getUsersInfoByUserId(hjhDebtCreditTender.getCreditUserId());
            Users creditUser = this.agreementService.getUsers(hjhDebtCreditTender.getCreditUserId());
            // 承接人信息
            UsersInfo usersInfo = this.agreementService.getUsersInfoByUserId(hjhDebtCreditTender.getUserId());
            Users user = this.agreementService.getUsers(hjhDebtCreditTender.getUserId());

            modelAndView.addObject("creditUserInfo", creditUserInfo);
            modelAndView.addObject("creditUser", creditUser);
            modelAndView.addObject("usersInfo", usersInfo);
            modelAndView.addObject("user", user);

            // 标的信息
            Borrow borrow = this.agreementService.getBorrowByNid(hjhDebtCreditTender.getBorrowNid());
            modelAndView.addObject("borrow", borrow);

            // 债转信息
            HjhDebtCredit hjhDebtCredit = this.agreementService
                    .getHjhDebtCreditByCreditNid(hjhDebtCreditTender.getCreditNid());
            modelAndView.addObject("hjhDebtCredit", hjhDebtCredit);
        }

        LogUtil.endLog(AppTenderCreditDefine.THIS_CLASS, AgreementDefine.PLAN_CREDIT_CONTRACT);
        return modelAndView;
    }


}
