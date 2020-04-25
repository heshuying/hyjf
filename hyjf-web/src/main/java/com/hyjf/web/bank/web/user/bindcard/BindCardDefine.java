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

package com.hyjf.web.bank.web.user.bindcard;

import com.hyjf.web.BaseDefine;

public class BindCardDefine extends BaseDefine {

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/bank/web/bindCard";

	/** @RequestMapping值 */
	public static final String CHECK_MAPPING = "/check";
	/** @RequestMapping值 */
	public static final String INDEX_MAPPING = "/index";

	public static final String BIND_CARD_NEW = "/bindCardNew";
	/** @RequestMapping值 */
	public static final String MYCARD_INIT = "/myCardInit";
	public static final String COLSE_BAND_CARD_RETURN = "/bandCardReturn";

	public static final String BAND_CARD_SUCCESS = "/bandCardSuccess";

	public static final String BAND_CARD_ERROR = "/bandCardError";
	/** 绑卡错误页面 */
	public static final String JSP_BINDCARD = "/bank/user/bindcard/bindcard";
	/** 绑卡错误页面 */
	public static final String JSP_BINDCARD_FALSE = "/bank/user/bindcard/bindcard_false";
	/** 绑卡错误页面 新 */
	public static final String JSP_BINDCARD_ERROR= "/bank/user/bankcardNew/bindcard-error";
	/** 绑卡成功页面 新*/
	public static final String JSP_BINDCARD_SUCCESS = "/bank/user/bankcardNew/bindcard-success";

	public static final String JSP_COLSE_BINDCARD_ERROR= "/bank/user/bankcardNew/closebindcard-error";

	public static final String JSP_COLSE_BINDCARD_SUCCESS= "/bank/user/bankcardNew/closebindcard-success";

	public static final String JSP_MYCARD = "/bank/user/bankcardNew/mycard";
	/** 绑卡新页面 */
	public static final String JSP_BIND_CARD_NEW = "/bank/user/bankcardNew/bindcard";
	/** 江西银行 发送短信码 */
    public static final String SEND_PLUS_CODE_ACTION = "/sendPlusCode";
    /** 江西银行绑卡增强 */
    /** @RequestMapping值 */
    public static final String BIND_CARD_PLUS = "/bindCardPlus";
	
}
