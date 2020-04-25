/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.bank.service.activity;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.mybatis.mapper.auto.TwoelevenInvestmentMapper;
import com.hyjf.mybatis.mapper.customize.TwoelevenCustomizeMapper;
import com.hyjf.mybatis.model.auto.ActivityMidauInfo;
import com.hyjf.mybatis.model.auto.TwoelevenInvestment;
import com.hyjf.mybatis.model.customize.admin.TwoelevenCustomize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yinhui
 * @version TwoElevenActvityServiceImpl, v0.1 2018/10/16 9:32
 */
@Service("bankTwoElevenActvityServiceImpl")
public class TwoElevenActvityServiceImpl extends BaseServiceImpl implements TwoElevenActvityService {

    private Logger _log = LoggerFactory.getLogger(TwoElevenActvityServiceImpl.class);

    @Autowired
    private TwoelevenCustomizeMapper twoelevenCustomizeMapper;
    @Autowired
    private TwoelevenInvestmentMapper twoelevenInvestmentMapper;

    @Override
    public boolean saveTwoelevenInvestment(Integer userId, String orderId, Integer productType, BigDecimal investMoney){

        TwoelevenCustomize customize = twoelevenCustomizeMapper.getUserInfos(userId);
        if(customize == null){
            _log.info("【双十一活动】找不到用户信息orderId：" + orderId+",userId="+userId);
            return false;
        }

        List<ActivityMidauInfo> tenderList = null;
        ActivityMidauInfo activityMidauInfo = null;
        //1=新手标，2=散标，3=汇计划
        if(productType == 3){
            //查询加入汇计划的出借信息
            tenderList = activityMidauInfoCustomizeMapper.queryPlanList(orderId,userId);
            if(CollectionUtils.isEmpty(tenderList)){
                _log.error("【双十一活动】TwoElevenActvityServiceImpl用户加入汇计划信息为空，orderId:"+orderId+",userId:"+userId);
                return true;
            }
            activityMidauInfo = tenderList.get(0);

        }else{
            //查询加出借散标的出借信息
            tenderList = activityMidauInfoCustomizeMapper.queryTenderList(orderId,userId);
            if(CollectionUtils.isEmpty(tenderList)){
                _log.error("【双十一活动】TwoElevenActvityServiceImpl用户出借的散标信息为空，orderId:"+orderId+",userId:"+userId);
                return true;
            }

            activityMidauInfo = tenderList.get(0);
        }

        BigDecimal yearAmount = BigDecimal.ZERO;
        //累计年化出借金额=SUM（m1*n1/12+m2*n2/12+...），m为单笔出借金额，n为单笔出借期限。
        //月=出借金额*几个月/12
        if(activityMidauInfo.getProductStyle().contains("个月")){
            String number = StringUtils.substringBefore(activityMidauInfo.getProductStyle(),"个月");
            if(StringUtils.isEmpty(number)){
                return false;
            }
            yearAmount = investMoney.multiply(new BigDecimal(number)).divide(new BigDecimal(12),4,BigDecimal.ROUND_HALF_UP);
        }
        //天=出借金额*天数/360
        else if(activityMidauInfo.getProductStyle().contains("天")){
            String number = StringUtils.substringBefore(activityMidauInfo.getProductStyle(),"天");
            if(StringUtils.isEmpty(number)){
                return false;
            }
            yearAmount = investMoney.multiply(new BigDecimal(number)).divide(new BigDecimal(360),4,BigDecimal.ROUND_HALF_UP);
        }

        if(yearAmount.compareTo(BigDecimal.ZERO) <= 0){
            _log.info("【双十一活动】用户年化出借金额小于或等于0 orderId:"+orderId+",userId:"+userId+",yearAmount="+yearAmount);
            return false;
        }

        TwoelevenInvestment twoelevenInvestment = new TwoelevenInvestment();
        twoelevenInvestment.setUserId(userId);
        twoelevenInvestment.setUsername(customize.getUsername());
        twoelevenInvestment.setTruename(customize.getTruename());
        twoelevenInvestment.setInvestAmount(investMoney);
        twoelevenInvestment.setInvestType(productType);
        twoelevenInvestment.setOrderId(orderId);
        twoelevenInvestment.setYearAmount(yearAmount);


        twoelevenInvestmentMapper.insert(twoelevenInvestment);
        _log.info("【双十一活动】保存用户信息orderId：" + orderId);
        return false;
    }

}
