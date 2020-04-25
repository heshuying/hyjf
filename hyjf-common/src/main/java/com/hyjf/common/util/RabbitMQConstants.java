package com.hyjf.common.util;

public class RabbitMQConstants {
    
    //优惠券exchange配置
    public static final String EXCHANGES_COUPON = "hyjf-direct-exchange";

    //优惠券exchange配置
    public static final String EXCHANGES_NAME = "hyjf-direct-exchange";
    
    //优惠券还款
    public static final String ROUTINGKEY_COUPONREPAY = "hyjf-routingkey-coupon-repay";
    public static final String QUEUE_COUPONREPAY = "hyjf-coupon-repay";
    
    //优惠券放款
    public static final String ROUTINGKEY_COUPONLOANS = "hyjf-routingkey-coupon-loans";
    public static final String QUEUE_COUPONLOANS = "hyjf-coupon-loans";
    
    //计划类优惠券还款
    public static final String ROUTINGKEY_COUPONREPAY_HJH = "hyjf-routingkey-coupon-planrepay";
    public static final String QUEUE_COUPONREPAY_HJH = "hyjf-coupon-planrepay";
    
    //计划类优惠券放款
    public static final String ROUTINGKEY_COUPONLOANS_HJH = "hyjf-routingkey-coupon-planloans";
    public static final String QUEUE_COUPONLOANS_HJH = "hyjf-coupon-planloans";
    
    //优惠券发放
    public static final String ROUTINGKEY_COUPON_SEND = "hyjf-routingkey-coupon-send";
    public static final String QUEUE_COUPON_SEND = "hyjf-coupon-send";
    
    //双十二气球活动
    public static final String ROUTINGKEY_ACT_BALLOON = "hyjf-routingkey-act-balloon";
    public static final String QUEUE_ACT_BALLOON = "hyjf-queue-act-balloon";


    // 自动录标
    public static final String ROUTINGKEY_BORROW_SEND = "hyjf-routingkey-borrow-send";
    public static final String QUEUE_BORROW_SEND = "hyjf-borrow-send";

    // 自动备案
    public static final String ROUTINGKEY_BORROW_RECORD = "hyjf-routingkey-borrow-record";
    public static final String QUEUE_BORROW_RECORD = "hyjf-borrow-record";

    /*-------------------upd by liushouyi HJH3 Start--------------------------*/
    // 自动审核保证金
    public static final String ROUTINGKEY_BORROW_BAIL = "hyjf-routingkey-borrow-bail";
    public static final String QUEUE_BORROW_BAIL = "hyjf-borrow-bail";
    /*-------------------upd by liushouyi HJH3 End--------------------------*/
    
    // 自动初审
    public static final String ROUTINGKEY_BORROW_PREAUDIT = "hyjf-routingkey-borrow-preaudit";
    public static final String QUEUE_BORROW_PREAUDIT = "hyjf-borrow-preaudit";

    // 自动关联计划
    public static final String ROUTINGKEY_BORROW_ISSUE = "hyjf-routingkey-borrow-issue";
    public static final String QUEUE_BORROW_ISSUE = "hyjf-borrow-issue";

    // 自动汇直投优惠券使用
    public static final String ROUTINGKEY_HZT_COUPON_TENDER = "hyjf-routingkey-hzt-coupon-tender";
    public static final String QUEUE_HZT_COUPON_TENDER = "hyjf-hzt-coupon-tender";
    //红包账户流水明细文件下载
    public static final String DOWNLOAD_FILE_EVE = "hyjf-download-file-eve";

    // 法大大客户信息修改MQ
    public static final String ROUTINGKEY_USER_INFO_CHANGE = "fdd-routingkey-user-info-change";
    public static final String QUEUE_USER_INFO_CHANGE  = "fdd-user-info-change";

    // 法大大电子签章CA认证MQ
    public static final String ROUTINGKEY_CERTIFICATE_AUTHORITY = "fdd-routingkey-certificate-authority";
    public static final String QUEUE_CERTIFICATE_AUTHORITY = "fdd-certificate-authority";

