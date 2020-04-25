package com.hyjf.batch.htl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.ProductInfo;
import com.hyjf.mybatis.model.auto.ProductInfoExample;

@Service
public class ProductInfoServiceImpl extends BaseServiceImpl implements ProductInfoService {

	/**
	 * 获取每日报表列表
	 * 
	 * @return
	 */
	public List<ProductInfo> getRecordList(ProductInfoBean ProductInfo, int limitStart, int limitEnd) {
		ProductInfoExample example = new ProductInfoExample();
		ProductInfoExample.Criteria cra = example.createCriteria();
		if(StringUtils.isNotEmpty(ProductInfo.getTimeStartSrch())){
			cra.andDataDateGreaterThanOrEqualTo(ProductInfo.getTimeStartSrch());
		}
		if(StringUtils.isNotEmpty(ProductInfo.getTimeEndSrch())){
			cra.andDataDateLessThanOrEqualTo(ProductInfo.getTimeEndSrch());
		}
		example.setOrderByClause("data_date desc");
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return productInfoMapper.selectByExample(example);
	}

	/**
	 * 获取单个权限维护
	 * 
	 * @return
	 */
	public ProductInfo getRecord(ProductInfo record) {
		ProductInfoExample example = new ProductInfoExample();
		ProductInfoExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<ProductInfo> ProductInfoList = productInfoMapper.selectByExample(example);
		if (ProductInfoList != null && ProductInfoList.size() > 0) {
			return ProductInfoList.get(0);
		}
		return new ProductInfo();
	}

	/**
	 * 根据主键判断数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(ProductInfo record) {
		if (record.getId() == null || record.getId() == 0) {
			return false;
		}
		ProductInfoExample example = new ProductInfoExample();
		ProductInfoExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<ProductInfo> ProductInfoList = productInfoMapper.selectByExample(example);
		if (ProductInfoList != null && ProductInfoList.size() > 0) {
			return true;
		}
		return false;
	}
	/**
	 * 根据时间判断数据是否存在
	 * 
	 * @return
	 */
	public int isExistsRecordByDataDate(ProductInfo record) {
		int pid = 0;
		if (StringUtils.isEmpty(record.getDataDate())) {
			return pid;
		}
		ProductInfoExample example = new ProductInfoExample();
		ProductInfoExample.Criteria cra = example.createCriteria();
		cra.andDataDateEqualTo(record.getDataDate());
		List<ProductInfo> ProductInfoList = productInfoMapper.selectByExample(example);
		if (ProductInfoList != null && ProductInfoList.size() > 0) {
			pid = ProductInfoList.get(0).getId();
		}
		return pid;
	}
	/**
	 * 产品插入
	 * 
	 * @param record
	 */
	public void insertRecord(ProductInfo record) {
		//record.setId(10001);
		//record.setCreateTime(GetDate.gettimestamp());
		productInfoMapper.insertSelective(record);
	}

	/**
	 * 产品更新
	 * 
	 * @param record
	 */
	public void updateRecord(ProductInfo record) {
		productInfoMapper.updateByPrimaryKeySelective(record);
	}

}
