package com.hyjf.admin.manager.borrow.applyagreement;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class ApplyAgreementDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/borrow/applyagreement";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/borrow/applyagreement/applyagreement";
	
	 /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:/manager/borrow/applyagreement/init";
    
    /** 结果页 路径 */
    public static final String RESULT_PATH = "/manager/borrow/applyagreement/applyagreement_result";

	/** 详细画面的路径 */
	public static final String INFO_PATH = "manager/borrow/applyagreement/borrowrepay_info";

	/** 取消详细画面 路径 */
	public static final String CANCEL_PATH = "manager/borrow/applyagreement/borrowrepay_cancel";
	
	/** 画面成功提交返回值Key */
    public static final String SUCCESS = "success";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";
	
	/** 担保机构债转协议标识 */
    public static final String DF = "DF";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";
	
	   
    /** 删除数据的 @RequestMapping值 */
    public static final String DELETE_ACTION = "deleteAction";
	
	/** 新增*/
	public static final String ADD_ACTION = "updateAction";
	
	/** 下载*/
	public static final String DOW_ACTION = "downloadAction";
	
	/** 二次提交后跳转的画面 */
    public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** 取消 @RequestMapping值 */
	public static final String CANCEL_ACTION = "cancelAction";

	/** 取消详细画面 @RequestMapping值 */
	public static final String CANCEL_INFO_ACTION = "cancelInfoAction";

	/** 导出 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";

	/** PDF签署Action @RequestMapping值*/
    public static final String PDF_SIGN_ACTION = "pdfSignAction";

	/** FROM */
	public static final String BORROW_FORM = "applyagreementForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "applyagreement";
	
	/** 列表查询权限 */
    public static final String PERMISSION_SEARCH = "SEARCH";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 查找权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 详细 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;

	/** 取消 */
	public static final String PERMISSIONS_CANCEL = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_CANCEL;

	/** 添加 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

}
