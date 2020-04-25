package com.hyjf.admin.manager.user.msp;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class MspApplyDefine extends BaseDefine {

	/** 活动列表 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/users/mspapply";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/users/mspapply/mspapply";
	
	/** 从定向 路径 */
	public static final String RE_LIST_PATH = "redirect:/manager/users/mspapply/init";
	
	/** 迁移 路径 */
	public static final String INFO_PATH = "manager/users/mspapply/mspapplyInfo";
	/** 迁移 路径2 */
	public static final String INFO_PATH2 = "manager/users/mspapply/mspapplyInfog";

	/** 下载路径 */
	public static final String DOWNLOAD_PATH = "manager/users/mspapply/fqz";
	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "manager/users/mspapply/mspapply";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 条件查询数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";
	
	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";
	
	/** 共享 @RequestMapping值 */
	public static final String APPLY_ACTION = "applyAction";

	public static final String DOWNLOAD_FILE = "downloadFile";
	
	/** 导出数据的 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** 保存之前的去重验证 @RequestMapping值 */
	public static final String VALIDATEBEFORE = "validateBeforeAction";

	/** FROM */
	public static final String CONFIGBANK_FORM = "mspapplyForm";


	/** 权限关键字 */
	public static final String PERMISSIONS = "mspapply";
	
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

	/** 返回给前端展示的key  msg */
    public static final String MSG = "msg";

    /** api-web工程路径 */
    public static final String HYJF_API_WEB_URL = "hyjf.api.web.url";
    
    public static final String RESULT_JSON_KEY_FQZ_SUCCESS = "fqz_success";
    public static final String RESULT_JSON_KEY_FQZ_MESS = "fqz_mess";
    public static final String RESULT_JSON_KEY_MSP_SUCCESS = "msp_success";
    public static final String RESULT_JSON_KEY_MSP_MESS = "msp_mess";
    public static final String RESULT_JSON_KEY_REQID = "reqId";
    public static final String HYJF_API_QUERY_URL = "/anrong/queryUser.do";
    
    public static final String HYJF_API_SEND_URL = "/anrong/send.do";
    /** 共享的 @RequestMapping值 */
    public static final String SHARE_USER_ACTION = "shareUser";

}
