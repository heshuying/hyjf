/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.register;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.register.UserRegisterServiceImpl;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.util.StringRandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AEMS系统:用户注册Service实现类
 *
 * @author liuyang
 * @version AemsUserRegisterServiceImpl, v0.1 2018/9/4 19:14
 */
@Service
public class AemsUserRegisterServiceImpl extends BaseServiceImpl implements AemsUserRegisterService {

    Logger log = LoggerFactory.getLogger(UserRegisterServiceImpl.class);


    @Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    /**
     * 根据手机号密码注册用户
     *
     * @param mobile
     * @param instCode
     * @param request
     * @param instType
     * @param utmPlat
     * @param platform
     * @return
     */
    @Override
    public Integer insertUserAction(String mobile, String instCode, HttpServletRequest request, Integer instType, UtmPlat utmPlat, String platform) {
        int userId = 0;
        try {
            Users user = new Users();
            user.setIsInstFlag(1);
            user.setInstCode(instCode);
            // 插入huiyingdai_users表
            this.insertUser(mobile, user, request, platform, instCode);
            userId = user.getUserId();
            // 插入huiyingdai_users_info
            this.insertUsersInfo(userId, instType);
            // 插入huiyingdai_account表
            this.insertAccount(userId);
            // 插入注册渠道
            this.insertUtmInfo(userId, user.getUsername(), utmPlat);
            // 插入注册记录表
            this.insertUsersLog(userId, request);
        } catch (Exception e) {
            LogUtil.errorLog(UserRegisterServiceImpl.class.getName(), "insertUserAction", e);
            return userId;
        }
        return userId;
    }

    /**
     * 插入用户渠道信息
     *
     * @param userId
     * @param userName
     * @param utmPlat
     */
    private void insertUtmInfo(int userId, String userName, UtmPlat utmPlat) {
        // 来源为PC统计
        if (utmPlat.getSourceType() == 0) {
            UtmReg utmReg = new UtmReg();
            utmReg.setCreateTime(GetDate.getNowTime10());
            utmReg.setUtmId(utmPlat.getSourceId());
            utmReg.setUserId(userId);
            utmReg.setOpenAccount(0);
            utmReg.setBindCard(0);
            utmRegMapper.insertSelective(utmReg);
            log.info("updateRegistUser***********************************预插入utmReg：" + JSON.toJSONString(utmReg));
        }
        // 来源为APP统计
        if (utmPlat.getSourceType() == 1) {
            AppChannelStatisticsDetail detail = new AppChannelStatisticsDetail();
            detail.setSourceId(utmPlat.getSourceId());
            detail.setSourceName(utmPlat.getSourceName() != null ? utmPlat.getSourceName() : "");
            detail.setUserId(userId);
            detail.setUserName(userName);
            detail.setRegisterTime(new Date());
            detail.setCumulativeInvest(BigDecimal.ZERO);
            appChannelStatisticsDetailMapper.insertSelective(detail);
            log.info("updateRegistUser***********************************预插入utmReg：" + JSON.toJSONString(detail));
        }
    }

    /**
     * 插入huiyingdai_user表
     *
     * @param mobile
     * @param user
     * @param request
     * @param platform
     * @throws Exception
     */
    private void insertUser(String mobile, Users user, HttpServletRequest request, String platform, String instCode) throws Exception {
        int nowTime = GetDate.getNowTime10();
        // 写入用户信息表
        String userName = getUniqueUsername(mobile);
        user.setUsername(userName);// 用户名
        user.setMobile(mobile); // 手机号
        user.setRechargeSms(0); // 充值成功短信 0发送 1不发送
        user.setWithdrawSms(0); // withdraw_sms
        user.setInvestflag(0);// 新手标志位：0新手 1老手
        user.setInvestSms(0);// 投资成功短信 0发送 1不发送
        user.setRecieveSms(0);// 回收成功短信 0发送 1不发送
        user.setVersion(BigDecimal.ZERO);// 版本号
        user.setUserType(0);// 用户类型 0普通用户 1企业用户
        user.setIsSetPassword(0);// 是否设置了交易密码 0未设置 1已设置
        user.setBankOpenAccount(0);// 是否银行开户,0未开户,1已开户
        user.setRegEsb(Integer.parseInt(platform));// 账户开通平台 0pc 1微信 2安卓 3IOS 4其他
        String codeSalt = GetCode.getRandomCode(6);

        //处理纳觅财富注册用户数据密码随机生成6位字母数字组合并发送短信通知用户
        if (instCode.equals("11000002") || instCode.equals("11000003")) {
            final String password = StringRandomUtil.getStringRandom(6);
            user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt)); // 登陆密码
            final Integer userId = user.getUserId();
            final String mobiles = mobile;
            Thread thread = new Thread() {
                public void run() {
                    log.info("线程启动，开始发短信");
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("val_password", password);
                    SmsMessage smsMessage = new SmsMessage(userId, param, mobiles, null, MessageDefine.SMSSENDFORMOBILE, null, CustomConstants.THIRD_PARTY_REGIEST_PASSWORD, CustomConstants.CHANNEL_TYPE_NORMAL);
                    smsProcesser.send(smsMessage);
                }
            };
            thread.start();


