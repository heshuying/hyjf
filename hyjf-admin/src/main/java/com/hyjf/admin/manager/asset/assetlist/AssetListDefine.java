/**
 * Description:资产所用常量
 * Copyright: (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 上午11:01:57
 * Modification History:
 * Modified by : 
 * */
package com.hyjf.admin.manager.asset.assetlist;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class AssetListDefine extends BaseDefine {

	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/asset/assetlist";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/asset/assetlist/assetList";

	/** 详细画面的路径 */
	public static final String DETAIL_PATH = "manager/asset/assetlist/assetDetail";

	/** 企业详细画面的路径 */
	public static final String COMPANY_DETAIL_PATH = "manager/asset/assetlist/companyAssetDetail";
	
	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "/init";

	/** 列表画面 @RequestMapping值 */
	public static final String SEARCH_ACTION = "/searchAction";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String DETAIL_ACTION = "/{instCode}/{assetId}/detailAction";

	/** 导出数据的 @RequestMapping值 */
	public static final String EXPORT_ACTION = "/exportAction";
	
	/** 联动的 @RequestMapping值 */
	public static final String ASSETTYPE_ACTION = "/assetTypeAction";
	
//	/** 二次提交后跳转的画面 */
//	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String ASSET_LIST_FORM = "assetListForm";
	
	public static final String DETAIL_FORM = "detailForm";

	/** 查看权限 */
	public static final String PERMISSIONS = "assetlist";

	/** 查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** 检索权限 */
	public static final String PERMISSIONS_SEARCH = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_SEARCH;

	/** 导出权限 */
	public static final String PERMISSIONS_EXPORT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_EXPORT;

	/** 详细权限 */
	public static final String PERMISSIONS_INFO = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_INFO;
}
