package com.hyjf.coupon.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.CouponConfig;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.CouponUserExample;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Users;

@Service("couponCheckService")
public class CouponCheckServiceImpl extends BaseServiceImpl implements CouponCheckService {

	/**
	 * 活动是否过期
	 */
    @Override
    public String checkActivityIfAvailable(String activityId) {
        if(activityId==null){
            return CouponCheckUtilDefine.ACTIVITYID_IS_NULL;
        }
        ActivityList activityList=activityListMapper.selectByPrimaryKey(new Integer(activityId));
        if(activityList==null){
            return CouponCheckUtilDefine.ACTIVITY_ISNULL;
        }
        if(activityList.getTimeStart()>GetDate.getNowTime10()){
            return CouponCheckUtilDefine.ACTIVITY_TIME_NOT_START;
        }
        if(activityList.getTimeEnd()<GetDate.getNowTime10()){
            return CouponCheckUtilDefine.ACTIVITY_TIME_END;
        }
        return "";
    }

    /**
     * 优惠券是否过期
     */
    @Override
    public String checkCouponUserExpiryDate(String couponUserId) {
        if(couponUserId==null){
            return CouponCheckUtilDefine.COUPONUSERID_IS_NULL;
        }
        CouponUser couponUser=couponUserMapper.selectByPrimaryKey(new Integer(couponUserId));
        if(couponUser==null){
            return CouponCheckUtilDefine.COUPONUSER_IS_NULL;
        }
        if(couponUser.getEndTime()<GetDate.getNowTime10()){
            return CouponCheckUtilDefine.COUPONUSER_TIME_END;
        }
        return "";
    }

    /**
     * 检查优惠券是否可用
     */
    @Override
    public String checkCouponUserFlag(String couponUserId) {
        if(couponUserId==null){
            return CouponCheckUtilDefine.COUPONUSERID_IS_NULL;
        }
        CouponUser couponUser=couponUserMapper.selectByPrimaryKey(new Integer(couponUserId));
        if(couponUser==null){
            return CouponCheckUtilDefine.COUPONUSER_IS_NULL;
        }
        
        if(couponUser.getUsedFlag()==1){
        	// 已使用
            return CouponCheckUtilDefine.COUPONUSER_USED_FLAG;
        }
        
        if(couponUser.getUsedFlag()==4){
        	// 已失效
            return CouponCheckUtilDefine.COUPONUSER_EFFECT_FLAG;
        }
        
        return "";
    }
    
    /**
     * 验证用户优惠券使用项目
     */
    @Override
    public String checkCouponUserProjectType(String couponUserId, String projectTypeCode) {
        if(couponUserId==null){
            return CouponCheckUtilDefine.COUPONUSERID_IS_NULL;
        }
        if(projectTypeCode==null){
            return CouponCheckUtilDefine.PROJECTTYPECODE_IS_NULL;
        }
        CouponUser couponUser=couponUserMapper.selectByPrimaryKey(new Integer(couponUserId));
        CouponConfig config=couponConfigMapper.selectByPrimaryKey(new Integer(couponUser.getCouponCode()));
        if(config==null){
            return CouponCheckUtilDefine.COUPONUCONFIG_IS_NULL;
        }
        String projectType=config.getProjectType();
        if(projectType==null||projectType.length()==0){
            return CouponCheckUtilDefine.PROJECTTYPE_IS_NULL;
        }
        String[] projectTypeArr=projectType.split(",");
        for (String projectTypeString : projectTypeArr) {
            if("-1".equals(projectTypeString)){
                return "";
            }
            if(projectTypeCode.equals(projectTypeString)){
                return "";
            }
        }
        return CouponCheckUtilDefine.COUPONUSER_NOT_USED;
    }

    
    /**
     * 验证用户优惠券使用项目时长
     */
    @Override
    public String checkProjectTerm(String couponUserId, String borrowNid) {
        if(couponUserId==null){
            return CouponCheckUtilDefine.COUPONUSERID_IS_NULL;
        }
        if(borrowNid==null){
            return CouponCheckUtilDefine.BORROWNID_IS_NULL;
        }
        CouponUser couponUser=couponUserMapper.selectByPrimaryKey(new Integer(couponUserId));
        CouponConfig config=couponConfigMapper.selectByPrimaryKey(new Integer(couponUser.getCouponCode()));
        if(config==null){
            return CouponCheckUtilDefine.COUPONUSER_IS_NULL;
        }
        BorrowExample example=new BorrowExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrowList=borrowMapper.selectByExample(example);
        if(borrowList==null||borrowList.size()==0){
            return CouponCheckUtilDefine.BORROW_IS_NULL;
        }
        Borrow borrow=borrowList.get(0);
        Integer type=config.getProjectExpirationType();
        if(type==1){
            if(config.getProjectExpirationLength()!= borrow.getBorrowPeriod()){
                return CouponCheckUtilDefine.COUPONUSER_NOT_USED;
            }
        }else if(type==3){
            if(config.getProjectExpirationLength()<= borrow.getBorrowPeriod()){
                return CouponCheckUtilDefine.COUPONUSER_NOT_USED;
            }
        }else if(type==4){
            if(config.getProjectExpirationLength()>= borrow.getBorrowPeriod()){
                return CouponCheckUtilDefine.COUPONUSER_NOT_USED;
            }
        }else if(type==2){
            if(config.getProjectExpirationLengthMin()>= borrow.getBorrowPeriod()||config.getProjectExpirationLengthMax()<= borrow.getBorrowPeriod()){
                return CouponCheckUtilDefine.COUPONUSER_NOT_USED;
            }
        }
        return "";
    }

