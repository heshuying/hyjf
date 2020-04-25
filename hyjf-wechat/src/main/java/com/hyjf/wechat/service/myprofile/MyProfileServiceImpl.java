package com.hyjf.wechat.service.myprofile;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hyjf.bank.service.user.bindcard.BindCardService;
import com.hyjf.common.http.HttpClientUtils;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import com.hyjf.mybatis.model.customize.web.CouponUserListCustomize;
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.controller.user.myprofile.MyProfileVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 账户总览
 * Created by cuigq on 2018/2/1.
 */
@Service
public class MyProfileServiceImpl extends BaseServiceImpl implements MyProfileService {

    @Autowired
    private BindCardService userBindCardService;

    @Override
    public String getUserTrueName(Integer userId) {
        String username = "";
        UsersInfo usersinfo = this.getUsersInfoByUserId(userId);
        if (StringUtils.isNotEmpty(usersinfo.getTruename())) {
            username = usersinfo.getTruename().substring(0, 1);
            if (usersinfo.getSex() == 2) { //女
                username = username + "女士";
            } else {
                username = username + "先生";
            }
        } else {
            Users user = this.getUsers(userId);
            username = user.getUsername();
            int len = username.length();
            if (isChineseChar(username)) {
                if (len > 4) {
                    username = username.substring(0, 4) + "...";
                }
            } else {
                if (len > 8) {
                    username = username.substring(0, 8) + "...";
                }
            }
        }
        return username;
    }

    private boolean isChineseChar(String username) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(username);
        if (m.find()) {
            return true;
        }
        return false;
    }

    @Override
    public void buildUserAccountInfo(Integer userId, MyProfileVO.UserAccountInfo userAccountInfo) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        Preconditions.checkArgument(users != null, userId + "用户不存在！");
        //是否绑定邮箱
        userAccountInfo.setSetEmail(!Strings.isNullOrEmpty(users.getEmail()));
        if (users.getOpenAccount() != null) {
            //汇付账户？
             userAccountInfo.setChinapnrUser(users.getOpenAccount().intValue() == 1);
        }
        //begin tyy 2018-12-17 关闭汇付余额显示
        ParamNameExample paramNameExample = new ParamNameExample();
        ParamNameExample.Criteria cra = paramNameExample.createCriteria();
        cra.andNameCdEqualTo("1");
        cra.andNameClassEqualTo("CLOSE_WITHDRAWAL");//关闭现提
        List<ParamName>  params =  paramNameMapper.selectByExample(paramNameExample);
        if(!CollectionUtils.isEmpty(params)){
                ParamName paramName = params.get(0);
            try {
                String closeTimeStr = paramName.getName();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date closeDate = sdf.parse(closeTimeStr);
                Date now = new Date();
                if(closeDate.getTime()<now.getTime()){
                    userAccountInfo.setChinapnrUser(false);
                }
            }catch (Exception e){
                userAccountInfo.setChinapnrUser(false);
            }

        }

        //end tyy 2018-12-17 关闭汇付余额显示
        if (users.getBankOpenAccount() != null) {
            //银行账户？
            userAccountInfo.setBankUser(users.getBankOpenAccount().intValue() == 1);
        }

        if (users.getIsSetPassword() != null) {
            //已设置支付密码？
            userAccountInfo.setSetPassword(users.getIsSetPassword().intValue() == 1);
        }

