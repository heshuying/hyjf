package com.hyjf.admin.manager.config.operationlog;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.manager.user.operationlog.UserOperationLogBean;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.customize.FeerateModifyLog;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhAssetTypeExample;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfigExample;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.customize.AssetListCustomize;

public interface OperationLogService {

    

    /**
     * 获取资产类型列表
     * 
     * @return
     */
    public List<HjhAssetType> getHjhAssetType(HjhAssetTypeExample hjhAssetType);
    /**
     * 获取资产来源
     * 
     * @return
     */
    public List<HjhInstConfig> getHjhInstConfig(HjhInstConfigExample hjhInstConfig);
    /**
	 * 获取记录数
	 * @param form
	 * @return
	 */
	public Integer getRecordCount(Map<String, Object> conditionMap);
	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<FeerateModifyLog> getRecordList(Map<String, Object> conditionMap, int limitStart, int limitEnd);

	/**
	 * 查询会员操作日志总数
	 * @param operationLog
	 * @return
	 */
	int countOperationLog(Map<String, Object> operationLog);

	/**
	 * 查询会员操作日志列表
	 * @param operationLog
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<UserOperationLogEntity> getOperationLogList(Map<String, Object> operationLog, int offset, int limit);
}
