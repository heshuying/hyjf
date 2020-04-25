/**
 * Description:汇添金计划service接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.hjhplan;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.operationreport.dao.TotalInvestAndInterestMongoDao;
import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistListCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class HjhPlanServiceImpl extends BaseServiceImpl implements HjhPlanService {

	@Autowired
	private TotalInvestAndInterestMongoDao totalInvestAndInterestMongoDao;

	/**
	 * 查询指定项目类型的项目列表
	 */
	@Override
	@Cached(name="webHomeHjhCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 2, stopRefreshAfterLastAccess = 60, timeUnit = TimeUnit.SECONDS)
	public List<HjhPlanCustomize> searchHjhPlanList(Map<String, Object> params) {
		
		List<HjhPlanCustomize> hjhPlanList = this.hjhPlanCustomizeMapper.selectHjhPlanList(params);
		
		return hjhPlanList;
	}
	
	/**
	 * 
	 * 列表记录数查询
	 * @author hsy
	 * @param params
	 * @return
	 */
	@Override
	@Cached(name="webHjhCntCache-", expire = CustomConstants.HOME_CACHE_LIVE_TIME, cacheType = CacheType.BOTH)
	@CacheRefresh(refresh = 2, stopRefreshAfterLastAccess = 60, timeUnit = TimeUnit.SECONDS)
    public Integer countHjhPlanList(Map<String, Object> params){
	    return hjhPlanCustomizeMapper.countHjhPlanList(params);
	}

	/**
	 * 
	 * 汇计划统计数据查询
	 * @author hsy
	 * @return
	 */
	@Override
    public List<Map<String, Object>> searchPlanStatisticData(){
	    return hjhPlanCustomizeMapper.searchPlanStatisticData();
	}
	/**
     * 
     * 查询用户汇计划出借明细
     * @author pcc
     * @param params
     * @return
     */
    @Override
    public UserHjhInvistDetailCustomize selectUserHjhInvistDetail(Map<String, Object> params) {
        return hjhPlanCustomizeMapper.selectUserHjhInvistDetail(params);
    }
    /**
     * 
     * 查询用户汇计划出借关联项目
     * @author pcc
     * @param params
     * @return
     */
    @Override
    public List<UserHjhInvistListCustomize> selectUserHjhInvistBorrowList(Map<String, Object> params) {
        return hjhPlanCustomizeMapper.selectUserHjhInvistBorrowList(params);
    }

    @Override
    public int countUserHjhInvistBorrowListTotal(Map<String, Object> params) {
        // TODO Auto-generated method stub
        return hjhPlanCustomizeMapper.countUserHjhInvistBorrowListTotal(params);
    }

	@Override
	public TotalInvestAndInterestEntity selectOperationData() {
		TotalInvestAndInterestEntity entity = totalInvestAndInterestMongoDao.findOne(new Query());
		if (entity != null) {
			return entity;
		}
		return null;
	}
}
