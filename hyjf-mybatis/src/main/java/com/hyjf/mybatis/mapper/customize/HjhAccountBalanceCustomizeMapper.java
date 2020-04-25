package com.hyjf.mybatis.mapper.customize;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hyjf.mybatis.model.auto.HjhAccountBalance;
import com.hyjf.mybatis.model.customize.HjhAccountBalanceCustomize;
import com.hyjf.mybatis.model.customize.TimeToolsBean;

public interface HjhAccountBalanceCustomizeMapper {
    
	public void insertHjhAccountBalance(HjhAccountBalanceCustomize entity);

    public void updateHjhAccountBalance(HjhAccountBalanceCustomize entity);

    public void deleteHjhAccountBalance(int id);
    
    /**
     *按日查询总计
     * @param hjhAccountBalanceCustomize
     * @return
     */
    public HjhAccountBalanceCustomize getHjhAccountBalanceSum(HjhAccountBalanceCustomize hjhAccountBalanceCustomize );
    /**
     *按月查询总计
     * @param hjhAccountBalanceCustomize
     * @return
     */
    public HjhAccountBalanceCustomize getHjhAccountBalanceMonthSum(HjhAccountBalanceCustomize hjhAccountBalanceCustomize );
    /**
     *按月查询总计new 
     * @param hjhAccountBalanceCustomize
     * @return
     */
    public HjhAccountBalanceCustomize getHjhAccountBalanceMonthSumNew(HjhAccountBalanceCustomize hjhAccountBalanceCustomize );

    /**
     * 按日统计查询数量
     * @param entity
     * @return
     */
    public Integer getHjhAccountBalancecount(HjhAccountBalanceCustomize entity);
    /**
     * 按月统计查询数量
     * @param entity
     * @return
     */
    public Integer getHjhAccountBalanceMonthCount(HjhAccountBalanceCustomize entity);
   
    
    
    public List<HjhAccountBalanceCustomize>  getHjhAccountBalanceList(HjhAccountBalanceCustomize entity);
    /**
     * 获取该日期的实际债转和复投金额
     * @param date
     * @return
     */
    List<HjhAccountBalance> selectHjhAccountBalanceForActList(Date date);
    /**
     * 按月统计查询数据
     * @param entity
     * @return
     */
	public List<HjhAccountBalanceCustomize> getHjhAccountBalanceMonthList(
			HjhAccountBalanceCustomize hjhAccountBalanceCustomize);
	public  int getHjhAccountBalanceMonthCountNew(HjhAccountBalanceCustomize hjhAccountBalanceCustomize);

	public List<HjhAccountBalanceCustomize> getHjhAccountBalanceMonthListNew(
			HjhAccountBalanceCustomize hjhAccountBalanceCustomize);
}
