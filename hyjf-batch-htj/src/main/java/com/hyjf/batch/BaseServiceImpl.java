package com.hyjf.batch;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsLogWithBLOBs;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;

public class BaseServiceImpl extends CustomizeMapper implements BaseService {
    /**
     * 添加短信记录表
     *
     * @param content
     *            短信内容
     * @param mobile
     *            手机号码
     * @param checkCode
     *            验证码(没有验证码，设为空)
     * @param remark
     *            发送备注，如 注册填写“注册”
     * @param status
     *            发送状态，第三方返回状态
     * @return
     */

    public boolean AddMessage(String content, String mobile, String checkCode, String remark, int status) {

        if (StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(content)) {
            // 带验证码
            if (checkCode != null) {
                SmsCode smsCode = new SmsCode();
                smsCode.setMobile(mobile);
                smsCode.setCheckfor(MD5.toMD5Code(mobile + checkCode));
                smsCode.setCheckcode(checkCode);
                smsCode.setPosttime(GetDate.getNowTime10());
                smsCode.setStatus(status);
                smsCodeMapper.insertSelective(smsCode);
            }

            // 插入短信记录
            SmsLogWithBLOBs smsLog = new SmsLogWithBLOBs();

            smsLog.setMobile(mobile);
            smsLog.setContent(content);
            smsLog.setPosttime(GetDate.getNowTime10());
            smsLog.setStatus(status);
            smsLog.setType(remark);
            smsLogMapper.insertSelective(smsLog);
            return true;
        }
        return false;
    }

    /**
     * 写入日志
     *
     * @return
     */
    public int insertChinapnrLog(ChinapnrLog chinapnrLog) {
        return chinapnrLogMapper.insertSelective(chinapnrLog);
    }

    /**
     * 获取用户在汇付天下的账号信息
     *
     * @return
     */
    public AccountChinapnr getChinapnrUserInfo(Integer userId) {
        if (userId != null) {
            AccountChinapnrExample example = new AccountChinapnrExample();
            AccountChinapnrExample.Criteria criteria = example.createCriteria();
            criteria.andUserIdEqualTo(userId);
            List<AccountChinapnr> list = this.accountChinapnrMapper.selectByExample(example);
            if (list != null && list.size() == 1) {
                return list.get(0);
            }
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
     * 根据用户ID取得用户信息
     *
     * @param userId
     * @return
     */
    public UsersInfo getUsersInfoByUserId(Integer userId) {
        if (userId != null) {
            UsersInfoExample example = new UsersInfoExample();
            example.createCriteria().andUserIdEqualTo(userId);
            List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(example);
            if (usersInfoList != null && usersInfoList.size() > 0) {
                return usersInfoList.get(0);
            }
        }
        return null;
    }
}
