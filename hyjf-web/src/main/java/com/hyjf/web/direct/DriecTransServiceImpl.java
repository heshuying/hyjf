package com.hyjf.web.direct;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountDirectionalTransfer;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedLog;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedLogExample;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecords;
import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecordsExample;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsCodeExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.web.BaseServiceImpl;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.user.regist.UserRegistDefine;

@Service
public class DriecTransServiceImpl extends BaseServiceImpl implements DriecTransService {

    /**
     * 获取用户信息通过用户id
     * @param userId
     * @return
     * @author Michael
     */

    @Override
    public WebViewUser getWebViewUserByUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        Users user = usersMapper.selectByPrimaryKey(userId);
        WebViewUser result = new WebViewUser();
        result.setUserId(user.getUserId());
        result.setUsername(user.getUsername());
        if (StringUtils.isNotBlank(user.getMobile())) {
            result.setMobile(user.getMobile());
        }
        if (StringUtils.isNotBlank(user.getIconurl())) {
            result.setIconurl(user.getIconurl());
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            result.setEmail(user.getEmail());
        }
        if (user.getOpenAccount() != null) {
            if (user.getOpenAccount().intValue() == 1) {
                result.setOpenAccount(true);
            } else {
                result.setOpenAccount(false);
            }
        }
        result.setRechargeSms(user.getRechargeSms());
        result.setWithdrawSms(user.getWithdrawSms());
        result.setInvestSms(user.getInvestSms());
        result.setRecieveSms(user.getRecieveSms());

