package com.hyjf.app.user.credit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.TypeReference;
import com.hyjf.app.user.manage.AppUserService;
import com.hyjf.app.user.project.InvestProjectService;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.util.*;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.BaseMapBean;
import com.hyjf.app.BaseResultBeanFrontEnd;
import com.hyjf.app.bank.user.auto.AutoDefine;
import com.hyjf.app.project.ProjectService;
import com.hyjf.app.user.plan.MyPlanService;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.bank.service.user.assetmanage.AssetManageService;
import com.hyjf.bank.service.user.auth.AuthService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.app.AppCouponInfoCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderBorrowCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditCustomize;
import com.hyjf.mybatis.model.customize.web.CurrentHoldRepayMentPlanListCustomize;

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
@RequestMapping(value = AppTenderCreditBorrowDefine.REQUEST_MAPPING)
public class AppTenderCreditBorrowController extends BaseController {
    private Logger logger =  LoggerFactory.getLogger(AppTenderCreditBorrowController.class);
    @Autowired
    private MqService mqService;
    @Autowired
    private AppTenderCreditService appTenderCreditService;
    @Autowired
    private DebtConfigService debtConfigService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MyPlanService myPlanService;
    @Autowired
    private AppUserService appUserService;
	@Autowired
    private AssetManageService assetManageService;
	@Autowired
    private AuthService authService;
    @Autowired
    private InvestProjectService investProjectService;
	//法大大
    @Autowired
    private FddGenerateContractService fddGenerateContractService;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

