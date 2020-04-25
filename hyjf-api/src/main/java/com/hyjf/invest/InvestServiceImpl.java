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

package com.hyjf.invest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.AverageCapitalPlusInterestUtils;
import com.hyjf.common.calculate.AverageCapitalUtils;
import com.hyjf.common.calculate.BeforeInterestAfterPrincipalUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.CouponRealTender;
import com.hyjf.mybatis.model.auto.CouponTender;
import com.hyjf.mybatis.model.auto.CouponUser;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;

@Service
public class InvestServiceImpl extends BaseServiceImpl implements InvestService {

    @Override
    public AvailableCouponResultBean getProjectAvailableUserCoupon(InvestBean investBean) {

        AvailableCouponResultBean availableCouponResultBean = new AvailableCouponResultBean();
        try {
            Integer userId = investBean.getUserId();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userId);
            String borrowNid = investBean.getBorrowNid();
            String money = investBean.getMoney();
            String platform = investBean.getPlatform();
            // 查询项目信息
            Borrow borrow = this.getBorrowByNid(borrowNid);
            List<CouponBean> availableCouponList = new ArrayList<CouponBean>();
            List<CouponBean> notAvailableCouponList = new ArrayList<CouponBean>();
            BorrowProjectType borrowProjectType = getProjectTypeByBorrowNid(borrowNid);
            Integer couponFlg = borrow.getBorrowInterestCoupon();
            Integer moneyFlg = borrow.getBorrowTasteMoney();

            List<UserCouponConfigCustomize> couponConfigs = couponConfigCustomizeMapper.getCouponConfigList(map);
            // 操作平台
            List<ParamName> clients = this.getParamNameList("CLIENT");

            String style = borrow.getBorrowStyle();
            for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
                // couponFlg=1
                // moneyFlg=1
                // 验证项目加息券或体验金是否可用
                if (couponFlg != null && couponFlg == 0) {
                    if (userCouponConfigCustomize.getCouponType() == 2) {
                        CouponBean couponBean = createCouponBean(userCouponConfigCustomize, "该项目加息券不能使用");
                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }
                if (moneyFlg != null && moneyFlg == 0) {
                    if (userCouponConfigCustomize.getCouponType() == 1) {
                        CouponBean couponBean = createCouponBean(userCouponConfigCustomize, "该项目优惠券不能使用");
                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }

                // 验证项目金额
                Integer tenderQuota = userCouponConfigCustomize.getTenderQuotaType();

                if (tenderQuota == 1) {
                    if (userCouponConfigCustomize.getTenderQuotaMin() > new Double(money)
                            || userCouponConfigCustomize.getTenderQuotaMax() < new Double(money)) {
                        CouponBean couponBean = createCouponBean(userCouponConfigCustomize,
                                userCouponConfigCustomize.getTenderQuotaMin() + "元~"
                                        + userCouponConfigCustomize.getTenderQuotaMax() + "元可用");
                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                } else if (tenderQuota == 2) {
                    if (userCouponConfigCustomize.getTenderQuota() > new Double(money)) {
                        CouponBean couponBean = createCouponBean(userCouponConfigCustomize,
                                "满" + userCouponConfigCustomize.getTenderQuota() + "元可用");
                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }

                // 验证项目期限
                Integer type = userCouponConfigCustomize.getProjectExpirationType();
                if ("endday".equals(style)) {
                    if (type == 1) {
                        if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) != borrow.getBorrowPeriod()) {
                            CouponBean couponBean = createCouponBean(userCouponConfigCustomize,
                                    "适用" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 3) {
                        if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) > borrow.getBorrowPeriod()) {
                            CouponBean couponBean = createCouponBean(userCouponConfigCustomize,
                                    "适用≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 4) {
                        if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) < borrow.getBorrowPeriod()) {
                            CouponBean couponBean = createCouponBean(userCouponConfigCustomize,
                                    "适用≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 2) {
                        if ((userCouponConfigCustomize.getProjectExpirationLengthMin() * 30) > borrow.getBorrowPeriod()
                                || (userCouponConfigCustomize.getProjectExpirationLengthMax() * 30) < borrow
                                        .getBorrowPeriod()) {
                            CouponBean couponBean =
                                    createCouponBean(userCouponConfigCustomize,
                                            "适用" + userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月~"
                                                    + userCouponConfigCustomize.getProjectExpirationLengthMax()
                                                    + "个月的项目");
                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    }
                } else {
                    if (type == 1) {
                        if (userCouponConfigCustomize.getProjectExpirationLength() != borrow.getBorrowPeriod()) {
                            CouponBean couponBean = createCouponBean(userCouponConfigCustomize,
                                    "适用" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 3) {
                        if (userCouponConfigCustomize.getProjectExpirationLength() > borrow.getBorrowPeriod()) {
                            CouponBean couponBean = createCouponBean(userCouponConfigCustomize,
                                    "适用≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 4) {
                        if (userCouponConfigCustomize.getProjectExpirationLength() < borrow.getBorrowPeriod()) {
                            CouponBean couponBean = createCouponBean(userCouponConfigCustomize,
                                    "适用≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    } else if (type == 2) {
                        if (userCouponConfigCustomize.getProjectExpirationLengthMin() > borrow.getBorrowPeriod()
                                || userCouponConfigCustomize.getProjectExpirationLengthMax() < borrow
                                        .getBorrowPeriod()) {
                            CouponBean couponBean =
                                    createCouponBean(userCouponConfigCustomize,
                                            "适用" + userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月~"
                                                    + userCouponConfigCustomize.getProjectExpirationLengthMax()
                                                    + "个月的项目");
                            notAvailableCouponList.add(couponBean);
                            continue;
                        }
                    }

                }

                // 验证可使用项目 pcc20160715
                String projectType = userCouponConfigCustomize.getProjectType();
                boolean ifprojectType = true;
                if (projectType.indexOf("-1") != -1) {
                	if(!"RTB".equals(borrowProjectType.getBorrowClass())){
                		ifprojectType = false;
                	}
                } else {
                    if ("HXF".equals(borrowProjectType.getBorrowClass())) {
                        if (projectType.indexOf("2") != -1) {
                            ifprojectType = false;
                        }
                    } else if ("NEW".equals(borrowProjectType.getBorrowClass())) {
                        if (projectType.indexOf("3") != -1) {
                            ifprojectType = false;
                        }
                    } else if ("ZXH".equals(borrowProjectType.getBorrowClass())) {
                        if (projectType.indexOf("4") != -1) {
                            ifprojectType = false;
                        }
                    } else {
                    	if(projectType.indexOf("1")!=-1){
	                		if(!"RTB".equals(borrowProjectType.getBorrowClass())){
	                    		ifprojectType=false; 
	                    	}
	                    }
                    }
                }
                if (ifprojectType) {
                    CouponBean couponBean =
                            createCouponBean(userCouponConfigCustomize, createProjectTypeString(projectType));
                    notAvailableCouponList.add(couponBean);
                    continue;
                }
                /**************逻辑修改 pcc start***************/
                //是否与本金公用
                boolean addFlg = false;
                if(userCouponConfigCustomize.getAddFlg()==1&&!"0".equals(money)){
                    addFlg = true;
                }
                if(addFlg){
                    CouponBean couponBean =
                            createCouponBean(userCouponConfigCustomize, "不能与本金共用");
                    notAvailableCouponList.add(couponBean);
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
                    CouponBean couponBean = createCouponBean(userCouponConfigCustomize,
                            createCouponSystemString(couponSystem, clients));
                    notAvailableCouponList.add(couponBean);
                } else {
                    CouponBean couponBean = createCouponBean(userCouponConfigCustomize, "");
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
        return "适用" + clientString;
    }

    private String createProjectTypeString(String projectType) {
        // 被选中项目类型 pcc20160715
        String projectString = "";
        // 被选中项目类型
        String projectSed[] = StringUtils.split(projectType, ",");
        if (projectType.indexOf("-1") != -1) {
            projectString = "所有散标/新手/智投项目";
        } else {
            projectString = projectString + "所有";
            for (String project : projectSed) {
                if ("1".equals(project)) {
                    projectString = projectString + "散标/";
                }
                if ("2".equals(project)) {
                    projectString = projectString + "";
                }
                if ("3".equals(project)) {
                    projectString = projectString + "新手/";
                }
                if ("4".equals(project)) {
                    projectString = projectString + "";
                }
                if ("5".equals(project)) {
                    projectString = projectString + "";
                }
                if ("6".equals(project)) {
                    projectString = projectString + "智投/";
                }

            }
            projectString = StringUtils.removeEnd(projectString, "/");
            projectString = projectString + "项目";
        }
        return "适用" + projectString;
    }

    private CouponBean createCouponBean(UserCouponConfigCustomize userCouponConfigCustomize, String remarks) {
        CouponBean couponBean = new CouponBean();
        couponBean.setUserCouponId(userCouponConfigCustomize.getUserCouponId());
        couponBean.setRemarks(remarks);
        if (userCouponConfigCustomize.getCouponType() == 1) {
            couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota() + "");
            couponBean.setCouponType("体验金");
        } else if (userCouponConfigCustomize.getCouponType() == 2) {
            couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota() + "%");
            couponBean.setCouponType("加息券");
        } else if (userCouponConfigCustomize.getCouponType() == 3) {
            couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota().intValue() + "");
            couponBean.setCouponType("代金券");
        }
        couponBean.setCouponTypeStr(userCouponConfigCustomize.getCouponType());
        couponBean.setCouponName(userCouponConfigCustomize.getCouponName());
        couponBean.setEndTime(
                userCouponConfigCustomize.getCouponAddTime() + "-" + userCouponConfigCustomize.getEndTime());
        if (userCouponConfigCustomize.getTenderQuotaType() == 0
                || userCouponConfigCustomize.getTenderQuotaType() == null) {
            couponBean.setTenderQuota("出借金额不限");
        } else if (userCouponConfigCustomize.getTenderQuotaType() == 1) {
            couponBean.setTenderQuota(userCouponConfigCustomize.getTenderQuotaMin() + "元~"
                    + userCouponConfigCustomize.getTenderQuotaMax() + "元可用");

        } else if (userCouponConfigCustomize.getTenderQuotaType() == 2) {
            couponBean.setTenderQuota("满" + userCouponConfigCustomize.getTenderQuota() + "元可用");
        }
        return couponBean;
    }

    /**
     * 
     * 根据borrowNid获取Borrow对象
     * @author pcc
     * @param borrowNid
     * @return
     */
    public Borrow getBorrowByNid(String borrowNid) {
        Borrow borrow = null;
        BorrowExample example = new BorrowExample();
        BorrowExample.Criteria criteria = example.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        List<Borrow> list = borrowMapper.selectByExample(example);
        if (list != null && !list.isEmpty()) {
            borrow = list.get(0);
        }
        return borrow;
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

    public List<BorrowProjectType> getProjectTypeList() {
        BorrowProjectTypeExample example = new BorrowProjectTypeExample();
        BorrowProjectTypeExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
        return this.borrowProjectTypeMapper.selectByExample(example);
    }
    
    
    
  //pcc20160715
    @Override
    public String validateCouponProjectType(String projectTypeList, String projectTypeCd) {
    	BorrowProjectType borrowProjectType=new BorrowProjectType();
        BorrowProjectTypeExample example=new BorrowProjectTypeExample();
        example.createCriteria().andBorrowCdEqualTo(projectTypeCd);
        List<BorrowProjectType> list=borrowProjectTypeMapper.selectByExample(example);
        borrowProjectType=list.get(0);
        boolean ifprojectType=true;
        if(projectTypeList.indexOf("-1")!=-1){
        	if(!"RTB".equals(borrowProjectType.getBorrowClass())){
        		ifprojectType = false;
        	}
        }else{
            
            if("HXF".equals(borrowProjectType.getBorrowClass())){
                if(projectTypeList.indexOf("2")!=-1){
                    ifprojectType=false; 
                }
            } else if("NEW".equals(borrowProjectType.getBorrowClass())){
                if(projectTypeList.indexOf("3")!=-1){
                    ifprojectType=false; 
                }
            } else if("ZXH".equals(borrowProjectType.getBorrowClass())){
                if(projectTypeList.indexOf("4")!=-1){
                    ifprojectType=false; 
                }
            } else {
            	if(projectTypeList.indexOf("1")!=-1){
            		if(!"RTB".equals(borrowProjectType.getBorrowClass())){
                		ifprojectType=false; 
                	}
                }
            }
        }
        if(!ifprojectType){
            return null;
        }
        return "对不起，您选择的优惠券不能用于当前类别标的";
    }
    
    

    @Override
    public CouponConfigCustomizeV2 getCouponUser(String couponGrantId, int userId) {
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("couponGrantId", couponGrantId);
        paramMap.put("userId", userId);
        CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
        return ccTemp;
    }
    
    
    


    /**
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
    public boolean updateCouponTender(String couponGrantId, String borrowNid, String ordDate, int userId,
        String account, String ip, int client, int couponOldTime, String mainTenderNid, Map<String, Object> retMap) throws Exception {
		LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借开始。。。。。。");
		System.out.println("优惠券出借开始。。。。。。券编号：" + couponGrantId);
		Borrow borrow = this.getBorrowByNid(borrowNid);
		String methodName = "updateCouponTender";
		int nowTime = GetDate.getNowTime10();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("couponGrantId", couponGrantId);
		paramMap.put("userId", userId);
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
			BigDecimal borrowApr = null;
			if (couponType == 1) {
				// 体验金 出借资金=体验金面值
				accountDecimal = couponQuota;
				borrowApr = borrow.getBorrowApr();
			} else if (couponType == 2) {
				// 加息券 出借资金=真实出借资金
				accountDecimal = new BigDecimal(account);
				borrowApr = couponQuota;
			} else if (couponType == 3) {
				// 代金券 出借资金=体验金面值
				accountDecimal = couponQuota;
				borrowApr = borrow.getBorrowApr();
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
			borrowTenderCpn.setApiStatus(0);//
			borrowTenderCpn.setAutoStatus(0);//
			borrowTenderCpn.setBorrowNid(borrowNid);
			borrowTenderCpn.setChangePeriod(0);//
			borrowTenderCpn.setChangeUserid(0);
			borrowTenderCpn.setClient(0);
			borrowTenderCpn.setContents("");//
			borrowTenderCpn.setFlag(0);//
			borrowTenderCpn.setIsok(0);
			borrowTenderCpn.setIsReport(0);
			borrowTenderCpn.setChangeStatus(0);
			borrowTenderCpn.setLoanAmount(new BigDecimal("0.00"));
			borrowTenderCpn.setNid(tenderNid);
			borrowTenderCpn.setOrderDate(ordDate);
			borrowTenderCpn.setPeriodStatus(0);//

			// 预期本息收益
			borrowTenderCpn.setRecoverAccountAll(new BigDecimal(0));
			// 预期利息
			borrowTenderCpn.setRecoverAccountInterest(new BigDecimal(0));
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
                couponInterest =getInterestDj(accountDecimal,ccTemp.getCouponProfitTime(),borrowApr);
            }else{
                couponInterest =this.calculateCouponInterest(borrow.getBorrowStyle(), accountDecimal, borrowApr,
                        borrow.getBorrowPeriod());
            }
			retMap.put("couponInterest", couponInterest);
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
			// 待收本息
			borrowTenderCpn.setRecoverAccountWait(recoverAccountWait);
			// 待收本金
			borrowTenderCpn.setRecoverAccountCapitalWait(recoverAccountCapitalWait);
			// 待收利息
			borrowTenderCpn.setRecoverAccountInterestWait(recoverAccountInterestWait);

			borrowTenderCpn.setRecoverAdvanceFee(new BigDecimal(0));
			borrowTenderCpn.setRecoverFee(new BigDecimal(0));
			borrowTenderCpn.setRecoverFullStatus(0);
			borrowTenderCpn.setRecoverLateFee(new BigDecimal(0));
			borrowTenderCpn.setRecoverTimes(0);
			borrowTenderCpn.setRecoverType("");
			borrowTenderCpn.setStatus(0);
			borrowTenderCpn.setTenderAwardAccount(new BigDecimal(0));
			borrowTenderCpn.setTenderAwardFee(new BigDecimal(0));
			borrowTenderCpn.setTenderNid(borrowNid);
			borrowTenderCpn.setTenderStatus(0);
			borrowTenderCpn.setUserId(userId);
			borrowTenderCpn.setRemark("");
			borrowTenderCpn.setWeb(0);
			borrowTenderCpn.setWebStatus(0);
			borrowTenderCpn.setClient(client);
			// 出借类别：1：直投类，2：汇添金
			borrowTenderCpn.setTenderType(1);
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
							System.out.println("*******CouponUser表，更新使用状态！券编号：" + couponGrantId);
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

    private BigDecimal getInterestDj(BigDecimal couponQuota, Integer couponProfitTime, BigDecimal borrowApr){
        BigDecimal earnings = new BigDecimal("0");
        
        earnings =couponQuota.multiply(borrowApr.divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_UP))
        .divide(new BigDecimal(365), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(couponProfitTime))
        .setScale(2, BigDecimal.ROUND_DOWN);

        return earnings;
        
    }
    @Override
    public String getUserCouponAvailableCount(String borrowNid, Integer userId, String money, String platform) {
//        System.out.println("~~~~~~~~~~~~~~~~getUserCouponAvailableCount~~~~~~~~~~~~~~~~~~");
        //Integer userId = 20000098;
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("userId", userId);
        // 查询项目信息
        Borrow borrow = this.getBorrowByNid(borrowNid);
        BorrowProjectType borrowProjectType=getProjectTypeByBorrowNid(borrowNid);
        int couponFlg = borrow.getBorrowInterestCoupon();
        int moneyFlg=borrow.getBorrowTasteMoney();
       /* int couponType = 0;
        if(couponFlg==1&&moneyFlg!=1){
            couponType = 2;
        }else if(couponFlg!=1&&moneyFlg==1){
            couponType = 1;
        }
        map.put("couponType", couponType);*/
        List<UserCouponConfigCustomize> couponConfigs=couponConfigCustomizeMapper.getCouponConfigList(map);
        
        List<CouponBean> availableCouponList=new ArrayList<CouponBean>();
        
        String style=borrow.getBorrowStyle();
        
        if(money==null||"".equals(money)||money.length()==0){
            money="0";
        }
        for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
            
          //验证项目加息券或体验金是否可用
            if(couponFlg==0){
                if(userCouponConfigCustomize.getCouponType()==2){
                    continue;
                }
            }
            if(moneyFlg==0){
                if(userCouponConfigCustomize.getCouponType()==1){
                    continue;
                }
            }
            //验证项目金额
            Integer tenderQuota=userCouponConfigCustomize.getTenderQuotaType();
           
            
            if(tenderQuota==1){
                if(userCouponConfigCustomize.getTenderQuotaMin()> new Double(money)||userCouponConfigCustomize.getTenderQuotaMax()<new Double(money)){
                    continue;
                }
            }else if(tenderQuota==2){
                if(userCouponConfigCustomize.getTenderQuota()> new Double(money)){
                    continue;
                }
            }
            //验证项目期限
            Integer type=userCouponConfigCustomize.getProjectExpirationType();
            if("endday".equals(style)){
                if(type==1){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)!= borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==3){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)> borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==4){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)< borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==2){
                    if((userCouponConfigCustomize.getProjectExpirationLengthMin()*30)> borrow.getBorrowPeriod()||
                            (userCouponConfigCustomize.getProjectExpirationLengthMax()*30)< borrow.getBorrowPeriod()){
                        continue;
                    }
                }
            }else{
                if(type==1){
                    if(userCouponConfigCustomize.getProjectExpirationLength()!= borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==3){
                    if(userCouponConfigCustomize.getProjectExpirationLength()> borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==4){
                    if(userCouponConfigCustomize.getProjectExpirationLength()< borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==2){
                    if(userCouponConfigCustomize.getProjectExpirationLengthMin()> borrow.getBorrowPeriod()||userCouponConfigCustomize.getProjectExpirationLengthMax()< borrow.getBorrowPeriod()){
                        continue;
                    }
                } 
                
            }
            //验证可使用项目
            String projectType=userCouponConfigCustomize.getProjectType();
            //String[] projectTypeArr=projectType.split(",");
            boolean ifprojectType=true;
            
            if(projectType.indexOf("-1")!=-1){
            	if(!"RTB".equals(borrowProjectType.getBorrowClass())){
            		ifprojectType = false;
            	}
            }else{
                if("HXF".equals(borrowProjectType.getBorrowClass())){
                    if(projectType.indexOf("2")!=-1){
                        ifprojectType=false; 
                    }
                } else if("NEW".equals(borrowProjectType.getBorrowClass())){
                    if(projectType.indexOf("3")!=-1){
                        ifprojectType=false; 
                    }
                } else if("ZXH".equals(borrowProjectType.getBorrowClass())){
                    if(projectType.indexOf("4")!=-1){
                        ifprojectType=false; 
                    }
                } else {
                	if(projectType.indexOf("1")!=-1){
                		if(!"RTB".equals(borrowProjectType.getBorrowClass())){
                    		ifprojectType=false; 
                    	}
                    }
                }
            }
            if(ifprojectType){
                continue;
            }
            
            //验证使用平台
            String couponSystem=userCouponConfigCustomize.getCouponSystem();
            String[] couponSystemArr=couponSystem.split(",");
            boolean ifcouponSystem=true;
            for (String couponSystemString : couponSystemArr) {
                if("-1".equals(couponSystemString)){
                    ifcouponSystem=false;
                    break;
                }
                if(platform.equals(couponSystemString)){
                    ifcouponSystem=false;
                    break;
                }
            }
          if(ifcouponSystem){
              continue;
          }else{
              CouponBean couponBean=createCouponBean(userCouponConfigCustomize,null);
              availableCouponList.add(couponBean);
          }
        }
//        System.out.println("~~~~~~~~~~~~~~~~统计结束~~~~~~~~~~~~~~~~~~"+availableCouponList.size());
        return availableCouponList.size()+"";
    }
    
    
    
    /**
     * 根据borrowNid获取借款项目
     * 
     * @param borrowNid
     * @return
     */
    @Override
    public AppProjectDetailCustomize selectProjectDetail(String borrowNid) {
        AppProjectDetailCustomize borrow = appProjectListCustomizeMapper.selectProjectDetail(borrowNid);
        return borrow;
    }
    
    
    @Override
    public UserCouponConfigCustomize getBestCoupon(String borrowNid, Integer userId, String money,String platform) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("userId", userId);
        // 查询项目信息
        Borrow borrow = this.getBorrowByNid(borrowNid);
        BorrowProjectType borrowProjectType=getProjectTypeByBorrowNid(borrowNid);
        String style=borrow.getBorrowStyle();
        //加息券是否启用  0禁用  1启用
        Integer couponFlg = borrow.getBorrowInterestCoupon();
        //体验金是否启用  0禁用  1启用
        Integer moneyFlg=borrow.getBorrowTasteMoney();
        /*int couponType = 0;
        if(couponFlg==1&&moneyFlg!=1){
            couponType = 2;
        }else if(couponFlg!=1&&moneyFlg==1){
            couponType = 1;
        }
        map.put("couponType", couponType);*/
        List<UserCouponConfigCustomize> couponConfigs=couponConfigCustomizeMapper.getCouponConfigList(map);
        Collections.sort(couponConfigs, new ComparatorUserCouponConfigBean());
//        List<UserCouponConfigCustomize> availableCouponConfigs=new ArrayList<UserCouponConfigCustomize>();
        for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
            
            
            //验证项目加息券或体验金是否可用
            if(couponFlg!=null&&couponFlg==0){
                if(userCouponConfigCustomize.getCouponType()==2){
                    continue;
                }
            }
            if(moneyFlg!=null&&moneyFlg==0){
                if(userCouponConfigCustomize.getCouponType()==1){
                    continue;
                }
            }
            //验证项目期限、
            Integer type=userCouponConfigCustomize.getProjectExpirationType();
            if("endday".equals(style)){
                if(type==1){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)!= borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==3){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)> borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==4){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)< borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==2){
                    if((userCouponConfigCustomize.getProjectExpirationLengthMin()*30)> borrow.getBorrowPeriod()||
                            (userCouponConfigCustomize.getProjectExpirationLengthMax()*30)< borrow.getBorrowPeriod()){
                        continue;
                    }
                } 
            }else{
                if(type==1){
                    if(userCouponConfigCustomize.getProjectExpirationLength()!= borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==3){
                    if(userCouponConfigCustomize.getProjectExpirationLength()> borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==4){
                    if(userCouponConfigCustomize.getProjectExpirationLength()< borrow.getBorrowPeriod()){
                        continue;
                    }
                }else if(type==2){
                    if(userCouponConfigCustomize.getProjectExpirationLengthMin()> borrow.getBorrowPeriod()||userCouponConfigCustomize.getProjectExpirationLengthMax()< borrow.getBorrowPeriod()){
                        continue;
                    }
                }
            }
            
            //验证项目金额
            Integer tenderQuota=userCouponConfigCustomize.getTenderQuotaType();
            if(tenderQuota==1){
                if(userCouponConfigCustomize.getTenderQuotaMin()> new Double(money)||userCouponConfigCustomize.getTenderQuotaMax()< new Double(money)){
                    continue;
                }
            }else if(tenderQuota==2){
                if(userCouponConfigCustomize.getTenderQuota()> new Double(money)){
                    continue;
                }
            }
            //验证优惠券适用的项目
            String projectType=userCouponConfigCustomize.getProjectType();
            boolean ifprojectType=true;
            if(projectType.indexOf("-1")!=-1){
            	if(!"RTB".equals(borrowProjectType.getBorrowClass())){
            		ifprojectType = false;
            	}
            }else{
                if("HXF".equals(borrowProjectType.getBorrowClass())){
                    if(projectType.indexOf("2")!=-1){
                        ifprojectType=false; 
                    }
                } else if("NEW".equals(borrowProjectType.getBorrowClass())){
                    if(projectType.indexOf("3")!=-1){
                        ifprojectType=false; 
                    }
                } else if("ZXH".equals(borrowProjectType.getBorrowClass())){
                    if(projectType.indexOf("4")!=-1){
                        ifprojectType=false; 
                    }
                } else {
                	if(projectType.indexOf("1")!=-1){
                		if(!"RTB".equals(borrowProjectType.getBorrowClass())){
                    		ifprojectType=false; 
                    	}
                    }
                }
            }
            if(ifprojectType){
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
            
            //验证使用平台
            String couponSystem=userCouponConfigCustomize.getCouponSystem();
            String[] couponSystemArr=couponSystem.split(",");
            for (String couponSystemString : couponSystemArr) {
                if("-1".equals(couponSystemString)){
                    return userCouponConfigCustomize;
                }
                if(platform.equals(couponSystemString)){
                    return userCouponConfigCustomize;
                }
            }
        }
        return null;
    }
    @Override
    public UserCouponConfigCustomize getBestCouponById(String couponId) {
        UserCouponConfigCustomize couponConfig=couponConfigCustomizeMapper.getBestCouponById(couponId);
        return couponConfig;
    }
    
    class ComparatorCouponBean implements Comparator<CouponBean> {
        @Override
        public int compare(CouponBean couponBean1, CouponBean couponBean2) {
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


    /**
     * 检查参数的正确性
     * 
     * @param userId
     * @param transAmt
     *            交易金额
     * @param flag
     *            交易类型，1购买 2赎回
     * @return
     */
    public JSONObject checkParam(String borrowNid, String account, String userId,String platform, CouponConfigCustomizeV2 cuc) {
        if(account==null||"".equals(account)){
            account="0"; 
        }
        // 判断用户userId是否存在
        if (StringUtils.isEmpty(userId) || !DigitalUtils.isInteger(userId)) {
            return jsonMessage("您未登陆，请先登录", "1");
        } else {
            Users user = this.getUsers(Integer.parseInt(userId));
            // 判断用户信息是否存在
            if (user == null) {
                return jsonMessage("用户信息不存在", "1");
            } else {
                // 判断用户是否禁用
                if (user.getStatus() == 1) {// 0启用，1禁用
                    return jsonMessage("该用户已被禁用", "1");
                } else {// 用户存在且用户未被禁用
                    // 检查用户角色是否能出借  合规接口改造之后需要判断
                    UsersInfo userInfo = getUsersInfoByUserId(Integer.parseInt(userId));
                    if (null != userInfo) {
                        String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
                        if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
                            if (userInfo.getRoleId() != 1) {// 非出借用户
                                return jsonMessage("仅限出借人进行出借", "1");
                            }
                        }

//                        if (userInfo.getRoleId() == 2) {// 借款人不能出借
//                            return jsonMessage("借款人用户不能进行出借", "1");
//                        }
                    } else {
                        return jsonMessage("账户信息异常", "1");
                    }
                    Borrow borrow = this.getBorrowByNid(borrowNid);
                    Integer projectType = borrow
                            .getProjectType();// 0，51老用户；1，新用户；2，全部用户
                    if (projectType != null) {
                        BorrowProjectType borrowProjectType = this
                                .getBorrowProjectType(
                                        String.valueOf(
                                                projectType));
                        if (borrowProjectType != null) {
                            // 51老用户标
                            if (borrowProjectType
                                    .getInvestUserType()
                                    .equals("0")) {
                                boolean is51User = this
                                        .checkIs51UserCanInvest(
                                                Integer.parseInt(
                                                        userId));
                                if (!is51User) {
                                    return jsonMessage(
                                            "该项目只能51老用户出借",
                                            "1");
                                }
                            }
                            if (borrowProjectType
                                    .getInvestUserType()
                                    .equals("1")) {
                                boolean newUser = this
                                        .checkIsNewUserCanInvest(
                                                Integer.parseInt(
                                                        userId),
                                                projectType);
                                if (!newUser) {
                                    return jsonMessage(
                                            "该项目只能新手出借",
                                            "1");
                                }
                            }
                        } else {
                            return jsonMessage(
                                    "未查询到该出借项目的设置信息",
                                    "1");
                        }
                    } else {
                        return jsonMessage(
                                "未设置该出借项目的项目类型",
                                "1");
                    }
                    AccountChinapnr accountChinapnrTender = this.getAccountChinapnr(Integer.parseInt(userId));
                    // 用户未在平台开户
                    if (accountChinapnrTender == null) {
                        return jsonMessage("用户开户信息不存在", "1");
                    } else {
                        // 判断借款人开户信息是否存在
                        if (accountChinapnrTender.getChinapnrUsrcustid() == null) {
                            return jsonMessage("用户汇付客户号不存在", "1");
                        } else {
                            // 判断借款编号是否存在
                            if (StringUtils.isEmpty(borrowNid)) {
                                return jsonMessage("借款项目不存在", "1");
                            } else {
                            
                                // 判断借款信息是否存在
                                if (borrow == null || borrow.getId() == null) {
                                    return jsonMessage("借款项目不存在", "1");
                                } else if (borrow.getUserId() == null) {
                                    return jsonMessage("借款人不存在", "1");
                                } else if (platform.equals("2")&&borrow.getCanTransactionAndroid().equals("0")) {
                                    String tmpInfo="";
                                    if (borrow.getCanTransactionPc().equals("1")) {
                                        tmpInfo+=" PC端  ";
                                    }
                                    if (borrow.getCanTransactionIos().equals("1")) {
                                        tmpInfo+=" Ios端  ";
                                    }
                                    if (borrow.getCanTransactionWei().equals("1")) {
                                        tmpInfo+=" 微信端  ";
                                    }
                                    return jsonMessage("此项目只能在"+tmpInfo+"出借", "1");
                                }else if (platform.equals("3")&&borrow.getCanTransactionIos().equals("0")) {
                                    String tmpInfo="";
                                    if (borrow.getCanTransactionPc().equals("1")) {
                                        tmpInfo+=" PC端  ";
                                    }
                                    if (borrow.getCanTransactionAndroid().equals("1")) {
                                        tmpInfo+=" Android端  ";
                                    }
                                    if (borrow.getCanTransactionWei().equals("1")) {
                                        tmpInfo+=" 微信端  ";
                                    }
                                    return jsonMessage("此项目只能在"+tmpInfo+"出借", "1");
                                }else {
                                    AccountChinapnr accountChinapnrBorrower = this
                                            .getAccountChinapnr(borrow.getUserId());
                                    if (accountChinapnrBorrower == null) {
                                        return jsonMessage("借款人未开户", "1");
                                    } else {
                                        if (accountChinapnrBorrower.getChinapnrUsrcustid() == null) {
                                            return jsonMessage("借款人汇付客户号不存在", "1");
                                        } else {
                                            if (userId.equals(String.valueOf(borrow.getUserId()))) {
                                                return jsonMessage("借款人不可以自己出借项目", "1");
                                            } else {
                                                // 判断借款是否流标
                                                if (borrow.getStatus() == 2) { // 流标
                                                    return jsonMessage("此项目已经流标", "1");
                                                } else {
                                                    // 已满标
                                                    if (borrow.getBorrowFullStatus() == 1) {
                                                        return jsonMessage("此项目已经满标", "1");
                                                    } else {
                                                        // 判断用户出借金额是否为空
                                                        if (!(StringUtils.isNotEmpty(account)||(StringUtils.isEmpty(account)&&cuc!=null&&cuc.getCouponType()==3))) {
                                                            return jsonMessage("请输入出借金额", "1");
                                                        } else {
                                                            // 还款金额是否数值
                                                            if (!DigitalUtils.isNumber(account)) {
                                                                return jsonMessage("出借金额格式错误", "1");
                                                            } else {
                                                                if (!(!"0".equals(account)||("0".equals(account)&&cuc!=null&&cuc.getCouponType()==3))) {
                                                                    return jsonMessage("出借金额不能为0元", "1");
                                                                } else {
                                                                    try {
                                                                        // 出借金额必须是整数
                                                                        int accountInt = Integer.parseInt(account);
                                                                        if(accountInt!=0&&cuc != null && cuc.getCouponType() == 1&&cuc.getAddFlg()==1){
                                                                            return jsonMessage("该优惠券只能单独使用", "1");
                                                                        }
                                                                        if (accountInt < 0) {
                                                                            return jsonMessage("出借金额不能为负数", "1");
                                                                        } else {
                                                                            // 将出借金额转化为BigDecimal
                                                                            BigDecimal accountBigDecimal = new BigDecimal(
                                                                                    account);
                                                                            String balance = RedisUtils
                                                                                    .get(borrowNid);
                                                                            if (StringUtils.isEmpty(balance)) {
                                                                                return jsonMessage("您来晚了，下次再来抢吧",
                                                                                        "1");
                                                                            } else {
                                                                            // 剩余可投金额
                                                                            Integer min = borrow
                                                                                    .getTenderAccountMin();
                                                                            // 当剩余可投金额小于最低起投金额，不做最低起投金额的限制

                                                                                if (min != null && min != 0
                                                                                        && new BigDecimal(
                                                                                                balance).compareTo(
                                                                                                        new BigDecimal(
                                                                                                                min)) == -1) {
                                                                                    if (accountBigDecimal
                                                                                            .compareTo(
                                                                                                    new BigDecimal(
                                                                                                            balance)) == 1) {
                                                                                        return jsonMessage(
                                                                                                "项目太抢手了！剩余可投金额只有"
                                                                                                        + balance
                                                                                                        + "元",
                                                                                                "1");
                                                                                        
                                                                                    }
                                                                                    if (accountBigDecimal.compareTo(new BigDecimal(balance)) != 0) {
                                                                                        return jsonMessage("剩余可投只剩"+ balance+ "元，须全部购买","1");
                                                                                    }
                                                                                } else {// 项目的剩余金额大于最低起投金额
                                                                                    if (accountBigDecimal
                                                                                            .compareTo(
                                                                                                    new BigDecimal(
                                                                                                            min)) == -1) {
                                                                                        if(accountBigDecimal.compareTo(BigDecimal.ZERO) == 0){
                                                                                            if(cuc!=null&&cuc.getCouponType()!=3){
                                                                                                return jsonMessage(borrow.getTenderAccountMin()+ "元起投","1");  
                                                                                            }
                                                                                        }else{
                                                                                            return jsonMessage(borrow.getTenderAccountMin()+ "元起投","1");
                                                                                        }
                                                                                        
                                                                                    } else {
                                                                                        Integer max = borrow
                                                                                                .getTenderAccountMax();
                                                                                        if (max != null
                                                                                                && max != 0
                                                                                                && accountBigDecimal
                                                                                                        .compareTo(
                                                                                                                new BigDecimal(
                                                                                                                        max)) == 1) {
                                                                                            return jsonMessage(
                                                                                                    "项目最大出借额为"
                                                                                                            + max
                                                                                                            + "元",
                                                                                                    "1");
                                                                                        }
                                                                                    }
                                                                                }
                                                                            if (accountBigDecimal.compareTo(
                                                                                    borrow.getAccount()) > 0) {
                                                                                return jsonMessage("出借金额不能大于项目总额", "1");
                                                                            } else {
                                                                                
                                                                                
                                                                                    if (StringUtils
                                                                                            .isNotEmpty(balance)) {
                                                                                        // redis剩余金额不足
                                                                                        if (accountBigDecimal.compareTo(
                                                                                                new BigDecimal(
                                                                                                        balance)) == 1) {
                                                                                            return jsonMessage("剩余可投金额为"
                                                                                                    + balance + "元",
                                                                                                    "1");
                                                                                        } else {
                                                                                            
                                                                                                    AppProjectDetailCustomize borrowDetail = appProjectListCustomizeMapper.selectProjectDetail(borrowNid);
                                                                                                    if (borrowDetail.getIncreaseMoney()!=null&&
                                                                                                            (accountInt-min)%Integer.parseInt(borrowDetail.getIncreaseMoney()) != 0
                                                                                                            &&accountBigDecimal.compareTo(new BigDecimal(balance))==-1) {
                                                                                                        return jsonMessage("出借递增金额须为"+borrowDetail.getIncreaseMoney()+" 元的整数倍", "1");
                                                                                                    }
                                                                                                    // 如果验证没问题，则返回出借人借款人的汇付账号
                                                                                                    Long borrowerUsrcustid = accountChinapnrBorrower
                                                                                                            .getChinapnrUsrcustid();
                                                                                                    Long tenderUsrcustid = accountChinapnrTender
                                                                                                            .getChinapnrUsrcustid();
                                                                                                    JSONObject jsonMessage = new JSONObject();
                                                                                                    jsonMessage.put(
                                                                                                            CustomConstants.APP_STATUS,
                                                                                                            "0");
                                                                                                    jsonMessage.put(
                                                                                                            "borrowerUsrcustid",
                                                                                                            borrowerUsrcustid);
                                                                                                    jsonMessage.put(
                                                                                                            "tenderUsrcustid",
                                                                                                            tenderUsrcustid);
                                                                                                    jsonMessage.put(
                                                                                                            "borrowId",
                                                                                                            borrow.getId());
                                                                                                    jsonMessage.put("bankInputFlag",borrow.getBankInputFlag()+"");
                                                                                                    return jsonMessage;
                                                                                                
                                                                                        }
                                                                                    } else {
                                                                                        return jsonMessage(
                                                                                                "您来晚了，下次再来抢吧", "1");
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    } catch (Exception e) {
                                                                        return jsonMessage("出借金额必须为整数", "1");
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 组成返回信息
     * 
     * @param message
     * @param status
     * @return
     */
    public JSONObject jsonMessage(String data, String error) {
        JSONObject jo = null;
        if (Validator.isNotNull(data)) {
            jo = new JSONObject();
            jo.put("error", error);
            jo.put("data", data);
        }
        return jo;
    }
    

    public BorrowProjectType getBorrowProjectType(String projectType) {
        if (StringUtils.isEmpty(projectType)) {
            return null;
        }
        // 查找用户
        BorrowProjectTypeExample borrowProjectTypeExample = new BorrowProjectTypeExample();
        BorrowProjectTypeExample.Criteria criteria2 = borrowProjectTypeExample.createCriteria();
        criteria2.andBorrowCdEqualTo(projectType);
        List<BorrowProjectType> list = borrowProjectTypeMapper.selectByExample(borrowProjectTypeExample);
        BorrowProjectType borrowProjectType = null;
        if (list != null && !list.isEmpty()) {
            borrowProjectType = list.get(0);

        }
        return borrowProjectType;
    }
    /**
     * 判断是否51老用户,如果是则返回true，否则返回false
     *
     * @param userId
     * @param projectType
     * @return
     * @author b
     */
    public boolean checkIs51UserCanInvest(Integer userId) {
        UsersInfoExample usersInfoExample = new UsersInfoExample();
        UsersInfoExample.Criteria borrowCriteria = usersInfoExample.createCriteria();
        borrowCriteria.andUserIdEqualTo(userId);
        List<UsersInfo> list = usersInfoMapper.selectByExample(usersInfoExample);
        if (list != null && !list.isEmpty()) {
            UsersInfo usersInfo = list.get(0);
            if (usersInfo != null) {
                Integer is51 = usersInfo.getIs51();// 1是51，0不是
                if (is51 != null && is51 == 1) {
                    return true;
                }
            }
        }
        return false;

    }
    
    /**
     * 新用户新手标验证，标如果是新用户标，查看此用户是否有过出借记录，如果有返回true，提示不能投标了
     *
     * @param userId
     * @param projectType
     * @return
     */
    public boolean checkIsNewUserCanInvest(Integer userId, Integer projectType) {

              //新的判断是否为新用户方法
                int total =webUserInvestListCustomizeMapper.countNewUserTotal(userId+"");
                if (total == 0) {
                    return true;
                }else{
                    return false;
                }

    }
    
}


