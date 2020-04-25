package com.hyjf.admin.exception.authexception;

import java.util.List;
import java.util.Map;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.admin.AdminUserAuthExptionListCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserAuthListCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserAuthLogListCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserPayAuthCustomize;
import com.hyjf.mybatis.model.customize.admin.AdminUserRePayAuthCustomize;

public interface UserauthExceptionService extends BaseService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminUserAuthListCustomize> getRecordList(Map<String, Object> bankCardUser, int limitStart, int limitEnd);

	/**
	 * @param authUser
	 * @return
	 */
	public int countRecordTotal(Map<String, Object> authUser);

	/**
	 * 
	 * 获取授权列表数量
	 * @author pcc
	 * @param authUser
	 * @return
	 */
    public int countRecordTotalLog(Map<String, Object> authUser);
    /**
     * 
     * 获取授权列表
     * @author pcc
     * @param authUser
     * @param limitStart
     * @param limitEnd
     * @return
     */
    public List<AdminUserAuthLogListCustomize> getRecordListLog(Map<String, Object> authUser, int limitStart, int limitEnd);
    
    
    /**
     * 缴费授权记录数
     */
    public int countRecordTotalPay(Map<String, Object> authUser);
    
    /**
     * 缴费授权列表
     */
    
    public List<AdminUserPayAuthCustomize> getRecordListPay(Map<String, Object> authUser, int limitStart, int limitEnd);
    /**
     * 更新缴费授权的状态
     */
    public void updatePayAuthRecord(int id,String signEndDate,int authtype);
    
    
    /**
     * 还款记录数
     * @param authUser
     * @return
     */
    public int countRecordTotalRePay(Map<String, Object> authUser);
    
    /**
     * 缴费授权列表
     */
    
    public List<AdminUserRePayAuthCustomize> getRecordListRePay(Map<String, Object> authUser, int limitStart, int limitEnd);
//    /**
//     * 更新缴费授权的状态
//     */
    public void updateRePayAuthRecord(int id,String signEndDate,int authtype);
    
    public int isDismissPay(int userid);
    
    public int isDismissRePay(int userid);

    /**
     * 
     * 异常处理页面查询列表
     * @author sunss
     * @param authUser
     * @param offset
     * @param limit
     * @return
     */
    public List<AdminUserAuthExptionListCustomize> getExceptionRecordList(Map<String, Object> authUser, int offset,
        int limit);
}
