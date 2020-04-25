/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.wechat.service.loginpassword;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.controller.user.loginpassword.BusinessException;
import com.hyjf.wechat.controller.user.loginpassword.SendSmsVo;
import com.hyjf.wechat.controller.user.loginpassword.WxLoginPasswordDefine;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fuqiang
 * @version WxLoginPasswordServiceImpl, v0.1 2018/3/23 10:58
 */
@Service
public class WxLoginPasswordServiceImpl extends BaseServiceImpl implements WxLoginPasswordService {

    private Logger logger = LoggerFactory.getLogger(WxLoginPasswordServiceImpl.class);

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    /**
     * 验证旧密码是否正确
     * @param userId
     * @param oldPassword
     * @return 0:验证成功|-1:旧密码不正确|-2:用户不存在|-3:存在多个相同用户
     */
    @Override
    public Integer queryOldPassword(String userId, String oldPassword) {
        String codeSalt = "";
        String passwordDb = "";

        UsersExample example = new UsersExample();
        example.createCriteria().andUserIdEqualTo(Integer.parseInt(userId));
        List<Users> usersList = usersMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(usersList)) {
            return -2;
        } else {
            if (usersList.size() == 1) {
                codeSalt = usersList.get(0).getSalt();
                passwordDb = usersList.get(0).getPassword();
            } else {
                return -3;
            }
        }
        // 验证用的password
        oldPassword = MD5Utils.MD5(MD5Utils.MD5(oldPassword) + codeSalt);
        // 密码正确时
        if (Validator.isNotNull(userId) && Validator.isNotNull(oldPassword) && oldPassword.equals(passwordDb)) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     * @return
     */
    @Override
    public boolean updatePasswordAction(String userId, String newPassword) {
        int cnt = 0;
        Users users = usersMapper.selectByPrimaryKey(Integer.parseInt(userId));
        if (users != null) {
            users.setPassword(MD5Utils.MD5(MD5Utils.MD5(newPassword) + users.getSalt()));
            cnt = usersMapper.updateByPrimaryKeySelective(users);
        }

        return cnt > 0;
    }

