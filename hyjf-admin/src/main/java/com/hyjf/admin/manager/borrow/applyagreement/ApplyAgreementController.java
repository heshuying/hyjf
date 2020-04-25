package com.hyjf.admin.manager.borrow.applyagreement;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.app.maintenance.banner.AppBannerController;
import com.hyjf.admin.maintenance.config.ConfigController;
import com.hyjf.admin.manager.borrow.borrow.BorrowController;
import com.hyjf.admin.manager.borrow.borrow.BorrowService;
import com.hyjf.admin.manager.borrow.borrowrecover.BorrowRecoverService;
import com.hyjf.admin.manager.borrow.borrowrepayment.BorrowRepaymentService;
import com.hyjf.admin.manager.borrow.credittender.CreditTenderDefine;
import com.hyjf.admin.manager.borrow.credittender.CreditTenderService;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractBean;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.FavFTPUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.util.SFTPParameter;
import com.hyjf.common.zip.ZIPGenerator;
import com.hyjf.mybatis.model.auto.ApplyAgreement;
import com.hyjf.mybatis.model.auto.ApplyAgreementExample;
import com.hyjf.mybatis.model.auto.ApplyAgreementInfo;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverPlan;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.HjhDebtCreditRepay;
import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import com.hyjf.mybatis.model.customize.admin.BorrowRepayAgreementCustomize;

/**
 * @package com.hyjf.admin.manager.borrow.applyagreement.AdminPermissions
 * @author zha daojian
 * @version V1.0  
 */
@Controller
@RequestMapping(value = ApplyAgreementDefine.REQUEST_MAPPING)
public class ApplyAgreementController extends BaseController {

	@Autowired
	private ApplyAgreementService applyAgreementService;
	@Autowired
    private BorrowService borrowService;
	@Autowired
    protected BorrowRecoverService borrowRecoverService;
	
	@Autowired
	protected BorrowRepaymentService borrowRepaymentService;
	
