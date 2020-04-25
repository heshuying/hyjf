package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.UtmPlat;

/**
 * @author xiasq
 * @version UtmPlatCustomizeMapper, v0.1 2017/11/20 17:45
 */
public interface UtmPlatCustomizeMapper {
    UtmPlat selectUtmPlatByUserId(Integer userId);
}
