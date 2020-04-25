package com.hyjf.batch.htl;

import java.util.List;

import com.hyjf.mybatis.model.auto.ProductInfo;

public interface ProductInfoService {

	/**
	 * 获取产品列表
	 * 
	 * @return
	 */
	public List<ProductInfo> getRecordList(ProductInfoBean product, int limitStart, int limitEnd);

	/**
	 * 获取单个产品
	 * 
	 * @return
	 */
	public ProductInfo getRecord(ProductInfo record);

	/**
	 * 根据主键判断数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(ProductInfo record);
	/**
	 * 根据时间判断数据是否存在（每天一条数据）
	 * 
	 * @return
	 */
	public int isExistsRecordByDataDate(ProductInfo record);
	
	/**
	 * 产品插入
	 * 
	 * @param record
	 */
	public void insertRecord(ProductInfo record);

	/**
	 * 产品维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(ProductInfo record);
}
