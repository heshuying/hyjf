/**
 * Description:出借常量类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: b
 * @version: 1.0
 *           Created at: 2015年12月4日 下午2:33:39
 *           Modification History:
 *           Modified by :
 */

package com.hyjf.app.server;

import com.hyjf.app.BaseDefine;

public class ServerDefine extends BaseDefine {

    /** @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/server";

    /** @RequestMapping值 */
    public static final String GET_BEST_SERVER_MAPPING = "/getBestServerAction";

    /** @RequestMapping值 */
    public static final String GET_KEY_MAPPING = "/getKeyAction";
    
    /** @RequestMapping值 */
    public static final String SIGN_SYNC_MAPPING = "/signSync";

    /** 获取神策服务器地址 @RequestMapping值 */
    public static final String GET_SENSOR_DATA_URL = "/getSensorsDataUrl";

    /** @RequestMapping值 */
    public static final String GET_BEST_SERVER_REQUEST = "/hyjf-app" + REQUEST_MAPPING + GET_BEST_SERVER_MAPPING;

    /** @RequestMapping值 */
    public static final String GET_KEY_REQUEST = "/hyjf-app" + REQUEST_MAPPING + GET_KEY_MAPPING;

    public static final String UPLOAD_AVATAR_MAPPING = "/uploadAvatarAction";
}
