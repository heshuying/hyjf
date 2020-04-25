package com.hyjf.mybatis.mapper.customize.app;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.app.AppUserPrizeCodeCustomize;
import com.hyjf.mybatis.model.customize.app.AppUserPrizeOpportunityCustomize;

/**
 * 
 * 兑奖码列表查询
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月25日
 * @see 下午1:45:42
 */
public interface AppUserPrizeCodeCustomizeMapper {

    /**
     * 
     * 兑奖码列表记录数
     * @author hsy
     * @param paraMap
     * @return
     */
    int selectRecordCount(Map<String, Object> paraMap);

    /**
     * 
     * 兑奖码列表
     * @author hsy
     * @param paraMap
     * @return
     */
    List<AppUserPrizeCodeCustomize> selectRecordList(Map<String, Object> paraMap);
    
    /**
     * 
     * 夺宝机会总记录数
     * @author hsy
     * @param paraMap
     * @return
     */
    int selectPrizeOpportunityCount(Map<String, Object> paraMap);

    /**
     * 
     * 夺宝机会列表
     * @author hsy
     * @param paraMap
     * @return
     */
    List<AppUserPrizeOpportunityCustomize> selectPrizeOpportunityList(Map<String, Object> paraMap);
    
    /**
     * 取得活动期内的出借金额合计
     * @param paraMap
     * @return
     */
    BigDecimal getUserTenderAccountSum(Map<String,Object> paraMap);
    
    /**
     * 取得某奖品的最后的兑奖码自增编号
     * @param paraMap
     * @return
     */
    AppUserPrizeCodeCustomize getUserPrizeIdentityId(Map<String,Object> paraMap);
    
    /**
     * 取得用户兑奖码列表
     * @param paraMap
     * @return
     */
    List<AppUserPrizeCodeCustomize> getUserPrizeCodeList(Map<String,Object> paraMap);
    
    /**
     * 取得奖品列表
     * @param paraMap
     * @return
     */
    List<AppUserPrizeCodeCustomize> getPrizeList(Map<String,Object> paraMap);
    
    /**
     * 更新已参与人数
     * @param paraMap
     */
    void updatePrizeJoinedCount(Map<String,Object> paraMap);
    
    /**
     * 校验参与人数是否已经达到最大参与人数
     * @return
     */
    int checkMaxJoinedCount(Map<String,Object> paraMap);
    

}