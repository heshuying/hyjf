/**
 * Description:用户出借服务
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.app.discovery;

import com.hyjf.app.BaseService;

public interface DiscoveryService extends BaseService {
    
	String getEvalationResultByUserId(Integer userId);
	
	String getEvalationMessage();

}
