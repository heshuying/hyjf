package com.hyjf.batch;

import com.hyjf.batch.debtTransfer.DebtTransferBean;
import com.hyjf.batch.result.account.UserAccountResultBean;
import com.hyjf.batch.result.debtTransfer.DebtTransferResultBean;
import com.hyjf.batch.result.subjectTransfer.SubjectTransferResultBean;
import com.hyjf.batch.subject.transfer.SubjectTransferBean;
import com.hyjf.batch.user.account.UserAccountBean;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;

/**
 * BaseService
 * @author Michael
 */
public interface BaseService {
	/**
	 * 获取用户
	 * 
	 * @param userId
	 * @return
	 */
	public Users getUsers(Integer userId);

	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public UsersInfo getUsersInfoByUserId(Integer userId);
	
	/**
	 * 批量开户
	 * @param resultBean
	 * @return
	 */
	public boolean insertBankOpenAccount(UserAccountResultBean resultBean);
	
	
	/**
	 * 批量开户异常处理(平台过滤)
	 * @param userAccountBean
	 * @param errorCode  1身份证号为空  2姓名为空 3手机号为空 4身份证15位 不符合规范 5用户未满18岁
	 */
	public void insertOpenAccountError(UserAccountBean userAccountBean,int errorCode);

	
	/**
	 * 批量开户异常处理(接口返回过滤)
	 * @param userAccountBean
	 */
	public void insertOpenAccountResultError(UserAccountResultBean userAccountResultBean,String error);
	
	/**
	 * 批量标的迁移入库处理
	 * @param readBean
	 * @return
	 */
	public void insertSubjectTransferData(SubjectTransferBean readBean);
	
	/**
	 * 批量标的迁移更新处理
	 * @param resultBean
	 * @return
	 */
	public void updateSubjectTransferData(SubjectTransferResultBean resultBean);
	
	/**
	 * 债权转移请求异常处理(平台过滤)
	 * @param DebtTransferBean
	 * @param errorCode  
	 */
	public void insertDebtTransferError(DebtTransferBean debtTransferBean);
	
	/**
	 * 债权转移结果异常处理(平台过滤)
	 * @param DebtTransferBean
	 * @param errorCode  
	 */
	public void updateDebtTransferData(DebtTransferResultBean resultBean);
	
	//** -----------债权相关 end----------- */
}
