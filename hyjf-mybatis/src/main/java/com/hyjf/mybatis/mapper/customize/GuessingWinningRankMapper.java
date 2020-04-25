package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.GuessingWinning;
import com.hyjf.mybatis.model.auto.WorldTeamMatch;

import java.util.List;
import java.util.Map;

public interface GuessingWinningRankMapper {

	/**
	 * 查看竞猜排行
	 * @return
	 */
    List<GuessingWinning> selectGuessRank();
    /**
     * 根据比赛类型获取比赛情况
     * @param param
     * @return
     */
    List<Map<String, Object>> searchTeam(Map<String, Object> param);
    /**
     * 查看我的竞猜记录
     * @param param
     * @return
     */
    List<Map<String, String>> searchRecord(Map<String, String> param);
    /**
     * 根据比赛类型查询当前比赛未结束得数量
     * @param matchType 比赛类型 
     * @return
     */
	Integer getTeamByType(int matchType);
	/**
	 * 根据条件获取用户所投球队比赛
	 * @param param
	 * @return
	 */
	List<WorldTeamMatch> getMatchTeamByParam(Map<String, Object> param);
}