        UsersInfoExample usersInfoExample = new UsersInfoExample();
        usersInfoExample.createCriteria().andUserIdEqualTo(userId);
        List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoExample);
        if (usersInfoList != null && usersInfoList.size() > 0) {
            result.setSex(usersInfoList.get(0).getSex());
            if (StringUtils.isNotBlank(usersInfoList.get(0).getNickname())) {
                result.setNickname(usersInfoList.get(0).getNickname());
            }
            if (StringUtils.isNotBlank(usersInfoList.get(0).getTruename())) {
                result.setTruename(usersInfoList.get(0).getTruename());
            }
            if (StringUtils.isNotBlank(usersInfoList.get(0).getIdcard())) {
                result.setIdcard(usersInfoList.get(0).getIdcard());
            }
        }
        result.setRoleId(usersInfoList.get(0).getRoleId() + "");
        //update by jijun 2018/04/09 合规接口改造一期
        //result.setPaymentAuthStatus(user.getPaymentAuthStatus());

        AccountChinapnrExample chinapnrExample = new AccountChinapnrExample();
        chinapnrExample.createCriteria().andUserIdEqualTo(userId);
        List<AccountChinapnr> chinapnrList = accountChinapnrMapper.selectByExample(chinapnrExample);
        if (chinapnrList != null && chinapnrList.size() > 0) {
            result.setChinapnrUsrid(chinapnrList.get(0).getChinapnrUsrid());
            result.setChinapnrUsrcustid(chinapnrList.get(0).getChinapnrUsrcustid());
        }
        return result;

    }

    /**
     * 获取用户信息 通过用户名
     * @param username
     * @return
     * @author Michael
     */

    @Override
    public WebViewUser getWebViewUserByUserName(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        Users user = null;
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andUsernameEqualTo(username);
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        if (usersList != null && usersList.size() > 0) {
            user = usersList.get(0);
        }
        if (user == null) {
            return null;
        }
        WebViewUser result = new WebViewUser();
        result.setUserId(user.getUserId());
        result.setUsername(user.getUsername());
        if (StringUtils.isNotBlank(user.getMobile())) {
            result.setMobile(user.getMobile());
        }
        if (StringUtils.isNotBlank(user.getIconurl())) {
            result.setIconurl(user.getIconurl());
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            result.setEmail(user.getEmail());
        }
        if (user.getOpenAccount() != null) {
            if (user.getOpenAccount().intValue() == 1) {
                result.setOpenAccount(true);
            } else {
                result.setOpenAccount(false);
            }
        }
        result.setRechargeSms(user.getRechargeSms());
        result.setWithdrawSms(user.getWithdrawSms());
        result.setInvestSms(user.getInvestSms());
        result.setRecieveSms(user.getRecieveSms());
		//update by jijun 2018/04/09 合规接口改造一期
        //result.setPaymentAuthStatus(user.getPaymentAuthStatus());

        UsersInfoExample usersInfoExample = new UsersInfoExample();
        usersInfoExample.createCriteria().andUserIdEqualTo(user.getUserId());
        List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoExample);
        if (usersInfoList != null && usersInfoList.size() > 0) {
            result.setSex(usersInfoList.get(0).getSex());
            if (StringUtils.isNotBlank(usersInfoList.get(0).getNickname())) {
                result.setNickname(usersInfoList.get(0).getNickname());
            }
            if (StringUtils.isNotBlank(usersInfoList.get(0).getTruename())) {
                result.setTruename(usersInfoList.get(0).getTruename());
            }
            if (StringUtils.isNotBlank(usersInfoList.get(0).getIdcard())) {
                result.setIdcard(usersInfoList.get(0).getIdcard());
            }
        }
        result.setRoleId(usersInfoList.get(0).getRoleId() + "");

        AccountChinapnrExample chinapnrExample = new AccountChinapnrExample();
        chinapnrExample.createCriteria().andUserIdEqualTo(user.getUserId());
        List<AccountChinapnr> chinapnrList = accountChinapnrMapper.selectByExample(chinapnrExample);
        if (chinapnrList != null && chinapnrList.size() > 0) {
            result.setChinapnrUsrid(chinapnrList.get(0).getChinapnrUsrid());
            result.setChinapnrUsrcustid(chinapnrList.get(0).getChinapnrUsrcustid());
        }
        return result;

    }

    /**
     * 根据企业账户id获取绑定用户
     * @param userId
     * @return
     * @author Michael
     */

    @Override
    public DirectionalTransferAssociatedRecords getDirectByOutUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        DirectionalTransferAssociatedRecordsExample example = new DirectionalTransferAssociatedRecordsExample();
        DirectionalTransferAssociatedRecordsExample.Criteria cra = example.createCriteria();
        cra.andTurnOutUserIdEqualTo(userId);
        cra.andAssociatedStateEqualTo(1);// 绑定成功账户
        List<DirectionalTransferAssociatedRecords> dList =
                directionalTransferAssociatedRecordsMapper.selectByExample(example);
        if (dList != null && dList.size() > 0) {
            return dList.get(0);
        }
        return null;
    }
    
    /**
     * 根据企业账户id获取绑定用户
     * @param userId
     * @return
     * @author Michael
     */

    @Override
    public DirectionalTransferAssociatedRecords getDirectByOutUserId(Integer userId,Integer associatedState) {
        if (userId == null) {
            return null;
        }
        DirectionalTransferAssociatedRecordsExample example = new DirectionalTransferAssociatedRecordsExample();
        DirectionalTransferAssociatedRecordsExample.Criteria cra = example.createCriteria();
        cra.andTurnOutUserIdEqualTo(userId);
        cra.andAssociatedStateEqualTo(associatedState);// 绑定成功账户
        List<DirectionalTransferAssociatedRecords> dList =
                directionalTransferAssociatedRecordsMapper.selectByExample(example);
        if (dList != null && dList.size() > 0) {
            return dList.get(0);
        }
        return null;
    }

    /**
     * 根据企业账户客户号 获取绑定用户
     * @param turnOutChinapnrUsrcustid
     * @return
     * @author Michael
     */
    public DirectionalTransferAssociatedRecords getDirectByOutChinapnrUsrcustid(Long turnOutChinapnrUsrcustid) {
        if (turnOutChinapnrUsrcustid == null) {
            return null;
        }
        DirectionalTransferAssociatedRecordsExample example = new DirectionalTransferAssociatedRecordsExample();
        DirectionalTransferAssociatedRecordsExample.Criteria cra = example.createCriteria();
        cra.andTurnOutChinapnrUsrcustidEqualTo(turnOutChinapnrUsrcustid);
        List<DirectionalTransferAssociatedRecords> dList =
                directionalTransferAssociatedRecordsMapper.selectByExample(example);
        if (dList != null && dList.size() > 0) {
            return dList.get(0);
        }
        return null;
    }

    /**
     * 绑定用户
     * @param bean
     * @return
     * @author Michael
     */
    @Override
    public int insertBindUser(DriecTransBean bean) {
        int recordId = 0;
        DirectionalTransferAssociatedRecords record = null;
        record = getDirectByOutUserId(bean.getOutUserId());
        if (record == null) {
            record = new DirectionalTransferAssociatedRecords();
            WebViewUser outUser = getWebViewUserByUserId(bean.getOutUserId());
            WebViewUser inUser = getWebViewUserByUserId(bean.getInUserId());
            record.setTurnOutUserId(outUser.getUserId());
            record.setTurnOutUsername(outUser.getUsername());
            record.setTurnOutMobile(outUser.getMobile());
            record.setTurnOutChinapnrUsrcustid(outUser.getChinapnrUsrcustid());
            record.setShiftToChinapnrUsrcustid(inUser.getChinapnrUsrcustid());
            record.setShiftToMobile(inUser.getMobile());
            record.setShiftToUserId(inUser.getUserId());
            record.setShiftToUsername(inUser.getUsername());
            record.setAssociatedState(0);// 初始状态
            record.setAssociatedTime(GetDate.getDate());
            this.directionalTransferAssociatedRecordsMapper.insertSelective(record);
            recordId = record.getId();
        } else {
            record.setAssociatedState(0);// 初始状态
            record.setAssociatedTime(GetDate.getDate());
            this.directionalTransferAssociatedRecordsMapper.updateByPrimaryKeySelective(record);
            recordId = record.getId();
        }
        return recordId;
    }

    /**
     * 绑定用户回调
     * @param bean
     * @return
     * @author Michael
     */
    @Override
    public void insertBindUserReturn(ChinapnrBean bean) {
        DirectionalTransferAssociatedRecords record =
                getDirectByOutChinapnrUsrcustid(Long.parseLong(bean.get(ChinaPnrConstant.PARAM_USRCUSTID)));
        // 返回结果是否成功
        boolean isSuccess = ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE));
        if (record.getAssociatedState() == 0) {// 添加
            // 成功
            if (isSuccess) {
                record.setAssociatedState(1);
            } else {
                record.setAssociatedState(2);
            }
        } else if (record.getAssociatedState() == 1) { // 修改
            // 成功
            if (isSuccess) {
                WebViewUser user =
                        getWebViewUserByChinapnrUsrcustid(Long.parseLong(bean.get(ChinaPnrConstant.PARAM_INUSRCUSTID)));
                record.setShiftToChinapnrUsrcustid(user.getChinapnrUsrcustid());
                record.setShiftToMobile(user.getMobile());
                record.setShiftToUsername(user.getUsername());
                record.setShiftToUserId(user.getUserId());
                record.setAssociatedTime(GetDate.getDate());
                record.setAssociatedState(1);
            }
        }
        this.directionalTransferAssociatedRecordsMapper.updateByPrimaryKeySelective(record);

        // 更新日志
        updateDirectLog(record.getTurnOutUserId(), isSuccess);
    }

    /**
     * 获取用户信息
     *
     * @param chinapnrUsrcustid
     * @return 用户的汇付信息
     */
    public WebViewUser getWebViewUserByChinapnrUsrcustid(Long chinapnrUsrcustid) {
        AccountChinapnr accountChinapnr = null;
        if (chinapnrUsrcustid == null) {
            return null;
        }
        AccountChinapnrExample example = new AccountChinapnrExample();
        AccountChinapnrExample.Criteria criteria = example.createCriteria();
        criteria.andChinapnrUsrcustidEqualTo(chinapnrUsrcustid);
        List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
        if (list != null && !list.isEmpty()) {
            accountChinapnr = list.get(0);
        }
        WebViewUser user = null;
        if (accountChinapnr != null) {
            user = this.getWebViewUserByUserId(accountChinapnr.getUserId());
        }
        return user;
    }

    /**
     * 执行前每个方法前需要添加BusinessDesc描述
     * @param bean
     * @return
     * @author Michael
     */

    @Override
    public int insertDirecTrans(DriecTransBean bean, String orderId) {
        int recordId = 0;
        AccountDirectionalTransfer record = new AccountDirectionalTransfer();
        WebViewUser outUser = getWebViewUserByUserId(bean.getOutUserId());
        WebViewUser inUser = getWebViewUserByUserId(bean.getInUserId());
        record.setOrderId(orderId);
        record.setTurnOutUserId(outUser.getUserId());
        record.setTurnOutUsername(outUser.getUsername());
        record.setShiftToUserId(inUser.getUserId());
        record.setShiftToUsername(inUser.getUsername());
        record.setTransferAccountsMoney(bean.getTransAmt());
        record.setTransferAccountsState(0);// 初始状态（转账中）
        record.setTransferAccountsTime(GetDate.getDate());
        record.setRemark("定向转账");
        this.accountDirectionalTransferMapper.insertSelective(record);
        recordId = record.getId();
        return recordId;

    }

    /**
     * 执行前每个方法前需要添加BusinessDesc描述
     * @param bean
     * @param recordId
     * @author Michael
     */

    @Override
    public void insertDirecTransReturn(ChinapnrBean bean, int recordId) {
        // 转账记录
        AccountDirectionalTransfer record = accountDirectionalTransferMapper.selectByPrimaryKey(recordId);
        // 增加时间
        Integer time = GetDate.getNowTime10();
        /**
         * 成功
         * 更新account表 （转出账户可用金额=可用金额-转账金额 转出账户资产总额=资产总额-转账金额 转入账户可用金额=可用金额+转账金额
         * 转入账户资产总额=资产总额+转账金额 ）
         * 资金收支列表增加2条收支明细
         * 转账状态变为成功
         * **/
        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(bean.get(ChinaPnrConstant.PARAM_RESPCODE))) {
            record.setTransferAccountsState(1);// 成功
            // 转出用户
            Account accountOut = this.getAccount(record.getTurnOutUserId());
            accountOut.setBalance(accountOut.getBalance().subtract(record.getTransferAccountsMoney()));
            accountOut.setTotal(accountOut.getTotal().subtract(record.getTransferAccountsMoney()));
            this.accountMapper.updateByPrimaryKey(accountOut);
            // 转入用户
            Account accountIn = this.getAccount(record.getShiftToUserId());
            accountIn.setBalance(accountIn.getBalance().add(record.getTransferAccountsMoney()));
            accountIn.setTotal(accountIn.getTotal().add(record.getTransferAccountsMoney()));
            this.accountMapper.updateByPrimaryKey(accountIn);
            // 资金明细
            AccountList accountListOut = new AccountList();
            accountListOut.setNid(record.getOrderId());
            accountListOut.setUserId(record.getTurnOutUserId());
            accountListOut.setAmount(record.getTransferAccountsMoney());
            accountListOut.setType(2);// 收支类型1收入2支出3冻结
            accountListOut.setTrade("directional_transfer");// 交易类型
            accountListOut.setTradeCode("balance");// 操作识别码 balance余额操作
            accountListOut.setTotal(accountOut.getTotal());// 资金总额
            accountListOut.setBalance(accountOut.getBalance());
            accountListOut.setFrost(accountOut.getFrost());
            accountListOut.setAwait(accountOut.getAwait());
            accountListOut.setRepay(accountOut.getRepay());
            accountListOut.setRemark("定向转账");
            accountListOut.setCreateTime(time);
            accountListOut.setInterest(null);
            accountListOut.setBaseUpdate(0);
            accountListOut.setIsUpdate(0);
            accountListOut.setWeb(0);
            this.accountListMapper.insertSelective(accountListOut);
            // 资金明细
            AccountList accountListIn = new AccountList();
            accountListIn.setNid(record.getOrderId());
            accountListIn.setUserId(record.getShiftToUserId());
            accountListIn.setAmount(record.getTransferAccountsMoney());
            accountListIn.setType(1);// 收支类型1收入2支出3冻结
            accountListIn.setTrade("directional_transfer");// 交易类型
            accountListIn.setTradeCode("balance");// 操作识别码 balance余额操作
            accountListIn.setTotal(accountIn.getTotal());// 资金总额
            accountListIn.setBalance(accountIn.getBalance());
            accountListIn.setFrost(accountIn.getFrost());
            accountListIn.setAwait(accountIn.getAwait());
            accountListIn.setRepay(accountIn.getRepay());
            accountListIn.setRemark("定向转账");
            accountListIn.setCreateTime(time);
            accountListIn.setInterest(null);
            accountListIn.setBaseUpdate(0);
            accountListIn.setIsUpdate(0);
            accountListIn.setWeb(0);
            this.accountListMapper.insertSelective(accountListIn);

        } else {// 失败
            record.setTransferAccountsState(2);// 失败
        }
        // 更新记录表
        this.accountDirectionalTransferMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 发送的短信验证码保存到数据库,使之前的验证码无效
     * @param mobile
     * @param code
     * @author Michael
     */
    @Override
    public int saveSmsCode(String mobile, String code, int status) {
        // 使之前的验证码无效
        SmsCodeExample example = new SmsCodeExample();
        SmsCodeExample.Criteria cra = example.createCriteria();
        cra.andMobileEqualTo(mobile);
        List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(UserRegistDefine.CKCODE_NEW);
        statusList.add(UserRegistDefine.CKCODE_YIYAN);
        cra.andStatusIn(statusList);
        List<SmsCode> codeList = smsCodeMapper.selectByExample(example);
        if (codeList != null && codeList.size() > 0) {
            for (SmsCode smsCode : codeList) {
                smsCode.setStatus(UserRegistDefine.CKCODE_FAILED);// 失效7
                smsCodeMapper.updateByPrimaryKey(smsCode);
            }
        }
        // 保存新验证码到数据库
        SmsCode smsCode = new SmsCode();
        smsCode.setCheckfor(MD5.toMD5Code(mobile + code));
        smsCode.setMobile(mobile);
        smsCode.setCheckcode(code);
        smsCode.setPosttime(GetDate.getMyTimeInMillis());
        smsCode.setStatus(status);
        smsCode.setUserId(0);
        smsCodeMapper.insertSelective(smsCode);
        return smsCode.getId();
    }

    /**
     * 校验验证码
     * @param id
     * @param code
     * @return
     * @author Michael
     */

    @Override
    public boolean checkMobileCode(int id, String code, String mobile) {
        SmsCode smsCode = smsCodeMapper.selectByPrimaryKey(id);
        if (smsCode == null) {
            SmsCodeExample example = new SmsCodeExample();
            SmsCodeExample.Criteria cra = example.createCriteria();
            cra.andMobileEqualTo(mobile);
            cra.andCheckcodeEqualTo(code);
            example.setOrderByClause("posttime desc");
            List<SmsCode> list = smsCodeMapper.selectByExample(example);
            if (list != null && list.size() > 0) {
                smsCode = smsCodeMapper.selectByExample(example).get(0);
            } else {
                return false;
            }
        }
        if (smsCode == null) {
            return false;
        }
        if (code.equals(smsCode.getCheckcode())) {
            return true;
        }
        return false;

    }

    /**
     * 执行前每个方法前需要添加BusinessDesc描述
     * @param userId
     * @return
     * @author Michael
     */

    @Override
    public DirectionalTransferAssociatedRecords getDirectByInUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        DirectionalTransferAssociatedRecordsExample example = new DirectionalTransferAssociatedRecordsExample();
        DirectionalTransferAssociatedRecordsExample.Criteria cra = example.createCriteria();
        cra.andShiftToUserIdEqualTo(userId);
        cra.andAssociatedStateEqualTo(1);// 绑定成功账户
        List<DirectionalTransferAssociatedRecords> dList =
                directionalTransferAssociatedRecordsMapper.selectByExample(example);
        if (dList != null && dList.size() > 0) {
            return dList.get(0);
        }
        return null;

    }

    /**
     * 插入日志
     * @param bean
     * @author Michael
     */

    @Override
    public void insertDirectLog(DriecTransBean bean, int updateFlag) {
        // TODO Auto-generated method stub
        DirectionalTransferAssociatedLog log = new DirectionalTransferAssociatedLog();
        WebViewUser outUser = getWebViewUserByUserId(bean.getOutUserId());
        WebViewUser inUser = getWebViewUserByUserId(bean.getInUserId());
        log.setTurnOutUserId(outUser.getUserId());
        log.setTurnOutUsername(outUser.getUsername());
        log.setTurnOutMobile(outUser.getMobile());
        log.setTurnOutChinapnrUsrcustid(outUser.getChinapnrUsrcustid());
        log.setShiftToChinapnrUsrcustid(inUser.getChinapnrUsrcustid());
        log.setShiftToMobile(inUser.getMobile());
        log.setShiftToUserId(inUser.getUserId());
        log.setShiftToUsername(inUser.getUsername());
        log.setAssociatedState(0);// 初始状态
        log.setAssociatedTime(GetDate.getDate());
        if (updateFlag == 0) {
            log.setRemark("新增绑定用户");
        } else {
            log.setRemark("修改绑定用户");
        }

        this.directionalTransferAssociatedLogMapper.insertSelective(log);
    }

    /**
     * 更新日志
     * @param bean
     * @author Michael
     */
    public void updateDirectLog(Integer userId, boolean isSuccess) {
        DirectionalTransferAssociatedLog log = null;
        DirectionalTransferAssociatedLogExample example = new DirectionalTransferAssociatedLogExample();
        DirectionalTransferAssociatedLogExample.Criteria cra = example.createCriteria();
        cra.andTurnOutUserIdEqualTo(userId);
        example.setOrderByClause(" id desc");
        List<DirectionalTransferAssociatedLog> list =
                this.directionalTransferAssociatedLogMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            log = list.get(0);
        }
        if (isSuccess) {
            log.setAssociatedState(1);
        } else {
            log.setAssociatedState(2);
        }
        this.directionalTransferAssociatedLogMapper.updateByPrimaryKeySelective(log);
    }

    @Override
    public int updateTransferAssociatedRecord(DirectionalTransferAssociatedRecords transferAssociatedRecord,
        Integer shiftInUserId) {
        WebViewUser inUser = getWebViewUserByUserId(shiftInUserId);
        transferAssociatedRecord.setShiftToChinapnrUsrcustid(inUser.getChinapnrUsrcustid());
        transferAssociatedRecord.setShiftToMobile(inUser.getMobile());
        transferAssociatedRecord.setShiftToUserId(inUser.getUserId());
        transferAssociatedRecord.setShiftToUsername(inUser.getUsername());
        transferAssociatedRecord.setAssociatedState(0);// 初始状态
        transferAssociatedRecord.setAssociatedTime(GetDate.getDate());
        return this.directionalTransferAssociatedRecordsMapper.updateByPrimaryKey(transferAssociatedRecord);
    }

}
