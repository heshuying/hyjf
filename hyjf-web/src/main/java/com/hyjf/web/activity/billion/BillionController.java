package com.hyjf.web.activity.billion;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ActivityBillionOne;
import com.hyjf.mybatis.model.auto.ActivityBillionSecond;
import com.hyjf.mybatis.model.auto.ActivityBillionSecondTime;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.billion.ActivityBillionThirdCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.util.WebUtils;

@Controller
@RequestMapping(value = BillionDefine.REQUEST_MAPPING)
public class BillionController extends BaseController {
    @Autowired
    private BillionService billionService;
    
    /**
     * 满心满亿活动
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = BillionDefine.INIT_ACTION, method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(BillionDefine.THIS_CLASS, BillionDefine.INIT_ACTION);
        ModelAndView modelAndView = new ModelAndView(BillionDefine.INIT_PATH);
        /**
         * 活动一
         */
        //出借总额
        BigDecimal amount = this.billionService.getTendSum();
        modelAndView.addObject("amount", amount.setScale(0, BigDecimal.ROUND_DOWN));
        //获取session用户
        Integer sessionUserId = WebUtils.getUserId(request);
        modelAndView.addObject("sessionUserId", sessionUserId);
        //奖励记录
        List<ActivityBillionOne> billionOneRecords = this.billionService.getBillionOneRecords();
        modelAndView.addObject("billionOneRecords", billionOneRecords);
        /**
         * 活动二
         */
        //活动二时间点记录
        List<ActivityBillionSecondTime> billionSecondTimeRecords = this.billionService.getBillionSecondTimeRecords();
        //页面返回bean
        BillionSecondTimeBean billionSecondTimeBean = new BillionSecondTimeBean();
        int isEnd = 1; //1结束 0未结束
        boolean flag = true;
        int  stage = 0;
        int minStage = 0;
        for(int i = 0;i < billionSecondTimeRecords.size();i++){
        	ActivityBillionSecondTime timeRecord = billionSecondTimeRecords.get(i);
        	if(timeRecord.getAccordTime() == null){
        		timeRecord.setAccordTime(0);
        		isEnd = 0;
        	}
        	//赋值
        	if(timeRecord.getAccordPoint() == 100){
        		billionSecondTimeBean.setTime100(timeRecord.getAccordTime());
        	}else if(timeRecord.getAccordPoint() == 101){
        		billionSecondTimeBean.setTime101(timeRecord.getAccordTime());
        	}else if(timeRecord.getAccordPoint() == 102){
        		billionSecondTimeBean.setTime102(timeRecord.getAccordTime());
        	}else if(timeRecord.getAccordPoint() == 103){
        		billionSecondTimeBean.setTime103(timeRecord.getAccordTime());
        	}else if(timeRecord.getAccordPoint() == 104){
        		billionSecondTimeBean.setTime104(timeRecord.getAccordTime());
        	}else if(timeRecord.getAccordPoint() == 105){
        		billionSecondTimeBean.setTime105(timeRecord.getAccordTime());
        	}
        	//判断在哪个阶段
        	if(flag){
        		if(timeRecord.getAccordPoint() == 100 && isEnd == 0){
            		stage = 0;
            		flag = false;
            	}else if(timeRecord.getAccordPoint() == 101 && isEnd == 0){
            		stage = 1;
            		flag = false;
            	}else if(timeRecord.getAccordPoint() == 102 && isEnd == 0){
            		stage = 2;
            		flag = false;
            	}else if(timeRecord.getAccordPoint() == 103 && isEnd == 0){
            		stage = 3;
            		flag = false;
            	}else if(timeRecord.getAccordPoint() == 104 && isEnd == 0){
            		stage = 4;
            		flag = false;
            	}else if(timeRecord.getAccordPoint() == 105 && isEnd == 0){
            		stage = 5;
            		flag = false;
            	}
        	}
        }
        //结束阶段
        if(isEnd == 1){
        	//结束阶段定义
        	stage = 6;
    		//定义时间段
    		int stage104to105 = 0,stage103to104 = 0,stage102to103 = 0,stage101to102 = 0,stage100to101 = 0;
    		int loopNum = 0;
    		int min = 0;
    		//取时间段,取最短时间
    		for(int i = billionSecondTimeRecords.size() - 1;i > 0 ;i--){
    			loopNum = billionSecondTimeRecords.get(i).getAccordTime() - billionSecondTimeRecords.get(i-1).getAccordTime();
    			if(i == 5){
    				stage104to105 = loopNum;
    				min = stage104to105;
    			}else if(i == 4){
    				stage103to104 = loopNum;
    				min = stage103to104 - min > 0 ? min : stage103to104;
    			}else if(i == 3){
    				stage102to103 = loopNum;
    				min = stage102to103 - min > 0 ? min : stage102to103;
    			}else if(i == 2){
    				stage101to102 = loopNum;
    				min = stage101to102 - min > 0 ? min : stage101to102;
    			}else if(i == 1){
    				stage100to101 = loopNum;
    				min = stage100to101 - min > 0 ? min : stage100to101;
    			}
    		}
    		
    		if(min == stage100to101){
    			minStage = 1;
    		}else if(min == stage101to102){
    			minStage = 2;
    		}else if(min == stage102to103){
    			minStage = 3;
    		}else if(min == stage103to104){
    			minStage = 4;
    		}else if(min == stage104to105){
    			minStage = 5;
    		}
        }
        
