/**
 * Description：用户交易明细控制器
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by :
 */
package com.hyjf.app.user.trade;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mybatis.model.customize.app.AppAccountTradeListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTradeListCustomize;

@Controller
@RequestMapping(value = TradeDetailDefine.REQUEST_MAPPING)
public class TradeDetailController extends BaseController {

    @Autowired
    private TradeDetailService tradeDetailService;

    /**
     * 用户收支明细
     *
     * @param trade
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = TradeDetailDefine.TRADE_LIST_ACTION,  produces = "application/json; charset=utf-8")
    public JSONObject searchTradeDetailList(@ModelAttribute TradeDetailListBean trade, HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(TradeDetailDefine.THIS_CLASS, TradeDetailDefine.TRADE_LIST_ACTION);
        JSONObject info = new JSONObject();
        // 唯一标识
        String sign = request.getParameter("sign");
        // 用户id
        Integer userId = SecretUtil.getUserId(sign);
        trade.setUserId(userId+"");
        this.createTradeDetailListPage(info, trade);
        info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
        info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
        info.put(CustomConstants.APP_REQUEST, TradeDetailDefine.REQUEST_HOME + TradeDetailDefine.REQUEST_MAPPING + TradeDetailDefine.TRADE_LIST_ACTION);
        LogUtil.endLog(TradeDetailDefine.THIS_CLASS, TradeDetailDefine.TRADE_LIST_ACTION);
        return info;
    }

    /**
     * 创建用户收支明细列表分页数据
     *
     * @param request
     * @param info
     * @param form
     */
    private void createTradeDetailListPage(JSONObject info, TradeDetailListBean form) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", form.getUserId());
        params.put("tradeType", form.getTradeType());

        String tradeYear = form.getYear();
        if(StringUtils.isNotBlank(tradeYear)){
            params.put("tradeYear", Integer.parseInt(tradeYear));
        }
        String tradeMonth = form.getMonth();
        if(StringUtils.isNotBlank(tradeMonth)){
            params.put("tradeMonth", Integer.parseInt(tradeMonth));
        }

        // 统计相应的用户出借项目总数
        int recordTotal = this.tradeDetailService.countTradeDetailListRecordTotal(params);
        if (recordTotal > 0) {

            // 查询相应的汇直投列表数据
            int limit = form.getPageSize();
            int page = form.getPage();
            int offSet = (page - 1) * limit;
            if (offSet == 0 || offSet > 0) {
                params.put("limitStart", offSet);
            }
            if (limit > 0) {
                params.put("limitEnd", limit);
            }
            // 查询相应的用户出借项目列表
            List<AppTradeListCustomize> userTrades = tradeDetailService.searchTradeDetailList(params);

            info.put("userTrades", userTrades);
            info.put("tradeTotal", String.valueOf(recordTotal));
        } else {
            logger.info("未查询到交易数据,返回月份标题....");
            info.put("userTrades", buildNoneTradeListReturn(tradeYear, tradeMonth));
            info.put("tradeTotal", "0");
        }
    }

    /**
     * 没有交易明细的返回json
     * @param tradeYear
     * @param tradeMonth
     * @return
     */
    private List<AppTradeListCustomize> buildNoneTradeListReturn(String tradeYear, String tradeMonth){
        List<AppTradeListCustomize> list =  new ArrayList<>();
        AppTradeListCustomize customize = new AppTradeListCustomize();
        Calendar cal = Calendar.getInstance();
        int newYear = cal.get(Calendar.YEAR);
        int newMonth = cal.get(Calendar.MONTH) + 1 ;

        // 没有年月的过滤条件，默认当前
        int paramYear = newYear;
        int paramMonth = newMonth;
        if(StringUtils.isNotBlank(tradeYear)){
            paramYear = Integer.parseInt(tradeYear);
        }
        if(StringUtils.isNotBlank(tradeMonth)){
            paramMonth = Integer.parseInt(tradeMonth);
        }

        if(paramYear != newYear){
            customize.setMonth(tradeYear + "年" + tradeMonth + "月");
        } else {
            if(paramMonth != newMonth){
                customize.setMonth(tradeMonth + "月");
            } else {
                customize.setMonth("本月");
            }
        }
        customize.setIsMonth("0");
        list.add(customize);
        return list;
    }

    /**
     * 用户注册初始化画面数据保存（保存到session）
     */
    @ResponseBody
    @RequestMapping(value = TradeDetailDefine.TRADE_TYPES_ACTION, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public JSONObject searchTradeTypes(HttpServletRequest request, HttpServletResponse response) {

        LogUtil.startLog(TradeDetailDefine.THIS_CLASS, TradeDetailDefine.TRADE_TYPES_ACTION);
        JSONObject info = new JSONObject();
        List<AppAccountTradeListCustomize> tradeTypes = tradeDetailService.searchTradeTypes();
        info.put("tradeTypes", tradeTypes);
        info.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
        info.put(CustomConstants.APP_STATUS_DESC, CustomConstants.APP_STATUS_DESC_SUCCESS);
        info.put(CustomConstants.APP_REQUEST, TradeDetailDefine.REQUEST_HOME + TradeDetailDefine.REQUEST_MAPPING + TradeDetailDefine.TRADE_TYPES_ACTION);
        LogUtil.endLog(TradeDetailDefine.THIS_CLASS, TradeDetailDefine.TRADE_TYPES_ACTION);
        return info;

    }

}
