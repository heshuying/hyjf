package com.hyjf.developer.registerfast;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.developer.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.PreRegist;
import com.hyjf.mybatis.model.auto.PreRegistExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.UsersLog;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.auto.UsersExample.Criteria;

@Service
public class UserRegistServiceImpl extends BaseServiceImpl implements UserRegistService{

    @Override
    public int insertUserActionNew(String mobile, String password, String verificationCode, String reffer, String loginIp, String platform, String utm_id, String utm_source, String utm_medium, String utm_content,  HttpServletRequest request, HttpServletResponse response) {
        int userId = 0;
        try {
            // 写入用户信息表
            Users user = new Users();
            String userName = getUniqueUsername(mobile);
            user.setUsername(userName);
            user.setMobile(mobile);
            user.setLoginIp(loginIp);
            user.setRechargeSms(0);
            user.setWithdrawSms(0);
            user.setInvestflag(0);
            user.setInvestSms(0);
            user.setRecieveSms(0);
            user.setVersion(new BigDecimal("0"));
            user.setUserType(0);
            // user.setEmail(ru.getEmail());
            List<Users> recommends = null;
            // 写入用户详情表
            UsersInfo userInfo = new UsersInfo();
            // 以下语句用来设置用户有无主单开始 2015年12月30日18:28:34 孙亮
            userInfo.setAttribute(0);// 默认为无主单
            //1.注册成功时，推荐人关联
            //B1、用户在注册时，没有填写推荐人，也没有预注册，或预注册时没有关联推荐人，则推荐人为空；
            //B2、用户注册时，填写了推荐人，忽略关联推荐人，以填写的推荐人为准；
            //B3、用户注册时，没有填写推荐人，预注册表中，有关联推荐人，以关联的推荐人为准；
            PreRegistExample preRegistExample = new PreRegistExample();
            preRegistExample.createCriteria().andMobileEqualTo(mobile);
            List<PreRegist> preRegistList = preRegistMapper.selectByExample(preRegistExample);
            if (preRegistList != null && preRegistList.size() != 0) {
                for (int i = 0; i < preRegistList.size(); i++) {
                    PreRegist model = preRegistList.get(i);
                    model.setRegistFlag(1);
                    model.setRegistTime(GetDate.getMyTimeInMillis());
                    preRegistMapper.updateByPrimaryKey(model);
                }
            }
            if (StringUtils.isEmpty(reffer)) {
                if (preRegistList != null && preRegistList.size() != 0) {
                    reffer = preRegistList.get(0).getReferrer();
                }
            }
            if (StringUtils.isNotEmpty(reffer)) {
                UsersExample exampleUser = new UsersExample();
                if (Validator.isMobile(reffer)) {
                    UsersExample.Criteria criteria = exampleUser.createCriteria();
                    criteria.andMobileEqualTo(reffer);
                } else {
                    UsersExample.Criteria criteria1 = exampleUser.createCriteria();
                    Integer recommend = Integer.valueOf(reffer);
                    criteria1.andUserIdEqualTo(recommend);
                }
                recommends = usersMapper.selectByExample(exampleUser);
                if (recommends != null && recommends.size() == 1) {
                    UsersInfoExample puie = new UsersInfoExample();
                    UsersInfoExample.Criteria puipec = puie.createCriteria();
                    puipec.andUserIdEqualTo(recommends.get(0).getUserId());
                    List<UsersInfo> pUsersInfoList = usersInfoMapper.selectByExample(puie);
                    if (pUsersInfoList != null && pUsersInfoList.size() == 1) {
                        // 如果该用户的上级不为空
                        UsersInfo parentInfo = pUsersInfoList.get(0);
                        if (Validator.isNotNull(parentInfo) && Validator.isNotNull(parentInfo.getAttribute())) {
                            user.setReferrer(recommends.get(0).getUserId());
                            user.setReferrerUserName(recommends.get(0).getUsername());
                            if (Validator.equals(parentInfo.getAttribute(), new Integer(2)) || Validator.equals(parentInfo.getAttribute(), new Integer(3))) {
                                // 有推荐人且推荐人为员工(Attribute=2或3)时才设置为有主单
                                userInfo.setAttribute(1);
                            }
                        }
                    }
                }
            }else{
                
            }
            // 以上语句用来设置用户有无主单结束 2015年12月30日18:28:34 孙亮
            if (StringUtils.isNotBlank(platform)) {
                user.setRegEsb(Integer.parseInt(platform)); //账户开通平台 0pc 1微信 2安卓 3IOS 4其他
            }
            int time = GetDate.getNowTime10();
            String codeSalt = GetCode.getRandomCode(6);
            user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt));
            user.setRegIp(loginIp);
            user.setRegTime(time);
            user.setLoginIp(loginIp);
            user.setLoginTime(time);
            user.setLastIp(loginIp);
            user.setLastTime(time);
            user.setLogintime(1);// 登录次数
            user.setStatus(0);

