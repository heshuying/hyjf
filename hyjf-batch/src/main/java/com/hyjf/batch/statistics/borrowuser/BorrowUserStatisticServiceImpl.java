package com.hyjf.batch.statistics.borrowuser;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.BorrowUserStatistic;
import com.hyjf.mybatis.model.auto.BorrowUserStatisticExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 运营数据统计（借款人相关）
 * @author hesy
 *
 */
@Service
public class BorrowUserStatisticServiceImpl extends BaseServiceImpl implements BorrowUserStatisticService{
    
    Logger _log = LoggerFactory.getLogger(BorrowUserStatisticServiceImpl.class);
    
    
    /**
     * 
     * 运营数据统计（借款人相关）
     * @author hsy
     */
    @Override
	public void statistic(){
    	// 累计借款人（定义：系统累计到现在进行过发表的底层借款人数量）
    	Integer countBorrowUser = borrowUserStatisticCustomizeMapper.countBorrowUser(new HashMap<String,Object>());
    	// 当前借款人（定义：当前有尚未结清债权的底层借款人数量）
    	Integer countBorrowUserCurrent = borrowUserStatisticCustomizeMapper.countCurrentBorrowUser(new HashMap<String,Object>());
    	// 当前出借人（定义：当前代还金额不为0的用户数量）
    	Integer countCurrentTenderUser = borrowUserStatisticCustomizeMapper.countCurrentTenderUser(new HashMap<String,Object>());
        Calendar calendar = Calendar.getInstance();
        // 要统计前一个月的数据，所以月份要减一
        calendar.add(Calendar.MONTH, -1);
    	// 代还总金额
    	BigDecimal sumBorrowUserMoney = borrowUserStatisticCustomizeMapper.sumBorrowUserMoney(getLastDay(calendar));
    	// 前十大借款人待还金额
    	BigDecimal sumBorrowUserMoneyTopTen = borrowUserStatisticCustomizeMapper.sumBorrowUserMoneyTopTen(new HashMap<String,Object>());
    	// 最大单一借款人待还金额
    	BigDecimal sumBorrowUserMoneyTopOne = borrowUserStatisticCustomizeMapper.sumBorrowUserMoneyTopOne(new HashMap<String,Object>());
    	
    	BorrowUserStatisticExample example = new BorrowUserStatisticExample();
    	List<BorrowUserStatistic> list = borrowUserStatisticMapper.selectByExample(example);
    	
    	BorrowUserStatistic record = new BorrowUserStatistic();
    	record.setBorrowuserCountTotal(countBorrowUser);
    	record.setBorrowuserCountCurrent(countBorrowUserCurrent);
    	record.setTenderuserCountCurrent(countCurrentTenderUser);
    	record.setBorrowuserMoneyTotal(sumBorrowUserMoney);
    	record.setBorrowuserMoneyTopone(sumBorrowUserMoneyTopOne);
    	record.setBorrowuserMoneyTopten(sumBorrowUserMoneyTopTen);
    	
    	if(list==null || list.isEmpty()){
    		// 第一次插入
    		record.setAddTime(GetDate.getNowTime10());
    		borrowUserStatisticMapper.insertSelective(record);
    	}else{
    		// 更新
    		record.setId(list.get(0).getId());
    		record.setUpdateTime(GetDate.getNowTime10());
    		borrowUserStatisticMapper.updateByPrimaryKey(record);
    	}
    }

    /**
     * 通过输入的日期，获取这个日期所在月份的最后一天
     *
     * @param cal
     * @return
     */
    public static Date getLastDay(Calendar cal) {
        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }


}
