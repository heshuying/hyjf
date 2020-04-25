package com.hyjf.wechat.service.myprofile;

import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import com.hyjf.mybatis.model.customize.web.CouponUserListCustomize;
import com.hyjf.wechat.base.BaseService;
import com.hyjf.wechat.controller.user.myprofile.MyProfileVO;

import java.util.List;

/**
 * Created by cuigq on 2018/2/1.
 */
public interface MyProfileService extends BaseService {

    String getUserTrueName(Integer userId);

    void buildUserAccountInfo(Integer userId, MyProfileVO.UserAccountInfo userAccountInfo);

    void buildOutInfo(Integer userId, MyProfileVO myProfileVO);

    List<CouponUserListCustomize> queryUserCouponList(Integer userId);

	String getUserCouponsData(String couponStatus, Integer page, Integer pageSize, Integer userId, String host);

    /**
     * 通过用户ID 关联用户所在的渠道
     * @param userId
     * @return
     * @Author : huanghui
     */
    AdminUserDetailCustomize selectUserUtmInfo(Integer userId);
}
