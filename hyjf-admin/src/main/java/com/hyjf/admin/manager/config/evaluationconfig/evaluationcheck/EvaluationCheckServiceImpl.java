package com.hyjf.admin.manager.config.evaluationconfig.evaluationcheck;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EvaluationCheckServiceImpl extends BaseServiceImpl implements EvaluationCheckService {
   

    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<EvaluationConfig> getRecordList() {
		EvaluationConfigExample example = new EvaluationConfigExample();
        // 条件查询
        example.setOrderByClause("create_time");
        return evaluationConfigMapper.selectByExample(example);
    }

    /**
     * 根据主键获取
     * 
     * @return
     */
    public EvaluationConfig getRecord(Integer record) {
		EvaluationConfig FeeConfig = evaluationConfigMapper.selectByPrimaryKey(record);
        return FeeConfig;
    }

	/**
	 * 维护更新
	 *
	 * @param record
	 */
	public int updateRecord(EvaluationConfig record) {
		record.setUpdateTime(GetDate.getDate());
		return  evaluationConfigMapper.updateByPrimaryKeySelective(record);
	}
}
