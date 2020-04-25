package com.hyjf.app.find.security;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.platdatastatistics.PlatDataStatisticsService;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.mapper.customize.OperationReportCustomizeMapper;
import com.hyjf.mybatis.model.customize.app.AppFindSecurityCustomize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Calendar;

import static java.math.RoundingMode.*;

/**
 * @author lisheng
 * @version AppFindSecurityController, v0.1 2018/5/4 10:22
 */
@Controller
@RequestMapping(value = AppFindSecurityDefine.REQUEST_MAPPING)
public class AppFindSecurityController {
    @Autowired
    private PlatDataStatisticsService platDataStatisticsService;
    /** 平台上线时间 */
    private static final String PUT_ONLINE_TIME = "2013-12";
    /**
     * 获取安全保障数据
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = AppFindSecurityDefine.SECURITY)
    public JSONObject getSecurityMessage(HttpServletRequest request, HttpServletResponse response) {
        JSONObject ret = new JSONObject();
        ret.put("status", "000");
        ret.put("statusDesc", "请求成功");
        try {
            AppFindSecurityCustomize appFindSecurityCustomize = new AppFindSecurityCustomize();
            BigDecimal bigDecimal = platDataStatisticsService.selectTotalInvest();
            String TotalCount = bigDecimal.divide(new BigDecimal(100000000), 0, BigDecimal.ROUND_DOWN).toString();
            BigDecimal bigDecimal1 = platDataStatisticsService.selectTotalInterest();
            String IotalInterest = bigDecimal1.divide(new BigDecimal(100000000), 0, BigDecimal.ROUND_DOWN).toString();

            String yearFromDate = String.valueOf(GetDate.getYearFromDate(PUT_ONLINE_TIME));
            int tenderCount = platDataStatisticsService.selectTotalTradeSum();//平台累计出借笔数
            BigDecimal total = new BigDecimal(tenderCount);
            String totalTradeVolume = total.divide(new BigDecimal(10000)).setScale(0, BigDecimal.ROUND_DOWN).toString();

            appFindSecurityCustomize.setTotalInvester(totalTradeVolume + "万"); // 平台累计出借者
            appFindSecurityCustomize.setTotalUserIncome(IotalInterest + "亿");// 累计收益
            appFindSecurityCustomize.setStartYear("2013");//成立年份
            appFindSecurityCustomize.setOperateYear(yearFromDate);//运营多少年
            appFindSecurityCustomize.setCompanyGrade("AAA");//企业评级
            appFindSecurityCustomize.setTotalTradeVolume(TotalCount + "亿");//平台累计出借
            ret.put("info", appFindSecurityCustomize);
        } catch (Exception e) {
            ret.put("status", "999");
            ret.put("statusDesc", "系统异常请稍后再试");
            ret.put("info", new AppFindSecurityCustomize());
        }
        return ret;

    }
}
