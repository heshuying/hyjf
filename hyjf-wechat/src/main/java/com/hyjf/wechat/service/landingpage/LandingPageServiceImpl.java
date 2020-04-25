package com.hyjf.wechat.service.landingpage;

import com.hyjf.mongo.operationreport.dao.TotalInvestAndInterestMongoDao;
import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;
import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import com.hyjf.wechat.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 着陆页接口实现
 * @Author : huanghui
 */
@Service
public class LandingPageServiceImpl extends BaseServiceImpl implements LandingPageService{

    @Autowired
    private TotalInvestAndInterestMongoDao totalInvestAndInterestMongoDao;

    @Override
    public Integer selectAllUserRegisterCounts() {

        Integer userallRegistCount = dataCustomizeMapper.selectAllUserRegistCount();
        return userallRegistCount;
    }

    @Override
    public BigDecimal selectInterestSum() {
        TotalInvestAndInterestEntity entity = totalInvestAndInterestMongoDao.findOne(new Query());
        if (entity != null) {
            return entity.getTotalInterestAmount();
        }
        return BigDecimal.ZERO;
    }

    /**
     * 通过用户ID 关联用户所在的渠道
     * @param userId
     * @return
     * @Author : huanghui
     */
    @Override
    public AdminUserDetailCustomize selectUserUtmInfo(Integer userId) {
        return adminUsersCustomizeMapper.selectUserUtmInfo(userId);
    }
}
