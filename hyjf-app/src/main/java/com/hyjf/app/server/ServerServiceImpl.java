/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:50:02
 * Modification History:
 * Modified by :
 */

package com.hyjf.app.server;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.AppAccessStatistics;

@Service
public class ServerServiceImpl extends BaseServiceImpl implements ServerService {

    @Override
    public int insertAccessInfo(AppAccessStatistics accessStatistics) {
        return appAccessStatisticsMapper.insertSelective(accessStatistics);
    }

}
