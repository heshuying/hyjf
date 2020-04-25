package com.hyjf.wechat.service.coupon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.coupon.PlanCouponResultBean;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.controller.user.coupon.CouponBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuigq on 2018/2/23.
 */
@Service
public class WxCouponServiceImpl extends BaseServiceImpl implements WxCouponService {

    @Override
    public Map<String, Object> getProjectAvailableUserCoupon(String borrowNid, String money, Integer userId) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        // 查询项目信息
        Borrow borrow = this.getBorrowByNid(borrowNid);
        List<CouponBean> availableCouponList = Lists.newArrayList();
        List<CouponBean> notAvailableCouponList = Lists.newArrayList();

        BorrowProjectType borrowProjectType = getProjectTypeByBorrowNid(borrowNid);
        //优惠券
        Integer couponFlg = borrow.getBorrowInterestCoupon();
        //体验金
        Integer moneyFlg = borrow.getBorrowTasteMoney();

        List<UserCouponConfigCustomize> couponConfigs = couponConfigCustomizeMapper.getCouponConfigList(map);
        //操作平台
        /*List<ParamName> clients = this.getParamNameList("CLIENT");*/

        String style = borrow.getBorrowStyle();
        for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
            /**************逻辑修改 pcc start***************/
            String system = userCouponConfigCustomize.getCouponSystem();
            //判断优惠券使用平台
            if (!StringUtils.contains(system, "-1") && !StringUtils.contains(system, "1")) {//微官网优惠券类型为1
                /*CouponBean couponBean =
                        createCouponBean(userCouponConfigCustomize, "当前平台不能使用");*/
//                notAvailableCouponList.add(couponBean);
                continue;
            }
            //是否与本金公用
            boolean addFlg = false;
            if (userCouponConfigCustomize.getAddFlg() == 1 && !"0".equals(money)) {
                addFlg = true;
            }
            if (addFlg) {
                /*CouponBean couponBean =
                        createCouponBean(userCouponConfigCustomize, "不能与本金共用");*/
//                notAvailableCouponList.add(couponBean);
                continue;
            }
            /**************逻辑修改 pcc end***************/
            //验证项目加息券或体验金是否可用
            if (couponFlg != null && couponFlg == 0) {
                if (userCouponConfigCustomize.getCouponType() == 2) {
                    /*CouponBean couponBean = createCouponBean(userCouponConfigCustomize, "该项目加息券不能使用");*/
//                    notAvailableCouponList.add(couponBean);
                    continue;
                }
            }
            if (moneyFlg != null && moneyFlg == 0) {
                if (userCouponConfigCustomize.getCouponType() == 1) {
                    /*CouponBean couponBean = createCouponBean(userCouponConfigCustomize, "该项目优惠券不能使用");*/
//                    notAvailableCouponList.add(couponBean);
                    continue;
                }
            }

            //验证项目金额
            Integer tenderQuota = userCouponConfigCustomize.getTenderQuotaType();


