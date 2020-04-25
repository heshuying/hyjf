package com.hyjf.activity.newyear.lanternfestival;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.NewyearQuestionConfig;
import com.hyjf.mybatis.model.auto.NewyearQuestionConfigExample;
import com.hyjf.mybatis.model.auto.NewyearQuestionUser;
import com.hyjf.mybatis.model.auto.NewyearQuestionUserExample;
import com.hyjf.mybatis.model.customize.apiweb.IanternFestivalCustomize;
import com.hyjf.mybatis.model.customize.apiweb.UserLanternIllumineCustomize;
@Service
public class LanternFestivalServiceImpl extends BaseServiceImpl implements LanternFestivalService{
    public static JedisPool pool = RedisUtils.getConnection();
    @Override
    public void getPresentRiddles(PresentRiddlesResultBean resultBean) {
        
        String answerTime=getAnswerTime();
        NewyearQuestionConfigExample example=new NewyearQuestionConfigExample();
        example.createCriteria().andAnswerTimeEqualTo(answerTime);
        example.setOrderByClause("answer_time DESC");
        List<NewyearQuestionConfig> newyearQuestionConfig=newyearQuestionConfigMapper.selectByExample(example);
        if(newyearQuestionConfig==null||newyearQuestionConfig.size()==0){
            
            resultBean.setIfReturnQuestion("0");
            
            NewyearQuestionConfigExample exampleConfigExample=new NewyearQuestionConfigExample();
            example.setOrderByClause("answer_time DESC");
            List<NewyearQuestionConfig> list=newyearQuestionConfigMapper.selectByExample(exampleConfigExample);
            String startTime=list.get(0).getAnswerTime();
            String endTime=list.get(list.size()-1).getAnswerTime();
            if(new Integer(startTime)>new Integer(answerTime)){
                resultBean.setLanternFestivalFlag("0");
            }else if(new Integer(answerTime)>new Integer(endTime)){
                resultBean.setLanternFestivalFlag("2");
            }
            
        } else{
            resultBean.setLanternFestivalFlag("1");
            resultBean.setIfReturnQuestion("1");
            createLanternFestival(newyearQuestionConfig.get(0),resultBean);
        }
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
    }

   

    @Override
    public void getUserPresentCumulativeCoupon(Integer userId, UserPresentCumulativeResultBean resultBean) {
        Map<String,Object> map=new HashMap<String,Object>(); 
        map.put("userId",userId);
        IanternFestivalCustomize ianternFestivalCustomize=null;
        List<IanternFestivalCustomize> list=ianternFestivalCustomizeMapper.getUserPresentCumulativeCoupon(map);
        if(list!=null&&list.size()>0){
            ianternFestivalCustomize=list.get(0);
            if(!"0".equals(ianternFestivalCustomize.getPrizeJine())){
                resultBean.setCanReceiveFlag("1");
                resultBean.setUserPresentCumulativeCoupon(ianternFestivalCustomize.getViewName());
                resultBean.setPrizeJine(ianternFestivalCustomize.getPrizeJine());
                if(ianternFestivalCustomize.getDoubleFlg()==1){
                    resultBean.setUserPresentCumulativeCouponCount(2);  
                }else{
                    resultBean.setUserPresentCumulativeCouponCount(1);
                }
                if(ianternFestivalCustomize.getQuestionNum()==7){
                    resultBean.setLastQuestion("2");
                }else{
                    resultBean.setLastQuestion("1");
                }
            }
            
            
        }else{
            resultBean.setCanReceiveFlag("0");
            resultBean.setPrizeJine("0");
            resultBean.setUserPresentCumulativeCoupon("0");
            resultBean.setUserPresentCumulativeCouponCount(0);
        }
        resultBean.setShowAnswerFlag("1");
        if(ianternFestivalCustomize!=null){
            
            Integer time = GetDate.getNowTime10();
            String userAnswerMapJSON = RedisUtils.get("userAnswerMap");
            Gson gson2 = new Gson();
            Map<String, String> userAnswerMap = gson2.fromJson(userAnswerMapJSON, new TypeToken<Map<String, String>>(){}.getType());
            String key = userId==null?"":userId+getAnswerTime();;
            // 判断用户是否已经抢过
            if(userAnswerMap!=null&&userAnswerMap.containsKey(key)){
                String answerTimeStr=userAnswerMap.get(key);
                if(ianternFestivalCustomize.getUserAnswerResult()==1&&(time-new Integer(answerTimeStr)<=120)){
                    resultBean.setShowAnswerFlag("0");
                }
           }
        }
        
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
    }
    
    
    @Override
    public void getUserLanternIllumineList(Integer userId, UserLanternIllumineResultBean resultBean) {
        List<UserLanternIllumineCustomize> userLanternIllumineList=new ArrayList<UserLanternIllumineCustomize>();
        Map<String,Object> map=new HashMap<String,Object>(); 
        map.put("userId",userId);
        userLanternIllumineList=ianternFestivalCustomizeMapper.getUserLanternIllumineList(map);
        for (UserLanternIllumineCustomize userLanternIllumineCustomize : userLanternIllumineList) {
            String answerTime=userLanternIllumineCustomize.getAnswerTime();
            userLanternIllumineCustomize.setAnswerTime(new Integer(answerTime.substring(answerTime.length()-2, answerTime.length())).toString());
        }
        
        resultBean.setUserLanternIllumineList(userLanternIllumineList);
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
    }
    
