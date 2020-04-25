package com.hyjf.wechat.service.landingpage;

import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import com.hyjf.wechat.base.BaseService;

import java.math.BigDecimal;

/**
 * 着陆页接口
 * @Author : huanghui
 */
public interface LandingPageService extends BaseService {

    /**
     * 累计注册数(包含未开户)
     * @return
     */
    Integer selectAllUserRegisterCounts();


    /**
     * 累计收益
     * @return
     */
    BigDecimal selectInterestSum();

    /**
     * 通过用户ID 关联用户所在的渠道
     * @param userId
     * @return
     * @Author : huanghui
     */
    AdminUserDetailCustomize selectUserUtmInfo(Integer userId);
}
