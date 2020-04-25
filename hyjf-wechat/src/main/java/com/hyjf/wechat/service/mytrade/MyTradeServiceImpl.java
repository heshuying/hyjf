package com.hyjf.wechat.service.mytrade;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hyjf.mybatis.model.customize.app.AppAccountTradeListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTradeListCustomize;
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.controller.user.mytrade.QueryTradeListQO;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by cuigq on 2018/2/8.
 */
@Service
public class MyTradeServiceImpl extends BaseServiceImpl implements MyTradeService {


    @Override
    public int countTradeList(QueryTradeListQO qo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", qo.getUserId());
        params.put("tradeType", qo.getTradeType());

        String tradeYear = qo.getYear();
        if (!Strings.isNullOrEmpty(tradeYear)) {
            params.put("tradeYear", Integer.parseInt(tradeYear));
        }
        String tradeMonth = qo.getMonth();
        if (!Strings.isNullOrEmpty(tradeMonth)) {
            params.put("tradeMonth", Integer.parseInt(tradeMonth));
        }
        return this.appTradeDetailCustomizeMapper.countTradeDetailListRecordTotal(params);
    }

    @Override
    public List<AppTradeListCustomize> queryTradeList(QueryTradeListQO qo) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", qo.getUserId());
        params.put("tradeType", qo.getTradeType());

        String tradeYear = qo.getYear();
        if (!Strings.isNullOrEmpty(tradeYear)) {
            params.put("tradeYear", Integer.parseInt(tradeYear));
        }
        String tradeMonth = qo.getMonth();
        if (!Strings.isNullOrEmpty(tradeMonth)) {
            params.put("tradeMonth", Integer.parseInt(tradeMonth));
        }

        int limit = qo.getPageSize();
        int page = qo.getCurrentPage();
        int offSet = (page - 1) * limit;
        if (offSet == 0 || offSet > 0) {
            params.put("limitStart", offSet);
        }
        if (limit > 0) {
            params.put("limitEnd", limit);
        }
        // 查询相应的用户出借项目列表
        List<AppTradeListCustomize> userTrades = this.appTradeDetailCustomizeMapper.searchTradeDetailList(params);

        return groupTradesByMonth(userTrades);
    }

    private List<AppTradeListCustomize> groupTradesByMonth(List<AppTradeListCustomize> userTrades) {
        List<AppTradeListCustomize> list = Lists.newArrayList();
        Calendar cal = Calendar.getInstance();
        String newYear = cal.get(Calendar.YEAR) + "";
        String newMonth = cal.get(Calendar.MONTH) + 1 + "";
        newMonth = newMonth.length() == 1 ? "0" + newMonth : newMonth;
        String monthSign = "0";
        String month = "";
        for (AppTradeListCustomize appTradeListCustomize : userTrades) {
            String tradeYear = appTradeListCustomize.getTradeYear();
            String tradeMonth = appTradeListCustomize.getTradeMonth();

            //本月
            if (newYear.equals(tradeYear) && newMonth.equals(tradeMonth)) {
                if (!monthSign.equals(tradeYear + tradeMonth)) {
                    AppTradeListCustomize customize = new AppTradeListCustomize();
                    customize.setIsMonth("0");
                    customize.setMonth("本月");
                    month = "本月";
                    monthSign = tradeYear + tradeMonth;
                    list.add(customize);
                }
            }

            //非本月放 xx月
            if (newYear.equals(tradeYear) && !newMonth.equals(tradeMonth)) {
                if (!monthSign.equals(tradeYear + tradeMonth)) {
                    AppTradeListCustomize customize = new AppTradeListCustomize();
                    customize.setIsMonth("0");
                    customize.setMonth(tradeMonth + "月");
                    monthSign = tradeYear + tradeMonth;
                    month = tradeMonth + "月";
                    list.add(customize);
                }
            }

            // 非本年 month 放 xxxx年xx月
            if (!newYear.equals(tradeYear)) {
                if (!monthSign.equals(tradeYear + tradeMonth)) {
                    AppTradeListCustomize customize = new AppTradeListCustomize();
                    customize.setIsMonth("0");
                    customize.setMonth(tradeYear + "年" + tradeMonth + "月");
                    monthSign = tradeYear + tradeMonth;
                    month = tradeYear + "年" + tradeMonth + "月";
                    list.add(customize);
                }
            }
            appTradeListCustomize.setBankBalance("可用金额".concat(appTradeListCustomize.getBankBalance()));
            appTradeListCustomize.setMonth(month);
            list.add(appTradeListCustomize);
        }
        return list;
    }

    @Override
    public List<AppAccountTradeListCustomize> queryTradeTypes() {
        List<AppAccountTradeListCustomize> list=appAccountTradeCustomizeMapper.selectTradeTypeList();
        /**创建优惠券回款相关交易明细查询条件*/
        AppAccountTradeListCustomize appAccountTradeListCustomize=new AppAccountTradeListCustomize();
        appAccountTradeListCustomize.setName("优惠券收益");
        appAccountTradeListCustomize.setValue("coupon_profit");
        list.add(appAccountTradeListCustomize);
        return list;
    }
}
