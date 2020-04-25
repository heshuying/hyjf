/**
 * Description:按照用户名/手机号查询出借明细（汇添金）用Service实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: pcc
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.callcenter.invest.htj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterHtjInvestCustomize;

@Service
public class HtjInvestServiceImpl extends CustomizeMapper implements HtjInvestService {

	/**
	 * 按照用户名/手机号查询出借明细（汇添金）
	 * @param user
	 * @return List<CallcenterHztInvestCustomize>
	 * @author 刘彬
	 */
	public List<CallcenterHtjInvestCustomize> getRecordList(Users user,Integer limitStart, Integer limitEnd) {
	 // 封装查询条件
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("userId", user.getUserId());
        paraMap.put("limitStart", limitStart);
        paraMap.put("limitEnd", limitEnd);
		// 查询用户列表
		List<CallcenterHtjInvestCustomize> users = callcenterHtjInvestCustomizeMapper.selectBorrowInvestList(paraMap);
		return users;
	}
}


