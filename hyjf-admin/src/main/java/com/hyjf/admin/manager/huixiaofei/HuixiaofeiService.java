package com.hyjf.admin.manager.huixiaofei;

import java.util.List;

import com.hyjf.mybatis.model.auto.Consume;
/**
 * 达飞金融-汇消费接口
 * @author 孙亮
 * @since 2015年12月14日 上午9:31:05
 */
import com.hyjf.mybatis.model.auto.ConsumeList;

public interface HuixiaofeiService {

	/**
	 * 根据条件获取汇消费汇总列表
	 * 
	 * @param bean
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<Consume> getConsumeRecordList(HuixiaofeiBean bean, int limitStart, int limitEnd);

	/**
	 * 根据ID获取汇消费汇总数据
	 * 
	 * @param id
	 * @return
	 */
	public Consume getRecordByID(Integer id);

	/**
	 * 根据条件获取汇消费详情List列表
	 * 
	 * @param bean
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	public List<ConsumeList> getConsumeListByCondition(HuixiaofeiListBean bean, int limitStart, int limitEnd);

	/**
	 * 下载达飞金融数据
	 */
	public void downLoadDataAction() throws Exception;

	/**
	 * 审核数据
	 */
	public void shenhe(List<Integer> ids, Integer updatestatus);

	/**
	 * 根据条件判断ConsumeList是否已经全部审核,true表示已经全部审核完,可以进行下一步操作,否则表示没有
	 */
	public Boolean isAllOver(String consumeID, String insertDay);

	/**
	 * 处理数据
	 * 
	 * @param id
	 *            consume的id
	 */
	public String chuli(Integer id) throws Exception;

}