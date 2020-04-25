package com.hyjf.bank.service.user.accountopenpage;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyjf.bank.service.mq.MqService;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.*;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.bank.service.user.bankopen.BankOpenService;
import com.hyjf.common.validator.Validator;
import com.hyjf.common.validator.ValidatorCheckUtil;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class UserOpenAccountPageServiceImpl extends BaseServiceImpl implements UserOpenAccountPageService {

    Logger _log = LoggerFactory.getLogger(UserOpenAccountPageService.class);
    
    @Autowired
    private BankOpenService openAccountService;

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MqService mqService;
    
    @Override
    public ModelAndView getCallbankMV(OpenAccountPageBean openBean) {
        ModelAndView mv = new ModelAndView();
        // 根据身份证号码获取性别
        String gender = "M";
        /*int sexInt = Integer.parseInt(openBean.getIdNo().substring(16, 17));// 性别   奇数为男，偶数为女
        if (sexInt % 2 == 0) {
            gender = "F";
        } else {
            gender = "M";
        }*/
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String bankInstCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String orderDate = GetOrderIdUtils.getOrderDate();
        String txDate = GetOrderIdUtils.getTxDate();
        String txTime = GetOrderIdUtils.getTxTime();
        String seqNo = GetOrderIdUtils.getSeqNo(6);
        String idType = BankCallConstant.ID_TYPE_IDCARD;
        // 调用开户接口
        BankCallBean openAccoutBean = new BankCallBean();
        openAccoutBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        openAccoutBean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_OPEN_PAGE);// 消息类型(用户开户)
        openAccoutBean.setInstCode(bankInstCode);// 机构代码
        openAccoutBean.setBankCode(bankCode);
        openAccoutBean.setTxDate(txDate);
        openAccoutBean.setTxTime(txTime);
        openAccoutBean.setSeqNo(seqNo);
        openAccoutBean.setChannel(openBean.getChannel());
        openAccoutBean.setIdType(idType);
        openAccoutBean.setName(openBean.getTrueName());
        openAccoutBean.setGender(gender);
        openAccoutBean.setMobile(openBean.getMobile());

        // 代偿角色的账户类型为  00100-担保账户  其他的是 00000-普通账户
        if(openBean.getIdentity().equals("3")){
            openAccoutBean.setAcctUse(BankCallConstant.ACCOUNT_USE_GUARANTEE);
        }else{
            openAccoutBean.setAcctUse(BankCallConstant.ACCOUNT_USE_COMMON);
        }
//        openAccoutBean.setAcctUse(openBean.getAcctUse());

        openAccoutBean.setIdentity(openBean.getIdentity());
        openAccoutBean.setRetUrl(openBean.getRetUrl());
        openAccoutBean.setNotifyUrl(openBean.getNotifyUrl()+"&openclient="+openBean.getPlatform());
        openAccoutBean.setSuccessfulUrl(openBean.getRetUrl()+"&isSuccess=1");
        openAccoutBean.setCoinstName(openBean.getCoinstName());
        // 银行卡号
        openAccoutBean.setCardNo(openBean.getCardNo());
        
        // 页面调用必须传的
        String orderId = GetOrderIdUtils.getOrderId2(openBean.getUserId());
        openAccoutBean.setLogBankDetailUrl(BankCallConstant.BANK_URL_ACCOUNT_OPEN_PAGE);
        openAccoutBean.setLogOrderId(orderId);
        openAccoutBean.setLogOrderDate(orderDate);
        openAccoutBean.setLogUserId(String.valueOf(openBean.getUserId()));
        openAccoutBean.setLogRemark("外部服务接口:开户页面");
        openAccoutBean.setLogIp(openBean.getIp());
        openAccoutBean.setLogClient(Integer.parseInt(openBean.getPlatform()));
        
        // 跳转到汇付天下画面
        openBean.setOrderId(orderId);
        _log.info("openAccoutBean:"+JSONObject.toJSONString(openAccoutBean));
        try {
            mv = BankCallUtils.callApi(openAccoutBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @Override
    public boolean updateCardNoToBank(BankCallBean bankCallBean) {
        boolean ok = false;
        Integer userId = Integer.parseInt(bankCallBean.getLogUserId());
        Users user = this.getUsers(userId);
        // 调用汇付接口(4.2.2 用户绑卡接口)
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_BIND_DETAILS_QUERY);
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
        bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setAccountId(bankCallBean.getAccountId());// 存管平台分配的账号
        bean.setState("1"); // 查询状态 0-所有（默认） 1-当前有效的绑定卡
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        bean.setLogUserId(String.valueOf(userId));
        // 调用汇付接口 4.4.11 银行卡查询接口
        BankCallBean call = BankCallUtils.callApiBg(bean);
        System.out.println( JSONObject.toJSONString(call, true));
        String respCode = call == null ? "" : call.getRetCode();
        // 如果接口调用成功
        if (BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
            String usrCardInfolist = call.getSubPacks();
            JSONArray array = JSONObject.parseArray(usrCardInfolist);
            JSONObject obj = null;
            System.out.println(array.size()+"           array:"+array.toString());
            if(array != null && array.size() != 0){
                obj = array.getJSONObject(0);
            }
            System.out.println("====="+obj.getString("cardNo"));
            BankCard bank = new BankCard();
            bank.setUserId(userId);
            // 设置绑定的手机号
            bank.setMobile(bankCallBean.getMobile());
            bank.setUserName(user.getUsername());
            bank.setStatus(1);// 默认都是1
            bank.setCardNo(obj.getString("cardNo"));
            // 根据银行卡号查询银行ID
            String bankId = this.getBankIdByCardNo(obj.getString("cardNo"));
            bank.setBankId(bankId == null ? 0 : Integer.valueOf(bankId));
            bank.setBank(bankId == null ? "" : this.getBankNameById(bankId));
            // 银行联号
            String payAllianceCode = "";
            // 调用江西银行接口查询银行联号
            BankCallBean payAllianceCodeQueryBean = this.payAllianceCodeQuery(obj.getString("cardNo"), userId);
            if (payAllianceCodeQueryBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(payAllianceCodeQueryBean.getRetCode())) {
                payAllianceCode = payAllianceCodeQueryBean.getPayAllianceCode();
            }
            // 如果此时银联行号还是为空,根据bankId查询本地存的银联行号
            if (StringUtils.isBlank(payAllianceCode)) {
                if(!StringUtils.isBlank(payAllianceCode)){
                    payAllianceCode = this.getPayAllianceCodeByBankId(bankId);
                }
            }
            bank.setPayAllianceCode(payAllianceCode);
            bankCallBean.setPayAllianceCode(payAllianceCode);
            // 三方接口回调优化：增加返回手机号和银行卡号 add by liushouyi 20180821
            bankCallBean.setCardNo(obj.getString("cardNo"));
            SimpleDateFormat sdf = GetDate.yyyymmddhhmmss;
            try {
                bank.setCreateTime(sdf.parse(obj.getString("txnDate") + obj.getString("txnTime")));
            } catch (ParseException e) {
            }
            bank.setCreateUserId(userId);
            bank.setCreateUserName(user.getUsername());
            ok = bankCardMapper.insertSelective(bank) > 0 ? true : false;
        }
        return ok;
    }

    @Override
    public boolean updateUserRoleId(int userId, int roleId) {
        UsersInfoExample uiexample = new UsersInfoExample();
        UsersInfoExample.Criteria crtui = uiexample.createCriteria();
        crtui.andUserIdEqualTo(userId);
        List<UsersInfo> userInfos = usersInfoMapper.selectByExample(uiexample);
        if (userInfos == null) {
            throw new RuntimeException("更新用户实行时候用户详情表数据错误，用户id：" + userId);
        } else if (userInfos.size() != 1) {
            throw new RuntimeException("更新用户实行时候用户详情表数据错误，用户id：" + userId);
        }
        UsersInfo usersInfo = userInfos.get(0);
        usersInfo.setRoleId(roleId);
        // 更新用户详细信息表
        boolean userInfoFlag = usersInfoMapper.updateByPrimaryKeySelective(usersInfo) > 0 ? true : false;
        if (!userInfoFlag) {
            throw new RuntimeException("更新用户实行时候更新用户详情表失败！");
        }
        return true;
    }

    @Override
    public Map<String, String> checkParm(Integer userId, String mobile, String name) {
        // 手机号
        if (Validator.isNull(mobile)) {
            return getErrorMsg("0", "手机号不能为空！");
        }
        // 姓名
        if (Validator.isNull(name)) {
            return getErrorMsg("0", "真实姓名不能为空！");
        }else{
            //判断真实姓名是否包含空格
            if (!ValidatorCheckUtil.verfiyChinaFormat(name)) {
                return getErrorMsg("0", "真实姓名不能包含空格！");
            }
            //判断真实姓名的长度,不能超过10位
            if (name.length() > 10) {
                return getErrorMsg("0", "真实姓名不能超过10位！");
            }
        }
        if(!Validator.isMobile(mobile)){
            return getErrorMsg("0", "手机号格式错误！");
        }
        /*// 产品说 开户手机号不是必须用注册的 直接用用户传过来的
        String phone = this.openAccountService.getUsersMobile(userId);
        if (StringUtils.isBlank(phone)) {
            if (StringUtils.isNotBlank(phone)) {
                if(openAccountService.existMobile(mobile)){
                    return getErrorMsg("0", "用户信息错误，手机号码重复！");
                }else{
                    mobile = phone;
                }
            } else {
                return getErrorMsg("0", "用户信息错误，未获取到用户的手机号码！");
            }
        } else {
            if (StringUtils.isNotBlank(mobile) && !mobile.equals(phone)) {
                return getErrorMsg("0", "用户信息错误，用户的手机号码错误！");
            }
        }*/
        return getErrorMsg("1", "成功！");
    }

    private Map<String, String> getErrorMsg(String success, String message) {
        Map<String, String> result = new HashMap<String, String>();
        result.put("success", success);
        result.put("message", message);
        return result;
    }

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
        String idNo = openAccountResult.getIdNo();
        _log.info("用户ID:" + userId + "],用户名:[" + userName + "],用户身份证号:[" + idNo + "]");
        // 真实姓名
        String trueName = openAccountResult.getName();
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
        // 开户+设密的话   状态改为已设置交易密码
        if (BankCallConstant.TXCODE_ACCOUNT_OPEN_ENCRYPT_PAGE.equals(openAccountResult.getTxCode())
                && "1".equals(openAccountResult.getStatus())) {
            user.setIsSetPassword(1);
        }
        user.setBankAccountEsb(openAccountResult.getLogClient());// 开户平台
        user.setRechargeSms(0);
        user.setWithdrawSms(0);
        user.setUserType(0);
        //user.setMobile(openAccoutLog.getMobile());
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
        // 更新角色
        userInfo.setRoleId(Integer.parseInt(openAccountResult.getIdentity()));
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

        // add by liuyang 神策数据统计追加 20180927 start
        if ("10000000".equals(user.getInstCode())) {
            if (!RedisUtils.exists("SENSORS_DATA_OPEN_ACCOUNT:" + userId)) {
                try {
                    RedisUtils.sadd("SENSORS_DATA_OPEN_ACCOUNT:" + userId, String.valueOf(userId));
                    SensorsDataBean sensorsDataBean = new SensorsDataBean();
                    sensorsDataBean.setEventCode("open_success");
                    sensorsDataBean.setUserId(userId);
                    this.sendSensorsDataMQ(sensorsDataBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // add by liuyang 神策数据统计追加 20180927 end

        // add 合规数据上报 埋点 liubin 20181122 start
        // 推送数据到MQ 开户 出借人
        if("1".equals(openAccountResult.getIdentity())){
            JSONObject params = new JSONObject();
            params.put("userId", userId);
            this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.OPEN_ACCOUNT_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
        }
        // add 合规数据上报 埋点 liubin 20181122 end
        return openAccountFlag;
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
    
    private BankOpenAccountLog checkBankOpenAccountLog(Integer userId, String orderId) {
        BankOpenAccountLogExample example = new BankOpenAccountLogExample();
        BankOpenAccountLogExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        cra.andOrderIdEqualTo(orderId);
        List<BankOpenAccountLog> list = this.bankOpenAccountLogMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public ModelAndView getCallbankEncryptPageMV(OpenAccountPageBean openBean) {

        ModelAndView mv = new ModelAndView();
        // 根据身份证号码获取性别
        String gender = "F";
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String bankInstCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String orderDate = GetOrderIdUtils.getOrderDate();
        String txDate = GetOrderIdUtils.getTxDate();
        String txTime = GetOrderIdUtils.getTxTime();
        String seqNo = GetOrderIdUtils.getSeqNo(6);
        String idType = BankCallConstant.ID_TYPE_IDCARD;
        // 调用开户接口
        BankCallBean openAccoutBean = new BankCallBean();
        openAccoutBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        openAccoutBean.setTxCode(BankCallConstant.TXCODE_ACCOUNT_OPEN_ENCRYPT_PAGE);
        openAccoutBean.setInstCode(bankInstCode);// 机构代码
        openAccoutBean.setBankCode(bankCode);
        openAccoutBean.setTxDate(txDate);
        openAccoutBean.setTxTime(txTime);
        openAccoutBean.setSeqNo(seqNo);
        openAccoutBean.setChannel(openBean.getChannel());
        openAccoutBean.setIdType(idType);
        openAccoutBean.setIdNo(openBean.getIdNo());
        openAccoutBean.setName(openBean.getTrueName());
        openAccoutBean.setGender(gender);
        openAccoutBean.setMobile(openBean.getMobile());

        // 代偿角色的账户类型为  00100-担保账户  其他的是 00000-普通账户
        if(openBean.getIdentity().equals("3")){
            openAccoutBean.setAcctUse(BankCallConstant.ACCOUNT_USE_GUARANTEE);
        }else{
            openAccoutBean.setAcctUse(BankCallConstant.ACCOUNT_USE_COMMON);
        }
//        openAccoutBean.setAcctUse(openBean.getAcctUse());

        openAccoutBean.setIdentity(openBean.getIdentity());
        openAccoutBean.setRetUrl(openBean.getRetUrl());
        openAccoutBean.setNotifyUrl(openBean.getNotifyUrl()+"&openclient="+openBean.getPlatform()+"&roleId="+openBean.getIdentity());
        openAccoutBean.setSuccessfulUrl(openBean.getRetUrl()+"&isSuccess=1");
        openAccoutBean.setCoinstName(openBean.getCoinstName());
        // 页面调用必须传的
        String orderId = GetOrderIdUtils.getOrderId2(openBean.getUserId());
        openAccoutBean.setLogBankDetailUrl(BankCallConstant.BANK_URL_ACCOUNT_OPEN_ENCRYPT_PAGE);
        openAccoutBean.setLogOrderId(orderId);
        openAccoutBean.setLogOrderDate(orderDate);
        openAccoutBean.setLogUserId(String.valueOf(openBean.getUserId()));
        openAccoutBean.setLogRemark("外部服务接口:开户+设密码页面");
        openAccoutBean.setLogIp(openBean.getIp());
        openAccoutBean.setLogClient(Integer.parseInt(openBean.getPlatform()));
        
        // 跳转到汇付天下画面
        openBean.setOrderId(orderId);
        try {
            mv = BankCallUtils.callApi(openAccoutBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    
    }

    /**
     * 根据用户ID查询银行卡
     *
     * @param userid
     * @return
     */
    @Override
    public BankCard getBankCardByUserId(String userid) {
        BankCardExample example = new BankCardExample();
        BankCardExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(Integer.parseInt(userid));
        List<BankCard> list = this.bankCardMapper.selectByExample(example);
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 开户成功后,发送神策数据统计MQ
     *
     * @param sensorsDataBean
     */
    @Override
    public void sendSensorsDataMQ(SensorsDataBean sensorsDataBean) {
        // 加入到消息队列
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eventCode", sensorsDataBean.getEventCode());
        params.put("userId", sensorsDataBean.getUserId());
        params.put("mqMsgId", GetCode.getRandomCode(10));
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.SENSORS_DATA_ROUTINGKEY_OPEN_ACCOUNT, JSONObject.toJSONString(params));
    }
}
