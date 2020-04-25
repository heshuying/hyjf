package com.hyjf.admin.manager.content.article;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ContentArticle;
import com.hyjf.mybatis.model.auto.ContentArticleExample;
import com.hyjf.mybatis.model.customize.ContentArticleCustomize;

@Service
public class ContentArticleServiceImpl extends BaseServiceImpl implements ContentArticleService {

	/**
	 * 获取文章列表列表
	 * 
	 * @return
	 */
	public List<ContentArticle> getRecordList(ContentArticle ContentArticle, int limitStart, int limitEnd) {
		ContentArticleExample example = new ContentArticleExample();

		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		return contentArticleMapper.selectByExample(example);
	}

	/**
	 * 获取单个文章维护
	 * 
	 * @return
	 */
	public ContentArticle getRecord(Integer record) {
		ContentArticle one = contentArticleMapper.selectByPrimaryKey(record);
		return one;
	}

	/**
	 * 根据主键判断文章维护中数据是否存在
	 * 
	 * @return
	 */
	public boolean isExistsRecord(ContentArticle record) {
		if (record.getId() == null) {
			return false;
		}
		ContentArticleExample example = new ContentArticleExample();
		ContentArticleExample.Criteria cra = example.createCriteria();
		cra.andIdEqualTo(record.getId());
		List<ContentArticle> ContentArticleList = contentArticleMapper.selectByExample(example);
		if (ContentArticleList != null && ContentArticleList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 文章维护插入
	 * 
	 * @param record
	 */
	public void insertRecord(ContentArticle record) {
		record.setCreateTime(GetDate.getDate());
		record.setUpdateTime(GetDate.getDate());
		record.setClick(0);
		contentArticleMapper.insertSelective(record);
	}

	/**
	 * 文章维护更新
	 * 
	 * @param record
	 */
	public void updateRecord(ContentArticle record) {
		record.setUpdateTime(GetDate.getDate());
		contentArticleMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 根据主键删除文章
	 * 
	 * @param recordList
	 */
	@Override
	public void deleteRecord(List<Integer> recordList) {
		for (Integer id : recordList) {
			contentArticleMapper.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 根据条件查询数据
	 */
	public List<ContentArticle> selectRecordList(ContentArticleBean form, int limitStart, int limitEnd) {
		ContentArticleCustomize example = new ContentArticleCustomize();
		if (limitStart != -1) {
			example.setLimitStart(limitStart);
			example.setLimitEnd(limitEnd);
		}
		if (form.getStatus() == null) {
			example.setStatus(3);
		} else if (form.getStatus() != null) {
			example.setStatus(form.getStatus());
		}
		if (StringUtils.isNotEmpty(form.getStartCreate())) {
			example.setStartCreate(GetDate.str2Timestamp(form.getStartCreate()));
		}
		if (StringUtils.isNotEmpty(form.getEndCreate())) {
			example.setEndCreate(GetDate.str2Timestamp(form.getEndCreate()));
		}
		// 获取活动名
		example.setTitle(form.getTitle());
		example.setType(form.getType());
		return contentArticleCustomizeMapper.selectContentArticle(example);
	}

}
