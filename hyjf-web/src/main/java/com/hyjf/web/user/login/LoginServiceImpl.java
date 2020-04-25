package com.hyjf.web.user.login;

import com.google.common.base.Preconditions;
import com.hyjf.bank.service.user.loginerror.LoginErrorConfigManager;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.security.utils.MD5Utils;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.mapper.auto.LoginErrorLockUserMapper;
import com.hyjf.mybatis.mapper.auto.ProtocolLogMapper;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.auto.UsersExample.Criteria;
import com.hyjf.web.BaseServiceImpl;
import com.hyjf.web.common.WebViewUser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl extends BaseServiceImpl implements LoginService {


    @Autowired
    protected LoginErrorLockUserMapper loginErrorLockUserMapper;

    @Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	@Override
    public boolean existUser(String userName) {
        UsersExample example1 = new UsersExample();
        UsersExample example2 = new UsersExample();
        example1.createCriteria().andUsernameEqualTo(userName);
        Criteria c2 = example2.createCriteria().andMobileEqualTo(userName);
        example1.or(c2);
        int size = usersMapper.countByExample(example1);
        if (size > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int queryPasswordAction(String username, String password) {
        String codeSalt = "";
        String passwordDb = "";
        Integer userId = null;

        UsersExample example1 = new UsersExample();
        UsersExample example2 = new UsersExample();
        example1.createCriteria().andUsernameEqualTo(username);
        Criteria c2 = example2.createCriteria().andMobileEqualTo(username);
        example1.or(c2);
        List<Users> usersList = usersMapper.selectByExample(example1);
        if (usersList == null || usersList.size() == 0) {
            return -1;
        } else {
            if (usersList.size() == 1) {
                userId = usersList.get(0).getUserId();
                codeSalt = usersList.get(0).getSalt();
                passwordDb = usersList.get(0).getPassword();
            } else {
                return -2;
            }
        }

        // 验证用的password
        password = MD5Utils.MD5(MD5Utils.MD5(password) + codeSalt);
        // 密码正确时
        if (Validator.isNotNull(userId) && Validator.isNotNull(password) && password.equals(passwordDb)) {
            return userId;
        } else {
            return -3;
        }
    }

    @Override
    public int queryPasswordAction2(String username, String password) {
        String codeSalt = "";
        String passwordDb = "";
        Integer userId = null;

        UsersExample example1 = new UsersExample();
        UsersExample example2 = new UsersExample();
        example1.createCriteria().andUsernameEqualTo(username);
        Criteria c2 = example2.createCriteria().andMobileEqualTo(username);
        example1.or(c2);
        List<Users> usersList = usersMapper.selectByExample(example1);
        if (usersList == null || usersList.size() == 0) {
            return -1;
        } else {
            if (usersList.size() == 1) {
                userId = usersList.get(0).getUserId();
                codeSalt = usersList.get(0).getSalt();
                passwordDb = usersList.get(0).getPassword();
            } else {
                return -2;
            }
        }

        // 验证用的password
        password = MD5Utils.MD5(password + codeSalt);
        // 密码正确时
        if (Validator.isNotNull(userId) && Validator.isNotNull(password) && password.equals(passwordDb)) {
            return userId;
        } else {
            return -3;
        }
    }

    /**
     * 登录
     *
     * @return -1:登录失败,用户不存在|-2:登录失败,存在多个相同用户|-3:登录失败,密码错误
     */
    @Override
    public int updateLoginInAction(String username, String password, String ip) {
        String codeSalt = "";
        String passwordDb = "";
        Integer userId = null;

        UsersExample example1 = new UsersExample();
        UsersExample example2 = new UsersExample();
        example1.createCriteria().andUsernameEqualTo(username);
        Criteria c2 = example2.createCriteria().andMobileEqualTo(username);
        example1.or(c2);
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
            } else {
                return -2;
            }
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
            u.setLoginIp(ip);
            u.setLoginTime(GetDate.getNowTime10());
            u.setLogintime(u.getLogintime() + 1);// 登录次数
            usersMapper.updateByPrimaryKeySelective(u);
            return userId;
        } else {
            return -3;
        }
    }

    /**
     * 登录
     *
     * @return -1:登录失败,用户不存在|-2:登录失败,存在多个相同用户|-3:登录失败,密码错误|-4:用户已禁用|-5：用户当天密码错误次数已达上限
     */
    @Override
    public int updateLoginInAction2(String username, String password, String ip) {

        //登录前校验
//		int returnValue = this.preLogin(username,ip);
//		if(returnValue < 0){
//			return returnValue;
//		}

        String codeSalt = "";
        String passwordDb = "";
        Integer userId = null;
        String usernameString = null;
        String mobile = null;

        List<Users> usersList = findUser(username);
        Users u = null;
        if (usersList == null || usersList.size() == 0) {
            return -1;
        }
        if (usersList.size() > 1) {
            return -2;
        }
        u = usersList.get(0);
        //锁定
        if (u.getStatus() == 1) {
            return -4;
        }
        //检查是否超过重试次数
        Integer maxLoginErrorNum = LoginErrorConfigManager.getInstance().getWebConfig().getMaxLoginErrorNum();//获取Redis配置的额登录最大错误次数
        String passwordErrorNum = RedisUtils.get(RedisConstants.PASSWORD_ERR_COUNT_ALL + u.getUserId());
        if (!StringUtils.isEmpty(passwordErrorNum) && Integer.parseInt(passwordErrorNum) >= maxLoginErrorNum) {
            return -5;
        }
        userId = usersList.get(0).getUserId();
        codeSalt = usersList.get(0).getSalt();
        passwordDb = usersList.get(0).getPassword();
        mobile = usersList.get(0).getMobile();
        usernameString=usersList.get(0).getUsername();

        // 页面传来的密码
        password = MD5Utils.MD5(password + codeSalt);
        // 密码正确时
        if (Validator.isNotNull(userId) && Validator.isNotNull(password) && password.equals(passwordDb)) {
            // 更新登录信息
            if (u.getLoginIp() != null) {
                u.setLastIp(u.getLoginIp());
            }
            if (u.getLoginTime() != null) {
                u.setLastTime(u.getLoginTime());
            }
            u.setLoginIp(ip);
            u.setLoginTime(GetDate.getNowTime10());
            u.setLogintime(u.getLogintime() + 1);// 登录次数
            usersMapper.updateByPrimaryKeySelective(u);
            return userId;
        } else {
            //增加密码错误次数
            long value = this.insertPassWordCount(RedisConstants.PASSWORD_ERR_COUNT_ALL + userId);//以用户手机号为key
            Integer loginLockTime = LoginErrorConfigManager.getInstance().getWebConfig().getLockLong();
            //1.获取该用户密码错误次数，2.判断是否错误超过错误次数
            if (value < maxLoginErrorNum) {
                return -3;
            } else {
                LoginErrorLockUser loginErrorLockUser = new LoginErrorLockUser();
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

    private List<Users> findUser(String username) {
        UsersExample example1 = new UsersExample();
        UsersExample example2 = new UsersExample();
        example1.createCriteria().andUsernameEqualTo(username);
        Criteria c2 = example2.createCriteria().andMobileEqualTo(username);
        example1.or(c2);
        List<Users> lstUser = usersMapper.selectByExample(example1);
        return lstUser;
    }

    public Integer getUser(String username) {
        List<Users> lstUser = findUser(username);
        Preconditions.checkArgument(lstUser.size() == 1);
        return lstUser.get(0).getUserId();
    }


    /**
     * 登录前校验
     *
     * @param username
     * @return
     */
    private int preLogin(String username, String ip) {
        int returnValue = 0;//默认成功
        String errCountKey = RedisConstants.PASSWORD_ERR_COUNT_ALL + username;//用户错误的键
        String loginCountKey = RedisConstants.LOGIN_ONE_COUNT_WEB + ip;
        String loginBlackList = RedisConstants.LOGIN_BLACK_LIST_WEB;
        //查询黑名单
		/*if(RedisUtils.sismember(loginBlackList,ip)){
			return -6 ;//已加入黑名单
		}*/

        //防暴力登录（将满足条件的加入黑名单）
		/*boolean flag = RedisUtils.isMaliciousRequest(loginCountKey,5,1);
		if(flag){
			//将该ip插入到set集合
			RedisUtils.sadd(loginBlackList,ip);
			RedisUtils.expire(loginBlackList,60*60*24*30);//设置一个月过期
			return -6 ;//已加入黑名单
		}*/

        //获取密码错误次数
        String errCount = RedisUtils.get(errCountKey);
        Integer maxLoginErrorNum = LoginErrorConfigManager.getInstance().getWebConfig().getMaxLoginErrorNum();//获取Redis配置的额登录最大错误次数
        if (StringUtils.isNotBlank(errCount) && Integer.parseInt(errCount) >= maxLoginErrorNum) {
            return -5;//用户当天密码错误次数已达上限
        }
        return returnValue;
    }


    @Override
    public void insertAccount() throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public WebViewUser getWebViewUserByUserId(Integer userId) {
        UsersExample usersExample = new UsersExample();
        usersExample.createCriteria().andUserIdEqualTo(userId);
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        Users user = usersList.get(0);
        WebViewUser result = new WebViewUser();
        result.setUserId(user.getUserId());
        result.setUsername(user.getUsername());
        if (StringUtils.isNotBlank(user.getMobile())) {
            result.setMobile(user.getMobile());
        }
        if (StringUtils.isNotBlank(user.getIconurl())) {
            String imghost = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.head.url"));
            imghost = imghost.substring(0, imghost.length() - 1);// http://cdn.huiyingdai.com/

            String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.head.path"));
            if (StringUtils.isNotEmpty(user.getIconurl())) {
                result.setIconurl(imghost + fileUploadTempPath + user.getIconurl());
            }
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
        if (user.getBankOpenAccount() != null) {
            if (user.getBankOpenAccount() == 1) {
                result.setBankOpenAccount(true);
            } else {
                result.setBankOpenAccount(false);
            }
        }
        result.setRechargeSms(user.getRechargeSms());
        result.setWithdrawSms(user.getWithdrawSms());
        result.setInvestSms(user.getInvestSms());
        result.setRecieveSms(user.getRecieveSms());
        result.setIsSetPassword(user.getIsSetPassword());
//		result.setIsEvaluationFlag(user.getIsEvaluationFlag());
        try {
            if (user.getIsEvaluationFlag() == 1 && null != user.getEvaluationExpiredTime()) {
                //测评到期日
                Long lCreate = user.getEvaluationExpiredTime().getTime();
                //当前日期
                Long lNow = System.currentTimeMillis();
                if (lCreate <= lNow) {
                    //已过期需要重新评测
                    result.setIsEvaluationFlag(2);
                    result.setEvaluationExpiredTime(user.getEvaluationExpiredTime());
                } else {
                    //未到一年有效期
                    result.setIsEvaluationFlag(1);
                    result.setEvaluationExpiredTime(user.getEvaluationExpiredTime());
                }
            } else {
                result.setIsEvaluationFlag(0);
            }
            // modify by liuyang 20180411 用户是否完成风险测评标识 end
        } catch (Exception e) {
            result.setIsEvaluationFlag(0);
        }
        result.setIsSmtp(user.getIsSmtp());
        result.setUserType(user.getUserType());
        //update by jijun 2018/04/09 合规接口改造一期
//		result.setPaymentAuthStatus(user.getPaymentAuthStatus());

        UsersInfoExample usersInfoExample = new UsersInfoExample();
        usersInfoExample.createCriteria().andUserIdEqualTo(userId);
        List<UsersInfo> usersInfoList = usersInfoMapper.selectByExample(usersInfoExample);
        if (usersInfoList != null && usersInfoList.size() > 0 && usersInfoList.get(0).getSex() != null) {
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
            result.setBorrowerType(usersInfoList.get(0).getBorrowerType());
        }
        result.setRoleId(usersInfoList.get(0).getRoleId() + "");

        AccountChinapnrExample chinapnrExample = new AccountChinapnrExample();
        chinapnrExample.createCriteria().andUserIdEqualTo(userId);
        List<AccountChinapnr> chinapnrList = accountChinapnrMapper.selectByExample(chinapnrExample);
        if (chinapnrList != null && chinapnrList.size() > 0) {
            result.setChinapnrUsrid(chinapnrList.get(0).getChinapnrUsrid());
            result.setChinapnrUsrcustid(chinapnrList.get(0).getChinapnrUsrcustid());
        }

        BankOpenAccount bankOpenAccount = this.getBankOpenAccount(userId);
        if (bankOpenAccount != null && StringUtils.isNotEmpty(bankOpenAccount.getAccount())) {
            if (result.isBankOpenAccount()) {
                result.setBankAccount(bankOpenAccount.getAccount());
            }
        }
        // 用户紧急联系人
        UsersContractExample usersContractExample = new UsersContractExample();
        usersContractExample.createCriteria().andUserIdEqualTo(userId);
        List<UsersContract> UsersContractList = usersContractMapper.selectByExample(usersContractExample);
        if (UsersContractList != null && UsersContractList.size() > 0) {
            result.setUsersContract(UsersContractList.get(0));
        }
        return result;
    }

    @Override
    public boolean existEmail(String email) {
        UsersExample example1 = new UsersExample();
        example1.createCriteria().andEmailEqualTo(email);
        int size = usersMapper.countByExample(example1);
        if (size > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer getUserIdByUsername(String userName) {
        UsersExample example1 = new UsersExample();
        UsersExample example2 = new UsersExample();
        example1.createCriteria().andUsernameEqualTo(userName);
        Criteria c2 = example2.createCriteria().andMobileEqualTo(userName);
        example1.or(c2);
        List<Users> usersList = usersMapper.selectByExample(example1);
        if (usersList == null || usersList.size() == 0) {
            return -1;
        } else {
            if (usersList.size() == 1) {
                return usersList.get(0).getUserId();
            } else {
                return -2;
            }
        }
    }

    @Override
    public DebtPlan queryPlanByNid(String bid) {
        DebtPlanExample example = new DebtPlanExample();
        example.createCriteria().andDebtPlanNidEqualTo(bid);
        List<DebtPlan> debtPlans = this.debtPlanMapper.selectByExample(example);
        if (null != debtPlans && debtPlans.size() > 0) {
            return debtPlans.get(0);
        } else {
            return null;
        }
    }

    /**
     * redis增加
     *
     * @param key
     */
    private long insertPassWordCount(String key) {
        long retValue = RedisUtils.incr(key);
//		RedisUtils.expire(key,RedisUtils.getRemainMiao());//给key设置过期时间
        Integer loginErrorConfigManager = LoginErrorConfigManager.getInstance().getWebConfig().getLockLong();
        RedisUtils.expire(key, loginErrorConfigManager * 3600);//给key设置过期时间
        return retValue;
    }


	/**
	 * 神策发送神策数据统计MQ
	 * @param sensorsDataBean
	 */
	@Override
	public void sendSensorsDataMQ(SensorsDataBean sensorsDataBean) {
		// 加入到消息队列
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("presetProps",sensorsDataBean.getPresetProps());
		params.put("mqMsgId", GetCode.getRandomCode(10));
		params.put("userId",sensorsDataBean.getUserId());
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_SENSORS_DATA_LOGIN, JSONObject.toJSONString(params));
	}
}
