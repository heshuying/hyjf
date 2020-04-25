/**
 * Description:我要融资常量定义
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.platdata;

import com.hyjf.web.BaseDefine;

public class PlatDataDefine extends BaseDefine {

	
	/** 权限 CONTROLLOR @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/paltdata";
	/** 平台数据 @RequestMapping值 */
	public static final String DATA_ACTION = "data";
	/** 平台数据画面 路径 */
	public static final String DATA_PATH = "aboutus/data";
	/** 当前controller名称 */
	public static final String THIS_CLASS = PlatDataController.class.getName();
}
