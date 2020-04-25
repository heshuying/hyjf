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

package com.hyjf.app.user.coupon;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.coupon.*;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("couponService")
public class CouponServiceImpl extends BaseServiceImpl implements CouponService {

    /**
     * 获取用户的优惠券列表
     * @return
     */
    @Override
    public List<CouponUserForAppCustomize> getCouponUserList(Map<String, Object> paraMap) {
        return couponUserCustomizeMapper.selectCouponUserListForApp(paraMap);
    }
    
    /**
     * 用户的优惠券总数
     * @author hsy
     * @param paraMap
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

    @Override
    public void getProjectAvailableUserCoupon(String platform,String borrowNid, String sign, JSONObject ret, String money) {
        Integer userId = SecretUtil.getUserId(sign);
        //Integer userId = 4540;
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("userId", userId);
        // 查询项目信息
        Borrow borrow = this.getBorrowByNid(borrowNid);
        List<CouponBean> availableCouponList=new ArrayList<CouponBean>();
        List<CouponBean> notAvailableCouponList=new ArrayList<CouponBean>();

        BorrowProjectType borrowProjectType=getProjectTypeByBorrowNid(borrowNid);
        //优惠券
        Integer couponFlg = borrow.getBorrowInterestCoupon();
        //体验金
        Integer moneyFlg=borrow.getBorrowTasteMoney();

        List<UserCouponConfigCustomize> couponConfigs=couponConfigCustomizeMapper.getCouponConfigList(map);
        //操作平台
        List<ParamName> clients=this.getParamNameList("CLIENT");
        
        String style=borrow.getBorrowStyle();
        for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
            /**************逻辑修改 pcc start***************/
            //是否与本金公用
            boolean addFlg = false;
            if(userCouponConfigCustomize.getAddFlg()==1&&!"0".equals(money)){
                addFlg = true;
            }
            if(addFlg){
              //  CouponBean couponBean =
               //         createCouponBean(userCouponConfigCustomize, "不能与本金共用");
             //  notAvailableCouponList.add(couponBean);
                continue;
            }
            /**************逻辑修改 pcc end***************/
            //验证项目加息券或体验金是否可用
            if(couponFlg!=null&&couponFlg==0){
                if(userCouponConfigCustomize.getCouponType()==2){
             //       CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"该项目加息券不能使用");
             //       notAvailableCouponList.add(couponBean);
                    continue;
                }
            }
            if(moneyFlg!=null&&moneyFlg==0){
                if(userCouponConfigCustomize.getCouponType()==1){
              //      CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"该项目优惠券不能使用");
             //       notAvailableCouponList.add(couponBean);
                    continue;
                }
            }
            
