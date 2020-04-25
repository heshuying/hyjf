package com.hyjf.web.user.login;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.user.loginerror.LoginErrorConfigManager;
import com.alibaba.fastjson.TypeReference;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.http.URLCodec;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.ThreeDESUtils;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseController;
import com.hyjf.web.bank.web.borrow.BorrowDefine;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.home.HomePageDefine;
import com.hyjf.web.plan.PlanDefine;
import com.hyjf.web.user.pandect.PandectDefine;
import com.hyjf.web.util.WebUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * LoginController
 * </p>
 *
 * @author gogtz
 * @version 1.0.0
 */
@Controller
@RequestMapping(LoginDefine.CONTROLLOR_REQUEST_MAPPING)
public class LoginController extends BaseController {

    Logger _log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    LoginService loginService;

    /**
     * 初期化,跳转到登录页面
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = LoginDefine.INIT, method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request) throws Exception {
        LogUtil.startLog(LoginController.class.getName(), LoginDefine.INIT);
        ModelAndView modeAndView = new ModelAndView(LoginDefine.INIT_PATH);
        if (RedisUtils.get(LoginDefine.CONTROLLOR_CLASS_NAME + "_loginvalidCode_" + request.getSession().getId()) == null) {
            RedisUtils.set(LoginDefine.CONTROLLOR_CLASS_NAME + "_loginvalidCode_" + request.getSession().getId(), "1",
                    10 * 60);
            // 不显示验证码
            modeAndView.addObject("hasValid", false);
        } else {
            // 显示验证码
            modeAndView.addObject("hasValid", true);
        }
        LogUtil.endLog(LoginController.class.getName(), LoginDefine.INIT);
        return modeAndView;
    }

    /**
     * 登录
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = LoginDefine.LOGIN, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public JSONObject login(HttpServletRequest request, HttpServletResponse response, LoginBean loginBean)
        throws Exception {
        LogUtil.startLog(LoginController.class.getName(), LoginDefine.LOGIN);
        ModelAndView modelAndView = new ModelAndView(LoginDefine.INIT_PATH);
        RedisUtils.set(LoginDefine.CONTROLLOR_CLASS_NAME + "_loginvalidCode_" + request.getSession().getId(), "1",10 * 60);
        JSONObject resultJson = new JSONObject();
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "loginUserName", loginBean.getLoginUserName());
        ValidatorFieldCheckUtil.validateRequired(modelAndView, "loginPassword", loginBean.getLoginPassword());
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "loginUserName", loginBean.getLoginUserName(), 16, true);
        ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "loginPassword", loginBean.getLoginPassword(), 32, true);

        if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
            resultJson.put(LoginDefine.STATUS, LoginDefine.STATUS_FALSE);
            resultJson.put(LoginDefine.ERROR, "账号或密码错误");
            return resultJson;
        }

        Integer result = loginService.updateLoginInAction2(loginBean.getLoginUserName(), loginBean.getLoginPassword(),CustomUtil.getIpAddr(request));
        switch (result) {
        case -1:
            resultJson.put(LoginDefine.STATUS, LoginDefine.STATUS_FALSE);
            resultJson.put(LoginDefine.ERROR, "登录失败,账号或密码错误");// 登录失败,用户不存在
            break;
        case -2:
            resultJson.put(LoginDefine.STATUS, LoginDefine.STATUS_FALSE);
            resultJson.put(LoginDefine.ERROR, "登录失败,存在多个相同用户");
            break;
        case -3:
            Integer userId= loginService.getUser(loginBean.getLoginUserName());
        	long retValue  = Long.parseLong(RedisUtils.get(RedisConstants.PASSWORD_ERR_COUNT_ALL+userId));
        	Integer maxLoginErrorNum=LoginErrorConfigManager.getInstance().getWebConfig().getMaxLoginErrorNum();
        	long reNumber=0;//输错密码剩余次数变量
        	reNumber=maxLoginErrorNum-retValue;
            resultJson.put(LoginDefine.STATUS, LoginDefine.STATUS_FALSE);
            if(reNumber<=3){
            	resultJson.put(LoginDefine.ERROR, "登录失败，您的登录机会还剩<span style='color: red;'>"+reNumber+"</span>次");
            }else{
            	resultJson.put(LoginDefine.ERROR, "登录失败,账号或密码错误");
            }
            break;
        case -4:
            resultJson.put(LoginDefine.STATUS, LoginDefine.STATUS_FALSE);
            resultJson.put(LoginDefine.ERROR, "抱歉，您的账户已被禁用，如有疑问请联系客服");
            break;
        case -5:
            Integer userId1= loginService.getUser(loginBean.getLoginUserName());
        	long retTime  = RedisUtils.ttl(RedisConstants.PASSWORD_ERR_COUNT_ALL+userId1);
            resultJson.put(LoginDefine.STATUS, LoginDefine.STATUS_FALSE);
            resultJson.put(LoginDefine.ERROR, "您的登录失败次数超限，请"+DateUtils.SToHMSStr(retTime)+"之后重试");
            break;
        case -6:
            resultJson.put(LoginDefine.STATUS, LoginDefine.STATUS_FALSE);
            resultJson.put(LoginDefine.ERROR, "存在恶意登录嫌疑，该IP已封");
            break;
        default:
            WebViewUser webUser = loginService.getWebViewUserByUserId(result);
            WebUtils.sessionLogin(request, response, webUser);
            _log.info("登录成功发送记录用户日志MQ开始");
            UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
            userOperationLogEntity.setOperationType(1);
            userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
            userOperationLogEntity.setPlatform(0);
            userOperationLogEntity.setRemark("");
            userOperationLogEntity.setOperationTime(new Date());
            userOperationLogEntity.setUserName(webUser.getUsername());
            userOperationLogEntity.setUserRole(webUser.getRoleId());
            userOperationLogEntity.setUserType(webUser.getUserType());
            loginService.sendUserLogMQ(userOperationLogEntity);
            _log.info("登录成功发送记录用户日志MQ结束");
            /***********登录时自动同步线下充值记录  pcc  start***********/
            if(webUser.isBankOpenAccount()){
                CommonSoaUtils.synBalance(webUser.getUserId());
            }
            /***********登录时自动同步线下充值记录  pcc  end***********/
            //如果登录成功就将登陆密码错误次数的key删除
            RedisUtils.del(RedisConstants.PASSWORD_ERR_COUNT_ALL+result);

