/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: Administrator
 * @version: 1.0
 * Created at: 2015年11月23日 下午1:25:59
 * Modification History:
 * Modified by : 
 */

package com.hyjf.batch.newyear2016.sendprize;

import java.math.BigDecimal;

import com.hyjf.common.util.PropUtils;

/**
 * @author Administrator
 */

public enum CouponEnum {
	
	// 10元代金券
	DAIJINQUAN_10(10, PropUtils.getSystem("hyjf.newyear.djq.code10")),
	// 20元代金券
	DAIJINQUAN_20(20, PropUtils.getSystem("hyjf.newyear.djq.code20")),
	// 30元代金券
	DAIJINQUAN_30(30, PropUtils.getSystem("hyjf.newyear.djq.code30")),
	// 40元代金券
	DAIJINQUAN_40(40, PropUtils.getSystem("hyjf.newyear.djq.code40")),
	// 50元代金券
	DAIJINQUAN_50(50, PropUtils.getSystem("hyjf.newyear.djq.code50")),
	// 60元代金券
	DAIJINQUAN_60(60, PropUtils.getSystem("hyjf.newyear.djq.code60"));

	// 成员变量
	private String value;
	private int key;

	// 构造方法
	private CouponEnum(int key, String value) {
		this.key = key;
		this.value = value;
	}

	// 普通方法
	public static String getName(int key) {
		for (CouponEnum c : CouponEnum.values()) {
			if (c.getKey() == key) {
				return c.value;
			}
		}
		return null;
	}

	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public static void main(String[] args) {
		BigDecimal VipValue = new BigDecimal("1.83");
		//ROUND_DOWN
		System.out.println(VipValue.setScale(0, BigDecimal.ROUND_DOWN));
	}
}
