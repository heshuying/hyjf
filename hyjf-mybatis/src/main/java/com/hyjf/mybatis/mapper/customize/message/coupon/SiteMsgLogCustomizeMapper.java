package com.hyjf.mybatis.mapper.customize.message.coupon;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.message.coupon.SiteMsgLogCustomize;

public interface SiteMsgLogCustomizeMapper {
    
    public List<SiteMsgLogCustomize> selectMsgLogList(Map paraMap);
    
    public Integer countMsgLog(Map paraMap);
}
