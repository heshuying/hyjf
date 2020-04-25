/**
 * Description：用户交易明细service接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.app.user.trade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.mybatis.model.customize.app.AppAccountTradeListCustomize;
import com.hyjf.mybatis.model.customize.app.AppTradeListCustomize;

@Service
public class TradeDetailServiceImpl extends BaseServiceImpl implements TradeDetailService {
	private Logger logger = LoggerFactory.getLogger(TradeDetailServiceImpl.class);
	
	@Override
	public List<AppAccountTradeListCustomize> searchTradeTypes() {
		List<AppAccountTradeListCustomize> list=appAccountTradeCustomizeMapper.selectTradeTypeList();
		/**创建优惠券回款相关交易明细查询条件*/
		AppAccountTradeListCustomize appAccountTradeListCustomize=new AppAccountTradeListCustomize();
		appAccountTradeListCustomize.setName("优惠券收益");
		appAccountTradeListCustomize.setValue("coupon_profit");
		list.add(appAccountTradeListCustomize);
		return list;
	}

	@Override
	public int countTradeDetailListRecordTotal(Map<String, Object> params) {
		int total= appTradeDetailCustomizeMapper.countTradeDetailListRecordTotal(params);
		return total;
	}

	@Override
	public List<AppTradeListCustomize> searchTradeDetailList(Map<String, Object> params) {
		List<AppTradeListCustomize> tradeList = appTradeDetailCustomizeMapper.searchTradeDetailList(params);
		List<AppTradeListCustomize> list = new ArrayList<AppTradeListCustomize>();
		Calendar cal = Calendar.getInstance();
		String newYear = cal.get(Calendar.YEAR) + "";
		String newMonth = cal.get(Calendar.MONTH) + 1 + "";
		newMonth = newMonth.length() == 1 ? "0" + newMonth : newMonth;
		String monthSign = "0";
		String month="";
		if(!CollectionUtils.isEmpty(tradeList)){
			for (AppTradeListCustomize appTradeListCustomize : tradeList) {
				String tradeYear = appTradeListCustomize.getTradeYear();
				String tradeMonth = appTradeListCustomize.getTradeMonth();
				
				//本月
				if (newYear.equals(tradeYear) && newMonth.equals(tradeMonth)) {
					if (!monthSign.equals(tradeYear + tradeMonth)) {
						AppTradeListCustomize customize = new AppTradeListCustomize();
						customize.setIsMonth("0");
						customize.setMonth("本月");
						month="本月";
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
						month=tradeMonth + "月";
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
						month=tradeYear + "年" + tradeMonth + "月";
						list.add(customize);
					}
				}
				appTradeListCustomize.setBankBalance("可用金额".concat(appTradeListCustomize.getBankBalance()));
				appTradeListCustomize.setMonth(month);
				list.add(appTradeListCustomize);
			}
		}else {
			logger.info("未查询到交易数据,返回月份标题....");
			AppTradeListCustomize customize = new AppTradeListCustomize();
			customize.setIsMonth("0");
			customize.setMonth(String.valueOf(params.get("tradeMonth")));
			list.add(customize);
		}

		return list;
	}
	

}
