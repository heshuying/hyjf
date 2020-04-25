package com.hyjf.mybatis.mapper.customize.act;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.ActdecTenderBalloon;


public interface ActCustomizeMapper {

	List<ActdecTenderBalloon> selectBalloonList(Map<String,Object> paraMap);
	
	int countBalloonList(Map<String,Object> paraMap);
}
