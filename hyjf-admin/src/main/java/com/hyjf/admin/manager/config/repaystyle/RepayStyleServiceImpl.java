package com.hyjf.admin.manager.config.repaystyle;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.BorrowStyleWithBLOBs;

@Service
public class RepayStyleServiceImpl extends BaseServiceImpl implements RepayStyleService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<BorrowStyleWithBLOBs> getRecordList(RepayStyleBean borrowFinserCharge, int limitStart, int limitEnd) {
		BorrowStyleExample example = new BorrowStyleExample();
//		BorrowStyleExample.Criteria cra = example.createCriteria();
//		cra.andStatusEqualTo(CustomConstants.FLAG_STATUS_ENABLE);
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return borrowStyleMapper.selectByExampleWithBLOBs(example);
	}
	
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param form
	 * @return
	 * @author Administrator
	 */
		
	@Override
	public int countRecordTotal(RepayStyleBean form) {
		BorrowStyleExample example = new BorrowStyleExample();
//		BorrowStyleExample.Criteria cra = example.createCriteria();
//		cra.andStatusEqualTo(CustomConstants.FLAG_STATUS_ENABLE);
		int total=borrowStyleMapper.countByExample(example);
		return total;
			
	}

	/**
	 * 获取单个权限维护
	 * 
	 * @return
	 */
	public BorrowStyle getRecord(BorrowStyle record) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<BorrowStyle> BorrowStyleList = borrowStyleMapper.selectByExample(example);
		if (BorrowStyleList != null && BorrowStyleList.size() > 0) {
			return BorrowStyleList.get(0);
		}
		return new BorrowStyle();
	}

	/**
	 * 根据ID获取单个还款方式
	 * 
	 * @return
	 */
	public BorrowStyleWithBLOBs getRecordById(Integer recordID) {
		BorrowStyleWithBLOBs result =borrowStyleMapper.selectByPrimaryKey(recordID);
		return  result;
	}

	/**
	 * 根据主键判断权限维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(BorrowStyle record) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<BorrowStyle> BorrowStyleList = borrowStyleMapper.selectByExample(example);
		if (BorrowStyleList != null && BorrowStyleList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据主键判断权限维护中权限是否存在
	 * 
	 * @return
	 */
	public boolean isExistsPermission(BorrowStyle record) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andNameEqualTo(record.getName());
		if (StringUtils.isNotEmpty(record.getNid())) {
			cra.andNidEqualTo(record.getNid());
		}
		List<BorrowStyle> BorrowStyleList = borrowStyleMapper.selectByExample(example);
		if (BorrowStyleList != null && BorrowStyleList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 权限维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(BorrowStyleWithBLOBs record) {
		//int nowTime = GetDate.getNowTime10();
//		record.setNid(CreateUUID.createUUID());
//		record.setStatus(CustomConstants.FLAG_STATUS_ENABLE);
		borrowStyleMapper.insertSelective(record);
	}

	/**
	 * 权限维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(BorrowStyleWithBLOBs record) {
		/*int nowTime = GetDate.getNowTime10();
		record.setUpdateTime(nowTime);*/
		borrowStyleMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 权限维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<String> recordList) {
		for (String permissionUuid : recordList) {
			BorrowStyleWithBLOBs record = new BorrowStyleWithBLOBs();
			record.setId(Integer.parseInt(permissionUuid));
			record.setStatus(CustomConstants.FLAG_STATUS_ENABLE);
			//record.setUpdateTime(GetDate.getNowTime10());
			borrowStyleMapper.updateByPrimaryKeySelective(record);
		}
	}

}
