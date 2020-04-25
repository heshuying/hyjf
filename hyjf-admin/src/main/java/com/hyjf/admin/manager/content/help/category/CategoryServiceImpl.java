package com.hyjf.admin.manager.content.help.category;

import java.util.ArrayList;
import java.util.List;

import com.hyjf.mybatis.model.customize.HelpCategoryCustomize;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.Category;
import com.hyjf.mybatis.model.auto.CategoryExample;
import com.hyjf.mybatis.model.auto.ContentHelp;
import com.hyjf.mybatis.model.auto.ContentHelpExample;
import com.hyjf.mybatis.model.customize.ContentHelpCustomize;

@Service
public class CategoryServiceImpl extends BaseServiceImpl implements CategoryService {

	/**
	 * 获取问题分类
	 * 
	 * @param form
	 * @return
	 */
	@Override
	public Integer getCategoryCount(CategoryExample form) {
		return categoryMapper.countByExample(form);
	}

	@Override
	public List<Category> getCategory(CategoryExample form) {
		return categoryMapper.selectByExample(form);
	}

	@Override
	public Category queryById(int id) {
		return categoryMapper.selectByPrimaryKey(id);
	}

	@Override
	public Integer addCate(Category cate) {
		return categoryMapper.insertSelective(cate);
	}

	@Override
	public Integer updateCate(Category cate) {
		return categoryMapper.updateByPrimaryKeySelective(cate);
	}

	@Override
	public Integer delCate(int id) {
		return categoryMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<ContentHelp> getCon(ContentHelpExample example) {
		return contentHelpMapper.selectByExample(example);
	}
	@Override
	public List<Integer> getConType() {
		List<Integer> ids = new ArrayList<>();
		List <HelpCategoryCustomize> list = helpCustomizeMapper.selectCategory("help");
		for(HelpCategoryCustomize customize:list){
			ids.add(customize.getId());
		}
		return ids;
	}
	@Override
	public Integer getConNum(ContentHelpExample example) {
		return contentHelpMapper.countByExample(example);
	}

	@Override
	public ContentHelp queryContentById(int id) {
		return contentHelpMapper.selectByPrimaryKey(id);
	}

	@Override
	public Integer addCon(ContentHelp form) {
		return contentHelpMapper.insertSelective(form);
	}

	@Override
	public Integer updateCon(ContentHelp form) {
		return contentHelpMapper.updateByPrimaryKeySelective(form);
	}

	@Override
	public Integer delCon(int id) {
		return contentHelpMapper.deleteByPrimaryKey(id);
	}

	@Override
	public Integer delByCateId(int cateId) {
		return contenthelpCustomizeMapper.delByCateId(cateId);
	}

	@Override
	public Integer updateByCateId(ContentHelpCustomize msg) {
		return contenthelpCustomizeMapper.updateByCateId(msg);
	}
}