    // 法大大生成合同接口
    public static final String ROUTINGKEY_GENERATE_CONTRACT = "fdd-routingkey-generate_contract";
    //法大大下载脱敏接口
    public static final String ROUTINGKEY_DOWNDESSENESITIZATION_CONTRACT = "fdd-routingkey-downdessenesitization_contract";


    public static final String ROUTINGKEY_AUTO_SIGN = "fdd-routingkey-auto_sign";
    
    // 风车理财通知配置
    public static final String ROUTINGKEY_WRB_CALLBACK_NOTIFY = "hyjf-routingkey-wrb-callback-notify";

    //红包账户流水明细文件数据导入
    public static final String READ_FILE_ALEVE = "hyjf-read-file-aleve";
    public static final String READ_FILE_EVE = "hyjf-read-file-eve";
    //rabbitMq的fanout模式(发布者订阅者模式)
    public static final String ALEV_FANOUT_EXCHANGE = "hyjf-aleve-fanout-exchange";
   // public static final String READ_DATA_EVE = "hyjf-read-data-eve";


    //crm出借信息推送
    public static final String ROUTINGKEY_POSTINTERFACE_CRM= "crm-routingkey-post-interface";
    public static final String QUEUE_POSTINTERFACE_CRM = "crm-post-interface";
    

    //crm开户信息推送
    public static final String ROUTINGKEY_BANCKOPEN_CRM= "crm-routingkey-banckopen-interface";
    public static final String QUEUE_BANCKOPEN_CRM = "crm-banckopen-interface";


    // 汇计划每小时计算公允价值
    public static final String ROUTINGKEY_HJH_CALCULATE_FAIR_VALUE = "hjh-calculate-fair-value";
    public static final String QUEUE_HJH_CALCULATE_FAIR_VALUE = "hjh_calculate_fair_value";
    //crm账户信息推送
//    public static final String ROUTINGKEY_DELAY_KEY="routingkey_delay_queue";
    public static final String ROUTINGKEY_DELAY_KEY="routingkey_delay_queue_new";
    public static final String ROUTINGKEY_ACCOUNT_CRM= "crm-routingkey-account-interface";
    public static final String QUEUE_ACCOUNT_CRM = "crm-account-interface";

    //运营数据
    public static final String ROUTINGKEY_OPERATION_DATA = "queue-operation-data";
    //生成居间服务协议后根据出借记录生成合同要素信息
    public static final String ROUTINGKEY_NIFA_CONTRACT_ESSENCE = "nifa-routingkey-contract-essence";
    //生成居间服务协议后根据出借记录生成合同要素信息
    public static final String ROUTINGKEY_NIFA_REPAY_INFO = "nifa-routingkey-repay-info";
    //生成居间服务协议后根据出借记录生成合同要素信息
    public static final String ROUTINGKEY_NIFA_REPAY_LATE = "nifa-routingkey-repay-late";
    
    //获取IP信息
    public static final String ROUTINGKEY_SYNC_USER_IP_USER = "hyjf-routingkey-userip-sync";
    public static final String QUEUE_SYNC_USER_IP_USER = "hyjf-userip-sync";


