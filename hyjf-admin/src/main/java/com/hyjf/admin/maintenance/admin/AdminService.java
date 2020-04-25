package com.hyjf.admin.maintenance.admin;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.customize.AdminCustomize;

public interface AdminService extends BaseService {

	/**
	 * 获取账户列表
	 * 
	 * @return
	 */
	public List<AdminCustomize> getRecordList(AdminCustomize adminCustomize);

	/**
	 * 获取单个账户
	 * 
	 * @return
	 */
	public AdminCustomize getRecord(Integer id);

	/**
	 * 根据主键判断账户数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(Integer id);

	/**
	 * 账户插入
	 * 
	 * @param record
	 */
	public void insertRecord(AdminBean form);

	/**
	 * 账户更新
	 * 
	 * @param record
	 */
	public void updateRecord(AdminBean form);

	/**
	 * 账户删除
	 * 
	 * @param ids
	 */
	public void deleteRecord(List<Integer> ids);
	
    /**
     * 检查手机号码唯一性
     * 
     * @param id
     * @param mobile
     */
    public int countAdminByMobile(Integer id, String mobile);
    
    /**
     * 检查用户名唯一性
     * 
     * @param id
     * @param username
     */
    public int countAdminByUsername(Integer id, String username);

    /**
     * 检查邮箱唯一性
     * 
     * @param id
     * @param username
     */
    public int countAdminByEmail(Integer id, String email);
    
    /**
     * 
     * 重置用户密码
     * @author pcc
     * @param recordList
     */
    public void resetPwdAction(List<Integer> recordList);
}