            resultJson.put(LoginDefine.STATUS, LoginDefine.STATUS_TRUE);
            resultJson.put(LoginDefine.INFO, "登录成功");
            // add by zhangjp 支持登录完成后跳转回原页面 20161014 start
            String redirectUrl = WebUtils.getCookie(request, "redirectUrl");

            if (StringUtils.isNotEmpty(redirectUrl)) {
                String retUrl = URLCodec.decodeURL(redirectUrl);
                retUrl = StringUtils.remove(retUrl, "/hyjf-web");
                resultJson.put("retUrl", retUrl);
            }
            // add by zhangjp 支持登录完成后跳转回原页面 20161014 end

            // add by liuyang 神策数据统计追加 登录成功后 将用户ID返回前端 20180717 start
            resultJson.put("userid", webUser.getUserId());
            // 登录成功
            String presetProps = loginBean.getPresetProps();
            if (StringUtils.isNotBlank(presetProps)) {
                try {
                    SensorsDataBean sensorsDataBean = new SensorsDataBean();
                    // 将json串转换成Bean
                    Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
                    });
                    sensorsDataBean.setPresetProps(sensorsDataMap);
                    sensorsDataBean.setUserId(webUser.getUserId());
                    // 发送神策数据统计MQ
                    this.loginService.sendSensorsDataMQ(sensorsDataBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // add by liuyang 神策数据统计追加 登录成功后 将用户ID返回前端 20180717 end
            break;
        }
        LogUtil.endLog(LoginController.class.getName(), LoginDefine.LOGIN);
        return resultJson;
    }

    /**
     * 登出
     *
     * @param request
     * @param response
     * @param loginBean
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = LoginDefine.LOGINOUT)
    public ModelAndView loginout(HttpServletRequest request, HttpServletResponse response, LoginBean loginBean)
        throws Exception {
        LogUtil.startLog(LoginController.class.getName(), LoginDefine.LOGINOUT);
        ModelAndView modelAndView = new ModelAndView("index");// HomePageDefine.HOME_ACTION
                                                              // bug1620: 账号退出
                                                              // 退出到首页
                                                              // 不是退出到登陆界面
        LogUtil.endLog(LoginController.class.getName(), LoginDefine.LOGINOUT);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = LoginDefine.CHECKUSER, method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public boolean checkUserAction(ModelAndView modelAndView, HttpServletRequest request) {
        String username = request.getParameter("username");
        if (Validator.isNull(username)) {
            return false;
        } else {
            // 验证是否存在账号
            if (!loginService.existUser(username)) {
                return false;
            }
            return true;
        }
    }

    private static String KEY = PropUtils.getSystem("hyjf.3des.key").trim();

    /**
     * 为php开发的第三方登录
     *
     * @param modelAndView
     * @param request
     * @return
     */
    @RequestMapping(value = LoginDefine.LOGIN_THREEPART)
    public ModelAndView threepartLogin(ModelAndView modelAndView, HttpServletRequest request,
        HttpServletResponse response) {
        String username = request.getParameter("username");
        String jtimestamp = request.getParameter("jtimestamp");
        String bid = request.getParameter("bid");
        String kkey = KEY + jtimestamp;
        JSONObject resultJson = new JSONObject();

        if (request.getMethod().equalsIgnoreCase("post")) {
            try {
                username = URLDecoder.decode(username, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
                resultJson.put("error", "1");
                resultJson.put("data", "登录失败,参数1错误");
                return new ModelAndView("redirect:" + CustomConstants.HOST + HomePageDefine.REQUEST_MAPPING
                        + HomePageDefine.HOME_ACTION + ".do");
            }

        }
        if (StringUtils.isNotEmpty(username)) {
            if (username.contains("%")) {
                try {
                    username = URLDecoder.decode(username, "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    resultJson.put("error", "1");
                    resultJson.put("data", "登录失败,参数2错误");
                    return new ModelAndView("redirect:" + CustomConstants.HOST + HomePageDefine.REQUEST_MAPPING
                            + HomePageDefine.HOME_ACTION + ".do");
                }
            }

            try {
                username = ThreeDESUtils.Decrypt3DES(kkey, username);
                if (StringUtils.isBlank(username)) {
                    resultJson.put("error", "1");
                    resultJson.put("data", "登录失败,参数3错误");
                    return new ModelAndView("redirect:" + CustomConstants.HOST + HomePageDefine.REQUEST_MAPPING
                            + HomePageDefine.HOME_ACTION + ".do");
                } else {
                    int userid = loginService.getUserIdByUsername(username);
                    if (userid == -1) {
                        resultJson.put("error", "1");
                        resultJson.put("data", "登录失败,不存在用户");
                        return new ModelAndView("redirect:" + CustomConstants.HOST + HomePageDefine.REQUEST_MAPPING
                                + HomePageDefine.HOME_ACTION + ".do");
                    }
                    if (userid == -2) {
                        resultJson.put("error", "1");
                        resultJson.put("data", "登录失败,存在多个相同用户");
                        return new ModelAndView("redirect:" + CustomConstants.HOST + HomePageDefine.REQUEST_MAPPING
                                + HomePageDefine.HOME_ACTION + ".do");
                    } else {
                        WebViewUser webUser = loginService.getWebViewUserByUserId(userid);
                        WebUtils.sessionLogin(request, response, webUser);
                        resultJson.put("error", "0");
                        resultJson.put("data", "登录成功");
                        if (StringUtils.isNotBlank(bid)) {
                            if (webUser.isOpenAccount()) {
                                if ("home".equals(bid)) {
                                    // 如果bid参数等于‘home’字段，跳转到首页，反之跳转到标的详情页
                                    return new ModelAndView("redirect:" + CustomConstants.HOST
                                            + HomePageDefine.REQUEST_MAPPING + HomePageDefine.HOME_ACTION + ".do");
                                } // 开户,标的详情
                                else {
                                    // plan表查询
                                    DebtPlan plan = this.loginService.queryPlanByNid(bid);
                                    if (null != plan) {
                                        return new ModelAndView("redirect:" + CustomConstants.HOST
                                                + PlanDefine.REQUEST_MAPPING + PlanDefine.PLAN_DETAIL_ACTION
                                                + ".do?planNid=" + bid);
                                    } else {
                                        return new ModelAndView("redirect:" + CustomConstants.HOST
                                                + BorrowDefine.REQUEST_MAPPING + "/"
                                                + BorrowDefine.BORROW_DETAIL_ACTION + ".do?borrowNid=" + bid);
                                    }
                                }
                            } else {
                                // 不开户,账户总览
                                return new ModelAndView("redirect:" + CustomConstants.HOST
                                        + PandectDefine.REQUEST_MAPPING + PandectDefine.PANDECT_ACTION + ".do");
                            }
                        } else {
                            return new ModelAndView("redirect:" + CustomConstants.HOST + PandectDefine.REQUEST_MAPPING
                                    + PandectDefine.PANDECT_ACTION + ".do");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultJson.put("error", "1");
                resultJson.put("data", "登录失败,参数5错误");
                return new ModelAndView("redirect:" + CustomConstants.HOST + HomePageDefine.REQUEST_MAPPING
                        + HomePageDefine.HOME_ACTION + ".do");
            }
        } else {
            return new ModelAndView("redirect:" + CustomConstants.HOST + HomePageDefine.REQUEST_MAPPING
                    + HomePageDefine.HOME_ACTION + ".do");
        }
    }
}
