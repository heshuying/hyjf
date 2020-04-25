package com.hyjf.mybatis.mapper.customize;
import com.hyjf.mybatis.model.customize.SensitiveDataDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SensitiveDataMapper {

	/**
	 * 查询date时间开户注册的用户
	 * @param date
	 * @return
	 */
	List<SensitiveDataDto> selectOpenUsersByDate(@Param("date") String date);

	/**
	 * 查询date时间改用户的充值总额
	 * @param userId
	 * @param date
	 * @return
	 */
	double selectRechargeTotalByDate(@Param("userId")Integer userId, @Param("date")String date);

	/**
	 * 查询用户是否有债权出借
	 * @param userId
	 * @return
	 */
	int selectTenderCount(@Param("userId")Integer userId);

	/**
	 * 查询用户在date的提现总额
	 * @param userId
	 * @param date
	 * @return
	 */
	double selectWithTotalByDate(@Param("userId")Integer userId, @Param("date")String date);
}