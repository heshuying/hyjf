package com.hyjf.admin.manager.config.borrow.finsercharge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowFinserCharge;
import com.hyjf.mybatis.model.auto.BorrowFinserChargeExample;
import com.hyjf.mybatis.model.customize.FinserChargeCustomize;

@Service
public class FinserChargeServiceImpl extends BaseServiceImpl implements FinserChargeService {

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<FinserChargeCustomize> getRecordList(FinserChargeBean borrowFinserCharge, int limitStart, int limitEnd) {
		Map<String, Object> param = new HashMap<>();
		param.put("limitStart", limitStart);
		param.put("limitEnd", limitEnd);
		return finserChargeCustomizeMapper.selectAll(param);
	}

	@Override
	public int countRecordTotal(FinserChargeBean form) {
		int total = finserChargeCustomizeMapper.countRecordTotal();
		return total;
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	public BorrowFinserCharge getRecord(BorrowFinserCharge record) {
		BorrowFinserChargeExample example = new BorrowFinserChargeExample();
		BorrowFinserChargeExample.Criteria cra = example.createCriteria();
		cra.andChargeCdEqualTo(record.getChargeCd());
		List<BorrowFinserCharge> BorrowFinserChargeList = borrowFinserChargeMapper.selectByExample(example);
		if (BorrowFinserChargeList != null && BorrowFinserChargeList.size() > 0) {
			return BorrowFinserChargeList.get(0);
		}
		return new BorrowFinserCharge();
	}

	/**
	 * 根据主键判断维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(BorrowFinserCharge record) {
		if (StringUtils.isEmpty(record.getChargeCd())) {
			return false;
		}
		BorrowFinserChargeExample example = new BorrowFinserChargeExample();
		BorrowFinserChargeExample.Criteria cra = example.createCriteria();
		cra.andChargeCdEqualTo(record.getChargeCd());
		List<BorrowFinserCharge> BorrowFinserChargeList = borrowFinserChargeMapper.selectByExample(example);
		if (BorrowFinserChargeList != null && BorrowFinserChargeList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(FinserChargeBean record) {
		int nowTime = GetDate.getNowTime10();
		BorrowFinserCharge borrowFinserCharge = new BorrowFinserCharge();
		borrowFinserCharge.setChargeCd(UUID.randomUUID().toString());
		borrowFinserCharge.setChargeType(this.getParamName(CustomConstants.ENDDAY_MONTH, record.getChargeTimeType()));
		borrowFinserCharge.setChargeTimeType(record.getChargeTimeType());
		borrowFinserCharge.setChargeRate(record.getChargeRate());
		borrowFinserCharge.setProjectType(record.getProjectType());
		if (StringUtils.isNotEmpty(record.getChargeTime())) {
			borrowFinserCharge.setChargeTime(Integer.valueOf(record.getChargeTime()));
		}
		borrowFinserCharge.setStatus(Integer.valueOf(record.getStatus()));

		if (StringUtils.isEmpty(record.getRemark())) {
			borrowFinserCharge.setRemark(StringUtils.EMPTY);
		} else {
			borrowFinserCharge.setRemark(record.getRemark());
		}

		borrowFinserCharge.setCreateTime(nowTime);
		borrowFinserCharge.setUpdateTime(nowTime);
		borrowFinserChargeMapper.insertSelective(borrowFinserCharge);
	}

	/**
	 * 维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(FinserChargeBean record) {
		int nowTime = GetDate.getNowTime10();
		BorrowFinserCharge borrowFinserCharge = new BorrowFinserCharge();
		borrowFinserCharge.setChargeCd(record.getChargeCd());
		borrowFinserCharge.setChargeType(this.getParamName(CustomConstants.ENDDAY_MONTH, record.getChargeTimeType()));
		borrowFinserCharge.setChargeTimeType(record.getChargeTimeType());
		borrowFinserCharge.setChargeRate(record.getChargeRate());
		borrowFinserCharge.setProjectType(record.getProjectType());
		if (StringUtils.isNotEmpty(record.getChargeTime())) {
			borrowFinserCharge.setChargeTime(Integer.valueOf(record.getChargeTime()));
		}
		borrowFinserCharge.setStatus(Integer.valueOf(record.getStatus()));
		if (StringUtils.isEmpty(record.getRemark())) {
			borrowFinserCharge.setRemark(StringUtils.EMPTY);
		} else {
			borrowFinserCharge.setRemark(record.getRemark());
		}
		borrowFinserCharge.setUpdateTime(nowTime);

		BorrowFinserChargeExample example = new BorrowFinserChargeExample();
		BorrowFinserChargeExample.Criteria cra = example.createCriteria();
		cra.andChargeCdEqualTo(record.getChargeCd());

		borrowFinserChargeMapper.updateByExampleSelective(borrowFinserCharge, example);
	}

	/**
	 * 数据删除
	 * 
	 * @param record
	 */
	@Override
	public void deleteRecord(String chargeCd) {
		borrowFinserChargeMapper.deleteByPrimaryKey(chargeCd);
	}

	/**
	 * 天标是否是唯一
	 * 
	 */
	@Override
	public boolean enddayIsExists(String chargeCd,Integer projectType) {
		BorrowFinserChargeExample example = new BorrowFinserChargeExample();
		BorrowFinserChargeExample.Criteria cra = example.createCriteria();
		cra.andChargeTimeTypeEqualTo("endday");
        cra.andProjectTypeEqualTo(projectType);
		if (StringUtils.isNotEmpty(chargeCd)) {
			cra.andChargeCdNotEqualTo(chargeCd);
		}

		List<BorrowFinserCharge> list = borrowFinserChargeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 月数是否是唯一
	 * 
	 */
	@Override
	public boolean onlyOneMonth(String chargeCd, String chargeTime,Integer projectType) {
		BorrowFinserChargeExample example = new BorrowFinserChargeExample();
		BorrowFinserChargeExample.Criteria cra = example.createCriteria();
		cra.andChargeTimeTypeEqualTo("month");
		cra.andChargeTimeEqualTo(Integer.valueOf(chargeTime));
		cra.andProjectTypeEqualTo(projectType);
		if (StringUtils.isNotEmpty(chargeCd)) {
			cra.andChargeCdNotEqualTo(chargeCd);
		}

		List<BorrowFinserCharge> list = borrowFinserChargeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 相同期限天标唯一
	 * @param chargeCd
	 * @param chargeTime
	 * @param projectType
	 * @return
	 */
	@Override
	public boolean onlyOneDay(String chargeCd, String chargeTime, Integer projectType) {
		BorrowFinserChargeExample example = new BorrowFinserChargeExample();
		BorrowFinserChargeExample.Criteria cra = example.createCriteria();
		cra.andChargeTimeTypeEqualTo("endday");
		cra.andChargeTimeEqualTo(Integer.valueOf(chargeTime));
		cra.andProjectTypeEqualTo(projectType);
		if (StringUtils.isNotEmpty(chargeCd)) {
			cra.andChargeCdNotEqualTo(chargeCd);
		}

		List<BorrowFinserCharge> list = borrowFinserChargeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}
}
