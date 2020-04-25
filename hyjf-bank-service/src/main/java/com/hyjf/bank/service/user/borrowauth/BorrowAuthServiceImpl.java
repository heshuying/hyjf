package com.hyjf.bank.service.user.borrowauth;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.web.WebBorrowAuthCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BorrowAuthServiceImpl extends BaseServiceImpl implements BorrowAuthService {

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
    
    /**标的状态7 */
    public static final Integer BORROW_STATUS_WITE_AUTHORIZATION = 7;
    /**是否受托支付 0否1 是 */
    public static final Integer ENTRUSTED_FLG = 1;
    
    // 获取借款人待授权列表
    @Override
    public int countBorrowNeedAuthRecordTotal(BorrowAuthRequestBean form) {
        // 条件  ：申请人用户ID   项目编号  项目添加时间
        Map<String, Object> params = getParams(form);
        int total = borrowAuthCustomizeMapper.countBorrowNeedAuthRecordTotal(params);

        return total;
    }

    private Map<String, Object> getParams(BorrowAuthRequestBean form) {
        Map<String, Object> params = new HashMap<String, Object>();  
        String startDate = StringUtils.isNotEmpty(form.getStartDate()) ? form.getStartDate() : null;
        String endDate = StringUtils.isNotEmpty(form.getEndDate()) ? form.getEndDate() : null;
        String borrowNid = StringUtils.isNotEmpty(form.getBorrowNid()) ? form.getBorrowNid() : null;
        params.put("user_id", form.getUserId());// 用户ID
        params.put("borrowNid", borrowNid); // 标的编号
        params.put("status", BorrowAuthServiceImpl.BORROW_STATUS_WITE_AUTHORIZATION); // 标的状态
        params.put("entrusted_flg", BorrowAuthServiceImpl.ENTRUSTED_FLG); // 是否受托支付
        
        if (StringUtils.isNotBlank(endDate)) {
            // 转化为时间戳
            endDate = GetDate.getSearchEndTime(endDate);
        }
        if (StringUtils.isNotBlank(startDate)) {
            // 转化为时间戳
            startDate = GetDate.getSearchStartTime(startDate);
        }
        System.out.println(startDate+"===="+endDate);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        
        return params;
    }

    // 分页查询待授权标的授权列表
    @Override
    public List<WebBorrowAuthCustomize> searchBorrowNeedAuthList(BorrowAuthRequestBean form, int limitStart, int limitEnd) {
        Map<String, Object> params = getParams(form); 
        
        if (limitStart == 0 || limitStart > 0) {
            params.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            params.put("limitEnd", limitEnd);
        }
        List<WebBorrowAuthCustomize> list = null;
        list = borrowAuthCustomizeMapper.searchBorrowNeedAuthList(params);
        
        return list;
    }

    @Override
    public int countBorrowAuthedRecordTotal(BorrowAuthRequestBean form) {
        // 条件  ：申请人用户ID   项目编号  项目添加时间
        Map<String, Object> params = getParams(form);
        int total = borrowAuthCustomizeMapper.countBorrowAuthedRecordTotal(params);

        return total;
    }

    @Override
    public List<WebBorrowAuthCustomize> searchBorrowAuthedList(BorrowAuthRequestBean form, int limitStart, int limitEnd) {
        Map<String, Object> params = getParams(form); 
        
        if (limitStart == 0 || limitStart > 0) {
            params.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            params.put("limitEnd", limitEnd);
        }
        List<WebBorrowAuthCustomize> list = null;
        list = borrowAuthCustomizeMapper.searchBorrowAuthedList(params);
        
        return list;
    }

    @Override
    public Borrow selectBorrowByProductId(String borrowId) {
        BorrowExample example = new BorrowExample();
        BorrowExample.Criteria crt = example.createCriteria();
        crt.andBorrowNidEqualTo(borrowId);
        List<Borrow> borrows = this.borrowMapper.selectByExample(example);
        if (borrows != null && borrows.size() > 0) {
            return borrows.get(0);
        }
        return null;
    }

    @Override
    public STZHWhiteList getSTZHWhiteListByUserID(Integer userId, Integer stzUserId) {
        STZHWhiteListExample example = new STZHWhiteListExample();
        example.createCriteria().andStUserIdEqualTo(stzUserId).andUserIdEqualTo(userId);
        List<STZHWhiteList> lists = this.sTZHWhiteListMapper.selectByExample(example);
        if (lists != null && lists.size() > 0) {
            return lists.get(0);
            
        }
        return null;
    }

    @Override
    public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLogByOrderId(String logOrderId) {
        ChinapnrExclusiveLogExample example = new ChinapnrExclusiveLogExample();
        ChinapnrExclusiveLogExample.Criteria cra = example.createCriteria();
        cra.andOrdidEqualTo(logOrderId);
        List<ChinapnrExclusiveLogWithBLOBs> result = chinapnrExclusiveLogMapper.selectByExampleWithBLOBs(example);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public boolean updateTrusteePaySuccess(BankCallBean bean) {
        // 修改huiyingdai_borrow表状态  根据返回值productId对应表的id   status改为2
        // 修改hyjf_hjh_plan_asset status改为
        String nid = bean.getProductId();
        BorrowWithBLOBs borrow = new BorrowWithBLOBs();
        borrow.setBorrowNid(nid);
        borrow.setStatus(1);
        borrow.setTrusteePayTime(GetDate.getNowTime10());// 受托支付完成时间
        BorrowExample example = new BorrowExample();
        example.createCriteria().andBorrowNidEqualTo(nid).andStatusEqualTo(7);
        boolean flag = this.borrowMapper.updateByExampleSelective(borrow, example) > 0 ? true : false;
        if(flag){
            HjhPlanAsset hp = new HjhPlanAsset();
            hp.setStatus(5);
            hp.setBorrowNid(nid);
            HjhPlanAssetExample hpexp = new HjhPlanAssetExample();
            hpexp.createCriteria().andBorrowNidEqualTo(nid);
            flag = this.hjhPlanAssetMapper.updateByExampleSelective(hp, hpexp) > 0 ? true : false;
        }
        return flag;
    }

    @Override
    public CorpOpenAccountRecord getCorpOpenAccountRecord(int userId) {
        CorpOpenAccountRecordExample example = new CorpOpenAccountRecordExample();
        CorpOpenAccountRecordExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        cra.andIsBankEqualTo(1);//江西银行
        List<CorpOpenAccountRecord> list = this.corpOpenAccountRecordMapper.selectByExample(example);
        if(list != null && list.size() > 0){
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
    
    /**
     * 获取标的流程配置信息
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
	
}
