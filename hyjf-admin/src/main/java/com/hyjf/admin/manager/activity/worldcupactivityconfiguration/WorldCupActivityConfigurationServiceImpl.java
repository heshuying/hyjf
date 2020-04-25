package com.hyjf.admin.manager.activity.worldcupactivityconfiguration;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.mapper.auto.WorldCupTeamMapper;
import com.hyjf.mybatis.mapper.auto.WorldTeamMatchMapper;
import com.hyjf.mybatis.model.auto.WorldCupTeam;
import com.hyjf.mybatis.model.auto.WorldTeamMatch;
import com.hyjf.mybatis.model.auto.WorldTeamMatchExample;
import com.hyjf.mybatis.model.customize.worldcup.WorldTeamCustomize;
import com.hyjf.mybatis.model.customize.worldcup.WorldTeamMatchCustomize;

/**
 * @author xiehuili on 2018/6/14.
 * 世界杯活动配置
 */
@Service
public class WorldCupActivityConfigurationServiceImpl extends BaseServiceImpl implements WorldCupActivityConfigurationService {

	@Autowired
	private WorldTeamMatchMapper worldTeamMatchMapper;

	@Autowired
	private WorldCupTeamMapper worldCupTeamMapper;
    /**
     * 获取世界杯活动配置球队
     *
     * @return
     */
    @Override
    public List<WorldCupTeam> getWorldCupTeamSelect(){
        return worldCupActivityCustomizeMapper.getWorldCupTeamSelect();
    }

	/**
	 * 查询已经选择的球队对阵
	 *
	 * @return
	 */
	@Override
	public List<WorldTeamCustomize> getWorldCupTeamSelected(){
		return worldCupActivityCustomizeMapper.getWorldCupTeamSelected();
	}
	/**
	 * 判断16进8球队结果是否已经有，
	 */
	@Override
	public Integer getWorldCupTeamResultCount(){
		return worldCupActivityCustomizeMapper.getWorldCupTeamResultCount();
	}
    /**
     * 获取世界杯活动决战赛比赛配置
     *
     * @return
     */
    @Override
    public  List<WorldTeamMatchCustomize> getWorldTeamMatchList(){
        return worldCupActivityCustomizeMapper.getWorldTeamMatchList();

    }


    /**
     * 决战赛比赛配置提交
     * @param request
     */
    @Override
    public JSONObject updateWorldCupMatch(HttpServletRequest request) {
    	JSONObject json = new JSONObject();
    	//对阵ID
        Integer id = Integer.valueOf(request.getParameter("id"));
        //比赛类型
        Integer matchType = Integer.valueOf(request.getParameter("matchType"));
        //比赛结果
        String matchResult =request.getParameter("matchResult");
        //胜队ID
        Integer winTeamId = Integer.valueOf(request.getParameter("winTeamId"));
//        Integer id = 261 ;
//        Integer matchType = 1;
//        Integer winTeamId = 7;
//        String matchResult = "阿根廷";
        if(null != id){
        	if(null != winTeamId){
            	WorldTeamMatch worldTeamMatch = worldTeamMatchMapper.selectByPrimaryKey(id);
            	if(null == matchType){
            		matchType = worldTeamMatch.getMatchType();
            	}
            	if(null == worldTeamMatch.getWinTeamId()){
            		try {
            			worldTeamMatch.setMatchResult(matchResult);
                		worldTeamMatch.setWinTeamId(winTeamId);
                		worldTeamMatchMapper.updateByPrimaryKey(worldTeamMatch);
                		if(3 != matchType){
                			//更新球队表，设置失败的球队状态为淘汰
                			if(4 == matchType || 5 == matchType){
                				worldCupActivityCustomizeMapper.updateIsEliminateById(worldTeamMatch.getVisitingMatchTeam());
                				worldCupActivityCustomizeMapper.updateIsEliminateById(worldTeamMatch.getHomeMatchTeam());            			
                			}else{                				
                				if(winTeamId == worldTeamMatch.getHomeMatchTeam()){
                					worldCupActivityCustomizeMapper.updateIsEliminateById(worldTeamMatch.getVisitingMatchTeam());
                				}else{
                					worldCupActivityCustomizeMapper.updateIsEliminateById(worldTeamMatch.getHomeMatchTeam());            			
                				}  			
                			}
                		}
                		
                		//根据胜队ID和对阵ID更新用户竞猜输赢数据
                		Map<String, Object> param = new HashMap<String, Object>();
                		param.put("guessingMatchId", id);
                		param.put("userMatchTeamId", winTeamId);
                		//更新猜中用户状态
                		worldCupActivityCustomizeMapper.updateGuessWin(param);
                		//更新猜中用户猜中场数
                		worldCupActivityCustomizeMapper.updateGuessWinNum(param);
                		//更新未猜中用户状态
                		worldCupActivityCustomizeMapper.updateGuessLose(param);
                		//更新猜中竞猜冠军用户球队胜场数
                		worldCupActivityCustomizeMapper.updateChampion(winTeamId);

                		//处理下一期对阵情况
                		if(matchType < 4){
                			
                			dealTeamMatch(worldTeamMatch, winTeamId, matchType);
                		}
                		json.put("status", "000");
                		json.put("statusDesc", "设置成功！");
					} catch (Exception e) {
						json.put("status", "11");
	            		json.put("statusDesc", "本场结果设置异常，请联系管理员！");
					}
            	}else{
            		
            		json.put("status", "11");
            		json.put("statusDesc", "本场结果已经设置，请勿重复设置！");
            	}
            }else{
            	json.put("status", "11");
            	json.put("statusDesc", "胜队ID为空！");
            }
        }else{
        	json.put("status", "11");
        	json.put("statusDesc", "对阵ID为空！");
        }
        return json;
    }

