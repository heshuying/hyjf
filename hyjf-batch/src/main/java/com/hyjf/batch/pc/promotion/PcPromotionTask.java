package com.hyjf.batch.pc.promotion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.customize.batch.BatchPcPromotionCustomize;

/**
 * PC渠道统计明细更新定时 此定时只跑一次
 * 
 * @author liuyang
 *
 */
public class PcPromotionTask {
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private PcPromotionService pcPromotionService;

	public void run() {
		// 调用更新接口
		update();
	}

	private void update() {
		System.out.println("PC渠道统计明细更新定时start");
		if (isRun == 0) {
			isRun = 1;
			try {
				//
				List<UtmReg> list = this.pcPromotionService.selectUtmRegList();
				if (list != null && list.size() != 0) {
					for (UtmReg utmReg : list) {
						Integer userId = utmReg.getUserId();
						// 根据用户Id检索用户的首投金额
						BatchPcPromotionCustomize batchPcPromotionCustomize = this.pcPromotionService.selectUserInvestByUserId(userId);
						if (batchPcPromotionCustomize != null) {
							// 更新utmReg表数据
							boolean isUtmUpdateFlag = this.pcPromotionService.updateUtmReg(utmReg, batchPcPromotionCustomize);
							if (!isUtmUpdateFlag) {
								throw new Exception("更新hyjf_utm_reg表失败!");
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 1;
			}
		}
		System.out.println("PC渠道统计明细更新定时end");
	}
}
