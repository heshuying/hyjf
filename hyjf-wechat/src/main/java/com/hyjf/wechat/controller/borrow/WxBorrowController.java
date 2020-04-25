
package com.hyjf.wechat.controller.borrow;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.wechat.service.login.LoginService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.hyjf.bank.service.user.tender.TenderService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallResult;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.BaseResultBeanFrontEnd;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.base.SimpleResultBean;
import com.hyjf.wechat.controller.user.coupon.CouponBean;
import com.hyjf.wechat.model.borrow.BorrowProjectDetailBean;
import com.hyjf.wechat.model.borrow.BorrowRepayPlanBean;
import com.hyjf.wechat.model.borrow.BorrowmsgBean;
import com.hyjf.wechat.model.borrow.ProjectInvestBean;
import com.hyjf.wechat.model.borrow.WxBorrowProjectInfoBean;
import com.hyjf.wechat.model.borrow.WxTenderVo;
import com.hyjf.wechat.model.borrow.result.BorrowDetailResultBean;
import com.hyjf.wechat.model.borrow.result.BorrowInvestListResultBean;
import com.hyjf.wechat.model.borrow.result.WxTenderResultBean;
import com.hyjf.wechat.service.borrow.WxBorrowService;
import com.hyjf.wechat.service.borrow.WxBorrowTenderService;
import com.hyjf.wechat.service.coupon.WxCouponService;
import com.hyjf.wechat.util.ResultEnum;

@Controller
@RequestMapping(value = WxBorrowDefine.REQUEST_MAPPING)
public class WxBorrowController extends BaseController {

    @Autowired
    private WxBorrowService wxBorrowService;
    @Autowired
    private WxBorrowTenderService wxBorrowTenderService;
    @Autowired
    private WxCouponService wxCouponService;
    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TenderService tenderService;

    @Autowired
    private LoginService loginService;

    private static DecimalFormat DF_FOR_VIEW = new DecimalFormat("#,##0.00");

    /**
     * 查询相应的项目详情
     */
    @ResponseBody
    @RequestMapping(value = WxBorrowDefine.PROJECT_DETAIL_ACTION)
    public BaseResultBean searchProjectDetail(@PathVariable("borrowId") String borrowNid,
                                              HttpServletRequest request) {

        LogUtil.startLog(WxBorrowDefine.THIS_CLASS, WxBorrowDefine.PROJECT_DETAIL_ACTION);
        BorrowDetailResultBean borrowDetailResultBean = new BorrowDetailResultBean();
        JSONObject userValidation = new JSONObject();
        Integer userId = requestUtil.getRequestUserId(request);
        String type = request.getParameter("borrowType");
        //获取用户个人信息
        boolean isLogined = false;
        boolean isOpened = false;
        boolean isSetPassword = false;
        boolean isAllowed = false;
        String isRiskTested = "0";
        boolean isAutoInves = false;
        boolean isInvested = false;
        Integer isPaymentAuth = 0;
        Integer roleId = 0;
        // 判断用户是否登录
        if (userId != null && StringUtils.isNotBlank(userId + "") && userId > 0) {
            isLogined = true;
            Users users = wxBorrowService.getUsers(userId);
            
            if (users != null) {
                //判断是否开户
                if (users.getBankOpenAccount() != null && users.getBankOpenAccount() == 1) {
                    isOpened = true;
                }
                
                // 检查用户角色是否能出借  合规接口改造之后需要判断
                UsersInfo userInfo = requestUtil.getUsersInfoByUserId(userId);
                roleId=userInfo.getRoleId();
                String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
                if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
                    if (userInfo.getRoleId() != 1) {// 担保机构用户
                        borrowDetailResultBean.setStatus("99");
                        borrowDetailResultBean.setStatusDesc("仅限出借人进行出借");
                        borrowDetailResultBean.setIsAllowedTender(Boolean.FALSE);
                    }
                }


                
                //判断是否设置交易密码
                if (users.getIsSetPassword() != null && users.getIsSetPassword() == 1) {
                    isSetPassword = true;
                }
                
                //是否授权
                if (users.getAuthStatus() != null && users.getAuthStatus() == 1) {
                    isAutoInves = true;
                }
                
                //服务费权状态
                if(users.getPaymentAuthStatus()!=null && users.getPaymentAuthStatus()==1){
                	isPaymentAuth = users.getPaymentAuthStatus();
                }
                
                //是否允许使用
                if (users.getStatus() != null && users.getStatus() == 0) {
                    isAllowed = true;
                }
                //是否完成风险测评
				if(users.getIsEvaluationFlag()==1 && null != users.getEvaluationExpiredTime()){
					//测评到期日
					Long lCreate = users.getEvaluationExpiredTime().getTime();
					//当前日期
					Long lNow = System.currentTimeMillis();
					if (lCreate <= lNow) {
						//已过期需要重新评测
						isRiskTested = "2";
					} else {
						//未到一年有效期
						isRiskTested = "1";
					}
				}else{
					isRiskTested = "0";
				}
                boolean isTender = wxBorrowService.isTenderBorrow(userId, borrowNid, type);
                if (isTender) {
                    isInvested = true;
                }
            }else{
            	borrowDetailResultBean.setStatus("99");
            	borrowDetailResultBean.setStatusDesc("用户信息不存在");
            	borrowDetailResultBean.setIsAllowedTender(Boolean.FALSE);
            }
        }
        // 2.根据项目标号获取相应的项目信息
        WxBorrowProjectInfoBean borrowProjectInfoBean = new WxBorrowProjectInfoBean();
        AppProjectDetailCustomize borrow = this.wxBorrowService.selectProjectDetail(borrowNid);
        // 获取还款信息 add by jijun 2018/04/27
        BorrowRepay borrowRepay = this.wxBorrowService.getBorrowRepay(borrowNid);

