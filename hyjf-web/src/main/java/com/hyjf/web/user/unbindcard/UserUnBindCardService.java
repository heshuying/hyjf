/**
 * Description:用户绑卡
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:45:13
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.user.unbindcard;

import com.hyjf.web.BaseService;

public interface UserUnBindCardService extends BaseService {
	/**
	 * 根据用户客户号获取用户userid
	 * @param custId
	 * @return
	 */
	public Integer getUserIdByCustId(Long custId);

	/**
	 * 根据userId调用汇付接口更新该用户的银行卡信息
	 * 
	 * @param userId
	 *            用户在汇盈金服的userId
	 * @return
	 */
	public String updateAccountBankByUserId(Integer userId);

}
