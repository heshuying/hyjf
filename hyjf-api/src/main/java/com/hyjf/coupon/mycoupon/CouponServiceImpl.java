package com.hyjf.coupon.mycoupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.CouponUserExample;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponUserForAppCustomize;

@Service("couponService")
public class CouponServiceImpl extends BaseServiceImpl implements CouponService {

    /**
     * 获取用户的优惠券列表
     * @return
     */
    @Override
    public List<CouponUserForAppCustomize> getCouponUserList(Map<String, Object> paraMap) {
    	List<CouponUserForAppCustomize> list =  couponUserCustomizeMapper.selectCouponUserListForApp(paraMap);
    	List<CouponUserForAppCustomize> dateList = new ArrayList<>();
    	for (CouponUserForAppCustomize couponUserForAppCustomize : list) {
			
    		String projectType = couponUserForAppCustomize.getProjectType();
    		String projectString = ",";
    		//勾选汇直投，尊享汇，融通宝
    		if (projectType.indexOf("1")!=-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")!=-1) {
    			projectString = projectString + "散标,";
    		}
    		//勾选汇直投  未勾选尊享汇，融通宝
    		if (projectType.indexOf("1")!=-1&&projectType.indexOf("4")==-1&&projectType.indexOf("7")==-1) {
    			projectString = projectString + "散标,";
    		}
    		//勾选汇直投，融通宝  未勾选尊享汇
    		if(projectType.indexOf("1")!=-1&&projectType.indexOf("4")==-1&&projectType.indexOf("7")!=-1){
    			projectString = projectString + "散标,";
    		}
    		//勾选汇直投，选尊享汇 未勾选融通宝
    		if(projectType.indexOf("1")!=-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")==-1){
    			projectString = projectString + "散标,";
    		}
    		//勾选尊享汇，融通宝  未勾选直投
    		if(projectType.indexOf("1")==-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")!=-1){
    			projectString = projectString + "散标,";
    		}
    		//勾选尊享汇  未勾选直投，融通宝
    		if(projectType.indexOf("1")==-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")==-1){
    			projectString = projectString + "散标,";
    		}
    		//勾选尊享汇  未勾选直投，融通宝
    		if(projectType.indexOf("1")==-1&&projectType.indexOf("4")==-1&&projectType.indexOf("7")!=-1){
    			projectString = projectString + "散标,";
    		}
    		
    		if (projectType.indexOf("3")!=-1) {
    			projectString = projectString + "新手,";
    		}
    		/*if (projectType.indexOf("5")!=-1) {
            projectString = projectString + "汇添金,";
        }*/
    		// mod by nxl 智投服务：修改汇计划->智投服务 start
    		/*if (projectType.indexOf("6")!=-1) {
    			projectString = projectString + "汇计划,";
    		}*/
			if (projectType.indexOf("6")!=-1) {
				projectString = projectString + "智投,";
			}
			// mod by nxl 智投服务：修改汇计划->智投服务 end
    		couponUserForAppCustomize.setProjectType(projectString.substring(1,projectString.length() -1));
    		
    		String clientString = "";
   	     // 操作平台
               List<ParamName> clients = this.getParamNameList("CLIENT");
   	        // 被选中操作平台
   	        String clientSed[] = StringUtils.split(couponUserForAppCustomize.getOperationPlatform(), ",");
   	        for (int i = 0; i < clientSed.length; i++) {
   	            if ("-1".equals(clientSed[i])) {
   	                clientString = clientString + "全部平台";
   	                break;
   	            } else {
   	                for (ParamName paramName : clients) {
   	                    if (clientSed[i].equals(paramName.getNameCd())) {
   	                        if (i != 0 && clientString.length() != 0) {
   	                            clientString = clientString + "、";
   	                        }
   	                        clientString = clientString + paramName.getName();

   	                    }
   	                }
   	            }
   	        }
   	        if(clientString.contains("Android、iOS")){
				clientString = clientString.replace("Android、iOS", "APP");
   	        }
   	        couponUserForAppCustomize.setOperationPlatform(clientString);

	   	     if("1".equals(couponUserForAppCustomize.getCouponType())){
	   	    	couponUserForAppCustomize.setCouponQuota(couponUserForAppCustomize.getCouponQuota());
	 	    }else if("2".equals(couponUserForAppCustomize.getCouponType())){
	 	    	couponUserForAppCustomize.setCouponQuota(couponUserForAppCustomize.getCouponQuota()+"%");
	 	    }else if("3".equals(couponUserForAppCustomize.getCouponType())){
	 	    	couponUserForAppCustomize.setCouponQuota(couponUserForAppCustomize.getCouponQuota());
	         }
	   	  if(couponUserForAppCustomize.getProjectExpirationType() != null){
		    		int i = couponUserForAppCustomize.getProjectExpirationType();
			    	switch(i) { 

			    	case 0: couponUserForAppCustomize.setInvestTime("不限");
			    	break; 
			    	
			    	case 1:couponUserForAppCustomize.setInvestTime(couponUserForAppCustomize.getProjectExpirationLength() + "个月"); 
			    	break;
			    	
			    	case 2: 
			    		String time = couponUserForAppCustomize.getProjectExpirationLengthMin() + "个月" + "-" + couponUserForAppCustomize.getProjectExpirationLengthMax() + "个月";
			    		couponUserForAppCustomize.setInvestTime(time);
			    	break;  
			    	
			    	case 3: 
			    		if(null != couponUserForAppCustomize.getProjectExpirationLength()){
			    			couponUserForAppCustomize.setInvestTime("适用于≥" +couponUserForAppCustomize.getProjectExpirationLength() + "个月的项目");
			    		}else{
			    			couponUserForAppCustomize.setInvestTime("");
			    		}
			    		break;  
			    		
			    	case 4: 
			    		if(null != couponUserForAppCustomize.getProjectExpirationLength()){
			    			couponUserForAppCustomize.setInvestTime("适用于≤" +couponUserForAppCustomize.getProjectExpirationLength() + "个月的项目");
			    		}else{
			    			couponUserForAppCustomize.setInvestTime("");
			    		}
			    		break;  

			    	default: 

			    		couponUserForAppCustomize.setInvestTime("不限");

			    	break; 

			    	}

		    }else{
		    	couponUserForAppCustomize.setInvestTime("");
		    }
	   	  		if(couponUserForAppCustomize.getTenderQuotaType() == 0){
	   	  			couponUserForAppCustomize.setInvestQuota(couponUserForAppCustomize.getTenderQuota());
	   	  		}else if (couponUserForAppCustomize.getTenderQuotaType() == 1) {
	   	  			Long min = Long.parseLong(couponUserForAppCustomize.getTenderQuotaMin());
	   	  			Long max = Long.parseLong(couponUserForAppCustomize.getTenderQuotaMax());
	   	  			String mins = "";
	   	  			String maxs = "";
					if( min >= 10000){
						Long i = min%10000;
						if(i >=0){
							mins = formatAmount(min) + "万元~";
						}else{
							mins = couponUserForAppCustomize.getTenderQuotaMin() + "元~";
						}
					}else{
						mins = couponUserForAppCustomize.getTenderQuotaMin() + "元~";
					}
					if( max >= 10000){
						Long i = max%10000;
						if(i >=0){
							maxs = formatAmount(max) + "万元可用";
						}else{
							maxs = couponUserForAppCustomize.getTenderQuotaMax() + "元可用";
						}
					}else{
						maxs = couponUserForAppCustomize.getTenderQuotaMax() + "元可用";
					}
					couponUserForAppCustomize.setInvestQuota(mins+maxs);
				}else if (couponUserForAppCustomize.getTenderQuotaType() == 2) {
					if(Long.parseLong(couponUserForAppCustomize.getTenderQuotaValue()) >= 10000){
						Long i = Long.parseLong(couponUserForAppCustomize.getTenderQuotaValue())%10000;
						if(i >= 0){
							couponUserForAppCustomize.setInvestQuota("满"+formatAmount(Long.parseLong(couponUserForAppCustomize.getTenderQuotaValue()))+"万元可用");
						}else {
							couponUserForAppCustomize.setInvestQuota(couponUserForAppCustomize.getTenderQuota());
						}
					}else{
						couponUserForAppCustomize.setInvestQuota(couponUserForAppCustomize.getTenderQuota());
					}
				}else if (couponUserForAppCustomize.getTenderQuotaType() == 3) {
					if(Long.parseLong(couponUserForAppCustomize.getTenderQuotaValue()) >= 10000){
						Long i = Long.parseLong(couponUserForAppCustomize.getTenderQuotaValue())%10000;
						if(i >= 0){
							couponUserForAppCustomize.setInvestQuota(formatAmount(Long.parseLong(couponUserForAppCustomize.getTenderQuotaValue()))+"万元（含）内可用");
						}else {
							couponUserForAppCustomize.setInvestQuota(couponUserForAppCustomize.getTenderQuota());
						}
					}else{
						couponUserForAppCustomize.setInvestQuota(couponUserForAppCustomize.getTenderQuota());
					}
				}else {
					couponUserForAppCustomize.setInvestQuota(couponUserForAppCustomize.getTenderQuota());
				}
	   	  		
	   	  
    		dateList.add(couponUserForAppCustomize);
		}
        return dateList;
    }

