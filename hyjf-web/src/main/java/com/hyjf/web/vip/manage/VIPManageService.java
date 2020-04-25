package com.hyjf.web.vip.manage;

import java.util.List;

import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.web.BaseService;

/**
 * 
 * VIP等级管理Service接口
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月12日
 * @see 下午3:52:54
 */
public interface VIPManageService extends BaseService {

	List<VipInfo> selectVIPInfoList();
	
	
}
