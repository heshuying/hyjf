package com.hyjf.batch.exception.transpassword;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BankTransPasswordExceptionServiceImpl extends BaseServiceImpl implements BankTransPasswordExceptionService {

	/** 设置交易密码状态:处理中 */
	private static final int ISSETPASSWORE_PROCESSING = 2;
	/** 江西银行开户状态:已开户 */
	private static final int ISOPENACCOUNT_SUCCESS = 1;

	Logger _log = LoggerFactory.getLogger(BankTransPasswordExceptionServiceImpl.class);

	@Override
	public void insertIsSetPassword() {

		// 查询出huiyingdai_users表isSetPassword为2的记录 2为处理中的状态
		UsersExample example = new UsersExample();
		example.createCriteria().andIsSetPasswordEqualTo(ISSETPASSWORE_PROCESSING);
		example.createCriteria().andBankOpenAccountEqualTo(ISOPENACCOUNT_SUCCESS);
		List<Users> list = this.usersMapper.selectByExample(example);
		// 遍历循环,根据银行的电子账号查询江西银行的设置交易密码记录,并将返回的是否设置了交易密码标示插入原表
		if (list != null && list.size() > 0) {
			_log.info("开始处理掉单的设置交易密码,处理数量:[" + list.size() + "]");
			for (Users users : list) {
				try {
					Integer userId = users.getUserId();
					_log.info("开始处理设置交易密码掉单,用户ID为:[" + userId + "]");
					// 根据userID 获得银行开户的电子账号
					BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
					// 用户电子账户号
					String accountId = bankOpenAccount == null ? "" : bankOpenAccount.getAccount();
					// 根据相应信息接口查询是否设置过交易密码
					BankCallBean bean = queryBankUsersList(accountId, String.valueOf(userId));
					if (bean != null && !StringUtils.isEmpty(bean.getPinFlag())) {
						users.setIsSetPassword(Integer.parseInt(bean.getPinFlag()));
						this.usersMapper.updateByPrimaryKey(users);
						_log.info("设置交易密码掉单异常处理成功,用户ID:[" + userId + "].");
					}
				} catch (Exception e) {
					_log.info("处理设置交易密码发生异常,异常信息:[" + e.getMessage() + "].");
				}
			}
		}
	}

	/**
	 * 查询用户的交易密码设置情况
	 * 
	 * @param accountId
	 * @param userId
	 * @return
	 */
	private BankCallBean queryBankUsersList(String accountId, String userId) {
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_PASSWORD_SET_QUERY);// 消息类型
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(accountId);// 电子账号
		bean.setLogUserId(userId);
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId)));
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
		bean.setLogRemark("电子账户密码是否设置查询");
		// 调用接口
		return BankCallUtils.callApiBg(bean);
	}

}