    /**
     * 验证用户优惠券使用项目金额
     */
    @Override
    public String checkProjectAmount(String couponUserId, String borrowNid) {
        if(couponUserId==null){
            return CouponCheckUtilDefine.COUPONUSERID_IS_NULL;
        }
        if(borrowNid==null){
            return CouponCheckUtilDefine.BORROWNID_IS_NULL;
        }
        CouponUser couponUser=couponUserMapper.selectByPrimaryKey(new Integer(couponUserId));
        CouponConfig config=couponConfigMapper.selectByPrimaryKey(new Integer(couponUser.getCouponCode()));
        if(config==null){
            return CouponCheckUtilDefine.COUPONUSER_IS_NULL;
        }
        BorrowExample example=new BorrowExample();
        example.createCriteria().andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrowList=borrowMapper.selectByExample(example);
        if(borrowList==null||borrowList.size()==0){
            return CouponCheckUtilDefine.BORROW_IS_NULL;
        }
        Borrow borrow=borrowList.get(0);
        Integer tenderQuota=config.getTenderQuotaType();
        if(tenderQuota==1){
            if(config.getTenderQuotaMin()> borrow.getAccount().doubleValue()||config.getTenderQuotaMax()<borrow.getAccount().doubleValue()){
                return CouponCheckUtilDefine.COUPONUSER_NOT_USED;
            }
        }else if(tenderQuota==2){
            if(config.getTenderQuota()< borrow.getAccount().doubleValue()){
                return CouponCheckUtilDefine.COUPONUSER_NOT_USED;
            }
        }
        return "";
    }

    @Override
    public String checkActivityPlatform(String activityId, String platform) {
        if(activityId==null){
            return CouponCheckUtilDefine.ACTIVITYID_IS_NULL;
        }
        ActivityList activityList=activityListMapper.selectByPrimaryKey(new Integer(activityId));
        if(activityList.getPlatform().indexOf(platform)==-1){
            
         // 操作平台
            List<ParamName> clients = this.getParamNameList("CLIENT");
         // 被选中操作平台
            String clientSed[] = StringUtils.split(activityList.getPlatform(),",");
            StringBuffer selectedClientDisplayBuffer=new StringBuffer();
            for (String client : clientSed) {
                // 被选中的平台编号
                for (ParamName pn : clients) {
                    if (StringUtils.equals(pn.getNameCd(), client)) {
                        // 被选中的平台名称 表示用
                        selectedClientDisplayBuffer.append(pn.getName());
                        selectedClientDisplayBuffer.append("/");
                    }
                }

            }
            return CouponCheckUtilDefine.PLATFORM_LIMIT.replace("***", selectedClientDisplayBuffer.toString());
        }
        return "";
    }

    @Override
    public String checkOldUser(String activityId, String userId) {
        if(activityId==null){
            return CouponCheckUtilDefine.ACTIVITYID_IS_NULL;
        }
        if(userId==null){
            return CouponCheckUtilDefine.USER_ID_IS_NULL;
        }
        Users users=usersMapper.selectByPrimaryKey(new Integer(userId));
        if(users==null){
            return CouponCheckUtilDefine.USER_IS_NULL;
        }
        ActivityList activityList=activityListMapper.selectByPrimaryKey(new Integer(activityId));
        if(activityList==null){
            return CouponCheckUtilDefine.ACTIVITY_ISNULL;
        }
        if(activityList.getTimeStart()>users.getRegTime()||activityList.getTimeEnd()<users.getRegTime()){
            return CouponCheckUtilDefine.OLD_USER_MESSAGE;
        }
        
        return "";
    }
    
    @Override
    public String checkActivityIfInvolvement(String activityId, String userId) {
        if(activityId==null){
            return CouponCheckUtilDefine.ACTIVITYID_IS_NULL;
        }
        if(userId==null){
            return CouponCheckUtilDefine.USER_ID_IS_NULL;
        }
        CouponUserExample example=new CouponUserExample();
        example.createCriteria().andActivityIdEqualTo(new Integer(activityId)).
            andUserIdEqualTo(new Integer(userId));
        List<CouponUser> list=couponUserMapper.selectByExample(example);
        if(list!=null&&list.size()>0){
            return CouponCheckUtilDefine.ACTIVITY_LIMIT;
        }
        return "";
    }

    

}









