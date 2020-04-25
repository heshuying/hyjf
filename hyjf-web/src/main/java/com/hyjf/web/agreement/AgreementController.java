/**
 * 个人设置控制器
 */
package com.hyjf.web.agreement;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.BorrowService;
import com.hyjf.bank.service.user.credit.CreditAssignedBean;
import com.hyjf.bank.service.user.credit.CreditResultBean;
import com.hyjf.bank.service.user.credit.CreditService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.ProtocolTemplate;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.PlanInvestCustomize;
import com.hyjf.mybatis.model.customize.PlanLockCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.htj.DebtBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectRepayListCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.hjhplan.HjhPlanService;
import com.hyjf.web.project.ProjectService;
import com.hyjf.web.user.mytender.MyTenderService;
import com.hyjf.web.user.mytender.ProjectRepayListBean;
import com.hyjf.web.user.mytender.UserInvestListBean;
import com.hyjf.web.user.planinfo.PlanInfoService;
import com.hyjf.web.user.planinfo.PlanListBean;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网站协议集合
 *
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年6月5日
 */
@Controller
@RequestMapping(value = AgreementDefine.REQUEST_MAPPING)
public class AgreementController extends BaseController {

    private static String HOST_APP_URL = PropUtils.getSystem(PropertiesConstants.HYJF_APP_URL);
    private static String HOST_WEB_URL = PropUtils.getSystem(PropertiesConstants.HYJF_WEB_URL);
    @Autowired
    private ProjectService projectService;
    @Autowired
    private CreditService creditService;
    @Autowired
    private PlanInfoService planInfoService;
    @Autowired
    private MyTenderService mytenderService;
    @Autowired
    private HjhPlanService hjhPlanService;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private AgreementService agreementService;

    /**
     * 汇盈金服出借咨询与管理服务协议
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.INVESTMENT_ADVISORY_AND_MANAGEMENT_SERVICES_ACTION)
    public ModelAndView investmentAdvisoryAndManagementServices(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.INVESTMENT_ADVISORY_AND_MANAGEMENT_SERVICES_ACTION);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.INVESTMENT_ADVISORY_AND_MANAGEMENT_SERVICES_PATH);
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.INVESTMENT_ADVISORY_AND_MANAGEMENT_SERVICES_ACTION);
        return modelAndView;
    }


    /**
     * 散标风险揭示书
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(AgreementDefine.CONFIRMATION_OF_INVESTMENT_RISK_ACTION)
    public ModelAndView confirmationOfInvestmentRisk(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.CONFIRMATION_OF_INVESTMENT_RISK_ACTION);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.CONFIRMATION_OF_INVESTMENT_RISK_PATH);
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.CONFIRMATION_OF_INVESTMENT_RISK_ACTION);
        return modelAndView;
    }

    /**
     * 散标-债权转让-债权转让协议(汇盈金服互联网金融服务平台债权转让协议):之前有过查询带值画面，目前web只有空JSP，之前查询逻辑先不删除
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.TRANSFER_OF_CREDITOR_RIGHT_ACTION)
    public ModelAndView transferOfCreditorRight(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CreditAssignedBean tenderCreditAssignedBean) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.TRANSFER_OF_CREDITOR_RIGHT_ACTION);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.TRANSFER_OF_CREDITOR_RIGHT_PATH);

        CreditResultBean creditResultBean = new CreditResultBean();
        Integer userId = null;
        try {
            userId = WebUtils.getUserId(request); // 用户ID
            if (userId != null && userId.intValue() != 0) {
                if (StringUtils.isEmpty(tenderCreditAssignedBean.getBidNid()) || StringUtils.isEmpty(tenderCreditAssignedBean.getCreditNid())
                        || StringUtils.isEmpty(tenderCreditAssignedBean.getCreditTenderNid()) || StringUtils.isEmpty(tenderCreditAssignedBean.getAssignNid())) {
                    creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
                    creditResultBean.setMsg("查看失败,参数不符");
                    creditResultBean.setData(null);
                    LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.TRANSFER_OF_CREDITOR_RIGHT_ACTION);
                    modelAndView.addObject("creditResult", creditResultBean);
                    return modelAndView;
                }
                Map<String, Object> creditContract = creditService.selectUserCreditContract(tenderCreditAssignedBean);
                creditResultBean.setResultFlag(CustomConstants.RESULT_SUCCESS);
                creditResultBean.setMsg("");
                creditResultBean.setData(creditContract);
            } else {
                LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "用户未登录");
                creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
                creditResultBean.setMsg("用户未登录");
                creditResultBean.setData(null);
            }
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "系统异常");
            creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
            creditResultBean.setMsg("系统异常");
            creditResultBean.setData(null);
        }

        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.TRANSFER_OF_CREDITOR_RIGHT_ACTION);
        modelAndView.addObject("creditResult", creditResultBean);
        return modelAndView;
    }


    /**
     * 汇添金出借计划服务协议详情页面
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.HTJ_INVESTMENT_PLANNING_SERVICES_ACTION)
    public ModelAndView htjInvestmentPlanningServices(HttpServletRequest request, HttpServletResponse response, PlanListBean form) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.HTJ_INVESTMENT_PLANNING_SERVICES_ACTION);
        /*ModelAndView modelAndView = new ModelAndView(AgreementDefine.HTJ_INVESTMENT_PLANNING_SERVICES_PATH);*/
        ModelAndView modelAndView = new ModelAndView();
        WebViewUser wuser = WebUtils.getUser(request);
        if (wuser == null) {
            modelAndView.addObject("message", "用户信息失效，请您重新登录。");
            return new ModelAndView(AgreementDefine.HTJ_INVESTMENT_PLANNING_SERVICES_PATH);
        }

