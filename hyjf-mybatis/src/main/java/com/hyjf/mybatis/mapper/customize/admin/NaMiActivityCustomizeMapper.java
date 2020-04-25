package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.NaMiActivityUserListCustomize;

/**
 * 纳米活动出借统计
 * 
 * @author pcc
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年1月6日
 * @see 上午10:56:05
 */
public interface NaMiActivityCustomizeMapper {
    /**
     * 
     * 活动期间出借用户数量
     * @author pcc
     * @param paraMap
     * @return
     */
    int selectRecordCount(Map<String, Object> paraMap);
    /**
     * 
     * 活动期间出借用户信息列表
     * @author pcc
     * @param paraMap
     * @return
     */
    List<NaMiActivityUserListCustomize> selectRecordList(Map<String, Object> paraMap);

    Double getFoldRatio(Map<String, Object> paraMap);
 

}