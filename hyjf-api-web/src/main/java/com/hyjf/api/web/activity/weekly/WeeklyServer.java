package com.hyjf.api.web.activity.weekly;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.activity.weekly.WeeklyDefine;
import com.hyjf.activity.weekly.WeeklyResultBean;
import com.hyjf.activity.weekly.WeeklyService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseBean;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderCpn;
import com.hyjf.mybatis.model.auto.CreditRepay;
import com.hyjf.mybatis.model.auto.CreditTender;
import com.hyjf.mybatis.model.auto.Events;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.WeeklyReport;

@Controller
@RequestMapping(value=WeeklyDefine.REQUEST_MAPPING)
public class WeeklyServer extends BaseController{
	
	 @Autowired
	 private WeeklyService weeklyService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdyy= new SimpleDateFormat("yyyy");
	SimpleDateFormat sddd= new SimpleDateFormat("dd");
    /**
     * 
     * 上周周报
     * @author dzs
     * @param form
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = WeeklyDefine.GET_WEEKLY_DATA)
    public WeeklyResultBean getWeeklyData(@ModelAttribute BaseBean requestBean, HttpServletRequest request, HttpServletResponse response) {
        String methodName = "getWeeklyData";
        LogUtil.startLog(this.getClass().getName(), methodName);
        WeeklyResultBean resultBean = new WeeklyResultBean();
       // resultBean.setSysDate(GetDate.getNowTime10());
        // 验签
        if(!this.checkSign(requestBean, WeeklyDefine.GET_WEEKLY_DATA)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        String userId = request.getParameter("userId");
        
        //验证请求参数
        if (Validator.isNull(userId)) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        
        Users user=weeklyService.getUsers(Integer.valueOf(userId));
        
        Map<String, String> date= this.getLastTimeInterval(user.getRegTime());
        int date1= Integer.valueOf(date.get("date1"));
        int date2=Integer.valueOf(date.get("date2"));
        String dat1=date.get("date1");
        String dat2=date.get("date2");
        

        
        List<WeeklyReport> wse1 = weeklyService.getWeeklyReport(0, dat1);
        
        if(wse1.isEmpty()) {
        	//放入公共周数据
            Events eal = weeklyService.getEventsAll(date1,date2);
            resultBean.setBenzhoutouzie(eal.getEventTime());
            resultBean.setBenzhoushouyi(eal.getContent());
            resultBean.setChengjiaoshu(eal.getEventYear());
            WeeklyReport wrt=new WeeklyReport();
            wrt.setUserId(0);
            wrt.setBeginDate(dat1);
            wrt.setTouzie(new BigDecimal(eal.getEventTime()));
            wrt.setShouyi(new BigDecimal(eal.getContent()));
            wrt.setBishu(eal.getEventYear());
            weeklyService.inWeeklyReport(wrt);
        }else {
            resultBean.setBenzhoutouzie(wse1.get(0).getTouzie().toString());
            resultBean.setBenzhoushouyi(wse1.get(0).getShouyi().toString());
            resultBean.setChengjiaoshu(wse1.get(0).getBishu());
        	
        }
        resultBean.setBeginDate(dat1);
        resultBean.setEndDate(dat2);
        resultBean.setZongtianshu(Integer.valueOf(date.get("zongtianshu")));
        resultBean.setDateString(date.get("dateString"));
        Account act = weeklyService.getAccount(Integer.valueOf(userId));
        resultBean.setZongshouyi(act.getBankInterestSum());
        resultBean.setZongjine(act.getBankTotal());
        List<WeeklyReport> wse2 = weeklyService.getWeeklyReport(Integer.valueOf(userId), dat1);
//        if(wse2.isEmpty()) {
        	WeeklyReport wrt=new WeeklyReport();
            wrt.setUserId(Integer.valueOf(userId));
            wrt.setBeginDate(dat1);
            wrt.setEndDate(dat2);
            wrt.setZongtianshu(Integer.valueOf(date.get("zongtianshu")));
            wrt.setDatestring(date.get("dateString"));
            
            Events eal2 = weeklyService.selectPercentage(act.getBankInterestSum().intValue(),date1,date2,Integer.valueOf(userId));
            resultBean.setBaifenbi(eal2.getEventYear());
            wrt.setBaifenbi(eal2.getEventYear());
            
            List<BorrowTender> bt=weeklyService.getBorrowTender(Integer.valueOf(userId),date1,date2);
            List<CreditTender> ct = weeklyService.getCreditTender(Integer.valueOf(userId),dat1,dat2);
         //   List<HjhAccede> ae = weeklyService.getAccede(Integer.valueOf(userId), date1,date2);
            List<BorrowTenderCpn> btc=weeklyService.getBorrowTenderCPN(Integer.valueOf(userId),date1,date2);

            resultBean.setBishu(bt.size()+ct.size());
            wrt.setBishu(bt.size()+ct.size());
            BigDecimal leijie = new BigDecimal(0);
            BigDecimal yuqi = new BigDecimal(0);
            List<String> list1=new ArrayList<String>();  
            for (BorrowTender borrowTender : bt) {
            	leijie=leijie.add(borrowTender.getAccount());
            	yuqi=yuqi.add(borrowTender.getRecoverAccountInterest());
       		 	Date mm = new Date(Long.valueOf(borrowTender.getAddtime())* 1000);
       		 	list1.add(sddd.format(mm));
    		}
            for (CreditTender creditTender : ct) {
            	leijie=leijie.add(creditTender.getAssignCapital());
            	yuqi=yuqi.add(creditTender.getAssignInterest());
       		 	Date mm = new Date(Long.valueOf(Integer.valueOf(creditTender.getAddTime())) * 1000);
       		 	list1.add(sddd.format(mm));
    		}
//            for (HjhAccede hjhAccede : ae) {
//            	yuqi=yuqi.add(hjhAccede.getShouldPayInterest());
//            	
//    		}
            for (BorrowTenderCpn borrowTenderCpn : btc) {
            	yuqi=yuqi.add(borrowTenderCpn.getRecoverAccountWait());
			}
            resultBean.setTouzigaikuang(this.quchong(list1));
            resultBean.setTouzie(leijie);
            resultBean.setShouyi(yuqi);
            wrt.setTouzie(leijie);
            wrt.setShouyi(yuqi);
            StringBuffer s = new StringBuffer();
            for(int i = 0;i < resultBean.getTouzigaikuang().size(); i ++){
            	   s.append(resultBean.getTouzigaikuang().get(i));
            	   if(i!=(resultBean.getTouzigaikuang().size()-1)) {
            		   s.append("|");
            	   }
            	}
            wrt.setTouzigaikuang(s.toString());
            
            List<BorrowRecover> br = weeklyService.getBorrowRecover(Integer.valueOf(userId),dat1,dat2);
            List<CreditRepay> cr = weeklyService.getCreditRepay(Integer.valueOf(userId),date1,date2);
            List<CreditRepay> cr2 = weeklyService.getCreditRepayToCredit(Integer.valueOf(userId),date1,date2);

            BigDecimal huankuan = new BigDecimal(0);
            List<String> list2=new ArrayList<String>();  
            BigDecimal zhaizhuan = new BigDecimal(0);
            for (BorrowRecover borrowRecover : br) {
            	huankuan=huankuan.add(borrowRecover.getRecoverAccountYes());
       		 	Date mm = new Date(Long.valueOf(Integer.valueOf(borrowRecover.getRecoverYestime())) * 1000);
       		 	list2.add(sddd.format(mm));
    			
    		}
            for (CreditRepay creditRepay : cr) {
            	huankuan=huankuan.add(creditRepay.getAssignRepayAccount());
       		 	Date mm = new Date(Long.valueOf(Integer.valueOf(creditRepay.getAddTime())) * 1000);
       		 	list2.add(sddd.format(mm));
    			
    		}
            for (CreditRepay creditRepay : cr2) {
            	zhaizhuan=zhaizhuan.add(creditRepay.getAssignRepayAccount());
			}
            huankuan=huankuan.subtract(zhaizhuan);
            String rr=eal2.getContent();
            if(rr!=null||"".equals(rr)) {
                huankuan=huankuan.add(new BigDecimal(rr));
            }
            resultBean.setHuankuanzonge(huankuan);
            resultBean.setHuankuangaikuang(this.quchong(list2));
            wrt.setHuankuanzonge(huankuan);
            StringBuffer s2 = new StringBuffer();
            for(int i = 0;i < resultBean.getHuankuangaikuang().size(); i ++){
            	   s2.append(resultBean.getHuankuangaikuang().get(i));
            	   if(i!=(resultBean.getHuankuangaikuang().size()-1)) {
            		   s2.append("|");
            	   }
            	}
            wrt.setHuankuangaikuang(s2.toString());
            

            weeklyService.inWeeklyReport(wrt);
            
      /*  }else {
        	resultBean.setBaifenbi(wse2.get(0).getBaifenbi());
        	resultBean.setBishu(wse2.get(0).getBishu());
        	resultBean.setTouzie(wse2.get(0).getTouzie());
        	resultBean.setShouyi(wse2.get(0).getShouyi());
        	resultBean.setHuankuanzonge(wse2.get(0).getHuankuanzonge());
        	List<String> list1 = new ArrayList<String>();
        	if(wse2.get(0).getHuankuangaikuang().contains("|"))
        	{
        		list1=Arrays.asList(wse2.get(0).getHuankuangaikuang().split("\\|"));
        		resultBean.setHuankuangaikuang(list1);
        	}else {
        		if(!wse2.get(0).getHuankuangaikuang().equals("")) {
            		list1.add(wse2.get(0).getHuankuangaikuang());
        		}
        		resultBean.setHuankuangaikuang(list1);

        	}
        	List<String> list2 = new ArrayList<String>();
        	if(wse2.get(0).getTouzigaikuang().contains("|"))
        	{
        		list2=Arrays.asList(wse2.get(0).getTouzigaikuang().split("\\|"));
        		resultBean.setTouzigaikuang(list2);
        	}else {
        		if(!wse2.get(0).getTouzigaikuang().equals("")) {
            		list2.add(wse2.get(0).getTouzigaikuang());
        		}
        		resultBean.setTouzigaikuang(list2);
        	}
        }*/
        if(weeklyService.coupon(Integer.valueOf(userId))) {
        	resultBean.setYouhuiquan(1);
        }else {
        	resultBean.setYouhuiquan(0);
        }
        resultBean.setHuodong(weeklyService.getActivity((int) (new Date().getTime()/1000)));
        resultBean.setJishi(weeklyService.getEvents(date1,date2,Integer.valueOf(date.get("year"))));




