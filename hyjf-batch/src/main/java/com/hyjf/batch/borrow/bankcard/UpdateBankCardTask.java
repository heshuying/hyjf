package com.hyjf.batch.borrow.bankcard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.util.TimeCountTool;
import com.hyjf.mybatis.model.auto.AccountChinapnr;

/**
 * 更新银行卡信息定时任务
 * 
 * @author 孙亮
 * @since 2016年1月18日 下午4:24:13
 */
public class UpdateBankCardTask {
	public static String METHODNAME = "run";
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	@Autowired
	private UpdateBankCardService updateBankCardService;

	public void run() {
		if (isOver) {
			isOver = false;
			TimeCountTool timeCountTool = new TimeCountTool();
			timeCountTool.printBeginDate();
			List<AccountChinapnr> accountChinapnrList = updateBankCardService.getAllAccountChinapnr();
			timeCountTool.printYongshi("查询AccountChinapnr列表");
			int size = accountChinapnrList.size();
			for (int i = 0; i < size; i++) {
				AccountChinapnr accountChinapnr = accountChinapnrList.get(i);
				updateBankCardService.updateAccountChinapnrBank(accountChinapnr);
//				timeCountTool.printYongshi("更新第" + (i + 1) + "个用户的银行卡信息,用户ID:"+accountChinapnr.getUserId()+",汇付号:"+accountChinapnr.getChinapnrUsrcustid()+",共" + size + "个");
			}
			timeCountTool.printZongYongshi();
			isOver = true;
		}
	}

}