    //add by liuyang  20180711 神策数据统计相关 start
    // 神策数据统计:事件类型:用户登录事件
    public static final String ROUTINGKEY_SENSORS_DATA_LOGIN = "sensors_data_login";
    // 神策数据统计:事件类型:充值结果
    public static final String ROUTINGKEY_SENSORS_DATA_RECHARGE_RESULT = "sensors_data_recharge_result";
    // 神策数据统计:事件类型:提现结果
    public static final String ROUTINGKEY_SENSORS_DATA_WITHDRAW_RESULT = "sensors_data_withdraw_result";
    // 神策数据统计:事件类型:注册事件
    public static final String ROUTINGKEY_SENSORS_DATA_SIGN_UP = "sensors_data_sign_up";
    // 神策数据统计:事件类型:债转相关
    public static final String SENSORS_DATA_ROUTINGKEY_CREDIT = "sensors_data_routingkey_credit";
    // 神策数据统计:事件类型:汇直投出借相关
    public static final String SENSORS_DATA_ROUTINGKEY_HZT_INVEST = "sensors_data_routingkey_hzt_invest";
    // 神策数据统计:事件类型:汇计划出借相关
    public static final String SENSORS_DATA_ROUTINGKEY_HJH_INVEST = "sensors_data_routingkey_hjh_invest";
    // 神策数据统计:事件类型:开户成功
    public static final String SENSORS_DATA_ROUTINGKEY_OPEN_ACCOUNT = "sensors_data_routingkey_open_account";
    // 神策数据统计:事件类型:授权结果
    public static final String SENSORS_DATA_ROUTINGKEY_AUTH_RESULT = "sensors_data_routingkey_auth_result";
    // add by liuyang  20180711 神策数据统计相关 end
    //2018中秋活动
    public static final String MDIAU_ACTIVITY = "hyjf-routingkey-mdiau-activity";

    //20181008用户合规日志
    public static final String USER_LOG_SAVE = "hyjf-user-log-save";
    //20181108用纳觅返现活动
    public static final String RETURN_CASH_ACTIVITY = "hyjf-return-cash-activity-save";
    //20181108用纳觅返现活动散标处理
    public static final String RETURN_CASH1_ACTIVITY = "hyjf-return-cash1-activity-save";
    //2018发放优惠券
    public static final String SENDCOUPON_ACTIVITY = "hyjf-routingkey-sendcoupon-activity";

    // add 合规数据上报 MQ配置 liubin 20181122 start
    // routingKey开户成功
    public static final String OPEN_ACCOUNT_SUCCESS_DELAY_KEY="open_account_success_delay";
    // routingKey发标成功
    public static final String ISSUE_INVESTING_DELAY_KEY="issue_investing_delay";
    // routingKey满标
    public static final String ISSUE_INVESTED_DELAY_KEY="issue_invested_delay";
    // routingKey放款成功
    public static final String LOAN_SUCCESS_DELAY_KEY="loan_success_delay";
    // routingKey智投放款成功
    public static final String LOAN_HJH_SUCCESS_DELAY_KEY="loan_hjh_success_delay";
    // routingKey转让成功
    public static final String TRANSFER_SUCCESS_DELAY_KEY="transfer_success_delay";
    // routingKey单笔承接成功
    public static final String UNDERTAKE_SINGLE_SUCCESS_DELAY_KEY="undertake_single_success_delay";
    // routingKey全部承接失败
    public static final String UNDERTAKE_ALL_FAIL_DELAY_KEY="undertake_all_fail_delay";
    // routingKey全部承接成功
    public static final String UNDERTAKE_ALL_SUCCESS_DELAY_KEY="undertake_all_success_delay";
    // routingKey单笔还款成功
    public static final String REPAY_SINGLE_SUCCESS_DELAY_KEY="repay_single_success_delay";
    // routingKey全部还款成功
    public static final String REPAY_ALL_SUCCESS_DELAY_KEY="repay_all_success_delay";
    // routingKey新增智投
    public static final String HJHPLAN_ADD_DELAY_KEY="hjhplan_add_delay";
    // routingKey用户信息修改(修改手机号,修改银行卡号,风险测评)
    public static final String USERINFO_CHANGE_DELAY_KEY = "userinfo_change_delay";
    // routingKey 中互金处理历史数据和数据修复用单发mq消息队列用 add by liushouyi nifa2
    // 放款成功
    public static final String HYJF_NIFA_TENDER_INFO = "hyjf_nifa_tender_info_repair";
    // 还款成功
    public static final String HYJF_NIFA_REPAY_INFO = "hyjf_nifa_repay_info_repair";
    // 债转成功
    public static final String HYJF_NIFA_CREDITINFO = "hyjf_nifa_credit_info_repair";
    // routingKey 中互金处理历史数据和数据修复用单发mq消息队列用 add by liushouyi nifa2
    // add 合规数据上报 MQ配置 liubin 20181122 end








