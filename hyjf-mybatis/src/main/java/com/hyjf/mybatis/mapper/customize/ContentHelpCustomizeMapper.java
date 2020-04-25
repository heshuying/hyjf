package com.hyjf.mybatis.mapper.customize;

import java.util.List;

import com.hyjf.mybatis.model.customize.ContentHelpCustomize;


public interface ContentHelpCustomizeMapper {

	/**
	 * 查询问题记录列表
	 * 
	 * @param alllBorrowCustomize
	 * @return
	 */
	public  List<ContentHelpCustomize> queryContent(ContentHelpCustomize msg);

	public  List<ContentHelpCustomize> queryContentCustomize(ContentHelpCustomize msg);

	public  Integer  queryContentCount(ContentHelpCustomize msg);
	
	public  ContentHelpCustomize queryContentById(int id);
	
	public  Integer  delByCateId(int cateId);
	
	public  Integer  updateByCateId(ContentHelpCustomize msg);
}