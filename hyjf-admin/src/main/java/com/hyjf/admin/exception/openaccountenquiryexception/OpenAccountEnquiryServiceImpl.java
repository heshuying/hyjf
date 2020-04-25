package com.hyjf.admin.exception.openaccountenquiryexception;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.admin.OpenAccountEnquiryBean;

@Service
public class OpenAccountEnquiryServiceImpl extends BaseServiceImpl implements OpenAccountEnquiryService {

    Logger _log = LoggerFactory.getLogger(OpenAccountEnquiryServiceImpl.class);

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MqService mqService;

    @Override
    public OpenAccountEnquiryBean accountEnquiry(Map<String, String> openAccountEnquiryBean) {
    	OpenAccountEnquiryBean accountMap=this.adminAccountCustomizeQuiryMapper.accountEnquiry(openAccountEnquiryBean);
        
    	return accountMap;   
    }
    @Override
    public List<BankOpenAccountLog> bankOpenAccountLogSelect(BankOpenAccountLog bankOpenAccountLog){
        List<BankOpenAccountLog> bankOpenAccountLogs=this.adminAccountCustomizeQuiryMapper.bankOpenAccountLogSelect(bankOpenAccountLog);
        return bankOpenAccountLogs;
//        BankOpenAccountLog bankOpenAccountLogs=this.usersMapper
    }

