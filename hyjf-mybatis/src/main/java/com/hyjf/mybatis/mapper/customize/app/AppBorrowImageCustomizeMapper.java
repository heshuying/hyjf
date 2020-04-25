package com.hyjf.mybatis.mapper.customize.app;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.app.AppBorrowImageCustomize;

public interface AppBorrowImageCustomizeMapper {


    List<AppBorrowImageCustomize> selectBorrowImageList(Map<String, Object> projectType);

}