    /**
     * 发送短信验证码
     * @param sendSmsVo
     * @return
     */
    @Override
    public JSONObject sendCode(SendSmsVo sendSmsVo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "000");
        jsonObject.put("statusDesc", "短信发送成功");
        try {
            //获取系统参数
            SmsConfigExample example = new SmsConfigExample();
            List<SmsConfig> list = smsConfigMapper.selectByExample(example);
            SmsConfig smsConfig = null;
            if (list != null && !list.isEmpty()) {
                smsConfig =  list.get(0);
            }
            //发送前校验
            this.validateData(sendSmsVo,smsConfig);
            //发送短信并将发送数据存到数据库和Redis
            this.sendSms(sendSmsVo,smsConfig);

        }catch (BusinessException e){
            jsonObject.put("status", "99");
            jsonObject.put("statusDesc",e.getMessage());
        } catch (Exception e){
            logger.error("发送短信异常",e);
            jsonObject.put("status", "99");
            jsonObject.put("statusDesc","失败");
        }
        return jsonObject;
    }

    /**
     * 验证短信验证码
     * @param sendSmsVo
     * @return
     */
    @Override
    public JSONObject validateVerificationCoden(SendSmsVo sendSmsVo,boolean isUpdate) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "000");
        jsonObject.put("statusDesc", "验证成功");
        try {
            //验证
            this.validateSmsCode(sendSmsVo,isUpdate);
        }catch (BusinessException e){
            jsonObject.put("status", "99");
            jsonObject.put("statusDesc",e.getMessage());
        } catch (Exception e){
            logger.error("发送短信异常",e);
            jsonObject.put("status", "99");
            jsonObject.put("statusDesc","短信验证失败");
        }
        return jsonObject;
    }

    @Override
    public String queryUserIdByMobile(String mobile) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<Users> users = usersMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(users)) {
            return users.get(0).getUserId().toString();
        }
        return null;
    }

    @Override
    public Users queryUserByUserId(Integer userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        if (users != null){
            return users;
        }
        return null;
    }

    /**
     * 参数校验
     * @param sendSmsVo
     */
    private void validateSmsCode(SendSmsVo sendSmsVo,boolean isUpdate) {

        if(StringUtils.isBlank(sendSmsVo.getMobile())){
            throw new BusinessException("2001","手机号不能为空");
        }
        if(StringUtils.isBlank(sendSmsVo.getSmscode())){
            throw new BusinessException("2002","验证码不能为空");
        }
        int time = GetDate.getNowTime10();
        int timeAfter = time - 900;// 15分钟有效 900
        SmsCodeExample example = new SmsCodeExample();
        SmsCodeExample.Criteria cra = example.createCriteria();
        cra.andPosttimeGreaterThanOrEqualTo(timeAfter);
        cra.andPosttimeLessThanOrEqualTo(time);
        cra.andMobileEqualTo(sendSmsVo.getMobile());
        cra.andCheckcodeEqualTo(sendSmsVo.getSmscode());
        List<Integer> status = new ArrayList<Integer>();
        status.add(WxLoginPasswordDefine.CKCODE_NEW);
        // status.add(WxLoginPasswordDefine.CKCODE_YIYAN);
        cra.andStatusIn(status);
        List<SmsCode> codeList = smsCodeMapper.selectByExample(example);
        if (codeList != null && codeList.size() > 0) {
            for (SmsCode smsCode : codeList) {
                if (smsCode.getCheckfor().equals(MD5.toMD5Code(sendSmsVo.getMobile() + sendSmsVo.getSmscode() + CustomConstants.PARAM_TPL_ZHAOHUIMIMA + CustomConstants.CLIENT_WECHAT))) {
                    smsCode.setStatus(WxLoginPasswordDefine.CKCODE_YIYAN);// 已验8或已读9
                    if(isUpdate){
                    	smsCodeMapper.updateByPrimaryKey(smsCode);
                    }
                }
            }
        } else {
            throw new BusinessException("2003","验证码不正确");
        }
    }

    /**
     * 发送短信
     * @param sendSmsVo
     */
    private void sendSms(SendSmsVo sendSmsVo,SmsConfig smsConfig) {
        // 生成验证码
        String checkCode = GetCode.getRandomSMSCode(6);
        Map<String, String> param = new HashMap<String, String>();
        param.put("val_code", checkCode);
        // 发送短信验证码
        SmsMessage smsMessage = new SmsMessage(null, param, sendSmsVo.getMobile(), null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.PARAM_TPL_ZHAOHUIMIMA, CustomConstants.CHANNEL_TYPE_NORMAL);
        logger.info(""+JSONObject.toJSONString(smsMessage)+"开始短信发送>>>>>>>>>>>>>>>>>>>>>>>>>");
        Integer result = (smsProcesser.gather(smsMessage) == 1) ? 0 : 1;

        // 短信发送成功后处理
        if (result != null && result == 0) {
            // 累加手机次数
            String currentMaxPhoneCount = RedisUtils.get(sendSmsVo.getMobile() + ":MaxPhoneCount");
            if (StringUtils.isBlank(currentMaxPhoneCount)) {
                currentMaxPhoneCount = "0";
            }
            RedisUtils.set(sendSmsVo.getMobile() + ":MaxPhoneCount", (Integer.valueOf(currentMaxPhoneCount) + 1) + "", RedisUtils.getRemainMiao());
        }
        // 保存短信验证码(由于验证没有用到，将其注释掉)
        this.saveSmsCode(sendSmsVo.getMobile(), checkCode, CustomConstants.PARAM_TPL_ZHAOHUIMIMA, WxLoginPasswordDefine.CKCODE_NEW, CustomConstants.CLIENT_WECHAT);
        // 发送checkCode最大时间间隔，默认60秒
        RedisUtils.set(sendSmsVo.getMobile() + ":" + CustomConstants.PARAM_TPL_ZHAOHUIMIMA + ":IntervalTime", sendSmsVo.getMobile(), smsConfig.getMaxIntervalTime() == null ? 60 : smsConfig.getMaxIntervalTime());
    }




    /**
     * 保存短信验证码
     * @param mobile
     * @param verificationCode
     * @param verificationType
     * @param status
     * @param platform
     */
    private void saveSmsCode(String mobile, String verificationCode, String verificationType, Integer status, String platform) {
        // 使之前的验证码无效
        SmsCodeExample example = new SmsCodeExample();
        SmsCodeExample.Criteria cra = example.createCriteria();
        cra.andMobileEqualTo(mobile);
        List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(WxLoginPasswordDefine.CKCODE_NEW);
        statusList.add(WxLoginPasswordDefine.CKCODE_YIYAN);
        cra.andStatusIn(statusList);
        List<SmsCode> codeList = smsCodeMapper.selectByExample(example);
        if (codeList != null && codeList.size() > 0) {
            for (SmsCode smsCode : codeList) {
                smsCode.setStatus(WxLoginPasswordDefine.CKCODE_FAILED);// 失效7
                smsCodeMapper.updateByPrimaryKey(smsCode);
            }
        }
        // 保存新验证码到数据库
        SmsCode smsCode = new SmsCode();
        smsCode.setCheckfor(MD5.toMD5Code(mobile + verificationCode + verificationType + platform));
        smsCode.setMobile(mobile);
        smsCode.setCheckcode(verificationCode);
        smsCode.setPosttime(GetDate.getMyTimeInMillis());
        smsCode.setStatus(status);
        smsCode.setUserId(0);
        smsCodeMapper.insertSelective(smsCode);
    }

    /**
     * 参数校验
     * @param sendSmsVo
     */
    private void validateData(SendSmsVo sendSmsVo,SmsConfig smsConfig) {
        //手机号为空
        if (Validator.isNull(sendSmsVo.getMobile())) {
            throw new BusinessException("2001","请填写手机号");
        }
        //手机格式
        if (!Validator.isMobile(sendSmsVo.getMobile())) {
            throw new BusinessException("2002","手机号格式不对");
        }
        //查询该手机号是在数据库中已存在
        UsersExample userExample = new UsersExample();
        UsersExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andMobileEqualTo(sendSmsVo.getMobile());
        List<Users> userList = usersMapper.selectByExample(userExample);

        //该手机号用户是否存在
        if(userList.size() < 1){
            throw new BusinessException("2003","您的手机号尚未注册");
        }
        // 判断发送间隔时间
        String key = sendSmsVo.getMobile() + ":" + CustomConstants.PARAM_TPL_ZHAOHUIMIMA + ":IntervalTime";
        String intervalTime = RedisUtils.get(key);
        if (StringUtils.isNotBlank(intervalTime)) {
            throw new BusinessException("2005","操作过于频繁,请稍后重试");
        }

        //判断该设备号的发送次数（暂时不做）

        // 判断最大发送数max_phone_count（当日该手机号发送的次数）
        String count = RedisUtils.get(sendSmsVo.getMobile() + ":MaxPhoneCount");
        if (StringUtils.isBlank(count) || !Validator.isNumber(count)) {
            count = "0";
            RedisUtils.set(sendSmsVo.getMobile() + ":MaxPhoneCount", "0");
        }

        if (Integer.valueOf(count) > smsConfig.getMaxPhoneCount()) {
            throw new BusinessException("2006","该手机号短信请求次数超限，请明日再试");
        }
    }
}
