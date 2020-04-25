/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.plan.coupon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.invest.InvestServiceImpl;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.BorrowTenderCpnExample;
import com.hyjf.mybatis.model.auto.CouponRealTender;
import com.hyjf.mybatis.model.auto.CouponRecover;
import com.hyjf.mybatis.model.auto.CouponTender;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.DebtPlan;
import com.hyjf.mybatis.model.auto.DebtPlanExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.PlanCouponResultBean;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;

@Service
public class PlanCouponServiceImpl extends BaseServiceImpl implements PlanCouponService {
    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
    @Override
    public PlanCountCouponUsersResultBean countCouponUsers(PlanCouponBean paramBean) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("usedFlag", paramBean.getUsedFlag());
        paraMap.put("userId", paramBean.getUserId());

        Integer count = couponUserCustomizeMapper.countCouponUserForApp(paraMap);
        PlanCountCouponUsersResultBean result = new PlanCountCouponUsersResultBean();
        result.setRecordTotal(count);
        result.setStatus(BaseResultBean.STATUS_SUCCESS);
        result.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        return result;
    }

    @Override
    public Integer getUserCouponAvailableCount(String planNid, Integer userId, String money, String platform) {
        // System.out.println("~~~~~~~~~~~~~~~~getUserCouponAvailableCount~~~~~~~~~~~~~~~~~~");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        // 查询项目信息(汇计划)
        HjhPlan hjhPlan=this.getHjhPlanByPlanNid(planNid);
        String couponConfig = hjhPlan.getCouponConfig();

        List<UserCouponConfigCustomize> couponConfigs = couponConfigCustomizeMapper.getCouponConfigList(map);

        List<PlanCouponResultBean> availableCouponList = new ArrayList<PlanCouponResultBean>();

        if (money == null || "".equals(money) || money.length() == 0) {
            money = "0";
        }
        for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {

            // 验证项目加息券或体验金是否可用
            if (couponConfig.indexOf("3") == -1) {
                if (userCouponConfigCustomize.getCouponType() == 3) {
                    continue;
                }
            }
            if (couponConfig.indexOf("2") == -1) {
                if (userCouponConfigCustomize.getCouponType() == 2) {
                    continue;
                }
            }
            if (couponConfig.indexOf("1") == -1) {
                if (userCouponConfigCustomize.getCouponType() == 1) {
                    continue;
                }
            }
            // 验证项目金额
            Integer tenderQuota = userCouponConfigCustomize.getTenderQuotaType();

            if (tenderQuota == 1) {
                if (userCouponConfigCustomize.getTenderQuotaMin() > new Double(money)
                        || userCouponConfigCustomize.getTenderQuotaMax() < new Double(money)) {
                    continue;
                }
            } else if (tenderQuota == 2) {
                if (userCouponConfigCustomize.getTenderQuota() > new Double(money)) {
                    continue;
                }
            }
            // 验证项目期限
            Integer type = userCouponConfigCustomize.getProjectExpirationType();
            //月标情况下判断项目期限
            if(!"endday".equals(hjhPlan.getBorrowStyle())){

                if (type == 1) {
                    if (userCouponConfigCustomize.getProjectExpirationLength() != 
                            hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 3) {
                    if (userCouponConfigCustomize.getProjectExpirationLength() > hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 4) {
                    if (userCouponConfigCustomize.getProjectExpirationLength() < hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 2) {
                    if (userCouponConfigCustomize.getProjectExpirationLengthMin() > hjhPlan.getLockPeriod()
                            || userCouponConfigCustomize.getProjectExpirationLengthMax() < hjhPlan.getLockPeriod()) {
                        continue;
                    }
                }
            }
            //天标情况下判断项目期限
            if("endday".equals(hjhPlan.getBorrowStyle())){

                if (type == 1) {
                    if ((userCouponConfigCustomize.getProjectExpirationLength()*30) != 
                            hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 3) {
                    if ((userCouponConfigCustomize.getProjectExpirationLength()*30) > hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 4) {
                    if ((userCouponConfigCustomize.getProjectExpirationLength()*30) < hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 2) {
                    if ((userCouponConfigCustomize.getProjectExpirationLengthMin()*30) > hjhPlan.getLockPeriod()
                            || (userCouponConfigCustomize.getProjectExpirationLengthMax()*30) < hjhPlan.getLockPeriod()) {
                        continue;
                    }
                }
            }
            
            
            // 验证可使用项目
            String projectType = userCouponConfigCustomize.getProjectType();
            // String[] projectTypeArr=projectType.split(",");
            boolean ifprojectType = true;

            
            if (projectType.indexOf("6") != -1) {
                ifprojectType = false;
            }
            if (ifprojectType) {
                continue;
            }
            /**************逻辑修改 pcc start***************/
            //是否与本金公用
            boolean addFlg = false;
            if(userCouponConfigCustomize.getAddFlg()==1&&!"0".equals(money)){
                addFlg = true;
            }
            if(addFlg){
                continue;
            }
            /**************逻辑修改 pcc end***************/
            // 验证使用平台
            String couponSystem = userCouponConfigCustomize.getCouponSystem();
            String[] couponSystemArr = couponSystem.split(",");
            boolean ifcouponSystem = true;
            for (String couponSystemString : couponSystemArr) {
                if ("-1".equals(couponSystemString)) {
                    ifcouponSystem = false;
                    break;
                }
                if (platform.equals(couponSystemString)) {
                    ifcouponSystem = false;
                    break;
                }
            }
            if (ifcouponSystem) {
                continue;
            } else {
                PlanCouponResultBean couponBean = createCouponBean(userCouponConfigCustomize,new PlanCouponResultBean(), null);
                availableCouponList.add(couponBean);
            }
        }
        // System.out.println("~~~~~~~~~~~~~~~~统计结束~~~~~~~~~~~~~~~~~~"+availableCouponList.size());
        return availableCouponList.size();
    }

    /**
     * 
     * 根据borrowNid获取项目类型对象
     * @author pcc
     * @param borrowNid
     * @return
     */
    public BorrowProjectType getProjectTypeByBorrowNid(String borrowNid) {
        BorrowProjectTypeExample example = new BorrowProjectTypeExample();
        BorrowProjectTypeExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL).andBorrowClassEqualTo(borrowNid.substring(0, 3));
        List<BorrowProjectType> borrowProjectTypes = this.borrowProjectTypeMapper.selectByExample(example);
        BorrowProjectType borrowProjectType = new BorrowProjectType();
        if (borrowProjectTypes != null && borrowProjectTypes.size() > 0) {
            borrowProjectType = borrowProjectTypes.get(0);
        }
        return borrowProjectType;
    }

    private PlanCouponResultBean createCouponBean(UserCouponConfigCustomize userCouponConfigCustomize, PlanCouponResultBean couponBean, String remarks) {
        couponBean.setUserCouponId(userCouponConfigCustomize.getUserCouponId());
        couponBean.setRemarks(remarks);
        if (userCouponConfigCustomize.getCouponType() == 1) {
            couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota().intValue() + "元");
            couponBean.setCouponType("体验金");
        } else if (userCouponConfigCustomize.getCouponType() == 2) {
            couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota() + "%");
            couponBean.setCouponType("加息券");
        } else if (userCouponConfigCustomize.getCouponType() == 3) {
            couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota().intValue() + "元");
            couponBean.setCouponType("代金券");
        }
        couponBean.setCouponTypeStr(userCouponConfigCustomize.getCouponType());
        couponBean.setCouponName(userCouponConfigCustomize.getCouponName());
        couponBean.setEndTime(userCouponConfigCustomize.getCouponAddTime() + "-"
                + userCouponConfigCustomize.getEndTime());
        couponBean.setCouponAddTime(userCouponConfigCustomize.getCouponAddTime());
        couponBean.setCouponEndTime(userCouponConfigCustomize.getEndTime());
        
        String projectType = userCouponConfigCustomize.getProjectType();
	    String projectString = "";
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
        // mod by nxl 智投服务：修改汇计划->智投 start
       /* if (projectType.indexOf("6")!=-1) {
            projectString = projectString + "汇计划,";
        }*/
        if (projectType.indexOf("6")!=-1) {
            projectString = projectString + "智投,";
        }
        // mod by nxl 智投服务：修改汇计划->智投 end
	    
	    couponBean.setProjectType(projectString.substring(0,projectString.length() -1));
	    
	    String clientString = "";
	     // 操作平台
           List<ParamName> clients = this.getParamNameList("CLIENT");
	        // 被选中操作平台
	        String clientSed[] = StringUtils.split(userCouponConfigCustomize.getCouponSystem(), ",");
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
	    couponBean.setOperationPlatform(clientString.replace("Android、iOS", "APP"));
        
        if (userCouponConfigCustomize.getTenderQuotaType() == 0
                || userCouponConfigCustomize.getTenderQuotaType() == null) {
            couponBean.setTenderQuota("金额不限");
            couponBean.setInvestQuota("金额不限");
        } else if (userCouponConfigCustomize.getTenderQuotaType() == 1) {
            
            String tenderQuotaMin=userCouponConfigCustomize.getTenderQuotaMin()+"";
            if(userCouponConfigCustomize.getTenderQuotaMin()>=10000 && userCouponConfigCustomize.getTenderQuotaMin()%10000>=0){
                tenderQuotaMin=formatAmount(userCouponConfigCustomize.getTenderQuotaMin())+"万";
            }
            String tenderQuotaMax=userCouponConfigCustomize.getTenderQuotaMax()+"";
            if(userCouponConfigCustomize.getTenderQuotaMax()>=10000 && userCouponConfigCustomize.getTenderQuotaMax()%10000>=0){
                tenderQuotaMax=formatAmount(userCouponConfigCustomize.getTenderQuotaMax())+"万";
            }
            couponBean.setInvestQuota(tenderQuotaMin+ "元~"+ tenderQuotaMax + "元可用");
            couponBean.setTenderQuota( tenderQuotaMin+ "元~"+ tenderQuotaMax + "元可用");

        } else if (userCouponConfigCustomize.getTenderQuotaType() == 2) {
            String tenderQuota=userCouponConfigCustomize.getTenderQuota()+"";
            if(userCouponConfigCustomize.getTenderQuota()>=10000 && userCouponConfigCustomize.getTenderQuota()%10000>=0){
                tenderQuota=formatAmount(userCouponConfigCustomize.getTenderQuota())+"万元可用";
            }else{
            	tenderQuota=userCouponConfigCustomize.getTenderQuota()+"元可用";
            }
            couponBean.setInvestQuota(tenderQuota);
            couponBean.setTenderQuota(tenderQuota);
        }
        
        couponBean.setTime(userCouponConfigCustomize.getCouponAddTime()+"-"+userCouponConfigCustomize.getEndTime());
        if(userCouponConfigCustomize.getProjectExpirationType() != null){
	    		int i = userCouponConfigCustomize.getProjectExpirationType();
		    	switch(i) { 

		    	case 0: couponBean.setInvestTime("不限");
		    	break; 
		    	
		    	case 1:couponBean.setInvestTime(userCouponConfigCustomize.getProjectExpirationLength() + "个月"); 
		    	break;
		    	
		    	case 2: 
		    		String time = userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月" + "-" + userCouponConfigCustomize.getProjectExpirationLengthMax() + "个月";
		    		couponBean.setInvestTime(time);
		    	break;  
		    	
		    	case 3: 
		    		if(null != userCouponConfigCustomize.getProjectExpirationLengthMin()){
		    			couponBean.setInvestTime("大于等于" +userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月");
		    		}else{
		    			couponBean.setInvestTime("不限");
		    		}
		    		break;  
		    		
		    	case 4: 
		    		if(null != userCouponConfigCustomize.getProjectExpirationLengthMin()){
		    			couponBean.setInvestTime("小于等于" +userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月");
		    		}else{
		    			couponBean.setInvestTime("不限");
		    		}
		    		break;  

		    	default: 

		    		 couponBean.setInvestTime("不限");

		    	break; 

		    	}

	    }else{
	    	couponBean.setInvestTime("不限");
	    }        
        
        couponBean.setCouponUserCode(userCouponConfigCustomize.getCouponUserCode());
        return couponBean;
    }

    @Override
    public UserCouponConfigCustomize getBestCoupon(String planNid, Integer userId, String money, String platform) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);

        // 汇计划查询项目信息
        HjhPlan hjhPlan=this.getHjhPlanByPlanNid(planNid);

        String couponConfig = hjhPlan.getCouponConfig();
        List<UserCouponConfigCustomize> couponConfigs = couponConfigCustomizeMapper.getCouponConfigList(map);

        // 排序
        Collections.sort(couponConfigs, new ComparatorUserCouponConfigBean());
        for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {

            // 验证项目加息券或体验金是否可用
            if (couponConfig.indexOf("3") == -1) {
                if (userCouponConfigCustomize.getCouponType() == 3) {
                    continue;
                }
            }
            if (couponConfig.indexOf("2") == -1) {
                if (userCouponConfigCustomize.getCouponType() == 2) {
                    continue;
                }
            }
            if (couponConfig.indexOf("1") == -1) {
                if (userCouponConfigCustomize.getCouponType() == 1) {
                    continue;
                }
            }
            // 验证项目期限、
            Integer type = userCouponConfigCustomize.getProjectExpirationType();
            //月标情况下判断项目期限
            if(!"endday".equals(hjhPlan.getBorrowStyle())){
                if (type == 1) {
                    if (userCouponConfigCustomize.getProjectExpirationLength() != hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 3) {
                    if (userCouponConfigCustomize.getProjectExpirationLength() > hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 4) {
                    if (userCouponConfigCustomize.getProjectExpirationLength() < hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 2) {
                    if (userCouponConfigCustomize.getProjectExpirationLengthMin() > hjhPlan.getLockPeriod()
                            || userCouponConfigCustomize.getProjectExpirationLengthMax() < hjhPlan.getLockPeriod()) {
                        continue;
                    }
                }
            }
            //天标情况下判断项目期限
            if("endday".equals(hjhPlan.getBorrowStyle())){

                if (type == 1) {
                    if ((userCouponConfigCustomize.getProjectExpirationLength()*30) != 
                            hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 3) {
                    if ((userCouponConfigCustomize.getProjectExpirationLength()*30) > hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 4) {
                    if ((userCouponConfigCustomize.getProjectExpirationLength()*30) < hjhPlan.getLockPeriod()) {
                        continue;
                    }
                } else if (type == 2) {
                    if ((userCouponConfigCustomize.getProjectExpirationLengthMin()*30) > hjhPlan.getLockPeriod()
                            || (userCouponConfigCustomize.getProjectExpirationLengthMax()*30) < hjhPlan.getLockPeriod()) {
                        continue;
                    }
                }
            }
            // 验证项目金额
            Integer tenderQuota = userCouponConfigCustomize.getTenderQuotaType();
            if (tenderQuota == 1) {
                if (userCouponConfigCustomize.getTenderQuotaMin() > new Double(money)
                        || userCouponConfigCustomize.getTenderQuotaMax() < new Double(money)) {
                    continue;
                }
            } else if (tenderQuota == 2) {
                if (userCouponConfigCustomize.getTenderQuota() > new Double(money)) {
                    continue;
                }
            }
            // 验证优惠券适用的项目 新逻辑 pcc20160715
            String projectType = userCouponConfigCustomize.getProjectType();
            boolean ifprojectType = true;
            
            if (projectType.indexOf("6") != -1) {
                ifprojectType = false;
            }
            if (ifprojectType) {
                continue;
            }

            /**************逻辑修改 pcc start***************/
            //是否与本金公用
            boolean addFlg = false;
            if(userCouponConfigCustomize.getAddFlg()==1&&!"0".equals(money)){
                addFlg = true;
            }
            if(addFlg){
                continue;
            }
            /**************逻辑修改 pcc end***************/
            // 验证使用平台
            String couponSystem = userCouponConfigCustomize.getCouponSystem();
            String[] couponSystemArr = couponSystem.split(",");
            for (String couponSystemString : couponSystemArr) {
                if ("-1".equals(couponSystemString)) {
                    return userCouponConfigCustomize;
                }
                if (platform.equals(couponSystemString)) {
                    return userCouponConfigCustomize;
                }
            }
        }
        return null;
    }
    class ComparatorUserCouponConfigBean implements Comparator<UserCouponConfigCustomize>{
        @Override
        public int compare(UserCouponConfigCustomize couponBean1, UserCouponConfigCustomize couponBean2) {
            if(1==couponBean1.getCouponType()){
                couponBean1.setCouponType(4);
            }
            if(1==couponBean2.getCouponType()){
                couponBean2.setCouponType(4);
            }
            int flag=couponBean1.getCouponType()-couponBean2.getCouponType();
            if(4==couponBean1.getCouponType()){
                couponBean1.setCouponType(1);
            }
            if(4==couponBean2.getCouponType()){
                couponBean2.setCouponType(1);
            }
            return flag;
        }
        
    }
    @Override
    public String getCouponInterest(String planNid, String couponGrantId, String money) {
        BigDecimal couponInterest=BigDecimal.ZERO;
        UserCouponConfigCustomize couponConfig=couponConfigCustomizeMapper.getBestCouponById(couponGrantId);
        HjhPlan hjhPlan=this.getHjhPlanByPlanNid(planNid);
        // 收益率
        BigDecimal planApr =hjhPlan.getExpectApr();
        // 计划期限
        int planPeriod = hjhPlan.getLockPeriod();
        String borrowStyle = hjhPlan.getBorrowStyle();
        if(!borrowStyle.equals("endday")){
        	borrowStyle = "end";
        }
        if(couponConfig.getCouponType()==1){
           couponInterest =getInterestDj(couponConfig.getCouponQuota(),couponConfig.getCouponProfitTime(),planApr);
       }else{
           couponInterest=getInterest(borrowStyle,couponConfig.getCouponType(),planApr,
                   couponConfig.getCouponQuota(),money,planPeriod);
       }
       return couponInterest.toString();
    }
    private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr){
        BigDecimal earnings = new BigDecimal("0");
        
        earnings =couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP))
        .divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(couponProfitTime))
        .setScale(2, BigDecimal.ROUND_DOWN);

        return earnings;
        
    }

    private BigDecimal getInterest(String borrowStyle, Integer couponType, BigDecimal borrowApr,
            BigDecimal couponQuota, String money,Integer borrowPeriod) {
         BigDecimal earnings = new BigDecimal("0");
         
        // 出借金额
        BigDecimal accountDecimal = null;
        if (couponType == 1) {
            // 体验金 出借资金=体验金面值
            accountDecimal = couponQuota;
        } else if (couponType == 2) {
            // 加息券 出借资金=真实出借资金
            accountDecimal = new BigDecimal(money);
            borrowApr = couponQuota;
        } else if (couponType == 3) {
            // 代金券 出借资金=体验金面值
            accountDecimal = couponQuota;
        }
        switch (borrowStyle) {
           case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
               // 计算预期收益
               earnings = DuePrincipalAndInterestUtils.getMonthInterest(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
               break;
           case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷360*天数；
               // 计算预期收益
               earnings = DuePrincipalAndInterestUtils.getDayInterest(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
               break;
           case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：预期收益=出借金额*年化收益÷12*月数；
               // 计算预期收益
               earnings = BeforeInterestAfterPrincipalUtils.getInterestCount(accountDecimal, borrowApr.divide(new BigDecimal("100")), borrowPeriod, borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
               break;
           case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：预期收益=出借金额*年化收益÷12*月数；
               // 计算预期收益
               earnings = AverageCapitalPlusInterestUtils.getInterestCount(accountDecimal, borrowApr.divide( new BigDecimal("100")), borrowPeriod).setScale(2, BigDecimal.ROUND_DOWN);
               break;
           case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
               earnings = AverageCapitalUtils
                       .getInterestCount(accountDecimal,
                               borrowApr.divide( new BigDecimal("100")),borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
               break;
           default:
               break;
           }
        if (couponType == 3) {
            earnings=earnings.add(couponQuota);
        }
        return earnings;
    }

    @Override
    public PlanAvailableCouponResultBean getProjectAvailableUserCoupon(PlanCouponBean paramBean) {


        PlanAvailableCouponResultBean availableCouponResultBean = new PlanAvailableCouponResultBean();
        try {
            Integer userId = paramBean.getUserId();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            String planNid = paramBean.getPlanNid();
            String money = paramBean.getMoney();
            String platform = paramBean.getPlatform();
            if(StringUtils.isBlank(money)){
                money = "0";
            }
            
            // 汇计划查询项目信息
            HjhPlan hjhPlan=this.getHjhPlanByPlanNid(planNid);

            //是否可以用劵：0 不可用 1 体验金 2 加息券 3 代金券
            String couponConfig = hjhPlan.getCouponConfig();
            
            List<PlanCouponResultBean> availableCouponList = new ArrayList<PlanCouponResultBean>();
            List<PlanCouponResultBean> notAvailableCouponList = new ArrayList<PlanCouponResultBean>();

            //根据用户ID获取所有的卷
            List<UserCouponConfigCustomize> couponConfigs = couponConfigCustomizeMapper.getCouponConfigList(map);
            // 操作平台
            List<ParamName> clients = this.getParamNameList("CLIENT");

            for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
                
                /**********************
                 * 网站改版新加参数构建 pcc start
                 ***************************/
                PlanCouponResultBean couponBean = new PlanCouponResultBean();
                // 优惠券使用平台
                couponBean.setCouponSystem(createCouponSystemString(userCouponConfigCustomize.getCouponSystem(), clients));
                // 优惠券使用项目
                couponBean.setProjectType(createProjectTypeString(userCouponConfigCustomize.getProjectType()));
                // 优惠券项目期限
                couponBean.setProjectExpiration(createProjectExpiration(hjhPlan, userCouponConfigCustomize));
                couponBean.setInvestTime(createProjectExpiration(hjhPlan, userCouponConfigCustomize));
                /**********************
                 * 网站改版新加参数构建 pcc end
                 ***************************/

                // 验证优惠券适用的项目 新逻辑 pcc20160715
                String projectType = userCouponConfigCustomize.getProjectType();
                boolean ifprojectType = true;
                
                if (projectType.indexOf("6") != -1) {
                    ifprojectType = false;
                }
                
                
                if (ifprojectType) {
//                    couponBean =
//                            createCouponBean(userCouponConfigCustomize,couponBean, "适用"+createProjectTypeString(projectType));
//                    notAvailableCouponList.add(couponBean);
                    continue;
                }/**************逻辑修改 pcc start***************/
                
                // 验证项目加息券或体验金是否可用
                if (couponConfig.indexOf("3") == -1) {
                    if (userCouponConfigCustomize.getCouponType() == 3) {
                        couponBean = createCouponBean(userCouponConfigCustomize,couponBean, "该项目代金券不能使用");
                        //notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }
                if (couponConfig.indexOf("2") == -1) {
                    if (userCouponConfigCustomize.getCouponType() == 2) {
                        //couponBean = createCouponBean(userCouponConfigCustomize,couponBean, "该项目加息券不能使用");
                        //notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }
                if (couponConfig.indexOf("1") == -1) {
                    if (userCouponConfigCustomize.getCouponType() == 1) {
                     //   couponBean = createCouponBean(userCouponConfigCustomize,couponBean, "该项目体验金不能使用");
                        //notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }
                //获取出借金额类别
                Integer tenderQuota = userCouponConfigCustomize.getTenderQuotaType();

                

                // 验证项目期限
                Integer type = userCouponConfigCustomize.getProjectExpirationType();
                if(!"endday".equals(hjhPlan.getBorrowStyle())){
                    if (type == 1) {
                        if (userCouponConfigCustomize.getProjectExpirationLength() != hjhPlan.getLockPeriod()) {
//                            couponBean = createCouponBean(userCouponConfigCustomize,couponBean,
//                                    "适用" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 3) {
                        if (userCouponConfigCustomize.getProjectExpirationLength() > hjhPlan.getLockPeriod()) {
//                            couponBean = createCouponBean(userCouponConfigCustomize,couponBean,
//                                    "适用≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 4) {
                        if (userCouponConfigCustomize.getProjectExpirationLength() < hjhPlan.getLockPeriod()) {
//                            couponBean = createCouponBean(userCouponConfigCustomize,couponBean,
//                                    "适用≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 2) {
                        if (userCouponConfigCustomize.getProjectExpirationLengthMin() > hjhPlan.getLockPeriod()
                                || userCouponConfigCustomize.getProjectExpirationLengthMax() < hjhPlan.getLockPeriod()) {
//                            couponBean =
//                                    createCouponBean(userCouponConfigCustomize,couponBean,
//                                            "适用" + userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月~"
//                                                    + userCouponConfigCustomize.getProjectExpirationLengthMax()
//                                                    + "个月的项目");
//                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    }
                }
                if("endday".equals(hjhPlan.getBorrowStyle())){
                    if (type == 1) {
                        if ((userCouponConfigCustomize.getProjectExpirationLength()*30) != hjhPlan.getLockPeriod()) {
//                            couponBean = createCouponBean(userCouponConfigCustomize,couponBean,
//                                    "适用" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 3) {
                        if ((userCouponConfigCustomize.getProjectExpirationLength()*30) > hjhPlan.getLockPeriod()) {
//                            couponBean = createCouponBean(userCouponConfigCustomize,couponBean,
//                                    "适用≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 4) {
                        if ((userCouponConfigCustomize.getProjectExpirationLength()*30) < hjhPlan.getLockPeriod()) {
//                            couponBean = createCouponBean(userCouponConfigCustomize,couponBean,
//                                    "适用≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 2) {
                        if ((userCouponConfigCustomize.getProjectExpirationLengthMin()*30) > hjhPlan.getLockPeriod()
                                || (userCouponConfigCustomize.getProjectExpirationLengthMax()*30) < hjhPlan.getLockPeriod()) {
//                            couponBean =
//                                    createCouponBean(userCouponConfigCustomize,couponBean,
//                                            "适用" + userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月~"
//                                                    + userCouponConfigCustomize.getProjectExpirationLengthMax()
//                                                    + "个月的项目");
//                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    }
                }
               
                //是否与本金公用
                boolean addFlg = false;
                if(userCouponConfigCustomize.getAddFlg()==1&& BigDecimal.ZERO.compareTo(new BigDecimal(money))<0){
                    addFlg = true;
                }
                if(addFlg){
                   // couponBean =
                   //         createCouponBean(userCouponConfigCustomize,couponBean, "不能与本金共用");
                   // notAvailableCouponList.add(couponBean);
                    continue;
                }
                /**************逻辑修改 pcc end***************/

                // 验证使用平台
                String couponSystem = userCouponConfigCustomize.getCouponSystem();
                String[] couponSystemArr = couponSystem.split(",");
                boolean ifcouponSystem = true;
                for (String couponSystemString : couponSystemArr) {
                    if ("-1".equals(couponSystemString)) {
                        ifcouponSystem = false;
                        break;
                    }

                    if (platform.equals(couponSystemString)) {
                        ifcouponSystem = false;
                        break;
                    }
                }

             
                if (ifcouponSystem) {
                	
//                    couponBean = createCouponBean(userCouponConfigCustomize,couponBean,
//                            "适用"+createCouponSystemString(couponSystem, clients));
//                    notAvailableCouponList.add(couponBean);
                } else {
                	// 验证项目金额
                    if (tenderQuota == 1) {
                        if (userCouponConfigCustomize.getTenderQuotaMin() > new Double(money)
                                || userCouponConfigCustomize.getTenderQuotaMax() < new Double(money)) {
                        	String remark1 = "";
                        	if(isSatisfy(userCouponConfigCustomize.getTenderQuotaMin())){
                        		remark1 = formatAmount(userCouponConfigCustomize.getTenderQuotaMin()) + "万元~";
                        	}else{
                        		remark1 = userCouponConfigCustomize.getTenderQuotaMin() + "元~";
                        	}
                        	String remark2 = "";
                        	if(isSatisfy(userCouponConfigCustomize.getTenderQuotaMax())){
                        		remark2 = formatAmount(userCouponConfigCustomize.getTenderQuotaMax()) + "万元";
                        	}else{
                        		remark2 = userCouponConfigCustomize.getTenderQuotaMax() + "元";
                        	}
                        	
                            couponBean = createCouponBean(userCouponConfigCustomize,couponBean,remark1 + remark2);
                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (tenderQuota == 2) {
                        if (userCouponConfigCustomize.getTenderQuota() > new Double(money)) {
                        	String remark = "";
                        	if(isSatisfy(userCouponConfigCustomize.getTenderQuota())){
                        		remark = "满" + formatAmount(userCouponConfigCustomize.getTenderQuota()) + "万元可用";
                        	}else{
                        		remark = "满" + userCouponConfigCustomize.getTenderQuota() + "元可用";
                        	}
                            couponBean = createCouponBean(userCouponConfigCustomize,couponBean,remark);
                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    }
                    couponBean = createCouponBean(userCouponConfigCustomize,couponBean, "");
                    availableCouponList.add(couponBean);
                }
            }

            // 排序
            Collections.sort(availableCouponList, new ComparatorCouponBean());
            // 排序
            Collections.sort(notAvailableCouponList, new ComparatorCouponBean());
            availableCouponResultBean.setAvailableCouponList(availableCouponList);
            availableCouponResultBean.setNotAvailableCouponList(notAvailableCouponList);
            availableCouponResultBean.setAvailableCouponListCount(availableCouponList.size());
            availableCouponResultBean.setNotAvailableCouponListCount(notAvailableCouponList.size());
            availableCouponResultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            availableCouponResultBean.setStatusDesc("");
        } catch (Exception e) {
            availableCouponResultBean.setStatus(BaseResultBean.STATUS_FAIL);
            availableCouponResultBean.setStatusDesc("系统异常");
            throw e;
        }
        return availableCouponResultBean;
    
    }

    /**
     * 万元格式化
     * @param amount
     * @return
     */
    private String formatAmount(Integer amount) {
        BigDecimal total = new BigDecimal(amount);
        BigDecimal monad = new BigDecimal(10000);
        BigDecimal result = total.divide(monad);

        return result.toString();
    }

    /**
     * 判断金额是否大于10000并且是否能被10000整除
     * @param money
     * @return boolean
     */
    private boolean isSatisfy(Integer money){
    	boolean remark = false;
    	if(money >= 10000){
    		Integer i = money%10000;
    		if(i >= 0){
    			remark = true;
    		}
    	}
    	return remark;
    }
    
    private String createProjectExpiration(HjhPlan hjhPlan,
        UserCouponConfigCustomize userCouponConfigCustomize) {
     // 验证项目期限
        Integer type = userCouponConfigCustomize.getProjectExpirationType();
        if(!"endday".equals(hjhPlan.getBorrowStyle())){
            if (type == 1) {
                    return "适用" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
            } else if (type == 3) {
                    return "适用≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
            } else if (type == 4) {
                    return "适用≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
            } else if (type == 2) {
                    return "适用" + userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月~"
                                            + userCouponConfigCustomize.getProjectExpirationLengthMax()
                                            + "个月的项目";
            }else if (type == 0) {
                return "不限";
            }
            return "不限";
        }else{
            if (type == 1) {
                    return "适用" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
            } else if (type == 3) {
                    return "适用≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
            } else if (type == 4) {
                    return "适用≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
            } else if (type == 2) {
                    return "适用" + userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月~"
                                            + userCouponConfigCustomize.getProjectExpirationLengthMax()
                                            + "个月的项目";
            }else if (type == 0) {
                return "不限";
            }
            return "不限";
        }
    }

    private String createProjectTypeString(String projectType) {
        String projectString = ",";
        if (projectType.indexOf("-1") != -1) {
            projectString = "不限";
        } else {
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
            // mod by nxl 智投服务：修改汇计划->智投 start
           /* if (projectType.indexOf("6")!=-1) {
                projectString = projectString + "汇计划,";
            }*/
            if (projectType.indexOf("6")!=-1) {
                projectString = projectString + "智投,";
            }
            // mod by nxl 智投服务：修改汇计划->智投 end
            projectString = projectString.substring(1, (projectString.length() - 1));
        }
        
        return projectString;
    }
    
    private String createCouponSystemString(String couponSystem, List<ParamName> clients) {
        String clientString = "";

        // 被选中操作平台
        String clientSed[] = StringUtils.split(couponSystem, ",");
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
        return clientString.replace("Android、iOS", "APP");
    }
    
    class ComparatorCouponBean implements Comparator<PlanCouponResultBean> {
        @Override
        public int compare(PlanCouponResultBean couponBean1, PlanCouponResultBean couponBean2) {
            if(1==couponBean1.getCouponTypeStr()){
                couponBean1.setCouponTypeStr(4);
            }
            if(1==couponBean2.getCouponTypeStr()){
                couponBean2.setCouponTypeStr(4);
            }
            int flag=couponBean1.getCouponTypeStr()-couponBean2.getCouponTypeStr();
            if(4==couponBean1.getCouponTypeStr()){
                couponBean1.setCouponTypeStr(1);
            }
            if(4==couponBean2.getCouponTypeStr()){
                couponBean2.setCouponTypeStr(1);
            }
            return flag;
        }

    }
    @Override
    public DebtPlanDetailCustomize selectDebtPlanDetail(String planNid) {
     // 查询项目信息
        DebtPlanDetailCustomize debtPlanDetail = this.debtPlanCustomizeMapper.selectDebtPlanDetail(planNid);
        return debtPlanDetail;
    }

    @Override
    public CouponConfigCustomizeV2 getCouponUser(String couponGrantId, int userId) {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("couponGrantId", couponGrantId);
        paramMap.put("userId", userId);
        CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
        return ccTemp;
    }

    @Override
    public DebtPlan getPlanByNid(String planNid) {
        DebtPlanExample example = new DebtPlanExample();
        DebtPlanExample.Criteria cri = example.createCriteria();
        cri.andDebtPlanNidEqualTo(planNid);
        cri.andDelFlagEqualTo(0);
        List<DebtPlan> debtBorrowList = debtPlanMapper.selectByExample(example);
        if (debtBorrowList != null && debtBorrowList.size() > 0) {
            return debtBorrowList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean updateCouponTender(String couponGrantId, String planNid, String ordDate, int userId,
        String account, String ip, int client, int couponOldTime, String mainTenderNid, Map<String, Object> retMap) throws Exception {
        LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借开始。。。。。。");
        System.out.println("优惠券出借开始。。。。。。券编号：" + couponGrantId);
        HjhPlan hjhPlan=this.getHjhPlanByPlanNid(planNid);
        String methodName = "updateCouponTender";
        int nowTime = GetDate.getNowTime10();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("couponGrantId", couponGrantId);
        paramMap.put("userId", userId);
        String borrowStyle = hjhPlan.getBorrowStyle();
        
        //汇计划只支持按天和按月
  		if(!borrowStyle.equals("endday")){
          	borrowStyle = "end";
        }
        CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
        if (Validator.isNotNull(ccTemp)) {
            // 优惠券类别
            int couponType = ccTemp.getCouponType();
            // 面值
            BigDecimal couponQuota = ccTemp.getCouponQuota();
            // 更新时间
            int updateTime = ccTemp.getUserUpdateTime();
            // 排他校验
            if (updateTime != couponOldTime || ccTemp.getUsedFlag() != 0) {
                System.out.println("此优惠券已被使用。。。。。。券编号：" + couponGrantId);
                // 优惠券已被使用
                return false;
            }
            // 生成订单id
            String tenderNid = GetOrderIdUtils.getOrderId2(Integer.valueOf(userId));

            // 出借金额
            BigDecimal accountDecimal = null;
            BigDecimal planApr = null;
            if (couponType == 1) {
                // 体验金 出借资金=体验金面值
                accountDecimal = couponQuota;
                planApr = hjhPlan.getExpectApr();
            } else if (couponType == 2) {
                // 加息券 出借资金=真实出借资金
                accountDecimal = new BigDecimal(account);
                planApr = couponQuota;
            } else if (couponType == 3) {
                // 代金券 出借资金=体验金面值
                accountDecimal = couponQuota;
                planApr = hjhPlan.getExpectApr();
            }
            // 优惠券出借额度
            retMap.put("couponAccount", accountDecimal);
            // 优惠券面值
            retMap.put("couponQuota", couponQuota);
            // 优惠券类别
            retMap.put("couponType", couponType);
            // 出借表数据插入
            BorrowTenderCpn borrowTenderCpn = new BorrowTenderCpn();
            borrowTenderCpn.setAccount(accountDecimal);
            borrowTenderCpn.setAccountTender(new BigDecimal(0));
            borrowTenderCpn.setActivityFlag(0);//
            borrowTenderCpn.setAddip(ip);
            borrowTenderCpn.setAddtime(Integer.valueOf((new Date().getTime() / 1000) + ""));
            borrowTenderCpn.setAutoStatus(0);//
            borrowTenderCpn.setBorrowNid(planNid);
            borrowTenderCpn.setChangePeriod(0);//
            borrowTenderCpn.setChangeUserid(0);
            borrowTenderCpn.setClient(0);
            borrowTenderCpn.setContents("");//
            borrowTenderCpn.setFlag(0);//
            borrowTenderCpn.setIsok(0);
            borrowTenderCpn.setIsReport(0);
            borrowTenderCpn.setChangeStatus(0);
            
            borrowTenderCpn.setNid(tenderNid);
            borrowTenderCpn.setOrderDate(ordDate);
            borrowTenderCpn.setPeriodStatus(0);//

            /*// 预期本息收益
            borrowTenderCpn.setRecoverAccountAll(new BigDecimal(0));
            // 预期利息
            borrowTenderCpn.setRecoverAccountInterest(new BigDecimal(0));*/
            // 已收本息
            borrowTenderCpn.setRecoverAccountYes(new BigDecimal(0));
            // 已收本金
            borrowTenderCpn.setRecoverAccountCapitalYes(new BigDecimal(0));
            // 已收利息
            borrowTenderCpn.setRecoverAccountInterestYes(new BigDecimal(0));

            BigDecimal recoverAccountWait = BigDecimal.ZERO;
            BigDecimal recoverAccountCapitalWait = BigDecimal.ZERO;
            BigDecimal recoverAccountInterestWait = BigDecimal.ZERO;
            BigDecimal couponInterest =BigDecimal.ZERO;
            if(ccTemp.getCouponType()==1){
                couponInterest =getInterestDj(accountDecimal,ccTemp.getCouponProfitTime(),planApr);
            }else{
                couponInterest =this.calculateCouponInterest(borrowStyle, accountDecimal, planApr,
                        hjhPlan.getLockPeriod());
            }
            
            if (couponType == 1) {
                // 体验金 出借
                // 待收本息
                recoverAccountWait = couponInterest;
                // 待收利息
                recoverAccountInterestWait = couponInterest;
            } else if (couponType == 2) {
                // 加息券 出借
                // 待收本息
                recoverAccountWait = couponInterest;
                // 待收利息
                recoverAccountInterestWait = couponInterest;
            } else if (couponType == 3) {
                // 代金券 出借资金=体验金面值
                // 待收本息
                recoverAccountWait = couponQuota.add(couponInterest);
                // 待收本金
                recoverAccountCapitalWait = couponQuota;
                // 待收利息
                recoverAccountInterestWait = couponInterest;
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);  
            recoverAccountInterestWait=new BigDecimal(decimalFormat.format(recoverAccountInterestWait));
            retMap.put("couponInterest", recoverAccountInterestWait);
            borrowTenderCpn.setRecoverAdvanceFee(new BigDecimal(0));
            borrowTenderCpn.setRecoverFee(new BigDecimal(0));
            borrowTenderCpn.setRecoverFullStatus(0);
            borrowTenderCpn.setRecoverLateFee(new BigDecimal(0));
            borrowTenderCpn.setRecoverTimes(0);
            borrowTenderCpn.setRecoverType("");
            borrowTenderCpn.setTenderAwardAccount(new BigDecimal(0));
            borrowTenderCpn.setTenderAwardFee(new BigDecimal(0));
            borrowTenderCpn.setTenderNid(planNid);
            borrowTenderCpn.setUserId(userId);
            borrowTenderCpn.setRemark("");
            borrowTenderCpn.setWebStatus(0);
            borrowTenderCpn.setClient(client);
            // 出借类别：1：直投类，2：汇添金 3：汇计划
            borrowTenderCpn.setTenderType(3);
            // 单笔出借的融资服务费
            borrowTenderCpn.setLoanFee(new BigDecimal("0.00"));
            String remark = StringUtils.EMPTY;
            if (couponType == 1) {
                remark = "体验金<br />编号：" + ccTemp.getCouponUserCode();
            } else if (couponType == 2) {
                remark = "加息券<br />编号：" + ccTemp.getCouponUserCode();
            } else if (couponType == 3) {
                remark = "代金券<br />编号：" + ccTemp.getCouponUserCode();
            }
            borrowTenderCpn.setRemark(remark);
            
            
            borrowTenderCpn.setLoanAmount(new BigDecimal("0.00"));
            // 待收本息
            borrowTenderCpn.setRecoverAccountWait(recoverAccountWait);
            // 预期本息收益
            borrowTenderCpn.setRecoverAccountAll(recoverAccountWait);
            // 待收利息
            borrowTenderCpn.setRecoverAccountInterestWait(recoverAccountInterestWait);
            // 预期利息
            borrowTenderCpn.setRecoverAccountInterest(recoverAccountInterestWait);
            // 待收本金
            borrowTenderCpn.setRecoverAccountCapitalWait(recoverAccountCapitalWait);
            
            // 状态 0，未放款，1，已放款
            borrowTenderCpn.setStatus(1);
            // 出借状态 0，未放款，1，已放款
            borrowTenderCpn.setTenderStatus(1);
            // 放款状态 0，未放款，1，已放款
            borrowTenderCpn.setApiStatus(0);
            // 写入网站收支明细
            borrowTenderCpn.setWeb(2);
            
            
            
            
            LogUtil.infoLog(InvestServiceImpl.class.toString(), methodName, "*******borrowTenderCpn表，插入数据！");
            boolean tenderCpnFlag = borrowTenderCpnMapper.insertSelective(borrowTenderCpn) > 0 ? true : false;
            if (tenderCpnFlag) {
                System.out.println("*******borrowTenderCpn表，插入数据！券编号：" + couponGrantId);
                UsersInfo userInfo = this.getUsersInfoByUserId(userId);
                
                // 优惠券出借表
                CouponTender ct = new CouponTender();
                // 优惠券用户编号
                ct.setCouponGrantId(Integer.valueOf(couponGrantId));
                // 优惠券出借编号
                ct.setOrderId(tenderNid);
                ct.setAddTime(nowTime);
                ct.setAddUser(String.valueOf(userId));
                ct.setUpdateTime(nowTime);
                ct.setUpdateUser(String.valueOf(userId));
                ct.setDelFlg(0);
                ct.setAttribute(userInfo.getAttribute());
                LogUtil.infoLog(InvestServiceImpl.class.toString(), methodName, "*******CouponTender表，插入数据！");
                boolean couponTenderFlag = this.couponTenderMapper.insertSelective(ct) > 0 ? true : false;
                if (couponTenderFlag) {
                    System.out.println("*******CouponTender表，插入数据！券编号：" + couponGrantId);
                    // 优惠券出借与真实出借关联表
                    CouponRealTender crt = new CouponRealTender();
                    // 优惠券出借编号
                    crt.setCouponTenderId(tenderNid);
                    // 主单出借编号
                    crt.setRealTenderId(mainTenderNid);
                    crt.setAddTime(nowTime);
                    crt.setAddUser(String.valueOf(userId));
                    crt.setUpdateTime(nowTime);
                    crt.setUpdateUser(String.valueOf(userId));
                    crt.setDelFlg(0);
                    LogUtil.infoLog(InvestServiceImpl.class.toString(), methodName, "*******CouponRealTender表，插入数据！");
                    boolean couponRealTenderFlag = this.couponRealTenderMapper.insertSelective(crt) > 0 ? true : false;
                    if (couponRealTenderFlag) {
                        System.out.println("*******CouponRealTender表，插入数据！券编号：" + couponGrantId);
                        // 优惠券用户表状态
                        CouponUser cu = new CouponUser();
                        cu.setId(Integer.valueOf(couponGrantId));
                        cu.setUpdateTime(nowTime);
                        cu.setUpdateUser(String.valueOf(userId));
                        // 状态更新为 1:已使用
                        cu.setUsedFlag(1);
                        LogUtil.infoLog(InvestServiceImpl.class.toString(), methodName, "*******CouponUser表，更新使用状态！");
                        boolean couponUserFlag = this.couponUserMapper.updateByPrimaryKeySelective(cu) > 0 ? true
                                : false;
                        if (couponUserFlag) {
                            /*// 更新用户账户余额表
                            Account investAccount = new Account();
                            // 承接用户id
                            investAccount.setUserId(userId);
                            investAccount.setBankTotal(recoverAccountWait);
                            // 计划待收利息
                            investAccount.setPlanInterestWait(recoverAccountWait);
                            // 计划累计待收本金
                            investAccount.setPlanCapitalWait(BigDecimal.ZERO);
                            // 计划累计待收总额
                            investAccount.setPlanAccountWait(recoverAccountWait);
                            // 更新用户计划账户
                            boolean accountFlag =
                                    this.adminAccountCustomizeMapper.updateOfPlanJoin(investAccount) > 0 ? true : false;

                            if (accountFlag) {
                                System.out.println("*******Account表，更新数据！用户id：" + userId);
                                return true;
                            }else{
                                throw new RuntimeException("出借人计划账户信息不存在。[出借人ID：" + userId + "]，" + "[出借订单号：" + planNid + "]");
                            }*/
                            
                            if(mainTenderNid==null||mainTenderNid.length()==0){
                                
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("mqMsgId", GetCode.getRandomCode(10));
                                // 借款项目编号
                                params.put("orderIdCoupon", tenderNid);
                                rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_COUPONLOANS_HJH, JSONObject.toJSONString(params));
                            }
                            
                            System.out.println("*******couponUser表更新失败！券编号：" + couponGrantId);
                            return true;
                        } else {
                            throw new RuntimeException("couponUser表更新失败");
                        }
                    } else {
                        throw new RuntimeException("couponRealTender表插入失败");
                    }
                } else {
                    throw new RuntimeException("couponTender表插入失败");
                }
            } else {
                throw new RuntimeException("borrowTenderCpn表插入失败");
            }
        } else {
            System.out.println("此优惠券不存在，优惠券编号：" + couponGrantId);
            throw new RuntimeException("此优惠券不存在，优惠券编号：" + couponGrantId);
        }
    }

    /**
     * 计划满标或进入锁定期，更新优惠券还款收益及还款时间等
     * @author pcc
     * @param planNid
     * @param planNid
     * @return
     * @throws RuntimeException
     * @see com.hyjf.plan.coupon.PlanCouponService (java.lang.String, java.lang.Integer)
     */
    @Override
    public void updateCouponRecoverHtj(String planNid) throws RuntimeException{
        DebtPlan debtPlan = this.getPlanByNid(planNid);
        Integer recoverTime = debtPlan.getRepayTimeLast(); 
        BorrowTenderCpnExample example = new BorrowTenderCpnExample();
        example.createCriteria().andBorrowNidEqualTo(planNid);
        List<BorrowTenderCpn> cpnList = this.borrowTenderCpnMapper.selectByExample(example);
        System.out.println("*******cpnListSize:"+cpnList.size()+"*******");
        for(BorrowTenderCpn cpn:cpnList){
            this.saveRecord(cpn, recoverTime);
        }
    }
    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;
    /** 预期收益 */
    private static final String VAL_PROFIT = "val_profit";
    /** 用户ID */
    private static final String USERID = "userId";
    /** 出借金额 */
    private static final String VAL_AMOUNT = "val_amount";
    /** 优惠券出借 */
    private static final String COUPON_TYPE = "coupon_type";
    /** 优惠券出借订单编号 */
    private static final String TENDER_NID = "tender_nid";
    /** 优惠券面值 */
    private static final String VAL_COUPON_BALANCE = "val_coupon_balance";
    /** Y优惠券类型 */
    private static final String VAL_COUPON_TYPE = "val_coupon_type";
    
    private void saveRecord(BorrowTenderCpn cpn,Integer recoverTime) throws RuntimeException{
        System.out.println("*******智投编号：" + cpn.getBorrowNid()+"还款开始，出借编号："+cpn.getNid());
        int nowTime = GetDate.getNowTime10();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("nid", cpn.getNid());
        CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByNid(paramMap);
        // 不分期还款的场合
        CouponRecover cr = new CouponRecover();
        // 出借订单编号
        cr.setTenderId(cpn.getNid());
        // 还款状态（0:未还款，1：已还款）
        cr.setRecoverStatus(0);
        // 收益领取状态（1：未回款，2：未领取，3：转账中,4：转账失败，5：已领取，6：已过期）
        cr.setReceivedFlg(1);
        // 还款期数
        cr.setRecoverPeriod(1);
        
        
        if(ccTemp.getRepayTimeConfig()==2){
            System.out.println("RepayTimeConfig:"+ccTemp.getRepayTimeConfig()+"~~~~~~~~~~~~~~~~~~~couponRecoverTime:"+GetDate.countDate(nowTime, 5, ccTemp.getCouponProfitTime())+"~~~~~~~~~~~~~~~~~~~");
            cr.setRecoverTime(GetDate.countDate(nowTime, 5, ccTemp.getCouponProfitTime()));
        }else{
            System.out.println("RepayTimeConfig:"+ccTemp.getRepayTimeConfig()+"~~~~~~~~~~~~~~~~~~~couponRecoverTime:"+recoverTime+"~~~~~~~~~~~~~~~~~~~");
            // 估计还款时间
            cr.setRecoverTime(recoverTime);
        }

        
        // 应还利息
        cr.setRecoverInterest(cpn.getRecoverAccountInterestWait());
        // 应还本息
        cr.setRecoverAccount(cpn.getRecoverAccountAll());
        cr.setRecoverCapital(cpn.getRecoverAccountCapitalWait());
        // 是否已通知用户
        cr.setNoticeFlg(0);
        // 作成时间
        cr.setAddTime(nowTime);
        // 作成用户，系统自动（system）
        cr.setAddUser(CustomConstants.OPERATOR_AUTO_LOANS);
        // 更新时间
        cr.setUpdateTime(nowTime);
        // 更新用户 系统自动（system）
        cr.setUpdateUser(CustomConstants.OPERATOR_AUTO_LOANS);
        // 删除标识
        cr.setDelFlag(0);
        cr.setCurrentRecoverFlg(1);
        // 还款类别：1：直投类，2：汇添金
        cr.setRecoverType(2);
        
        boolean couponRecoverFlag =this.couponRecoverMapper.insertSelective(cr) > 0 ? true : false;
        if(couponRecoverFlag){
            System.out.println("*******CouponRecover表，插入数据！编号：" + cr.getId());
        }else{
            throw new RuntimeException("couponRecover表更新失败");
        }
        //发送消息
        sendSms(cpn,ccTemp);
    }
    
    private void sendSms(BorrowTenderCpn cpn, CouponConfigCustomizeV2 ccTemp) {
        //发送消息
        Map<String, String> msg = new HashMap<String, String>();
        // 出借人编号
        msg.put(USERID, cpn.getUserId().toString());
        // 出借额
        msg.put(VAL_AMOUNT, cpn.getRecoverAccountAll().toString());
        // 代收收益
        msg.put(VAL_PROFIT, cpn.getRecoverAccountAll().toString());
        // 优惠券类型
        msg.put(VAL_COUPON_TYPE, ccTemp.getCouponType() == 1 ? "体验金" : ccTemp.getCouponType() == 2 ? "加息券" : "代金券");
        // 优惠券面值
        msg.put(VAL_COUPON_BALANCE, ccTemp.getCouponType() == 1 ? ccTemp.getCouponQuota() + "元"
                : ccTemp.getCouponType() == 2 ? ccTemp.getCouponQuota() + "%" : ccTemp.getCouponQuota() + "元");
        // 出借订单编号
        msg.put(TENDER_NID, cpn.getNid());
        // 优惠券
        msg.put(COUPON_TYPE, "1");
        
        if (Validator.isNotNull(msg.get(USERID)) && Validator.isNotNull(msg.get(VAL_AMOUNT))
                && new BigDecimal(msg.get(VAL_AMOUNT)).compareTo(BigDecimal.ZERO) > 0) {
            Users users = getUsersByUserId(Integer.valueOf(msg.get(USERID)));
            if (users == null || Validator.isNull(users.getMobile()) || (users.getInvestSms() != null && users.getInvestSms() == 1)) {
                return;
            }
            SmsMessage smsMessage = new SmsMessage(Integer.valueOf(msg.get(USERID)), msg, null, null, MessageDefine.SMSSENDFORUSER, null,
                    CustomConstants.PARAM_TPL_COUPON_TENDER, CustomConstants.CHANNEL_TYPE_NORMAL);
            smsProcesser.gather(smsMessage);
        }
    }

    /**
     * 取出账户信息
     * 
     * @param userId
     * @return
     */
    public Account getAccountByUserId(Integer userId) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andUserIdEqualTo(userId);
        List<Account> listAccount = this.accountMapper.selectByExample(accountExample);
        if (listAccount != null && listAccount.size() > 0) {
            return listAccount.get(0);
        }
        return null;
    }

    /*
     * 计算得到优惠券预期收益
     * @author pcc
     * @param borrowStyle
     * @param couponAccount
     * @param couponRate
     * @param borrowPeriod
     * @return
     */
    private BigDecimal calculateCouponInterest(String borrowStyle,BigDecimal couponAccount,BigDecimal couponRate,Integer borrowPeriod){
        BigDecimal earnings = BigDecimal.ZERO;
        switch (borrowStyle) {
        case CalculatesUtil.STYLE_END:// 还款方式为”按月计息，到期还本还息“：预期收益=出借金额*年化收益÷12*月数；
            // 计算预期收益
                earnings = DuePrincipalAndInterestUtils
                        .getMonthInterest(couponAccount, couponRate, borrowPeriod)
                        .divide(new BigDecimal("100"));
            
            break;
        case CalculatesUtil.STYLE_ENDDAY:// 还款方式为”按天计息，到期还本还息“：预期收益=出借金额*年化收益÷360*天数；
            
                earnings = DuePrincipalAndInterestUtils
                        .getDayInterest(couponAccount, couponRate, borrowPeriod)
                        .divide(new BigDecimal("100"));
            
            break;
        case CalculatesUtil.STYLE_ENDMONTH:// 还款方式为”先息后本“：预期收益=出借金额*年化收益÷12*月数；
            
                earnings = BeforeInterestAfterPrincipalUtils
                        .getInterestCount(couponAccount, couponRate, borrowPeriod, borrowPeriod)
                        .divide(new BigDecimal("100"));
            
            break;
        case CalculatesUtil.STYLE_MONTH:// 还款方式为”等额本息“：预期收益=出借金额*年化收益÷12*月数；
            
                earnings = AverageCapitalPlusInterestUtils
                        .getInterestCount(couponAccount, couponRate, borrowPeriod)
                        .divide(new BigDecimal("100"));
            
            break;
        case CalculatesUtil.STYLE_PRINCIPAL:// 还款方式为”等额本金“
            
                earnings = AverageCapitalUtils
                        .getInterestCount(couponAccount, couponRate, borrowPeriod)
                        .divide(new BigDecimal("100"));
            
            break;
        default:
            break;
        }
        return earnings;
    }

    @Override
    public HjhPlan getHjhPlanByPlanNid(String planNid) {
        HjhPlanExample example=new HjhPlanExample();
        example.createCriteria().andPlanNidEqualTo(planNid);
        List<HjhPlan> list=hjhPlanMapper.selectByExample(example);
        return list.get(0);
    }
}
