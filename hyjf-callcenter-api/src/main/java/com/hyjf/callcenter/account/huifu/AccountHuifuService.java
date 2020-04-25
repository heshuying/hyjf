/**
 * Description:按照用户名/手机号查询汇付绑卡关系用Service接口
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 刘彬
 * @version: 1.0
 *           Created at: 2017年7月15日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.callcenter.account.huifu;

import java.util.List;

import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.callcenter.CallcenterAccountHuifuCustomize;

public interface AccountHuifuService {

	/**
	 * 按照用户名/手机号查询汇付绑卡关系
	 * @param user
	 * @return List<CallcenterAccountHuifuCustomize>
	 * @author 刘彬
	 */
	public List<CallcenterAccountHuifuCustomize> getRecordList(Users user,Integer limitStart, Integer limitEnd);
}
