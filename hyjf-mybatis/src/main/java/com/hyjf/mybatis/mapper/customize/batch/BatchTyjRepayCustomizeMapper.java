package com.hyjf.mybatis.mapper.customize.batch;

import java.util.List;
import java.util.Map;

public interface BatchTyjRepayCustomizeMapper {

	/**
	 * 查询出到期但是未满标的标的,给其发短信
	 * 
	 * @return
	 */
	List<String> selectNidForTyj(Map<String,Object> paramMap);

}