    // 合规数据上报 CERT MQ start

    // 异常处理MQ start
    public static final String ROUTINGKEY_CERT_ERROR_SEND = "cert_error_send";
    // 手机号哈希MQ start
    public static final String ROUTINGKEY_CERT_MOBILE_HASH= "cert_mobile_hash";
    // 异常处理MQ end

    // 旧数据上报  投资人  start
    public static final String ROUTINGKEY_CERT_OLD_DATA_TENDER_USER = "cert_old_data_tender_user";
    // 旧数据上报  借款人  start
    public static final String ROUTINGKEY_CERT_BORROW_USER = "cert_old_data_borrow_user";
    // 旧数据上报  标的  start
    public static final String ROUTINGKEY_CERT_BORROW_OLD = "cert_old_data_borrow";


    // 交易流水MQ start
    public static final String ROUTINGKEY_CERT_TRANSACT = "cert_transact";
    // 交易流水MQ end

    // 查询批次数据入库消息 start add by nxl
    public static final String ROUTINGKEY_CERT_YIBUMESSAGE = "cert_getyibu_message";
    // 查询批次数据入库消息 end add by nxl

    // 应急中心历史数据上报 add by nxl start
    // 散标状态
    public static final String ROUTINGKEY_CERT_BORROW_STATUS = "cert_old_borrow_status";
    // 还款计划
    public static final String ROUTINGKEY_CERT_BORROW_REPAYMENTPLAN = "cert_old_borrow_repaymentplan";
    // 债权信息
    public static final String ROUTINGKEY_CERT_TENDER_INFO = "cert_old_tender_info";
    // 承接信息
    public static final String ROUTINGKEY_CERT_CREDITTENDERINFO = "cert_old_credittenderinfo";
    // 转让项目
    public static final String ROUTINGKEY_CERT_OLD_CREDITINFO = "cert-old-creditinfo";
    // 转让状态
    public static final String ROUTINGKEY_CERT_OLD_CREDIT_STATUS = "cert-old-credit-status";
    // 交易明细
    public static final String ROUTINGKEY_CERT_OLD_TRANSACT = "cert-old-transact";
 // 交易明细
    public static final String ROUTINGKEY_CERT_OLD_TRANSACT_REPAY_SUCCESS = "cert-old-transact-repay-success";
    // 交易明细mongo上报
    public static final String ROUTINGKEY_CERT_OLD_ACCOUNT_LIST = "cert-old-accountlist";
    // 应急中心历史数据上报 add by nxl end



    // 异常中心  单个消息补发
    // 异常中心  用户信息
    public static final String ROUTINGKEY_CERT_EXCEPTION_TENDER_USER = "cert_exception_user";
    // 异常中心  标的信息
    public static final String ROUTINGKEY_CERT_EXCEPTION_BORROW_USER = "cert_exception_borrow";
    // 异常中心  交易流水
    public static final String ROUTINGKEY_CERT_EXCEPTION_TRANSACT = "cert_exception_transact";
    // 异常中心  散标状态
    public static final String ROUTINGKEY_CERT_EXCEPTION_BORROW_STATUS = "cert_exception_borrow_status";
    // 异常中心  还款计划
    public static final String ROUTINGKEY_CERT_EXCEPTION_BORROW_REPAYMENTPLAN = "cert_exception_borrow_repaymentplan";
    // 异常中心  债权信息
    public static final String ROUTINGKEY_CERT_EXCEPTION_TENDER_INFO = "cert_exception_tender_info";
    // 异常中心  承接信息
    public static final String ROUTINGKEY_CERT_EXCEPTION_CREDITTENDERINFO = "cert_exception_credittenderinfo";
    // 异常中心  转让项目
    public static final String ROUTINGKEY_CERT_EXCEPTION_CREDITINFO = "cert_exception_creditinfo";
    // 异常中心  转让状态
    public static final String ROUTINGKEY_CERT_EXCEPTION_CREDIT_STATUS = "cert_exception_credit_status";


    // 合规数据上报 CERT MQ end
}
