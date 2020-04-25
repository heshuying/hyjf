package com.hyjf.bank.service.borrow.bail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.borrow.AssetServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowBail;
import com.hyjf.mybatis.model.auto.BorrowBailExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowTypeExample;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhPlanAssetExample;

@Service
public class AutoBailServiceImpl extends AssetServiceImpl implements AutoBailService {
	Logger _log = LoggerFactory.getLogger(AutoBailServiceImpl.class);

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

	/**
	 * 汇消费的项目类型编号
	 */
	public static String PROJECT_TYPE_HXF = "8";
	
	/**
	 * 查询已经初审中状态的资产
	 * 
	 * @param accountManageBean
	 * @return
	 */
	@Override
	public List<HjhPlanAsset> selectAutoAuditList() {
		
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria crt = example.createCriteria();
        crt.andVerifyStatusEqualTo(1);
        crt.andStatusEqualTo(5);//初审中
        
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
		
		return list;
	}
	
	/**
	 * 资产自动保证金审核
	 * 
	 * @param hjhPlanAsset
	 * @return
	 */

	@Override
	public boolean updateRecordBorrow(Borrow borrow,HjhAssetBorrowType hjhAssetBorrowType) {
		// 借款编号存在
		if (StringUtils.isNotEmpty(borrow.getBorrowNid())) {
			BorrowExample example = new BorrowExample();
			BorrowExample.Criteria cra = example.createCriteria();
			cra.andBorrowNidEqualTo(borrow.getBorrowNid());
			List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(example);
			if (borrowList != null && borrowList.size() == 1) {
				BorrowWithBLOBs borrowWithBLOBs = borrowList.get(0);
				// 该借款编号没有交过保证金
				BorrowBailExample exampleBail = new BorrowBailExample();
				BorrowBailExample.Criteria craBail = exampleBail.createCriteria();
				craBail.andBorrowNidEqualTo(borrowWithBLOBs.getBorrowNid());
				List<BorrowBail> borrowBailList = this.borrowBailMapper.selectByExample(exampleBail);
				if (borrowBailList == null || borrowBailList.size() == 0) {
//							AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
					BorrowBail borrowBail = new BorrowBail();
					// 借款人的ID
					borrowBail.setBorrowUid(borrowWithBLOBs.getUserId());
					// 操作人的ID
					borrowBail.setOperaterUid(1);
					// 借款编号
					borrowBail.setBorrowNid(borrowWithBLOBs.getBorrowNid());
					// 保证金数值
					BigDecimal bailPercent = new BigDecimal(this.getBorrowConfig(CustomConstants.BORROW_BAIL_RATE));// 计算公式：保证金金额=借款金额×3％
					BigDecimal accountBail = (borrowWithBLOBs.getAccount()).multiply(bailPercent).setScale(2, BigDecimal.ROUND_DOWN);
					borrowBail.setBailNum(accountBail);
					// 10位系统时间（到秒）
					borrowBail.setUpdatetime(GetDate.getNowTime10());
					boolean bailFlag = this.borrowBailMapper.insertSelective(borrowBail) > 0 ? true : false;
					if (bailFlag) {
						borrowWithBLOBs.setVerifyStatus(1);
						boolean borrowFlag = this.borrowMapper.updateByPrimaryKeyWithBLOBs(borrowWithBLOBs) > 0 ? true : false;
						if (borrowFlag) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获取资产项目类型
	 * 
	 * @return
	 */
	@Override
	public HjhAssetBorrowType selectAssetBorrowType(Borrow borrow) {
		HjhAssetBorrowTypeExample example = new HjhAssetBorrowTypeExample();
		HjhAssetBorrowTypeExample.Criteria cra = example.createCriteria();
		cra.andInstCodeEqualTo(borrow.getInstCode());
		cra.andAssetTypeEqualTo(borrow.getAssetType());
		cra.andIsOpenEqualTo(1);

		List<HjhAssetBorrowType> list = this.hjhAssetBorrowTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
    /**
     * 推送消息到MQ
     */
    @Override
    public void sendToMQ(Borrow borrow,String routingKey){
		// 成功后到关联计划队列
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("mqMsgId", GetCode.getRandomCode(10));
	    params.put("borrowNid", borrow.getBorrowNid());
	    params.put("instCode", borrow.getInstCode());
	    rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, routingKey, JSONObject.toJSONString(params));
    }
}
