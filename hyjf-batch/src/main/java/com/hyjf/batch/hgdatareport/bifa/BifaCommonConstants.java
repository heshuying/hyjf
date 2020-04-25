/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa;

/**
 * @author jun
 * @version BifaCommonConstants, v0.1 2018/11/30 14:05
 */
public class BifaCommonConstants {

    public static final String SUCCESSCODE = "0000";
    /**
     * 已经上报过
     */
    public static final String REPORTEDCODE = "0007";
    /**
     * 已经上报过描述
     */
    public static final String EXIST= "不可以重复添加";

    public static final String SUCCESSDESC = "操作成功";

    public static final String ERRCODE = "9999";

    public static final String ERRDESC = "操作失败";

    /**
     * 出借=0的用户  未开户用户不报送
     */
    public static final String DATATYPE_1003 = "1003";
    /**
     * 出借大于0的用户
     */
    public static final String DATATYPE_1002 = "1002";
    /**
     * 注册用户
     */
    public static final String DATATYPE_1005 = "1005";
    /**
     *借贷用户
     */
    public static final String DATATYPE_1000 = "1000";

    /**
     * sha256索引集合名
     */
    public static final String HT_USERINFOSHA256_COLLECTIONNAME = "ht_user_info_sha256";

    /**
     * 散标信息集合名
     */
    public static final String HT_BIFA_BORROWINFO = "ht_bifa_borrowinfo";

    /**
     * 标的状态更新集合名
     */
    public static final String HT_BIFA_BORROW_STATUS = "ht_bifa_borrow_status";

    /**
     * 标的转让集合名
     */
    public static final String HT_BIFA_CREDITTENDERINFO = "ht_bifa_credittenderinfo";

    /**
     * 智投集合名
     */
    public static final String HT_BIFA_HJH_PLANINFO = "ht_bifa_hjh_planinfo";
}
