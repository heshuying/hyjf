/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.bank.service.user.bankopen;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.IdCard15To18;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.auto.BankOpenAccountLogExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

@Service
public class BankOpenServiceImpl extends BaseServiceImpl implements BankOpenService {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	Logger _log = LoggerFactory.getLogger(BankOpenServiceImpl.class);
	/**
	 * 保存开户的数据
	 */
	@Override
	public boolean updateUserAccount(BankCallBean bean) {

		String orderId = bean.getLogOrderId();
		int userId = Integer.parseInt(bean.getLogUserId());// 注册人userId
		BankOpenAccountLog openAccoutLog = this.selectBankOpenAccountLog(orderId);
		if (Validator.isNull(openAccoutLog)) {
			throw new RuntimeException("查询用户开户日志表失败，用户开户订单号：" + orderId + ",用户userId:" + userId);
		}
		BankOpenAccountLogExample accountLogExample = new BankOpenAccountLogExample();
		accountLogExample.createCriteria().andUserIdEqualTo(userId);
		boolean deleteLogFlag = this.bankOpenAccountLogMapper.deleteByExample(accountLogExample) > 0 ? true : false;
		if (!deleteLogFlag) {
			throw new RuntimeException("删除用户开户日志表失败，用户开户订单号：" + orderId + ",用户userId:" + userId);
		}
		Users user = this.getUsers(userId);// 获取用户信息
		String userName = user.getUsername();
		Date nowDate = new Date();// 当前时间
		String idCard = openAccoutLog.getIdNo(); // 身份证号
		String trueName = null;// 真实姓名
		try {
			trueName = URLDecoder.decode(openAccoutLog.getName(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			_log.info("用户编号："+ userId +",身份证号:"+ idCard +",开户获取真实姓名异常");
		}
		if (idCard != null && idCard.length() < 18) {
			try {
				idCard = IdCard15To18.getEighteenIDCard(idCard);
			} catch (Exception e) {
				_log.info("用户编号："+ userId +",身份证号:"+ idCard +",获取身份证异常");
			}
		}
		int sexInt = Integer.parseInt(idCard.substring(16, 17));// 性别
		if (sexInt % 2 == 0) {
			sexInt = 2;
		} else {
			sexInt = 1;
		}
		String birthDayTemp = idCard.substring(6, 14);// 出生日期
		String birthDay = StringUtils.substring(birthDayTemp, 0, 4) + "-" + StringUtils.substring(birthDayTemp, 4, 6)
				+ "-" + StringUtils.substring(birthDayTemp, 6, 8);
		user.setBankOpenAccount(1);
//		user.setAccountEsb(bean.getLogClient());
		user.setBankAccountEsb(bean.getLogClient());
		user.setRechargeSms(0);
		user.setWithdrawSms(0);
		user.setUserType(0);
		user.setMobile(openAccoutLog.getMobile());
		user.setVersion(user.getVersion().add(new BigDecimal("1")));
		// 更新相应的用户表
		boolean usersFlag = usersMapper.updateByPrimaryKeySelective(user) > 0 ? true : false;
		if (!usersFlag) {
			throw new RuntimeException("更新用户表失败！");
		}
		UsersInfoExample uiexample = new UsersInfoExample();
		UsersInfoExample.Criteria crtui = uiexample.createCriteria();
		crtui.andUserIdEqualTo(userId);
		List<UsersInfo> userInfos = usersInfoMapper.selectByExample(uiexample);
		if (userInfos == null) {
			throw new RuntimeException("用户详情表数据错误，用户id：" + user.getUserId());
		} else if (userInfos.size() != 1) {
			throw new RuntimeException("用户详情表数据错误，用户id：" + user.getUserId());
		}
		UsersInfo usersInfo = userInfos.get(0);
		usersInfo.setTruename(trueName);
		usersInfo.setIdcard(idCard);
		usersInfo.setSex(sexInt);
		usersInfo.setBirthday(birthDay);
		usersInfo.setTruenameIsapprove(1);
		usersInfo.setMobileIsapprove(1);
		// 更新用户详细信息表
		boolean userInfoFlag = usersInfoMapper.updateByPrimaryKeySelective(usersInfo) > 0 ? true : false;
		if (!userInfoFlag) {
			throw new RuntimeException("更新用户详情表失败！");
		}
		// 插入江西银行关联表
		BankOpenAccount openAccount = new BankOpenAccount();
		openAccount.setUserId(userId);
		openAccount.setUserName(user.getUsername());
		openAccount.setAccount(bean.getAccountId());
		openAccount.setCreateTime(nowDate);
		openAccount.setCreateUserId(userId);
		openAccount.setCreateUserName(userName);
		boolean openAccountFlag = this.bankOpenAccountMapper.insertSelective(openAccount) > 0 ? true : false;
		if (!openAccountFlag) {
			throw new RuntimeException("插入用户开户表失败！");
		}
		// 根据银行卡号查询所属银行ID
		String bankId = this.getBankIdByCardNo(openAccoutLog.getCardNo());
		// 插入相应的银行卡
		BankCard bankCard = new BankCard();
		bankCard.setUserId(userId);
		bankCard.setUserName(userName);
		bankCard.setCardNo(openAccoutLog.getCardNo());
		bankCard.setStatus(1);
		bankCard.setBankId(bankId == null ? 0 : Integer.valueOf(bankId));
		bankCard.setBank(bankId == null ? "" : this.getBankNameById(bankId));
		// 银行联号
		String payAllianceCode = "";
		// 调用江西银行接口查询银行联号
		BankCallBean payAllianceCodeQueryBean = this.payAllianceCodeQuery(openAccoutLog.getCardNo(), userId);
		if(payAllianceCodeQueryBean!= null && BankCallConstant.RESPCODE_SUCCESS.equals(payAllianceCodeQueryBean.getRetCode())){
			payAllianceCode = payAllianceCodeQueryBean.getPayAllianceCode();
		}
		// 如果此时银行联行还是为空,调用本地数据库查询银行总行的行联号
		if(StringUtils.isBlank(payAllianceCode) && !StringUtils.isBlank(bankId)){
			payAllianceCode = this.getPayAllianceCodeByBankId(bankId);
		}
		bankCard.setMobile(bean.getMobile());
		bankCard.setPayAllianceCode(payAllianceCode);
		bankCard.setCreateTime(nowDate);
		bankCard.setCreateUserId(userId);
		bankCard.setCreateUserName(userName);
		boolean bankFlag = this.bankCardMapper.insertSelective(bankCard) > 0 ? true : false;
		if (!bankFlag) {
			throw new RuntimeException("插入用户银行卡失败！");
		}
		// 开户更新开户渠道统计开户时间
		AppChannelStatisticsDetailExample example = new AppChannelStatisticsDetailExample();
		AppChannelStatisticsDetailExample.Criteria crt = example.createCriteria();
		crt.andUserIdEqualTo(userId);
		List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper
				.selectByExample(example);
		if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
			AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
			channelDetail.setOpenAccountTime(new Date());
			this.appChannelStatisticsDetailMapper.updateByPrimaryKeySelective(channelDetail);
		}
		return true;
	}

	/***
	 * 根据订单号查询用户的开户记录
	 * 
	 * @param orderId
	 * @return
	 */
	private BankOpenAccountLog selectBankOpenAccountLog(String orderId) {
		BankOpenAccountLogExample openAccoutLogExample = new BankOpenAccountLogExample();
		BankOpenAccountLogExample.Criteria crt = openAccoutLogExample.createCriteria();
		crt.andOrderIdEqualTo(orderId);
		List<BankOpenAccountLog> openAccountLogs = this.bankOpenAccountLogMapper.selectByExample(openAccoutLogExample);
		if (openAccountLogs != null && openAccountLogs.size() == 1) {
			return openAccountLogs.get(0);
		} else {
			return null;
		}
	}

	@Override
	public String getUsersMobile(Integer userId) {
		Users user = this.usersMapper.selectByPrimaryKey(userId);
		if (user != null) {
			return user.getMobile();
		}
		return null;
	}

	@Override
	public JSONObject selectUserByMobile(int userId, String mobile) {

		JSONObject ret = new JSONObject();
		UsersExample userExample = new UsersExample();
		UsersExample.Criteria crt = userExample.createCriteria();
		crt.andMobileEqualTo(mobile);
		List<Users> users = this.usersMapper.selectByExample(userExample);
		if (users != null && users.size() == 1) {
			return null;
		} else if (users.size() > 0) {
			for (Users user : users) {
				if (user.getUserId() == userId) {
					ret.put("newUserName", user.getUsername());
				} else {
					String oldUserName = "";
					if (StringUtils.isNotBlank(ret.getString("oldUserName"))) {
						oldUserName = ret.getString("oldUserName") + "," + user.getUsername();
						ret.put("oldUserName", oldUserName);
					} else {
						oldUserName = user.getUsername();
						ret.put("oldUserName", oldUserName);
					}
				}
			}
		}
		return ret;
	}

	@Override
	public boolean checkIfSendCoupon(Users user) {
		// TODO 预留活动id
		String activityId = CustomConstants.OPENACCOUNT_ACTIVITY_ID;
		ActivityList activityList = activityListMapper.selectByPrimaryKey(Integer.parseInt(activityId));
		if (activityList == null) {
			return false;
		}
		Integer timeStart = activityList.getTimeStart();
		Integer timeEnd = activityList.getTimeEnd();
		if (timeStart > GetDate.getNowTime10()) {
			return false;
		}
		if (timeEnd != null && timeEnd < GetDate.getNowTime10()) {
			return false;
		}
		Long time = new Date().getTime() / 1000;
		// 判断推荐人
		SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
		SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
		spreadsUsersExampleCriteria.andUserIdEqualTo(user.getUserId());
		List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
		if (sList != null && sList.size() == 1) {
			int refUserId = sList.get(0).getSpreadsUserid();
			// 判断是否是投之家来的
			if (refUserId != 111734) {
				return false;
			} else {
				// 共用判断是否可以发券开始
				if (user.getRegTime() < timeStart) {
					return false;
				} else {
					if (time < timeStart) {
						return false;
					} else {
						return true;
					}
				}
				// 共用判断是否可以发券结束
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean updateUserAccountLog(BankOpenAccountLog openAccountLog, BankCallBean openAccoutBean) {

		Date date = new Date();
		int userId = Integer.parseInt(openAccoutBean.getLogUserId());
		openAccountLog.setName(openAccoutBean.getName());
		openAccountLog.setIdNo(openAccoutBean.getIdNo());
		openAccountLog.setIdType(openAccoutBean.getIdType());
		openAccountLog.setCardNo(openAccoutBean.getCardNo());
		openAccountLog.setAcctUse(openAccoutBean.getAcctUse());
		openAccountLog.setUserIp(openAccoutBean.getLogIp());
		openAccountLog.setUpdateTime(date);
		openAccountLog.setUpdateUserId(userId);
		openAccountLog.setUpdateUserName(openAccountLog.getCreateUserName());
		boolean flag = this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(openAccountLog) > 0 ? true : false;
		return flag;
	}

	@Override
	public boolean updateUserAccountLog(int userId, String userName, String mobile, String logOrderId, String clientPc,String name,String idno,String cardNo) {
		Date date = new Date();
		BankOpenAccountLogExample example = new BankOpenAccountLogExample();
		example.createCriteria().andUserIdEqualTo(userId).andOrderIdEqualTo(logOrderId);
		List<BankOpenAccountLog> bankOpenAccountLogs = this.bankOpenAccountLogMapper.selectByExample(example);
		if (bankOpenAccountLogs != null && bankOpenAccountLogs.size() == 1) {
			BankOpenAccountLog openAccountLog = bankOpenAccountLogs.get(0);
			openAccountLog.setMobile(mobile);
			openAccountLog.setStatus(0);
			openAccountLog.setUpdateTime(date);
			openAccountLog.setUpdateUserId(userId);
			openAccountLog.setUpdateUserName(openAccountLog.getCreateUserName());
			boolean updateFlag = this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(openAccountLog) > 0 ? true: false;
			return updateFlag;
		} else {
			BankOpenAccountLog bankOpenAccountLog = new BankOpenAccountLog();
			bankOpenAccountLog.setUserId(userId);
			bankOpenAccountLog.setUserName(userName);
			bankOpenAccountLog.setMobile(mobile);
			bankOpenAccountLog.setStatus(0);
			bankOpenAccountLog.setOrderId(logOrderId);
			bankOpenAccountLog.setCreateTime(date);
			bankOpenAccountLog.setCreateUserId(userId);
			bankOpenAccountLog.setCreateUserName(userName);
			bankOpenAccountLog.setName(name);
			bankOpenAccountLog.setIdNo(idno);
			bankOpenAccountLog.setCardNo(cardNo);
			bankOpenAccountLog.setClient(Integer.parseInt(clientPc));
			boolean flag = this.bankOpenAccountLogMapper.insertSelective(bankOpenAccountLog) > 0 ? true : false;
			return flag;
		}
	}

	@Override
	public boolean updateUserAccountLog(int userId, String logOrderId, String srvAuthCode) {
		Date date = new Date();
		BankOpenAccountLogExample example = new BankOpenAccountLogExample();
		example.createCriteria().andUserIdEqualTo(userId).andOrderIdEqualTo(logOrderId);
		List<BankOpenAccountLog> bankOpenAccountLogs = this.bankOpenAccountLogMapper.selectByExample(example);
		if (bankOpenAccountLogs != null && bankOpenAccountLogs.size() == 1) {
			BankOpenAccountLog openAccountLog = bankOpenAccountLogs.get(0);
			openAccountLog.setStatus(1);
			openAccountLog.setLastSrvAuthCode(srvAuthCode);
			openAccountLog.setUpdateTime(date);
			openAccountLog.setUpdateUserId(userId);
			openAccountLog.setUpdateUserName(openAccountLog.getCreateUserName());
			boolean updateFlag = this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(openAccountLog) > 0 ? true
					: false;
			if (updateFlag) {
				return true;
			}
		}
		return false;
	}

	@Override
	public BankOpenAccountLog selectUserAccountLog(Integer userId, String logOrderId) {
		BankOpenAccountLogExample example = new BankOpenAccountLogExample();
		example.createCriteria().andUserIdEqualTo(userId).andOrderIdEqualTo(logOrderId);
		List<BankOpenAccountLog> bankOpenAccountLogs = this.bankOpenAccountLogMapper.selectByExample(example);
		if (bankOpenAccountLogs != null && bankOpenAccountLogs.size() == 1) {
			BankOpenAccountLog openAccountLog = bankOpenAccountLogs.get(0);
			return openAccountLog;
		}
		return null;
	}

	@Override
	public boolean updateUserAccountLog(Integer userId, String logOrderId, int status) {
		Date date = new Date();
		BankOpenAccountLogExample example = new BankOpenAccountLogExample();
		example.createCriteria().andUserIdEqualTo(userId).andOrderIdEqualTo(logOrderId);
		List<BankOpenAccountLog> bankOpenAccountLogs = this.bankOpenAccountLogMapper.selectByExample(example);
		if (bankOpenAccountLogs != null && bankOpenAccountLogs.size() == 1) {
			BankOpenAccountLog openAccountLog = bankOpenAccountLogs.get(0);
			openAccountLog.setStatus(status);
			openAccountLog.setUpdateTime(date);
			openAccountLog.setUpdateUserId(userId);
			openAccountLog.setUpdateUserName(openAccountLog.getCreateUserName());
			boolean updateFlag = this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(openAccountLog) > 0 ? true
					: false;
			if (updateFlag) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean existMobile(String mobile) {
		UsersExample example = new UsersExample();
		example.createCriteria().andMobileEqualTo(mobile);
		int size = usersMapper.countByExample(example);
		if (size > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean checkIdNo(String idNo) {
		UsersInfoExample example = new UsersInfoExample();
		example.createCriteria().andIdcardEqualTo(idNo);
		List<UsersInfo> userInfo = usersInfoMapper.selectByExample(example);
		if (userInfo!=null&&userInfo.size()>0) {
			for (int i = 0; i < userInfo.size(); i++) {
				Integer userId = userInfo.get(i).getUserId();
				UsersExample userExample = new UsersExample();
				userExample.createCriteria().andUserIdEqualTo(userId);
				List<Users> user = usersMapper.selectByExample(userExample );
				if (user.get(0).getBankOpenAccount()==1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 保存用户的初始开户记录 add by jijun 2018/04/04
	 * @param userId
	 * @param userName
	 * @param orderId
	 * @param clientPc
	 * @param clientPc2 
	 * @return
	 */
	@Override
	public boolean updateUserAccountLog(int userId, String userName, String mobile, String logOrderId, String clientPc) {
		Date date = new Date();
		BankOpenAccountLogExample example = new BankOpenAccountLogExample();
		example.createCriteria().andUserIdEqualTo(userId).andOrderIdEqualTo(logOrderId);
		List<BankOpenAccountLog> bankOpenAccountLogs = this.bankOpenAccountLogMapper.selectByExample(example);
		if (bankOpenAccountLogs != null && bankOpenAccountLogs.size() == 1) {
			BankOpenAccountLog openAccountLog = bankOpenAccountLogs.get(0);
			openAccountLog.setMobile(mobile);
			openAccountLog.setStatus(0);
			openAccountLog.setUpdateTime(date);
			openAccountLog.setUpdateUserId(userId);
			openAccountLog.setUpdateUserName(openAccountLog.getCreateUserName());
			boolean updateFlag = this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(openAccountLog) > 0 ? true
					: false;
			if (updateFlag) {
				return true;
			} else {
				return false;
			}
		} else {
			BankOpenAccountLog bankOpenAccountLog = new BankOpenAccountLog();
			bankOpenAccountLog.setUserId(userId);
			bankOpenAccountLog.setUserName(userName);
			bankOpenAccountLog.setMobile(mobile);
			bankOpenAccountLog.setStatus(0);
			bankOpenAccountLog.setOrderId(logOrderId);
			bankOpenAccountLog.setCreateTime(date);
			bankOpenAccountLog.setCreateUserId(userId);
			bankOpenAccountLog.setCreateUserName(userName);
			bankOpenAccountLog.setClient(Integer.parseInt(clientPc));
			boolean flag = this.bankOpenAccountLogMapper.insertSelective(bankOpenAccountLog) > 0 ? true : false;
			return flag;
		}
	}
	
	
	/**
	 * 开户成功后,发送CA认证MQ update by jijun 2018/04/11
	 * @param userId
	 */
	@Override
	public void sendCAMQ(String userId){
		_log.info("开户成功后,发送法大大CA认证MQ,用户ID:[" + userId + "].");
		// add by liuyang 20180209 开户成功后,将用户ID加入到CA认证消息队列 start
		// 加入到消息队列
		Map<String, String> params = new HashMap<String, String>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("userId", String.valueOf(userId));
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_CERTIFICATE_AUTHORITY, JSONObject.toJSONString(params));
		// add by liuyang 20180209 开户成功后,将用户ID加入到CA认证消息队列 end
	}

	@Override
	public UsersInfo selectByUserId(Integer userId) {
		UsersInfo usersInfo = new UsersInfo();
		UsersInfoExample example = new UsersInfoExample();
		example.createCriteria().andUserIdEqualTo(userId);
		List<UsersInfo> userList = usersInfoMapper.selectByExample(example);
		if (userList.size() > 0) {
			usersInfo = userList.get(0);
		} 		
		return usersInfo;
	}

}
