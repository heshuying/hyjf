/**
 * Description:按照用户名/手机号查询江西银行绑卡关系用Service实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.account.bank;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.Users;

@Service
public class AccountBankServiceImpl extends CustomizeMapper implements AccountBankService {
	/**
	 * 按照用户名/手机号查询江西银行绑卡关系
	 * @param user
	 * @return List<BankCard>
	 * @author 刘彬
	 */
	@Override
	public List<BankCard> getTiedCardOfAccountBank(Users user) {
		
		BankCardExample example = new BankCardExample();
        example.createCriteria().andUserIdEqualTo(user.getUserId());
        
        List<BankCard> list = bankCardMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
        	return list;
        }	
        return null;
	}
    
}


