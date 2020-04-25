package com.hyjf.admin.manager.config.borrow.finmancharge;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CreateUUID;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowFinmanCharge;
import com.hyjf.mybatis.model.auto.BorrowFinmanChargeExample;

@Service
public class FinmanChargeServiceImpl extends BaseServiceImpl implements FinmanChargeService {

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	public List<BorrowFinmanCharge> getRecordList(FinmanChargeBean vinmanChargeBean, int limitStart, int limitEnd) {
		BorrowFinmanChargeExample example = new BorrowFinmanChargeExample();

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}

		return borrowFinmanChargeMapper.selectByExample(example);
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	public BorrowFinmanCharge getRecord(String record) {
		BorrowFinmanCharge feeConfig = borrowFinmanChargeMapper.selectByPrimaryKey(record);
		return feeConfig;
	}

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(FinmanChargeBean record) {
		int nowTime = GetDate.getNowTime10();
		BorrowFinmanCharge borrowFinmanCharge = new BorrowFinmanCharge();
		borrowFinmanCharge.setManChargeCd(CreateUUID.createUUID());
		borrowFinmanCharge.setManChargeType(this.getParamName("ENDDAY_MONTH", record.getChargeTimeType()));
		borrowFinmanCharge.setManChargePer(record.getManChargePer());
		borrowFinmanCharge.setChargeTimeType(record.getChargeTimeType());
		borrowFinmanCharge.setStatus(Integer.valueOf(record.getStatus()));
		borrowFinmanCharge.setRemark(record.getRemark());

		if (StringUtils.isEmpty(record.getRemark())) {
			borrowFinmanCharge.setRemark(StringUtils.EMPTY);
		} else {
			borrowFinmanCharge.setRemark(record.getRemark());
		}

		borrowFinmanCharge.setCreateTime(nowTime);
		borrowFinmanCharge.setUpdateTime(nowTime);
		borrowFinmanChargeMapper.insertSelective(borrowFinmanCharge);
	}

	/**
	 * 维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(FinmanChargeBean record) {
		int nowTime = GetDate.getNowTime10();
		BorrowFinmanCharge borrowFinmanCharge = new BorrowFinmanCharge();
		borrowFinmanCharge.setManChargeCd(record.getManChargeCd());
		borrowFinmanCharge.setManChargeType(this.getParamName("ENDDAY_MONTH", record.getChargeTimeType()));
		borrowFinmanCharge.setManChargePer(record.getManChargePer());
		borrowFinmanCharge.setChargeTimeType(record.getChargeTimeType());
		borrowFinmanCharge.setStatus(Integer.valueOf(record.getStatus()));
		if (StringUtils.isEmpty(record.getRemark())) {
			borrowFinmanCharge.setRemark(StringUtils.EMPTY);
		} else {
			borrowFinmanCharge.setRemark(record.getRemark());
		}
		borrowFinmanCharge.setUpdateTime(nowTime);
		borrowFinmanChargeMapper.updateByPrimaryKeySelective(borrowFinmanCharge);
	}

	/**
	 * 根据主键删除
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(String manchargeCd) {
		borrowFinmanChargeMapper.deleteByPrimaryKey(manchargeCd);
	}

	/**
	 * 类型是否重复
	 * 
	 * @param isExists
	 */
	@Override
	public boolean isManChargeTypeExists(String chargeTimeType) {
		BorrowFinmanChargeExample borrowFinmanChargeExample = new BorrowFinmanChargeExample();
		BorrowFinmanChargeExample.Criteria cra = borrowFinmanChargeExample.createCriteria();
		cra.andChargeTimeTypeEqualTo(chargeTimeType);

		List<BorrowFinmanCharge> list = borrowFinmanChargeMapper.selectByExample(borrowFinmanChargeExample);
		if (list != null && list.size() > 0) {
			return true;
		}

		return false;
	}

}
