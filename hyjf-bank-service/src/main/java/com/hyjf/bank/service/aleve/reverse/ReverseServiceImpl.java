package com.hyjf.bank.service.aleve.reverse;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.customize.AleveCustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.customize.AleveLogCustomize;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *自动同步利息业务类
 */
@Service
public class ReverseServiceImpl extends BaseServiceImpl implements ReverseService {
	Logger _log = LoggerFactory.getLogger(ReverseServiceImpl.class);
	
	@Autowired
	private AleveCustomizeMapper aleveCustomizeMapper;
	
	/**
	 * 检查导入的ALEVE数据中是否含有利息相关记录
	 * @param map
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	
	@Override
	public List<AleveLogCustomize> selectAleveReverseList(List<String> tranStype) {
		return this.aleveCustomizeMapper.queryAleveLogListByTranstype(tranStype);	
	}

	/**
	 * 检索手动冲正数量
	 * @param form
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public int countManualReverse(AleveLogCustomize aleveLogCustomize) {

		//通过账号ID获取用户信息
        List<String> userIds = this.selectUserIdsByAccount(aleveLogCustomize.getCardnbr());
        if (userIds.isEmpty()) {
        	_log.error("【自动冲正异常】获取用户信息失败、电子账号：" + aleveLogCustomize.getCardnbr());
        	return 1;
        } else if (userIds.size() > 1) {
        	_log.error("【自动冲正异常】获取用户信息不唯一、电子账号：" + aleveLogCustomize.getCardnbr());
			return 1;
		}
        
        String userIdStr = userIds.get(0);
        if (StringUtils.isBlank(userIdStr)) {
            _log.error("【自动冲正异常】获取用户信息异常、电子账号：" + aleveLogCustomize.getCardnbr());
            return 1;
        }
		Map<String, Object> param = new HashMap<String, Object>();
		
		// 用户名
		param.put("userIdSrch", userIdStr);
		// 原交易流水号
		if (aleveLogCustomize.getSeqno() > 0) {
			param.put("seqNoSrch", aleveLogCustomize.getSeqno());
		}
		// 电子账号
		if (StringUtils.isNotEmpty(aleveLogCustomize.getCardnbr())) {
			param.put("accountIdSrch", aleveLogCustomize.getCardnbr());
		}
		
		int count = this.manualReverseCustomizeMapper.countManualReverse(param);
		return count;
	}

	/**
	 * 冲正处理
	 * @param aleveLog
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public boolean forReverse(AleveLogCustomize aleveLogCustomize) {

		//冲正金额
		BigDecimal total = aleveLogCustomize.getAmount();

        //当前系统时间（数字类型、入库用）
        int nowTime = GetDate.getNowTime10();

		//通过账号ID获取用户信息
        List<String> userIds = this.selectUserIdsByAccount(aleveLogCustomize.getCardnbr());
        if (userIds.isEmpty()) {
        	_log.error("【自动冲正异常】获取用户信息失败、电子账号：" + aleveLogCustomize.getCardnbr());
        	return false;
        } else if (userIds.size() > 1) {
        	_log.error("【自动冲正异常】获取用户信息不唯一、电子账号：" + aleveLogCustomize.getCardnbr());
			return false;
		}
        
        String userIdStr = userIds.get(0);
        if (StringUtils.isBlank(userIdStr)) {
            _log.error("【自动冲正异常】获取用户信息异常、电子账号：" + aleveLogCustomize.getCardnbr());
            return false;
        }
        Integer userId = Integer.parseInt(userIdStr);

        // 重新获取用户信息
		Account account = this.getAccount(userId);
		// 写入收支明细
		AccountList accountList = new AccountList();
		// 账户信息
		// 订单号：空
		//accountList.setNid(GetOrderIdUtils.getOrderId2(user.getUserId()));
		accountList.setUserId(userId);
		accountList.setAmount(total);
		//list表->1收入2支出
		accountList.setType(1);
		accountList.setTrade("auto_reverse");
		//操作识别码 balance余额操作 frost冻结操作 await待收操作
		accountList.setTradeCode("balance");
		accountList.setTotal(account.getTotal());
		accountList.setBalance(account.getBalance());
		accountList.setFrost(account.getFrost());
		accountList.setAwait(account.getAwait());
		accountList.setRepay(account.getRepay());
		accountList.setBankTotal(account.getBankTotal().add(total)); // 银行总资产
		accountList.setBankBalance(account.getBankBalance().add(total)); // 银行可用余额
		accountList.setBankFrost(account.getBankFrost());// 银行冻结金额
		accountList.setBankWaitCapital(account.getBankWaitCapital());// 银行待还本金
		accountList.setBankWaitInterest(account.getBankWaitInterest());// 银行待还利息
		accountList.setBankAwaitCapital(account.getBankAwaitCapital());// 银行待收本金
		accountList.setBankAwaitInterest(account.getBankAwaitInterest());// 银行待收利息
		accountList.setBankAwait(account.getBankAwait());// 银行待收总额
		accountList.setBankInterestSum(account.getBankInterestSum()); // 银行累计收益
		accountList.setBankInvestSum(account.getBankInvestSum());// 银行累计出借
		accountList.setBankWaitRepay(account.getBankWaitRepay());// 银行待还金额
		accountList.setPlanBalance(account.getPlanBalance());//汇计划账户可用余额
		accountList.setPlanFrost(account.getPlanFrost());
		accountList.setSeqNo(aleveLogCustomize.getSeqno().toString());
		accountList.setTxDate(nowTime);
		accountList.setTxTime(nowTime);
		//accountList.setBankSeqNo(form.getBankSeqNo());
		accountList.setAccountId(aleveLogCustomize.getCardnbr());
		//accountList.setRemark("原交易订单号 " + form.getBankSeqNo());
		accountList.setCreateTime(nowTime);
		accountList.setBaseUpdate(nowTime);
		//操作员
		//accountList.setOperator(createUserId);
		//操作IP
		//accountList.setIp(createIp);
		accountList.setIsUpdate(0);
		accountList.setBaseUpdate(0);
		accountList.setInterest(null);
		accountList.setIsBank(1);
		accountList.setWeb(0);
		accountList.setCheckStatus(1);// 对账状态0：未对账 1：已对账
		accountList.setTradeStatus(1);// 0失败1成功2失败
		//插入资产明细
		boolean isAccountListFlag = this.accountListMapper.insertSelective(accountList) > 0 ? true : false;
		if (!isAccountListFlag) {
			_log.error("【自动冲正异常】：插入资产明细失败:" + aleveLogCustomize.getCardnbr() + "----Seqno:" + aleveLogCustomize.getSeqno() + "----CreateTime:" + aleveLogCustomize.getCreateTime());
			return false;
		}
		//用户相应余额增加
		Account newAccount = new Account();
		
		newAccount.setUserId(userId);// 用户Id
		newAccount.setBankTotal(total); // 累加到账户总资产
		newAccount.setBankBalance(total); // 累加可用余额
		newAccount.setBankBalanceCash(total);// 江西银行可用余额
		//余额恢复
		boolean isAccountUpdateFlag = this.adminAccountCustomizeMapper.updateManualReverseSuccess(newAccount) > 0 ? true : false;
		if (!isAccountUpdateFlag) {
			_log.error("【自动冲正异常】：余额恢复失败:" + aleveLogCustomize.getCardnbr() + "----Seqno:" + aleveLogCustomize.getSeqno() + "----CreateTime:" + aleveLogCustomize.getCreateTime());
			return false;
		}

		//同步冲正后更新处理flg
		if(!this.updateAleveLog(aleveLogCustomize)) {
			_log.error("【自动冲正异常】：aleve数据处理完成字段更新失败:" + aleveLogCustomize.getCardnbr() + "----Seqno:" + aleveLogCustomize.getSeqno() + "----CreateTime:" + aleveLogCustomize.getCreateTime());
			return false;
		}
		return true;	
	}
}
