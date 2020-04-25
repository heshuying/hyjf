package com.hyjf.admin.maintenance.redis;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

public class RedisWriteDefine extends BaseDefine {

	/** 网站redis写入 @RequestMapping值 */
	public static final String REQUEST_MAPPING = "/maintenance/redis";;

	/** 网站redis数据写入 @RequestMapping值 */
	public static final String WRITE_REDIS_ACTION = "/writeRedisAction";

	/** 网站redis数据写入 @RequestMapping值 */
	public static final String WRITE_REDIS_PATH = "/maintenance/redis/writeRedis";

	/** 列表画面 @RequestMapping值 */
	public static final String INIT = "init";

	/** redis权限 */
	public static final String PERMISSIONS = "redis";

	/** redis写入画面查看权限 */
	public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;

	/** redis数据写入 */
	public static final String PERMISSIONS_WRITE_REDIS = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_WRITE_REDIS;

}
