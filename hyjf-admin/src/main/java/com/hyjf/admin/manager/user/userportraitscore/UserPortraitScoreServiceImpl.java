/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.user.userportraitscore;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.admin.UserPortraitScoreCustomize;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yaoyong
 * @version UserPortraitScoreServiceImpl, v0.1 2018/7/9 17:48
 */
@Service
public class UserPortraitScoreServiceImpl extends BaseServiceImpl implements UserPortraitScoreService {

    @Override
    public int countRecordTotal(Map<String, Object> userPortraitScore) {
        int countTotal = userInfoCustomizeMapper.countRecordTotal(userPortraitScore);
        return countTotal;
    }

    @Override
    public List<UserPortraitScoreCustomize> getRecordList(Map<String, Object> userPortrait, int limitStart, int limitEnd) {
        List<UserPortraitScoreCustomize> list = new ArrayList<>();

        String redisKey = RedisConstants.USERPORTRAIT_SCORE;
        if (limitStart == 0 || limitStart > 0) {
            userPortrait.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            userPortrait.put("limitEnd", limitEnd);
        }

        Jedis jedis = getJedis(redisKey);

        List<UsersPortrait> usersPortraits = userInfoCustomizeMapper.selectUserPortraitList(userPortrait);
        if (!CollectionUtils.isEmpty(usersPortraits)) {
            try {
                for (UsersPortrait usersPortrait : usersPortraits) {
                    UserPortraitScoreCustomize customize = new UserPortraitScoreCustomize();
                    //性别&年龄评分
                    String sex = usersPortrait.getSex();
                    String age = String.valueOf(usersPortrait.getAge());
                    if (usersPortrait.getAge() != null && usersPortrait.getAge() > 100) {
                        age = "100";
                    }
                    if (usersPortrait.getSex() != null && usersPortrait.getAge() != null) {
                        sex = usersPortrait.getSex().equals("男") ? "MAN" : "WOMAN";
                        String sexAge = jedis.hget(redisKey, sex + age);
                        customize.setSexAge(sexAge);
                    } else {
                        customize.setSexAge("0");
                    }

                    String nameClass = null;
                    String nameCd = null;

                    //资金量评分
                    double funds = 0;
                    if (usersPortrait.getInvestSum().compareTo(new BigDecimal(50000)) > 0) {
                        funds = 100;
                        customize.setFunds(funds);
                    } else {
                        BigDecimal fund = usersPortrait.getInvestSum().divide(new BigDecimal(50000), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                        customize.setFunds(fund.doubleValue());
                    }

                    //累计收益评分
                    if (usersPortrait.getInterestSum() != null) {
                        if (usersPortrait.getInterestSum().compareTo(new BigDecimal(1000)) >= 0) {
                            BigDecimal interest = new BigDecimal(100);
                            customize.setInterest(interest);
                        } else {
                            String interest = String.format("%.2f", Math.pow(usersPortrait.getInterestSum().doubleValue(), 2) / 10000);
                            customize.setInterest(new BigDecimal(interest));
                        }
                    } else {
                        customize.setInterest(new BigDecimal(0));
                    }

                    //交易笔数评分
                    String tradeNumber = null;
                    nameClass = "TRADE_NUMBER";
                    if (usersPortrait.getTradeNumber() != null && usersPortrait.getTradeNumber() >= 5) {
                        nameCd = "5+";
                        tradeNumber = jedis.hget(redisKey, nameClass + nameCd);
                        customize.setTradeNumber(Integer.parseInt(tradeNumber));
                    } else {
                        nameCd = String.valueOf(usersPortrait.getTradeNumber());
                        if (usersPortrait.getTradeNumber() == null) {
                            nameCd = "0";
                        }
                        tradeNumber = jedis.hget(redisKey, nameClass + nameCd);
                        customize.setTradeNumber(Integer.parseInt(tradeNumber));
                    }

                    //客户来源评分
                    SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
                    spreadsUsersExample.createCriteria().andUserIdEqualTo(usersPortrait.getUserId());
                    List<SpreadsUsers> spreadsUsers = spreadsUsersMapper.selectByExample(spreadsUsersExample);
                    if (!CollectionUtils.isEmpty(spreadsUsers) && usersPortrait.getAttribute() == 0) {
                        customize.setCustomerSource(80);
                    } else {
                        customize.setCustomerSource(20);
                    }
                    if (usersPortrait.getAttribute() != null && usersPortrait.getAttribute() == 1) {
                        customize.setCustomerSource(20);
                    }

                    //出借进程评分
                    if (usersPortrait.getInvestProcess() != null) {
                        String investProcess = usersPortrait.getInvestProcess();
                        if (investProcess.equals("注册")) {
                            customize.setInvestProcess("20");
                        } else if (investProcess.equals("开户")) {
                            customize.setInvestProcess("60");
                        } else if (investProcess.equals("充值")) {
                            customize.setInvestProcess("80");
                        } else if (investProcess.equals("出借")) {
                            customize.setInvestProcess("100");
                        }
                    } else {
                        customize.setInvestProcess("0");
                    }

                    //回款活跃评分
                    int nowTime = new Long(System.currentTimeMillis() / 1000).intValue();
                    if (usersPortrait.getLastLoginTime() != null && usersPortrait.getLastRepayTime() != null) {
                        if (usersPortrait.getLastRepayTime() > nowTime - usersPortrait.getLastLoginTime() * (60 * 60 * 24)) {
                            customize.setReturnActive("100");
                        } else {
                            int days = (nowTime - usersPortrait.getLastLoginTime() * (60 * 60 * 24) - usersPortrait.getLastRepayTime()) / (60 * 60 * 24);
                            int returnActive = 0;
                            for (int j = 0; j + days < 100; j++) {
                                returnActive = j;
                            }
                            customize.setReturnActive(String.valueOf(returnActive));
                        }
                    } else {
                        customize.setReturnActive("0");
                    }

                    //登录活跃评分
                    int loginTime = 0;
                    if (usersPortrait.getLastLoginTime() != null) {
                        loginTime = usersPortrait.getLastLoginTime();
                    }
                    if (loginTime == 0) {
                        customize.setLoginActive("100");
                    }
                    if (loginTime >= 1 && loginTime < 7) {
                        customize.setLoginActive("90");
                    }
                    if (loginTime >= 7 && loginTime < 14) {
                        customize.setLoginActive("80");
                    }
                    if (loginTime >= 14 && loginTime < 30) {
                        customize.setLoginActive("60");
                    }
                    if (loginTime >= 30 && loginTime < 60) {
                        customize.setLoginActive("30");
                    }
                    if (loginTime >= 60 && loginTime < 90) {
                        customize.setLoginActive("10");
                    }
                    if (loginTime >= 90) {
                        customize.setLoginActive("0");
                    }


                    //邀约活跃评分
                    String inviteActive = null;
                    int rechargeCount = usersPortrait.getInviteRecharge();
                    nameClass = "INVITE_RECHARGE";
                    if (rechargeCount >= 12) {
                        nameCd = "12+";
                        inviteActive = jedis.hget(redisKey, nameClass + nameCd);
                        customize.setInviteActive(inviteActive);
                    } else {
                        inviteActive = jedis.hget(redisKey, nameClass + String.valueOf(rechargeCount));
                        customize.setInviteActive(inviteActive);
                    }

                    //资金存留比
                    BigDecimal rechargeSum = new BigDecimal(0);
                    AccountRechargeExample rechargeExample = new AccountRechargeExample();
                    AccountRechargeExample.Criteria criteria1 = rechargeExample.createCriteria();
                    criteria1.andUserIdEqualTo(usersPortrait.getUserId());
                    List<AccountRecharge> recharges = accountRechargeMapper.selectByExample(rechargeExample);
                    if (!CollectionUtils.isEmpty(recharges)) {
                        for (AccountRecharge accountRecharge : recharges) {
                            rechargeSum = rechargeSum.add(accountRecharge.getBalance());
                        }
                    }
                    customize.setFundRetentionPercent(usersPortrait.getFundRetention());

                    //大客户判定
                    ArrayList<Double> arrayList = new ArrayList<>();
                    if (sex != null && usersPortrait.getAge() != null) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("sex",usersPortrait.getSex());
                        map.put("age",usersPortrait.getAge());
                        List<BigDecimal> investSums = userInfoCustomizeMapper.selectInvest(map);
                        for (BigDecimal investSum : investSums) {
                            arrayList.add(investSum.doubleValue());
                        }
                        Double[] array = arrayList.toArray(new Double[arrayList.size()]);

                        //计算平均值
                        double dV = Variance(array);
                        //计算标准差
                        double dS = GetAverageandStandardDevition(array);
                        double standardDevition = (double) Math.round(dS * 100) / 100;

                        if (usersPortrait.getInvestSum().doubleValue() > (dV + standardDevition * 2)) {
                            customize.setIsBigCustomer("1");
                        } else {
                            customize.setIsBigCustomer("0");
                        }
                    } else {
                        customize.setIsBigCustomer("0");
                    }


                    //状态标签
                    String loginPeriod;
                    if (loginTime >= 0 && loginTime < 30) {
                        loginPeriod = "0-30";
                    } else if (loginTime >= 30 && loginTime < 90) {
                        loginPeriod = "30-90";
                    } else if (loginTime >= 90 && loginTime < 180) {
                        loginPeriod = "90-180";
                    } else {
                        loginPeriod = "180-∞";
                    }

                    String fundRetentionPeriod;
                    if (usersPortrait.getFundRetention().compareTo(new BigDecimal(0)) >= 0 && usersPortrait.getFundRetention().compareTo(new BigDecimal(25)) < 0) {
                        fundRetentionPeriod = "0-25";
                    } else if (usersPortrait.getFundRetention().compareTo(new BigDecimal(25)) >= 0 && usersPortrait.getFundRetention().compareTo(new BigDecimal(50)) < 0) {
                        fundRetentionPeriod = "25-50";
                    } else if (usersPortrait.getFundRetention().compareTo(new BigDecimal(50)) >= 0 && usersPortrait.getFundRetention().compareTo(new BigDecimal(75)) < 0) {
                        fundRetentionPeriod = "50-75";
                    } else {
                        fundRetentionPeriod = "75-100";
                    }
                    String statusTab = jedis.hget(redisKey, loginPeriod + fundRetentionPeriod);
                    customize.setStatusTab(statusTab);

                    //身份标签
                    if (usersPortrait.getAge() != null) {
                        int age1 = usersPortrait.getAge();
                        if ("1".equals(customize.getIsBigCustomer())) {
                            customize.setIdentityLabel("土豪");
                        } else if (age1 >= 40 && age1 < 65 && "WOMAN".equals(sex) && rechargeCount > 1) {
                            customize.setIdentityLabel("广场舞大妈");
                        } else if (age1 >= 35 && age1 < 58) {
                            customize.setIdentityLabel("普通出借者");
                        } else if (age1 >= 58 && age1 < 80) {
                            customize.setIdentityLabel("老年出借者");
                        } else if (age1 >= 18 && age1 <= 34) {
                            customize.setIdentityLabel("青年出借者");
                        } else if (loginTime > 30 && rechargeSum.compareTo(new BigDecimal(0)) == 0) {
                            customize.setIdentityLabel("任务刷单者");
                        } else {
                            customize.setIdentityLabel("无");
                        }
                    } else {
                        if (loginTime > 30) {
                            customize.setIdentityLabel("任务刷单者");
                        } else {
                            customize.setIdentityLabel("无");
                        }
                    }

                    DecimalFormat df = new DecimalFormat("#.00");
                    //VIP
                    double vip = customize.getFunds() * 0.8 + Double.valueOf(customize.getSexAge()) * 0.2;
                    //信任
                    double trust = customize.getInterest().doubleValue() * 0.6 + Double.valueOf(tradeNumber) * 0.25 + (double) customize.getCustomerSource() * 0.15;
                    //竞争
                    double compete = Double.valueOf(customize.getInvestProcess()) * 0.5 + Double.valueOf(customize.getInviteActive()) * 0.5;
                    //流失
                    double loss = customize.getFundRetentionPercent().multiply(new BigDecimal(0.6)).doubleValue() + Double.valueOf(customize.getReturnActive()) * 0.4;
                    //意向
                    double intention = Double.valueOf(customize.getLoginActive()) * 0.6 + Double.valueOf(customize.getInviteActive()) * 0.4;

                    customize.setVip(Double.valueOf(df.format(vip)));
                    customize.setTrust(Double.valueOf(df.format(trust)));
                    customize.setCompete(Double.valueOf(df.format(compete)));
                    customize.setLoss(Double.valueOf(df.format(loss)));
                    customize.setIntention(Double.valueOf(df.format(intention)));
                    customize.setUserId(usersPortrait.getUserId());
                    customize.setUserName(usersPortrait.getUserName());

                    list.add(customize);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private Jedis getJedis(String redisKey) {

        JedisPool pool = RedisUtils.getPool();
        Jedis jedis = pool.getResource();
        Boolean exists = jedis.exists(redisKey);
        if (!exists) {
            ParamNameExample example = new ParamNameExample();
            ParamNameExample.Criteria criteria = example.createCriteria();
            criteria.andOther1EqualTo("1");
            List<ParamName> list = paramNameMapper.selectByExample(example);
            Map<String, String> map = new HashMap<>();
            for (ParamName paramName : list) {
                String key = paramName.getNameClass() + paramName.getNameCd();
                String value = paramName.getName();
                map.put(key, value);
            }
            jedis.hmset(redisKey, map);
        }
        return jedis;
    }


    /**
     * 计算平均值
     *
     * @param x
     * @return
     */
    public static double Variance(Double[] x) {
        int m = x.length;
        double sum = 0;
        for (int i = 0; i < m; i++) {
            sum += x[i];
        }
        double dAve = sum / m;
//        double dVar = 0;
//        for (int i = 0; i < m; i++) {
//            dVar += (x[i] - dAve) * (x[i] - dAve);
//        }
//        return dVar / m;
        return dAve;
    }

    /**
     * 计算标准差
     *
     * @param x
     * @return
     */
    public static double GetAverageandStandardDevition(Double[] x) {
        int m = x.length;
        double sum = 0;
        for (int i = 0; i < m; i++) {
            sum += x[i];
        }
        double dAve = sum / m;
        double dVar = 0;
        for (int i = 0; i < m; i++) {
            dVar += (x[i] - dAve) * (x[i] - dAve);
        }
        return Math.sqrt(dVar / m);
    }


}
