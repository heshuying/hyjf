/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.service.loginpassword;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.wechat.controller.user.loginpassword.SendSmsVo;

/**
 * @author fuqiang
 * @version WxLoginPasswordService, v0.1 2018/3/23 10:57
 */
public interface WxLoginPasswordService {
    /**
     * 验证旧密码是否正确
     * @param userId
     * @param oldPassword
     * @return 0:验证成功|-1:旧密码不正确|-2:用户不存在|-3:存在多个相同用户
     */
    Integer queryOldPassword(String userId, String oldPassword);

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     * @return
     */
    boolean updatePasswordAction(String userId, String newPassword);

    /**
     * @author fp
     * 发送短信验证码
     * @param sendSmsVo
     * @return
     */
    JSONObject sendCode(SendSmsVo sendSmsVo);

    /**
     * @author fp
     * 验证短信验证码
     * @param sendSmsVo
     * @return
     */
    JSONObject validateVerificationCoden(SendSmsVo sendSmsVo,boolean isUpdate);

    /**
     * 根据号码查询用户id
     * @param mobile
     * @return
     */
    String queryUserIdByMobile(String mobile);

    /**
     * 根据用户Id查询用户
     * @param userId
     * @return
     */
    Users queryUserByUserId(Integer userId);
}