//        if (users.getIsEvaluationFlag() != null) {
//            //已测评？
//            userAccountInfo.setEvaluated(users.getIsEvaluationFlag().intValue() == 1);
//        }
        try {
			if(users.getIsEvaluationFlag()==1 && null != users.getEvaluationExpiredTime()){
				//测评到期日
				Long lCreate = users.getEvaluationExpiredTime().getTime();
				//当前日期
				Long lNow = System.currentTimeMillis();
				if (lCreate <= lNow) {
					//已过期需要重新评测
					userAccountInfo.setEvaluated("2");
				} else {
					//未到一年有效期
					userAccountInfo.setEvaluated("1");
				}
			}else{
				//未评测
				userAccountInfo.setEvaluated("0");
			}
		} catch (Exception e) {
			userAccountInfo.setEvaluated("0");
		}
        // 设置服务费授权
        if (users.getPaymentAuthStatus() != null) {
            userAccountInfo.setPaymentAuthStatus(users.getPaymentAuthStatus());
        }
 
        // 设置服务费授权开关 0未开启  1已开启
        userAccountInfo.setPaymentAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
        
        if (users.getUserId()!=null){
            List<BankCard> bankCardList= userBindCardService.getAccountBankByUserId(String.valueOf(users.getUserId()));
            if(CollectionUtils.isNotEmpty(bankCardList) && bankCardList.size()>0){
                userAccountInfo.setIsBindCard(true);
            }else{
            	userAccountInfo.setIsBindCard(false);
            }
        }

        userAccountInfo.setUserName(users.getUsername());

        userAccountInfo.setInvitationCode(users.getUserId());

    }

    @Override
    public void buildOutInfo(Integer userId, MyProfileVO myProfileVO) {
        Account account = this.getAccount(userId);
        Preconditions.checkArgument(account != null, "userId=【" + userId + "】没有账户信息！");

        //资产总额
        myProfileVO.setAccountTotle(account.getBankTotal() == null ? BigDecimal.ZERO : account.getBankTotal());

        //可用余额
        myProfileVO.setBankBalance(account.getBankBalance() == null ? BigDecimal.ZERO : account.getBankBalance());

        //累计收益
        myProfileVO.setInterestTotle(account.getBankInterestSum() == null ? BigDecimal.ZERO : account.getBankInterestSum());

        //待收收益
        if (account.getPlanInterestWait() != null && account.getBankAwaitInterest() != null) {
            myProfileVO.setWaitInterest(account.getBankAwaitInterest().add(account.getPlanInterestWait()));
        } else {
            myProfileVO.setWaitInterest(BigDecimal.ZERO);
        }

        //汇付帐户
       if (myProfileVO.getUserAccountInfo().isChinapnrUser()) {
            myProfileVO.setChinapnrBalance(account.getBalance() == null ? BigDecimal.ZERO : account.getBalance());
        } else {
            myProfileVO.setChinapnrBalance(BigDecimal.ZERO);
        }

        //我的散标
        myProfileVO.setBankAwait(account.getBankAwait() == null ? BigDecimal.ZERO : account.getBankAwait());
        //我的计划
        myProfileVO.setPlanAccountWait(account.getPlanAccountWait() == null ? BigDecimal.ZERO : account.getPlanAccountWait());

        //优惠卷数量
        Integer couponValidCount = couponUserCustomizeMapper.countCouponValid(userId);
        myProfileVO.setCouponValidCount(couponValidCount == null ? 0 : couponValidCount);

    }

    /**
     * 查询用户优惠卷列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<CouponUserListCustomize> queryUserCouponList(Integer userId) {
        List<CouponUserListCustomize> lstCoupon = Lists.newArrayList();

        Map<String, Object> mapParameter = Maps.newHashMap();
        //未使用
        mapParameter.put("usedFlag", "0");
        mapParameter.put("userId", userId);
        mapParameter.put("limitStart",-1);
        List<CouponUserListCustomize> lstResult = couponUserListCustomizeMapper.selectCouponUserList(mapParameter);

        //平台
        List<ParamName> clients = this.getParamNameList("CLIENT");
        Map<String, String> mapPlatform = Maps.newHashMap();
        for (ParamName client : clients) {
            mapPlatform.put(client.getNameCd(), client.getName());
        }

        //平台中文转换
        for (CouponUserListCustomize result : lstResult) {
            String systemStr = result.getCouponSystem();
            List<String> lstSystem = Lists.newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(systemStr));

            if (lstSystem.contains("-1")) {
                result.setCouponSystem("不限");
            } else {
                List<String> lstTemp = Lists.newArrayList();
                for (String system : lstSystem) {
                    String chinesePlat = mapPlatform.get(system);
                    Preconditions.checkArgument(chinesePlat != null, "字典表中没有值=" + system + "的平台");
                    lstTemp.add(chinesePlat);
                }
                String couponSystem = Joiner.on("/").join(lstTemp);
                result.setCouponSystem(couponSystem);
            }

            String projectType = result.getProjectType();
            String projectString = getProjectString(projectType);
            result.setProjectType(projectString);
        }

        lstCoupon.addAll(lstResult);
        return lstCoupon;
    }

    /**
     * 项目类型转换
     *
     * @param projectType
     * @return
     */
    private String getProjectString(String projectType) {
        String projectString = "";
        if (projectType.indexOf("-1") != -1) {
            projectString = "不限";
        } else {
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
            // mod by nxl 智投服务：修改汇计划->智投服务 start
       /* if (projectType.indexOf("6")!=-1) {
            projectString = projectString + "汇计划,";
        }*/
            if (projectType.indexOf("6")!=-1) {
                projectString = projectString + "智投,";
            }
            // mod by nxl 智投服务：修改汇计划->智投服务 end
            projectString = StringUtils.removeEnd(projectString, ",");
        }
        return projectString;
    }

	@Override
	public String getUserCouponsData(String couponStatus, Integer page,
			Integer pageSize, Integer userId, String host) {
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

    @Override
    public AdminUserDetailCustomize selectUserUtmInfo(Integer userId) {
        return adminUsersCustomizeMapper.selectUserUtmInfo(userId);
    }
}
