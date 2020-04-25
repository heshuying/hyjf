/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.common.util;

/**
 * 存放所有活动的开始日期 - 结束日期
 * @author yinhui
 * @version ActivityDateUtil, v0.1 2018/9/8 15:36
 */
public class ActivityDateUtil {

    /** 2018中秋活动 ID ,在 huiyingdai_activity_list 表里*/
    public static  String MIDAU_ACTIVITY_ID = null;
    /** 2018中秋活动 奖励-0.9%加息券*/
    public static  String MIDAU_ACTIVITY_JX_POINT_NINE = null;
    /** 2018中秋活动 奖励-1.0%加息券*/
    public static  String MIDAU_ACTIVITY_JX_POINT_ZERO = null;
    /** 2018中秋活动 奖励-1.1%加息券*/
    public static  String MIDAU_ACTIVITY_JX_POINT_ONE = null;
    /** 2018中秋活动 奖励-1.2%加息券*/
    public static  String MIDAU_ACTIVITY_JX_POINT_TWO = null;
    /** 2018中秋活动 奖励-1.3%加息券*/
    public static  String MIDAU_ACTIVITY_JX_POINT_THREE= null;
    /** 2018中秋活动 奖励-1.4%加息券*/
    public static  String MIDAU_ACTIVITY_JX_POINT_FOUR = null;
    /** 2018中秋活动 奖励-1.5%加息券*/
    public static  String MIDAU_ACTIVITY_JX_POINT_FIVE = null;

    /** 2018中秋活动 奖励-40元代金券*/
    public static  String MIDAU_ACTIVITY_DJ_FOUR = null;
    /** 2018中秋活动 奖励-80元代金券*/
    public static  String MIDAU_ACTIVITY_DJ_EIGHT= null;
    /** 2018中秋活动 奖励-120元代金券*/
    public static  String MIDAU_ACTIVITY_DJ_TWELVE = null;
    /** 2018中秋活动 奖励-400元代金券*/
    public static  String MIDAU_ACTIVITY_DJ_FORTY = null;
    /** 2018中秋活动 奖励-800元代金券*/
    public static  String MIDAU_ACTIVITY_DJ_EIGHTY = null;
    /** 2018中秋活动 奖励-1200元代金券*/
    public static  String MIDAU_ACTIVITY_DJ_TWELVEHUNDRED = null;
    /** 2018中秋活动 奖励-1600元代金券*/
    public static  String MIDAU_ACTIVITY_DJ_SIXTEENHUNDRED = null;

    /** 2018双十一活动 ID ,在 huiyingdai_activity_list 表里*/
    public static  String TWOELEVEN_ACTIVITY_ID = null;

    /** 2018双十一活动 11:11 ~ 13:11奖励-0.8%加息券*/
    public static  String TWOELEVEN_ACTIVITY_JX_ONE = null;
    /** 2018双十一活动 11:11 ~ 13:11奖励-0.9%加息券*/
    public static  String TWOELEVEN_ACTIVITY_JX_TWO = null;
    /** 2018双十一活动 11:11 ~ 13:11奖励-1.0%加息券*/
    public static  String TWOELEVEN_ACTIVITY_JX_THREE= null;
    /** 2018双十一活动 13:11 ~ 15:11奖励-0.8%加息券*/
    public static  String TWOELEVEN_ACTIVITY_JX_FOUR = null;
    /** 2018双十一活动 13:11 ~ 15:11奖励-0.9%加息券*/
    public static  String TWOELEVEN_ACTIVITY_JX_FIVE = null;
    /** 2018双十一活动 13:11 ~ 15:11奖励-1.0%加息券*/
    public static  String TWOELEVEN_ACTIVITY_JX_SIX= null;
    /** 2018双十一活动 17:11 ~ 19:11奖励-0.8%加息券*/
    public static  String TWOELEVEN_ACTIVITY_JX_SEVEN = null;
    /** 2018双十一活动 17:11 ~ 19:11奖励-0.9%加息券*/
    public static  String TWOELEVEN_ACTIVITY_JX_EIGHT = null;
    /** 2018双十一活动 17:11 ~ 19:11奖励-1.0%加息券*/
    public static  String TWOELEVEN_ACTIVITY_JX_NINE= null;

