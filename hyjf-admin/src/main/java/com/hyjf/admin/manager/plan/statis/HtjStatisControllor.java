package com.hyjf.admin.manager.plan.statis;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.htl.productinfo.ProductInfoBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.customize.PlanStatisCustomize;

/**
 * @package com.hyjf.admin.maintenance.Product
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = StatisDefine.REQUEST_MAPPING)
public class HtjStatisControllor extends BaseController {

	/** THIS_CLASS */
	private static final String THIS_CLASS = HtjStatisControllor.class.getName();
	@Autowired
	private PlanStatisService planStatisService;

	/**
	 * 统计画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(StatisDefine.INIT)
	@RequiresPermissions(StatisDefine.STATIS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr,
			@ModelAttribute(StatisDefine.HTLSTATIS_FORM) ProductInfoBean form) {
		LogUtil.startLog(THIS_CLASS, StatisDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(StatisDefine.LIST_PATH);
		LogUtil.endLog(THIS_CLASS, StatisDefine.INIT);
		return modelAndView;
	}

	/**
	 * 债权分布
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = StatisDefine.SEARCH, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String searchAction(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute(StatisDefine.HTLSTATIS_FORM) ProductInfoBean form) {
		LogUtil.startLog(THIS_CLASS, StatisDefine.SEARCH);
		PlanStatisCustomize PlanStatisCustomize = new PlanStatisCustomize();
		String viewFlag = request.getParameter("viewFlag");
		if (viewFlag.contains("-2")) {
			PlanStatisCustomize.setvFlag(viewFlag);
		}
		if ("10-2".equals(viewFlag)) {
			PlanStatisCustomize.setHourSearch("01");
		}
		PlanStatisCustomize.setSort("asc");
		// 返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		List<PlanStatisCustomize> productinfolist = this.planStatisService.getRecordList(PlanStatisCustomize);
		int size = productinfolist.size();
		String days[] = new String[size];// 时间
		String hour[] = new String[size];// 时间小时
		String month[] = new String[size];// 时间月
		BigDecimal waitInvest[] = new BigDecimal[size];// 待成交资产-专属资产
		BigDecimal waitCredit[] = new BigDecimal[size];// 待成交资产-债权转让
		Integer yesInvest[] = new Integer[size]; // 已成交资产数量-专属资产
		Integer yesCredit[] = new Integer[size];// 已成交资产数量-债权转让
		BigDecimal waitRepay[] = new BigDecimal[size];// 持有债权待还总额
		BigDecimal yesRepay[] = new BigDecimal[size];// 持有债权已还总额
		BigDecimal planRepayWait[] = new BigDecimal[size];// 应还总额
		BigDecimal planRepayYes[] = new BigDecimal[size];// 已还款总额
		BigDecimal expireFairValue[] = new BigDecimal[size];// 到期公允价值
		BigDecimal planAccedeAll[] = new BigDecimal[size];// 计划加入总额
		if (productinfolist.size() > 0) {
			for (int i = 0; i < productinfolist.size(); i++) {
				PlanStatisCustomize productInfo = productinfolist.get(i);
				days[i] = productInfo.getDataDate();
				hour[i] = productInfo.getDataHour();
				month[i] = productInfo.getDataMonth();
				if (productInfo.getWaitInvest() != null && productInfo.getWaitInvest().compareTo(BigDecimal.ZERO) > 0) {
					waitInvest[i] = productInfo.getWaitInvest().divide(new BigDecimal(10000), 2);
				} else {
					waitInvest[i] = BigDecimal.ZERO;
				}
				if (productInfo.getWaitCredit() != null && productInfo.getWaitCredit().compareTo(BigDecimal.ZERO) > 0) {
					waitCredit[i] = productInfo.getWaitCredit().divide(new BigDecimal(10000), 2);
				} else {
					waitCredit[i] = BigDecimal.ZERO;
				}
				if (productInfo.getYesInvest() != null) {
					yesInvest[i] = productInfo.getYesInvest();
				} else {
					yesInvest[i] = 0;
				}
				if (productInfo.getYesCredit() != null) {
					yesCredit[i] = productInfo.getYesCredit();
				} else {
					yesCredit[i] = 0;
				}
				if (productInfo.getWaitRepay() != null && productInfo.getWaitRepay().compareTo(BigDecimal.ZERO) > 0) {
					waitRepay[i] = productInfo.getWaitRepay().divide(new BigDecimal(10000), 2);
				} else {
					waitRepay[i] = BigDecimal.ZERO;
				}
				if (productInfo.getYesRepay() != null && productInfo.getYesRepay().compareTo(BigDecimal.ZERO) > 0) {
					yesRepay[i] = productInfo.getYesRepay().divide(new BigDecimal(10000), 2);
				} else {
					yesRepay[i] = BigDecimal.ZERO;
				}
				if (productInfo.getPlanRepayYes() != null
						&& productInfo.getPlanRepayYes().compareTo(BigDecimal.ZERO) > 0) {
					planRepayYes[i] = productInfo.getPlanRepayYes().divide(new BigDecimal(10000), 2);
				} else {
					planRepayYes[i] = BigDecimal.ZERO;
				}
				if (productInfo.getPlanRepayWait() != null
						&& productInfo.getPlanRepayWait().compareTo(BigDecimal.ZERO) > 0) {
					planRepayWait[i] = productInfo.getPlanRepayWait().divide(new BigDecimal(10000), 2);
				} else {
					planRepayWait[i] = BigDecimal.ZERO;
				}
				// 计划加入总额
				if (productInfo.getPlanAccedeAll() != null
						&& productInfo.getPlanAccedeAll().compareTo(BigDecimal.ZERO) > 0) {
					planAccedeAll[i] = productInfo.getPlanAccedeAll().divide(new BigDecimal(10000), 2);
				} else {
					planAccedeAll[i] = BigDecimal.ZERO;
				}
				// 到期公允价值
				if (productInfo.getExpireFairValue() != null
						&& productInfo.getExpireFairValue().compareTo(BigDecimal.ZERO) > 0) {
					expireFairValue[i] = productInfo.getExpireFairValue().divide(new BigDecimal(10000), 2);
				} else {
					expireFairValue[i] = BigDecimal.ZERO;
				}
			}
		}
		if (StringUtils.isNotEmpty(viewFlag)) {
			if (viewFlag.equals("1-1")) {
				map.put("days", hour);
				map.put("data", waitInvest);
			} else if (viewFlag.equals("1-2")) {
				map.put("days", days);
				map.put("data", waitInvest);
			} else if (viewFlag.equals("2-1")) {
				map.put("days", hour);
				map.put("data", waitCredit);
			} else if (viewFlag.equals("2-2")) {
				map.put("days", days);
				map.put("data", waitCredit);
			} else if (viewFlag.equals("3-1")) {
				map.put("days", hour);
				map.put("data", yesInvest);
			} else if (viewFlag.equals("3-2")) {
				map.put("days", days);
				map.put("data", yesInvest);
			} else if (viewFlag.equals("4-1")) {
				map.put("days", hour);
				map.put("data", yesCredit);
			} else if (viewFlag.equals("4-2")) {
				map.put("days", days);
				map.put("data", yesCredit);
			} else if (viewFlag.equals("7-1")) {
				map.put("days", hour);
				map.put("data", waitRepay);
			} else if (viewFlag.equals("7-2")) {
				map.put("days", days);
				map.put("data", waitRepay);
			} else if (viewFlag.equals("8-1")) {
				map.put("days", hour);
				map.put("data", yesRepay);
			} else if (viewFlag.equals("8-2")) {
				map.put("days", days);
				map.put("data", yesRepay);
			} else if (viewFlag.equals("9-1")) {
				map.put("days", days);
				map.put("data", planAccedeAll);
			} else if (viewFlag.equals("9-2")) {
				map.put("days", hour);
				map.put("data", planAccedeAll);
			} else if (viewFlag.equals("10-1")) {
				map.put("days", days);
				map.put("data", planRepayWait);
			} else if (viewFlag.equals("10-2")) {
				map.put("days", hour);
				map.put("data", planRepayWait);
			} else if (viewFlag.equals("11-1")) {
				map.put("days", days);
				map.put("data", planRepayYes);
			} else if (viewFlag.equals("11-2")) {
				map.put("days", hour);
				map.put("data", planRepayYes);
			} else if (viewFlag.equals("12-1")) {
				map.put("days", days);
				map.put("data", expireFairValue);
			} else if (viewFlag.equals("12-2")) {
				map.put("days", hour);
				map.put("data", expireFairValue);
			}
		}
		String rep = JSON.toJSONString(map);
		LogUtil.endLog(THIS_CLASS, StatisDefine.SEARCH);
		return rep;
	}

	/**
	 * 数据分布
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = StatisDefine.COUNT_ACTION, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String countAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, StatisDefine.COUNT_ACTION);
		PlanStatisCustomize PlanStatisCustomize = new PlanStatisCustomize();
		String viewFlag = request.getParameter("viewFlag");

		String daySearch = request.getParameter("daySearch");
		/*
		 * String timeStart = request.getParameter("timeStart"); String timeEnd
		 * = request.getParameter("timeEnd"); if
		 * (StringUtils.isNotEmpty(timeStart)) {
		 * PlanStatisCustomize.setTimeStart(timeStart); } if
		 * (StringUtils.isNotEmpty(timeEnd)) {
		 * PlanStatisCustomize.setTimeEnd(timeEnd); }
		 */
		if (StringUtils.isNotEmpty(daySearch)) {
			PlanStatisCustomize.setDaySearch(daySearch);
		}
		if (StringUtils.isNotEmpty(viewFlag)) {
			if (viewFlag.equals("5-1") || viewFlag.equals("5-2") || viewFlag.equals("6-1") || viewFlag.equals("6-2")) {
				return view56(PlanStatisCustomize, viewFlag);
			}
			if (viewFlag.equals("14-1") || viewFlag.equals("14-2") || viewFlag.equals("15-1")
					|| viewFlag.equals("15-2") || viewFlag.equals("16-1") || viewFlag.equals("16-2")) {
				return view14(PlanStatisCustomize, viewFlag);
			}
			if (viewFlag.equals("17-1") || viewFlag.equals("17-2") || viewFlag.equals("18-1")
					|| viewFlag.equals("18-2")) {
				return view17(PlanStatisCustomize, viewFlag);
			}
		}
		LogUtil.endLog(THIS_CLASS, StatisDefine.COUNT_ACTION);
		return null;
	}

	/**
	 * 出借人本金 金额分布 人数分布
	 * 
	 * @param PlanStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view56(PlanStatisCustomize PlanStatisCustomize, String viewFlag) {
		// 返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		PlanStatisCustomize.setSort("asc");
		List<PlanStatisCustomize> productinfolist = this.planStatisService.getRecordList(PlanStatisCustomize);
		int size = productinfolist.size();
		// 统计值
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		int count5 = 0;
		int count6 = 0;
		if (size > 0) {
			PlanStatisCustomize productInfo = productinfolist.get(productinfolist.size() - 1);
			if (StringUtils.isNotEmpty(viewFlag)) {
				if (viewFlag.equals("5-1") || viewFlag.equals("5-2")) {
					count1 = productInfo.getInvestPeriodOne();
					count2 = productInfo.getInvestPeriodTwoFour();
					count3 = productInfo.getInvestPeriodFiveEight();
					count4 = productInfo.getInvestPeriodNineTwel();
					count5 = productInfo.getInvestPeriodTwelTf();
					count6 = productInfo.getInvestPeriodTf();

				}
				if (viewFlag.equals("6-1") || viewFlag.equals("6-2")) {
					count1 = productInfo.getCreditPeriodOne();
					count2 = productInfo.getCreditPeriodTwoFour();
					count3 = productInfo.getCreditPeriodFiveEight();
					count4 = productInfo.getCreditPeriodNineTwel();
					count5 = productInfo.getCreditPeriodTwelTf();
					count6 = productInfo.getCreditPeriodTf();
				}
			}
		}
		map.put("count1", count1);
		map.put("count2", count2);
		map.put("count3", count3);
		map.put("count4", count4);
		map.put("count5", count5);
		map.put("count6", count6);
		String rep = JSON.toJSONString(map);
		return rep;
	}

	/**
	 * 加入
	 * 
	 * @param PlanStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view14(PlanStatisCustomize PlanStatisCustomize, String viewFlag) {
		// 返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> productinfolist = this.planStatisService.getAccedeInfo(PlanStatisCustomize);
		int size = productinfolist.size();
		// 统计值
		BigDecimal count1 = BigDecimal.ZERO;
		BigDecimal count2 = BigDecimal.ZERO;
		BigDecimal count3 = BigDecimal.ZERO;
		BigDecimal count4 = BigDecimal.ZERO;
		BigDecimal count5 = BigDecimal.ZERO;
		BigDecimal count6 = BigDecimal.ZERO;
		if (size > 0) {
			Map<String, Object> productInfo = productinfolist.get(0);
			if (StringUtils.isNotEmpty(viewFlag)) {
				if (viewFlag.equals("14-1") || viewFlag.equals("14-2")) {
					// 加入金额分布-金额分布
					if (productInfo.get("accede_money_one") != null) {
						count1 = new BigDecimal(productInfo.get("accede_money_one") + "");
					}
					if (productInfo.get("accede_money_two") != null) {
						count2 = new BigDecimal(productInfo.get("accede_money_two") + "");
					}
					if (productInfo.get("accede_money_three") != null) {
						count3 = new BigDecimal(productInfo.get("accede_money_three") + "");
					}
					if (productInfo.get("accede_money_four") != null) {
						count4 = new BigDecimal(productInfo.get("accede_money_four") + "");
					}
					if (productInfo.get("accede_money_five") != null) {
						count5 = new BigDecimal(productInfo.get("accede_money_five") + "");
					}
					if (productInfo.get("accede_money_five_up") != null) {
						count6 = new BigDecimal(productInfo.get("accede_money_five_up") + "");
					}

				}
				if (viewFlag.equals("15-1") || viewFlag.equals("15-2")) {
					// 加入金额分布-人次分布
					if (productInfo.get("accede_money_count_one") != null) {
						count1 = new BigDecimal(productInfo.get("accede_money_count_one") + "");
					}
					if (productInfo.get("accede_money_count_two") != null) {
						count2 = new BigDecimal(productInfo.get("accede_money_count_two") + "");
					}
					if (productInfo.get("accede_money_count_three") != null) {
						count3 = new BigDecimal(productInfo.get("accede_money_count_three") + "");
					}
					if (productInfo.get("accede_money_count_four") != null) {
						count4 = new BigDecimal(productInfo.get("accede_money_count_four") + "");
					}
					if (productInfo.get("accede_money_count_five") != null) {
						count5 = new BigDecimal(productInfo.get("accede_money_count_five") + "");
					}
					if (productInfo.get("accede_money_count_five_up") != null) {
						count6 = new BigDecimal(productInfo.get("accede_money_count_five_up") + "");
					}
				}
				if (viewFlag.equals("16-1") || viewFlag.equals("16-2")) {
					// 加入次数分布-人次分布
					if (productInfo.get("accede_count_one") != null) {
						count1 = new BigDecimal(productInfo.get("accede_count_one") + "");
					}
					if (productInfo.get("accede_count_two_four") != null) {
						count2 = new BigDecimal(productInfo.get("accede_count_two_four") + "");
					}
					if (productInfo.get("accede_count_five_egiht") != null) {
						count3 = new BigDecimal(productInfo.get("accede_count_five_egiht") + "");
					}
					if (productInfo.get("accede_count_nine_fifteen") != null) {
						count4 = new BigDecimal(productInfo.get("accede_count_nine_fifteen") + "");
					}
					if (productInfo.get("accede_count_sixteen_thirty") != null) {
						count5 = new BigDecimal(productInfo.get("accede_count_sixteen_thirty") + "");
					}
					if (productInfo.get("accede_count_thirtyfirst_up") != null) {
						count6 = new BigDecimal(productInfo.get("accede_count_thirtyfirst_up") + "");
					}
				}
			}
		}
		map.put("count1", count1);
		map.put("count2", count2);
		map.put("count3", count3);
		map.put("count4", count4);
		map.put("count5", count5);
		map.put("count6", count6);
		String rep = JSON.toJSONString(map);
		return rep;
	}

	/**
	 * 平台分布
	 * 
	 * @param PlanStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view17(PlanStatisCustomize PlanStatisCustomize, String viewFlag) {
		// 返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> productinfolist = this.planStatisService.getAccedeInfo(PlanStatisCustomize);
		int size = productinfolist.size();
		// 统计值
		BigDecimal count1 = BigDecimal.ZERO;
		BigDecimal count2 = BigDecimal.ZERO;
		BigDecimal count3 = BigDecimal.ZERO;
		BigDecimal count4 = BigDecimal.ZERO;
		if (size > 0) {
			Map<String, Object> productInfo = productinfolist.get(0);
			if (StringUtils.isNotEmpty(viewFlag)) {
				if (viewFlag.equals("17-1") || viewFlag.equals("17-2")) {
					// 加入金额分布-金额分布
					if (productInfo.get("accede_client_money_pc") != null) {
						count1 = new BigDecimal(productInfo.get("accede_client_money_pc") + "");
					}
					if (productInfo.get("accede_client_money_wei") != null) {
						count2 = new BigDecimal(productInfo.get("accede_client_money_wei") + "");
					}
					if (productInfo.get("accede_client_money_ios") != null) {
						count3 = new BigDecimal(productInfo.get("accede_client_money_ios") + "");
					}
					if (productInfo.get("accede_client_money_android") != null) {
						count4 = new BigDecimal(productInfo.get("accede_client_money_android") + "");
					}
				}
				if (viewFlag.equals("18-1") || viewFlag.equals("18-2")) {
					// 加入金额分布-人次分布
					if (productInfo.get("accede_client_count_pc") != null) {
						count1 = new BigDecimal(productInfo.get("accede_client_count_pc") + "");
					}
					if (productInfo.get("accede_client_count_wei") != null) {
						count2 = new BigDecimal(productInfo.get("accede_client_count_wei") + "");
					}
					if (productInfo.get("accede_client_count_ios") != null) {
						count3 = new BigDecimal(productInfo.get("accede_client_count_ios") + "");
					}
					if (productInfo.get("accede_client_count_android") != null) {
						count4 = new BigDecimal(productInfo.get("accede_client_count_android") + "");
					}
				}
			}
		}
		map.put("count1", count1);
		map.put("count2", count2);
		map.put("count3", count3);
		map.put("count4", count4);
		String rep = JSON.toJSONString(map);
		return rep;
	}

}
