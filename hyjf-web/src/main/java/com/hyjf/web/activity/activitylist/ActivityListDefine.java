package com.hyjf.web.activity.activitylist;

import com.hyjf.web.BaseDefine;

public class ActivityListDefine extends BaseDefine {

    /** 活动列表 @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/activity";

    /** 活动列表画面路径 */
    public static final String LIST_PATH = "activity/activitylist";

    /** 活动列表 @RequestMapping值 */
    public static final String ACTIVITY_LISTPAGE_ACTION = "/getActivityListPage";

    /** 列表画面 @RequestMapping值 */
    public static final String ACTIVITY_LIST_ACTION = "/getActivityList";

    /** 活动详情@RequestMapping值 */
    public static final String ACTIVITY_DETAIL_ACTION = "detail";

    /** 类名 */
    public static final String THIS_CLASS = ActivityListController.class.getName();

    /** FROM */
    public static final String ACTIVITYLIST_FORM = "activitylistForm";
}