    /**
     *
     * 转让设置画面
     *
     * @author liuyang
     * @param request
     * @param response
     * @param appTenderCreditCustomize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_DETAIL)
    public AppBorrowTransferDetailResultBean searchTenderToCreditDetail(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_DETAIL);
        AppBorrowTransferDetailResultBean result = new AppBorrowTransferDetailResultBean();
        JSONObject projectInfo = new JSONObject();

        String sign = request.getParameter("sign");
        String tenderId = request.getParameter("tenderId");
        String borrowId = request.getParameter("borrowId");

        logger.info("sing is :{}", sign);
        logger.info("转让设置画面, tenderId is: {}, borrowId is:{}", tenderId, borrowId);

		if (StringUtils.isBlank(sign) || StringUtils.isBlank(tenderId) || StringUtils.isBlank(borrowId)) {
			result.setStatus(BaseResultBeanFrontEnd.FAIL);
			result.setStatusDesc("参数异常...");
			return result;
		}

        try {
            // 用户id
            Integer userId = SecretUtil.getUserId(sign);
            if (userId != null && userId.intValue() != 0) {
                // 获取当前时间
                Integer nowTime = GetDate.getNowTime10();
                // 获取债转详情页面
                List<AppTenderCreditCustomize> appTenderToCreditDetail = this.appTenderCreditService.selectTenderToCreditDetail(userId, nowTime,borrowId,
                        tenderId);
                //债转费率配置
                List<DebtConfig> config = debtConfigService.selectDebtConfigList();
                if(!CollectionUtils.isEmpty(config)){
                    projectInfo.put("attornRate",config.get(0).getAttornRate().setScale(2, BigDecimal.ROUND_DOWN));
                    projectInfo.put("concessionRateUp",config.get(0).getConcessionRateUp().setScale(1, BigDecimal.ROUND_DOWN));
                    projectInfo.put("concessionRateDown",config.get(0).getConcessionRateDown().setScale(1, BigDecimal.ROUND_DOWN));
                    projectInfo.put("toggle",config.get(0).getToggle());
                    projectInfo.put("closeDes",config.get(0).getCloseDes());
                    projectInfo.put("serviceRate", config.get(0).getAttornRate().setScale(2, BigDecimal.ROUND_DOWN));//原来逻辑的字段
                }
                if (appTenderToCreditDetail != null && appTenderToCreditDetail.size() > 0) {
                    int lastdays = 0;
                    String borrowNid = appTenderToCreditDetail.get(0).getBorrowNid();
                    // 根据borrowNid判断是否分期
                    Borrow borrow = this.appTenderCreditService.selectBorrowByBorrowNid(borrowNid);
                    String borrowStyle = borrow.getBorrowStyle();
                    if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
                        lastdays = GetDate
                                .daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime), GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(appTenderToCreditDetail.get(0).getRecoverTime())));
                    }
                    // 等额本息和等额本金和先息后本
                    if (borrowStyle.equals(CalculatesUtil.STYLE_MONTH) || borrowStyle.equals(CalculatesUtil.STYLE_PRINCIPAL) || borrowStyle.equals(CalculatesUtil.STYLE_ENDMONTH)) {
                        List<BorrowRepayPlan> list = this.appTenderCreditService.searchBorrowRepayPlanList(borrowNid, borrow.getBorrowPeriod());
                        if (list != null && list.size() > 0) {
                            lastdays = GetDate.daysBetween(GetDate.getDateTimeMyTimeInMillis(nowTime), GetDate.getDateTimeMyTimeInMillis(Integer.parseInt(list.get(0).getRepayTime())));
                        }
                    }
                    AppTenderCreditCustomize tenderCreditCustomize=appTenderToCreditDetail.get(0);
                    tenderCreditCustomize.setLastDays(lastdays + "");

                    Map<String, BigDecimal> creditCreateMap = this.appTenderCreditService.selectassignInterestForBigDecimal(borrowId,
                            tenderId, "0.0", nowTime.intValue());
                    if (creditCreateMap != null) {
                        projectInfo.put("assignInterestAdvance", creditCreateMap.get("assignInterestAdvance"));
                        projectInfo.put("expectHoldProfit", creditCreateMap.get("assignInterestAdvance"));// 预期持有收益
                    }

                    projectInfo.put("borrowApr", tenderCreditCustomize.getBorrowApr());
                    projectInfo.put("borrowPeriod", tenderCreditCustomize.getBorrowPeriod());
                    projectInfo.put("borrowPeriodUnit", "endday".equals(borrow.getBorrowStyle())?"天":"个月");
                    projectInfo.put("account", creditCreateMap.get("creditCapital"));
                    projectInfo.put("borrowId", tenderCreditCustomize.getBorrowNid());
                    projectInfo.put("tenderPeriod", tenderCreditCustomize.getTenderPeriod());
                    projectInfo.put("lastDays", tenderCreditCustomize.getLastDays());

                    result.setStatus(BaseResultBeanFrontEnd.SUCCESS);
                    result.setStatusDesc(BaseResultBeanFrontEnd.SUCCESS_MSG);
                    result.setProjectInfo(projectInfo);
                } else {
                    result.setStatus(BaseResultBeanFrontEnd.FAIL);
                    result.setStatusDesc("获取债转数据失败!");
                    result.setProjectInfo(projectInfo);
                    return result;
                }
            } else {
                LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "用户未登录");
                result.setStatus(BaseResultBeanFrontEnd.FAIL);
                result.setStatusDesc("用户未登录");
                result.setProjectInfo(projectInfo);
                System.out.println(userId);
            }
        } catch (Exception e) {
            LogUtil.infoLog(this.getClass().getName(), "searchTenderToCreditDetail", "系统异常");
            result.setStatus(BaseResultBeanFrontEnd.FAIL);
            result.setStatusDesc("系统异常");
            result.setProjectInfo(projectInfo);
            return result;
        }

        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_DETAIL);
        return result;
    }



    /**
     * 发送短信验证码（ajax请求） 短信验证码数据保存
     */
    @ResponseBody
    @RequestMapping(value = AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, produces = "application/json; charset=utf-8")
    public String sendCode(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE);
        String sign = request.getParameter("sign");
        // 用户id
        Integer userId = SecretUtil.getUserId(sign);
//        Integer userId = 3630;
        JSONObject ret = new JSONObject();
        JSONObject info = new JSONObject();
        JSONObject jo = new JSONObject();
        if (userId != null && userId.intValue() != 0) {

        UserInfoCustomize userInfo = this.appTenderCreditService.getUserInfoByUserId(userId+"");
            if (userInfo != null) {
                // 手机号码(必须,数字,最大长度)
                String mobile = userInfo.getMobile();
                SmsConfig smsConfig = appTenderCreditService.getSmsConfig();
                logger.info("smsConfig---------------------------------------" + JSON.toJSONString(smsConfig));
                String ip = GetCilentIP.getIpAddr(request);
                String ipCount = RedisUtils.get(ip + ":MaxIpCount");
                if (StringUtils.isEmpty(ipCount)) {
                    ipCount = "0";
                    RedisUtils.set(ip + ":MaxIpCount", "0");
                }
                logger.info(mobile + "------ip---" + ip + "----------MaxIpCount-----------" + ipCount);
                if (Integer.valueOf(ipCount) >= smsConfig.getMaxIpCount()) {
                    if (Integer.valueOf(ipCount) == smsConfig.getMaxIpCount()) {
                        try {
                            appTenderCreditService.sendSms(mobile, "IP访问次数超限:" + ip);
                        } catch (Exception e) {
                            LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, e);
                        }
                        RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(ipCount) + 1) + "", 24 * 60 * 60);
                    }
                    try {
                        appTenderCreditService.sendEmail(mobile, "IP访问次数超限" + ip);
                    } catch (Exception e) {
                        LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, e);
                    }
                    jo.put(AppTenderCreditBorrowDefine.CODE_ERROR, AppTenderCreditBorrowDefine.ERROR_MAXCOUNT_OTHERS);
                    ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                    ret.put(CustomConstants.DATA, jo);
                    ret.put("status", "1");
                    ret.put(CustomConstants.APP_STATUS_DESC, "IP访问次数超限");
                    LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
                    return JSONObject.toJSONString(ret);
                }

                // 判断最大发送数max_phone_count
                String count = RedisUtils.get(mobile + ":MaxPhoneCount");
                if (StringUtils.isEmpty(count)) {
                    count = "0";
                    RedisUtils.set(mobile + ":MaxPhoneCount", "0");
                }
                logger.info(mobile + "----------MaxPhoneCount-----------" + count);
                if (Integer.valueOf(count) >= smsConfig.getMaxPhoneCount()) {
                    if (Integer.valueOf(count) == smsConfig.getMaxPhoneCount()) {
                        try {
                            appTenderCreditService.sendSms(mobile, "手机发送次数超限");
                        } catch (Exception e) {
                            LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, e);
                        }
                        RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(count) + 1) + "", 24 * 60 * 60);
                    }
                    try {
                        appTenderCreditService.sendEmail(mobile, "手机发送次数超限");
                    } catch (Exception e) {
                        LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, e);
                    }
                    jo.put(AppTenderCreditBorrowDefine.CODE_ERROR, AppTenderCreditBorrowDefine.ERROR_MAXCOUNT_OTHERS);
                    ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                    ret.put(CustomConstants.DATA, jo);
                    ret.put("status", "1");
                    ret.put(CustomConstants.APP_STATUS_DESC, "手机发送次数超限");
                    LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
                    return JSONObject.toJSONString(ret);
                }
                // 判断发送间隔时间
                String intervalTime = RedisUtils.get(mobile + ":IntervalTime");
                logger.info(mobile + "----------IntervalTime-----------" + intervalTime);
                if (!StringUtils.isEmpty(intervalTime)) {
                    jo.put(AppTenderCreditBorrowDefine.CODE_ERROR, AppTenderCreditBorrowDefine.ERROR_INTERVAL_TIME_OTHERS);
                    ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                    ret.put(CustomConstants.DATA, jo);
                    ret.put("status", "1");
                    ret.put(CustomConstants.APP_STATUS_DESC, "发送时间间隔太短");
                    LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
                    return JSONObject.toJSONString(ret);
                }
                if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
                    jo.put(AppTenderCreditBorrowDefine.MOBILE_ERROR, AppTenderCreditBorrowDefine.ERROR_TYPE_REQUIRED);
                } else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
                    jo.put(AppTenderCreditBorrowDefine.MOBILE_ERROR, AppTenderCreditBorrowDefine.ERROR_TYPE_OTHERS);
                }

                // 手机验证码
                if (jo == null || jo.isEmpty()) {
                    // 生成验证码
                    String checkCode = GetCode.getRandomSMSCode(6);
                    logger.info("生成的验证码checkCode：{}", checkCode);

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("val_code", checkCode);
                    // 发送短信验证码
                    SmsMessage smsMessage = new SmsMessage(null, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_ZHUCE, CustomConstants.CHANNEL_TYPE_NORMAL);
                    Integer result = smsProcesser.gather(smsMessage);
                    // checkCode过期时间，默认120秒
                    RedisUtils.set(mobile + ":MaxValidTime", checkCode, smsConfig.getMaxValidTime() == null ? 120 : smsConfig.getMaxValidTime() * 60);
                    // 发送checkCode最大时间间隔，默认60秒
                    RedisUtils.set(mobile + ":IntervalTime", mobile, smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime());
                    // 短信发送成功后处理
                    if (result != null && result == 1) {
                        // 累计IP次数
                        String currentMaxIpCount = RedisUtils.get(ip + ":MaxIpCount");
                        if (StringUtils.isEmpty(currentMaxIpCount)) {
                            currentMaxIpCount = "0";
                        }
                        // 累加手机次数
                        String currentMaxPhoneCount = RedisUtils.get(mobile + ":MaxPhoneCount");
                        if (StringUtils.isEmpty(currentMaxPhoneCount)) {
                            currentMaxPhoneCount = "0";
                        }
                        RedisUtils.set(ip + ":MaxIpCount", (Integer.valueOf(currentMaxIpCount) + 1) + "", 24 * 60 * 60);
                        RedisUtils.set(mobile + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", 24 * 60 * 60);
                    }
                    // 保存短信验证码
                    this.appTenderCreditService.saveSmsCode(mobile, checkCode);
                    jo.put("status", "0");
                    // jo.put("checkCode", checkCode);
                    ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
                    ret.put("status", "0");
                    String maxValidTime = "60";
                    Integer time = smsConfig.getMaxIntervalTime();
                    if (time != null) {
                        maxValidTime = time + "";
                    }
                    ret.put(CustomConstants.MAX_VALIDTIME, maxValidTime);
                    ret.put(CustomConstants.APP_STATUS_DESC, "手机验证码已经发送完成");
                    LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE);
                    return JSONObject.toJSONString(ret);
                } else {
                    ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                    ret.put(CustomConstants.DATA, jo);
                    ret.put("status", "1");
                    ret.put(CustomConstants.APP_STATUS_DESC, "系统错误");
                    LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
                    return JSONObject.toJSONString(ret);
                }
            } else {
                ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
                ret.put(CustomConstants.DATA, "");
                ret.put("status", "1");
                ret.put(CustomConstants.APP_STATUS_DESC, "用户未登录");
                LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
                return JSONObject.toJSONString(ret);
            }
        }else{
            ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
            ret.put(CustomConstants.DATA, "");
            ret.put("status", "1");
            ret.put(CustomConstants.APP_STATUS_DESC, "用户未登录");
            LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SEND_CODE, "短信验证码发送失败", null);
            return JSONObject.toJSONString(ret);
        }
    }


    /**
     * 短信验证码校验
     *
     * 用户注册数据提交（获取session数据并保存） 1.校验验证码
     * 2.若验证码正确，则获取session数据，并将相应的注册数据写入数据库（三张表），跳转相应的注册成功界面
     */
	private boolean checkCode(String userId, String code) {
		logger.info("checkCode start...userId is:{}, code is: {}", userId, code);
		UserInfoCustomize userInfo = this.appTenderCreditService.getUserInfoByUserId(userId);
		JSONObject ret = new JSONObject();
		JSONObject info = new JSONObject();
		JSONObject jo = new JSONObject();
		if (userInfo != null) {
			// 手机号码(必须,数字,最大长度)
			String mobile = userInfo.getMobile();
			if (!ValidatorCheckUtil.validateRequired(info, null, null, mobile)) {
				jo.put(AppTenderCreditBorrowDefine.MOBILE_ERROR, AppTenderCreditBorrowDefine.ERROR_TYPE_REQUIRED);
			} else if (!ValidatorCheckUtil.validateMobile(info, null, null, mobile, 11, false)) {
				jo.put(AppTenderCreditBorrowDefine.MOBILE_ERROR, AppTenderCreditBorrowDefine.ERROR_TYPE_OTHERS);
			}

			if (!ValidatorCheckUtil.validateRequired(info, null, null, code)) {
				jo.put(AppTenderCreditBorrowDefine.CODE_ERROR, AppTenderCreditBorrowDefine.ERROR_TYPE_REQUIRED);
			}
			if (jo == null || jo.isEmpty()) {
				int cnt = this.appTenderCreditService.checkMobileCode(mobile, code);
				if (cnt > 0) {
					ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_SUCCESS);
					ret.put(CustomConstants.DATA, jo);
					ret.put(CustomConstants.MSG, "");
					return true;
				} else {
					jo.put(AppTenderCreditBorrowDefine.CODE_ERROR, AppTenderCreditBorrowDefine.ERROR_TYPE_OTHERS);
					LogUtil.errorLog(AppTenderCreditBorrowDefine.THIS_CLASS, "checkCode", "验证码验证失败", null);
				}
			} else {
				ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
				ret.put(CustomConstants.DATA, "");
				ret.put(CustomConstants.MSG, jo);
			}
		} else {
			ret.put(CustomConstants.RESULT_FLAG, CustomConstants.RESULT_FAIL);
			ret.put(CustomConstants.DATA, jo);
			ret.put(CustomConstants.MSG, "用户未登录");
		}
		LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, "checkCode");
		return false;
	}



    /**
     *
     * 债转提交保存
     *
     * @author liuyang
     * @param request
     * @param response
     * @param appTenderBorrowCreditCustomize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String saveTenderToCredit(HttpServletRequest request, HttpServletResponse response, @ModelAttribute AppTenderBorrowCreditCustomize appTenderBorrowCreditCustomize) {
        LogUtil.startLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
        // 返回的JSON对象
        JSONObject ret = new JSONObject();
        ret.put("request", AppTenderCreditBorrowDefine.REQUEST_MAPPING+AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
        String sign = request.getParameter("sign");
        // 用户id
        Integer userId = SecretUtil.getUserId(sign);
        String platform = request.getParameter("platform");
     // 短信验证码
        String code = request.getParameter("code");
        // 神策数据统计 add by liuyang 20180726 start
        String presetProps = request.getParameter("presetProps");
        // 神策数据统计 add by liuyang 20180726 end
        // 债转编号
        String creditNid = request.getParameter("creditNid");
        try {
            if (userId != 0) {
                Users users = appTenderCreditService.getUsers(userId);
                UsersInfo usersInfo = appTenderCreditService.getUsersInfoByUserId(userId);
                UserInfoCustomize userInfo = this.appTenderCreditService.getUserInfoByUserId(String.valueOf(userId));
                if (userInfo != null) {
                    if(!checkCode(String.valueOf(userId), code)){
                        ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                        ret.put(CustomConstants.APP_STATUS_DESC, "验证码无效");
                        ret.put("resultUrl", "");
                        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
                        return JSONObject.toJSONString(ret, true);
                    }
                    Integer nowTime = GetDate.getNowTime10();// 获取当前时间
                    // 获取当前时间的日期
                    String nowDate = (GetDate.yyyyMMdd.format(new Date()) != null && !"".equals(GetDate.yyyyMMdd.format(new Date()))) ? GetDate.yyyyMMdd.format(new Date()) : "0";
                    Integer creditedNum = this.appTenderCreditService.tenderAbleToCredit(userId, Integer.parseInt(nowDate));
                    if (creditedNum != null && creditedNum >= 3) {
                        ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                        ret.put(CustomConstants.APP_STATUS_DESC, "今天的转让次数已满3次,请明天再试");
                        ret.put("resultUrl", "");
                        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
                        return JSONObject.toJSONString(ret, true);
                    }
                    // 验证BorrowNid和TenderNid
                    if (StringUtils.isEmpty(appTenderBorrowCreditCustomize.getBorrowNid()) || StringUtils.isEmpty(appTenderBorrowCreditCustomize.getTenderNid())) {
                        ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                        ret.put(CustomConstants.APP_STATUS_DESC, "无法获取债转借款编号和出借编号");
                        ret.put("resultUrl", "");
                        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
                        return JSONObject.toJSONString(ret, true);
                    }
                    // 根据BorrowNid和TenderNid判断,用户是否已经发起债转
                    BorrowCredit borrowCredit = this.appTenderCreditService.selectBorrowCreditByBorrowNid(appTenderBorrowCreditCustomize.getBorrowNid(), appTenderBorrowCreditCustomize.getTenderNid(),
                            userId+"");
                    if (borrowCredit != null) {
                        ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                        ret.put(CustomConstants.APP_STATUS_DESC, "债权转让中，请勿重复提交申请");
                        ret.put("resultUrl", "");
                        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
                        return JSONObject.toJSONString(ret, true);
                    }
                    if (!authService.checkPaymentAuthStatus(userId)) {
                        ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                        ret.put(CustomConstants.APP_STATUS_DESC, "您尚未进行服务费授权");
                        ret.put("resultUrl", "");
                        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
                        return JSONObject.toJSONString(ret, true);
                    }
                    // 验证折价率
                    //新增配置表校验
                    List<DebtConfig> config = debtConfigService.selectDebtConfigList();
                    if (StringUtils.isEmpty(appTenderBorrowCreditCustomize.getCreditDiscount())||CollectionUtils.isEmpty(config)) {
                        ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                        ret.put(CustomConstants.APP_STATUS_DESC, "折让率不能为空");
                        ret.put("resultUrl", "");
                        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
                        return JSONObject.toJSONString(ret, true);
                    } else {
                        if (appTenderBorrowCreditCustomize.getCreditDiscount().matches("\\d{1,2}\\.\\d{1}")) {
                            float creditDiscount = Float.parseFloat(appTenderBorrowCreditCustomize.getCreditDiscount());
                            DebtConfig debtConfig = config.get(0);
                            if (creditDiscount > debtConfig.getConcessionRateUp().floatValue() || creditDiscount < debtConfig.getConcessionRateDown().floatValue()) {
                                ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                                ret.put(CustomConstants.APP_STATUS_DESC, "折让率范围错误");
                                ret.put("resultUrl", "");
                                LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
                                return JSONObject.toJSONString(ret, true);
                            }
                        } else {
                            ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                            ret.put(CustomConstants.APP_STATUS_DESC, "折让率格式错误");
                            ret.put("resultUrl", "");
                            LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
                            return JSONObject.toJSONString(ret, true);
                        }
                    }
                    // 验证手机验证码
                    if (StringUtils.isEmpty(code)) {
                        ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                        ret.put(CustomConstants.APP_STATUS_DESC, "请输入手机验证码");
                        ret.put("resultUrl", "");
                        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
                        return JSONObject.toJSONString(ret, true);
                    }
                    // 债转保存
                    Integer tenderCredit = appTenderCreditService.insertTenderToCredit(userId, nowTime, appTenderBorrowCreditCustomize, platform);
                    if (tenderCredit != null && tenderCredit > 0) {

                        // add 合规数据上报 埋点 liubin 20181122 start
                        // 推送数据到MQ 转让 散
                        JSONObject params = new JSONObject();
                        params.put("creditNid", tenderCredit+"");
                        params.put("flag", "1"); //1（散）2（智投）
                        params.put("status", "0"); //0转让
                        this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.TRANSFER_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
                        // add 合规数据上报 埋点 liubin 20181122 end

                        logger.info("债转mq发送开始====="+appTenderBorrowCreditCustomize.getBorrowNid());
                        Borrow borrow = investProjectService.selectBorrowByBorrowNid(appTenderBorrowCreditCustomize.getBorrowNid());
                        boolean isMonth = CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle())
                                || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle())|| CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle());

                        String dayOrMonth ="";
                        String lockPeriod = String.valueOf(borrow.getBorrowPeriod());
                        if(isMonth){//月标
                            dayOrMonth = lockPeriod + "个月散标";
                        }else{
                            dayOrMonth = lockPeriod + "天散标";
                        }
                        UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
                        userOperationLogEntity.setOperationType(5);
                        userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
                        userOperationLogEntity.setPlatform(request.getParameter("realPlatform")==null?Integer.valueOf(platform):Integer.valueOf(request.getParameter("realPlatform")));
                        userOperationLogEntity.setRemark(dayOrMonth);
                        userOperationLogEntity.setOperationTime(new Date());
                        userOperationLogEntity.setUserName(users.getUsername());
                        userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
                        appUserService.sendUserLogMQ(userOperationLogEntity);
                        logger.info("债转mq发送结束====="+appTenderBorrowCreditCustomize.getBorrowNid());
                        ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
                        ret.put(CustomConstants.APP_STATUS_DESC, "保存成功");
                        ret.put("resultUrl", CustomConstants.HOST+AppTenderCreditBorrowDefine.REQUEST_HOME+
                                AppTenderCreditBorrowDefine.REQUEST_MAPPING+AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_RESULT+"?creditNid="+tenderCredit);
                        // 神策数据统计 add by liuyang 20180726 start
                        if (StringUtils.isNotBlank(presetProps)){
                            SensorsDataBean sensorsDataBean = new SensorsDataBean();
                            // 将json串转换成Bean
                            try {
                                Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
                                });
                                sensorsDataBean.setPresetProps(sensorsDataMap);
                                sensorsDataBean.setUserId(userId);
                                sensorsDataBean.setEventCode("submit_credit_assign");
                                sensorsDataBean.setCreditNid(String.valueOf(tenderCredit));
                                // 发送神策数据统计MQ
                                this.appTenderCreditService.sendSensorsDataMQ(sensorsDataBean);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // 神策数据统计 add by liuyang 20180726 end
                    } else {
                        ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                        ret.put(CustomConstants.APP_STATUS_DESC, "保存时出现异常,请联系客服");
                        ret.put("resultUrl", "");
                    }
                } else {
                    LogUtil.infoLog(this.getClass().getName(), "saveTenderToCredit", "用户未登录");
                    ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                    ret.put(CustomConstants.APP_STATUS_DESC, "用户未登录");
                    ret.put("resultUrl", "");
                }
            } else {
                LogUtil.infoLog(this.getClass().getName(), "saveTenderToCredit", "用户未登录");
                ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                ret.put(CustomConstants.APP_STATUS_DESC, "用户未登录");
                ret.put("resultUrl", "");
            }
        } catch (Exception e) {
            logger.error("债转提交保存异常...", e);
            // LogUtil.infoLog(this.getClass().getName(), "saveTenderToCredit", "系统异常");
            ret.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
            ret.put(CustomConstants.APP_STATUS_DESC, "系统异常");
            ret.put("resultUrl", "");
        }
        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_SAVE);
        return JSONObject.toJSONString(ret, true);
    }

    /**
     *
     * 提交债转后页面跳转
     *
     * @author liuyang
     * @param request
     * @param response
     * @param creditNid
     * @return
     */
    @RequestMapping(value = AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_RESULT)
    public ModelAndView tenderToCreditResult(HttpServletRequest request, HttpServletResponse response, @RequestParam String creditNid) {
        LogUtil.startLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_RESULT);
        ModelAndView modelAndView = new ModelAndView();
        if (StringUtils.isNotEmpty(creditNid)) {
            List<BorrowCredit> borrowCreditList = appTenderCreditService.selectBorrowCreditByNid(creditNid);
            if (borrowCreditList != null && borrowCreditList.size() > 0) {
                BorrowCredit creditTender = borrowCreditList.get(0);
                modelAndView.addObject("borrowCredit", creditTender);

                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "申请成功");
                baseMapBean.set("amount", creditTender.getCreditCapital().toString());
                baseMapBean.set("price", creditTender.getCreditPrice().toString());
                baseMapBean.set("endTime", GetDate.timestamptoStrYYYYMMDDHHMMSS(creditTender.getEndTime()+""));
                baseMapBean.setCallBackAction(CustomConstants.HOST+AppTenderCreditBorrowDefine.JUMP_HTML_SUCCESS_PATH.replace("{creditNid}", creditNid));
                modelAndView.addObject("callBackForm", baseMapBean);
            } else {

                modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
                BaseMapBean baseMapBean=new BaseMapBean();
                baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.SUCCESS);
                baseMapBean.set(CustomConstants.APP_STATUS_DESC, "获取债转信息失败");
                baseMapBean.setCallBackAction(CustomConstants.HOST+AppTenderCreditBorrowDefine.JUMP_HTML_ERROR_PATH.replace("{creditNid}", creditNid));
                modelAndView.addObject("callBackForm", baseMapBean);
            }
        }else{
            modelAndView = new ModelAndView(AutoDefine.JUMP_HTML);
            BaseMapBean baseMapBean=new BaseMapBean();
            baseMapBean.set(CustomConstants.APP_STATUS, BaseResultBeanFrontEnd.FAIL);
            baseMapBean.set(CustomConstants.APP_STATUS_DESC, "获取债转信息失败");
            baseMapBean.setCallBackAction(CustomConstants.HOST+AppTenderCreditBorrowDefine.JUMP_HTML_ERROR_PATH.replace("{creditNid}", creditNid));
            modelAndView.addObject("callBackForm", baseMapBean);
        }
        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.TENDER_TO_CREDIT_RESULT);
        return modelAndView;
    }



    /**
     *
     * 获取债权出借详情
     *
     * @author liuyang
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppTenderCreditBorrowDefine.PROJECT_DETAIL_ACTION)
    public JSONObject searchTenderCreditDetail(@PathVariable("borrowId") String borrowNid, HttpServletRequest request) {
        LogUtil.startLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.PROJECT_DETAIL_ACTION);
        JSONObject jsonObject = new JSONObject();
        String token = request.getParameter("token");
        String sign = request.getParameter("sign");
        String orderId = request.getParameter("orderId");
        // 优惠券类型，0代表本金出借
        String couponType = request.getParameter("couponType");
        // 如果不为空，为承接的标的
        String assignNid = request.getParameter("assignNid");
        // 如果不为空 为加息收益  并且=1
        String isIncrease = request.getParameter("isIncrease");
        // 还款日历里面点详情传入的是tender_nid  别的传的是ordid  加个字段区分一下  =1是还款日历的
        String isCalendar = request.getParameter("isCalendar");


        AppProjectDetailCustomize borrow = projectService.selectProjectDetail(borrowNid);

        jsonObject.put("projectName", borrow == null ? "" : borrow.getBorrowNid());
        jsonObject.put("couponType", "");
        /**
         * 验证用户是否登录
         */
        if (StringUtils.isEmpty(token)) {
            jsonObject.put("status", AppTenderCreditBorrowDefine.FAIL);
            jsonObject.put("statusDesc", AppTenderCreditBorrowDefine.FAIL_MSG);
            jsonObject.put("projectDetail", new ArrayList<>());
            jsonObject.put("repayPlan", new ArrayList<>());
            jsonObject.put("transferInfo", null);
            return jsonObject;
        }
        /**
         * 如果标不存在，则返回
         */
        if (borrow == null) {
            jsonObject.put("status", AppTenderCreditBorrowDefine.FAIL);
            jsonObject.put("statusDesc", AppTenderCreditBorrowDefine.FAIL_BORROW_MSG);
            jsonObject.put("projectName", "");
            jsonObject.put("projectDetail", new ArrayList<>());
            jsonObject.put("repayPlan", new ArrayList<>());
            jsonObject.put("transferInfo", null);
            return jsonObject;
        }

        //Integer userId = Integer.parseInt(request.getParameter("userId"));
        Integer userId = null;
        try {
            userId = SecretUtil.getUserId(sign);
        } catch (Exception e2) {
            logger.error("用户sign异常，sign:" + sign, e2);
            jsonObject.put("status", AppTenderCreditBorrowDefine.FAIL);
            jsonObject.put("statusDesc", "用户sign异常，sign:" + sign);
            jsonObject.put("projectName", "");
            jsonObject.put("projectDetail", new ArrayList<>());
            jsonObject.put("repayPlan", new ArrayList<>());
            jsonObject.put("transferInfo", null);
            return jsonObject;
        }

        // 跳转到智投服务协议
        jsonObject.put("isCredit", false);

        // 1. 资产信息
        List<TenderProjectDetailBean> detailBeansList = new ArrayList<>();
        List<TenderCreditBorrowBean> borrowBeansList = new ArrayList<>();
        // 如果加息的展示加息部分
        if (isIncrease != null && "1".equals(isIncrease)) {
            preckCredit(borrowBeansList, "历史年回报率", borrow.getBorrowExtraYield() + "%");
        }else{
            preckCredit(borrowBeansList, "历史年回报率", borrow.getBorrowApr() + "%");
        }

        if ("endday".equals(borrow.getBorrowStyle())) {
            preckCredit(borrowBeansList, "项目期限", borrow.getBorrowPeriod() + "天");
        } else {
            preckCredit(borrowBeansList, "项目期限", borrow.getBorrowPeriod() + "个月");
        }
        preckCredit(borrowBeansList, "回款方式", borrow.getRepayStyle());

        preck(detailBeansList, "资产信息", borrowBeansList);

        AppRiskControlCustomize riskControl = projectService.selectRiskControl(borrow.getBorrowNid());
        if(riskControl!=null){
			riskControl.setControlMeasures(riskControl.getControlMeasures()==null?"":riskControl.getControlMeasures().replace("\r\n", ""));
			riskControl.setControlMort(riskControl.getControlMort()==null?"":riskControl.getControlMort().replace("\r\n", ""));
        }
        jsonObject.put("riskControl", riskControl);
        // 加息收益
        if (isIncrease != null && "1".equals(isIncrease)) {
            List<TenderCreditBorrowBean> borrowBeansList3 = new ArrayList<>();
            IncreaseInterestInvest inc =  null;
            // 还款日历
            if (isCalendar != null && "1".equals(isCalendar)) {
                inc = appTenderCreditService.getIncreaseInterestInvestByTenderNid(orderId);
            }else{
                inc = appTenderCreditService.getIncreaseInterestInvestByOrdId(orderId);
            }

            if (inc != null) {
                // 2. 出借信息 ( 有真实资金，显示出借信息 )
                this.setTenderInfoToResult(detailBeansList, inc.getTenderNid());
                jsonObject.put("couponType", "加息"+borrow.getBorrowExtraYield() + "%");
                jsonObject.put("couponTypeCode","4");
               /* jsonObject.put("couponType", "加息"+inc.getBorrowExtraYield()+"%");
                preckCredit(borrowBeansList3, "待收利息", CommonUtils.formatAmount(inc.getRepayInterestWait()) + "元");
                preckCredit(borrowBeansList3, "已收利息", CommonUtils.formatAmount(inc.getRepayInterestYes()) + "元");
                preckCredit(borrowBeansList3, "待收总额", CommonUtils.formatAmount(inc.getRepayInterest()) + "元");
                preck(detailBeansList, "加息信息", borrowBeansList3);*/
                // 3.回款计划  加息的  二期做
                /*this.setIncreaseRepayPlanByStagesToResult(jsonObject, orderId);*/
            }
        }else if (!"0".equals(couponType)) {
         // 区别本金出借和优惠券出借，返回值不同
            AppCouponInfoCustomize appCouponInfoCustomize = projectService.getCouponfigByUserIdAndBorrowNid(userId, orderId);
            if (appCouponInfoCustomize != null) {
                if(StringUtils.isNotBlank(appCouponInfoCustomize.getRealOrderId())){
                    // 2. 出借信息 ( 有真实资金，显示出借信息 )
                    this.setTenderInfoToResult(detailBeansList, appCouponInfoCustomize.getRealOrderId());
                }
                List<TenderCreditBorrowBean> borrowBeansList2 = new ArrayList<>();
                switch (appCouponInfoCustomize.getCouponType()) {
                case "1":
                    jsonObject.put("couponType", "体验金");
                    jsonObject.put("couponTypeCode",appCouponInfoCustomize.getCouponType());
                    preckCredit(borrowBeansList2, "优惠券面额", CommonUtils.formatAmount(new BigDecimal(appCouponInfoCustomize.getCouponQuota())) + "元");
                    preckCredit(borrowBeansList2, "优惠券类型", "体验金");
                    preckCredit(borrowBeansList2, "待收利息", CommonUtils.formatAmount(new BigDecimal(appCouponInfoCustomize.getRecoverAccountInterestWait())) + "元");
                    break;
                case "2":
                    jsonObject.put("couponType", "加息券");
                    jsonObject.put("couponTypeCode",appCouponInfoCustomize.getCouponType());
                    preckCredit(borrowBeansList2, "优惠券面额", appCouponInfoCustomize.getCouponQuota() + "%");
                    preckCredit(borrowBeansList2, "优惠券类型", "加息券");
                    preckCredit(borrowBeansList2, "待收利息", CommonUtils.formatAmount(new BigDecimal(appCouponInfoCustomize.getRecoverAccountInterestWait())) + "元");
                    break;
                case "3":
                    jsonObject.put("couponType", "代金券");
                    jsonObject.put("couponTypeCode",appCouponInfoCustomize.getCouponType());
                    preckCredit(borrowBeansList2, "优惠券面额", CommonUtils.formatAmount(new BigDecimal(appCouponInfoCustomize.getCouponQuota())) + "元");
                    preckCredit(borrowBeansList2, "优惠券类型", "代金券");
					preckCredit(borrowBeansList2, "待收利息",
                            CommonUtils.formatAmount(new BigDecimal(appCouponInfoCustomize.getRecoverAccountInterestWait()).subtract(
                                    new BigDecimal(appCouponInfoCustomize.getRecoverAccountCapitalWait()))) + "元");
                    break;
                default:
                    logger.error("coupon type is error");
                }

                preckCredit(borrowBeansList2, "待收本金", CommonUtils.formatAmount(new BigDecimal(appCouponInfoCustomize.getRecoverAccountCapitalWait())) + "元");
                preckCredit(borrowBeansList2, "待收总额", CommonUtils.formatAmount(new BigDecimal(appCouponInfoCustomize.getRecoverAaccountWait())) + "元");
                preck(detailBeansList, "优惠券信息", borrowBeansList2);
            } else {
                logger.error("未查询到优惠券信息...");
                preck(detailBeansList, "优惠券信息", new ArrayList<TenderCreditBorrowBean>());
                jsonObject.put("couponType", "体验金");
                jsonObject.put("couponTypeCode",appCouponInfoCustomize.getCouponType());
            }

            // 3. 优惠券回款计划
            this.setCouponRepayPlanToResult(jsonObject, orderId);
        } else {
            // 这里要区别是普通出借 还是 承接债转
            logger.info("债转编号： {}, 空代表普通出借，否则为承接债转...,出借订单号: {}", assignNid, orderId);
            if(StringUtils.isBlank(assignNid)){
                // 2. 出借信息(本金出借)
                this.setTenderInfoToResult(detailBeansList, orderId);

                if(CommonUtils.isStageRepay(borrow.getBorrowStyle())){
                    // 3.回款计划(本金出借 - 分期)
                    this.setRepayPlanByStagesToResult(jsonObject, orderId);
                } else {
                    // 3.回款计划(本金出借 - 不分期)
                    this.setRepayPlanToResult(jsonObject, orderId);
                }
            } else {
                CreditTender creditTender = projectService.selectCreditTender(assignNid);
				if (creditTender != null) {
					// 2. 出借信息(承接标的出借信息)
					this.setCreditTenderInfoToResult(detailBeansList, creditTender);
					// 3.回款计划(承接标的)
					this.setCreditRepayPlanByStagesToResult(jsonObject, orderId);
					// 跳转到债转协议
					this.setCreditUrlValue(jsonObject, creditTender);
				}

            }

        }
        jsonObject.put("projectDetail", detailBeansList);
        // 4. 转让信息
        if (orderId != null) {
            if (isIncrease == null || "".equals(isIncrease)) {
                List<BorrowCredit> borrowCreditList = projectService.getBorrowList(orderId,userId);
                JSONArray jsonArray = new JSONArray();
                 for (BorrowCredit borrowCredit : borrowCreditList) {
                    Integer creditNid = borrowCredit.getCreditNid();
                    JSONObject js = new JSONObject();
                    js.put("date", GetDate.times10toStrYYYYMMDD(borrowCredit.getAddTime()));
                    js.put("transferPrice", CommonUtils.formatAmount(borrowCredit.getCreditCapital()));
                    js.put("discount", CommonUtils.formatAmount(borrowCredit.getCreditDiscount()));
                    js.put("remainTime", borrowCredit.getCreditTerm());
                    js.put("realAmount", CommonUtils.formatAmount(borrowCredit.getCreditPrice()));
                    js.put("hadTransfer", CommonUtils.formatAmount(borrowCredit.getCreditCapitalAssigned()));
                    js.put("serviceCharge",CommonUtils.formatAmount(this.projectService.getCreditTender(String.valueOf(creditNid))));
                    jsonArray.add(js);
                }
                jsonObject.put("transferInfo", jsonArray);
            }
        } else {
            jsonObject.put("transferInfo", null);
        }
        //增加返回值，用于前端判断调到那个协议
        if (Validator.isIncrease(borrow.getIncreaseInterestFlag(),
                new BigDecimal(borrow.getBorrowExtraYield() == null ? "0" : borrow.getBorrowExtraYield()))) {
        	//如果是13表示是融通宝，调到融通宝的相关协议
        	/*jsonObject.put("isPreferred", true);
        	if(borrow.getBorrowPublisher().equalsIgnoreCase("嘉诺")){
        		jsonObject.put("publisher", 2);
        	}else{
        		jsonObject.put("publisher", 1);
        	}*/
            // 产品说融通宝不显示协议
        }else{
        	//调到居间服务协议
        	jsonObject.put("isPreferred", false);
        }
        List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(orderId+"");//居间协议
        if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
            TenderAgreement tenderAgreement = tenderAgreementsNid.get(0);
            Integer fddStatus = tenderAgreement.getStatus();
            //法大大协议生成状态：0:初始,1:成功,2:失败，3下载成功
            //System.out.println("计划详情的接口参数******************1法大大协议状态："+tenderAgreement.getStatus());
            if(fddStatus.equals(3)){
                jsonObject.put("fddStatus", 1);
            }else {
                //隐藏下载按钮
                //System.out.println("计划详情的接口参数******************2法大大协议状态：0");
                jsonObject.put("fddStatus", 0);
            }
        }else {
            //下载老版本协议
            //System.out.println("计划详情的接口参数******************3法大大协议状态：2");
            jsonObject.put("fddStatus", 1);;
        }
        jsonObject.put("status", AppTenderCreditBorrowDefine.SUCCESS);
        jsonObject.put("statusDesc", AppTenderCreditBorrowDefine.SUCCESS_MSG);
        // 获取债转详细的参数
        LogUtil.endLog(AppTenderCreditBorrowDefine.THIS_CLASS, AppTenderCreditBorrowDefine.PROJECT_DETAIL_ACTION);
        return jsonObject;
    }

    /**
     *
     * 产品加息还款计划
     * @author sunss
     * @param jsonObject
     * @param orderId
     */
	private void setIncreaseRepayPlanByStagesToResult(JSONObject jsonObject, String orderId,String borrowStyle) {
	    //二期实现  还款计划
	    /* RepaymentPlanAjaxBean bean = new RepaymentPlanAjaxBean();
	    if(CommonUtils.isStageRepay(borrowStyle)){
            // 3.回款计划(本金出借 - 分期)
            this.assetManageService.rtbRepaymentPlanStages(borrowStyle);
        } else {
            // 3.回款计划(本金出借 - 不分期)
            this.assetManageService.rtbRepaymentPlanNoStages();
        }*/
    }



    /**
	 * 本金出借还款计划 - 不分期
	 * @param result
	 * @param userId
	 * @param borrowNid
	 */
	private void setRepayPlanToResult(JSONObject result, String orderId) {
		BorrowRecover borrowRecover = projectService.selectBorrowRecoverByNid(orderId);
		JSONArray jsonArray = new JSONArray();
		if (borrowRecover != null) {
			JSONObject js = new JSONObject();
			js.put("time", GetDate.times10toStrYYYYMMDD(Integer.parseInt(borrowRecover.getRecoverTime())));
			js.put("number", "1");
			js.put("account", CommonUtils.formatAmount(borrowRecover.getRecoverAccount()));
			if (borrowRecover.getRecoverStatus() == 1) {
			    js.put("status", "已回款");
			} else {
			    js.put("status", "未回款");
			}
			jsonArray.add(js);
		}
		result.put("repayPlan", jsonArray);
	}

	/**
	 * 本金出借还款计划 - 分期
	 * @param result
	 * @param userId
	 * @param borrowNid
	 */
	private void setRepayPlanByStagesToResult(JSONObject result, String orderId) {
		List<BorrowRecoverPlan> recoverPlanList = projectService.selectBorrowRecoverPlanByNid(orderId);
		JSONArray jsonArray = new JSONArray();
		if (!CollectionUtils.isEmpty(recoverPlanList)) {
			for (BorrowRecoverPlan plan : recoverPlanList) {
				JSONObject js = new JSONObject();
				js.put("time", GetDate.times10toStrYYYYMMDD(Integer.parseInt(plan.getRecoverTime())));
				js.put("number", plan.getRecoverPeriod());
				js.put("account", CommonUtils.formatAmount(plan.getRecoverAccount()));
				if (plan.getRecoverStatus() == 1) {
				    js.put("status", "已回款");
				} else {
				    js.put("status", "未回款");
				}
				jsonArray.add(js);
			}
		}
		result.put("repayPlan", jsonArray);
	}

	/**
	 * 优惠券出借还款计划
	 *
	 * @param result
	 * @param orderId
	 */
	private void setCouponRepayPlanToResult(JSONObject result, String orderId) {
		List<CurrentHoldRepayMentPlanListCustomize> repaymentPlanList = myPlanService.getPlanCouponRepayment(orderId);

		JSONArray jsonArray = new JSONArray();
		if (!CollectionUtils.isEmpty(repaymentPlanList)) {

			// 体验金只有一期还款，但是期数是第三期，强制改成1
			if (repaymentPlanList.size() == 1) {
				CurrentHoldRepayMentPlanListCustomize entity = repaymentPlanList.get(0);
				JSONObject js = new JSONObject();
				js.put("time", entity.getRecoverTime());
				js.put("number", "1");
				js.put("account", entity.getRecoverAccountWait());
				js.put("status", entity.getRecoveStatus());
				jsonArray.add(js);
			} else {
				for (CurrentHoldRepayMentPlanListCustomize entity : repaymentPlanList) {
					JSONObject js = new JSONObject();
					js.put("time", entity.getRecoverTime());
					js.put("number", entity.getRecoverPeriod());
					js.put("account", entity.getRecoverAccountWait());
					js.put("status", entity.getRecoveStatus());
					jsonArray.add(js);
				}
			}
		}
		result.put("repayPlan", jsonArray);
	}

	private void preck(List<TenderProjectDetailBean> jsonObject,String keyName,List<TenderCreditBorrowBean> msg){
		TenderProjectDetailBean detailBean = new TenderProjectDetailBean();
		detailBean.setId("");
		detailBean.setTitle(keyName);
		detailBean.setMsg(msg);
		jsonObject.add(detailBean);
	}

	/**
	 * 封装TenderCreditBorrowBean对象，放入list中
	 * @param borrowBeansList
	 * @param key 字段名
	 * @param val 字段值
	 */
	private void preckCredit(List<TenderCreditBorrowBean> borrowBeansList,String key, String val){
		if(!StringUtils.isEmpty(key)){
			TenderCreditBorrowBean borrowBean = new TenderCreditBorrowBean();
			borrowBean.setId("");
			borrowBean.setKey(key);
			borrowBean.setVal(val);
			borrowBeansList.add(borrowBean);
		}
	}

    /**
     * 出借信息
     * @param detailBeansList
     * @param orderId
     */
    private void setTenderInfoToResult(List<TenderProjectDetailBean> detailBeansList, String orderId) {
        // 2. 出借信息(本金出借)
        BorrowTender borrowTender = projectService.selectBorrowTender(orderId);
        if (borrowTender != null) {
            List<TenderCreditBorrowBean> borrowBeansList1 = new ArrayList<>();
            preckCredit(borrowBeansList1, "出借本金", CommonUtils.formatAmount(borrowTender.getAccount()) + "元");
            preckCredit(borrowBeansList1, "已收本息", CommonUtils.formatAmount(borrowTender.getRecoverAccountYes()) + "元");
            String borrowNid = borrowTender.getBorrowNid();
            AccountBorrow accountBorrow = projectService.selectAccountBorrow(borrowNid);
            if (accountBorrow == null) {
                preckCredit(borrowBeansList1, "待收本金", "--");
                preckCredit(borrowBeansList1, "待收利息", "--");
            } else {
                preckCredit(borrowBeansList1, "待收本金", CommonUtils.formatAmount(borrowTender.getRecoverAccountCapitalWait()) + "元");
                preckCredit(borrowBeansList1, "待收利息", CommonUtils.formatAmount(borrowTender.getRecoverAccountInterestWait()) + "元");
            }

            if (borrowTender.getAddtime() != null) {
                preckCredit(borrowBeansList1, "出借时间", GetDate.timestamptoStrYYYYMMDDHHMM(String.valueOf(borrowTender.getAddtime())));
            } else {
                preckCredit(borrowBeansList1, "出借时间", "");
            }

            preck(detailBeansList, "出借信息", borrowBeansList1);
        } else {
            preck(detailBeansList, "出借信息", new ArrayList<TenderCreditBorrowBean>());
        }
    }


    /**
     * 出借信息 - 承接标
     * @param detailBeansList
     * @param assignNid
     */
    private void setCreditTenderInfoToResult(List<TenderProjectDetailBean> detailBeansList, CreditTender creditTender) {
        // 2. 出借信息(本金出借)
        CreditRepay creditRepay = projectService.selectCreditRepayList(creditTender.getAssignNid()).get(0);
        if (creditTender != null) {
            List<TenderCreditBorrowBean> borrowBeansList1 = new ArrayList<>();
            preckCredit(borrowBeansList1, "出借本金", CommonUtils.formatAmount(creditTender.getAssignCapital()) + "元");
            preckCredit(borrowBeansList1, "已收本息", CommonUtils.formatAmount(creditRepay.getAssignRepayAccount()) + "元");
            preckCredit(borrowBeansList1, "待收本金", CommonUtils.formatAmount(creditRepay.getAssignCapital().subtract(creditRepay.getAssignRepayCapital())) + "元");
            if (creditRepay.getStatus() != 0){//已回款
                preckCredit(borrowBeansList1, "待收利息", "0.00元");
            } else {
                preckCredit(borrowBeansList1, "待收利息", CommonUtils.formatAmount(creditTender.getAssignInterest()) + "元");
            }
            preckCredit(borrowBeansList1, "出借时间", GetDate.timestamptoStrYYYYMMDDHHMM(creditTender.getAddTime()));
            preck(detailBeansList, "出借信息", borrowBeansList1);
        } else {
            preck(detailBeansList, "出借信息", new ArrayList<TenderCreditBorrowBean>());
        }
    }

    /**
     * 债转出借还款计划
     * @param result
     * @param userId
     * @param borrowNid
     */
    private void setCreditRepayPlanByStagesToResult(JSONObject result, String assignNid) {
        List<CreditRepay> creditRepays = projectService.selectCreditRepayList(assignNid);
        JSONArray jsonArray = new JSONArray();
        if (!CollectionUtils.isEmpty(creditRepays)) {
            for (CreditRepay creditRepay : creditRepays) {
                JSONObject js = new JSONObject();
                js.put("number", creditRepay.getAssignRepayPeriod());
                js.put("account", CommonUtils.formatAmount(creditRepay.getAssignAccount()));
                if (creditRepay.getStatus() == 0) {
                    js.put("status", "未回款");
                } else {
                    js.put("status", "已回款");
                }
                js.put("time", GetDate.times10toStrYYYYMMDD(creditRepay.getAssignRepayNextTime()));
                jsonArray.add(js);
            }
        }
        result.put("repayPlan", jsonArray);
    }


    private void setCreditUrlValue(JSONObject result,CreditTender creditTender){
        result.put("isCredit", true);
        // 跳债转协议需要的字段
        // 原标nid
        result.put("bidNid", creditTender.getBidNid());
        // 债转标号
        result.put("creditNid", creditTender.getCreditNid());
        // 债转投标单号
        result.put("creditTenderNid", creditTender.getCreditTenderNid());
        // 认购单号
        result.put("assignNid", creditTender.getAssignNid());
    }

}
