package com.hyjf.admin.message.template;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class SmsTemplateDefine extends BaseDefine {
	
	/** 模板列表入口 CONTROLLOR @RequestMapping值 */
	public static final String TEMPLATE="message/template";
	
	/** 模板列表入口 CONTROLLOR @RequestMapping值 */
	public static final String INIT="init";

	
	/** 模板列表页面 CONTROLLOR @RequestMapping值 */
	public static final String LIST_VIEW="message/template/templatelist";
	
	/** 重定向列表页面 CONTROLLOR @RequestMapping值 */
	public static final String RE_LIST_VIEW="redirect:/message/template/templatelist";
	
	/** 迁移 路径 */
	public static final String INFO_PATH = "message/template/templateInfo";
	
	
	/** form CONTROLLOR @RequestMapping值 */
	public static final String TEMPLATE_LIST_FORM="listForm";
	
	/** 切换到详情页 CONTROLLOR @RequestMapping值 */
	public static final String INFO_ACTION="infoAction";
	
	
	/** 详情页数据CONTROLLOR @RequestMapping值 */
	public static final String INFO_FORM="infoForm";
	
	
	/** 新增模板 CONTROLLOR @RequestMapping值 */
	public static final String ADD_ACTION="addAction";
	
	/** 修改模板 CONTROLLOR @RequestMapping值 */
	public static final String UPDATE_ACTION="updateAction";
	
	
	/** 关闭模板 CONTROLLOR @RequestMapping值 */
	public static final String CLOSE_ACTION="closeAction";
	
	/** 启用模板 CONTROLLOR @RequestMapping值 */
	public static final String OPEN_ACTION="openAction";

	/** 权限关键字 */
	public static final String PERMISSIONS = "template";
	
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 修改权限 */
	public static final String PERMISSIONS_MODIFY = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_MODIFY;

	/** 删除权限 */
	public static final String PERMISSIONS_DELETE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_DELETE;

	/** 添加权限 */
	public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
}
