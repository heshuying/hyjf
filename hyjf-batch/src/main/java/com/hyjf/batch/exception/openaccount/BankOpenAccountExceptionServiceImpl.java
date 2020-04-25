package com.hyjf.batch.exception.openaccount;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.BankUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.IdCard15To18;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.auto.BankOpenAccountLogExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BankOpenAccountExceptionServiceImpl extends BaseServiceImpl implements BankOpenAccountExceptionService {

	Logger _log = LoggerFactory.getLogger(BankOpenAccountExceptionServiceImpl.class);

	@Override
	public void updateBankOpen() {
		// 从开户日志表中获取开户掉单的数据,进行掉单修复
		BankOpenAccountLogExample example = new BankOpenAccountLogExample();
		example.createCriteria().andIdNoIsNotNull();// 身份证不为空
		example.createCriteria().andIdTypeIsNotNull();// 证件类型不为空
		example.createCriteria().andStatusEqualTo(1);
		List<BankOpenAccountLog> logList = bankOpenAccountLogMapper.selectByExample(example);
		if (logList != null && logList.size() > 0) {
			_log.info("===============cwyang 开始进行开户掉单处理  list.size is " + logList.size());
			for (int i = 0; i < logList.size(); i++) {
				Integer userId = logList.get(i).getUserId();
				BankOpenAccountLog logInfo = logList.get(i);
				try {
					// 先判断用户是否已开户
					BankOpenAccountExample example2 = new BankOpenAccountExample();
					example2.createCriteria().andUserIdEqualTo(userId);
					List<BankOpenAccount> accountList = this.bankOpenAccountMapper.selectByExample(example2);
					if (accountList != null && accountList.size() > 0) {

						// 用户已开户,删除日志表无用数据
						BankOpenAccountLogExample deleteExample = new BankOpenAccountLogExample();
						deleteExample.createCriteria().andUserIdEqualTo(userId);
						int delCount = this.bankOpenAccountLogMapper.deleteByExample(deleteExample);
						_log.info("用户已开户:开户用户ID:[" + userId + "],电子账户号:[" + accountList.get(0).getAccount() + "],删除此用户的开户记录.删除件数:[" + delCount + "].");
						continue;
					}
					// 准备数据查询江西银行开户数据
					String idNo = logList.get(i).getIdNo();
					String idType = logList.get(i).getIdType();
					BankCallBean bankInfo = queryOpenAccountList(idNo, idType, String.valueOf(userId));
					if (bankInfo == null) {
						_log.info("调用银行接口按证件号查询电子账号查询失败,身份证号:[" + idNo + "],开户用户ID:[" + userId + "].");
						logInfo.setStatus(3);// 开户失败
						this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(logInfo);
						continue;
					}
					String retCode = bankInfo.getRetCode() == null ? "" : bankInfo.getRetCode();
					if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode) && StringUtils.isNotBlank(bankInfo.getAccountId())) {// 开户掉单修复
						updateUserAccount(logList.get(i).getOrderId(), userId, logList.get(i).getClient(), bankInfo.getAccountId());
					} else {
						// _log.info("调用银行接口按证件号查询电子账号查询失败,身份证号:[" + idNo + "],开户用户ID:[" + userId + "].银行返回代码:[" + retCode + "]");
						logInfo.setStatus(3);// 开户失败
						this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(logInfo);
						continue;
					}
				} catch (Exception e) {
					logInfo.setStatus(3);// 开户失败
					this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(logInfo);
				}
			}
		}
	}

	/**
	 * 根据相应信息接口查询开户详情
	 * 
	 * @param accountId
	 * @param orgOrderId
	 * @return
	 */
	private BankCallBean queryOpenAccountList(String idNo, String idType, String userId) {
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_ACCOUNTID_QUERY);// 消息类型
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setIdType(idType);
		bean.setIdNo(idNo);
		bean.setLogUserId(userId);
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId)));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		bean.setLogRemark("按证件号查询电子账号");
		// 调用接口
		return BankCallUtils.callApiBg(bean);
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
		}
		return null;
	}

	/**
	 * 保存开户的数据
	 */
	public boolean updateUserAccount(String orderId, Integer userId, Integer client, String account) {

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
		// 查询返回的电子账号是否已开户
		boolean result = checkAccount(account);
		if (!result) {// 校验未通过
			_log.info("==========该电子账号已被用户关联,无法完成掉单修复!============关联电子账号: " + account);
			return false;
		}
		Users user = this.getUsersByUserId(userId);// 获取用户信息
		String userName = user.getUsername();
		Date nowDate = new Date();// 当前时间
		String idCard = openAccoutLog.getIdNo(); // 身份证号
		String trueName = null;// 真实姓名
		try {
			trueName = URLDecoder.decode(openAccoutLog.getName(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("===========url编码异常!=============");
		}
		if (idCard != null && idCard.length() < 18) {
			try {
				idCard = IdCard15To18.getEighteenIDCard(idCard);
			} catch (Exception e) {
				System.out.println("===========url编码异常!=============");
			}
		}
		int sexInt = Integer.parseInt(idCard.substring(16, 17));// 性别
		if (sexInt % 2 == 0) {
			sexInt = 2;
		} else {
			sexInt = 1;
		}
		String birthDayTemp = idCard.substring(6, 14);// 出生日期
		String birthDay = StringUtils.substring(birthDayTemp, 0, 4) + "-" + StringUtils.substring(birthDayTemp, 4, 6) + "-" + StringUtils.substring(birthDayTemp, 6, 8);
		user.setBankOpenAccount(1);
		user.setBankAccountEsb(client);
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
		// 插入汇付关联表
		BankOpenAccount openAccount = new BankOpenAccount();
		openAccount.setUserId(userId);
		openAccount.setUserName(user.getUsername());
		openAccount.setAccount(account);
		openAccount.setCreateTime(nowDate);
		openAccount.setCreateUserId(userId);
		openAccount.setCreateUserName(userName);
		boolean openAccountFlag = this.bankOpenAccountMapper.insertSelective(openAccount) > 0 ? true : false;
		if (!openAccountFlag) {
			throw new RuntimeException("插入用户开户表失败！");
		}
		String bank = BankUtil.getNameOfBank(openAccoutLog.getCardNo());
		// 插入相应的银行卡
		BankCard bankCard = new BankCard();
		bankCard.setUserId(userId);
		bankCard.setUserName(userName);
		bankCard.setCardNo(openAccoutLog.getCardNo());
		bankCard.setBank(bank);
		bankCard.setStatus(1);
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
		List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper.selectByExample(example);
		if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
			AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
			channelDetail.setOpenAccountTime(new Date());
			this.appChannelStatisticsDetailMapper.updateByPrimaryKeySelective(channelDetail);
		}
		return true;
	}

	/**
	 * 校验返回的电子账号是否已被使用
	 * 
	 * @param account
	 * @return
	 */
	private boolean checkAccount(String account) {
		// 根据account查询用户是否开户
		BankOpenAccountExample example = new BankOpenAccountExample();
		example.createCriteria().andAccountEqualTo(account);
		List<BankOpenAccount> bankOpenList = this.bankOpenAccountMapper.selectByExample(example);
		if (bankOpenList != null && bankOpenList.size() > 0) {
			for (int i = 0; i < bankOpenList.size(); i++) {
				Integer userId = bankOpenList.get(i).getUserId();
				UsersExample userExample = new UsersExample();
				userExample.createCriteria().andUserIdEqualTo(userId);
				List<Users> user = this.usersMapper.selectByExample(userExample);
				if (user != null && user.size() > 0) {
					for (int j = 0; j < user.size(); j++) {
						Users info = user.get(j);
						Integer bankOpenFlag = info.getBankOpenAccount();
						if (bankOpenFlag == 1) {
							return false;
						}
					}
				}
			}

		}
		return true;
	}
}
