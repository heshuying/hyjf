/**
 * Description:按照用户名/手机号查询江西银行绑卡关系用Service接口
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

import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.Users;

public interface AccountBankService {

	/**
	 * @param user
	 * @return
	 */
	List<BankCard> getTiedCardOfAccountBank(Users user);
}
