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
	
package com.hyjf.batch.eve.batchAleve;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.AleveLog;
import com.hyjf.mybatis.model.auto.AleveLogExample;


/**
 * @author liubin
 */

@Service("AleveService")
public class AleveFileServiceImpl extends CustomizeMapper implements AleveFileService {


	
		
	@Override
	public void saveFile(ArrayList<AleveLog> list) {
	    for (AleveLog aleve : list) {
	        int nowTime = GetDate.getNowTime10();
	        aleve.setCreateTime(nowTime);
	        aleve.setCreateUserId(1028);
	        aleve.setDelFlg(0);
	        aleveLogMapper.insert(aleve);
        }
	}
	@Override
    public int countByExample(String beforeDay){
	    AleveLogExample example = new AleveLogExample();
	    AleveLogExample.Criteria criteria = example.createCriteria();
	    criteria.andCreateDayEqualTo(beforeDay);
	    return aleveLogMapper.countByExample(example);
    }
}
	