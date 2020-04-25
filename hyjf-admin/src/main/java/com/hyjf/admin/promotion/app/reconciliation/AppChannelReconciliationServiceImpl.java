package com.hyjf.admin.promotion.app.reconciliation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.UtmPlat;
import com.hyjf.mybatis.model.auto.UtmPlatExample;
import com.hyjf.mybatis.model.customize.admin.AppChannelReconciliationCustomize;

@Service
public class AppChannelReconciliationServiceImpl extends BaseServiceImpl implements AppChannelReconciliationService {


	/**
	 * 获取列表数
	 * @param appChannelReconciliationCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public Integer countAppChannelReconciliationRecord(AppChannelReconciliationCustomize appChannelReconciliationCustomize) {
		return appChannelReconciliationCustomizeMapper.countAppChannelReconciliationRecord(appChannelReconciliationCustomize);
			
	}

	/**
	 * 获取列表数_汇计划
	 * @param appChannelReconciliationCustomize
	 * @return
	 */
	@Override
	public Integer countAppChannelReconciliationRecordHjh(AppChannelReconciliationCustomize appChannelReconciliationCustomize) {
		return appChannelReconciliationCustomizeMapper.countAppChannelReconciliationRecordHjh(appChannelReconciliationCustomize);
	}

	/**
	 * 获取列表
	 * @param appChannelReconciliationCustomize
	 * @return
	 * @author Michael
	 */
		
	@Override
	public List<AppChannelReconciliationCustomize> selectAppChannelReconciliationRecord(
			AppChannelReconciliationCustomize appChannelReconciliationCustomize) {
		return appChannelReconciliationCustomizeMapper.selectAppChannelReconciliationRecord(appChannelReconciliationCustomize);

	}

	/**
	 * 获取列表_汇计划
	 * @param appChannelReconciliationCustomize
	 * @return
	 */
	@Override
	public List<AppChannelReconciliationCustomize> selectAppChannelReconciliationRecordHjh(AppChannelReconciliationCustomize appChannelReconciliationCustomize) {
		return appChannelReconciliationCustomizeMapper.selectAppChannelReconciliationRecordHjh(appChannelReconciliationCustomize);
	}

	/**
	 *获取app渠道
	 */
	@Override
	public List<UtmPlat> utmPlatListAppGet(UtmPlat utmPlat) {
		UtmPlatExample utmPlatExample=new UtmPlatExample();
		UtmPlatExample.Criteria cra = utmPlatExample.createCriteria();
		cra.andSourceTypeEqualTo(utmPlat.getSourceType());
		List<UtmPlat> utmtTypeList=this.utmPlatMapper.selectByExample(utmPlatExample);
		return utmtTypeList;
	}

}
