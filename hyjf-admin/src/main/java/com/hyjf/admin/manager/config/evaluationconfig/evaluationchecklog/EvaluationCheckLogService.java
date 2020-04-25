package com.hyjf.admin.manager.config.evaluationconfig.evaluationchecklog;

import com.hyjf.mybatis.model.auto.EvaluationConfigLog;

import java.util.List;

public interface EvaluationCheckLogService {

    /**
     * 获取测评配置,开关列表
     * 
     * @return
     */
     List<EvaluationConfigLog> getRecordList(EvaluationCheckLogBean log,int limitStart, int limitEnd);

    /**
     * 获取单个手续费列表维护
     * 
     * @return
     */
    EvaluationConfigLog getRecord(Integer record);

    /**
     * 更新
     *
     * @param record
     */
    int updateRecord(EvaluationConfigLog record);

    /**
     * 新增
     * @param record
     * @return
     */
    int insetRecord(EvaluationConfigLog record);

}