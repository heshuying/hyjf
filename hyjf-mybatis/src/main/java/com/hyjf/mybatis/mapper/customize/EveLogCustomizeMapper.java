package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.EveLog;
import com.hyjf.mybatis.model.customize.EveLogCustomize;

import java.util.List;

/**
 * Created by cui on 2018/1/19.
 */
public interface EveLogCustomizeMapper {

    Integer queryEveLogCount(EveLogCustomize eveLogCustomize);

    List<EveLogCustomize> queryEveLogList(EveLogCustomize eveLogCustomize);

}