		return resultBean;
    	
    }
    

      
    /** 
    * 根据当前日期获得上周的日期区间（上周周一和周日日期） 
    *  
    * @return 
    * @author dzs
    */  
    public Map<String, String>  getLastTimeInterval(int regTime) {  
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Calendar calendar1 = Calendar.getInstance();  
         Calendar calendar2 = Calendar.getInstance(); 
         calendar1.set(Calendar.HOUR_OF_DAY, 0);
         calendar1.set(Calendar.MINUTE, 0);
         calendar1.set(Calendar.SECOND, 0);
         calendar2.set(Calendar.HOUR_OF_DAY, 23);
         calendar2.set(Calendar.MINUTE, 59);
         calendar2.set(Calendar.SECOND, 59);
         int dayWeek = calendar1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
         if (1 == dayWeek) {  
        	 calendar1.add(Calendar.DAY_OF_MONTH, -1);  
         }  
         int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;  
         int offset1 = 1 - dayOfWeek;  
         int offset2 = 7 - dayOfWeek;  
         calendar1.add(Calendar.DATE, offset1 - 7);  
         calendar2.add(Calendar.DATE, offset2 - 7);  
          
         Date date1 = calendar1.getTime();
         Date date2 = calendar2.getTime();
         long time = Long.valueOf(regTime) * 1000;
		 Date zongtianshu = new Date(time);
		 Calendar calendar3 = calendar1;
		 String dateString=sddd.format(calendar3.getTime());
		 for (int i = 1; i <= 6; i++) {
			 calendar3.add(Calendar.DATE, 1);
			 dateString=dateString+"|"+ sddd.format(calendar3.getTime());
		}
         
         Map<String, String> date=new HashMap<>();
         date.put("zongtianshu", String.valueOf(this.differentDaysByMillisecond(zongtianshu, date2)));
         date.put("date1", String.valueOf((int)(date1.getTime()/1000)));
         date.put("date2", String.valueOf((int)(date2.getTime()/1000)));
         date.put("dateString", dateString);
         date.put("year", sdyy.format(date2));
         String lastBeginDate = sdf.format(calendar1.getTime());
         String lastEndDate = sdf.format(calendar2.getTime());
         System.out.println(lastBeginDate+"kkkk"+lastEndDate);
         return date;  
    }
    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public  int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
    public  List<String> quchong( List<String> list){
    	
    	HashSet<String> h = new HashSet<String>(list);      
    	list.clear();      
    	list.addAll(h); 
        return list;
    }
}
