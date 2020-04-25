package com.hyjf.web.platdata;



import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



/**
 *
 *平台数据
 */
@Controller
@RequestMapping(PlatDataDefine.REQUEST_MAPPING)
public class PlatDataController  {
    @Autowired
   private PlatDataService platDataService;

	/**
	 * 平台数据
	 * 
	 * @return
	 */
	@RequestMapping(value = PlatDataDefine.DATA_ACTION)
	public ModelAndView data(HttpServletRequest request, HttpServletResponse response, ModelMap modelmap) {
		ModelAndView modelAndView = new ModelAndView(PlatDataDefine.DATA_PATH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("#,##0");
		Map<String, Object> data = platDataService.selectData();
		if (data != null) {

			modelmap.put("tenderMoney",
					df.format(new BigDecimal(data.get("tender_sum") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			modelmap.put("recoverInterest",
					df.format(new BigDecimal(data.get("interest_sum") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			modelmap.put("tenderMoney7", df.format(
					new BigDecimal(data.get("seven_day_tender_sum") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			modelmap.put("recoverInterest7", df.format(
					new BigDecimal(data.get("seven_day_interest_sum") + "").setScale(0, BigDecimal.ROUND_HALF_DOWN)));
			// 融资期限分布
			Map<String, Object> periodInfo = new HashMap<String, Object>();
			// 出借金额占比
			Map<String, Object> TendMoneyInfo = new HashMap<String, Object>();
			modelmap.put("newdate", sdf.format(data.get("update_time")));
			periodInfo.put("zeroone", data.get("borrow_zero_one"));
			periodInfo.put("onethree", data.get("borrow_one_three"));
			periodInfo.put("threesex", data.get("borrow_three_six"));
			periodInfo.put("sextw", data.get("borrow_six_twelve"));
			periodInfo.put("tw", data.get("borrow_twelve_up"));

			TendMoneyInfo.put("zeroone", data.get("invest_one_down"));
			TendMoneyInfo.put("onefive", data.get("invest_one_five"));
			TendMoneyInfo.put("fiveten", data.get("invest_five_ten"));
			TendMoneyInfo.put("tenfive", data.get("invest_ten_fifth"));
			TendMoneyInfo.put("five", data.get("invest_fifth_up"));
			modelmap.put("periodInfo", periodInfo);
			modelmap.put("TendMoneyInfo", TendMoneyInfo);
		}

		List<Map<String, Object>> tenderlist = platDataService.selectTenderListMap(0);
		List<Map<String, Object>> creditlist = platDataService.selectTenderListMap(1);
		List<Map<String, Object>> htjlist = platDataService.selectTenderListMap(2);
		// 开始先把日期都放集合里 然后排序，
		// 然后 循环对比集合里是否有此日期的数据 么有的话放0，有的话放入
		// 最后生成两个一样大小的有序集合
		List<String> timelist = new ArrayList<String>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.addAll(tenderlist);
		list.addAll(creditlist);
		list.addAll(htjlist);
		for (int i = 0; i < list.size(); i++) {
			timelist.add(list.get(i).get("time") + "");
		}
		HashSet<String> h = new HashSet<String>(timelist);
		timelist.clear();
		timelist.addAll(h);
		Collections.sort(timelist);
		List<Map<String, Object>> newtenderlist = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> newcreditlist = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> newhtjlist = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (int j = 0; j < tenderlist.size(); j++) {

			for (; i < timelist.size(); i++) {
				if (timelist.get(i).compareTo(tenderlist.get(j).get("time") + "") != 0) {
					Map<String, Object> tmpmap = new HashMap<String, Object>();
					tmpmap.put("money", "0");
					tmpmap.put("time", timelist.get(i));
					newtenderlist.add(tmpmap);
				} else {
					newtenderlist.add(tenderlist.get(j));
					if (j != (tenderlist.size() - 1)) {
						i++;
						break;
					}
				}
			}
		}
		i = 0;
		for (int j = 0; j < creditlist.size(); j++) {
			for (; i < timelist.size(); i++) {
				if (timelist.get(i).compareTo(creditlist.get(j).get("time") + "") != 0) {
					Map<String, Object> tmpmap = new HashMap<String, Object>();
					tmpmap.put("money", "0");
					tmpmap.put("time", timelist.get(i));
					newcreditlist.add(tmpmap);
				} else {
					newcreditlist.add(creditlist.get(j));
					if (j != (creditlist.size() - 1)) {
						i++;
						break;
					}
				}

			}
		}
		i = 0;
		for (int j = 0; j < htjlist.size(); j++) {
			for (; i < timelist.size(); i++) {
				if (timelist.get(i).compareTo(htjlist.get(j).get("time") + "") != 0) {
					Map<String, Object> tmpmap = new HashMap<String, Object>();
					tmpmap.put("money", "0");
					tmpmap.put("time", timelist.get(i));
					newhtjlist.add(tmpmap);
				} else {
					newhtjlist.add(htjlist.get(j));
					if (j != (htjlist.size() - 1)) {
						i++;
						break;
					}
				}

			}
		}
		// 为前台报表js不能写循环？ 制造数据
		for (int j = 0; j < timelist.size(); j++) {
			timelist.set(j, "\"" + timelist.get(j) + "\"");
		}
		List<String> tendermoneylist = new ArrayList<String>();
		List<String> creditmoneylist = new ArrayList<String>();
		List<String> htjmoneylist = new ArrayList<String>();
		for (int j = 0; j < newtenderlist.size(); j++) {
			tendermoneylist.add(newtenderlist.get(j).get("money") + "");
			if (newcreditlist != null && newcreditlist.size() > 0) {
				creditmoneylist.add(newcreditlist.get(j).get("money") + "");
			}
			if (newhtjlist != null && newhtjlist.size() > 0) {
				htjmoneylist.add(newhtjlist.get(j).get("money") + "");
			}
		}
		modelmap.put("tendermoneylist", tendermoneylist);
		modelmap.put("creditmoneylist", creditmoneylist);
		modelmap.put("htjmoneylist", htjmoneylist);
		modelmap.put("timelist", timelist);
		modelmap.put("timeSize", timelist.size());

		return modelAndView;
	}
   /**
    * 
    * @method: data2php
    * @description: 	给php的平台数据统计接口		
    * @param request
    * @param response
    * @return 
    * @return: Object
    * @mender: zhouxiaoshuai
    * @date:   2016年5月27日 下午2:45:06
    */
   @RequestMapping("data2php")
   @ResponseBody
   public Object data2php(HttpServletRequest request,HttpServletResponse response) {
       Map<String, Object> map=new HashMap<String, Object>();
       Map<String, Object> data=platDataService.selectData();   
       if (data!=null) {
    	   map.put("tenderMoney", data.get("tender_sum"));
    	   map.put("recoverInterest", data.get("interest_sum"));
    	   map.put("tenderMoney7", data.get("seven_day_tender_sum"));
    	   map.put("recoverInterest7", data.get("seven_day_interest_sum"));
		}
    return map;
  } 
 
}