    /**
     * 处理下一期对阵
     * @param worldTeamMatch 本次对阵
     * @param winTeamId 胜对ID
     * @param matchType 比赛类型
     */
    public void dealTeamMatch(WorldTeamMatch worldTeamMatch,Integer winTeamId,Integer matchType){
    	WorldTeamMatch nextworldTeamMatch = null;
    	WorldTeamMatch raworldTeamMatch = null;

    	Integer matchTeam = worldTeamMatch.getMatchTeam();
    	if(matchType == 1){
    		nextworldTeamMatch = new WorldTeamMatch();
    		//处理1/8决赛情况
    		if(matchTeam % 2 == 1){
    			//处理新增新的1/4对阵
    				
    			saveOrUpdateTeamMatchs(nextworldTeamMatch, "nextTeam"+(matchTeam/2+1), matchTeam/2+1, winTeamId, matchType);

    		}else{
    			saveOrUpdateTeamMatchs(nextworldTeamMatch, "nextTeam"+matchTeam/2, matchTeam/2, winTeamId, matchType);
    				
    		}
    	}else if (matchType == 2) {
    		nextworldTeamMatch = new WorldTeamMatch();
    		//处理1/4决赛情况
    		nextworldTeamMatch.setMatchType(3);
    		if(matchTeam % 2 == 1){
    			//处理新增新的1/2对阵
				
    			saveOrUpdateTeamMatchs(nextworldTeamMatch, "semifinals1", 1, winTeamId, matchType);
    		}else{
    			//处理更新新的1/2对阵
    			saveOrUpdateTeamMatchs(nextworldTeamMatch, "semifinals2", 2, winTeamId, matchType);
    		}

		}else if (matchType == 3) {
				//处理新增季军赛
				Integer loseTeamId = null;
				if(winTeamId != worldTeamMatch.getHomeMatchTeam()){
					loseTeamId = worldTeamMatch.getHomeMatchTeam();
				}else{
					loseTeamId = worldTeamMatch.getVisitingMatchTeam();
				}
				saveOrUpdateTeamMatch(raworldTeamMatch, matchTeam, loseTeamId, 4, "seasonArmy");
    			//处理新增总决赛对阵
				saveOrUpdateTeamMatch(nextworldTeamMatch, matchTeam, winTeamId, 5, "champion");

		}

    }
    
