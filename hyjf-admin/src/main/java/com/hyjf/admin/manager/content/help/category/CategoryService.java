package com.hyjf.admin.manager.content.help.category;

import java.util.List;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.Category;
import com.hyjf.mybatis.model.auto.CategoryExample;
import com.hyjf.mybatis.model.auto.ContentHelp;
import com.hyjf.mybatis.model.auto.ContentHelpExample;
import com.hyjf.mybatis.model.customize.ContentHelpCustomize;

public interface CategoryService extends BaseService {

	/**
	 * 获取问题分类数量
	 * 
	 * @param form
	 * @return
	 */
	public Integer getCategoryCount(CategoryExample form);

	/**
	 * 获取列表
	 */

	public List<Category> getCategory(CategoryExample form);

	/**
	 * 根据ID查询
	 * 
	 * @param id
	 * @return
	 */
	public Category queryById(int id);

	/**
	 * 添加分类
	 * 
	 * @param smsTem
	 * @return
	 */
	public Integer addCate(Category cate);

	/**
	 * 修改分类
	 */
	public Integer updateCate(Category cate);

	/**
	 * 删除分类
	 */
	public Integer delCate(int id);

	// 以下是问题列表所属

	/**
	 * 查询问题
	 */
	public List<ContentHelp> getCon(ContentHelpExample form);
	/**
	 * 查询问题類型
	 */
	public List<Integer> getConType();

	/**
	 * 查询问题条数
	 */
	public Integer getConNum(ContentHelpExample form);

	/**
	 * 根据ID查询问题
	 * 
	 * @param id
	 * @return
	 */
	public ContentHelp queryContentById(int id);

	/**
	 * 添加问题
	 */
	public Integer addCon(ContentHelp form);

	/**
	 * 修改问题
	 */
	public Integer updateCon(ContentHelp form);

	/**
	 * 删除问题
	 */
	public Integer delCon(int id);

	/**
	 * 根据分类ID删除下属问题
	 * 
	 * @param cateId
	 * @return
	 */
	public Integer delByCateId(int cateId);

	/**
	 * 根据分类ID修改分类ID
	 * 
	 * @param msg
	 * @return
	 */
	public Integer updateByCateId(ContentHelpCustomize msg);
}
