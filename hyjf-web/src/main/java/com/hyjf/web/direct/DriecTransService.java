package com.hyjf.web.direct;

import com.hyjf.mybatis.model.auto.DirectionalTransferAssociatedRecords;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.web.BaseService;
import com.hyjf.web.common.WebViewUser;

public interface DriecTransService extends BaseService {

      
	/**
	 * 根据userid 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public WebViewUser getWebViewUserByUserId(Integer userId);

	
	/**
	 * 根据username 获取用户信息
	 * 
	 * @param username
	 * @return
	 */
	public WebViewUser getWebViewUserByUserName(String username);

	/**
	 * 根据 企业用户 userid 获取关联信息
	 * @param userId
	 * @return
	 */
	public DirectionalTransferAssociatedRecords  getDirectByOutUserId(Integer userId);
	
	/**
	 * 
	 * 根据 企业用户 userid 和绑定状态 获取关联信息
	 * @author renxingchen
	 * @param userId
	 * @param associatedState
	 * @return
	 */
	public DirectionalTransferAssociatedRecords getDirectByOutUserId(Integer userId,Integer associatedState);
	
	
	
	/**
	 * 根据 被绑定用户 userid 获取关联信息
	 * @param userId
	 * @return
	 */
	public DirectionalTransferAssociatedRecords  getDirectByInUserId(Integer userId);

	/**
	 * 绑定用户
	 * @param bean
	 * @return
	 */
	public int insertBindUser(DriecTransBean bean);
	
	
	/**
	 * 绑定用户回调
	 * @param bean 
	 * @return
	 */
	public void insertBindUserReturn(ChinapnrBean bean);

	/**
	 * 定向转账
	 * @param bean
	 * @return
	 */
	public int insertDirecTrans(DriecTransBean bean,String orderId);
	
	/**
	 * 定向转账回调
	 * @param bean recordId(记录id)
	 * @return
	 */
	public void insertDirecTransReturn(ChinapnrBean bean,int recordId);

	/**
	 * 保存短信验证码
	 * @param mobile
	 * @param code
	 */
	public int saveSmsCode(String mobile,String code,int status);
	
	/**
	 * 校验短信验证码是否与发送一致
	 * @param id
	 * @return
	 */
	public boolean checkMobileCode(int id,String code,String mobile);

	/**
	 * 插入绑定日志
	 * @param bean
	 * @param updateFlag 0新增 1修改
	 */
	public void insertDirectLog(DriecTransBean bean,int updateFlag);
	
	/**
	 * 
	 * 此处为方法说明
	 * @author renxingchen
	 * @param transferAssociatedRecords
	 * @return
	 */
	public int updateTransferAssociatedRecord(DirectionalTransferAssociatedRecords transferAssociatedRecord,Integer shiftInUserId);
}
