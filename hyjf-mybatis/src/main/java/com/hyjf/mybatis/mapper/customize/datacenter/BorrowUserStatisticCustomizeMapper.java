package com.hyjf.mybatis.mapper.customize.datacenter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BorrowUserStatisticCustomizeMapper {

    /**
     * 累计借款人数据统计（定义：系统累计到现在进行过发表的底层借款人数量）
     * 
     */
    Integer countBorrowUser(Map<String,Object> paraMap);
    
    /**
     * 当前借款人（定义：当前有尚未结清债权的底层借款人数量）
     * 
     */
    Integer countCurrentBorrowUser(Map<String,Object> paraMap);
    
    /**
     * 前十大借款人待还金额占比（定义：当前代还金额排名前十的底层借款人的代还金额之和占总代还的占比）
     * 
     */
    BigDecimal sumBorrowUserMoneyTopTen(Map<String,Object> paraMap);

    
    /**
     * 最大单一借款人待还金额占比（定义：当前代还金额排名第一的底层借款人的代还金额之和占总代还的占比）
     * 
     */
    BigDecimal sumBorrowUserMoneyTopOne(Map<String,Object> paraMap);
    
    
    /**
     * 所有标的总代还金额统计
     * 
     */
    BigDecimal sumBorrowUserMoney(Date date);

    /**
     * 当前出借人（定义：当前代还金额不为0的用户数量）
     * 
     */
    Integer countCurrentTenderUser(Map<String,Object> paraMap);

    /**
     * 当前投资人数userIds
     * @param stringObjectHashMap
     * @return
     */
    List<Integer> getCurrentTenderUserIds(HashMap<String,Object> stringObjectHashMap);
}