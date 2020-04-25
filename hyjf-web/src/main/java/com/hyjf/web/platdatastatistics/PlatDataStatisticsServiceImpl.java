package com.hyjf.web.platdatastatistics;

import java.math.BigDecimal;
import java.util.List;

import com.hyjf.mongo.operationreport.dao.TotalInvestAndInterestMongoDao;
import com.hyjf.mongo.operationreport.entity.TotalInvestAndInterestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.hyjf.mybatis.model.auto.BorrowUserStatistic;
import com.hyjf.mybatis.model.auto.BorrowUserStatisticExample;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.mybatis.model.auto.CalculateInvestInterestExample;
import com.hyjf.web.BaseServiceImpl;

/**
 * 平台数据统计Service实现类
 *
 * @author liuyang
 */
@Service
public class PlatDataStatisticsServiceImpl extends BaseServiceImpl implements PlatDataStatisticsService {

    @Autowired
    private TotalInvestAndInterestMongoDao totalInvestAndInterestMongoDao;

    /**
     * 获取累计出借累计收益
     *
     * @return
     */
    @Override
    public CalculateInvestInterest selectCalculateInvestInterest() {
        CalculateInvestInterestExample example = new CalculateInvestInterestExample();
        CalculateInvestInterestExample.Criteria cra = example.createCriteria();
        List<CalculateInvestInterest> list = this.calculateInvestInterestMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 累计交易总数
     *
     * @return
     */
    @Override
    public Integer selectTenderCounts() {
        // 汇直投交易笔数
        Integer hztTenderCount = dataCustomizeMapper.selectHztTenderCount();
        // 汇添金交易笔数
        Integer htjTenderCount = dataCustomizeMapper.selectHtjTenderCount();
        // 汇转让交易笔数
        Integer hzrTenderCount = dataCustomizeMapper.selectHzrTenderCount();
        Integer total = hztTenderCount + htjTenderCount + hzrTenderCount;
        return total;
    }

    /**
     * 累计注册数
     * @return
     */
    @Override
    public Integer selectUserRegisterCounts() {
        // 汇转让交易笔数
        Integer userRegistCount = dataCustomizeMapper.selectUserRegistCount();
        return userRegistCount;
    }

    @Override
    public BigDecimal selectRepayTotal() {
        BigDecimal repayAwaitTotal = dataCustomizeMapper.selectRepayTotal();
        return repayAwaitTotal;
    }
    
    /**
     * 检索运营统计数据
     * @return
     */
    @Override
	public BorrowUserStatistic selectBorrowUserStatistic(){
    	BorrowUserStatisticExample example = new BorrowUserStatisticExample();
    	List<BorrowUserStatistic> list = borrowUserStatisticMapper.selectByExample(example);
    	
    	if(list == null || list.isEmpty()){
    		return null;
    	}
    	
    	return list.get(0);
    }

    @Override
    public BigDecimal selectTotalInvest() {
        TotalInvestAndInterestEntity entity = getTotalInvestAndInterestEntity();
        if (entity != null) {
            return entity.getTotalInvestAmount();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal selectTotalInterest() {
        TotalInvestAndInterestEntity entity = getTotalInvestAndInterestEntity();
        if (entity != null) {
            return entity.getTotalInterestAmount();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public Integer selectTotalTradeSum() {
        TotalInvestAndInterestEntity entity = getTotalInvestAndInterestEntity();
        if (entity != null) {
            return entity.getTotalInvestNum();
        }
        return 0;
    }

    private TotalInvestAndInterestEntity getTotalInvestAndInterestEntity() {
        return totalInvestAndInterestMongoDao.findOne(new Query());
    }

}
