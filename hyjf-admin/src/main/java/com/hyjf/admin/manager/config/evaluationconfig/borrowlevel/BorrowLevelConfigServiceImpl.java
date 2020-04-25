/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.config.evaluationconfig.borrowlevel;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.EvaluationConfig;
import com.hyjf.mybatis.model.auto.EvaluationConfigExample;
import com.hyjf.mybatis.model.auto.EvaluationConfigLog;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 风险测评配置-信用等级配置Service实现类
 *
 * @author liuyang
 * @version BorrowLevelConfigServiceImpl, v0.1 2018/11/28 17:14
 */
@Service
public class BorrowLevelConfigServiceImpl extends BaseServiceImpl implements BorrowLevelConfigService {

    /**
     * 获取风险测评配置
     *
     * @return
     */
    @Override
    public List<EvaluationConfig> getRecordList() {
        return this.evaluationConfigMapper.selectByExample(new EvaluationConfigExample());
    }

    @Override
    public EvaluationConfig getRecord(Integer record) {
        EvaluationConfig evaluationConfig = evaluationConfigMapper.selectByPrimaryKey(record);
        return evaluationConfig;
    }

    /**
     * 更新风险测评配置-信用评级配置
     *
     * @param record
     * @return
     */
    @Override
    public int updateRecord(EvaluationConfig record) {
        return evaluationConfigMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 插入配置日志
     *
     * @param log
     * @return
     */
    @Override
    public int insetRecord(EvaluationConfigLog log) {
        return this.evaluationConfigLogMapper.insertSelective(log);
    }

}
