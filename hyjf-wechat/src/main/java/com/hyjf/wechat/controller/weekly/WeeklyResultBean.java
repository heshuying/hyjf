package com.hyjf.wechat.controller.weekly;

import java.util.List;

import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.Events;
import com.hyjf.wechat.base.BaseResultBean;

public class WeeklyResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    //是否开户,0未开户,1已开户
    int kaihu;
    public int getKaihu() {
		return kaihu;
	}
	public void setKaihu(int kaihu) {
		this.kaihu = kaihu;
	}
	public String getYonghuming() {
		return yonghuming;
	}
	public void setYonghuming(String yonghuming) {
		this.yonghuming = yonghuming;
	}
	public String getXingming() {
		return xingming;
	}
	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	public String getTouxiang() {
		return touxiang;
	}
	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}
	//用户名
    String yonghuming;
    //真实姓名
    String xingming;
    //头像
    String touxiang;
    //周一
    String beginDate;
    //周日
    String endDate;
    //7日日期
    String dateString;
    //总收益
    String zongshouyi;
    //出借百分比
    Integer baifenbi;
    //总天数
    Integer zongtianshu;
    //账户总金额
    String zongjine;
    //本周出借额
    String touzie;
    //本周累计笔数
    Integer bishu;
    //本周还款总额
    String huankuanzonge;
    //预计收益
    String shouyi;
    //本人是否有优惠券 0:无 1:有
    Integer youhuiquan;
    //本周出借额
    String benzhoutouzie;
    //本周收益
    String benzhoushouyi;
    //本周成交数
    Integer chengjiaoshu;
    //还款概况
    List<String> huankuangaikuang;
    //出借概况
    List<String> touzigaikuang;
    //活动列表
    List<ActivityList> huodong;
    //纪事列表
    List<Events> jishi;
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getZongshouyi() {
		return zongshouyi;
	}
	public void setZongshouyi(String zongshouyi) {
		this.zongshouyi = zongshouyi;
	}
	public Integer getBaifenbi() {
		return baifenbi;
	}
	public void setBaifenbi(Integer baifenbi) {
		this.baifenbi = baifenbi;
	}
	public Integer getZongtianshu() {
		return zongtianshu;
	}
	public void setZongtianshu(Integer zongtianshu) {
		this.zongtianshu = zongtianshu;
	}
	public String getZongjine() {
		return zongjine;
	}
	public void setZongjine(String zongjine) {
		this.zongjine = zongjine;
	}
	public Integer getBishu() {
		return bishu;
	}
	public void setBishu(Integer bishu) {
		this.bishu = bishu;
	}
	public String getHuankuanzonge() {
		return huankuanzonge;
	}
	public void setHuankuanzonge(String huankuanzonge) {
		this.huankuanzonge = huankuanzonge;
	}
	public String getShouyi() {
		return shouyi;
	}
	public void setShouyi(String shouyi) {
		this.shouyi = shouyi;
	}
	public Integer getYouhuiquan() {
		return youhuiquan;
	}
	public void setYouhuiquan(Integer youhuiquan) {
		this.youhuiquan = youhuiquan;
	}
	public String getTouzie() {
		return touzie;
	}
	public void setTouzie(String touzie) {
		this.touzie = touzie;
	}
	public List<String> getHuankuangaikuang() {
		return huankuangaikuang;
	}
	public void setHuankuangaikuang(List<String> huankuangaikuang) {
		this.huankuangaikuang = huankuangaikuang;
	}
	public List<String> getTouzigaikuang() {
		return touzigaikuang;
	}
	public void setTouzigaikuang(List<String> touzigaikuang) {
		this.touzigaikuang = touzigaikuang;
	}
	public List<ActivityList> getHuodong() {
		return huodong;
	}
	public void setHuodong(List<ActivityList> huodong) {
		this.huodong = huodong;
	}
	public List<Events> getJishi() {
		return jishi;
	}
	public void setJishi(List<Events> jishi) {
		this.jishi = jishi;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getBenzhoutouzie() {
		return benzhoutouzie;
	}
	public void setBenzhoutouzie(String benzhoutouzie) {
		this.benzhoutouzie = benzhoutouzie;
	}
	public String getBenzhoushouyi() {
		return benzhoushouyi;
	}
	public void setBenzhoushouyi(String benzhoushouyi) {
		this.benzhoushouyi = benzhoushouyi;
	}
	public Integer getChengjiaoshu() {
		return chengjiaoshu;
	}
	public void setChengjiaoshu(Integer chengjiaoshu) {
		this.chengjiaoshu = chengjiaoshu;
	}

    
}
