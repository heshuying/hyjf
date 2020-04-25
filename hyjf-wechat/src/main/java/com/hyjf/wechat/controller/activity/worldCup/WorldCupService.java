package com.hyjf.wechat.controller.activity.worldCup;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.GuessingWinning;
import com.hyjf.mybatis.model.auto.WorldTeamMatch;
import com.hyjf.wechat.base.BaseService;

/**
 * @author limeng
 * @version WorldCupService, v0.1 2018/6/13 16:16
 */
public interface WorldCupService extends BaseService {

    /**
     * 根据比赛进程查询比赛场次信息
     * @param matchType
     * @return
     */
    List<Map<String,Object>> selectTeam(Integer matchType);

    /**
     * 查看我的竞猜记录
     * @param userId
     * @return
     */
    List<Map<String,String>> searchMyRecord(String userId);

    /**
     * 查看我的竞猜机会
     * @param userId
     * @return
     */
    List<Map<String,Object>>  searchMyChance(String userId );

    /**
     *查看竞猜排行
     * @return
     */
	List<GuessingWinning>  searchRankList();

    /**
     *竞猜冠军列表
     * @return
     */
    List<Map<String,Object>> searchChampionList(String userId,Integer limitStart,Integer offset);

    /**
     *竞猜冠军列表（未登录,或未参与）
     * @return
     */
    List<Map<String,Object>> searchChampionListAll(Integer limitStart,Integer offset);

    /**
     * 查看用户是否参与冠军竞猜
     * @param userId
     * @return
     */
    boolean checkJoinChampion(String userId);
    /**
     * 判断用户是否已经有自己的冠军球队
     * @param userId 用户ID
     * @return boolean
     */
    boolean selectIsChampion(Integer userId);

    /**
     * 保存用户参与冠军球队投递
     * @param userId 用户ID
     * @param championId 所选择冠军球队ID
     * @return JSONObject
     */
    JSONObject participateChampion(Integer userId, Integer championId);

    /**
     * 获取用户在活动期间加入计划的期限和加入计划金额
     * @param userId 用户ID
     * @param startTime 加入开始时间
     * @param endTime 加入结束时间
     * @param account 加入金额
     * @param flag 标志位，如不为null，则添加判断条件必须有推荐人
     * @return
     */
    List<Map<String, Object>> getHjhPlanListByUserId(Integer userId,
                                                     Integer startTime, Integer endTime, long account, Integer flag);

	/**
	 * 获取用户推荐
	 * @param userId 推荐人用户ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	List<Integer> getspreadUser(Integer userId, Integer startTime, Integer endTime);

	/**
	 * 统计用户已经使用的机会数
	 * @param userId 用户ID
	 * @return
	 */
	Integer getUserdChance(Integer userId);

	/**
	 * 判断当前是否可以参与活动
	 * @param guessingMatchId 对阵ID
	 * @return
	 */
	boolean getMatchTime(String guessingMatchId);

	/**
	 * 判断用户是否已经参与了本场比赛
	 * @param userId 用户ID
	 * @param guessingMatchId 比赛对阵ID
	 * @return
	 */
	boolean getMatchChance(Integer userId, String guessingMatchId);

	/**
	 * 保存用户参与竞猜输赢
	 * @param userId 用户ID
	 * @param guessingMatchId 参与竞猜对阵
	 * @param matchTeamId 支持获胜球队ID
	 */
	void saveGuessMatch(Integer userId, String guessingMatchId,
			String matchTeamId);


	/**
	 * 查询用户竞猜标语
	 * @param userId
	 * @return
	 */
	String findUserTitle(String userId);

	/**
	 * 查询当前进行的比赛类型  1/8 1/4。。。
	 * @return
	 */
	Integer findMatchType();
	
	/**
	 * 根据用户ID获取用户参与的机会数
	 * @param userId
	 * @return
	 */
	public Integer hasChance(Integer userId);

	/**
	 * 根据用户ID获取用户选择的冠军球队LOGO
	 * @param userId 用户ID
	 * @return
	 */
	String getMyChampionLogo(Integer userId);

	/**
	 * 判断当前用户是否可以参与竞猜本次冠军球队
	 * @param championId 球队ID
	 * @return
	 */
	boolean selectIsGuessing(Integer championId);

	Integer isGuessing(Integer userId);

	/**
	 * 判断用户是否可以参与竞猜冠军球队
	 * @return
	 */
	Integer selectIsGuessing();

	/**
	 * 根据比赛ID查询比赛
	 * @param guessingMatchId 比赛ID
	 * @return
	 */
	WorldTeamMatch getTeamMatchById(Integer guessingMatchId);

	/**
	 * 根据类型判断比赛是否结束
	 * @param matchType比赛类型
	 * @return（0：未结束，1：已结束，2：未知）
	 */
	Integer findIsOver(Integer matchType);
}
