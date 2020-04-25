package com.hyjf.admin.manager.config.bankretcodeconfig;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankReturnCodeConfig;
import com.hyjf.mybatis.model.auto.BankReturnCodeConfigExample;

@Service
public class BankRetcodeConfigServiceImpl extends BaseServiceImpl implements BankRetcodeConfigService {

	/**
	 * 获取列表列表
	 * 
	 * @return
	 */
	@Override
	public List<BankReturnCodeConfig> getRecordList(BankReturnCodeConfig bankreturncodeconfig, int limitStart, int limitEnd) {
		BankReturnCodeConfigExample example = new BankReturnCodeConfigExample();

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		BankReturnCodeConfigExample.Criteria criteria = example.createCriteria();
		// 条件查询
		if (Validator.isNotNull(bankreturncodeconfig.getTxCode())) {
			criteria.andTxCodeLike("%" + bankreturncodeconfig.getTxCode() + "%");
		}
		if (Validator.isNotNull(bankreturncodeconfig.getRetCode())) {
			criteria.andRetCodeLike("%" + bankreturncodeconfig.getRetCode() + "%");
		}
		if (Validator.isNotNull(bankreturncodeconfig.getErrorMsg())) {
			criteria.andErrorMsgLike("%" + bankreturncodeconfig.getErrorMsg() + "%");
		}
		return bankReturnCodeConfigMapper.selectByExample(example);
	}

	/**
	 * 获取单个维护
	 * 
	 * @return
	 */
	@Override
	public BankReturnCodeConfig getRecord(BankReturnCodeConfig record) {
		BankReturnCodeConfigExample example = new BankReturnCodeConfigExample();
		BankReturnCodeConfigExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<BankReturnCodeConfig> list = bankReturnCodeConfigMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new BankReturnCodeConfig();
	}

	/**
	 * 根据主键判断维护中数据是否存在
	 * 
	 * @return
	 */
	@Override
	public boolean isExistsRecord(BankReturnCodeConfig record) {
		if (Validator.isNotNull(record.getId())) {
			return false;
		}
		BankReturnCodeConfigExample example = new BankReturnCodeConfigExample();
		BankReturnCodeConfigExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<BankReturnCodeConfig> bankReturnCodeConfigList = bankReturnCodeConfigMapper.selectByExample(example);
		if (bankReturnCodeConfigList != null && bankReturnCodeConfigList.size() > 0) {
			return true;
		}
		return false;

	}

	/**
	 * 根据主键判断维护中数据是否存在
	 * 
	 * @return
	 */
	@Override
	public boolean isExistsReturnCode(BankReturnCodeConfig record) {
		BankReturnCodeConfigExample example = new BankReturnCodeConfigExample();
		BankReturnCodeConfigExample.Criteria cra = example.createCriteria();
		if (Validator.isNotNull(record.getTxCode()) && Validator.isNotNull(record.getRetCode())) {
			cra.andTxCodeLike("%" + record.getTxCode() + "%");
			cra.andRetCodeLike("%" + record.getRetCode() + "%");
		}
		List<BankReturnCodeConfig> bankReturnCodeConfigList = bankReturnCodeConfigMapper.selectByExample(example);
		if (bankReturnCodeConfigList != null && bankReturnCodeConfigList.size() > 0) {
			return true;
		}
		return false;

	}

	/**
	 * 返回码插入
	 * 
	 * @param record
	 */
	@Override
	public void insertRecord(BankReturnCodeConfig record) {
		record.setCreateTime(GetDate.getDate());
		record.setUpdateTime(GetDate.getDate());
		bankReturnCodeConfigMapper.insertSelective(record);

	}

	/**
	 * 返回码更新
	 * 
	 * @param record
	 */
	@Override
	public void updateRecord(BankReturnCodeConfig record) {
		record.setUpdateTime(GetDate.getDate());
		bankReturnCodeConfigMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 返回码删除
	 * 
	 * @param record
	 */
	@Override
	public void deleteRecord(List<Integer> recordList) {
		for (Integer id : recordList) {
			bankReturnCodeConfigMapper.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 根据主键判断维护中数据是否存在
	 * 
	 * @param form
	 * @return
	 */
	@Override
	public int countRecord(BankRetcodeConfigBean form) {

		BankReturnCodeConfigExample example = new BankReturnCodeConfigExample();
		BankReturnCodeConfigExample.Criteria criteria = example.createCriteria();
		// 条件查询
		if (Validator.isNotNull(form.getTxCode())) {
			criteria.andTxCodeEqualTo(form.getTxCode());
		}
		if (Validator.isNotNull(form.getRetCode())) {
			criteria.andRetCodeEqualTo(form.getRetCode());
		}
		return bankReturnCodeConfigMapper.countByExample(example);
	}

}
