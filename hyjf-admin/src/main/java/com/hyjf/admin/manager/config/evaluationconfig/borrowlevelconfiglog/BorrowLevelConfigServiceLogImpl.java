/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.evaluationconfig.borrowlevelconfiglog;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.EvaluationConfigLog;
import com.hyjf.mybatis.model.auto.EvaluationConfigLogExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 风险测评信用评级配置logService
 *
 * @author liuyang
 * @version BorrowLevelConfigServiceLogImpl, v0.1 2018/11/29 10:03
 */
@Service
public class BorrowLevelConfigServiceLogImpl extends BaseServiceImpl implements BorrowLevelConfigLogService {

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取风险测评信用评级配置log
     *
     * @param bean
     * @return
     */
    @Override
    public List<EvaluationConfigLog> getRecordList(BorrowLevelConfigLogBean bean,Integer limitStart,Integer limitEnd)  {
        EvaluationConfigLogExample example = new EvaluationConfigLogExample();
        EvaluationConfigLogExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(3);
        if (limitStart!= -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        if (StringUtils.isNotBlank(bean.getUpdateUserSrch())) {
            cra.andUpdateUserEqualTo(bean.getUpdateUserSrch());
        }
        if (StringUtils.isNotBlank(bean.getStartTimeSrch()) && StringUtils.isNotBlank(bean.getEndTimeSrch())) {
            cra.andCreateTimeBetween(GetDate.str2Date(GetDate.getDayStart(bean.getStartTimeSrch()),datetimeFormat), GetDate.str2Date(GetDate.getDayEnd(bean.getEndTimeSrch()),datetimeFormat));
        }
        // 条件查询
        example.setOrderByClause("create_time desc");
        return evaluationConfigLogMapper.selectByExample(example);
    }

    @Override
    public Integer countRecordList(BorrowLevelConfigLogBean bean, Integer limitStart, Integer limitEnd) {
        EvaluationConfigLogExample example = new EvaluationConfigLogExample();
        EvaluationConfigLogExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(3);
        if (limitStart!= -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        if (StringUtils.isNotBlank(bean.getUpdateUserSrch())) {
            cra.andUpdateUserEqualTo(bean.getUpdateUserSrch());
        }
        if (StringUtils.isNotBlank(bean.getStartTimeSrch()) && StringUtils.isNotBlank(bean.getEndTimeSrch())) {
            cra.andCreateTimeBetween(GetDate.str2Date(GetDate.getDayStart(bean.getStartTimeSrch()),datetimeFormat), GetDate.str2Date(GetDate.getDayEnd(bean.getEndTimeSrch()),datetimeFormat));
        }
        // 条件查询
        example.setOrderByClause("create_time desc");
        return evaluationConfigLogMapper.countByExample(example);
    }
}
