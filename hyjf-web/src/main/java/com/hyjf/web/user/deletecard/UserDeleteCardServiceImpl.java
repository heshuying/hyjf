/**
 * Description:用户删除银行卡实现类
 */

package com.hyjf.web.user.deletecard;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.common.util.GetDate;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.web.BaseServiceImpl;

@Service
public class UserDeleteCardServiceImpl extends BaseServiceImpl implements UserDeleteCardService {

    /**
     * 用户删除银行卡后调用方法
     *
     * @param bean
     * @return
     */
    public synchronized String updateAfterDeleteCard(ChinapnrBean bean, Integer userId) {
        int nowTime = GetDate.getNowTime10(); // 当前时间
        int ret = 0;
        AccountBankExample accountBankExample = new AccountBankExample();
        AccountBankExample.Criteria aCriteria = accountBankExample.createCriteria();
        aCriteria.andUserIdEqualTo(userId);
        aCriteria.andAccountEqualTo(bean.getCardId()); // 银行卡账号
        List<AccountBank> accountBank = this.accountBankMapper.selectByExample(accountBankExample);
        this.accountBankMapper.deleteByExample(accountBankExample);

        // 插入操作记录表
        BankAccountLog bankAccountLog = new BankAccountLog();
        bankAccountLog.setUserId(userId);
        bankAccountLog.setUserName(this.getUsers(userId).getUsername());
        bankAccountLog.setBankCode(accountBank.get(0).getBank());
        bankAccountLog.setBankAccount(bean.getCardId());

        // 获取银行卡姓名
        BankConfig config = this.getBankcardConfig(bankAccountLog.getBankCode());
        if (config != null) {
            bankAccountLog.setBankName(config.getName());
        } else {
            bankAccountLog.setBankName("");
        }
        bankAccountLog.setCardType(0);// 卡类型 0普通提现卡1默认卡2快捷支付卡
        bankAccountLog.setOperationType(1);// 操作类型 0绑定 1删除
        bankAccountLog.setStatus(0);// 成功
        bankAccountLog.setCreateTime(nowTime);// 操作时间
        ret += this.bankAccountLogMapper.insertSelective(bankAccountLog);
        return ret + "";
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

	@Override
	public AccountBank getUserBankCard(int cardId) {
		return this.accountBankMapper.selectByPrimaryKey(cardId);
	}

	@Override
    public BankCallBean getBankBalanceCall(Integer userId, String accountId) {
        BankCallBean resultBean = null;
        // 账户可用余额
        BigDecimal balance = BigDecimal.ZERO;
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
        bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
        bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
        bean.setAccountId(accountId);// 电子账号
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));// 订单号
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogRemark("电子账户余额查询");
        bean.setLogClient(0);// 平台
        try {
            resultBean = new BankCallBean();
            resultBean = BankCallUtils.callApiBg(bean);
            /*if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
            }*/
            return resultBean;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultBean;
    }

}
