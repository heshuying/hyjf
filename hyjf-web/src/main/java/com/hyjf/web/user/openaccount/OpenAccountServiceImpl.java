/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:50:02
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.web.user.openaccount;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.IdCard15To18;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecordExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.web.BaseServiceImpl;

@Service
public class OpenAccountServiceImpl extends BaseServiceImpl implements OpenAccountService {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    /**
     * 保存开户的数据
     */
    @Override
    public boolean userOpenAccount(ChinapnrBean bean) {

        // 手动控制事务
        TransactionStatus txStatus = null;
        // 注册人ID
        Integer userId = bean.getLogUserId();
        System.out.println("saveOpenAccount----------------  userId:" + userId);
        // 判断是否存在
        UsersExample uexample = new UsersExample();
        UsersExample.Criteria crtu = uexample.createCriteria();
        crtu.andUserIdEqualTo(userId);
        List<Users> users = usersMapper.selectByExample(uexample);
        if (users != null && users.size() == 1) {
            // 更新用户表
            Users user = users.get(0);
            if (user.getOpenAccount() == 1) {
                return true;
            } else {
                try {
                    // 开启事务
                    txStatus = this.transactionManager.getTransaction(transactionDefinition);
                    // 当前时间
                    int nowTime = GetDate.getNowTime10();
                    // 身份证号
                    String idCard = bean.getIdNo();
                    System.out.println("身份证号码:" + idCard);
                    // 真实姓名
                    String trueName = null;
                    try {
                        trueName = URLDecoder.decode(bean.getUsrName(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (idCard != null && idCard.length() < 18) {
                        try {
                            idCard = IdCard15To18.getEighteenIDCard(idCard);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // 性别
                    int sexInt = Integer.parseInt(idCard.substring(16, 17));
                    if (sexInt % 2 == 0) {
                        sexInt = 2;
                    } else {
                        sexInt = 1;
                    }
                    // 出生日期
                    String birthDayTemp = idCard.substring(6, 14);
                    String birthDay =
                            StringUtils.substring(birthDayTemp, 0, 4) + "-" + StringUtils.substring(birthDayTemp, 4, 6)
                                    + "-" + StringUtils.substring(birthDayTemp, 6, 8);
                    // 更新用户表
                    boolean usersFlag = false;
                    user.setOpenAccount(1); // 已经开户
                    // 更新手机号
                    user.setMobile(bean.getUsrMp());
                    user.setAccountEsb(Integer.valueOf(bean.getLogClient()));
                    user.setRechargeSms(0);
                    user.setWithdrawSms(0);
                    user.setUserType(0);
                    crtu.andVersionEqualTo(user.getVersion());
                    user.setVersion(user.getVersion().add(new BigDecimal("1")));
                    usersFlag = usersMapper.updateByPrimaryKeySelective(user) > 0 ? true : false;
                    if (usersFlag) {
                        // 更新用户详细信息表
                        UsersInfoExample uiexample = new UsersInfoExample();
                        UsersInfoExample.Criteria crtui = uiexample.createCriteria();
                        crtui.andUserIdEqualTo(userId);
                        List<UsersInfo> userInfos = usersInfoMapper.selectByExample(uiexample);
                        if (userInfos != null && userInfos.size() == 1) {
                            boolean userInfoFlag = false;
                            UsersInfo usersInfo = userInfos.get(0);
                            usersInfo.setTruename(trueName);
                            usersInfo.setIdcard(idCard);
                            usersInfo.setSex(sexInt);
                            usersInfo.setBirthday(birthDay);
                            usersInfo.setTruenameIsapprove(1);
                            usersInfo.setMobileIsapprove(1);
                            userInfoFlag = usersInfoMapper.updateByPrimaryKeySelective(usersInfo) > 0 ? true : false;
                            if (userInfoFlag) {
                                boolean accountChinapnrFlag = false;
                                // 插入汇付关联表
                                AccountChinapnr accountChinapnr = new AccountChinapnr();
                                accountChinapnr.setUserId(userId);
                                accountChinapnr.setChinapnrUsrid(bean.getUsrId());
                                accountChinapnr.setChinapnrUsrcustid(Long.valueOf(bean.getUsrCustId()));
                                accountChinapnr.setAddtime(String.valueOf(nowTime));
                                accountChinapnr.setAddip(bean.getLogIp());
                                accountChinapnr.setIsok(1);
                                accountChinapnr.setEggsIsok(0);
                                accountChinapnrFlag =
                                        this.accountChinapnrMapper.insertSelective(accountChinapnr) > 0 ? true : false;
                                if (accountChinapnrFlag) {
                                    // 开户更新开户渠道统计开户时间
                                    AppChannelStatisticsDetailExample example = new AppChannelStatisticsDetailExample();
                                    AppChannelStatisticsDetailExample.Criteria crt = example.createCriteria();
                                    crt.andUserIdEqualTo(userId);
                                    List<AppChannelStatisticsDetail> appChannelStatisticsDetails =
                                            this.appChannelStatisticsDetailMapper.selectByExample(example);
                                    if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
                                        AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
                                        channelDetail.setOpenAccountTime(new Date());
                                        this.appChannelStatisticsDetailMapper
                                                .updateByPrimaryKeySelective(channelDetail);
                                    }
                                    // 提交事务
                                    this.transactionManager.commit(txStatus);
                                    return true;

                                } else {
                                    throw new RuntimeException("插入用户开户表失败！");
                                }
                            } else {
                                throw new RuntimeException("更新用户详情表失败！");
                            }
                        } else {
                            throw new RuntimeException("用户详情表数据错误，用户id：" + user.getUserId());
                        }
                    } else {
                        throw new RuntimeException("更新用户表失败！");
                    }
                } catch (Exception e) {
                    // 回滚事务
                    this.transactionManager.rollback(txStatus);
                    return false;
                }
            }
        } else {
            throw new RuntimeException("未查询到用户数据，用户id：" + userId);
        }
    }

    @Override
    public Users selectUserById(int userId) {
        Users user = this.usersMapper.selectByPrimaryKey(userId);
        return user;
    }

    @Override
    public String getUsersMobile(Integer userId) {
        Users user = this.usersMapper.selectByPrimaryKey(userId);
        if (user != null) {
            return user.getMobile();
        }
        return null;
    }

    @Override
    public JSONObject selectUserByMobile(int userId, String mobile) {

        JSONObject ret = new JSONObject();
        UsersExample userExample = new UsersExample();
        UsersExample.Criteria crt = userExample.createCriteria();
        crt.andMobileEqualTo(mobile);
        List<Users> users = this.usersMapper.selectByExample(userExample);
        if (users != null && users.size() == 1) {
            return null;
        } else if (users.size() > 0) {
            for (Users user : users) {
                if (user.getUserId() == userId) {
                    ret.put("newUserName", user.getUsername());
                } else {
                    String oldUserName = "";
                    if (StringUtils.isNotBlank(ret.getString("oldUserName"))) {
                        oldUserName = ret.getString("oldUserName") + "," + user.getUsername();
                        ret.put("oldUserName", oldUserName);
                    } else {
                        oldUserName = user.getUsername();
                        ret.put("oldUserName", oldUserName);
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public int insertCorpOpenAccountRecord(int userId, String username, String busiCode, String guarType) {

        Date date = new Date();
        CorpOpenAccountRecordExample corpOpenAccountExample = new CorpOpenAccountRecordExample();
        CorpOpenAccountRecordExample.Criteria corpCrt1 = corpOpenAccountExample.createCriteria();
        corpCrt1.andBusiCodeEqualTo(busiCode);
        CorpOpenAccountRecordExample.Criteria corpCrt2 = corpOpenAccountExample.createCriteria();
        corpCrt2.andUserIdEqualTo(userId);
        corpOpenAccountExample.or(corpCrt2);
        List<CorpOpenAccountRecord> CorpOpenAccountRecords =
                this.corpOpenAccountRecordMapper.selectByExample(corpOpenAccountExample);
        if (CorpOpenAccountRecords != null && CorpOpenAccountRecords.size() > 0) {
            if (CorpOpenAccountRecords.size() == 1) {
                CorpOpenAccountRecord corpOpenAccountRecord = CorpOpenAccountRecords.get(0);
                if (corpOpenAccountRecord.getUserId() == userId && corpOpenAccountRecord.getBusiCode().equals(busiCode)) {
                    corpOpenAccountRecord.setStatus(0);
                    corpOpenAccountRecord.setUpdateTime(date);
                    corpOpenAccountRecord.setGuarType(guarType);
                    return this.corpOpenAccountRecordMapper.updateByPrimaryKeySelective(corpOpenAccountRecord);
                } else {
                    return 2;
                }
            } else {
                return 3;
            }
        } else {
            CorpOpenAccountRecord corpOpenAccount = new CorpOpenAccountRecord();
            corpOpenAccount.setUserId(userId);
            corpOpenAccount.setBusiCode(busiCode);
            corpOpenAccount.setGuarType(guarType);
            corpOpenAccount.setUsername(username);
            corpOpenAccount.setStatus(0);
            corpOpenAccount.setAddTime(date);
            return this.corpOpenAccountRecordMapper.insertSelective(corpOpenAccount);
        }
    }

    /**
     * 保存开户的数据
     */
    @Override
    public boolean corpOpenAccount(ChinapnrBean bean) {
        // 手动控制事务
        TransactionStatus txStatus = null;
        // 系统是将
        Date date = new Date();
        // 注册人ID
        Integer userId = bean.getLogUserId();
        System.out.println("saveOpenAccount----------------  userId:" + userId);
        int status = 7;
        String corpStatus = bean.getAuditStat();
        if (StringUtils.isNotBlank(corpStatus)) {
            switch (corpStatus) {
            case "I":// 初始
                status = 0;
                break;
            case "T":// 提交
                status = 1;
                break;
            case "P":
                status = 2;
                break;
            case "R":
                status = 3;
                break;
            case "F":
                status = 4;
                break;
            case "K":
                status = 5;
                break;
            case "Y":
                status = 6;
                break;
            default:
                status = 7;
            }
        } else {
            status = 7;
        }
        // 只有用户返回的是成功
        if (status == 6) {
            // 判断是否存在
            UsersExample uexample = new UsersExample();
            UsersExample.Criteria crtu = uexample.createCriteria();
            crtu.andUserIdEqualTo(userId);
            List<Users> users = usersMapper.selectByExample(uexample);
            if (users != null && users.size() == 1) {
                // 更新用户表
                Users user = users.get(0);
                if (user.getOpenAccount() == 1) {
                    return true;
                } else {
                    try {
                        // 开启事务
                        txStatus = this.transactionManager.getTransaction(transactionDefinition);
                        // 当前时间
                        int nowTime = GetDate.getNowTime10();
                        // 真实姓名
                        String trueName = null;
                        try {
                            trueName = URLDecoder.decode(bean.getUsrName(), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // 更新用户表
                        user.setOpenAccount(1); // 已经开户
                        user.setMobile(bean.getUsrMp()); // 更新手机号
                        user.setAccountEsb(Integer.valueOf(bean.getLogClient()));
                        user.setRechargeSms(0);
                        user.setWithdrawSms(0);
                        user.setUserType(1);
                        // 更新用户详细信息表
                        UsersInfoExample uiexample = new UsersInfoExample();
                        UsersInfoExample.Criteria crtui = uiexample.createCriteria();
                        crtui.andUserIdEqualTo(userId);
                        List<UsersInfo> userInfos = usersInfoMapper.selectByExample(uiexample);
                        if (userInfos != null && userInfos.size() == 1) {
                            boolean userInfoFlag = false;
                            UsersInfo usersInfo = userInfos.get(0);
                            // 查询用户开户是否选择了担保类型
                            CorpOpenAccountRecordExample accountRecordExample = new CorpOpenAccountRecordExample();
                            accountRecordExample.createCriteria().andUserIdEqualTo(userId);
                            List<CorpOpenAccountRecord> corpOpenAccountRecords =
                                    this.corpOpenAccountRecordMapper.selectByExample(accountRecordExample);
                            if (null != corpOpenAccountRecords && corpOpenAccountRecords.size() == 1) {
                                if ("Y".equals(corpOpenAccountRecords.get(0).getGuarType())) {
                                    usersInfo.setRoleId(3);
                                }
                            }
                            usersInfo.setTruename(trueName);
                            usersInfo.setTruenameIsapprove(1);
                            usersInfo.setMobileIsapprove(1);
                            userInfoFlag = usersInfoMapper.updateByPrimaryKeySelective(usersInfo) > 0 ? true : false;
                            if (userInfoFlag) {
                                boolean accountChinapnrFlag = false;
                                // 插入汇付关联表
                                AccountChinapnr accountChinapnr = new AccountChinapnr();
                                accountChinapnr.setUserId(userId);
                                accountChinapnr.setChinapnrUsrid(bean.getUsrId());
                                accountChinapnr.setChinapnrUsrcustid(Long.valueOf(bean.getUsrCustId()));
                                accountChinapnr.setAddtime(String.valueOf(nowTime));
                                accountChinapnr.setAddip(bean.getLogIp());
                                accountChinapnr.setIsok(1);
                                accountChinapnr.setEggsIsok(0);
                                accountChinapnrFlag =
                                        this.accountChinapnrMapper.insertSelective(accountChinapnr) > 0 ? true : false;
                                if (accountChinapnrFlag) {
                                    boolean usersFlag = false;
                                    crtu.andVersionEqualTo(user.getVersion());
                                    user.setVersion(user.getVersion().add(new BigDecimal("1")));
                                    usersFlag = usersMapper.updateByExampleSelective(user, uexample) > 0 ? true : false;
                                    if (usersFlag) {
                                        CorpOpenAccountRecordExample corpOpenAccountExample =
                                                new CorpOpenAccountRecordExample();
                                        CorpOpenAccountRecordExample.Criteria corpCrt =
                                                corpOpenAccountExample.createCriteria();
                                        corpCrt.andUserIdEqualTo(userId);
                                        List<CorpOpenAccountRecord> CorpOpenAccountRecords =
                                                this.corpOpenAccountRecordMapper
                                                        .selectByExample(corpOpenAccountExample);
                                        if (CorpOpenAccountRecords != null && CorpOpenAccountRecords.size() == 1) {
                                            CorpOpenAccountRecord corpOpenAccountRecord = CorpOpenAccountRecords.get(0);
                                            corpOpenAccountRecord.setStatus(status);
                                            if (StringUtils.isNotBlank(bean.getOpenBankId())) {
                                                corpOpenAccountRecord.setOpenBankId(bean.getOpenBankId());
                                            }
                                            if (StringUtils.isNotBlank(bean.getCardId())) {
                                                corpOpenAccountRecord.setCardId(bean.getCardId());
                                            }
                                            corpOpenAccountRecord.setUpdateTime(date);
                                            boolean corpFlag =
                                                    this.corpOpenAccountRecordMapper
                                                            .updateByPrimaryKeySelective(corpOpenAccountRecord) > 0 ? true
                                                            : false;
                                            if (corpFlag) {
                                                // 开户更新开户渠道统计开户时间
                                                AppChannelStatisticsDetailExample example =
                                                        new AppChannelStatisticsDetailExample();
                                                AppChannelStatisticsDetailExample.Criteria crt =
                                                        example.createCriteria();
                                                crt.andUserIdEqualTo(userId);
                                                List<AppChannelStatisticsDetail> appChannelStatisticsDetails =
                                                        this.appChannelStatisticsDetailMapper.selectByExample(example);
                                                if (appChannelStatisticsDetails != null
                                                        && appChannelStatisticsDetails.size() == 1) {
                                                    AppChannelStatisticsDetail channelDetail =
                                                            appChannelStatisticsDetails.get(0);
                                                    channelDetail.setOpenAccountTime(new Date());
                                                    this.appChannelStatisticsDetailMapper
                                                            .updateByPrimaryKeySelective(channelDetail);
                                                }
                                                // 添加企业账号普通提现卡
                                                AccountBank bank = new AccountBank();
                                                bank.setUserId(userId);
                                                bank.setAccount(bean.getCardId());
                                                bank.setStatus(0);
                                                bank.setBank(bean.getOpenBankId());
                                                bank.setCardType("0");// 普通提现卡
                                                bank.setAddtime(String.valueOf(GetDate.getNowTime10()));
                                                bank.setAddip(bean.getLogIp());
                                                bank.setUpdateIp(bean.getLogIp());
                                                bank.setUpdateTime(String.valueOf(GetDate.getNowTime10()));
                                                bank.setProvince("0");
                                                bank.setCity("0");
                                                bank.setArea(0);
                                                this.accountBankMapper.insertSelective(bank);
                                                // 提交事务
                                                this.transactionManager.commit(txStatus);
                                                return true;
                                            } else {
                                                throw new RuntimeException("企业开户状态表更新失败！");
                                            }
                                        } else {
                                            throw new RuntimeException("企业开户信息查询失败！");
                                        }
                                    } else {
                                        throw new RuntimeException("更新用户表失败！");
                                    }
                                } else {
                                    throw new RuntimeException("插入用户开户表失败！");
                                }
                            } else {
                                throw new RuntimeException("更新用户详情表失败！");
                            }
                        } else {
                            throw new RuntimeException("用户详情数据查询错误，用户id：" + user.getUserId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 回滚事务
                        this.transactionManager.rollback(txStatus);
                        Users userNew = usersMapper.selectByPrimaryKey(user.getUserId());
                        if (userNew.getOpenAccount() == 1) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                throw new RuntimeException("未查询到用户数据，用户id：" + userId);
            }
        } else {
            CorpOpenAccountRecord openRecord = this.getCorpOpenAccountRecordByUserId(userId);
            if (openRecord != null) {
                openRecord.setBusiCode(bean.getBusiCode());
                openRecord.setStatus(status);
                openRecord.setUpdateTime(date);
                openRecord.setCardId(bean.getCardId());
                openRecord.setOpenBankId(bean.getOpenBankId());
                if (status == 3 || status == 4) {
                    try {
                        String mm = URLDecoder.decode(bean.getRespDesc(), "UTF-8");
                        openRecord.setRemark(mm);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    openRecord.setRemark(bean.getRespDesc());
                }
                boolean corpFlag =
                        this.corpOpenAccountRecordMapper.updateByPrimaryKeySelective(openRecord) > 0 ? true : false;
                if (!corpFlag) {
                    throw new RuntimeException("企业开户状态表更新失败！");
                } else {
                    return true;
                }
            } else {
                throw new RuntimeException("企业开户信息查询失败！");
            }
        }
    }

    @Override
    public boolean checkIfSendCoupon(Users user) {
        // TODO 预留活动id
        String activityId = CustomConstants.OPENACCOUNT_ACTIVITY_ID;
        ActivityList activityList = activityListMapper.selectByPrimaryKey(Integer.parseInt(activityId));
        if (activityList == null) {
            return false;
        }
        Integer timeStart = activityList.getTimeStart();
        Integer timeEnd = activityList.getTimeEnd();
        if (timeStart > GetDate.getNowTime10()) {
            return false;
        }
        if (timeEnd != null && timeEnd < GetDate.getNowTime10()) {
            return false;
        }
        Long time = new Date().getTime() / 1000;
        // 判断推荐人
        SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
        SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
        spreadsUsersExampleCriteria.andUserIdEqualTo(user.getUserId());
        List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
        if (sList != null && sList.size() == 1) {
            int refUserId = sList.get(0).getSpreadsUserid();
            // 判断是否是投之家来的
            if (refUserId != 111734) {
                return false;
            } else {
                // 共用判断是否可以发券开始
                if (user.getRegTime() < timeStart) {
                    return false;
                } else {
                    if (time < timeStart) {
                        return false;
                    } else {
                        return true;
                    }
                }
                // 共用判断是否可以发券结束
            }
        } else {
            return false;
        }
    }

    /**
     * 执行前每个方法前需要添加BusinessDesc描述
     * @param userId
     * @return
     * @author Michael
     */

    @Override
    public CorpOpenAccountRecord getCorpOpenAccountRecordByUserId(int userId) {
        CorpOpenAccountRecordExample corpOpenAccountExample = new CorpOpenAccountRecordExample();
        CorpOpenAccountRecordExample.Criteria corpCrt = corpOpenAccountExample.createCriteria();
        corpCrt.andUserIdEqualTo(userId);
        List<CorpOpenAccountRecord> corpOpenAccountRecords =
                this.corpOpenAccountRecordMapper.selectByExample(corpOpenAccountExample);
        if (corpOpenAccountRecords != null && corpOpenAccountRecords.size() > 0) {
            return corpOpenAccountRecords.get(0);
        }
        return null;
    }

}
