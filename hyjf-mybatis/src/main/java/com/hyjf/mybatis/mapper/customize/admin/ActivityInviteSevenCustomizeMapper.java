package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.InviteSevenCustomize;

public interface ActivityInviteSevenCustomizeMapper {

	List<InviteSevenCustomize> selectInviteSevenList(Map<String, Object> paraMap);

	List<Map<String,Object>> selectInviteSevenTenList();
}