package com.hyjf.activity.mgm10.prizedraw;

import java.math.BigDecimal;

/**
 * 
 * 中奖概率区间Bean
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年9月29日
 * @see 下午3:02:10
 */
public class PrizeGenerateBean {
    /**
     * 中奖区间基数100万
     */
    public static final int RANGE_BASE_NUM = 1000000;
    /**
     * 奖品分组编码
     */
    private String groupCode;
    
    /**
     * 中奖几率
     */
    private BigDecimal prizeProbability;
    
    /**
     * 奖品剩余数量
     */
    private Integer prizeRemaindCount;
    
    /**
     * 奖品类型
     */
    private String prizeType;
    
    /**
     * 中奖区间开始
     */
    private int rangeStart;
    
    /**
     * 中奖区间结束
     */
    private int rangeEnd;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public BigDecimal getPrizeProbability() {
        return prizeProbability;
    }

    public void setPrizeProbability(BigDecimal prizeProbability) {
        this.prizeProbability = prizeProbability;
    }

    public int getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(int rangeStart) {
        this.rangeStart = rangeStart;
    }

    public int getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(int rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public int getPrizeRemaindCount() {
        return prizeRemaindCount;
    }

    public void setPrizeRemaindCount(Integer prizeRemaindCount) {
        this.prizeRemaindCount = prizeRemaindCount;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }
    

    
    
}
