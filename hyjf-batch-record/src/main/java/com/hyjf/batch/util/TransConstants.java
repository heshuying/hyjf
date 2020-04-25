package com.hyjf.batch.util;

import com.hyjf.common.util.GetOrderIdUtils;

/**
 * 常量文件
 *
 * @author Michael
 * @version 1.0.0
 */
public class TransConstants {

	/** 空格定义 */
	public static final String BLANK = " ";

	/** 还款类型   等额本息 */
	public static final String BORROW_STYLE_MONTH = "month";

	/** 还款类型   等额本金*/
	public static final String BORROW_STYLE_PRINCIPAL = "principal";

	/** 还款类型   按月计息，到期还本还息*/
	public static final String BORROW_STYLE_END = "end";

	/** 还款类型   按天计息，到期还本还息*/
	public static final String BORROW_STYLE_ENDDAY = "endday";

	/** 还款类型   先息后本*/
	public static final String BORROW_STYLE_ENDMONTH = "endmonth";

	/** 银行编号*/
	public static final String BANK_CODE = "3005";
	
	/** 产品编号*/
	public static final String PRODUCT_CODE = "0082";
	
	/** 产品发行方*/
	public static final String PRODUCT_USER = "LZ";
	
	/** 账户类型  - 2活期账户*/
	public static final Integer ACC_TYPE_2 = 2;
	
	/** 合作编号*/
	public static final String COINST_CODE = "000154";
	
	/** 01-身份证18位 */
	public static final String ID_TYPE_1 = "01";
	
	/** 02-身份证15位*/
	public static final String ID_TYPE_2 = "02";
	
	/** 账户类型 - 0个人账户*/
	public static final String ACCOUNT_TYPE = "0";
	
	/**业务类型 - APPZX批量开户*/
    public static final String PRODUCT_CODE_APPZX = "APPZX";
    
    /**业务类型  - BID债转迁移 */
    public static final String PRODUCT_CODE_BID = "BID";
    
    /**业务类型 - DEBT标的迁移*/
    public static final String PRODUCT_CODE_DEBT = "DEBT";
    
    /**业务类型 - BIDIN标的迁移*/
    public static final String PRODUCT_CODE_BIDIN = "BIDIN";
    
    /**业务类型 - SIGRAN签约关系*/
    public static final String PRODUCT_CODE_SIGTRAN = "SIGTRAN";

    /**币种 156-人民币*/
    public static final String CURRENCY = "156";
    
    /**江西sit时间*/
    public static final String SIT_TIME = GetOrderIdUtils.getTxDate();
    
    /**符号*/
    public static final String SYMBOL = "-";
    
    /**FOURZERO*/
    public static final String FOURZERO = "0000";
    
    /**TWOZERO*/
    public static final String TWOZERO = "00";
    
    /**开户结果文件长度*/
    public static final Integer ACCRETLENGTH = 364;
    
    /**债权迁移结果文件长度*/
    public static final Integer DEBTRETLENGTH = 282;
    
    /**签约关系结果文件长度*/
    public static final Integer SIGRETLENGTH = 290;
    
    /**标的迁移结果文件长度*/
    public static final Integer SUBJECTRETLENGTH = 473;
    
}
