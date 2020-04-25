/**
 * Description:用户信息管理业务处理类接口
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年11月11日 上午11:05:26
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.admin.manager.user.manageruser;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserEvalationResultCustomize;
import com.hyjf.mybatis.model.customize.admin.*;

/**
 * @author 王坤
 */

public interface ManageUsersService extends BaseService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminUserListCustomize> getRecordList(Map<String, Object> userListCustomizes, int limitStart, int limitEnd);

	/**
	 * 当分账类型选择按出借人分账时，需选择出借人分公司，从会员中心-会员管理-分公司字段读取所有分公司
	 * @return
	 * @author wgx
	 */
	List<Map<String,String>> selectRegionNameList();

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 * @return
	 */

	public int updateUser(AdminUserUpdateCustomize user);

	/**
	 * 根据用户id查询用户详情
	 * 
	 * @param userId
	 * @return
	 */

	public AdminUserDetailCustomize searchUserDetailById(int userId);

	/**
	 * 通过用户ID 关联用户所在的渠道
	 * @param userId
	 * @return
	 * @Author : huanghui
	 */
	AdminUserDetailCustomize selectUserUtmInfo(int userId);

	/**
	 * 查询用户更新信息
	 * 
	 * @param userId
	 * @return
	 */

	public AdminUserUpdateCustomize searchUserUpdateById(int userId);

	/**
	 * 获取总记录条数
	 * 
	 * @param userListCustomizeBean
	 * @return
	 */

	public int countRecordTotal(Map<String, Object> userListCustomizeBean);

	/**
	 * @param userId
	 * @return
	 */

	public AdminUserRecommendCustomize searchUserRecommend(int userId);

	/**
	 * 更新用户attribute属性
	 * 
	 * @param userId
	 */
	public void updateUserParam(Integer userId);

	/**
	 * 更新全部用户attribute属性
	 *
	 */
	public void updateAllUserParam();

	/**
	 * @param form
	 */

	public void updateUserRe(AdminUserRecommendCustomize form);

	/**
	 * @param form
	 */

	public void updateUserIdCard(AdminUserRecommendCustomize form);
	
	/**
	 * 根据用户mobile
	 * 
	 * @param userId
	 * @param param
	 * @return
	 */
	public int countUserByMobile(int userId, String param);

	/**
	 * 获取指定时间内修改的推荐人信息
	 * 
	 * @param userRecommendCustomize
	 * @return
	 */
	public void querySpreadsUsersLog(AdminUserRecommendCustomize userRecommendCustomize);

	public int checkRecommend(String userId, String recommendName);

	public Users getUsersByUserName(String param);
	
	

	/**
	 * CRM平台调用修改推荐人用 根据用户id检索用户是否存在
	 * 
	 * @author liuyang
	 * @param userId
	 * @return
	 */
	public int countUserById(String userId);

	/**
	 * CRM平台用,修改推荐人
	 * 
	 * @param userId
	 * @param spreadsUserId
	 * @param operationName
	 * @return
	 */
	public int updateSpreadsUsers(String userId, String spreadsUserId, String operationName, String ip);

	/**
	 * 通过id获取VIP信息
	 * 
	 * @param vipId
	 * @return
	 */
	public VipInfo getVipInfoById(int vipId);

	/**
	 * 获取风险测评信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserEvalationResultCustomize getEvalationResultByUserId(int userId);

	/**
	 * 
	 * @Description:通过身份证号获取户籍所在地
	 * @param idCard
	 * @return String
	 * @exception:
	 * @author: xulijie
	 * @time:2017年5月3日 下午4:26:43
	 */
	public String getAreaByIdCard(String idCard);
    /**
     * 
     * 查询用户银行开户信息
     * @author pcc
     * @param userId
     * @return
     */
    public AdminUserBankOpenAccountCustomize selectBankOpenAccountById(int userId);

    /**
     * 根据电子账号查询企业用户信息
     * @param accountId
     * @param ret 
     * @param userId 
     * @return
     */
	public CompanyInfoBean queryCompanyInfoByAccoutnId(String accountId, JSONObject ret, String userId);

	/**
	 * 存储企业开户信息
	 * @param form
	 * @param ret
	 * @return 
	 */
	public boolean updateCompanyInfo(CompanyInfoBean form, JSONObject ret);

	/**
	 * 根据userID 获得企业用户信息
	 * @param userId
	 * @return
	 */
	public CompanyInfoBean selectCompanyInfoByUserId(int userId);
	
	
	/**
     * 第三方平台绑定信息
     * @param userId 
     * @param bindPlatformId 
     * @return
     */
	public BindUsers queryBindUsers(int userId, Integer bindPlatformId);
	
	/**
	 * 根据userID 获得电子签章信息
	 * @param userId
	 * @return
	 */
	public CertificateAuthority selectCertificateAuthorityByUserId(int userId);

	/**
	 * 发送CA认证信息修改MQ
	 * @param form
	 */
	void sendCAChangeMQ(AdminUserUpdateCustomize form);
	
	/**
	 * 发送CA认证信息修改MQ
	 * @param form
	 */
	public void sendCAChangeMQ(AdminUserRecommendCustomize form);

	/**
	 * 更新用户角色
	 * @param mobile
	 * @return
	 */
    JSONObject updateUserRoleId(Integer userId);

	/**
	 * 保存先
	 * @param userUpdate
	 * @return
	 */
	int updateUserInfos(AdminUserInfosUpdCustomize userUpdate);
	/**
	 * 根据用户id查找开户信息
	 * @param userId
	 * @return
	 */
	BankCard selectBankCardByUserId(Integer userId);

	/**
	 * 修改银行卡信息
	 * @param userUpdate
	 * @return
	 */
	int updateUserBankInfo(AdminUserInfosUpdCustomize userUpdate);
	/**
	 * 根据银行卡名查找银行卡配置
	 * @param bankName
	 * @return
	 */
	BanksConfig selectBankConfigBybankName(String bankName);
	/**
	 * 查看银联号
	 * @param form
	 * @return
	 */
	JSONObject searchPayAllianceCode(AdminUserInfosUpdCustomize form);
}
