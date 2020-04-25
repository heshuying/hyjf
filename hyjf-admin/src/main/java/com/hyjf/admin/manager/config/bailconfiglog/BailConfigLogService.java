package com.hyjf.admin.manager.config.bailconfiglog;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.HjhBailConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigInfoCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigLogCustomize;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 */
public interface BailConfigLogService extends BaseService {

    /**
     * 获取保证金配置列表
     *
     * @return
     */
    List<HjhBailConfigLogCustomize> getRecordList(HjhBailConfigLogCustomize hjhBailConfigLogCustomize);

    /**
     * 获取保证金配置总数
     *
     * @return
     */
    int countBailConfig(HjhBailConfigLogCustomize hjhBailConfigLogCustomize);
}
