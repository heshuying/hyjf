package com.hyjf.batch.cert.oldborrowuser;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.batch.cert.olduser.OldUserService;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mongo.hgdatareport.dao.CertBorrowDao;
import com.hyjf.mongo.hgdatareport.entity.CertBorrowEntity;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.customize.CertSendUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 合规数据上报 CERT 国家互联网应急中心 借款人老数据上报
 * @Author sunss
 * @Date 2018/11/30 14:20
 */
@Service
public class OldBorrowUserServiceImpl extends BaseServiceImpl implements OldBorrowUserService {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private CertBorrowDao certBorrowDao;

	@Autowired
	private MqService mqService;


	/**
	 * 发送Mq
	 * @param routingkey
	 */
	@Override
	public void sendToMQ(String routingkey) {
		// 调用MQ处理 用户信息
		Map<String, String> params = new HashMap<String, String>();
		params.put("nowtime", GetDate.getNowTime10()+"");
		this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, routingkey, JSONObject.toJSONString(params));
	}

	/**
	 * 检索未上报的在贷标的数量
	 *
	 * @return
	 */
	@Override
	public Long getCertUserCount() {
		// 查询未上报数量
		return certBorrowDao.getCertUserCount();
	}

	/**
	 * 初始化标的  改为dba同步
	 */
	@Override
	public void insertInitBorrow(List<String> list) {
		List<CertBorrowEntity> saveData = new ArrayList<>();
		for (String item:list) {
			CertBorrowEntity entity = new CertBorrowEntity(item);
			saveData.add(entity);
		}
		certBorrowDao.saveAll(saveData);
	}

	/**
	 * 获得所有在贷的标的
	 *
	 * @return
	 */
	@Override
	public List<String> getCertBorrowNotInit() {
		return borrowCustomizeMapper.getCertBorrowNotInit();
	}

	/**
	 * 检索未 上报的标的的数量
	 *
	 * @return
	 */
	@Override
	public Long getCertBorrowCount() {
		// 查询未上报数量
		return certBorrowDao.getCertBorrowCount();
	}

}