    /**
     * 处理下一期对阵数据
     * @param nextworldTeamMatch 对象
     * @param keys 获取对阵时间主键
     * @param matchTeam 下一对阵小组
     * @param winTeamId 下一对阵战队
     * @param matchType 对阵类型
     */
    public void saveOrUpdateTeamMatchs(WorldTeamMatch nextworldTeamMatch,
    		String keys,Integer matchTeam,Integer winTeamId,Integer matchType){
    	Map<String, Object> param = new HashMap<>();
    	param.put("matchTeam", matchTeam);
		param.put("matchType", matchType + 1);
		nextworldTeamMatch = worldCupActivityCustomizeMapper.getWorldTeam(param);
		if(null != nextworldTeamMatch){
			nextworldTeamMatch = updateTeamMatch(nextworldTeamMatch, winTeamId);
			worldTeamMatchMapper.updateByPrimaryKey(nextworldTeamMatch);
			
		}else{    				
			nextworldTeamMatch = new WorldTeamMatch();
			nextworldTeamMatch = setTeamMatch(nextworldTeamMatch, keys, matchTeam, winTeamId);
			nextworldTeamMatch.setMatchType(matchType + 1);
			WorldCupTeam worldCupTeam = worldCupTeamMapper.selectByPrimaryKey(winTeamId);
			nextworldTeamMatch.setBatchName(worldCupTeam.getTeamName());
			worldTeamMatchMapper.insert(nextworldTeamMatch);
		}
    }
    

    /**
     * 处理季军赛,总决赛
     * @param nextworldTeamMatch
     * @param matchTeam 比赛小组
     * @param winTeamId 球队ID
     * @param matchType 比赛类型（4：季军赛；5：总决赛）
     * @param keys
     */
    public void saveOrUpdateTeamMatch(WorldTeamMatch nextworldTeamMatch,Integer matchTeam,Integer winTeamId,Integer matchType,
    		String keys){

		Map<String, Object> param = new HashMap<>();
    	param.put("matchTeam", 1);
		param.put("matchType", matchType);
		nextworldTeamMatch = worldCupActivityCustomizeMapper.getWorldTeam(param);
		if(nextworldTeamMatch != null){
			nextworldTeamMatch.setVisitingMatchTeam(winTeamId);
			WorldCupTeam worldCupTeam = worldCupTeamMapper.selectByPrimaryKey(nextworldTeamMatch.getHomeMatchTeam());
			String name = worldCupTeam.getTeamName();
			worldCupTeam = worldCupTeamMapper.selectByPrimaryKey(winTeamId);
			nextworldTeamMatch.setBatchName(name + "vs" + worldCupTeam.getTeamName());
			worldTeamMatchMapper.updateByPrimaryKey(nextworldTeamMatch);
		}else{
			nextworldTeamMatch = new WorldTeamMatch();
			nextworldTeamMatch.setMatchType(matchType);
			nextworldTeamMatch.setMatchTeam(1);
			nextworldTeamMatch.setHomeMatchTeam(winTeamId);
			nextworldTeamMatch.setMatchTime(getTimes(keys));
			WorldCupTeam worldCupTeam = worldCupTeamMapper.selectByPrimaryKey(winTeamId);
			nextworldTeamMatch.setBatchName(worldCupTeam.getTeamName());
			nextworldTeamMatch.setCreateTime(getSecondTimestampTwo(new Date()));
			worldTeamMatchMapper.insert(nextworldTeamMatch);
		}
    }

    /**
     * 更新下一期对阵比赛
     * @param nextworldTeamMatch 球队对阵
     * @param matchTeam 比赛小组
     * @param winTeamId 客队ID
     * @return
     */
    public WorldTeamMatch updateTeamMatch(WorldTeamMatch nextworldTeamMatch,Integer winTeamId){
		nextworldTeamMatch.setVisitingMatchTeam(winTeamId);
		WorldCupTeam worldCupTeam = worldCupTeamMapper.selectByPrimaryKey(nextworldTeamMatch.getHomeMatchTeam());
		String name = worldCupTeam.getTeamName();
		worldCupTeam = worldCupTeamMapper.selectByPrimaryKey(winTeamId);
		nextworldTeamMatch.setBatchName(name + "vs" + worldCupTeam.getTeamName());
		return nextworldTeamMatch;
    }

