package com.hyjf.batch.synchronizeMeaage.mobile;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.auto.AccountMobileAynchMapper;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.admin.UserInfoForLogCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步手机号Service实现类
 * 
 * @author liuyang
 *
 */
@Service
public class MobileSynchronizeServiceImpl extends BaseServiceImpl implements MobileSynchronizeService {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	@Autowired
	AccountMobileAynchMapper accountMobileAynchMapper;

	Logger _log = LoggerFactory.getLogger(MobileSynchronizeTask.class);
	/** 类名 */
	private static final String THIS_CLASS = MobileSynchronizeTask.class.getName();
	/**
	 * 更新用户手机号
	 * 
	 * @param accountMobileAynch
	 * @return
	 */
	@Override
	public boolean updateMobile(AccountMobileAynch accountMobileAynch) throws Exception {
		Integer userId = accountMobileAynch.getUserId();
		String accountId = accountMobileAynch.getAccountid();
		Integer id = accountMobileAynch.getId();
		Integer searchtime = accountMobileAynch.getSearchtime()+1;
		accountMobileAynch.setSearchtime(searchtime);
		// 根据用户ID查询用户当前手机号
		Users users = this.getUsersByUserId(userId);
		if (users == null) {
			throw new Exception("获取用户信息失败,用户ID:" + userId);
		}
		String mobile = accountMobileAynch.getMobile();
		// 调用银行接口查询电子账户手机号
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String channel = BankCallConstant.CHANNEL_PC;
		String orderId = GetOrderIdUtils.getOrderId2(userId);
		String orderDate = GetOrderIdUtils.getOrderDate();
		String txDate = GetOrderIdUtils.getTxDate();
		String txTime = GetOrderIdUtils.getTxTime();
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_MOBILE_MAINTAINACE);// 消息类型
		bean.setInstCode(instCode);// 机构代码
		bean.setBankCode(bankCode);
		bean.setTxDate(txDate);
		bean.setTxTime(txTime);
		bean.setSeqNo(seqNo);
		bean.setChannel(channel);
		bean.setAccountId(accountId);
		bean.setOption("0");
		bean.setLogUserId(userId+"");
		bean.setLogOrderId(orderId);
		bean.setLogOrderDate(orderDate);
		bean.setLogRemark("电子账户手机号查询");
		try {
			BankCallBean resultBean = BankCallUtils.callApiBg(bean);
			if (Validator.isNotNull(resultBean)) {
				String retCode = StringUtils.isNotBlank(resultBean.getRetCode()) ? resultBean.getRetCode() : "";
				// 如果返回是成功
				if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
					// 获取银行返回的手机号
					String bankMobile = resultBean.getMobile();
					// 如果本地记录的手机号跟银行的手机号不一致,更新本地记录的手机号
					if (!bankMobile.equals(mobile)) {
						users.setMobile(bankMobile);
						accountMobileAynch.setNewMobile(bankMobile);
						accountMobileAynch.setStatus(1);
						int i = this.usersMapper.updateByPrimaryKeySelective(users);
						int s = this.accountMobileAynchMapper.updateByPrimaryKeySelective(accountMobileAynch);
						boolean isMobileUpdateFlag=	s>0&&s>0?true:false;
						return isMobileUpdateFlag;

					} else {
						// 如果手机号相同,不用同步
						boolean b = this.accountMobileAynchMapper.updateByPrimaryKeySelective(accountMobileAynch) > 0 ? true : false;
						return b;
					}
				}
			}
                _log.info(THIS_CLASS + "==>" + userId + "==>" + "接口查询银行手机号异常！");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}


	/**
	 * 同步手机号之后,发送CA认证客户信息修改MQ
	 * @param userId
	 */
	@Override
	public void sendCAMQ(String userId) {
		// add by liuyang 20180227 修改手机号后 发送更新客户信息MQ start
		// 加入到消息队列
		Map<String, String> params = new HashMap<String, String>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("userId", String.valueOf(userId));
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_USER_INFO_CHANGE, JSONObject.toJSONString(params));
		// add by liuyang 20180227 修改手机号后 发送更新客户信息MQ end
	}

	@Override
	public List<AccountMobileAynch> searchAccountMobileAynch(String flag) {
		AccountMobileAynchExample accountMobileAynchExample = new AccountMobileAynchExample();
		AccountMobileAynchExample.Criteria criteria = accountMobileAynchExample.createCriteria();
		criteria.andStatusEqualTo(0);
		if(StringUtils.isNotBlank(flag)&&StringUtils.equals("1",flag)){
			criteria.andFlagEqualTo(1);
		}else if(StringUtils.isNotBlank(flag)&&StringUtils.equals("2",flag)){
			criteria.andFlagEqualTo(2);
		}

		accountMobileAynchMapper.selectByExample(accountMobileAynchExample);
		return accountMobileAynchMapper.selectByExample(accountMobileAynchExample);
	}
}