        userValidation.put("isLogined", isLogined);
        userValidation.put("isOpened", isOpened);
        userValidation.put("isSetPassword", isSetPassword);
        userValidation.put("isAllowed", isAllowed);
        userValidation.put("isRiskTested", isRiskTested);
        userValidation.put("isAutoInves", isAutoInves);
        userValidation.put("isInvested", isInvested);
        //是否缴费授权
        userValidation.put("paymentAuthStatus", isPaymentAuth);
        //角色认证是否打开
        userValidation.put("isCheckUserRole", Boolean.parseBoolean(PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN)));
        userValidation.put("paymentAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
        //自动出借开关
        userValidation.put("invesAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH).getEnabledStatus());
        //自动债转开关
        userValidation.put("creditAuthOn", CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_CREDIT_AUTH).getEnabledStatus());
        userValidation.put("roleId", roleId);

        borrowDetailResultBean.setUserValidation(userValidation);

        //获取标的信息
        if (borrow == null) {
            borrowDetailResultBean.setEnum(ResultEnum.BORROE_ERROR_100);
            return borrowDetailResultBean;
        } else {
            borrowDetailResultBean.setEnum(ResultEnum.SUCCESS);
            borrowProjectInfoBean.setBorrowRemain(borrow.getInvestAccount());
            borrowProjectInfoBean.setBorrowProgress(borrow.getBorrowSchedule());
            borrowProjectInfoBean.setOnTime(borrow.getOnTime());
            borrowProjectInfoBean.setAccount(borrow.getAccount());
            borrowProjectInfoBean.setBorrowApr(borrow.getBorrowApr());
            borrowProjectInfoBean.setBorrowId(borrowNid);
            borrowProjectInfoBean.setInvestLevel(borrow.getInvestLevel());
            // 加息率为0的话不显示
            if(borrow.getBorrowExtraYield()!=null&&!"".equals(borrow.getBorrowExtraYield())){
                if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), new BigDecimal(borrow.getBorrowExtraYield()))) {
                    borrowProjectInfoBean.setBorrowExtraYield(borrow.getBorrowExtraYield());
                }else{
                    borrowProjectInfoBean.setBorrowExtraYield("");
                }
            }else{
                borrowProjectInfoBean.setBorrowExtraYield("");
            }

            borrowProjectInfoBean.setOnAccrual((borrow.getReverifyTime() == null ? "放款成功立即计息" : borrow.getReverifyTime()));
            //0：备案中 1：初审中 2：出借中 3：复审中 4：还款中 5：已还款 6：已流标 7：待授权
            borrowProjectInfoBean.setStatus(borrow.getBorrowStatus());
            //0初始 1放款请求中 2放款请求成功 3放款校验成功 4放款校验失败 5放款失败 6放款成功
            borrowProjectInfoBean.setBorrowProgressStatus(borrow.getProjectStatus());

            if ("endday".equals(borrow.getBorrowStyle())) {
                borrowProjectInfoBean.setBorrowPeriodUnit("天");
            } else {
                borrowProjectInfoBean.setBorrowPeriodUnit("月");
            }
            borrowProjectInfoBean.setBorrowPeriod(borrow.getBorrowPeriod());
            borrowProjectInfoBean.setType(borrowNid.substring(0, 3));
            //只需要处理优享汇（融通宝）和尊享汇
            if ("11".equals(borrow.getType())) {
                borrowProjectInfoBean.setTag("尊享汇");
            } else if ("13".equals(borrow.getType())) {
                borrowProjectInfoBean.setTag("优享汇");
            } else {
                borrowProjectInfoBean.setTag("");
            }
            borrowProjectInfoBean.setRepayStyle(borrow.getRepayStyle());
            borrowDetailResultBean.setProjectInfo(borrowProjectInfoBean);

            //获取项目详情信息
            //借款人企业信息
            BorrowUsers borrowUsers = wxBorrowService.getBorrowUsersByNid(borrowNid);
            //借款人信息
            BorrowManinfo borrowManinfo = wxBorrowService.getBorrowManinfoByNid(borrowNid);
            //基础信息
            List<BorrowmsgBean> baseTableData = null;
            //项目介绍
            List<BorrowmsgBean> intrTableData = null;
            //信用状况
            List<BorrowmsgBean> credTableData = null;
            //审核信息
            List<BorrowmsgBean> reviewTableData = null;
            //其他信息 add by jijun 2018/04/27
			List<BorrowmsgBean> otherTableData = null;
            //借款类型
            int borrowType = Integer.parseInt(borrow.getComOrPer());

            if (borrowType == 1 && borrowUsers != null) {
                //基础信息
                baseTableData = packDetail(borrowUsers, 1, borrowType, borrow.getBorrowLevel());
                //信用状况
                credTableData = packDetail(borrowUsers, 4, borrowType, borrow.getBorrowLevel());
                //审核信息
                reviewTableData = packDetail(borrowUsers, 5, borrowType, borrow.getBorrowLevel());
                //其他信息 add by jijun 2018/04/27
				otherTableData =  packDetail(borrowUsers, 6, borrowType, borrow.getBorrowLevel());
            } else {
                if (borrowManinfo != null) {
                    //基础信息
                    baseTableData = packDetail(borrowManinfo, 1, borrowType, borrow.getBorrowLevel());
                    //信用状况
                    credTableData = packDetail(borrowManinfo, 4, borrowType, borrow.getBorrowLevel());
                    //审核信息
                    reviewTableData = packDetail(borrowManinfo, 5, borrowType, borrow.getBorrowLevel());
                    //其他信息 add by jijun 2018/04/27
					otherTableData =  packDetail(borrowManinfo, 6, borrowType, borrow.getBorrowLevel());
                }
            }

            //项目介绍
            intrTableData = packDetail(borrow, 3, borrowType, borrow.getBorrowLevel());

        	// 风控信息
			AppRiskControlCustomize riskControl = wxBorrowService.selectRiskControl(borrowNid);
			if(riskControl==null){
				riskControl = new AppRiskControlCustomize();
				riskControl.setControlMeasures("");
	            riskControl.setControlMort("");
			}else {
			    riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
	            riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
			}
			//风控信息对象返回给前端
            borrowDetailResultBean.setAppRiskControlCustomize(riskControl);
			
            //处理借款信息
            List<BorrowProjectDetailBean> projectDetailList = new ArrayList<>();
            projectDetailList = dealDetail(projectDetailList, baseTableData, "baseTableData",null);
            if (userId != null) {
                projectDetailList = dealDetail(projectDetailList, intrTableData, "intrTableData",null);
            } else {
                projectDetailList = dealDetail(projectDetailList, new ArrayList<BorrowmsgBean>(), "intrTableData",null);
            }
            projectDetailList = dealDetail(projectDetailList, credTableData, "credTableData",null);
            projectDetailList = dealDetail(projectDetailList, reviewTableData, "reviewTableData",null);
            // 信批需求新增(放款后才显示)
 			if(Integer.parseInt(borrow.getBorrowStatus())>=4){
 				//其他信息
 				String updateTime = getUpdateTime(Integer.parseInt(borrowRepay.getAddtime()), StringUtils.isBlank(borrowRepay.getRepayYestime())?null:Integer.parseInt(borrowRepay.getRepayYestime()));
 				projectDetailList = dealDetail(projectDetailList, otherTableData, "otherTableData",updateTime);
 			}


            borrowDetailResultBean.setProjectDetail(projectDetailList);


            //获取还款计划
            //处理借款信息
            List<BorrowRepayPlanBean> repayPlanList = new ArrayList<>();
            if ("end".equals(borrow.getBorrowStyle()) || "endday".equals(borrow.getBorrowStyle())) {
                List<BorrowRepayPlan> repayPlanLists = wxBorrowService.findRepayPlanByBorrowNid(borrowNid);
                BorrowRepayPlan borrowRepayPlan = repayPlanLists.get(0);
                BorrowRepayPlanBean borrowRepayPlanBean = new BorrowRepayPlanBean();
                if (borrowRepayPlan.getRepayTime().equals("-")) {
                    borrowRepayPlanBean.setTime("--");
                } else {
                    borrowRepayPlanBean.setTime(borrowRepayPlan.getRepayTime());
                }
                borrowRepayPlanBean.setNumber("第1期");
                borrowRepayPlanBean.setAccount(DF_FOR_VIEW.format(borrowRepayPlan.getRepayAccount()));
                repayPlanList.add(borrowRepayPlanBean);
            } else {
                List<BorrowRepayPlan> repayPlanLists = wxBorrowService.findRepayPlanByBorrowNid(borrowNid);
                if (repayPlanLists != null && repayPlanLists.size() > 0) {
                    for (int i = 0; i < repayPlanLists.size(); i++) {
                        BorrowRepayPlan borrowRepayPlan = repayPlanLists.get(i);
                        BorrowRepayPlanBean borrowRepayPlanBean = new BorrowRepayPlanBean();
                        if (borrowRepayPlan.getRepayTime().equals("-")) {
                            borrowRepayPlanBean.setTime("--");
                        } else {
                            borrowRepayPlanBean.setTime(borrowRepayPlan.getRepayTime());
                        }
                        borrowRepayPlanBean.setNumber("第" + (i + 1) + "期");
                        borrowRepayPlanBean.setAccount(DF_FOR_VIEW.format(borrowRepayPlan.getRepayAccount()));
                        repayPlanList.add(borrowRepayPlanBean);
                    }
                }
            }
            borrowDetailResultBean.setRepayPlan(repayPlanList);
            borrowDetailResultBean.setBorrowMeasuresMea(borrow.getBorrowMeasuresMea());
            LogUtil.endLog(WxBorrowDefine.THIS_CLASS, WxBorrowDefine.PROJECT_DETAIL_ACTION);
            return borrowDetailResultBean;
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
     * 封装项目详情页
     *
     * @param objBean
     * @param type        1 基础信息 2资产信息 3项目介绍 4信用状况 5审核状况
     * @param borrowType  1借款人 2企业借款
     * @param borrowLevel 信用评级
     * @return
     */
    private List<BorrowmsgBean> packDetail(Object objBean, int type, int borrowType, String borrowLevel) {
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
                        if (type == 1) {
                            if (borrowType == 2) {//个人借款
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
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("男");
                                        } else {
                                            detailBean.setVal("女");
                                        }
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "old":
                                        if (!"0".equals(detailBean.getVal())) {
                                            detailBean.setKey("年龄");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "merry":
                                        if (!("0".equals(result.toString()) || result.toString() == null)) {
                                            detailBean.setKey("婚姻状况");
                                            if ("1".equals(result.toString())) {
                                                detailBean.setVal("已婚");
                                            } else if ("2".equals(result.toString())) {
                                                detailBean.setVal("未婚");
                                            } else if ("3".equals(result.toString())) {
                                                detailBean.setVal("离异");
                                            } else if ("4".equals(result.toString())) {
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
                                    default:
                                        break;
                                }
                            } else {//企业借款

                                switch (fName) {
                                    case "currencyName":
                                        currencyName = detailBean.getVal();
                                        break;
                                    case "username":
                                        detailBean.setKey("借款主体");
                                        detailBean.setVal(AsteriskProcessUtil.getAsteriskedValue(detailBean.getVal(), detailBean.getVal().length() - 2));
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "city":
                                        detailBean.setKey("注册地区");
                                        detailBeanList.add(detailBean);
                                        break;
                                    case "regCaptial":
                                        detailBean.setKey("注册资本");
                                        if (StringUtils.isNotBlank(detailBean.getVal())) {
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
                                    default:
                                        break;
                                }
                            }
                        } else if (type == 2) {
                            switch (fName) {
                                case "housesType":
                                    detailBean.setKey("资产类型");
                                    String houseType = this.wxBorrowService.getParamName("HOUSES_TYPE", detailBean.getVal());
                                    if (houseType != null) {
                                        detailBean.setVal(houseType);
                                    } else {
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
                                    if (StringUtils.isNotBlank(detailBean.getVal())) {
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
                                    if (StringUtils.isNotBlank(detailBean.getVal())) {
                                        detailBean.setVal(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())) + "元");
                                    }
                                    detailBeanList.add(detailBean);
                                    break;
                                case "toprice":
                                    detailBean.setKey("评估价值");
                                    if (StringUtils.isNotBlank(detailBean.getVal())) {
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

                        } else if (type == 3) {
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
                                    if (StringUtils.isNotBlank(CustomConstants.DF_FOR_VIEW.format(new BigDecimal(detailBean.getVal())))) {
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
                        } else if (type == 4) {
                            switch (fName) {
                                case "overdueTimes":
                                    detailBean.setKey("在平台逾期次数");
                                    detailBeanList.add(detailBean);
                                    break;
                                case "overdueAmount":
                                    detailBean.setKey("在平台逾期金额");
                                    if (StringUtils.isNotBlank(detailBean.getVal())) {
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
                        } else if (type == 5) {
                            if (borrowType == 2) {
                                switch (fName) {
                                    case "isCard":
                                        detailBean.setKey("身份证");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isIncome":
                                        detailBean.setKey("收入状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isCredit":
                                        detailBean.setKey("信用状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isAsset":
                                        detailBean.setKey("资产状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isVehicle":
                                        detailBean.setKey("车辆状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isDrivingLicense":
                                        detailBean.setKey("行驶证");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isVehicleRegistration":
                                        detailBean.setKey("车辆登记证");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isMerry":
                                        detailBean.setKey("婚姻状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isWork":
                                        detailBean.setKey("工作状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isAccountBook":
                                        detailBean.setKey("户口本");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                switch (fName) {
                                    case "isCertificate":
                                        detailBean.setKey("企业证件");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isOperation":
                                        detailBean.setKey("经营状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isFinance":
                                        detailBean.setKey("财务状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isEnterpriseCreidt":
                                        detailBean.setKey("企业信用");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isLegalPerson":
                                        detailBean.setKey("法人信息");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isAsset":
                                        detailBean.setKey("资产状况");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isPurchaseContract":
                                        detailBean.setKey("购销合同");
                                        if ("1".equals(result.toString())) {
                                            detailBean.setVal("已审核");
                                            detailBeanList.add(detailBean);
                                        }
                                        break;
                                    case "isSupplyContract":
                                        detailBean.setKey("供销合同");
                                        if ("1".equals(result.toString())) {
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
        if (type == 1 || type == 4) {
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
     * 处理对象数据
     *
     * @param projectDetailList 返回List对象
     * @param tableData         传入参数
     * @param keys              参数含义
     * @return
     */
    private List<BorrowProjectDetailBean> dealDetail(List<BorrowProjectDetailBean> projectDetailList, List<BorrowmsgBean> tableData, String keys, String updateTime) {
        if (tableData != null && tableData.size() > 0) {
            BorrowProjectDetailBean projectDetailBean = new BorrowProjectDetailBean();
            if ("baseTableData".equals(keys)) {
                projectDetailBean.setTitle("基础信息");
            }
            if ("intrTableData".equals(keys)) {
                projectDetailBean.setTitle("项目介绍");
            }
            if ("credTableData".equals(keys)) {
                projectDetailBean.setTitle("信用状况");
            }
            if ("reviewTableData".equals(keys)) {
                projectDetailBean.setTitle("审核状态");
            }
            //add by jijun 2018/04/27
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
     * 查询相应的项目的出借列表
     *
     * @param request
     * @param response
     * @return
     */

    @ResponseBody
    @RequestMapping(value = WxBorrowDefine.PROJECT_INVEST_ACTION, produces = "application/json; charset=utf-8")
    public BaseResultBean searchProjectInvestList(@PathVariable("borrowId") String borrowNid, HttpServletRequest request, HttpServletResponse response) {

        BorrowInvestListResultBean borrowInvestListResultBean = new BorrowInvestListResultBean();
        LogUtil.startLog(WxBorrowDefine.THIS_CLASS, WxBorrowDefine.PROJECT_INVEST_ACTION);
        Integer currentPage = 1;
        if (request.getParameter("currentPage") != null) {
            currentPage = Integer.parseInt(request.getParameter("currentPage"));
        }
        Integer size = 10;
        if (request.getParameter("pageSize") != null) {
            size = Integer.parseInt(request.getParameter("pageSize"));
        }
        borrowInvestListResultBean.setEnum(ResultEnum.SUCCESS);

        ProjectInvestBean form = new ProjectInvestBean();
        form.setBorrowNid(borrowNid);
        form.setPage(currentPage);
        form.setPageSize(size);
        this.createProjectInvestPage(borrowInvestListResultBean, form);
        LogUtil.endLog(getClass().getName(), WxBorrowDefine.PROJECT_INVEST_ACTION);
        return borrowInvestListResultBean;
    }

    /**
     * 创建相应的项目的用户出借分页信息
     *
     * @param borrowInvestListResultBean
     * @param form
     */

    private void createProjectInvestPage(BorrowInvestListResultBean borrowInvestListResultBean, ProjectInvestBean form) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("borrowNid", form.getBorrowNid());
        int recordTotal = this.wxBorrowService.countProjectInvestRecordTotal(params);
        String count = this.wxBorrowService.countMoneyByBorrowId(params);
        if (count != null && !"".equals(count)) {
            borrowInvestListResultBean.setAccount(DF_FOR_VIEW.format(new BigDecimal(count)));
        } else {
            borrowInvestListResultBean.setAccount("0.00");
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
            List<AppProjectInvestListCustomize> recordList = wxBorrowService.searchProjectInvestList(params);
            borrowInvestListResultBean.setList(recordList);
            borrowInvestListResultBean.setUserCount(String.valueOf(recordTotal));
            //判断本次查询是否已经全部查出数据
            if ((page * limit) > recordTotal) {
                borrowInvestListResultBean.setEnd(true);
            } else {
                borrowInvestListResultBean.setEnd(false);
            }
        } else {
            borrowInvestListResultBean.setList(new ArrayList<AppProjectInvestListCustomize>());
            borrowInvestListResultBean.setUserCount("0");
            borrowInvestListResultBean.setEnd(true);
        }
    }

    /**
     * 查询散标出借信息接口，預期收益等
     *
     * @param request
     * @return
     */
    @SignValidate
    @ResponseBody
    @RequestMapping(value = WxBorrowDefine.GET_INVEST_INFO_MAPPING)
    public BaseResultBean getInvestInfo(HttpServletRequest request) {
        SimpleResultBean<InvestInfoResultVo> resultBean = new SimpleResultBean<>();
        InvestInfoResultVo vo = new InvestInfoResultVo();
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
        Integer userId = requestUtil.getRequestUserId(request);
        // 检查用户角色是否能出借  合规接口改造之后需要判断
        UsersInfo userInfo = requestUtil.getUsersInfoByUserId(userId);
        String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
        if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
            if (userInfo.getRoleId() != 1) {// 担保机构用户
                vo.setStatus("99");
                vo.setStatusDesc("仅限出借人进行出借");
                vo.setIsAllowedTender(Boolean.FALSE);
            }
        }
        
        String borrowNid = request.getParameter("borrowNid");
        String money = request.getParameter("money");
        //
        String version = "2.1.0";

        //优惠券有可能为空
        String couponId = request.getParameter("couponId");
        logger.info("使用的优惠券id: {}", couponId);

        Preconditions.checkArgument(StringUtils.isNotEmpty(borrowNid) && StringUtils.isNotEmpty(money), "参数不正确！");

        String investType = borrowNid.substring(0, 3);
        Preconditions.checkArgument((!("HZR".equals(investType))) && (!("HJH".equals(investType))), "不是散标项目！");

        Borrow borrow = wxBorrowService.getBorrowByNid(borrowNid);
        Preconditions.checkArgument(borrow != null);
        vo.setBorrowApr(borrow.getBorrowApr() + "%");
        vo.setBorrowNid(borrowNid);

        //设置是否有可用优惠券
        Map<String, Object> mapUserCouponInfo = wxCouponService.getProjectAvailableUserCoupon(borrowNid, money, userId);
        int availableCouponCount = (int) mapUserCouponInfo.get("availableCouponListCount");
        int notAvailableCouponCount = (int) mapUserCouponInfo.get("notAvailableCouponListCount");
        if (availableCouponCount > 0) {
            vo.setIsThereCoupon("1");
            vo.setCouponDescribe("请选择");
        } else if (availableCouponCount == 0 && notAvailableCouponCount > 0) {
            vo.setIsThereCoupon("1");
            vo.setCouponDescribe("暂无可用");
        } else {
            vo.setIsThereCoupon("0");
            vo.setCouponDescribe("无可用");
        }


        BigDecimal borrowAccountWait = borrow.getBorrowAccountWait();
        // 去最小值 最大可投和 项目可投
        if (borrow.getTenderAccountMax() != null && borrowAccountWait != null && (borrow.getProjectType() == 4 || borrow.getProjectType() == 11)) {
            BigDecimal TenderAccountMax = new BigDecimal(borrow.getTenderAccountMax());
            if (TenderAccountMax.compareTo(borrowAccountWait) == -1) {
                vo.setBorrowAccountWait(CommonUtils.formatAmount(version, TenderAccountMax));
            } else {
                vo.setBorrowAccountWait(CommonUtils.formatAmount(version, borrowAccountWait));
            }
        } else {
            vo.setBorrowAccountWait(CommonUtils.formatAmount(version, borrowAccountWait));
        }
        String balanceWait = borrow.getBorrowAccountWait() + "";
        if (balanceWait == null || balanceWait.equals("")) {
            balanceWait = "0";
        }
        // 剩余可投小于起投，计算收益按照剩余可投计算
        if ((org.apache.commons.lang3.StringUtils.isBlank(money) || money.equals("0")) && new BigDecimal(balanceWait).compareTo(new BigDecimal(borrow.getTenderAccountMin())) < 1) {
            money = new BigDecimal(balanceWait).intValue() + "";
        }

        // 设置产品加息 显示收益率
        if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
            vo.setBorrowExtraYield(borrow.getBorrowExtraYield()+"%");
        }

        //計算本金收益
        BigDecimal earnings = new BigDecimal("0");
        if (!StringUtils.isBlank(money) && Double.parseDouble(money) >= 0) {
            String borrowStyle = borrow.getBorrowStyle();
            // 收益率
            BigDecimal borrowApr = borrow.getBorrowApr();
            // 周期
            Integer borrowPeriod = borrow.getBorrowPeriod();
            // 计算本金出借预期收益
            switch (borrowStyle) {
                case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money),
                            borrowApr.divide(new BigDecimal("100")), borrowPeriod)
                            .setScale(2, BigDecimal.ROUND_DOWN);
                    vo.setInterest(CommonUtils.formatAmount(version, earnings));
                    break;
                case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“: 预期收益=出借金额*年化收益÷360*天数；
                    earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(money),
                            borrowApr.divide(new BigDecimal("100")), borrowPeriod)
                            .setScale(2, BigDecimal.ROUND_DOWN);
                    vo.setInterest(CommonUtils.formatAmount(version, earnings));
                    break;
                case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(money),
                            borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod)
                            .setScale(2, BigDecimal.ROUND_DOWN);
                    vo.setInterest(CommonUtils.formatAmount(version, earnings));
                    break;
                case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(money),
                            borrowApr.divide(new BigDecimal("100")), borrowPeriod)
                            .setScale(2, BigDecimal.ROUND_DOWN);
                    vo.setInterest(CommonUtils.formatAmount(version, earnings));
                    break;
                case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(money),
                            borrowApr.divide(new BigDecimal("100")), borrowPeriod)
                            .setScale(2, BigDecimal.ROUND_DOWN);
                    vo.setInterest(CommonUtils.formatAmount(version, earnings));
                    break;
                default:
                    vo.setInterest("");
                    break;
            }
            logger.info("散标本金预期收益:  {}", earnings);
        }

        // 产品加息收益计算
        if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
            BigDecimal incEarnings = tenderService.increaseCalculate(borrow.getBorrowPeriod(), borrow.getBorrowStyle(), money, borrow.getBorrowExtraYield());
            earnings = earnings.add(incEarnings);
            logger.info("散标本金+产品加息预期收益:  {}", earnings);
        }

        if (Strings.isNullOrEmpty(couponId)) {
            //未选择优惠券
            vo.setDesc("年化利率: " + borrow.getBorrowApr() + "%      预期收益: " + CommonUtils.formatAmount(version, earnings) + "元");
            vo.setProspectiveEarnings(CommonUtils.formatAmount(version, earnings) + "元");
        } else {
            //选择优惠券计算优惠券收益
            BigDecimal couponInterest = BigDecimal.ZERO;
            List<CouponBean> lstAvailableCoupon = (List<CouponBean>) mapUserCouponInfo.get("availableCouponList");
            if (isCouponAvailable(couponId, lstAvailableCoupon)) {
                UserCouponConfigCustomize couponConfig = wxCouponService.getCouponById(couponId);
                Preconditions.checkArgument(couponConfig != null);
                couponInterest = calculateCouponTenderInterest(couponConfig, money, borrow);
            } else {
                logger.warn("非法的优惠券【{}】", couponId);
            }
            vo.setDesc("年化利率: " + borrow.getBorrowApr() + "%      预期收益: " + CommonUtils.formatAmount(version, earnings.add(couponInterest)) + "元");
            vo.setProspectiveEarnings(CommonUtils.formatAmount(version, earnings.add(couponInterest)) + "元");

        }

        vo.setInitMoney(borrow.getTenderAccountMin() + "");
        vo.setIncreaseMoney(String.valueOf(borrow.getBorrowIncreaseMoney()));
        vo.setInvestmentDescription(borrow.getTenderAccountMin() + "元起投," + borrow.getBorrowIncreaseMoney() + "元递增");
        // 可用余额的递增部分
        Account account = wxBorrowService.getAccount(userId);
        BigDecimal balance = account.getBankBalance();
        vo.setUserBalance(CommonUtils.formatAmount(version,balance));
        BigDecimal tmpmoney = balance.subtract(new BigDecimal(borrow.getTenderAccountMin())).divide(new BigDecimal(borrow.getBorrowIncreaseMoney()), 0, BigDecimal.ROUND_DOWN)
                .multiply(new BigDecimal(borrow.getBorrowIncreaseMoney())).add(new BigDecimal(borrow.getTenderAccountMin()));
        if (balance.subtract(new BigDecimal(borrow.getTenderAccountMin())).compareTo(new BigDecimal("0")) < 0) {
            // 可用余额<起投金额 时 investAllMoney 传 -1
            // 全投金额
            vo.setInvestAllMoney("-1");
        } else {
            String borrowAccountWaitStr = vo.getBorrowAccountWait().replace(",", "");
            if (new BigDecimal(borrow.getTenderAccountMax()).compareTo(new BigDecimal(borrowAccountWaitStr)) < 0) {
                vo.setInvestAllMoney(borrow.getTenderAccountMax() + "");
            } else if (tmpmoney.compareTo(new BigDecimal(borrowAccountWaitStr)) < 0) {
                // 全投金额
                vo.setInvestAllMoney(tmpmoney + "");
            } else {
                // 全投金额
                vo.setInvestAllMoney(vo.getBorrowAccountWait() + "");
            }
        }
        vo.setBorrowAccountWait(borrow.getBorrowAccountWait().intValue()+"");

        // add by liuyang 神策数据统计 20180823 start
        if (borrow.getProjectType() == 4 ){
            vo.setProjectTag("新手专享");
        }else{
            vo.setProjectTag("普通标");
        }
        // 项目名称
        vo.setProjectName(borrow.getProjectName());
        // 项目期限
        vo.setProjectDuration(borrow.getBorrowPeriod());
        // 折价率
        vo.setDiscountApr(BigDecimal.ZERO);
        // 项目编号
        vo.setProjectId(borrowNid);
        //
        if("endday".equals(borrow.getBorrowStyle())) {
            vo.setDurationUnit("天");
        }else{
            vo.setDurationUnit("月");
        }
        // 历史年回报率
        vo.setProjectApr(borrow.getBorrowApr());

        // 获取标的还款方式
        BorrowStyle projectBorrowStyle = this.wxBorrowTenderService.getProjectBorrowStyle(borrow.getBorrowStyle());
        if (projectBorrowStyle != null) {
            vo.setProjectRepaymentType(org.apache.commons.lang3.StringUtils.isBlank(projectBorrowStyle.getName()) ? "" : projectBorrowStyle.getName());
        } else {
            vo.setProjectRepaymentType("");
        }
        // add by liuyang 神策数据统计 20180823 end
        resultBean.setObject(vo);
        return resultBean;
    }

    /**
     * 判断优惠券是否可用
     *
     * @param userCouponId
     * @return
     */
    private boolean isCouponAvailable(String userCouponId, List<CouponBean> lstAvailableCoupon) {
        if (!CollectionUtils.isEmpty(lstAvailableCoupon)) {
            for (CouponBean couponBean : lstAvailableCoupon) {
                if (userCouponId.equals(couponBean.getUserCouponId())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 计算优惠券预期收益
     *
     * @param money  出借本金
     * @param borrow
     * @return
     */
    private BigDecimal calculateCouponTenderInterest(UserCouponConfigCustomize couponConfig, String money, Borrow borrow) {
        //计算优惠券预期收益
        BigDecimal couponInterest = BigDecimal.ZERO;
        BigDecimal borrowApr = borrow.getBorrowApr();
        String borrowStyle = borrow.getBorrowStyle();

        if (couponConfig != null) {
            Integer couponType = couponConfig.getCouponType();
            BigDecimal couponQuota = couponConfig.getCouponQuota();
            Integer couponProfitTime = couponConfig.getCouponProfitTime();

            if (couponType == 1) {
                couponInterest = getInterestDj(couponQuota, couponProfitTime, borrowApr);
            } else {
                couponInterest = getInterest(borrowStyle, couponType, borrowApr, couponQuota, money, borrow.getBorrowPeriod());
            }
        }
        logger.info("优惠券的收益: {} ", couponInterest);
        return couponInterest;
    }

    /**
     * 体验金收益计算
     *
     * @param couponQuota
     * @param couponProfitTime
     * @param borrowApr
     * @return
     */
    private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr) {
        return couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP)).divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(couponProfitTime)).setScale(2, BigDecimal.ROUND_DOWN);
    }


    private BigDecimal getInterest(String borrowStyle, Integer couponType, BigDecimal borrowApr, BigDecimal couponQuota, String money, Integer borrowPeriod) {
        BigDecimal earnings = new BigDecimal("0");

        // 出借金额
        BigDecimal accountDecimal = null;
        if (couponType == 1) {
            // 体验金 出借资金=体验金面值
            accountDecimal = couponQuota;
        } else if (couponType == 2) {
            // 加息券 出借资金=真实出借资金
            accountDecimal = new BigDecimal(money);
            borrowApr = couponQuota;
        } else if (couponType == 3) {
            // 代金券 出借资金=体验金面值
            accountDecimal = couponQuota;
        }
        switch (borrowStyle) {
            case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“: 预期收益=出借金额*年化收益÷12*月数；
                // 计算预期收益
                earnings = DuePrincipalAndInterestUtils.getMonthInterest(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                break;
            case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“: 预期收益=出借金额*年化收益÷360*天数；
                // 计算预期收益
                earnings = DuePrincipalAndInterestUtils.getDayInterest(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                break;
            case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“: 预期收益=出借金额*年化收益÷12*月数；
                // 计算预期收益
                earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                break;
            case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“: 预期收益=出借金额*年化收益÷12*月数；
                // 计算预期收益
                earnings = AverageCapitalPlusInterestUtils.getInterestCount(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                break;
            case CalculatesUtil.STYLE_PRINCIPAL: //还款方式为“等额本金” 预期收益=出借金额*年化收益÷12*月数；
                // 计算预期收益
                earnings = AverageCapitalUtils.getInterestCount(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                break;
            default:
                break;
        }
        if (couponType == 3) {
            earnings = earnings.add(couponQuota);
        }
        return earnings;
    }


    /**
     * 获取出借url
     *
     * @param vo
     * @return
     * @author pcc
     */
    @SignValidate
    @ResponseBody
    @RequestMapping(value = WxBorrowDefine.TENDER_URL_ACTION, produces = "application/json; charset=utf-8")
    public BaseResultBean getTenderUrl(@ModelAttribute WxTenderVo vo, HttpServletRequest request) {
        WxTenderResultBean info = new WxTenderResultBean();
        String couponGrantId = vo.getCouponGrantId();
        if (vo.getAccount() == null || "".equals(vo.getAccount())) {
            vo.setAccount("0");
        }
        if ((Validator.isNull(vo.getAccount()) && StringUtils.isEmpty(couponGrantId)) || Validator.isNull(vo.getBorrowNid())) {
            info.setEnum(ResultEnum.PARAM);
        } else {// 拼接充值地址并返回

            // 当前登录用户id
            Integer userId = requestUtil.getRequestUserId(request);
            // 当前登录用户
            Users user = this.requestUtil.getUsers(userId);
            
			// 缴费授权状态
            info.setPaymentAuthStatus(user.getPaymentAuthStatus());
            
            // 校验相应的出借参数
            JSONObject result = null;
            int couponOldTime = Integer.MIN_VALUE;
            CouponConfigCustomizeV2 cuc = null;
            logger.info("couponGrantId is:{}, userId is:{}", couponGrantId, userId);
            if (StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
                cuc = wxBorrowService.getCouponUser(couponGrantId, userId);
                // 排他check用
                couponOldTime = cuc.getUserUpdateTime();
            }

            result = wxBorrowService.checkParam(vo.getBorrowNid(), vo.getAccount(), String.valueOf(userId), cuc);
            logger.info("===================getTenderURL 返回信息为: " + result);
            if (!result.get(CustomConstants.APP_STATUS).equals("1") && StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
                // 体验金出借（无本金）
                // 根据项目编号获取相应的项目
                Borrow borrow = wxBorrowService.getBorrowByNid(vo.getBorrowNid());
                result = wxBorrowService.checkParamForCoupon(borrow, vo, String.valueOf(userId), cuc, couponGrantId);
            }
            // modify by zhangjp 优惠券相关 end

            if (result == null || result.get(CustomConstants.APP_STATUS).equals("1")) {
            	ResultEnum e = ResultEnum.FAIL;
            	e.setStatusDesc(result.getString(CustomConstants.APP_STATUS_DESC));
                info.setEnum(e);
            } else {
                /**************************** 风险测评强制测评标示 开始 pcc ***********************************/
                // 新加参数userEvaluationResultFlag 状态码 0:未测评,1:已测评
                if (0 == user.getIsEvaluationFlag()) {
                    info.setEnum(ResultEnum.USER_ERROR_213);
                    return info;
                } else {
    				if(user.getIsEvaluationFlag()==1 && null != user.getEvaluationExpiredTime()){
    					//测评到期日
    					Long lCreate = user.getEvaluationExpiredTime().getTime();
    					//当前日期
    					Long lNow = System.currentTimeMillis();
    					if (lCreate <= lNow) {
    						//已过期需要重新评测
    	                    info.setEnum(ResultEnum.USER_ERROR_216);
    	                    return info;
    					}
    				} else {
						//未获取到测评信息
	                    info.setEnum(ResultEnum.USER_ERROR_213);
	                    return info;
    				}
                }
                /**************************** 风险测评强制测评标示 结束 pcc ***********************************/
                //用户测评
                //从user中获取客户类型，ht_user_evalation_result（用户测评总结表）
                UserEvalationResultCustomize userEvalationResultCustomize = evaluationService.selectUserEvalationResultByUserId(userId);
                if(userEvalationResultCustomize != null){
                    EvaluationConfig evalConfig = new EvaluationConfig();
                    //1.散标／债转出借者测评类型校验
                    String debtEvaluationTypeCheck = "0";
                    //2.散标／债转单笔投资金额校验
                    String deptEvaluationMoneyCheck = "0";
                    //3.散标／债转待收本金校验
                    String deptCollectionEvaluationCheck = "0";
                    //获取开关信息
                    List<EvaluationConfig> evalConfigList = evaluationService.selectEvaluationConfig(evalConfig);
                    if(evalConfigList != null && evalConfigList.size() > 0){
                        evalConfig = evalConfigList.get(0);
                        //1.散标／债转出借者测评类型校验
                        debtEvaluationTypeCheck = evalConfig.getDebtEvaluationTypeCheck() == null ? "0" : String.valueOf(evalConfig.getDebtEvaluationTypeCheck());
                        //2.散标／债转单笔投资金额校验
                        deptEvaluationMoneyCheck = evalConfig.getDeptEvaluationMoneyCheck() == null ? "0" : String.valueOf(evalConfig.getDeptEvaluationMoneyCheck());
                        //3.散标／债转待收本金校验
                        deptCollectionEvaluationCheck = evalConfig.getDeptCollectionEvaluationCheck() == null ? "0" : String.valueOf(evalConfig.getDeptCollectionEvaluationCheck());
                        //7.投标时校验（二期）(预留二期开发)
                    }
                    //初始化金额返回值
                    String revaluation_money,revaluation_money_principal;
                    //根据类型从redis或数据库中获取测评类型和上限金额
                    String eval_type = userEvalationResultCustomize.getType();
                    switch (eval_type){
                        case "保守型":
                            //从redis获取金额（单笔）
                            revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE);
                            //如果reids不存在或者为零尝试获取数据库（单笔）
                            if("0".equals(revaluation_money)){
                                revaluation_money = evalConfig.getConservativeEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationSingleMoney());
                            }
                            //从redis获取金额（代收本金）
                            revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL);
                            //如果reids不存在或者为零尝试获取数据库（代收本金）
                            if("0".equals(revaluation_money_principal)){
                                revaluation_money_principal = evalConfig.getConservativeEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationPrincipalMoney());
                            }
                            break;
                        case "稳健型":
                            //从redis获取金额（单笔）
                            revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS);
                            //如果reids不存在或者为零尝试获取数据库（单笔）
                            if("0".equals(revaluation_money)){
                                revaluation_money = evalConfig.getSteadyEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationSingleMoney());
                            }
                            //从redis获取金额（代收本金）
                            revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL);
                            //如果reids不存在或者为零尝试获取数据库（代收本金）
                            if("0".equals(revaluation_money_principal)){
                                revaluation_money_principal = evalConfig.getSteadyEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationPrincipalMoney());
                            }
                            break;
                        case "成长型":
                            //从redis获取金额（单笔）
                            revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_GROWTH) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_GROWTH);
                            //如果reids不存在或者为零尝试获取数据库（单笔）
                            if("0".equals(revaluation_money)){
                                revaluation_money = evalConfig.getGrowupEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationSingleMoney());
                            }
                            //从redis获取金额（代收本金）
                            revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_GROWTH_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_GROWTH_PRINCIPAL);
                            //如果reids不存在或者为零尝试获取数据库（代收本金）
                            if("0".equals(revaluation_money_principal)){
                                revaluation_money_principal = evalConfig.getGrowupEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationPrincipalMoney());
                            }
                            break;
                        case "进取型":
                            //从redis获取金额（单笔）
                            revaluation_money = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE);
                            //如果reids不存在或者为零尝试获取数据库（单笔）
                            if("0".equals(revaluation_money)){
                                revaluation_money = evalConfig.getEnterprisingEvaluationSinglMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationSinglMoney());
                            }
                            //从redis获取金额（代收本金）
                            revaluation_money_principal = RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL) == null ? "0": RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL);
                            //如果reids不存在或者为零尝试获取数据库（代收本金）
                            if("0".equals(revaluation_money_principal)){
                                revaluation_money_principal = evalConfig.getEnterprisingEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationPrincipalMoney());
                            }
                            break;
                        default:
                            revaluation_money = null;
                            revaluation_money_principal = null;
                    }
                    //测评到期日
                    Long lCreate = user.getEvaluationExpiredTime().getTime();
                    //当前日期
                    Long lNow = System.currentTimeMillis();
                    if (lCreate <= lNow) {
                        //返回错误码
                        info.setEnum(ResultEnum.USER_ERROR_1012);
                        return info;
                    }
                    if(CustomConstants.EVALUATION_CHECK.equals(debtEvaluationTypeCheck)){
                        //计划类判断用户类型为稳健型以上才可以投资
                        // 根据项目编号获取相应的项目
                        Borrow borrow = wxBorrowService.getBorrowByNid(vo.getBorrowNid());
                        if(borrow != null){
                            if(!CommonUtils.checkStandardInvestment(eval_type,"BORROW_SB",borrow.getInvestLevel())){
                                //返回错误码
                                info.setEnum(ResultEnum.USER_ERROR_1014);
                                //返回类型（用户类型）
                                info.setEvalType(eval_type);
                                //返回类型（标的配置类型）
                                info.setEvalFlagType(borrow.getInvestLevel());
                                return info;
                            }
                        }
                    }
                    if(revaluation_money == null){
                        logger.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
                    }else {
                        if(CustomConstants.EVALUATION_CHECK.equals(deptEvaluationMoneyCheck)){
                            //金额对比判断（校验金额 大于 设置测评金额）
                            if (new BigDecimal(vo.getAccount()).compareTo(new BigDecimal(revaluation_money)) > 0) {
                                //返回错误码
                                info.setEnum(ResultEnum.USER_ERROR_1013);
                                //返回类型和限额
                                info.setEvalType(eval_type);
                                info.setRevaluationMoney(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money).intValue()));
                                return info;
                            }
                        }
                    }
                    if(revaluation_money_principal == null){
                        logger.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
                    }else {
                        //代收本金限额校验
                        if(CustomConstants.EVALUATION_CHECK.equals(deptCollectionEvaluationCheck)){
                            //获取冻结金额和代收本金
                            List<AccountDetailCustomize> accountInfos = evaluationService.queryAccountEvalDetail(userId);
                            if(accountInfos!= null || accountInfos.size() >0){
                                AccountDetailCustomize accountDetail =  accountInfos.get(0);
                                BigDecimal planFrost = accountDetail.getPlanFrost();// plan_frost 汇添金计划真实冻结金额
                                BigDecimal bankFrost = accountDetail.getBankFrost();// bank_frost 银行冻结金额
                                BigDecimal bankAwaitCapital = accountDetail.getBankAwaitCapital();// bank_await_capital 银行待收本金
                                BigDecimal account = BigDecimal.ZERO;
                                //加法运算
                                account = account.add(planFrost).add(bankFrost).add(bankAwaitCapital).add(new BigDecimal(vo.getAccount()));
                                //金额对比判断（校验金额 大于 设置测评金额）（代收本金）
                                if (account.compareTo(new BigDecimal(revaluation_money_principal)) > 0) {
                                    //返回错误码
                                    info.setEnum(ResultEnum.USER_ERROR_1015);
                                    //返回类型和限额
                                    info.setEvalType(eval_type);
                                    info.setRevaluationMoneyPrincipal(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money_principal).intValue()));
                                    return info;
                                }
                            }
                        }
                    }
                }else{
                    logger.info("=============该用户测评总结数据为空! userId="+userId);
                }
                // TODO: 2018/10/13  出借开始
                StringBuffer sb = new StringBuffer(WxBorrowDefine.HOST + WxBorrowDefine.REQUEST_HOME + WxBorrowDefine.REQUEST_MAPPING + WxBorrowDefine.INVEST_ACTION);
                sb.append(".page?sign=").append(vo.getSign()).append(CustomConstants.APP_PARM_AND).append("borrowNid=").append(vo.getBorrowNid()).append(CustomConstants.APP_PARM_AND)
                        .append("account=").append(vo.getAccount());
                if (StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
                    sb.append(CustomConstants.APP_PARM_AND).append("couponGrantId=").append(couponGrantId);
                }

                if (couponOldTime != Integer.MIN_VALUE) {
                    sb.append(CustomConstants.APP_PARM_AND).append("couponOldTime=").append(couponOldTime);
                }
                info.setTenderUrl(sb.toString());
                // 输出出借url
                info.setEnum(ResultEnum.SUCCESS);
            }
        }
        logger.info("==============getTenderUREL 返回结果 " + info);
        return info;
    }


    /**
     * 微信出借
     *
     * @param request
     * @param response
     * @return
     */
    @SignValidate
    @RequestMapping(value = WxBorrowDefine.INVEST_ACTION)
    public ModelAndView tender(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(WxBorrowDefine.THIS_CLASS, WxBorrowDefine.INVEST_ACTION);
        logger.info("开始调用出借");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView = new ModelAndView(WxBorrowDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        // 项目id
        String borrowNid = request.getParameter("borrowNid");
        // 出借金额
        String account = request.getParameter("account");
        // 出借金额
        String sign = request.getParameter("sign");
        String message = "出借失败";
        // 用户id
        Integer userId = requestUtil.getRequestUserId(request);
        String couponGrantId = request.getParameter("couponGrantId");
        logger.info("couponGrantId is:{}, userId is:{}", couponGrantId, userId);
        // 优惠券用户id （coupon_user的id）
        // 更新时间 排他check用
        int couponOldTime = StringUtils.isEmpty(request.getParameter("couponOldTime")) ? Integer.MIN_VALUE : Integer.valueOf(request.getParameter("couponOldTime"));

        if (userId == null) {
            message = "用户未登录,请登录！";
            // 回调url（h5错误页面）
            modelAndView = new ModelAndView(WxBorrowDefine.JUMP_HTML);
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "用户未登录,请登录！");
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", borrowNid));
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        } else {

            Borrow borrow = wxBorrowService.getBorrowByNid(borrowNid);
            // 是否月标(true:月标, false:天标)
            boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
                    || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle())|| CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle());

            String dayOrMonth ="";
            String lockPeriod = String.valueOf(borrow.getBorrowPeriod());
            if (isMonth) {//月标
                dayOrMonth = lockPeriod + "个月散标";
            } else {
                dayOrMonth = lockPeriod + "天散标";
            }
            Users users = wxBorrowService.getUsers(userId);
            UsersInfo usersInfo = wxBorrowService.getUsersInfoByUserId(userId);
            UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
            userOperationLogEntity.setOperationType(4);
            userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
            userOperationLogEntity.setPlatform(1);
            userOperationLogEntity.setRemark(dayOrMonth);
            userOperationLogEntity.setOperationTime(new Date());
            userOperationLogEntity.setUserName(users.getUsername());
            userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
            loginService.sendUserLogMQ(userOperationLogEntity);
            CouponConfigCustomizeV2 cuc = null;
            // 如果有优惠券出借
            if (StringUtils.isNotEmpty(couponGrantId) && new Integer(couponGrantId) > 0) {
                // 优惠券出借
                cuc = wxBorrowService.getCouponUser(couponGrantId, userId);
            }
            // 如果没有本金出借且有优惠券出借
            BigDecimal decimalAccount = StringUtils.isNotEmpty(account) ? new BigDecimal(account) : BigDecimal.ZERO;
            if (decimalAccount.compareTo(BigDecimal.ZERO) != 1 && cuc != null && (cuc.getCouponType() == 3 || cuc.getCouponType() == 1)) {
                this.couponTender(modelAndView, request, cuc, userId, couponOldTime);
                return modelAndView;
            }
            logger.info("tender出借用户id: " + userId + "~~~~~~~~~~标号: " + borrowNid);
            // 校验相应的出借参数
            JSONObject result = wxBorrowService.checkParam(borrowNid, account, String.valueOf(userId.intValue()), cuc);
            logger.info("=========cwyang 出借校验返回结果: " + result);
            if (result == null || result.get(CustomConstants.APP_STATUS).equals("1")) {
                // 回调url（h5错误页面）

                modelAndView = new ModelAndView(WxBorrowDefine.JUMP_HTML);
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, (result.get(CustomConstants.APP_STATUS) == null ? "" : result.get(CustomConstants.APP_STATUS).toString()));
                baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", borrowNid));
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
            Map<String, String> map = new HashMap<String, String>();
            // 出借用户汇付客户号
            String tenderUsrcustid = result.getString("tenderUsrcustid");
            String tenderUserName = result.getString("tenderUserName");
            // 生成订单id
            String orderId = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));
            // 写日志
            Boolean flag = wxBorrowService.updateBeforeChinaPnR(request, borrowNid, orderId, Integer.valueOf(userId), account, GetCilentIP.getIpAddr(request), couponGrantId, tenderUserName);
            // 日志插入成功后调用出借接口
            if (flag) {
                // 获取共同参数
                String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
                String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
                // 同步回调路径
                String returl = WxBorrowDefine.HOST + WxBorrowDefine.REQUEST_HOME + WxBorrowDefine.REQUEST_MAPPING + WxBorrowDefine.RETURL_SYN_ACTION + ".page?userId=" + userId  + "&couponGrantId="
                        + couponGrantId + "&couponUpdateTime=" + couponOldTime+"&sign="+sign+"&borrowNid="+borrowNid;
                // 异步回调路径
                String bgRetUrl = WxBorrowDefine.HOST + WxBorrowDefine.REQUEST_HOME + WxBorrowDefine.REQUEST_MAPPING + WxBorrowDefine.RETURL_ASY_ACTION + ".do?userId=" + userId + "&couponGrantId="
                        + couponGrantId + "&couponUpdateTime=" + couponOldTime;
                //忘记密码url
                String forgetPassworedUrl = CustomConstants.FORGET_PASSWORD_URL + "?sign=" + sign;

                BankCallBean bean = new BankCallBean();
                bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
                bean.setTxCode(BankCallConstant.TXCODE_BID_APPLY);// 交易代码
                bean.setInstCode(instCode);
                bean.setBankCode(bankCode);
                bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
                bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
                bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
                bean.setChannel(BankCallConstant.CHANNEL_WEI);// 交易渠道
                bean.setAccountId(tenderUsrcustid);// 电子账号
                bean.setOrderId(orderId);// 订单号
                bean.setTxAmount(CustomUtil.formatAmount(account));// 交易金额
                bean.setProductId(borrowNid);// 标的号
                bean.setFrzFlag(BankCallConstant.DEBT_FRZFLAG_UNFREEZE);// 是否冻结金额  modify by cwyang 2017-10-25 实时放款出借不冻结
                bean.setForgotPwdUrl(forgetPassworedUrl);
                bean.setSuccessfulUrl(returl+"&isSuccess=1");// 银行同步返回地址
                bean.setRetUrl(returl);// 银行同步返回地址
                bean.setNotifyUrl(bgRetUrl);// 银行异步返回地址
                bean.setLogOrderId(orderId);// 订单号
                bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单日期
                bean.setLogIp(GetCilentIP.getIpAddr(request));// 客户IP
                bean.setLogUserId(String.valueOf(userId));// 出借用户
                bean.setLogUserName(tenderUserName);// 出借用户名
                bean.setLogClient(1);
                bean.setLogBankDetailUrl(BankCallConstant.BANK_URL_MOBILE_BIDAPPLY);// 银行请求详细url
                try {
                    modelAndView = BankCallUtils.callApi(bean);
                    return modelAndView;
                } catch (Exception e) {
                    e.printStackTrace();
                    map.put("error", "1");
                    map.put("message", "出借失败");
                    String url = JSON.toJSONString(map);
                    try {
                        url = URLEncoder.encode(url, "UTF-8");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                    message = "江西银行接口调用失败";
                    LogUtil.errorLog(WxBorrowDefine.THIS_CLASS, message, e);

                    modelAndView = new ModelAndView(WxBorrowDefine.JUMP_HTML);
                    baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                    baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                    baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", borrowNid));
                    modelAndView.addObject("callBackForm", baseMapBean);
                    return modelAndView;
                }

            } else {
                message = "写入出借日志失败";
                LogUtil.errorLog(WxBorrowDefine.THIS_CLASS, message, null);

                modelAndView = new ModelAndView(WxBorrowDefine.JUMP_HTML);
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", borrowNid));
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
        }
    }

    /**
     * 散标出借同步回调
     *
     * @param request
     * @param bean
     * @return
     */
    @RequestMapping(WxBorrowDefine.RETURL_SYN_ACTION)
    public ModelAndView tenderRetUrlSyn(HttpServletRequest request, @ModelAttribute BankCallBean bean) {

        ModelAndView modelAndView = new ModelAndView(WxBorrowDefine.JUMP_HTML);
        BaseMapBean baseMapBean = new BaseMapBean();
        String logOrderId = StringUtils.isBlank(bean.getLogOrderId()) ? "" : bean.getLogOrderId();// 订单号
        int userId = StringUtils.isBlank(bean.getLogUserId()) ? 0 : Integer.parseInt(bean.getLogUserId());// 用户Userid
        String ip = bean.getUserIP();// 用户操作ip
        String couponGrantId = request.getParameter("couponGrantId");// 优惠券用户id
        String couponOldTime = StringUtils.isNotEmpty(request.getParameter("couponOldTime")) ? request.getParameter("couponOldTime") : "0";
        String respCode = bean.getRetCode();// 出借结果返回码
        String platForm = "1";

        String accountId = StringUtils.isBlank(bean.getAccountId()) ? "" : bean.getAccountId();// 电子账号
        String isSuccess = request.getParameter("isSuccess");
        logger.info("用户:" + userId + "**isSuccess：" + isSuccess);
        String bNid = request.getParameter("borrowNid");
        logger.info("用户:" + userId + "**borrowNid：" + bNid);
        // 打印返回码
        logger.info("散标出借同步回调, 用户: {} , 出借接口结果码: {}", userId, respCode);
        logger.info("出借订单号: {}， 优惠券id: {}", logOrderId, couponGrantId);
        logger.info("电子账号: {}", accountId);
        String message = "出借失败！";// 错误信息

        
        if (StringUtils.isBlank(respCode)||!"1".equals(isSuccess)) {
            // 有出借记录，则返回出借成功
            if (!isTenderSuccess(userId, accountId, logOrderId)) {
                baseMapBean.setCallBackAction(CustomConstants.HOST
                        + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", bNid));
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            } else {
                respCode = BankCallConstant.RESPCODE_SUCCESS;
            }
        }

        if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)&&!"1".equals(isSuccess)) {

            // 返回码提示余额不足
            if (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(respCode)) {
                logger.info("用户:" + userId + "**出借接口调用失败，余额不足，错误码: " + respCode);
                message = "出借失败，可用余额不足！请联系客服.";
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", bean.getProductId()));
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            } else {
                message = bean.getRetMsg();
                logger.info("用户:" + userId + "**出借接口调用失败,系统订单号: " + logOrderId + "**接口返回错误码" + respCode);
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", bean.getProductId()));
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }
        }

        bean.convert();
        String borrowNid = bean.getProductId();// 借款Id
        logger.info("===== borrowid 为 " + borrowNid);
        String account = bean.getTxAmount();// 借款金额
        logger.info("===== account 为 " + account);
        String orderId = bean.getOrderId();// 订单id
        logger.info("===== orderId 为 " + orderId);
        logger.info("===== platFrom 为 " + platForm);
        if (StringUtils.isNotEmpty(platForm)) {
            bean.setLogClient(Integer.parseInt(platForm));// 
        }
        // 根据借款Id检索标的信息
        BorrowWithBLOBs borrow = this.wxBorrowTenderService.getBorrowByNid(borrowNid);

        logger.info("标号:" + borrowNid);
        if (StringUtils.isBlank(borrowNid)) {
            message = "回调时,borrowNid为空";
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", bean.getProductId()));
            modelAndView.addObject("callBackForm", baseMapBean);
            return modelAndView;
        }

        // 优惠券出借结果
        JSONObject couponTender = null;
        // 出借状态
        String status = BankCallConstant.STATUS_FAIL;

        //出借结果
        JSONObject tenderResult = null;

        try {
            // 进行出借 tendertmp锁住
            tenderResult = this.wxBorrowTenderService.userTender(borrow, bean);
            logger.info("出借结果: {}", tenderResult == null ? "" : tenderResult.toJSONString());
            // 出借成功
            if (tenderResult.getString("status").equals("1")) {
                logger.info("用户:" + userId + "***出借成功: " + account);
                message = "恭喜您出借成功!";
                status = BankCallConstant.STATUS_SUCCESS;
                if (StringUtils.isNotEmpty(couponGrantId)) {
                    // 优惠券出借校验
                    try {
                        // 优惠券出借校验
                        JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account,
                                CustomConstants.CLIENT_WECHAT, couponGrantId);
                        logger.info("优惠券出借校验结果: {}", couponCheckResult == null ? "" : couponCheckResult.toJSONString());

                        if (couponCheckResult != null) {
                            int couponStatus = couponCheckResult.getIntValue("status");
                            if (couponStatus == 0) {
                                // 优惠券出借
                                // couponTender = CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid,
                                // account, CustomConstants.CLIENT_PC, couponGrantId, orderId, ip,
                                // couponOldTime);
                                // update by pcc 放入汇直投优惠券使用mq队列 start

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("mqMsgId", GetCode.getRandomCode(10));
                                // 真实出借金额
                                params.put("money", account);
                                // 借款项目编号
                                params.put("borrowNid", borrowNid);
                                // 平台
                                params.put("platform", platForm);
                                // 优惠券id
                                params.put("couponGrantId", couponGrantId);
                                // ip
                                params.put("ip", ip);
                                // 真实出借订单号
                                params.put("ordId", orderId);
                                // 优惠券修改时间
                                params.put("couponOldTime", couponOldTime + "");
                                // 用户编号
                                params.put("userId", userId + "");
                                logger.info("优惠券出借任务push到redis queue, param is :{}", JSONObject.toJSONString(params));
                                rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON,
                                        RabbitMQConstants.ROUTINGKEY_HZT_COUPON_TENDER,
                                        JSONObject.toJSONString(params));
                                // update by pcc 放入汇直投优惠券使用mq队列 end
                            }
                        }

                    } catch (Exception e) {
                        logger.error("优惠券出借失败...", e);
                    }
                }
            }
            // 出借失败 回滚redis
            else {
                // 更新tendertmp
                boolean updateFlag = wxBorrowTenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
                // 更新失败，出借失败
                if (updateFlag) {
                    if (tenderResult.getString("status").equals("-1")) {// 同步/异步 优先执行完毕
                        // add by cwyang 增加redis防重校验 2017-08-02
                        boolean checkTender = RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);
                        if (!checkTender) {// 同步/异步 优先执行完毕
                            message = "恭喜您出借成功!";
                            status = BankCallConstant.STATUS_SUCCESS;
                        } else {
                            message = "投标失败,请联系客服人员!";
                        }
                    } else {
                        // 出借失败,出借撤销
                        try {
                            boolean flag = wxBorrowTenderService.bidCancel(userId, borrowNid, orderId, account);
                            if (!flag) {
                                message = "投标失败,请联系客服人员!";
                            }
                        } catch (Exception e) {
                            logger.error("投标申请撤销异常...", e);
                            message = "投标申请撤销失败,请联系客服人员!";
                        }
                    }
                } else {
                    message = "恭喜您出借成功!";
                    status = BankCallConstant.STATUS_SUCCESS;
                }
            }
        } catch (Exception e) {
            logger.error("出借异常...", e);
            // 更新tendertmp
            boolean updateFlag = wxBorrowTenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
            // 更新成功，出借失败
            if (updateFlag) {
                // 出借失败,出借撤销
                try {
                    boolean flag = wxBorrowTenderService.bidCancel(userId, borrowNid, orderId, account);
                    if (!flag) {
                        message = "投标失败,请联系客服人员!";
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    message = "投标申请撤销失败,请联系客服人员!";
                }
            } else {
                message = "恭喜您出借成功!";
                status = BankCallConstant.STATUS_SUCCESS;
            }
        }

        if (status.equals(BankCallConstant.STATUS_FAIL)) {
            // 返回标的暂无可投信息
            String borrowAccountWait = wxBorrowService.getBorrowAccountWait(borrowNid);
            if (borrowAccountWait.equals("0.00")) {
                message = "该标的可投金额不足, 请选择其他标的";
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
                baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", bean.getProductId()));
                modelAndView.addObject("callBackForm", baseMapBean);
                return modelAndView;
            }

            modelAndView.addObject("investDesc", message);
            modelAndView.addObject("projectType", borrow.getProjectType());
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", bean.getProductId()));
            modelAndView.addObject("callBackForm", baseMapBean);
        } else {
            logger.info("出借成功逻辑分支开始执行....");
            DecimalFormat df = CustomConstants.DF_FOR_VIEW;

            // 判断一下优惠券是否已被使用(由于异步调用可能在同步调用之前执行,导致无法获得优惠券的使用情况,所以这里需要重新获取一下)并且同步调用未进行优惠券出借
            if (StringUtils.isNotEmpty(couponGrantId)) {
                int index = 0;
                do {
                    try {
                        Thread.sleep(500);
                        logger.info("==================cwyang 异步调用优先执行,重新获取优惠券信息.============");
                        couponTender = wxBorrowTenderService.getCouponIsUsed(orderId, couponGrantId, userId);
                        if (!CustomConstants.RESULT_SUCCESS.equals(couponTender.getString("isSuccess"))) {
                            logger.error("====================cwyang 获取优惠券信息失败!==================");
                            couponTender = null;
                            index++;
                        }
                    } catch (Exception e) {
                        logger.error("获取优惠券信息异常!", e);
                    }
                } while (couponTender == null && index < 3);
            }


            logger.info("开始获取结果页展示数据....");
            String interest = null;
            String borrowStyle = borrow.getBorrowStyle();// 项目还款方式
            Integer borrowPeriod = borrow.getBorrowPeriod();// 周期
            BigDecimal borrowApr = borrow.getBorrowApr();// 項目预期年化收益率

            BigDecimal earnings;
            // 计算预期收益
            switch (borrowStyle) {
                case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                    interest = df.format(earnings);
                    break;
                case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“: 预期收益=出借金额*年化收益÷360*天数；
                    earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                    interest = df.format(earnings);
                    break;
                case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2,
                            BigDecimal.ROUND_DOWN);
                    interest = df.format(earnings);
                    break;
                case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“: 预期收益=出借金额*年化收益÷12*月数；
                    earnings = AverageCapitalPlusInterestUtils.getInterestCount(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                    interest = df.format(earnings);
                    break;
                case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
                    earnings = AverageCapitalUtils.getInterestCount(new BigDecimal(account), borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
                    interest = df.format(earnings);
                    break;
                default:
                    break;
            }

            //预期收益
            BigDecimal income = BigDecimal.ZERO;
            if (StringUtils.isNotBlank(interest)) {
                // 本金的收益
                interest = interest.replaceAll(",", "");
                income = new BigDecimal(interest).add(income);
            }
            // 预期收益
            baseMapBean.set("income", income.toString());
            logger.info("本金收益: {}", income);

            if (Validator.isNotNull(couponTender)) {
                logger.info("优惠券出借结果: {}", couponTender.toJSONString());
                int couponStatus = couponTender.getIntValue("status");
                if (couponStatus == 0) {
                    // 优惠券面值
                    String couponQuota = couponTender.getString("couponQuota");
                    // 优惠券收益率
                    // 优惠券类别
                    Integer couponType = couponTender.getIntValue("couponTypeInt");
                    // 加息券收益
                    String couponInterest = couponTender.getString("couponInterest");
                    if (StringUtils.isNotEmpty(couponInterest)) {
                        // 代金券需要加上券的面值
                        if (couponType == 3) {
                            logger.info("代金券加上券的面值....");
                            income = income.add(new BigDecimal(couponQuota));
                        }
                        income = income.add(new BigDecimal(couponInterest));
                        // 预期收益
                        baseMapBean.set("income", income.toString());
                    }
                    if (StringUtils.isNotEmpty(couponQuota)) {
                        // 加息券增加%
                        if (couponType == 2) {
                            try {
                                couponQuota = URLEncoder.encode(couponQuota.concat("%"), "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                logger.error("URLEncoder编码错误....", e);
                            }
                            logger.info("加息券面额....{}", couponQuota);
                        }
                        // 优惠券面值
                        baseMapBean.set("couponValue", couponQuota);
                    }
                    if (couponType != Integer.MIN_VALUE) {
                        // 优惠券类别
                        baseMapBean.set("couponType", String.valueOf(couponType));
                    }
                }
            }

            // 产品加息预期收益
            if (Validator.isIncrease(borrow.getIncreaseInterestFlag(), borrow.getBorrowExtraYield())) {
                BigDecimal incEarnings = tenderService.increaseCalculate(borrowPeriod, borrowStyle, account, borrow.getBorrowExtraYield());
                income = income.add(incEarnings);
                baseMapBean.set("income", CommonUtils.formatAmount(income));
            }

            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, message);
            baseMapBean.set("amount", CommonUtils.formatAmount(account));
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_SUCCESS_PATH.replace("{borrowId}", bean.getProductId()));
            logger.info("出借结果信息展示: {}", JSONObject.toJSONString(baseMapBean));
            modelAndView.addObject("callBackForm", baseMapBean);

            //同步通知（风车理财，tender_from为wrb时成立，发mq）
            //出借成功，发mq
            if ("wrb".equals(tenderResult.getString("tenderFrom"))) {
                //同步回调通知
                logger.info("风车理财出借回调,订单Id :{}", orderId);
                this.notifyToWrb(userId, orderId);
            }
        }
        return modelAndView;

    }


    /**
     * 散标出借异步回调结果
     *
     * @param request
     * @param response
     * @param bean
     * @return
     */
    @ResponseBody
    @RequestMapping(WxBorrowDefine.RETURL_ASY_ACTION)
    public BankCallResult tenderRetUrlAsy(HttpServletRequest request, HttpServletResponse response, @ModelAttribute BankCallBean bean) {

        logger.info("开始调用出借异步方法");
        int userId = StringUtils.isBlank(bean.getLogUserId()) ? 0 : Integer.parseInt(bean.getLogUserId());// 用户Userid
        String ip = bean.getUserIP();// 用户操作ip
        String couponGrantId = request.getParameter("couponGrantId");// 优惠券用户id
        String couponOldTime = StringUtils.isNotEmpty(request.getParameter("couponOldTime")) ? request.getParameter("couponOldTime") : "0";
        String respCode = bean.getRetCode();// 出借结果返回码
        BankCallResult result = new BankCallResult();
        String message = "出借失败！";// 错误信息
        if (StringUtils.isBlank(respCode)) {
            result.setMessage(message);
            return result;
        }
        if (!BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
            // 返回码提示余额不足，不结冻
            if (BankCallConstant.RETCODE_BIDAPPLY_YUE_FAIL.equals(respCode)) {
                logger.info("PC用户:" + userId + "**出借接口调用失败，余额不足，错误码: " + respCode);
                message = "出借失败，可用余额不足！请联系客服.";
                result.setMessage(message);
                return result;
            } else {
                message = bean.getRetMsg();
                result.setMessage(message);
                return result;
            }
        }
        bean.convert();
        String borrowId = bean.getProductId();// 借款Id
        String account = bean.getTxAmount();// 借款金额
        String orderId = bean.getOrderId();// 订单id
        // 将出借平台放到bean中传输
        bean.setLogClient(Integer.parseInt(CustomConstants.CLIENT_WECHAT));// add by cwyang
        // 根据借款Id检索标的信息
        BorrowWithBLOBs borrow = this.wxBorrowTenderService.getBorrowByNid(borrowId);
        String borrowNid = borrowId == null ? "" : borrow.getBorrowNid();
        logger.info("出借异步回调: " + bean.getAllParams().toString());
        logger.info("标号:" + borrowNid);
        if (StringUtils.isBlank(borrowNid)) {
            message = "回调时,borrowNid为空";
            result.setMessage(message);
            return result;
        }
        try {
            // 进行出借, tendertmp锁住
            JSONObject tenderResult = this.wxBorrowTenderService.userTender(borrow, bean);
            // 出借成功
            if (tenderResult.getString("status").equals("1")) {
                logger.info("微信用户:" + userId + "***出借成功: " + account);
                message = "恭喜您出借成功!";
                logger.info("异步调用优惠劵ID:" + couponGrantId);
                if (StringUtils.isNotEmpty(couponGrantId)) {
                    // 优惠券出借校验
                    try {
                        // 优惠券出借校验
                        logger.info("异步调用优惠劵ID:" + couponGrantId);
                        JSONObject couponCheckResult = CommonSoaUtils.CheckCoupon(userId + "", borrowNid, account, CustomConstants.CLIENT_WECHAT, couponGrantId);
                        int couponStatus = couponCheckResult.getIntValue("status");
                        logger.info("优惠券出借校验结果: {}" + couponCheckResult == null ? "" : couponCheckResult.toJSONString());
                        if (couponStatus == 0) {
                            // 优惠券出借
                            //CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, CustomConstants.CLIENT_PC, couponGrantId, orderId, ip, couponOldTime);
                            // update by pcc 放入汇直投优惠券使用mq队列 start
                            LogUtil.infoLog(WxBorrowDefine.THIS_CLASS, "updateCouponTender", "同步方法放入汇直投优惠券使用mq队列: ");

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("mqMsgId", GetCode.getRandomCode(10));
                            // 真实出借金额
                            params.put("money", account);
                            // 借款项目编号
                            params.put("borrowNid", borrowNid);
                            // 平台
                            params.put("platform", CustomConstants.CLIENT_WECHAT);
                            // 优惠券id
                            params.put("couponGrantId", couponGrantId);
                            // ip
                            params.put("ip", ip);
                            // 真实出借订单号
                            params.put("ordId", orderId);
                            // 优惠券修改时间
                            params.put("couponOldTime", couponOldTime + "");
                            // 用户编号
                            params.put("userId", userId + "");
                            logger.info("优惠券出借任务push到redis queue, param is :{}", JSONObject.toJSONString(params));
                            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_HZT_COUPON_TENDER, JSONObject.toJSONString(params));
                            // update by pcc 放入汇直投优惠券使用mq队列 end
                        }
                    } catch (Exception e) {
                        LogUtil.infoLog(WxBorrowDefine.THIS_CLASS, "tenderRetUrl", "优惠券出借失败");
                    }
                }
            }
            // 出借失败 回滚redis
            else {

                // 更新tendertmp
                boolean updateFlag = wxBorrowTenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
                // 更新失败，出借失败
                if (updateFlag) {
                    if (tenderResult.getString("status").equals("-1")) {// 同步/异步 优先执行完毕
                        message = "恭喜您出借成功!";
                        //add by cwyang 增加redis防重校验 2017-08-02
                        boolean checkTender = RedisUtils.tranactionSet("tendersuccess_orderid" + orderId, 20);
                        if (!checkTender) {//同步/异步 优先执行成功!
                            boolean updateTenderFlag = this.wxBorrowTenderService.updateTender(userId, borrowNid, orderId, bean);
                            if (!updateTenderFlag) {
                                message = "投标出現错误,请联系客服人员!";
                            }
                        } else {
                            message = "投标失败,请联系客服人员!";
                        }
                        //end
                    } else {
                        // 出借失败,出借撤销
                        try {
                            boolean flag = wxBorrowTenderService.bidCancel(userId, borrowId, orderId, account);
                            if (!flag) {
                                message = "投标失败,请联系客服人员!";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            message = "投标申请撤销失败,请联系客服人员!";
                        }
                    }
                } else {
                    message = "恭喜您出借成功!";
                    boolean updateTenderFlag = this.wxBorrowTenderService.updateTender(userId, borrowNid, orderId, bean);
                    if (!updateTenderFlag) {
                        message = "投标出現错误,请联系客服人员!";
                    }
                }
            }
        } catch (Exception e) {
            // 更新tendertmp
            boolean updateFlag = wxBorrowTenderService.updateBorrowTenderTmp(userId, borrowNid, orderId);
            // 更新失败，出借失败
            if (updateFlag) {
                // 出借失败,出借撤销
                try {
                    boolean flag = wxBorrowTenderService.bidCancel(userId, borrowId, orderId, account);
                    if (!flag) {
                        message = "投标失败,请联系客服人员!";
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    message = "投标申请撤销失败,请联系客服人员!";
                }
            } else {
                message = "恭喜您出借成功!";
                boolean updateTenderFlag = this.wxBorrowTenderService.updateTender(userId, borrowNid, orderId, bean);
                if (!updateTenderFlag) {
                    message = "投标出現错误,请联系客服人员!";
                }
            }
        }
        if(message.equals("恭喜您出借成功!")) {
			CommonSoaUtils.listedTwoInvestment(userId, new BigDecimal(account));
			CommonSoaUtils.listInvestment(userId, new BigDecimal(account), borrow.getBorrowStyle(), borrow.getBorrowPeriod());
        }
        result.setStatus(true);
        return result;
    }

    /**
     * 优惠券出借
     *
     * @param modelAndView
     * @param request
     * @param cuc
     * @param userId
     * @return
     */
    private void couponTender(ModelAndView modelAndView, HttpServletRequest request, CouponConfigCustomizeV2 cuc, int userId, int couponOldTime) {
        // 金额
        String ip = GetCilentIP.getIpAddr(request);
        String account = request.getParameter("account");
        String couponGrantId = request.getParameter("couponGrantId");
        String borrowNid = request.getParameter("borrowNid");
        if (account == null || "".equals(account)) {
            account = "0";
        }
        BaseMapBean baseMapBean = new BaseMapBean();

        // 根据项目编号获取相应的项目
        Borrow borrow = wxBorrowService.getBorrowByNid(borrowNid);
        // 优惠券出借校验
        Map<String, String> validateMap = this.validateCoupon(account, couponGrantId, borrow, "1", cuc, String.valueOf(userId));
        logger.info("=====================cwyang优惠券出借校验结果:" + validateMap);
        if (validateMap.containsKey(CustomConstants.APP_STATUS_DESC)) {

            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, validateMap.get(CustomConstants.APP_STATUS_DESC));
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", borrowNid));
            modelAndView.addObject("callBackForm", baseMapBean);
        }
        JSONObject jsonObject = CommonSoaUtils.CouponInvestForPC(userId + "", borrowNid, account, CustomConstants.CLIENT_WECHAT, couponGrantId, "", ip, couponOldTime + "");
        logger.info("仅有优惠券出借返回结果: {}", jsonObject);
        if (jsonObject.getIntValue("status") == 0) {
            // 优惠券类别
            String couponType = jsonObject.getString("couponTypeInt") == null ? "" : jsonObject.getString("couponTypeInt").toString();
            baseMapBean.set("couponType", couponType);
            // 优惠券额度
            String couponQuota = jsonObject.getString("couponQuota") == null ? ""
                    : jsonObject.getString("couponQuota").toString();
            if ("2".equals(couponType)) {
                try {
                    couponQuota = URLEncoder.encode(couponQuota.concat("%"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    logger.error("URLEncoder编码错误....", e);
                }
            }
            baseMapBean.set("couponValue", couponQuota);

            String income = jsonObject.getString("couponInterest") == null ? ""
                    : jsonObject.getString("couponInterest").toString();
            if ("3".equals(couponType)) {
                logger.info("代金券计算预期收益需要加上券的面值...");
                income = String
                        .valueOf(new BigDecimal(income).add(new BigDecimal(jsonObject.getString("couponQuota") == null
                                ? "" : jsonObject.getString("couponQuota").toString())));
            }
            // 优惠券收益
            baseMapBean.set("income", income);

            // 金额
            baseMapBean.set("amount", account);
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, validateMap.get(CustomConstants.APP_STATUS_DESC));
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_SUCCESS_PATH.replace("{borrowId}", borrowNid));

            //logger.info("出借成功跳转baseMapBean is :{}", JSONObject.toJSONString(baseMapBean));
            modelAndView.addObject("callBackForm", baseMapBean);
        } else {
            LogUtil.infoLog(WxBorrowDefine.THIS_CLASS, "updateCouponTender", "优惠券出借结束。。。。。。");

            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, jsonObject.getString("statusDesc"));
            baseMapBean.setCallBackAction(CustomConstants.HOST + WxBorrowDefine.JUMP_HTML_WECHAT_FAILED_PATH.replace("{borrowId}", borrowNid));
            modelAndView.addObject("callBackForm", baseMapBean);
        }

    }


    private boolean isTenderSuccess(int userId, String accountId, String orderId) {
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String channel = BankCallConstant.CHANNEL_APP;
        // 查询相应的债权状态
        try {
            String logOrderId = GetOrderIdUtils.getOrderId2(userId);
            String orderDate = GetOrderIdUtils.getOrderDate();
            String txDate = GetOrderIdUtils.getTxDate();
            String txTime = GetOrderIdUtils.getTxTime();
            String seqNo = GetOrderIdUtils.getSeqNo(6);
            // 调用投标申请查询接口
            BankCallBean bankCallBean = new BankCallBean();
            bankCallBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            bankCallBean.setTxCode(BankCallConstant.TXCODE_BID_APPLY_QUERY);// 消息类型(批量还款)
            bankCallBean.setInstCode(instCode);// 机构代码
            bankCallBean.setBankCode(bankCode);
            bankCallBean.setTxDate(txDate);
            bankCallBean.setTxTime(txTime);
            bankCallBean.setSeqNo(seqNo);
            bankCallBean.setChannel(channel);
            bankCallBean.setAccountId(accountId);
            bankCallBean.setOrgOrderId(orderId);
            bankCallBean.setLogUserId(String.valueOf(userId));
            bankCallBean.setLogOrderId(logOrderId);
            bankCallBean.setLogOrderDate(orderDate);
            bankCallBean.setLogRemark("查询债权状态请求");
            bankCallBean.setLogClient(0);
            BankCallBean statusResult = BankCallUtils.callApiBg(bankCallBean);
            if (Validator.isNotNull(statusResult)) {
                String retCode = StringUtils.isNotBlank(statusResult.getRetCode()) ? statusResult.getRetCode() : "";
                if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
                    if (StringUtils.isNotBlank(statusResult.getState()) && !"9".equals(statusResult.getState())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("查询债权异常...", e);
        }
        return false;
    }

    /**
     * 优惠券出借校验
     *
     * @param account
     * @return
     */
    private Map<String, String> validateCoupon(String account, String couponGrantId, Borrow borrow, String platform, CouponConfigCustomizeV2 couponConfig, String userId) {

        JSONObject jsonObject = CommonSoaUtils.CheckCoupon(userId, borrow.getBorrowNid(), account, platform, couponGrantId);
        int status = jsonObject.getIntValue("status");
        String statusDesc = jsonObject.getString("statusDesc");

        Map<String, String> paramMap = new HashMap<String, String>();
        if (status == 1) {
            paramMap.put(CustomConstants.APP_STATUS_DESC, statusDesc);
        }

        return paramMap;

    }

    /**
     * 风车理财出借同步回调通知
     */
    private void notifyToWrb(int userId, String nid) {
        JSONObject params = new JSONObject();
        params.put("userId", userId);
        params.put("nid", nid);
        params.put("returnType", "1");
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_WRB_CALLBACK_NOTIFY, JSONObject.toJSONString(params));
    }

}
