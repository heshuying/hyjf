package com.hyjf.app.setmsgmail;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Users;

/**
 * Created by yaoyong on 2017/12/7.
 */

@Service
public class MsgMailServiceImpl extends BaseServiceImpl implements MsgMailService {

    @Override
    public void updateStatusByUserId(Integer userId, String smsOpenStatus, String emailOpenStatus) {
        Users user = usersMapper.selectByPrimaryKey(userId);
        if (user != null) {
            if("0".equals(emailOpenStatus)){
                user.setIsSmtp(1);
            }else{
                user.setIsSmtp(0);
            }
            if("0".equals(smsOpenStatus)){
                user.setWithdrawSms(1);
                user.setInvestSms(1);
                user.setRechargeSms(1);
                user.setRecieveSms(1);
            }else{
                user.setWithdrawSms(0);
                user.setInvestSms(0);
                user.setRechargeSms(0);
                user.setRecieveSms(0);
            }
            
            usersMapper.updateByPrimaryKeySelective(user);
        }

    }
}
