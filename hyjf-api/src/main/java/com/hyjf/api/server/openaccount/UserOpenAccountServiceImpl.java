package com.hyjf.api.server.openaccount;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.IdCard15To18;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

/**
 * 用户开户Service实现类
 *
 * @author liuyang
 */
@Service
public class UserOpenAccountServiceImpl extends BaseServiceImpl implements UserOpenAccountService {

    Logger _log = LoggerFactory.getLogger(UserOpenAccountServiceImpl.class);
    
    /**
     * 根据手机号检索用户信息
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
     * 调用银行发送短信接口
     *
     * @param userId
     * @param orderId
     * @param srvTxCode
     * @param mobile
     * @param channel
     * @return
     */
    @Override
    public BankCallBean sendOpenAccountSms(Integer userId, String orderId, String srvTxCode, String mobile, String channel) {
        // 调用存管接口发送验证码
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_SMSCODE_APPLY);// 交易代码cardBind
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
        bean.setTxDate(GetOrderIdUtils.getOrderDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getOrderTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(channel);// 交易渠道000001手机APP 000002网页
        bean.setSrvTxCode(srvTxCode);
        bean.setMobile(mobile);// 交易渠道
        bean.setLogOrderId(orderId);// 订单号
        bean.setLogRemark("发送短信验证码");
        bean.setLogUserId(String.valueOf(userId));// 请求用户名
        try {
            BankCallBean mobileBean = BankCallUtils.callApiBg(bean);
            if (Validator.isNull(mobileBean)) {
                return null;
            }
            // 短信发送返回结果码
            String retCode = mobileBean.getRetCode();
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
                return null;
            }
            if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
                return null;
            }
            // 业务授权码
            String srvAuthCode = mobileBean.getSrvAuthCode();
            if (Validator.isNotNull(mobileBean.getSrvAuthCode())) {
                boolean smsFlag = this.updateBankSmsLog(userId, srvTxCode, srvAuthCode);
                if (smsFlag) {
                    return mobileBean;
                }
            } else {
                // 保存用户开户日志
                srvAuthCode = this.selectBankSmsLog(userId, srvTxCode, srvAuthCode);
                if (Validator.isNull(srvAuthCode)) {
                    return null;
                } else {
                    mobileBean.setSrvAuthCode(srvAuthCode);
                    return mobileBean;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存用户的相应的授权代码
     *
     * @param userId
     * @param srvTxCode
     * @param srvAuthCode
     * @return
     */
    private boolean updateBankSmsLog(Integer userId, String srvTxCode, String srvAuthCode) {
        Date nowDate = new Date();
        Users user = this.getUsers(userId);
        BankSmsAuthCodeExample example = new BankSmsAuthCodeExample();
        example.createCriteria().andUserIdEqualTo(userId).andSrvTxCodeEqualTo(srvTxCode);
        List<BankSmsAuthCode> smsAuthCodeList = bankSmsAuthCodeMapper.selectByExample(example);
        if (smsAuthCodeList != null && smsAuthCodeList.size() == 1) {
            BankSmsAuthCode smsAuthCode = smsAuthCodeList.get(0);
            smsAuthCode.setSrvAuthCode(srvAuthCode);
            smsAuthCode.setUpdateTime(nowDate);
            smsAuthCode.setUpdateUserId(userId);
            smsAuthCode.setUpdateUserName(user.getUsername());
            boolean smsAuthCodeUpdateFlag = this.bankSmsAuthCodeMapper.updateByPrimaryKeySelective(smsAuthCode) > 0 ? true : false;
            if (smsAuthCodeUpdateFlag) {
                return true;
            } else {
                return false;
            }
        } else {
            this.bankSmsAuthCodeMapper.deleteByExample(example);
            BankSmsAuthCode smsAuthCode = new BankSmsAuthCode();
            smsAuthCode.setUserId(userId);
            smsAuthCode.setSrvTxCode(srvTxCode);
            smsAuthCode.setSrvAuthCode(srvAuthCode);
            smsAuthCode.setStatus(1);
            smsAuthCode.setCreateTime(nowDate);
            smsAuthCode.setCreateUserId(userId);
            smsAuthCode.setCreateUserName(user.getUsername());
            boolean smsAuthCodeInsertFlag = this.bankSmsAuthCodeMapper.insertSelective(smsAuthCode) > 0 ? true : false;
            if (smsAuthCodeInsertFlag) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 查询用户的授权码
     *
     * @param userId
     * @param srvTxCode
     * @param srvAuthCode
     * @return
     */
    private String selectBankSmsLog(Integer userId, String srvTxCode, String srvAuthCode) {
        BankSmsAuthCodeExample example = new BankSmsAuthCodeExample();
        example.createCriteria().andUserIdEqualTo(userId).andSrvTxCodeEqualTo(srvTxCode);
        List<BankSmsAuthCode> smsAuthCodeList = bankSmsAuthCodeMapper.selectByExample(example);
        if (smsAuthCodeList != null && smsAuthCodeList.size() == 1) {
            BankSmsAuthCode smsAuthCode = smsAuthCodeList.get(0);
            return smsAuthCode.getSrvAuthCode();
        }
        return null;
    }

    /**
     * 更新开户日志表
     *
     * @param userId
     * @param orderId
     * @param srvAuthCode
     * @return
     */
    @Override
    public boolean updateUserAccountLog(Integer userId, String orderId, String srvAuthCode) {
        Date date = new Date();
        BankOpenAccountLogExample example = new BankOpenAccountLogExample();
        example.createCriteria().andUserIdEqualTo(userId).andOrderIdEqualTo(orderId);
        List<BankOpenAccountLog> bankOpenAccountLogs = this.bankOpenAccountLogMapper.selectByExample(example);
        if (bankOpenAccountLogs != null && bankOpenAccountLogs.size() == 1) {
            BankOpenAccountLog openAccountLog = bankOpenAccountLogs.get(0);
            openAccountLog.setStatus(1);
            openAccountLog.setLastSrvAuthCode(srvAuthCode);
            openAccountLog.setUpdateTime(date);
            openAccountLog.setUpdateUserId(userId);
            openAccountLog.setUpdateUserName(openAccountLog.getCreateUserName());
            boolean updateFlag = this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(openAccountLog) > 0 ? true : false;
            return updateFlag;
        }
        return false;
    }


    /**
     * 保存用户的开户参数
     *
     * @param openAccountLog
     * @param openAccoutBean
     * @return
     */
    @Override
    public boolean updateUserAccountLog(BankOpenAccountLog openAccountLog, BankCallBean openAccoutBean) {
        Date date = new Date();
        int userId = Integer.parseInt(openAccoutBean.getLogUserId());
        openAccountLog.setName(openAccoutBean.getName());
        openAccountLog.setIdNo(openAccoutBean.getIdNo());
        openAccountLog.setIdType(openAccoutBean.getIdType());
        openAccountLog.setCardNo(openAccoutBean.getCardNo());
        openAccountLog.setAcctUse(openAccoutBean.getAcctUse());
        openAccountLog.setUserIp(openAccoutBean.getLogIp());
        openAccountLog.setUpdateTime(date);
        openAccountLog.setUpdateUserId(userId);
        openAccountLog.setUpdateUserName(openAccountLog.getCreateUserName());
        boolean flag = this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(openAccountLog) > 0 ? true : false;
        return flag;
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
     * 开户成功后,更新用户账户信息
     *
     * @param openAccountResult
     * @return
     */
    @Override
    public boolean updateUserAccount(BankCallBean openAccountResult) {
        _log.info("开户成功后,更新用户账户信息");
        // 需要调用查询接口查询用户的银行卡号  手机号  绑卡关系查询接口&&&&&&&&&&&&&&&&&&&&&&&&&&&
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
        user.setBankAccountEsb(openAccountResult.getLogClient());// 开户平台
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
        _log.info("开始调用银行卡查询接口,用户ID:[" + userId + "]  CardNo：["+openAccoutLog.getCardNo()+"]");
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
        bankCard.setMobile(openAccountResult.getMobile());
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
        // PC渠道统计表更新开户
        UtmReg utmReg = this.selectUtmRegByUserId(userId);
        if (utmReg != null) {
            utmReg.setBindCard(1);
            utmReg.setOpenAccount(1);
            this.utmRegMapper.updateByPrimaryKeySelective(utmReg);
        }
        // APP渠道统计明细更新
        AppChannelStatisticsDetail appChannelStatisticsDetail = this.selectAppChannelByUserId(userId);
        if (appChannelStatisticsDetail != null) {
            appChannelStatisticsDetail.setOpenAccountTime(new Date());
            this.appChannelStatisticsDetailMapper.updateByPrimaryKeySelective(appChannelStatisticsDetail);
        }

        return openAccountFlag;
    }

    @Override
    public BankCard checkPayAllianceCode(Integer userId) {
        BankCardExample example = new BankCardExample();
        BankCardExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<BankCard> list = this.bankCardMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

   

    /**
     * 检验用户身份证号是否已开户
     *
     * @param idNo
     * @return
     */
    @Override
    public boolean checkIdNo(String idNo) {
        UsersInfoExample example = new UsersInfoExample();
        example.createCriteria().andIdcardEqualTo(idNo);
        List<UsersInfo> userInfo = usersInfoMapper.selectByExample(example);
        if (userInfo != null && userInfo.size() > 0) {
            for (int i = 0; i < userInfo.size(); i++) {
                Integer userId = userInfo.get(i).getUserId();
                UsersExample userExample = new UsersExample();
                userExample.createCriteria().andUserIdEqualTo(userId);
                List<Users> user = usersMapper.selectByExample(userExample);
                if (user.get(0).getBankOpenAccount() == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 根据身份证号查询用户信息
     *
     * @param idNo
     * @return
     */
    @Override
    public Users selectUserByIdNo(String idNo) {
        UsersInfoExample example = new UsersInfoExample();
        example.createCriteria().andIdcardEqualTo(idNo);
        List<UsersInfo> userInfo = usersInfoMapper.selectByExample(example);
        if (userInfo != null && userInfo.size() > 0){
            for (int i = 0; i < userInfo.size(); i++){
                Integer userId = userInfo.get(i).getUserId();
                UsersExample userExample = new UsersExample();
                userExample.createCriteria().andUserIdEqualTo(userId);
                List<Users> users = usersMapper.selectByExample(userExample);
                for (Users user : users){
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * 根据用户ID检索PC渠道统计明细
     *
     * @param userId
     * @return
     */
    private UtmReg selectUtmRegByUserId(Integer userId) {
        UtmRegExample example = new UtmRegExample();
        UtmRegExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<UtmReg> list = this.utmRegMapper.selectByExample(example);
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据用户ID检索APP渠道统计明细
     *
     * @param userId
     * @return
     */
    private AppChannelStatisticsDetail selectAppChannelByUserId(Integer userId) {
        AppChannelStatisticsDetailExample example = new AppChannelStatisticsDetailExample();
        AppChannelStatisticsDetailExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<AppChannelStatisticsDetail> list = this.appChannelStatisticsDetailMapper.selectByExample(example);
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public String getBankOpenAccountByMobile(String mobile) {
        try {
            BankCallBean bean = new BankCallBean();
            bean.setLogOrderId(GetOrderIdUtils.getOrderId2(1));
            bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
            bean.setLogUserId(StringUtil.valueOf(1));
            bean.setLogRemark("根据手机号查询银行电子账户号");
            bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
            bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
            bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
            bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
            bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
            bean.setVersion(ChinaPnrConstant.VERSION_10);
            bean.setTxCode(BankCallMethodConstant.TXCODE_ACCOUNT_QUERY_BY_MOBILE);
            bean.setMobile(mobile);
            // 调用汇付接口
            BankCallBean retBean = BankCallUtils.callApiBg(bean);
            return retBean.getAccountId();
        } catch (Exception e) {
            LogUtil.endLog(UserOpenAccountServiceImpl.class.getName(), "根据手机号查询电子账户信息失败");
            return "";
        }
        
    }

    /**
     * 判断用户是否存在电子账户号
     *
     * @param userId
     * @return
     */
    @Override
    public boolean existBankAccountId(Integer userId) {
        BankOpenAccountExample example = new BankOpenAccountExample();
        BankOpenAccountExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<BankOpenAccount> list = this.bankOpenAccountMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据用户ID 查询用户银行卡信息
     *
     * @return
     */
    @Override
    public BankCard selectBankCardByUserId(Integer userId) {
        BankCardExample example = new BankCardExample();
        BankCardExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        List<BankCard> list = this.bankCardMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }


}
