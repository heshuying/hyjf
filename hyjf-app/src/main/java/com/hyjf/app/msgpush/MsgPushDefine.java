package com.hyjf.app.msgpush;

import com.hyjf.app.BaseDefine;

public class MsgPushDefine extends BaseDefine {


	/** REQUEST_MAPPING */
	public static final String REQUEST_MAPPING = "/msgpush";

	/** 获取标签列表  */
	public static final String TAG_ACTION = "/getTagListAction";
	
	/** 获取消息列表  */
	public static final String MSG_ACTION = "/getMsgListAction";
	
	/** 消息标识已读  */
	public static final String ALREADY_READ_ACTION = "/alreadyReadAction";
	
	/** 消息及消息推送已读  */
	public static final String MSG_READ_ACTION = "/msgReadAction";
	
	/** 获取消息详情页  */
	public static final String MSG_DETAIL_ACTION = "/msgDetailAction";
	
	/**消息详情页  */
	public static final String MSG_DETAIL_PAGE = "/msg_push_detail";
	
	/**异常页  */
	public static final String ERROR_PAGE = "/error";
	
	
    /** @RequestMapping值 */
    public static final String RETURN_REQUEST= REQUEST_HOME + REQUEST_MAPPING;
}
