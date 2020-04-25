package com.hyjf.mybatis.mapper.customize.whereaboutspage;

import java.util.List;

import com.hyjf.mybatis.model.customize.whereaboutspage.WhereaboutsPageConfigCustomize;

public interface WhereaboutsPageCustomizeMapper {

    Integer countWhereaboutsPage(WhereaboutsPageConfigCustomize form);

    List<WhereaboutsPageConfigCustomize> selectWhereaboutsPageList(WhereaboutsPageConfigCustomize form);

	

}