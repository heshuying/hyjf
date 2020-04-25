/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.user.portrait;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.IdCardUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author ${yaoy}
 * @version OntimeUserPortraitServiceImpl, v0.1 2018/5/9 11:57
 */
@Service
public class OntimeUserPortraitServiceImpl extends BaseServiceImpl implements OntimeUserPortraitService {

    @Override
    public List<UsersPortrait> selcetUserList() {
        UsersPortrait usersPortrait = null;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = GetDate.date_sdf.format(cal.getTime());
        String yesterdayBegin = yesterday + " 00:00:00";
        String yesterdayEnd = yesterday + " 23:59:59";
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();
        criteria.andLoginTimeBetween(GetDate.strYYYYMMDDHHMMSS2Timestamp2(yesterdayBegin), GetDate.strYYYYMMDDHHMMSS2Timestamp2(yesterdayEnd));
        List<Users> users = usersMapper.selectByExample(example);
        List<UsersPortrait> list = new ArrayList<>();

        Map<String, Object> map = PropUtils.getJson();

        if (!CollectionUtils.isEmpty(users)) {
            try {
                for (Users user : users) {
                    usersPortrait = new UsersPortrait();
                    Integer userId = user.getUserId();
                    String userName = user.getUsername();
                    String mobile = user.getMobile();
                    //累计收益
                    BigDecimal interestSum = borrowCustomizeMapper.getInterestSum(userId);
                    if (interestSum == null) {
                        interestSum = new BigDecimal(0.00);
                    }

                    //散标累计年化出借金额
                    BigDecimal investSum = borrowCustomizeMapper.getInvestSum(userId);
                    if (investSum == null) {
                        investSum = new BigDecimal(0.00);
                    }
                    //计划累计年化出借金额
                    BigDecimal planSum = borrowCustomizeMapper.getPlanSum(userId);
                    //累计充值金额
                    BigDecimal rechargeSum = borrowCustomizeMapper.getRechargeSum(userId);
                    if (rechargeSum == null) {
                        rechargeSum = new BigDecimal(0.00);
                    }
                    //累计提现金额
                    BigDecimal withdrawSum = borrowCustomizeMapper.getWithdrawSum(userId);
                    if (withdrawSum == null) {
                        usersPortrait.setWithdrawSum(new BigDecimal(0.00));
                    }else {
                        usersPortrait.setWithdrawSum(withdrawSum);
                    }
                    //交易笔数
                    int tradeNumber = borrowCustomizeMapper.getTradeNumber(userId);

                    //出借进程
                    int tenderRecord = borrowCustomizeMapper.countInvest(userId);
                    AccountRechargeExample accountRechargeExample = new AccountRechargeExample();
                    AccountRechargeExample.Criteria criteria1 = accountRechargeExample.createCriteria();
                    criteria1.andUserIdEqualTo(userId).andStatusEqualTo(2);
                    int count1 = accountRechargeMapper.countByExample(accountRechargeExample);
                    BankCardExample bankCardExample = new BankCardExample();
                    BankCardExample.Criteria criteria2 = bankCardExample.createCriteria();
                    criteria2.andUserIdEqualTo(userId);
                    int count2 = bankCardMapper.countByExample(bankCardExample);
                    if (tenderRecord > 0) {
                        usersPortrait.setInvestProcess("出借");
                    } else if (count1 > 0) {
                        usersPortrait.setInvestProcess("充值");
                    } else if (count2 > 0) {
                        usersPortrait.setInvestProcess("开户");
                    } else {
                        usersPortrait.setInvestProcess("注册");
                    }

                    UsersInfoExample usersInfoExample = new UsersInfoExample();
                    usersInfoExample.createCriteria().andUserIdEqualTo(userId);
                    List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(usersInfoExample);
                    if (usersInfoList != null && usersInfoList.size() > 0) {
                        UsersInfo usersInfo = usersInfoList.get(0);
                        //性别
                        if (usersInfo != null && usersInfo.getSex() != null) {
                            if (usersInfo.getSex() == 1) {
                                usersPortrait.setSex("男");
                            } else if (usersInfo.getSex() == 2){
                                usersPortrait.setSex("女");
                            } else {
                                usersPortrait.setSex("未知");
                            }
                        }
                        //年龄
                        if (usersInfo != null && StringUtils.isNotBlank(usersInfo.getIdcard())) {
                            try {
                                String idcard = usersInfo.getIdcard();
                                SimpleDateFormat sdf = GetDate.date_sdf;
                                Boolean isIdCard = IdCardUtil.isValid(idcard);
                                if (isIdCard) {
                                    if (idcard.length() == 15) {
                                        idcard = IdCardUtil.id15To18(idcard);
                                    }
                                    String birthday = idcard.substring(6, 10) + "-" + idcard.substring(10, 12) + "-" + idcard.substring(12, 14);
                                    String age = GetDate.getAge(GetDate.str2Date(birthday, sdf));
                                    usersPortrait.setAge(Integer.valueOf(age));
                                } else {
                                    usersPortrait.setAge(null);
                                }

                                //城市
                                if (isIdCard) {
                                    Object area = map.get(idcard.substring(0, 4));
                                    if (area != null) {
                                        usersPortrait.setCity(String.valueOf(area));
                                    } else {
                                        usersPortrait.setCity("");
                                    }
                                } else {
                                    usersPortrait.setCity("");
                                }

                                //是否有主单
                                if (usersInfo.getAttribute() != null) {
                                        usersPortrait.setAttribute(usersInfo.getAttribute());
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }

                    //最后一次登录时间
                    UsersExample usersExample = new UsersExample();
                    usersExample.createCriteria().andUserIdEqualTo(userId);
                    List<Users> usersList = this.usersMapper.selectByExample(usersExample);
                    if (usersList != null && usersList.size() > 0) {
                        Users users1 = usersList.get(0);
                        if (users1 != null) {
                            int lastLoginTime = users1.getLoginTime();
                            usersPortrait.setLastLoginTime(lastLoginTime);
                        }
                    }

                    //最后一次取现时间
                    AccountwithdrawExample accountwithdrawExample = new AccountwithdrawExample();
                    accountwithdrawExample.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(2);
                    accountwithdrawExample.setOrderByClause("addtime desc");
                    List<Accountwithdraw> accountwithdraws = accountwithdrawMapper.selectByExample(accountwithdrawExample);
                    if (!CollectionUtils.isEmpty(accountwithdraws)) {
                        int addTime = Integer.parseInt(accountwithdraws.get(0).getAddtime());
                        usersPortrait.setLastWithdrawTime(addTime);
                    }

                    //最后一次充值时间
                    AccountRechargeExample accountRechargeExample1 = new AccountRechargeExample();
                    accountRechargeExample1.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(2);
                    accountRechargeExample1.setOrderByClause("addtime desc");
                    List<AccountRecharge> accountRecharges = accountRechargeMapper.selectByExample(accountRechargeExample1);
                    if (!CollectionUtils.isEmpty(accountRecharges)) {
                        int createTime = accountRecharges.get(0).getCreateTime();
                        usersPortrait.setLastRechargeTime(createTime);
                    }

                    //最后一笔回款时间
                    BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
                    borrowRecoverExample.createCriteria().andUserIdEqualTo(userId);
                    borrowRecoverExample.setOrderByClause("addtime desc");
                    List<BorrowRecover> borrowRecovers = borrowRecoverMapper.selectByExample(borrowRecoverExample);
                    if (!CollectionUtils.isEmpty(borrowRecovers)) {
                        int repayLastTime = Integer.parseInt(borrowRecovers.get(0).getRecoverTime());
                        usersPortrait.setLastRepayTime(repayLastTime);
                    }


                    //账户余额
                    AccountExample accountExample = new AccountExample();
                    AccountExample.Criteria criteria3 = accountExample.createCriteria();
                    criteria3.andUserIdEqualTo(userId);
                    List<Account> accounts = accountMapper.selectByExample(accountExample);
                    Account account = accounts.get(0);
                    BigDecimal bankTotal = account.getBankTotal();
                    BigDecimal bankInterestSum = account.getBankInterestSum();
                    usersPortrait.setBankTotal(bankTotal);

                    //账户可用余额
                    BigDecimal bankBalance = account.getBankBalance();
                    usersPortrait.setBankBalance(bankBalance);

                    //账户待还金额
                    BigDecimal bankAwait = account.getBankAwait();
                    BigDecimal planAccountWait = account.getPlanAccountWait();
                    usersPortrait.setAccountAwait(bankAwait.add(planAccountWait));

                    //账户冻结金额
                    BigDecimal bankFrost = account.getBankFrost();
                    usersPortrait.setBankFrost(bankFrost);

                    //资金存留比
                    BigDecimal balance = new BigDecimal(0);
                    AccountRechargeExample rechargeExample = new AccountRechargeExample();
                    AccountRechargeExample.Criteria criteria4 = rechargeExample.createCriteria();
                    criteria4.andUserIdEqualTo(userId).andStatusEqualTo(2);
                    List<AccountRecharge> recharges = accountRechargeMapper.selectByExample(rechargeExample);
                    if (!CollectionUtils.isEmpty(recharges)) {
                        for (AccountRecharge accountRecharge : recharges) {
                            balance = balance.add(accountRecharge.getBalance());
                        }
                    }
                    if (bankInterestSum != null && balance != null && bankInterestSum.add(balance).compareTo(new BigDecimal(0)) > 0 ) {
                        BigDecimal fundRetention = (account.getBankBalance().divide(bankInterestSum.add(balance), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                        usersPortrait.setFundRetention(fundRetention);
                    } else {
                        usersPortrait.setFundRetention(new BigDecimal(0));
                    }


                    //邀约客户注册数
                    UsersExample usersExample1 = new UsersExample();
                    usersExample1.createCriteria().andReferrerEqualTo(userId);
                    int count = usersMapper.countByExample(usersExample1);
                    List<Users> users1 = usersMapper.selectByExample(usersExample1);
                    usersPortrait.setInviteRegist(count);

                    //邀约充值客户数
                    int rechargeCount = 0;
                    int tenderCount = 0;
                    for (Users user1 : users1) {
                        AccountRechargeExample accountRechargeExample2 = new AccountRechargeExample();
                        accountRechargeExample2.createCriteria().andUserIdEqualTo(user1.getUserId()).andStatusEqualTo(2);

                        BorrowTenderExample borrowTenderExample = new BorrowTenderExample();
                        borrowTenderExample.createCriteria().andUserIdEqualTo(user1.getUserId());
                        int count4 = borrowTenderMapper.countByExample(borrowTenderExample);

                        int count3 = accountRechargeMapper.countByExample(accountRechargeExample2);
                        if (count3 > 0) {
                            rechargeCount++;
                        }
                        if (count4 > 4) {
                            tenderCount++;
                        }
                    }
                    usersPortrait.setInviteRecharge(rechargeCount);
                    usersPortrait.setInviteTender(tenderCount);

                    //客均收益率
                    BigDecimal tenderSum = new BigDecimal(0);
                    BorrowTenderExample borrowTenderExample1 = new BorrowTenderExample();
                    borrowTenderExample1.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(1);
                    List<BorrowTender> borrowTenders = borrowTenderMapper.selectByExample(borrowTenderExample1);
                    for (BorrowTender borrowTender : borrowTenders) {
                        tenderSum = tenderSum.add(borrowTender.getAccount());
                    }
                    if (tenderSum.compareTo(new BigDecimal(0)) > 0) {
                        BigDecimal yield = interestSum.divide(tenderSum, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                        usersPortrait.setYield(yield);
                    } else {
                        usersPortrait.setYield(new BigDecimal(0));
                    }

                    usersPortrait.setUserId(userId);
                    usersPortrait.setUserName(userName);
                    usersPortrait.setMobile(mobile);
                    usersPortrait.setInterestSum(interestSum);
                    usersPortrait.setInvestSum(investSum.add(planSum));
                    usersPortrait.setRechargeSum(rechargeSum);
                    usersPortrait.setTradeNumber(tradeNumber);
                    list.add(usersPortrait);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
        return null;
    }

    @Override
    public int updateImformation(UsersPortrait usersPortrait) {
        int count = usersPortraitMapper.updateByPrimaryKeySelective(usersPortrait);
        return count;
    }

    @Override
    public int insertImformation(UsersPortrait usersPortrait) {
        int count1 = borrowCustomizeMapper.insertUsersPortrait(usersPortrait);
        return count1;
    }

}
