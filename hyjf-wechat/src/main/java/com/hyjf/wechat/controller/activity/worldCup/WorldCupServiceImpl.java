package com.hyjf.wechat.controller.activity.worldCup;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.mybatis.mapper.auto.GuessingWinningMapper;
import com.hyjf.mybatis.mapper.auto.WorldCupTeamMapper;
import com.hyjf.mybatis.mapper.auto.WorldTeamMatchMapper;
import com.hyjf.mybatis.mapper.customize.GuessingWinningRankMapper;
import com.hyjf.mybatis.mapper.customize.worldcup.WorldCupActivityCustomizeMapper;
import com.hyjf.mybatis.model.auto.GuessingChampion;
import com.hyjf.mybatis.model.auto.GuessingChampionExample;
import com.hyjf.mybatis.model.auto.GuessingWinning;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.WorldCupTeam;
import com.hyjf.mybatis.model.auto.WorldCupTeamExample;
import com.hyjf.mybatis.model.auto.WorldTeamMatch;
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.service.regist.UserService;

/**
 * @author limeng
 * @version WorldCupService, v0.1 2018/6/13 16:16
 */
@Service
public class WorldCupServiceImpl extends BaseServiceImpl implements
		WorldCupService {
	private Logger logger = LoggerFactory.getLogger(WorldCupServiceImpl.class);
    //活动开始时间
    public static final String startTime = "2018-06-29 00:00:00";
    //活动结束时间
    public static final String endTime = "2018-07-15 23:59:59";
    @Autowired
	public WorldCupTeamMapper worldCupTeamMapper;
    @Autowired
    public com.hyjf.mybatis.mapper.auto.GuessingChampionMapper GuessingChampionMapper;
    @Autowired
	public WorldCupActivityCustomizeMapper worldCupActivityCustomizeMapper;
	@Autowired
	public WorldTeamMatchMapper worldTeamMatchMapper;
	@Autowired
	public GuessingWinningMapper guessingWinningMapper;
	@Autowired
    private GuessingWinningRankMapper guessingWinningRankMapper;
    @Autowired
    private UserService userService;

	@Override
	public boolean selectIsChampion(Integer userId) {
		boolean flag = false;
		try {
			GuessingChampionExample example = new GuessingChampionExample();
			GuessingChampionExample.Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(userId);
			List<GuessingChampion> list = GuessingChampionMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("用户ID："+userId+"异常！",e);
		}
		return flag;
	}

	@Override
	public JSONObject participateChampion(Integer userId, Integer championId) {
		JSONObject jsonObject = new JSONObject();
		try {
			UsersInfo userInfo = worldCupActivityCustomizeMapper.getByUserId(userId);
			GuessingChampion GuessingChampion = new GuessingChampion();
			GuessingChampion.setUserId(userId);
			GuessingChampion.setTrueName(userInfo.getTruename());
			GuessingChampion.setGuessingChampionId(championId);
			GuessingChampion.setVoteTime(getSecondTimestampTwo(new Date()));
			GuessingChampion.setCreateTime(getSecondTimestampTwo(new Date()));
			GuessingChampionMapper.insertSelective(GuessingChampion);
			jsonObject.put("status", "000");
			jsonObject.put("statusDesc", "投注成功！");
			logger.info("用户ID："+userId+"投注"+"球队ID："+championId+"成功");
		} catch (Exception e) {
			jsonObject.put("status", 11);
			jsonObject.put("statusDesc", "投注异常，请联系管理员！");
			logger.error("用户ID："+userId+"投注"+"球队ID："+championId+"失败",e);
		}
		return jsonObject;
	}
	
	 /**
     * 获取精确到秒的时间戳 
     * @param date
     * @return
     */
    public static int getSecondTimestampTwo(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        return Integer.valueOf(timestamp);
    }

	@Override
	public List<Map<String, Object>> getHjhPlanListByUserId(Integer userId,
			Integer startTime, Integer endTime, long account, Integer flag) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		param.put("account", account);
		param.put("flag", flag);
		return worldCupActivityCustomizeMapper.getHjhPlanListByUserId(param);
	}

    @Override
    public List<Integer> getspreadUser(Integer userId, Integer startTime,
                                       Integer endTime) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        return worldCupActivityCustomizeMapper.getspreadUser(param);
    }

    @Override
    public Integer getUserdChance(Integer userId) {
        return worldCupActivityCustomizeMapper.getUserdChance(userId);
    }


    /**
     * 根据比赛进程查询比赛场次信息
     *
     * @param matchType
     * @return
     */
    @Override
    public List<Map<String, Object>> selectTeam(Integer matchType) {
        Map<String, Object> param=new HashMap<>();
        if(null != matchType && matchType < 6){
        	boolean flag = false;
        	if(matchType < 6 && matchType > 3){
        		//根据比赛类型查询当前类型比赛未结束得数量
        		Integer count = guessingWinningRankMapper.getTeamByType(3);
        		if(count > 0){
        			flag = true;
        		}
        	}else if(matchType > 1){
        		//根据比赛类型查询当前类型比赛未结束得数量
        		Integer count = guessingWinningRankMapper.getTeamByType(matchType -1);
        		if(count > 0){
        			flag = true;
        		}
        	}
        	if(!flag){
           		param.put("matchType",matchType);
        		return guessingWinningRankMapper.searchTeam(param);
        	}
        }
        return null;
    }

    /**
     * 查看我的竞猜记录
     *
     * @param userId
     * @return
     */
    @Override
    public List<Map<String, String>> searchMyRecord(String userId) {
        Map<String, String> param=new HashMap<>();
        param.put("userId",userId);
        List<Map<String, String>> result = guessingWinningRankMapper.searchRecord(param);
        return result;
    }

    /**
     * 查看我的竞猜机会
     *
     * @param userId
     * @return
     */
    @Override
    public List<Map<String, Object>> searchMyChance(String userId) {
        Users user = userService.getUserByUserId(Integer.valueOf(userId));
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = df.parse(startTime);
            dt2 = df.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer timestamp1 = Integer.valueOf((int) (dt1.getTime() / 1000));
        Integer timestamp2 = Integer.valueOf((int) (dt2.getTime() / 1000));

        List<Map<String, Object>> hjhPlanListByUserId = getHjhPlanListByUserId(Integer.valueOf(userId), timestamp1, timestamp2,10000,null);
        for (Map<String, Object> map : hjhPlanListByUserId) {

            HashMap<String, Object> record = new HashMap<>();
            record.put("planNid", map.get("planNid"));//计划编号
            record.put("PlanName", map.get("PlanName"));//计划名称
            record.put("lockPeriod", map.get("lockPeriod"));//锁定期
            record.put("entryPlanTime", map.get("entryPlanTime"));//加入时间

            BigDecimal accedeAccount = (BigDecimal) map.get("accedeAccount");
            CommonUtils.formatAmount(accedeAccount);
            record.put("accedeAccount", CommonUtils.formatAmount(accedeAccount));//加入金额
            long l = accedeAccount.longValue();
            String keys = this.getkey(l, Integer.valueOf((Integer) map.get("lockPeriod")));
            Integer chance = this.getChance(keys);
            
            record.put("guessingChance", chance);//竞猜机会
            result.add(record);
        }

        //计算当前用户是否在活动期间注册切加入计划首笔出借满5000元（计划类1月及以上标）
        if (null != user.getRegTime()) {
            if (timestamp2 >= user.getRegTime() && user.getRegTime() >= timestamp1) {
                List<Map<String, Object>> hjhPlanList = getFirstHjhPlanListByUserId(Integer.valueOf(userId), timestamp1, timestamp2, 5000,1);
                if(hjhPlanList!=null&&hjhPlanList.size()>0){
                	
                	Map<String, Object> stringObjectMap = hjhPlanList.get(0);
      			  long accedeAccount = ((java.math.BigDecimal) stringObjectMap.get("accedeAccount")).longValue();
      			  if(accedeAccount >= 5000){
      				  HashMap<String, Object> record = new HashMap<>();
      				  record.put("planNid", stringObjectMap.get("planNid"));//计划编号
      				  record.put("PlanName", stringObjectMap.get("PlanName"));//计划名称
      				  record.put("accedeAccount", CommonUtils.formatAmount(stringObjectMap.get("accedeAccount")+""));//加入金额
      				  record.put("lockPeriod", stringObjectMap.get("lockPeriod"));//锁定期
      				  record.put("entryPlanTime", stringObjectMap.get("entryPlanTime"));//加入时间
      				  record.put("guessingChance", 1);//竞猜机会
      				  result.add(record);
      			  }
                    
                }
            }
        }

        return result;
    }

    /**
     * 查看竞猜排行
     *
     * @return
     */
    @Override
    public List<GuessingWinning> searchRankList() {
        List<GuessingWinning> guessingWinnings = guessingWinningRankMapper.selectGuessRank();
        return guessingWinnings;
    }

    /**
     * 竞猜冠军列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> searchChampionList(String userId, Integer limitStart, Integer offset) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        GuessingChampionExample GuessingChampionExample = new GuessingChampionExample();
        GuessingChampionExample.Criteria criteria = GuessingChampionExample.createCriteria();
        criteria.andUserIdEqualTo(Integer.valueOf(userId));
        List<GuessingChampion> GuessingChampions = GuessingChampionMapper.selectByExample(GuessingChampionExample);
        GuessingChampion GuessingChampion = GuessingChampions.get(0);
        Integer guessingChampionId = GuessingChampion.getGuessingChampionId();//用户支持的冠军球队

        Map<String, Object> param = new HashMap<>();
        param.put("matchTime", GuessingChampion.getCreateTime());
        param.put("guessingChampionId", guessingChampionId);
        //获取用户所投球队得比赛
        List<WorldTeamMatch> page = guessingWinningRankMapper.getMatchTeamByParam(param);
        
        int size=page.size();
        
        param.put("limitStart", limitStart);
        param.put("offset", offset);
        List<WorldTeamMatch> worldTeamMatches = guessingWinningRankMapper.getMatchTeamByParam(param);//查询支持球队的对战记录
        HashMap<String, Object> first = new HashMap<>();
        int endPage = size % offset == 0 ? size / offset : (size / offset + 1);//最后一页
        first.put("endPage",endPage);
        result.add(first);
        for (WorldTeamMatch worldTeamMatch : worldTeamMatches) {
            HashMap<String, Object> record = new HashMap<>();
            Integer homeMatchTeam = worldTeamMatch.getHomeMatchTeam();
            Integer visitingMatchTeam = worldTeamMatch.getVisitingMatchTeam();
            if (!(guessingChampionId == homeMatchTeam)) {
                visitingMatchTeam = homeMatchTeam;
                homeMatchTeam = guessingChampionId;
            }
            WorldCupTeam worldCupTeam = worldCupTeamMapper.selectByPrimaryKey(homeMatchTeam);
            WorldCupTeam worldCupTeam1 = worldCupTeamMapper.selectByPrimaryKey(visitingMatchTeam);
            record.put("teamId", guessingChampionId);//球队ID
            record.put("matchType", worldTeamMatch.getMatchType());//比赛类型
            record.put("teamName", "");//球队名称名称
            record.put("teamLogo", "");//球队LOGO
            record.put("homeTeamId", worldCupTeam==null?"":worldCupTeam.getId());//对阵主队ID
            record.put("visitingTeamId", worldCupTeam1==null?"":worldCupTeam1.getId());//对阵客队ID
            record.put("homeTeamName", worldCupTeam==null?"":worldCupTeam.getTeamName());//对阵主队名称
            record.put("visitingTeamName", worldCupTeam1==null?"":worldCupTeam1.getTeamName());//对阵客队名称
            record.put("winTeamId", worldTeamMatch.getWinTeamId());//胜利球队ID
            record.put("homeTeamLogo", worldCupTeam==null?"":worldCupTeam.getTeamLogo());//对阵主队LOGO
            record.put("visitingTeamLogo", worldCupTeam1==null?"":worldCupTeam1.getTeamLogo());//对阵客队LOGO
            result.add(record);
        }
        return result;
    }

    /**
     * 竞猜冠军列表（未登录,或未参与）
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> searchChampionListAll(Integer limitStart,Integer offset) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        //查询是否拥有比赛
        int count = worldCupActivityCustomizeMapper.countTeamMatch();
        if(count > 0){
        	
        	WorldCupTeamExample worldCupTeamExample = new WorldCupTeamExample();
        	WorldCupTeamExample.Criteria criteria = worldCupTeamExample.createCriteria();
        	criteria.andIsEliminateEqualTo(1);
        	worldCupTeamExample.setOrderByClause("champion_num,id desc");
        	List<WorldCupTeam> page = worldCupTeamMapper.selectByExample(worldCupTeamExample);
        	int size=page.size();
        	worldCupTeamExample.setLimitStart(limitStart);
        	worldCupTeamExample.setLimitEnd(offset);
        	List<WorldCupTeam> worldCupTeams = worldCupTeamMapper.selectByExample(worldCupTeamExample);
        	HashMap<String, Object> first = new HashMap<>();
        	
        	int endPage = size % offset == 0 ? size / offset : (size / offset + 1);//最后一页
        	first.put("endPage",endPage);
        	result.add(first);
        	for (WorldCupTeam worldCupTeam : worldCupTeams) {
        		HashMap<String, Object> record = new HashMap<>();
        		record.put("matchType", "");//比赛类型
        		record.put("teamId", worldCupTeam.getId());//球队ID
        		record.put("teamName", worldCupTeam.getTeamName());//球队名称名称
        		record.put("teamLogo", worldCupTeam.getTeamLogo());//球队LOGO
        		record.put("homeTeamName", "");//对阵主队名称
        		record.put("visitingTeamName", "");//对阵客队名称
        		record.put("winTeamId", "");//胜利球队ID
        		record.put("homeTeamLogo", "");//对阵主队LOGO
        		record.put("visitingTeamLogo", "");//对阵客队LOGO
        		result.add(record);
        		
        	}
        }
        return result;
    }

    /**
     * 查看用户是否参与冠军竞猜
     *
     * @param userId
     * @return
     */
    @Override
    public boolean checkJoinChampion(String userId) {
        GuessingChampionExample GuessingChampionExample = new GuessingChampionExample();
        GuessingChampionExample.Criteria criteria = GuessingChampionExample.createCriteria();
        criteria.andUserIdEqualTo(Integer.valueOf(userId));
        List<GuessingChampion> guessingChampions = GuessingChampionMapper.selectByExample(GuessingChampionExample);
        if (guessingChampions != null && !guessingChampions.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean getMatchTime(String guessingMatchId) {
        boolean flag = false;
        try {
            WorldTeamMatch worldTeamMatch = worldTeamMatchMapper.selectByPrimaryKey(Integer.parseInt(guessingMatchId));
            //比赛开始时间
            Integer matchTime = worldTeamMatch.getMatchTime();
            long curren = System.currentTimeMillis();
            curren += 10 * 60 * 1000;
            //十分钟之后的时间戳
            String timestamp1 = String.valueOf(curren / 1000);
            if (matchTime >= Integer.parseInt(timestamp1)) {
                flag = true;
            }
        } catch (Exception e) {
            logger.error("处理比赛前十分钟异常", e);
        }

        return flag;
    }

    @Override
    public boolean getMatchChance(Integer userId, String guessingMatchId) {
        boolean flag = false;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("guessingMatchId", Integer.parseInt(guessingMatchId));
        Integer count = worldCupActivityCustomizeMapper.getMatchChance(param);
        if (count <= 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public void saveGuessMatch(Integer userId, String guessingMatchId,
                               String matchTeamId) {
    	if(getMatchChance(userId, guessingMatchId)){
    		
    		GuessingWinning guessingWinning = new GuessingWinning();
    		guessingWinning.setUserId(userId);
    		UsersInfo userInfo = worldCupActivityCustomizeMapper.getByUserId(userId);
    		guessingWinning.setTrueName(userInfo==null?"":userInfo.getTruename());
    		guessingWinning.setGuessingMatchId(Integer.parseInt(guessingMatchId));
    		guessingWinning.setUserMatchTeamId(Integer.parseInt(matchTeamId));
    		Map<String, Object> param = new HashMap<String, Object>();
    		param.put("userId", userId);
    		//处理用户竞猜次数
    		Integer count1 = worldCupActivityCustomizeMapper.getMatchChance(param);
    		guessingWinning.setGuessingMatchNum(count1+1);
    		param.put("guessingMatchResult", 1);
    		//处理用户猜中次数
    		Integer count2 = worldCupActivityCustomizeMapper.getMatchChance(param);
    		guessingWinning.setGuessingFieldNum(count2);
    		//获取用户猜中场次排名
    		Integer num = worldCupActivityCustomizeMapper.getChanceNum(userId);
    		guessingWinning.setGuessingRankings(num);
    		
    		long curren = System.currentTimeMillis();
    		String timestamp1 = String.valueOf(curren / 1000);
    		guessingWinning.setGuessingTime(Integer.parseInt(timestamp1));
    		guessingWinning.setCreateTime(Integer.parseInt(timestamp1));
    		guessingWinningMapper.insert(guessingWinning);
    	}
    }


    /**
     * 查询用户竞猜标语
     * (淘汰：争冠失败，止步n强；
     * 未淘汰：先下一城，挺进1/n决赛)
     * @param userId
     * @return
     */
    @Override
    public String findUserTitle(String userId) {
        GuessingChampionExample guessingChampioonExample = new GuessingChampionExample();
        GuessingChampionExample.Criteria criteria = guessingChampioonExample.createCriteria();
        criteria.andUserIdEqualTo(Integer.valueOf(userId));
        List<GuessingChampion> guessingChampioons = GuessingChampionMapper.selectByExample(guessingChampioonExample);
        GuessingChampion guessingChampioon = guessingChampioons.get(0);
        Integer guessingChampionId = guessingChampioon.getGuessingChampionId();//用户支持的球队id

        Map<String, Object> param = new HashMap<>();
        param.put("matchTime", guessingChampioon.getCreateTime());
        param.put("guessingChampionId", guessingChampionId);
        param.put("orderBy", "desc");
        //获取用户所投球队得比赛
        List<WorldTeamMatch> worldTeamMatches = guessingWinningRankMapper.getMatchTeamByParam(param);//查询支持球队的对战记录

        WorldTeamMatch worldTeamMatch = worldTeamMatches.get(0);//得到最后一场比赛
        Integer winTeamId = worldTeamMatch.getWinTeamId();//获胜球队id
        Integer matchType = worldTeamMatch.getMatchType();//(1:1/8决赛;2:1/4决赛;3:半决赛;4季军赛;5:总决赛)',
        String middle="";
        
        if(1  < worldTeamMatches.size()){      
			if(null != winTeamId && winTeamId!=guessingChampionId){
				if(matchType==1){
					middle="";
				}else if(matchType==2){
					middle="争冠失败，止步8强";
				}else if(matchType==3){
					middle="争冠失败，勇争第三";
				}else if(matchType==4){
					middle="争冠失败，止步4强";
				}else if(matchType==5){
					middle="亚军";
				}
			}else {
				if(matchType==1){
					middle="";
				}else if(matchType==2){
					middle="先下一城，挺近1/4决赛";
				}else if(matchType==3){
					middle="再下一城，挺进半决赛";
				}else if(matchType==4){
					if(null != winTeamId && winTeamId==guessingChampionId){        				
						middle="第三名";
					}else if(null == winTeamId){
						middle="争冠失败，勇争第三";
					}else{
						middle="争冠失败，止步4强";        				
					}
				}else if(matchType==5){
					if(null != winTeamId && winTeamId==guessingChampionId){        				
						middle="冠军";
					}else if(null == winTeamId){
						middle="再下一城，挺进决赛";
					}
				}
			}
		}else{
			if(1 ==worldTeamMatches.size()){
				if(null != winTeamId && winTeamId!=guessingChampionId){	
					if(1 == matchType){			
						middle="争冠失败，止步16强";
					}else if(2 == matchType){
						middle="争冠失败，止步8强";						
					}else if(3 == matchType){
						middle="争冠失败，勇夺第三";						
					}else if(4 == matchType){
						middle="争冠失败，止步四强";						
					}else if(5 == matchType){
						middle="亚军";						
					}
				}else if(null != winTeamId && winTeamId==guessingChampionId){	
					if(1 == matchType){			
						middle="";
					}else if(2 == matchType){
						middle="先下一城，挺近1/4决赛";						
					}else if(3 == matchType){
						middle="争冠失败，勇夺第三";						
					}else if(4 == matchType){
						middle="第三名";						
					}else if(5 == matchType){
						middle="冠军";						
					}
				}{}
			}
		}
        
        return middle;
    }

    /**
     * 查询当前的比赛类型
     * @return
     */
    @Override
    public Integer findMatchType() {
        List<WorldTeamMatch> worldTeamMatches = worldCupActivityCustomizeMapper.selectByExample();
        if(worldTeamMatches!=null&&!worldTeamMatches.isEmpty()){
            WorldTeamMatch worldTeamMatch = worldTeamMatches.get(0);
            return worldTeamMatch.getMatchType();
        }
        return null;

    }
    
    /**
	  * 判断当前用户有几次参与活动的机会
	  * @author walter.limeng
	  * @param userId 用户ID
	  * @return Integer
	  */
    @Override
	 public Integer hasChance(Integer userId){
		 Integer chance = 0;
		 	//未登录用户机会为0
			 if(null != userId){
				 DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			      try {
			          Date dt1 = df.parse(startTime);
			          Date dt2 = df.parse(endTime);

			          String timestamp1 = String.valueOf(dt1.getTime()/1000);
			          String timestamp2 = String.valueOf(dt2.getTime()/1000);

			          //获取用户在活动期间加入的计划
			          List<Map<String, Object>> hjhPlanList = this.getHjhPlanListByUserId(userId,Integer.valueOf(timestamp1),Integer.valueOf(timestamp2),10000,null);
			          //规则1：根据用户在活动期间加入计划的情况计算获取的机会
			          for (Map<String, Object> map : hjhPlanList) {
			        	  Integer lockPeriod = 0;
			              Long accedeAccount = 0l;
			              for (Map.Entry<String, Object> entry : map.entrySet()) {
			                if ("lockPeriod".equals(entry.getKey())) {
			                	lockPeriod = (Integer) entry.getValue();
			                } else if ("accedeAccount".equals(entry.getKey())) {
			                	accedeAccount = ((java.math.BigDecimal) entry.getValue()).longValue();
			                }
			              }
			              //加入计划大于10000元才会有机会
			              String keys = getkey(accedeAccount,lockPeriod);
			              chance = chance + getChance(keys);
			          }

			          //规则2：活动期间，邀请好友注册且活动期间该好友首笔出借满5000元（计划类1月及以上标），双方各获得一次竞猜机会；
			          //计算当前用户邀请的用户加入计划获取的机会
			          List<Integer> userList = this.getspreadUser(userId,Integer.valueOf(timestamp1),Integer.valueOf(timestamp2));
			          if(null != userList && userList.size() > 0){
			        	  for (Integer usersId : userList) {
			        		  //计算该用户是否在活动期间注册切加入计划首笔出借满5000元（计划类1月及以上标）
			        		  List<Map<String, Object>> hjhPlanList1 = this.getFirstHjhPlanListByUserId(usersId,Integer.valueOf(timestamp1),Integer.valueOf(timestamp2),0,1);
			    	          if(null != hjhPlanList1 && hjhPlanList1.size() > 0){
			    	        	  Map<String, Object> result = hjhPlanList1.get(0);
			        			  long accedeAccount = ((java.math.BigDecimal) result.get("accedeAccount")).longValue();
			        			  if(accedeAccount >= 5000){			        				  
			        				  chance = chance + 1;
			        			  }
			    	          }
						}
			          }

			          //计算当前用户是否在活动期间注册切加入计划首笔出借满5000元（计划类1月及以上标）
			          Users user =  userService.getUserByUserId(userId);
			          if(null != user.getRegTime()){			        	  			        	  
			        	  
			        	  if(Integer.valueOf(timestamp2) >= user.getRegTime() && user.getRegTime() >= Integer.valueOf(timestamp1)){
			        		  
			        		  List<Map<String, Object>> hjhPlanList1 = this.getFirstHjhPlanListByUserId(userId,Integer.valueOf(timestamp1),Integer.valueOf(timestamp2),0,1);
			        		  if(null != hjhPlanList1 && hjhPlanList1.size() > 0){
			        			  Map<String, Object> result = hjhPlanList1.get(0);
			        			  long accedeAccount = ((java.math.BigDecimal) result.get("accedeAccount")).longValue();
			        			  if(accedeAccount >= 5000){			        				  
			        				  chance = chance + 1;
			        			  }
			        		  }
			        	  }
			          }


			          //计算用户已经使用了机会数
			          Integer userdChance = this.getUserdChance(userId);
			          chance = chance - userdChance;
			      } catch (Exception e) {
			    	  logger.error("判断当前时间是否再活动时间内异常！",e);
			      }
			 }
		 return chance;
	 }
	 
	 
	 public List<Map<String, Object>> getFirstHjhPlanListByUserId(Integer userId,
				Integer startTime, Integer endTime, long account, Integer flag) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId", userId);
			param.put("startTime", startTime);
			param.put("endTime", endTime);
			param.put("account", account);
			param.put("flag", flag);
			return worldCupActivityCustomizeMapper.getFirstHjhPlanListByUserId(param);
		}

	/**
	  * @author walter.limeng
	  * 根据金额获取keys
	  * @param num
	  * @return
	  */
	 public String getkey(long num,Integer lockPeriod) {
		 String keys = "";
		 String letter = num >= 100000 ? "e" : num >= 80000 ? "d" : num >= 50000 ? "c" : num >= 30000 ? "b" : num >= 10000 ? "a":"f";
		 keys = letter + lockPeriod.toString();
	     return keys;
	    }

	 /**
	  * @author walter.limeng
	  * 根据key获取机会
	  * @param key
	  * @return
	  */
	 public Integer getChance(String key){
		 Map<String, Integer> resultMap = new HashMap<>();
		 resultMap.put("a1", 2);resultMap.put("a3", 2);resultMap.put("a6", 4);resultMap.put("a12", 8);
		 resultMap.put("b1", 3);resultMap.put("b3", 3);resultMap.put("b6", 6);resultMap.put("b12", 12);
		 resultMap.put("c1", 5); resultMap.put("c3", 5);resultMap.put("c6", 10);resultMap.put("c12", 16);
		 resultMap.put("d1", 8);resultMap.put("d3", 8);resultMap.put("d6", 16);resultMap.put("d12", 16);
		 resultMap.put("e1", 16);resultMap.put("e3", 16);resultMap.put("e6", 16);resultMap.put("e12", 16);
		 Integer chance = resultMap.get(key);
		 if(null == chance){
			 chance = 0;
		 }
		 return chance;
	 }

	@Override
	public String getMyChampionLogo(Integer userId) {
			String logo = "";
			if(null != userId){		
				WorldCupTeam worldCupTeam = worldCupActivityCustomizeMapper.getChampionLogo(userId);
				logo = worldCupTeam==null?"":worldCupTeam.getTeamLogo();
			}
		return logo;
	}

	@Override
	public boolean selectIsGuessing(Integer championId) {
		boolean flag = false;
		WorldCupTeam worldCupTeam = worldCupTeamMapper.selectByPrimaryKey(championId);
		if(null != worldCupTeam.getIsEliminate() && 1 == worldCupTeam.getIsEliminate()){
			
			Map<String, Object> param = new HashMap<>();
	        param.put("guessingChampionId", worldCupTeam.getId());
	        param.put("orderBy", "desc");
	        //获取用户所投球队得比赛
	        List<WorldTeamMatch> worldTeamMatches = guessingWinningRankMapper.getMatchTeamByParam(param);//查询支持球队的对战记录

	        WorldTeamMatch worldTeamMatch = worldTeamMatches.get(0);//得到最后一场比赛
	      try {
	          	          
	            long curren = System.currentTimeMillis();
	            curren += 10 * 60 * 1000;
	            //十分钟之后的时间戳
	            String timestamp1 = String.valueOf(curren / 1000);
	            
	            if (worldTeamMatch.getMatchTime() >= Integer.parseInt(timestamp1)) {
	                flag = true;
	            }
	      } catch (Exception e) {
	    	  logger.error("判断当前时间是否再活动时间内异常！",e);
	      }
		}
		return flag;
	}

	@Override
	public Integer isGuessing(Integer userId) {
		Integer count = worldCupActivityCustomizeMapper.isGuessing(userId);
		Integer flag = 0;
		if(count > 0){
			flag = 1;
		}
		return flag;
	}
	
	@Override
	public Integer selectIsGuessing() {
		Integer flag = 0;	
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	      try {
	          Date dt1 = df.parse("2018-07-15 23:00:00");
	          
	            long curren = System.currentTimeMillis();
	            curren += 10 * 60 * 1000;
	            //十分钟之后的时间戳
	            String timestamp1 = String.valueOf(curren / 1000);
	            
	            //最后一场比赛时间错
	            String timestamp = String.valueOf(dt1.getTime()/ 1000);
	            
	            if (Integer.parseInt(timestamp) >= Integer.parseInt(timestamp1)) {
	                flag = 1;
	            }
	      } catch (Exception e) {
	    	  logger.error("判断当前时间是否再活动时间内异常！",e);
	      }
		
		return flag;
	}

	@Override
	public WorldTeamMatch getTeamMatchById(Integer guessingMatchId) {
		return worldTeamMatchMapper.selectByPrimaryKey(guessingMatchId);
	}

	@Override
	public Integer findIsOver(Integer matchType) {
		Integer flag = 1;
		Integer count = worldCupActivityCustomizeMapper.findIsOver(matchType);
		if(count > 0){
			flag = 0;
		}
		return flag;
	}
}
