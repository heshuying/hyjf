/**
 * Description:获取指定的项目类型的项目常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.bank.web.borrow;

import com.hyjf.web.BaseDefine;

public class BorrowDefine extends BaseDefine {

	public static final String CONTROLLER_NAME = "WebBorrowController";

	/** 指定类型的项目 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/borrow";

	/** 初始化指定类型的项目列表 @RequestMapping值 */
	public static final String INIT_BORROW_LIST_ACTION = "/initBorrowList";

	/** 新手汇项目列表初始化 */
	public static final String NEW_BORROW_LIST_ACTION = "/newBorrowList";
	
	/** 指定类型的项目项目列表 @RequestMapping值 */
	public static final String BORROW_LIST_ACTION = "/getBorrowList";

	/** 项目详情 @RequestMapping值 */
	public static final String BORROW_DETAIL_ACTION = "/getBorrowDetail";
	
	/** 项目详情 @RequestMapping值 */
	public static final String BORROW_DETAIL_PREVIEW_ACTION = "/getBorrowDetailPreview";

	/** 项目预览 @RequestMapping值 */
	public static final String BORROW_PREVIEW_ACTION = "/getBorrowPreview";

	/** 出借记录 @RequestMapping值 */
	public static final String BORROW_INVEST_ACTION = "/getBorrowInvest";
	
	/** 汇消费记录 @RequestMapping值 */
	public static final String BORROW_CONSUME_ACTION = "/getBorrowConsume";

	/** 融通宝介绍页 @RequestMapping值 */
	public static final String RTB_INTR_ACTION = "/rtbInit";

	/** 项目详情 @RequestMapping值 */
	public static final String BORROW_STATUS_ACTION = "/getBorrowStatus";

	/** 定时发标验证及状态操作 @RequestMapping值 */
	public static final String BORROW_ONTIME_ACTION = "/{borrowNid}/ontimeBorrow";
//	public static final String BORROW_ONTIME_ACTION = "/ontimeBorrow";

	/** 汇直投项目详情 @Path值 */
	public static final String BORROW_DETAIL_PTAH = "bank/borrow/borrow-detail-old";
	
	/** 汇直投项目详情 @Path值 */
	public static final String BORROW_DETAIL_NEW_PTAH = "bank/borrow/borrow-detail-new";
	
	/** 汇直投项目详情 @Path值 */
	public static final String BORROW_DETAIL_PREVIEW_OLD_PTAH = "bank/borrow/borrow-detail-preview-old";
	
	/** 汇直投项目详情 @Path值 */
	public static final String BORROW_DETAIL_PREVIEW_NEW_PTAH = "bank/borrow/borrow-detail-preview-new";

	/** 汇直投项目详情预览 @Path值 */
	public static final String BORROW_PREVIEW_PTAH = "bank/borrow/borrowview";

	/** 融通宝介绍页 @Path值 */
	public static final String BORROW_RTBINIT_PTAH = "bank/borrow/rtbinit";

	/** 汇直投项目列表 @Path值 */
	public static final String BORROW_LIST_PTAH = "bank/borrow/bond-investlist";

	/** 新手汇项目列表 @Path值 */
	public static final String NEW_BORROW_LIST_PTAH = "bank/borrow/newinvestlist";
	
	/** 项目详情 @Path值 */
	public static final String ERROR_PTAH = "error";

	/** 类名 */
	public static final String THIS_CLASS = BorrowController.class.getName();

}
