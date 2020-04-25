package com.hyjf.web.user.safe;

import java.util.List;

import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.UserBindEmailLog;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersContract;
import com.hyjf.web.BaseService;
import com.hyjf.web.common.WebViewUser;

public interface SafeService extends BaseService {
	
	/**
	 * 获取用户银行卡列表
	 * @param userid
	 * @return
	 */
	public List<AccountBank> getAccountBankList(Integer userid);
	/**
	 * 验证旧密码是否正确
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean validPassword(Integer userid, String password);
	/**
	 * 更新密码
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean updatePassword(Integer userid, String password);
	/**
	 * 更新昵称
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean updatenickname(Integer userid, String nickname);
	/**
	 * 更新紧急联系人关系
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean updateRelation(Integer userid, UsersContract contract);
	/**
	 * 更新电话
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean updateMobile(Integer userid, String mobile);
	/**
	 * 发送绑定邮箱邮件给用户
	 * @param userid
	 * @param username
	 * @param email
	 * @return
	 */
	public Boolean sendEmailToUser(WebViewUser user, String email);
	/**
	 * 更新邮箱
	 * 
	 * @param username
	 * @param password
	 * @param log
	 * @return
	 */
	public Boolean updateEmail(Integer userid, String email,UserBindEmailLog log);
	/**
	 * 获取用户绑定邮箱的数据
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public UserBindEmailLog getUserBindEmail(Integer userid);
	/**
	 * 更新sms配置
	 * @param userid
	 * @param smsKey
	 * @param smsValue
	 * @return
	 */
	public Boolean updateSmsConfig(Integer userid, String smsKey,Integer smsValue);
	/**
	 * 
	 * 修改头像图片
	 * @author pcc
	 * @param userId
	 * @param iconUrl
	 */
    public void updateUserIconImg(Integer userId, String iconUrl);
    public int updateMessageNotificationAction(Users user);
    /**
     * 
     * 根据用户编号查询签约信息
     * @author pcc
     * @param userId
     * @return
     */
    public HjhUserAuth getHjhUserAuthByUserId(Integer userId);

	/**
	 * 邮箱更新成功后,发送CA认证客户信息修改MQ
	 * @param userId
	 */
	void sendCAMQ(Integer userId);
}
