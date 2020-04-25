package com.hyjf.api.surong.user.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;

@Service
public class RdfAccountServiceImpl extends BaseServiceImpl implements RdfAccountService{

	@Override
	public String getBalance(String mobile) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andMobileEqualTo(mobile);
		List<Users> users = usersMapper.selectByExample(example);
		if(users.isEmpty()){
			return "0";
		}
		Integer userId = users.get(0).getUserId();
		//查询账户
		AccountExample accountExample = new AccountExample();
        AccountExample.Criteria accountCriteria = accountExample.createCriteria();
        accountCriteria.andUserIdEqualTo(userId);
        Account account = this.accountMapper.selectByExample(accountExample).get(0);
        if(account==null||account.getBankBalance()==null){
        	return "0";
        }
        return account.getBankBalance().toString();
	}

	@Override
	public BankCard getBankCard(String mobile) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria cra = example.createCriteria();
		cra.andMobileEqualTo(mobile);
		List<Users> users = usersMapper.selectByExample(example);
		if(users.isEmpty()){
			return null;
		}
		Integer userId = users.get(0).getUserId();
		BankCardExample accountBankExample = new BankCardExample();
		BankCardExample.Criteria aCriteria = accountBankExample.createCriteria();
		aCriteria.andUserIdEqualTo(userId);
		aCriteria.andStatusEqualTo(1);
		List<BankCard> list = this.bankCardMapper.selectByExample(accountBankExample);
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<Map<String, String>> balanceSync(List<Integer> ids) {
		AccountExample example =  new AccountExample();
		AccountExample.Criteria cra = example.createCriteria();
		cra.andUserIdIn(ids);
		List<Account> accounts = accountMapper.selectByExample(example);
		List<Map<String, String>> results = new ArrayList<>();
		for (Account account : accounts) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("hydId",account.getUserId().toString());
			map.put("balance", account.getBankBalance()==null?"0":account.getBankBalance()+"");
			results.add(map);
		}
		return results;
	}
}
