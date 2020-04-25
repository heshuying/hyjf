package com.hyjf.mybatis.mapper.customize.app;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.app.AppProjectListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditAssignedDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditAssignedListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordDetailListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRecordListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderCreditRepayPlanListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditListCustomize;

public interface AppTenderCreditCustomizeMapper {
    /**
     * 
     * 获取汇转让出借列表件数
     * @author liuyang
     * @param params
     * @return
     */
    public int countTenderCreditListRecordCount(Map<String, Object> params);

    /**
     * 获取汇转让出借列表
     * @author liuyang
     * @param params
     * @return
     */
    public List<AppProjectListCustomize> searchTenderCreditList(Map<String, Object> params);

    /**
     * 出借汇转让详情
     * @author liuyang
     * @param params
     * @return
     */
    public List<AppTenderToCreditDetailCustomize> selectCreditTenderDetail(Map<String, Object> params);

    /**
     * 
     * 查询债转出借记录件数
     * @author liuyang
     * @param params
     * @return
     */
    public int countTenderCreditInvestRecordTotal(Map<String, Object> params);

    /**
     * 
     * 查询债转出借记录列表
     * @author liuyang
     * @param params
     * @return
     */
    public List<AppTenderCreditInvestListCustomize> searchTenderCreditInvestList(Map<String, Object> params);

    /**
     * 
     * 查询可转让列表件数
     * @author liuyang
     * @param params
     * @return
     */
    public int countTenderToCredit(Map<String, Object> params);

    /**
     * 
     * 获取可债转列表数据
     * @author liuyang
     * @param params
     * @return
     */
    public List<AppTenderToCreditListCustomize> selectTenderToCreditList(Map<String, Object> params);

    /**
     * 
     * 验证出借人当天是否可以债转
     * @author liuyang
     * @param params
     * @return
     */
    public int tenderAbleToCredit(Map<String, Object> params);

    /**
     * 
     * 获取债转详情
     * @author liuyang
     * @param params
     * @return
     */
    public List<AppTenderCreditCustomize> selectTenderToCreditDetail(Map<String, Object> params);

    /**
     * 
     * 获取用户已承接债转的件数
     * @author liuyang
     * @param params
     * @return
     */
    public int countCreditTenderAssigned(Map<String, Object> params);

    /**
     * 
     * 获取用户已承接债转列表
     * @author liuyang
     * @param params
     * @return
     */
    public List<AppTenderCreditAssignedListCustomize> selectCreditTenderAssigned(Map<String, Object> params);

    /**
     * 
     * 获取用户已承接债转详情
     * @author liuyang
     * @param params
     * @return
     */
    public AppTenderCreditAssignedDetailCustomize getCreditAssignDetail(Map<String, Object> params);

    /**
     *
     * 获取用户转让记录条数
     * @author liuyang
     * @param params
     * @return
     */
    public int countCreditRecordTotal(Map<String, Object> params);
    /**
     * 
     * 获取用户转让记录列表
     * @author liuyang
     * @param params
     * @return
     */
    public List<AppTenderCreditRecordListCustomize> searchCreditRecordList(Map<String, Object> params);

    /**
     * 
     * 根据债转编号获取用户转让记录详情
     * @author liuyang
     * @param params
     * @return
     */
    public AppTenderCreditRecordDetailCustomize selectTenderCreditRecordDetail(Map<String, Object> params);

    /**
     * 
     * 根据用户id,债转编号获取用户转让明细列表件数
     * @author liuyang
     * @param params
     * @return
     */
    public int countCreditRecordDetailList(Map<String, Object> params);

    /**
     * 
     * 根据用户id,债转编号获取用户转让明细列表
     * @author liuyang
     * @param params
     * @return
     */
    public List<AppTenderCreditRecordDetailListCustomize> getCreditRecordDetailList(Map<String, Object> params);

    /**
     * 
     * 获取不分期债转的还款计划件数
     * @author liuyang
     * @param params
     * @return
     */
    public int countRepayRecoverListRecordTotal(Map<String, Object> params);

    /**
     * 
     * 获取不分期债转的还款计划列表
     * @author liuyang
     * @param params
     * @return
     */
    public List<AppTenderCreditRepayPlanListCustomize> selectRepayRecoverList(Map<String, Object> params);

    /**
     * 
     * 获取分支债转的还款计划件数
     * @author liuyang
     * @param params
     * @return
     */
    public int countRepayPlanListRecordTotal(Map<String, Object> params);

    /**
     * 
     * 获取分期债转的还款计划
     * @author liuyang
     * @param params
     * @return
     */
    public List<AppTenderCreditRepayPlanListCustomize> selectRepayRecoverPlanList(Map<String, Object> params);
}
