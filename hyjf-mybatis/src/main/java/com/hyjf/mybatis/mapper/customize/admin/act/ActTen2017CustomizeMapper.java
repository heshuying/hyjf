package com.hyjf.mybatis.mapper.customize.admin.act;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.act.ActTen2017Customize;

public interface ActTen2017CustomizeMapper {

	List<ActTen2017Customize> selectTenderReturnList(Map<String, Object> paraMap);
	
	Integer selectTenderReturnListCount(Map<String, Object> paraMap);

	List<ActTen2017Customize> selectTenderReturnDetail(Map<String, Object> paraMap);
	
	Integer selectTenderReturnDetailCount(Map<String, Object> paraMap);
}