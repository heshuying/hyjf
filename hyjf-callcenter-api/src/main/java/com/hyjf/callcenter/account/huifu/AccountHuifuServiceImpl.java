/**
 * Description:按照用户名/手机号查询汇付绑卡关系用Service实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.account.huifu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterAccountHuifuCustomize;

@Service
public class AccountHuifuServiceImpl extends CustomizeMapper implements AccountHuifuService {

	/**
	 * 按照用户名/手机号查询汇付绑卡关系
	 * @param user
	 * @return List<CallcenterAccountHuifuCustomize>
	 * @author 刘彬
	 */
	public List<CallcenterAccountHuifuCustomize> getRecordList(Users user,Integer limitStart, Integer limitEnd) {
		// 封装查询条件
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("userName", user.getUsername());
		conditionMap.put("limitStart", limitStart);
		conditionMap.put("limitEnd", limitEnd);
		// 查询用户列表
		List<CallcenterAccountHuifuCustomize> users = callcenterAccountHuifuCustomizeMapper.selectBankCardList(conditionMap);
		return users;
	}
}


