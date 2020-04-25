package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.ActivityF1;

public interface ActivityF1CustomizeMapper {

    /**
     * 根据检索条件检索活动列表
     * @author liuyang
     * @param activityF1Customize
     * @return
     */
    List<ActivityF1> selectActivityF1List(Map<String, Object> parm);

    /**
     * 根据检索条件检索活动件数
     * @author liuyang
     * @param activityF1Customize
     * @return
     */
    public Integer queryActivityF1Count(Map<String, Object> parm);

}
