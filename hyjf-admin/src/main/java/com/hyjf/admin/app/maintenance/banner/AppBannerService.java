package com.hyjf.admin.app.maintenance.banner;

import java.util.List;

import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.AdsType;
import com.hyjf.mybatis.model.auto.AdsWithBLOBs;

public interface AppBannerService {

    /**
     * 获取列表
     * 
     * @return
     */
    public List<Ads> getRecordList(AppBannerBean bean, int limitStart, int limitEnd);

    /**
     * 获取列表
     * 
     * @return
     */
    public Integer countRecordList(AppBannerBean bean);

    
    /**
     * 获取单个活动列表维护
     * 
     * @return
     */
    public AdsWithBLOBs getRecord(Integer record);

    /**
     * 根据主键判断活动列表中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(Ads record);

    /**
     * 活动列表插入
     * 
     * @param record
     */
    public void insertRecord(AdsWithBLOBs record);

    /**
     * 活动列表更新
     * 
     * @param record
     */
    public void updateRecord(AdsWithBLOBs record);

    /**
     * 活动列表删除
     * 
     * @param record
     */
    public void deleteRecord(List<Integer> recordList);

    /**
     * 获取广告类型（手机端）
     * @return
     */
    public List<AdsType> getAdsTypeList();

    /**
     * 根据id查询广告位类型
     * 
     * @param typeid
     * @return
     */
    public AdsType getAdsType(Integer typeid);

}