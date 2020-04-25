/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月18日 下午12:34:08
 * Modification History:
 * Modified by : 
 */

package com.hyjf.web.user.safe;

import com.hyjf.web.BaseDefine;

/**
 * @author Administrator
 */

public class SafeDefine extends BaseDefine {

    /** CONTROLLOR @value值 */
    public static final String CONTROLLOR_CLASS_NAME = "SafeController";

    /** CONTROLLOR @RequestMapping值 */
    public static final String CONTROLLOR_REQUEST_MAPPING = "/user/safe";

    /** 路径 */
    public static final String INIT_PATH = "user/safe/account-setting-index";
    /** 绑定邮件结果页面url@RequestMapping值 */
    public static final String BINDEMAILRESULT = "user/safe/bind-email-result";

    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";
    
    /** 参数验证@RequestMapping值 */
    public static final String CHECKPARAM = "checkParam";
    
    /** 修改密码前的验证@RequestMapping值 */
    public static final String UPDATEPASSWORD = "updatePassword";
    
    /** 修改昵称@RequestMapping值 */
    public static final String UPDATENICKNAME = "updatenickname";
    /** 修改紧急联系人@RequestMapping值 */
    public static final String UPDATERELATION = "updateRelation";
    /** 修改手机号@RequestMapping值 */
    public static final String CHANGEMOBILE = "changeMobile";
    /** 发送邮件@RequestMapping值 */
    public static final String SENDEMAIL = "sendEmail";
    /** 跳转到绑定邮件url@RequestMapping值 */
    public static final String BINDEMAIL = "bindEmail";
    /** 更新Sms配置 */
    public static final String UPDATESMSCONFIG = "updateSmsConfig";
	/** 邮箱状态1未验证  */
	public static final int EMAILSTATUS_1 = 1;
	/** 邮箱状态2已验证 */
	public static final int EMAILSTATUS_2 = 2;
	/** 邮箱状态3过期(或已发送新的邮件) */
	public static final int EMAILSTATUS_3 = 3;
	
	/** 列表画面 @RequestMapping值 */
    public static final String UPLOAD_AVATAR_INIT_ACTION = "uploadAvatarInitAction";
    /** 列表画面 @RequestMapping值 */
    public static final String UPLOAD_AVATAR_INIT_PATH = "user/safe/account-setting-avatar";
	/** 上传头像 */
    public static final String UPLOAD_AVATAR_ACTION = "/uploadAvatarAction";
    
    /** 账户设置未绑定时，点击 绑定 跳转 */
    public static final String BINDING_EMAIL_ACTION = "bindingEmail";  
    /** 账户设置未绑定跳转后画面*/
    public static final String BINDING_EMAIL_PATH = "user/safe/account-setting-email"; 

    
    /** 修改登录密码 跳转 */
    public static final String MODIFY_CODE_ACTION = "modifyCode"; 
    /** 修改登录密码跳转后画面*/
    public static final String MODIFY_CODE_PATH = "user/safe/account-password"; 
 
    /** 验证输入的原密码是否已存在 */
    public static final String CHECK_ORIGIN_PW = "checkOriginaPw"; 
    
    
    public static final String SHOW_MODIFY_SUCCESS = "modifySuccess"; 
    public static final String MODIFY_SUCCESS_PATH = "user/safe/account-password-success"; 
    
    /** 跳转到紧急联系人设置页面*/
    public static final String CONTACT_SET_PAGE_ACTION = "contactSetInit";
    /** 紧急联系人设置操作*/
    public static final String CONTACT_SET_UPDATE_ACTION = "contactSetUpdate";
    /** 紧急联系人设置成功页面*/
    public static final String CONTACT_SET_SUCCESS_ACTION = "contactSetSuccess";
    /** 紧急联系人设置页面路径*/
    public static final String CONTACT_SET_PAGE_PATH = "user/safe/account-contact";
    public static final String CONTACT_SET_SUCCESS_PATH = "user/safe/account-contact-success";
    
    /** 消息通知页面初始化 */
    public static final String MESSAGE_NOTIFICATION_INIT_ACTION = "/messageNotificationInitAction";
    
    /** 消息通知页面初始化 */
    public static final String UPDATE_MESSAGE_NOTIFICATION_ACTION = "/updateMessageNotificationAction";
    
    
    /** 列表画面 @RequestMapping值 */
    public static final String MESSAGE_NOTIFICATION_INIT_PATH = "user/safe/account-newset";
}