    /**
     * 设置下一期对阵
     * @param nextworldTeamMatch 球队对阵
     * @param keys keys
     * @param matchTeam 对阵小组
     * @param winTeamId 主队ID
     * @return WorldTeamMatch
     */
    public WorldTeamMatch setTeamMatch(WorldTeamMatch nextworldTeamMatch,String keys,Integer matchTeam,Integer winTeamId){
    	nextworldTeamMatch.setMatchTeam(matchTeam);
		nextworldTeamMatch.setHomeMatchTeam(winTeamId);
		nextworldTeamMatch.setMatchTime(getTimes(keys));
		nextworldTeamMatch.setCreateTime(getSecondTimestampTwo(new Date()));
		return nextworldTeamMatch;
    }

    /**
     * 根据keys获取下一期比赛时间戳
     * @param keys
     * @return
     */
    public Integer getTimes(String keys){
    	Map<String, Object> param = new HashMap<>();
    	param.put("nextTeam1", "2018-07-06 22:00:00");param.put("nextTeam2", "2018-07-07 22:00:00");
    	param.put("nextTeam3", "2018-07-07 02:00:00");param.put("nextTeam4", "2018-07-08 02:00:00");
    	param.put("semifinals1", "2018-07-11 02:00:00");param.put("semifinals2", "2018-07-12 02:00:00");
    	param.put("seasonArmy", "2018-07-14 22:00:00");param.put("champion", "2018-07-15 23:00:00");
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    	try {
			return getSecondTimestampTwo(df.parse(param.get(keys)+""));
		} catch (ParseException e) {
			return null;
		}
    }

