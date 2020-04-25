package com.hyjf.mybatis.mapper.customize.act;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.act.ActNovBargainCustomize;
import com.hyjf.mybatis.model.customize.act.ActNovPrizeCustomize;
import com.hyjf.mybatis.model.customize.act.ActNovPrizeDetailCustomize;


public interface ActNovBargainCustomizeMapper {

	List<ActNovPrizeCustomize> selectPrizeList(Map<String,Object> paraMap);
	
	List<ActNovPrizeDetailCustomize> selectPrizeDetail(Map<String,Object> paraMap);
	
	List<ActNovBargainCustomize> selectBargainList(Map<String,Object> paraMap);
	
	Integer countBargainList(Map<String,Object> paraMap);
	
	BigDecimal selectCurrentBargainMoney(Map<String,Object> paraMap);
}
