package com.hyjf.mybatis.mapper.customize.admin.act;

import com.hyjf.mybatis.model.auto.ActSignin;
import com.hyjf.mybatis.model.auto.ActSigninExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ActSigninCustomizeMapper {
    int countByExample(ActSigninExample example);

    List<ActSignin> selectByExample(Map<String, Object> paraMap);

    List<ActSignin> selectByPrimaryKey(Map<String, Object> paraMap);

}