package com.hyjf.admin.htl.product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Product;
import com.hyjf.mybatis.model.auto.ProductExample;

@Service
public class HtlProductServiceImpl extends BaseServiceImpl implements HtlProductService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<Product> getRecordList(Product Product, int limitStart, int limitEnd) {
		ProductExample example = new ProductExample();
//		ProductExample.Criteria cra = example.createCriteria();
		
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return productMapper.selectByExample(example);
	}

	/**
	 * 获取汇天利产品信息
	 * 
	 * @return
	 */
	public Product getRecord(Product record) {
		ProductExample example = new ProductExample();
		ProductExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<Product> ProductList = productMapper.selectByExample(example);
		if (ProductList != null && ProductList.size() > 0) {
			return ProductList.get(0);
		}
		return new Product();
	}

	/**
	 * 根据主键判断数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(Product record) {
		if (record.getId() == null || record.getId() == 0) {
			return false;
		}
		ProductExample example = new ProductExample();
		ProductExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<Product> ProductList = productMapper.selectByExample(example);
		if (ProductList != null && ProductList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 产品插入
	 * 
	 * @param record
	 */
	public void insertRecord(Product record) {
		record.setId(10001);
		record.setCreateTime(GetDate.gettimestamp());
		productMapper.insertSelective(record);
	}

	/**
	 * 产品更新
	 * 
	 * @param record
	 */
	public void updateRecord(Product record) {
		//int nowTime = GetDate.getNowTime10();
		//record.setUpdatetime(nowTime);
		productMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 产品删除
	 * 
	 * @param record
	 */
	public void deleteRecord(List<String> recordList) {
		for (String pid : recordList) {
//			Product record = new Product();
//			record.setPermissionUuid(permissionUuid);
//			record.setDelFlag(CustomConstants.FLAG_DELETE);
//			record.setUpdatetime(GetDate.getNowTime10());
//			productMapper.updateByPrimaryKeySelective(record);
			
			productMapper.deleteByPrimaryKey(Integer.parseInt(pid));
		}
	}

}
