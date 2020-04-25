package com.hyjf.mybatis.mapper.customize.admin;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.admin.PrizeGetCustomize;

public interface PrizeGetCustomizeMapper {
	
    /**
     * 
     * 获取奖品配置列表
     * @author hsy
     * @param paraMap
     * @return
     */
    List<PrizeGetCustomize> selectPrizeConfList(Map<String, Object> paraMap);
    
    /**
     * 
     * 更新奖品已用数量
     * @author hsy
     * @param paraMap
     * @return
     */
    public int updatePrizeCountUsed(Map<String, Object> paraMap);
    
    /**
     * 
     * 更新奖品可用状态
     * @author hsy
     * @param paraMap
     * @return
     */
    public int updatePrizeStatus(Map<String, Object> paraMap);
    
    /**
     * 
     * 获取已中奖用户列表
     * @author hsy
     * @param paraMap
     * @return
     */
    public List<Map<String, Object>> selectPrizeWinList(Map<String, Object> paraMap);
    
    /**
     * 
     * 根据奖品分组编码删除奖品配置
     * @author hsy
     * @param groupCode
     * @return
     */
    public int deletePrizeByGroupCode(String groupCode);

}
