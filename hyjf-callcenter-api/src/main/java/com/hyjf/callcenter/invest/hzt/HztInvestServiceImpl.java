/**
 * Description:按照用户名/手机号查询出借明细（直投产品）用Service实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.invest.hzt;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterHztInvestCustomize;

@Service
public class HztInvestServiceImpl extends CustomizeMapper implements HztInvestService {

	/**
	 * 按照用户名/手机号查询出借明细（直投产品）
	 * @param user
	 * @return List<CallcenterHztInvestCustomize>
	 * @author 刘彬
	 */
	public List<CallcenterHztInvestCustomize> getRecordList(Users user,Integer limitStart, Integer limitEnd) {
		// 封装查询条件
		CallcenterHztInvestCustomize callcenterHztInvestCustomize = new CallcenterHztInvestCustomize(); 
		
		callcenterHztInvestCustomize.setUserId(user.getUserId()+"");
		callcenterHztInvestCustomize.setLimitStart(limitStart);
		callcenterHztInvestCustomize.setLimitEnd(limitEnd);
		// 查询用户列表
		List<CallcenterHztInvestCustomize> users = callcenterHztInvestCustomizeMapper.selectBorrowInvestList(callcenterHztInvestCustomize);
		return users;
	}
}