        //系统当前时间戳
        billionSecondTimeBean.setNowTime(GetDate.getNowTime10());
        //活动是否已结束
        billionSecondTimeBean.setIsEnd(isEnd);
        //活动进行到几阶段
        billionSecondTimeBean.setStage(stage);
        //最小时间段
        billionSecondTimeBean.setMinStage(minStage);
        modelAndView.addObject("billionSecondTimeBean", billionSecondTimeBean);
        //活动二获奖记录
        List<ActivityBillionSecond> billionSecondRecords = this.billionService.getBillionSecondRecords();
        modelAndView.addObject("billionSecondRecords", billionSecondRecords);
		// 奖品类型
        List<ParamName> billionPrizeTypes = this.billionService.getParamNameList("BILLION_PRIZE_TYPE");
        modelAndView.addObject("billionPrizeTypes", billionPrizeTypes);
        
        /**
         * 活动三
         */
        List<ActivityBillionThirdCustomize> list = billionService.getBillionThirdActivityList();
        modelAndView.addObject("billionThirdList", list);
        /**
         * 默认显示活动A
		 * 活动C开始后，默认显示活动C
		 * 活动C结束后，默认显示活动A
	     * 活动A结束后，默认显示活动B
	     * activityFlag
	     * 1、活动一 2、活动二 3、活动三  
         */
        int activityFlag = 0;
        for (ActivityBillionThirdCustomize activityBillionThirdCustomize : list) {
            if("2".equals(activityBillionThirdCustomize.getKillStatus())){
                modelAndView.addObject("nextKillId", activityBillionThirdCustomize.getId());
                Date killTime = getKillTime(activityBillionThirdCustomize.getSecKillTime());
                modelAndView.addObject("nextKillTime", killTime.getTime());
                modelAndView.addObject("currentTime", new Date().getTime());
                modelAndView.addObject("remainingNum", activityBillionThirdCustomize.getRemainingNum());
            }
            //活动三是否开始
            if(activityFlag != 3 && "0".equals(activityBillionThirdCustomize.getStatus())){
                activityFlag = 3 ;
            } 
        }
        //活动一是否开始
        if(activityFlag == 0){
        	 for(ActivityBillionOne record : billionOneRecords){
             	if(record.getAccordMoney() == 104 && record.getStatus() != 2){
             		activityFlag = 1;
             	}
             }
        }
        //是否展示活动二
        if(activityFlag == 0){
        	activityFlag = 2;
        }
        modelAndView.addObject("activityFlag", activityFlag);
        
