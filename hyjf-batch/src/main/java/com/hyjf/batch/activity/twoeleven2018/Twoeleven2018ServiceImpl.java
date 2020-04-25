package com.hyjf.batch.activity.twoeleven2018;

import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.mapper.customize.TwoelevenCustomizeMapper;
import com.hyjf.mybatis.model.customize.admin.TwoelevenCustomize;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author xiehuili on 2018/10/12.
 */
@Service
public class Twoeleven2018ServiceImpl implements Twoeleven2018Service {

    @Autowired
    private TwoelevenCustomizeMapper twoelevenCustomizeMapper;
    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 添加累计年化出借奖励记录
     */
    @Override
   public void insertInvestReward(){
        //查询活动期间的累计出借金额及用户信息
        Map<String, Object> map = new HashMap<>();
        // 查询累计年华出借额》=70万的数据。
        List<TwoelevenCustomize> twoelevenCustomizeList =twoelevenCustomizeMapper.selectInvestReward(map);
        List<TwoelevenCustomize> newTwoelevenCustomizeList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(twoelevenCustomizeList)){
            // 查询累计年华出借实物奖励记录(奖励类型是实物的记录)
            List<Integer> investRewardSW =twoelevenCustomizeMapper.selectInvestRewardSW(twoelevenCustomizeList);
            //实物奖励
            for(TwoelevenCustomize twoelevenCustomize:twoelevenCustomizeList){
                BigDecimal money = twoelevenCustomize.getYearAmountAll() == null? BigDecimal.ZERO:twoelevenCustomize.getYearAmountAll();
                //userId存在，已经添加过奖励，不要在添加
                if (!investRewardSW.contains(twoelevenCustomize.getUserId())) {
                    if (money.compareTo(new BigDecimal("700000")) >= 0 && money.compareTo(new BigDecimal("900000")) < 0) {
                        // 累计年化出借额》=70且《90万 实物奖励iPhone XR
                        setBatchInsertInvestReward("iPhone XR",twoelevenCustomize);
                        newTwoelevenCustomizeList.add(twoelevenCustomize);
                    } else if (money.compareTo(new BigDecimal("900000")) >= 0 && money.compareTo(new BigDecimal("1000000")) < 0) {
                        // 累计年化出借额》=90且《100万 实物奖励iPhone XS
                        setBatchInsertInvestReward("iPhone XS",twoelevenCustomize);
                        newTwoelevenCustomizeList.add(twoelevenCustomize);
                    } else if (money.compareTo(new BigDecimal("1000000")) >= 0) {
                        // 累计年化出借额》=100实物奖励iPhone XS
                        setBatchInsertInvestReward("iPhone XS Max",twoelevenCustomize);
                        newTwoelevenCustomizeList.add(twoelevenCustomize);
                    }
                }
            }
            //批量添加奖励记录
            try {
                if(!CollectionUtils.isEmpty(newTwoelevenCustomizeList)){
                    twoelevenCustomizeMapper.batchInsertInvestReward(newTwoelevenCustomizeList);
                }
            } catch (Exception e) {
                logger.error("双十一秒杀活动获取实物奖励失败......", e);
            }
        }
    }

    /**
     * 批量插入实物奖励封装数据
     *
     * @param rewardName
     * @param twoelevenCustomize
     */
    private void setBatchInsertInvestReward(String rewardName,TwoelevenCustomize twoelevenCustomize) {
        twoelevenCustomize.setRewardName(rewardName);
        twoelevenCustomize.setRewardType("实物");
        twoelevenCustomize.setDistributionStatus(1);
        twoelevenCustomize.setStatus(0);
        twoelevenCustomize.setObtainTime(GetDate.formatTime2());
    }

    public String getToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成防刷token
     */
    @Override
    public void setRedisActivityToken(){
        RedisUtils.set(RedisConstants.TWO_ELEVEN_ACTIVITY_TOKEN,getToken());
    }

}
