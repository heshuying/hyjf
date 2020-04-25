/**
 * Description:用户充值
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.app.bank.user.transpassword;

import com.hyjf.app.BaseService;
import com.hyjf.mongo.operationlog.entity.UserOperationLogEntity;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.Users;

public interface AppTransPasswordService extends BaseService {

	/**
	 * 更新用户是否设置了交易密码
	 * @param user
	 * @param isFlag 0：未设置 1：已设置
	 * @return
	 */
	public boolean updateUserIsSetPassword(Users user,int isFlag);
	
	/**
	 * 更新用户手机号
	 * @param userId
	 * @param mobile 新
	 * @return
	 */
	public boolean updateUserMobile(Integer userId,String mobile);

	
    /**
     * 给管理员发送短信提醒
     * 
     * @return
     */
    void sendSms(String mobile, String reason) throws Exception;
    
    
    /**
     * 保存短信验证码信息
     * 
     * @return
     */
    int saveSmsCode(String mobile, String checkCode);
    
    /**
     * 检查短信验证码
     * 
     * @return
     */
    int checkMobileCode(String phone, String code);

    public SmsConfig getSmsConfig();
    
    
    /**
     * 获取企业开户信息
     * @param userId
     * @return
     */
    CorpOpenAccountRecord getCorpOpenAccountRecord(Integer userId);

	/**
	 * 设置交易密码日志
	 * @param userOperationLogEntity
	 */
	void sendUserLogMQ(UserOperationLogEntity userOperationLogEntity);
}
