package com.hyjf.api.server.registeropenaccount;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.IdCard15To18;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountLog;
import com.hyjf.mybatis.model.auto.BankOpenAccountLogExample;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfigExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.auto.UsersLog;
import com.hyjf.mybatis.model.auto.UsersLogExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

/**
 * 外部服务接口:用户注册加开户Service实现类
 *
 * @author liuyang
 */
@Service
public class UserRegisterAndOpenAccountServiceImpl extends BaseServiceImpl implements UserRegisterAndOpenAccountService {

    Logger _log = LoggerFactory.getLogger(UserRegisterAndOpenAccountServiceImpl.class);
    /**
     * 根据手机号检索手机号是否在平台已经注册
     *
     * @param mobile
     * @return
     */
    @Override
    public Users selectUserByMobile(String mobile) {
        UsersExample example = new UsersExample();
        UsersExample.Criteria cra = example.createCriteria();
        cra.andMobileEqualTo(mobile);
        List<Users> list = this.usersMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据机构编号检索机构信息
     *
     * @param instCode
     * @return
     */
    @Override
    public HjhInstConfig selectInstConfigByInstCode(String instCode) {
        HjhInstConfigExample example = new HjhInstConfigExample();
        HjhInstConfigExample.Criteria cra = example.createCriteria();
        cra.andInstCodeEqualTo(instCode);
        List<HjhInstConfig> list = this.hjhInstConfigMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据手机号注册用户
     *
     * @param mobile
     * @param instCode
     * @return
     */
    @Override
    public Integer insertUserAction(String mobile, String instCode, HttpServletRequest request) {
        int userId = 0;
        try {
            Users user = new Users();
            user.setIsInstFlag(1);// 是否是第三方推送用户(0:否,1:是)
            user.setInstCode(instCode);// 机构编号
            // 插入huiyingdai_users表
            this.insertUser(mobile, user, request);
            userId = user.getUserId();
            // 插入huiyingdai_users_info
            this.insertUsersInfo(userId);
            // 插入huiyingdai_account表
            this.insertAccount(userId);
            // 插入注册记录表
            this.insertUsersLog(userId, request);
        } catch (Exception e) {
            LogUtil.errorLog(UserRegisterAndOpenAccountServiceImpl.class.getName(), "insertUserAction", e);
            return userId;
        }
        return userId;
    }

    /**
     * 开户失败后,根据用户ID删除用户信息
     *
     * @param userId
     */
    @Override
    public void userDeleteByUserId(Integer userId) {
        // 删除用户表
        this.usersMapper.deleteByPrimaryKey(userId);
        UsersInfoExample usersInfoExample = new UsersInfoExample();
        UsersInfoExample.Criteria cra = usersInfoExample.createCriteria();
        cra.andUserIdEqualTo(userId);
        // 删除用户详情表
        this.usersInfoMapper.deleteByExample(usersInfoExample);
        // 删除account表
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria accountCra = accountExample.createCriteria();
        accountCra.andUserIdEqualTo(userId);
        this.accountMapper.deleteByExample(accountExample);
        // 删除注册记录表
        UsersLogExample usersLogExample = new UsersLogExample();
        UsersLogExample.Criteria usersLogCra = usersLogExample.createCriteria();
        usersLogCra.andUserIdEqualTo(userId);
        this.usersLogMapper.deleteByExample(usersLogExample);
    }

    /**
     * 开户成功后保存
     *
     * @param openAccountResult
     * @return
     */
    @Override
    public boolean updateUserAccount(BankCallBean openAccountResult) {
        _log.info("开户成功后,更新用户账户信息");
        // 当前日期
        Date nowDate = new Date();
        // 开户订单号
        String orderId = openAccountResult.getLogOrderId();
        // 用户ID
        Integer userId = Integer.parseInt(openAccountResult.getLogUserId());
        BankOpenAccountLog openAccoutLog = this.checkBankOpenAccountLog(userId, orderId);
        if (Validator.isNull(openAccoutLog)) {
            throw new RuntimeException("查询用户开户日志表失败，用户开户订单号：" + orderId + ",用户ID:" + userId);
        }
        BankOpenAccountLogExample accountLogExample = new BankOpenAccountLogExample();
        accountLogExample.createCriteria().andUserIdEqualTo(userId);
        boolean deleteLogFlag = this.bankOpenAccountLogMapper.deleteByExample(accountLogExample) > 0 ? true : false;
        if (!deleteLogFlag) {
            throw new RuntimeException("删除用户开户日志表失败，用户开户订单号：" + orderId + ",用户userId:" + userId);
        }
        // 获取用户信息
        Users user = this.getUsers(userId);
        // 用户名
        String userName = user.getUsername();
        // 身份证号
        String idNo = openAccoutLog.getIdNo();
        _log.info("用户ID:" + userId + "],用户名:[" + userName + "],用户身份证号:[" + idNo + "]");
        // 真实姓名
        String trueName = null;
        try {
            trueName = URLDecoder.decode(openAccoutLog.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 根据身份证号获取用户相关信息
        if (idNo != null && idNo.length() < 18) {
            try {
                idNo = IdCard15To18.getEighteenIDCard(idNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int sexInt = Integer.parseInt(idNo.substring(16, 17));// 性别
        if (sexInt % 2 == 0) {
            sexInt = 2;
        } else {
            sexInt = 1;
        }
        String birthDayTemp = idNo.substring(6, 14);// 出生日期
        String birthDay = StringUtils.substring(birthDayTemp, 0, 4) + "-" + StringUtils.substring(birthDayTemp, 4, 6) + "-" + StringUtils.substring(birthDayTemp, 6, 8);
        user.setBankOpenAccount(1);// 银行是否开户
        user.setBankAccountEsb(4);// 开户平台
        user.setRechargeSms(0);
        user.setWithdrawSms(0);
        user.setUserType(0);
        user.setMobile(openAccoutLog.getMobile());
        user.setVersion(user.getVersion().add(BigDecimal.ONE));
        // 更新相应的用户表
        boolean usersFlag = usersMapper.updateByPrimaryKeySelective(user) > 0 ? true : false;
        if (!usersFlag) {
            _log.info("开户成功后,更新用户信息失败,用户ID:[" + userId + "]");
            throw new RuntimeException("更新用户表失败！");
        }
        // 根据用户ID查询用户信息表
        UsersInfo userInfo = this.getUsersInfoByUserId(userId);
        if (userInfo == null) {
            _log.info("获取用户详情表失败,用户ID:[" + userId + "]");
            throw new RuntimeException("根据用户ID,查询用户详情失败");
        }
        userInfo.setTruename(trueName);// 姓名
        userInfo.setIdcard(idNo);// 身份证号
        userInfo.setSex(sexInt);// 性别
        userInfo.setBirthday(birthDay); // 出生年月日
        userInfo.setTruenameIsapprove(1);
        userInfo.setMobileIsapprove(1);
        // 更新用户详细信息表
        boolean userInfoFlag = usersInfoMapper.updateByPrimaryKeySelective(userInfo) > 0 ? true : false;
        if (!userInfoFlag) {
            _log.info("更新用户详细信息表失败,用户ID:[" + userId + "]");
            throw new RuntimeException("更新用户详情表失败！");
        }
        // 插入银行账户关联表
        BankOpenAccount openAccount = new BankOpenAccount();
        openAccount.setUserId(userId);
        openAccount.setUserName(user.getUsername());
        openAccount.setAccount(openAccountResult.getAccountId());
        openAccount.setCreateTime(nowDate);
        openAccount.setCreateUserId(userId);
        openAccount.setCreateUserName(userName);
        boolean openAccountFlag = this.bankOpenAccountMapper.insertSelective(openAccount) > 0 ? true : false;
        if (!openAccountFlag) {
            _log.info("开户成功后,插入用户银行账户关联表失败,用户ID:[" + userId + "]");
            throw new RuntimeException("插入用户银行账户关联表失败！");
        }
        // 根据银行卡号查询所属银行ID
        String bankId = this.getBankIdByCardNo(openAccoutLog.getCardNo());
        // 所属银行ID为空
        bankId = bankId == null ? "0" : bankId;
        // 插入相应的银行卡
        BankCard bankCard = new BankCard();
        bankCard.setUserId(userId);
        bankCard.setUserName(userName);
        bankCard.setCardNo(openAccoutLog.getCardNo());
        bankCard.setBankId(bankId == null ? 0 : Integer.valueOf(bankId));
        bankCard.setBank(bankId == null ? "" : this.getBankNameById(bankId));
        // 银行联号
        String payAllianceCode = "";
        // 调用江西银行接口查询银行联号
        BankCallBean payAllianceCodeQueryBean = this.payAllianceCodeQuery(openAccoutLog.getCardNo(), userId);
        if (payAllianceCodeQueryBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(payAllianceCodeQueryBean.getRetCode())) {
            payAllianceCode = payAllianceCodeQueryBean.getPayAllianceCode();
        }
        // 如果此时银行联号还是为空,调用本地查询银行联号
        if (StringUtils.isBlank(payAllianceCode)) {
            payAllianceCode = this.getPayAllianceCodeByBankId(bankId);
        }
        bankCard.setPayAllianceCode(payAllianceCode == null ? StringUtils.EMPTY : payAllianceCode);
        bankCard.setStatus(1);
        bankCard.setCreateTime(nowDate);
        bankCard.setCreateUserId(userId);
        bankCard.setCreateUserName(userName);
        boolean bankFlag = this.bankCardMapper.insertSelective(bankCard) > 0 ? true : false;
        if (!bankFlag) {
            _log.info("开户成功后,插入用户银行卡信息表失败,用户ID:[" + userName + "].");
            throw new RuntimeException("插入用户银行卡信息表失败");
        }
        return bankFlag;
    }

    /**
     * 更新开户日志表
     *
     * @param userId
     * @param logOrderId
     * @param status
     */
    @Override
    public void updateUserAccountLog(Integer userId, String logOrderId, int status) {
        Date date = new Date();
        BankOpenAccountLogExample example = new BankOpenAccountLogExample();
        example.createCriteria().andUserIdEqualTo(userId).andOrderIdEqualTo(logOrderId);
        List<BankOpenAccountLog> bankOpenAccountLogs = this.bankOpenAccountLogMapper.selectByExample(example);
        if (bankOpenAccountLogs != null && bankOpenAccountLogs.size() == 1) {
            BankOpenAccountLog openAccountLog = bankOpenAccountLogs.get(0);
            openAccountLog.setStatus(status); // 更新开户状态
            openAccountLog.setUpdateTime(date);
            openAccountLog.setUpdateUserId(userId);
            openAccountLog.setUpdateUserName(openAccountLog.getCreateUserName());
            this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(openAccountLog);
        }
    }

    /**
     * 插入huiyingdai_users表
     *
     * @param mobile
     * @param user
     * @param request
     */
    private void insertUser(String mobile, Users user, HttpServletRequest request) throws Exception {
        int nowTime = GetDate.getNowTime10();
        // 写入用户信息表
        String userName = getUniqueUsername(mobile);
        user.setUsername(userName);// 用户名
        user.setMobile(mobile); // 手机号
        user.setRechargeSms(0); // 充值成功短信 0发送 1不发送
        user.setWithdrawSms(0); // withdraw_sms
        user.setInvestflag(0);// 新手标志位：0新手 1老手
        user.setInvestSms(0);// 出借成功短信 0发送 1不发送
        user.setRecieveSms(0);// 回收成功短信 0发送 1不发送
        user.setVersion(BigDecimal.ZERO);// 版本号
        user.setUserType(0);// 用户类型 0普通用户 1企业用户
        user.setIsSetPassword(0);// 是否设置了交易密码 0未设置 1已设置
        user.setBankOpenAccount(0);// 是否银行开户,0未开户,1已开户
        user.setRegEsb(4);// 账户开通平台 0pc 1微信 2安卓 3IOS 4其他
        String codeSalt = GetCode.getRandomCode(6);
        // 第三方推送用户密码
        String password = "asdf1234";
        user.setPassword(MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt)); // 登陆密码
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
     * 插入huiyingdai_users_info表
     *
     * @param userId
     */
    private void insertUsersInfo(int userId) throws Exception {
        // 当期时间
        int nowTime = GetDate.getNowTime10();
        UsersInfo userInfo = new UsersInfo();
        userInfo.setAttribute(0);// 默认为无主单
        userInfo.setUserId(userId);
        userInfo.setRoleId(2);// 用户角色:借款人
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
        userInfo.setBorrowerType(2);// 借款人类型 1：内部机构 2：外部机构
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
    private void insertAccount(int userId) throws Exception {
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
        account.setPlanAccedeFrost(BigDecimal.ZERO);// 出借汇添金标的出借的未放款金额
        account.setVersion(BigDecimal.ZERO);
        // 江西银行账户相关
        account.setBankBalance(BigDecimal.ZERO);// 江西银行可用余额
        account.setBankBalanceCash(BigDecimal.ZERO);// 江西银行可提现金额(银行电子账户余额)
        account.setBankFrost(BigDecimal.ZERO);// 江西银行冻结金额
        account.setBankFrostCash(BigDecimal.ZERO);// 江西银行冻结金额(银行电子账户冻结金额)
        account.setBankInterestSum(BigDecimal.ZERO);// 银行累计收益
        account.setBankInvestSum(BigDecimal.ZERO);// 银行累计出借
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
     * 插入用户记录表
     *
     * @param userId
     * @param request
     */
    private void insertUsersLog(int userId, HttpServletRequest request) throws Exception {
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
}