            //验证项目期限
            Integer type = userCouponConfigCustomize.getProjectExpirationType();
            if ("endday".equals(style)) {
                if (type == 1) {
                    if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) != borrow.getBorrowPeriod()) {
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                } else if (type == 3) {
                    if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) > borrow.getBorrowPeriod()) {
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≥"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                } else if (type == 4) {
                    if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) < borrow.getBorrowPeriod()) {
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≤"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                } else if (type == 2) {
                    if ((userCouponConfigCustomize.getProjectExpirationLengthMin() * 30) > borrow.getBorrowPeriod() ||
                            (userCouponConfigCustomize.getProjectExpirationLengthMax() * 30) < borrow.getBorrowPeriod()) {
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
//                                "适用"+userCouponConfigCustomize.getProjectExpirationLengthMin()+"个月~"
//                        +userCouponConfigCustomize.getProjectExpirationLengthMax()
//                        +"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }
            } else {
                if (type == 1) {
                    if (!userCouponConfigCustomize.getProjectExpirationLength().equals(borrow.getBorrowPeriod())) {
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                } else if (type == 3) {
                    if (userCouponConfigCustomize.getProjectExpirationLength() > borrow.getBorrowPeriod()) {
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≥"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                } else if (type == 4) {
                    if (userCouponConfigCustomize.getProjectExpirationLength() < borrow.getBorrowPeriod()) {
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≤"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                } else if (type == 2) {
                    if (userCouponConfigCustomize.getProjectExpirationLengthMin() > borrow.getBorrowPeriod() || userCouponConfigCustomize.getProjectExpirationLengthMax() < borrow.getBorrowPeriod()) {
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
//                                "适用"+userCouponConfigCustomize.getProjectExpirationLengthMin()+"个月~"
//                        +userCouponConfigCustomize.getProjectExpirationLengthMax()
//                        +"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }

            }


            //验证可使用项目  pcc20160715
            String projectType = userCouponConfigCustomize.getProjectType();
            boolean ifprojectType = true;
            if (projectType.indexOf("-1") != -1) {
                if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
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
                    if (projectType.indexOf("1") != -1) {
                        if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
                            ifprojectType = false;
                        }
                    }
                }
            }
            if (ifprojectType) {
//                CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
//                        createProjectTypeString(projectType));
//                notAvailableCouponList.add(couponBean);
                continue;
            }

            //验证使用平台 优惠券的使用平台0:全部，1：PC，2：微官网，3：Android，4：IOS
            String couponSystem = system;
            String[] couponSystemArr = couponSystem.split(",");
            boolean ifcouponSystem = true;
            for (String couponSystemString : couponSystemArr) {
                if ("-1".equals(couponSystemString)) {
                    ifcouponSystem = false;
                    break;
                }
                if (CustomConstants.CLIENT_WECHAT.equals(couponSystemString)) {
                    ifcouponSystem = false;
                    break;
                }
            }
            
            if (tenderQuota == 1) {
                if (userCouponConfigCustomize.getTenderQuotaMin() > new Double(money) || userCouponConfigCustomize.getTenderQuotaMax() < new Double(money)) {
                    String remark1 = "";
                    if (isSatisfy(userCouponConfigCustomize.getTenderQuotaMin())) {
                        remark1 = userCouponConfigCustomize.getTenderQuotaMin() / 10000 + "万元可用";
                    } else {
                        remark1 = userCouponConfigCustomize.getTenderQuotaMin() + "元可用";
                    }
                    String remark2 = "";
                    if (isSatisfy(userCouponConfigCustomize.getTenderQuotaMax())) {
                        remark2 = userCouponConfigCustomize.getTenderQuotaMax() / 10000 + "万元可用";
                    } else {
                        remark2 = CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuotaMax().toString()) + "元可用";
                    }
                    CouponBean couponBean = createCouponBean(userCouponConfigCustomize,
                            remark1 + remark2);
                    notAvailableCouponList.add(couponBean);
                    continue;
                }
            } else if (tenderQuota == 2) {
                if (userCouponConfigCustomize.getTenderQuota() > new Double(money)) {
                    boolean flag = isSatisfy(userCouponConfigCustomize.getTenderQuota());
                    String remark = "";
                    if (flag) {
                        remark = "满" + userCouponConfigCustomize.getTenderQuota() / 10000 + "万元可用";
                    } else {
                        remark = "满" + CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuota().toString()) + "元可用";
                    }
                    CouponBean couponBean = createCouponBean(userCouponConfigCustomize, remark);
                    notAvailableCouponList.add(couponBean);
                    continue;
                }
            }
            

           if (!ifcouponSystem) {
                CouponBean couponBean = createCouponBean(userCouponConfigCustomize, "");
                availableCouponList.add(couponBean);
            }
        }

        Map<String, Object> ret = Maps.newHashMap();
        ret.put("availableCouponList", availableCouponList);
        ret.put("notAvailableCouponList", notAvailableCouponList);
        ret.put("availableCouponListCount", availableCouponList.size());
        ret.put("notAvailableCouponListCount", notAvailableCouponList.size());
        
        return ret;
    }

    private CouponBean createCouponBean(UserCouponConfigCustomize userCouponConfigCustomize, String remarks) {
    	DecimalFormat df = new DecimalFormat(",###");
		df.setRoundingMode(RoundingMode.FLOOR);
        CouponBean couponBean = new CouponBean();
        couponBean.setUserCouponId(userCouponConfigCustomize.getUserCouponId());
        couponBean.setRemarks(remarks);
        if (userCouponConfigCustomize.getCouponType() == 1) {
            couponBean.setCouponQuota(df.format(userCouponConfigCustomize.getCouponQuota()));
            couponBean.setCouponType("体验金");
        } else if (userCouponConfigCustomize.getCouponType() == 2) {
            couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota()+"");
            couponBean.setCouponType("加息券");
        } else if (userCouponConfigCustomize.getCouponType() == 3) {
            couponBean.setCouponQuota(df.format(userCouponConfigCustomize.getCouponQuota()));
            couponBean.setCouponType("代金券");
        }
        String projectType = userCouponConfigCustomize.getProjectType();
        String projectString = ",";
        //勾选汇直投，尊享汇，融通宝
        if (projectType.indexOf("1") != -1 && projectType.indexOf("4") != -1 && projectType.indexOf("7") != -1) {
            projectString = projectString + "散标,";
        }
        //勾选汇直投  未勾选尊享汇，融通宝
        if (projectType.indexOf("1") != -1 && projectType.indexOf("4") == -1 && projectType.indexOf("7") == -1) {
            projectString = projectString + "散标,";
        }
        //勾选汇直投，融通宝  未勾选尊享汇
        if (projectType.indexOf("1") != -1 && projectType.indexOf("4") == -1 && projectType.indexOf("7") != -1) {
            projectString = projectString + "散标,";
        }
        //勾选汇直投，选尊享汇 未勾选融通宝
        if (projectType.indexOf("1") != -1 && projectType.indexOf("4") != -1 && projectType.indexOf("7") == -1) {
            projectString = projectString + "散标,";
        }
        //勾选尊享汇，融通宝  未勾选直投
        if (projectType.indexOf("1") == -1 && projectType.indexOf("4") != -1 && projectType.indexOf("7") != -1) {
            projectString = projectString + "散标,";
        }
        //勾选尊享汇  未勾选直投，融通宝
        if (projectType.indexOf("1") == -1 && projectType.indexOf("4") != -1 && projectType.indexOf("7") == -1) {
            projectString = projectString + "散标,";
        }
        //勾选尊享汇  未勾选直投，融通宝
        if (projectType.indexOf("1") == -1 && projectType.indexOf("4") == -1 && projectType.indexOf("7") != -1) {
            projectString = projectString + "散标,";
        }

        if (projectType.indexOf("3") != -1) {
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

        couponBean.setProjectType(projectString.substring(1, projectString.length() - 1));
        if (userCouponConfigCustomize.getProjectExpirationType() != null) {
            int i = userCouponConfigCustomize.getProjectExpirationType();
            switch (i) {

                case 0:
                    couponBean.setInvestTime("不限");
                    break;

                case 1:
                    couponBean.setInvestTime(userCouponConfigCustomize.getProjectExpirationLength() + "个月");
                    break;

                case 2:
                    String time = userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月" + "-" + userCouponConfigCustomize.getProjectExpirationLengthMax() + "个月";
                    couponBean.setInvestTime(time);
                    break;

                case 3:
                    if (null != userCouponConfigCustomize.getProjectExpirationLength()) {
                        couponBean.setInvestTime("适用于≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
                    } else {
                        couponBean.setInvestTime("");
                    }
                    break;

                case 4:
                    if (null != userCouponConfigCustomize.getProjectExpirationLength()) {
                        couponBean.setInvestTime("适用于≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
                    } else {
                        couponBean.setInvestTime("");
                    }
                    break;


                default:

                    couponBean.setInvestTime("不限");

                    break;

            }

        } else {
            couponBean.setInvestTime("");
        }
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
        couponBean.setTime(userCouponConfigCustomize.getCouponAddTime() + "-" + userCouponConfigCustomize.getEndTime());
        if (userCouponConfigCustomize.getTenderQuotaType() == 0 || userCouponConfigCustomize.getTenderQuotaType() == null) {
            couponBean.setInvestQuota("出借金额不限");
        } else if (userCouponConfigCustomize.getTenderQuotaType() == 1) {
            couponBean.setInvestQuota(CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuotaMin().toString()) + "元~" + CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuotaMax().toString()) + "元可用");

        } else if (userCouponConfigCustomize.getTenderQuotaType() == 2) {
            couponBean.setInvestQuota("满" + CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuota().toString()) + "元可用");
        }

        if (userCouponConfigCustomize.getTenderQuotaType() == 0) {
            couponBean.setInvestQuota("金额不限");
        } else if (userCouponConfigCustomize.getTenderQuotaType() == 1) {
            Integer min = userCouponConfigCustomize.getTenderQuotaMin();
            Integer max = userCouponConfigCustomize.getTenderQuotaMax();
            String mins = "";
            String maxs = "";
            if (userCouponConfigCustomize.getTenderQuotaMin() > 1000 && (userCouponConfigCustomize.getTenderQuotaMin() % 10000 == 0)) {
                mins = min / 10000 + "万元~";
            } else {
                mins = CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuotaMin().toString()) + "元~";
            }
            if (userCouponConfigCustomize.getTenderQuotaMax() > 1000 && (userCouponConfigCustomize.getTenderQuotaMax() % 10000 == 0)) {
                maxs = max / 10000 + "万元可用";
            } else {
                maxs = userCouponConfigCustomize.getTenderQuotaMax() + "元可用";
            }
            couponBean.setInvestQuota(mins + maxs);
        } else if (userCouponConfigCustomize.getTenderQuotaType() == 2) {
            String remark = "";
            if (userCouponConfigCustomize.getTenderQuota() > 1000 && (userCouponConfigCustomize.getTenderQuota() % 10000 == 0)) {
                remark = "满" + userCouponConfigCustomize.getTenderQuota() / 10000 + "万元可用";
            } else {
                remark = "满" + CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuota().toString()) + "元可用";
            }
            couponBean.setInvestQuota(remark);
        } else if (userCouponConfigCustomize.getTenderQuotaType() == 3) {
            String remark = "";
            if (userCouponConfigCustomize.getTenderQuota() > 1000 && (userCouponConfigCustomize.getTenderQuota() % 10000 == 0)) {
                remark = userCouponConfigCustomize.getTenderQuota() / 10000 + "万元（含）内可用";
            } else {
                remark = CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuota().toString()) + "元（含）内可用";
            }
            couponBean.setInvestQuota(remark);

        } else {
            couponBean.setInvestQuota("金额不限");
        }
        return couponBean;
    }

    /**
     * 根据borrowNid获取项目类型对象
     *
     * @param borrowNid
     * @return
     * @author pcc
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

    /**
     * 判断金额是否大于10000并且是否能被10000整除
     *
     * @param money
     * @return boolean
     */
    private boolean isSatisfy(Integer money) {
        boolean remark = false;
        if (money > 10000) {
            Integer i = money % 10000;
            if (i == 0) {
                remark = true;
            }
        }
        return remark;
    }

    @Override
    public UserCouponConfigCustomize getCouponById(String couponId) {
        UserCouponConfigCustomize couponConfig = couponConfigCustomizeMapper.getBestCouponById(couponId);
        return couponConfig;
    }
    
    /**
     * 汇计划获取可用优惠券列表
     */
    @Override
	public void getHJHProjectAvailableUserCoupon(String platform, String planNid, Integer userId, JSONObject ret,
        String money) {

        JSONObject jsonObject = CommonSoaUtils.getProjectAvailableUserCoupon(platform, planNid, money, userId);
        DecimalFormat df = new DecimalFormat(",###");
		df.setRoundingMode(RoundingMode.FLOOR);
        List<PlanCouponResultBean> availableCouponList=JSONArray.parseArray(
                JSONObject.toJSONString(jsonObject.get("availableCouponList")), PlanCouponResultBean.class);
        List<PlanCouponResultBean> notAvailableCouponList=JSONArray.parseArray(
                JSONObject.toJSONString(jsonObject.get("notAvailableCouponList")), PlanCouponResultBean.class);
        for (PlanCouponResultBean planCouponResultBean : availableCouponList) {
        	if(planCouponResultBean.getCouponQuota().indexOf("元")!=-1||planCouponResultBean.getCouponQuota().indexOf("%")!=-1){
        		String couponQuota=planCouponResultBean.getCouponQuota().substring(0, planCouponResultBean.getCouponQuota().length()-1);
        		planCouponResultBean.setCouponQuota(couponQuota);
        		if(!"加息券".equals(planCouponResultBean.getCouponType())){
        			planCouponResultBean.setCouponQuota(df.format(new BigDecimal(couponQuota)));
        		}
        	}else{
        		if(!"加息券".equals(planCouponResultBean.getCouponType())){
        			planCouponResultBean.setCouponQuota(df.format(new BigDecimal(planCouponResultBean.getCouponQuota())));
        		}
        	}
		}
        for (PlanCouponResultBean planCouponResultBean : notAvailableCouponList) {
        	if(planCouponResultBean.getCouponQuota().indexOf("元")!=-1||planCouponResultBean.getCouponQuota().indexOf("%")!=-1){
        		String couponQuota=planCouponResultBean.getCouponQuota().substring(0, planCouponResultBean.getCouponQuota().length()-1);
        		planCouponResultBean.setCouponQuota(couponQuota);
        		if(!"加息券".equals(planCouponResultBean.getCouponType())){
        			planCouponResultBean.setCouponQuota(df.format(new BigDecimal(couponQuota)));
        		}
        	}else{
        		if(!"加息券".equals(planCouponResultBean.getCouponType())){
        			planCouponResultBean.setCouponQuota(df.format(new BigDecimal(planCouponResultBean.getCouponQuota())));
        		}
        		
        	}
		}
        ret.put("availableCouponList",availableCouponList );
        ret.put("notAvailableCouponList", notAvailableCouponList);
        ret.put("availableCouponListCount", jsonObject.get("availableCouponListCount"));
        ret.put("notAvailableCouponListCount", jsonObject.get("notAvailableCouponListCount"));
    }

public static void main(String[] args) {
    DecimalFormat df = new DecimalFormat(",###");
    df.setRoundingMode(RoundingMode.FLOOR);
    System.out.println(df.format(new BigDecimal("1.5")));
}
}
