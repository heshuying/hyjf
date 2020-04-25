///**
// * Description:用户出借实现类
// * Copyright: Copyright (HYJF Corporation)2015
// * Company: HYJF Corporation
// * @author: 王坤
// * @version: 1.0
// *           Created at: 2015年12月4日 下午1:50:02
// *           Modification History:
// *           Modified by :
// */
//
//package com.hyjf.app.user.reward;
//
//import java.util.ArrayList;
//
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.hyjf.common.util.CustomConstants;
//import com.hyjf.mybatis.model.auto.Borrow;
//import com.hyjf.mybatis.model.auto.BorrowExample;
//import com.hyjf.mybatis.model.auto.BorrowProjectType;
//import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
//import com.hyjf.mybatis.model.auto.CouponUser;
//import com.hyjf.mybatis.model.auto.CouponUserExample;
//import com.hyjf.mybatis.model.auto.ParamName;
//import com.hyjf.mybatis.model.customize.coupon.PlanCouponResultBean;
//import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
//import com.hyjf.mybatis.model.customize.web.CouponUserListCustomize;
//import com.hyjf.soa.apiweb.CommonSoaUtils;
//import com.hyjf.app.BaseServiceImpl;
//
//@Service("couponService")
//public class CouponServiceImpl extends BaseServiceImpl implements CouponService {
//
//	@Override
//	public void getProjectAvailableUserCoupon(String platform, String borrowNid, Integer userId, JSONObject ret, String money) {
//		// Integer userId = SecretUtil.getUserId(sign);
//		// Integer userId = 10000361;
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("userId", userId);
//		// 查询项目信息
//		Borrow borrow = this.getBorrowByNid(borrowNid);
//		List<CouponBean> availableCouponList = new ArrayList<CouponBean>();
//		List<CouponBean> notAvailableCouponList = new ArrayList<CouponBean>();
//		BorrowProjectType borrowProjectType = getProjectTypeByBorrowNid(borrowNid);
//		Integer couponFlg = borrow.getBorrowInterestCoupon();
//		Integer moneyFlg = borrow.getBorrowTasteMoney();
//		/*
//		 * int couponType = 0; if(couponFlg==1&&moneyFlg!=1){ couponType = 2;
//		 * }else if(couponFlg!=1&&moneyFlg==1){ couponType = 1; }
//		 * map.put("couponType", couponType);
//		 */
//
//		List<UserCouponConfigCustomize> couponConfigs = couponConfigCustomizeMapper.getCouponConfigList(map);
//		// 操作平台
//		List<ParamName> clients = this.getParamNameList("CLIENT");
//
//		String style = borrow.getBorrowStyle();
//		for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
//			// couponFlg=1
//			// moneyFlg=1
//			/**********************
//			 * 网站改版新加参数构建 pcc start
//			 ***************************/
//			CouponBean couponBean = new CouponBean();
//			// 优惠券使用平台
//			couponBean.setCouponSystem(createCouponSystemString(userCouponConfigCustomize.getCouponSystem(), clients));
//			// 优惠券使用项目
//			couponBean.setProjectType(createProjectTypeString(userCouponConfigCustomize.getProjectType()));
//			// 优惠券项目期限
//			couponBean.setProjectExpiration(createProjectExpiration(borrow, userCouponConfigCustomize, style));
//			/**********************
//			 * 网站改版新加参数构建 pcc end
//			 ***************************/
//			// 验证项目加息券或体验金是否可用
//			if (couponFlg != null && couponFlg == 0) {
//				if (userCouponConfigCustomize.getCouponType() == 2) {
//					couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "该项目加息券不能使用");
//					notAvailableCouponList.add(couponBean);
//					continue;
//				}
//			}
//			if (moneyFlg != null && moneyFlg == 0) {
//				if (userCouponConfigCustomize.getCouponType() == 1) {
//					couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "该项目优惠券不能使用");
//					notAvailableCouponList.add(couponBean);
//					continue;
//				}
//			}
//
//			// 验证项目金额
//			Integer tenderQuota = userCouponConfigCustomize.getTenderQuotaType();
//
//			if (tenderQuota == 1) {
//				if (userCouponConfigCustomize.getTenderQuotaMin() > new Double(money) || userCouponConfigCustomize.getTenderQuotaMax() < new Double(money)) {
//					couponBean = createCouponBean(userCouponConfigCustomize, couponBean, userCouponConfigCustomize.getTenderQuotaMin() + "元~" + userCouponConfigCustomize.getTenderQuotaMax() + "元可用");
//					notAvailableCouponList.add(couponBean);
//					continue;
//				}
//			} else if (tenderQuota == 2) {
//				if (userCouponConfigCustomize.getTenderQuota() > new Double(money)) {
//					couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "满" + userCouponConfigCustomize.getTenderQuota() + "元可用");
//					notAvailableCouponList.add(couponBean);
//					continue;
//				}
//			}
//
//			// 验证项目期限
//			Integer type = userCouponConfigCustomize.getProjectExpirationType();
//			if ("endday".equals(style)) {
//				if (type == 1) {
//					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) != borrow.getBorrowPeriod()) {
//						couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "适用" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//						notAvailableCouponList.add(couponBean);
//						continue;
//					}
//				} else if (type == 3) {
//					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) > borrow.getBorrowPeriod()) {
//						couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "适用≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//						notAvailableCouponList.add(couponBean);
//						continue;
//					}
//				} else if (type == 4) {
//					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) < borrow.getBorrowPeriod()) {
//						couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "适用≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//						notAvailableCouponList.add(couponBean);
//						continue;
//					}
//				} else if (type == 2) {
//					if ((userCouponConfigCustomize.getProjectExpirationLengthMin() * 30) > borrow.getBorrowPeriod()
//							|| (userCouponConfigCustomize.getProjectExpirationLengthMax() * 30) < borrow.getBorrowPeriod()) {
//						couponBean = createCouponBean(userCouponConfigCustomize, couponBean,
//								"适用" + userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月~" + userCouponConfigCustomize.getProjectExpirationLengthMax() + "个月的项目");
//						notAvailableCouponList.add(couponBean);
//						continue;
//					}
//				}
//			} else {
//				if (type == 1) {
//					if (userCouponConfigCustomize.getProjectExpirationLength() != borrow.getBorrowPeriod()) {
//						couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "适用" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//						notAvailableCouponList.add(couponBean);
//						continue;
//					}
//				} else if (type == 3) {
//					if (userCouponConfigCustomize.getProjectExpirationLength() > borrow.getBorrowPeriod()) {
//						couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "适用≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//						notAvailableCouponList.add(couponBean);
//						continue;
//					}
//				} else if (type == 4) {
//					if (userCouponConfigCustomize.getProjectExpirationLength() < borrow.getBorrowPeriod()) {
//						couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "适用≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目");
//						notAvailableCouponList.add(couponBean);
//						continue;
//					}
//				} else if (type == 2) {
//					if (userCouponConfigCustomize.getProjectExpirationLengthMin() > borrow.getBorrowPeriod() || userCouponConfigCustomize.getProjectExpirationLengthMax() < borrow.getBorrowPeriod()) {
//						couponBean = createCouponBean(userCouponConfigCustomize, couponBean,
//								"适用" + userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月~" + userCouponConfigCustomize.getProjectExpirationLengthMax() + "个月的项目");
//						notAvailableCouponList.add(couponBean);
//						continue;
//					}
//				}
//
//			}
//
//			// 验证可使用项目 新逻辑 pcc20160715
//			String projectType = userCouponConfigCustomize.getProjectType();
//			boolean ifprojectType = true;
//			if (projectType.indexOf("-1") != -1) {
//				if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
//					ifprojectType = false;
//				}
//			} else {
//				if ("HXF".equals(borrowProjectType.getBorrowClass())) {
//					if (projectType.indexOf("2") != -1) {
//						ifprojectType = false;
//					}
//				} else if ("NEW".equals(borrowProjectType.getBorrowClass())) {
//					if (projectType.indexOf("3") != -1) {
//						ifprojectType = false;
//					}
//				} else if ("ZXH".equals(borrowProjectType.getBorrowClass())) {
//					if (projectType.indexOf("4") != -1) {
//						ifprojectType = false;
//					}
//				} else {
//					if (projectType.indexOf("1") != -1) {
//						if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
//							ifprojectType = false;
//						}
//					}
//				}
//			}
//			if (ifprojectType) {
//				couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "适用" + createProjectTypeString(projectType));
//				notAvailableCouponList.add(couponBean);
//				continue;
//			}
//
//			/**************逻辑修改 pcc start***************/
//            //是否与本金公用
//            boolean addFlg = false;
//            if(userCouponConfigCustomize.getAddFlg()==1&&!"0".equals(money)){
//                addFlg = true;
//            }
//            if(addFlg){
//                couponBean =
//                        createCouponBean(userCouponConfigCustomize,couponBean, "不能与本金公用");
//                notAvailableCouponList.add(couponBean);
//                continue;
//            }
//            /**************逻辑修改 pcc end***************/
//			
//			// 验证使用平台
//			String couponSystem = userCouponConfigCustomize.getCouponSystem();
//			String[] couponSystemArr = couponSystem.split(",");
//			boolean ifcouponSystem = true;
//			for (String couponSystemString : couponSystemArr) {
//				if ("-1".equals(couponSystemString)) {
//					ifcouponSystem = false;
//					break;
//				}
//				if (platform.equals(couponSystemString)) {
//					ifcouponSystem = false;
//					break;
//				}
//			}
//
//			if (ifcouponSystem) {
//				couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "适用" + createCouponSystemString(couponSystem, clients));
//				notAvailableCouponList.add(couponBean);
//			} else {
//				couponBean = createCouponBean(userCouponConfigCustomize, couponBean, "");
//				availableCouponList.add(couponBean);
//			}
//			// updateCouponBean(couponBean,userCouponConfigCustomize,clients);
//
//		}
//
//		// 排序
//		Collections.sort(availableCouponList, new ComparatorCouponBean());
//		// 排序
//		Collections.sort(notAvailableCouponList, new ComparatorCouponBean());
//
//		ret.put("availableCouponList", availableCouponList);
//		ret.put("notAvailableCouponList", notAvailableCouponList);
//		ret.put("availableCouponListCount", availableCouponList.size());
//		ret.put("notAvailableCouponListCount", notAvailableCouponList.size());
//	}
//
//	/********************** 网站改版新加参数构建方法 pcc start ***************************/
//	private String createProjectExpiration(Borrow borrow, UserCouponConfigCustomize userCouponConfigCustomize, String style) {
//		// 验证项目期限
//		Integer type = userCouponConfigCustomize.getProjectExpirationType();
//		if ("endday".equals(style)) {
//			if (type == 1) {
//				if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) != borrow.getBorrowPeriod()) {
//					return "适用" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
//				}
//			} else if (type == 3) {
//				if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) > borrow.getBorrowPeriod()) {
//					return "适用≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
//				}
//			} else if (type == 4) {
//				if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) < borrow.getBorrowPeriod()) {
//					return "适用≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
//				}
//			} else if (type == 2) {
//				if ((userCouponConfigCustomize.getProjectExpirationLengthMin() * 30) > borrow.getBorrowPeriod()
//						|| (userCouponConfigCustomize.getProjectExpirationLengthMax() * 30) < borrow.getBorrowPeriod()) {
//					return "适用" + userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月~" + userCouponConfigCustomize.getProjectExpirationLengthMax() + "个月的项目";
//				}
//			} else if (type == 0) {
//                return "不限";
//            }
//		} else {
//			if (type == 1) {
//				if (userCouponConfigCustomize.getProjectExpirationLength() != borrow.getBorrowPeriod()) {
//					return "适用" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
//				}
//			} else if (type == 3) {
//				if (userCouponConfigCustomize.getProjectExpirationLength() > borrow.getBorrowPeriod()) {
//					return "适用≥" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
//				}
//			} else if (type == 4) {
//				if (userCouponConfigCustomize.getProjectExpirationLength() < borrow.getBorrowPeriod()) {
//					return "适用≤" + userCouponConfigCustomize.getProjectExpirationLength() + "个月的项目";
//				}
//			} else if (type == 2) {
//				if (userCouponConfigCustomize.getProjectExpirationLengthMin() > borrow.getBorrowPeriod() || userCouponConfigCustomize.getProjectExpirationLengthMax() < borrow.getBorrowPeriod()) {
//					return "适用" + userCouponConfigCustomize.getProjectExpirationLengthMin() + "个月~" + userCouponConfigCustomize.getProjectExpirationLengthMax() + "个月的项目";
//				}
//			} else if (type == 0) {
//			    return "不限";
//            }
//
//		}
//		return "不限";
//	}
//
//	/********************** 网站改版新加参数构建方法 pcc end ***************************/
//	private String createCouponSystemString(String couponSystem, List<ParamName> clients) {
//		String clientString = "";
//
//		// 被选中操作平台
//		String clientSed[] = StringUtils.split(couponSystem, ",");
//		for (int i = 0; i < clientSed.length; i++) {
//			if ("-1".equals(clientSed[i])) {
//				clientString = clientString + "全部平台";
//				break;
//			} else {
//				for (ParamName paramName : clients) {
//					if (clientSed[i].equals(paramName.getNameCd())) {
//						if (i != 0 && clientString.length() != 0) {
//							clientString = clientString + "、";
//						}
//						clientString = clientString + paramName.getName();
//
//					}
//				}
//			}
//		}
//		return clientString.replace("Android、iOS", "APP");
//	}
//
//	private String createProjectTypeString(String projectType) {
//	    String projectString = "";
//        if (projectType.indexOf("-1") != -1) {
//            projectString = "不限";
//        } else {
//            //勾选汇直投，尊享汇，融通宝
//            if (projectType.indexOf("1")!=-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")!=-1) {
//                projectString = projectString + "债权,";
//            }
//            //勾选汇直投  未勾选尊享汇，融通宝
//            if (projectType.indexOf("1")!=-1&&projectType.indexOf("4")==-1&&projectType.indexOf("7")==-1) {
//                projectString = projectString + "债权(尊享,优选除外),";
//            }
//            //勾选汇直投，融通宝  未勾选尊享汇
//            if(projectType.indexOf("1")!=-1&&projectType.indexOf("4")==-1&&projectType.indexOf("7")!=-1){
//                projectString = projectString + "债权(尊享除外),";
//            }
//            //勾选汇直投，选尊享汇 未勾选融通宝
//            if(projectType.indexOf("1")!=-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")==-1){
//                projectString = projectString + "债权(优选除外),";
//            }
//            //勾选尊享汇，融通宝  未勾选直投
//            if(projectType.indexOf("1")==-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")!=-1){
//                projectString = projectString + "债权(仅限尊享,优选),";
//            }
//            //勾选尊享汇  未勾选直投，融通宝
//            if(projectType.indexOf("1")==-1&&projectType.indexOf("4")!=-1&&projectType.indexOf("7")==-1){
//                projectString = projectString + "债权(仅限尊享),";
//            }
//            //勾选尊享汇  未勾选直投，融通宝
//            if(projectType.indexOf("1")==-1&&projectType.indexOf("4")==-1&&projectType.indexOf("7")!=-1){
//                projectString = projectString + "债权(仅限优选),";
//            }
//            
//            if (projectType.indexOf("3")!=-1) {
//                projectString = projectString + "新手,";
//            }
//            /*if (projectType.indexOf("5")!=-1) {
//                projectString = projectString + "汇添金,";
//            }*/
//            if (projectType.indexOf("6")!=-1) {
//                projectString = projectString + "汇计划,";
//            }
//            projectString = StringUtils.removeEnd(projectString, ",");
//        }
//		return projectString;
//	}
//
//	private CouponBean createCouponBean(UserCouponConfigCustomize userCouponConfigCustomize, CouponBean couponBean, String remarks) {
//		couponBean.setUserCouponId(userCouponConfigCustomize.getUserCouponId());
//		couponBean.setRemarks(remarks);
//		couponBean.setCouponUserCode(userCouponConfigCustomize.getCouponUserCode());
//		if (userCouponConfigCustomize.getCouponType() == 1) {
//			couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota().intValue() + "");
//			couponBean.setCouponType("体验金");
//
//		} else if (userCouponConfigCustomize.getCouponType() == 2) {
//			couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota() + "");
//			couponBean.setCouponType("加息券");
//		} else if (userCouponConfigCustomize.getCouponType() == 3) {
//			couponBean.setCouponQuota(userCouponConfigCustomize.getCouponQuota().intValue() + "");
//			couponBean.setCouponType("代金券");
//		}
//		couponBean.setCouponTypeStr(userCouponConfigCustomize.getCouponType());
//		couponBean.setCouponName(userCouponConfigCustomize.getCouponName());
//		couponBean.setEndTime(userCouponConfigCustomize.getCouponAddTime() + "-" + userCouponConfigCustomize.getEndTime());
//		couponBean.setCouponAddTime(userCouponConfigCustomize.getCouponAddTime());
//		couponBean.setCouponEndTime(userCouponConfigCustomize.getEndTime());
//		if (userCouponConfigCustomize.getTenderQuotaType() == 0 || userCouponConfigCustomize.getTenderQuotaType() == null) {
//			couponBean.setTenderQuota("出借金额不限");
//		} else if (userCouponConfigCustomize.getTenderQuotaType() == 1) {
//		    String tenderQuotaMin=userCouponConfigCustomize.getTenderQuotaMin()+"";
//            if(userCouponConfigCustomize.getTenderQuotaMin()>=10000 && userCouponConfigCustomize.getTenderQuotaMin()%10000==0){
//                tenderQuotaMin=userCouponConfigCustomize.getTenderQuotaMin()/10000+"万";
//            }
//            String tenderQuotaMax=userCouponConfigCustomize.getTenderQuotaMax()+"";
//            if(userCouponConfigCustomize.getTenderQuotaMax()>=10000 && userCouponConfigCustomize.getTenderQuotaMax()%10000==0){
//                tenderQuotaMax=userCouponConfigCustomize.getTenderQuotaMax()/10000+"万";
//            }
//            couponBean.setTenderQuota( tenderQuotaMin+ "元~"+ tenderQuotaMax + "元可用");
//
//		} else if (userCouponConfigCustomize.getTenderQuotaType() == 2) {
//		    String tenderQuota=userCouponConfigCustomize.getTenderQuota()+"";
//            if(userCouponConfigCustomize.getTenderQuota()>=10000 && userCouponConfigCustomize.getTenderQuota()%10000==0){
//                tenderQuota=userCouponConfigCustomize.getTenderQuota()/10000+"万";
//            }
//            couponBean.setTenderQuota("满" + tenderQuota + "元可用");
//		}
//		return couponBean;
//	}
//
//	/**
//	 * 
//	 * 根据borrowNid获取Borrow对象
//	 * 
//	 * @author pcc
//	 * @param borrowNid
//	 * @return
//	 */
////	public Borrow getBorrowByNid(String borrowNid) {
////		Borrow borrow = null;
////		BorrowExample example = new BorrowExample();
////		BorrowExample.Criteria criteria = example.createCriteria();
////		criteria.andBorrowNidEqualTo(borrowNid);
////		List<Borrow> list = borrowMapper.selectByExample(example);
////		if (list != null && !list.isEmpty()) {
////			borrow = list.get(0);
////		}
////		return borrow;
////	}
//
//	/**
//	 * 
//	 * 根据borrowNid获取项目类型对象
//	 * 
//	 * @author pcc
//	 * @param borrowNid
//	 * @return
//	 */
//	public BorrowProjectType getProjectTypeByBorrowNid(String borrowNid) {
//		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
//		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
//		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL).andBorrowClassEqualTo(borrowNid.substring(0, 3));
//		List<BorrowProjectType> borrowProjectTypes = this.borrowProjectTypeMapper.selectByExample(example);
//		BorrowProjectType borrowProjectType = new BorrowProjectType();
//		if (borrowProjectTypes != null && borrowProjectTypes.size() > 0) {
//			borrowProjectType = borrowProjectTypes.get(0);
//		}
//		return borrowProjectType;
//	}
//
//	public List<BorrowProjectType> getProjectTypeList() {
//		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
//		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
//		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
//		return this.borrowProjectTypeMapper.selectByExample(example);
//	}
//
//	@Override
//	public String getUserCouponAvailableCount(String borrowNid, Integer userId, String money, String platform) {
//		// System.out.println("~~~~~~~~~~~~~~~~getUserCouponAvailableCount~~~~~~~~~~~~~~~~~~");
//		// Integer userId = 20000098;
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("userId", userId);
//		// 查询项目信息
//		Borrow borrow = this.getBorrowByNid(borrowNid);
//		BorrowProjectType borrowProjectType = getProjectTypeByBorrowNid(borrowNid);
//		Integer couponFlg = borrow.getBorrowInterestCoupon();
//		Integer moneyFlg = borrow.getBorrowTasteMoney();
//		List<UserCouponConfigCustomize> couponConfigs = couponConfigCustomizeMapper.getCouponConfigList(map);
//
//		List<CouponBean> availableCouponList = new ArrayList<CouponBean>();
//
//		String style = borrow.getBorrowStyle();
//
//		if (money == null || "".equals(money) || money.length() == 0) {
//			money = "0";
//		}
//		for (UserCouponConfigCustomize userCouponConfigCustomize : couponConfigs) {
//
//			// 验证项目加息券或体验金是否可用
//			if (couponFlg != null && couponFlg == 0) {
//				if (userCouponConfigCustomize.getCouponType() == 2) {
//					continue;
//				}
//			}
//			if (moneyFlg != null && moneyFlg == 0) {
//				if (userCouponConfigCustomize.getCouponType() == 1) {
//					continue;
//				}
//			}
//			// 验证项目金额
//			Integer tenderQuota = userCouponConfigCustomize.getTenderQuotaType();
//
//			if (tenderQuota == 1) {
//				if (userCouponConfigCustomize.getTenderQuotaMin() > new Double(money) || userCouponConfigCustomize.getTenderQuotaMax() < new Double(money)) {
//					continue;
//				}
//			} else if (tenderQuota == 2) {
//				if (userCouponConfigCustomize.getTenderQuota() > new Double(money)) {
//					continue;
//				}
//			}
//			// 验证项目期限
//			Integer type = userCouponConfigCustomize.getProjectExpirationType();
//			if ("endday".equals(style)) {
//				if (type == 1) {
//					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) != borrow.getBorrowPeriod()) {
//						continue;
//					}
//				} else if (type == 3) {
//					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) > borrow.getBorrowPeriod()) {
//						continue;
//					}
//				} else if (type == 4) {
//					if ((userCouponConfigCustomize.getProjectExpirationLength() * 30) < borrow.getBorrowPeriod()) {
//						continue;
//					}
//				} else if (type == 2) {
//					if ((userCouponConfigCustomize.getProjectExpirationLengthMin() * 30) > borrow.getBorrowPeriod()
//							|| (userCouponConfigCustomize.getProjectExpirationLengthMax() * 30) < borrow.getBorrowPeriod()) {
//						continue;
//					}
//				}
//			} else {
//				if (type == 1) {
//					if (userCouponConfigCustomize.getProjectExpirationLength() != borrow.getBorrowPeriod()) {
//						continue;
//					}
//				} else if (type == 3) {
//					if (userCouponConfigCustomize.getProjectExpirationLength() > borrow.getBorrowPeriod()) {
//						continue;
//					}
//				} else if (type == 4) {
//					if (userCouponConfigCustomize.getProjectExpirationLength() < borrow.getBorrowPeriod()) {
//						continue;
//					}
//				} else if (type == 2) {
//					if (userCouponConfigCustomize.getProjectExpirationLengthMin() > borrow.getBorrowPeriod() || userCouponConfigCustomize.getProjectExpirationLengthMax() < borrow.getBorrowPeriod()) {
//						continue;
//					}
//				}
//
//			}
//			// 验证可使用项目
//			String projectType = userCouponConfigCustomize.getProjectType();
//			// String[] projectTypeArr=projectType.split(",");
//			boolean ifprojectType = true;
//
//			if (projectType.indexOf("-1") != -1) {
//				if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
//					ifprojectType = false;
//				}
//			} else {
//				if ("HXF".equals(borrowProjectType.getBorrowClass())) {
//					if (projectType.indexOf("2") != -1) {
//						ifprojectType = false;
//					}
//				} else if ("NEW".equals(borrowProjectType.getBorrowClass())) {
//					if (projectType.indexOf("3") != -1) {
//						ifprojectType = false;
//					}
//				} else if ("ZXH".equals(borrowProjectType.getBorrowClass())) {
//					if (projectType.indexOf("4") != -1) {
//						ifprojectType = false;
//					}
//				} else {
//					if (projectType.indexOf("1") != -1) {
//						if (!"RTB".equals(borrowProjectType.getBorrowClass())) {
//							ifprojectType = false;
//						}
//					}
//				}
//			}
//			if (ifprojectType) {
//				continue;
//			}
//			/**************逻辑修改 pcc start***************/
//            //是否与本金公用
//            boolean addFlg = false;
//            if(userCouponConfigCustomize.getAddFlg()==1&&!"0".equals(money)){
//                addFlg = true;
//            }
//            if(addFlg){
//                continue;
//            }
//            /**************逻辑修改 pcc end***************/
//			// 验证使用平台
//			String couponSystem = userCouponConfigCustomize.getCouponSystem();
//			String[] couponSystemArr = couponSystem.split(",");
//			boolean ifcouponSystem = true;
//			for (String couponSystemString : couponSystemArr) {
//				if ("-1".equals(couponSystemString)) {
//					ifcouponSystem = false;
//					break;
//				}
//				if (platform.equals(couponSystemString)) {
//					ifcouponSystem = false;
//					break;
//				}
//			}
//			if (ifcouponSystem) {
//				continue;
//			} else {
//				CouponBean couponBean = createCouponBean(userCouponConfigCustomize, new CouponBean(), null);
//				availableCouponList.add(couponBean);
//			}
//		}
//		// System.out.println("~~~~~~~~~~~~~~~~统计结束~~~~~~~~~~~~~~~~~~"+availableCouponList.size());
//		return availableCouponList.size() + "";
//	}
//
//	@Override
//	public int updateCouponReadFlag(Integer userId, Integer readFlag) {
//		CouponUser couponUser = new CouponUser();
//		couponUser.setReadFlag(readFlag);
//
//		CouponUserExample example = new CouponUserExample();
//		CouponUserExample.Criteria cra = example.createCriteria();
//		cra.andUserIdEqualTo(userId).andUsedFlagEqualTo(CustomConstants.USER_COUPON_STATUS_UNUSED).andDelFlagEqualTo(Integer.parseInt(CustomConstants.FLAG_NORMAL));
//
//		return couponUserMapper.updateByExampleSelective(couponUser, example);
//	}
//
//	/**
//	 * 
//	 * 我的优惠券列表
//	 * 
//	 * @author hsy
//	 * @param form
//	 * @param limitStart
//	 * @param limitEnd
//	 * @return
//	 * @see com.hyjf.web.coupon.CouponService#selectUserCouponList(com.hyjf.web.coupon.CouponListBean,
//	 *      int, int)
//	 */
//	@Override
//	public List<CouponUserListCustomize> selectUserCouponList(CouponListBean form, int limitStart, int limitEnd) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		Integer usedFlag =  form.getUsedFlag();
//		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
//		params.put("usedFlag", usedFlag);
//		params.put("userId", userId);
//		params.put("limitStart", -1);
//		params.put("limitEnd", -1);
//		if (limitStart == 0 || limitStart > 0) {
//			params.put("limitStart", limitStart);
//		}
//		if (limitEnd > 0) {
//			params.put("limitEnd", limitEnd);
//		}
//		List<CouponUserListCustomize> list = couponUserListCustomizeMapper.selectCouponUserList(params);
//		return list;
//	}
//
//	/**
//	 * 
//	 * 我的优惠券列表总记录数
//	 * 
//	 * @author hsy
//	 * @param form
//	 * @return
//	 * @see com.hyjf.web.coupon.CouponService#countUserCouponTotal(com.hyjf.web.coupon.CouponListBean)
//	 */
//	@Override
//	public int countUserCouponTotal(CouponListBean form) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		Integer usedFlag = form.getUsedFlag();
//		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
//		params.put("userId", userId);
//		params.put("usedFlag", usedFlag);
//		int total = couponUserListCustomizeMapper.countCouponUserList(params);
//		return total;
//	}
//
//	/**
//	 * 用户的优惠券总数
//	 * 
//	 * @author hsy
//	 * @param couponUserCustomize
//	 * @return
//	 */
//	@Override
//	public Integer countCouponUsers(Map<String, Object> paraMap) {
//		return couponUserCustomizeMapper.countCouponUserForApp(paraMap);
//	}
//
//	/**
//	 * 获取用户可用优惠券数
//	 * 
//	 * @param userId
//	 * @return
//	 */
//	@Override
//	public Integer selectCouponValidCount(Integer userId) {
//		Integer couponValidCount = couponUserCustomizeMapper.countCouponValid(userId);
//		if (couponValidCount == null) {
//			couponValidCount = 0;
//		}
//
//		return couponValidCount;
//	}
//
//    @Override
//    public void getHJHProjectAvailableUserCoupon(String platform, String borrowNid, JSONObject ret, String money,
//        String userId) {
//        JSONObject jsonObject = CommonSoaUtils.getProjectAvailableUserCoupon(platform, borrowNid, money, Integer.parseInt(userId));
//        
//        
//        ret.put("availableCouponList", JSONArray.parseArray(
//                JSONObject.toJSONString(jsonObject.get("availableCouponList")), PlanCouponResultBean.class));
//        ret.put("notAvailableCouponList", JSONArray.parseArray(
//                JSONObject.toJSONString(jsonObject.get("notAvailableCouponList")), PlanCouponResultBean.class));
//        ret.put("availableCouponListCount", jsonObject.get("availableCouponListCount"));
//        ret.put("notAvailableCouponListCount", jsonObject.get("notAvailableCouponListCount"));
//    }
//
//}
//
//class ComparatorCouponBean implements Comparator<CouponBean> {
//
//	@Override
//	public int compare(CouponBean couponBean1, CouponBean couponBean2) {
//		if (1 == couponBean1.getCouponTypeStr()) {
//			couponBean1.setCouponTypeStr(4);
//		}
//		if (1 == couponBean2.getCouponTypeStr()) {
//			couponBean2.setCouponTypeStr(4);
//		}
//		int flag = couponBean1.getCouponTypeStr() - couponBean2.getCouponTypeStr();
//		if (4 == couponBean1.getCouponTypeStr()) {
//			couponBean1.setCouponTypeStr(1);
//		}
//		if (4 == couponBean2.getCouponTypeStr()) {
//			couponBean2.setCouponTypeStr(1);
//		}
//		return flag;
//	}
//
//}
