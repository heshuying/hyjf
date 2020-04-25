package com.hyjf.admin.manager.config.htlconfig;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductExample;

@Service
public class HtlConfigServiceImpl extends BaseServiceImpl implements HtlConfigService {

	/**
	 * 获取取汇天利列表
	 * 
	 * @return
	 */
	public List<Product> getRecordList(Product product, int limitStart, int limitEnd) {
		ProductExample example = new ProductExample();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return productMapper.selectByExample(example);
	}

	/**
	 * 获取单个取汇天利维护
	 * 
	 * @return
	 */
	public Product getRecord(Integer record) {
		Product htlConfig = productMapper.selectByPrimaryKey(record);
		return htlConfig;
	}

	/**
	 * 根据主键判断取汇天利中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(Product record) {
		ProductExample example = new ProductExample();
		ProductExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<Product> HtlConfigList = productMapper.selectByExample(example);
		if (HtlConfigList != null && HtlConfigList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据主键判断取汇天利中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsPermission(Product record) {
		ProductExample example = new ProductExample();
		ProductExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		if (record.getId() != null) {
			cra.andIdEqualTo(record.getId());
		}
		List<Product> HtlConfigList = productMapper.selectByExample(example);
		if (HtlConfigList != null && HtlConfigList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 取汇天利更新
	 * 
	 * @param record
	 */
	public void updateRecord(HtlConfigBean record) {
		Product product = productMapper.selectByExample(new ProductExample()).get(0);
		switch (record.getName()) {
		case "年化收益率":
			product.setInterestRate(new BigDecimal(record.getValue()));
			break;
		case "产品总额":
			product.setAllpupper(new BigDecimal(record.getValue()));
			break;
		case "单户上限":
			product.setPupper(new BigDecimal(record.getValue()));
			break;
		case "单户下限":
			product.setPlower(new BigDecimal(record.getValue()));
			break;
		case "可赎回":
			product.setIsRedeem(Integer.parseInt(record.getValue()));
			break;
		case "可转让":
			product.setIsTender(Integer.parseInt(record.getValue()));
			break;
		}
		if (record != null && StringUtils.isNotEmpty(record.getName()) && StringUtils.isNotEmpty(record.getValue())) {
			productMapper.updateByPrimaryKeySelective(product);
		}
	}

	/**
	 * 取汇天利维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<Integer> recordList) {
		for (Integer id : recordList) {
			productMapper.deleteByPrimaryKey(id);
		}
	}

}
