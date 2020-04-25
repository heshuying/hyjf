package com.hyjf.mqreceiver.crm.task;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hyjf.common.http.HttpDeal;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.SpringContextHolder;
import com.hyjf.mqreceiver.crm.utils.CheckSignUtil;
import com.hyjf.mqreceiver.crm.utils.PropUtils;
import com.hyjf.mybatis.mapper.auto.BankOpenAccountMapper;
import com.hyjf.mybatis.mapper.auto.UsersInfoMapper;
import com.hyjf.mybatis.mapper.auto.UsersMapper;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

public class BankOpenPostTask extends Thread {

    private static Logger logger = LoggerFactory.getLogger(BankOpenPostTask.class);

    private Integer userId;

    public BankOpenPostTask(Integer userId) {
        this.userId = userId;
    }

    private String CRM_INSERTCUSTOMER_ACTION_URL = PropUtils.getSystem("crm.insertCustomerAction.url");


    @Override
    public void run() {
        logger.info("crm接口调用开始");
        String result =
                HttpDeal.postJson(CRM_INSERTCUSTOMER_ACTION_URL, this.buildData().toJSONString());

        logger.info("crmurl。。。。。。。。。"+CRM_INSERTCUSTOMER_ACTION_URL);
        logger.info("crmdata。。。。。。。。。"+this.buildData().toJSONString());
        logger.info("crm接口调用结束");
    }

    private JSONObject buildData() {
        JSONObject ret = new JSONObject();
        Map<String, Object> map = Maps.newHashMap();

        UsersInfo userInfo = this.getUserInfo(userId);
        Users user = this.getUser(userId);
        BankOpenAccount account = this.getOpenInfo(userId);
        if(user.getReferrer() != null && user.getReferrer() != 0){
            UsersInfo referrerInfo = this.getUserInfo(user.getReferrer());
            Users referrerUser = this.getUser(user.getReferrer());  
            map.put("recommendCardId", referrerInfo.getIdcard());
            map.put("recommendName", referrerInfo.getTruename());
            map.put("recommondUsername", referrerUser.getUsername());
            map.put("recommendMobile", referrerUser.getMobile());
        }

        map.put("customerCardId", userInfo.getIdcard());
        map.put("availableBalance", 0);
        map.put("customerId", String.valueOf(userId));
        map.put("customerMobile", user.getMobile());
        map.put("customerName", userInfo.getTruename());
        map.put("customerUsername", user.getUsername());
        map.put("openingTimeStr", GetDate.dateToDateFormatStr(account.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        map.put("platformInstcode", 10000001);
        map.put("registerTimeStr", GetDate.timestamptoStrYYYYMMDDHHMMSS(String.valueOf(user.getRegTime())));
        map.put("recommondId", user.getReferrer());

        String sign = CheckSignUtil.encryptByRSA(map, "10000001");
        ret.put("instCode", "10000001");
        ret.put("timeStamp", String.valueOf(GetDate.getDate().getTime()));
        ret.put("object", map);
        ret.put("sign", sign);
        return ret;
    }

    // 取得用户详细信息
    private UsersInfo getUserInfo(Integer userId) {
        UsersInfoMapper usersInfoMapper = SpringContextHolder.getBean(UsersInfoMapper.class);
        UsersInfoExample userInfoExample = new UsersInfoExample();
        userInfoExample.createCriteria().andUserIdEqualTo(userId);
        return usersInfoMapper.selectByExample(userInfoExample).get(0);
    }

    // 取得用户详信息
    private Users getUser(Integer userId) {
        UsersMapper usersMapper = SpringContextHolder.getBean(UsersMapper.class);
        UsersExample userExample = new UsersExample();
        userExample.createCriteria().andUserIdEqualTo(userId);
        return usersMapper.selectByExample(userExample).get(0);
    }

    // 取得用户开户信息
    private BankOpenAccount getOpenInfo(Integer userId) {
        BankOpenAccountMapper bankOpenAccountMapper = SpringContextHolder.getBean(BankOpenAccountMapper.class);
        BankOpenAccountExample bankOpenAccountExample = new BankOpenAccountExample();
        bankOpenAccountExample.createCriteria().andUserIdEqualTo(userId);
        return bankOpenAccountMapper.selectByExample(bankOpenAccountExample).get(0);
    }
}
