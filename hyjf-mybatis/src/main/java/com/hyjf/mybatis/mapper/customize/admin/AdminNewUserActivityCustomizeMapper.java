package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminNewUserActivityCustomize;

/**
 * 
 * 九月份运营活动注册送体验金
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月25日
 * @see 下午1:45:42
 */
public interface AdminNewUserActivityCustomizeMapper {

    /**
     * 
     * 列表记录数
     * @author hsy
     * @param paraMap
     * @return
     */
    int selectRecordCount(Map<String, Object> paraMap);

    /**
     * 
     * 列表查询
     * @author hsy
     * @param paraMap
     * @return
     */
    List<AdminNewUserActivityCustomize> selectRecordList(Map<String, Object> paraMap);
    
    /**
     * 
     * 全站用户送券活动数
     * @author hsy
     * @param paraMap
     * @return
     */
    int selectRegistAllCount(Map<String, Object> paraMap);

    /**
     * 
     * 全站用户送券活动列表
     * @author hsy
     * @param paraMap
     * @return
     */
    List<AdminNewUserActivityCustomize> selectRegistAllList(Map<String, Object> paraMap);
    
}