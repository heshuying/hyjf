/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize.admin;

import com.hyjf.mybatis.model.customize.admin.AssetExceptionCustomize;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version AssetExceptionCustomizeMapper, v0.1 2018/8/2 11:23
 */
public interface AssetExceptionCustomizeMapper {
    /**
     * 查询总件数
     *
     * @param assetExceptionCustomize
     * @return
     */
    Integer countBorrowDelete(AssetExceptionCustomize assetExceptionCustomize);

    /**
     * 查询列表
     *
     * @param assetExceptionCustomize
     * @return
     */
    List<AssetExceptionCustomize> selectBorrowDeleteList(AssetExceptionCustomize assetExceptionCustomize);
}
