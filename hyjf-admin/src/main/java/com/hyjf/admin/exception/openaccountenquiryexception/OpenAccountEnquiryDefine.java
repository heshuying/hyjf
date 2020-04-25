package com.hyjf.admin.exception.openaccountenquiryexception;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class OpenAccountEnquiryDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/exception/openaccountenquiry";
	/** 初始化action */
    public static final String INIT_PATH = "/initAction";
	/** 查询action */
	public static final String UPDATE_PATH = "/openaccountenquiryAction";
	/** 更新开户状态action */
	public static final String UPDATEACCOUNT_PATH = "/openaccountenquiryupdateAction";
	
	public static final String ERROR = "openAccountEnquiryError";
	/** 查询 路径 */
    public static final String UPDATE_ACCOUNTENQURIYINFO_PATH = "/exception/openaccountenquiryexception/openaccountenquiryinfo";
    public static final String UPDATE_ACCOUNTENQURIY_PATH = "/exception/openaccountenquiryexception/openaccountenquiryexception";
	/**
     * 类名
     */
	public static final String THIS_CLASS = OpenAccountEnquiryController.class.getName();
	/**
	 * form
	 */
	public static final String ACCOUNT_ENQUIRY = "accountEnquiry";
	/** 查看权限 */
	public static final String PERMISSIONS = "openaccountenquiry";
	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

}
