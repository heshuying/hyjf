package com.hyjf.admin.manager.user.account;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminAccountListCustomize;

public interface AccountService extends BaseService {

	/**
	 * 根据开户参数，获取开户信息
	 * @param accountUser
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	 
	public List<AdminAccountListCustomize> getRecordList(Map<String, Object> accountUser, int limitStart, int limitEnd);

	/**
	 * 统计开户记录总数
	 * @param userListCustomizeBean
	 * @return
	 */

	public int countRecordTotal(Map<String, Object> accountUser);
	
	
	/**
     * 根据开户参数，获取开户信息
     * @param accountUser
     * @param limitStart
     * @param limitEnd
     * @return
     */
     
    public List<AdminAccountListCustomize> getBankRecordList(Map<String, Object> accountUser, int limitStart, int limitEnd);

    /**
     * 统计开户记录总数
     * @param userListCustomizeBean
     * @return
     */

    public int countBankRecordTotal(Map<String, Object> accountUser);

	/**
	 * 
	 * @Description:通过身份证号获取户籍所在地
	 * @param idCard
	 * @return String
	 * @exception:
	 * @author: xulijie
	 * @time:2017年5月3日 下午4:26:43
	 */
	String getAreaByIdCard(String idCard);
	
}
