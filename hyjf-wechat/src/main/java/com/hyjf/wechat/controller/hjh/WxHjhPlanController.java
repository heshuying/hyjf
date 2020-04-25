/**
 * Description:汇计划服务器
 * Copyright: Copyright (HYJF Corporation) 2017
 * Company: HYJF Corporation
 *
 * @author: LIBIN
 * @version: 1.0
 */
package com.hyjf.wechat.controller.hjh;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;
import com.hyjf.wechat.service.borrow.WxBorrowService;
import com.hyjf.wechat.service.login.LoginService;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.wechat.BaseResultBeanFrontEnd;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseMapBean;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.base.SimpleResultBean;
import com.hyjf.wechat.controller.borrow.WxBorrowDefine;
import com.hyjf.wechat.controller.hjh.vo.HjhPlanAccedeResultBean;
import com.hyjf.wechat.controller.hjh.vo.HjhPlanBorrowResultBean;
import com.hyjf.wechat.controller.hjh.vo.HjhPlanResultBean;
import com.hyjf.wechat.controller.hjh.vo.HjhTenderResultBean;
import com.hyjf.wechat.controller.hjh.vo.WxHJHTenderVo;
import com.hyjf.bank.service.evalation.EvaluationService;
import com.hyjf.wechat.util.ResultEnum;

@RestController
@RequestMapping(value = WxHjhPlanDefine.REQUEST_MAPPING)
public class WxHjhPlanController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(WxHjhPlanController.class);

    /**
     * 加入条件
     */
    // mod by nxl 计划详情修改加入条件
