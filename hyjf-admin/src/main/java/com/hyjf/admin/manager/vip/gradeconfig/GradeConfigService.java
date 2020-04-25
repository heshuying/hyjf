package com.hyjf.admin.manager.vip.gradeconfig;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.VipInfo;

public interface GradeConfigService extends BaseService {

    /**
     * 获取VIP等级列表列表
     * 
     * @return
     */
    public List<VipInfo> getRecordList(GradeConfigBean from, int limitStart, int limitEnd);

    /**
     * 获取单个VIP等级
     * 
     * @return
     */
    public VipInfo getRecord(Integer record);



    /**
     * VIP等级插入
     * 
     * @param record
     */
    public void insertRecord(GradeConfigBean record);

    public int getVipInfoByVipLevel(GradeConfigBean record);

    /**
     * VIP等级更新
     * 
     * @param record
     */
    public void updateRecord(GradeConfigBean record);
    



}