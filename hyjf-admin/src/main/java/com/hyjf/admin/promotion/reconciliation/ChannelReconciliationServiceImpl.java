package com.hyjf.admin.promotion.reconciliation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.admin.PcChannelReconciliationCustomize;

@Service
public class ChannelReconciliationServiceImpl extends BaseServiceImpl implements ChannelReconciliationService {


	/**
	 * 获取列表数
	 * @param pcChannelReconciliationCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer countPcChannelReconciliationRecord(PcChannelReconciliationCustomize pcChannelReconciliationCustomize) {
		return pcChannelReconciliationCustomizeMapper.countPcChannelReconciliationRecord(pcChannelReconciliationCustomize);
			
	}
	
	
	/**
     * 获取列表数-计划
     * @param pcChannelReconciliationCustomize
     * @return
     * @author Michael
     */
	@Override
    public Integer countPcChannelReconciliationRecordHjh(PcChannelReconciliationCustomize pcChannelReconciliationCustomize) {
        return pcChannelReconciliationCustomizeMapper.countPcChannelReconciliationRecordHjh(pcChannelReconciliationCustomize);
            
    }

	/**
	 * 获取列表
	 * @param pcChannelReconciliationCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<PcChannelReconciliationCustomize> selectPcChannelReconciliationRecord(
			PcChannelReconciliationCustomize pcChannelReconciliationCustomize) {
		return pcChannelReconciliationCustomizeMapper.selectPcChannelReconciliationRecord(pcChannelReconciliationCustomize);
			
	}
	
	/**
     * 获取列表-计划
     * @param pcChannelReconciliationCustomize
     * @return
     * @author Michael
     */
	@Override
    public List<PcChannelReconciliationCustomize> selectPcChannelReconciliationRecordHjh(
            PcChannelReconciliationCustomize pcChannelReconciliationCustomize) {
        return pcChannelReconciliationCustomizeMapper.selectPcChannelReconciliationRecordHjh(pcChannelReconciliationCustomize);
            
    }
	/**
	 *获取pc渠道
	 */
	@Override
	public List<UtmPlat> utmPlatListPcGet(UtmPlat utmPlat) {
		UtmPlatExample utmPlatExample=new UtmPlatExample();
		UtmPlatExample.Criteria cra = utmPlatExample.createCriteria();
		cra.andSourceTypeEqualTo(utmPlat.getSourceType());
		List<UtmPlat> utmtTypeList=this.utmPlatMapper.selectByExample(utmPlatExample);
		return utmtTypeList;
	}

}
