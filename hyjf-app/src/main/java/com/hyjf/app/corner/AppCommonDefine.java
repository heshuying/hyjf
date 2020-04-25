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
	
package com.hyjf.app.corner;

import com.hyjf.app.BaseDefine;

public class AppCommonDefine  extends BaseDefine{

	/** @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/app/common";
	
	/** 获取版本号 */
	public static final String VERSION_MAPPING = "/getVersion";
	/** 设置设备唯一标识码 */
	public static final String MOBILE_CODE_MAPPING = "/mobileCode";
	/** 设置角标 */
	public static final String CORNER_MAPPING = "/setCorner";
	/** 获取最新版本号下载地址 */
    public static final String NEW_VERSION_URL_MAPPING = "/getNewVersionURL";
}

	