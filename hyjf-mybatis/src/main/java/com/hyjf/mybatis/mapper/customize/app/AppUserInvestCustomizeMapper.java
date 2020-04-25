/**
 * Description:我的出借
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */

package com.hyjf.mybatis.mapper.customize.app;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.app.AppAlreadyRepayListCustomize;
import com.hyjf.mybatis.model.customize.app.AppInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectContractDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectContractRecoverPlanCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayListCustomize;
import com.hyjf.mybatis.model.customize.app.AppRepayPlanListCustomize;

public interface AppUserInvestCustomizeMapper {

	List<AppRepayListCustomize> selectRepayList(Map<String, Object> params);

	int countRepayListRecordTotal(Map<String, Object> params);

	int countInvestListRecordTotal(Map<String, Object> params);

	List<AppInvestListCustomize> selectInvestList(Map<String, Object> params);

	int countAlreadyRepayListRecordTotal(Map<String, Object> params);

	List<AppAlreadyRepayListCustomize> selectAlreadyRepayList(Map<String, Object> params);

	List<AppRepayPlanListCustomize> selectRepayRecoverPlanList(Map<String, Object> params);

	int countRepayRecoverPlanListRecordTotal(Map<String, Object> params);

	List<AppRepayPlanListCustomize> selectRepayRecoverList(Map<String, Object> params);

	int countRepayRecoverListRecordTotal(Map<String, Object> params);

	List<AppProjectContractRecoverPlanCustomize> selectProjectContractRecoverPlan(Map<String, Object> params);

	AppProjectContractDetailCustomize selectProjectContractDetail(Map<String, Object> params);

    List<AppRepayPlanListCustomize> selectCouponRepayRecoverList(Map<String, Object> params);

    int countCouponRepayRecoverListRecordTotal(Map<String, Object> params);
    
    String selectReceivedInterest(Map<String, Object> params);
    
    /**
     * 
     * 我的出借回款中持有详情
     * @author liuyang
     * @param params
     * @return
     */
    public AppRepayDetailCustomize selectRepayDetail(Map<String, Object> params);
    
    /**
     * 优惠券出借项目详情
     * @author liuyang
     * @param params
     * @return
     */
    public AppRepayDetailCustomize selectCouponRepayDetail(Map<String, Object> params);
    
    /**
     * 
     * 获取出借中项目详情
     * @author liuyang
     * @param params
     * @return
     */
    public AppRepayDetailCustomize selectInvestProjectDetail(Map<String, Object> params);
    
    /**
     * 
     * 获取出借中的优惠券项目详情
     * @author liuyang
     * @param params
     * @return
     */
    public AppRepayDetailCustomize selectCouponInvestProjectDetail(Map<String, Object> params);
    
    /**
     * 
     * 获取回款中的项目详情
     * @author liuyang
     * @param params
     * @return
     */
    public AppRepayDetailCustomize selectRepayedProjectDetail(Map<String, Object> params);
    
    /**
     * 
     * 获取回款中的优惠券出借项目详情
     * @author liuyang
     * @param params
     * @return
     */
    public AppRepayDetailCustomize selectCouponRepayedProjectDetail(Map<String, Object> params);
    
    /**
     * 
     * 查询用户出借次数 包含直投类、债转、汇添金
     * @author hsy
     * @param paraMap
     * @return
     */
    public Integer selectUserTenderCount(Map<String, Object> paraMap);
}
