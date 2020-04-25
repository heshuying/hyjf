/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.web.api.user.userinfo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.common.validator.CheckUtil;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.web.api.base.ApiBaseServiceImpl;

/**
 * @author liubin
 */

@Service("ApiUserInfoService")
public class ApiUserInfoServiceImpl extends ApiBaseServiceImpl implements ApiUserInfoService {
	
	/**
	 * 根据用户Id取得资产总额
	 * @param bean
	 * @return
	 * @author liubin
	 */
	@Override
	public String getTotleAssetsByUserId(Integer userId) {
		//检索用户资产总额
		AccountExample example = new AccountExample();
        example.createCriteria().andUserIdEqualTo(userId);
		List<Account> list = this.accountMapper.selectByExample(example);
		//无记录时，未绑定汇盈金服
		CheckUtil.check(list.size() > 0, "user.unbound");
		
		return list.get(0).getBankTotal().toString();	
	}
}

	