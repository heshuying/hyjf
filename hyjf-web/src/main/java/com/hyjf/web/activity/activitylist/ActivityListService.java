package com.hyjf.web.activity.activitylist;

import java.util.List;

import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.AdsType;
import com.hyjf.web.BaseService;

public interface ActivityListService extends BaseService {

    /**
     * 获取活动列表列表
     * 
     * @return
     */
    public List<Ads> getRecordList(Integer typeId, int limitStart, int limitEnd);

    public int getRecordListCountByTypeid(Integer typeId);

    /**
     * 获取广告类型
     * @param code
     * @return
     */
    public AdsType getAdsTypeByCode(String code);
}