    /**
     * 获取精确到秒的时间戳
     * @param date
     * @return
     */
  public static int getSecondTimestampTwo(Date date){
      if (null == date) {
          return 0;
      }
      String timestamp = String.valueOf(date.getTime()/1000);
      return Integer.valueOf(timestamp);
  }
	/**
	 * 决战赛比赛配置校验球队是否唯一选中 @RequestMapping值
	 */
	@Override
	public JSONObject validationWorldCupTeam(List<WorldTeamCustomize> list){
		JSONObject json = new JSONObject();
		List<String> res =new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			int home = list.get(i).getHomeTeamGroupings();
			for (int j = 0; j < list.size(); j++) {
				int visi = list.get(j).getVisitedTeamGroupings();
				if(home == visi){
					Integer homeId = list.get(i).getHomeId();
					Integer visiId = list.get(j).getVisitedId();
					if(homeId !=null && visiId!=null&& homeId.intValue()==visiId.intValue()){
						res.add(list.get(i).getHomeName().trim()+" ");
					}
				}
			}
			json.put("error",res);
		}
		return json;
	}
    /**
     * 决战赛球队配置提交
     */
    @Override
    public void updateWorldCupTeam(List<WorldTeamCustomize> list) {
    	List<WorldCupTeam> listTeam = new ArrayList<>();
		List<WorldTeamMatch> listMatch = new ArrayList<>();
		for(int i=0;i<list.size();i++){
			WorldCupTeam worldCupTeam1 =new WorldCupTeam();
			WorldCupTeam worldCupTeam2 =new WorldCupTeam();
			WorldTeamMatch worldTeamMatch= new WorldTeamMatch();
			worldCupTeam1.setId(list.get(i).getHomeId());
			worldCupTeam1.setTeamLogo(list.get(i).getHomeLogo());
			worldCupTeam2.setId(list.get(i).getVisitedId());
			worldCupTeam2.setTeamLogo(list.get(i).getVisitedLogo());
			//设置球队对阵参数
			worldTeamMatch.setMatchTeam(i+1);
			worldTeamMatch.setMatchType(1);
			//设置比赛时间
			if(i==0){
				worldTeamMatch.setMatchTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-06-30 22:00:00"));
			}
			if(i == 1){
				worldTeamMatch.setMatchTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-07-01 02:00:00"));
			}
			
			if(i ==2){
				worldTeamMatch.setMatchTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-07-03 22:00:00"));
			}
			if(i== 3){
				worldTeamMatch.setMatchTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-07-04 02:00:00"));
			}
			
			if(i == 4){
				worldTeamMatch.setMatchTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-07-02 22:00:00"));
			}
			if(i ==5){
				worldTeamMatch.setMatchTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-07-03 02:00:00"));
			}
			
			if(i ==6){
				worldTeamMatch.setMatchTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-07-01 22:00:00"));
			}
			if(i ==7){
				worldTeamMatch.setMatchTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2("2018-07-02 02:00:00"));
			}
			worldTeamMatch.setHomeMatchTeam(list.get(i).getHomeId());
			worldTeamMatch.setVisitingMatchTeam(list.get(i).getVisitedId());
			worldTeamMatch.setBatchName(list.get(i).getHomeName().trim()+"vs"+list.get(i).getVisitedName().trim());
			listTeam.add(worldCupTeam1);
			listTeam.add(worldCupTeam2);
			listMatch.add(worldTeamMatch);
		}
		//修改世界杯16强球队
		for(int i=0;i<listTeam.size();i++){
			worldCupTeamMapper.updateByPrimaryKeySelective(listTeam.get(i));
		}
		//16进8比赛世界杯球对对阵图
		WorldTeamMatchExample exe = new WorldTeamMatchExample();
		exe.createCriteria().andMatchTypeEqualTo(1);
		List<WorldTeamMatch> selectMatch2 = worldTeamMatchMapper.selectByExample(exe);
		if(selectMatch2.size()>0){
			//16进8比赛已经存在，进行修改
			for(int i=0;i<selectMatch2.size();i++){
				//设置id
				listMatch.get(i).setId(selectMatch2.get(i).getId());
			}
			for(int i=0;i<listMatch.size();i++){
				//16进8比赛不存在，进行新增
				worldTeamMatchMapper.updateByPrimaryKeySelective(listMatch.get(i));
			}
		}else{
			for(int i=0;i<listMatch.size();i++){
				//16进8比赛不存在，进行新增
				worldTeamMatchMapper.insertSelective(listMatch.get(i));
			}
		}
    }

    /**
     * 资料上传
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart(request);
		String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
		String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
		String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.temp.path"));

		String logoRealPathDir = filePhysicalPath + fileUploadTempPath;

		File logoSaveFile = new File(logoRealPathDir);
		if (!logoSaveFile.exists()) {
			logoSaveFile.mkdirs();
		}

		BorrowCommonImage fileMeta = null;
		LinkedList<BorrowCommonImage> files = new LinkedList<BorrowCommonImage>();

		Iterator<String> itr = multipartRequest.getFileNames();
		MultipartFile multipartFile = null;

		while (itr.hasNext()) {
			multipartFile = multipartRequest.getFile(itr.next());
			String fileRealName = String.valueOf(new Date().getTime());
			String originalFilename = multipartFile.getOriginalFilename();
			fileRealName = fileRealName + UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());

			// 文件大小
			String errorMessage = null;
			try {
				errorMessage = UploadFileUtils.upload4Stream(fileRealName, logoRealPathDir, multipartFile.getInputStream(), 5000000L);
			} catch (Exception e) {
				e.printStackTrace();
			}

			fileMeta = new BorrowCommonImage();
			int index = originalFilename.lastIndexOf(".");
			if (index != -1) {
				fileMeta.setImageName(originalFilename.substring(0, index));
			} else {
				fileMeta.setImageName(originalFilename);
			}

			fileMeta.setImageRealName(fileRealName);
			fileMeta.setImageSize(multipartFile.getSize() / 1024 + "");// KB
			fileMeta.setImageType(multipartFile.getContentType());
			fileMeta.setErrorMessage(errorMessage);
			// 获取文件路径
			fileMeta.setImagePath(fileDomainUrl+fileUploadTempPath + fileRealName);
			fileMeta.setImageSrc(fileDomainUrl + fileUploadTempPath + fileRealName);
			files.add(fileMeta);

		}
		return JSONObject.toJSONString(files, true);
	}

}
