package com.hyjf.activity.mgm10.prizechange;

import com.hyjf.base.bean.BaseResultBean;


public class PrizeChangeResultBean extends BaseResultBean {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 失败原因编码  0：校验成功  1：请求参数不正确  2：用户推荐星不足  3：兑奖数量需要大于0  4：奖品不足  5：对应奖品不存在
     */
    private String errCode = "0";

    
    // 奖品名称
    private String prizeName;
    
    private String prizePicUrl;
    // 奖品备注说明
    private String remark;
    // 奖品抽中数量
    private int prizeCount;
    // 抽奖消耗的推荐星数量
    private int recommendCost;
    // 成功提示消息
    private String successMsg;
    
    public String getPrizeName() {
        return prizeName;
    }
    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public int getPrizeCount() {
        return prizeCount;
    }
    public void setPrizeCount(int prizeCount) {
        this.prizeCount = prizeCount;
    }
    public int getRecommendCost() {
        return recommendCost;
    }
    public void setRecommendCost(int recommendCost) {
        this.recommendCost = recommendCost;
    }
    public String getPrizePicUrl() {
        return prizePicUrl;
    }
    public void setPrizePicUrl(String prizePicUrl) {
        this.prizePicUrl = prizePicUrl;
    }
    public String getSuccessMsg() {
        return successMsg;
    }
    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }
    public String getErrCode() {
        return errCode;
    }
    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    
    
}
