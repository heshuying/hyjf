package com.hyjf.api.server.assetpush;

import java.util.List;
import java.util.Map;

import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.*;

public interface PushService extends BaseService {

	/**
	 * 插入资产表
	 *
	 * @param bean
	 * @return
	 */
	int insertAssert(HjhPlanAsset record, Map<String, String> params);
	
	/**
     *获取开户的数据
     */
	BankOpenAccount selectBankAccountById(int userId);
	
    /**
     *获取用户信息
     */
    UsersInfo selectUserInfoByNameAndCard(String trueName,String idCrad);
    
    /**
     *获取用户的数据
     */
    Users selectUsersById(int userId);
    
    /**
     *获取机构信息
     */
    HjhAssetBorrowType selectAssetBorrowType(String instCode,int assetType);
    
    
    /**
     * 根据项目类型去还款方式
     * @param borrowcCd
     * @return
     */
    List<BorrowProjectRepay> selectProjectRepay(String borrowcCd);
    
    /**
     *获取受托支付电子账户列表
     */
    STZHWhiteList selectStzfWhiteList(String instCode,String stAccountid);
	
    /**
     * 推送消息到MQ
     */
    void sendToMQ(HjhPlanAsset hjhPlanAsset);

    /**
     *通过用户名获得用户的详细信息
     */
    Users selectUserInfoByUsername(String userName);

    /**
     *通过机构编号获得机构信息
     */
    CorpOpenAccountRecord selectUserBusiNameByUsername(String userName);

    /**
     * 通过用户id获得用户真实姓名和身份证号
     * @param userId
     * @return
     */
    UsersInfo selectUserInfoByUserId(Integer userId);

    /**
     * 检查是否存在重复资产
     * @param userId
     * @return
     */
    HjhPlanAsset checkDuplicateAssetId(String assetId);

    HjhBailConfig getBailConfig(String instCode);
}
