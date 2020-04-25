package com.hyjf.admin.exception.bindcardexception;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.BindCardExceptionCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BindCardExceptionServiceImpl extends BaseServiceImpl implements BindCardExceptionService {

	/**
	 * 检索用户银行卡列表件数
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public int countBankCardList(Map<String, Object> param) {
		return this.bindCardExceptionCustomizeMapper.countBankCardList(param);
	}

	/**
	 * 检索用户银行卡列表
	 * 
	 * @param param
	 * @return
	 */
	@Override
	public List<BindCardExceptionCustomize> selectBankCardList(Map<String, Object> param) {
		return this.bindCardExceptionCustomizeMapper.selectBankCardList(param);
	}

	/**
	 * 更新用户银行卡信息
	 * 
	 * @param userId
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	@Override
	public void updateBindCard(Integer userId, String accountId) throws Exception {
		String respCode = "";
		try {
			LogUtil.startLog(BindCardExceptionServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "开始更新用户银行卡信息,用户ID:" + userId);
			BankOpenAccountExample example = new BankOpenAccountExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<BankOpenAccount> list = bankOpenAccountMapper.selectByExample(example);
			if (list != null && list.size() != 0) {
				BankOpenAccount bankOpenAccount = list.get(0);
				Users user = this.getUsersByUserId(userId);
				// 调用汇付接口(4.2.2 用户绑卡接口)
				BankCallBean bean = new BankCallBean();
				bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
				bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_BIND_DETAILS_QUERY);
				bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
				bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
				bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
				bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
				bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
				bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
				bean.setAccountId(bankOpenAccount.getAccount());// 存管平台分配的账号
				bean.setState("1"); // 查询状态 0-所有（默认） 1-当前有效的绑定卡
				bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
				bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
				bean.setLogUserId(String.valueOf(userId));
				// 调用汇付接口 4.4.11 银行卡查询接口
				BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
				respCode = bankCallBean == null ? "" : bankCallBean.getRetCode();
				BankCardExample bankCardExample = new BankCardExample();
				BankCardExample.Criteria cri = bankCardExample.createCriteria();
				cri.andUserIdEqualTo(bankOpenAccount.getUserId());
				// 如果接口调用成功
				if (BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
					String usrCardInfolist = bankCallBean.getSubPacks();
					JSONArray array = JSONObject.parseArray(usrCardInfolist);
					if (array != null && array.size() != 0) {
						List<BankCard> bankCardList = new ArrayList<BankCard>();
						for (int j = 0; j < array.size(); j++) {
							JSONObject obj = array.getJSONObject(j);
							BankCard bank = new BankCard();
							bank.setUserId(bankOpenAccount.getUserId());
							bank.setUserName(user.getUsername());
							bank.setStatus(1);// 默认都是1
							bank.setCardNo(obj.getString("cardNo"));
							// 根据银行卡号查询银行ID
							String bankId = this.getBankIdByCardNo(obj.getString("cardNo"));
							bank.setBankId(bankId == null ? 0 : Integer.valueOf(bankId));
							bank.setBank(bankId == null ? "" : this.getBankNameById(bankId));
							// 银行联号
							String payAllianceCode = "";
							// 调用江西银行接口查询银行联号
							BankCallBean payAllianceCodeQueryBean = this.payAllianceCodeQuery(obj.getString("cardNo"), userId);
							if (payAllianceCodeQueryBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(payAllianceCodeQueryBean.getRetCode())) {
								payAllianceCode = payAllianceCodeQueryBean.getPayAllianceCode();
							}
							// 如果此时银联行号还是为空,根据bankId查询本地存的银联行号
							if (StringUtils.isBlank(payAllianceCode)) {
								payAllianceCode = this.getPayAllianceCodeByBankId(bankId);
							}
							bank.setPayAllianceCode(payAllianceCode);
							SimpleDateFormat sdf = GetDate.yyyymmddhhmmss;
							bank.setCreateTime(sdf.parse(obj.getString("txnDate") + obj.getString("txnTime")));
							bank.setCreateUserId(userId);
							bank.setCreateUserName(user.getUsername());
							bankCardList.add(bank);
						}
						int count = bankCardMapper.countByExample(bankCardExample);
						if (count > 0) {
							// 初始化状态即账户没卡时,不用操作数据库
							// 数据库操作
							boolean isDelFlag = bankCardMapper.deleteByExample(bankCardExample) > 0 ? true : false;
							if (!isDelFlag) {
								throw new Exception("银行卡删除失败~!,userid is " + bankOpenAccount.getUserId());
							}
						}
						for (BankCard bank : bankCardList) {
							boolean isInsertFlag = bankCardMapper.insertSelective(bank) > 0 ? true : false;
							if (!isInsertFlag) {
								throw new Exception("银行卡插入失败~!");
							}
						}
					}
				}
			}
			LogUtil.endLog(BindCardExceptionServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "更新用户银行卡信息结束,用户ID:" + userId);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.errorLog(BindCardExceptionServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "更新用户银行卡信息失败,用户ID:" + userId, null);
		}
	}

}
