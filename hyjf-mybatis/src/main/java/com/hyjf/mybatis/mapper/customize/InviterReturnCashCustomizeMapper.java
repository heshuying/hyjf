package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.InviterReturnCashCustomize;
import com.hyjf.mybatis.model.customize.ReturncashCustomize;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InviterReturnCashCustomizeMapper {

    /**
     * 获取部门和推荐人
     * @param userId
     * @return
     */
    InviterReturnCashCustomize selectReturnCashList(@Param("userId") Integer userId);
}