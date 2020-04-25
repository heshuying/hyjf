package com.hyjf.bank.service.borrow.preaudit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.alibaba.fastjson.JSON;
import com.hyjf.bank.service.borrow.AssetServiceImpl;
import com.hyjf.bank.service.borrow.send.RedisBorrow;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowBail;
import com.hyjf.mybatis.model.auto.BorrowBailExample;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowTypeExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhPlanAssetExample;

@Service
public class AutoPreAuditServiceImpl extends AssetServiceImpl implements AutoPreAuditService {
	Logger _log = LoggerFactory.getLogger(AutoPreAuditServiceImpl.class);

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;
	

	public static JedisPool pool = RedisUtils.getPool();
	/**
	 * 汇消费的项目类型编号
	 */
	public static String PROJECT_TYPE_HXF = "8";
	
	/**
	 * 查询已经初审中状态的资产
	 * 
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
	 * 资产自动初审
	 * 
	 * @param hjhPlanAsset
	 * @return
	 */

	@Override
	public boolean updateRecordBorrow(HjhPlanAsset hjhPlanAsset,HjhAssetBorrowType hjhAssetBorrowType) {
		
		// 验证资产风险保证金是否足够（redis）,关联汇计划才验证
//		if (!checkAssetCanSend(hjhPlanAsset)) {
//			_log.info("资产编号："+hjhPlanAsset.getAssetId()+" 保证金不足");
//			//add by pcc 20180531 增加待补缴状态
//			HjhPlanAsset planAsset = new HjhPlanAsset();
//			planAsset.setId(hjhPlanAsset.getId());
//			planAsset.setStatus(1);//待补缴保证金
//			this.hjhPlanAssetMapper.updateByPrimaryKeySelective(planAsset);
//			//end
//			return false;
//		}
		
		// 风险保证金，初审
//		if(hjhAssetBorrowType.getAutoBail() != null && hjhAssetBorrowType.getAutoBail() == 1){
//			saveBailRecord(hjhPlanAsset.getBorrowNid());
//		}
		
		//修改发标状态 更新资产表，更新borrow
		if(hjhAssetBorrowType.getAutoAudit() != null && hjhAssetBorrowType.getAutoAudit() == 1){
			
			updateOntimeRecord(hjhPlanAsset,hjhAssetBorrowType);
			
			HjhPlanAsset hjhPlanAssetnew = new HjhPlanAsset();
			hjhPlanAssetnew.setId(hjhPlanAsset.getId());
			hjhPlanAssetnew.setStatus(7);//出借中
			//获取当前时间
			int nowTime = GetDate.getNowTime10();
			hjhPlanAssetnew.setUpdateTime(nowTime);
			hjhPlanAssetnew.setUpdateUserId(1);
			boolean borrowFlag = this.hjhPlanAssetMapper.updateByPrimaryKeySelective(hjhPlanAssetnew)>0?true:false;
			if(borrowFlag){
				return true;
			}
		}
		return false;
	}

	/**
	 * 手动录标自动备案-自动初审
	 * 
	 * @return
	 * @author PC-LIUSHOUYI
	 */
	@Override
	public boolean updateRecordBorrow(Borrow borrows) {

		//检查是否交过保证金
		String borrowNid = borrows.getBorrowNid();

		// 合规删除 modify by hesy 2018-12-06
		/*BorrowBailExample exampleBail = new BorrowBailExample();
		BorrowBailExample.Criteria craBail = exampleBail.createCriteria();
		craBail.andBorrowNidEqualTo(borrowNid);
		List<BorrowBail> borrowBailList = this.borrowBailMapper.selectByExample(exampleBail);
		// 该借款编号没有交过保证金
		if (borrowBailList == null || borrowBailList.size() == 0) {
			_log.info("该借款编号没有交过保证金 "+borrowNid);
			return false;
		}*/
		
		// 插入时间
		int systemNowDateLong = GetDate.getNowTime10();
		Date systemNowDate = GetDate.getDate(systemNowDateLong);
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);
		if (borrowList != null && borrowList.size() == 1) {
			BorrowWithBLOBs borrow = borrowList.get(0);
			// 剩余的金额
			borrow.setBorrowAccountWait(borrow.getAccount());
			int time = systemNowDateLong;
			
			// 是否可以进行借款
			borrow.setBorrowStatus(1);
			// 初审时间
			borrow.setVerifyTime(String.valueOf(GetDate.getNowTime10()));
			// 发标的状态
			borrow.setVerifyStatus(Integer.valueOf(4));
			// 状态
			borrow.setStatus(2);
			// 借款到期时间
			borrow.setBorrowEndTime(String.valueOf(time + borrow.getBorrowValidTime() * 86400));
			// 更新时间
			borrow.setUpdatetime(systemNowDate);
			this.borrowMapper.updateByExampleSelective(borrow, borrowExample);
			
			return true;
			
		}
		
