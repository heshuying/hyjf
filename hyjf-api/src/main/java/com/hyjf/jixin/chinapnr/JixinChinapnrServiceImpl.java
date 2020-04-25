/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.jixin.chinapnr;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ChinapnrSendlogExample;
import com.hyjf.mybatis.model.auto.ChinapnrSendlogWithBLOBs;

@Service
public class JixinChinapnrServiceImpl extends BaseServiceImpl implements JixinChinapnrService {

	@Override
	public String getContentOfChinapnr(JixinChinapnrBean bean) {
		String result = "";
        ChinapnrSendlogExample example = new ChinapnrSendlogExample();
        ChinapnrSendlogExample.Criteria criteria = example.createCriteria();
        criteria.andTxDateEqualTo(bean.getTxDate());
        criteria.andTxTimeEqualTo(bean.getTxTime());
        criteria.andSeqNoEqualTo(bean.getSeqNo());
        
        List<ChinapnrSendlogWithBLOBs> list = chinapnrSendlogMapper.selectByExampleWithBLOBs(example);
        if (list != null && !list.isEmpty()) {
        	result = list.get(0).getContent();
        }
        return result;
	}
    
}


