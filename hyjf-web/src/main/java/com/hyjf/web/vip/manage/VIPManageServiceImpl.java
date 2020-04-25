package com.hyjf.web.vip.manage;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.mybatis.model.auto.VipInfoExample;
import com.hyjf.web.BaseServiceImpl;

/**
 * 
 * VIP管理Service
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月12日
 * @see 下午2:41:32
 */
@Service
public class VIPManageServiceImpl extends BaseServiceImpl implements VIPManageService {

    /**
     * 
     * 获取VIP等级列表
     * @author hsy
     * @return
     * @see com.hyjf.web.vip.manage.VIPManageService#selectVIPInfoList()
     */
	@Override
	public List<VipInfo> selectVIPInfoList() {
		VipInfoExample example = new VipInfoExample();
		example.createCriteria().andDelFlgEqualTo(0);
		example.setOrderByClause("vip_level ASC");
		List<VipInfo> list = vipInfoMapper.selectByExample(example);
		return list;
	}
	
    
}
