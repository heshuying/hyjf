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
package com.hyjf.web.bank.wechat.borrow;

import com.hyjf.web.BaseDefine;
import com.hyjf.web.bank.web.borrow.BorrowController;

public class BorrowDefine extends BaseDefine {
	
	public static final String CONTROLLER_NAME = "WeChatBorrowController";

	/** 指定类型的项目 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "bank/wechat/borrow";
	
	/** 指定类型的项目项目列表 @RequestMapping值 */
	public static final String BORROW_LIST_ACTION = "/getBorrowList";

	/** 项目详情 @RequestMapping值 */
	public static final String BORROW_DETAIL_ACTION = "/getBorrowDetail";

	/** 出借记录 @RequestMapping值 */
	public static final String BORROW_INVEST_ACTION = "/getBorrowInvest";

	/** 类名 */
	public static final String THIS_CLASS = BorrowController.class.getName();

}
