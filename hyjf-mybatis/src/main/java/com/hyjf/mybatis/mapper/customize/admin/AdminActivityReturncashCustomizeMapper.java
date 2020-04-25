package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminActivityReturncashCustomize;

public interface AdminActivityReturncashCustomizeMapper {

    /**
     * 获取未返现列表数量
     * 
     * @param returncashCustomize
     * @return
     */
    int selectReturncashCount(Map<String, Object> paraMap);

    /**
     * 获取未返现列表
     * 
     * @param returncashCustomize
     * @return
     */
    List<AdminActivityReturncashCustomize> selectReturncashList(Map<String, Object> paraMap);
    
    /**
     * 获取已返现列表数量
     * 
     * @param returncashCustomize
     * @return
     */
    int selectReturnedcashCount(Map<String, Object> paraMap);

    /**
     * 获取已返现列表
     * 
     * @param returncashCustomize
     * @return
     */
    List<AdminActivityReturncashCustomize> selectReturnedcashList(Map<String, Object> paraMap);

}