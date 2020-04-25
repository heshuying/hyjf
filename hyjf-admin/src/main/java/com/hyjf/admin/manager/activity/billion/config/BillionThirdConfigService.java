package com.hyjf.admin.manager.activity.billion.config;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.ActivityBillionThirdConfig;
import com.hyjf.mybatis.model.auto.CouponConfig;

public interface BillionThirdConfigService extends BaseService {

	public Integer selectRecordCount(BillionThirdConfigBean form);

	public List<ActivityBillionThirdConfig> getRecordList(BillionThirdConfigBean form, int limitStart, int limitEnd);

    
    /**
     * 获取单个
     * 
     * @return
     */
    public ActivityBillionThirdConfig getRecord(Integer recordId);

    /**
     * 根据主键判断数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(ActivityBillionThirdConfig record);

    /**
     * 更新
     * 
     * @param record
     */
    public void updateRecord(ActivityBillionThirdConfig record);

    public CouponConfig selectConfigByCode(String couponCode);

}
