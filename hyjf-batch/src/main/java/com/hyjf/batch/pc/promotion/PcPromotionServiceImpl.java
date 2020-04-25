package com.hyjf.batch.pc.promotion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.UtmReg;
import com.hyjf.mybatis.model.auto.UtmRegExample;
import com.hyjf.mybatis.model.customize.batch.BatchPcPromotionCustomize;

/**
 * PC渠道统计明细更新定时Service实现类
 * 
 * @author liuyang
 *
 */
@Service
public class PcPromotionServiceImpl extends BaseServiceImpl implements PcPromotionService {

	@Override
	public List<UtmReg> selectUtmRegList() {
		UtmRegExample example = new UtmRegExample();
		UtmRegExample.Criteria cra = example.createCriteria();
		// 检索首投时间为空的
		cra.andInvestTimeIsNull();
		return this.utmRegMapper.selectByExample(example);
	}

	/**
	 * 根据用户ID检索用户出借信息
	 */
	@Override
	public BatchPcPromotionCustomize selectUserInvestByUserId(Integer userId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		List<BatchPcPromotionCustomize> result = this.batchPcPromotionCustomizeMapper.selectPcPromotionCustomizeList(param);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 更新UtmReg表
	 */
	@Override
	public boolean updateUtmReg(UtmReg utmReg, BatchPcPromotionCustomize batchPcPromotionCustomize) {
		// 首投时间
		utmReg.setInvestTime(Integer.parseInt(batchPcPromotionCustomize.getInvestTime()));
		// 首投金额
		utmReg.setInvestAmount(batchPcPromotionCustomize.getInvestAmount());
		// 首投项目期限
		utmReg.setInvestProjectPeriod(batchPcPromotionCustomize.getInvestProjectPeriod());
		// 首投项目类型
		utmReg.setInvestProjectType(batchPcPromotionCustomize.getInvestProjectType());

		return this.utmRegMapper.updateByPrimaryKeySelective(utmReg) > 0 ? true : false;
	}
}