            log.info("账号：" + mobile + "，密码：" + password);
        } else {
            // String password = PropUtils.getSystem(CustomConstants.HYJF_THIRD_PARTY_USER_PASSWORD);
            String password = StringRandomUtil.getStringRandom(6);
            user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt)); // 登陆密码
            log.info("账号：" + mobile + "，密码：" + password);
        }

        user.setRegIp(GetCilentIP.getIpAddr(request)); // 注册IP
        user.setRegTime(nowTime);// 注册时间
        user.setLoginIp(GetCilentIP.getIpAddr(request));// 登陆ip
        user.setLoginTime(nowTime);// 当期时间
        user.setLastIp(GetCilentIP.getIpAddr(request));// 上次登陆IP
        user.setLastTime(nowTime);// 上次登陆时间
        user.setLogintime(1);// 登录次数
        user.setStatus(0);
        user.setSalt(codeSalt);
        user.setBorrowSms(0);
        user.setPid(0);// 第三方平台ID
        user.setUsernamep("");// 第三方平台用户名
        user.setPtype(0); // 是否新注册 0为新 1为关联
        user.setOpenAccount(0);// 是否开户,0未开户,1已开户
        boolean isInsertFlag = usersMapper.insertSelective(user) > 0 ? true : false;
        if (!isInsertFlag) {
            throw new Exception("插入User表失败~");
        }
    }


    /**
     * 插入huiyingdai_usersInfo
     *
     * @param userId
     */
    private void insertUsersInfo(Integer userId, Integer instType) throws Exception {
        // 当期时间
        int nowTime = GetDate.getNowTime10();
        UsersInfo userInfo = new UsersInfo();
        userInfo.setAttribute(0);// 默认为无主单
        userInfo.setUserId(userId);
        if (instType == 0) {
            userInfo.setRoleId(2);// 用户角色1投资人2借款人3垫付机构
            userInfo.setBorrowerType(2);// 借款人类型 1：内部机构 2：外部机构
        } else {
            userInfo.setRoleId(1);
        }
        userInfo.setMobileIsapprove(1);
        userInfo.setTruenameIsapprove(0);
        userInfo.setEmailIsapprove(0);
        userInfo.setPromoteway(0);
        userInfo.setIs51(0);
        userInfo.setIsStaff(0);
        userInfo.setDepartmentId(0);
        userInfo.setNickname("");// 昵称
        userInfo.setBirthday("");// 生日
        userInfo.setSex(1);// 性别
        userInfo.setIdcard("");// 身份证号
        userInfo.setEducation("");// 学历
        userInfo.setAddress("");// 地址
        userInfo.setIntro("");// 个人简介
        userInfo.setInterest("");// 兴趣爱好
        userInfo.setUpdateTime(nowTime);// 更新时间
        userInfo.setParentId(0);
        userInfo.setIsContact(false);
        boolean isInsertFlag = usersInfoMapper.insertSelective(userInfo) > 0 ? true : false;
        if (!isInsertFlag) {
            throw new Exception("插入用户信息表失败~,用户ID:[" + userId + "].");
        }
    }

    /**
     * 插入huiyingdai_account表
     *
     * @param userId
     */
    private void insertAccount(Integer userId) throws Exception {
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
        // 计划账户相关
        account.setPlanAccedeTotal(BigDecimal.ZERO);// 计划累计加入总额
        account.setPlanBalance(BigDecimal.ZERO);// 计划可用余额
        account.setPlanFrost(BigDecimal.ZERO);// 计划冻结金额
        account.setPlanAccountWait(BigDecimal.ZERO);// 计划待收总额
        account.setPlanCapitalWait(BigDecimal.ZERO);// 计划待收本金
        account.setPlanInterestWait(BigDecimal.ZERO);// 计划待收利息
        account.setPlanRepayInterest(BigDecimal.ZERO);// 计划累计收益
        account.setPlanAccedeBalance(BigDecimal.ZERO);// 计划加入可用余额
        account.setPlanAccedeFrost(BigDecimal.ZERO);// 投资汇添金标的投资的未放款金额
        account.setVersion(BigDecimal.ZERO);
        // 江西银行账户相关
        account.setBankBalance(BigDecimal.ZERO);// 江西银行可用余额
        account.setBankBalanceCash(BigDecimal.ZERO);// 江西银行可提现金额(银行电子账户余额)
        account.setBankFrost(BigDecimal.ZERO);// 江西银行冻结金额
        account.setBankFrostCash(BigDecimal.ZERO);// 江西银行冻结金额(银行电子账户冻结金额)
        account.setBankInterestSum(BigDecimal.ZERO);// 银行累计收益
        account.setBankInvestSum(BigDecimal.ZERO);// 银行累计投资
        account.setBankWaitCapital(BigDecimal.ZERO);// 银行待还本金
        account.setBankWaitInterest(BigDecimal.ZERO);// 银行待还利息
        account.setBankWaitRepay(BigDecimal.ZERO);// 银行待还本息
        account.setBankTotal(BigDecimal.ZERO);// 银行总资产
        account.setBankAwaitCapital(BigDecimal.ZERO);// 银行待收本金
        account.setBankAwaitInterest(BigDecimal.ZERO);// 银行待收利息
        account.setBankAwait(BigDecimal.ZERO);// 银行待收总额
        account.setBankWaitRepayOrg(BigDecimal.ZERO);// 待还垫付机构金额
        account.setBankAwaitOrg(BigDecimal.ZERO);// 银行垫付机构待收垫付总额
        boolean isInsertFlag = accountMapper.insertSelective(account) > 0 ? true : false;
        // 插入账户表失败
        if (!isInsertFlag) {
            throw new Exception("插入用户账户表失败~,用户ID:[" + userId + "]");
        }
    }

    /**
     * 插入注册记录表
     *
     * @param userId
     * @throws Exception
     */
    private void insertUsersLog(Integer userId, HttpServletRequest request) throws Exception {
        UsersLog userLog = new UsersLog();
        userLog.setUserId(userId);
        userLog.setIp(GetCilentIP.getIpAddr(request));
        userLog.setEvent("register");
        userLog.setContent("注册成功");
        boolean isInsertFlag = usersLogMapper.insertSelective(userLog) > 0 ? true : false;
        if (!isInsertFlag) {
            throw new Exception("插入注册记录表失败~,用户ID:[" + userId + "].");
        }
    }


    /**
     * 获取唯一username
     *
     * @param mobile
     * @return
     */
    private String getUniqueUsername(String mobile) {
        String username = "hyjf" + mobile.substring(mobile.length() - 6, mobile.length());
        // 第一规则
        UsersExample ue = new UsersExample();
        UsersExample.Criteria cr = ue.createCriteria();
        cr.andUsernameEqualTo(username);
        int cn1 = usersMapper.countByExample(ue);
        if (cn1 > 0) {
            // 第二规则
            UsersExample ue2 = new UsersExample();
            UsersExample.Criteria cr2 = ue2.createCriteria();
            username = "hyjf" + mobile;
            cr2.andUsernameEqualTo(username);
            int cn2 = usersMapper.countByExample(ue2);
            if (cn2 > 0) {
                // 第三规则
                int i = 0;
                while (true) {
                    i++;
                    UsersExample ue3 = new UsersExample();
                    UsersExample.Criteria cr3 = ue3.createCriteria();
                    username = "hyjf" + mobile.substring(mobile.length() - 6, mobile.length()) + i;
                    cr3.andUsernameEqualTo(username);
                    int cn3 = usersMapper.countByExample(ue3);
                    if (cn3 == 0) {
                        break;
                    }
                }
            }
        }
        return username;
    }

    /**
     * 根据ip地址获取注册所在地
     *
     * @param ip
     * @param usersInfo
     * @author hsy
     */
    public void getAddress(String ip, UsersInfo usersInfo) {
        if (StringUtils.isEmpty(ip)) {
            return;
        }
        try {
            String ipInfoUrl = PropUtils.getSystem("hyjf.ip.taobo.url");
            log.info("根据ip获取注册地请求url：" + ipInfoUrl + "?ip=" + ip);
            String result = HttpDeal.get(ipInfoUrl + "?ip=" + ip);
            log.info("根据ip获取注册地返回结果：" + result);
            JSONObject resultObj = (JSONObject) JSONObject.parse(result);
            String resultCode = resultObj.getString("code");
            if (resultCode.equals("0")) { // 查询成功
                String region = resultObj.getJSONObject("data").getString("region");
                String city = resultObj.getJSONObject("data").getString("city");
                String county = resultObj.getJSONObject("data").getString("county");
                usersInfo.setProvince(StringUtils.isEmpty(region) ? "" : region);
                usersInfo.setCity(StringUtils.isEmpty(city) ? "" : city);
                usersInfo.setArea(StringUtils.isEmpty(county) ? "" : county);
            } else {
                LogUtil.errorLog(this.getClass().getName(), "getAddress", "根据ip地址获取所在地失败，ip：" + ip + " 返回信息：" + result, null);
            }
        } catch (Exception e) {
            LogUtil.errorLog(this.getClass().getName(), "getAddress", "根据ip地址获取所在地出错，ip：" + ip, e);
        }

    }

    /**
     * 根据手机号检索该手机号是否已注册
     *
     * @param mobile
     * @return
     */
    @Override
    public Users selectUserByMobile(String mobile) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<Users> list = usersMapper.selectByExample(example);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    /**
     * 根据用户ID检索用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public Users checkUserByUserId(Integer userId) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<Users> usersList = this.usersMapper.selectByExample(example);
        if (usersList != null && usersList.size() > 0) {
            return usersList.get(0);
        }
        return null;
    }

    /**
     * 根据用户ID查询用户开户信息
     *
     * @param userId
     * @return
     */
    @Override
    public BankOpenAccount selectBankOpenAccountByUserId(Integer userId) {
        BankOpenAccountExample example = new BankOpenAccountExample();
        BankOpenAccountExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<BankOpenAccount> list = this.bankOpenAccountMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据渠道号检索渠道是否存在
     *
     * @param utmId
     * @return
     */
    @Override
    public UtmPlat selectUtmPlatByUtmId(String utmId) {
        UtmPlatExample example = new UtmPlatExample();
        UtmPlatExample.Criteria cra = example.createCriteria();
        cra.andSourceIdEqualTo(Integer.parseInt(utmId));
        cra.andDelFlagEqualTo("0");
        List<UtmPlat> list = this.utmPlatMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据推荐人手机号或userId判断推荐人是否存在
     */
    @Override
    public List<Users> selectUserByRecommendName(String param) {
        UsersExample example = new UsersExample();
        if (CommonUtils.isMobile(param)) {
            UsersExample.Criteria criteria = example.createCriteria();
            criteria.andMobileEqualTo(param);
        } else {
            UsersExample.Criteria criteriaUserName = example.createCriteria();
            try {
                Integer recommend = Integer.valueOf(param);
                criteriaUserName.andUserIdEqualTo(recommend);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        List<Users> users = usersMapper.selectByExample(example);
        return users;
    }

    @Override
    public void inserSpreadUser(Integer userId, Users user) {
        SpreadsUsers spreadUser = new SpreadsUsers();
        spreadUser.setUserId(userId);
        spreadUser.setSpreadsUserid(user.getUserId());
        spreadUser.setAddtime(String.valueOf(GetDate.getNowTime10()));
        spreadUser.setType("reg");
        spreadUser.setOpernote("reg");
        spreadUser.setOperation(userId + "");
        log.info("saveRegistUser***********************************预插入spreadUser："
                + JSON.toJSONString(spreadUser));
        spreadsUsersMapper.insertSelective(spreadUser);
    }

    /**
     * 发送短信(注册发送密码)
     *
     * @param userId   用户ID
     * @param mobile   手机号码
     * @param content  短信内容（尊敬的用户，您在汇盈金服登录的密码为[val_password]，恭喜您成功注册汇盈金服账号，感谢您的支持，祝您生活愉快。）
     * @param password 密码
     */
    @Override
    public String sendSmsMessage(Integer userId, String mobile, String content,
                                 String password) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("val_password", password);
        SmsMessage smsMessage = new SmsMessage(userId, param, mobile, null, MessageDefine.SMSSENDFORMOBILE, null, content, CustomConstants.CHANNEL_TYPE_NORMAL);
        boolean flag = smsProcesser.send(smsMessage);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", flag);
        return jsonObject.toString();
    }

    /**
     * 更新用户
     */
    @Override
    public void updateByPrimaryKeySelective(Users users) {
        usersMapper.updateByPrimaryKeySelective(users);
    }
}