            //验证项目金额
            Integer tenderQuota=userCouponConfigCustomize.getTenderQuotaType();
           
            
            if(tenderQuota==1){
                if(userCouponConfigCustomize.getTenderQuotaMin()> new Double(money)||userCouponConfigCustomize.getTenderQuotaMax()<new Double(money)){
                	String remark1 = "";
                	if(isSatisfy(userCouponConfigCustomize.getTenderQuotaMin())){
                		remark1 = formatAmount(userCouponConfigCustomize.getTenderQuotaMin())+"万元可用";
                	}else{
                		remark1 = userCouponConfigCustomize.getTenderQuotaMin()+"元可用";
                	}
                	String remark2 = "";
                	if(isSatisfy(userCouponConfigCustomize.getTenderQuotaMax())){
                		remark2 = formatAmount(userCouponConfigCustomize.getTenderQuotaMax())+"万元可用";
                	}else{
                		remark2 = CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuotaMax().toString())+"元可用";
                	}
                    CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
                    		remark1+remark2);
                    notAvailableCouponList.add(couponBean);
                    continue;
                }
            }else if(tenderQuota==2){
                if(userCouponConfigCustomize.getTenderQuota()> new Double(money)){
                	boolean flag = isSatisfy(userCouponConfigCustomize.getTenderQuota());
                	String remark = "";
                	if(flag){
                		remark = "满"+formatAmount(userCouponConfigCustomize.getTenderQuota())+"万元可用";
                	}else{
                		remark = "满"+CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuota().toString())+"元可用";
                	}
                	CouponBean couponBean=createCouponBean(userCouponConfigCustomize,remark);
                    notAvailableCouponList.add(couponBean);
                    continue;
                }
            }
            
            
            //验证项目期限
            Integer type=userCouponConfigCustomize.getProjectExpirationType();
            if("endday".equals(style)){
                if(type==1){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)!= borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==3){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)> borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≥"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==4){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)< borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≤"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==2){
                    if((userCouponConfigCustomize.getProjectExpirationLengthMin()*30)> borrow.getBorrowPeriod()||
                            (userCouponConfigCustomize.getProjectExpirationLengthMax()*30)< borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
//                                "适用"+userCouponConfigCustomize.getProjectExpirationLengthMin()+"个月~"
//                        +userCouponConfigCustomize.getProjectExpirationLengthMax()
//                        +"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }
            }else{
                if(type==1){
                    if(userCouponConfigCustomize.getProjectExpirationLength()!= borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==3){
                    if(userCouponConfigCustomize.getProjectExpirationLength()> borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≥"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==4){
                    if(userCouponConfigCustomize.getProjectExpirationLength()< borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≤"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==2){
                    if(userCouponConfigCustomize.getProjectExpirationLengthMin()> borrow.getBorrowPeriod()||userCouponConfigCustomize.getProjectExpirationLengthMax()< borrow.getBorrowPeriod()){
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
//                CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
//                        createProjectTypeString(projectType));
//                notAvailableCouponList.add(couponBean);
                continue;
            }
            
            //验证使用平台 优惠券的使用平台0:全部，1：PC，2：微官网，3：Android，4：IOS
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
//                CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
//                        createCouponSystemString(couponSystem,clients));
//                notAvailableCouponList.add(couponBean);
            }else{
                CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"");
                availableCouponList.add(couponBean);
            }
        }
        
//        //排序
//        Collections.sort(availableCouponList, new ComparatorCouponBean());
//        //排序
//        Collections.sort(notAvailableCouponList, new ComparatorCouponBean());
        ret.put("availableCouponList", availableCouponList);
        ret.put("notAvailableCouponList", notAvailableCouponList);
        ret.put("availableCouponListCount", availableCouponList.size());
        ret.put("notAvailableCouponListCount", notAvailableCouponList.size());
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
	private String createCouponSystemString(String couponSystem, List<ParamName> clients) {
	    String clientString = "";

	      //被选中操作平台
	        String clientSed[] = StringUtils.split(couponSystem, ",");
	        for(int i=0 ; i< clientSed.length;i++){
	            if("-1".equals(clientSed[i])){
	                clientString=clientString+"全部平台";
	                break;
	            }else{
	                for (ParamName paramName : clients) {
	                    if(clientSed[i].equals(paramName.getNameCd())){
	                        if(i!=0&&clientString.length()!=0){
	                            clientString=clientString+"、";
	                        }
	                        clientString=clientString+paramName.getName();
	                        
	                    }
	                }
	            }
	        }
        return "适用"+clientString;
    }

    private String createProjectTypeString(String projectType) {
      //被选中项目类型 pcc20160715
        String projectString = "";
        //被选中项目类型
        String projectSed[] = StringUtils.split(projectType, ",");
         if(projectType.indexOf("-1")!=-1){
             projectString="所有散标/新手/";
           }else{
               projectString=projectString+"所有";
               for (String project : projectSed) {
                    if("1".equals(project)){
                        projectString=projectString+"散标/";
                    }  
                    if("2".equals(project)){
                        projectString=projectString+"";
                    } 
                    if("3".equals(project)){
                        projectString=projectString+"新手/";
                    } 
                    if("4".equals(project)){
                        projectString=projectString+"";
                    } 
                    
               }
               projectString = StringUtils.removeEnd(
                       projectString, "/");
               projectString=projectString+"项目";
        }
        return "适用"+projectString;
    }

    private CouponBean createCouponBean(UserCouponConfigCustomize userCouponConfigCustomize, String remarks) {
	    CouponBean couponBean=new CouponBean();
	    couponBean.setUserCouponId(userCouponConfigCustomize.getUserCouponId());
	    couponBean.setRemarks(remarks);
	    if(userCouponConfigCustomize.getCouponType()==1){
	        couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota().intValue()+"元");
	        couponBean.setCouponType("体验金");
	    }else if(userCouponConfigCustomize.getCouponType()==2){
	        couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota()+"%");
	        couponBean.setCouponType("加息券");
	    }else if(userCouponConfigCustomize.getCouponType()==3){
            couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota().intValue()+"元");
            couponBean.setCouponType("代金券");
        }
	    String projectType = userCouponConfigCustomize.getProjectType();
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
        // mod by nxl 智投服务：修改汇计划->智投 start
       /* if (projectType.indexOf("6")!=-1) {
            projectString = projectString + "汇计划,";
        }*/
        if (projectType.indexOf("6")!=-1) {
            projectString = projectString + "智投,";
        }
        // mod by nxl 智投服务：修改汇计划->智投 end
	    
	    couponBean.setProjectType(projectString.substring(1,projectString.length() -1));
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
		    		if(null != userCouponConfigCustomize.getProjectExpirationLength()){
		    			couponBean.setInvestTime("适用于≥" +userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
		    		}else{
		    			couponBean.setInvestTime("");
		    		}
		    		break;  
		    		
		    	case 4: 
		    		if(null != userCouponConfigCustomize.getProjectExpirationLength()){
		    			couponBean.setInvestTime("适用于≤" +userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
		    		}else{
		    			couponBean.setInvestTime("");
		    		}
		    		break;  
  

		    	default: 

		    		 couponBean.setInvestTime("不限");

		    	break; 

		    	}

	    }else{
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
	    couponBean.setTime(userCouponConfigCustomize.getCouponAddTime()+"-"+userCouponConfigCustomize.getEndTime());
	    if(userCouponConfigCustomize.getTenderQuotaType()==0||userCouponConfigCustomize.getTenderQuotaType()==null){
	        couponBean.setInvestQuota("出借金额不限");
	    }else if(userCouponConfigCustomize.getTenderQuotaType()==1){
	        couponBean.setInvestQuota(CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuotaMin().toString())+"元~"+CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuotaMax().toString())+"元可用");
    
        }else if(userCouponConfigCustomize.getTenderQuotaType()==2){
            couponBean.setInvestQuota("满"+ CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuota().toString())+"元可用");
        }
	    
	    if(userCouponConfigCustomize.getTenderQuotaType() == 0){
	    	couponBean.setInvestQuota("金额不限");
	  		}else if (userCouponConfigCustomize.getTenderQuotaType() == 1) {
	  			Integer min = userCouponConfigCustomize.getTenderQuotaMin();
	  			Integer max = userCouponConfigCustomize.getTenderQuotaMax();
	  			String mins = "";
	  			String maxs = "";
	  			if(userCouponConfigCustomize.getTenderQuotaMin() >= 10000 && (userCouponConfigCustomize.getTenderQuotaMin()%10000>=0)){
	  				mins = formatAmount(min) + "万元~";
	  			}else{
	  				mins = CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuotaMin().toString()) + "元~";
	  			}
	  			if(userCouponConfigCustomize.getTenderQuotaMax() >= 10000&& (userCouponConfigCustomize.getTenderQuotaMax()%10000>=0)){
	  				maxs = formatAmount(max) + "万元可用";
	  			}else{
	  				maxs = userCouponConfigCustomize.getTenderQuotaMax() + "元可用";
	  			}
			couponBean.setInvestQuota(mins+maxs);
		}else if (userCouponConfigCustomize.getTenderQuotaType() == 2) {
			String remark = "";
			if(userCouponConfigCustomize.getTenderQuota() >= 10000 && (userCouponConfigCustomize.getTenderQuota()%10000>=0)){
  				remark = "满"+formatAmount(userCouponConfigCustomize.getTenderQuota())+"万元可用";
  			}else{
  				remark = "满"+ CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuota().toString())+"元可用";
  			}
			couponBean.setInvestQuota(remark);
		}else if (userCouponConfigCustomize.getTenderQuotaType() == 3) {
			String remark = "";
			if(userCouponConfigCustomize.getTenderQuota() >= 10000 && (userCouponConfigCustomize.getTenderQuota()%10000>=0)){
  				remark = formatAmount(userCouponConfigCustomize.getTenderQuota())+"万元（含）内可用";
  			}else{
  				remark = CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuota().toString())+"元（含）内可用";
  			}
			couponBean.setInvestQuota(remark);
			
		}else {
			couponBean.setInvestQuota("金额不限");
		}
        return couponBean;
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
        cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL).andBorrowClassEqualTo(borrowNid.substring(0,3));
        List<BorrowProjectType> borrowProjectTypes=this.borrowProjectTypeMapper.selectByExample(example);
        BorrowProjectType borrowProjectType=new BorrowProjectType();
        if(borrowProjectTypes!=null&&borrowProjectTypes.size()>0){
            borrowProjectType=borrowProjectTypes.get(0);
        }
        return borrowProjectType;
    }
    
    public List<BorrowProjectType> getProjectTypeList() {
        BorrowProjectTypeExample example = new BorrowProjectTypeExample();
        BorrowProjectTypeExample.Criteria cra = example.createCriteria();
        cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
        return this.borrowProjectTypeMapper.selectByExample(example);
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
    
    @Override
    public int updateCouponReadFlag(Integer userId, Integer readFlag){
        CouponUser couponUser = new CouponUser();
        couponUser.setReadFlag(readFlag);
        
        CouponUserExample example = new CouponUserExample();
        CouponUserExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId).andUsedFlagEqualTo(CustomConstants.USER_COUPON_STATUS_UNUSED).andDelFlagEqualTo(Integer.parseInt(CustomConstants.FLAG_NORMAL));
        
        return couponUserMapper.updateByExampleSelective(couponUser, example);
    }
    
    @Override
    public String getUserCouponsData(String couponStatus, Integer page, Integer pageSize, Integer userId, String host){
        String SOA_INTERFACE_KEY = PropUtils.getSystem("aop.interface.accesskey");
        String GET_USERCOUPONS = "coupon/getUserCoupons.json";
        
        String timestamp = String.valueOf(GetDate.getNowTime10());
        String chkValue = StringUtils.lowerCase(MD5.toMD5Code(SOA_INTERFACE_KEY + userId + couponStatus + page + pageSize + timestamp + SOA_INTERFACE_KEY));
        
        Map<String, String> params = new HashMap<String, String>();
        // 时间戳
        params.put("timestamp", timestamp);
        // 签名
        params.put("chkValue", chkValue);
        // 用户id
        params.put("userId", String.valueOf(userId));
        // 商品id
        params.put("couponStatus", couponStatus);
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("host", host);
        
        // 请求路径
        String requestUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL) + GET_USERCOUPONS;
        // 0:成功，1：失败
        String date = HttpClientUtils.post(requestUrl, params);
        return date;

    }
    
    /**
     * 
     * 调用接口获取优惠券详情数据
     * @author hsy
     * @param couponId
     * @return
     */
    @Override
    public String getCouponsDetailData(String couponId){
        String SOA_INTERFACE_KEY = PropUtils.getSystem("aop.interface.accesskey");
        String GET_COUPONDETAIL = "coupon/getUserCouponDetail.json";
 
        String timestamp = String.valueOf(GetDate.getNowTime10());
        String chkValue = StringUtils.lowerCase(MD5.toMD5Code(SOA_INTERFACE_KEY + couponId + timestamp + SOA_INTERFACE_KEY));
       
        Map<String, String> params = new HashMap<String, String>();
        // 时间戳
        params.put("timestamp", timestamp);
        // 签名
        params.put("chkValue", chkValue);
        // 优惠券id
        params.put("id", couponId);

        // 请求路径
        String requestUrl = PropUtils.getSystem(PropertiesConstants.HYJF_API_WEB_URL) + GET_COUPONDETAIL;
        
        // 0:成功，1：失败
        return HttpClientUtils.post(requestUrl, params);
        
    }

    @Override
    public void getHJHProjectAvailableUserCoupon(String platform, String planNid, String sign, JSONObject ret,
        String money) {

        Integer userId = SecretUtil.getUserId(sign);
       // userId = 4540;
        JSONObject jsonObject = CommonSoaUtils.getProjectAvailableUserCoupon(platform, planNid, money, userId);
        
        
        ret.put("availableCouponList", JSONArray.parseArray(
                JSONObject.toJSONString(jsonObject.get("availableCouponList")), PlanCouponResultBean.class));
        ret.put("notAvailableCouponList", JSONArray.parseArray(
                JSONObject.toJSONString(jsonObject.get("notAvailableCouponList")), PlanCouponResultBean.class));
        ret.put("availableCouponListCount", jsonObject.get("availableCouponListCount"));
        ret.put("notAvailableCouponListCount", jsonObject.get("notAvailableCouponListCount"));
    }

    @Override
    public int countUserCouponTotal(int userId) {
        // TODO Auto-generated method stub
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        int total = couponUserListCustomizeMapper.countCouponUserList(params);
        return total;
    }

	@Override
	public JSONObject getUserCoupon(String borrowNid, Integer userId,
			String money, String platform) {
		JSONObject jsonObject = new JSONObject();
		Map<String,Object> map=new HashMap<String,Object>();
        map.put("userId", userId);
        // 查询项目信息
        Borrow borrow = this.getBorrowByNid(borrowNid);
        List<CouponBean> availableCouponList=new ArrayList<CouponBean>();
        List<CouponBean> notAvailableCouponList=new ArrayList<CouponBean>();

        BorrowProjectType borrowProjectType=getProjectTypeByBorrowNid(borrowNid);
        //优惠券
        Integer couponFlg = borrow.getBorrowInterestCoupon();
        //体验金
        Integer moneyFlg=borrow.getBorrowTasteMoney();

        List<UserCouponConfigCustomize> couponConfigs=couponConfigCustomizeMapper.getCouponConfigList(map);
        //操作平台
        List<ParamName> clients=this.getParamNameList("CLIENT");
        
        String style=borrow.getBorrowStyle();
        for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
            //验证项目加息券或体验金是否可用
            if(couponFlg!=null&&couponFlg==0){
                if(userCouponConfigCustomize.getCouponType()==2){
                    CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"该项目加息券不能使用");
                    notAvailableCouponList.add(couponBean);
                    continue;
                }
            }
            if(moneyFlg!=null&&moneyFlg==0){
                if(userCouponConfigCustomize.getCouponType()==1){
                    CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"该项目优惠券不能使用");
                    notAvailableCouponList.add(couponBean);
                    continue;
                }
            }
            
            //验证项目金额
            Integer tenderQuota=userCouponConfigCustomize.getTenderQuotaType();
           
            
            if(tenderQuota==1){
                if(userCouponConfigCustomize.getTenderQuotaMin()> new Double(money)||userCouponConfigCustomize.getTenderQuotaMax()<new Double(money)){
                	String remark1 = "";
                	if(isSatisfy(userCouponConfigCustomize.getTenderQuotaMin())){
                		remark1 = formatAmount(userCouponConfigCustomize.getTenderQuotaMin())+"万元可用";

                	}else{
                		remark1 = CommonUtils.formatAmount((userCouponConfigCustomize.getTenderQuotaMin()).toString())+"元可用";
                	}
                	String remark2 = "";
                	if(isSatisfy(userCouponConfigCustomize.getTenderQuotaMax())){
                		remark2 = formatAmount(userCouponConfigCustomize.getTenderQuotaMax())+"万元可用";
                	}else{
                		remark2 = CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuotaMax().toString())+"元可用";
                	}
                    CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
                    		remark1+remark2);
                    notAvailableCouponList.add(couponBean);
                    continue;
                }
            }else if(tenderQuota==2){
                if(userCouponConfigCustomize.getTenderQuota()> new Double(money)){
                	boolean flag = isSatisfy(userCouponConfigCustomize.getTenderQuota());
                	String remark = "";
                	if(flag){
                		remark = "满"+formatAmount(userCouponConfigCustomize.getTenderQuota())+"万元可用";
                	}else{
                		remark = "满"+ CommonUtils.formatAmount(userCouponConfigCustomize.getTenderQuota().toString())+"元可用";
                	}
                	CouponBean couponBean=createCouponBean(userCouponConfigCustomize,remark);
                    notAvailableCouponList.add(couponBean);
                    continue;
                }
            }
            
            
            //验证项目期限
            Integer type=userCouponConfigCustomize.getProjectExpirationType();
            if("endday".equals(style)){
                if(type==1){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)!= borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==3){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)> borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≥"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==4){
                    if((userCouponConfigCustomize.getProjectExpirationLength()*30)< borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≤"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==2){
                    if((userCouponConfigCustomize.getProjectExpirationLengthMin()*30)> borrow.getBorrowPeriod()||
                            (userCouponConfigCustomize.getProjectExpirationLengthMax()*30)< borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
//                                "适用"+userCouponConfigCustomize.getProjectExpirationLengthMin()+"个月~"
//                        +userCouponConfigCustomize.getProjectExpirationLengthMax()
//                        +"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }
            }else{
                if(type==1){
                    if(userCouponConfigCustomize.getProjectExpirationLength()!= borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==3){
                    if(userCouponConfigCustomize.getProjectExpirationLength()> borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≥"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==4){
                    if(userCouponConfigCustomize.getProjectExpirationLength()< borrow.getBorrowPeriod()){
//                        CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"适用≤"+userCouponConfigCustomize.getProjectExpirationLength()+"个月的项目");
//                        notAvailableCouponList.add(couponBean);
                        continue;
                    }
                }else if(type==2){
                    if(userCouponConfigCustomize.getProjectExpirationLengthMin()> borrow.getBorrowPeriod()||userCouponConfigCustomize.getProjectExpirationLengthMax()< borrow.getBorrowPeriod()){
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
//                CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
//                        createProjectTypeString(projectType));
//                notAvailableCouponList.add(couponBean);
                continue;
            }
            
            //验证使用平台 优惠券的使用平台0:全部，1：PC，2：微官网，3：Android，4：IOS
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
//                CouponBean couponBean=createCouponBean(userCouponConfigCustomize,
//                        createCouponSystemString(couponSystem,clients));
//                notAvailableCouponList.add(couponBean);
            }else{
                CouponBean couponBean=createCouponBean(userCouponConfigCustomize,"");
                availableCouponList.add(couponBean);
            }
        }
        
        jsonObject.put("availableCouponList", availableCouponList);
        jsonObject.put("notAvailableCouponList", notAvailableCouponList);
        jsonObject.put("availableCouponListCount", availableCouponList.size());
        jsonObject.put("notAvailableCouponListCount", notAvailableCouponList.size());
		return jsonObject;
	}

	@Override
	public JSONObject getHJHProjectUserCoupon(String planNid, Integer userId,
			String money, String platform) {
		JSONObject json = new JSONObject();
        JSONObject jsonObject = CommonSoaUtils.getProjectAvailableUserCoupon(platform, planNid, money, userId);
        
        
        json.put("availableCouponList", JSONArray.parseArray(
                JSONObject.toJSONString(jsonObject.get("availableCouponList")), PlanCouponResultBean.class));
        json.put("notAvailableCouponList", JSONArray.parseArray(
                JSONObject.toJSONString(jsonObject.get("notAvailableCouponList")), PlanCouponResultBean.class));
        json.put("availableCouponListCount", jsonObject.get("availableCouponListCount"));
        json.put("notAvailableCouponListCount", jsonObject.get("notAvailableCouponListCount"));
		return json;
	}

    
}
