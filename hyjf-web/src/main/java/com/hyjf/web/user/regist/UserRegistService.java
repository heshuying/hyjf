package com.hyjf.web.user.regist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.bank.service.sensorsdata.SensorsDataBean;
import com.hyjf.mybatis.model.auto.SmsConfig;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.web.BaseService;

public interface UserRegistService extends BaseService {

	int countUserByRecommendName(String param);

	Boolean insertUserAction(String mobile, String password, String verificationCode, String reffer, String loginIp,String platform, HttpServletRequest request,HttpServletResponse response, Integer userType);
	//新建--李深强
	int insertUserActionNew(String mobile, String password, String verificationCode, String reffer, String loginIp,String platform, String utm_id, String utm_source, String utm_medium, String utm_content, HttpServletRequest request,HttpServletResponse response, Integer userType);
	int checkMobileCode(String phone, String code);

	int saveUtmReg(UtmReg utmReg);

	SmsConfig getSmsConfig();

	/**
	 * 短信平台遭到攻击,发送邮件和短信给管理员
	 * @param mobile
	 * @param reason
	 * @author 孙亮
	 * @since 2016年1月16日 下午3:16:04
	 */
	void sendEmail(String mobile, String reason) throws Exception;
	
	void sendSms(String mobile, String reason) throws Exception;
	
	/** 发送的短信验证码保存到数据库 */
	public int saveSmsCode(String mobile, String verificationCode, String verificationType, Integer status, String platform);

	/** 检查短信验证码searchStatus:查询的短信状态,updateStatus:查询结束后的短信状态 */
	public int updateCheckMobileCode(String mobile, String verificationCode, String verificationType, String platform, Integer searchStatus, Integer updateStatus);

	boolean checkIfSendCoupon();
	
	String getRefferUserId(String reffer);

	/**
	 * 发送神策数据统计MQ
	 *
	 * @param sensorsDataBean
	 */
	void sendSensorsDataMQ(SensorsDataBean sensorsDataBean);
}
