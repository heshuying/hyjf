package com.hyjf.wechat.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.RSAJSPUtil;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.base.BaseResultBean;
import com.hyjf.wechat.service.login.LoginService;
import com.hyjf.wechat.util.AppUserToken;
import com.hyjf.wechat.util.ResultEnum;
import com.hyjf.wechat.util.SecretUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 *
 * 登录 退出功能
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 上午10:20:44
 */
@Controller(LoginDefine.CONTROLLER_NAME)
@RequestMapping(value = LoginDefine.REQUEST_MAPPING)
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;

    Logger _log = LoggerFactory.getLogger(LoginController.class);

    /**
     *
     * 登录接口
     * 请求地址:/wx/login/doLogin.do
     * 需要参数 userName  password
     * 返回 sign
     * @author sunss
     * @param userName
     * @param password
     * @param env 登陆来源，处理跳转页面时需要跳转的H5环境 update by limeng 2018-04-16
     * @return
     */
    @ResponseBody
    @RequestMapping(value = LoginDefine.DOLOGIN_MAPPING, method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public BaseResultBean doLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam String userName, @RequestParam String password,
                                  @RequestParam(value = "env", defaultValue = "") String env) {
        LogUtil.startLog(LoginController.class.getName(), LoginDefine.DOLOGIN_MAPPING);
        // 从payload里面获取预置属性
        String presetProps = getStringFromStream(request);
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        _log.info("用户userName："+userName+"登陆来源:"+tempContextUrl);
        LoginResultBean result = new LoginResultBean();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return result.setEnum(ResultEnum.PARAM);
        }
        // 现只支持两个参数  1微信  2风车理财
        if (!"1".equals(env) && !"2".equals(env)) {
            return result.setEnum(ResultEnum.PARAM);
        }
        //密码解密
        password = RSAJSPUtil.rsaToPassword(password);
        int userId = loginService.updateLoginInAction(userName, password, CustomUtil.getIpAddr(request));

        switch (userId) {
            case -1:
                result.setEnum(ResultEnum.ERROR_003);
                break;
            case -2:
                result.setEnum(ResultEnum.ERROR_002);
                break;
            case -3:
                result.setEnum(ResultEnum.ERROR_003);
                break;
            case -4:
                result.setEnum(ResultEnum.ERROR_043);
                break;
            case -5:
                result.setEnum(ResultEnum.ERROR_046);
                break;
            default:
                Users users = loginService.getUsers(userId);
                UsersInfo usersInfo = loginService.getUsersInfoByUserId(userId);
                UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
                userOperationLogEntity.setOperationType(1);
                userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
                userOperationLogEntity.setPlatform(1);
                userOperationLogEntity.setRemark("");
                userOperationLogEntity.setOperationTime(new Date());
                userOperationLogEntity.setUserName(users.getUsername());
                userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
                userOperationLogEntity.setUserType(users.getUserType());
                loginService.sendUserLogMQ(userOperationLogEntity);
                BankOpenAccount account = loginService.getBankOpenAccount(userId);
                String accountId = null;
                if (account != null && StringUtils.isNoneBlank(account.getAccount())) {
                    accountId = account.getAccount();
                    /*********** 登录时自动同步线下充值记录 start ***********/
                    if (users.getBankOpenAccount() == 1) {
                        CommonSoaUtils.synBalance(users.getUserId());
                    }
                    /*********** 登录时自动同步线下充值记录 end ***********/
                }
                String sign = SecretUtil.createToken(userId, users.getUsername(), accountId);
                if (StringUtils.isNotBlank(env)) {
                    //登录成功之后风车理财的特殊标记，供后续出借使用
                    RedisUtils.del("loginFrom"+userId);
                    RedisUtils.set("loginFrom"+userId, env, 1800);
                }
                //登录成功就将登陆密码错误次数的key删除
                RedisUtils.del(RedisConstants.PASSWORD_ERR_COUNT_ALL+userId);
                // 输出登录来源
                _log.info("loginFrom:"+RedisUtils.get("loginFrom"+userId));
                // 登录完成返回值
                result.setStatus(ResultEnum.SUCCESS.getStatus());
                result.setStatusDesc("登录成功");
                result.setSign(sign);
                // add by liuyang 神策数据统计追加 登录成功后 将用户ID返回前端 20180717 start
                // 登录成功后,将用户ID返回给前端
                result.setUserId(String.valueOf(userId));
                // 预置属性不为空,发送神策登陆事件MQ
                _log.info("presetProps:" + presetProps);
                if (StringUtils.isNotBlank(presetProps)) {
                    try {
                        SensorsDataBean sensorsDataBean = new SensorsDataBean();
                        // 将json串转换成Bean
                        Map<String, Object> sensorsDataMap = JSONObject.parseObject(presetProps, new TypeReference<Map<String, Object>>() {
                        });
                        sensorsDataBean.setPresetProps(sensorsDataMap);
                        sensorsDataBean.setUserId(userId);
                        // 发送神策数据统计MQ
                        this.loginService.sendSensorsDataMQ(sensorsDataBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // add by liuyang 神策数据统计追加 登录成功后 将用户ID返回前端 20180717 end
                // add by huanghui 合规改造 20181120 start
                // 用户 类型,普通用户或者企业用户. 为方便
                // 数据用户类型为0,1.  前台使用的为1,2
                Integer userType = users.getUserType();
                String userTypeStr = "";
                if (userType == 1){
                    // 企业用户
                    userTypeStr = "2";
                }else {
                    userTypeStr = "1";
                }
                result.setUserType(userTypeStr);
                // add by huanghui 合规改造 20181120 end
                break;
        }

        LogUtil.endLog(LoginController.class.getName(), LoginDefine.DOLOGIN_MAPPING);
        return result;
    }

    /**
     *
     * 退出操作
     * 请求地址:/wx/login/doLoginOut.do
     * 需要参数: sign
     * @author sunss
     * @param request
     * @param response
     * @return
     */
    @SignValidate
    @ResponseBody
    @RequestMapping(value = LoginDefine.DOLOGINOUT_MAPPING, method = RequestMethod.POST)
    public BaseResultBean doLoginOut(HttpServletRequest request, HttpServletResponse response, String sign) {
        LogUtil.startLog(LoginController.class.getName(), LoginDefine.DOLOGINOUT_MAPPING);
        _log.info("请求退出接口,sign为：【"+sign+"】");
        LoginResultBean result = new LoginResultBean();
        result.setStatus(ResultEnum.SUCCESS.getStatus());
        result.setStatusDesc("退出成功");
        if(StringUtils.isBlank(sign)){
            return result.setEnum(ResultEnum.PARAM);
        }
        AppUserToken token = SecretUtil.getUserId(sign);
        if (token != null && token.getUserId() != null) {
            Users users = loginService.getUsers(token.getUserId());
            UsersInfo usersInfo = loginService.getUsersInfoByUserId(token.getUserId());
            UserOperationLogEntity userOperationLogEntity = new UserOperationLogEntity();
            userOperationLogEntity.setOperationType(2);
            userOperationLogEntity.setIp(GetCilentIP.getIpAddr(request));
            userOperationLogEntity.setPlatform(1);
            userOperationLogEntity.setRemark("");
            userOperationLogEntity.setOperationTime(new Date());
            userOperationLogEntity.setUserName(users.getUsername());
            userOperationLogEntity.setUserRole(String.valueOf(usersInfo.getRoleId()));
            loginService.sendUserLogMQ(userOperationLogEntity);
            // 清除sign
            SecretUtil.clearToken(sign);
            RedisUtils.del("loginFrom"+token.getUserId());
        } else {
            result.setEnum(ResultEnum.ERROR_004);
        }
        LogUtil.endLog(LoginController.class.getName(), LoginDefine.DOLOGINOUT_MAPPING);
        return result;
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