    @Override
    public synchronized int updateUserAnswerRecord(LanternFestivalBean lanternFestivalBean, UserAnswerResultBean resultBean) {
        
       NewyearQuestionConfig newyearQuestionConfig=newyearQuestionConfigMapper.selectByPrimaryKey(lanternFestivalBean.getQuestionId());
        
        
       NewyearQuestionUserExample example=new NewyearQuestionUserExample();
       example.createCriteria().andUserIdEqualTo(lanternFestivalBean.getUserId()).andCurrentExchangeEqualTo(1);
       List<NewyearQuestionUser> list=newyearQuestionUserMapper.selectByExample(example);
       int inserCount=0;
       
       
       NewyearQuestionUser newyearQuestionUser=list.get(0);
       if((!newyearQuestionConfig.getQuestionNum().equals(newyearQuestionUser.getQuestionNum()))||newyearQuestionUser.getUserAnswerResult()==1){
           newyearQuestionUser.setUserAnswer(lanternFestivalBean.getUserAnswer());
           if(newyearQuestionConfig.getQuestionAnswer().equals(lanternFestivalBean.getUserAnswer())){
               if(newyearQuestionConfig.getQuestionNum()!=7||(newyearQuestionConfig.getQuestionNum()==7&&newyearQuestionUser.getPrizeJine()==0)){
                   newyearQuestionUser.setPrizeJine(newyearQuestionUser.getPrizeJine()+10);
                   newyearQuestionUser.setViewName("价值￥"+newyearQuestionUser.getPrizeJine()+"元代金券");  
                   resultBean.setPrompt(newyearQuestionUser.getPrizeJine()+"");
                   resultBean.setCouponCount("1");
               }else{
                   newyearQuestionUser.setDoubleFlg(1);
                   resultBean.setPrompt(newyearQuestionUser.getPrizeJine()+"");
                   resultBean.setCouponCount("2");
               }
               newyearQuestionUser.setUserAnswerResult(0);
               resultBean.setIsCorrect("1");
           }else{
               resultBean.setIsCorrect("0");
           }
           inserCount=newyearQuestionUserMapper.updateByPrimaryKeySelective(newyearQuestionUser);
       }else{
           if(newyearQuestionUser.getDoubleFlg()==0){
               resultBean.setCouponCount("1");  
           }else{
               resultBean.setCouponCount("2");
           }
           resultBean.setPrompt(newyearQuestionUser.getPrizeJine()+"");
           resultBean.setIsCorrect("1");
           inserCount=1;
       }
       
        
        return inserCount;
    }
    
    
    @Override
    public synchronized int insertUserAnswerRecordInit(LanternFestivalBean lanternFestivalBean) throws RuntimeException{
        NewyearQuestionConfig newyearQuestionConfig=newyearQuestionConfigMapper.selectByPrimaryKey(lanternFestivalBean.getQuestionId());
        
        NewyearQuestionUserExample example=new NewyearQuestionUserExample();
        example.createCriteria().andUserIdEqualTo(lanternFestivalBean.getUserId()).andCurrentExchangeEqualTo(1);
        List<NewyearQuestionUser> list=newyearQuestionUserMapper.selectByExample(example);

        Jedis jedis=pool.getResource();
        
        int count=1;
        do {
            if("OK".equals(jedis.watch("userAnswerMap"))){
                String userAnswerMapJSON = RedisUtils.get("userAnswerMap");
                Gson gson1 = new Gson();
                Map<String, String> userAnswerMap = gson1.fromJson(userAnswerMapJSON, new TypeToken<Map<String, String>>(){}.getType());
                String key = lanternFestivalBean.getUserId()+newyearQuestionConfig.getAnswerTime();
                // 判断用户是否已经抢过
                if(userAnswerMap!=null&&userAnswerMap.containsKey(key)){
                    jedis.unwatch();
                    return 0;
                }
            }
            count++;
        } while (count!=3);

        int inserCount=0;
        Integer addTime = GetDate.getNowTime10();
        NewyearQuestionUser newyearQuestionUser=null;
        if(list==null||list.size()==0){
            //第一次答题
            newyearQuestionUser=new NewyearQuestionUser();
            newyearQuestionUser.setQuestionId(newyearQuestionConfig.getId());
            newyearQuestionUser.setQuestionNum(newyearQuestionConfig.getQuestionNum());
            newyearQuestionUser.setUserId(lanternFestivalBean.getUserId());
            newyearQuestionUser.setCurrentExchange(1);
            newyearQuestionUser.setUserAnswer("");
            newyearQuestionUser.setDoubleFlg(0);
            newyearQuestionUser.setViewName("暂无累计到代金券");
            newyearQuestionUser.setPrizeJine(0);
            newyearQuestionUser.setUserAnswerResult(1);
            newyearQuestionUser.setAddTime(addTime);
            inserCount=newyearQuestionUserMapper.insertSelective(newyearQuestionUser);
        }else{
           newyearQuestionUser=list.get(0);
           
           NewyearQuestionUser insertNewyearQuestionUser=new NewyearQuestionUser();
           insertNewyearQuestionUser.setQuestionId(newyearQuestionConfig.getId());
           insertNewyearQuestionUser.setQuestionNum(newyearQuestionConfig.getQuestionNum());
           insertNewyearQuestionUser.setUserId(lanternFestivalBean.getUserId());
           insertNewyearQuestionUser.setCurrentExchange(1);
           insertNewyearQuestionUser.setUserAnswer("");
           insertNewyearQuestionUser.setDoubleFlg(0);
           insertNewyearQuestionUser.setPrizeJine(newyearQuestionUser.getPrizeJine());
           insertNewyearQuestionUser.setViewName("价值￥"+newyearQuestionUser.getPrizeJine()+"元代金券");
           insertNewyearQuestionUser.setUserAnswerResult(1);
               
           insertNewyearQuestionUser.setAddTime(addTime);
           inserCount = newyearQuestionUserMapper.insertSelective(insertNewyearQuestionUser);
           if(inserCount!=0){
               newyearQuestionUser.setCurrentExchange(0);
                   newyearQuestionUserMapper.updateByPrimaryKeySelective(newyearQuestionUser);
           }
        }
        
        
        
        
        
        
        List<Object> result=null;
        System.out.println("OK".equals(jedis.watch("userAnswerMap")));
        do {
            if("OK".equals(jedis.watch("userAnswerMap"))){
                Transaction tx = jedis.multi();
                String userAnswerMapJSON = RedisUtils.get("userAnswerMap");
                Gson gson = new Gson();
                Map<String, String> userAnswerMap = gson.fromJson(userAnswerMapJSON, new TypeToken<Map<String, String>>(){}.getType());
                if(userAnswerMap==null||userAnswerMap.size()==0){
                    userAnswerMap=new HashMap<String, String>();
                }
                String key = lanternFestivalBean.getUserId()+newyearQuestionConfig.getAnswerTime();
                userAnswerMap.put(key, addTime+"");
                tx.set("userAnswerMap", JSONArray.toJSONString(userAnswerMap));
                result = tx.exec();

                if (result == null || result.isEmpty()) {
                    jedis.unwatch();
                    String userAnswerMapJSON1 = RedisUtils.get("userAnswerMap");
                    Gson gson1 = new Gson();
                    Map<String, String> userAnswerMap1 = gson1.fromJson(userAnswerMapJSON1, new TypeToken<Map<String, String>>(){}.getType());
                    if(userAnswerMap1.containsKey(key)){
                        throw new RuntimeException("并发异常,用户编号:"+lanternFestivalBean.getUserId());
                    }
                }
            }
        } while (result == null || result.isEmpty());

        return inserCount;
    }

    
    
