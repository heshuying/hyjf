package com.hyjf.wechat.service.login;

import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.util.ConvertUtils;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyjf.bank.service.user.loginerror.LoginErrorConfigManager;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.auto.LoginErrorLockUserMapper;
import com.hyjf.mybatis.model.auto.LoginErrorLockUser;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.wechat.base.BaseServiceImpl;

/**
 * 
 * 登录 注销
 * @author sunss
 * @version hyjf 1.0
 * @since hyjf 1.0 2018年2月1日
 * @see 下午2:44:24
 */
@Service
public class LoginServiceImpl extends BaseServiceImpl implements LoginService{

	@Autowired
    protected LoginErrorLockUserMapper loginErrorLockUserMapper;

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    @Override
    public int updateLoginInAction(String userName, String password, String ipAddr) {
        String codeSalt = "";
        String passwordDb = "";
        Integer userId = null;
        String usernameString=null;
		String mobile=null;

        UsersExample example1 = new UsersExample();
        example1.createCriteria().andMobileEqualTo(userName);
        List<Users> usersList = usersMapper.selectByExample(example1);
        Users u = null;
        if (usersList == null || usersList.size() == 0) {
            return -1;
        } else {
            if (usersList.size() == 1) {
                u = usersList.get(0);
                userId = usersList.get(0).getUserId();
                codeSalt = usersList.get(0).getSalt();
                passwordDb = usersList.get(0).getPassword();
                usernameString=usersList.get(0).getUsername();
				mobile =usersList.get(0).getMobile();
            } else {
                return -2;
            }
            if (usersList.size() == 1 && usersList.get(0).getStatus() == 1) {
                return -4;
            }
        }
        //2.判断是否错误超过错误次数
  		Integer maxLoginErrorNum=LoginErrorConfigManager.getInstance().getWebConfig().getMaxLoginErrorNum();//获取Redis配置的额登录最大错误次数
  		//1.获取该用户密码错误次数
  		String passwordErrorNum=RedisUtils.get(RedisConstants.PASSWORD_ERR_COUNT_ALL+userId);
  		//判断密码错误次数是否超限
  		if (!StringUtils.isEmpty(passwordErrorNum)&&Integer.parseInt(passwordErrorNum)>=maxLoginErrorNum) {
  			return -5;//密码错误次数已达上限
  		}
        // 验证用的password
        password = MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt);
        // 密码正确时
        if (Validator.isNotNull(userId) && Validator.isNotNull(password) && password.equals(passwordDb)) {
            // 更新登录信息
            if (u.getLoginIp() != null) {
                u.setLastIp(u.getLoginIp());
            }
            if (u.getLoginTime() != null) {
                u.setLastTime(u.getLoginTime());
            }
            u.setLoginIp(ipAddr);
            u.setLoginTime(GetDate.getNowTime10());
            u.setLogintime(u.getLogintime() + 1);// 登录次数
            usersMapper.updateByPrimaryKeySelective(u);
            return userId;
        } else {
        	//增加密码错误次数
			long value = this.insertPassWordCount(RedisConstants.PASSWORD_ERR_COUNT_ALL+userId);//以用户手机号为key
			Integer loginLockTime=LoginErrorConfigManager.getInstance().getWebConfig().getLockLong();
			//1.获取该用户密码错误次数，2.判断是否错误超过错误次数
			if(value < maxLoginErrorNum){
				return -3;
			}else{
				LoginErrorLockUser loginErrorLockUser=new LoginErrorLockUser();
				loginErrorLockUser.setUserid(userId);
				loginErrorLockUser.setUsername(usernameString);
				loginErrorLockUser.setMobile(mobile);
				loginErrorLockUser.setLockTime(new Date());
				loginErrorLockUser.setUnlockTime(DateUtils.nowDateAddDate(loginLockTime));
				loginErrorLockUser.setFront(1);
				loginErrorLockUserMapper.insertSelective(loginErrorLockUser);
				return -5;//用户当天密码错误次数已达上限
			}
        }
    }

	@Override
	public int updateSSOLoginInAction(String userId, String ipAddr) {
        Integer uId = null;
        try {
	        UsersExample example1 = new UsersExample();
	        example1.createCriteria().andUserIdEqualTo(Integer.parseInt(userId));
	        List<Users> usersList = usersMapper.selectByExample(example1);
	        Users u = null;
	        if (usersList == null || usersList.size() == 0) {
	            return -1;
	        } else {
	            if (usersList.size() == 1) {
	                u = usersList.get(0);
	                uId = usersList.get(0).getUserId();
	            } else {
	                return -2;
	            }
	            if (usersList.size() == 1 && usersList.get(0).getStatus() == 1) {
	                return -4;
	            }
	            u.setLoginIp(ipAddr);
	            u.setLoginTime(GetDate.getNowTime10());
	            u.setLogintime(u.getLogintime() + 1);// 登录次数
	            usersMapper.updateByPrimaryKeySelective(u);
	            return uId;
	            
	        }
        } catch (Exception e) {
        	 return -1;
        }
	}
	/**
	 * redis增加
	 * @param key
	 */
	private long insertPassWordCount(String key) {
		long retValue  = RedisUtils.incr(key);
//		RedisUtils.expire(key,RedisUtils.getRemainMiao());//给key设置过期时间
		Integer loginErrorConfigManager=LoginErrorConfigManager.getInstance().getWebConfig().getLockLong();
		RedisUtils.expire(key,loginErrorConfigManager*3600);//给key设置过期时间
		return retValue;
	}

    @Override
    public void sendSensorsDataMQ(SensorsDataBean sensorsDataBean) {
		// 加入到消息队列
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("presetProps",sensorsDataBean.getPresetProps());
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("userId",sensorsDataBean.getUserId());
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_SENSORS_DATA_LOGIN, JSONObject.toJSONString(params));
    }
	/**
	 * 发送用户日志记录MQ
	 *
	 * @param userOperationLogEntity
	 */
	@Override
	public void sendUserLogMQ(UserOperationLogEntity userOperationLogEntity){
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.USER_LOG_SAVE, JSONObject.toJSONString(userOperationLogEntity));

	}
}
