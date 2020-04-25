package com.hyjf.wechat.controller.activity.worldCup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.GuessingWinning;
import com.hyjf.mybatis.model.auto.WorldTeamMatch;
import com.hyjf.wechat.annotation.SignValidate;
import com.hyjf.wechat.base.BaseController;
import com.hyjf.wechat.service.regist.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author limeng
 * @version WorldCupController, v0.1 2018/6/13 16:16
 */
@RequestMapping(WorldCupDefine.REQUEST_MAPPING)
@RestController
public class WorldCupController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(WorldCupController.class);
    //活动开始时间
    public static final String startTime = "2018-06-29 00:00:00";
    //活动结束时间
    public static final String endTime = "2018-07-15 23:59:59";
    @Autowired
    private WorldCupService worldCupService;
    @Autowired
    private UserService userService;

    /**
     * 测试方法
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "helllo", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    private String Hello(HttpServletRequest request, @RequestBody Map<String, Object> params) {
//    	String param = (String) params.get("param");
    	//先把字符串转成JSONArray
//    	JSONArray jsonArray = JSONArray.parseArray(param);
//    	String message = "";
//    	for (int i = 0; i < jsonArray.size(); i++) {
//    		jsonArray.get(i);
//			System.out.println(jsonArray.get(i));
//		}	
    	//再把jsonArray转成对象集合   	
//    	List < Users > list = (List) JSONArray.parseArray(param, Users.class);
        return "";
    }

    /**
     * @author lisheng
     * 竞猜输赢活动页
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WorldCupDefine.GUESS_PATH, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    private JSONObject activity(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) {
        JSONObject result = new JSONObject();
		Integer matchType = (Integer) params.get("matchType");
        if (matchType==null) {
            result.put("status", "11");
            result.put("statusDesc", "参数校验失败");
            return result;
        }
        //竞猜次数 未登录用户为0
        Integer userId = requestUtil.getRequestUserId(request);
        if (null == userId) {
            result.put("guessingChance", 0);
            result.put("isGuess", 0);
        } else {//已登录用户机会查询
            Integer gessCount = worldCupService.hasChance(Integer.valueOf(userId));
            result.put("guessingChance", gessCount);
            result.put("isGuess", worldCupService.isGuessing(userId));
        }

        if (matchType==0) {// 查询当前进行
            Integer matchType1 = worldCupService.findMatchType();//查询当前的比赛类型
            if(null == matchType1){
            	matchType1 = 5;
            }
            List<Map<String, Object>> matchList = worldCupService.selectTeam(matchType1);
            Integer isSubmit = worldCupService.findIsOver(matchType1);//查询当前的比赛类型
            result.put("isSubmit", isSubmit);
            result.put("matchList", matchList);
        } else {//根据传入比赛类型查询
            List<Map<String, Object>> matchList = worldCupService.selectTeam(matchType);
            result.put("matchList", matchList);
            Integer isSubmit = worldCupService.findIsOver(matchType);//查询当前的比赛类型
            result.put("isSubmit", isSubmit);
        }
        if(!isStart(new Date())){
        	result.put("isSubGuess", 0);
        }else{
        	result.put("isSubGuess", 1);        	
        }
        result.put("status", "000");
        result.put("statusDesc", "查询成功");
        return result;
    }


    /**
     * @author lisheng
     * 查看我的竞猜记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WorldCupDefine.FIND_MYRECORD, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    private JSONObject findMyRecord(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        List<Map<String, String>> guessingList = new ArrayList<>();
        //竞猜次数 未登录用户为0
        Integer userId = requestUtil.getRequestUserId(request);
        if (null == userId) {
            result.put("status", "999");
            result.put("statusDesc", "用户未登陆");
            result.put("guessingList", guessingList);
            return result;
        }
        guessingList = worldCupService.searchMyRecord(userId+"");
        result.put("guessingList", guessingList);
        result.put("status", "000");
        result.put("statusDesc", "查询成功");
        return result;
    }


    /**
     * @author lisheng
     * 查看我的竞猜机会
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WorldCupDefine.FIND_MYCHANCE, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    private JSONObject findMyChance(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        //竞猜次数 未登录用户为0
        Integer userId = requestUtil.getRequestUserId(request);;
        if (null == userId) {
            result.put("status", "999");
            result.put("statusDesc", "用户未登陆");
            return result;
        }
        List<Map<String, Object>> tenderList = worldCupService.searchMyChance(userId+"");
        result.put("tenderList", tenderList);
        result.put("status", "000");
        result.put("statusDesc", "查询成功");
        result.put("allChance", worldCupService.hasChance(userId));
        return result;
    }

    /**
     * @author lisheng
     * 查看竞猜排行榜
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WorldCupDefine.FIND_RANKLIST, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    private JSONObject rankList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
		List<GuessingWinning>  GuessResultList= worldCupService.searchRankList();
        result.put("GuessResultList", GuessResultList);
        result.put("status", "000");
        result.put("statusDesc", "查询成功");
        return result;
    }

    /**
     * @author lisheng
     * 竞猜冠军列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WorldCupDefine.FIND_CHAMPION_LIST, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    private JSONObject championList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> params) {
        JSONObject result = new JSONObject();
        List<Map<String, Object>> GuessResultList = new ArrayList<>();
        Integer currentPage = (Integer) params.get("currentPage");
        Integer offset = (Integer) params.get("pageSize");
        if(null == currentPage){
        	currentPage = 1;
        }
        if(null == offset){
        	offset = 32;
        }
        Integer limitStart = (currentPage - 1) * offset;
        Integer userId = requestUtil.getRequestUserId(request);
        if (null == userId) {//用户未登录
            GuessResultList = worldCupService.searchChampionListAll(limitStart, offset);
            result.put("isGuess", 1);//未参与竞猜
        } else {
            if (worldCupService.checkJoinChampion(userId+"")) {//用户参与了竞猜
                GuessResultList = worldCupService.searchChampionList(userId+"", limitStart, offset);
                String userTitle = worldCupService.findUserTitle(userId+"");
                result.put("guessSlogan", userTitle);
                result.put("isGuess", 0);//参与了竞猜
            } else {//用户未参与竞猜
                GuessResultList = worldCupService.searchChampionListAll(limitStart, offset);
                result.put("isGuess", 1);//未参与竞猜
            }
        }
		Object endPage=0;
        if(GuessResultList!=null&& GuessResultList.size() >0){
			Map<String, Object> stringStringMap = GuessResultList.get(0);
			endPage = stringStringMap.get("endPage");
		}
        
        Integer isSubGuess = worldCupService.selectIsGuessing();
        if(GuessResultList.size() > 0){  	
        	GuessResultList.remove(0);
        }
        //处理我的冠军球队LOGO
        String myChampionLogo = worldCupService.getMyChampionLogo(userId);
        if(GuessResultList.size() == 0){        	
        	result.put("isSubGuess", 0);
        }else{        	
        	result.put("isSubGuess", isSubGuess);
        }
        if(!isStart(new Date())){
        	result.put("isSubGuess", 0);
        }
        result.put("myChampionLogo", myChampionLogo);
        result.put("guessChampion", GuessResultList);
        result.put("status", "000");
        result.put("statusDesc", "查询成功");
        result.put("endPage", endPage);
        result.put("currentPage", currentPage);
        return result;
    }


    /**
     * 用户参与竞猜冠军
     * @author walter.limeng
     * @param request
     * @return
     */
    @SignValidate
    @ResponseBody
    @RequestMapping(value = WorldCupDefine.REQUEST_CHAMPION, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public JSONObject participateChampion(HttpServletRequest request,@RequestBody Map<String, Object> params) {
		JSONObject jsonObject = new JSONObject();
		Integer userId = requestUtil.getRequestUserId(request);
		if(isStart(new Date())){
			Integer championId = null;
			try {
				//支持冠军球队ID
				championId = (Integer) params.get("championId");
			} catch (Exception e) {
				logger.error("参数异常，params："+params,e);
				jsonObject.put("status", "11");
				jsonObject.put("statusDesc", "参数异常！");
				return jsonObject;
			}
			if(null != championId){
				//判断当前用户是否已经选择支持的冠军球队
				boolean flag = worldCupService.selectIsChampion(userId);
				if(!flag){
					//判断当前用户是否可以参与竞猜本次冠军球队
					boolean isGuess = worldCupService.selectIsGuessing(championId);					
					if(isGuess){						
						jsonObject = worldCupService.participateChampion(userId,championId);
					}else{						
						jsonObject.put("status", "11");
						jsonObject.put("statusDesc", "比赛已开始，停止投票！");
					}
				}else{
					jsonObject.put("status", "11");
					jsonObject.put("statusDesc", "请勿重复选择！");
				}
			}else{
				jsonObject.put("status", "11");
				jsonObject.put("statusDesc", "请您先选择您的冠军球队！");
			}
		}else{
			jsonObject.put("status", "11");
			jsonObject.put("statusDesc", "活动暂未开始，不能参与！");
		}

		return jsonObject;
	}


	/**
	 * 判断当前用户参与时间是否在活动时间内
	 * @author walter.limeng
	 * @return
	 */
	 public Boolean isStart(Date data){
			boolean flag = false;
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	      try {
	          Date dt1 = df.parse(startTime);
	          Date dt2 = df.parse(endTime);
	          long nowDate = data.getTime();

	          if (dt2.getTime() > nowDate && nowDate > dt1.getTime()) {
	              flag = true;
	          }
	      } catch (Exception e) {
	    	  logger.error("判断当前时间是否再活动时间内异常！",e);
	      }
			return flag;
		}
	 
		
		/**
		 * @author walter.limeng
		 * 用户竞猜输赢
		 * @param request
		 * @return
		 */
		@SignValidate
		@ResponseBody
	    @RequestMapping(value = WorldCupDefine.REQUEST_PARTICIPATE, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	    public JSONObject participateWin(HttpServletRequest request,@RequestBody Map<String,Object> params) {
			JSONObject jsonObject = new JSONObject();
			Integer userId = requestUtil.getRequestUserId(request);
//			Integer userId = 4714;
			List < WorldCupRequestBean > list = new ArrayList<>();
			//判断活动是否开始
			if(isStart(new Date())){
				String guessMatchList = (String) params.get("guessMatchList");   	
		    	//再把String转成对象集合
		    	
				try {
					list = (List< WorldCupRequestBean >) JSONArray.parseArray(guessMatchList, WorldCupRequestBean.class);
				} catch (Exception e) {
					logger.error("参数异常，params："+params,e);
					jsonObject.put("status", "11");
					jsonObject.put("statusDesc", "参数异常！");
					jsonObject.put("chances", worldCupService.hasChance(userId));			
					return jsonObject;
				}
				
				//校验所选场次是否可以进行投注
				jsonObject = validateMatch(list, userId);
				if("11".equals(jsonObject.getString("status"))){
					jsonObject.put("chances", worldCupService.hasChance(userId));			
					return jsonObject;					
				}
			}else{
				jsonObject.put("status", "11");
				jsonObject.put("statusDesc", "活动暂未开始，不能参与！");
				jsonObject.put("chances", worldCupService.hasChance(userId));			
				return jsonObject;
			}
			
			for (WorldCupRequestBean worldCupRequestBean : list) {
				worldCupService.saveGuessMatch(userId,worldCupRequestBean.getGuessingMatchId()+"",worldCupRequestBean.getMatchTeamId()+"");
			}
			jsonObject.put("status", "000");
			jsonObject.put("statusDesc", "恭喜您，参与成功！");			
			jsonObject.put("chances", worldCupService.hasChance(userId));			
			return jsonObject;
		}
		
		/**
		 * 判断用户竞猜校验
		 * @param list 用户
		 * @param userId
		 * @return
		 */
		public JSONObject validateMatch(List < WorldCupRequestBean > list,Integer userId){
			JSONObject jsonObject = new JSONObject();
			if(list.size() > 0){
				//判断用户是否还有机会参与
				if(worldCupService.hasChance(userId) >= list.size()){
					for (WorldCupRequestBean worldCupRequestBean : list) {
						Integer guessingMatchId = worldCupRequestBean.getGuessingMatchId();
						Integer matchTeamId = worldCupRequestBean.getMatchTeamId();
						if(null != guessingMatchId){
							if(null != matchTeamId){
									//判断用户是否已经参与本场比赛
									boolean isHasChance = worldCupService.getMatchChance(userId,guessingMatchId+"");
									if(isHasChance){
										WorldTeamMatch worldTeamMatch = worldCupService.getTeamMatchById(guessingMatchId);
										if(null != worldTeamMatch.getHomeMatchTeam()&& null != worldTeamMatch.getVisitingMatchTeam()){											
											//判断比赛开始时间
											boolean isMatch = worldCupService.getMatchTime(guessingMatchId+"");
											if(!isMatch){
												jsonObject.put("status", "11");
												jsonObject.put("statusDesc", "该场比赛已停止竞猜！");			
												return jsonObject;
											}
										}else{
											jsonObject.put("status", "11");
											jsonObject.put("statusDesc", "竞猜暂时开始！");			
											return jsonObject;
										}
									}else{
										jsonObject.put("status", "11");
										jsonObject.put("statusDesc", "已经选择该场比赛！");			
										return jsonObject;
									}
							}else{
								jsonObject.put("status", "11");
								jsonObject.put("statusDesc", "请选择获胜球队！");			
								return jsonObject;
							}
						}else{
							jsonObject.put("status", "11");
							jsonObject.put("statusDesc", "请选择竞猜场次！");			
							return jsonObject;
						}
					}
					jsonObject.put("status", "000");
					jsonObject.put("statusDesc", "恭喜您，可以参与！");	
					return jsonObject;
				}else{
					jsonObject.put("status", "11");
					jsonObject.put("statusDesc", "当前竞猜机会不足！");			
					return jsonObject;
				}
			}else{
				jsonObject.put("status", "11");
				jsonObject.put("statusDesc", "请选择竞猜场次！");
				return jsonObject;
			}
		}
}
