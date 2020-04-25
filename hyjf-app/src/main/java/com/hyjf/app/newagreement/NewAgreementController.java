package com.hyjf.app.newagreement;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.agreement.AgreementController;
import com.hyjf.app.project.ProjectService;
import com.hyjf.app.user.project.InvestProjectService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.enums.utils.ProtocolEnum;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.web.WebUserInvestListCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 新版网站协议集合
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年6月5日
 * @see 17:27:14
 */
@Controller
@RequestMapping(value = NewAgreementDefine.REQUEST_MAPPING)
public class NewAgreementController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(NewAgreementController.class);

	@Autowired
	private NewAgreementService agreementService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private InvestProjectService investProjectService;
	@Autowired
    private FddGenerateContractService fddGenerateContractService;
    /**
     * 
     * （一）居间服务借款协议
     * @author pcc
     * @param request
     * @param response
     * @return
     */
	@ResponseBody
    @RequestMapping(NewAgreementDefine.INTER_SERVICE_LOAN_AGREEMENT_ACTION)
    public NewAgreementResultBean interServiceLoanAgreement(HttpServletRequest request, HttpServletResponse response) {
	    System.out.println("*******************************居间服务借款协议************************************");
        LogUtil.startLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.INTER_SERVICE_LOAN_AGREEMENT_ACTION);
        NewAgreementResultBean newAgreementResultBean = new NewAgreementResultBean();
        newAgreementResultBean.setAgreementImages("");
        JSONObject jsonObject = new JSONObject();
        String sign = request.getParameter("sign");
        String tenderNid = request.getParameter("tenderNid");
        String borrowNid = request.getParameter("borrowNid");
        String userIdStr = request.getParameter("userId");
        logger.info("get sign is: {}",sign);
        logger.info("get tenderNid is: {}",tenderNid);
        logger.info("get borrowNid is: {}",borrowNid);
        Integer userId = null;
        try {
            if(userIdStr!=null&&StringUtils.isNumeric(userIdStr)){
                if (StringUtils.isEmpty(tenderNid)   
                        || StringUtils.isEmpty(borrowNid)) {
                    newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
                    newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
                    newAgreementResultBean.setInfo(jsonObject);
                    return newAgreementResultBean;
                } 
                userId=Integer.parseInt(userIdStr);
            }else{
                if (StringUtils.isEmpty(sign)
                        || StringUtils.isEmpty(tenderNid)   
                        || StringUtils.isEmpty(borrowNid)) {
                    newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
                    newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
                    newAgreementResultBean.setInfo(jsonObject);
                    return newAgreementResultBean;
                } 
                userId = SecretUtil.getUserId(sign);
            }
            
            /*userId = WebUtils.getUserId(request); */// 用户ID
            
            if (userId != null && userId.intValue() != 0) {
                jsonObject = agreementService.interServiceLoanAgreement(userId,tenderNid,borrowNid);
                //获取法大大合同url
                List<TenderAgreement> tenderAgreements= fddGenerateContractService.selectByExample(tenderNid);
                String agreementImages = "";
                if(null != tenderAgreements && tenderAgreements.size() > 0){
                    String imgUrl = tenderAgreements.get(0).getImgUrl();
                    if(StringUtils.isNotBlank(imgUrl) ){
                        String basePathImage = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_IMG);//ftp文件路劲
                        String basePathurl = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_URL);//ftp映射路劲
                        String url = basePathurl+basePathImage;
                        imgUrl = imgUrl.replaceAll("PDF/", url+"PDF/");
                    }else{
                        imgUrl="";
                    }
                    agreementImages = imgUrl;
                }
                newAgreementResultBean.setAgreementImages(agreementImages);
                newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
                newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
                newAgreementResultBean.setInfo(jsonObject);
            } else {
                LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "用户未登录");
                newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
                newAgreementResultBean.setStatusDesc("用户未登录");
                newAgreementResultBean.setInfo(jsonObject);
            }
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "系统异常");
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
            newAgreementResultBean.setStatusDesc("系统异常");
            newAgreementResultBean.setInfo(jsonObject);
        }
        logger.info("get newAgreementResultBean is: {}",JSONObject.toJSON(newAgreementResultBean));
        LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.INTER_SERVICE_LOAN_AGREEMENT_ACTION);
        return newAgreementResultBean;
    }


    /**
     * 
     *（二）汇计划智投服务协议
     * @author pcc
     * @param request
     * @return
     */
	@ResponseBody
    @RequestMapping(value = NewAgreementDefine.HJH_INFO_AGREEMENT)
	public NewAgreementResultBean hjhInfo(HttpServletRequest request) {
	    LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.HJH_INFO_AGREEMENT);
        NewAgreementResultBean newAgreementResultBean = new NewAgreementResultBean();
        newAgreementResultBean.setAgreementImages("");
		String accedeOrderId = request.getParameter("accedeOrderId");
		String sign = request.getParameter("sign");
		logger.info("get sign is: {}",sign);
		logger.info("get accedeOrderId is: {}",accedeOrderId);
		JSONObject jsonObject = new JSONObject();
		try {
		    if (StringUtils.isEmpty(sign)
	                || StringUtils.isEmpty(accedeOrderId)) {
	            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
	            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
	            newAgreementResultBean.setInfo(jsonObject);
	            return newAgreementResultBean;
	        }
	        if (StringUtils.isNotEmpty(accedeOrderId)) {
	          //获取法大大合同url
                List<TenderAgreement> tenderAgreements= fddGenerateContractService.selectByExample(accedeOrderId);
                String agreementImages = "";
                if(null != tenderAgreements && tenderAgreements.size() > 0){
                    String imgUrl = tenderAgreements.get(0).getImgUrl();
                    if(StringUtils.isNotBlank(imgUrl) ){
                        String basePathImage = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_IMG);//ftp文件路劲
                        String basePathurl = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_URL);//ftp映射路劲
                        String url = basePathurl+basePathImage;
                        imgUrl = imgUrl.replaceAll("PDF/", url+"PDF/");
                    }else{
                        imgUrl="";
                    }
                    agreementImages = imgUrl;
                    
                }
                newAgreementResultBean.setAgreementImages(agreementImages);
	            Integer userId = SecretUtil.getUserId(sign);
	            logger.info("get userId is: {}",userId);
	            // 1基本信息
	            Map<String, Object> params = new HashMap<String, Object>();
	            params.put("accedeOrderId", accedeOrderId);
	            params.put("userId", userId);
	            UsersInfo userInfo = agreementService.getUsersInfoByUserId(userId);
	            Users users=agreementService.getUsers(userId);
	            UserHjhInvistDetailCustomize userHjhInvistDetailCustomize = agreementService
	                    .selectUserHjhInvistDetail(params);
	            jsonObject.put("accedeOrderId" , accedeOrderId );
	            jsonObject.put("addTime" ,userHjhInvistDetailCustomize.getCountInterestTime());
	            jsonObject.put("truename" , userInfo.getTruename() );
	            jsonObject.put("idcard" , userInfo.getIdcard() );
	            jsonObject.put("username" , users.getUsername() );
	            jsonObject.put("accedeAccount" , userHjhInvistDetailCustomize.getAccedeAccount() );
	            jsonObject.put("planPeriod" , userHjhInvistDetailCustomize.getPlanPeriod() );
	            jsonObject.put("planApr" , userHjhInvistDetailCustomize.getPlanApr() );
	            jsonObject.put("countInterestTime" , userHjhInvistDetailCustomize.getCountInterestTime() );
	            jsonObject.put("quitTime" , userHjhInvistDetailCustomize.getQuitTime() );
	            jsonObject.put("incomeManageMode" , "收益复投" );
	            jsonObject.put("shouldPayTotal" , userHjhInvistDetailCustomize.getShouldPayTotal() );
	            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
	            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
	            newAgreementResultBean.setInfo(jsonObject);
	        } else {
	            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
	            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
	            newAgreementResultBean.setInfo(jsonObject);
	        }
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "系统异常");
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
            newAgreementResultBean.setStatusDesc("系统异常");
            newAgreementResultBean.setInfo(jsonObject);
        }

		logger.info("get newAgreementResultBean is: {}",JSONObject.toJSON(newAgreementResultBean));
		LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.HJH_INFO_AGREEMENT);
		return newAgreementResultBean;
	}
	
	
	
	/**
     * 
     * （三）债权转让协议
     * 
     * @author pcc
     * @param request
     * @param response
     * @param appTenderCreditAssignedBean
     * @return
     */
	@ResponseBody
    @RequestMapping(value = NewAgreementDefine.CREDIT_CONTRACT)
    public NewAgreementResultBean userCreditContract(HttpServletRequest request, HttpServletResponse response ) {
	    System.out.println("*******************************债权转让协议************************************");
        LogUtil.startLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.CREDIT_CONTRACT);
        NewAgreementResultBean newAgreementResultBean = new NewAgreementResultBean();
        newAgreementResultBean.setAgreementImages("");
        JSONObject jsonObject = new JSONObject();

        String borrowType = request.getParameter("borrowType");
        try {
            if(borrowType!=null&&"HJH".equals(borrowType)){
                String userId = request.getParameter("userId"); // 随机字符串
                String nid = request.getParameter("nid");
                
                if (StringUtils.isEmpty(userId)) {
                    userId="0";
                }
                logger.info("get userId is: {}",userId);
                logger.info("我的计划-计划详情-资产列表-协议，债转id :{}", nid);
                
                DecimalFormat df = CustomConstants.DF_FOR_VIEW;
                
                // 债转承接信息
                HjhDebtCreditTender hjhDebtCreditTender = this.agreementService.getHjhDebtCreditTender(Integer.parseInt(nid));
    
                if (hjhDebtCreditTender != null) {
                    //获取承接订单;
                    String assignNid =  hjhDebtCreditTender.getAssignOrderId();
                    logger.info("我的计划-计划详情-资产列表-协议，债转标号assignNid :{}", assignNid);
                    List<TenderAgreement> tenderAgreements= fddGenerateContractService.selectByExample(assignNid);
                    //获取法大大合同url
                    String agreementImages = "";
                    if(null != tenderAgreements && tenderAgreements.size() > 0){
                        String imgUrl = tenderAgreements.get(0).getImgUrl();
                        if(StringUtils.isNotBlank(imgUrl) ){
                            String basePathImage = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_IMG);//ftp文件路劲
                            String basePathurl = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_URL);//ftp映射路劲
                            String url = basePathurl+basePathImage;
                            imgUrl = imgUrl.replaceAll("PDF/", url+"PDF/");
                        }else{
                            imgUrl="";
                        }
                        agreementImages = imgUrl;
                    }
                    // 转让人信息
                    UsersInfo creditUserInfo = this.agreementService
                            .getUsersInfoByUserId(hjhDebtCreditTender.getCreditUserId());
                    Users creditUser = this.agreementService.getUsers(hjhDebtCreditTender.getCreditUserId());
                    // 承接人信息
                    UsersInfo usersInfo = this.agreementService.getUsersInfoByUserId(hjhDebtCreditTender.getUserId());
                    Users user = this.agreementService.getUsers(hjhDebtCreditTender.getUserId());
    
                    // 标的信息
                    Borrow borrow = this.agreementService.getBorrowByNid(hjhDebtCreditTender.getBorrowNid());
                    Users borrowUser = this.agreementService.getUsers(borrow.getUserId());
                    // 债转信息
                    HjhDebtCredit hjhDebtCredit = this.agreementService
                            .getHjhDebtCreditByCreditNid(hjhDebtCreditTender.getCreditNid());
                    jsonObject.put("addTime", GetDate.times10toStrYYYYMMDD(hjhDebtCreditTender.getCreateTime()));
                    jsonObject.put("remainderPeriod", hjhDebtCredit.getRemainDays()+"天");
                    jsonObject.put("assignTime", GetDate.times10toStrYYYYMMDD(hjhDebtCredit.getCreateTime()));
                    jsonObject.put("assignCapital", df.format(hjhDebtCreditTender.getAssignCapital()));
                    jsonObject.put("assignPay", df.format(hjhDebtCreditTender.getAssignPrice()));
                    jsonObject.put("orderId", hjhDebtCreditTender.getAssignOrderId());
                    jsonObject.put("borrowNid", hjhDebtCreditTender.getBorrowNid());
                    jsonObject.put("borrowAccount", df.format(borrow.getAccount()));
                    jsonObject.put("borrowApr", borrow.getBorrowApr()+"%");
                    jsonObject.put("borrowStyle", getBorrowStyle(borrow.getBorrowStyle()));
                    jsonObject.put("borrowPeriod", getBorrowPeriod(borrow.getBorrowStyle(), borrow.getBorrowPeriod()));
                    
                    if(user.getUserId().equals(new Integer(userId))){
                        jsonObject.put("newCreditTruename", usersInfo.getTruename());
                        jsonObject.put("newCreditIdcard", usersInfo.getIdcard());
                        jsonObject.put("newCreditUsername", user.getUsername());
                    }else{
                        String truename=usersInfo.getTruename();
                        String encryptedTruename=truename.substring(0, 1);
                        for (int i = 0; i < truename.length()-1; i++) {
                            encryptedTruename+="*";
                        }
                        jsonObject.put("newCreditTruename", encryptedTruename);
                        String idCard=usersInfo.getIdcard();
                        String encryptedIdCard=idCard.substring(0, 4);
                        for (int i = 0; i < idCard.length()-4; i++) {
                            encryptedIdCard+="*";
                        }
                        jsonObject.put("newCreditIdcard", encryptedIdCard);
                        String userName= user.getUsername();
                        String encryptedUserName=userName.substring(0, 1);
                        for (int i = 0; i < 5; i++) {
                            encryptedUserName+="*";
                        }
                        jsonObject.put("newCreditUsername", encryptedUserName);  
                    }
                    
                    if(creditUser.getUserId().equals(new Integer(userId))){
                        
                        jsonObject.put("oldCreditUsername", creditUser.getUsername());
                        jsonObject.put("oldCreditTruename", creditUserInfo.getTruename());
                        jsonObject.put("oldCreditIdcard", creditUserInfo.getIdcard());
                    }else{
                        String userName= creditUser.getUsername();
                        String encryptedUserName=userName.substring(0, 1);
                        for (int i = 0; i < 5; i++) {
                            encryptedUserName+="*";
                        }
                        jsonObject.put("oldCreditUsername", encryptedUserName);
                        
                        String truename=creditUserInfo.getTruename();
                        String encryptedTruename=truename.substring(0, 1);
                        for (int i = 0; i < truename.length()-1; i++) {
                            encryptedTruename+="*";
                        }
                        jsonObject.put("oldCreditTruename", encryptedTruename);
                        
                        String idCard=creditUserInfo.getIdcard();
                        String encryptedIdCard=idCard.substring(0, 4);
                        for (int i = 0; i < idCard.length()-4; i++) {
                            encryptedIdCard+="*";
                        }
                        jsonObject.put("oldCreditIdcard", encryptedIdCard);
                    }
                    if(borrow.getUserId().equals(new Integer(userId))){
                        
                        jsonObject.put("borrowUsername", borrowUser.getUsername());  
                    } else {
                        String userName= borrowUser.getUsername();
                        String encryptedUserName=userName.substring(0, 1);
                        for (int i = 0; i < 5; i++) {
                            encryptedUserName+="*";
                        }
                        jsonObject.put("borrowUsername", encryptedUserName);
                    }
                    newAgreementResultBean.setAgreementImages(agreementImages);
                    newAgreementResultBean.setInfo(jsonObject);
                    
                }
                logger.info("get newAgreementResultBean is: {}",JSONObject.toJSON(newAgreementResultBean));
                LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.OPEN_AGREEMENT);
                return newAgreementResultBean;
            }else{
                String sign = request.getParameter("sign");
                String bidNid = request.getParameter("bidNid");
                String creditNid = request.getParameter("creditNid");
                String creditTenderNid = request.getParameter("creditTenderNid");
                String assignNid = request.getParameter("assignNid");
                
                NewCreditAssignedBean appTenderCreditAssignedBean=new NewCreditAssignedBean();
                appTenderCreditAssignedBean.setBidNid(bidNid);
                appTenderCreditAssignedBean.setCreditNid(creditNid);
                appTenderCreditAssignedBean.setCreditTenderNid(creditTenderNid);
                appTenderCreditAssignedBean.setAssignNid(assignNid);
                
                logger.info("get appTenderCreditAssignedBean is: {}",JSONObject.toJSON(appTenderCreditAssignedBean));
                // 获取用户id
                
                try {
                    if (StringUtils.isEmpty(sign)
                            || StringUtils.isEmpty(bidNid)|| StringUtils.isEmpty(creditNid)
                            || StringUtils.isEmpty(creditTenderNid)|| StringUtils.isEmpty(assignNid)) {
                        newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
                        newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
                        newAgreementResultBean.setInfo(jsonObject);
                        return newAgreementResultBean;
                    }
                    Integer userId = SecretUtil.getUserId(sign);
    //                Integer userId = 36;
                    //获取承接订单;
                    List<TenderAgreement> tenderAgreements= fddGenerateContractService.selectByExample(assignNid);
                    //获取法大大合同url
                    String agreementImages = "";
                    if(null != tenderAgreements && tenderAgreements.size() > 0){
                        String imgUrl = tenderAgreements.get(0).getImgUrl();
                        if(StringUtils.isNotBlank(imgUrl) ){
                            String basePathImage = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_BASEPATH_IMG);//ftp文件路劲
                            String basePathurl = PropUtils.getSystem(FddGenerateContractConstant.HYJF_FTP_URL);//ftp映射路劲
                            String url = basePathurl+basePathImage;
                            imgUrl = imgUrl.replaceAll("PDF/", url+"PDF/");
                        }else{
                            imgUrl="";
                        }
                        agreementImages = imgUrl;
                        newAgreementResultBean.setAgreementImages(agreementImages);
                    }
                    if (userId != null && userId.intValue() != 0) {
                        if (StringUtils.isEmpty(appTenderCreditAssignedBean.getBidNid()) || StringUtils.isEmpty(appTenderCreditAssignedBean.getCreditNid())
                                || StringUtils.isEmpty(appTenderCreditAssignedBean.getCreditTenderNid()) || StringUtils.isEmpty(appTenderCreditAssignedBean.getAssignNid())) {
                            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
                            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
                            newAgreementResultBean.setInfo(jsonObject);
                            return newAgreementResultBean;
                        }
                        jsonObject = this.agreementService.selectUserCreditContract(appTenderCreditAssignedBean,userId);
                        newAgreementResultBean.setInfo(jsonObject);
                    } else {
                        LogUtil.infoLog(this.getClass().getName(), "userCreditContract", "用户未登录");
                        
                        newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
                        newAgreementResultBean.setStatusDesc("用户未登录");
                        newAgreementResultBean.setInfo(jsonObject);
                    }
                } catch (Exception e) {
                    LogUtil.infoLog(this.getClass().getName(), "userCreditContract", "系统异常");
                    newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
                    newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.FAIL_MSG);
                    newAgreementResultBean.setInfo(jsonObject);
                }
                
                logger.info("get newAgreementResultBean is: {}",JSONObject.toJSON(newAgreementResultBean));
                LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.CREDIT_CONTRACT);
                return newAgreementResultBean;
            }
            
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "系统异常");
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
            newAgreementResultBean.setStatusDesc("系统异常");
            newAgreementResultBean.setInfo(jsonObject);
        }
        return newAgreementResultBean;
    }
	
	
    
    
    /**
     *（四）融通宝（嘉诺）协议
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewAgreementDefine.RONG_TONG_BAO_JIA)
    public NewAgreementResultBean rongtongbao(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.RONG_TONG_BAO_JIA);
        NewAgreementResultBean newAgreementResultBean = new NewAgreementResultBean();
        newAgreementResultBean.setAgreementImages("");
		String orderId = request.getParameter("orderId");
	    String borrowNid = request.getParameter("borrowNid");

		String sign = request.getParameter("sign"); // 随机字符串
		logger.info("get sign is: {}",sign);
		logger.info("get orderId is: {}",orderId);
		logger.info("get borrowNid is: {}",borrowNid);
		JSONObject jsonObject = new JSONObject();
		if (StringUtils.isEmpty(sign)
                || StringUtils.isEmpty(orderId)
                || StringUtils.isEmpty(borrowNid)) {
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
            newAgreementResultBean.setInfo(jsonObject);
            return newAgreementResultBean;
        }
		
		try {
            
             // 用户id
            Integer userId = SecretUtil.getUserId(sign);
            
            
            // 2.根据项目标号获取相应的项目信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("borrowNid", borrowNid);
            params.put("orderId", orderId);
            params.put("userId", userId);

            AppProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);

            UsersInfo userinfo = investProjectService.getUsersInfoByUserId(userId);

            List<WebUserInvestListCustomize> invest = investProjectService.selectUserInvestList(borrowNid, userId,
                    orderId, 0, 10);
            WebUserInvestListCustomize investDeatil=null;
            if (invest != null && invest.size() > 0) {
                investDeatil= invest.get(0);
            }
            DecimalFormat df = CustomConstants.DF_FOR_VIEW;
            jsonObject.put("contractPeriod" , borrow.getContractPeriod() );
            jsonObject.put("recoverLastTime" ,  borrow.getRecoverLastTime());
            jsonObject.put("truename" , userinfo.getTruename() );
            jsonObject.put("idcard" , userinfo.getIdcard().substring(0, 4)+"**********"+
                            userinfo.getIdcard().substring(userinfo.getIdcard().length()-4));
            jsonObject.put("legalRepresentative" , "" );
            jsonObject.put("institutionalContacts" , "" );
            jsonObject.put("contactNumber" , "" );
            jsonObject.put("contactAddress" , "" );
            jsonObject.put("borrowPeriod" , borrow.getBorrowPeriod() );
            jsonObject.put("borrowPeriodType" , borrow.getBorrowPeriodType() );
            jsonObject.put("borrowApr" , borrow.getBorrowApr()+"%" );
            jsonObject.put("tenderAccountMinWan " , df.format(new BigDecimal(borrow.getTenderAccountMinWan()))+"元" );
            jsonObject.put("increaseMoney" , df.format(new BigDecimal(borrow.getIncreaseMoney()))+"元" );
            if(investDeatil==null){
                jsonObject.put("account" , "0.00 元");
            }else{
                jsonObject.put("account" , df.format(new BigDecimal(investDeatil.getAccount()))+"元" );
            }
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
                newAgreementResultBean.setInfo(jsonObject);
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "系统异常");
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
            newAgreementResultBean.setStatusDesc("系统异常");
            newAgreementResultBean.setInfo(jsonObject);
        }
		logger.info("get newAgreementResultBean is: {}",JSONObject.toJSON(newAgreementResultBean));
		LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.RONG_TONG_BAO_JIA);
        return newAgreementResultBean;
    }
    
    /**
     * （五）融通宝（中商储）协议
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewAgreementDefine.RONG_TONG_BAO_ZHONG)
    public NewAgreementResultBean rongtongbaoZhong(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.RONG_TONG_BAO_ZHONG);
        NewAgreementResultBean newAgreementResultBean = new NewAgreementResultBean();
        String orderId = request.getParameter("orderId");
        String borrowNid = request.getParameter("borrowNid");

        String sign = request.getParameter("sign"); // 随机字符串
        logger.info("get sign is: {}",sign);
        logger.info("get orderId is: {}",orderId);
        logger.info("get borrowNid is: {}",borrowNid);
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isEmpty(sign)
                || StringUtils.isEmpty(orderId)
                || StringUtils.isEmpty(borrowNid)) {
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
            newAgreementResultBean.setInfo(jsonObject);
            return newAgreementResultBean;
        }
        
        try {
            
          Integer userId = SecretUtil.getUserId(sign);
            
            
            // 2.根据项目标号获取相应的项目信息
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("borrowNid", borrowNid);
            params.put("orderId", orderId);
            params.put("userId", userId);

            AppProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);

            UsersInfo userinfo = investProjectService.getUsersInfoByUserId(userId);

            List<WebUserInvestListCustomize> invest = investProjectService.selectUserInvestList(borrowNid, userId,
                    orderId, 0, 10);
            WebUserInvestListCustomize investDeatil=null;
            if (invest != null && invest.size() > 0) {
                investDeatil= invest.get(0);
            }
            DecimalFormat df = CustomConstants.DF_FOR_VIEW;
            jsonObject.put("contractPeriod" , borrow.getContractPeriod() );
            jsonObject.put("recoverLastTime" ,  borrow.getRecoverLastTime()==null?"待确定":borrow.getRecoverLastTime());
            jsonObject.put("truename" , userinfo.getTruename() );
            jsonObject.put("idcard" , userinfo.getIdcard().substring(0, 4)+"**********"+
                    userinfo.getIdcard().substring(userinfo.getIdcard().length()-4));
            jsonObject.put("legalRepresentative" , "" );
            jsonObject.put("institutionalContacts" , "" );
            jsonObject.put("contactNumber" , "" );
            jsonObject.put("contactAddress" , "" );
            jsonObject.put("borrowPeriod" , borrow.getBorrowPeriod() );
            jsonObject.put("borrowPeriodType" , borrow.getBorrowPeriodType() );
            jsonObject.put("borrowApr" , borrow.getBorrowApr()+"%" );
            jsonObject.put("tenderAccountMinWan" , df.format(new BigDecimal(borrow.getTenderAccountMinWan()))+"元" );
            jsonObject.put("increaseMoney" , df.format(new BigDecimal(borrow.getIncreaseMoney()))+"元" );
            if(investDeatil==null){
                jsonObject.put("account" , "0.00 元");
            }else{
                jsonObject.put("account" , df.format(new BigDecimal(investDeatil.getAccount()))+"元" );
            }
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
                newAgreementResultBean.setInfo(jsonObject);
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "系统异常");
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
            newAgreementResultBean.setStatusDesc("系统异常");
            newAgreementResultBean.setInfo(jsonObject);
        }
        logger.info("get newAgreementResultBean is: {}",JSONObject.toJSON(newAgreementResultBean));
        LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.RONG_TONG_BAO_ZHONG);
        return newAgreementResultBean;
    }
    
    
    
    /**
     * （六）开户协议
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewAgreementDefine.OPEN_AGREEMENT)
    public NewAgreementResultBean openAgreement(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.OPEN_AGREEMENT);
        NewAgreementResultBean newAgreementResultBean = new NewAgreementResultBean();

        String sign = request.getParameter("sign"); // 随机字符串
        logger.info("get sign is: {}",sign);
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isEmpty(sign)) {
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
            newAgreementResultBean.setInfo(jsonObject);
            return newAgreementResultBean;
        }
        
        try {
            
             // 用户id
            Integer userId = SecretUtil.getUserIdNoException(sign);
            if (userId == null) {
                newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
                newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
                newAgreementResultBean.setInfo(jsonObject);
                return newAgreementResultBean;
            }
            
            Users user = agreementService.getUsers(userId);

            jsonObject.put("username" , user.getUsername());

            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
            newAgreementResultBean.setInfo(jsonObject);
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "系统异常");
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
            newAgreementResultBean.setStatusDesc("系统异常");
            newAgreementResultBean.setInfo(jsonObject);
        }
        logger.info("get newAgreementResultBean is: {}",JSONObject.toJSON(newAgreementResultBean));
        LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.OPEN_AGREEMENT);
        return newAgreementResultBean;
    }


    /**
     * app 我的计划-计划详情-资产列表-协议（转让）列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = NewAgreementDefine.PLAN_CREDIT_CONTRACT_LIST)
    public NewAgreementResultBean userCreditContractList(HttpServletRequest request) {
        LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.PLAN_CREDIT_CONTRACT_LIST);
        NewAgreementResultBean newAgreementResultBean = new NewAgreementResultBean();

        String sign = request.getParameter("sign"); // 随机字符串
        String version = request.getParameter("version");
        String nid = request.getParameter("nid");
        String borrowType=request.getParameter("borrowType");
        logger.info("get sign is: {}",sign);
        logger.info("get version is: {}",version);
        logger.info("我的计划-计划详情-资产列表-协议，债转id :{}", nid);
        Integer userId=null;
        try {
            
            // 用户id
           userId = SecretUtil.getUserId(sign);

       } catch (Exception e) {
           LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "系统异常");
           newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
           newAgreementResultBean.setStatusDesc("系统异常");
           return newAgreementResultBean;
       }
        if("HJH".equals(borrowType)){
            List<NewAgreementBean> list=new ArrayList<NewAgreementBean>();
            String investOrderId=null;
            // 债转承接信息
            HjhDebtCreditTender hjhDebtCreditTender = this.agreementService.getHjhDebtCreditTenderByCreditNid(nid);
            while (hjhDebtCreditTender!=null && investOrderId==null) {
                NewAgreementBean newAgreementBean=new NewAgreementBean("《债权转让协议》"+hjhDebtCreditTender.getAssignOrderDate(), PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+
                        NewAgreementDefine.TRANS_FER_AGREEMENT_PATH+"?nid="+hjhDebtCreditTender.getId()+"&borrowType="+borrowType+"&userId="+userId);
                list.add(newAgreementBean);
                if(!hjhDebtCreditTender.getInvestOrderId().equals(hjhDebtCreditTender.getSellOrderId())){
                    // 债转承接信息
                    hjhDebtCreditTender = this.agreementService.getHjhDebtCreditTenderByCreditNid(hjhDebtCreditTender.getSellOrderId());
                }else{
                    investOrderId=hjhDebtCreditTender.getInvestOrderId();
                }
            }
            BorrowTender borrowTender=agreementService.getBorrowTenderByNid(investOrderId);
            NewAgreementBean newAgreementBean=new NewAgreementBean("《居间服务借款协议》", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+
                    NewAgreementDefine.SERVICE_LOAN_AGREEMENT_PATH+"?tenderNid="+borrowTender.getNid()+
                    "&borrowNid="+borrowTender.getBorrowNid()+"&userId="+userId);
            list.add(newAgreementBean);
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
            newAgreementResultBean.setList(list); 
        }else{
            List<NewAgreementBean> list=new ArrayList<NewAgreementBean>();
            // 债转承接信息
            CreditTender creditTender = this.agreementService.getCreditTenderByCreditNid(nid);
            NewAgreementBean newAgreementBean=new NewAgreementBean("《债权转让协议》", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+
                    NewAgreementDefine.TRANS_FER_AGREEMENT_PATH+"?bidNid="+creditTender.getBidNid()+
                    "&creditNid="+creditTender.getCreditNid()+
                    "&creditTenderNid="+creditTender.getCreditTenderNid()+
                    "&assignNid="+creditTender.getAssignNid()+
                    "&sign="+sign+
                    "&borrowType="+borrowType);
            list.add(newAgreementBean);
            BorrowTender borrowTender=agreementService.getBorrowTenderByNid(creditTender.getCreditTenderNid());
            NewAgreementBean newAgreementBean1=new NewAgreementBean("《居间服务借款协议》", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+
                    NewAgreementDefine.SERVICE_LOAN_AGREEMENT_PATH+"?tenderNid="+borrowTender.getNid()+
                    "&borrowNid="+borrowTender.getBorrowNid()+"&userId="+userId);
            list.add(newAgreementBean1);
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
            newAgreementResultBean.setList(list);
        }
        
        logger.info("get newAgreementResultBean is: {}",JSONObject.toJSON(newAgreementResultBean));
        LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.PLAN_CREDIT_CONTRACT_LIST);
        return newAgreementResultBean;
    }
    
    
    /**
     * app 我的计划-计划详情-资产列表-协议（转让）列表
     * @param request
     * @return
     */
   /* @ResponseBody
    @RequestMapping(value = NewAgreementDefine.PLAN_CREDIT_CONTRACT_LIST)
    public NewAgreementResultBean userCreditContractListFdd(HttpServletRequest request) {
        LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.PLAN_CREDIT_CONTRACT_LIST);
        NewAgreementResultBean newAgreementResultBean = new NewAgreementResultBean();

        String sign = request.getParameter("sign"); // 随机字符串
        String version = request.getParameter("version");
        String nid = request.getParameter("nid");
        String borrowType=request.getParameter("borrowType");
        logger.info("get sign is: {}",sign);
        logger.info("get version is: {}",version);
        logger.info("我的计划-计划详情-资产列表-协议，债转id :{}", nid);
        Integer userId=null;
        try {
            
            // 用户id
           userId = SecretUtil.getUserId(sign);

       } catch (Exception e) {
           LogUtil.infoLog(this.getClass().getName(), "userCreditContractAssign", "系统异常");
           newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.FAIL);
           newAgreementResultBean.setStatusDesc("系统异常");
           return newAgreementResultBean;
       }
        if("HJH".equals(borrowType)){
            List<NewAgreementBean> list=new ArrayList<NewAgreementBean>();
            String investOrderId=null;
            // 债转承接信息
            HjhDebtCreditTender hjhDebtCreditTender = this.agreementService.getHjhDebtCreditTenderByCreditNid(nid);
            NewAgreementBean newAgreementBean=new NewAgreementBean("《债权转让协议》"+hjhDebtCreditTender.getAssignOrderDate(), PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+
                    NewAgreementDefine.TRANS_FER_AGREEMENT_PATH+"?nid="+hjhDebtCreditTender.getId()+"&borrowType="+borrowType+"&userId="+userId);
            list.add(newAgreementBean);
            investOrderId=hjhDebtCreditTender.getInvestOrderId();
            while (hjhDebtCreditTender!=null && investOrderId==null) {
                if(!hjhDebtCreditTender.getInvestOrderId().equals(hjhDebtCreditTender.getSellOrderId())){
                    // 债转承接信息
                    hjhDebtCreditTender = this.agreementService.getHjhDebtCreditTenderByCreditNid(hjhDebtCreditTender.getSellOrderId());
                }else{
                    investOrderId=hjhDebtCreditTender.getInvestOrderId();
                }
            }
            BorrowTender borrowTender=agreementService.getBorrowTenderByNid(investOrderId);
            NewAgreementBean investAgreementBean=new NewAgreementBean("《居间服务借款协议》", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+
                    NewAgreementDefine.SERVICE_LOAN_AGREEMENT_PATH+"?tenderNid="+borrowTender.getNid()+
                    "&borrowNid="+borrowTender.getBorrowNid()+"&userId="+userId);
            list.add(investAgreementBean);
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
            newAgreementResultBean.setList(list); 
        }else{
            List<NewAgreementBean> list=new ArrayList<NewAgreementBean>();
            // 债转承接信息
            CreditTender creditTender = this.agreementService.getCreditTenderByCreditNid(nid);
            NewAgreementBean newAgreementBean=new NewAgreementBean("《债权转让协议》", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+
                    NewAgreementDefine.TRANS_FER_AGREEMENT_PATH+"?bidNid="+creditTender.getBidNid()+
                    "&creditNid="+creditTender.getCreditNid()+
                    "&creditTenderNid="+creditTender.getCreditTenderNid()+
                    "&assignNid="+creditTender.getAssignNid()+
                    "&sign="+sign+
                    "&borrowType="+borrowType);
            list.add(newAgreementBean);
            BorrowTender borrowTender=agreementService.getBorrowTenderByNid(creditTender.getCreditTenderNid());
            NewAgreementBean newAgreementBean1=new NewAgreementBean("《居间服务借款协议》", PropUtils.getSystem(CustomConstants.HYJF_WEB_URL)+
                    NewAgreementDefine.SERVICE_LOAN_AGREEMENT_PATH+"?tenderNid="+borrowTender.getNid()+
                    "&borrowNid="+borrowTender.getBorrowNid()+"&userId="+userId);
            list.add(newAgreementBean1);
            newAgreementResultBean.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            newAgreementResultBean.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
            newAgreementResultBean.setList(list);
        }
        
        logger.info("get newAgreementResultBean is: {}",JSONObject.toJSON(newAgreementResultBean));
        LogUtil.endLog(NewAgreementDefine.THIS_CLASS, NewAgreementDefine.PLAN_CREDIT_CONTRACT_LIST);
        return newAgreementResultBean;
    }*/
    
    private String getBorrowStyle(String borrowStyle) {
        switch (borrowStyle) {
        case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“
            return "按月计息，到期还本还息";
        case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“；
            return "按天计息，到期还本还息";
        case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“；
            return "先息后本";
        case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“；
            return "等额本息";
        case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“；
            return "等额本金";
        default:
            return "按月计息，到期还本还息";
        }
    }
    
    
    private String getBorrowPeriod(String borrowStyle, Integer borrowPeriod) {
        if(CalculatesUtil.STYLE_ENDDAY.equals(borrowStyle)){
            return borrowPeriod+"天";
        }else{
            return borrowPeriod+"个月";
        }
    }

    /**
     * 获得 协议模板pdf显示地址
     *
     * @return
     */
    @RequestMapping(value = NewAgreementDefine.GOTAGREEMENTPDF_OR_IMG, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public NewAgreementResultBean gotAgreementPdfOrImg(@RequestParam String aliasName) {
        LogUtil.startLog(AgreementController.class.getName(), NewAgreementDefine.GOTAGREEMENTPDF_OR_IMG);

        return agreementService.setProtocolImg(aliasName);
    }

    /**
     * 协议名称 动态获得
     *
     * @return
     */
    @RequestMapping(value = NewAgreementDefine.GET_DISPLAY_NAME_DYNAMIC, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getdisplayNameDynamic(){
        LogUtil.startLog(AgreementController.class.getName(), NewAgreementDefine.GET_DISPLAY_NAME_DYNAMIC);
        JSONObject jsonObject = null;
        HashMap<String, Object> map = new HashMap<String, Object>();

        jsonObject = JSONObject.parseObject(RedisUtils.get(RedisConstants.PROTOCOL_PARAMS));
        if (jsonObject == null) {
            jsonObject = new JSONObject();
            try {
                List<ProtocolTemplate> list = agreementService.getdisplayNameDynamic();
                //是否在枚举中有定义
                for (ProtocolTemplate p : list) {
                    String protocolType = p.getProtocolType();
                    String alia = ProtocolEnum.getAlias(protocolType);
                    if (alia != null){
                        map.put(alia, p.getDisplayName());
                    }
                }
                jsonObject.put("status","000");
                jsonObject.put("statusDesc","成功");
                jsonObject.put("displayName",map);
            } catch (Exception e) {
                jsonObject.put("status","99");
                jsonObject.put("statusDesc","失败");
            }
            RedisUtils.set(RedisConstants.PROTOCOL_PARAMS,jsonObject.toString());
        }
        return jsonObject;
    }

}
