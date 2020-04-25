package com.hyjf.admin.htl.product;

import java.util.List;

import com.hyjf.mybatis.model.auto.Product;

public interface HtlProductService {

	/**
	 * 获取产品列表
	 * 
	 * @return
	 */
	public List<Product> getRecordList(Product product, int limitStart, int limitEnd);

	/**
	 * 获取单个产品
	 * 
	 * @return
	 */
	public Product getRecord(Product record);

	/**
	 * 根据主键判断数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(Product record);

	/**
	 * 产品插入
	 * 
	 * @param record
	 */
	public void insertRecord(Product record);

	/**
	 * 产品维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(Product record);

	/**
	 * 产品维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<String> recordList);
	
}
