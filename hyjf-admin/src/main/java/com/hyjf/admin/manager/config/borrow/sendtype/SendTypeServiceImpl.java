package com.hyjf.admin.manager.config.borrow.sendtype;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowSendType;
import com.hyjf.mybatis.model.auto.BorrowSendTypeExample;

@Service
public class SendTypeServiceImpl extends BaseServiceImpl implements SendTypeService {

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	public List<BorrowSendType> getRecordList(SendTypeBean sendTypeBean, int limitStart, int limitEnd) {
		BorrowSendTypeExample example = new BorrowSendTypeExample();

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}

		return borrowSendTypeMapper.selectByExample(example);
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	public BorrowSendType getRecord(String record) {
		BorrowSendType borrowSendType = borrowSendTypeMapper.selectByPrimaryKey(record);
		return borrowSendType;
	}

	/**
	 * 维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(SendTypeBean sendTypeBean) {
		int nowTime = GetDate.getNowTime10();
		BorrowSendType record = new BorrowSendType();
		record.setSendCd(sendTypeBean.getSendCd());
		record.setSendName(sendTypeBean.getSendName());
		record.setAfterTime(Integer.valueOf(sendTypeBean.getAfterTime()));
		if (StringUtils.isEmpty(record.getRemark())) {
			record.setRemark(StringUtils.EMPTY);
		} else {
			record.setRemark(sendTypeBean.getRemark());
		}
		record.setCreateTime(nowTime);
		record.setUpdateTime(nowTime);
		borrowSendTypeMapper.insertSelective(record);
	}

	/**
	 * 维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(SendTypeBean sendTypeBean) {
		int nowTime = GetDate.getNowTime10();
		BorrowSendType record = new BorrowSendType();
		record.setSendCd(sendTypeBean.getSendCd());
		record.setSendName(sendTypeBean.getSendName());
		record.setAfterTime(Integer.valueOf(sendTypeBean.getAfterTime()));
		if (StringUtils.isEmpty(sendTypeBean.getRemark())) {
			record.setRemark(StringUtils.EMPTY);
		} else {
			record.setRemark(sendTypeBean.getRemark());
		}
		record.setUpdateTime(nowTime);
		borrowSendTypeMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 根据主键删除
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(String sendCd) {
		borrowSendTypeMapper.deleteByPrimaryKey(sendCd);
	}

}
