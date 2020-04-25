package com.hyjf.web.stock;

import java.util.List;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.StockInfo;
import com.hyjf.mybatis.model.auto.StockInfoExample;
import com.hyjf.mybatis.model.customize.web.StockInfo2Customize;
import com.hyjf.mybatis.model.customize.web.StockInfoCustomize;
import com.hyjf.web.BaseServiceImpl;

@Service
public class StockInfoServiceImpl extends BaseServiceImpl implements StockInfoService {

	/**
	 * 查询股票数据
	 * @return
	 */

	public List<StockInfo> queryStockInfoList(String type){

		StockInfoCustomize stockInfoCustomize= new StockInfoCustomize();
//		Long nowtime= (new Date().getTime())/1000;
		long current=System.currentTimeMillis();//当前时间毫秒数  
		long zero= (current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000; //今天零点零分零秒的秒数  
		long twelve=(zero*1000+24*60*60*1000-1)/1000; //今天23点59分59秒的秒数 
		if("1".equals(type)){//分时图
			stockInfoCustomize.setTimeBegin((int)zero);
			stockInfoCustomize.setTimeEnd((int)twelve);
		}else if("2".equals(type)){// 5日图
			long fivedaysAgo=System.currentTimeMillis()-24*60*60*1000*5;//5天前的这一时间的毫秒数 
			long fivezero= (fivedaysAgo/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000; //5天前零点零分零秒的秒数  
			stockInfoCustomize.setTimeBegin((int)fivezero);
			stockInfoCustomize.setTimeEnd((int)twelve);
			stockInfoCustomize.setTimeBetween(10);//间隔十分钟 
		}else if("3".equals(type)){// 1月图
			long thirtydaysAgo=System.currentTimeMillis()-24*60*60*1000*30;//30天前的这一时间的毫秒数 
			long thirtyzero= (thirtydaysAgo/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000; //30天前零点零分零秒的秒数  
			stockInfoCustomize.setTimeBegin((int)thirtyzero);
			stockInfoCustomize.setTimeEnd((int)twelve);
			stockInfoCustomize.setTimeBetween(24*60);//间隔一天
		}else if("4".equals(type)){// 1年图
			long oneyearAgo=System.currentTimeMillis()-24*60*60*1000*360;//360天前的这一时间的毫秒数 
			long oneyearzero= (oneyearAgo/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000; //360天前零点零分零秒的秒数  
			stockInfoCustomize.setTimeBegin((int)oneyearzero);
			stockInfoCustomize.setTimeEnd((int)twelve);
			stockInfoCustomize.setTimeBetween(24*60);//间隔一天
		}

		List<StockInfo> result = stockInfoCustomizeMapper.queryStockInfos(stockInfoCustomize);
		return result;
	}
	
	/**
	 * 查询股票数据
	 * @return
	 */

	public List<StockInfo2Customize> queryStockInfoList2(String type){
		List<StockInfo2Customize> result = null;
		StockInfoCustomize stockInfoCustomize= new StockInfoCustomize();
//		Long nowtime= (new Date().getTime())/1000;
		long current=System.currentTimeMillis();//当前时间毫秒数  
//		long zero= (current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000; //今天零点零分零秒的秒数  
//		long twelve=(zero*1000+24*60*60*1000-1)/1000; //今天23点59分59秒的秒数 
		long zero= (current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset()-5*3600*1000)/1000; //昨天19点的秒数  
		long twelve=(zero*1000+11*60*60*1000)/1000; //今天6点的秒数 
		if("1".equals(type)){//分时图
			stockInfoCustomize.setTimeBegin((int)zero);
			stockInfoCustomize.setTimeEnd((int)twelve);
			result = stockInfoCustomizeMapper.queryStockInfos2(stockInfoCustomize);
		}else if("2".equals(type)){// 5日图
			long fivedaysAgo=current/1000-24*60*60*5;//5天前的这一时间的毫秒数 
//			long fivezero= (fivedaysAgo/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000; //5天前零点零分零秒的秒数  
			long fivezero= (fivedaysAgo*1000/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset()-5*3600*1000)/1000; //6天前19点的秒数  
			stockInfoCustomize.setTimeBegin((int)fivezero);
			stockInfoCustomize.setTimeEnd((int)twelve);
			stockInfoCustomize.setTimeBetween(10);//间隔十分钟 
			result = stockInfoCustomizeMapper.queryStockInfos2(stockInfoCustomize);
		}else if("3".equals(type)){// 1月图
			long thirtydaysAgo=current/1000-24*60*60*30;//30天前的这一时间的毫秒数 
//			long thirtyzero= (thirtydaysAgo/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000; //30天前零点零分零秒的秒数  
			long thirtyzero= (thirtydaysAgo*1000/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset()-5*3600*1000)/1000; //31天前19点的秒数  
			stockInfoCustomize.setTimeBegin((int)thirtyzero);
			stockInfoCustomize.setTimeEnd((int)twelve);
			stockInfoCustomize.setTimeBetween(11*60);//间隔一天 （美国开盘时间从北京时间晚上19点到次日凌晨6点，共11小时）
			result = stockInfoCustomizeMapper.queryStockInfosForMonthYear(stockInfoCustomize);
		}else if("4".equals(type)){// 1年图
			long oneyearAgo=current/1000-24*60*60*360;//360天前的这一时间的秒数 
//			long oneyearzero= (oneyearAgo/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000; //360天前零点零分零秒的秒数  
			long oneyearzero= (oneyearAgo*1000/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset()-5*3600*1000)/1000; //361天前19点的秒数  
			
			stockInfoCustomize.setTimeBegin((int)oneyearzero);
			stockInfoCustomize.setTimeEnd((int)twelve);
			stockInfoCustomize.setTimeBetween(11*60);//间隔一天（美国开盘时间从北京时间晚上19点到次日凌晨6点，共11小时）
			result = stockInfoCustomizeMapper.queryStockInfosForMonthYear(stockInfoCustomize);
		}
		
//		List<StockInfo2Customize> result = stockInfoCustomizeMapper.queryStockInfos2(stockInfoCustomize);
		return result;
	}
	
	/**
	 * 查询当前股票数据
	 * @return
	 */

	public StockInfo queryStockInfo(){
		StockInfoExample example= new StockInfoExample();
//		StockInfoExample.Criteria cri = example.createCriteria();
		example.setOrderByClause(" id desc ");
		example.setLimitStart(0);
		example.setLimitEnd(1);
		List<StockInfo> s= stockInfoMapper.selectByExample(example);
		if(s!=null && s.size()>0){
			return s.get(0);
		}
		return null;
	}
	
//	public static void main(String[] args){
//		long current=System.currentTimeMillis();//当前时间毫秒数  
//		long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数  
//		long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数  
////		System.out.println((new Date().getTime())/1000);
//		System.out.println(current/1000);
//		System.out.println(zero/1000);
//		System.out.println((System.currentTimeMillis()-24*60*60*1000*5)/1000);
//System.out.println(((System.currentTimeMillis()-24*60*60*1000*5)/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset())/1000);
//
//		System.out.println(24/4*4);
//	}
}
