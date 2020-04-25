package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.SubmissionsCustomize;

public interface SubmissionsCustomizeMapper {

    /**
     * 取得数据总数量
     *
     * @param username
     * @return
     */
    int countRecordTotal(Map<String,Object>  paramMap);

    /**
     * 根据查询条件 取得数据
     * @param paramMap
     * @return
     */
    List<SubmissionsCustomize> queryRecordList(Map<String,Object>  paramMap);
    

}
