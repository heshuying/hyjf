package com.hyjf.app.user.transfer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.hjhplan.HjhPlanService;
import com.hyjf.app.project.ProjectService;
import com.hyjf.app.project.RepayPlanBean;
import com.hyjf.app.user.credit.AppTenderCreditService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.AsteriskProcessUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowHouses;
import com.hyjf.mybatis.model.auto.BorrowManinfo;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowUsers;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTransferDetailCustomize;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

/**
 * 
 * app债权转让
 * 
 * @author liuyang
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月28日
 * @see 13:59:50
 */
@Controller
@RequestMapping(value = AppTransferDefine.REQUEST_MAPPING)
public class AppTransferController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(AppTransferController.class);
	@Autowired
	private AppTransferService transferService;
	
	@Autowired
    private AppTenderCreditService appTenderCreditService;

	@Autowired
	private ProjectService projectService;
	@Autowired
    private HjhPlanService hjhPlanService;

    private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

	/**
	 * 
	 * 获取债转出借详情
	 * 
	 * @author liuyang
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppTransferDefine.PROJECT_DETAIL_ACTION)
	public BaseResultBeanFrontEnd searchTenderCreditDetail(HttpServletRequest request, @PathVariable("transferId") String transferId) {
		LogUtil.startLog(AppTransferDefine.THIS_CLASS, AppTransferDefine.PROJECT_DETAIL_ACTION);
		TransferDetailResultBean result = new TransferDetailResultBean();
		String token = request.getParameter("token");
		String sign = request.getParameter("sign");
		try {
    		//构建userValidation参数
    		result.setUserValidation(createUserValidation(token,sign));
    		
    		
            // 根据债转标号查询相应的项目信息
    		AppTenderToCreditDetailCustomize tenderCredit = this.transferService.selectCreditTenderDetail(transferId);
    		// 还款信息
    		BorrowRepay borrowRepay = projectService.getBorrowRepay(tenderCredit.getBidNid());
    		if(tenderCredit!=null){
    		    result.setProjectInfo(createTenderCreditDetail(tenderCredit));
                List<Object> projectDetail = new ArrayList<Object>();
                
                //资产列表
                String borrowNid=tenderCredit.getBidNid();
                // 2.根据项目标号获取相应的项目信息
                AppProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);
                //借款人企业信息
                BorrowUsers borrowUsers = projectService.getBorrowUsersByNid(borrowNid);
                //借款人信息
                BorrowManinfo borrowManinfo = projectService.getBorrowManinfoByNid(borrowNid);
                //房产抵押信息
                List<BorrowHouses> borrowHousesList = projectService.getBorrowHousesByNid(borrowNid);
                //车辆抵押信息
                List<BorrowCarinfo> borrowCarinfoList = projectService.getBorrowCarinfoByNid(borrowNid);
                //基础信息
                List<BorrowDetailBean> baseTableData = new ArrayList<BorrowDetailBean>();
                //资产信息
                List<BorrowDetailBean> assetsTableData = new ArrayList<BorrowDetailBean>();
                //项目介绍
                List<BorrowDetailBean> intrTableData = new ArrayList<BorrowDetailBean>();
                //信用状况
                List<BorrowDetailBean> credTableData = new ArrayList<BorrowDetailBean>();
                //审核信息
                List<BorrowDetailBean> reviewTableData = new ArrayList<BorrowDetailBean>();
                //其他信息
    			List<BorrowDetailBean> otherTableData = new ArrayList<BorrowDetailBean>();
                //借款类型
                int borrowType = Integer.parseInt(borrow.getComOrPer());
        
                if(borrowType == 1 && borrowUsers != null){
                    //基础信息
                    baseTableData = packDetail(borrowUsers, 1, borrowType, borrow.getBorrowLevel());
                    //信用状况
                    credTableData = packDetail(borrowUsers, 4, borrowType, borrow.getBorrowLevel());
                    //审核信息
                    reviewTableData =  packDetail(borrowUsers, 5, borrowType, borrow.getBorrowLevel());
                    //其他信息
    				otherTableData =  packDetail(borrowUsers, 6, borrowType, borrow.getBorrowLevel());
                }else{
                    if(borrowManinfo != null){
                        //基础信息
                        baseTableData = packDetail(borrowManinfo, 1, borrowType, borrow.getBorrowLevel());
                        //信用状况
                        credTableData = packDetail(borrowManinfo, 4, borrowType, borrow.getBorrowLevel());
                        //审核信息
                        reviewTableData = packDetail(borrowManinfo, 5, borrowType, borrow.getBorrowLevel());
                        //其他信息
    					otherTableData =  packDetail(borrowManinfo, 6, borrowType, borrow.getBorrowLevel());
                    }
                }
                //资产信息
                if(borrowHousesList != null && borrowHousesList.size() > 0){
                    for (BorrowHouses  borrowHouses: borrowHousesList) {
                        assetsTableData.addAll(packDetail(borrowHouses, 2, borrowType, borrow.getBorrowLevel())); 
                    }
                }
                if(borrowCarinfoList != null && borrowCarinfoList.size() > 0){
                    for (BorrowCarinfo borrowCarinfo : borrowCarinfoList) {
                        assetsTableData.addAll(packDetail(borrowCarinfo, 2, borrowType, borrow.getBorrowLevel()));
                    }
                }
                //项目介绍
                intrTableData = packDetail(borrow, 3, borrowType, borrow.getBorrowLevel());
                JSONObject baseTableDataJson = new JSONObject();
                JSONObject assetsTableDataJson = new JSONObject();
                JSONObject intrTableDataJson = new JSONObject();
                JSONObject credTableDataJson = new JSONObject();
                JSONObject reviewTableDataJson = new JSONObject();
                JSONObject otherTableDataJson = new JSONObject();
                baseTableDataJson.put("title", "基础信息");
                baseTableDataJson.put("msg", baseTableData);
                assetsTableDataJson.put("title", "资产信息");
                assetsTableDataJson.put("msg", assetsTableData);
                intrTableDataJson.put("title", "项目介绍");
                intrTableDataJson.put("msg", intrTableData);
                credTableDataJson.put("title", "信用状况");
                credTableDataJson.put("msg", credTableData);
                reviewTableDataJson.put("title", "审核信息");
                reviewTableDataJson.put("msg", reviewTableData);
                // 信批需求新增(放款后才显示)
     			if(Integer.parseInt(borrow.getBorrowStatus())>=4 && borrowRepay != null){
     				//其他信息
     				String updateTime = getUpdateTime(Integer.parseInt(borrowRepay.getAddtime()), StringUtils.isBlank(borrowRepay.getRepayYestime())?null:Integer.parseInt(borrowRepay.getRepayYestime()));
     				otherTableDataJson.put("title", "其他信息（更新于" + updateTime + "）");
     				otherTableDataJson.put("msg", otherTableData);
     			}
                projectDetail.add(baseTableDataJson);
                projectDetail.add(assetsTableDataJson);
                projectDetail.add(intrTableDataJson);
                projectDetail.add(credTableDataJson);
                projectDetail.add(reviewTableDataJson);
                projectDetail.add(otherTableDataJson);
                result.setProjectDetail(projectDetail);
                // 查询相应的还款计划
                List<RepayPlanBean> repayPlan = this.transferService.getRepayPlan(tenderCredit.getBidNid());
                result.setRepayPlan(repayPlan);
                // 风控信息
                AppRiskControlCustomize riskControl = projectService.selectRiskControl(borrowNid);
                if(riskControl!=null){
        			riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
        			riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
                }
                result.setRiskControl(riskControl);
                
                result.setStatus(BaseResultBeanFrontEnd.SUCCESS);
                result.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
    		}else{
    		    result.setProjectInfo(new AppTransferDetailCustomize());
    		    result.setProjectDetail(new ArrayList<Object>());
    		    result.setRepayPlan(new ArrayList<RepayPlanBean>());
    		    result.setStatus(BaseResultBeanFrontEnd.FAIL);
                result.setStatusDesc(BaseResultBeanFrontEnd.FAIL_MSG);
    		}
		} catch (Exception e) {
		    result.setStatus(BaseResultBeanFrontEnd.FAIL);
            result.setStatusDesc(BaseResultBeanFrontEnd.FAIL_MSG);
            e.printStackTrace();
        }
		// 获取债转详细的参数
		LogUtil.endLog(AppTransferDefine.THIS_CLASS, AppTransferDefine.PROJECT_DETAIL_ACTION);
		return result;
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
	
	/**
     * 查询相应的项目的出借记录列表
     * 
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppTransferDefine.PROJECT_INVEST_ACTION, produces = "application/json; charset=utf-8")
    public BaseResultBeanFrontEnd investRecord(@PathVariable("transferId") String transferId,Integer currentPage,Integer pageSize) {
        LogUtil.startLog(AppTransferDefine.THIS_CLASS, AppTransferDefine.PROJECT_INVEST_ACTION);
        TransferInvestRecordResultBean result = new TransferInvestRecordResultBean();
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("creditNid", transferId);
            int recordTotal = this.appTenderCreditService.countTenderCreditInvestRecordTotal(params);
            if (recordTotal > 0) { // 查询相应的汇直投列表数据
                int limit = pageSize;
                int page = currentPage;
                int offSet = (page - 1) * limit;
                if (offSet == 0 || offSet > 0) {
                    params.put("limitStart", offSet);
                }
                if (limit > 0) {
                    params.put("limitEnd", limit);
                }
                List<AppTenderCreditInvestListCustomize> recordList = this.appTenderCreditService.searchTenderCreditInvestList(params);
                //获取债转出借人次和已债转金额
                List<BorrowCredit> creditList = this.appTenderCreditService.selectBorrowCreditByNid(transferId);
                if (creditList != null && creditList.size() == 1) {
                    BorrowCredit credit = creditList.get(0);
                    result.setUserCount(String.valueOf(credit.getAssignNum()));
                    result.setAccount(CommonUtils.formatAmount(credit.getCreditCapitalAssigned()));
                }else{
                    result.setAccount("0");
                    result.setUserCount("0");
                }
                //判断是否最后一页
                if(recordTotal<=page*limit){
                    result.setIsEnd(true);
                }else{
                    result.setIsEnd(false);
                }
                result.setIsEnd(true);
                result.setList(recordList);
            } else {
                result.setAccount("0");
                result.setUserCount("0");
                result.setIsEnd(true);
                result.setList(new ArrayList<AppTenderCreditInvestListCustomize>());
            }
            result.setStatus(BaseResultBeanFrontEnd.SUCCESS);
            result.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
        } catch (Exception e) {
            result.setStatus(BaseResultBeanFrontEnd.FAIL);
            result.setStatusDesc(BaseResultBeanFrontEnd.FAIL_MSG);
        }
        
        LogUtil.endLog(AppTransferDefine.THIS_CLASS, AppTransferDefine.PROJECT_INVEST_ACTION);
        return result;
    }
    private AppTransferDetailCustomize createTenderCreditDetail(AppTenderToCreditDetailCustomize tenderCredit) {
        AppTransferDetailCustomize tenderCreditDetail = new AppTransferDetailCustomize();
        tenderCreditDetail.setBorrowRemain(CommonUtils.formatAmount(tenderCredit.getCreditCapital()));
        tenderCreditDetail.setBorrowProgress(tenderCredit.getBorrowSchedule());
        tenderCreditDetail.setTransferId(tenderCredit.getCreditNid()+"");
        tenderCreditDetail.setOnTime(tenderCredit.getCreditTime());
        tenderCreditDetail.setTransferDiscount(tenderCredit.getCreditDiscount());
        tenderCreditDetail.setAccount(tenderCredit.getCreditCapital());
        tenderCreditDetail.setBorrowApr(tenderCredit.getBidApr());
        tenderCreditDetail.setBorrowId(tenderCredit.getBidNid());
        tenderCreditDetail.setTransferLeft(tenderCredit.getCreditTermHold());
        tenderCreditDetail.setStatus(tenderCredit.getStatus());
        tenderCreditDetail.setBorrowProgressStatus("4");
        tenderCreditDetail.setBorrowPeriod(tenderCredit.getCreditTerm());
        tenderCreditDetail.setBorrowPeriodUnit("天");
        tenderCreditDetail.setTag("");
        tenderCreditDetail.setType("HZR");
        tenderCreditDetail.setRepayStyle(tenderCredit.getRepayStyle());;
        return tenderCreditDetail;
    }
    private JSONObject createUserValidation(String token, String sign) {
        JSONObject userValidation = new JSONObject();
        
        if (StringUtils.isEmpty(token)) {
            userValidation.put("isLogined", false);
            userValidation.put("isOpened", false);
            userValidation.put("isSetPassword", false);
            userValidation.put("investflag", false);
            userValidation.put("isAllowed", false);
            userValidation.put("isAutoInves", false);
            userValidation.put("roleId", 0);
        } else {// 用户已经登陆
            userValidation.put("isLogined", true);
            Integer userId = SecretUtil.getUserId(sign);
            Users user = this.projectService.searchLoginUser(userId);
            //是否开户0未开户  1已开户
            if (user.getBankOpenAccount() == 0) {
                userValidation.put("isOpened", false);
            } else {
                userValidation.put("isOpened", true);
            }
            //是否设置过交易密码0未设置  1已设置
            if (user.getIsSetPassword() == 1) {
                userValidation.put("isSetPassword", true);
            } else {
                userValidation.put("isSetPassword", false);
            }
            // 用户角色
            UsersInfo usersInfo = projectService.getUsersInfoByUserId(userId);
            if (usersInfo != null) {
                userValidation.put("roleId", usersInfo.getRoleId());
            }else{
                userValidation.put("roleId", 0);
            }
            
            // 是否是新手0新手 1老手
            if (user.getInvestflag() == 0) {
                userValidation.put("investflag", true);
            } else {
                userValidation.put("investflag", false);
            }
            //0未锁定,1锁定
            if (user.getStatus() == 0) {
                userValidation.put("isAllowed", true); 
            } else {
                userValidation.put("isAllowed", false);
            }
            // 缴费授权状态 
            userValidation.put("paymentAuthStatus", user.getPaymentAuthStatus());
            // 角色验证开关
            userValidation.put("isCheckUserRole", Boolean.parseBoolean(PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN)));
            // 服务费授权开关
            userValidation.put("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());

            try{
                // modify by liuyang 20180411 用户是否完成风险测评标识 start
                // 是否进行过风险测评 0未测评 1已测评
//                JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
//                logger.info("风险测评结果： {}", jsonObject.toJSONString());
//                if ((Integer)jsonObject.get("userEvaluationResultFlag") == 1) {
//                    userValidation.put("isRiskTested", true);
//                } else {
//                    userValidation.put("isRiskTested", false);
//                }
				if(user.getIsEvaluationFlag()==1 && null != user.getEvaluationExpiredTime()){
					//测评到期日
					Long lCreate = user.getEvaluationExpiredTime().getTime();
					//当前日期
					Long lNow = System.currentTimeMillis();
					if (lCreate <= lNow) {
						//已过期需要重新评测
	                    userValidation.put("isRiskTested", "2");
					} else {
						//未到一年有效期
	                    userValidation.put("isRiskTested", "1");
					}
                }else{
                    userValidation.put("isRiskTested", "0");
                }
                // modify by liuyang 20180411 用户是否完成风险测评标识 end
            }catch (Exception e){
                logger.error("是否进行过风险测评查询出错....", e);
                userValidation.put("isRiskTested", false);
            }

            //
            HjhUserAuth hjhUserAuth = this.hjhPlanService.getHjhUserAuthByUserId(userId);
            if (Validator.isNotNull(hjhUserAuth) && (hjhUserAuth.getAutoInvesStatus() == 0||hjhUserAuth.getAutoCreditStatus()==0)) {
                userValidation.put("isAutoInves", false);
            } else {
                userValidation.put("isAutoInves", true);
            }
        }
        return userValidation;
    }
    /**
     * 封装项目详情页
     * @param objBean
     * @param type  1 基础信息 2资产信息 3项目介绍 4信用状况 5审核状况
     * @param borrowType  1借款人 2企业借款
     * @param borrowLevel  信用评级
     * @return
     */
    private List<BorrowDetailBean> packDetail(Object objBean,int type,int borrowType, String borrowLevel){
        List<BorrowDetailBean> detailBeanList = new ArrayList<BorrowDetailBean>();
        String currencyName = "元";
        // 得到对象
        Class c = objBean.getClass();
        // 得到方法
        Field fieldlist[] = c.getDeclaredFields();
        for (int i = 0; i < fieldlist.length; i++) {
            // 获取类属性
            Field f = fieldlist[i];
            // 得到方法名
            String fName = f.getName();
            try {
                // 参数方法获取
                String paramName = fName.substring(0, 1).toUpperCase() + fName.substring(1, fName.length());
                // 取得结果
                Method getMethod = c.getMethod(BankCallConstant.GET + paramName);
                if (getMethod != null) {
                    Object result = getMethod.invoke(objBean);
                    // 结果不为空时
                    if (Validator.isNotNull(result)) {
                        //封装bean
                        BorrowDetailBean detailBean = new BorrowDetailBean();
                        detailBean.setId(fName);
                        detailBean.setVal(result.toString());
                        if(type == 1){
                            if(borrowType == 2){//个人借款
                                switch (fName) {
                                case "name":
                                    detailBean.setKey("姓名");
                                    //数据脱敏
                                    detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 1, 2));
                                    detailBeanList.add(detailBean);
                                    break;
                                case "cardNo":
                                    detailBean.setKey("身份证号");
                                    //数据脱敏
                                    detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 10));
                                    detailBeanList.add(detailBean);
                                    break;
                                case "sex":
                                    detailBean.setKey("性别");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("男");
                                    }else{
                                        detailBean.setVal("女");
                                    }
                                    detailBeanList.add(detailBean);
                                    break;
                                case "old":
                                    if(!"0".equals(detailBean.getVal())){
                                        detailBean.setKey("年龄");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;
                                case "merry":
                                    if(!("0".equals(result.toString()) || result.toString()==null)){
                                        detailBean.setKey("婚姻状况");
                                        if("1".equals(result.toString())){
                                            detailBean.setVal("已婚");
                                        }else if("2".equals(result.toString())) {
                                            detailBean.setVal("未婚");
                                        }else if("3".equals(result.toString())) {
                                            detailBean.setVal("离异");
                                        }else if("4".equals(result.toString())) {
                                            detailBean.setVal("丧偶");
                                        }
                                        detailBeanList.add(detailBean);
                                    }
                                    break;      
                                case "city":
                                    detailBean.setKey("工作城市");
                                    detailBeanList.add(detailBean);
                                    break;                      
                                case "domicile":
                                    detailBean.setKey("户籍地");
                                    detailBeanList.add(detailBean);
                                    break;  
                                case "position":
                                    detailBean.setKey("岗位职业");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "annualIncome":
									detailBean.setKey("年收入");
									detailBeanList.add(detailBean);
									break;
								case "overdueReport":
									detailBean.setKey("征信报告逾期情况");
									detailBeanList.add(detailBean);
									break;
								case "debtSituation":
									detailBean.setKey("重大负债状况");
									detailBeanList.add(detailBean);
									break;
								case "otherBorrowed":
									detailBean.setKey("其他平台借款情况");
									detailBeanList.add(detailBean);
									break;
                                default:
                                    break;
                                }
                            }else{//企业借款
                                
                                switch (fName) {
                                case "currencyName":
                                    currencyName = detailBean.getVal();
                                    break;
                                case "username":
                                    detailBean.setKey("借款主体");
                                    detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(),detailBean.getVal().length()-2));
                                    detailBeanList.add(detailBean);
                                    break;
                                case "city":
                                    detailBean.setKey("注册地区");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "regCaptial":
                                    detailBean.setKey("注册资本");
                                    if(StringUtils.isNotBlank(detailBean.getVal())){
                                        detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + currencyName);
                                    }
                                    detailBeanList.add(detailBean);
                                    break;
                                case "comRegTime":
                                    detailBean.setKey("注册时间");
                                    detailBeanList.add(detailBean);
                                    break;                      
                                case "socialCreditCode":
                                    detailBean.setKey("统一社会信用代码");
                                    //数据脱敏
                                    detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 10));
                                    detailBeanList.add(detailBean);
                                    break;  
                                case "registCode":
                                    detailBean.setKey("注册号");
                                    //数据脱敏
                                    detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 10));
                                    detailBeanList.add(detailBean);
                                    break;                      
                                case "legalPerson":
                                    detailBean.setKey("法定代表人");
                                    //数据脱敏
                                    detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 1, 2));
                                    detailBeanList.add(detailBean);
                                    break;  
                                case "industry":
                                    detailBean.setKey("所属行业");
                                    detailBeanList.add(detailBean);
                                    break;                          
                                case "mainBusiness":
                                    detailBean.setKey("主营业务");
                                    detailBeanList.add(detailBean);
                                    break;                   
                                case "overdueReport":
									detailBean.setKey("征信报告逾期情况");
									detailBeanList.add(detailBean);
									break;
								case "debtSituation":
									detailBean.setKey("重大负债状况");
									detailBeanList.add(detailBean);
									break;
								case "otherBorrowed":
									detailBean.setKey("其他平台借款情况");
									detailBeanList.add(detailBean);
									break;
                                default:
                                    break;
                                }
                            }
                        }else if(type == 2){
                            switch (fName) {
                            case "housesType":
                                detailBean.setKey("资产类型");
                                String houseType = this.projectService.getParamName("HOUSES_TYPE", detailBean.getVal());
                                if(houseType != null){
                                     detailBean.setVal(houseType);
                                }else{
                                    detailBean.setVal("住宅");
                                }
                                detailBeanList.add(detailBean);
                                break;
                            case "housesArea":
                                detailBean.setKey("资产面积");
                                detailBean.setVal(detailBean.getVal() + "m<sup>2</sup>");
                                detailBeanList.add(detailBean);
                                break;
                            case "housesCnt":
                                detailBean.setKey("资产数量");
                                detailBeanList.add(detailBean);
                                break;
                            case "housesToprice":
                                detailBean.setKey("评估价值");
                                if(StringUtils.isNotBlank(detailBean.getVal())){
                                    detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
                                }
                                detailBeanList.add(detailBean);
                                break;                      
                            case "housesBelong":
                                detailBean.setKey("资产所属");
                                detailBeanList.add(detailBean);
                                break;  
                            //车辆
                            case "brand":
                                BorrowDetailBean carBean = new BorrowDetailBean();
                                carBean.setId("carType");
                                carBean.setKey("资产类型");
                                carBean.setVal("车辆");
                                detailBeanList.add(carBean);
                                detailBean.setKey("品牌");
                                detailBeanList.add(detailBean);
                                break;  
    
                            case "model":
                                detailBean.setKey("型号");
                                detailBeanList.add(detailBean);
                                break;  
                            case "place":
                                detailBean.setKey("产地");
                                detailBeanList.add(detailBean);
                                break;  
                            case "price":
                                detailBean.setKey("购买价格");
                                if(StringUtils.isNotBlank(detailBean.getVal())){
                                    detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
                                }
                                detailBeanList.add(detailBean);
                                break;  
                            case "toprice":
                                detailBean.setKey("评估价值");
                                if(StringUtils.isNotBlank(detailBean.getVal())){
                                    detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
                                }
                                detailBeanList.add(detailBean);
                                break;                              
                            case "number":
                                detailBean.setKey("车牌号");
                                //数据脱敏
                                detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 2, 4));
                                detailBeanList.add(detailBean);
                                break;                              
                            case "registration":
                                detailBean.setKey("车辆登记地");
                                detailBeanList.add(detailBean);
                                break;                              
                            case "vin":
                                detailBean.setKey("车架号");
                                //数据脱敏
                                detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), 4, 5));
                                detailBeanList.add(detailBean);
                                break;                              
                            default:
                                break;
                            }
                            
                        }else if(type == 3){
                            switch (fName) {
                            case "borrowContents": 
                                detailBean.setKey("项目信息");
                                detailBeanList.add(detailBean);
                                break;
                            case "fianceCondition":
                                detailBean.setKey("财务状况 ");
                                detailBeanList.add(detailBean);
                                break;
                            case "financePurpose":
                                detailBean.setKey("借款用途");
                                detailBeanList.add(detailBean);
                                break;
                            case "monthlyIncome":
                                detailBean.setKey("月薪收入");
                                if(StringUtils.isNotBlank(detailBean.getVal())){
                                    detailBean.setVal(DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())));
                                }
                                detailBeanList.add(detailBean);
                                break;                      
                            case "payment":
                                detailBean.setKey("还款来源");
                                detailBeanList.add(detailBean);
                                break;  
                            case "firstPayment":
                                detailBean.setKey("第一还款来源");
                                detailBeanList.add(detailBean);
                                break;                      
                            case "secondPayment"://还没有
                                detailBean.setKey("第二还款来源");
                                detailBeanList.add(detailBean);
                                break;  
                            case "costIntrodution":
                                detailBean.setKey("费用说明");
                                detailBeanList.add(detailBean);
                                break;  
                            default:
                                break;
                            }
                        }else if(type == 4){
                            switch (fName) {
                            case "overdueTimes":
                                detailBean.setKey("在平台逾期次数");
                                detailBeanList.add(detailBean);
                                break;
                            case "overdueAmount":
                                detailBean.setKey("在平台逾期金额");
                                if(StringUtils.isNotBlank(detailBean.getVal())){
                                    detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
                                }
                                detailBeanList.add(detailBean);
                                break;
                            case "litigation":
                                detailBean.setKey("涉诉情况");
                                detailBeanList.add(detailBean);
                                break;
                            default:
                                break;
                            }
                        }else if(type == 5){
                            if(borrowType == 2){
                                switch (fName) {
                                case "isCard":
                                    detailBean.setKey("身份证");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;
                                case "isIncome":
                                    detailBean.setKey("收入状况");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;
                                case "isCredit":
                                    detailBean.setKey("信用状况");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;
                                case "isAsset":
                                    detailBean.setKey("资产状况");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;
                                case "isVehicle":
                                    detailBean.setKey("车辆状况");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;                              
                                case "isDrivingLicense":
                                    detailBean.setKey("行驶证");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;                              
                                case "isVehicleRegistration":
                                    detailBean.setKey("车辆登记证");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;                              
                                case "isMerry":
                                    detailBean.setKey("婚姻状况");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;                              
                                case "isWork":
                                    detailBean.setKey("工作状况");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;  
                                case "isAccountBook":
                                    detailBean.setKey("户口本");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;                                  
                                default:
                                    break;
                                }
                            }else{
                                switch (fName) {
                                case "isCertificate":
                                    detailBean.setKey("企业证件");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;
                                case "isOperation":
                                    detailBean.setKey("经营状况");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;
                                case "isFinance":
                                    detailBean.setKey("财务状况");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;
                                case "isEnterpriseCreidt":
                                    detailBean.setKey("企业信用");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;
                                case "isLegalPerson":
                                    detailBean.setKey("法人信息");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;                              
                                case "isAsset":
                                    detailBean.setKey("资产状况");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;                              
                                case "isPurchaseContract":
                                    detailBean.setKey("购销合同");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;                              
                                case "isSupplyContract":
                                    detailBean.setKey("供销合同");
                                    if("1".equals(result.toString())){
                                        detailBean.setVal("已审核");
                                        detailBeanList.add(detailBean);
                                    }
                                    break;                              
                                default:
                                    break;
                                }
                            }
                        }else if(type == 6){
							switch (fName) {
							case "isFunds":
								detailBean.setKey("借款资金运用情况");
								detailBeanList.add(detailBean);
								break;
							case "isManaged":
								detailBean.setKey("借款人经营状况及财务状况");
								detailBeanList.add(detailBean);
								break;
							case "isAbility":
								detailBean.setKey("借款人还款能力变化情况");
								detailBeanList.add(detailBean);
								break;
							case "isOverdue":
								detailBean.setKey("借款人逾期情况");
								detailBeanList.add(detailBean);
								break;
							case "isComplaint":
								detailBean.setKey("借款人涉诉情况");
								detailBeanList.add(detailBean);
								break;								
							case "isPunished":
								detailBean.setKey("借款人受行政处罚情况");
								detailBeanList.add(detailBean);
								break;								
							default:
								break;
							}
						}
                    }
                }

            } catch (Exception e) {
                continue;
            }
        }
        if(type == 1 || type == 4){
            //信用评级单独封装
            BorrowDetailBean detailBean = new BorrowDetailBean();
            detailBean.setId("borrowLevel");
            detailBean.setKey("信用评级");
            detailBean.setVal(borrowLevel);
            detailBeanList.add(detailBean);
        }
        return detailBeanList;
    }
}
