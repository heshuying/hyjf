package com.hyjf.mybatis.mapper.customize.apiweb;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.apiweb.IanternFestivalCustomize;
import com.hyjf.mybatis.model.customize.apiweb.UserLanternIllumineCustomize;


public interface IanternFestivalCustomizeMapper {

    List<IanternFestivalCustomize> getUserPresentCumulativeCoupon(Map<String, Object> map);

    List<UserLanternIllumineCustomize> getUserLanternIllumineList(Map<String, Object> map);

    

}
