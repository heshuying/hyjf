package com.hyjf.web.wisdomtooth;

import com.hyjf.mybatis.model.auto.ContentHelp;
import com.hyjf.mybatis.model.customize.ContentHelpCustomize;
import com.hyjf.web.BaseService;

import java.util.List;

public interface WisdomToothService extends BaseService {

	/**
	 * 查询问题列表
	 *
	 * @param  form
	 * @return
	 */
	public List<ContentHelpCustomize> queryContentCustomize(ContentHelpCustomize contentHelpCustomize);

	/**
	 * 根据ID查询问题
	 *
	 * @param id
	 * @return
	 */
	public ContentHelp queryContentById(int id);

}
