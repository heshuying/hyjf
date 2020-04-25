package com.hyjf.web.wisdomtooth;

import com.hyjf.mybatis.model.auto.ContentHelp;
import com.hyjf.mybatis.model.auto.ContentHelpExample;
import com.hyjf.mybatis.model.customize.ContentHelpCustomize;
import com.hyjf.web.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WisdomToothServiceImpl extends BaseServiceImpl implements WisdomToothService {

	@Override
	public List<ContentHelpCustomize> queryContentCustomize(ContentHelpCustomize contentHelpCustomize) {
		return contenthelpCustomizeMapper.queryContentCustomize(contentHelpCustomize);
	}
	@Override
	public ContentHelp queryContentById(int id) {
		return contentHelpMapper.selectByPrimaryKey(id);
	}
}
