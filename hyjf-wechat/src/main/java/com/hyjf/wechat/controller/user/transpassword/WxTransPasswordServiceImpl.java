package com.hyjf.wechat.controller.user.transpassword;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecordExample;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsCodeExample;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.SmsConfigExample;
import com.hyjf.mybatis.model.auto.Users;

@Service
public class WxTransPasswordServiceImpl extends BaseServiceImpl implements WxTransPasswordService {

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;
	
	/**
	 * 更新是否设置交易密码标识位
	 * @param userId
	 * @param isFlag
	 * @return
	 * @author Michael
	 */
		
	@Override
	public boolean updateUserIsSetPassword(Users user, int isFlag) {
		
		user.setIsSetPassword(isFlag);
		return this.usersMapper.updateByPrimaryKeySelective(user) > 0 ? true : false;
			
	}

	/**
	 * 更新手机号
	 * @param user
	 * @param mobile
	 * @return
	 * @author Michael
	 */
		
	@Override
	public boolean updateUserMobile(Integer userId, String mobile) {
		Users user = this.usersMapper.selectByPrimaryKey(userId);
		if(StringUtils.isNotEmpty(mobile)){
			user.setMobile(mobile);
		}
		return this.usersMapper.updateByPrimaryKeySelective(user) > 0 ? true : false;
	}

	
	
    /**
     * 给管理员发送短信提醒
     * @param mobile
     * @param reason
     * @throws Exception
     * @author b
     */

    @Override
    public void sendSms(String mobile, String reason) throws Exception {
        Map<String, String> replaceStrs = new HashMap<String, String>();
        replaceStrs.put("var_phonenu", mobile);
        replaceStrs.put("val_reason", reason);
        // 发送短信验证码
        SmsMessage smsMessage =
                new SmsMessage(null, replaceStrs, null, null, MessageDefine.SMSSENDFORMANAGER, null,
                        CustomConstants.PARAM_TPL_DUANXINCHAOXIAN, CustomConstants.CHANNEL_TYPE_NORMAL);
        smsProcesser.gather(smsMessage);
    }

    /**
     * 保存短信验证码
     */
    @Override
    public int saveSmsCode(String mobile, String checkCode) {
        SmsCode smsCode = new SmsCode();
        smsCode.setCheckfor(MD5.toMD5Code(mobile + checkCode));
        smsCode.setMobile(mobile);
        smsCode.setCheckcode(checkCode);
        smsCode.setPosttime(GetDate.getMyTimeInMillis());
        smsCode.setStatus(0);
        smsCode.setUserId(0);
        return smsCodeMapper.insertSelective(smsCode);
    }

    /**
     * 检查短信验证码
     */
    @Override
    public int checkMobileCode(String phone, String code) {
        int time = GetDate.getNowTime10();
        int timeAfter = time - 180;
        SmsCodeExample example = new SmsCodeExample();
        SmsCodeExample.Criteria cra = example.createCriteria();
        cra.andPosttimeGreaterThanOrEqualTo(timeAfter);
        cra.andPosttimeLessThanOrEqualTo(time);
        cra.andMobileEqualTo(phone);
        cra.andCheckcodeEqualTo(code);
        cra.andStatusEqualTo(0);
        
        int count=smsCodeMapper.countByExample(example);
        if(count==1){
            List<SmsCode> list=smsCodeMapper.selectByExample(example);
            SmsCode smsCode=list.get(0);
            smsCode.setStatus(8);// 已验证8
            smsCodeMapper.updateByPrimaryKey(smsCode);
        }
        
        return count;
    }
    /**
     * 获取短信配置
     * 
     * @return
     * @author Michael
     */

    @Override
    public SmsConfig getSmsConfig() {
        SmsConfigExample example = new SmsConfigExample();
        List<SmsConfig> list = smsConfigMapper.selectByExample(example);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

	/**
	 * 根据用户ID获取企业开户信息
	 * @param userId
	 * @return
	 * @author Michael
	 */
	@Override
	public CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId) {
		CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
		CorpOpenAccountRecordExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		cra.andIsBankEqualTo(1);//江西银行
		List<CorpOpenAccountRecord> list = this.corpOpenAccountRecordMapper.selectByExample(example);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

}
