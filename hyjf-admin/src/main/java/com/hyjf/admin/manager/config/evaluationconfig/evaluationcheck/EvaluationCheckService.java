package com.hyjf.admin.manager.config.evaluationconfig.evaluationcheck;

import com.hyjf.mybatis.model.auto.BankRechargeLimitConfig;
import com.hyjf.mybatis.model.auto.EvaluationConfig;

import java.util.List;

public interface EvaluationCheckService {

    /**
     * 获取测评配置,开关列表
     * 
     * @return
     */
     List<EvaluationConfig> getRecordList();

    /**
     * 获取单个手续费列表维护
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

}