    /** 2018双十一活动 11:11 ~ 13:11奖励-11.11元代金券*/
    public static  String TWOELEVEN_ACTIVITY_DJ_ONE = null;
    /** 2018双十一活动 11:11 ~ 13:11奖励-88.88元代金券*/
    public static  String TWOELEVEN_ACTIVITY_DJ_TWO = null;
    /** 2018双十一活动 11:11 ~ 13:11奖励-111元代金券*/
    public static  String TWOELEVEN_ACTIVITY_DJ_THREE= null;
    /** 2018双十一活动 13:11 ~ 15:11奖励-11.11元代金券*/
    public static  String TWOELEVEN_ACTIVITY_DJ_FOUR = null;
    /** 2018双十一活动 13:11 ~ 15:11奖励-88.88元代金券*/
    public static  String TWOELEVEN_ACTIVITY_DJ_FIVE = null;
    /** 2018双十一活动 13:11 ~ 15:11奖励-111元代金券*/
    public static  String TWOELEVEN_ACTIVITY_DJ_SIX= null;
    /** 2018双十一活动 17:11 ~ 19:11奖励-11.11元代金券*/
    public static  String TWOELEVEN_ACTIVITY_DJ_SEVEN = null;
    /** 2018双十一活动 17:11 ~ 19:11奖励-88.88元代金券*/
    public static  String TWOELEVEN_ACTIVITY_DJ_EIGHT= null;
    /** 2018双十一活动 17:11 ~ 19:11奖励-111元代金券*/
    public static  String TWOELEVEN_ACTIVITY_DJ_NINE= null;

    /** 2018纳觅活动 ID ,在 huiyingdai_activity_list 表里*/
    public static  String RETURNCASH_ACTIVITY_ID = null;


