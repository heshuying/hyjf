/**
 * Description:按照用户名/手机号查询出借明细（汇添金）用Service接口
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: pcc
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.callcenter.invest.htj;

import java.util.List;

import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterHtjInvestCustomize;

public interface HtjInvestService {

	/**
	 * 按照用户名/手机号查询出借明细（汇添金）
	 * @param user
	 * @return List<CallcenterHztInvestCustomize>
	 * @author 刘彬
	 */
	public List<CallcenterHtjInvestCustomize> getRecordList(Users user,Integer limitStart, Integer limitEnd);
}
