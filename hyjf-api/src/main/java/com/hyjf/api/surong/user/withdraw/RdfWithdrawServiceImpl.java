package com.hyjf.api.surong.user.withdraw;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Accountwithdraw;
import com.hyjf.mybatis.model.auto.AccountwithdrawExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;
@Service
public class RdfWithdrawServiceImpl extends BaseServiceImpl implements RdfWithdrawService{
	
	Logger _log = LoggerFactory.getLogger(RdfWithdrawServiceImpl.class);

	@Override
	public void getWithdrawResult(Integer userId, String orderId,Map<String, String> result) {
		// 根据用户ID查询用户银行卡信息
		BankCard bankCard = this.selectBankCardByUserId(userId);
		AccountwithdrawExample accountWithdrawExample = new AccountwithdrawExample();
		accountWithdrawExample.createCriteria().andNidEqualTo(orderId);
		List<Accountwithdraw> listAccountWithdraw = this.accountwithdrawMapper.selectByExample(accountWithdrawExample);
		if(!listAccountWithdraw.isEmpty()){
			// 提现信息
		    Accountwithdraw accountWithdraw = listAccountWithdraw.get(0);
		    result.put("cardNo", bankCard.getCardNo());
		    result.put("total", accountWithdraw.getTotal()==null?"0":accountWithdraw.getTotal().toString());
		    result.put("balance", accountWithdraw.getCredited()==null?"0":accountWithdraw.getCredited().toString());
		    result.put("fee", accountWithdraw.getFee());
		    result.put("orderId", orderId);
		    if(accountWithdraw.getStatus().equals("2")){
		    	result.put("status", "0");
		    }
		}
	}
	
	
	/**
	 * 根据用户ID查询用户银行卡信息
	 * 
	 * @param userId
	 * @return
	 */
	private BankCard selectBankCardByUserId(Integer userId) {
		BankCardExample example = new BankCardExample();
		BankCardExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		List<BankCard> list = this.bankCardMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


	@Override
	public String getBankLogoByBankName(String name) {
		if(StringUtils.isBlank(name)){
			return null;
		}
		if(name.contains("农业银行")){
			name = "中国农业银行";
		}
		if(name.contains("工商银行")){
			name = "中国工商银行";
		}
		if(name.contains("建设银行")){
			name = "中国建设银行";
		}
		if(name.contains("招商银行")){
			name = "招商银行";
		}
		if(name.contains("邮政储蓄银行")){
			name = "中国邮政储蓄";
		}
		if(name.contains("平安银行")){
			name = "平安银行";
		}
		if(name.contains("民生银行")){
			name = "中国民生银行";
		}
		if(name.contains("光大银行")){
			name = "中国光大银行";
		}
		BankConfigExample example = new BankConfigExample();
		BankConfigExample.Criteria cra = example.createCriteria();
		cra.andNameEqualTo(name);
		List<BankConfig> bankcard = bankConfigMapper.selectByExample(example);
		if(!bankcard.isEmpty()){
			return bankcard.get(0).getAppLogo();
		}
		return null;
	}
	
}