	/**
	 * 万元格式化
	 * @param amount
	 * @return
	 */
	private String formatAmount(Long amount) {
		BigDecimal total = new BigDecimal(amount);
		BigDecimal monad = new BigDecimal(10000);
		BigDecimal result = total.divide(monad);

    	return result.toString();
	}

	/**
     * 用户的优惠券总数
     * @author hsy
     * @param couponUserCustomize
     * @return
     */
    @Override
    public Integer countCouponUsers(Map<String, Object> paraMap){
        return couponUserCustomizeMapper.countCouponUserForApp(paraMap);
    }
    
    /**
     * 获取优惠券详情
     */
    @Override
    public CouponTenderDetailCustomize getUserCouponDetail(Map<String,Object> paramMap){
        return couponTenderCustomizeMapper.selectCouponTenderDetailCustomize(paramMap);
    }
    
    /**
     * 获取优惠券收益
     */
    @Override
    public List<CouponRecoverCustomize> getUserCouponRecover(Map<String,Object> paramMap) {
        return couponTenderCustomizeMapper.getCouponRecoverCustomize(paramMap);
    }
    
    /**
     * 更新新增优惠券的已读状态
     */
    @Override
    public int updateCouponReadFlag(Integer userId, Integer readFlag){
        CouponUser couponUser = new CouponUser();
        couponUser.setReadFlag(readFlag);
        
        CouponUserExample example = new CouponUserExample();
        CouponUserExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId).andUsedFlagEqualTo(CustomConstants.USER_COUPON_STATUS_UNUSED).andDelFlagEqualTo(Integer.parseInt(CustomConstants.FLAG_NORMAL));
        
        return couponUserMapper.updateByExampleSelective(couponUser, example);
    }
    
    
}
