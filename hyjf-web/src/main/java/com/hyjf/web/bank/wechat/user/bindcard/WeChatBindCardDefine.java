/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 * Created at: 2015年12月4日 下午2:33:39
 * Modification History:
 * Modified by :
 */

package com.hyjf.web.bank.wechat.user.bindcard;

import com.hyjf.web.BaseDefine;

public class WeChatBindCardDefine extends BaseDefine {

	public static final String SUCCESS = "000";
	public static final String SUCCESS_MSG = "成功";
	public static final String FAIL = "99";
	public static final String FAIL_MSG = "失败";

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/wechat/bindCard";

	/** 获取用户银行卡列表 @RequestMapping值 */
	public static final String GET_BANK_CARD_MAPPING = "/getBankCardByUserId";

	/** 江西银行 发送短信码 */
	public static final String SEND_PLUS_CODE_ACTION = "/sendPlusCode";

	/** 江西银行绑卡增强 */
	/** @RequestMapping值 */
	public static final String BIND_CARD_PLUS = "/bindCardPlus";

	/** 绑卡画面路径 @RequestMapping */
	public static final String INIT_BINDCARD_DATA_ACTION = "/initBindCardData";
}
