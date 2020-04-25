package com.hyjf.web.agreement;

import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.bank.service.user.credit.CreditAssignedBean;
import com.hyjf.bank.service.user.credit.CreditService;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.*;
import com.hyjf.common.zip.ZIPGenerator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
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
import com.hyjf.web.user.planinfo.PlanInfoDefine;
import com.hyjf.web.user.planinfo.PlanInfoService;
import com.hyjf.web.util.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 网站协议下载控制器
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年6月5日
 * @see 下午5:27:14
 */

@Controller
@RequestMapping(value = CreateAgreementDefine.REQUEST_MAPPING)
public class CreateAgreementController extends BaseController {
	
	@Autowired
	private CreditService tenderCreditService;
	@Autowired
	private MyTenderService mytenderService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private PlanInfoService planInfoService;
	
	@Autowired
    private HjhPlanService hjhPlanService;
	
	@Autowired
	private FddGenerateContractService fddGenerateContractService;
	
	
	/**
	 * 生成债权转让协议的PDF文件下载 --散标
	 * 需要下载债转和居间协议，法大大需要区分脱敏和原始pdf
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = CreateAgreementDefine.CREDIT_TRANSFER_AGREEMENT_PDF, method = RequestMethod.GET)
	public void userCreditContractToSealPDF(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CreditAssignedBean tenderCreditAssignedBean) {
		LogUtil.startLog(CreateAgreementController.class.toString(), CreateAgreementDefine.CREDIT_CONTRACT);
		Integer currentUserId = null;
		//获取当前登录这用户id
		currentUserId = WebUtils.getUserId(request);
		String borrowNid = tenderCreditAssignedBean.getBidNid();
		String nid = tenderCreditAssignedBean.getCreditTenderNid();//原始标编号(取居间协议)
		String assignNid = tenderCreditAssignedBean.getAssignNid();//承接订单号(取债转协议)
		String flag = "1";
		// 出让人userid
        String creditUserId = tenderCreditAssignedBean.getCreditUserId();
		//输出文件集合
        List<File> files = new ArrayList<File>();
		TenderAgreement tenderAgreement = new TenderAgreement();
		List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(nid);//居间协议
		List<TenderAgreement> tenderAgreementsAss= fddGenerateContractService.selectByExample(assignNid);//债转协议
        if(tenderAgreementsAss!=null && tenderAgreementsAss.size()>0){
            tenderAgreement = tenderAgreementsAss.get(0);
            //下载法大大协议--债转
            if(tenderAgreement!=null){
                /** 脱敏规则三期  
                 *  出借债转：可以看到脱敏后的债权转让协议，出让人和承接人信息（姓名、证件号、盖章）均为脱敏后的信息*/
                files = createFaddPDFImgFile(files,tenderAgreement);//下载脱敏
            }
            //下载法大大协议--居间
            if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
                tenderAgreement = tenderAgreementsNid.get(0);
                if(tenderAgreement!=null){
                    /** 脱敏规则三期  
                     *  出借债转：可以看到脱敏后的债权转让协议，出让人和承接人信息（姓名、证件号、盖章）均为脱敏后的信息*/
                    files = createFaddPDFImgFile(files,tenderAgreement);//下载脱敏
                }
            } 
        }else{//下载平台老协议
            // 单个文件
            // 居间借款协议
            File file1;
            if (currentUserId != null && currentUserId.intValue() != 0) {
                if (StringUtils.isNotEmpty(borrowNid) && StringUtils.isNotEmpty(nid)) {
                    UserInvestListBean userInvestListBean = new UserInvestListBean();
                    userInvestListBean.setBorrowNid(borrowNid);
                    userInvestListBean.setNid(nid);
                    userInvestListBean.setFlag(flag);
                    userInvestListBean.setCreditUserId(creditUserId);
                    file1 = createAgreementPDFFile(request, response, userInvestListBean);
                    if (file1 != null) {
                        files.add(file1);
                    }
                }
            }

            //(2)债转协议
            ServletContext sc = request.getSession().getServletContext();
            String webInfoPath = sc.getRealPath("/WEB-INF"); // 值为D:\apache-tomcat-6.0.26\webapps\webmonitor
            // 把路径中的反斜杠转成正斜杠
            webInfoPath = webInfoPath.replaceAll("\\\\", "/"); // 值为D:/apache-tomcat-6.0.26/webapps/webmonitor
            
            try {
                //以下参数为下载居间借款协议使用
                //borrow表的borrowNid ：tenderCreditAssignedBean.getBidNid()
                //huiyingdai_borrow_tender 的 nid : tenderCreditAssignedBean.getCreditTenderNid()
                if (currentUserId != null && currentUserId.intValue() != 0) {
                    if (StringUtils.isEmpty(tenderCreditAssignedBean.getBidNid()) || StringUtils.isEmpty(tenderCreditAssignedBean.getCreditNid())
                            || StringUtils.isEmpty(tenderCreditAssignedBean.getCreditTenderNid()) || StringUtils.isEmpty(tenderCreditAssignedBean.getAssignNid())) {
                        LogUtil.endLog(CreateAgreementController.class.toString(), CreateAgreementDefine.CREDIT_CONTRACT);
                        return;
                    }
                    //将当前登陆者的ID传送
                    tenderCreditAssignedBean.setCurrentUserId(currentUserId);
                    // 模板参数对象
                    Map<String, Object> creditContract = tenderCreditService.selectUserCreditContract(tenderCreditAssignedBean);
                    // 临时文件夹生成PDF文件
                    //generatePdfFile
                    //原：PdfGenerator.generatePdf(request, response, ((CreditTender) creditContract.get("creditTender")).getAssignNid() + ".pdf", CustomConstants.CREDIT_CONTRACT, creditContract);
                    File file2 = PdfGenerator.generatePdfFile(request, response, ((CreditTender) creditContract.get("creditTender")).getAssignNid() + ".pdf", CustomConstants.CREDIT_CONTRACT, creditContract);
                    if(file2!=null){
                        files.add(file2);
                    }
                } else {
                    LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "用户未登录");
                    return;
                }
            } catch (Exception e) {
                LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "系统异常");
                return ;
            }
        }
		if(files!=null && files.size()>0){
		    ZIPGenerator.generateZip(response, files, tenderCreditAssignedBean.getBidNid());
		}else{
		    LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "下载失败，请稍后重试。。。。");
		    return ;

		}
		LogUtil.endLog(CreateAgreementController.class.toString(), CreateAgreementDefine.TENDER_TO_CREDIT_DETAIL);
        return ;
	}

	
	
	
	/**
     * 债权还款计划导出PDF文件 
     * 
     * @param request
     * @param response
     * @param form
     */
    @RequestMapping(value = CreateAgreementDefine.CREDIT_DIARY, method = RequestMethod.GET)
    public void createDiaryPDF(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute UserInvestListBean form) {
        LogUtil.startLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台借款协议）");
        if (form.getBorrowNid() == null || "".equals(form.getBorrowNid().trim())) {
            System.out.println("标的信息不存在,下载汇盈金服互联网金融服务平台借款协议PDF失败。");
            return;
        }
       
        // 查询借款人用户名
        BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
        // 借款编码
        borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
        List<BorrowCustomize> recordList1 = mytenderService.selectBorrowList(borrowCommonCustomize);
       
        List<BorrowCustomize> recordList = recordList1;
        Map<String, Object> contents = new HashMap<String, Object>();
        
        contents.put("borrowNid", form.getBorrowNid());
        contents.put("nid", form.getNid());
        // 借款人用户名
        if(recordList.size()>0){
            contents.put("record", recordList.get(0));
            // 借款人用户名
            int userIds = recordList.get(0).getUserId();
            UsersInfo userInfo=planInfoService.getUsersInfoByUserId(userIds);
            contents.put("borrowUsername", userInfo.getTruename());
            contents.put("idCard", userInfo.getIdcard());
            contents.put("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间
            /*BorrowRecoverExample example = new BorrowRecoverExample();
            BorrowRecoverExample.Criteria borrowRecoverCra = example.createCriteria();
            borrowRecoverCra.andAccedeOrderIdEqualTo(form.getAssetNumber());
            List<BorrowRecover> recovers = mytenderService.selectBorrowRecover(example );
            if(recovers.size()>0){
                contents.put("recoverAccount", recovers.get(0).getRecoverAccount());
            }else {
                contents.put("recoverAccount", null);
            }*/
            contents.put("recoverAccount", form.getAccount());
            if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
                // 最后一笔的放款完成时间 (协议签订日期)
                contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
            } else {
                // 设置为满标时间
                contents.put("recoverTime", recordList.get(0).getReverifyTime());
            }
        }
        // 用户ID
        Integer userId = WebUtils.getUserId(request);
        form.setUserId(userId.toString());
        // 用户出借列表
        List<WebUserInvestListCustomize> tzList = mytenderService.selectUserInvestList(form, 0, 100);
        if (tzList != null && tzList.size() > 0) {
            contents.put("userInvest", tzList.get(0));
        }
        // 导出PDF文件
        try {
            PdfGenerator.generatePdf(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
                    CustomConstants.NEW_DIARY_CONTRACT, contents);
        } catch (Exception e) {
            LogUtil.errorLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
            e.printStackTrace();
        }
        LogUtil.endLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）");

    }
	
	
    
    /**
     * 下载汇盈金服互联网金融服务平台居间服务协议—————— 借款人
     * 
     * @param request
     * @param response
     * @param form
     */
    @RequestMapping(value = CreateAgreementDefine.CREDIT_PAYMENT_PLAN_REPAY, method = RequestMethod.GET)
    public File createAgreementPDFFileRepay(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute UserInvestListBean form) {
        LogUtil.startLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）");
        if (form.getBorrowNid() == null || "".equals(form.getBorrowNid().trim())) {
            System.out.println("标的信息不存在,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
            return null;
        }
        // 查询借款人用户名
        BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
        // 借款编码
        borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
        List<BorrowCustomize> recordList1 = mytenderService.selectBorrowList(borrowCommonCustomize);
        // 出让人userid
        TenderAgreement tenderAgreement = new TenderAgreement();
        List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(form.getNid());//居间协议
        File filePdf= null;
         WebViewUser loInfo = WebUtils.getUser(request);
        //下载法大大协议--居间
        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
            tenderAgreement = tenderAgreementsNid.get(0);
            if(tenderAgreement!=null){
                filePdf = createFaddPDFImgFileOne(tenderAgreement);//下载脱敏 
            }
            return filePdf;
        }
     // 融通宝静态打印
        if (StringUtils.isNotEmpty(form.getProjectType())) {
            if (form.getProjectType().equals("13")) {
                Map<String, Object> contents = new HashMap<String, Object>();
                // 用户ID
                Integer userId = WebUtils.getUserId(request);
                UsersInfo userinfo = mytenderService.getUsersInfoByUserId(userId);
                form.setUserId(userId + "");
                List<WebUserInvestListCustomize> invest = mytenderService.selectUserInvestList(form, 0, 10);
                if (invest != null && invest.size() > 0) {
                    contents.put("investDeatil", invest.get(0));
                }
                WebProjectDetailCustomize borrow = this.projectService.selectProjectDetail(form.getBorrowNid());
                contents.put("projectDeatil", borrow);
                contents.put("truename", userinfo.getTruename());
                contents.put("idcard", userinfo.getIdcard());
                 System.out.println("222------------------------idCard:"+userinfo.getIdcard());
                contents.put("borrowNid", form.getBorrowNid());// 标的号
                contents.put("nid", form.getNid());// 标的号
                contents.put("assetNumber", form.getAssetNumber());// 资产编号
                contents.put("projectType", form.getProjectType());// 项目类型
                String moban = CustomConstants.RTB_TENDER_CONTRACT;
                if (borrow != null && borrow.getBorrowPublisher() != null && borrow.getBorrowPublisher().equals("中商储")) {
                    moban = CustomConstants.RTB_TENDER_CONTRACT_ZSC;
                }
                // 导出PDF文件
                try {
                    String flag = form.getFlag();
                    if(flag!=null && flag=="1"){
                        File file  = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + ".pdf", moban, contents);
                        return file;
                    }else {
                        PdfGenerator.generatePdf(request, response, form.getBorrowNid() + ".pdf", moban, contents);
                    }
                    
                } catch (Exception e) {
                    LogUtil.errorLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
                    e.printStackTrace();
                }
            }
        } else {
            
            // 判断是否是汇添金专属标的导出
            if (recordList1.size() != 1) {
                Map<String, Object> contents = new HashMap<String, Object>();
                // 查询借款人用户名
                DebtBorrowCommonCustomize debtBorrowCommonCustomize = new DebtBorrowCommonCustomize();
                // 借款编码
                debtBorrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
                List<DebtBorrowCustomize> recordList = planInfoService.selectBorrowList(debtBorrowCommonCustomize);
                if (recordList.size() != 1) {
                    System.out.println("标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
                    return null;
                }
                contents.put("borrowNid", form.getBorrowNid());
                contents.put("nid", form.getNid());
                // 借款人用户名
                int userIds = recordList.get(0).getUserId();
                UsersInfo userInfo=planInfoService.getUsersInfoByUserId(userIds);
                contents.put("borrowUsername", userInfo.getTruename());
                contents.put("idCard", userInfo.getIdcard());
                System.out.println("333------------------------idCard:"+userInfo.getIdcard());
                contents.put("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间
                if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
                    // 最后一笔的放款完成时间 (协议签订日期)
                    contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
                } else {
                    // 设置为满标时间
                    contents.put("recoverTime", recordList.get(0).getReverifyTime());
                }
                // 用户ID
                Integer userId = WebUtils.getUserId(request);
                form.setUserId(userId.toString());
                // 用户出借列表
                List<WebUserInvestListCustomize> tzList = planInfoService.selectUserInvestList(form, 0, 100);
                if (tzList != null && tzList.size() > 0) {
                    contents.put("userInvest", tzList.get(0));
                }

                // 如果是分期还款，查询分期信息
                String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
                if (borrowStyle != null) {
                    if ("month".equals(borrowStyle) || "principal".equals(borrowStyle)
                            || "endmonth".equals(borrowStyle)) {
                        ProjectRepayListBean bean = new ProjectRepayListBean();
                        bean.setUserId(WebUtils.getUserId(request).toString());
                        bean.setBorrowNid(form.getBorrowNid());
                        bean.setNid(form.getNid());
                        int recordTotal = this.planInfoService.countProjectRepayPlanRecordTotal(bean);
                        if (recordTotal > 0) {
                            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
                            List<WebProjectRepayListCustomize> fqList = planInfoService.selectProjectRepayPlanList(
                                    bean, paginator.getOffset(), paginator.getLimit());
                            contents.put("paginator", paginator);
                            contents.put("repayList", fqList);
                        } else {
                            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
                            contents.put("paginator", paginator);
                            contents.put("repayList", "");
                        }
                    }
                }

                // 导出PDF文件
                try {
                    
                    String flag = form.getFlag();
                    if(flag!=null && flag=="1"){
                        File file  =  PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
                                CustomConstants.TENDER_CONTRACT, contents);
                       return file;
                    }else {
                        PdfGenerator.generatePdf(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
                                CustomConstants.TENDER_CONTRACT, contents);
                    }
                    
                } catch (Exception e) {
                    LogUtil.errorLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
                    e.printStackTrace();
                }
            } else {
                List<BorrowCustomize> recordList = recordList1;
                Map<String, Object> contents = new HashMap<String, Object>();
                contents.put("record", recordList.get(0));
                contents.put("borrowNid", form.getBorrowNid());
                contents.put("nid", form.getNid());
                
                contents.put("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间                
                if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
                    // 最后一笔的放款完成时间 (协议签订日期)
                    contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
                } else {
                    // 设置为满标时间
                    contents.put("recoverTime", recordList.get(0).getReverifyTime());
                }
                // 用户ID
                Integer userId = WebUtils.getUserId(request);
                
                //承接和非承接做了判断
                String flag = form.getFlag();
                // 借款人用户名
                int userIds = recordList.get(0).getUserId();
                UsersInfo userInfo=planInfoService.getUsersInfoByUserId(userIds);
                String borrowUsername = userInfo.getTruename();
                System.out.println("2221------------------------idCard:"+userInfo.getIdcard());
                if(flag!=null && flag=="1"){
                    form.setUserId(form.getCreditUserId());
                    List<WebUserInvestListCustomize> tzList = mytenderService.selectUserInvestList(form, 0, 100);
                    if (tzList != null && tzList.size() > 0) {
                        WebUserInvestListCustomize userInvest = tzList.get(0);
                        if(loInfo!=null&&!((loInfo.getUserId()+"").equals(userInvest.getUserId()))){
                      
                            userInvest.setRealName(userInvest.getRealName().substring(0,1)+"**");
                            userInvest.setUsername(userInvest.getUsername().substring(0,1)+"*****");
                            userInvest.setIdCard(userInvest.getIdCard().substring(0,4)+"**************");
                        }
                        contents.put("userInvest", userInvest);
                    }else {
                        return null;
                    }
                 } else {
                     form.setUserId(userId.toString());
                     List<WebUserInvestListCustomize> tzList = mytenderService.selectUserInvestList(form, 0, 100);
                    if (tzList != null && tzList.size() > 0) {
                        WebUserInvestListCustomize userInvest = tzList.get(0);
                        if(loInfo!=null&&!((loInfo.getUserId()+"").equals(userInvest.getUserId()))){
                            userInvest.setRealName(userInvest.getRealName().substring(0,1)+"**");
                            userInvest.setUsername(userInvest.getUsername().substring(0,1)+"*****");
                            userInvest.setIdCard(userInvest.getIdCard().substring(0,4)+"**************");
                        }
                        contents.put("userInvest", userInvest);
                    }else {
                        return null;
                    }
                 }
                 if(!(loInfo.getUserId()+"").equals(userInfo.getUserId()+"") ){
                     borrowUsername = borrowUsername.substring(0,1)+"**";
                     
                 }
                 contents.put("borrowUsername", borrowUsername);
                // 如果是分期还款，查询分期信息
                String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
                if (borrowStyle != null) {
                  //计算预期收益
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
                        // 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷365*锁定期；
                        earnings = DuePrincipalAndInterestUtils.getDayInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                    } else {
                        // 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
                        earnings = DuePrincipalAndInterestUtils.getMonthInterest(account, borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);

                    }
                    contents.put("earnings", earnings);
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
                            contents.put("paginator", paginator);
                            contents.put("repayList", fqList);
                        } else {
                            Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
                            contents.put("paginator", paginator);
                            contents.put("repayList", "");
                        }
                    }
                }

                // 导出PDF文件
                try {
                    String flag1 = form.getFlag();
                    if(flag1!=null && flag1=="1"){
                         File file  =  PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
                                CustomConstants.TENDER_CONTRACT, contents);
                       return file;
                    }else {
                        PdfGenerator.generatePdf(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
                                CustomConstants.TENDER_CONTRACT, contents);
                    }
                    
                } catch (Exception e) {
                    LogUtil.errorLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
                    e.printStackTrace();
                }

            }
        }

        LogUtil.endLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）");
        return null;
    }
    
	/**
	 * 下载脱敏后居间服务借款协议（原始标的）_计划出借人
	 * 
	 * @param request
	 * @param response
	 * @param form
	 */
	@RequestMapping(value = CreateAgreementDefine.CREDIT_PAYMENT_PLAN, method = RequestMethod.GET)
	public File createAgreementPDFFile(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute UserInvestListBean form) {
		LogUtil.startLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）");
		if (form.getBorrowNid() == null || "".equals(form.getBorrowNid().trim())) {
			System.out.println("标的信息不存在,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
			return null;
		}
		// 查询借款人用户名
        BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
        // 借款编码
        borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
        List<BorrowCustomize> recordList1 = mytenderService.selectBorrowList(borrowCommonCustomize);
        //输出文件集合
        List<File> files = new ArrayList<File>();
        TenderAgreement tenderAgreement = new TenderAgreement();
        List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(form.getNid());//居间协议
        
         WebViewUser loInfo = WebUtils.getUser(request);
        //下载法大大协议--居间
        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
            tenderAgreement = tenderAgreementsNid.get(0);
            if(tenderAgreement!=null){
                if(recordList1.size()>0){
                    files = createFaddPDFImgFile(files,tenderAgreement);//下载脱敏
                }
                if(files!=null && files.size()>0){
                    ZIPGenerator.generateZip(response, files, form.getNid());
                    
                }
                return null;
            }
        }
		// 融通宝静态打印
		if (StringUtils.isNotEmpty(form.getProjectType())) {
			if (form.getProjectType().equals("13")) {
				Map<String, Object> contents = new HashMap<String, Object>();
				// 用户ID
				Integer userId = WebUtils.getUserId(request);
				UsersInfo userinfo = mytenderService.getUsersInfoByUserId(userId);
				form.setUserId(userId + "");
				List<WebUserInvestListCustomize> invest = mytenderService.selectUserInvestList(form, 0, 10);
				if (invest != null && invest.size() > 0) {
					contents.put("investDeatil", invest.get(0));
				}
				WebProjectDetailCustomize borrow = this.projectService.selectProjectDetail(form.getBorrowNid());
				contents.put("projectDeatil", borrow);
				contents.put("truename", userinfo.getTruename());
				contents.put("idcard", userinfo.getIdcard());
				 System.out.println("222------------------------idCard:"+userinfo.getIdcard());
				contents.put("borrowNid", form.getBorrowNid());// 标的号
				contents.put("nid", form.getNid());// 标的号
				contents.put("assetNumber", form.getAssetNumber());// 资产编号
				contents.put("projectType", form.getProjectType());// 项目类型
				String moban = CustomConstants.RTB_TENDER_CONTRACT;
				if (borrow != null && borrow.getBorrowPublisher() != null && borrow.getBorrowPublisher().equals("中商储")) {
					moban = CustomConstants.RTB_TENDER_CONTRACT_ZSC;
				}
				// 导出PDF文件
				try {
				    String flag = form.getFlag();
				    if(flag!=null && flag=="1"){
				        File file  = PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + ".pdf", moban, contents);
				        return file;
				    }else {
				        PdfGenerator.generatePdf(request, response, form.getBorrowNid() + ".pdf", moban, contents);
                    }
					
				} catch (Exception e) {
					LogUtil.errorLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
					e.printStackTrace();
				}
			}
		} else {
			
			// 判断是否是汇添金专属标的导出
			if (recordList1.size() != 1) {
				Map<String, Object> contents = new HashMap<String, Object>();
				// 查询借款人用户名
				DebtBorrowCommonCustomize debtBorrowCommonCustomize = new DebtBorrowCommonCustomize();
				// 借款编码
				debtBorrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
				List<DebtBorrowCustomize> recordList = planInfoService.selectBorrowList(debtBorrowCommonCustomize);
				if (recordList.size() != 1) {
					System.out.println("标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
					return null;
				}
				contents.put("borrowNid", form.getBorrowNid());
				contents.put("nid", form.getNid());
				// 借款人用户名
				int userIds = recordList.get(0).getUserId();
		        UsersInfo userInfo=planInfoService.getUsersInfoByUserId(userIds);
		        contents.put("borrowUsername", userInfo.getTruename());
		        contents.put("idCard", userInfo.getIdcard());
		        System.out.println("333------------------------idCard:"+userInfo.getIdcard());
		        contents.put("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间
				if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
					// 最后一笔的放款完成时间 (协议签订日期)
					contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
				} else {
					// 设置为满标时间
					contents.put("recoverTime", recordList.get(0).getReverifyTime());
				}
				// 用户ID
				Integer userId = WebUtils.getUserId(request);
				form.setUserId(userId.toString());
				// 用户出借列表
				List<WebUserInvestListCustomize> tzList = planInfoService.selectUserInvestList(form, 0, 100);
				if (tzList != null && tzList.size() > 0) {
					contents.put("userInvest", tzList.get(0));
				}

				// 如果是分期还款，查询分期信息
				String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
				if (borrowStyle != null) {
					if ("month".equals(borrowStyle) || "principal".equals(borrowStyle)
							|| "endmonth".equals(borrowStyle)) {
						ProjectRepayListBean bean = new ProjectRepayListBean();
						bean.setUserId(WebUtils.getUserId(request).toString());
						bean.setBorrowNid(form.getBorrowNid());
						bean.setNid(form.getNid());
						int recordTotal = this.planInfoService.countProjectRepayPlanRecordTotal(bean);
						if (recordTotal > 0) {
							Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
							List<WebProjectRepayListCustomize> fqList = planInfoService.selectProjectRepayPlanList(
									bean, paginator.getOffset(), paginator.getLimit());
							contents.put("paginator", paginator);
							contents.put("repayList", fqList);
						} else {
							Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
							contents.put("paginator", paginator);
							contents.put("repayList", "");
						}
					}
				}

				// 导出PDF文件
				try {
				    
				    String flag = form.getFlag();
                    if(flag!=null && flag=="1"){
                        File file  =  PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
                                CustomConstants.TENDER_CONTRACT, contents);
                       return file;
                    }else {
                        PdfGenerator.generatePdf(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
                                CustomConstants.TENDER_CONTRACT, contents);
                    }
					
				} catch (Exception e) {
					LogUtil.errorLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
					e.printStackTrace();
				}
			} else {
				List<BorrowCustomize> recordList = recordList1;
				Map<String, Object> contents = new HashMap<String, Object>();
				contents.put("record", recordList.get(0));
				contents.put("borrowNid", form.getBorrowNid());
				contents.put("nid", form.getNid());
				
                contents.put("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间				
				if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
					// 最后一笔的放款完成时间 (协议签订日期)
					contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
				} else {
					// 设置为满标时间
					contents.put("recoverTime", recordList.get(0).getReverifyTime());
				}
				// 用户ID
				Integer userId = WebUtils.getUserId(request);
				
				//承接和非承接做了判断
				String flag = form.getFlag();
				// 借款人用户名
                int userIds = recordList.get(0).getUserId();
                UsersInfo userInfo=planInfoService.getUsersInfoByUserId(userIds);
                String borrowUsername = userInfo.getTruename();
                System.out.println("2221------------------------idCard:"+userInfo.getIdcard());
				if(flag!=null && flag=="1"){
					form.setUserId(form.getCreditUserId());
					List<WebUserInvestListCustomize> tzList = mytenderService.selectUserInvestList(form, 0, 100);
					if (tzList != null && tzList.size() > 0) {
					    WebUserInvestListCustomize userInvest = tzList.get(0);
					    if(loInfo!=null&&!((loInfo.getUserId()+"").equals(userInvest.getUserId()))){
					  
    					    userInvest.setRealName(userInvest.getRealName().substring(0,1)+"**");
    					    userInvest.setUsername(userInvest.getUsername().substring(0,1)+"*****");
    					    userInvest.setIdCard(userInvest.getIdCard().substring(0,4)+"**************");
					    }
						contents.put("userInvest", userInvest);
					}else {
                        return null;
                    }
				 } else {
					 form.setUserId(userId.toString());
					 List<WebUserInvestListCustomize> tzList = mytenderService.selectUserInvestList(form, 0, 100);
					if (tzList != null && tzList.size() > 0) {
					    WebUserInvestListCustomize userInvest = tzList.get(0);
					    if(loInfo!=null&&!((loInfo.getUserId()+"").equals(userInvest.getUserId()))){
					        userInvest.setRealName(userInvest.getRealName().substring(0,1)+"**");
                            userInvest.setUsername(userInvest.getUsername().substring(0,1)+"*****");
                            userInvest.setIdCard(userInvest.getIdCard().substring(0,4)+"**************");
					    }
						contents.put("userInvest", userInvest);
					}else {
                        return null;
                    }
				 }
				 if(!(loInfo.getUserId()+"").equals(userInfo.getUserId()+"") ){
				     borrowUsername = borrowUsername.substring(0,1)+"**";
				     
				 }
				 contents.put("borrowUsername", borrowUsername);
				// 如果是分期还款，查询分期信息
				String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
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
							contents.put("paginator", paginator);
							contents.put("repayList", fqList);
						} else {
							Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
							contents.put("paginator", paginator);
							contents.put("repayList", "");
						}
					}
				}

				// 导出PDF文件
				try {
				    String flag1 = form.getFlag();
                    if(flag1!=null && flag1=="1"){
                         File file  =  PdfGenerator.generatePdfFile(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
                                CustomConstants.TENDER_CONTRACT, contents);
                       return file;
                    }else {
                        PdfGenerator.generatePdf(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
                                CustomConstants.TENDER_CONTRACT, contents);
                    }
					
				} catch (Exception e) {
					LogUtil.errorLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
					e.printStackTrace();
				}

			}
		}

		LogUtil.endLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）");
        return null;

	}
	
	
	/**
	 * 汇计划还款计划导出PDF文件 
	 * 
	 * @param request
	 * @param response
	 * @param planOrderId
	 */
	@RequestMapping(value = CreateAgreementDefine.PLAN_PAYMENT_PLAN, method = RequestMethod.GET)
	public void createAgreementPDF(String planNid,String planOrderId, HttpServletRequest request,
			HttpServletResponse response) {
		LogUtil.startLog(CreateAgreementController.class.toString(), "createAgreementPDF 汇添金导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）");
		WebViewUser wuser = WebUtils.getUser(request);
		if (wuser == null) {
			System.out.println("用户信息失效，请您重新登录。");
			return;
		}
		Integer userId = wuser.getUserId();
		if (planNid == null || "".equals(planNid.trim())||planOrderId == null || "".equals(planOrderId.trim())) {
			System.out.println("计划编号或计划订单号不存在，请重新查证。");
			return;
		}
		DebtPlanAccede debtPlanAccede=new DebtPlanAccede();
		debtPlanAccede.setPlanNid(planNid);
		debtPlanAccede.setAccedeOrderId(planOrderId);
		List<DebtPlan> debtPlanList=planInfoService.getPlanByPlanNid(planNid);
		if (debtPlanList!=null&&debtPlanList.size()>0) {
			Map<String, Object> contents = new HashMap<String, Object>();
			DebtPlan debtPlan=debtPlanList.get(0);
			if (debtPlan.getFullExpireTime()!=null&&debtPlan.getFullExpireTime()!=0) {
				contents.put("fullDate", GetDate.timestamptoStrYYYYMMDD(debtPlan.getFullExpireTime()));
			}else {
				contents.put("fullDate", GetDate.timestamptoStrYYYYMMDD(debtPlan.getBuyEndTime()));
			}
			contents.put("debtPlan", debtPlan);
			//1基本信息
			Map<String ,Object> params=new HashMap<String ,Object>();
			params.put("accedeOrderId", planOrderId);
			params.put("userId", userId);
			UsersInfo userInfo=planInfoService.getUsersInfoByUserId(userId);
			contents.put("userInfo", userInfo);
			List<PlanLockCustomize> recordList = planInfoService.selectUserProjectListCapital(params);
			if (recordList!=null&&recordList.size()>0) {
				PlanLockCustomize planinfo=recordList.get(0);
				contents.put("planinfo", planinfo);
			}
			// 导出PDF文件
			try {
				PdfGenerator.generatePdf(request, response, planNid + "_" + planOrderId + ".pdf",
						CustomConstants.HTJ_INVEST_CONTRACT, contents);
			} catch (Exception e) {
				LogUtil.errorLog(PlanInfoDefine.THIS_CLASS, "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
				e.printStackTrace();
			}

		}
		LogUtil.endLog(PlanInfoDefine.THIS_CLASS, "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）");
	}
	

	/**
	 * 导出PDF文件 （汇添金出借计划服务协议）
	 * 
	 * @param request
	 * @param response
	 * @param planOrderId
	 */
	@RequestMapping(value = CreateAgreementDefine.HTJ_INVEST_PLAN_AGREEMENT_PDF, method = RequestMethod.GET)    
	public void htjInvestPlanAgreementPDF(String planNid,String planOrderId, HttpServletRequest request,
			HttpServletResponse response) {
		WebViewUser wuser = WebUtils.getUser(request);
		if (wuser == null) {
			System.out.println("用户信息失效，请您重新登录。");
			return;
		}
		Integer userId = wuser.getUserId();
		if (planNid == null || "".equals(planNid.trim())||planOrderId == null || "".equals(planOrderId.trim())) {
			System.out.println("计划编号或计划订单号不存在，请重新查证。");
			return;
		}
		DebtPlanAccede debtPlanAccede=new DebtPlanAccede();
		debtPlanAccede.setPlanNid(planNid);
		debtPlanAccede.setAccedeOrderId(planOrderId);
		List<DebtPlan> debtPlanList=planInfoService.getPlanByPlanNid(planNid);
		if (debtPlanList!=null&&debtPlanList.size()>0) {
			Map<String, Object> contents = new HashMap<String, Object>();
			DebtPlan debtPlan=debtPlanList.get(0);
			if (debtPlan.getFullExpireTime()!=null&&debtPlan.getFullExpireTime()!=0) {
				contents.put("fullDate", GetDate.timestamptoStrYYYYMMDD(debtPlan.getFullExpireTime()));
			}else {
				contents.put("fullDate", GetDate.timestamptoStrYYYYMMDD(debtPlan.getBuyEndTime()));
			}
			contents.put("debtPlan", debtPlan);
			//1基本信息
			Map<String ,Object> params=new HashMap<String ,Object>();
			params.put("accedeOrderId", planOrderId);
			params.put("userId", userId);
			UsersInfo userInfo=planInfoService.getUsersInfoByUserId(userId);
			contents.put("userInfo", userInfo);
			List<PlanLockCustomize> recordList = planInfoService.selectUserProjectListCapital(params);
			if (recordList!=null&&recordList.size()>0) {
				PlanLockCustomize planinfo=recordList.get(0);
				contents.put("planinfo", planinfo);
			}
			// 导出PDF文件
			try {
				PdfGenerator.generatePdf(request, response, planNid + "_" + planOrderId + ".pdf",
						CustomConstants.HTJ_INVEST_CONTRACT, contents);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	
	/**
     * 导出PDF文件 （汇盈金服互联网金融服务平台汇计划智投服务协议）
     * 
     * @param request
     * @param response
     * @param accedeOrderId
     */
    @RequestMapping(value = CreateAgreementDefine.NEW_HJH_INVEST_PLAN_AGREEMENT_PDF, method = RequestMethod.GET)    
    public void newHjhInvestPDF(String planNid,String accedeOrderId, HttpServletRequest request,
            HttpServletResponse response) {
        WebViewUser wuser = WebUtils.getUser(request);
        if (wuser == null) {
            System.out.println("用户信息失效，请您重新登录。");
            return;
        }
        Integer userId = wuser.getUserId();
        if (StringUtils.isEmpty(accedeOrderId)&&StringUtils.isEmpty(planNid)) {
            System.out.println("计划编号或计划订单号不存在，请重新查证。");
            return;
        }
        String nid = accedeOrderId;
        //输出文件集合
        List<File> files = new ArrayList<File>();
        TenderAgreement tenderAgreement = new TenderAgreement();
        List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(nid);//居间协议
        /*******************************下载法大大合同************************************/
        
        //下载法大大协议--债转
        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
            tenderAgreement = tenderAgreementsNid.get(0);
            try {
                if(StringUtils.isNotBlank(tenderAgreement.getDownloadUrl())){
                    File filePdf= FileUtil.getFile(request,response,tenderAgreement.getDownloadUrl(),nid+".pdf");//债转协议
                    if(filePdf!=null){
                        files.add(filePdf);
                    }
                }
                if(files!=null && files.size()>0){
                    ZIPGenerator.generateZip(response, files, nid);
                    
                }
             } catch (IOException e) {
                 LogUtil.infoLog(this.getClass().getName(), "newHjhInvestPDF", "下载失败，请稍后重试。。。。");
                 return;
             }
        }else{//下载原来的
            Map<String, Object> contents = new HashMap<String, Object>();
            //1基本信息
            Map<String ,Object> params=new HashMap<String ,Object>();
            params.put("accedeOrderId", accedeOrderId);
            params.put("userId", userId);
            UsersInfo userInfo=planInfoService.getUsersInfoByUserId(userId);
            contents.put("userInfo", userInfo);
            contents.put("username", wuser.getUsername());
            UserHjhInvistDetailCustomize userHjhInvistDetailCustomize = hjhPlanService.selectUserHjhInvistDetail(params);
            contents.put("userHjhInvistDetail", userHjhInvistDetailCustomize);
            // 导出PDF文件
            try {
                PdfGenerator.generatePdf(request, response, planNid + "_" + accedeOrderId + ".pdf",
                        CustomConstants.NEW_HJH_INVEST_CONTRACT, contents);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	/**
	 * 账户中心-债权管理-订单详情 债转被出借的协议PDF文件下载
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = CreateAgreementDefine.PLAN_CREDIT_CONTRACT_ASSIGN_SEAL_PDF, method = RequestMethod.GET)
	public void planuserCreditContractToSealPDF(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute CreditAssignedBean tenderCreditAssignedBean) {
		ServletContext sc = request.getSession().getServletContext();
		String webInfoPath = sc.getRealPath("/WEB-INF"); // 值为D:\apache-tomcat-6.0.26\webapps\webmonitor
		// 把路径中的反斜杠转成正斜杠
		webInfoPath = webInfoPath.replaceAll("\\\\", "/"); // 值为D:/apache-tomcat-6.0.26/webapps/webmonitor
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId == null) {
				LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "用户未登录");
				return;
			}
			if (StringUtils.isEmpty(tenderCreditAssignedBean.getCreditNid())
					|| StringUtils.isEmpty(tenderCreditAssignedBean.getCreditTenderNid())) {
				return;
			}
			// 模板参数对象
			Map<String, Object> creditContract = tenderCreditService.selectUserPlanCreditContract(tenderCreditAssignedBean);
			// 临时文件夹生成PDF文件
			PdfGenerator.generatePdf(request, response,
					((DebtCreditTender) creditContract.get("creditTender")).getAssignOrderId() + ".pdf",
					CustomConstants.PLAN_CREDIT_CONTRACT, creditContract);
		} catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "系统异常");
			e.printStackTrace();
			return;
		}
	}
	
	
	/**
	 * 账户中心-资产管理--散标--当前持有--出借协议(实际为散标居间协议)下载
	 * 
	 * @param request
	 * @param response
	 * @param form
	 */
	@RequestMapping(value = CreateAgreementDefine.INTERMEDIARY_SERVICE_AGREEMENT_PDF, method = RequestMethod.GET)
	public void downloadIntermediaryAgreementPDF(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute UserInvestListBean form) {
		LogUtil.startLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）");
		if (form.getBorrowNid() == null || "".equals(form.getBorrowNid().trim())) {
			System.out.println("标的信息不存在,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
			return;
		}
		// 查询借款人用户名
        BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
        // 借款编码
        borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
        List<BorrowCustomize> recordList1 = mytenderService.selectBorrowList(borrowCommonCustomize);
		String nid = form.getNid();
        //输出文件集合
		List<File> files = new ArrayList<File>();
        TenderAgreement tenderAgreement = new TenderAgreement();
        List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(nid);//居间协议
        /*******************************下载法大大合同************************************/
		//下载法大大协议--居间
        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
            
            tenderAgreement = tenderAgreementsNid.get(0);
            if(tenderAgreement!=null){
                files = createFaddPDFImgFile(files,tenderAgreement);//下载脱敏
                
                /** 脱敏规则三期  
                 * 出借散标：可以看到脱敏后的居间服务借款协议，出借人本人和借款人信息（姓名、证件号、盖章）均为脱敏后的信息*/
               /* Integer creditUserId =0;
                if(recordList1.size()>0){
                    creditUserId =recordList1.get(0).getUserId();
                }
                // 登录用户ID
                Integer userId = WebUtils.getUserId(request);
                if(!userId.equals(creditUserId)){//出借人不是当前登录用户
                    files = createFaddPDFImgFile(nid,files,tenderAgreement);//下载脱敏
                }else {//下载原始
                    try {
                        if(StringUtils.isNotBlank(tenderAgreement.getDownloadUrl())){
                            File filePdf= FileUtil.getFile(request,response,tenderAgreement.getDownloadUrl(),nid+".pdf");//债转协议
                            if(filePdf!=null){
                                files.add(filePdf);
                            }
                        }
                     } catch (IOException e) {
                         return;
                     }
                    
                }*/
                if(files!=null && files.size()>0){
                    ZIPGenerator.generateZip(response, files, nid);
                }
                return;
            }
            
        }
        //下载平台老协议
		// 融通宝静态打印
		if (StringUtils.isNotEmpty(form.getProjectType()) && form.getProjectType().equals("13")) {
			Map<String, Object> contents = new HashMap<String, Object>();
			// 用户ID
			Integer userId = WebUtils.getUserId(request);
			UsersInfo userinfo = mytenderService.getUsersInfoByUserId(userId);
			form.setUserId(userId + "");
			List<WebUserInvestListCustomize> invest = mytenderService.selectUserInvestList(form, 0, 10);
			if (invest != null && invest.size() > 0) {
				contents.put("investDeatil", invest.get(0));
			}
			WebProjectDetailCustomize borrow = this.projectService.selectProjectDetail(form.getBorrowNid());
			contents.put("projectDeatil", borrow);
			contents.put("truename", userinfo.getTruename());
			contents.put("idcard", userinfo.getIdcard());
			contents.put("borrowNid", form.getBorrowNid());// 标的号
			contents.put("nid", form.getNid());// 标的号
			contents.put("assetNumber", form.getAssetNumber());// 资产编号
			contents.put("projectType", form.getProjectType());// 项目类型
			String moban = CustomConstants.RTB_TENDER_CONTRACT;
			if (borrow != null && borrow.getBorrowPublisher() != null && borrow.getBorrowPublisher().equals("中商储")) {
				moban = CustomConstants.RTB_TENDER_CONTRACT_ZSC;
			}
			// 导出PDF文件
			try {
				PdfGenerator.generatePdf(request, response, form.getBorrowNid() + ".pdf", moban, contents);
			} catch (Exception e) {
				LogUtil.errorLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
				e.printStackTrace();
			}
		} else {
		 // 用户ID
            
            WebViewUser loInfo = WebUtils.getUser(request);
            Integer userId = loInfo.getUserId();
			// 判断是否是汇添金专属标的导出
			if (recordList1.size() != 1) {
				Map<String, Object> contents = new HashMap<String, Object>();
				// 查询借款人用户名
				DebtBorrowCommonCustomize debtBorrowCommonCustomize = new DebtBorrowCommonCustomize();
				// 借款编码
				debtBorrowCommonCustomize.setBorrowNidSrch(form.getBorrowNid());
				List<DebtBorrowCustomize> recordList = planInfoService.selectBorrowList(debtBorrowCommonCustomize);
				if (recordList.size() != 1) {
					System.out.println("标的信息异常（0条或者大于1条信息）,下载汇盈金服互联网金融服务平台居间服务协议PDF失败。");
					return;
				}
				contents.put("borrowNid", form.getBorrowNid());
				contents.put("nid", form.getNid());
				// 借款人用户名
				contents.put("borrowUsername", recordList.get(0).getUsername());
				if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
					// 最后一笔的放款完成时间 (协议签订日期)
					contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
				} else {
					// 设置为满标时间
					contents.put("recoverTime", recordList.get(0).getReverifyTime());
				}
				
				form.setUserId(userId.toString());
				// 用户出借列表
				List<WebUserInvestListCustomize> tzList = planInfoService.selectUserInvestList(form, 0, 100);
				if (tzList != null && tzList.size() > 0) {
					contents.put("userInvest", tzList.get(0));
				}

				// 如果是分期还款，查询分期信息
				String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
				if (borrowStyle != null) {
					if ("month".equals(borrowStyle) || "principal".equals(borrowStyle)
							|| "endmonth".equals(borrowStyle)) {
						ProjectRepayListBean bean = new ProjectRepayListBean();
						bean.setUserId(WebUtils.getUserId(request).toString());
						bean.setBorrowNid(form.getBorrowNid());
						bean.setNid(form.getNid());
						int recordTotal = this.planInfoService.countProjectRepayPlanRecordTotal(bean);
						if (recordTotal > 0) {
							Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
							List<WebProjectRepayListCustomize> fqList = planInfoService.selectProjectRepayPlanList(
									bean, paginator.getOffset(), paginator.getLimit());
							contents.put("paginator", paginator);
							contents.put("repayList", fqList);
						} else {
							Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
							contents.put("paginator", paginator);
							contents.put("repayList", "");
						}
					}
				}
				// 导出PDF文件
				try {
					PdfGenerator.generatePdf(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
							CustomConstants.TENDER_CONTRACT, contents);
				} catch (Exception e) {
					LogUtil.errorLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
					e.printStackTrace();
				}
			} else {
				List<BorrowCustomize> recordList = recordList1;
				Map<String, Object> contents = new HashMap<String, Object>();
				// 借款人用户名
		        if(recordList.size()>0){
		            contents.put("record", recordList.get(0));
		            // 借款人用户名
		            int userIds = recordList.get(0).getUserId();
		            UsersInfo userInfo=planInfoService.getUsersInfoByUserId(userIds);
		            String borrowUsername = userInfo.getTruename();
		            if(!(loInfo.getUserId()+"").equals(userInfo.getUserId()+"") ){
	                     borrowUsername = borrowUsername.substring(0,borrowUsername.length()-1)+"*";
	                 }
		            contents.put("borrowUsername", borrowUsername);
		            contents.put("idCard", userInfo.getIdcard());
		            contents.put("recoverLastDay", recordList.get(0).getRecoverLastDay());// 最后一笔的放款完成时间
		            contents.put("recoverAccount", form.getAccount());
		            contents.put("borrowNid", form.getBorrowNid());
		            contents.put("nid", form.getNid());
		            if (StringUtils.isNotBlank(recordList.get(0).getRecoverLastTime())) {
		                // 最后一笔的放款完成时间 (协议签订日期)
		                contents.put("recoverTime", recordList.get(0).getRecoverLastTime());
		            } else {
		                // 设置为满标时间
		                contents.put("recoverTime", recordList.get(0).getReverifyTime());
		            }
		        }
				form.setUserId(userId.toString());
				// 用户出借列表
				List<WebUserInvestListCustomize> tzList = mytenderService.selectUserInvestList(form, 0, 100);
				if (tzList != null && tzList.size() > 0) {
				    WebUserInvestListCustomize userInvest = tzList.get(0);
				    if(loInfo!=null&&!((loInfo.getUserId()+"").equals(userInvest.getUserId()))){
				        userInvest.setRealName(userInvest.getRealName().substring(0,1)+"**");
				        userInvest.setUsername(userInvest.getUsername().substring(0,1)+"*****");
				        userInvest.setIdCard(userInvest.getIdCard().substring(0,4)+"**************");
				    }
                    contents.put("userInvest", userInvest);
				}

				// 如果是分期还款，查询分期信息
				String borrowStyle = recordList.get(0).getBorrowStyle();// 还款模式
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
							contents.put("paginator", paginator);
							contents.put("repayList", fqList);
						} else {
							Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal);
							contents.put("paginator", paginator);
							contents.put("repayList", "");
						}
					}
				}
			   
				// 导出PDF文件
				try {
					PdfGenerator.generatePdf(request, response, form.getBorrowNid() + "_" + form.getNid() + ".pdf",
							CustomConstants.TENDER_CONTRACT, contents);
				} catch (Exception e) {
					LogUtil.errorLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）", e);
					e.printStackTrace();
				}

			}
		}
		LogUtil.endLog(CreateAgreementController.class.toString(), "createAgreementPDF 导出PDF文件（汇盈金服互联网金融服务平台居间服务协议）");

	}
	

	
	/**
	 * 生成债权转让协议的PDF文件下载
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = CreateAgreementDefine.CREDIT_HJH_TRANSFER_AGREEMENT_PDF, method = RequestMethod.GET)
	public void userHJHCreditContractToSealPDF(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CreditAssignedBean tenderCreditAssignedBean) {
		LogUtil.startLog(CreateAgreementController.class.toString(), CreateAgreementDefine.CREDIT_HJH_TRANSFER_AGREEMENT_PDF);
		ServletContext sc = request.getSession().getServletContext();
		String webInfoPath = sc.getRealPath("/WEB-INF"); // 值为D:\apache-tomcat-6.0.26\webapps\webmonitor
		// 把路径中的反斜杠转成正斜杠
		webInfoPath = webInfoPath.replaceAll("\\\\", "/"); // 值为D:/apache-tomcat-6.0.26/webapps/webmonitor
		Integer userId = null;
		try {
			userId = WebUtils.getUserId(request); // 用户ID
			if (userId != null && userId.intValue() != 0) {
				if (StringUtils.isEmpty(tenderCreditAssignedBean.getBidNid()) || StringUtils.isEmpty(tenderCreditAssignedBean.getCreditNid())
						|| StringUtils.isEmpty(tenderCreditAssignedBean.getCreditTenderNid()) || StringUtils.isEmpty(tenderCreditAssignedBean.getAssignNid())) {
				
					LogUtil.endLog(CreateAgreementController.class.toString(), CreateAgreementDefine.CREDIT_HJH_TRANSFER_AGREEMENT_PDF);
					return;
				}
				// 模板参数对象(查新表)
				/*Map<String, Object> creditContract = tenderCreditService.selectUserCreditContract(tenderCreditAssignedBean);*/
				// 模板参数对象(查新表)
				Map<String, Object> creditContract = tenderCreditService.selectHJHUserCreditContract(tenderCreditAssignedBean);
				// 临时文件夹生成PDF文件//ip处会为空
				PdfGenerator.generatePdf(request, response, ((HjhDebtCreditTender) creditContract.get("creditTender")).getAssignOrderId() + ".pdf", CustomConstants.HJH_CREDIT_CONTRACT, creditContract);
				/*PdfGenerator.generatePdf(request, response, ((HjhDebtCreditTender) creditContract.get("creditTender")).getAssignNid() + ".pdf", CustomConstants.HJH_CREDIT_CONTRACT, creditContract);*/
			} else {
				LogUtil.infoLog(this.getClass().getName(), "userHJHCreditContractToSealPDF", "用户未登录");
				return;
			}
		}  catch (Exception e) {
			LogUtil.infoLog(this.getClass().getName(), "userHJHCreditContractToSealPDF", "系统异常");
			e.printStackTrace();
			return;
		}
		LogUtil.endLog(CreateAgreementController.class.toString(), CreateAgreementDefine.CREDIT_HJH_TRANSFER_AGREEMENT_PDF);
	}
	
	/**
     * 下载法大大协议 直接下载打包，返回成功状态
     * 
     * @param response
     * @param nid
     * 返回 0:下载成功；1:下载失败；2:没有生成法大大合同记录
     */
    @RequestMapping(value = CreateAgreementDefine.CREDIT_FDD_PAYMENT_PLAN, method = RequestMethod.GET)
    public int createFaddPDFFile(HttpServletResponse response,String nid) {
        SFTPParameter para = new SFTPParameter() ;
        String ftpIP = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_IP);
        String port = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PORT);
        String basePathImage = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_IMG);
        String basePathPdf = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_PDF);
        String password = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PASSWORD);
        String username = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_USERNAME);
        para.hostName = ftpIP;//ftp服务器地址
        para.userName = username;//ftp服务器用户名
        para.passWord = password;//ftp服务器密码
        para.port = Integer.valueOf(port);//ftp服务器端口
        
        para.downloadPath =basePathImage;//ftp服务器文件目录
        para.savePath = "/pdf_tem/pdf/" + nid;
        List<File> files = new ArrayList<File>();
        TenderAgreement tenderAgreement = new TenderAgreement();
        List<TenderAgreement> tenderAgreements= fddGenerateContractService.selectByExample(nid);
        if(tenderAgreements!=null && tenderAgreements.size()>0){
            tenderAgreement = tenderAgreements.get(0);
            /*String[] imgUrls = imgUrl.split(";");
            if(imgUrls.length>1){
                for (int i = 0; i < imgUrls.length; i++) {
                    String img = imgUrls[i];
                    System.out.println("img__________________________:"+img);
                    String imagepath = img.substring(0,img.lastIndexOf("/"));
                    String imagename = img.substring(img.lastIndexOf("/")+1);
                    para.downloadPath ="/"+basePathImage+imagepath;//ftp服务器文件目录
                    System.out.println("downloadPath__________________________:"+para.downloadPath);
                    para.sftpKeyFile = imagename;
                    File file =  FavFTPUtil.downloadDirectory(para);
                    files.add(file);
                }
            }else{
                return 1;
            }*/
            String imgUrl = tenderAgreement.getImgUrl();
            String pdfUrl = tenderAgreement.getPdfUrl();
            para.fileName=tenderAgreement.getTenderNid();
            if(StringUtils.isNotBlank(pdfUrl)){
				String pdfpath = pdfUrl.substring(0,imgUrl.lastIndexOf("/"));
				String pdfname = pdfUrl.substring(imgUrl.lastIndexOf("/")+1);
				para.downloadPath =basePathPdf + "/" + pdfpath;//ftp服务器文件目录
				System.out.println("downloadPath__________________________:"+para.downloadPath);
				para.sftpKeyFile = pdfname;
				File file =  FavFTPUtil.downloadDirectory(para);
				files.add(file);
			}else if(StringUtils.isNotBlank(imgUrl)){
                String imagepath = imgUrl.substring(0,imgUrl.lastIndexOf("/"));
                String imagename = imgUrl.substring(imgUrl.lastIndexOf("/")+1);
                para.downloadPath ="/"+basePathImage+imagepath;//ftp服务器文件目录
                System.out.println("downloadPath__________________________:"+para.downloadPath);
                para.sftpKeyFile = imagename;
                File file =  FavFTPUtil.downloadDirectory(para);
                files.add(file);
            }else{
                return 1;
            }
            ZIPGenerator.generateZipAndDel(response, files, nid); 
        }else{
            return 2;
        }
        return 0;
    }
	/**
     * 下载法大大协议 __居间协议
     * 
     * @param nid
     * @param files
     * @param tenderAgreement
     * 返回 0:下载成功；1:下载失败；2:没有生成法大大合同记录
     */
    @RequestMapping(value = CreateAgreementDefine.CREDIT_FDD_PAYMENT_PLAN_IMG, method = RequestMethod.GET)
    public List<File> createFaddPDFImgFile(List<File> files,TenderAgreement tenderAgreement) {
        SFTPParameter para = new SFTPParameter() ;
        String ftpIP = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_IP);
        String port = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PORT);
        String basePathImage = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_IMG);
        String basePathPdf = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_PDF);
        String password = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PASSWORD);
        String username = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_USERNAME);
        para.hostName = ftpIP;//ftp服务器地址
        para.userName = username;//ftp服务器用户名
        para.passWord = password;//ftp服务器密码
        para.port = Integer.valueOf(port);//ftp服务器端口
        para.fileName=tenderAgreement.getTenderNid();
