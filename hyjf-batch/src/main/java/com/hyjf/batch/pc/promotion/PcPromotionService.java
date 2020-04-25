package com.hyjf.batch.pc.promotion;

import java.util.List;

import com.hyjf.batch.BaseService;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.customize.batch.BatchPcPromotionCustomize;

/**
 * PC渠道统计明细更新Service
 * 
 * @author liuyang
 *
 */
public interface PcPromotionService extends BaseService {
	/**
	 * 检索UtmReg列表
	 * 
	 * @return
	 */
	public List<UtmReg> selectUtmRegList();

	/**
	 * 根据用户Id获取用户出借信息
	 * 
	 * @param userId
	 * @return
	 */
	public BatchPcPromotionCustomize selectUserInvestByUserId(Integer userId);

	/**
	 * 更新UtmReg表
	 * 
	 * @param utmReg
	 * @param batchPcPromotionCustomize
	 * @return
	 */
	public boolean updateUtmReg(UtmReg utmReg, BatchPcPromotionCustomize batchPcPromotionCustomize);
}