		return false;
	}

	/**
	 * 交保证金（默认已交风险保证金）
	 * 
	 */
	private boolean saveBailRecord(String borrowPreNid) {
		// 借款编号存在
		if (StringUtils.isNotEmpty(borrowPreNid)) {
			BorrowExample example = new BorrowExample();
			BorrowExample.Criteria cra = example.createCriteria();
			cra.andBorrowNidEqualTo(borrowPreNid);
			List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(example);
			if (borrowList != null && borrowList.size() == 1) {
				BorrowWithBLOBs borrowWithBLOBs = borrowList.get(0);
				// 该借款编号没有交过保证金
				BorrowBailExample exampleBail = new BorrowBailExample();
				BorrowBailExample.Criteria craBail = exampleBail.createCriteria();
				craBail.andBorrowNidEqualTo(borrowWithBLOBs.getBorrowNid());
				List<BorrowBail> borrowBailList = this.borrowBailMapper.selectByExample(exampleBail);
				if (borrowBailList == null || borrowBailList.size() == 0) {
//					AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
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
	 * 发标，更新状态
	 */
	private boolean updateOntimeRecord(HjhPlanAsset hjhPlanAsset,HjhAssetBorrowType hjhAssetBorrowType) {
		
		//检查是否交过保证金
		String borrowNid = hjhPlanAsset.getBorrowNid();
		
		
		/*BorrowBailExample exampleBail = new BorrowBailExample();
		BorrowBailExample.Criteria craBail = exampleBail.createCriteria();
		craBail.andBorrowNidEqualTo(borrowNid);
		List<BorrowBail> borrowBailList = this.borrowBailMapper.selectByExample(exampleBail);
		// 该借款编号没有交过保证金
		if (borrowBailList == null || borrowBailList.size() == 0) {
			_log.info("该借款编号没有交过保证金 "+borrowNid);
			return false;
		}*/
		
		// 插入时间
		int systemNowDateLong = GetDate.getNowTime10();
		Date systemNowDate = GetDate.getDate(systemNowDateLong);
		BorrowExample borrowExample = new BorrowExample();
		BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
		borrowCra.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> borrowList = this.borrowMapper.selectByExampleWithBLOBs(borrowExample);
		if (borrowList != null && borrowList.size() == 1) {
			BorrowWithBLOBs borrow = borrowList.get(0);
			// 剩余的金额
			borrow.setBorrowAccountWait(borrow.getAccount());
			int time = systemNowDateLong;
			
			// 是否可以进行借款
			borrow.setBorrowStatus(1);
			// 初审时间
			borrow.setVerifyTime(String.valueOf(GetDate.getNowTime10()));
			// 发标的状态
			borrow.setVerifyStatus(Integer.valueOf(4));
			// 状态
			borrow.setStatus(2);
			// 借款到期时间
			borrow.setBorrowEndTime(String.valueOf(time + borrow.getBorrowValidTime() * 86400));
			// 更新时间
			borrow.setUpdatetime(systemNowDate);
			this.borrowMapper.updateByExampleSelective(borrow, borrowExample);
			
			return true;
			
		}
		
		return false;
	}
	
	
	/**
	 * 并发情况下保证设置一个值
	 * @param key
	 * @param value
	 */
	private void redisAdd(String key,String value){

		Jedis jedis = pool.getResource();
		
		while ("OK".equals(jedis.watch(key))) {
			List<Object> results = null;
			
			String balance = jedis.get(key);
			BigDecimal bal = new BigDecimal(0);
			if (balance != null) {
				bal =  new BigDecimal(balance);
			}
			BigDecimal val =  new BigDecimal(value);
			
			Transaction tx = jedis.multi();
			String valbeset = bal.add(val).toString();
			tx.set(key, valbeset);
			results = tx.exec();
			if (results == null || results.isEmpty()) {
				jedis.unwatch();
			} else {
				String ret = (String) results.get(0);
				if (ret != null && ret.equals("OK")) {
					// 成功后
					break;
				} else {
					jedis.unwatch();
				}
			}
		}
	}
	
	/**
	 * 并发情况下保证设置一个值
	 * @param key
	 * @param value
	 */
	private boolean redisSubstrack(String key,String value){

		Jedis jedis = pool.getResource();
		boolean result = false;
		
		while ("OK".equals(jedis.watch(key))) {
			List<Object> results = null;
			
			String balance = jedis.get(key);
			BigDecimal bal = new BigDecimal(balance);
			BigDecimal val = new BigDecimal(value);
			
			if(val.compareTo(bal)>0){
				return false;
			}
			
			Transaction tx = jedis.multi();
			String valbeset = bal.subtract(val).toString();
			tx.set(key, valbeset);
			results = tx.exec();
			if (results == null || results.isEmpty()) {
				jedis.unwatch();
			} else {
				String ret = (String) results.get(0);
				if (ret != null && ret.equals("OK")) {
					// 成功后
					result = true;
					break;
				} else {
					jedis.unwatch();
				}
			}
		}
		
		return result;
	}

	/**
	 * 获取资产项目类型
	 * 
	 * @return
	 */
	@Override
	public HjhAssetBorrowType selectAssetBorrowType(HjhPlanAsset hjhPlanAsset) {
		HjhAssetBorrowTypeExample example = new HjhAssetBorrowTypeExample();
		HjhAssetBorrowTypeExample.Criteria cra = example.createCriteria();
		cra.andInstCodeEqualTo(hjhPlanAsset.getInstCode());
		cra.andAssetTypeEqualTo(hjhPlanAsset.getAssetType());
		cra.andIsOpenEqualTo(1);

		List<HjhAssetBorrowType> list = this.hjhAssetBorrowTypeMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
		
	}
	
	/**
	 * 获取标的自动流程配置
	 * 
	 * @param borrow
	 * @return
	 * @author PC-LIUSHOUYI
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
	 * 验证资产风险保证金是否足够（redis）
	 * @param hjhPlanAsset
	 * @return
	 */
	private boolean checkAssetCanSend(HjhPlanAsset hjhPlanAsset) {
		String instCode = hjhPlanAsset.getInstCode();
		
		String capitalToplimit = RedisUtils.get(RedisConstants.CAPITAL_TOPLIMIT_+instCode);
		BigDecimal lcapitalToplimit = new BigDecimal(capitalToplimit);
		BigDecimal assetAcount = new BigDecimal(hjhPlanAsset.getAccount());
		
		if (BigDecimal.ZERO.compareTo(lcapitalToplimit) >= 0) {
			_log.info("资产编号："+hjhPlanAsset.getAssetId()+" 风险保证金小于等于零 "+capitalToplimit);
			// 风险保证金小于等于0不能发标
			return false;
		}
		
		if(assetAcount.compareTo(lcapitalToplimit) > 0){
			_log.info("资产编号："+hjhPlanAsset.getAssetId()+" 金额： "+assetAcount+" 风险保证金小于等于零 "+capitalToplimit);
			// 风险保证金不够不能发标
			return false;
		}
		
		return true;
	}
	
}
