package com.hyjf.admin.exception.bankrepayfreezeexception;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankRepayFreezeLog;
import com.hyjf.mybatis.model.auto.BankRepayFreezeLogExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BankRepayFreezeServiceImpl extends BaseServiceImpl implements BankRepayFreezeService {

	private Logger logger = LoggerFactory.getLogger(BankRepayFreezeServiceImpl.class);

	@Override
	public Integer selectCountRepayFreezeList() {
		BankRepayFreezeLogExample example = new BankRepayFreezeLogExample();
		example.createCriteria().andDelFlagEqualTo(0);
		int countByExample = this.bankRepayFreezeLogMapper.countByExample(example);
		return countByExample;
	}

	@Override
	public List<BankRepayFreezeLog> selectBankFreezeList(BankRepayFreezeBean form) {
		BankRepayFreezeLogExample example = new BankRepayFreezeLogExample();
		example.createCriteria().andDelFlagEqualTo(0);
		example.setLimitStart(form.getLimitStart());
		example.setLimitEnd(form.getLimitEnd());
		example.setOrderByClause(" id ASC ");
		List<BankRepayFreezeLog> freezeLogList = this.bankRepayFreezeLogMapper.selectByExample(example);
		return freezeLogList;
	}

	@Override
	public BankRepayFreezeLog selectBankFreezeLog(String orderId) {
		BankRepayFreezeLogExample example = new BankRepayFreezeLogExample();
		example.createCriteria().andOrderIdEqualTo(orderId).andDelFlagEqualTo(0);
		List<BankRepayFreezeLog> repayFreezeLogs = this.bankRepayFreezeLogMapper.selectByExample(example);
		if (repayFreezeLogs != null && repayFreezeLogs.size() > 0) {
			return repayFreezeLogs.get(0);
		}
		return null;
	}

	@Override
	public boolean repayUnfreeze(BankRepayFreezeLog repayFreezeFlog) {

		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bean.setTxCode(BankCallConstant.TXCODE_BALANCE_UNFREEZE);// 消息类型
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
		bean.setTxDate(GetOrderIdUtils.getTxDate());
		bean.setTxTime(GetOrderIdUtils.getTxTime());
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
		bean.setChannel(BankCallConstant.CHANNEL_PC);
		bean.setAccountId(repayFreezeFlog.getAccount());// 电子账号
		bean.setOrderId(GetOrderIdUtils.getUsrId(repayFreezeFlog.getUserId()));// 订单号
		bean.setOrgOrderId(repayFreezeFlog.getOrderId());// 原订单号
		bean.setProductId(repayFreezeFlog.getBorrowNid());
		bean.setTxAmount(String.valueOf(repayFreezeFlog.getAmount()));// 冻结资金
		bean.setLogUserId(String.valueOf(repayFreezeFlog.getUserId())); // 操作者ID
		bean.setLogOrderId(GetOrderIdUtils.getUsrId(repayFreezeFlog.getUserId()));
		bean.setLogClient(0);
		// 调用接口
		BankCallBean callApiBg = BankCallUtils.callApiBg(bean);
		if (Validator.isNotNull(callApiBg)) {
			logger.info("记录状态: {}", callApiBg.getState());

			String retCode = callApiBg.getRetCode();
			if (StringUtils.isBlank(retCode)) {
				logger.info("=============返回码为空===========");
				return false;
			}
			if (BankCallConstant.RESPCODE_SUCCESS.equals(retCode)) {
				logger.info("===========冻结订单号为: {}", repayFreezeFlog.getOrderId());
				return true;
			} else if (retCode.equals(BankCallConstant.RETCODE_UNFREEZE_NOT_EXIST)
					|| retCode.equals(BankCallConstant.RETCODE_FREEZE_FAIL)
					|| retCode.equals(BankCallConstant.RETCODE_UNFREEZE_ALREADY)) {
				logger.info("===============冻结记录不存在,不予处理========");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateBankRepayFreeze(BankRepayFreezeLog repayFreezeFlog) {
		boolean updateFlag = this.bankRepayFreezeLogMapper.deleteByPrimaryKey(repayFreezeFlog.getId()) > 0 ? true
				: false;
		return updateFlag;
	}

}
