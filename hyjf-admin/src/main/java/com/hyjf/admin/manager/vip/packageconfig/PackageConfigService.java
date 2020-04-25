package com.hyjf.admin.manager.vip.packageconfig;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.VipAuthCustomize;

public interface PackageConfigService extends BaseService {

    /**
     * 获取VIP等级列表列表
     * 
     * @return
     */
    public List<VipAuthCustomize> getRecordList(VipAuthCustomize vipAuthCustomize);

    /**
     * 获取单个VIP等级
     * 
     * @return
     */
    public VipAuthCustomize getRecord(Integer record);

    public Integer countRecord(PackageConfigBean form);



    /**
     * VIP等级插入
     * 
     * @param record
     */
    public void insertRecord(PackageConfigBean record);

    public int getVipAuthByIdAndCode( Integer id,Integer vipId, String couponCode);


    /**
     * VIP等级更新
     * 
     * @param record
     */
    public void updateRecord(PackageConfigBean record);
    



}