    @Override
    public void check(LanternFestivalBean lanternFestivalBean, CheckResultBean resultBean) {
        String answerTime=getAnswerTime();
        NewyearQuestionConfigExample example=new NewyearQuestionConfigExample();
        example.createCriteria().andAnswerTimeEqualTo(answerTime);
        example.setOrderByClause("answer_time DESC");
        List<NewyearQuestionConfig> newyearQuestionConfigList=newyearQuestionConfigMapper.selectByExample(example);

        if(newyearQuestionConfigList==null||newyearQuestionConfigList.size()==0){
            
            NewyearQuestionConfigExample exampleConfigExample=new NewyearQuestionConfigExample();
            example.setOrderByClause("answer_time DESC");
            List<NewyearQuestionConfig> list=newyearQuestionConfigMapper.selectByExample(exampleConfigExample);
            String startTime=list.get(0).getAnswerTime();
            String endTime=list.get(list.size()-1).getAnswerTime();
            if(new Integer(startTime)>new Integer(answerTime)){
                resultBean.setCheckStatus("0");
                resultBean.setMessage1("活动暂未开始");
                resultBean.setMessage2("活动开始时间：2017年2月5日");
            }else if(new Integer(answerTime)>new Integer(endTime)){
                resultBean.setCheckStatus("0");
                resultBean.setMessage1("活动已结束");
                resultBean.setMessage2("活动时间：2017年2月5日至2017年2月11日");
            }
            
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            return ;
        } else{
           NewyearQuestionConfig newyearQuestionConfig=newyearQuestionConfigList.get(0);
           if(newyearQuestionConfig.getId()!=lanternFestivalBean.getQuestionId()){
               resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
               resultBean.setCheckStatus("0");
               resultBean.setMessage1("访问异常,该谜语不是今天的谜语");
               return ;
           }else{
               
               String userAnswerMapJSON = RedisUtils.get("userAnswerMap");
               Gson gson2 = new Gson();
               Map<String, String> userAnswerMap = gson2.fromJson(userAnswerMapJSON, new TypeToken<Map<String, String>>(){}.getType());
               String key = lanternFestivalBean.getUserId()+answerTime;
               // 判断用户是否已经抢过
               if(userAnswerMap!=null&&userAnswerMap.containsKey(key)){
                   resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
                   resultBean.setCheckStatus("0");
                   resultBean.setMessage1("很抱歉，今日答题已结束");
                   return ;
               }
           }
        }
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setCheckStatus("1");
    }
    