            user.setSalt(codeSalt);
            user.setBorrowSms(0);
            // user.setAccountEsb(0);
            user.setPid(0);
            user.setUsernamep("");
            user.setPtype(0);
            user.setOpenAccount(0);
            usersMapper.insertSelective(user);
            userId = user.getUserId();
            // userInfo表
            userInfo.setUserId(userId);
            userInfo.setRoleId(1);
            userInfo.setMobileIsapprove(1);
            userInfo.setTruenameIsapprove(0);
            userInfo.setEmailIsapprove(0);
            userInfo.setUpdateTime(time);
            userInfo.setPromoteway(0);
            userInfo.setIs51(0);
            userInfo.setIsStaff(0);
            userInfo.setDepartmentId(0);
            userInfo.setNickname("");
            userInfo.setBirthday("");
            userInfo.setSex(1);
            userInfo.setIdcard("");
            userInfo.setEducation("");
            userInfo.setProvince("");
            userInfo.setCity("");
            userInfo.setArea("");
            userInfo.setAddress("");
            userInfo.setIntro("");
            userInfo.setInterest("");
            userInfo.setUpdateTime((int) System.currentTimeMillis() / 1000);
            userInfo.setParentId(0);
            userInfo.setTruenameIsapprove(0);
            userInfo.setEmailIsapprove(0);
            userInfo.setPromoteway(0);
            userInfo.setIsContact(false);
            System.out.println("saveRegistUser***********************************预插入userInfo：" + JSON.toJSONString(userInfo));
            usersInfoMapper.insertSelective(userInfo);
            // 写入用户账户表
            {
                Account account = new Account();
                account.setUserId(userId);
                account.setTotal(BigDecimal.ZERO);
                account.setIncome(BigDecimal.ZERO);
                account.setExpend(BigDecimal.ZERO);
                account.setBalance(BigDecimal.ZERO);
                account.setBalanceCash(BigDecimal.ZERO);
                account.setBalanceFrost(BigDecimal.ZERO);
                account.setFrost(BigDecimal.ZERO);
                account.setAwait(BigDecimal.ZERO);
                account.setRepay(BigDecimal.ZERO);
                account.setFrostCash(BigDecimal.ZERO);
                account.setRecMoney(BigDecimal.ZERO);
                account.setFee(BigDecimal.ZERO);
                account.setInMoney(BigDecimal.ZERO);
                account.setInMoneyFlag(0);
                // 注册时为account表增加 planTotal,planBalance,planFrost,planAccountWait,planCapitalWait,planInterestWait,planRepayInterest默认值
                account.setPlanAccedeTotal(BigDecimal.ZERO);
                account.setPlanBalance(BigDecimal.ZERO);
                account.setPlanFrost(BigDecimal.ZERO);
                account.setPlanAccountWait(BigDecimal.ZERO);
                account.setPlanCapitalWait(BigDecimal.ZERO);
                account.setPlanInterestWait(BigDecimal.ZERO);
                account.setPlanRepayInterest(BigDecimal.ZERO);
                account.setVersion(BigDecimal.ZERO);
                System.out.println("saveRegistUser***********************************预插入account：" + JSON.toJSONString(account));
                accountMapper.insertSelective(account);
            }
            if (recommends != null && recommends.size() == 1) {
                int referer = recommends.get(0).getUserId();
                SpreadsUsers spreadUser = new SpreadsUsers();
                spreadUser.setUserId(userId);
                spreadUser.setSpreadsUserid(referer);
                spreadUser.setAddip(loginIp);
                spreadUser.setAddtime(String.valueOf(time));
                spreadUser.setType("reg");
                spreadUser.setOpernote("reg");
                spreadUser.setOperation(userId + "");
                System.out.println("saveRegistUser***********************************预插入spreadUser：" + JSON.toJSONString(spreadUser));
                spreadsUsersMapper.insertSelective(spreadUser);
            }

