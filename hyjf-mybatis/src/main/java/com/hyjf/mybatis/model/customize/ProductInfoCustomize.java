package com.hyjf.mybatis.model.customize;

/**
  * @ClassName: ProductInfoCustomize   定时任务查询数据用
  * @Description: TODO
  * @author Michael
  * @date 2015年11月24日 下午2:38:14
 */
public class ProductInfoCustomize {

	private Integer userId;//用户id
	private Integer timeStart; // 开始时间（后台查询用）
    private Integer timeEnd; //结束时间（后台查询用）
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(Integer timeStart) {
		this.timeStart = timeStart;
	}
	public Integer getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(Integer timeEnd) {
		this.timeEnd = timeEnd;
	}
	
    
    

}