	@Autowired
    private FddGenerateContractService fddGenerateContractService;
	@Autowired
    private CreditTenderService creditTenderService;
	@Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
	private String timeString = ApplyAgreementDefine.DF;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ApplyAgreementDefine.INIT)
	@RequiresPermissions(ApplyAgreementDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response, 
			@ModelAttribute ApplyAgreementBean form) {
		LogUtil.startLog(ApplyAgreementController.class.toString(), ApplyAgreementDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(ApplyAgreementDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form, null);
		LogUtil.endLog(ApplyAgreementController.class.toString(), ApplyAgreementDefine.INIT);
		return modeAndView;
	}
	/**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ApplyAgreementDefine.SEARCH_ACTION)
    @RequiresPermissions(ApplyAgreementDefine.PERMISSIONS_SEARCH)
    public ModelAndView searchAction(HttpServletRequest request, ApplyAgreementBean form) {
        LogUtil.startLog(BorrowController.class.toString(), ApplyAgreementDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(ApplyAgreementDefine.LIST_PATH);       
        // 创建分页
        this.createPage(request, modelAndView, form, null);
        LogUtil.endLog(BorrowController.class.toString(), ApplyAgreementDefine.INIT);
        return modelAndView;
    }
	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modeAndView, ApplyAgreementBean borrowCreditRepayBean, String export) {
	    ApplyAgreementExample applyAgreement = new ApplyAgreementExample();
	    ApplyAgreementExample.Criteria criteria = applyAgreement.createCriteria();
	    if(StringUtils.isNotEmpty(borrowCreditRepayBean.getBorrowNidSrch())){
	        criteria.andBorrowNidEqualTo(borrowCreditRepayBean.getBorrowNidSrch());
	    }
	    if(StringUtils.isNotEmpty(borrowCreditRepayBean.getBorrowPeriod())){
	        criteria.andRepayPeriodEqualTo(Integer.valueOf(borrowCreditRepayBean.getBorrowPeriod()));
	    }
	    if(StringUtils.isNotEmpty(borrowCreditRepayBean.getTimeStartSrch()) && 
	            StringUtils.isNotEmpty(borrowCreditRepayBean.getTimeEndSrch())){
            criteria.andCreateTimeBetween(GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayStart(borrowCreditRepayBean.getTimeStartSrch())), GetDate.strYYYYMMDDHHMMSS2Timestamp(GetDate.getDayEnd(borrowCreditRepayBean.getTimeEndSrch())));
        }
		Integer count = this.applyAgreementService.countApplyAgreement(applyAgreement);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(borrowCreditRepayBean.getPaginatorPage(), count);
			applyAgreement.setLimitStart(paginator.getOffset());
			if (export != null && !export.equals("") && !export.equals("undefind")) {
			    applyAgreement.setLimitEnd(9999999);
			} else {
			    applyAgreement.setLimitEnd(paginator.getLimit());
			}
			List<ApplyAgreement> recordList = this.applyAgreementService.selectApplyAgreement(applyAgreement);
			borrowCreditRepayBean.setPaginator(paginator);
			modeAndView.addObject("recordList", recordList);
		}
		modeAndView.addObject(ApplyAgreementDefine.BORROW_FORM, borrowCreditRepayBean);
	}
	
	/**
     * 详细画面
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(value = ApplyAgreementDefine.INFO_ACTION)
    public ModelAndView repayInfoAction(HttpServletRequest request, HttpServletResponse response, BorrowRepayAgreementBean form) {
        LogUtil.startLog(ApplyAgreementController.class.toString(), ApplyAgreementDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(ApplyAgreementDefine.INFO_PATH);
        this.createInfoPage(request, modelAndView, form);
        LogUtil.endLog(ApplyAgreementController.class.toString(), ApplyAgreementDefine.INFO_ACTION);
        return modelAndView;
    }
    
    /**
     * 删除
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(ApplyAgreementDefine.DELETE_ACTION)
    public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
        LogUtil.startLog(ConfigController.class.toString(), ApplyAgreementDefine.DELETE_ACTION);

        ModelAndView modelAndView = new ModelAndView(ApplyAgreementDefine.RE_LIST_PATH);
        // 解析json字符串
        List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
        this.applyAgreementService.deleteRecord(recordList);
        LogUtil.endLog(AppBannerController.class.toString(), ApplyAgreementDefine.DELETE_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modeAndView
     * @param form
     */
    private void createInfoPage(HttpServletRequest request, ModelAndView modeAndView, BorrowRepayAgreementBean form) {
        String borrowNid = form.getBorrowNidSrch();
        BorrowRepayAgreementCustomize borrowRepayAgreementCustomize = new BorrowRepayAgreementCustomize();
        BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
        List<BorrowRepayAgreementCustomize> recordList = new ArrayList<BorrowRepayAgreementCustomize>();
        if(StringUtils.isEmpty(borrowNid)){
            modeAndView.addObject("recordList", recordList);
            return;
        }else{
            borrowRepayAgreementCustomize.setBorrowNidSrch((form.getBorrowNidSrch()));
            borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
        }
        if(StringUtils.isNotEmpty(form.getTimeStartSrch())){
           
            borrowRepayAgreementCustomize.setTimeStartSrch(String.valueOf(form.getTimeStartSrch()));
        }
        if(StringUtils.isNotEmpty(form.getTimeEndSrch())){
            borrowRepayAgreementCustomize.setTimeEndSrch(String.valueOf(form.getTimeEndSrch()));
        }
        List<BorrowCustomize> borrowList = borrowService.selectBorrowList(borrowCommonCustomize);
        if(borrowList!=null && borrowList.size()>0){
            BorrowCustomize borrow = borrowList.get(0);
            /*if("1".equals(borrow.getRepayFullStatus())){//只有状态是还款成功的标地才能申请协议。
                modeAndView.addObject("recordList", recordList);
                return;
            }*/
            String borrowStyle =  borrow.getBorrowStyle();
           // 是否分期(true:分期, false:不分期)
            boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                    || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
            if(isMonth){//分期
                Long count = this.applyAgreementService.countBorrowRepayPlan(borrowRepayAgreementCustomize);
                if (count != null && count > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(count+""));
                    borrowRepayAgreementCustomize.setLimitStart(paginator.getOffset());
                    borrowRepayAgreementCustomize.setLimitEnd(paginator.getLimit());
                    borrowRepayAgreementCustomize.setRepayStatus("1");
                    form.setPaginator(paginator);
                    recordList = this.applyAgreementService.selectBorrowRepayPlan(borrowRepayAgreementCustomize);
                }
            }else{
                Long count = this.applyAgreementService.countBorrowRepay(borrowRepayAgreementCustomize);
                if (count != null && count > 0) {
                    Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(count+""));
                    borrowRepayAgreementCustomize.setLimitStart(paginator.getOffset());
                    borrowRepayAgreementCustomize.setLimitEnd(paginator.getLimit());
                    borrowRepayAgreementCustomize.setRepayStatus("1");
                    form.setPaginator(paginator);
                    recordList = this.applyAgreementService.selectBorrowRepay(borrowRepayAgreementCustomize);
                }
            }
            modeAndView.addObject("recordList", recordList); 
        }
        modeAndView.addObject(ApplyAgreementDefine.BORROW_FORM, form);

    }
    /**
     * 垫付债转协议生成
     * @param request
     * @param response
     * @param form
     * @return
     */
   // @Token(check = true, forward = ApplyAgreementDefine.TOKEN_INIT_PATH)
    @RequestMapping(value = ApplyAgreementDefine.ADD_ACTION)
    public ModelAndView updateAction(HttpServletRequest request, RedirectAttributes attr, BorrowRepayAgreementBean form) {
        LogUtil.startLog(ApplyAgreementController.class.toString(), ApplyAgreementDefine.ADD_ACTION);
        ModelAndView modelAndView = new ModelAndView(
                ApplyAgreementDefine.RESULT_PATH);
        modelAndView.addObject(ApplyAgreementDefine.SUCCESS,
                ApplyAgreementDefine.SUCCESS);
        String ids = request.getParameter("ids");
        List<String> recordList = null;
        try {
            recordList = JSONArray.parseArray(ids, String.class);
        } catch (Exception e) {
            ids = "['"+ids+"']";//直接从管理页面，重新签署
            recordList = JSONArray.parseArray(ids, String.class);
            modelAndView = new ModelAndView(
                    ApplyAgreementDefine.RE_LIST_PATH);
        }
        int agreementsSUM = 0;
        for (String string : recordList) { 
            System.out.println(string+"____________________");
            String[] paemStrings =  string.split("_");
            if(paemStrings!=null && paemStrings.length>0){
                int agreements = 0;
                String borrow_nid = paemStrings[0];//项目编号
                int repay_period = Integer.valueOf(paemStrings[1]);//期数
               // boolean isMonth = Boolean.valueOf(paemStrings[2]);//是否分期
                // 标的信息
                Borrow borrow = this.borrowRecoverService.getBorrowByNid(borrow_nid);
                
                if (borrow == null){
                    return modelAndView;
                }else{
                   String borrowStyle =  borrow.getBorrowStyle();
                       // 是否分期(true:分期, false:不分期)
                   boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
                    String planNid =  borrow.getPlanNid();//为空时，为直投，否则为计划
                    //承接人都是担保机构
                    int repayOrgUserId = borrow.getRepayOrgUserId();//担保机构用户ID
                  //huiyingdai_borrow_recover(标的放款记录（出借人） 总表)

                    if(StringUtils.isEmpty(planNid)){//直投
                        if(isMonth){//分期
                            //huiyingdai_borrow_recover_plan(标的放款记录分期（出借人）)-borrow_nid,repay_period
                            //huiyingdai_borrow_recover(标的放款记录（出借人） 总表)
                           // List<BorrowRecoverPlan> borrowRecoverList = borrowRecoverService.selectBorrowRecoverPlan(borrow_nid,repay_period);
                            
                            List<BorrowRecover> borrowRecoverPlist = borrowRecoverService.selectBorrowRecover(borrow_nid);
                            for (BorrowRecover borrowRecoverP : borrowRecoverPlist) {
                                BigDecimal  creditAmountp = borrowRecoverP.getCreditAmount();
                                BigDecimal  recoverCapitalp = borrowRecoverP.getRecoverCapital();
                                List<BorrowRecoverPlan> borrowRecoverList = borrowRecoverService.selectBorrowRecoverPlanByNid(borrowRecoverP.getNid(),repay_period);
                                   for (BorrowRecoverPlan borrowRecover : borrowRecoverList) {
                                       // 合同编号,拼接
                                       String nid = borrowRecover.getNid();
                                      // String nid = borrow_nid+"-"+assign_nid;
                                       BigDecimal creditAmount = borrowRecover.getCreditAmount();
                                       BigDecimal recoverCapital = borrowRecover.getRecoverCapital();
                                       List<CreditRepay> creditRepayList = applyAgreementService.selectCreditRepayByExample(nid,repay_period);
                                       if(creditRepayList!=null && creditRepayList.size()>0){//债转   
                                           if ((creditAmountp.compareTo(new BigDecimal("0.00"))==1) && (creditAmountp.compareTo(recoverCapitalp)==0)) {//全部债转
                                               for (CreditRepay creditRepay : creditRepayList) {
                                                   FddGenerateContractBean bean = new FddGenerateContractBean();
                                                 //垫付协议申请-协议生成详情
                                                   ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                                   applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                                   applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                                   applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                                   applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                                   applyAgreementInfo.setCreditUserId(creditRepay.getCreditUserId()+"");//承接人-担保机构
                                                   applyAgreementInfo.setStatus(5);  
                                                   applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                                   bean.setBorrowNid(borrow_nid);//标的编号
                                                   bean.setRepayPeriod(repay_period);//期数
                                                   bean.setTransType(5);//交易类型
                                                   bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                                   bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                                   bean.setCreditUserID(creditRepay.getCreditUserId());//出讓人
                                                   JSONObject paramter = applyAgreementService.getAllcreditParamter(creditRepay,bean,borrow);
                                                   //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                                   bean.setParamter(paramter); bean.setTeString(timeString); 
                                                 this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                                   agreements++;
                                               }
                                                                                   
                                           }else if ((creditAmountp.compareTo(new BigDecimal("0.00"))==1) && (creditAmountp.compareTo(recoverCapitalp)==-1)) {//部分债转
                                               //出让人-承接部分人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                               BigDecimal assignPay  = new BigDecimal("0.00");
                                               for (CreditRepay creditRepay : creditRepayList) {
                                                   assignPay = assignPay.add(creditRepay.getAssignRepayInterest());
                                                   FddGenerateContractBean bean = new FddGenerateContractBean();
                                                   //垫付协议申请-协议生成详情
                                                   ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                                   applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                                   applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                                   applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                                   applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                                   applyAgreementInfo.setCreditUserId(creditRepay.getCreditUserId()+"");//承接人-担保机构
                                                   applyAgreementInfo.setStatus(5);
                                                   applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                                   bean.setBorrowNid(borrow_nid);//标的编号
                                                   bean.setRepayPeriod(repay_period);//期数
                                                   bean.setTransType(5);//交易类型
                                                   bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                                   bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                                   bean.setCreditUserID(creditRepay.getCreditUserId());//出讓人
                                                   JSONObject paramter = applyAgreementService.getAllcreditParamter(creditRepay,bean,borrow);
                                                   //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                                   bean.setParamter(paramter); bean.setTeString(timeString); 
                                                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                                   agreements++;
                                               }
                                               /***************剩余部分********/
                                               //垫付协议申请-协议生成详情
                                               ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                               applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                               applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                               applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                               applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                               applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                               applyAgreementInfo.setStatus(5);
                                               applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                               BigDecimal assignCapital = recoverCapital.subtract(creditAmount);
                                               assignPay = borrowRecover.getRecoverInterestYes().subtract(assignPay);
                                               borrowRecover.setRecoverCapital(assignCapital);
                                               borrowRecover.setRecoverInterestYes(assignPay);
                                               FddGenerateContractBean bean = new FddGenerateContractBean();
                                               bean.setBorrowNid(borrow_nid);//标的编号
                                               bean.setRepayPeriod(repay_period);//期数
                                               bean.setTransType(5);//交易类型
                                               bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                               bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                               bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                               JSONObject paramter = applyAgreementService.getNocreditParamterPlan(borrowRecover,bean,borrow);
                                               //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                               bean.setParamter(paramter); bean.setTeString(timeString); 
                                             this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                               agreements++;
                                           }
                                           
                                       }else {//非债转   
                                         //垫付协议申请-协议生成详情
                                           ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                           applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                           applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                           applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                           applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                           applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                           applyAgreementInfo.setStatus(5);
                                           applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                           FddGenerateContractBean bean = new FddGenerateContractBean();
                                           bean.setBorrowNid(borrow_nid);//标的编号
                                           bean.setRepayPeriod(repay_period);//期数
                                           bean.setTransType(5);//交易类型
                                           bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                           bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                           bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                           JSONObject paramter = applyAgreementService.getNocreditParamterPlan(borrowRecover,bean,borrow);
                                           //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                          bean.setParamter(paramter); bean.setTeString(timeString); 
                                          this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                           agreements++;
                                    }   
                                }                             
                            }
                            
                        }else {//不分期
                            //huiyingdai_borrow_recover(标的放款记录（出借人） 总表)
                            List<BorrowRecover> borrowRecoverList = borrowRecoverService.selectBorrowRecover(borrow_nid);
                            for (BorrowRecover borrowRecover : borrowRecoverList) {
                                // 合同编号,拼接
                                String assign_nid = borrowRecover.getNid();
                                String nid = assign_nid;
                               // String nid = borrow_nid+"-"+assign_nid;
                                BigDecimal creditAmount = borrowRecover.getCreditAmount();
                                BigDecimal recoverCapital = borrowRecover.getRecoverCapital();
                                if(creditAmount.compareTo(new BigDecimal("0.00"))==0){//非债转                                    
                                  //垫付协议申请-协议生成详情
                                    ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                    applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                    applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                    applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                    applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                    applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                    applyAgreementInfo.setStatus(5);
                                    applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                    FddGenerateContractBean bean = new FddGenerateContractBean();
                                    bean.setBorrowNid(borrow_nid);//标的编号
                                    bean.setRepayPeriod(repay_period);//期数
                                    bean.setTransType(5);//交易类型
                                    bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                    bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                    bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                    JSONObject paramter = applyAgreementService.getNocreditParamter(borrowRecover,bean,borrow);
                                    //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                    bean.setParamter(paramter); bean.setTeString(timeString); 
                                    this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                    agreements++;

                                }else if ((creditAmount.compareTo(new BigDecimal("0.00"))==1) && (creditAmount.compareTo(recoverCapital)==0)) {//全部债转
                                    List<CreditRepay> creditRepayList = applyAgreementService.selectCreditRepayByExample(nid,1);
                                    for (CreditRepay creditRepay : creditRepayList) {
                                        //垫付协议申请-协议生成详情
                                        ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                        applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                        applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                        applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                        applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                        applyAgreementInfo.setCreditUserId(creditRepay.getCreditUserId()+"");//承接人-担保机构
                                        applyAgreementInfo.setStatus(5);
                                        applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                        FddGenerateContractBean bean = new FddGenerateContractBean();
                                        bean.setBorrowNid(borrow_nid);//标的编号
                                        bean.setRepayPeriod(repay_period);//期数
                                        bean.setTransType(5);//交易类型
                                        bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                        bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                        bean.setCreditUserID(creditRepay.getCreditUserId());//出讓人
                                        JSONObject paramter = applyAgreementService.getAllcreditParamter(creditRepay,bean,borrow);
                                        //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                        bean.setParamter(paramter); bean.setTeString(timeString);             
                                       this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                        agreements++;
                                    }
                                                                        
                                }else if ((creditAmount.compareTo(new BigDecimal("0.00"))==1) && (creditAmount.compareTo(recoverCapital)==-1)){ //部分债转
                                    //出让人-承接部分人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                    List<CreditRepay> creditRepayList = applyAgreementService.selectCreditRepayByExample(nid,repay_period);
                                    BigDecimal assignPay  = new BigDecimal("0.00");
                                    for (CreditRepay creditRepay : creditRepayList) {
                                      //垫付协议申请-协议生成详情
                                        ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                        applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                        applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                        applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                        applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                        applyAgreementInfo.setCreditUserId(creditRepay.getCreditUserId()+"");//承接人-担保机构
                                        applyAgreementInfo.setStatus(5);
                                        applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                        assignPay = assignPay.add(creditRepay.getAssignRepayInterest());
                                        FddGenerateContractBean bean = new FddGenerateContractBean();
                                        bean.setBorrowNid(borrow_nid);//标的编号
                                        bean.setRepayPeriod(repay_period);//期数
                                        bean.setTransType(5);//交易类型
                                        bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                        bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                        bean.setCreditUserID(creditRepay.getCreditUserId());//出讓人
                                        JSONObject paramter = applyAgreementService.getAllcreditParamter(creditRepay,bean,borrow);
                                        //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                        bean.setParamter(paramter); bean.setTeString(timeString); 
                                      this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                        agreements++;
                                    }
                                    //垫付协议申请-协议生成详情
                                    ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                    applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                    applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                    applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                    applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                    applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                    applyAgreementInfo.setStatus(5);
                                    applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                    /***************剩余部分********/
                                    BigDecimal assignCapital = recoverCapital.subtract(creditAmount);
                                    assignPay = borrowRecover.getRecoverInterestYes().subtract(assignPay);
                                    borrowRecover.setRecoverCapital(assignCapital);
                                    borrowRecover.setRecoverInterestYes(assignPay);
                                    FddGenerateContractBean bean = new FddGenerateContractBean();
                                    bean.setBorrowNid(borrow_nid);//标的编号
                                    bean.setRepayPeriod(repay_period);//期数
                                    bean.setTransType(5);//交易类型
                                    bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                    bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                    bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                    JSONObject paramter = applyAgreementService.getNocreditParamter(borrowRecover,bean,borrow);
                                    //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                    bean.setParamter(paramter); bean.setTeString(timeString); 
                                   this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                    agreements++;
                                    
                                }
                              
                            }
                            
                        } 
                        
                    }else {//计划
                        if(isMonth){//分期
                            //huiyingdai_borrow_recover_plan(标的放款记录分期（出借人）)-borrow_nid,repay_period
                            //huiyingdai_borrow_recover(标的放款记录（出借人） 总表)
                            
                            List<BorrowRecover> borrowRecoverPlist = borrowRecoverService.selectBorrowRecover(borrow_nid);
                            for (BorrowRecover borrowRecoverP : borrowRecoverPlist) {
                                BigDecimal creditAmountp = borrowRecoverP.getCreditAmount();
                                BigDecimal recoverCapitalp = borrowRecoverP.getRecoverCapital();
                                List<BorrowRecoverPlan> borrowRecoverList = borrowRecoverService.selectBorrowRecoverPlanByNid(borrowRecoverP.getNid(),repay_period);
                                   for (BorrowRecoverPlan borrowRecover : borrowRecoverList) {
                                       // 合同编号,拼接
                                       String nid = borrowRecover.getNid();
                                      // String nid = borrow_nid+"-"+assign_nid;
                                       BigDecimal creditAmount = borrowRecover.getCreditAmount();
                                       BigDecimal recoverCapital = borrowRecover.getRecoverCapital();
                                       List<HjhDebtCreditRepay> hjhDebtCreditRepayList = applyAgreementService.selectHjhDebtCreditRepayByExample(nid,repay_period);
                                       if(hjhDebtCreditRepayList!=null && hjhDebtCreditRepayList.size()>0){//债转   
                                           if ((creditAmountp.compareTo(new BigDecimal("0.00"))==1) && (creditAmountp.compareTo(recoverCapitalp)==0)) {//全部债转
                                               for (HjhDebtCreditRepay hjhDebtCreditRepay : hjhDebtCreditRepayList) {
                                                 //垫付协议申请-协议生成详情
                                                   ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                                   applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                                   applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                                   applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+hjhDebtCreditRepay.getUniqueNid());//合同编号
                                                   applyAgreementInfo.setUserId(repayOrgUserId+"");//承接人-担保机构
                                                   applyAgreementInfo.setCreditUserId(hjhDebtCreditRepay.getCreateUserId()+"");//出借人(出让人)
                                                   applyAgreementInfo.setStatus(6);
                                                   applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                                   FddGenerateContractBean bean = new FddGenerateContractBean();
                                                   bean.setBorrowNid(borrow_nid);//标的编号
                                                   bean.setRepayPeriod(repay_period);//期数
                                                   bean.setTransType(6);//交易类型
                                                   bean.setTenderType(1);//出借类型 0：原始 1：债转 2 :计划
                                                   bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                                   bean.setCreditUserID(hjhDebtCreditRepay.getCreateUserId());//出讓人
                                                   JSONObject paramter = applyAgreementService.getAllcreditParamterHjh(hjhDebtCreditRepay,bean,borrow);
                                                  
                                                   bean.setParamter(paramter); bean.setTeString(timeString); 
                                                   this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                                   agreements++;
                                               }
                                                                                   
                                           }else if ((creditAmountp.compareTo(new BigDecimal("0.00"))==1) && (creditAmountp.compareTo(recoverCapitalp)==-1))  {//部分债转
                                               //出让人-承接部分人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                               BigDecimal assignPay  = new BigDecimal("0.00");
                                               for (HjhDebtCreditRepay creditRepay : hjhDebtCreditRepayList) {
                                                   //垫付协议申请-协议生成详情
                                                     ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                                     applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                                     applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                                     applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                                     applyAgreementInfo.setUserId(repayOrgUserId+"");//承接人-担保机构
                                                     applyAgreementInfo.setCreditUserId(creditRepay.getCreateUserId()+"");//出借人(出让人)
                                                     applyAgreementInfo.setStatus(6);
                                                     applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                                     assignPay = assignPay.add(creditRepay.getRepayInterest()).add(creditRepay.getRepayAdvanceInterest()).add(creditRepay.getRepayLateInterest()).add(creditRepay.getRepayDelayInterest());
                                                     FddGenerateContractBean bean = new FddGenerateContractBean();
                                                     bean.setBorrowNid(borrow_nid);//标的编号
                                                     bean.setRepayPeriod(repay_period);//期数
                                                     bean.setTransType(6);//交易类型
                                                     bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                                     bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                                     bean.setCreditUserID(creditRepay.getCreateUserId());//出讓人
                                                     JSONObject paramter = applyAgreementService.getAllcreditParamterHjh(creditRepay,bean,borrow);
                                                     //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                                     bean.setParamter(paramter); bean.setTeString(timeString); 
                                                     this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                                     agreements++;
                                                 }
                                                 /***************剩余部分********/
                                                   BigDecimal assignCapital = recoverCapital.subtract(creditAmount);
                                                   assignPay = borrowRecover.getRecoverInterestYes().subtract(assignPay);
                                                   if((!assignCapital.equals(new BigDecimal("0.00"))) && (!assignPay.equals(new BigDecimal("0.00")))){
                                                       borrowRecover.setRecoverCapital(assignCapital);
                                                       borrowRecover.setRecoverInterestYes(assignPay);
                                                     FddGenerateContractBean bean = new FddGenerateContractBean();
                                                     bean.setBorrowNid(borrow_nid);//标的编号
                                                     bean.setRepayPeriod(repay_period);//期数
                                                     bean.setTransType(6);//交易类型
                                                     bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                                     bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                                     bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                                     JSONObject paramter = applyAgreementService.getNocreditParamterPlan(borrowRecover,bean,borrow);
                                                     //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                                     bean.setParamter(paramter); bean.setTeString(timeString); 
                                                     this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                                     agreements++;
                                                     //垫付协议申请-协议生成详情
                                                     ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                                     applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                                     applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                                     applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                                     applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                                     applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                                     applyAgreementInfo.setStatus(6);
                                                     applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                                   }
                                                  
                                           }
                                           
                                       }else {//非债转   
                                         //垫付协议申请-协议生成详情
                                           ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                           applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                           applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                           applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                           applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                           applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                           applyAgreementInfo.setStatus(5);
                                           applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                           FddGenerateContractBean bean = new FddGenerateContractBean();
                                           bean.setBorrowNid(borrow_nid);//标的编号
                                           bean.setRepayPeriod(repay_period);//期数
                                           bean.setTransType(6);//交易类型
                                           bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                           bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                           bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                           JSONObject paramter = applyAgreementService.getNocreditParamterPlan(borrowRecover,bean,borrow);
                                           //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                           bean.setParamter(paramter); bean.setTeString(timeString); 
                                           this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                           agreements++;
                                    }   
                                }                             
                            }

                        }else {//不分期
                            //huiyingdai_borrow_recover(标的放款记录（出借人） 总表)
                            List<BorrowRecover> borrowRecoverList = borrowRecoverService.selectBorrowRecover(borrow_nid);
                            for (BorrowRecover borrowRecover : borrowRecoverList) {
                                // 合同编号,拼接
                                String nid = borrowRecover.getNid();
                                //String nid = borrow_nid+"-"+assign_nid;
                                BigDecimal creditAmount = borrowRecover.getCreditAmount();
                                BigDecimal recoverCapital = borrowRecover.getRecoverCapital();
                                if(creditAmount.compareTo(new BigDecimal("0.00"))==0){//非债转                                   
                                  //垫付协议申请-协议生成详情
                                    ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                    applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                    applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                    applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                    applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                    applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                    applyAgreementInfo.setStatus(5);
                                    applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                    FddGenerateContractBean bean = new FddGenerateContractBean();
                                    bean.setBorrowNid(borrow_nid);//标的编号
                                    bean.setRepayPeriod(repay_period);//期数
                                    bean.setTransType(6);//交易类型
                                    bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                    bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                    bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                    JSONObject paramter = applyAgreementService.getNocreditParamter(borrowRecover,bean,borrow);
                                    //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                    bean.setParamter(paramter); bean.setTeString(timeString); 
                                    this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                    agreements++;
                                }else if ((creditAmount.compareTo(new BigDecimal("0.00"))==1) && (creditAmount.compareTo(recoverCapital)==0)) {//全部债转
                                  //出让人-承接人hyjf_hjh_debt_credit_repay（nid-invest_order_id del_flag=0）-user_id(承接人的id)_List
                                    List<HjhDebtCreditRepay> hjhDebtCreditRepayList = applyAgreementService.selectHjhDebtCreditRepayByExample(nid,repay_period);
                                    for (HjhDebtCreditRepay hjhDebtCreditRepay : hjhDebtCreditRepayList) {
                                      //垫付协议申请-协议生成详情
                                        ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                        applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                        applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                        applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+hjhDebtCreditRepay.getUniqueNid());//合同编号
                                        applyAgreementInfo.setUserId(repayOrgUserId+"");//承接人-担保机构
                                        applyAgreementInfo.setCreditUserId(hjhDebtCreditRepay.getCreateUserId()+"");//出借人(出让人)
                                        applyAgreementInfo.setStatus(6);
                                        applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                        FddGenerateContractBean bean = new FddGenerateContractBean();
                                        bean.setBorrowNid(borrow_nid);//标的编号
                                        bean.setRepayPeriod(repay_period);//期数
                                        bean.setTransType(6);//交易类型
                                        bean.setTenderType(1);//出借类型 0：原始 1：债转 2 :计划
                                        bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                        bean.setCreditUserID(hjhDebtCreditRepay.getCreateUserId());//出讓人
                                        JSONObject paramter = applyAgreementService.getAllcreditParamterHjh(hjhDebtCreditRepay,bean,borrow);
                                       
                                        bean.setParamter(paramter); bean.setTeString(timeString); 
                                        this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                        agreements++;
                                    }
                                }else if ((creditAmount.compareTo(new BigDecimal("0.00"))==1) && (creditAmount.compareTo(recoverCapital)==-1))  {//部分债转
                                  //出让人-承接人hyjf_hjh_debt_credit_repay（nid-invest_order_id del_flag=0）-user_id(承接人的id)_List
                                    List<HjhDebtCreditRepay> hjhDebtCreditRepayList = applyAgreementService.selectHjhDebtCreditRepayByExample(nid,repay_period);
                                    BigDecimal assignPay  = new BigDecimal("0.00");
                                    for (HjhDebtCreditRepay creditRepay : hjhDebtCreditRepayList) {
                                        //assignPay = assignPay.add(creditRepay.getRepayInterest());
                                        assignPay = assignPay.add(creditRepay.getRepayInterest()).add(creditRepay.getRepayAdvanceInterest()).add(creditRepay.getRepayLateInterest()).add(creditRepay.getRepayDelayInterest());
                                        FddGenerateContractBean bean = new FddGenerateContractBean();
                                      //垫付协议申请-协议生成详情
                                        ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                        applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                        applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                        applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                        applyAgreementInfo.setUserId(repayOrgUserId+"");//承接人-担保机构
                                        applyAgreementInfo.setCreditUserId(creditRepay.getCreateUserId()+"");//出借人(出让人)
                                        applyAgreementInfo.setStatus(6);
                                        applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                        bean.setBorrowNid(borrow_nid);//标的编号
                                        bean.setRepayPeriod(repay_period);//期数
                                        bean.setTransType(6);//交易类型
                                        bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                        bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                        bean.setCreditUserID(creditRepay.getCreateUserId());//出讓人
                                        JSONObject paramter = applyAgreementService.getAllcreditParamterHjh(creditRepay,bean,borrow);
                                        //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                        bean.setParamter(paramter); bean.setTeString(timeString); 
                                        this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                        agreements++;
                                    }
                                    /***************剩余部分********/
                                    BigDecimal assignCapital = recoverCapital.subtract(creditAmount);
                                    assignPay = borrowRecover.getRecoverInterestYes().subtract(assignPay);
                                    if((!assignCapital.equals(new BigDecimal("0.00"))) && (!assignPay.equals(new BigDecimal("0.00")))){
                                        borrowRecover.setRecoverCapital(assignCapital);
                                        borrowRecover.setRecoverInterestYes(assignPay);
                                        FddGenerateContractBean bean = new FddGenerateContractBean();
                                        bean.setBorrowNid(borrow_nid);//标的编号
                                        bean.setRepayPeriod(repay_period);//期数
                                        bean.setTransType(6);//交易类型
                                        bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                        bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                        bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                        JSONObject paramter = applyAgreementService.getNocreditParamter(borrowRecover,bean,borrow);
                                        //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                        bean.setParamter(paramter); bean.setTeString(timeString); 
                                        this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                        agreements++;
                                        //垫付协议申请-协议生成详情
                                        ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                        applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                        applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                        applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                        applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                        applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                        applyAgreementInfo.setStatus(6);
                                        applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                    }
                                }
                              
                            } 
                        }
                    }   
                } 
                ApplyAgreement applyAgreement = new ApplyAgreement();
                applyAgreement.setBorrowNid(borrow_nid);
                applyAgreement.setRepayPeriod(repay_period);
                applyAgreement.setApplyUserId(ShiroUtil.getLoginUserId());
                applyAgreement.setApplyUserName(ShiroUtil.getLoginUsername());
                applyAgreement.setAgreementNumber(agreements);
                applyAgreement.setStatus(1);
                applyAgreement.setCreateTime(GetDate.getNowTime10());
                applyAgreement.setDelFlg(0);
                ApplyAgreementExample example = new ApplyAgreementExample();//垫付协议申请记录
                ApplyAgreementExample.Criteria criteria = example.createCriteria();
                criteria.andBorrowNidEqualTo(borrow_nid);
                criteria.andRepayPeriodEqualTo(repay_period);
                List<ApplyAgreement> applyAgreements = this.applyAgreementService.selectApplyAgreement(example);
                if(applyAgreements!=null && applyAgreements.size()>0){
                    applyAgreement = applyAgreements.get(0);
                    applyAgreement.setUpdateTime(GetDate.getNowTime10());
                    applyAgreement.setUpdateUser(Integer.valueOf(ShiroUtil.getLoginUserId()));
                    this.applyAgreementService.updateByPrimaryKey(applyAgreement);
                }else {
                    this.applyAgreementService.insert(applyAgreement);
                }
                agreementsSUM = agreementsSUM+agreements;
                
            }  
           
        }
        form.setAgreementNumber(agreementsSUM);
        modelAndView.addObject(ApplyAgreementDefine.BORROW_FORM, form);
        LogUtil.startLog(ApplyAgreementController.class.toString(), ApplyAgreementDefine.ADD_ACTION);
       // 跳转页面用（info里面有）
        return modelAndView;
    }
    
    /**
     * PDF文件签署
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ApplyAgreementDefine.PDF_SIGN_ACTION, produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
    public String pdfSignAction(HttpServletRequest request) {
        LogUtil.startLog(CreditTenderDefine.class.toString(), CreditTenderDefine.PDF_SIGN_ACTION);
        JSONObject ret = new JSONObject();
        // 标的编号
        String borrow_nid = request.getParameter("borrowNid");
        //期数
        String repayPeriod = request.getParameter("repayPeriod");
        if(StringUtils.isBlank(borrow_nid)){
            ret.put(ApplyAgreementDefine.JSON_RESULT_KEY, "操作失败,没有该标记录");
            ret.put(ApplyAgreementDefine.JSON_STATUS_KEY, ApplyAgreementDefine.JSON_STATUS_NG);
            LogUtil.endLog(CreditTenderDefine.class.toString(), ApplyAgreementDefine.PDF_SIGN_ACTION);
            return ret.toString();
        }
        if(StringUtils.isBlank(repayPeriod)){
            ret.put(ApplyAgreementDefine.JSON_RESULT_KEY, "操作失败,没有该标的期数记录");
            ret.put(ApplyAgreementDefine.JSON_STATUS_KEY, ApplyAgreementDefine.JSON_STATUS_NG);
            LogUtil.endLog(CreditTenderDefine.class.toString(), ApplyAgreementDefine.PDF_SIGN_ACTION);
            return ret.toString();
        }
        int repay_period = Integer.valueOf(repayPeriod);
        int agreements = 0;
        // 标的信息
        Borrow borrow = this.borrowRecoverService.getBorrowByNid(borrow_nid);
        
        if (borrow == null){
            ret.put(ApplyAgreementDefine.JSON_RESULT_KEY, "操作失败,没有该标的期数记录");
            ret.put(ApplyAgreementDefine.JSON_STATUS_KEY, ApplyAgreementDefine.JSON_STATUS_NG);
            LogUtil.endLog(CreditTenderDefine.class.toString(), ApplyAgreementDefine.PDF_SIGN_ACTION);
            return ret.toString();
        }else{
           String borrowStyle =  borrow.getBorrowStyle();
               // 是否分期(true:分期, false:不分期)
           boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrowStyle) || CustomConstants.BORROW_STYLE_MONTH.equals(borrowStyle)
                        || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrowStyle);
            String planNid =  borrow.getPlanNid();//为空时，为直投，否则为计划
            //承接人都是担保机构
            int repayOrgUserId = borrow.getRepayOrgUserId();//担保机构用户ID
            //huiyingdai_borrow_recover(标的放款记录（出借人） 总表)

              if(StringUtils.isEmpty(planNid)){//直投
                  if(isMonth){//分期
                      //huiyingdai_borrow_recover_plan(标的放款记录分期（出借人）)-borrow_nid,repay_period
                      //huiyingdai_borrow_recover(标的放款记录（出借人） 总表)
                     // List<BorrowRecoverPlan> borrowRecoverList = borrowRecoverService.selectBorrowRecoverPlan(borrow_nid,repay_period);
                      
                      List<BorrowRecover> borrowRecoverPlist = borrowRecoverService.selectBorrowRecover(borrow_nid);
                      for (BorrowRecover borrowRecoverP : borrowRecoverPlist) {
                          BigDecimal  creditAmountp = borrowRecoverP.getCreditAmount();
                          BigDecimal  recoverCapitalp = borrowRecoverP.getRecoverCapital();
                          List<BorrowRecoverPlan> borrowRecoverList = borrowRecoverService.selectBorrowRecoverPlanByNid(borrowRecoverP.getNid(),repay_period);
                             for (BorrowRecoverPlan borrowRecover : borrowRecoverList) {
                                 // 合同编号,拼接
                                 String nid = borrowRecover.getNid();
                                // String nid = borrow_nid+"-"+assign_nid;
                                 BigDecimal creditAmount = borrowRecover.getCreditAmount();
                                 BigDecimal recoverCapital = borrowRecover.getRecoverCapital();
                                 List<CreditRepay> creditRepayList = applyAgreementService.selectCreditRepayByExample(nid,repay_period);
                                 if(creditRepayList!=null && creditRepayList.size()>0){//债转   
                                     if ((creditAmountp.compareTo(new BigDecimal("0.00"))==1) && (creditAmountp.compareTo(recoverCapitalp)==0)) {//全部债转
                                         for (CreditRepay creditRepay : creditRepayList) {
                                             FddGenerateContractBean bean = new FddGenerateContractBean();
                                           //垫付协议申请-协议生成详情
                                             ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                             applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                             applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                             applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                             applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                             applyAgreementInfo.setCreditUserId(creditRepay.getCreditUserId()+"");//承接人-担保机构
                                             applyAgreementInfo.setStatus(5);  
                                             applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                             bean.setBorrowNid(borrow_nid);//标的编号
                                             bean.setRepayPeriod(repay_period);//期数
                                             bean.setTransType(5);//交易类型
                                             bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                             bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                             bean.setCreditUserID(creditRepay.getCreditUserId());//出讓人
                                             JSONObject paramter = applyAgreementService.getAllcreditParamter(creditRepay,bean,borrow);
                                             //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                             bean.setParamter(paramter); bean.setTeString(timeString); 
                                           this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                             agreements++;
                                         }
                                                                             
                                     }else if ((creditAmountp.compareTo(new BigDecimal("0.00"))==1) && (creditAmountp.compareTo(recoverCapitalp)==-1)) {//部分债转
                                         //出让人-承接部分人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                         BigDecimal assignPay  = new BigDecimal("0.00");
                                         for (CreditRepay creditRepay : creditRepayList) {
                                             assignPay = assignPay.add(creditRepay.getAssignRepayInterest());
                                             FddGenerateContractBean bean = new FddGenerateContractBean();
                                             //垫付协议申请-协议生成详情
                                             ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                             applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                             applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                             applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                             applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                             applyAgreementInfo.setCreditUserId(creditRepay.getCreditUserId()+"");//承接人-担保机构
                                             applyAgreementInfo.setStatus(5);
                                             applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                             bean.setBorrowNid(borrow_nid);//标的编号
                                             bean.setRepayPeriod(repay_period);//期数
                                             bean.setTransType(5);//交易类型
                                             bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                             bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                             bean.setCreditUserID(creditRepay.getCreditUserId());//出讓人
                                             JSONObject paramter = applyAgreementService.getAllcreditParamter(creditRepay,bean,borrow);
                                             //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                             bean.setParamter(paramter); bean.setTeString(timeString); 
                                          this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                             agreements++;
                                         }
                                         /***************剩余部分********/
                                         //垫付协议申请-协议生成详情
                                         ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                         applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                         applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                         applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                         applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                         applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                         applyAgreementInfo.setStatus(5);
                                         applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                         BigDecimal assignCapital = recoverCapital.subtract(creditAmount);
                                         assignPay = borrowRecover.getRecoverInterestYes().subtract(assignPay);
                                         borrowRecover.setRecoverCapital(assignCapital);
                                         borrowRecover.setRecoverInterestYes(assignPay);
                                         FddGenerateContractBean bean = new FddGenerateContractBean();
                                         bean.setBorrowNid(borrow_nid);//标的编号
                                         bean.setRepayPeriod(repay_period);//期数
                                         bean.setTransType(5);//交易类型
                                         bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                         bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                         bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                         JSONObject paramter = applyAgreementService.getNocreditParamterPlan(borrowRecover,bean,borrow);
                                         //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                         bean.setParamter(paramter); bean.setTeString(timeString); 
                                       this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                         agreements++;
                                     }
                                     
                                 }else {//非债转   
                                   //垫付协议申请-协议生成详情
                                     ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                     applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                     applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                     applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                     applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                     applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                     applyAgreementInfo.setStatus(5);
                                     applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                     FddGenerateContractBean bean = new FddGenerateContractBean();
                                     bean.setBorrowNid(borrow_nid);//标的编号
                                     bean.setRepayPeriod(repay_period);//期数
                                     bean.setTransType(5);//交易类型
                                     bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                     bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                     bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                     JSONObject paramter = applyAgreementService.getNocreditParamterPlan(borrowRecover,bean,borrow);
                                     //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                    bean.setParamter(paramter); bean.setTeString(timeString); 
                                    this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                     agreements++;
                              }   
                          }                             
                      }
                      
                  }else {//不分期
                      //huiyingdai_borrow_recover(标的放款记录（出借人） 总表)
                      List<BorrowRecover> borrowRecoverList = borrowRecoverService.selectBorrowRecover(borrow_nid);
                      for (BorrowRecover borrowRecover : borrowRecoverList) {
                          // 合同编号,拼接
                          String assign_nid = borrowRecover.getNid();
                          String nid = assign_nid;
                         // String nid = borrow_nid+"-"+assign_nid;
                          BigDecimal creditAmount = borrowRecover.getCreditAmount();
                          BigDecimal recoverCapital = borrowRecover.getRecoverCapital();
                          if(creditAmount.compareTo(new BigDecimal("0.00"))==0){//非债转                                    
                            //垫付协议申请-协议生成详情
                              ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                              applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                              applyAgreementInfo.setRepayPeriod(repay_period);//期数
                              applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                              applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                              applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                              applyAgreementInfo.setStatus(5);
                              applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                              FddGenerateContractBean bean = new FddGenerateContractBean();
                              bean.setBorrowNid(borrow_nid);//标的编号
                              bean.setRepayPeriod(repay_period);//期数
                              bean.setTransType(5);//交易类型
                              bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                              bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                              bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                              JSONObject paramter = applyAgreementService.getNocreditParamter(borrowRecover,bean,borrow);
                              //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                              bean.setParamter(paramter); bean.setTeString(timeString); 
                              this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                              agreements++;

                          }else if ((creditAmount.compareTo(new BigDecimal("0.00"))==1) && (creditAmount.compareTo(recoverCapital)==0)) {//全部债转
                              List<CreditRepay> creditRepayList = applyAgreementService.selectCreditRepayByExample(nid,1);
                              for (CreditRepay creditRepay : creditRepayList) {
                                  //垫付协议申请-协议生成详情
                                  ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                  applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                  applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                  applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                  applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                  applyAgreementInfo.setCreditUserId(creditRepay.getCreditUserId()+"");//承接人-担保机构
                                  applyAgreementInfo.setStatus(5);
                                  applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                  FddGenerateContractBean bean = new FddGenerateContractBean();
                                  bean.setBorrowNid(borrow_nid);//标的编号
                                  bean.setRepayPeriod(repay_period);//期数
                                  bean.setTransType(5);//交易类型
                                  bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                  bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                  bean.setCreditUserID(creditRepay.getCreditUserId());//出讓人
                                  JSONObject paramter = applyAgreementService.getAllcreditParamter(creditRepay,bean,borrow);
                                  //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                  bean.setParamter(paramter); bean.setTeString(timeString);             
                                 this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                  agreements++;
                              }
                                                                  
                          }else if ((creditAmount.compareTo(new BigDecimal("0.00"))==1) && (creditAmount.compareTo(recoverCapital)==-1)){ //部分债转
                              //出让人-承接部分人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                              List<CreditRepay> creditRepayList = applyAgreementService.selectCreditRepayByExample(nid,repay_period);
                              BigDecimal assignPay  = new BigDecimal("0.00");
                              for (CreditRepay creditRepay : creditRepayList) {
                                //垫付协议申请-协议生成详情
                                  ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                  applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                  applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                  applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                  applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                  applyAgreementInfo.setCreditUserId(creditRepay.getCreditUserId()+"");//承接人-担保机构
                                  applyAgreementInfo.setStatus(5);
                                  applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                  assignPay = assignPay.add(creditRepay.getAssignRepayInterest());
                                  FddGenerateContractBean bean = new FddGenerateContractBean();
                                  bean.setBorrowNid(borrow_nid);//标的编号
                                  bean.setRepayPeriod(repay_period);//期数
                                  bean.setTransType(5);//交易类型
                                  bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                  bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                  bean.setCreditUserID(creditRepay.getCreditUserId());//出讓人
                                  JSONObject paramter = applyAgreementService.getAllcreditParamter(creditRepay,bean,borrow);
                                  //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                  bean.setParamter(paramter); bean.setTeString(timeString); 
                                this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                  agreements++;
                              }
                              //垫付协议申请-协议生成详情
                              ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                              applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                              applyAgreementInfo.setRepayPeriod(repay_period);//期数
                              applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                              applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                              applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                              applyAgreementInfo.setStatus(5);
                              applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                              /***************剩余部分********/
                              BigDecimal assignCapital = recoverCapital.subtract(creditAmount);
                              assignPay = borrowRecover.getRecoverInterestYes().subtract(assignPay);
                              borrowRecover.setRecoverCapital(assignCapital);
                              borrowRecover.setRecoverInterestYes(assignPay);
                              FddGenerateContractBean bean = new FddGenerateContractBean();
                              bean.setBorrowNid(borrow_nid);//标的编号
                              bean.setRepayPeriod(repay_period);//期数
                              bean.setTransType(5);//交易类型
                              bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                              bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                              bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                              JSONObject paramter = applyAgreementService.getNocreditParamter(borrowRecover,bean,borrow);
                              //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                              bean.setParamter(paramter); bean.setTeString(timeString); 
                             this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                              agreements++;
                              
                          }
                        
                      }
                      
                  } 
                  
              }else {//计划
                  if(isMonth){//分期
                      //huiyingdai_borrow_recover_plan(标的放款记录分期（出借人）)-borrow_nid,repay_period
                      //huiyingdai_borrow_recover(标的放款记录（出借人） 总表)
                      
                      List<BorrowRecover> borrowRecoverPlist = borrowRecoverService.selectBorrowRecover(borrow_nid);
                      for (BorrowRecover borrowRecoverP : borrowRecoverPlist) {
                          BigDecimal creditAmountp = borrowRecoverP.getCreditAmount();
                          BigDecimal recoverCapitalp = borrowRecoverP.getRecoverCapital();
                          List<BorrowRecoverPlan> borrowRecoverList = borrowRecoverService.selectBorrowRecoverPlanByNid(borrowRecoverP.getNid(),repay_period);
                             for (BorrowRecoverPlan borrowRecover : borrowRecoverList) {
                                 // 合同编号,拼接
                                 String nid = borrowRecover.getNid();
                                // String nid = borrow_nid+"-"+assign_nid;
                                 BigDecimal creditAmount = borrowRecover.getCreditAmount();
                                 BigDecimal recoverCapital = borrowRecover.getRecoverCapital();
                                 List<HjhDebtCreditRepay> hjhDebtCreditRepayList = applyAgreementService.selectHjhDebtCreditRepayByExample(nid,repay_period);
                                 if(hjhDebtCreditRepayList!=null && hjhDebtCreditRepayList.size()>0){//债转   
                                     if ((creditAmountp.compareTo(new BigDecimal("0.00"))==1) && (creditAmountp.compareTo(recoverCapitalp)==0)) {//全部债转
                                         for (HjhDebtCreditRepay hjhDebtCreditRepay : hjhDebtCreditRepayList) {
                                           //垫付协议申请-协议生成详情
                                             ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                             applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                             applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                             applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+hjhDebtCreditRepay.getUniqueNid());//合同编号
                                             applyAgreementInfo.setUserId(repayOrgUserId+"");//承接人-担保机构
                                             applyAgreementInfo.setCreditUserId(hjhDebtCreditRepay.getCreateUserId()+"");//出借人(出让人)
                                             applyAgreementInfo.setStatus(6);
                                             applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                             FddGenerateContractBean bean = new FddGenerateContractBean();
                                             bean.setBorrowNid(borrow_nid);//标的编号
                                             bean.setRepayPeriod(repay_period);//期数
                                             bean.setTransType(6);//交易类型
                                             bean.setTenderType(1);//出借类型 0：原始 1：债转 2 :计划
                                             bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                             bean.setCreditUserID(hjhDebtCreditRepay.getCreateUserId());//出讓人
                                             JSONObject paramter = applyAgreementService.getAllcreditParamterHjh(hjhDebtCreditRepay,bean,borrow);
                                            
                                             bean.setParamter(paramter); bean.setTeString(timeString); 
                                             this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                             agreements++;
                                         }
                                                                             
                                     }else if ((creditAmountp.compareTo(new BigDecimal("0.00"))==1) && (creditAmountp.compareTo(recoverCapitalp)==-1))  {//部分债转
                                         //出让人-承接部分人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                         BigDecimal assignPay  = new BigDecimal("0.00");
                                         for (HjhDebtCreditRepay creditRepay : hjhDebtCreditRepayList) {
                                             //垫付协议申请-协议生成详情
                                               ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                               applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                               applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                               applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                               applyAgreementInfo.setUserId(repayOrgUserId+"");//承接人-担保机构
                                               applyAgreementInfo.setCreditUserId(creditRepay.getCreateUserId()+"");//出借人(出让人)
                                               applyAgreementInfo.setStatus(6);
                                               applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                               assignPay = assignPay.add(creditRepay.getRepayInterest()).add(creditRepay.getRepayAdvanceInterest()).add(creditRepay.getRepayLateInterest()).add(creditRepay.getRepayDelayInterest());
                                               FddGenerateContractBean bean = new FddGenerateContractBean();
                                               bean.setBorrowNid(borrow_nid);//标的编号
                                               bean.setRepayPeriod(repay_period);//期数
                                               bean.setTransType(6);//交易类型
                                               bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                               bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                               bean.setCreditUserID(creditRepay.getCreateUserId());//出讓人
                                               JSONObject paramter = applyAgreementService.getAllcreditParamterHjh(creditRepay,bean,borrow);
                                               //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                               bean.setParamter(paramter); bean.setTeString(timeString); 
                                               this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                               agreements++;
                                           }
                                           /***************剩余部分********/
                                             BigDecimal assignCapital = recoverCapital.subtract(creditAmount);
                                             assignPay = borrowRecover.getRecoverInterestYes().subtract(assignPay);
                                             if((!assignCapital.equals(new BigDecimal("0.00"))) && (!assignPay.equals(new BigDecimal("0.00")))){
                                                 borrowRecover.setRecoverCapital(assignCapital);
                                                 borrowRecover.setRecoverInterestYes(assignPay);
                                               FddGenerateContractBean bean = new FddGenerateContractBean();
                                               bean.setBorrowNid(borrow_nid);//标的编号
                                               bean.setRepayPeriod(repay_period);//期数
                                               bean.setTransType(6);//交易类型
                                               bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                               bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                               bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                               JSONObject paramter = applyAgreementService.getNocreditParamterPlan(borrowRecover,bean,borrow);
                                               //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                               bean.setParamter(paramter); bean.setTeString(timeString); 
                                               this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                               agreements++;
                                               //垫付协议申请-协议生成详情
                                               ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                               applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                               applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                               applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                               applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                               applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                               applyAgreementInfo.setStatus(6);
                                               applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                             }
                                            
                                     }
                                     
                                 }else {//非债转   
                                   //垫付协议申请-协议生成详情
                                     ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                     applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                     applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                     applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                     applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                     applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                     applyAgreementInfo.setStatus(5);
                                     applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                     FddGenerateContractBean bean = new FddGenerateContractBean();
                                     bean.setBorrowNid(borrow_nid);//标的编号
                                     bean.setRepayPeriod(repay_period);//期数
                                     bean.setTransType(6);//交易类型
                                     bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                     bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                     bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                     JSONObject paramter = applyAgreementService.getNocreditParamterPlan(borrowRecover,bean,borrow);
                                     //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                     bean.setParamter(paramter); bean.setTeString(timeString); 
                                     this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                     agreements++;
                              }   
                          }                             
                      }

                  }else {//不分期
                      //huiyingdai_borrow_recover(标的放款记录（出借人） 总表)
                      List<BorrowRecover> borrowRecoverList = borrowRecoverService.selectBorrowRecover(borrow_nid);
                      for (BorrowRecover borrowRecover : borrowRecoverList) {
                          // 合同编号,拼接
                          String nid = borrowRecover.getNid();
                          //String nid = borrow_nid+"-"+assign_nid;
                          BigDecimal creditAmount = borrowRecover.getCreditAmount();
                          BigDecimal recoverCapital = borrowRecover.getRecoverCapital();
                          if(creditAmount.compareTo(new BigDecimal("0.00"))==0){//非债转                                   
                            //垫付协议申请-协议生成详情
                              ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                              applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                              applyAgreementInfo.setRepayPeriod(repay_period);//期数
                              applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                              applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                              applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                              applyAgreementInfo.setStatus(5);
                              applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                              FddGenerateContractBean bean = new FddGenerateContractBean();
                              bean.setBorrowNid(borrow_nid);//标的编号
                              bean.setRepayPeriod(repay_period);//期数
                              bean.setTransType(6);//交易类型
                              bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                              bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                              bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                              JSONObject paramter = applyAgreementService.getNocreditParamter(borrowRecover,bean,borrow);
                              //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                              bean.setParamter(paramter); bean.setTeString(timeString); 
                              this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                              agreements++;
                          }else if ((creditAmount.compareTo(new BigDecimal("0.00"))==1) && (creditAmount.compareTo(recoverCapital)==0)) {//全部债转
                            //出让人-承接人hyjf_hjh_debt_credit_repay（nid-invest_order_id del_flag=0）-user_id(承接人的id)_List
                              List<HjhDebtCreditRepay> hjhDebtCreditRepayList = applyAgreementService.selectHjhDebtCreditRepayByExample(nid,repay_period);
                              for (HjhDebtCreditRepay hjhDebtCreditRepay : hjhDebtCreditRepayList) {
                                //垫付协议申请-协议生成详情
                                  ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                  applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                  applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                  applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+hjhDebtCreditRepay.getUniqueNid());//合同编号
                                  applyAgreementInfo.setUserId(repayOrgUserId+"");//承接人-担保机构
                                  applyAgreementInfo.setCreditUserId(hjhDebtCreditRepay.getCreateUserId()+"");//出借人(出让人)
                                  applyAgreementInfo.setStatus(6);
                                  applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                  FddGenerateContractBean bean = new FddGenerateContractBean();
                                  bean.setBorrowNid(borrow_nid);//标的编号
                                  bean.setRepayPeriod(repay_period);//期数
                                  bean.setTransType(6);//交易类型
                                  bean.setTenderType(1);//出借类型 0：原始 1：债转 2 :计划
                                  bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                  bean.setCreditUserID(hjhDebtCreditRepay.getCreateUserId());//出讓人
                                  JSONObject paramter = applyAgreementService.getAllcreditParamterHjh(hjhDebtCreditRepay,bean,borrow);
                                 
                                  bean.setParamter(paramter); bean.setTeString(timeString); 
                                  this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                  agreements++;
                              }
                          }else if ((creditAmount.compareTo(new BigDecimal("0.00"))==1) && (creditAmount.compareTo(recoverCapital)==-1))  {//部分债转
                            //出让人-承接人hyjf_hjh_debt_credit_repay（nid-invest_order_id del_flag=0）-user_id(承接人的id)_List
                              List<HjhDebtCreditRepay> hjhDebtCreditRepayList = applyAgreementService.selectHjhDebtCreditRepayByExample(nid,repay_period);
                              BigDecimal assignPay  = new BigDecimal("0.00");
                              for (HjhDebtCreditRepay creditRepay : hjhDebtCreditRepayList) {
                                  //assignPay = assignPay.add(creditRepay.getRepayInterest());
                                  assignPay = assignPay.add(creditRepay.getRepayInterest()).add(creditRepay.getRepayAdvanceInterest()).add(creditRepay.getRepayLateInterest()).add(creditRepay.getRepayDelayInterest());
                                  FddGenerateContractBean bean = new FddGenerateContractBean();
                                //垫付协议申请-协议生成详情
                                  ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                  applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                  applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                  applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+creditRepay.getUniqueNid());//合同编号
                                  applyAgreementInfo.setUserId(repayOrgUserId+"");//承接人-担保机构
                                  applyAgreementInfo.setCreditUserId(creditRepay.getCreateUserId()+"");//出借人(出让人)
                                  applyAgreementInfo.setStatus(6);
                                  applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                                  bean.setBorrowNid(borrow_nid);//标的编号
                                  bean.setRepayPeriod(repay_period);//期数
                                  bean.setTransType(6);//交易类型
                                  bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                  bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                  bean.setCreditUserID(creditRepay.getCreateUserId());//出讓人
                                  JSONObject paramter = applyAgreementService.getAllcreditParamterHjh(creditRepay,bean,borrow);
                                  //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                  bean.setParamter(paramter); bean.setTeString(timeString); 
                                  this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                  agreements++;
                              }
                              /***************剩余部分********/
                              BigDecimal assignCapital = recoverCapital.subtract(creditAmount);
                              assignPay = borrowRecover.getRecoverInterestYes().subtract(assignPay);
                              if((assignCapital.compareTo(new BigDecimal("0.00"))!=1) && (assignPay.compareTo(new BigDecimal("0.00"))!=1)){
                                  borrowRecover.setRecoverCapital(assignCapital);
                                  borrowRecover.setRecoverInterestYes(assignPay);
                                  FddGenerateContractBean bean = new FddGenerateContractBean();
                                  bean.setBorrowNid(borrow_nid);//标的编号
                                  bean.setRepayPeriod(repay_period);//期数
                                  bean.setTransType(6);//交易类型
                                  bean.setTenderType(2);//出借类型 0：原始 1：债转 2 :计划
                                  bean.setTenderUserId(repayOrgUserId);//出借人-承接人（担保机构）
                                  bean.setCreditUserID(borrowRecover.getUserId());//出讓人
                                  JSONObject paramter = applyAgreementService.getNocreditParamter(borrowRecover,bean,borrow);
                                  //出让人-承接人huiyingdai_credit_repay（nid-credit_tender_nid）-user_id(承接人的id)_List
                                  bean.setParamter(paramter); bean.setTeString(timeString); 
                                  this.rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_GENERATE_CONTRACT, JSONObject.toJSONString(bean));
                                  agreements++;
                                  //垫付协议申请-协议生成详情
                                  ApplyAgreementInfo applyAgreementInfo = new ApplyAgreementInfo();
                                  applyAgreementInfo.setBorrowNid(borrow_nid);//项目编号
                                  applyAgreementInfo.setRepayPeriod(repay_period);//期数
                                  applyAgreementInfo.setContractId(timeString+"-"+repay_period+"-"+borrowRecover.getNid());//合同编号
                                  applyAgreementInfo.setUserId(repayOrgUserId+"");//出借人(出让人)
                                  applyAgreementInfo.setCreditUserId(borrowRecover.getUserId()+"");//承接人-担保机构
                                  applyAgreementInfo.setStatus(6);
                                  applyAgreementService.saveApplyAgreementInfo(applyAgreementInfo);
                              }
                          }
                        
                      } 
                  }
              }   
          } 
        ApplyAgreement applyAgreement = new ApplyAgreement();
        applyAgreement.setBorrowNid(borrow_nid);
        applyAgreement.setRepayPeriod(repay_period);
        applyAgreement.setApplyUserId(ShiroUtil.getLoginUserId());
        applyAgreement.setApplyUserName(ShiroUtil.getLoginUsername());
        applyAgreement.setAgreementNumber(agreements);
        applyAgreement.setStatus(1);
        applyAgreement.setCreateTime(GetDate.getNowTime10());
        applyAgreement.setDelFlg(0);
        ApplyAgreementExample example = new ApplyAgreementExample();//垫付协议申请记录
        ApplyAgreementExample.Criteria criteria = example.createCriteria();
        criteria.andBorrowNidEqualTo(borrow_nid);
        criteria.andRepayPeriodEqualTo(repay_period);
        List<ApplyAgreement> applyAgreements = this.applyAgreementService.selectApplyAgreement(example);
        if(applyAgreements!=null && applyAgreements.size()>0){
            applyAgreement.setId(applyAgreements.get(0).getId());
            applyAgreement.setUpdateTime(GetDate.getNowTime10());
            applyAgreement.setUpdateUser(Integer.valueOf(ShiroUtil.getLoginUserId()));
            this.applyAgreementService.updateByPrimaryKey(applyAgreement);
        }else {
            this.applyAgreementService.insert(applyAgreement);
        }
        ret.put(ApplyAgreementDefine.JSON_RESULT_KEY, "操作成功,签署MQ已发送");
        ret.put(ApplyAgreementDefine.JSON_STATUS_KEY, ApplyAgreementDefine.JSON_STATUS_OK);
        LogUtil.endLog(ApplyAgreementDefine.class.toString(), ApplyAgreementDefine.PDF_SIGN_ACTION);
        return ret.toString();
    }
    
    /**
     * 下载文件签署
     * @param request
     * @return
     */
    @RequestMapping(value = ApplyAgreementDefine.DOW_ACTION)
    public void downloadAction(HttpServletRequest request, HttpServletResponse response) {
        String status = request.getParameter("status");//1:脱敏，0：原始
        String assignNid = timeString+"-"+request.getParameter("period")+"-";
        String nid = request.getParameter("nid");
        List<TenderAgreement> tenderAgreementsAss= fddGenerateContractService.selectLikeByExample(assignNid+"%",nid);//债转协议
        //输出文件集合
        List<File> files = new ArrayList<File>();
        for (TenderAgreement tenderAgreement : tenderAgreementsAss) {
            if(tenderAgreementsAss!=null && tenderAgreementsAss.size()>0){
                if("1".equals(status)){
                    files = createFaddPDFImgFile(files,tenderAgreement);
                }else {
                    if(StringUtils.isNotBlank(tenderAgreement.getDownloadUrl())){
                        File filePdf = null;
                        try {
                            filePdf = FileUtil.getFile(request,response,tenderAgreement.getDownloadUrl(),tenderAgreement.getTenderNid()+".pdf");
                        } catch (IOException e) {
                            filePdf = null;
                        }//债转协议
                        if(filePdf!=null){
                            files.add(filePdf);
                        }
                    } 
                } 
            }
        }
        if(files!=null && files.size()>0){
            ZIPGenerator.generateZip(response, files, assignNid);
        }else{
            LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "下载失败，请稍后重试。。。。");
            return ;

        }   
    }
    /**
     * 下载法大大协议 __垫付
     * 
     * @param nid
     * @param files
     * @param tenderAgreement
     * 返回 0:下载成功；1:下载失败；2:没有生成法大大合同记录
     */
    public List<File> createFaddPDFImgFile(List<File> files,TenderAgreement tenderAgreement) {
        SFTPParameter para = new SFTPParameter() ;
        String ftpIP = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_IP);
        String port = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PORT);
        String basePathPdf = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_PDF);
        String password = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PASSWORD);
        String username = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_USERNAME);
        para.hostName = ftpIP;//ftp服务器地址
        para.userName = username;//ftp服务器用户名
        para.passWord = password;//ftp服务器密码
        para.port = Integer.valueOf(port);//ftp服务器端口
        para.fileName=tenderAgreement.getTenderNid();
        para.savePath = "/pdf_tem/pdf/" + tenderAgreement.getTenderNid();
        String imgUrl = tenderAgreement.getImgUrl();
        String pdfUrl = tenderAgreement.getPdfUrl();
        //获取文件目录
        int index = pdfUrl.lastIndexOf("/");
        String pdfPath = pdfUrl.substring(0,index);
        //文件名称
        String pdfName = pdfUrl.substring(index+1);
        para.downloadPath = basePathPdf + "/" + pdfPath;
        para.sftpKeyFile = pdfName;
       
        File file =  FavFTPUtil.downloadDirectory(para);
        files.add(file);
        return files;
    }
}