//    private final String PLAN_ADD_CONDITION = "{0}元起，以{1}元的倍数递增";
    private final String PLAN_ADD_CONDITION = "加入金额{0}元起，且以{1}元的整数倍递增";

    /**
     * 计息时间
     */
    private final String PLAN_ON_ACCRUAL = "计划进入锁定期后开始计息";

    /**
     * 汇计划类型简称
     */
    private final String PLAN_TYPE_NAME = "HJH";

    @Autowired
    private WxHjhPlanService hjhPlanService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private EvaluationService evaluationService;
    /**
     * 查询汇计划详细信息,不需要登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = WxHjhPlanDefine.HJH_PLAN_DETAIL_ACTION)
    public BaseResultBean searchHjhPlanDetail(HttpServletRequest request, @PathVariable String planId) {

        SimpleResultBean<HjhPlanResultBean> result = new SimpleResultBean<>();

        LogUtil.startLog(WxHjhPlanController.class.getName(), WxHjhPlanDefine.HJH_PLAN_DETAIL_ACTION);

        logger.info("入参planId:{}", planId);

        HjhPlanResultBean hjhPlanResultBean = new HjhPlanResultBean();

        // 根据计划编号获取相应的计划详情信息
        DebtPlanDetailCustomize customize = hjhPlanService.selectDebtPlanDetail(planId);
        if (customize == null) {
            logger.error("传入计划id无对应计划,planNid is {}...", planId);
            result.setStatus("99");
            result.setStatusDesc("传入计划id无对应计划,planNid is"+planId);
            return result;
        }

        logger.info("customize:{}", JSONObject.toJSONString(customize));
        // 计划基本信息
        this.setPlanInfo(hjhPlanResultBean, customize);
        // 用户的用户验证
        this.setUserValidationInfo(hjhPlanResultBean, request);

        LogUtil.endLog(WxHjhPlanController.class.getName(), WxHjhPlanDefine.HJH_PLAN_DETAIL_ACTION);

        result.setObject(hjhPlanResultBean);
        return result;
    }

    /**
     * 查询相应的汇计划的标的组成
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    @RequestMapping(value = WxHjhPlanDefine.HJH_PLAN_BORROW_ACTION, produces = "application/json; charset=utf-8")
    public BaseResultBean searchHjhPlanBorrow(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @PathVariable String planId) {

        SimpleResultBean<HjhPlanBorrowResultBean> result = new SimpleResultBean<>();

        LogUtil.startLog(WxHjhPlanController.class.getName(), WxHjhPlanDefine.HJH_PLAN_BORROW_ACTION);

        HjhPlanBorrowResultBean vo = new HjhPlanBorrowResultBean();

        this.searchHjhPlanBorrow(vo, planId, currentPage, pageSize);
        LogUtil.endLog(WxHjhPlanController.class.getName(), WxHjhPlanDefine.HJH_PLAN_BORROW_ACTION);

        result.setObject(vo);
        return result;
    }

    /**
     * 查询相应的汇计划的加入记录
     *
     * @param currentPage
     * @param pageSize
     * @param planId
     * @return
     */
    @RequestMapping(value = WxHjhPlanDefine.HJH_PLAN_ACCEDE_ACTION, produces = "application/json; charset=utf-8")
    public BaseResultBean searchHjhPlanAccede(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @PathVariable String planId) {
        LogUtil.startLog(WxHjhPlanController.class.getName(), WxHjhPlanDefine.HJH_PLAN_ACCEDE_ACTION);

        SimpleResultBean<HjhPlanAccedeResultBean> result = new SimpleResultBean<>();

        HjhPlanAccedeResultBean vo = new HjhPlanAccedeResultBean();
        this.getHjhPlanAccede(vo, planId, currentPage, pageSize);
        LogUtil.endLog(WxHjhPlanController.class.getName(), WxHjhPlanDefine.HJH_PLAN_ACCEDE_ACTION);

        result.setObject(vo);
        return result;
    }

    /**
     * 返回汇计划基本信息
     *
     * @param result
     * @param customize
     */
    private void setPlanInfo(HjhPlanResultBean result, DebtPlanDetailCustomize customize) {
        HjhPlanResultBean.ProjectInfo projectInfo = result.getProjectInfo();
        projectInfo.setType(PLAN_TYPE_NAME);
        // 计划开放额度
        String openAccount = customize.getAvailableInvestAccount();
        projectInfo.setAccount(CommonUtils.formatAmount(openAccount));
        boolean isOpenAccountMoreZero = new BigDecimal(openAccount).compareTo(BigDecimal.ZERO) > 0;
        if ("1".equals(customize.getPlanStatus()) && isOpenAccountMoreZero) {
            // 立即加入
            projectInfo.setStatus("1");
        } else {
            // 稍后开启
            projectInfo.setStatus("0");
            if(!isOpenAccountMoreZero){
                projectInfo.setAccount("0.00");
            }
        }

        projectInfo.setPlanApr(customize.getPlanApr());
        projectInfo.setPlanPeriod(customize.getPlanPeriod());
        projectInfo.setPlanPeriodUnit(CommonUtils.getPeriodUnitByRepayStyle(customize.getBorrowStyle()));

        // 计划的加入人次
        projectInfo.setPlanPersonTime(String.valueOf(countPlanPersonTime(customize.getPlanNid())));

        // 项目进度 本期预留，填写固定值
        projectInfo.setPlanProgressStatus("4");
        projectInfo.setPlanName(customize.getPlanName());
        // 计息时间
        projectInfo.setOnAccrual(PLAN_ON_ACCRUAL);
        projectInfo.setRepayStyle(customize.getBorrowStyleName());

        HjhPlanResultBean.ProjectDetail projectDetail = result.getProjectDetail();
        projectDetail.setAddCondition(MessageFormat.format(PLAN_ADD_CONDITION, customize.getDebtMinInvestment(),
                customize.getDebtInvestmentIncrement()));

        // 数据库保存的p标签取出，避免影响前端排版
        String planInfo = customize.getPlanConcept();
        if (!StringUtils.isEmpty(planInfo)) {
            // jsoup 解析html富文本
            Document doc = Jsoup.parseBodyFragment(planInfo);
            // 获取P标签里面的值
            if (doc != null) {
                planInfo = doc.getElementsByTag("p").text();
            }
        }
        projectDetail.setPlanInfo(planInfo);
    }


    /**
     * 获取特定计划加入人次
     *
     * @param planNid
     * @return
     */
    private Integer countPlanPersonTime(String planNid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("planNid", planNid);
        return this.hjhPlanService.countPlanAccedeRecordTotal(params);
    }

    /**
     * 检查当前访问用户是否登录、是否开户、是否设置交易密码、是否允许使用、是否完成风险测评、是否授权
     *
     * @param result
     * @param request
     */
    private void setUserValidationInfo(HjhPlanResultBean result, HttpServletRequest request) {
        String sign = request.getParameter("sign");
        Integer userId = requestUtil.getRequestUserId(request);
        HjhPlanResultBean.UserValidation userValidation = result.getUserValidation();
        // 1. 检查登录状态
        if (StringUtils.isEmpty(sign) || userId == null || userId == 0) {
            userValidation.setLogined(Boolean.FALSE);
        } else {
        	
        	//角色认证是否打开
            userValidation.setIsCheckUserRole(Boolean.parseBoolean(PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN)));
            userValidation.setLogined(Boolean.TRUE);

            //Integer userId = requestUtil.getRequestUserId(request);
            Users user = this.hjhPlanService.searchLoginUser(userId);
            UsersInfo usersInfo=hjhPlanService.getUsersInfoByUserId(userId);
            userValidation.setRoleId(usersInfo.getRoleId());
            // 2. 用户是否被禁用
            userValidation.setAllowed(user.getStatus() == 0 ? Boolean.TRUE : Boolean.FALSE);
            // 3. 是否开户
            userValidation.setOpened(user.getBankOpenAccount() == 0 ? Boolean.FALSE : Boolean.TRUE);
            // 4. 是否设置过交易密码
            userValidation.setSetPassword(user.getIsSetPassword() == 1 ? Boolean.TRUE : Boolean.FALSE);

            // 7.缴费授权状态
            userValidation.setPaymentAuthStatus(user.getPaymentAuthStatus());
            userValidation.setPaymentAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
            userValidation.setInvesAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH).getEnabledStatus());
            userValidation.setCreditAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_CREDIT_AUTH).getEnabledStatus());
            // 5. 用户是否完成风险测评标识：0未测评 1已测评
            Users users = this.hjhPlanService.getUsers(userId);
			if(users.getIsEvaluationFlag()==1 && null != users.getEvaluationExpiredTime()){
				//测评到期日
				Long lCreate = users.getEvaluationExpiredTime().getTime();
				//当前日期
				Long lNow = System.currentTimeMillis();
				if (lCreate <= lNow) {
					//已过期需要重新评测
	                userValidation.setRiskTested("2");
				} else {
					//已测评并未过有效期
					userValidation.setRiskTested("1");
				}
            }else{
            	//未测评
                userValidation.setRiskTested("0");
            }
            try {
                // 6. 用户是否完成自动授权标识: 0: 未授权 1:已授权
                HjhUserAuth hjhUserAuth = this.hjhPlanService.getHjhUserAuthByUserId(userId);
                if (hjhUserAuth == null || (Validator.isNotNull(hjhUserAuth) && hjhUserAuth.getAutoInvesStatus() == 0)) {
                    userValidation.setAutoInves(Boolean.FALSE);
                } else {
                    userValidation.setAutoInves(Boolean.TRUE);
                }

                if ( hjhUserAuth == null || (Validator.isNotNull(hjhUserAuth) && hjhUserAuth.getAutoCreditStatus() == 0)) {
                    userValidation.setAutoTransfer(Boolean.FALSE);
                } else {
                    userValidation.setAutoTransfer(Boolean.TRUE);
                }
            } catch (Exception e) {
                logger.error("用户是否完成自动授权标识出错....", e);
                userValidation.setAutoInves(Boolean.FALSE);
                userValidation.setAutoTransfer(Boolean.FALSE);
            }

        }
    }

    /**
     * 创建相应的项目的加入记录分页信息
     *
     * @param result
     * @param planNid
     * @param pageNo
     * @param pageSize
     */
    private void getHjhPlanAccede(HjhPlanAccedeResultBean result, String planNid, int pageNo, int pageSize) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("planNid", planNid);
        int recordTotal = this.hjhPlanService.countPlanAccedeRecordTotal(params);

        // 加入总人次
        result.setUserCount(recordTotal);
        // 加入总金额
        result.setAccount(this.getPlanAccedeAccount(params));
        if (recordTotal > 0) {
            int limit = pageSize;
            int page = pageNo;
            int offSet = (page - 1) * limit;
            if (offSet == 0 || offSet > 0) {
                params.put("limitStart", offSet);
            }
            if (limit > 0) {
                params.put("limitEnd", limit);
            }
            List<DebtPlanAccedeCustomize> recordList = hjhPlanService.selectPlanAccedeList(params);

            if (!CollectionUtils.isEmpty(recordList)) {
                List<HjhPlanAccedeResultBean.AccedeList> accedeList = result.getAccedeList();
                HjhPlanAccedeResultBean.AccedeList accede = null;
                for (DebtPlanAccedeCustomize entity : recordList) {
                    accede = new HjhPlanAccedeResultBean.AccedeList();
                    accede.setAccedeAccount(entity.getAccedeAccount());
                    accede.setAccedeTime(entity.getAccedeTime());
                    accede.setUserName(entity.getUserName());
                    accedeList.add(accede);
                }
            }

            // 判断本次查询是否已经全部查出数据
            if ((page * limit) > recordTotal) {
                result.setEnd(Boolean.TRUE);
            } else {
                result.setEnd(Boolean.FALSE);
            }
        }
    }

    /**
     * 创建计划的标的组成分页信息
     *
     * @param result
     * @param planNid
     * @param pageNo
     * @param pageSize
     */
    private void searchHjhPlanBorrow(HjhPlanBorrowResultBean result, String planNid, int pageNo, int pageSize) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("planNid", planNid);
		Date date = GetDate.getDate();
		int dayStart10 = GetDate.getDayStart10(date);
		int dayEnd10 = GetDate.getDayEnd10(date);
		params.put("startTime", dayStart10);
		params.put("endTime", dayEnd10);
        int recordTotal = this.hjhPlanService.countPlanBorrowRecordTotal(params);
        // 加入总人次
        result.setUserCount(recordTotal);
        // 加入总金额
        result.setAccount(this.getPlanAccedeAccount(params));
        if (recordTotal > 0) {
            int limit = pageSize;
            int page = pageNo;
            int offSet = (page - 1) * limit;
            if (offSet == 0 || offSet > 0) {
                params.put("limitStart", offSet);
            }
            if (limit > 0) {
                params.put("limitEnd", limit);
            }
            List<DebtPlanBorrowCustomize> consumeList = hjhPlanService.selectPlanBorrowList(params);

            if (!CollectionUtils.isEmpty(consumeList)) {
                List<HjhPlanBorrowResultBean.BorrowList> borrowList = result.getBorrowList();
                HjhPlanBorrowResultBean.BorrowList borrow = null;
                for (DebtPlanBorrowCustomize entity : consumeList) {
                    borrow = new HjhPlanBorrowResultBean.BorrowList();
                    borrow.setBorrowApr(entity.getBorrowApr());
                    borrow.setBorrowNid(entity.getBorrowNid());
                    borrow.setBorrowPeriod(entity.getBorrowPeriod());
                    borrow.setTrueName(entity.getTrueName());
                    borrowList.add(borrow);
                }
            }

            // 判断本次查询是否已经全部查出数据
            if ((page * limit) > recordTotal) {
                result.setEnd(Boolean.TRUE);
            } else {
                result.setEnd(Boolean.FALSE);
            }
        }
    }

    /**
     * 根据planNid获取计划加入金额
     *
     * @param params
     * @return
     */
    private String getPlanAccedeAccount(Map<String, Object> params) {
        Long sum = hjhPlanService.selectPlanAccedeSum(params);// 加入总金额
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
        if (sum == null || sum == 0) {
            return "0";
        } else {
            return df.format(sum);
        }
    }
    
    /**
     * 
     * 获取汇计划出借url
     * @author sunss
     * @param vo
     * @param request
     * @return
     */
    @SignValidate
    @ResponseBody
    @RequestMapping(value = WxHjhPlanDefine.TENDER_URL_ACTION)
    public BaseResultBean getHJHTenderUrl(@ModelAttribute WxHJHTenderVo vo, HttpServletRequest request) {

	    // 2017-11-08 by sunss  汇计划验证判断
	    // 先进行check  然后 进行出借
	    // check start
	    // 取得承接债转的用户在汇付天下的客户号
    	HjhTenderResultBean result = new HjhTenderResultBean();
    	Integer userId = requestUtil.getRequestUserId(request);
    	// 神策数据统计 add by liuyang 20180726 start
        String presetProps  = vo.getPresetProps();
    	// 神策数据统计 add by liuyang 20180726 end
        BankOpenAccount accountChinapnrCrediter = hjhPlanService.getBankOpenAccount(userId);
        Users user = this.hjhPlanService.getUsers(userId);
        
        // 检查用户角色是否能出借  合规接口改造之后需要判断
        UsersInfo userInfo = hjhPlanService.getUsersInfoByUserId(userId);
        if (null != userInfo) {
            String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
            if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
                if (userInfo.getRoleId() != 1) {// 担保机构用户
                    result.setStatus("99");
                    result.setStatusDesc("仅限出借人进行出借");
                    result.setIsAllowedTender(Boolean.FALSE);
                    return result;
                }
            }


        } else {
        	result.setStatus("99");
        	result.setStatusDesc("用户信息不存在");
        	result.setIsAllowedTender(Boolean.FALSE);
        }
		// 缴费授权状态
        result.setPaymentAuthStatus(user.getPaymentAuthStatus());
        
        
        if (accountChinapnrCrediter == null || Validator.isNull(accountChinapnrCrediter.getAccount())) {
        	return result.setEnum(ResultEnum.USER_ERROR_200);
        }
        /**************************** 风险测评强制测评标示 开始 pcc ***********************************/
        // 0未测评 1已测评 2已过期
        Integer userEvaluationResultFlag = user.getIsEvaluationFlag();
        if (0 == userEvaluationResultFlag) {
        	return result.setEnum(ResultEnum.USER_ERROR_213);
        } else {
			if(user.getIsEvaluationFlag()==1 && null != user.getEvaluationExpiredTime()){
				//测评到期日
				Long lCreate = user.getEvaluationExpiredTime().getTime();
				//当前日期
				Long lNow = System.currentTimeMillis();
				if (lCreate <= lNow) {
					//已过期需要重新评测
		        	return result.setEnum(ResultEnum.USER_ERROR_216);
				}
			} else {
				//未获取到测评信息
	        	return result.setEnum(ResultEnum.USER_ERROR_213);
			}
        }
        CouponConfigCustomizeV2 cuc = null;
        int couponOldTime = Integer.MIN_VALUE;
		logger.info("couponGrantId is:{}, userId is:{}", vo.getCouponGrantId(), userId);
		if (StringUtils.isNotEmpty(vo.getCouponGrantId()) && new Integer(vo.getCouponGrantId()) > 0) {
			cuc = hjhPlanService.getCouponUser(vo.getCouponGrantId(), userId);
			// 排他check用
			couponOldTime = cuc.getUserUpdateTime();
		}
		JSONObject checkResult = new JSONObject();
		checkResult  = hjhPlanService.checkHJHParam(vo.getBorrowNid(), vo.getAccount(), String.valueOf(userId), CustomConstants.CLIENT_WECHAT, cuc);
	    if(CustomConstants.APP_STATUS_FAIL.equals(checkResult.getString(CustomConstants.APP_STATUS))){
	        // check 失败
	    	result.setStatus(ResultEnum.FAIL.getStatus());
	    	result.setStatusDesc(checkResult.getString(CustomConstants.APP_STATUS_DESC));
	        return result;
	    }
	    StringBuffer sb = new StringBuffer(WxHjhPlanDefine.HOST + WxHjhPlanDefine.REQUEST_HOME + WxHjhPlanDefine.REQUEST_MAPPING + WxHjhPlanDefine.INVEST_ACTION);
        sb.append(".page?sign=").append(vo.getSign()).append(CustomConstants.APP_PARM_AND).append("borrowNid=").append(vo.getBorrowNid()).append(CustomConstants.APP_PARM_AND)
                .append("account=").append(vo.getAccount()).append(CustomConstants.APP_PARM_AND).append("couponGrantId=").append(strEncode(vo.getCouponGrantId()));
        if (StringUtils.isNotEmpty(vo.getCouponGrantId()) && new Integer(vo.getCouponGrantId()) > 0) {
            sb.append(CustomConstants.APP_PARM_AND).append("couponGrantId=").append(vo.getCouponGrantId());
        }

        if (couponOldTime != Integer.MIN_VALUE) {
            sb.append(CustomConstants.APP_PARM_AND).append("couponOldTime=").append(couponOldTime);
        }

        //用户测评校验开始
        //从user中获取客户类型，ht_user_evalation_result（用户测评总结表）
        UserEvalationResultCustomize userEvalationResultCustomize = evaluationService.selectUserEvalationResultByUserId(userId);
        if(userEvalationResultCustomize != null){
            EvaluationConfig evalConfig = new EvaluationConfig();
            //4.智投出借者测评类型校验
            String intellectualEveluationTypeCheck = "0";
            //5.智投单笔投资金额校验
            String intellectualEvaluationMoneyCheck = "0";
            //6.智投待收本金校验
            String intellectualCollectionEvaluationCheck = "0";
            //获取开关信息
            List<EvaluationConfig> evalConfigList = evaluationService.selectEvaluationConfig(evalConfig);
            if(evalConfigList != null && evalConfigList.size() > 0){
                evalConfig = evalConfigList.get(0);
                //4.智投出借者测评类型校验
                intellectualEveluationTypeCheck = evalConfig.getIntellectualEveluationTypeCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualEveluationTypeCheck());
                //5.智投单笔投资金额校验
                intellectualEvaluationMoneyCheck = evalConfig.getIntellectualEvaluationMoneyCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualEvaluationMoneyCheck());
                //6.智投待收本金校验
                intellectualCollectionEvaluationCheck = evalConfig.getIntellectualCollectionEvaluationCheck() == null ? "0" : String.valueOf(evalConfig.getIntellectualCollectionEvaluationCheck());
                //7.投标时校验（二期）(预留二期开发)
            }
            //初始化金额返回值
            String revaluation_money,revaluation_money_principal;
            //根据类型从redis或数据库中获取测评类型和上限金额
            String eval_type = userEvalationResultCustomize.getType();
            switch (eval_type){
                case "保守型":
                    //从redis获取金额（单笔）
                    revaluation_money = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_CONSERVATIVE) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_CONSERVATIVE);
                    //如果reids不存在或者为零尝试获取数据库（单笔）
                    if("0".equals(revaluation_money)){
                        revaluation_money = evalConfig.getConservativeEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationSingleMoney());
                    }
                    //从redis获取金额（代收本金）
                    revaluation_money_principal = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_CONSERVATIVE_PRINCIPAL);
                    //如果reids不存在或者为零尝试获取数据库（代收本金）
                    if("0".equals(revaluation_money_principal)){
                        revaluation_money_principal = evalConfig.getConservativeEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getConservativeEvaluationPrincipalMoney());
                    }
                    break;
                case "稳健型":
                    //从redis获取金额（单笔）
                    revaluation_money = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_ROBUSTNESS) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_ROBUSTNESS);
                    //如果reids不存在或者为零尝试获取数据库（单笔）
                    if("0".equals(revaluation_money)){
                        revaluation_money = evalConfig.getSteadyEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationSingleMoney());
                    }
                    //从redis获取金额（代收本金）
                    revaluation_money_principal = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_ROBUSTNESS_PRINCIPAL);
                    //如果reids不存在或者为零尝试获取数据库（代收本金）
                    if("0".equals(revaluation_money_principal)){
                        revaluation_money_principal = evalConfig.getSteadyEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getSteadyEvaluationPrincipalMoney());
                    }
                    break;
                case "成长型":
                    //从redis获取金额（单笔）
                    revaluation_money = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_GROWTH) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_GROWTH);
                    //如果reids不存在或者为零尝试获取数据库（单笔）
                    if("0".equals(revaluation_money)){
                        revaluation_money = evalConfig.getGrowupEvaluationSingleMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationSingleMoney());
                    }
                    //从redis获取金额（代收本金）
                    revaluation_money_principal = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_GROWTH_PRINCIPAL) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_GROWTH_PRINCIPAL);
                    //如果reids不存在或者为零尝试获取数据库（代收本金）
                    if("0".equals(revaluation_money_principal)){
                        revaluation_money_principal = evalConfig.getGrowupEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getGrowupEvaluationPrincipalMoney());
                    }
                    break;
                case "进取型":
                    //从redis获取金额（单笔）
                    revaluation_money = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_AGGRESSIVE) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_AGGRESSIVE);
                    //如果reids不存在或者为零尝试获取数据库（单笔）
                    if("0".equals(revaluation_money)){
                        revaluation_money = evalConfig.getEnterprisingEvaluationSinglMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationSinglMoney());
                    }
                    //从redis获取金额（代收本金）
                    revaluation_money_principal = RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL) == null ? "0": RedisUtils.get(com.hyjf.common.cache.RedisConstants.REVALUATION_AGGRESSIVE_PRINCIPAL);
                    //如果reids不存在或者为零尝试获取数据库（代收本金）
                    if("0".equals(revaluation_money_principal)){
                        revaluation_money_principal = evalConfig.getEnterprisingEvaluationPrincipalMoney() == null ? "0": String.valueOf(evalConfig.getEnterprisingEvaluationPrincipalMoney());
                    }
                    break;
                default:
                    revaluation_money = null;
                    revaluation_money_principal = null;
            }
            //风险类型校验
            if(CustomConstants.EVALUATION_CHECK.equals(intellectualEveluationTypeCheck)){
                //计划类判断用户类型为稳健型以上才可以投资
                HjhPlan plan = hjhPlanService.getPlanByNid(vo.getBorrowNid());
                if(plan != null){
                    if(!CommonUtils.checkStandardInvestment(eval_type,"HJHPLAN",plan.getInvestLevel())){
                        //返回错误码
                        result.setStatus(CustomConstants.BANK_TENDER_RETURN_CUSTOMER_STANDARD_FAIL);
                        //返回类型和限额
                        result.setEvalType(eval_type);
                        result.setEvalFlagType(plan.getInvestLevel());
                        return result;
                    }
                }
            }
            if(revaluation_money == null){
                logger.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
            }else {
                if(CustomConstants.EVALUATION_CHECK.equals(intellectualEvaluationMoneyCheck)){
                    //金额对比判断（校验金额 大于 设置测评金额）
                    if (new BigDecimal(vo.getAccount()).compareTo(new BigDecimal(revaluation_money)) > 0) {
                        //返回错误码
                        result.setStatus(CustomConstants.BANK_TENDER_RETURN_LIMIT_EXCESS);
                        //返回类型和限额
                        result.setEvalType(eval_type);
                        result.setRevaluationMoney(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money).intValue()));
                        return result;
                    }
                }
            }
            if(revaluation_money_principal == null){
                logger.info("=============从redis中获取测评类型和上限金额异常!(没有获取到对应类型的限额数据) eval_type="+eval_type);
            }else {
                //代收本金限额校验
                if(CustomConstants.EVALUATION_CHECK.equals(intellectualCollectionEvaluationCheck)){
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
                            result.setStatus(CustomConstants.BANK_TENDER_RETURN_LIMIT_EXCESS_PRINCIPAL);
                            //返回类型和限额
                            result.setEvalType(eval_type);
                            result.setRevaluationMoneyPrincipal(StringUtil.getTenThousandOfANumber(Double.valueOf(revaluation_money_principal).intValue()));
                            return result;
                        }
                    }
                }
            }
        }else{
            logger.info("=============该用户测评总结数据为空! userId="+userId);
        }
        logger.info("神策预置属性:" + presetProps);

        // 神策数据统计 add by liuyang 20180726 start
