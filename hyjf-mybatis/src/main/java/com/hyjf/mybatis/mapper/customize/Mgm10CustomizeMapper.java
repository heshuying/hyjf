package com.hyjf.mybatis.mapper.customize;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface Mgm10CustomizeMapper {

	/**
	 * 根据指定条件取得用户的出借总额
	 * @param paramMap
	 * @return
	 */
    BigDecimal getTenderAccountSum(Map<String,Object> paramMap);
    
    /**
     * 根据用户编号更新用户推荐星数量
     * @param userId
     */
    void updateUserRecommend(Map<String,Object> paramMap);
    
    /**
     * 取得达到或超过3次有效邀请的用户
     * @return
     */
    List<Integer> selectThrUser();

}
