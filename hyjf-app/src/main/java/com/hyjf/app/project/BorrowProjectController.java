/**
 * Description:获取指定的项目类型的项目列表
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.app.project;

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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.borrow.BorrowService;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.AsteriskProcessUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.auto.HjhDebtCreditMapper;
import com.hyjf.mybatis.model.auto.ActdecListedOne;
import com.hyjf.mybatis.model.auto.BorrowManinfo;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowUsers;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.mybatis.model.customize.web.WebProjectUndertakeListCustomize;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

@Controller
@RequestMapping(value = BorrowProjectDefine.REQUEST_MAPPING)
public class BorrowProjectController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(BorrowProjectController.class);

	private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

	@Autowired
	private ProjectService projectService;
	@Autowired
	private HjhDebtCreditMapper hjhDebtCreditMapper;
	@Autowired
	private BorrowService borrowService;
	/**
	 * 查询相应的项目详情
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BorrowProjectDefine.PROJECT_DETAIL_ACTION)
	public JSONObject searchProjectDetail(@PathVariable("borrowId") String borrowNid,
			HttpServletRequest request) {

		LogUtil.startLog(BorrowProjectDefine.THIS_CLASS, BorrowProjectDefine.PROJECT_DETAIL_ACTION);
		JSONObject jsonObject = new JSONObject();
		JSONObject userValidation = new JSONObject();
		String token = request.getParameter("token");
		String sign = request.getParameter("sign");
		String type = request.getParameter("borrowType");
		String version = request.getParameter("version");
		boolean isLogined = false;
		boolean isOpened = false;
		boolean isSetPassword = false;
		boolean isAllowed = false;
		String isRiskTested = "0";
		boolean isAutoInves = false;
		boolean isInvested = false;
		Integer paymentAuthStatus = 0; //  0：未授权1：已授权
		Integer userId = null;
		Integer roleId = 0; //  用户角色
		// 判断用户是否登录
		if(StringUtils.isNotEmpty(sign) && StringUtils.isNotEmpty(token)){
			userId = SecretUtil.getUserId(sign);
			isLogined = true;
			Users users = projectService.getUsers(userId);
			if(users != null){
				//判断是否设置交易密码
				if(users.getIsSetPassword() != null && users.getIsSetPassword() == 1){
					isSetPassword = true;
				}
				//判断是否开户
				if(users.getBankOpenAccount() != null && users.getBankOpenAccount() == 1){
					isOpened = true;
				}
				//是否授权
				if(users.getAuthStatus() != null && users.getAuthStatus() == 1){
					isAutoInves = true;
				}
				//是否允许使用
				if(users.getStatus() != null && users.getStatus() == 0){
					isAllowed = true;
				}
				//是否完成风险测评
				if (users.getIsEvaluationFlag()==1 && null != users.getEvaluationExpiredTime()) {
					//测评到期日
					Long lCreate = users.getEvaluationExpiredTime().getTime();
					//当前日期
					Long lNow = System.currentTimeMillis();
					if (lCreate <= lNow) {
						//已过期需要重新评测
						isRiskTested = "2";
					} else {
						//测评未过期
						isRiskTested = "1";
					}
				}
				boolean isTender = projectService.isTenderBorrow(userId, borrowNid,type);
				if (isTender) {
					isInvested = true;
				}
				//是否缴费授权
				paymentAuthStatus = users.getPaymentAuthStatus();
				// 用户角色
				UsersInfo usersInfo = projectService.getUsersInfoByUserId(userId);
                if (usersInfo != null) {
                    roleId = usersInfo.getRoleId();
                }
			}
		}
		logger.info("version: " + version);

		// 2.根据项目标号获取相应的项目信息
		BorrowProjectInfoBean borrowProjectInfoBean = new BorrowProjectInfoBean();
		AppProjectDetailCustomize borrow = this.projectService.selectProjectDetail(borrowNid);
		// 还款信息
		BorrowRepay borrowRepay = projectService.getBorrowRepay(borrowNid);

		userValidation.put("isLogined", isLogined);
		userValidation.put("isOpened", isOpened);
		userValidation.put("isSetPassword", isSetPassword);
		userValidation.put("isAllowed", isAllowed);
		userValidation.put("isRiskTested", isRiskTested);
		userValidation.put("isAutoInves", isAutoInves);
		userValidation.put("isInvested", isInvested);
		// 服务费授权状态
		userValidation.put("paymentAuthStatus", paymentAuthStatus);
		// 角色验证开关
		userValidation.put("isCheckUserRole", Boolean.parseBoolean(PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN)));
		// 服务费授权开关
		userValidation.put("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
		
		userValidation.put("invesAuthOn",CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH).getEnabledStatus());
		userValidation.put("creditAuthOn",CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_CREDIT_AUTH).getEnabledStatus());

		userValidation.put("roleId", roleId);

		jsonObject.put("userValidation", userValidation);
		if (borrow == null) {
			jsonObject.put("status", BorrowProjectDefine.FAIL);
			jsonObject.put("statusDesc", BorrowProjectDefine.FAIL_BORROW_MSG);
			return jsonObject;
		} else {
			jsonObject.put("status", BorrowProjectDefine.SUCCESS);
			jsonObject.put("statusDesc", BorrowProjectDefine.SUCCESS_MSG);
			borrowProjectInfoBean.setBorrowRemain(borrow.getInvestAccount());
			borrowProjectInfoBean.setBorrowProgress(borrow.getBorrowSchedule());
			borrowProjectInfoBean.setOnTime(borrow.getOnTime());
			borrowProjectInfoBean.setAccount(borrow.getAccount());
			borrowProjectInfoBean.setBorrowApr(borrow.getBorrowApr());
			borrowProjectInfoBean.setBorrowId(borrowNid);
			borrowProjectInfoBean.setOnAccrual((borrow.getReverifyTime()==null?"放款成功立即计息":borrow.getReverifyTime()));
			//0：备案中 1：初审中 2：出借中 3：复审中 4：还款中 5：已还款 6：已流标 7：待授权
			borrowProjectInfoBean.setStatus(borrow.getBorrowStatus());
			//0初始 1放款请求中 2放款请求成功 3放款校验成功 4放款校验失败 5放款失败 6放款成功
			borrowProjectInfoBean.setBorrowProgressStatus(borrow.getProjectStatus());
			
			if("endday".equals(borrow.getBorrowStyle())){
				borrowProjectInfoBean.setBorrowPeriodUnit("天");
			}else{
				borrowProjectInfoBean.setBorrowPeriodUnit("月");
			}
			borrowProjectInfoBean.setBorrowPeriod(borrow.getBorrowPeriod());
			borrowProjectInfoBean.setType(borrowNid.substring(0, 3));
			//只需要处理优享汇（融通宝）和尊享汇
			if("11".equals(borrow.getType())){
				borrowProjectInfoBean.setTag("尊享汇");
			}else if ("13".equals(borrow.getType())) {
				borrowProjectInfoBean.setTag("优享汇");
			}else{
				borrowProjectInfoBean.setTag("");
			}
			borrowProjectInfoBean.setRepayStyle(borrow.getRepayStyle());
			/**
             * 产品加息
             */
			BigDecimal borrowExtraYield = new BigDecimal(borrow.getBorrowExtraYield()==null?"0":borrow.getBorrowExtraYield());
            if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrowExtraYield)) {
                borrowProjectInfoBean.setBorrowExtraYield(borrow.getBorrowExtraYield());
            }else{
                borrowProjectInfoBean.setBorrowExtraYield("");
            }
            
			jsonObject.put("projectInfo", borrowProjectInfoBean);
			
			//借款人企业信息
			BorrowUsers borrowUsers = projectService.getBorrowUsersByNid(borrowNid);
			//借款人信息
			BorrowManinfo borrowManinfo = projectService.getBorrowManinfoByNid(borrowNid);
			
			//基础信息
			List<BorrowmsgBean> baseTableData = null;
			//项目介绍
			List<BorrowmsgBean> intrTableData = null;
			//信用状况
			List<BorrowmsgBean> credTableData = null;
			//审核信息
			List<BorrowmsgBean> reviewTableData = null;
			//其他信息
			List<BorrowmsgBean> otherTableData = null;
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
			
			//项目介绍
			intrTableData = packDetail(borrow, 3, borrowType, borrow.getBorrowLevel());
			// 风控信息
			AppRiskControlCustomize riskControl = projectService.selectRiskControl(borrowNid);
			if(riskControl!=null){
			    //riskControl.setControlMeasures(riskControl.getControlMeasures().replace("\r\n", ""));
	            //riskControl.setControlMort(riskControl.getControlMort().replace("\r\n", ""));
				riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
				riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
			}
			jsonObject.put("riskControl", riskControl);

			//处理借款信息
			List<BorrowProjectDetailBean> projectDetailList = new ArrayList<>();
			projectDetailList = dealDetail(projectDetailList, baseTableData, "baseTableData",null);
			if(userId != null){
				projectDetailList = dealDetail(projectDetailList, intrTableData, "intrTableData",null);
			}else {
				projectDetailList = dealDetail(projectDetailList, new ArrayList<BorrowmsgBean>(), "intrTableData",null);
			}
            projectDetailList = dealDetail(projectDetailList, credTableData, "credTableData",null);
            projectDetailList = dealDetail(projectDetailList, reviewTableData, "reviewTableData",null);
            // 信批需求新增(放款后才显示)
 			if(Integer.parseInt(borrow.getBorrowStatus())>=4 && borrowRepay != null){
 				//其他信息
 				String updateTime = getUpdateTime(Integer.parseInt(borrowRepay.getAddtime()), StringUtils.isBlank(borrowRepay.getRepayYestime())?null:Integer.parseInt(borrowRepay.getRepayYestime()));
 				projectDetailList = dealDetail(projectDetailList, otherTableData, "otherTableData",updateTime);
 			}
			jsonObject.put("projectDetail", projectDetailList);
			
			//处理借款信息
			List<BorrowRepayPlanBean> repayPlanList = new ArrayList<>();
			if("end".equals(borrow.getBorrowStyle()) || "endday".equals(borrow.getBorrowStyle())){
					List<BorrowRepayPlan> repayPlanLists = projectService.findRepayPlanByBorrowNid(borrowNid);
					BorrowRepayPlan borrowRepayPlan = repayPlanLists.get(0);
					BorrowRepayPlanBean borrowRepayPlanBean = new BorrowRepayPlanBean();
					if (borrowRepayPlan.getRepayTime().equals("-")) {
						borrowRepayPlanBean.setTime("-");
					} else {
						borrowRepayPlanBean.setTime(borrowRepayPlan.getRepayTime());
					}
					borrowRepayPlanBean.setNumber("第1期");
					borrowRepayPlanBean.setAccount(DF_FOR_VIEW.format(borrowRepayPlan.getRepayAccount()));
					repayPlanList.add(borrowRepayPlanBean);
			}else{
				List<BorrowRepayPlan> repayPlanLists = projectService.findRepayPlanByBorrowNid(borrowNid);
				if(repayPlanLists != null && repayPlanLists.size() > 0){
					for (int i = 0; i < repayPlanLists.size(); i++) {
						BorrowRepayPlan borrowRepayPlan = repayPlanLists.get(i);
						BorrowRepayPlanBean borrowRepayPlanBean = new BorrowRepayPlanBean();
						if (borrowRepayPlan.getRepayTime().equals("-")) {
							borrowRepayPlanBean.setTime("-");
						} else {
							borrowRepayPlanBean.setTime(borrowRepayPlan.getRepayTime());
						}
						borrowRepayPlanBean.setNumber("第"+(i+1)+"期");
						borrowRepayPlanBean.setAccount(DF_FOR_VIEW.format(borrowRepayPlan.getRepayAccount()));
						repayPlanList.add(borrowRepayPlanBean);
					}
				}
			}
			 // add 汇计划二期前端优化  针对区分原始标与债转标 nxl 20180424 start
            /**
             * 查看标的详情
             * 原始标：复审中、还款中、已还款状态下 如果当前用户是否投过此标，是：可看，否则不可见
             * 债转标的：未被完全承接时，项目详情都可看；被完全承接时，只有出借者和承接者可查看
             */
			// 如果用户未登录  则不能看
			int count = 0;
            if (userId != null) {
                count = projectService.countUserInvest(userId, borrowNid);
            }
			Boolean viewableFlag = false;
			String statusDescribe = "";
			Map<String, Object> mapObj = new HashMap<String, Object>();
			mapObj.put("borrowNid", borrowNid);
			List<HjhDebtCredit> listHjhDebtCredit = this.projectService.selectHjhDebtCreditList(mapObj);
			// add by nxl 是否发生过债转
			Boolean isDept = false;
			if (null != listHjhDebtCredit && listHjhDebtCredit.size() > 0) {
				// 部分承接
				List<Integer> valuesIn = new ArrayList<>();
				valuesIn.add(0);
				valuesIn.add(1);
				mapObj.put("inStatus", valuesIn);
				List<HjhDebtCredit> listHjhDebtCreditOnePlace = this.projectService.selectHjhDebtCreditList(mapObj);
				if (null != listHjhDebtCreditOnePlace && listHjhDebtCreditOnePlace.size() > 0) {
					// 部分债转
					viewableFlag = true;
				} else {
					// 完全债转
        			for(HjhDebtCredit deptcredit:listHjhDebtCredit) {
        				//待承接本金 = 0
        				if(null!=deptcredit.getCreditCapitalWait()&&deptcredit.getCreditCapitalWait().intValue()==0) {
        					Map<String,Object> mapParam = new HashMap<String,Object>();
                            int intCount = 0;
                            if (userId != null) {
                                mapParam.put("userId", userId);
                                mapParam.put("borrowNid", borrowNid);
                                projectService.countCreditTender(mapParam);
                            }
                    		if(intCount>0||count>0) {
                    			viewableFlag = true;
                    		}
        				}
        			}
				}
				statusDescribe = "还款中";
				isDept = true;
			} else {
				// 原始标
				// 复审中，还款中和已还款状态出借者(可看)
				if (borrow.getBorrowStatus().equals("3") || borrow.getBorrowStatus().equals("4")
						|| borrow.getBorrowStatus().equals("5")) {
					if (count > 0) {
						// 可以查看标的详情
						viewableFlag = true;
					} else {
						// 提示仅出借者可看
						viewableFlag = false;
					}
				} else {
					viewableFlag = true;
				}
				// 原始标根据状态显示
				switch (borrow.getBorrowStatus()) {
				case "0":
					statusDescribe = "备案中";
					break;
				case "1":
					statusDescribe = "初审中";
					break;
				case "2":
					statusDescribe = "出借中";
					break;
				case "3":
					statusDescribe = "复审中";
					break;
				case "4":
					statusDescribe = "还款中";
					break;
				case "5":
					statusDescribe = "已还款";
					break;
				case "6":
					statusDescribe = "已流标";
					break;
				case "7":
					statusDescribe = "待授权";
					break;
				default:
					break;
				}
			}
			userValidation.put("viewableFlag", viewableFlag);
			userValidation.put("statusDescribe", statusDescribe);
			// 是否发生过债转
			userValidation.put("isDept", isDept);
			jsonObject.put("userValidation", userValidation);
            // add 汇计划二期前端优化  针对区分原始标与债转标  nxl 20180424 end
			jsonObject.put("repayPlan", repayPlanList);

			LogUtil.endLog(BorrowProjectDefine.THIS_CLASS, BorrowProjectDefine.PROJECT_DETAIL_ACTION);
			return jsonObject;
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

	
	/**
	 * 处理对象数据
	 * @param projectDetailList 返回List对象
	 * @param tableData 传入参数
	 * @param keys 参数含义
	 * @return
	 */
	private List<BorrowProjectDetailBean> dealDetail(List<BorrowProjectDetailBean> projectDetailList,List<BorrowmsgBean> tableData, String keys, String updateTime){
		if(tableData != null && tableData.size() > 0){
			BorrowProjectDetailBean projectDetailBean = new BorrowProjectDetailBean();
			if("baseTableData".equals(keys)){
				projectDetailBean.setTitle("基础信息");
			}
			if("intrTableData".equals(keys)){
				projectDetailBean.setTitle("项目介绍");
			}
			if("credTableData".equals(keys)){
				projectDetailBean.setTitle("信用状况");
			}
			if("reviewTableData".equals(keys)){
				projectDetailBean.setTitle("审核状态");
			}
			if("otherTableData".equals(keys)){
				projectDetailBean.setTitle("其他信息（更新于" + updateTime + "）");
			}
			projectDetailBean.setId("");
			projectDetailBean.setMsg(tableData);
			projectDetailList.add(projectDetailBean);
		}
		return projectDetailList;
	}
	
	/**
	 * 封装项目详情页
	 * @param objBean
	 * @param type  1 基础信息 2资产信息 3项目介绍 4信用状况 5审核状况
	 * @param borrowType  1借款人 2企业借款
	 * @param borrowLevel  信用评级
	 * @return
	 */
	private List<BorrowmsgBean> packDetail(Object objBean,int type,int borrowType, String borrowLevel){
		List<BorrowmsgBean> detailBeanList = new ArrayList<BorrowmsgBean>();
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
						BorrowmsgBean detailBean = new BorrowmsgBean();
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
								BorrowmsgBean carBean = new BorrowmsgBean();
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
								if(StringUtils.isNotBlank(DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())))){
									detailBean.setVal(detailBean.getVal());
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
			BorrowmsgBean detailBean = new BorrowmsgBean();
			detailBean.setId("borrowLevel");
			detailBean.setKey("信用评级");
			detailBean.setVal(borrowLevel);
			detailBeanList.add(detailBean);
		}
		return detailBeanList;
	}
	
	/**
	 * 查询相应的项目的出借列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = BorrowProjectDefine.PROJECT_INVEST_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject searchProjectInvestList(@PathVariable("borrowId") String borrowNid, HttpServletRequest request, HttpServletResponse response) {

		JSONObject info = new JSONObject();
		LogUtil.startLog(BorrowProjectDefine.THIS_CLASS, BorrowProjectDefine.PROJECT_INVEST_ACTION);
		Integer currentPage = 1;
		if(request.getParameter("currentPage") != null){
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		Integer size = 10;
		if(request.getParameter("pageSize") != null){
			size = Integer.parseInt(request.getParameter("pageSize"));
		}
		info.put("status", BorrowProjectDefine.SUCCESS);
		info.put("statusDesc", BorrowProjectDefine.SUCCESS_MSG);

		ProjectInvestBean form = new ProjectInvestBean();
		form.setBorrowNid(borrowNid);
		form.setPage(currentPage);
		form.setPageSize(size);
		this.createProjectInvestPage(info, form);
		LogUtil.endLog(BorrowProjectDefine.THIS_CLASS, BorrowProjectDefine.PROJECT_INVEST_ACTION);
		return info;
	}
	
	/**
	 * 创建相应的项目的用户出借分页信息
	 * 
	 * @param info
	 * @param form
	 */

	private void createProjectInvestPage(JSONObject info, ProjectInvestBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", form.getBorrowNid());
		int recordTotal = this.projectService.countProjectInvestRecordTotal(params);
		String count = this.projectService.countMoneyByBorrowId(params);
		if(count != null && !"".equals(count)){
			info.put("account", DF_FOR_VIEW.format(new BigDecimal(count)));
		}else{
			info.put("account", "0");
		}
		if (recordTotal > 0) { // 查询相应的汇直投列表数据
			int limit = form.getPageSize();
			int page = form.getPage();
			int offSet = (page - 1) * limit;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<AppProjectInvestListCustomize> recordList = projectService.searchProjectInvestList(params);
			List<ActdecListedOne> alo = this.projectService.getActdecList(form.getBorrowNid());
			if(!alo.isEmpty()) {
				if(page==1) {
					AppProjectInvestListCustomize all=recordList.get(0);
					all.setRedbag("1");
					recordList.set(0, all);
				}
				if((page * limit) > recordTotal){
					AppProjectInvestListCustomize all=recordList.get(recordList.size()-1);
					all.setRedbag("1");
					recordList.set(recordList.size()-1, all);
				}

			}
			info.put("list", recordList);
			info.put("userCount", String.valueOf(recordTotal));
			//判断本次查询是否已经全部查出数据
			if((page * limit) > recordTotal){
				info.put("isEnd", true);
			}else{
				info.put("isEnd", false);
			}
		} else {
			info.put("list", new ArrayList<AppProjectInvestListCustomize>());
			info.put("userCount", "0");
			info.put("isEnd", true);
		}
	}

	/**
	 * add by nxl 新加承接记录列表显示 20180515 查询相应的项目的出借列表
	 * @param borrowNid
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BorrowProjectDefine.PROJECT_UNDERTAKE_ACTION, produces = "application/json; charset=utf-8")
	public JSONObject searchProjectUndertakeList(@PathVariable("borrowId") String borrowNid, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject info = new JSONObject();
		LogUtil.startLog(BorrowProjectDefine.THIS_CLASS, BorrowProjectDefine.PROJECT_UNDERTAKE_ACTION);
		Integer currentPage = 1;
		if (request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		Integer size = 10;
		if (request.getParameter("pageSize") != null) {
			size = Integer.parseInt(request.getParameter("pageSize"));
		}
		info.put("status", BorrowProjectDefine.SUCCESS);
		info.put("statusDesc", BorrowProjectDefine.SUCCESS_MSG);

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("page", currentPage);
		mapParam.put("pageSize", size);
		mapParam.put("borrowNid", borrowNid);
		this.createProjectUndertakePage(info, mapParam);
		LogUtil.endLog(BorrowProjectDefine.THIS_CLASS, BorrowProjectDefine.PROJECT_UNDERTAKE_ACTION);
		return info;
	}

	/**
	 * 承接记录列表分页
	 * @param info
	 * @param mapParam
	 */
	private void createProjectUndertakePage(JSONObject info, Map<String, Object> mapParam) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("borrowNid", mapParam.get("borrowNid").toString());
		int undertRecordCount = this.borrowService.countCreditTenderByBorrowNid(mapParam.get("borrowNid").toString());
		String strAccount = this.borrowService.sumUndertakeAccount(mapParam.get("borrowNid").toString());
		String strUndertakeAccount = DF_FOR_VIEW.format(new BigDecimal("0"));
		if (StringUtils.isNotEmpty(strAccount)) {
			BigDecimal bdAccount = new BigDecimal(strAccount);
			strUndertakeAccount = DF_FOR_VIEW.format(bdAccount);
		}
		if (undertRecordCount > 0) {
			// 查询相应的汇直投列表数据
			int limit = Integer.parseInt(mapParam.get("pageSize").toString());
			int page = Integer.parseInt(mapParam.get("page").toString());
			int offSet = (page - 1) * limit;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<WebProjectUndertakeListCustomize> undertRecordList = this.borrowService
					.selectProjectUndertakeList(params);
			info.put("list", undertRecordList);
			// 总承接金额
			info.put("undertakeAccount", String.valueOf(strUndertakeAccount));
			// 承接总人次
			info.put("userCount", undertRecordCount);
			// 判断本次查询是否已经全部查出数据
			if ((page * limit) > undertRecordCount) {
				info.put("isEnd", true);
			} else {
				info.put("isEnd", false);
			}
		} else {
			info.put("list", new ArrayList<WebProjectUndertakeListCustomize>());
			// 总承接金额
			info.put("undertakeAccount", "0");
			// 承接总人次
			info.put("userCount", 0);
			info.put("isEnd", true);
		}
	}
	
}
