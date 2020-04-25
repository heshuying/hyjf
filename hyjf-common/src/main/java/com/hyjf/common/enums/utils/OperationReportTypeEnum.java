/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.common.enums.utils;

/**
 * 运营报告类型
 * @author yinhui
 * @version OperationReportType, v0.1 2018/6/28 11:57
 */
public enum OperationReportTypeEnum {

    /**月度*/
    MONTH(1),
    /**季度*/
    QUARTER(2),
    /**半年度*/
    HALFYEAR(3),
    /**全年度*/
    YEAR(4);

    int code;

    private OperationReportTypeEnum(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
