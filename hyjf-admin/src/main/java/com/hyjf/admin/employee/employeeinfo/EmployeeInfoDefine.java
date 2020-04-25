package com.hyjf.admin.employee.employeeinfo;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class EmployeeInfoDefine extends BaseDefine{
	
	/** CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/employee/employeeinfo";

	/** 路径 */
	public static final String LIST_PATH = "employee/employeeinfo/employeeinfo_li";
	/** 列表画面 @RequestMapping值,所有员工列表 */
	public static final String LIST = "/employeeinfo_list";
	/** FROM */
	public static final String EMPLOYEEINFO_FORM = "employeeinfoForm";
	
	/** 路径 */
	public static final String DETAIL_PATH = "employee/employeeinfo/employeeinfo_detail";
	/** 列表画面 @RequestMapping值，员工详细 */
	public static final String DETAIL = "/detail";
	
	/** 路径 */
	public static final String EDIT_PATH = "employee/employeeinfo/employeeinfo_edit";
	/** 列表画面 @RequestMapping值，编辑员工 */
	public static final String EDIT = "/edit";
	
	/** 路径 */
	public static final String RESETPASSWORD_PATH = "employee/employeeinfo/employeeinfo_reset";
	/** 列表画面 @RequestMapping值，重置密码 */
	public static final String RESETPASSWORD = "/resetPassword";
	
	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + LIST;
	
	public static final String LEAVESTAFFOK = "leaveStaffOK";
	

    /** 导出账户列表 @RequestMapping值 */
    public static final String EXPORT_ACCOUNTMANAGE_ACTION = "exportEmployeeInfoExcel";
	
	/** ACTION*/
	/** 查询数据 @RequestMapping值 */
    public static final String SEARCH_ACTION = "searchAction";
    
	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";

    /** 检查是否唯一 @RequestMapping值 */
    public static final String CHECK_ACTION = "checkAction";

    
    /** 查看权限 */
	public static final String PERMISSIONS = "employeeinfo";
    
	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

}
