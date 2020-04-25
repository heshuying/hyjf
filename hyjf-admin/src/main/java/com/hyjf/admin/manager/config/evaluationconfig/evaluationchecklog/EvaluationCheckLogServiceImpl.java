package com.hyjf.admin.manager.config.evaluationconfig.evaluationchecklog;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.EvaluationConfig;
import com.hyjf.mybatis.model.auto.EvaluationConfigExample;
import com.hyjf.mybatis.model.auto.EvaluationConfigLog;
import com.hyjf.mybatis.model.auto.EvaluationConfigLogExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class EvaluationCheckLogServiceImpl extends BaseServiceImpl implements EvaluationCheckLogService {

	private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 获取列表列表
     * 
     * @return
     */
    public List<EvaluationConfigLog> getRecordList(EvaluationCheckLogBean log,int limitStart, int limitEnd) {
		EvaluationConfigLogExample example = new EvaluationConfigLogExample();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		EvaluationConfigLogExample.Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo(1);
		if(StringUtils.isNotEmpty(log.getUpdateUser())){
			criteria.andUpdateUserEqualTo(log.getUpdateUser());
		}
		if (StringUtils.isNotBlank(log.getStartTime()) && StringUtils.isNotBlank(log.getEndTime())) {
			criteria.andUpdateTimeBetween(GetDate.str2Date(GetDate.getDayStart(log.getStartTime()),datetimeFormat), GetDate.str2Date(GetDate.getDayEnd(log.getEndTime()),datetimeFormat));
		}
        // 条件查询
        example.setOrderByClause("create_time DESC");
        return evaluationConfigLogMapper.selectByExample(example);
    }

    /**
     * 获取单个维护
     * 
     * @return
     */
    public EvaluationConfigLog getRecord(Integer record) {
		EvaluationConfigLog FeeConfig = evaluationConfigLogMapper.selectByPrimaryKey(record);
        return FeeConfig;
    }

	/**
	 * 维护更新
	 *
	 * @param record
	 */
	public int updateRecord(EvaluationConfigLog record) {
		record.setUpdateTime(GetDate.getDate());
		return  evaluationConfigLogMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 新增操作日志
	 *
	 * @param record
	 */
	public int insetRecord(EvaluationConfigLog record) {
		record.setCreateTime(GetDate.getDate());
		return  evaluationConfigLogMapper.insertSelective(record);
	}
}
