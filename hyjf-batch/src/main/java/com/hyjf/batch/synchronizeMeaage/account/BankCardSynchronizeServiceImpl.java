package com.hyjf.batch.synchronizeMeaage.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.mapper.auto.AccountMobileAynchMapper;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 * @author lisheng
 *
 */
@Service
public class BankCardSynchronizeServiceImpl extends BaseServiceImpl implements BankCardSynchronizeService {
	@Autowired
	AccountMobileAynchMapper accountMobileAynchMapper;
	/** 类名 */
	private static final String THIS_CLASS = AccountSynchronizeTask.class.getName();
	Logger _log = LoggerFactory.getLogger(AccountSynchronizeTask.class);
	@Override
	public boolean updateAccountBankByUserId(AccountMobileAynch accountMobileAynch) {
		Integer userId = accountMobileAynch.getUserId();
		String account = accountMobileAynch.getAccount();
		String respCode = "";
		//查询次数加一
		Integer searchtime = accountMobileAynch.getSearchtime()+1;
		accountMobileAynch.setSearchtime(searchtime);
		try {
			LogUtil.startLog(BankCardSynchronizeServiceImpl.class.getName(), "updateAccountBankByUserId", "开始更新用户银行卡信息,用户ID:" + userId);
			BankOpenAccountExample example = new BankOpenAccountExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<BankOpenAccount> list = bankOpenAccountMapper.selectByExample(example);
			if (list != null && list.size() != 0) {
				BankOpenAccount bankOpenAccount = list.get(0);
				Users user = this.getUsers(userId);
				// 调用汇付接口(4.2.2 用户绑卡接口)
				BankCallBean bean = new BankCallBean();
				bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
				bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_BIND_DETAILS_QUERY);
				bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
				bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
				bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
				bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
				bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
				bean.setChannel(BankCallConstant.CHANNEL_APP);// 交易渠道
				bean.setAccountId(bankOpenAccount.getAccount());// 存管平台分配的账号
				bean.setState("1"); // 查询状态 0-所有（默认） 1-当前有效的绑定卡
				bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
				bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
				bean.setLogRemark("绑卡关系查询");
				bean.setLogUserId(String.valueOf(userId));
				// 调用汇付接口 4.4.11 银行卡查询接口
				BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
				respCode = bankCallBean == null ? "" : bankCallBean.getRetCode();
				BankCardExample bankCardExample = new BankCardExample();
				BankCardExample.Criteria cri = bankCardExample.createCriteria();
				cri.andUserIdEqualTo(bankOpenAccount.getUserId());
				// 如果接口调用成功
				if (BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
					String cardNo ="";
					String txnDate = "";
					String txnTime = "";
					String usrCardInfolist = bankCallBean.getSubPacks();
					JSONArray array = JSONObject.parseArray(usrCardInfolist);
					if (array != null && array.size() != 0) {
						List<BankCard> bankCardList = new ArrayList<BankCard>();
						JSONObject obj = array.getJSONObject(0);
						cardNo=obj.getString("cardNo");
						txnDate=obj.getString("txnDate");
						txnTime=obj.getString("txnTime");
					}
					if(!StringUtils.equals(cardNo,account)){

						BankCard bank = new BankCard();
						bank.setUserId(bankOpenAccount.getUserId());
						bank.setUserName(user.getUsername());
						bank.setStatus(1);// 默认都是1
						bank.setCardNo(cardNo);
						// 根据银行卡号查询银行ID
						String bankId = this.getBankIdByCardNo(cardNo);
                        _log.info(THIS_CLASS + "==>" + bankId + "==>" + "bankId！");
						bank.setBankId(!StringUtils.isNotBlank(bankId) ? 0 : Integer.valueOf(bankId));
						bank.setBank(!StringUtils.isNotBlank(bankId) ? "" : this.getBankNameById(bankId));
						// 银行联号
						String payAllianceCode = "";
						// 调用江西银行接口查询银行联号
						BankCallBean payAllianceCodeQueryBean = this.payAllianceCodeQuery(cardNo, userId);
						if (payAllianceCodeQueryBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(payAllianceCodeQueryBean.getRetCode())) {
							payAllianceCode = payAllianceCodeQueryBean.getPayAllianceCode();
						}
						// 如果此时银行联号还是为空,调用本地查询银行联号
						if (StringUtils.isBlank(payAllianceCode)) {
						    if(StringUtils.isNotBlank(bankId)){
                                payAllianceCode = this.getPayAllianceCodeByBankId(bankId);
                            }else{
                                payAllianceCode="";
                            }

						}
						bank.setPayAllianceCode(payAllianceCode);
						SimpleDateFormat sdf = GetDate.yyyymmddhhmmss;
						bank.setCreateTime(sdf.parse(txnDate + txnTime));
						bank.setCreateUserId(userId);
						bank.setCreateUserName(user.getUsername());
						// 数据库操作
						int cardDeleteFlag = bankCardMapper.deleteByExample(bankCardExample);
						int cardAddFlag = bankCardMapper.insertSelective(bank);
						accountMobileAynch.setNewAccount(cardNo);
						accountMobileAynch.setStatus(1);
						int CardUpdateFlag = accountMobileAynchMapper.updateByPrimaryKeySelective(accountMobileAynch);
						boolean result = cardDeleteFlag > 0 && cardAddFlag > 0 && CardUpdateFlag > 0 ? true : false;
						return result;
					}else{
						boolean b = accountMobileAynchMapper.updateByPrimaryKeySelective(accountMobileAynch) > 0 ? true : false;
						return b;
					}
				}else{
					_log.info(THIS_CLASS + "==>" + userId + "==>" + "接口查询银行卡号异常！");
				}
			}
			LogUtil.endLog(BankCardSynchronizeServiceImpl.class.getName(), "updateAccountBankByUserId", "更新用户银行卡信息结束,用户ID:" + userId);
		} catch (Exception e) {
			LogUtil.errorLog(BankCardSynchronizeServiceImpl.class.getName(), "updateAccountBankByUserId", "更新用户银行卡信息失败,用户ID:" + userId, null);
			e.printStackTrace();
		}
		return  false;
	}


}
