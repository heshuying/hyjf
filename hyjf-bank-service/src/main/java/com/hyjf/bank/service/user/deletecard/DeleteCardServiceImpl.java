package com.hyjf.bank.service.user.deletecard;

import java.util.List;

import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.util.GetDate;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户删除银行卡Service实现类
 * 
 * @author liuyang
 *
 */
@Service
public class DeleteCardServiceImpl extends BaseServiceImpl implements DeleteCardService {

	/**
	 * 用户删除银行卡后调用方法
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean updateAfterDeleteCard(BankCallBean bean, Integer userId) throws Exception {
		int nowTime = GetDate.getNowTime10(); // 当前时间
		boolean ret = false;
		LogAcqResBean logAcqResBean = bean.getLogAcqResBean();
		BankCardExample bankCardExample = new BankCardExample();
		BankCardExample.Criteria aCriteria = bankCardExample.createCriteria();
		aCriteria.andUserIdEqualTo(userId);
		aCriteria.andCardNoEqualTo(logAcqResBean.getCardNo()); // 银行卡账号
		aCriteria.andIdEqualTo(logAcqResBean.getCardId());// 银行卡Id
		List<BankCard> accountBank = this.bankCardMapper.selectByExample(bankCardExample);
		boolean isDeleteFlag = this.bankCardMapper.deleteByExample(bankCardExample) > 0 ? true : false;
		if (!isDeleteFlag) {
			throw new Exception("删除银行卡失败,请联系客服人员!");
		}
		// 插入操作记录表
		BankAccountLog bankAccountLog = new BankAccountLog();
		bankAccountLog.setUserId(userId);
		bankAccountLog.setUserName(this.getUsers(userId).getUsername());
		bankAccountLog.setBankCode(String.valueOf(accountBank.get(0).getBankId()));
		bankAccountLog.setBankAccount(logAcqResBean.getCardNo());
		bankAccountLog.setBankName(accountBank.get(0).getBank());
		bankAccountLog.setCardType(0);// 卡类型 0普通提现卡1默认卡2快捷支付卡
		bankAccountLog.setOperationType(1);// 操作类型 0绑定 1删除
		bankAccountLog.setStatus(0);// 成功
		bankAccountLog.setCreateTime(nowTime);// 操作时间
		ret = this.bankAccountLogMapper.insertSelective(bankAccountLog) > 0 ? true : false;
		return ret;
	}

	/**
	 * 获取用户的身份证号
	 *
	 * @param userId
	 * @return 用户的身份证号
	 */
	public String getUserIdcard(Integer userId) {
		// 取得身份证号
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		usersInfoExample.createCriteria().andUserIdEqualTo(userId);
		List<UsersInfo> listUsersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);
		if (listUsersInfo != null && listUsersInfo.size() > 0) {
			return listUsersInfo.get(0).getIdcard();
		}
		return "";
	}

	/**
	 * 获取银行列表
	 * 
	 * @param string
	 * @return
	 * @author Administrator
	 */
	public BankConfig getBankcardConfig(String bankCode) {
		BankConfigExample example = new BankConfigExample();
		BankConfigExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(bankCode)) {
			cra.andCodeEqualTo(bankCode);
		} else {
			return null;
		}
		List<BankConfig> banks = bankConfigMapper.selectByExample(example);
		if (banks != null && banks.size() > 0) {
			return banks.get(0);
		}
		return null;
	}

	/**
	 * 根据用户id和银行卡编号检索银行卡信息
	 */
	@Override
	public BankCard getBankCardByCardNO(Integer userId, String cardNo) {
		BankCardExample example = new BankCardExample();
		BankCardExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId).andCardNoEqualTo(cardNo);
		List<BankCard> list = bankCardMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据用户Id,银行卡Id查询用户银行卡信息
	 * 
	 * @param userId
	 * @param cardId
	 * @return
	 */
	@Override
	public BankCard getBankCardById(Integer userId, String cardId) {
		BankCardExample example = new BankCardExample();
		BankCardExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId).andIdEqualTo(Integer.parseInt(cardId));
		List<BankCard> list = bankCardMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}
	/**
     * 根据用户Id,银行卡卡号查询用户银行卡信息
     * 
     * @param userId
     * @param cardId
     * @return
     */
    @Override
    public BankCard getBankCardByCardNo(Integer userId, String cardNo) {
        BankCardExample example = new BankCardExample();
        BankCardExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        cra.andCardNoEqualTo(cardNo);
        List<BankCard> list = bankCardMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

	/**
	 * 解卡页面调用(合规四期需求)
	 * @param bean
	 * @return
	 */
	@Override
	public ModelAndView getCallbankMV(DeleteCardPageBean bean) {
		ModelAndView mv = new ModelAndView();
		// 获取共同参数
		String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
		String bankInstCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
		String orderDate = GetOrderIdUtils.getOrderDate();
		String txDate = GetOrderIdUtils.getTxDate();
		String txTime = GetOrderIdUtils.getTxTime();
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		String idType = BankCallConstant.ID_TYPE_IDCARD;

		// 调用开户接口
		BankCallBean bindCardBean = new BankCallBean();
		//通用必填参数
		bindCardBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		bindCardBean.setInstCode(bankInstCode);// 机构代码
		bindCardBean.setBankCode(bankCode);
		bindCardBean.setTxDate(txDate);
		bindCardBean.setTxTime(txTime);
		bindCardBean.setSeqNo(seqNo);
		bindCardBean.setChannel(bean.getChannel());

		//解卡参数
		bindCardBean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_UNBINDCARD_PAGE);
		bindCardBean.setAccountId(bean.getAccountId());
		bindCardBean.setName(bean.getName());
		bindCardBean.setIdType(idType);
		bindCardBean.setIdNo(bean.getIdNo());
		bindCardBean.setCardNo(bean.getCardNo());
		bindCardBean.setMobile(bean.getMobile());
		bindCardBean.setRetUrl(bean.getRetUrl());
		bindCardBean.setForgotPwdUrl(bean.getForgotPwdUrl());
		bindCardBean.setSuccessfulUrl(bean.getSuccessfulUrl());
		bindCardBean.setNotifyUrl(bean.getNotifyUrl());

		// 页面调用必须传的
		String orderId = GetOrderIdUtils.getOrderId2(bean.getUserId());
		bindCardBean.setLogBankDetailUrl(BankCallConstant.BANK_URL_UNBIND_CARD_PAGE);
		bindCardBean.setLogOrderId(orderId);
		bindCardBean.setLogOrderDate(orderDate);
		bindCardBean.setLogUserId(String.valueOf(bean.getUserId()));
		bindCardBean.setLogRemark("外部服务接口:绑卡页面");
		bindCardBean.setLogIp(bean.getIp());
		bindCardBean.setLogClient(Integer.parseInt(bean.getPlatform()));
		try {
			mv = BankCallUtils.callApi(bindCardBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}

	/**
	 * 解绑银行卡后(异步回调删除)
	 * 合规四期(解卡页面调用)
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@Override
	public synchronized boolean deleteBankCard(BankCallBean bean, Integer userId) throws Exception {
		int nowTime = GetDate.getNowTime10(); // 当前时间
		boolean ret = false;
		BankCardExample bankCardExample = new BankCardExample();
		BankCardExample.Criteria aCriteria = bankCardExample.createCriteria();
		aCriteria.andUserIdEqualTo(userId);
		List<BankCard> accountBank = this.bankCardMapper.selectByExample(bankCardExample);
		// 插入操作记录表
		BankAccountLog bankAccountLog = new BankAccountLog();
		bankAccountLog.setUserId(userId);
		bankAccountLog.setUserName(this.getUsers(userId).getUsername());
		bankAccountLog.setBankCode(String.valueOf(accountBank.get(0).getBankId()));
		bankAccountLog.setBankAccount(accountBank.get(0).getCardNo());
		bankAccountLog.setBankName(accountBank.get(0).getBank());
		//删除银行卡
		boolean isDeleteFlag = this.bankCardMapper.deleteByExample(bankCardExample) > 0 ? true : false;
		if (!isDeleteFlag) {
			throw new Exception("删除银行卡失败,请联系客服人员!");
		}
		bankAccountLog.setCardType(0);// 卡类型 0普通提现卡1默认卡2快捷支付卡
		bankAccountLog.setOperationType(1);// 操作类型 0绑定 1删除
		bankAccountLog.setStatus(0);// 成功
		bankAccountLog.setCreateTime(nowTime);// 操作时间
		ret = this.bankAccountLogMapper.insertSelective(bankAccountLog) > 0 ? true : false;
		return ret;
	}
}
