package com.hyjf.admin.manager.config.borrow.finhxfmancharge;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CreateUUID;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowFinhxfmanCharge;
import com.hyjf.mybatis.model.auto.BorrowFinhxfmanChargeExample;

@Service
public class FinhxfmanChargeServiceImpl extends BaseServiceImpl implements FinhxfmanChargeService {

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	public List<BorrowFinhxfmanCharge> getRecordList(FinhxfmanChargeBean vinmanChargeBean, int limitStart, int limitEnd) {
		BorrowFinhxfmanChargeExample example = new BorrowFinhxfmanChargeExample();

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}

		return borrowFinhxfmanChargeMapper.selectByExample(example);
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	public BorrowFinhxfmanCharge getRecord(String record) {
		BorrowFinhxfmanCharge feeConfig = borrowFinhxfmanChargeMapper.selectByPrimaryKey(record);
		return feeConfig;
	}

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(FinhxfmanChargeBean record) {
		int nowTime = GetDate.getNowTime10();
		BorrowFinhxfmanCharge borrowFinmanCharge = new BorrowFinhxfmanCharge();
		borrowFinmanCharge.setManChargeCd(CreateUUID.createUUID());
		borrowFinmanCharge.setManChargeType(this.getParamName("ENDDAY_MONTH", record.getChargeTimeType()));
		borrowFinmanCharge.setManChargePer(record.getManChargePer());
		borrowFinmanCharge.setManChargePerEnd(record.getManChargePerEnd());
		borrowFinmanCharge.setChargeTimeType(record.getChargeTimeType());
		if (StringUtils.isNotEmpty(record.getChargeTime())) {
			borrowFinmanCharge.setChargeTime(Integer.valueOf(record.getChargeTime()));
		}
		borrowFinmanCharge.setStatus(Integer.valueOf(record.getStatus()));
		if (StringUtils.isEmpty(record.getRemark())) {
			borrowFinmanCharge.setRemark(StringUtils.EMPTY);
		} else {
			borrowFinmanCharge.setRemark(record.getRemark());
		}

		borrowFinmanCharge.setCreateTime(nowTime);
		borrowFinmanCharge.setUpdateTime(nowTime);
		borrowFinhxfmanChargeMapper.insertSelective(borrowFinmanCharge);
	}

	/**
	 * 维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(FinhxfmanChargeBean record) {
		int nowTime = GetDate.getNowTime10();
		BorrowFinhxfmanCharge borrowFinmanCharge = new BorrowFinhxfmanCharge();
		borrowFinmanCharge.setManChargeCd(record.getManChargeCd());
		borrowFinmanCharge.setManChargeType(this.getParamName("ENDDAY_MONTH", record.getChargeTimeType()));
		borrowFinmanCharge.setManChargePer(record.getManChargePer());
		borrowFinmanCharge.setManChargePerEnd(record.getManChargePerEnd());
		borrowFinmanCharge.setChargeTimeType(record.getChargeTimeType());
		if (StringUtils.isNotEmpty(record.getChargeTime())) {
			borrowFinmanCharge.setChargeTime(Integer.valueOf(record.getChargeTime()));
		}
		borrowFinmanCharge.setStatus(Integer.valueOf(record.getStatus()));
		if (StringUtils.isEmpty(record.getRemark())) {
			borrowFinmanCharge.setRemark(StringUtils.EMPTY);
		} else {
			borrowFinmanCharge.setRemark(record.getRemark());
		}
		borrowFinmanCharge.setUpdateTime(nowTime);
		borrowFinhxfmanChargeMapper.updateByPrimaryKeySelective(borrowFinmanCharge);
	}

	/**
	 * 根据主键删除
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(String manchargeCd) {
		borrowFinhxfmanChargeMapper.deleteByPrimaryKey(manchargeCd);
	}

	/**
	 * 类型是否重复
	 * 
	 * @param isExists
	 */
	@Override
	public boolean isManChargeTypeExists(String chargeTimeType) {
		BorrowFinhxfmanChargeExample borrowFinmanChargeExample = new BorrowFinhxfmanChargeExample();
		BorrowFinhxfmanChargeExample.Criteria cra = borrowFinmanChargeExample.createCriteria();
		cra.andChargeTimeTypeEqualTo(chargeTimeType);

		List<BorrowFinhxfmanCharge> list = borrowFinhxfmanChargeMapper.selectByExample(borrowFinmanChargeExample);
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
	public boolean onlyOneMonth(String chargeCd, String chargeTime) {
		BorrowFinhxfmanChargeExample example = new BorrowFinhxfmanChargeExample();
		BorrowFinhxfmanChargeExample.Criteria cra = example.createCriteria();
		cra.andChargeTimeTypeEqualTo("month");
		cra.andChargeTimeEqualTo(Integer.valueOf(chargeTime));

		if (StringUtils.isNotEmpty(chargeCd)) {
			cra.andManChargeCdNotEqualTo(chargeCd);
		}

		List<BorrowFinhxfmanCharge> list = borrowFinhxfmanChargeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

}