        if (form.getAccedeOrderId() == null || "".equals(form.getAccedeOrderId())) {
            modelAndView.setViewName(AgreementDefine.HTJ_INVESTMENT_PLANNING_SERVICES_PATH);
            return modelAndView;
        }
        Integer userId = wuser.getUserId();
        //1基本信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accedeOrderId", form.getAccedeOrderId());
        params.put("userId", userId);
        List<PlanLockCustomize> recordList = planInfoService.selectUserProjectListCapital(params);

        if (recordList != null && recordList.size() > 0) {
            PlanLockCustomize planinfo = recordList.get(0);
            modelAndView.addObject("planinfo", planinfo);
            BigDecimal accedeAccount = new BigDecimal(planinfo.getAccedeAccount());
            BigDecimal lockPeriod = new BigDecimal(planinfo.getDebtLockPeriod());
            BigDecimal expectApr = new BigDecimal(planinfo.getExpectApr()).divide(new BigDecimal("100"));
            BigDecimal repayAccountYes = new BigDecimal(planinfo.getRepayAccountYes());
            //2资产统计
            HashMap<String, Object> map = planInfoService.selectPlanInfoSum(form.getAccedeOrderId());
            BigDecimal investSum = BigDecimal.ZERO;
            if (map != null) {
                //当前持有资产总计
                investSum = new BigDecimal(map.get("investSum") + "");
                modelAndView.addObject("investSum", investSum);
            }
            //预计到期收益 加入计划金额*计划期限*计划收益率/12；
            BigDecimal expectIntrest = accedeAccount.multiply(lockPeriod).multiply(expectApr).divide(new BigDecimal("12"), 2, BigDecimal.ROUND_DOWN);
            modelAndView.addObject("expectIntrest", expectIntrest);
            //回款总金额
            modelAndView.addObject("repayAccountYes", repayAccountYes);
            modelAndView.addObject("factIntrest", planinfo.getRepayInterestYes());
            Map<String, Object> params1 = new HashMap<String, Object>();
            params1.put("planOrderId", form.getAccedeOrderId());
            //params1.put("type", 1);
            //3持有项目列表
            if (form.getType() != null && form.getType().equals("1")) {
                //锁定中
                //TODO  不要分页  查两次 合并
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
                modelAndView.setViewName(AgreementDefine.PROJECT_DETAIL_LOCK_PATH);
            } else if (form.getType() != null && form.getType().equals("2")) {
                params1.put("status", "11");
                //已退出
                modelAndView.setViewName(AgreementDefine.PROJECT_DETAIL_EXIT_PATH);
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
                //申购中
                modelAndView.setViewName(AgreementDefine.PROJECT_DETAIL_PATH);
            }
        }
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.HTJ_INVESTMENT_PLANNING_SERVICES_ACTION);
        return modelAndView;
    }

    /**
     * 汇盈金服互联网金融服务平台居间服务协议--散标
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.INITBORROW_INTERMEDIARY_SERVICES_ACTION)
    public ModelAndView initBorrowIntermediaryServices(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CreditAssignedBean tenderCreditAssignedBean) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.INTERMEDIARY_SERVICES_ACTION);
        // ModelAndView modelAndView = new ModelAndView(AgreementDefine.INTERMEDIARY_BORROW_SERVICES_PATH);
        //新版《居间服务借款协议》
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.INTERMEDIARY_SERVICES_PATH);
        CreditResultBean creditResultBean = new CreditResultBean();
        Integer userId = null;
        String borrowNid = request.getParameter("borrowNid");
        if (StringUtils.isNotBlank(borrowNid)) {
            Borrow borrow = borrowService.getBorrowByNid(borrowNid);
            if (borrow != null) {
                if (StringUtils.isNotBlank(borrow.getPlanNid())) {
                    modelAndView = new ModelAndView(AgreementDefine.INTERMEDIARY_SERVICES_PATH);
                }
            }
        }
        try {

            userId = WebUtils.getUserId(request); // 用户ID
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
                Map<String, Object> creditContract = creditService.selectUserCreditContract(tenderCreditAssignedBean);
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
     * 汇盈金服互联网金融服务平台居间服务协议
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.INTERMEDIARY_SERVICES_ACTION)
    public ModelAndView intermediaryServices(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CreditAssignedBean tenderCreditAssignedBean) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.INTERMEDIARY_SERVICES_ACTION);

        ModelAndView modelAndView = new ModelAndView(AgreementDefine.INTERMEDIARY_SERVICES_PATH);
        CreditResultBean creditResultBean = new CreditResultBean();
        Integer userId = null;
        try {
            userId = WebUtils.getUserId(request); // 用户ID
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
                Map<String, Object> creditContract = creditService.selectUserCreditContract(tenderCreditAssignedBean);
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
     * 出借协议《借款协议》
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.DIARY_SERVICES_ACTION)
    public ModelAndView diaryServices(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CreditAssignedBean tenderCreditAssignedBean) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.DIARY_SERVICES_ACTION);
        System.out.println(HOST_APP_URL);
        System.out.println(HOST_WEB_URL);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.DIARY_SERVICES_PATH);
        CreditResultBean creditResultBean = new CreditResultBean();
        Integer userId = null;
        try {
            userId = WebUtils.getUserId(request); // 用户ID
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
                Map<String, Object> creditContract = creditService.selectUserCreditContract(tenderCreditAssignedBean);
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
     * 出借协议《借款协议》--查询有填充值
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = AgreementDefine.HJH_DIARY_AGREEMENT)
    public ModelAndView hjhDiaryAgreement(HttpServletRequest request, @ModelAttribute UserInvestListBean form) {
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.DIARY_SERVICES_PATH);
        if (StringUtils.isEmpty(form.getBorrowNid()) || StringUtils.isEmpty(form.getNid())) {
            modelAndView = new ModelAndView("error/systemerror");
            modelAndView.addObject("message", "标的信息不存在，请重新查证。");
            return modelAndView;
        }
        // 查询借款人用户名
        BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
        // 借款编码
        borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
        List<BorrowCustomize> recordList = mytenderService.selectBorrowList(borrowCommonCustomize);
        modelAndView.addObject("borrowNid", form.getBorrowNid());
        modelAndView.addObject("nid", form.getNid());
        if (recordList.size() > 0) {
            modelAndView.addObject("record", recordList.get(0));
            // 借款人用户名
            int userIds = recordList.get(0).getUserId();
            UsersInfo userInfo = planInfoService.getUsersInfoByUserId(userIds);
            modelAndView.addObject("borrowUsername", userInfo.getTruename());
            modelAndView.addObject("idCard", userInfo.getIdcard());
            modelAndView.addObject("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间
            /*BorrowRecoverExample example = new BorrowRecoverExample();
            BorrowRecoverExample.Criteria borrowRecoverCra = example.createCriteria();
            borrowRecoverCra.andAccedeOrderIdEqualTo(form.getAssetNumber());
            List<BorrowRecover> recovers = mytenderService.selectBorrowRecover(example );
            if(recovers.size()>0){
                modelAndView.addObject("recoverAccount", recovers.get(0).getRecoverAccount());
            }else {
                modelAndView.addObject("recoverAccount", null);
            }*/
            modelAndView.addObject("recoverAccount", form.getAccount());
            if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
                // 最后一笔的放款完成时间 (协议签订日期)
                modelAndView.addObject("recoverTime", recordList.get(0).getRecoverLastTime());
            } else {
                // 设置为满标时间
                modelAndView.addObject("recoverTime", recordList.get(0).getReverifyTime());
            }
        }

        // 用户ID
        Integer userId = WebUtils.getUserId(request);
        form.setUserId(userId.toString());
        // 用户出借列表
        List<WebUserInvestListCustomize> tzList = mytenderService.selectUserInvestList(form, 0, 100);
        if (tzList != null && tzList.size() > 0) {
            modelAndView.addObject("userInvest", tzList.get(0));
        }
        return modelAndView;
    }

    /**
     * 江西银行网络交易资金账户服务三方协议
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.JX_BANK_SERVICE_ACTION)
    public ModelAndView jxBankService(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.JX_BANK_SERVICE_ACTION);
        WebViewUser user = WebUtils.getUser(request);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.JX_BANK_SERVICE_PATH);
        modelAndView.addObject("user", user);
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.JX_BANK_SERVICE_ACTION);
        return modelAndView;
    }


    /**
     * 汇盈金服互联网金融服务平台隐私条款
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.PRIVACY_CLAUSE_ACTION)
    public ModelAndView privacyClause(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.PRIVACY_CLAUSE_ACTION);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.PRIVACY_CLAUSE_PATH);
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.PRIVACY_CLAUSE_ACTION);
        return modelAndView;
    }

    /**
     * 汇盈金服注册协议
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.REGISTRATION_PROTOCOL_ACTION)
    public ModelAndView registrationProtocol(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.REGISTRATION_PROTOCOL_ACTION);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.REGISTRATION_PROTOCOL_PATH);
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.REGISTRATION_PROTOCOL_ACTION);
        return modelAndView;
    }

    /**
     * (融通宝)温州金融资产交易中心股份有限公司个人会员服务协议
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.INDIVIDUAL_MEMBER_SERVICES_ACTION)
    public ModelAndView individualMemberServices(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.INDIVIDUAL_MEMBER_SERVICES_ACTION);
        String borrowNid = request.getParameter("borrowNid");
        WebProjectDetailCustomize borrow = new WebProjectDetailCustomize();
        borrow = this.projectService.selectProjectDetail(borrowNid);
        ModelAndView modelAndView = new ModelAndView();

        if (borrow != null && borrow.getBorrowPublisher() != null && borrow.getBorrowPublisher().equals("中商储")) {
            modelAndView = new ModelAndView("agreement/type-open-contract-zsc");
        } else {
            modelAndView = new ModelAndView("agreement/type-rtb-open-contract");
        }
        modelAndView.addObject("projectDeatil", borrow);
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.INDIVIDUAL_MEMBER_SERVICES_ACTION);
        return modelAndView;
    }

    /**
     * (融通宝)汇盈金服平台服务协议
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.PLATFORM_SERVICE_AGREEMENT_ACTION)
    public ModelAndView platformServiceAgreement(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.PLATFORM_SERVICE_AGREEMENT_ACTION);
        ModelAndView modelAndView = new ModelAndView("agreement/type-rtb-contract");
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.PLATFORM_SERVICE_AGREEMENT_ACTION);
        return modelAndView;
    }

    /**
     * 开户时 用户授权协议
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.USER_LICENSE_AGREEMENT)
    public ModelAndView userLicenseAgreement(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.USER_LICENSE_AGREEMENT);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.USER_LICENSE_AGREEMENT_PATH);
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.USER_LICENSE_AGREEMENT);
        return modelAndView;
    }


    /**
     * 资产管理-计划-计划协议-汇添金出借计划服务协议（查看协议）
     *
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = AgreementDefine.PLAN_INFO_AGREEMENT, method = RequestMethod.GET)
    public ModelAndView searchUserInvestList(String planNid, String planOrderId, HttpServletRequest request,
                                             HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.PLAN_INFO_AGREEMENT_PATH);
        WebViewUser wuser = WebUtils.getUser(request);
        if (wuser == null) {
            modelAndView.addObject("message", "用户信息失效，请您重新登录。");
            return new ModelAndView("error/systemerror");
        }
        Integer userId = wuser.getUserId();
        if (planNid == null || "".equals(planNid.trim()) || planOrderId == null || "".equals(planOrderId.trim())) {
            modelAndView = new ModelAndView("error/systemerror");
            modelAndView.addObject("message", "计划编号或计划订单号不存在，请重新查证。");
            return modelAndView;
        }
        List<DebtPlan> debtPlanList = planInfoService.getPlanByPlanNid(planNid);
        if (debtPlanList != null && debtPlanList.size() > 0) {
            DebtPlan debtPlan = debtPlanList.get(0);
            if (debtPlan.getFullExpireTime() != null && debtPlan.getFullExpireTime() != 0) {
                modelAndView.addObject("fullDate", GetDate.timestamptoStrYYYYMMDD(debtPlan.getFullExpireTime()));
            } else {
                modelAndView.addObject("fullDate", GetDate.timestamptoStrYYYYMMDD(debtPlan.getBuyEndTime()));
            }
            modelAndView.addObject("debtPlan", debtPlan);
        }
        //1基本信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("accedeOrderId", planOrderId);
        params.put("userId", userId);
        UsersInfo userInfo = planInfoService.getUsersInfoByUserId(userId);
        modelAndView.addObject("userInfo", userInfo);
        List<PlanLockCustomize> recordList = planInfoService.selectUserProjectListCapital(params);
        if (recordList != null && recordList.size() > 0) {
            PlanLockCustomize planinfo = recordList.get(0);
            modelAndView.addObject("planinfo", planinfo);
        }
        return modelAndView;
    }


    /**
     * 资产管理-计划-转让记录-持有项目列表-出借协议
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = AgreementDefine.PLAN_CONTENT_DETAIL_AGREEMENT)
    public ModelAndView goDetail(HttpServletRequest request, @ModelAttribute UserInvestListBean form) {
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.PLAN_CONTENT_DETAIL_AGREEMENT_PATH);
        if (form.getBorrowNid() == null || "".equals(form.getBorrowNid().trim())) {
            modelAndView = new ModelAndView("error/systemerror");
            modelAndView.addObject("message", "标的信息不存在，请重新查证。");
            return modelAndView;
        }
        // 查询借款人用户名
        DebtBorrowCommonCustomize debtBorrowCommonCustomize = new DebtBorrowCommonCustomize();
        // 借款编码
        debtBorrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
        List<DebtBorrowCustomize> recordList = planInfoService.selectBorrowList(debtBorrowCommonCustomize);
        if (recordList.size() != 1) {
            modelAndView = new ModelAndView("error/systemerror");
            modelAndView.addObject("message", "标的信息异常，请重新查证。");
            return modelAndView;
        }
        modelAndView.addObject("borrowNid", form.getBorrowNid());// 标的号
        modelAndView.addObject("nid", form.getNid());// 出借标示
        modelAndView.addObject("jkrUsername", recordList.get(0).getUsername());// 借款人用户名
        modelAndView.addObject("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间

        // 如果是分期还款，查询分期信息
        String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
        if (borrowStyle != null) {
            if ("month".equals(borrowStyle) || "principal".equals(borrowStyle) || "endmonth".equals(borrowStyle)) {
                ProjectRepayListBean bean = new ProjectRepayListBean();
                bean.setUserId(WebUtils.getUserId(request).toString());
                bean.setBorrowNid(form.getBorrowNid());
                bean.setNid(form.getNid());
                int recordTotal = this.planInfoService.countProjectRepayPlanRecordTotal(bean);
                if (recordTotal > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
                    List<WebProjectRepayListCustomize> fqList = planInfoService.selectProjectRepayPlanList(bean,
                            paginator.getOffset(), paginator.getLimit());
                    modelAndView.addObject("paginator", paginator);
                    modelAndView.addObject("fqList", fqList);
                } else {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
                    modelAndView.addObject("paginator", paginator);
                    modelAndView.addObject("fqList", "");
                }
            }
        }
        // 用户ID
        Integer userId = WebUtils.getUserId(request);
        form.setUserId(userId.toString());
        createUserInvestPage(request, modelAndView, form);
        modelAndView.addObject("requestBean", form);
        modelAndView.addObject("phpHost", PropUtils.getSystem("hyjf.web.host.php"));// php路径，用于用php接口导出pdf
        return modelAndView;
    }

    /**
     * 创建用户出借分页信息
     *
     * @param request
     * @param info
     * @param form
     */
    private void createUserInvestPage(HttpServletRequest request, ModelAndView modelAndView, UserInvestListBean form) {

        int recordTotal1 = this.planInfoService.countUserInvestRecordTotal(form);
        if (recordTotal1 > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal1);
            List<WebUserInvestListCustomize> recordList = planInfoService.selectUserInvestList(form,
                    paginator.getOffset(), paginator.getLimit());
            modelAndView.addObject("paginator", paginator);
            modelAndView.addObject("userinvestlist", recordList);
        } else {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal1);
            modelAndView.addObject("paginator", paginator);
            modelAndView.addObject("userinvestlist", "");
        }

    }

    /**
     * 账户中心-债权管理-订单详情 债转被出借的协议
     *
     * @param hzr
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = AgreementDefine.PLAN_CREDIT_CONTRACT)
    public ModelAndView planUserCreditContract(HttpServletRequest request, HttpServletResponse response,
                                               @ModelAttribute CreditAssignedBean tenderCreditAssignedBean) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.PLAN_CREDIT_CONTRACT);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.TRANSFER_OF_CREDITOR_PATH);
        CreditResultBean creditResultBean = new CreditResultBean();
        Integer userId = null;
        try {
            userId = WebUtils.getUserId(request); // 用户ID
            if (userId != null && userId.intValue() != 0) {
                if (StringUtils.isEmpty(tenderCreditAssignedBean.getCreditNid())
                        || StringUtils.isEmpty(tenderCreditAssignedBean.getCreditTenderNid())) {
                    creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
                    creditResultBean.setMsg("查看失败,参数不符");
                    creditResultBean.setData(null);
                    modelAndView.addObject("creditResult", creditResultBean);
                    return modelAndView;
                }
                Map<String, Object> creditContract = creditService.selectUserPlanCreditContract(tenderCreditAssignedBean);
                creditResultBean.setResultFlag(CustomConstants.RESULT_SUCCESS);
                creditResultBean.setMsg("");
                creditResultBean.setData(creditContract);
            } else {
                LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "用户未登录");
                creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
                creditResultBean.setMsg("用户未登录");
                creditResultBean.setData(null);
            }
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "系统异常");
            creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
            creditResultBean.setMsg("系统异常");
            creditResultBean.setData(null);
        }
        modelAndView.addObject("creditResult", creditResultBean);
        return modelAndView;
    }


    /**
     * 资产管理-计划-计划协议-汇计划出借计划服务协议（查看协议）
     *
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = AgreementDefine.HJH_INFO_AGREEMENT)
    public ModelAndView hjhInfo(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.HJH_INFO_AGREEMENT_PATH);
        String planNid = request.getParameter("planNid");
        String accedeOrderId = request.getParameter("accedeOrderId");

        if (StringUtils.isNotEmpty(accedeOrderId) && StringUtils.isNotEmpty(planNid)) {
            WebViewUser wuser = WebUtils.getUser(request);
            if (wuser == null) {
                modelAndView.addObject("message", "用户信息失效，请您重新登录。");
                return new ModelAndView("error/systemerror");
            }
            Integer userId = wuser.getUserId();
            //1基本信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("accedeOrderId", accedeOrderId);
            params.put("userId", userId);
            UsersInfo userInfo = planInfoService.getUsersInfoByUserId(userId);
            modelAndView.addObject("username", wuser.getUsername());//用户名
            modelAndView.addObject("userInfo", userInfo);
            UserHjhInvistDetailCustomize userHjhInvistDetailCustomize = hjhPlanService.selectUserHjhInvistDetail(params);

            modelAndView.addObject("userHjhInvistDetail", userHjhInvistDetailCustomize);
        } else {
            modelAndView.addObject("userHjhInvistDetail", null);
        }

        return modelAndView;
    }


    /**
     * 资产管理-计划-转让记录-持有项目列表-出借协议
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = AgreementDefine.HJH_BORROW_AGREEMENT)
    public ModelAndView hjhBorrowAgreement(HttpServletRequest request, @ModelAttribute UserInvestListBean form) {
       /* ModelAndView modelAndView = new ModelAndView(AgreementDefine.HJH_BORROW_AGREEMENT_PATH);*/
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.INTERMEDIARY_SERVICES_PATH);
        if (StringUtils.isEmpty(form.getBorrowNid()) || StringUtils.isEmpty(form.getNid())) {
            modelAndView = new ModelAndView("error/systemerror");
            modelAndView.addObject("message", "标的信息不存在，请重新查证。");
            return modelAndView;
        }
        // 查询借款人用户名
        BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
        // 借款编码
        borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
        List<BorrowCustomize> recordList = mytenderService.selectBorrowList(borrowCommonCustomize);
        if (recordList.size() > 0) {
            modelAndView.addObject("record", recordList.get(0));
            // 借款人用户名
            int userIds = recordList.get(0).getUserId();
            UsersInfo userInfo = planInfoService.getUsersInfoByUserId(userIds);
            String borrowUsername = userInfo.getTruename();
            WebViewUser loInfo = WebUtils.getUser(request);
            if (loInfo != null && !((loInfo.getUserId() + "").equals(userInfo.getUserId()))) {
                borrowUsername = borrowUsername.substring(0, borrowUsername.length() - 1) + "*";
            }
            modelAndView.addObject("borrowUsername", borrowUsername);
            modelAndView.addObject("idCard", userInfo.getIdcard());
            modelAndView.addObject("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间
            modelAndView.addObject("borrowNid", form.getBorrowNid());
            modelAndView.addObject("nid", form.getNid());

            modelAndView.addObject("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间
            if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
                // 最后一笔的放款完成时间 (协议签订日期)
                modelAndView.addObject("recoverTime", recordList.get(0).getRecoverLastTime());
            } else {
                // 设置为满标时间
                modelAndView.addObject("recoverTime", recordList.get(0).getReverifyTime());
            }
            // 用户ID
            Integer userId = WebUtils.getUserId(request);
            form.setUserId(userId.toString());
            // 用户出借列表
            List<WebUserInvestListCustomize> tzList = mytenderService.selectUserInvestList(form, 0, 100);
            if (tzList != null && tzList.size() > 0) {
                WebUserInvestListCustomize userInvest = tzList.get(0);
                if (loInfo != null && !((loInfo.getUserId() + "").equals(userInvest.getUserId()))) {
                    userInvest.setRealName(userInvest.getRealName().substring(0, userInvest.getRealName().length() - 1) + "*");
                    userInvest.setIdCard(userInvest.getIdCard().substring(0, userInvest.getIdCard().length() - 4) + "****");
                }
                modelAndView.addObject("userInvest", userInvest);
            }
            // 如果是分期还款，查询分期信息
            String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
            if (borrowStyle != null) {
                //计算历史回报
                BigDecimal earnings = new BigDecimal("0");
                // 收益率

                String borrowAprString = StringUtils.isEmpty(recordList.get(0).getBorrowApr()) ? "0.00" : recordList.get(0).getBorrowApr().replace("%", "");
                BigDecimal borrowApr = new BigDecimal(borrowAprString);
                //出借金额
                String accountString = StringUtils.isEmpty(recordList.get(0).getAccount()) ? "0.00" : recordList.get(0).getAccount().replace(",", "");
                BigDecimal account = new BigDecimal(accountString);
                // 周期
                String borrowPeriodString = StringUtils.isEmpty(recordList.get(0).getBorrowPeriod()) ? "0" : recordList.get(0).getBorrowPeriod();
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(borrowPeriodString);
                borrowPeriodString = m.replaceAll("").trim();
                Integer borrowPeriod = Integer.valueOf(borrowPeriodString);
                if (StringUtils.equals("endday", borrowStyle)) {
                    // 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷365*锁定期；
                    earnings = DuePrincipalAndInterestUtils.getDayInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                } else {
                    // 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
                    earnings = DuePrincipalAndInterestUtils.getMonthInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);

                }
                modelAndView.addObject("earnings", earnings);
                if ("month".equals(borrowStyle) || "principal".equals(borrowStyle)
                        || "endmonth".equals(borrowStyle)) {
                    ProjectRepayListBean bean = new ProjectRepayListBean();
                    bean.setUserId(WebUtils.getUserId(request).toString());
                    bean.setBorrowNid(form.getBorrowNid());
                    bean.setNid(form.getNid());
                    int recordTotal = this.mytenderService.countProjectRepayPlanRecordTotal(bean);
                    if (recordTotal > 0) {
                        Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
                        List<WebProjectRepayListCustomize> fqList = mytenderService.selectProjectRepayPlanList(
                                bean, paginator.getOffset(), paginator.getLimit());
                        modelAndView.addObject("paginator", paginator);
                        modelAndView.addObject("repayList", fqList);
                    } else {
                        Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
                        modelAndView.addObject("paginator", paginator);
                        modelAndView.addObject("repayList", "");
                    }
                }
            }
        }
        return modelAndView;
    }


    /**
     * 汇盈金服授权协议
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.HYJF_AUTH_ACTION)
    public ModelAndView getAuthAgreementServices(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.HYJF_AUTH_ACTION);
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.HYJF_AUTH_ACTION_PATH);
        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.HYJF_AUTH_ACTION);
        return modelAndView;
    }


    /**
     * 汇盈金服互联网金融服务平台债权转让协议(汇计划债转专用)
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(AgreementDefine.TRANSFER_OF_HJH_CREDITOR_RIGHT_ACTION)
    public ModelAndView transferOfHJHCreditorRight(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CreditAssignedBean tenderCreditAssignedBean) {
        LogUtil.startLog(AgreementDefine.THIS_CLASS, AgreementDefine.TRANSFER_OF_HJH_CREDITOR_RIGHT_ACTION);
        //方法进来了且有值
        ModelAndView modelAndView = new ModelAndView(AgreementDefine.TRANSFER_OF_HJH_CREDITOR_RIGHT_PATH);//展示协议画面共用

        CreditResultBean creditResultBean = new CreditResultBean();//返回消息内容实体
        Integer userId = null;
        try {
            userId = WebUtils.getUserId(request); // 用户ID

            if (userId != null && userId.intValue() != 0) {
                if (StringUtils.isEmpty(tenderCreditAssignedBean.getBidNid()) || StringUtils.isEmpty(tenderCreditAssignedBean.getCreditNid())
                        || StringUtils.isEmpty(tenderCreditAssignedBean.getCreditTenderNid()) || StringUtils.isEmpty(tenderCreditAssignedBean.getAssignNid())) {

                    creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
                    creditResultBean.setMsg("查看失败,参数不符");
                    creditResultBean.setData(null);
                    LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.TRANSFER_OF_HJH_CREDITOR_RIGHT_ACTION);

                    modelAndView.addObject("creditResult", creditResultBean);
                    return modelAndView;
                }
                //为下载画面准备数据
                modelAndView.addObject("borrowNid", tenderCreditAssignedBean.getBidNid());
                modelAndView.addObject("creditNid", tenderCreditAssignedBean.getCreditNid());
                modelAndView.addObject("creditTenderNid", tenderCreditAssignedBean.getCreditTenderNid());
                modelAndView.addObject("assignNid", tenderCreditAssignedBean.getAssignNid());
                Map<String, Object> creditContract = creditService.selectHJHUserCreditContract(tenderCreditAssignedBean);
                creditResultBean.setResultFlag(CustomConstants.RESULT_SUCCESS);
                creditResultBean.setMsg("");
                creditResultBean.setData(creditContract);//部署查询数据

            } else {
                LogUtil.infoLog(this.getClass().getName(), "transferOfHJHCreditorRight", "用户未登录");
                creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
                creditResultBean.setMsg("用户未登录");
                creditResultBean.setData(null);
            }

        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "transferOfHJHCreditorRight", "系统异常");
            creditResultBean.setResultFlag(CustomConstants.RESULT_FAIL);
            creditResultBean.setMsg("系统异常");
            creditResultBean.setData(null);
        }

        LogUtil.endLog(AgreementDefine.THIS_CLASS, AgreementDefine.TRANSFER_OF_HJH_CREDITOR_RIGHT_ACTION);
        modelAndView.addObject("creditResult", creditResultBean);
        return modelAndView;
    }

    /**
     * 获得 协议模板pdf显示地址
     *
     * @return
     */
    @RequestMapping(value = AgreementDefine.GOAGREEMENT_PDF, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
//    @ResponseBody
    public ModelAndView gotAgreementPdf(@RequestParam String aliasName) throws IOException, ServletException {
        LogUtil.startLog(AgreementController.class.getName(), AgreementDefine.GOAGREEMENT_PDF);
        String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.web.host"));

        if (StringUtils.isEmpty(aliasName)) {
            return new ModelAndView(new RedirectView(fileDomainUrl + "error/404"));
        }

        //是否在枚举中有定义
        String displayName = ProtocolEnum.getDisplayName(aliasName);
        if (org.apache.commons.lang3.StringUtils.isEmpty(displayName)) {
            return new ModelAndView(new RedirectView(fileDomainUrl + "error/404"));
        }

        String protocolId = RedisUtils.get(RedisConstants.PROTOCOL_TEMPLATE_ALIAS + aliasName);
        if (StringUtils.isEmpty(protocolId)) {
            boolean flag = agreementService.setRedisProtocolTemplate(displayName);
            if (!flag) {
                return new ModelAndView(new RedirectView(fileDomainUrl + "error/404"));
            }
            protocolId = RedisUtils.get(RedisConstants.PROTOCOL_TEMPLATE_ALIAS + aliasName);
        }

        String pdfUrl = null;
        try {
            pdfUrl = agreementService.getAgreementPdf(protocolId);
        } catch (Exception e) {
            return new ModelAndView(new RedirectView(fileDomainUrl + "error/404"));
        }
        LogUtil.endLog(AgreementController.class.getName(), AgreementDefine.GOAGREEMENT_PDF);
        return new ModelAndView(new RedirectView(fileDomainUrl + pdfUrl));
    }

    /**
     * 协议名称 动态获得
     *
     * @return
     */
    @RequestMapping(value = AgreementDefine.GET_DISPLAY_NAME_DYNAMIC, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public JSONObject getdisplayNameDynamic(){
        LogUtil.startLog(AgreementController.class.getName(), AgreementDefine.GET_DISPLAY_NAME_DYNAMIC);
        JSONObject jsonObject = null;

        jsonObject = JSONObject.parseObject(RedisUtils.get(RedisConstants.PROTOCOL_PARAMS));
        if (jsonObject == null) {
            jsonObject = new JSONObject();
            List<ProtocolTemplate> list = agreementService.getdisplayNameDynamic();
            if(CollectionUtils.isNotEmpty(list)){
                //是否在枚举中有定义
                for (ProtocolTemplate p : list) {
                    String protocolType = p.getProtocolType();
                    String alia = ProtocolEnum.getAlias(protocolType);
                    if (alia != null){
                        jsonObject.put(alia, p.getDisplayName());
                    }
                }
            }
            RedisUtils.set(RedisConstants.PROTOCOL_PARAMS,jsonObject.toString());
        }
        return jsonObject;
    }
}