    static {
        // 取得是否线上
        String online = null;
        /**#环境类型  0测试   1预生产  99正式*/
        online = PropUtils.getSystem(PropertiesConstants.HYJF_ONLINE_TYPE);
        if (online == null ) {
            online = "0";
        }
        //测试
        if ("0".equals(online)) {
            MIDAU_ACTIVITY_ID = "55";
            RETURNCASH_ACTIVITY_ID = "58";
            /** 2018中秋活动 奖励-0.9%加息券*/
            MIDAU_ACTIVITY_JX_POINT_NINE =  "PJ8164532";
            /** 2018中秋活动 奖励-1.0%加息券*/
            MIDAU_ACTIVITY_JX_POINT_ZERO =  "PJ4829516";
            /** 2018中秋活动 奖励-1.1%加息券*/
            MIDAU_ACTIVITY_JX_POINT_ONE =   "PJ8105923";
            /** 2018中秋活动 奖励-1.2%加息券*/
            MIDAU_ACTIVITY_JX_POINT_TWO =   "PJ9243510";
            /** 2018中秋活动 奖励-1.3%加息券*/
            MIDAU_ACTIVITY_JX_POINT_THREE=  "PJ4695238";
            /** 2018中秋活动 奖励-1.4%加息券*/
            MIDAU_ACTIVITY_JX_POINT_FOUR =  "PJ5081976";
            /** 2018中秋活动 奖励-1.5%加息券*/
            MIDAU_ACTIVITY_JX_POINT_FIVE =  "PJ1509367";

            /** 2018中秋活动 奖励-40元代金券*/
            MIDAU_ACTIVITY_DJ_FOUR = "PD9087563";
            /** 2018中秋活动 奖励-80元代金券*/
            MIDAU_ACTIVITY_DJ_EIGHT= "PD0589461";
            /** 2018中秋活动 奖励-120元代金券*/
            MIDAU_ACTIVITY_DJ_TWELVE = "PD1059683";
            /** 2018中秋活动 奖励-400元代金券*/
            MIDAU_ACTIVITY_DJ_FORTY = "PD5084179";
            /** 2018中秋活动 奖励-800元代金券*/
            MIDAU_ACTIVITY_DJ_EIGHTY = "PD7608231";
            /** 2018中秋活动 奖励-1200元代金券*/
            MIDAU_ACTIVITY_DJ_TWELVEHUNDRED = "PD3268714";
            /** 2018中秋活动 奖励-1600元代金券*/
            MIDAU_ACTIVITY_DJ_SIXTEENHUNDRED = "PD2609147";

            TWOELEVEN_ACTIVITY_ID= "55";
            /** 2018双十一活动 11:11 ~ 13:11奖励-0.8%加息券*/
           TWOELEVEN_ACTIVITY_JX_ONE = "PJ3296705";
            /** 2018双十一活动 11:11 ~ 13:11奖励-0.9%加息券*/
           TWOELEVEN_ACTIVITY_JX_TWO = "PJ1948706";
            /** 2018双十一活动 11:11 ~ 13:11奖励-1.0%加息券*/
           TWOELEVEN_ACTIVITY_JX_THREE= "PJ7026831";
            /** 2018双十一活动 13:11 ~ 15:11奖励-0.8%加息券*/
           TWOELEVEN_ACTIVITY_JX_FOUR = "PJ7469315";
            /** 2018双十一活动 13:11 ~ 15:11奖励-0.9%加息券*/
           TWOELEVEN_ACTIVITY_JX_FIVE = "PJ2806917";
            /** 2018双十一活动 13:11 ~ 15:11奖励-1.0%加息券*/
           TWOELEVEN_ACTIVITY_JX_SIX= "PJ3950126";
            /** 2018双十一活动 17:11 ~ 19:11奖励-0.8%加息券*/
           TWOELEVEN_ACTIVITY_JX_SEVEN = "PJ5732496";
            /** 2018双十一活动 17:11 ~ 19:11奖励-0.9%加息券*/
           TWOELEVEN_ACTIVITY_JX_EIGHT = "PJ5238971";
            /** 2018双十一活动 17:11 ~ 19:11奖励-1.0%加息券*/
           TWOELEVEN_ACTIVITY_JX_NINE= "PJ7289130";

            /** 2018双十一活动 11:11 ~ 13:11奖励-11.11元代金券*/
           TWOELEVEN_ACTIVITY_DJ_ONE = "PD4928136";
            /** 2018双十一活动 11:11 ~ 13:11奖励-88.88元代金券*/
           TWOELEVEN_ACTIVITY_DJ_TWO = "PD9170624";
            /** 2018双十一活动 11:11 ~ 13:11奖励-111元代金券*/
           TWOELEVEN_ACTIVITY_DJ_THREE= "PD4296510";
            /** 2018双十一活动 13:11 ~ 15:11奖励-11.11元代金券*/
           TWOELEVEN_ACTIVITY_DJ_FOUR = "PD1489620";
            /** 2018双十一活动 13:11 ~ 15:11奖励-88.88元代金券*/
           TWOELEVEN_ACTIVITY_DJ_FIVE = "PD4197325";
            /** 2018双十一活动 13:11 ~ 15:11奖励-111元代金券*/
           TWOELEVEN_ACTIVITY_DJ_SIX= "PD6720583";
            /** 2018双十一活动 17:11 ~ 19:11奖励-11.11元代金券*/
           TWOELEVEN_ACTIVITY_DJ_SEVEN = "PD1807596";
            /** 2018双十一活动 17:11 ~ 19:11奖励-88.88元代金券*/
           TWOELEVEN_ACTIVITY_DJ_EIGHT= "PD5746328";
            /** 2018双十一活动 17:11 ~ 19:11奖励-111元代金券*/
           TWOELEVEN_ACTIVITY_DJ_NINE= "PD2951786";

        }
        //预生产
        else if ("1".equals(online)){
            MIDAU_ACTIVITY_ID = "55";
            RETURNCASH_ACTIVITY_ID = "58";
            /** 2018中秋活动 奖励-0.9%加息券*/
            MIDAU_ACTIVITY_JX_POINT_NINE =  "PJ9213486";
            /** 2018中秋活动 奖励-1.0%加息券*/
            MIDAU_ACTIVITY_JX_POINT_ZERO =  "PJ0176593";
            /** 2018中秋活动 奖励-1.1%加息券*/
            MIDAU_ACTIVITY_JX_POINT_ONE =   "PJ8463971";
            /** 2018中秋活动 奖励-1.2%加息券*/
            MIDAU_ACTIVITY_JX_POINT_TWO =   "PJ8240569";
            /** 2018中秋活动 奖励-1.3%加息券*/
            MIDAU_ACTIVITY_JX_POINT_THREE=  "PJ7685921";
            /** 2018中秋活动 奖励-1.4%加息券*/
            MIDAU_ACTIVITY_JX_POINT_FOUR =  "PJ1072683";
            /** 2018中秋活动 奖励-1.5%加息券*/
            MIDAU_ACTIVITY_JX_POINT_FIVE =  "PJ7945260";

            /** 2018中秋活动 奖励-40元代金券*/
            MIDAU_ACTIVITY_DJ_FOUR = "PD4295130";
            /** 2018中秋活动 奖励-80元代金券*/
            MIDAU_ACTIVITY_DJ_EIGHT= "PD8712354";
            /** 2018中秋活动 奖励-120元代金券*/
            MIDAU_ACTIVITY_DJ_TWELVE = "PD8132769";
            /** 2018中秋活动 奖励-400元代金券*/
            MIDAU_ACTIVITY_DJ_FORTY = "PD7143205";
            /** 2018中秋活动 奖励-800元代金券*/
            MIDAU_ACTIVITY_DJ_EIGHTY = "PD9128307";
            /** 2018中秋活动 奖励-1200元代金券*/
            MIDAU_ACTIVITY_DJ_TWELVEHUNDRED = "PD4382017";
            /** 2018中秋活动 奖励-1600元代金券*/
            MIDAU_ACTIVITY_DJ_SIXTEENHUNDRED = "PD6184930";
        }
        //生产
        else if ("99".equals(online)) {
            MIDAU_ACTIVITY_ID = "55";
            RETURNCASH_ACTIVITY_ID = "58";
            /** 2018中秋活动 奖励-0.9%加息券*/
            MIDAU_ACTIVITY_JX_POINT_NINE =  "PJ4829017";
            /** 2018中秋活动 奖励-1.0%加息券*/
            MIDAU_ACTIVITY_JX_POINT_ZERO =  "PJ9405832";
            /** 2018中秋活动 奖励-1.1%加息券*/
            MIDAU_ACTIVITY_JX_POINT_ONE =   "PJ0793215";
            /** 2018中秋活动 奖励-1.2%加息券*/
            MIDAU_ACTIVITY_JX_POINT_TWO =   "PJ8417936";
            /** 2018中秋活动 奖励-1.3%加息券*/
            MIDAU_ACTIVITY_JX_POINT_THREE=  "PJ6934512";
            /** 2018中秋活动 奖励-1.4%加息券*/
            MIDAU_ACTIVITY_JX_POINT_FOUR =  "PJ3421897";
            /** 2018中秋活动 奖励-1.5%加息券*/
            MIDAU_ACTIVITY_JX_POINT_FIVE =  "PJ2801453";

            /** 2018中秋活动 奖励-40元代金券*/
            MIDAU_ACTIVITY_DJ_FOUR = "PD1390854";
            /** 2018中秋活动 奖励-80元代金券*/
            MIDAU_ACTIVITY_DJ_EIGHT= "PD7283109";
            /** 2018中秋活动 奖励-120元代金券*/
            MIDAU_ACTIVITY_DJ_TWELVE = "PD0496715";
            /** 2018中秋活动 奖励-400元代金券*/
            MIDAU_ACTIVITY_DJ_FORTY = "PD3045718";
            /** 2018中秋活动 奖励-800元代金券*/
            MIDAU_ACTIVITY_DJ_EIGHTY = "PD1438759";
            /** 2018中秋活动 奖励-1200元代金券*/
            MIDAU_ACTIVITY_DJ_TWELVEHUNDRED = "PD7926581";
            /** 2018中秋活动 奖励-1600元代金券*/
            MIDAU_ACTIVITY_DJ_SIXTEENHUNDRED = "PD3658974";


            TWOELEVEN_ACTIVITY_ID= "56";
            /** 2018双十一活动 11:11 ~ 13:11奖励-0.8%加息券*/
            TWOELEVEN_ACTIVITY_JX_ONE = "PJ7859162";
            /** 2018双十一活动 11:11 ~ 13:11奖励-0.9%加息券*/
            TWOELEVEN_ACTIVITY_JX_TWO = "PJ4589760";
            /** 2018双十一活动 11:11 ~ 13:11奖励-1.0%加息券*/
            TWOELEVEN_ACTIVITY_JX_THREE= "PJ3095247";
            /** 2018双十一活动 13:11 ~ 15:11奖励-0.8%加息券*/
            TWOELEVEN_ACTIVITY_JX_FOUR = "PJ9104872";
            /** 2018双十一活动 13:11 ~ 15:11奖励-0.9%加息券*/
            TWOELEVEN_ACTIVITY_JX_FIVE = "PJ4693802";
            /** 2018双十一活动 13:11 ~ 15:11奖励-1.0%加息券*/
            TWOELEVEN_ACTIVITY_JX_SIX= "PJ0917542";
            /** 2018双十一活动 17:11 ~ 19:11奖励-0.8%加息券*/
            TWOELEVEN_ACTIVITY_JX_SEVEN = "PJ3512680";
            /** 2018双十一活动 17:11 ~ 19:11奖励-0.9%加息券*/
            TWOELEVEN_ACTIVITY_JX_EIGHT = "PJ4068239";
            /** 2018双十一活动 17:11 ~ 19:11奖励-1.0%加息券*/
            TWOELEVEN_ACTIVITY_JX_NINE= "PJ4305812";

            /** 2018双十一活动 11:11 ~ 13:11奖励-11.11元代金券*/
            TWOELEVEN_ACTIVITY_DJ_ONE = "PD8250173";
            /** 2018双十一活动 11:11 ~ 13:11奖励-88.88元代金券*/
            TWOELEVEN_ACTIVITY_DJ_TWO = "PD4768251";
            /** 2018双十一活动 11:11 ~ 13:11奖励-111元代金券*/
            TWOELEVEN_ACTIVITY_DJ_THREE= "PD8321049";
            /** 2018双十一活动 13:11 ~ 15:11奖励-11.11元代金券*/
            TWOELEVEN_ACTIVITY_DJ_FOUR = "PD0348597";
            /** 2018双十一活动 13:11 ~ 15:11奖励-88.88元代金券*/
            TWOELEVEN_ACTIVITY_DJ_FIVE = "PD9152437";
            /** 2018双十一活动 13:11 ~ 15:11奖励-111元代金券*/
            TWOELEVEN_ACTIVITY_DJ_SIX= "PD3786145";
            /** 2018双十一活动 17:11 ~ 19:11奖励-11.11元代金券*/
            TWOELEVEN_ACTIVITY_DJ_SEVEN = "PD3107495";
            /** 2018双十一活动 17:11 ~ 19:11奖励-88.88元代金券*/
            TWOELEVEN_ACTIVITY_DJ_EIGHT= "PD0715869";
            /** 2018双十一活动 17:11 ~ 19:11奖励-111元代金券*/
            TWOELEVEN_ACTIVITY_DJ_NINE= "PD4037251";
        }
    }


}
