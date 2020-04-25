/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.content.operationreport;

import com.hyjf.admin.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yinhui
 * @version Collct, v0.1 2018/6/22 16:35
 * TODO 正式上线前删除
 */
@Controller
@RequestMapping(value = "/manager/content/report")
public class Collct  extends BaseController {

    @Autowired
    private  StatisticsOperationReportInfoService statisticsOperationReportInfoService;

    /**
     *
     * 首页面跳转
     *
     * @author Michael
     * @return
     */
    @RequestMapping(value = "/auto", method = RequestMethod.GET)
    @ResponseBody
    public String Test(@RequestParam("year") String year,@RequestParam("month") String month) {
        try {
            if(StringUtils.isEmpty(year) || StringUtils.isEmpty(month)){
                return "参数不能为空";
            }

            //测试
            int lastMonth = 0;
            if(month.equals("1")){
                lastMonth=12;
            }else{
                lastMonth = Integer.valueOf(month)-1;
            }


//            String year = String.valueOf(GetDate.getYear());
//            String month = GetDate.getMonth();
//
//            SimpleDateFormat sdf = new SimpleDateFormat("MM");
//            Calendar calendar = Calendar.getInstance();//日历对象
//            calendar.setTime(new Date());//设置当前日期
//            calendar.add(Calendar.MONTH, -1);//月份减一
//            //输出上个月的日期
//            int lastMonth = Integer.valueOf(sdf.format( calendar.getTime()));

            //每个月月初的1号，自动统计出上一个月的数据，统计顺序依次是：
            //1月，2月，第一季度，4月，5月，上半年，7月，8月，第三季度，10月，11月，年度报告
            if(lastMonth == 12){
                statisticsOperationReportInfoService.setYearReport(year,month);
            } else if(lastMonth == 6 ){
                statisticsOperationReportInfoService.setHalfYearReport(year,month);
            }else if(lastMonth == 3 || lastMonth == 9 ){
                statisticsOperationReportInfoService.setQuarterReport(year,month);
            }else{
                statisticsOperationReportInfoService.setMonthReport(year,month);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succeed";
    }
}
