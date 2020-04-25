package com.hyjf.admin.exception.mobilesynchronize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.user.changelog.ChangeLogDefine;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersChangeLog;
import com.hyjf.mybatis.model.auto.UsersChangeLogExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.MobileSynchronizeCustomize;
import com.hyjf.mybatis.model.customize.admin.UserInfoForLogCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

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

	/**
	 * 检索已开户用户数量
	 * 
	 * @param form
	 * @return
	 */
	@Override
	public int countBankOpenAccountUsers(MobileSynchronizeBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 电子账号
		if (StringUtils.isNotEmpty(form.getAccountIdSrch())) {
			param.put("accountIdSrch", form.getAccountIdSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 手机号
		if (StringUtils.isNotEmpty(form.getMobileSrch())) {
			param.put("mobileSrch", form.getMobileSrch());
		}
		int count = this.mobileSynchronizeCustomizeMapper.countBankOpenAccountUsers(param);
		return count;
	}

	/**
	 * 检索已开户用户列表
	 * 
	 * @param form
	 * @return
	 */
	@Override
	public List<MobileSynchronizeCustomize> selectBankOpenAccountUsersList(MobileSynchronizeBean form, int limitStart, int limitEnd) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 电子账号
		if (StringUtils.isNotEmpty(form.getAccountIdSrch())) {
			param.put("accountIdSrch", form.getAccountIdSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 手机号
		if (StringUtils.isNotEmpty(form.getMobileSrch())) {
			param.put("mobileSrch", form.getMobileSrch());
		}
		if (limitStart != -1) {
			param.put("limitStart", limitStart);
			param.put("limitEnd", limitEnd);
		}
		return this.mobileSynchronizeCustomizeMapper.selectBankOpenAccountUsersList(param);
	}

	/**
	 * 更新用户手机号
	 * 
	 * @param accountId
	 * @param userId
	 * @return
	 */
	@Override
	public boolean updateMobile(String accountId, String userId) throws Exception {
		// 根据用户ID查询用户当前手机号
		Users users = this.getUsersByUserId(Integer.parseInt(userId));
		if (users == null) {
			throw new Exception("获取用户信息失败,用户ID:" + userId);
		}
		String mobile = users.getMobile();
		// 调用银行接口查询电子账户手机号
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String instCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String channel = BankCallConstant.CHANNEL_PC;
		String orderId = GetOrderIdUtils.getOrderId2(Integer.parseInt(userId));
		String orderDate = GetOrderIdUtils.getOrderDate();
		String txDate = GetOrderIdUtils.getTxDate();
		String txTime = GetOrderIdUtils.getTxTime();
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_MOBILE_MAINTAINACE);// 消息类型(用户开户)
		bean.setInstCode(instCode);// 机构代码
		bean.setBankCode(bankCode);
		bean.setTxDate(txDate);
		bean.setTxTime(txTime);
		bean.setSeqNo(seqNo);
		bean.setChannel(channel);
		bean.setAccountId(accountId);
		bean.setOption("0");
		bean.setLogUserId(userId);
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
						boolean isMobileUpdateFlag = this.usersMapper.updateByPrimaryKeySelective(users) > 0 ? true : false;
						if (!isMobileUpdateFlag) {
							throw new Exception("更新用户手机号失败,用户ID:" + userId);
						}
						// 获取用户详情
						UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.parseInt(userId));
						// 插入手机号修改记录表
						UsersChangeLog changeLog = new UsersChangeLog();
						List<UserInfoForLogCustomize> userInfoForLog = usersCustomizeMapper.selectUserByUserId(Integer.parseInt(userId));
						if (userInfoForLog != null && userInfoForLog.size() == 1) {
							UserInfoForLogCustomize logRecord = userInfoForLog.get(0);
							changeLog.setUserId(logRecord.getUserId());
							changeLog.setUsername(logRecord.getUserName());
							changeLog.setAttribute(logRecord.getAttribute());
							changeLog.setIs51(logRecord.getIs51());
							changeLog.setRealName(logRecord.getRealName());
							changeLog.setRecommendUser(logRecord.getRecommendName());
							changeLog.setChangeType(ChangeLogDefine.CHANGELOG_TYPE_USERINFO);
							changeLog.setMobile(logRecord.getMobile());
							changeLog.setRole(logRecord.getUserRole());
							changeLog.setStatus(logRecord.getUserStatus());
							UsersChangeLogExample logExample = new UsersChangeLogExample();
							UsersChangeLogExample.Criteria logCriteria = logExample.createCriteria();
							logCriteria.andUserIdEqualTo(Integer.parseInt(userId));
							int count = usersChangeLogMapper.countByExample(logExample);
							if (count <= 0) {
								// 如果从来没有添加过操作日志，则将原始信息插入修改日志中
								if (userInfoForLog != null && !userInfoForLog.isEmpty()) {
									changeLog.setRemark("初始注册");
									changeLog.setChangeUser("system");
									changeLog.setChangeTime(logRecord.getRegTime());
									boolean isInsertFlag = usersChangeLogMapper.insertSelective(changeLog) > 0 ? true : false;
									if (!isInsertFlag) {
										throw new Exception("插入修改记录失败");
									}
								}
							}
							// 保存用户信息修改日志
							AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
							// 插入一条用户信息修改日志
							changeLog.setMobile(users.getMobile());
							changeLog.setStatus(users.getStatus());
							changeLog.setRole(usersInfo.getRoleId());
							changeLog.setChangeUser(adminSystem.getUsername());
							changeLog.setChangeUserid(Integer.parseInt(adminSystem.getId()));
							changeLog.setRemark("江西银行手机号同步");
							changeLog.setChangeTime(GetDate.getNowTime10());
							boolean isUsersChangeLogFlag = usersChangeLogMapper.insertSelective(changeLog) > 0 ? true : false;
							if (!isUsersChangeLogFlag) {
								throw new Exception("插入修改日志失败");
							}
							return isUsersChangeLogFlag;
						}
					} else {
						// 如果手机号相同,不用同步
						return true;
					}
				}
			}
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
}
