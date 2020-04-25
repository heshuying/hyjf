/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.app.user.vip;

import com.hyjf.app.BaseDefine;

public class VipDefine extends BaseDefine {

    /** vip @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/vip";
   
    /** 用户vip等级详情页 @RequestMapping值*/
    public static final String USER_VIP_DETAIL_ACTIVE_INIT = "/userVipDetailInit";
    
    /** 会员等级说明页面 @RequestMapping值*/
    public static final String VIP_LEVEL_CAPTION_ACTIVE_INIT = "/vipLevelCaptionInit";
    
    
    /** 会员特权详情页面 @RequestMapping值*/
    public static final String PRIVILEGE_DETAIL_ACTIVE_INIT = "/privilegeDetailInit";
    
    /** 用户vip等级详情页的路径 */
    public static final String USER_VIP_DETAIL_PATH = "/vip/manage/my-club";
    
    /** 会员等级说明页面的路径 */
    public static final String VIP_LEVEL_CAPTION_PATH = "/vip/manage/vip-level";
    
    /** 会员特权详情页面的路径 */
    public static final String PRIVILEGE_DETAIL_PATH = "other";


}