    /**
     * 开户同步数据库
     * @param userid
     * @param account
     * @param roleId
     * @param idCard
     * @param trueName
     * @param regTimeEnd
     * @param isSetPassword
     */
    @Override
    public void updateUserAccountData(String userid, String account, Integer roleId, String idCard, String trueName, String regTimeEnd, Integer isSetPassword) {
        {
            Integer userId = Integer.parseInt(userid);
            BankOpenAccountLog openAccoutLog = this.selectBankOpenAccountLogByUserId(userid);
            if(trueName==null||"".equals(trueName)){
                if(openAccoutLog!=null){
                    trueName = openAccoutLog.getName();
                }else {
                    trueName = "";
                }
            }
            BankOpenAccountLogExample accountLogExample = new BankOpenAccountLogExample();
            accountLogExample.createCriteria().andUserIdEqualTo(userId);
            boolean deleteLogFlag = this.bankOpenAccountLogMapper.deleteByExample(accountLogExample) > 0 ? true : false;
            if (!deleteLogFlag) {
                _log.info("删除用户开户日志表失败， 用户userId:" + userId);
                //throw new RuntimeException("删除用户开户日志表失败，用户开户订单号：" + orderId + ",用户userId:" + userId);
            }
            // 查询返回的电子账号是否已开户
            boolean result = checkAccount(account);
            if (!result) {// 校验未通过
                _log.info("==========该电子账号已被用户关联,无法完成掉单修复!============关联电子账号: " + account);
            }
            Users user = this.getUsersByUserId(userId);// 获取用户信息
            String userName = user.getUsername();
            int sexInt = Integer.parseInt(idCard.substring(16, 17));// 性别
            if (sexInt % 2 == 0) {
                sexInt = 2;
            } else {
                sexInt = 1;
            }
            String birthDayTemp = idCard.substring(6, 14);// 出生日期
            String birthDay = StringUtils.substring(birthDayTemp, 0, 4) + "-" + StringUtils.substring(birthDayTemp, 4, 6) + "-" + StringUtils.substring(birthDayTemp, 6, 8);
            user.setBankOpenAccount(1);
            user.setBankAccountEsb(1);
            user.setRechargeSms(0);
            user.setWithdrawSms(0);
            user.setUserType(0);
            user.setVersion(user.getVersion().add(new BigDecimal("1")));
            _log.info("用户设置交易密码状态："+isSetPassword);
            user.setIsSetPassword(isSetPassword);
            // 更新相应的用户表
            boolean usersFlag = usersMapper.updateByPrimaryKeySelective(user) > 0 ? true : false;
            if (!usersFlag) {
                throw new RuntimeException("更新用户表失败！");
            }
            UsersInfoExample uiexample = new UsersInfoExample();
            UsersInfoExample.Criteria crtui = uiexample.createCriteria();
            crtui.andUserIdEqualTo(userId);
            List<UsersInfo> userInfos = usersInfoMapper.selectByExample(uiexample);
            if (userInfos == null) {
                throw new RuntimeException("用户详情表数据错误，用户id：" + user.getUserId());
            } else if (userInfos.size() != 1) {
                throw new RuntimeException("用户详情表数据错误，用户id：" + user.getUserId());
            }
            UsersInfo usersInfo = userInfos.get(0);
            usersInfo.setTruename(trueName);
            usersInfo.setIdcard(idCard);
            usersInfo.setSex(sexInt);
            usersInfo.setBirthday(birthDay);
            usersInfo.setTruenameIsapprove(1);
            usersInfo.setMobileIsapprove(1);
            if (roleId!=null && roleId!=0){
                usersInfo.setRoleId(roleId);
            }
            // 更新用户详细信息表
            boolean userInfoFlag = usersInfoMapper.updateByPrimaryKeySelective(usersInfo) > 0 ? true : false;
            if (!userInfoFlag) {
                throw new RuntimeException("更新用户详情表失败！");
            }

            // 插入关联表
            BankOpenAccount openAccount = new BankOpenAccount();
            openAccount.setUserId(userId);
            openAccount.setUserName(user.getUsername());
            openAccount.setAccount(account);
            openAccount.setCreateTime(GetDate.str2Date(regTimeEnd,GetDate.yyyyMMdd));
            openAccount.setCreateUserId(userId);
            openAccount.setCreateUserName(userName);
            BankOpenAccount userAccount = getAccountByUserId(userId);
            if(userAccount!=null){
                openAccount.setId(userAccount.getId());
                boolean openAccountFlag = this.bankOpenAccountMapper.updateByPrimaryKeySelective(openAccount) > 0 ? true : false;
                if (!openAccountFlag) {
                    throw new RuntimeException("插入用户开户表失败！");
                }
            }else{
                boolean openAccountFlag = this.bankOpenAccountMapper.insertSelective(openAccount) > 0 ? true : false;
                if (!openAccountFlag) {
                    throw new RuntimeException("插入用户开户表失败！");
                }
            }
            // 开户更新开户渠道统计开户时间
            AppChannelStatisticsDetailExample example = new AppChannelStatisticsDetailExample();
            AppChannelStatisticsDetailExample.Criteria crt = example.createCriteria();
            crt.andUserIdEqualTo(userId);
            List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper.selectByExample(example);
            if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
                AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
                channelDetail.setOpenAccountTime(GetDate.str2Date(regTimeEnd,GetDate.yyyyMMdd));
                this.appChannelStatisticsDetailMapper.updateByPrimaryKeySelective(channelDetail);
            }

            // add 合规数据上报 埋点 liubin 20181122 start
            // 推送数据到MQ 开户 出借人
            if("1".equals(roleId+"")){
                JSONObject params = new JSONObject();
                params.put("userId", userId);
                this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.OPEN_ACCOUNT_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
            }
            // add 合规数据上报 埋点 liubin 20181122 end

            // add by liuyang 20180227 开户掉单处理成功之后 发送法大大CA认证MQ  start
            // 加入到消息队列
            Map<String, String> params = new HashMap<String, String>();
            params.put("mqMsgId", GetCode.getRandomCode(10));
            params.put("userId", String.valueOf(userId));
            rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_CERTIFICATE_AUTHORITY, JSONObject.toJSONString(params));
            // add by liuyang 20180227 开户掉单处理成功之后 发送法大大CA认证MQ  end
        }
    }


    private BankOpenAccountLog selectBankOpenAccountLogByUserId(String userid) {
        BankOpenAccountLogExample openAccoutLogExample = new BankOpenAccountLogExample();
        BankOpenAccountLogExample.Criteria crt = openAccoutLogExample.createCriteria();
        crt.andUserIdEqualTo(Integer.parseInt(userid));
        List<BankOpenAccountLog> openAccountLogs = this.bankOpenAccountLogMapper.selectByExample(openAccoutLogExample);
        if (openAccountLogs != null && openAccountLogs.size() == 1) {
            return openAccountLogs.get(0);
        }
        return null;
    }

    private BankOpenAccount getAccountByUserId(Integer userId) {
        BankOpenAccountExample example = new BankOpenAccountExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<BankOpenAccount> bankOpenList = this.bankOpenAccountMapper.selectByExample(example);
        if (bankOpenList != null && bankOpenList.size() > 0) {
            return bankOpenList.get(0);
        }
        return null;
    }

    /**
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
    public Users getUsersByUserId(Integer userId) {
        if (userId != null) {
            UsersExample example = new UsersExample();
            example.createCriteria().andUserIdEqualTo(userId);
            List<Users> usersList = this.usersMapper.selectByExample(example);
            if (usersList != null && usersList.size() > 0) {
                return usersList.get(0);
            }
        }
        return null;
    }

    /**
     * 校验返回的电子账号是否已被使用
     *
     * @param account
     * @return
     */
    private boolean checkAccount(String account) {
        // 根据account查询用户是否开户
        BankOpenAccountExample example = new BankOpenAccountExample();
        example.createCriteria().andAccountEqualTo(account);
        List<BankOpenAccount> bankOpenList = this.bankOpenAccountMapper.selectByExample(example);
        if (bankOpenList != null && bankOpenList.size() > 0) {
            for (int i = 0; i < bankOpenList.size(); i++) {
                Integer userId = bankOpenList.get(i).getUserId();
                UsersExample userExample = new UsersExample();
                userExample.createCriteria().andUserIdEqualTo(userId);
                List<Users> user = this.usersMapper.selectByExample(userExample);
                if (user != null && user.size() > 0) {
                    for (int j = 0; j < user.size(); j++) {
                        Users info = user.get(j);
                        Integer bankOpenFlag = info.getBankOpenAccount();
                        if (bankOpenFlag == 1) {
                            return false;
                        }
                    }
                }
            }

        }
        return true;
    }

}
