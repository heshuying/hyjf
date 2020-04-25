/*
 * Copyright(c) 2012-2016 JD Pharma.Ltd. All Rights Reserved.
 */
package com.hyjf.batch.recharge;

import java.util.List;

import com.hyjf.mybatis.model.customize.RechargeCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

/**
 * 充值相关服务层接口
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年1月11日
 * @see 下午2:05:06
 */
public interface RechargeService {
    
    public List<RechargeCustomize> queryNoResultRechargeList(); 
    
    public void handleRechargeStatus(ChinapnrBean bean, Integer userId, String feeFrom);

}
