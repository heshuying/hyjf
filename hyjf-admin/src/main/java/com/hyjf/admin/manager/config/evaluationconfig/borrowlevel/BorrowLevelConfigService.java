/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.evaluationconfig.borrowlevel;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.EvaluationConfig;
import com.hyjf.mybatis.model.auto.EvaluationConfigLog;

import java.util.List;

/**
 * 风险测评配置-信用等级配置Service
 *
 * @author liuyang
 * @version BorrowLevelConfigService, v0.1 2018/11/28 17:14
 */
public interface BorrowLevelConfigService extends BaseService {
    /**
     * 获取风险测评配置
     *
     * @return
     */
    List<EvaluationConfig> getRecordList();

    /**
     * 获取风险测评配置详情
     *
     * @return
     */
    EvaluationConfig getRecord(Integer record);

    /**
     * 更新
     *
     * @param record
     */
    int updateRecord(EvaluationConfig record);

    /**
     * 插入日志
     *
     * @param log
     */
    int insetRecord(EvaluationConfigLog log);
}
