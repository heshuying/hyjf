package com.hyjf.admin.manager.config.salesconfig;

/**
 * @author lisheng
 * @version EmailRecipientDefine, v0.1 2018/7/23 16:42
 */

public class EmailRecipientDefine {
    /**  @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/manager/config/email";

    /** FROM */
    public static final String EMAIL_FORM = "EmailRecipientForm";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "/init";

    public static final String ADD_ACTION = "addAction";

    public static final String LIST_PATH = "manager/config/salesconfig/emailconfiglist";
    /**添加修改界面*/
    public static final String ADD_LIST_PATH = "manager/config/salesconfig/emailconfigInfo";

    /**查看详情*/
    public static final String INFO_ACTION ="infoAction";
    /**校验邮箱*/
    public static final String CHECK_EMAIL_ACTION ="checkEmail";

    /**详情界面*/
    public static final String INFO_PATH="manager/config/salesconfig/emailconfigDetail";
    /**修改转跳*/
    public static final String JUMP_UPDATE_ACTION ="jumpUpdateAction";



    /**修改*/
    public static final String UPDATE_ACTION ="updateAction";

    /**添加*/
    public static final String INSERT_ACTION ="insertAction";
    /**禁用*/
    public static final String FORBIDDEN_ACTION="forbiddenAction";
    /**启用*/
    public static final String START_ACTION="startAction";


}
