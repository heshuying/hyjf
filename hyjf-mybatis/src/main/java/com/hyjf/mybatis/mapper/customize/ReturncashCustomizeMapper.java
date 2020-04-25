package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ReturncashCustomize;

public interface ReturncashCustomizeMapper {

    /**
     * 获取未返现列表数量
     * 
     * @param returncashCustomize
     * @return
     */
    int selectReturncashCount(ReturncashCustomize returncashCustomize);

    /**
     * 获取未返现列表
     * 
     * @param returncashCustomize
     * @return
     */
    List<ReturncashCustomize> selectReturncashList(ReturncashCustomize returncashCustomize);

    /**
     * 获取已返现列表数量
     * 
     * @param returncashCustomize
     * @return
     */
    int selectReturnedcashCount(ReturncashCustomize returncashCustomize);

    /**
     * 获取已返现列表
     * 
     * @param returncashCustomize
     * @return
     */
    List<ReturncashCustomize> selectReturnedcashList(ReturncashCustomize returncashCustomize);
}