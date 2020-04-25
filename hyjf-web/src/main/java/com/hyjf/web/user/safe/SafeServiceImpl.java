package com.hyjf.web.user.safe;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountBankExample;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.HjhUserAuthExample;
import com.hyjf.mybatis.model.auto.UserBindEmailLog;
import com.hyjf.mybatis.model.auto.UserBindEmailLogExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersContract;
import com.hyjf.mybatis.model.auto.UsersContractExample;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.util.mail.MailUtil;
import com.hyjf.web.BaseServiceImpl;
import com.hyjf.web.common.WebViewUser;

@Service
public class SafeServiceImpl extends BaseServiceImpl implements SafeService {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	@Override
	public List<AccountBank> getAccountBankList(Integer userid) {
		AccountBankExample example = new AccountBankExample();
		example.createCriteria().andUserIdEqualTo(userid);
		example.setOrderByClause("");
		accountBankMapper.selectByExample(example);
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 验证密码是否正确
	 */
	@Override
	public Boolean validPassword(Integer userid, String password) {
		String codeSalt = "";
		String passwordDb = "";

		UsersExample example1 = new UsersExample();
		example1.createCriteria().andUserIdEqualTo(userid);
		List<Users> usersList = usersMapper.selectByExample(example1);
		codeSalt = usersList.get(0).getSalt();
		passwordDb = usersList.get(0).getPassword();

		// 验证用的password
		password = MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt);
		// 密码正确时
		if (password.equals(passwordDb)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean updatePassword(Integer userid, String password) {
		UsersExample example1 = new UsersExample();
		example1.createCriteria().andUserIdEqualTo(userid);
		List<Users> usersList = usersMapper.selectByExample(example1);
		Users u = usersList.get(0);
		String codeSalt = u.getSalt();
		password = MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt);
		u.setPassword(password);
		usersMapper.updateByPrimaryKeySelective(u);
		return true;
	}

	@Override
	public Boolean updatenickname(Integer userid, String nickname) {
		UsersInfoExample example1 = new UsersInfoExample();
		example1.createCriteria().andUserIdEqualTo(userid);
		List<UsersInfo> usersList = usersInfoMapper.selectByExample(example1);
		UsersInfo u = usersList.get(0);
		u.setNickname(nickname);
		usersInfoMapper.updateByPrimaryKeySelective(u);
		return true;
	}

	@Override
	public Boolean updateRelation(Integer userid, UsersContract contract) {
		UsersContractExample example = new UsersContractExample();
		example.createCriteria().andUserIdEqualTo(userid);
		usersContractMapper.deleteByExample(example);
		usersContractMapper.insertSelective(contract);
		return true;
	}

	@Override
	public Boolean updateMobile(Integer userid, String mobile) {
		UsersExample example = new UsersExample();
		example.createCriteria().andUserIdEqualTo(userid);
		List<Users> usersList = usersMapper.selectByExample(example);
		Users u = usersList.get(0);
		// 如果原手机号和原用户名相等,则更新用户名
		if (u.getUsername().equals(u.getMobile())) {
			u.setUsername(getUniqueUsername(mobile));
		}
		u.setMobile(mobile);
		usersMapper.updateByPrimaryKeySelective(u);
		return true;
	}

	@Override
	public Boolean sendEmailToUser(WebViewUser user, String email) {
		String[] toMailArray = new String[] { email };
		String subject = "";
		String activeCode = GetCode.getRandomCode(6);
		// 将之前的邮件失效
		UserBindEmailLogExample example = new UserBindEmailLogExample();
		example.createCriteria().andUserIdEqualTo(user.getUserId()).andUserEmailStatusEqualTo(SafeDefine.EMAILSTATUS_1);
		List<UserBindEmailLog> bingEmailLogList = userBindEmailLogMapper.selectByExample(example);
		if (bingEmailLogList != null && bingEmailLogList.size() > 0) {
			for (int i = 0; i < bingEmailLogList.size(); i++) {
				bingEmailLogList.get(i).setUserEmailStatus(SafeDefine.EMAILSTATUS_3);
				userBindEmailLogMapper.updateByPrimaryKeySelective(bingEmailLogList.get(i));
			}
		}
		// 插入新的邮件
		UserBindEmailLog log = new UserBindEmailLog();
		log.setCreatetime(new Date());
		log.setEmailActiveCode(activeCode);
		log.setEmailActiveUrlDeadtime(GetDate.getSomeDayBeforeOrAfter(new Date(), 1));
		log.setUserEmail(email);
		log.setUserEmailStatus(SafeDefine.EMAILSTATUS_1);
		log.setUserId(user.getUserId());
		userBindEmailLogMapper.insertSelective(log);
		// 加密
//		String useridStr = MD5Utils.MD5(MD5Utils.MD5(user.getUserId() + ""));
//		email = MD5Utils.MD5(MD5Utils.MD5(email));
		activeCode = MD5Utils.MD5(MD5Utils.MD5(activeCode));
		String url = PropUtils.getSystem("hyjf.web.host").trim() + SafeDefine.CONTROLLOR_REQUEST_MAPPING + "/" + SafeDefine.BINDEMAIL + ".do?key=" + user.getUserId() + "&value=" + activeCode + "&email=" + email;
		System.out.println(url);
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put("url_name", url);
		if (StringUtils.isNotBlank(user.getNickname())) {
			replaceMap.put("username_name", user.getNickname());
		} else {
			replaceMap.put("username_name", user.getUsername());
		}
		MailUtil.sendMail(toMailArray, subject, CustomConstants.EMAILPARAM_TPL_BINDEMAIL, replaceMap, null);
		return true;
	}

	@Override
	public Boolean updateEmail(Integer userid, String email,UserBindEmailLog log) {
		UsersExample example = new UsersExample();
		example.createCriteria().andUserIdEqualTo(userid);
		List<Users> usersList = usersMapper.selectByExample(example);
		Users u = usersList.get(0);
		u.setEmail(email);
		log.setUserEmailStatus(SafeDefine.EMAILSTATUS_2);
		userBindEmailLogMapper.updateByPrimaryKey(log);
		usersMapper.updateByPrimaryKeySelective(u);
		return true;
	}

	@Override
	public UserBindEmailLog getUserBindEmail(Integer userid) {
		UserBindEmailLogExample example = new UserBindEmailLogExample();
		example.createCriteria().andUserIdEqualTo(userid).andUserEmailStatusEqualTo(SafeDefine.EMAILSTATUS_1);
		List<UserBindEmailLog> userBindEmailLogList = userBindEmailLogMapper.selectByExample(example);
		if (userBindEmailLogList != null && userBindEmailLogList.size() > 0) {
			return userBindEmailLogList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Boolean updateSmsConfig(Integer userid, String smsKey, Integer smsValue) {
		UsersExample example = new UsersExample();
		example.createCriteria().andUserIdEqualTo(userid);
		List<Users> usersList = usersMapper.selectByExample(example);
		Users u = usersList.get(0);
		if(smsKey.equals("rechargeSms")){
			//充值成功短信
			u.setRechargeSms(smsValue);
		}
		if(smsKey.equals("withdrawSms")){
			//提现成功短信
			u.setWithdrawSms(smsValue);
		}
		if(smsKey.equals("investSms")){
			//出借成功短信
			u.setInvestSms(smsValue);
		}
		if(smsKey.equals("recieveSms")){
			//回收成功短信
			u.setRecieveSms(smsValue);
		}
		usersMapper.updateByPrimaryKeySelective(u);
		return true;
	}

	// public static void main(String[] args) {
	// System.out.println(MD5Utils.MD5(MD5Utils.MD5("asdf1234") + "VmF6QP"));
	// }
	/** 修改用户头像 */
    @Override
    public void updateUserIconImg(Integer userId, String iconurl) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setIconurl(iconurl);
        usersMapper.updateByPrimaryKeySelective(users);
    }

    @Override
    public int updateMessageNotificationAction(Users user) {
        // TODO Auto-generated method stub
        return usersMapper.updateByPrimaryKeySelective(user);
    }
    
    /**
     * 
     * 根据用户编号查询签约信息
     * @author pcc
     * @param userId
     * @return
     */
    @Override
    public HjhUserAuth getHjhUserAuthByUserId(Integer userId) {
        HjhUserAuthExample example=new HjhUserAuthExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<HjhUserAuth> list=hjhUserAuthMapper.selectByExample(example);
        if(list!=null&&list.size()>0){
           return  list.get(0);
        }else{
            HjhUserAuth hjhUserAuth=new HjhUserAuth();
            hjhUserAuth.setAutoInvesStatus(0);
            hjhUserAuth.setAutoCreditStatus(0);
            return hjhUserAuth;
        }
    }

	/**
	 * 邮箱更新成功后,发送CA认证客户信息修改MQ
	 * @param userId
	 */
	@Override
	public void sendCAMQ(Integer userId) {
		// add by liuyang 20180227 修改手机号后 发送更新客户信息MQ start
		// 加入到消息队列
		Map<String, String> params = new HashMap<String, String>();
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("userId", String.valueOf(userId));
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_USER_INFO_CHANGE, JSONObject.toJSONString(params));
		// add by liuyang 20180227 修改手机号后 发送更新客户信息MQ end
	}
}
