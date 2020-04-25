package com.hyjf.batch.bail;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.HjhBailConfig;

import java.util.List;

/**
 * 日发标额度累计
 *
 * @author PC-LIUSHOUYI
 */
public interface DayMarkLineTotalService extends BaseService {

    /**
     * 获取所有选择日累计的数据
     *
     * @return
     */
    List<HjhBailConfig> selectAccumulateList();

    /**
     * 获取所有选择日累计的数据(包括已删除)
     *
     * @return
     */
    List<HjhBailConfig> selectAccumulateListAll();
}
