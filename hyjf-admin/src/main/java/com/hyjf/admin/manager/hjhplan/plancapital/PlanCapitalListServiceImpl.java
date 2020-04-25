package com.hyjf.admin.manager.hjhplan.plancapital;

import cn.jpush.api.utils.StringUtils;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.auto.HjhPlanCapitalMapper;
import com.hyjf.mybatis.model.auto.HjhPlanCapital;
import com.hyjf.mybatis.model.auto.HjhPlanCapitalExample;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.customize.HjhAssetBorrowTypeCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class PlanCapitalListServiceImpl extends BaseServiceImpl implements PlanCapitalListService {

	Logger _log = LoggerFactory.getLogger(this.getClass());

	@Override
	public int countRecord(PlanCapitalListBean form) {
		// 封装查询条件
		HjhPlanCapitalExample example = setCondition(form);
		return hjhPlanCapitalMapper.countByExample(example);
	}

	@Override
	public List<HjhPlanCapital> getRecordList(PlanCapitalListBean form) {
		// 封装查询条件
		HjhPlanCapitalExample example = setCondition(form);
		// 分页条件
		example.setLimitStart(form.getLimitStart());
		example.setLimitEnd(form.getLimitEnd());
		// 排序
		example.setOrderByClause("`date` asc,`is_month` asc,`lock_period` asc ");
		return hjhPlanCapitalMapper.selectByExample(example);
	}

	@Override
	public Map<String, Object> sumRecord(PlanCapitalListBean form) {
		// 封装查询条件
		HjhPlanCapitalExample example = setCondition(form);
		return this.hjhPlanCapitalCustomizeMapper.selectSumRecord(example);
	}

	private HjhPlanCapitalExample setCondition(PlanCapitalListBean form) {
		HjhPlanCapitalExample example = new HjhPlanCapitalExample();
		HjhPlanCapitalExample.Criteria cra = example.createCriteria();
		// 查询条件计划编号
		if (StringUtils.isNotEmpty(form.getPlanNidSrch())) {
			cra.andPlanNidEqualTo(form.getPlanNidSrch() );
		}
		// 查询条件计划名称
		if (StringUtils.isNotEmpty(form.getPlanNameSrch())) {
			cra.andPlanNameEqualTo(form.getPlanNameSrch());
		}
		// 查询条件锁定期
		if (StringUtils.isNotEmpty(form.getLockPeriodSrch())) {
			cra.andLockPeriodEqualTo(Integer.valueOf(form.getLockPeriodSrch()));
		}
		// 查询条件开始日期
		if (StringUtils.isNotEmpty(form.getDateFromSrch())) {
			cra.andDateGreaterThanOrEqualTo(GetDate.stringToDate2(form.getDateFromSrch()));
		}
		// 查询条件结束日期
		if (StringUtils.isNotEmpty(form.getDateToSrch())) {
			cra.andDateLessThanOrEqualTo(GetDate.stringToDate2(form.getDateToSrch()));
		}

		return example;
	}

}