            // String version = request.getParameter("version");
            // if (StringUtils.isNotBlank(version)) {
            // String[] shuzu = version.split("\\.");
            // if (shuzu.length >= 4) {
            // System.out.println(shuzu[3]);
            // try {
            // Integer utmplatSourceId = Integer.parseInt(shuzu[3]);// sourceid
            // // 查询推广渠道
            // UtmPlatExample example = new UtmPlatExample();
            // example.createCriteria().andSourceIdEqualTo(utmplatSourceId);
            // List<UtmPlat> utmPlatList =
            // utmPlatMapper.selectByExample(example);
            // if (utmPlatList != null && utmPlatList.size() > 0) {
            // UtmPlat plat = utmPlatList.get(0);
            // AppChannelStatisticsDetail detail = new
            // AppChannelStatisticsDetail();
            // detail.setSourceId(utmplatSourceId);
            // detail.setSourceName(plat.getSourceName() != null ?
            // plat.getSourceName() : "");
            // detail.setUserId(userId);
            // detail.setUserName(userName);
            // detail.setRegisterTime(new Date());
            // detail.setCumulativeInvest(BigDecimal.ZERO);
            // appChannelStatisticsDetailMapper.insert(detail);
            // }
            // } catch (Exception e) {
            // // e.printStackTrace();
            // }
            // //
            // }
            // }
            // 插入utmReg表
            if (request.getSession().getAttribute("utm_id") != null && StringUtils.isNotEmpty(request.getSession().getAttribute("utm_id").toString())) {
                if (Validator.isNumber(request.getSession().getAttribute("utm_id").toString())) {
                    // 从session中取
                    UtmReg utmReg = new UtmReg();
                    utmReg.setCreateTime(GetDate.getNowTime10());
                    utmReg.setUtmId(Integer.parseInt(request.getSession().getAttribute("utm_id").toString()));
                    utmReg.setUserId(userId);
                    utmReg.setOpenAccount(0);
                    utmReg.setBindCard(0);
                    saveUtmReg(utmReg);
                    System.out.println("updateRegistUser***********************************预插入utmReg：" + JSON.toJSONString(utmReg));
                }
            } else if (StringUtils.isNotEmpty(request.getParameter("utm_id"))) {
                if (Validator.isNumber(request.getParameter("utm_id"))) {
                    // 从request中取
                    UtmReg utmReg = new UtmReg();
                    utmReg.setCreateTime(GetDate.getNowTime10());
                    utmReg.setUtmId(Integer.parseInt(request.getParameter("utm_id")));
                    utmReg.setUserId(userId);
                    utmReg.setOpenAccount(0);
                    utmReg.setBindCard(0);
                    saveUtmReg(utmReg);
                    System.out.println("updateRegistUser***********************************预插入utmReg：" + JSON.toJSONString(utmReg));
                }
            }
            // 保存用户注册日志
            {
                UsersLog userLog = new UsersLog();
                userLog.setUserId(userId);
                userLog.setIp(loginIp);
                userLog.setEvent("register");
                userLog.setContent("注册成功");
                System.out.println("saveRegistUser***********************************预插入userLog：" + JSON.toJSONString(userLog));
                usersLogMapper.insertSelective(userLog);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            LogUtil.errorLog(UserRegistServiceImpl.class.getName(), "saveRegistUser", e);
            return 0;
        }
        return userId;
    }
    
    public int saveUtmReg(UtmReg utmReg) {
        return utmRegMapper.insertSelective(utmReg);
    }
    
    @Override
    public boolean existUser(String userName) {
        UsersExample example1 = new UsersExample();
        UsersExample example2 = new UsersExample();
        example1.createCriteria().andUsernameEqualTo(userName);
        Criteria c2 = example2.createCriteria().andMobileEqualTo(userName);
        example1.or(c2);
        int size = usersMapper.countByExample(example1);
        if (size > 0) {
            return true;
        } else {
            return false;
        }
    }



}
