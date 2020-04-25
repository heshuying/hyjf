/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize.admin;

import com.hyjf.mybatis.model.customize.admin.AssetExceptionCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigLogCustomize;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version HjhBailConfigLogCustomizeMapper, v0.1 2018/7/30 11:32
 */
public interface HjhBailConfigLogCustomizeMapper {
    /**
     * 获取保证金配置日志列表数据
     *
     * @param hjhBailConfigLogCustomize
     * @return
     */
    List<HjhBailConfigLogCustomize> selectHjhBailConfigLogList(HjhBailConfigLogCustomize hjhBailConfigLogCustomize);

    /**
     * 查询总件数
     *
     * @param hjhBailConfigLogCustomize
     * @return
     */
    Integer countHjhBailConfigLog(HjhBailConfigLogCustomize hjhBailConfigLogCustomize);

}
