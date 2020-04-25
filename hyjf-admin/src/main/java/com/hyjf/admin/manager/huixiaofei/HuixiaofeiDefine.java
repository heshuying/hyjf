package com.hyjf.admin.manager.huixiaofei;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class HuixiaofeiDefine extends BaseDefine {

	/** 活动列表 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/manager/huixiaofei";

	/** 列表画面 路径 */
	public static final String LIST_PATH = "manager/huixiaofei/huixiaofei";

	/** 画面迁移 路径 */
	public static final String INFO_PATH = "manager/huixiaofei/huixiaofeiInfo";

	/** 黑名单路径 */
	public static final String HEIMINGDAN_PATH = "manager/huixiaofei/huixiaofeiHeimingdan";

	/** 从定向 路径 */
	public static final String RE_LIST_PATH = "redirect:/manager/huixiaofei/init";

	/** 删除后 路径 */
	public static final String DELETE_AFTER_PATH = "manager/huixiaofei/huixiaofei";

	/** 审核页面 路径 */
	public static final String SHENHEINFO_PATH = "manager/huixiaofei/huixiaofeiShenheInfo";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** 条件查询数据 @RequestMapping值 */
	public static final String SEARCH_ACTION = "searchAction";

	/** 迁移到详细画面 @RequestMapping值 */
	public static final String INFO_ACTION = "infoAction";

	/** 插入数据 @RequestMapping值 */
	public static final String INSERT_ACTION = "insertAction";

	/** 更新数据 @RequestMapping值 */
	public static final String UPDATE_ACTION = "updateAction";

	/** 更新数据 @RequestMapping值 */
	public static final String STATUS_ACTION = "statusAction";

	/** 删除数据的 @RequestMapping值 */
	public static final String DELETE_ACTION = "deleteAction";
	/**
	 * 打包前的Ajax验证
	 */
	public static final String VALIDPACKAGEFORM = "validPackageFormAction";
	/**
	 * 下载达飞数据
	 */
	public static final String DOWNLOADDATA = "downLoadDataAction";
	/**
	 * 跳转到审核页面
	 */
	public static final String SHENHEINFO = "shenheInfoAction";
	/**
	 * 审核数据
	 */
	public static final String SHENHE = "shenheAction";
	/**
	 * 处理数据
	 */
	public static final String CHULI = "chuliAction";
	/** 导出数据 @RequestMapping值 */
	public static final String EXPORT_ACTION = "exportAction";
	/** 导出数据 @RequestMapping值 */
	public static final String HEIMINGDANEXPORT_ACTION = "heimingdanExportAction";

	/**
	 * 跳转到黑名单
	 */
	public static final String TOHEIMINGDAN = "toheimingdanAction";
	/** 二次提交后跳转的画面 */
	public static final String TOKEN_INIT_PATH = REQUEST_MAPPING + StringPool.FORWARD_SLASH + INIT;

	/** FROM */
	public static final String HUIXIAOFEI_FORM = "huixiaofeiForm";
	/** consume */
	public static final String HUIXIAOFEICONSUME = "huixiaofeiconsume";
	/** ListFROM */
	public static final String HUIXIAOFEILISTList = "huixiaofeiListList";

	// 以下是状态位
	/**
	 * CONSUME STATUS 0:未审核
	 */
	public static final Integer CONSUME_STATUS_0 = 0;
	/**
	 * CONSUME STATUS 0:已审核
	 */
	public static final Integer CONSUME_STATUS_1 = 1;
	/**
	 * CONSUME STATUS 0:已处理
	 */
	public static final Integer CONSUME_STATUS_2 = 2;
	/**
	 * CONSUME STATUS 0:未打包
	 */
	public static final Integer CONSUME_RELEASE_0 = 0;
	/**
	 * CONSUME STATUS 0:已打包
	 */
	public static final Integer CONSUME_RELEASE_1 = 1;
	/**
	 * CONSUMELIST STATUS 0:通过
	 */
	public static final Integer CONSUMELIST_STATUS_0 = 0;
	/**
	 * CONSUMELIST STATUS 0:不通过
	 */
	public static final Integer CONSUMELIST_STATUS_1 = 1;
	/**
	 * CONSUMELIST STATUS 0:未审核
	 */
	public static final Integer CONSUMELIST_STATUS_2 = 2;
	/**
	 * CONSUMELIST STATUS 0:已处理(不通过且已回传,黑名单)
	 */
	public static final Integer CONSUMELIST_STATUS_3 = 3;
	/**
	 * CONSUMELIST STATUS 0:未打包
	 */
	public static final Integer CONSUMELIST_RELEASE_0 = 0;
	/**
	 * CONSUMELIST STATUS 1:已打包
	 */
	public static final Integer CONSUMELIST_RELEASE_1 = 1;
	
	/** 权限关键字 */
	public static final String PERMISSIONS = "huixiaofei";
	
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
	
	/** 打包权限 */
	public static final String PERMISSIONS_PACKAGE = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_PACKAGE;

	/** 审核权限 */
	public static final String PERMISSIONS_AUDIT = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_AUDIT;

	/** 处理权限 */
	public static final String PERMISSIONS_CHULI = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_CHULI;
}
