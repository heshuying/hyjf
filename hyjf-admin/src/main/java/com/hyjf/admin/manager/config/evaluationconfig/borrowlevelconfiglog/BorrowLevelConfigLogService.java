/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.evaluationconfig.borrowlevelconfiglog;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.EvaluationConfigLog;

import java.text.ParseException;
import java.util.List;

/**
 * 风险测评信用评级配置logService
 *
 * @author liuyang
 * @version BorrowLevelConfigLogService, v0.1 2018/11/29 10:03
 */
public interface BorrowLevelConfigLogService extends BaseService {

    /**
     * 获取风险测评信用评级配置log
     *
     * @param bean
     * @param limitStart
     * @param limitEnd
     * @return
     */
    List<EvaluationConfigLog> getRecordList(BorrowLevelConfigLogBean bean, Integer limitStart, Integer limitEnd);

    /**
     * 获取风险测评信用评级配置log件数
     *
     * @param bean
     * @param limitStart
     * @param limitEnd
     * @return
     */
    Integer countRecordList(BorrowLevelConfigLogBean bean, Integer limitStart, Integer limitEnd);
}
