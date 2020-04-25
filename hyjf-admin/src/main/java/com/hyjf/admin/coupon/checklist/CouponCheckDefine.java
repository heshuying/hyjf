package com.hyjf.admin.coupon.checklist;

/**
 * @author lisheng
 * @version CouponCheckDefine, v0.1 2018/6/6 15:51
 */

public class CouponCheckDefine {
    /** 权限 CONTROLLOR @RequestMapping值 */
    public static final String REQUEST_MAPPING = "/coupon/checklist";
    /** 列表画面 路径 */
    public static final String LIST_PATH = "coupon/user/couponCheckList";
    /** 列表画面 @RequestMapping值 */
    public static final String INIT = "init";

    /** FORM */
    public static final String FORM = "couponCheckForm";

    public static final String DELETE_ACTION="deleteAction";

    public static final String IMP_DISTRIBUTEVIEW_ACTION = "impDistributeViewAction";

    /** 手动发布画面的路径 */
    public static final String IMP_DISTRIBUTE_PATH = "coupon/user/impCoupon";

    /** 批量手动发布的@RequestMapping值  */
    public static final String UPLOAD_ACTION = "uploadAction";

    /** 下载  */
    public static final String DOWNLOAD_ACTION = "downloadAction";
    /** 转跳审核页面  */
    public static final String AUDIT_INIT_ACTION = "auditInitAction";

    public static final String COUPON_PATH = "coupon/user/couponAudit";
    /** 审核  */
    public static final String AUDIT_ACTION= "auditAction";
    /**
	 * vip礼包
	 */
	protected static final Integer NUM_TWO = 2;
}
