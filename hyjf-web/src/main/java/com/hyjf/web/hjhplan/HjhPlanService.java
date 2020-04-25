/**
 * Description:项目列表查询service接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 下午2:17:31
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.hjhplan;

import java.util.List;
import java.util.Map;

import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;
import com.hyjf.mybatis.model.customize.web.hjh.HjhPlanCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistListCustomize;
import com.hyjf.web.BaseService;

public interface HjhPlanService extends BaseService {

	/**
	 * 首页查询计划列表
	 * 
	 * @param params
	 * @return
	 */
	List<HjhPlanCustomize> searchHjhPlanList(Map<String, Object> params);

    List<Map<String, Object>> searchPlanStatisticData();

    Integer countHjhPlanList(Map<String, Object> params);
    /**
     * 
     * 查询用户汇计划出借明细
     * @author pcc
     * @param params
     * @return
     */
    UserHjhInvistDetailCustomize selectUserHjhInvistDetail(Map<String, Object> params);
    /**
     * 
     * 查询用户汇计划出借关联项目
     * @author pcc
     * @param params
     * @return
     */
    List<UserHjhInvistListCustomize> selectUserHjhInvistBorrowList(Map<String, Object> params);

    int countUserHjhInvistBorrowListTotal(Map<String, Object> params);

    /**
     * 查询汇计划运营数据
     * @return
     */
    TotalInvestAndInterestEntity selectOperationData();
}