//        para.downloadPath =basePathImage;//ftp服务器文件目录creditUserId
        para.savePath = "/pdf_tem/pdf/" + tenderAgreement.getTenderNid();
        String imgUrl = tenderAgreement.getImgUrl();
        String pdfUrl = tenderAgreement.getPdfUrl();
        if(StringUtils.isNotBlank(pdfUrl)){
        	//获取文件目录
			int index = pdfUrl.lastIndexOf("/");
			String pdfPath = pdfUrl.substring(0,index);
			//文件名称
			String pdfName = pdfUrl.substring(index+1);
        	para.downloadPath = basePathPdf + "/" + pdfPath;
        	para.sftpKeyFile = pdfName;
        }else if(StringUtils.isNotBlank(imgUrl)){
			int index = imgUrl.lastIndexOf("/");
			String imgPath = imgUrl.substring(0,index);
			//文件名称
			String imgName = imgUrl.substring(index+1);
			para.downloadPath = "/" + basePathImage + "/" + imgPath;
			para.sftpKeyFile = imgName;
        }else{
            return null;
        }
		File file =  FavFTPUtil.downloadDirectory(para);
		files.add(file);
       /* String[] imgUrls = imgUrl.split(";");
        if(imgUrls.length>1){
            for (int i = 0; i < imgUrls.length; i++) {
                String img = imgUrls[i];
                System.out.println("img__________________________:"+img);
                String imagepath = img.substring(0,img.lastIndexOf("/"));
                String imagename = img.substring(img.lastIndexOf("/")+1);
                para.downloadPath ="/"+basePathImage+imagepath;//ftp服务器文件目录
                System.out.println("downloadPath__________________________:"+para.downloadPath);
                para.sftpKeyFile = imagename;
                File file =  FavFTPUtil.downloadDirectory(para);
                files.add(file);
            }
        }else{
            return null;
        }*/
        return files;
    }
    
    
    /**
     * 下载法大大协议 __居间协议单个
     * 
     * @param nid
     * @param files
     * @param tenderAgreement
     * 返回 0:下载成功；1:下载失败；2:没有生成法大大合同记录
     */
    @RequestMapping(value = CreateAgreementDefine.CREDIT_FDD_PAYMENT_PLAN_IMG_ONE, method = RequestMethod.GET)
    public File createFaddPDFImgFileOne(TenderAgreement tenderAgreement) {
        SFTPParameter para = new SFTPParameter() ;
        String ftpIP = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_IP);
        String port = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PORT);
        String basePathImage = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_IMG);
        String basePathPdf = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_PDF);
        String password = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_PASSWORD);
        String username = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_USERNAME);
        para.hostName = ftpIP;//ftp服务器地址
        para.userName = username;//ftp服务器用户名
        para.passWord = password;//ftp服务器密码
        para.port = Integer.valueOf(port);//ftp服务器端口
        para.savePath = "/pdf_tem/pdf/" + tenderAgreement.getTenderNid();
        para.fileName=tenderAgreement.getTenderNid();
        String imgUrl = tenderAgreement.getImgUrl();
        String pdfUrl = tenderAgreement.getPdfUrl();
        if(StringUtils.isNotBlank(pdfUrl)){
            //获取文件目录
            int index = pdfUrl.lastIndexOf("/");
            String pdfPath = pdfUrl.substring(0,index);
            //文件名称
            String pdfName = pdfUrl.substring(index+1);
            para.downloadPath = basePathPdf + "/" + pdfPath;
            para.sftpKeyFile = pdfName;

        }else if(StringUtils.isNotBlank(imgUrl)){
            int index = imgUrl.lastIndexOf("/");
            String imgPath = imgUrl.substring(0,index);
            //文件名称
            String imgName = imgUrl.substring(index+1);
            para.downloadPath = "/" + basePathImage + "/" + imgPath;
            para.sftpKeyFile = imgName;
        }else{
            return null;
        }
        File file =  FavFTPUtil.downloadDirectory(para);
        return file;
    }
    
    /**
     * 查看法大大协议 
     * 
     * @param fileName
     */
    @RequestMapping(value = CreateAgreementDefine.SELECT_FDD_PAYMENT_PLAN, method = RequestMethod.GET)
    public TenderAgreement selectFaddPDFFile(String fileName) {
        TenderAgreement tenderAgreement = new TenderAgreement();
        List<TenderAgreement> tenderAgreements= fddGenerateContractService.selectByExample("");
        if(tenderAgreements!=null && tenderAgreements.size()>0){
            tenderAgreement = tenderAgreements.get(0);
        }
        return tenderAgreement;
    }
	
}
