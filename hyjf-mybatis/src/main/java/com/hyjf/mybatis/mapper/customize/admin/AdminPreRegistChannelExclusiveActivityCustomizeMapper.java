/**
 * Description:预注册渠道专属活动
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 朱晓东
 * @version: 1.0
 * Created at: 2016年06月23日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
	
package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.AdminPreRegistChannelExclusiveActivityCustomize;



public interface AdminPreRegistChannelExclusiveActivityCustomizeMapper {
    
    /**
     * 初始化预注册渠道专属活动数据
     * @param registUser
     * @return
     */
        
    List<AdminPreRegistChannelExclusiveActivityCustomize> iniPreRegistChannelExclusiveActivity(Map<String, Object> map);
    
    /**
     * 获取预注册渠道专属活动数据数目
     * @param form
     * @return
     */
    int countRecordTotal(Map<String, Object> map);

    /**
     * 获取预注册渠道专属活动数据列表
     * 
     * @return
     */
    List<AdminPreRegistChannelExclusiveActivityCustomize> selectRecordList(Map<String, Object> map);
}

	