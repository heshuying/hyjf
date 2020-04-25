package com.hyjf.mybatis.mapper.customize;

import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.customize.PushMoneyCustomize;

public interface PushMoneyCustomizeMapper {
	/**
	 * 提成管理 （列表数量）
	 * @param accountManageBean
	 * @return
	 */
	public Integer queryPushMoneyCount(PushMoneyCustomize pushMoneyCustomize) ;

	/**
	 * 提成管理 （列表）
	 * @param accountManageBean
	 * @return
	 */
	public List<PushMoneyCustomize> queryPushMoneyList(PushMoneyCustomize pushMoneyCustomize) ;

	   /**
     * 提成管理 （明细数量）
     * @param accountManageBean
     * @return
     */
    public Integer queryPushMoneyDetailCount(PushMoneyCustomize pushMoneyCustomize) ;

    /**
     * 提成管理 （明细）
     * @param accountManageBean
     * @return
     */
    public List<PushMoneyCustomize> queryPushMoneyDetail(PushMoneyCustomize pushMoneyCustomize) ;

    /**
     * 查询金额总计 
     * @param id
     * @return
     */
	public Map<String, Object> queryPushMoneyTotle(
			PushMoneyCustomize pushMoneyCustomize);

}