        //billionThirdStarFlag 0活动未开启  1活动开启 2活动结束
        Integer billionThirdStarFlag=getBillionThirdStar(billionSecondTimeBean.getTime100());
        modelAndView.addObject("billionThirdStarFlag", billionThirdStarFlag);
        return modelAndView;
    }
    
    
    private Integer getBillionThirdStar(Integer time100) {
        Integer flag=0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = sdf.format(new Date());
        String time100String = sdf.format(new Date());
        try {
            time100String = sdf.format(new Date(time100*1000L));
        } catch (Exception e) {
            return 0;
        }
        if(time100!=null&&time100!=0&&!currentDateString.equals(time100String)){

            long nd = 24*60*60*1000;//一天的秒数
            long diff = 0;
            try {
                diff =sdf.parse(sdf.format(new Date())).getTime()-sdf.parse(sdf.format(new Date(time100*1000L))).getTime();

            } catch (ParseException e) {
            }
            long day = diff/nd;//计算差多少天

            if(day>3){
                flag=2;
            }else{
                flag=1; 
            }
            
        }
        return flag;
    }


    private Date getKillTime(String secKillTime) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat gsdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDateString = sdf.format(new Date());
        currentDateString=currentDateString+secKillTime;
        Date time=null;
        try {
           time= gsdf.parse(currentDateString);

        } catch (Exception e) {

           e.printStackTrace();
        }
        return time;
    }


    /**
     * 
     * 返回百亿活动3商品列表
     * 
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BillionDefine.GET_BILLION_THIRD_ACTIVITY_LIST_ACTION)
    public JSONObject getBillionThirdActivityList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        
        List<ActivityBillionThirdCustomize> list=billionService.getBillionThirdActivityList();
        ret.put("billionThirdList", list);
        ret.put("status", "0");
        ret.put(CustomConstants.APP_REQUEST, BillionDefine.REQUEST_HOME + BillionDefine.REQUEST_MAPPING
                + BillionDefine.GET_BILLION_THIRD_ACTIVITY_LIST_ACTION);
        ret.put("statusDesc", "成功");
        return ret;
    }
    
    
    /**
     * 
     * 返回百亿活动3商品列表
     * 
     * @author pcc
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = BillionDefine.USER_PARTAKE_BILLION_THIRD_ACTIVITY_ACTION)
    public JSONObject userPartakeBillionThirdActivity(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        Integer userId = WebUtils.getUserId(request);
//        Integer userId=22400111;
        if(userId==null || userId==0){
            ret.put("error", 0);
            ret.put("status", 1);
            ret.put("host", BillionDefine.LOGIN_REQUEST_MAPPING);
            ret.put("msg", "用户未登录");
            return ret;
        }
        String killId = request.getParameter("killId");
        ret.put("host", BillionDefine.INVEST_REQUEST_MAPPING);
        if(StringUtils.isBlank(killId)){
            ret.put("error", 0);
            ret.put("msg", "活动编号为空");
            return ret;
        }
        //billionThirdStarFlag 0活动未开启  1活动开启
        //活动二时间点记录
        List<ActivityBillionSecondTime> billionSecondTimeRecords = this.billionService.getBillionSecondTimeRecords();
        Integer billionThirdStarFlag=getBillionThirdStar(billionSecondTimeRecords.get(0).getAccordTime());
        if(billionThirdStarFlag==0){
            ret.put("error", 0);
            ret.put("msg", "活动未开启");
            return ret;
        }
        String msg =billionService.updateUserPartakeBillionThirdActivity(killId,userId);
        
        
        List<ActivityBillionThirdCustomize> list=billionService.getBillionThirdActivityList();
        ret.put("billionThirdList", list);
        for (ActivityBillionThirdCustomize activityBillionThirdCustomize : list) {
            if("2".equals(activityBillionThirdCustomize.getKillStatus())){
                ret.put("nextKillId", activityBillionThirdCustomize.getId());
                Date killTime=getKillTime(activityBillionThirdCustomize.getSecKillTime());
                ret.put("nextKillTime", killTime.getTime());
                ret.put("currentTime", GetDate.getNowTime10());
                ret.put("remainingNum", activityBillionThirdCustomize.getRemainingNum());
            }
            if(killId.equals(activityBillionThirdCustomize.getId()+"")){
                String successMsg="恭喜您成功获得";
                if("2".equals(activityBillionThirdCustomize.getCouponType())){
                    successMsg=successMsg+activityBillionThirdCustomize.getCouponQuota()+"%代金券";
                }else{
                    successMsg=successMsg+activityBillionThirdCustomize.getCouponQuota()+"元代金券";
                }
                ret.put("msg", successMsg);
            }
        }
        if(msg == null||"".equals(msg)){
            ret.put("error", 1);
        }else {
            ret.put("error", 0);
            ret.put("msg", msg);
        }
       
        return ret;
    }
    
    
}
