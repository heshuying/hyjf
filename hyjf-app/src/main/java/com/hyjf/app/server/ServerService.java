/**
 * Description:用户充值
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:45:13
 * Modification History:
 * Modified by :
 */
package com.hyjf.app.server;

import com.hyjf.app.BaseService;
import com.hyjf.mybatis.model.auto.AppAccessStatistics;

public interface ServerService extends BaseService {

    int insertAccessInfo(AppAccessStatistics accessStatistics);

}
