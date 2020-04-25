package com.hyjf.admin.manager.user.preregistcea;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminPreRegistChannelExclusiveActivityCustomize;

public interface PreRegistChannelExclusiveActivityService extends BaseService {
    
    /**
     * 初始化预注册数据
     * map参数需要传入preRegChannelExclusiveActivityTimeStart,preRegChannelExclusiveActivityTimeEnd两个时间
     * @param form
     * @return
     */
    List<AdminPreRegistChannelExclusiveActivityCustomize> iniPreRegistChannelExclusiveActivity(Map<String, Object> map);
    
    /**
     * 初始化预注册数据更新表数据
     * @param form
     * @return
     */
    void iniUpdatePreRegistChannelExclusiveActivity(List<AdminPreRegistChannelExclusiveActivityCustomize> preRegistChannelExclusiveActivityList);

    /**
     * 获取预注册渠道专属活动数据数目
     * @param form
     * @return
     */
    public int countRecordTotal(Map<String, Object> map);

    /**
     * 获取预注册渠道专属活动数据列表
     * 
     * @return
     */
    public List<AdminPreRegistChannelExclusiveActivityCustomize> getRecordList(Map<String, Object> map, int limitStart, int limitEnd);
}
