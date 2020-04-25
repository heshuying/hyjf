package com.hyjf.app.user.transfer;

import java.util.List;

import com.hyjf.app.BaseService;
import com.hyjf.app.project.RepayPlanBean;
import com.hyjf.mybatis.model.customize.app.AppTenderToCreditDetailCustomize;

public interface AppTransferService extends BaseService {

    
    /**
     * 
     * 获取出借承接债转详情
     * 
     * @author liuyang
     * @param creditNid
     * @return
     */
    public AppTenderToCreditDetailCustomize selectCreditTenderDetail(String creditNid);
    
    /**
     * 计算获取还款计划
     * 
     * @param borrowNid
     * @return
     */
    public List<RepayPlanBean> getRepayPlan(String borrowNid);
	
}
