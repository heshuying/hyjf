package com.hyjf.admin.manager.config.borrow.projecttype;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.BorrowProjectRepay;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.customize.ProjectTypeCustomize;

public interface ProjectTypeService extends BaseService {

	/**
	 * 获取汇直投项目类型列表
	 * 
	 * @return
	 */
	public List<ProjectTypeCustomize> getProjectTypeList(ProjectTypeCustomize projectTypeCustomize);

	/**
	 * 获取单个汇直投项目类型维护
	 * 
	 * @return
	 */
	public BorrowProjectType getRecord(BorrowProjectType record);

	/**
	 * 根据主键判断汇直投项目类型维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(BorrowProjectType record);

	/**
	 * 汇直投项目类型维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(ProjectTypeBean record);

	/**
	 * 汇直投项目类型维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(ProjectTypeBean record);

	/**
	 * 汇直投项目类型维护删除
	 * 
	 * @param record
	 */
	public void deleteRecord(String borrowCd);

	/**
	 * 项目编号是否存在
	 * 
	 * @param borrowCd
	 * @param borrowName
	 * @return
	 */
	public int borrowCdIsExists(String borrowCd);

	/**
	 * 根据项目编号查询还款方式
	 * 
	 * @param str
	 * 
	 * @param record
	 */
	public List<BorrowProjectRepay> selectRepay(String str);

	/**
	 * 查询类型表
	 * 
	 * @param record
	 */
	public List<BorrowStyle> selectStyles();

	/**
	 * 根据条件删除
	 * 
	 * @param borrowClass
	 */
	public void delectRepay(String borrowClass);

	/**
	 * 插入数据
	 * 
	 * @param form
	 */
	public void insertRepay(ProjectTypeBean form);
	
	/**
	 * 插入数据
	 * 
	 * @param form
	 */
	public void insertAsset(ProjectTypeBean form);
	
	
	/**
	 * 删除数据
	 * 
	 * @param form
	 */
	public void deleteAsset(Integer assetType);
	
	/**
	 * 更新数据
	 * 
	 * @param form
	 */
	public void updateAsset(ProjectTypeBean form);
}
