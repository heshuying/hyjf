package com.hyjf.mybatis.mapper.customize.app;

import java.util.List;

import com.hyjf.mybatis.model.customize.app.AppAccountTradeListCustomize;

public interface AppAccountTradeCustomizeMapper {

	List<AppAccountTradeListCustomize> selectTradeTypeList();
}