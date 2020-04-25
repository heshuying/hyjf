package com.hyjf.api.server.user.register;

import com.hyjf.api.server.register.UserRegisterService;
import com.hyjf.api.server.user.openaccountplus.BindUserService;
import com.hyjf.api.server.util.ErrorCodeConstant;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UtmPlat;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 外部服务接口:用户注册
 *
 * @author liuyang
 */
@Controller
@RequestMapping(value = UserRegisterDefine.REQUEST_MAPPING)
public class UserRegisterServer extends BaseController {

    Logger _log = LoggerFactory.getLogger(UserRegisterServer.class);

    @Autowired
    private UserRegisterService userRegisterService;
    @Autowired
    private BindUserService bindUserService;
    /**
     * 用户注册接口
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = UserRegisterDefine.REGISTER_ACTION)
    public UserRegisterResultBean userRegister(@RequestBody UserRegisterRequestBean userRegisterRequestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(UserRegisterDefine.THIS_CLASS, UserRegisterDefine.REGISTER_ACTION);
        _log.info(UserRegisterDefine.REQUEST_MAPPING + UserRegisterDefine.REGISTER_ACTION + "，register mobile:"+userRegisterRequestBean.getMobile()+"，机构编号："+userRegisterRequestBean.getInstCode());
        UserRegisterResultBean resultBean = new UserRegisterResultBean();
        // 手机号
        String mobile = userRegisterRequestBean.getMobile();
        // 机构编号
        String instCode = userRegisterRequestBean.getInstCode();
        // 注册平台
        String platform = userRegisterRequestBean.getPlatform();
        // 注册渠道
        String utmId = userRegisterRequestBean.getChannel();
        //推荐人
        String recommended = userRegisterRequestBean.getRecommended();
        try {
            // 验证请求参数
            // 手机号
            if (Validator.isNull(mobile)) {
                resultBean.setStatus(ErrorCodeConstant.STATUS_ZC000001);
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000001);
                resultBean.setStatusDesc("手机号不能为空");
                return resultBean;
            }
            // 机构编号
            if (Validator.isNull(instCode)) {
                resultBean.setStatus(ErrorCodeConstant.STATUS_ZC000002);
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000002);
                resultBean.setStatusDesc("机构编号不能为空");
                return resultBean;
            }
            // 注册平台
            if (Validator.isNull(platform)) {
                resultBean.setStatus(ErrorCodeConstant.STATUS_ZC000018);
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000018);
                resultBean.setStatusDesc("注册平台不能为空");
                return resultBean;
            }
            // 推广渠道
            if (Validator.isNull(utmId)) {
                resultBean.setStatus(ErrorCodeConstant.STATUS_ZC000019);
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000019);
                resultBean.setStatusDesc("推广渠道不能为空");
                return resultBean;
            }
            //如果有推荐人，校验推荐人的正确性
            Users recommendedUser = null;
            if (!StringUtils.isBlank(recommended)){
                List<Users> users = userRegisterService.selectUserByRecommendName(recommended);
                if (CollectionUtils.isEmpty(users)){
                    //先不处理推荐人输入错误 todo 2018.01.31
//                    resultBean.setStatus(ErrorCodeConstant.STATUS_CE999999);
//                    resultBean.setStatusDesc("推荐人无效");
//                    return resultBean;
                }else {
                    recommendedUser = users.get(0);
                }
            }
            // 验签
            if (!this.verifyRequestSign(userRegisterRequestBean, BaseDefine.METHOD_SERVER_REGISTER)) {
                _log.info("----验签失败----");
                resultBean.setStatus(ErrorCodeConstant.STATUS_CE000002);
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE000002);
                resultBean.setStatusDesc("验签失败！");
                return resultBean;
            }
            // 手机号合法性校验
            if (!Validator.isMobile(mobile)) {
                resultBean.setStatus(ErrorCodeConstant.STATUS_ZC000003);
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000003);
                resultBean.setStatusDesc("请输入您的真实手机号码");
                return resultBean;
            }
            // 根据机构编号检索机构信息
            HjhInstConfig instConfig = this.userRegisterService.selectInstConfigByInstCode(instCode);
            // 机构编号
            if (instConfig == null) {
                _log.info("获取机构信息为空,机构编号:[" + instCode + "].");
                resultBean.setStatus(ErrorCodeConstant.STATUS_ZC000004);
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000004);
                resultBean.setStatusDesc("机构编号错误");
                return resultBean;
            }
            // 根据渠道号检索推广渠道是否存在
            UtmPlat utmPlat = this.userRegisterService.selectUtmPlatByUtmId(utmId);
            if (utmPlat == null) {
                _log.info("根据渠道号获取推广渠道为空,渠道号:[" + utmId + "].");
                resultBean.setStatus(ErrorCodeConstant.STATUS_ZC000020);
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000020);
                resultBean.setStatusDesc("推广渠道号非法");
                return resultBean;
            }
            // 根据手机号检索用户是否存在
            Users user = this.userRegisterService.selectUserByMobile(mobile);
            // 如果用户已经存在,表示该手机号已经注册
            if (user != null) {
                _log.info("用户手机号已在平台注册:用户名:[" + user.getUsername() + ",用户手机号:[" + mobile + "]");
                resultBean.setStatus(ErrorCodeConstant.STATUS_ZC000005);
                resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_ZC000005);
                resultBean.setStatusDesc("手机号已在平台注册");
                resultBean.setIsOpenAccount(String.valueOf(user.getBankOpenAccount()));
                if (user.getBankOpenAccount() != null && user.getBankOpenAccount() == 1){
                    resultBean.setAccount(bindUserService.getAccountId(user.getUserId()));
                }
                if (user.getIsSetPassword() != null){
                    resultBean.setIsSetPassword(String.valueOf(user.getIsSetPassword()));
                }
                resultBean.setAutoInvesStatus(bindUserService.getAutoInvesStatus(user.getUserId()));
                resultBean.setUserId(user.getUserId());
                return resultBean;
            } else {
                // 手机号未注册
                _log.info("手机号在平台未注册,手机号:[" + mobile + "]");
                Integer userId = this.userRegisterService.insertUserAction(mobile, instCode, request, instConfig.getInstType(), utmPlat,platform);
                if (userId == null || userId == 0) {
                    _log.info("用户注册失败,手机号:[" + mobile + "]");
                    resultBean.setStatus(ErrorCodeConstant.STATUS_CE999999);
                    resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                    resultBean.setStatusDesc("注册失败");
                    return resultBean;
                } else {
                    Users users = this.userRegisterService.checkUserByUserId(userId);
                    if (users == null) {
                        _log.info("根据用户ID获取用户信息表失败,用户ID:[" + userId + "]");
                        resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
                        resultBean.setStatusDesc("注册失败");
                        return resultBean;
                    }
                    //如果有推荐人，将推荐人保存
                    if (recommendedUser != null){
                        _log.info("recommended is:{}",recommendedUser);
                        userRegisterService.inserSpreadUser(userId,recommendedUser);
                        users.setReferrer(recommendedUser.getUserId());
                        userRegisterService.updateByPrimaryKeySelective(users);
                    }
                    String userName = users.getUsername();
                    // 用户注册成功
                    _log.info("用户注册成功,手机号:[" + mobile + "],用户ID:[" + userId + ",用户名:[" + userName + "].");
                    resultBean.setStatus(ErrorCodeConstant.SUCCESS);
                    resultBean.setStatusForResponse(ErrorCodeConstant.SUCCESS);
                    resultBean.setStatusDesc("注册成功");
                    resultBean.setUserId(userId);// 用户Id
                    resultBean.setUserName(userName);// 用户名
                    resultBean.setIsOpenAccount("0");
                    LogUtil.endLog(UserRegisterDefine.THIS_CLASS, UserRegisterDefine.REGISTER_ACTION);
                    return resultBean;
                }
            }
        } catch (Exception e) {
            _log.info("用户注册失败~,手机号:[" + mobile + "],失败原因:[" + e.getMessage() + "]");
            resultBean.setStatusForResponse(ErrorCodeConstant.STATUS_CE999999);
            resultBean.setStatusDesc("注册失败");
            return resultBean;
        }
    }
}
