package com.hyjf.admin.manager.borrow.borrowregist;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.BorrowRegistCustomize;

public interface BorrowRegistService extends BaseService {

	/**
	 * 数据合计
	 * 
	 * @return
	 */
	public Integer countBorrowRegist(BorrowCommonCustomize corrowCommonCustomize);

	/**
	 * 获取列表
	 * 
	 * @return
	 */
	public List<BorrowRegistCustomize> selectBorrowRegistList(BorrowCommonCustomize corrowCommonCustomize);
	
	/**
	 * 统计总额
	 * 
	 * @param corrowCommonCustomize
	 * @return
	 */
	public String sumBorrowRegistAccount(BorrowCommonCustomize corrowCommonCustomize);

	/**
	 * 借款人标的登记
	 * @param borrowNid
	 * @param result
	 * @return
	 */
	public JSONObject debtRegist(String borrowNid, JSONObject result);
	
	/**
     * 
     * 根据用户id查询用户签约授权信息
     * @author pcc
     * @param userId
     * @return
     */
    HjhUserAuth getHjhUserAuthByUserID(Integer userId);
    
	/**
     * 根据borrorNid查询资产表
     * @return
     */
    public List<HjhPlanAsset> getAssetListByBorrowNid(String borrowNid);
    
    /**
     * 发送初审队列消息
     * 
     * @param borrowNid
     * @param routingKey
     */
    void sendToMQ(String nid,String InstCode,String routingKey);
    
    /**
	 * 资产备案成功后更新资产表
	 * 
	 * @param hjhPlanAsset
	 * @return
	 */
     boolean updateRecordBorrow(HjhPlanAsset hjhPlanAsset,HjhAssetBorrowType hjhAssetBorrowType);
     
}
