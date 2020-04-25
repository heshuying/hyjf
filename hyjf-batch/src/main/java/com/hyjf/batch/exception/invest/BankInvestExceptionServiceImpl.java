package com.hyjf.batch.exception.invest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.customize.batch.BatchBorrowTenderCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class BankInvestExceptionServiceImpl extends BaseServiceImpl implements BankInvestExceptionService {

    Logger _log = LoggerFactory.getLogger(BankInvestExceptionServiceImpl.class);

    @Override
    public void insertAuthCode(List<BatchBorrowTenderCustomize> list) {
        // 遍历循环,根据原订单号查询江西银行的投标记录,并将返回的authcode表插入原表
        if (list != null && list.size() > 0) {
            _log.info("开始处理掉单的出借,处理件数:[" + list.size() + "].");
            for (BatchBorrowTenderCustomize batchBorrowTenderCustomize : list) {
                try {
                    // 获得原出借的订单号
                    String orderid = batchBorrowTenderCustomize.getNid();
                    _log.info("开始处理出借订单号:[" + orderid + "].");
                    // 用户电子账户号
                    String accountId = batchBorrowTenderCustomize.getAccountNo();
                    // 出借用户ID
                    String userId = String.valueOf(batchBorrowTenderCustomize.getUserId());
                    // 根据相应信息接口查询订单
                    BankCallBean bean = queryBorrowTenderList(accountId, orderid, userId);
                    if (bean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
                        if (StringUtils.isNoneBlank(bean.getAuthCode())) {
                            // 将返回的authcode放入出借表中
                            BorrowTender record = this.borrowTenderMapper.selectByPrimaryKey(batchBorrowTenderCustomize.getId());
                            record.setAuthCode(bean.getAuthCode());
                            borrowTenderMapper.updateByPrimaryKeySelective(record);
                            _log.info("出借异常处理成功,出借记录ID:[" + batchBorrowTenderCustomize.getId() + "],出借订单号:[" + orderid + "],银行返回授权码:[" + bean.getAuthCode() + "].");
                        }
                    }
                } catch (Exception e) {
                    _log.info("=========================cwayng 处理出借异常失败!=====");
                }
            }
        }
    }

    @Override
    public List<BatchBorrowTenderCustomize> queryAuthCodeBorrowTenderList() {
        // 查询出出借表authcode为空的记录
        List<BatchBorrowTenderCustomize> list = this.batchBorrowTenderExceptionCustomizeMapper.queryAuthCodeBorrowTenderList();
        return list;
    }

    /**
     * 根据相应信息接口查询投标申请
     *
     * @param accountId
     * @param orgOrderId
     * @return
     */
    private BankCallBean queryBorrowTenderList(String accountId, String orgOrderId, String userId) {
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallConstant.TXCODE_BID_APPLY_QUERY);// 消息类型
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));
        bean.setTxDate(GetOrderIdUtils.getTxDate());
        bean.setTxTime(GetOrderIdUtils.getTxTime());
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));
        bean.setChannel(BankCallConstant.CHANNEL_PC);
        bean.setAccountId(accountId);// 电子账号
        bean.setOrgOrderId(orgOrderId);
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.parseInt(userId)));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        bean.setLogUserId(userId);
        bean.setLogRemark("出借人投标申请查询");
        // 调用接口
        return BankCallUtils.callApiBg(bean);
    }

}
