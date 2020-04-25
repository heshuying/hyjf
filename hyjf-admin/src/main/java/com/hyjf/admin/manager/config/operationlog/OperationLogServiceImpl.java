package com.hyjf.admin.manager.config.operationlog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.operationlog.dao.UserOperationLogDao;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.FeerateModifyLog;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhAssetTypeExample;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfigExample;

import org.springframework.data.mongodb.core.query.Query;


@Service
public class OperationLogServiceImpl extends BaseServiceImpl implements OperationLogService {

	@Autowired
	private UserOperationLogDao userOperationLogDao;

	@Override
	public List<HjhAssetType> getHjhAssetType(HjhAssetTypeExample hjhAssetTypeExample) {
		// TODO Auto-generated method stub
		List<HjhAssetType> hjhAssetTypes=hjhAssetTypeMapper.selectByExample(hjhAssetTypeExample);
		return hjhAssetTypes;
	}
	/**
     * 获取资产来源
     * 
     * @return
     */
    public List<HjhInstConfig> getHjhInstConfig(HjhInstConfigExample hjhInstConfig){
    	List<HjhInstConfig> hjhAssetTypes=hjhInstConfigMapper.selectByExample(hjhInstConfig);
		return hjhAssetTypes;
    }
	/**
	 * 获取记录数
	 * @param form
	 * @return
	 * @author LiuBin
	 */
	@Override
	public Integer getRecordCount(Map<String, Object> conditionMap) {
		return feerateModifyLogsMapper.countAssetList(conditionMap);
	}
	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<FeerateModifyLog> getRecordList(Map<String, Object> conditionMap, int limitStart, int limitEnd) {
		if (limitStart == 0 || limitStart > 0) {
			conditionMap.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			conditionMap.put("limitEnd", limitEnd);
		}
		List<FeerateModifyLog> list = feerateModifyLogsMapper.selectAssetListList(conditionMap);
		return list;
	}

    @Override
    public int countOperationLog(Map<String, Object> operationLog) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (operationLog.get("userName") != null) {
			criteria.and("userName").is(operationLog.get("userName"));
		}
		if (operationLog.get("operationType") != null) {
			criteria.and("operationType").is(operationLog.get("operationType"));
		}
		if (operationLog.get("userRole") != null) {
			criteria.and("userRole").is(operationLog.get("userRole"));
		}
		if (operationLog.get("operationTimeStart") != null || operationLog.get("operationTimeEnd") != null) {
			Date operationTimeStart = GetDate.stringToDate(operationLog.get("operationTimeStart").toString());
			Date operationTimeEnd = GetDate.stringToDate(operationLog.get("operationTimeEnd").toString());
			criteria.and("operationTime").gte(operationTimeStart).lte(operationTimeEnd);
		}
		query.addCriteria(criteria);
		List<UserOperationLogEntity> list = userOperationLogDao.find(query);
        return list.size();
    }

	@Override
	public List<UserOperationLogEntity> getOperationLogList(Map<String, Object> operationLog, int limitStart, int limitEnd) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (operationLog.get("userName") != null) {
			criteria.and("userName").is(operationLog.get("userName"));
		}
		if (operationLog.get("operationType") != null) {
			criteria.and("operationType").is(operationLog.get("operationType"));
		}
		if (operationLog.get("userRole") != null) {
			criteria.and("userRole").is(operationLog.get("userRole"));
		}
		if (operationLog.get("operationTimeStart") != null || operationLog.get("operationTimeEnd") != null) {
			Date operationTimeStart = GetDate.stringToDate(operationLog.get("operationTimeStart").toString());
			Date operationTimeEnd = GetDate.stringToDate(operationLog.get("operationTimeEnd").toString());
			criteria.and("operationTime").gte(operationTimeStart).lte(operationTimeEnd);
		}
		query.addCriteria(criteria);
		query.skip(limitStart).limit(limitEnd);
		query.with(new Sort(Sort.Direction.DESC, "operationTime"));
		List<UserOperationLogEntity> list = userOperationLogDao.find(query);
		return list;
	}


}
