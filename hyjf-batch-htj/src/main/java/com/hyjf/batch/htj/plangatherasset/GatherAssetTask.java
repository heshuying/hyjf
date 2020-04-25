package com.hyjf.batch.htj.plangatherasset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.customize.batch.BatchDebtPlanBorrowCustomize;

/**
 * 汇添金定时自动出借
 * @author wangkun
 */
public class GatherAssetTask {
	
	/** 运行状态 */
	private static int isRun = 0;
	
	@Autowired
	GatherAssetService gatherAssetService;

	/**
	 * 汇添金自动出借
	 */
	public void run() {
		getherAsset();
	}

	/**
	 * 汇添金主动出借逻辑
	 * @return
	 */
	private boolean getherAsset() {
		if (isRun == 0) {
			isRun = 1;
			System.out.println("汇添金资产未募集满  GatherAssetTask.run ... ");
			try {
				List<BatchDebtPlanBorrowCustomize> borrows = this.gatherAssetService.selectUnFullDebtBorrow(2);
				if(borrows!=null&&borrows.size()>0){
					for(BatchDebtPlanBorrowCustomize borrow : borrows){
						//计划编号
						String borrowNid = borrow.getBorrowNid();
						this.gatherAssetService.sendSms(borrowNid);
						this.gatherAssetService.sendEmail(borrowNid);
					}
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
				System.out.println("汇添金资产未募集满  GatherAssetTask.end ... ");
			}
		}
		return false;
	}
}
