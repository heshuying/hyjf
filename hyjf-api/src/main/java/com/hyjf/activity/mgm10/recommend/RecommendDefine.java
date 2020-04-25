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

package com.hyjf.activity.mgm10.recommend;

import com.hyjf.base.bean.BaseDefine;

public class RecommendDefine extends BaseDefine {

    public static final String REQUEST_MAPPING = "/recommend";
    /** 获得用户推荐星信息 @RequestMapping值*/
    public static final String GET_USER_RECOMMEND_INFO = "/getRecommendInfo";
    /** 获得用户推荐星获取记录 @RequestMapping值*/
    public static final String GET_USER_RECOMMEND_STAR_PRIZE_LIST = "/getUserRecommendStarPrizeList";
    
    /** 获得用户推荐星使用记录 @RequestMapping值*/
    public static final String GET_USER_RECOMMEND_STAR_USED_PRIZE_LIST = "/getUserRecommendStarUsedPrizeList";
    
    /** 获得用户标识 @RequestMapping值*/
    public static final String GET_USER_FLAG = "/getUserFlag";
    
    
}
