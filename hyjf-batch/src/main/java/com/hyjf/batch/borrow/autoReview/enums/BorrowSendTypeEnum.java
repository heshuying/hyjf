package com.hyjf.batch.borrow.autoReview.enums;

public enum BorrowSendTypeEnum {
	FUSHENSEND_CD("AUTO_FULL"), FABIAO_CD("AUTO_BAIL");

	private String value;

	// 构造器默认也只能是private, 从而保证构造函数只能在内部使用
	BorrowSendTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
