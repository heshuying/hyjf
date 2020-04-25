package com.hyjf.mybatis.mapper.customize.worldcup;

import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.WorldCupTeam;
import com.hyjf.mybatis.model.auto.WorldTeamMatch;
import com.hyjf.mybatis.model.customize.worldcup.GuessingActivitieCustomize;
import com.hyjf.mybatis.model.customize.worldcup.WorldTeamCustomize;
import com.hyjf.mybatis.model.customize.worldcup.WorldTeamMatchCustomize;

import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/6/12.
 */
public interface WorldCupActivityCustomizeMapper {

    /**
     * 根据条件查询竞猜输赢列表
     *
     * @return
     */
    List<GuessingActivitieCustomize> getRecordList(Map<String, Object> record);

    /**
     * 根据条件查询竞猜输赢个数
     *
     * @return
     */
    Integer countRecordBySearchCon(Map<String, Object> record);

    /**
     * 根据条件查询竞猜冠军列表
     *
     * @return
     */
    List<GuessingActivitieCustomize> getChampionRecordList(Map<String, Object> record);

    /**
     * 按条件查询竞猜冠军个数
     *
     * @return
     */
    Integer countChampionRecordBySearchCon(Map<String, Object> record);

    /**
     * 获取世界杯活动配置球队
     *
     * @return
     */
     List<WorldCupTeam> getWorldCupTeamSelect();

	/**
	 * 查询已经选择的球队对阵
	 *
	 * @return
	 */
	 List<WorldTeamCustomize> getWorldCupTeamSelected();

    /**
     * 获取世界杯活动决战赛比赛配置
     *
     * @return
     */
    List<WorldTeamMatchCustomize> getWorldTeamMatchList();


    /**
     * 根据用户ID查询用户信息
     * @param userId
     * @return
     */
	UsersInfo getByUserId(Integer userId);
	/**
	 * 获取用户在活动期间加入计划的期限和加入计划金额
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getHjhPlanListByUserId(Map<String, Object> param);

	/**
	 * 获取用户推荐
	 * @param param
	 * @return
	 */
	List<Integer> getspreadUser(Map<String, Object> param);

	/**
	 * 统计当前用户已经使用的机会
	 * @param userId 用户ID
	 * @return
	 */
	Integer getUserdChance(Integer userId);

	/**
	 * 统计用户参与本场比赛次数
	 * @param param
	 * @return
	 */
	Integer getMatchChance(Map<String, Object> param);

	/**
	 * 获取用户猜中场次所在排名
	 * @param userId 用户ID
	 * @return
	 */
	Integer getChanceNum(Integer userId);

	/**
	 * 根据球队ID设置球队淘汰
	 * @param teamId
	 */
	void updateIsEliminateById(Integer teamId);

	/**
	 * 根据对阵ID和获胜球队ID更新猜中用户
	 * @param param
	 */
	void updateGuessWin(Map<String, Object> param);
	/**
	 * 根据对阵ID和获胜球队ID更新未猜中用户
	 * @param param
	 */
	void updateGuessLose(Map<String, Object> param);

	/**
	 * 更新猜中竞猜冠军用户球队胜场数
	 * @param winTeamId 获胜球队ID
	 */
	void updateChampion(Integer winTeamId);


	/**
	 * 获取用户在活动期间加入计划的期限和加入计划金额
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getHjhPlanByUserId(Map<String, Object> param);
	
	/**
	 * 根据比赛类型和对阵小组获取对阵信息
	 * @param param
	 * @return
	 */
	WorldTeamMatch getWorldTeam(Map<String, Object> param);

	/**
	 * 根据用户ID获取用户的冠军球队LOGO
	 * @param userId 用户ID
	 * @return
	 */
	WorldCupTeam getChampionLogo(Integer userId);

	/**
	 * 判断16进8球队结果是否已经有，
	 */
	 Integer getWorldCupTeamResultCount();

	 /**
	  * 查询是否有比赛
	  * @return
	  */
	int countTeamMatch();

	/**
	 * 获取用户参与竞猜次数
	 * @param userId 用户ID
	 * @return
	 */
	Integer isGuessing(Integer userId);

	/**
	 * 判断当前比赛类型是否已经结束
	 * @param matchType 比赛类型
	 * @return
	 */
	Integer findIsOver(Integer matchType);

	/**
	 *查询条件下得加入计划，按照时间正向排序
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getFirstHjhPlanListByUserId(
			Map<String, Object> param);

	/**
	 * 查询所有未结束得比赛
	 * @return
	 */
	List<WorldTeamMatch> selectByExample();

	/**
	 * 更新用户猜中场数
	 * @param param
	 */
	void updateGuessWinNum(Map<String, Object> param);

}
