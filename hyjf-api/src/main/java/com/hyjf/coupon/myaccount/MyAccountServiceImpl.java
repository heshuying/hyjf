package com.hyjf.coupon.myaccount;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.enums.utils.VipImageUrlEnum;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.mybatis.model.customize.coupon.CouponUserCustomize;

@Service("myAccountService")
public class MyAccountServiceImpl extends BaseServiceImpl implements MyAccountService {

    /**
     * 
     * 获取用户的VIP信息
     * @author hsy
     * @param userId
     * @return
     * @see com.hyjf.coupon.myaccount.MyAccountService#getVipInfo(java.lang.Integer)
     */
    @Override
    public VipInfoResultBean getVipInfo(Integer userId){
        String webhost = UploadFileUtils.getDoPath(PropUtils.getSystem("hyjf.api.web.url"));
        
        VipInfoResultBean result = new VipInfoResultBean();
        
        UsersInfoExample example = new UsersInfoExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UsersInfo> list = usersInfoMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            /**获取用户信息中vip信息开始*/
            if(list.get(0).getVipId()!=null&&list.get(0).getVipId()>0){
                result.setIsVip("1");
                VipInfo vipInfo=vipInfoMapper.selectByPrimaryKey(list.get(0).getVipId());
                //vip名称显示图片
                result.setVipPictureUrl(webhost + "/img/"+VipImageUrlEnum.getName(vipInfo.getVipLevel()));
                //vip等级显示图片
                result.setVipLevel(vipInfo.getVipName());
                //跳转到VIP详情页面
                result.setVipJumpUrl(webhost + "/vip/userVipDetailInit");
            }else{
                result.setIsVip("0");
                result.setVipLevel("尊享特权");
                //vip名称显示图片
                result.setVipPictureUrl(webhost + "/img/"+VipImageUrlEnum.getName(0));
                //跳转到VIP介绍页面
                result.setVipJumpUrl(webhost + "/vip/apply/vipApply");
            }
        }
        
        Integer couponValidCount = couponUserCustomizeMapper.countCouponValid(userId);
        if (couponValidCount != null && couponValidCount > 0) {
            result.setCouponDescription(couponValidCount + "张可用");
            List<CouponUserCustomize> coupons = couponUserCustomizeMapper.selectLatestCouponValidUNReadList(userId);
            LogUtil.infoLog(MyAccountServiceImpl.class.getName(), "getUserParameters", "未读优惠券数：" + coupons.size());
            if (coupons != null && !coupons.isEmpty()) {
                result.setReadFlag("1");
            } else {
                result.setReadFlag("0");
            }
        } else {
            result.setCouponDescription("暂无可用");
            result.setReadFlag("0");
        }
        
        // 我的账户更新标识(0未更新，1已更新)
        if (result.getReadFlag().equals("0")) {
            result.setIsUpdate("0");
        } else {
            result.setIsUpdate("1");
        }
        
        return result;
    }
    
    /**
     * 
     * 获取我的账户优惠券描述信息
     * @author hsy
     * @param userId
     * @return
     * @see com.hyjf.coupon.myaccount.MyAccountService#getCouponInfo(java.lang.Integer)
     */
    @Override
    public CouponInfoResultBean getCouponInfo(Integer userId){
        CouponInfoResultBean result = new CouponInfoResultBean();
        Integer couponValidCount = couponUserCustomizeMapper.countCouponValid(userId);
        if (couponValidCount != null && couponValidCount > 0) {
            result.setCouponDescription(couponValidCount + "张可用");
            List<CouponUserCustomize> coupons = couponUserCustomizeMapper.selectLatestCouponValidUNReadList(userId);
            LogUtil.infoLog(MyAccountServiceImpl.class.getName(), "getUserParameters", "未读优惠券数：" + coupons.size());
            if (coupons != null && !coupons.isEmpty()) {
                result.setReadFlag("1");
            } else {
                result.setReadFlag("0");
            }
        } else {
            result.setCouponDescription("暂无可用");
            result.setReadFlag("0");
        }
        
        // 我的账户更新标识(0未更新，1已更新)
        if (result.getReadFlag().equals("0")) {
            result.setIsUpdate("0");
        } else {
            result.setIsUpdate("1");
        }
        
        return result;
    }
    
    
}
