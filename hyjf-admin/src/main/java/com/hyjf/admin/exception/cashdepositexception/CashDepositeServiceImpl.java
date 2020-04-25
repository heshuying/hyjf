package com.hyjf.admin.exception.cashdepositexception;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.util.StringUtil;
import com.hyjf.mybatis.mapper.auto.BorrowMapper;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhPlanAssetExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lisheng
 * @version CashDepositeServiceImpl, v0.1 2018/5/22 15:47
 */
@Service
public class CashDepositeServiceImpl extends BaseServiceImpl implements CashDepositeService {

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    /**
     * 修改 hyjf_hjh_plan_asset  汇计划资产表 的状态  status： 0 补交保证金，15为流标
     * @param assetId 界面选择的资产编号
     * @param menuHide 1  重新验证保证金 0 流标
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCashDepositeStatus(String assetId,String menuHide) throws Exception {
        HjhPlanAssetExample hjhPlanAssetExample = new HjhPlanAssetExample();
        HjhPlanAssetExample.Criteria criteria = hjhPlanAssetExample.createCriteria();
        criteria.andAssetIdEqualTo(assetId);
        List<HjhPlanAsset> hjhPlanAssets = hjhPlanAssetMapper.selectByExample(hjhPlanAssetExample);
        HjhPlanAsset asset = hjhPlanAssets.get(0);
        String instCode = asset.getInstCode();
        String borrowNid = asset.getBorrowNid();

        HjhPlanAsset hjhPlanAsset = new HjhPlanAsset();
        if (StringUtils.isNotBlank(assetId)) {
            if (StringUtils.equals("1", menuHide) && StringUtils.isBlank(borrowNid)) {
                hjhPlanAsset.setStatus(0);//初始
            } else if (StringUtils.equals("1", menuHide)) {
                hjhPlanAsset.setStatus(5);//初审中
            } else {
                hjhPlanAsset.setStatus(15);//流标
            }
            int i = hjhPlanAssetMapper.updateByExampleSelective(hjhPlanAsset, hjhPlanAssetExample);
            if (i > 0 && StringUtils.equals("1", menuHide) && StringUtils.isBlank(borrowNid)) {
                String redisKey = "borrowsend:" + instCode + assetId;
                if (RedisUtils.exists(redisKey)) {
                    RedisUtils.del(redisKey);
                }
            } else if (i > 0 && StringUtils.equals("1", menuHide) && StringUtils.isNotBlank(borrowNid)) {
                String redisKey = "borrowpreaudit:" + instCode + assetId;
                if (RedisUtils.exists(redisKey)) {
                    RedisUtils.del(redisKey);
                }
            }
        }
    }

    /**
     * 让重新验证保证金的标的  进入自动录标队列
     * @param assetId  资产编号
     * @param instCode 资产来源编号
     */
    public void sendToMQ(String assetId,String instCode) throws AmqpException {
        // 加入到消息队列
        Map<String, String> params = new HashMap<String, String>();
        params.put("mqMsgId", GetCode.getRandomCode(10));
        params.put("assetId", assetId);
        params.put("instCode", instCode);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_COUPON, RabbitMQConstants.ROUTINGKEY_BORROW_SEND, JSONObject.toJSONString(params));

    }
}
