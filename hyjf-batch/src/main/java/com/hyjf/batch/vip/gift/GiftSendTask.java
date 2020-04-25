package com.hyjf.batch.vip.gift;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;

public class GiftSendTask {
	/** 类名 */
	private static final String THIS_CLASS = GiftSendTask.class.getName();
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	GiftSendService giftSendService;

	public void run() {
		if (isOver) {
			try {
				isOver = false;
				// 发放vip礼包
				giftSend();
			} finally {
				isOver = true;
			}
		}
	}

	/**
	 * 发放vip礼包
	 */
	private void giftSend() {
		String methodName = "giftSend";
		LogUtil.startLog(THIS_CLASS, methodName, "检查未发放vip礼包 开始。");

		giftSendService.updateGiftSend();

		LogUtil.endLog(THIS_CLASS, methodName, "检查未发放vip礼包 结束。");
	}

}
