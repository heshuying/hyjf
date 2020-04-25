/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.register;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.aems.util.AemsErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UtmPlat;

/**
 * AEMS系统:用户注册
 *
 * @author liuyang
 * @version UserRegisterServer, v0.1 2018/9/4 9:14
 */
@Controller
@RequestMapping(value = AemsUserRegisterDefine.REQUEST_MAPPING)
public class AemsUserRegisterServer extends BaseController {

    Logger _log = LoggerFactory.getLogger(AemsUserRegisterServer.class);

    @Autowired
    private AemsUserRegisterService aemsUserRegisterService;

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 用户注册接口
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AemsUserRegisterDefine.REGISTER_ACTION)
    public AemsUserRegisterResultBean userRegister(@RequestBody AemsUserRegisterRequestBean aemsUserRegisterRequestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(AemsUserRegisterDefine.THIS_CLASS, AemsUserRegisterDefine.REGISTER_ACTION);
        _log.info(AemsUserRegisterDefine.REQUEST_MAPPING + AemsUserRegisterDefine.REGISTER_ACTION + "，register mobile:"+ aemsUserRegisterRequestBean.getMobile()+"，机构编号："+ aemsUserRegisterRequestBean.getInstCode());
        AemsUserRegisterResultBean resultBean = new AemsUserRegisterResultBean();
        // 手机号
        String mobile = aemsUserRegisterRequestBean.getMobile();
        // 机构编号
        String instCode = aemsUserRegisterRequestBean.getInstCode();
        // 注册平台
        String platform = aemsUserRegisterRequestBean.getPlatform();
        // 注册渠道
        String utmId = aemsUserRegisterRequestBean.getChannel();
        // 推荐人
        String recommended = aemsUserRegisterRequestBean.getRecommended();

        try {
            // 验证请求参数
            // 手机号
            if (Validator.isNull(mobile)) {
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_ZC000001);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_ZC000001);
                resultBean.setStatusDesc("手机号不能为空");
                return resultBean;
            }
            // 机构编号
            if (Validator.isNull(instCode)) {
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_ZC000002);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_ZC000002);
                resultBean.setStatusDesc("机构编号不能为空");
                return resultBean;
            }
            // 注册平台
            if (Validator.isNull(platform)) {
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_ZC000018);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_ZC000018);
                resultBean.setStatusDesc("注册平台不能为空");
                return resultBean;
            }
            // 推广渠道
            if (Validator.isNull(utmId)) {
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_ZC000019);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_ZC000019);
                resultBean.setStatusDesc("推广渠道不能为空");
                return resultBean;
            }
            //如果有推荐人，校验推荐人的正确性
            Users recommendedUser = null;
            if (!StringUtils.isBlank(recommended)){
                List<Users> users = aemsUserRegisterService.selectUserByRecommendName(recommended);
                if (CollectionUtils.isEmpty(users)){
                    //先不处理推荐人输入错误 todo 2018.01.31
//                    resultBean.setStatus(AemsErrorCodeConstant.STATUS_CE999999);
//                    resultBean.setStatusDesc("推荐人无效");
//                    return resultBean;
                }else {
                    recommendedUser = users.get(0);
                }
            }
            // 验签
            if (!this.AEMSVerifyRequestSign(aemsUserRegisterRequestBean,AemsUserRegisterDefine.REQUEST_MAPPING + AemsUserRegisterDefine.REGISTER_ACTION )) {
                _log.info("----验签失败----");
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_CE000002);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE000002);
                resultBean.setStatusDesc("验签失败！");
                return resultBean;
            }
            // 手机号合法性校验
            if (!Validator.isMobile(mobile)) {
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_ZC000003);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_ZC000003);
                resultBean.setStatusDesc("请输入您的真实手机号码");
                return resultBean;
            }
            // 根据机构编号检索机构信息
            HjhInstConfig instConfig = this.aemsUserRegisterService.selectInstConfigByInstCode(instCode);
            // 机构编号
            if (instConfig == null) {
                _log.info("获取机构信息为空,机构编号:[" + instCode + "].");
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_ZC000004);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_ZC000004);
                resultBean.setStatusDesc("机构编号错误");
                return resultBean;
            }
            // 根据渠道号检索推广渠道是否存在
            UtmPlat utmPlat = this.aemsUserRegisterService.selectUtmPlatByUtmId(utmId);
            if (utmPlat == null) {
                _log.info("根据渠道号获取推广渠道为空,渠道号:[" + utmId + "].");
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_ZC000020);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_ZC000020);
                resultBean.setStatusDesc("推广渠道号非法");
                return resultBean;
            }
            // 根据手机号检索用户是否存在
            Users user = this.aemsUserRegisterService.selectUserByMobile(mobile);
            // 如果用户已经存在,表示该手机号已经注册
            if (user != null) {
                _log.info("用户手机号已在平台注册:用户名:[" + user.getUsername() + ",用户手机号:[" + mobile + "]");
                resultBean.setStatus(AemsErrorCodeConstant.STATUS_ZC000005);
                resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_ZC000005);
                resultBean.setStatusDesc("手机号已在平台注册");
                resultBean.setIsOpenAccount(String.valueOf(user.getBankOpenAccount()));
                if (user.getBankOpenAccount() != null && user.getBankOpenAccount() == 1){
                    // TODO
                   // resultBean.setAccount(bindUserService.getAccountId(user.getUserId()));
                }
                if (user.getIsSetPassword() != null){
                    resultBean.setIsSetPassword(String.valueOf(user.getIsSetPassword()));
                }
                // TODO
                // resultBean.setAutoInvesStatus(bindUserService.getAutoInvesStatus(user.getUserId()));
                resultBean.setUserId(user.getUserId());
                return resultBean;
            } else {
                // 手机号未注册
                _log.info("手机号在平台未注册,手机号:[" + mobile + "]");
                Integer userId = this.aemsUserRegisterService.insertUserAction(mobile, instCode, request, instConfig.getInstType(), utmPlat,platform);
                if (userId == null || userId == 0) {
                    _log.info("用户注册失败,手机号:[" + mobile + "]");
                    resultBean.setStatus(AemsErrorCodeConstant.STATUS_CE999999);
                    resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE999999);
                    resultBean.setStatusDesc("注册失败");
                    return resultBean;
                } else {
                    Users users = this.aemsUserRegisterService.checkUserByUserId(userId);
                    if (users == null) {
                        _log.info("根据用户ID获取用户信息表失败,用户ID:[" + userId + "]");
                        resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE999999);
                        resultBean.setStatusDesc("注册失败");
                        return resultBean;
                    }
                    //如果有推荐人，将推荐人保存
                    if (recommendedUser != null){
                        _log.info("recommended is:{}",recommendedUser);
                        aemsUserRegisterService.inserSpreadUser(userId,recommendedUser);
                        users.setReferrer(recommendedUser.getUserId());
                        aemsUserRegisterService.updateByPrimaryKeySelective(users);
                    }
                    // 注册成功后,发送MQ,根据IP查询用户地址
                    String userName = users.getUsername();
                    // 用户注册成功
                    _log.info("用户注册成功,手机号:[" + mobile + "],用户ID:[" + userId + ",用户名:[" + userName + "].");
                    resultBean.setStatus(AemsErrorCodeConstant.SUCCESS);
                    resultBean.setStatusForResponse(AemsErrorCodeConstant.SUCCESS);
                    resultBean.setStatusDesc("注册成功");
                    resultBean.setUserId(userId);// 用户Id
                    resultBean.setUserName(userName);// 用户名
                    resultBean.setIsOpenAccount("0");
                    LogUtil.endLog(AemsUserRegisterDefine.THIS_CLASS, AemsUserRegisterDefine.REGISTER_ACTION);
                    return resultBean;
                }
            }
        } catch (Exception e) {
            _log.info("用户注册失败~,手机号:[" + mobile + "],失败原因:[" + e.getMessage() + "]");
            resultBean.setStatusForResponse(AemsErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusDesc("注册失败");
            return resultBean;
        }
    }

    /**
     * 发送成功消息队列
     * @param userid
     * @param ip
     */
    private void sendIpInfo(int userid, String ip) {

        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("mqMsgId", GetCode.getRandomCode(10));
            params.put("userId", String.valueOf(userid));
            params.put("regIp", ip);

            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME,
                    RabbitMQConstants.ROUTINGKEY_SYNC_USER_IP_USER, JSONObject.toJSONString(params));

        } catch (Exception e) {
            _log.error(e.getMessage());
        }
    }
}