//        if (StringUtils.isNotBlank(presetProps)) {
//            sb.append(CustomConstants.APP_PARM_AND).append("presetProps=").append(presetProps);
//        }
        // 神策数据统计 add by liuyang 20180726 end

        result.setEnum(ResultEnum.SUCCESS);
        logger.info("hjh tender url:{}, userId is:{}", sb.toString(), userId);
        result.setUrl(sb.toString());
	
        return result;
    }

    /**
     * 加入计划
     * @param request
     * @param response
     * @return
     */
    @SignValidate
	@RequestMapping(value = WxHjhPlanDefine.INVEST_ACTION)
	public ModelAndView tender(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(WxHjhPlanDefine.class.toString(), WxHjhPlanDefine.INVEST_ACTION);
		logger.info("开始调用汇计划出借");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView = new ModelAndView(WxHjhPlanDefine.JUMP_HTML);
		BaseMapBean baseMapBean = new BaseMapBean();
		// 项目id
		String borrowNid = request.getParameter("borrowNid");
		// 出借金额
		String account = request.getParameter("account");
		// 用户id
		Integer userId = requestUtil.getRequestUserId(request);

        HjhPlan plan =  hjhPlanService.getPlanByNid(borrowNid);
        logger.info("开始获取查询的数据="+ JSON.toJSONString(plan));
        // 是否月标(true:月标, false:天标)
        String lockPeriod = plan.getLockPeriod().toString();
        String dayOrMonth="";
        if (plan.getIsMonth().intValue()!=0) {//月标
            dayOrMonth = lockPeriod + "个月智投";
        } else {
            dayOrMonth = lockPeriod + "天智投";
        }
        Users users = hjhPlanService.getUsers(userId);
        UsersInfo usersInfo = hjhPlanService.getUsersInfoByUserId(userId);
        UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
        userOperationLogEntity.setOperationType(4);
        userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
        userOperationLogEntity.setPlatform(1);
        userOperationLogEntity.setRemark(dayOrMonth);
        userOperationLogEntity.setOperationTime(new Date());
        userOperationLogEntity.setUserName(users.getUsername());
        userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
        loginService.sendUserLogMQ(userOperationLogEntity);
		// 神策数据统计 add by liuyang 20180726 start
        // 神策数据统计事件的预置属性
        String presetProps = request.getParameter("presetProps");
        // 神策数据统计 add by liuyang 20180726 end
		String couponGrantId = request.getParameter("couponGrantId");
		logger.info("hjh出借 is:{}, userId is:{}", couponGrantId, userId);
		// check 通过了 就 出借
		JSONObject result;
		result = hjhPlanService.updateInvestInfo(modelAndView, borrowNid, account, String.valueOf(userId), "1",
				GetCilentIP.getIpAddr(request), couponGrantId);
		ModelMap map = modelAndView.getModelMap();
		logger.info("请求出借返回结果result: " + result);
		logger.info("请求出借返回结果modelAndView: " + JSONObject.toJSONString(map));
		if (result != null && result.get(CustomConstants.APP_STATUS).equals(CustomConstants.APP_STATUS_SUCCESS)) {
			modelAndView = new ModelAndView(WxHjhPlanDefine.JUMP_HTML);
			// 成功
			baseMapBean.set("amount", CommonUtils.formatAmount(account));

			String couponType = (map == null ? "0" : String.valueOf(map.get("couponType")));
			baseMapBean.set("couponType", couponType);

			String couponQuota = (map == null ? "" : String.valueOf(map.get("couponQuota")));

			// 优惠券利息
			String couponInterestStr = (map == null ? "" : String.valueOf(map.get("couponInterest")));
			BigDecimal couponInterest = new BigDecimal(couponInterestStr);
			// 本金出借利息
			String earningsStr = (map == null ? "" : String.valueOf(map.get("earnings")));
			BigDecimal earnings = new BigDecimal(earningsStr);

			// 预期收益
			BigDecimal capitalInterest = couponInterest.add(earnings);
			// 代金券需要加上券的面值
			if ("3".equals(couponType)) {
				logger.info("代金券加上券的面值....面值为："+couponQuota);
				couponQuota = couponQuota.replaceAll(",", "");
				capitalInterest = capitalInterest.add(new BigDecimal(couponQuota));
			}

			// 加息券增加%
			if ("2".equals(couponType)) {
				try {
					couponQuota = URLEncoder.encode(couponQuota.concat("%"), "utf-8");
				} catch (UnsupportedEncodingException e) {
					logger.error("URLEncoder编码错误....", e);
				}
				logger.info("加息券面额....{}", couponQuota);
			}
			baseMapBean.set("couponValue", couponQuota);

			// capitalInterest.add(new BigDecimal(account));
			logger.info("计算出的预期收益, {}", String.valueOf(capitalInterest));
	        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
	        baseMapBean.set(CustomConstants.APP_STATUS_DESC, "出借成功！");
			baseMapBean.set("income", String.valueOf(capitalInterest));
			baseMapBean.setCallBackAction(CustomConstants.HOST+WxHjhPlanDefine.JUMP_HJH_TENDER_SUCCESS_URL.replace("{planId}", borrowNid));
			logger.info("baseMapBean: {}", JSONObject.toJSONString(baseMapBean));
			modelAndView.addObject("callBackForm", baseMapBean);
			// 神策数据统计 add by liuyang 20180726 start
            logger.info("神策预置属性presetProps1:[" + presetProps + "]");
            if (StringUtils.isNotBlank(presetProps)){
                try {
                    presetProps = URLDecoder.decode(presetProps,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                logger.info("神策预置属性presetProps2:[" + presetProps + "]");
                SensorsDataBean sensorsDataBean = new SensorsDataBean();
                // 将json串转换成Bean
                try {
                    Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
                    });
                    sensorsDataBean.setPresetProps(sensorsDataMap);
                    sensorsDataBean.setUserId(userId);
                    sensorsDataBean.setEventCode("submit_intelligent_invest");
                    sensorsDataBean.setOrderId(String.valueOf(result.get("accedeOrderId")));
                    // 发送神策数据统计MQ
                    this.hjhPlanService.sendSensorsDataMQ(sensorsDataBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 神策数据统计 add by liuyang 20180726 end

			return modelAndView;
		} else {
			modelAndView = new ModelAndView(WxBorrowDefine.JUMP_HTML);
	        baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
	        baseMapBean.set(CustomConstants.APP_STATUS_DESC, result.getString(CustomConstants.APP_STATUS_DESC));
	        baseMapBean.setCallBackAction(CustomConstants.HOST+WxHjhPlanDefine.JUMP_HJH_TENDER_ERROR_URL.replace("{planId}", borrowNid));
	        modelAndView.addObject("callBackForm", baseMapBean);
			return modelAndView;
		}
	}


    /**
     * 从payload里面取神策预置属性,为解决从request里面取乱码的问题
     *
     * @param req
     * @return
     */
    private String getStringFromStream(HttpServletRequest req) {
        ServletInputStream is;
        try {
            is = req.getInputStream();
            int nRead = 1;
            int nTotalRead = 0;
            byte[] bytes = new byte[10240];
            while (nRead > 0) {
                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
                if (nRead > 0)
                    nTotalRead = nTotalRead + nRead;
            }
            String str = new String(bytes, 0, nTotalRead, "utf-8");
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
