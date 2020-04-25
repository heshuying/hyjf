package com.hyjf.wechat.service.regist;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.customize.app.AppAdsCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.wechat.base.BaseService;
import com.hyjf.wechat.controller.regist.UserParameters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface UserService extends BaseService {
	/**
	 * 验证旧密码 
	 * @return 0:验证成功|-1:旧密码不正确|-2:用户不存在|-3:存在多个相同用户
	 */
	public int queryOldPassword(String username, String password);

	/**
	 * 登录
	 * @return -1:登录失败,用户不存在|-2:登录失败,存在多个相同用户|-3:登录失败,密码错误
	 */
	public int updateLoginInAction(String username, String password, String ip);

	/** 获取各种用户属性 */
	public UserParameters getUserParameters(Integer userId, HttpServletRequest request);

	/** 修改用户头像 */
	public void updateUserIconImg(Integer userId, String iconurl);

	/** 修改联系人 */
	public Boolean updateUrgentAction(Integer userId, Integer urgentRelation, String urgentName, String urgentMobile);

	/** 修改昵称 */
	public Boolean updateNicknameAction(Integer userId, String nickname);

	/** 注册 */
	public int insertUserAction(String mobile, String password, String verificationCode, String reffer, String loginIp, HttpServletRequest request,Users user, Integer userType);

	/** 根据userid取得用户 */
	public Users getUserByUserId(Integer userid);

	/** 根据手机号取得用户ID */
	public Integer getUserIdByMobile(String mobile);

	/** 修改密码 */
	public Boolean updatePasswordAction(Integer userId, String password);

	/** 
	 * 绑定新手机 
	 * @return 0:绑定成功|-1:新绑手机号不能与原手机号相同
	 */
	public int updateNewPhoneAction(Integer userId, String mobile);

	/** ==========跟短信验证有关的方法========== */

	/** 获取短信加固模版信息 */
	public SmsConfig getSmsConfig();

	/** 发送email给后台管理员 */
	public void sendEmail(String mobile, String reason) throws Exception;

	/** 发送短信给后台管理员 */
	public void sendSms(String mobile, String reason) throws Exception;

	/** 发送的短信验证码保存到数据库 */
	public int saveSmsCode(String mobile, String verificationCode, String verificationType, Integer status);

	/** 检查短信验证码searchStatus:查询的短信状态,updateStatus:查询结束后的短信状态 */
	public int updateCheckMobileCode(String mobile, String verificationCode, String verificationType, Integer searchStatus, Integer updateStatus);

	/** 根据手机号判断用户是否存在 */
	public int countUserByMobile(String mobile);

	/** 根据推荐人手机号或userId判断推荐人是否存在 */
	public int countUserByRecommendName(String param);

	public boolean checkIfSendCoupon(Users user);
	
	String getRefferUserId(String reffer);

    UtmPlat getUtmPlatBySourceId(Integer sourceId);
    /**
     * 
     * 银行存管发送短信验证码
     * @author pcc
     * @param userId
     * @param txcodeMobileModifyPlus
     * @param mobile
     * @param channelPc
     * @return
     */
    public BankCallBean sendSms(Integer userId, String txcodeMobileModifyPlus, String mobile, String channelPc);

    List<AppAdsCustomize> searchBannerList(Map<String, Object> ads);
    /**
     * 
     * 退出清空MobileCod
     * @author pcc
     * @param userId
     * @param sign
     */
    public void clearMobileCode(Integer userId, String sign);

    /**
     * 获取着陆页-注册数据
     * @param form
     * @return
     */
	public JSONObject getRegistLandingPageData(UserParameters form);
	
	
	int insertUserActionUtm(String mobile, String password, String verificationCode, String reffer, String loginIp,String platform, String utm_id, String utm_source, HttpServletRequest request,HttpServletResponse response, Users user);

	/**
	 * 发送神策数据统计MQ
	 *
	 * @param sensorsDataBean
	 */
	void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
