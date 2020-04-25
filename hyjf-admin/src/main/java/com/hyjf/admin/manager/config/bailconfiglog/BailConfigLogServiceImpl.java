package com.hyjf.admin.manager.config.bailconfiglog;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigInfoCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigLogCustomize;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author PC-LIUSHOUYI
 */
@Service
public class BailConfigLogServiceImpl extends BaseServiceImpl implements BailConfigLogService {

    @Override
    public int countBailConfig(HjhBailConfigLogCustomize hjhBailConfigLogCustomize) {
        return hjhBailConfigLogCustomizeMapper.countHjhBailConfigLog(hjhBailConfigLogCustomize);
    }

    /**
     * 获取保证金配置列表
     *
     * @return
     */
    @Override
    public List<HjhBailConfigLogCustomize> getRecordList(HjhBailConfigLogCustomize hjhBailConfigLogCustomize) {
        return hjhBailConfigLogCustomizeMapper.selectHjhBailConfigLogList(hjhBailConfigLogCustomize);
    }
}