    @Override
    public void getTodayUserAnswerFlag(LanternFestivalResultBean resultBean, LanternFestivalBean lanternFestivalBean) {
        String answerTime=getAnswerTime();
        String userAnswerMapJSON = RedisUtils.get("userAnswerMap");
        Gson gson2 = new Gson();
        Map<String, String> userAnswerMap = gson2.fromJson(userAnswerMapJSON, new TypeToken<Map<String, String>>(){}.getType());
        String key = lanternFestivalBean.getUserId()+answerTime;
        // 判断用户是否已经抢过
        if(userAnswerMap!=null&&userAnswerMap.containsKey(key)){
            resultBean.setUserAnswerFlag("0");
        }else{
            resultBean.setUserAnswerFlag("1");
        }
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
    }
    
    private void createLanternFestival(NewyearQuestionConfig newyearQuestionConfig, PresentRiddlesResultBean resultBean) {
        resultBean.setQuestionId(newyearQuestionConfig.getId()+"");
        resultBean.setQuestionNum(newyearQuestionConfig.getQuestionNum()+"");
        resultBean.setQuestionContent(newyearQuestionConfig.getQuestionContent());
        resultBean.setQuestionAnswer(newyearQuestionConfig.getQuestionAnswer());
        resultBean.setQuestionHint(newyearQuestionConfig.getQuestionHint());
        resultBean.setAnswerTime(newyearQuestionConfig.getAnswerTime());
        resultBean.setQuestionImageName(newyearQuestionConfig.getQuestionImageName());
    }

    public String getAnswerTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        
        return sdf.format(new Date());
        
    }





}
