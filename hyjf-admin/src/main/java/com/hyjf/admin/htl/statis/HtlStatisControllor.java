package com.hyjf.admin.htl.statis;

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
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.ProductList;
import com.hyjf.mybatis.model.auto.ProductRedeem;
import com.hyjf.mybatis.model.customize.ProductStatisCustomize;


/**
 * @package com.hyjf.admin.maintenance.Product
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = StatisDefine.REQUEST_MAPPING)
public class HtlStatisControllor extends BaseController {

    /** THIS_CLASS */
    private static final String THIS_CLASS = HtlStatisControllor.class.getName();
	@Autowired
	private ProductStatisService productStatisService;


	
	/**
	 * 统计画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(StatisDefine.INIT)
    @RequiresPermissions(StatisDefine.STATIS_VIEW)
	public ModelAndView init(HttpServletRequest request, RedirectAttributes attr, @ModelAttribute(StatisDefine.HTLSTATIS_FORM) ProductInfoBean form) {
		LogUtil.startLog(THIS_CLASS, StatisDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(StatisDefine.LIST_PATH);
		LogUtil.endLog(THIS_CLASS, StatisDefine.INIT);
		return modelAndView;
	}



	/**
	 * 出借总览
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = StatisDefine.SEARCH, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String searchAction(HttpServletRequest request, HttpServletResponse response,@ModelAttribute(StatisDefine.HTLSTATIS_FORM) ProductInfoBean form) {
		LogUtil.startLog(THIS_CLASS, StatisDefine.SEARCH);
		ProductStatisCustomize productStatisCustomize = new ProductStatisCustomize();
		String viewFlag  = request.getParameter("viewFlag");
		if(viewFlag.contains("-2")){
			productStatisCustomize.setvFlag(viewFlag);
		}
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		List<ProductStatisCustomize> productinfolist = this.productStatisService.getRecordList(productStatisCustomize);
		int size = productinfolist.size();
		String days[] = new String[size];//时间
		BigDecimal inAmount[] = new BigDecimal[size];//转入金额
		BigDecimal outAmount[] = new BigDecimal[size];//转出金额
		Integer inCount[] = new Integer[size]; //转入用户数
		Integer outCount[] = new Integer[size];//转出用户数
		BigDecimal investAmount[] = new BigDecimal[size];//出借金额
		BigDecimal loanBalance[] = new BigDecimal[size];//资管公司账户余额
		if(productinfolist.size() > 0){
			for(int i = 0;i<productinfolist.size();i++){
				ProductStatisCustomize productInfo = productinfolist.get(i);
				days[i] = productInfo.getDataDate();
				if(productInfo.getInAmount().compareTo(BigDecimal.ZERO) > 0){
					inAmount[i] = productInfo.getInAmount().divide(new BigDecimal(10000), 2);
				}else{
					inAmount[i] = BigDecimal.ZERO;
				}
				if(productInfo.getOutAmount().compareTo(BigDecimal.ZERO) > 0){
					outAmount[i] = productInfo.getOutAmount().divide(new BigDecimal(10000), 2);
				}else{
					outAmount[i] = BigDecimal.ZERO;
				}
				if(productInfo.getInvestAmount().compareTo(BigDecimal.ZERO) > 0){
					investAmount[i] = productInfo.getInvestAmount().divide(new BigDecimal(10000), 2);
				}else{
					investAmount[i] = BigDecimal.ZERO;
				}
				if(productInfo.getLoanBalance().compareTo(BigDecimal.ZERO) > 0){
					loanBalance[i] = productInfo.getLoanBalance().divide(new BigDecimal(10000), 2);
				}else{
					loanBalance[i] = BigDecimal.ZERO;
				}
				inCount[i] = productInfo.getInCount();
				outCount[i] = productInfo.getOutCount();
			}
		}
		if(StringUtils.isNotEmpty(viewFlag)){
			if(viewFlag.equals("1-1")){
				map.put("days", days);
				map.put("data",investAmount);
			}else if(viewFlag.equals("1-2")){
				map.put("days", days);
				map.put("data",investAmount);
			}else if(viewFlag.equals("2-1")){
				map.put("days", days);
				map.put("data",loanBalance);
			}else if(viewFlag.equals("2-2")){
				map.put("days", days);
				map.put("data",loanBalance);
			}else if(viewFlag.equals("3-1")){
				map.put("days", days);
				map.put("data",inAmount);
				map.put("data2",outAmount);
			}else if(viewFlag.equals("3-2")){
				map.put("days", days);
				map.put("data",inAmount);
				map.put("data2",outAmount);
			}else if(viewFlag.equals("4-1")){
				map.put("days", days);
				map.put("data",inCount);
				map.put("data2",outCount);
			}else if(viewFlag.equals("4-2")){
				map.put("days", days);
				map.put("data",inCount);
				map.put("data2",outCount);
			}
		}
		String rep = JSON.toJSONString(map);
		LogUtil.endLog(THIS_CLASS, StatisDefine.SEARCH);
		return rep;
	}
	
	/**
	 * 数据分布
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(value = StatisDefine.COUNT_ACTION, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String countAction(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(THIS_CLASS, StatisDefine.COUNT_ACTION);
		ProductStatisCustomize productStatisCustomize = new ProductStatisCustomize();
		String viewFlag  = request.getParameter("viewFlag");
		String timeStart = request.getParameter("timeStart");
		String timeEnd = request.getParameter("timeEnd");
		if(StringUtils.isNotEmpty(timeStart)){
			productStatisCustomize.setTimeStart(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayStart(timeStart)));
		}
		if(StringUtils.isNotEmpty(timeEnd)){
			productStatisCustomize.setTimeEnd(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.getDayEnd(timeEnd)));
		}
		if(StringUtils.isNotEmpty(viewFlag)){
			if(viewFlag.equals("5-1") || viewFlag.equals("6-1")){
				return view56(productStatisCustomize,viewFlag);
			}
			if(viewFlag.equals("7-1") || viewFlag.equals("7-2")){
				return view7(productStatisCustomize,viewFlag);
			}
			if(viewFlag.equals("8-1") || viewFlag.equals("8-2")){
				return view8(productStatisCustomize,viewFlag);
			}
			if(viewFlag.equals("9-1")){
				return view9(productStatisCustomize);
			}
			if(viewFlag.equals("10-1")){
				return view10(productStatisCustomize);
			}
			if(viewFlag.equals("11-1") || viewFlag.equals("11-2")){
				return view11(productStatisCustomize,viewFlag);
			}
			if(viewFlag.equals("12-1") || viewFlag.equals("12-2")){
				return view12(productStatisCustomize,viewFlag);
			}
			if(viewFlag.equals("13-1") || viewFlag.equals("13-2")){
				return view13(productStatisCustomize,viewFlag);
			}
			if(viewFlag.equals("14-1") || viewFlag.equals("14-2")){
				return view14(productStatisCustomize,viewFlag);
			}
			if(viewFlag.equals("15-1") || viewFlag.equals("15-2")){
				return view15(productStatisCustomize,viewFlag);
			}
			if(viewFlag.equals("16-1") || viewFlag.equals("16-2")){
				return view16(productStatisCustomize,viewFlag);
			}
		}
		LogUtil.endLog(THIS_CLASS, StatisDefine.COUNT_ACTION);
		return null;
	}

	
	/**
	 * 出借人本金   金额分布 人数分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view56(ProductStatisCustomize productStatisCustomize,String viewFlag){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		List<ProductStatisCustomize> productinfolist = this.productStatisService.selectUserProductPrincipal(productStatisCustomize);
		int size = productinfolist.size();
		//统计值
		BigDecimal count1 = BigDecimal.ZERO;
		BigDecimal count2 = BigDecimal.ZERO;
		BigDecimal count3 = BigDecimal.ZERO;
		BigDecimal count4 = BigDecimal.ZERO;
		BigDecimal count5 = BigDecimal.ZERO;
		BigDecimal count6 = BigDecimal.ZERO;
		if(size > 0){
			for(int i = 0;i<size;i++){
				ProductStatisCustomize productInfo = productinfolist.get(i);
				BigDecimal principal = productInfo.getPrincipal();
				if(principal != null){
					if(StringUtils.isNotEmpty(viewFlag)){
						if(viewFlag.equals("5-1")){
							if(principal.compareTo(new BigDecimal(10000)) < 0 ){ //小于一万
								count1 = count1.add(principal);
							}else if(principal.compareTo(new BigDecimal(10000)) >= 0 && principal.compareTo(new BigDecimal(20000)) < 0){
								count2 = count2.add(principal);
							}else if(principal.compareTo(new BigDecimal(20000)) >= 0 && principal.compareTo(new BigDecimal(30000)) < 0){
								count3 = count3.add(principal);
							}else if(principal.compareTo(new BigDecimal(30000)) >= 0 && principal.compareTo(new BigDecimal(40000)) < 0){
								count4 = count4.add(principal);
							}else if(principal.compareTo(new BigDecimal(40000)) >= 0 && principal.compareTo(new BigDecimal(50000)) < 0){
								count5 = count5.add(principal);
							}else{
								count6 = count6.add(principal);
							}
						}
						if(viewFlag.equals("6-1")){
							if(principal.compareTo(new BigDecimal(10000)) < 0 ){ //小于一万
								count1 = count1.add(BigDecimal.ONE);
							}else if(principal.compareTo(new BigDecimal(10000)) >= 0 && principal.compareTo(new BigDecimal(20000)) < 0){
								count2 = count2.add(BigDecimal.ONE);
							}else if(principal.compareTo(new BigDecimal(20000)) >= 0 && principal.compareTo(new BigDecimal(30000)) < 0){
								count3 = count3.add(BigDecimal.ONE);
							}else if(principal.compareTo(new BigDecimal(30000)) >= 0 && principal.compareTo(new BigDecimal(40000)) < 0){
								count4 = count4.add(BigDecimal.ONE);
							}else if(principal.compareTo(new BigDecimal(40000)) >= 0 && principal.compareTo(new BigDecimal(50000)) < 0){
								count5 = count5.add(BigDecimal.ONE);
							}else{
								count6 = count6.add(BigDecimal.ONE);
							}
						}
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
	 * 转出金额 人数分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view7(ProductStatisCustomize productStatisCustomize,String viewFlag){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		List<ProductRedeem> productlist = this.productStatisService.selectUserProductRedeemRecord(productStatisCustomize);
		int size = productlist.size();
		//统计值
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		int count5 = 0;
		int count6 = 0;
		if(size > 0){
			for(int i = 0;i<size;i++){
				ProductRedeem productInfo = productlist.get(i);
				BigDecimal redeemAmount = productInfo.getAmount();
				if(redeemAmount != null){
					if(redeemAmount.compareTo(new BigDecimal(10000)) < 0 ){ //小于一万
						count1++;
					}else if(redeemAmount.compareTo(new BigDecimal(10000)) >= 0 && redeemAmount.compareTo(new BigDecimal(20000)) < 0){
						count2++;
					}else if(redeemAmount.compareTo(new BigDecimal(20000)) >= 0 && redeemAmount.compareTo(new BigDecimal(30000)) < 0){
						count3++;
					}else if(redeemAmount.compareTo(new BigDecimal(30000)) >= 0 && redeemAmount.compareTo(new BigDecimal(40000)) < 0){
						count4++;
					}else if(redeemAmount.compareTo(new BigDecimal(40000)) >= 0 && redeemAmount.compareTo(new BigDecimal(50000)) < 0){
						count5++;
					}else{
						count6++;
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
	 * 转入金额 人数分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view8(ProductStatisCustomize productStatisCustomize,String viewFlag){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		List<ProductList> productlist = this.productStatisService.selectUserProductBuyRecord(productStatisCustomize);
		int size = productlist.size();
		//统计值
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		int count5 = 0;
		int count6 = 0;
		if(size > 0){
			for(int i = 0;i<size;i++){
				ProductList productInfo = productlist.get(i);
				BigDecimal buyAmount = productInfo.getAmount();
				if(buyAmount != null){
					if(buyAmount.compareTo(new BigDecimal(10000)) < 0 ){ //小于一万
						count1++;
					}else if(buyAmount.compareTo(new BigDecimal(10000)) >= 0 && buyAmount.compareTo(new BigDecimal(20000)) < 0){
						count2++;
					}else if(buyAmount.compareTo(new BigDecimal(20000)) >= 0 && buyAmount.compareTo(new BigDecimal(30000)) < 0){
						count3++;
					}else if(buyAmount.compareTo(new BigDecimal(30000)) >= 0 && buyAmount.compareTo(new BigDecimal(40000)) < 0){
						count4++;
					}else if(buyAmount.compareTo(new BigDecimal(40000)) >= 0 && buyAmount.compareTo(new BigDecimal(50000)) < 0){
						count5++;
					}else{
						count6++;
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
	 * 新老客户 人数分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view9(ProductStatisCustomize productStatisCustomize){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ProductStatisCustomize> productlist = this.productStatisService.selectUserIsNewBuy(productStatisCustomize);
			int size = productlist.size();
			//统计值
			int count1 = 0;
			int count2 = 0;
			int count3 = 0;
			int count4 = 0;
			if(size > 0){
				for(int i = 0;i<size;i++){
					ProductStatisCustomize productInfo = productlist.get(i);
					int regTime = productInfo.getRegTime();
					int endDate = GetDate.getNowTime10();
					int days = GetDate.daysBetween(regTime, endDate);
					if(days < 10){ //0-10天
						count1++;
					}else if(days >= 10 && days < 20){ //10-20天
						count2++;
					}else if(days >= 20 && days < 30){//20-30天
						count3++;
					}else if(days >= 30){
						count4++;
					}
				}
			}
			map.put("count1", count1);
			map.put("count2", count2);
			map.put("count3", count3);
			map.put("count4", count4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String rep = JSON.toJSONString(map);
		return rep;
	}
	/**
	 * 新老客户 本金金额分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view10(ProductStatisCustomize productStatisCustomize){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ProductStatisCustomize> productlist = this.productStatisService.selectUserIsNewPrincipal(productStatisCustomize);
			int size = productlist.size();
			//统计值
			BigDecimal count1 = BigDecimal.ZERO;
			BigDecimal count2 = BigDecimal.ZERO;
			BigDecimal count3 = BigDecimal.ZERO;
			BigDecimal count4 = BigDecimal.ZERO;
			if(size > 0){
				for(int i = 0;i<size;i++){
					ProductStatisCustomize productInfo = productlist.get(i);
					int regTime = productInfo.getRegTime();
					int endDate = GetDate.getNowTime10();
					int days = GetDate.daysBetween(regTime, endDate);
					if(days < 10){ //0-10天
						count1 = count1.add(productInfo.getPrincipal());
					}else if(days >= 10 && days < 20){ //10-20天
						count2 = count2.add(productInfo.getPrincipal());
					}else if(days >= 20 && days < 30){//20-30天
						count3 = count3.add(productInfo.getPrincipal());
					}else if(days >= 30){
						count4 = count4.add(productInfo.getPrincipal());
					}
				}
			}
			map.put("count1", count1);
			map.put("count2", count2);
			map.put("count3", count3);
			map.put("count4", count4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String rep = JSON.toJSONString(map);
		return rep;
	}

	/**
	 *  新老客户  转入金额分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view11(ProductStatisCustomize productStatisCustomize,String viewFlag){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ProductStatisCustomize> productlist = this.productStatisService.selectUserIsNewBuy(productStatisCustomize);
			int size = productlist.size();
			//统计值
			BigDecimal count1 = BigDecimal.ZERO;
			BigDecimal count2 = BigDecimal.ZERO;
			BigDecimal count3 = BigDecimal.ZERO;
			BigDecimal count4 = BigDecimal.ZERO;
			if(size > 0){
				for(int i = 0;i<size;i++){
					ProductStatisCustomize productInfo = productlist.get(i);
					int regTime = productInfo.getRegTime();
					int endDate = GetDate.getNowTime10();
					int days = GetDate.daysBetween(regTime, endDate);
					if(days < 10){ //0-10天
						count1 = count1.add(productInfo.getAmount());
					}else if(days >= 10 && days < 20){ //10-20天
						count2 = count2.add(productInfo.getAmount());
					}else if(days >= 20 && days < 30){//20-30天
						count3 = count3.add(productInfo.getAmount());
					}else if(days >= 30){
						count4 = count4.add(productInfo.getAmount());
					}
				}
			}
			map.put("count1", count1);
			map.put("count2", count2);
			map.put("count3", count3);
			map.put("count4", count4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String rep = JSON.toJSONString(map);
		return rep;
	}
	/**
	 *  新老客户  转出金额分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view12(ProductStatisCustomize productStatisCustomize,String viewFlag){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ProductStatisCustomize> productlist = this.productStatisService.selectUserIsNewRedeem(productStatisCustomize);
			int size = productlist.size();
			//统计值
			BigDecimal count1 = BigDecimal.ZERO;
			BigDecimal count2 = BigDecimal.ZERO;
			BigDecimal count3 = BigDecimal.ZERO;
			BigDecimal count4 = BigDecimal.ZERO;
			if(size > 0){
				for(int i = 0;i<size;i++){
					ProductStatisCustomize productInfo = productlist.get(i);
					int regTime = productInfo.getRegTime();
					int endDate = GetDate.getNowTime10();
					int days = GetDate.daysBetween(regTime, endDate);
					if(days < 10){ //0-10天
						count1 = count1.add(productInfo.getAmount());
					}else if(days >= 10 && days < 20){ //10-20天
						count2 = count2.add(productInfo.getAmount());
					}else if(days >= 20 && days < 30){//20-30天
						count3 = count3.add(productInfo.getAmount());
					}else if(days >= 30){
						count4 = count4.add(productInfo.getAmount());
					}
				}
			}
			map.put("count1", count1);
			map.put("count2", count2);
			map.put("count3", count3);
			map.put("count4", count4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String rep = JSON.toJSONString(map);
		return rep;
	}
	
	
	/**
	 * 平台分布 转入金额分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view13(ProductStatisCustomize productStatisCustomize,String viewFlag){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ProductList> productlist = this.productStatisService.selectUserProductBuyRecord(productStatisCustomize);
			int size = productlist.size();
			//统计值
			BigDecimal count1 = BigDecimal.ZERO;
			BigDecimal count2 = BigDecimal.ZERO;
			BigDecimal count3 = BigDecimal.ZERO;
			BigDecimal count4 = BigDecimal.ZERO;
			if(size > 0){
				for(int i = 0;i<size;i++){
					ProductList productInfo = productlist.get(i);
					int client = productInfo.getClient();
					BigDecimal buyAmount = productInfo.getAmount();
					switch (client) {
					case 0:
						count1 = count1.add(buyAmount);
						break;
					case 1:
						count2 = count2.add(buyAmount);
						break;
					case 2:
						count3 = count3.add(buyAmount);
						break;
					case 3:
						count4 = count4.add(buyAmount);
						break;
					default:
						break;
					}
				}
			}
			map.put("count1", count1);
			map.put("count2", count2);
			map.put("count3", count3);
			map.put("count4", count4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String rep = JSON.toJSONString(map);
		return rep;
	}

	/**
	 * 平台 转出金额分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view14(ProductStatisCustomize productStatisCustomize,String viewFlag){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		List<ProductRedeem> productlist = this.productStatisService.selectUserProductRedeemRecord(productStatisCustomize);
		int size = productlist.size();
		//统计值
		BigDecimal count1 = BigDecimal.ZERO;
		BigDecimal count2 = BigDecimal.ZERO;
		BigDecimal count3 = BigDecimal.ZERO;
		BigDecimal count4 = BigDecimal.ZERO;
		
		if(size > 0){
			for(int i = 0;i<size;i++){
				ProductRedeem productInfo = productlist.get(i);
				int client = productInfo.getClient();
				BigDecimal redeemAmount = productInfo.getAmount();
				switch (client) {
				case 0:
					count1 = count1.add(redeemAmount);
					break;
				case 1:
					count2 = count2.add(redeemAmount);
					break;
				case 2:
					count3 = count3.add(redeemAmount);
					break;
				case 3:
					count4 = count4.add(redeemAmount);
					break;
				default:
					break;
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

	/**
	 * 平台分布 转入用户分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view15(ProductStatisCustomize productStatisCustomize,String viewFlag){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ProductList> productlist = this.productStatisService.selectUserProductBuyRecord(productStatisCustomize);
			int size = productlist.size();
			//统计值
			int count1 = 0;
			int count2 = 0;
			int count3 = 0;
			int count4 = 0;
			if(size > 0){
				for(int i = 0;i<size;i++){
					ProductList productInfo = productlist.get(i);
					int client = productInfo.getClient();
					switch (client) {
					case 0:
						count1++;
						break;
					case 1:
						count2++;
						break;
					case 2:
						count3++;
						break;
					case 3:
						count4++;
						break;
					default:
						break;
					}
				}
			}
			map.put("count1", count1);
			map.put("count2", count2);
			map.put("count3", count3);
			map.put("count4", count4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String rep = JSON.toJSONString(map);
		return rep;
	}
	/**
	 * 平台分布 转出用户分布
	 * @param productStatisCustomize
	 * @param viewFlag
	 * @return
	 */
	public String view16(ProductStatisCustomize productStatisCustomize,String viewFlag){
		//返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<ProductRedeem> productlist = this.productStatisService.selectUserProductRedeemRecord(productStatisCustomize);
			int size = productlist.size();
			//统计值
			int count1 = 0;
			int count2 = 0;
			int count3 = 0;
			int count4 = 0;
			if(size > 0){
				for(int i = 0;i<size;i++){
					ProductRedeem productInfo = productlist.get(i);
					int client = productInfo.getClient();
					switch (client) {
					case 0:
						count1++;
						break;
					case 1:
						count2++;
						break;
					case 2:
						count3++;
						break;
					case 3:
						count4++;
						break;
					default:
						break;
					}
				}
			}
			map.put("count1", count1);
			map.put("count2", count2);
			map.put("count3", count3);
			map.put("count4", count4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String rep = JSON.toJSONString(map);
		return rep;
	}

	


}
