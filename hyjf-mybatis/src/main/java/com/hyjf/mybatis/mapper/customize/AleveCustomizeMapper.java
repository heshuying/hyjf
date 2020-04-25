package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.customize.AleveLogCustomize;

import java.util.List;

/**
 * Created by cuigq on 2018/1/22.
 */
public interface AleveCustomizeMapper {

    Integer queryAleveLogCount(AleveLogCustomize aleveLogCustomize);

    List<AleveLogCustomize> queryAleveLogList(AleveLogCustomize aleveLogCustomize);

    List<AleveLogCustomize> queryAleveLogListByTranstype(List<String> tranStype);
}
