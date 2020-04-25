package com.hyjf.bank.service.user.transpassword;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mongo.dao.ChinapnrSendLogDao;
import com.hyjf.mongo.entity.ChinapnrSendlog;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;

@Service
public class TransPasswordServiceImpl extends BaseServiceImpl implements TransPasswordService {

    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;


    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ChinapnrSendLogDao chinapnrSendLogDao;

    private static final String BACKLOG = "banklog";

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
		boolean isUpdate = this.usersMapper.updateByPrimaryKeySelective(user) > 0 ? true : false;
		return isUpdate;
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

    /**
     * 发送CA认证客户信息修改MQ
     * @param userId
     */
	@Override
	public  void sendCAMQ(Integer userId){
        // add by liuyang 20180227 修改手机号后 发送更新客户信息MQ start
        // 加入到消息队列
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("userId", String.valueOf(userId));
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_USER_INFO_CHANGE, JSONObject.toJSONString(params));
        // add by liuyang 20180227 修改手机号后 发送更新客户信息MQ end
    }

    /**
     * 根据订单号获取重置交易密码是否成功
     * @param orderId
     * @return
     */
    @Override
    public boolean backLogIsSuccess(String orderId){
        //add by cwyang 查询银行设置交易密码是否成功
        boolean backIsSuccess = true;
        ChinapnrSendlog chinapnrSendlog = selectBankLogByOrderId(orderId);
        if(chinapnrSendlog != null){
            String msgdata = chinapnrSendlog.getMsgdata();
            JSONObject jsonObject = JSONObject.parseObject(msgdata);
            String retCode = jsonObject.getString("retCode");
            if(retCode!=null&&!BankCallConstant.RESPCODE_SUCCESS.equals(retCode)){
                backIsSuccess = false;
            }
        }else{
            backIsSuccess = false;
        }

        return backIsSuccess;
    }


    /**
     * 根据订单号查询返回日志
     *
     * @param orderId
     * @return
     */
    public ChinapnrSendlog selectBankLogByOrderId(String orderId) {
        Query query = new Query();
        Criteria criteria = Criteria.where("ordid").is(orderId);
        query.addCriteria(criteria);
        return this.chinapnrSendLogDao.findOne(query, BACKLOG);
    }


}
