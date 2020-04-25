/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.mapper.customize.nifa;

import com.hyjf.mybatis.model.customize.nifa.NifaContractTemplateCustomize;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version NifaContractTemplateCustomizeMapper, v0.1 2018/7/6 11:32
 */
public interface NifaContractTemplateCustomizeMapper {
    /**
     * 获取前一天修改的合同模版约定条款
     *
     * @return
     */
    List<NifaContractTemplateCustomize> selectNifaContractTemplate();
}
