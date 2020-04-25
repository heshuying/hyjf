package com.hyjf.admin.message.coupon.msglist;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.message.coupon.SiteMsgLogCustomize;

/**
 * service接口实现类
 */
@Service
public class SiteMsgLogServiceImpl extends BaseServiceImpl implements SiteMsgLogService {

    /**
     * 获取站内信列表
     */
    @Override
    public List<SiteMsgLogCustomize> selectMsgLogList(Map paraMap) {
        
        return siteMsgLogCustomizeMapper.selectMsgLogList(paraMap);
    }

    /**
     * 获得记录数
     * @return
     */
    @Override
    public Integer countMsgLog(Map paraMap) {
        
        return siteMsgLogCustomizeMapper.countMsgLog(paraMap);
    }

    
}
