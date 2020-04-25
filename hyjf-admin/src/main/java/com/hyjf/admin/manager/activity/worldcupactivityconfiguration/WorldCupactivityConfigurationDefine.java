package com.hyjf.admin.manager.activity.worldcupactivityconfiguration;

import com.hyjf.admin.BaseDefine;
import com.hyjf.common.util.ShiroConstants;
import com.hyjf.common.util.StringPool;

/**
 * @author xiehuili on 2018/6/13.
 */
public class WorldCupactivityConfigurationDefine extends BaseDefine{

    /** 权限关键字 */
    public static final String PERMISSIONS = "worldCupactivityConf";

    /** 查看权限 */
    public static final String PERMISSIONS_VIEW = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_VIEW;


    /** 修改权限 */
    public static final String PERMISSIONS_ADD = PERMISSIONS + StringPool.COLON + ShiroConstants.PERMISSION_ADD;

    /**
     * 世界杯活动配置 @RequestMapping值
     */
    public static final String REQUEST_MAPPING = "/manager/activity/worldCupActivityconfiguration";

    /**
     * 列表画面 @RequestMapping值
     */
    public static final String INIT = "init";

    /**
     * 列表画面 路径
     */
    public static final String LIST_PATH = "manager/activity/worldCupActivityconfiguration/worldCupActivityconfiguration";

    /** 从定向 路径 */
    public static final String RE_LIST_PATH = "redirect:" + REQUEST_MAPPING + "/" + INIT;


    public static final String UPLOAD_FILE = "uploadFile";

    /**
     * 决战赛比赛列表画面 @RequestMapping值
     */
    public static final String MATCH_INIT = "matchInit";

    /**
     * 列表画面 路径
     */
    public static final String MATCH_LIST_PATH = "manager/activity/worldCupActivityconfiguration/worldCupActivityconfigurationmatch";
    /**
     * 决战赛球队配置提交 @RequestMapping值
     */
    public static final String SUBMIT_TEAM_ACTION = "submitTeamAction";


    /**
     * 决战赛比赛配置校验球队是否唯一选中  @RequestMapping值
     */
    public static final String VALIDDATION_TEAM= "validationWorldCupTeam";

    /**
     * 决战赛比赛配置提交 @RequestMapping值
     */
    public static final String UPDATE_WORLD_CUP_MATCH = "updateWorldCupMatch";

    /**
     * FROM
     */
    public static final String FORM = "wccmForm";

    /**
     * 决战赛球队配置FROM
     */
    public static final String MATCH_FORM = "wccmForm";

}
