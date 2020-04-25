/**
 * url定义
 * @package com.hyjf.app.home
 * @author 王坤
 * @date 2016/02/05 10:30
 * @version V1.0  
 */
package com.hyjf.app.home;

import com.hyjf.app.BaseDefine;

public class HomePageDefine extends BaseDefine {

	/** 首页接口  @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/homepage";

	/** 首页项目列表  @RequestMapping值 */
	public static final String PROJECT_LIST_ACTION = "/getProjectList";

	/** 首页项目列表  @RequestMapping值 */
	public static final String START_PAGE_ACTION = "/getStartPage";
	
	/** 400客服电话  @RequestMapping值 */
	public static final String SERVICE_PHONE_NUMBER_ACTION = "/getServicePhoneNumber";
	
	/** 获取JumpCommend接口  @RequestMapping值 */
    public static final String GET_JUMP_COMMEND_ACTION = "/getJumpCommend";
	
	
	/** app banner 分页 limit起始 */
	public static final int BANNER_SIZE_LIMIT_START = 0;

	/** app banner 分页limitsize */
	public static final int BANNER_SIZE_LIMIT_END = 5;
	
	/** app banner 分页 limit起始 */
	public static final int PROJECT_SIZE_LIMIT_START = 0;

	/** app banner 分页limitsize */
	public static final int PROJECT_SIZE_LIMIT_END = 50;
	
	/** log日志controller类名 */
	public static final String THIS_CLASS = HomePageController.class.getName();

	/** 获取统计数据 */
	public static final String TOTALSTATICS_ACTION = "/getTotalStatics";
	
	/** 关于我们  - 资质文件 */
	public static final String CONTENT_QUALIFY_ACTION = "/getCompanyQualify";
	
	/** 关于我们  - 平台数据 */
	public static final String PLATFORM_DATA_ACTION = "/getPlatformData";

	/** url */
	public static final String APP_NEW_TEST = "http://appnewtest4.hyjf.com";

	/** 关于我们 */
	public static final String ABOUTS = "/aboutus";

	/** 运营报告 */
	public static final String REPORT = "/find/report";

	/** 资金存管 */
	public static final String DEPOSITORY = "/jsp/jx-bank-intr.jsp";

	/** 散标链接 */
	public static final String BORROW = "/borrow/";

	/** 计划链接 */
	public static final String PLAN = "/plan/";

	/** 首页module地址 */
    public static String MODULE_URL = "/data/upfiles/appmodule/";

	/** 评分标准地址 */
	public static final String RESULT_STANDARD = "/gradingStandardResult";
}
