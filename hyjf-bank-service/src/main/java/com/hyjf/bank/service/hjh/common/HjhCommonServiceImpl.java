package com.hyjf.bank.service.hjh.common;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.HjhAccede;

/**
 * 
 * 汇计划更新表
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年12月15日
 */
@Service
public class HjhCommonServiceImpl extends BaseServiceImpl implements HjhCommonService{
	Logger _log = LoggerFactory.getLogger(HjhCommonServiceImpl.class);
    /**
     * 汇计划全部流程用更新用户的账户表
     * @param hjhProcessFlg
     * @param userId
     * @param amount
     * @param interest
     * @return
     */
    @Override
    public Boolean updateAccountForHjh(String hjhProcessFlg, Integer userId, BigDecimal amount, BigDecimal interest) {
        _log.info("updateAccountForHjh:" + hjhProcessFlg + "," + userId + "," +  amount + "," +  interest);
        //更新用户的账户表
        Account accountBean = new Account();
        accountBean.setUserId(userId);
        switch (hjhProcessFlg) {
            case CustomConstants.HJH_PROCESS_B:
                //计划订单-自动投标
            case CustomConstants.HJH_PROCESS_BF:
                //计划订单-自动投标/复投
                //amount=自动投标金额=b
                accountBean.setPlanBalance(amount.negate()); // 汇计划可用余额  -b
                accountBean.setPlanFrost(amount); // 汇计划冻结金额 +b
                break;
                
            case CustomConstants.HJH_PROCESS_D:
                //计划订单-自动承接(出借)
            case CustomConstants.HJH_PROCESS_DF:
                //计划订单-自动承接(复投)
                //amount=自动投标金额=d
                accountBean.setPlanBalance(amount.negate()); // 汇计划可用余额  -d
                break;
                
            case CustomConstants.HJH_PROCESS_F:
                //计划订单锁定期-债权回款（承接和还款，要复投）
                //amount=回款总额=f
                accountBean.setPlanBalance(amount); // 汇计划可用余额  +f
                break;
            case CustomConstants.HJH_PROCESS_H:
                //汇计划清算-债权回款（承接和还款，不复投）
                //amount=回款总额=h
                accountBean.setPlanFrost(amount); // 汇计划冻结金额 +h
                break;
            default:
                break;
        }
        
        Boolean accountFlag = this.adminAccountCustomizeMapper.updateAccountForHjhProcess(accountBean) > 0 ? true : false;
        if (!accountFlag) {
            throw new RuntimeException("用户账户信息表更新失败");
        }

        return accountFlag;
    }

    /**
     * 汇计划重算更新汇计划加入明细表
     * @param hjhProcessFlg
     * @param id
     * @param amount
     * @param interest
     * @return
     */
    @Override
    public Boolean updateHjhAccedeForHjh(String hjhProcessFlg, Integer id, BigDecimal amount, BigDecimal interest, BigDecimal serviceFee) {
        _log.info("updateHjhAccedeForHjh:" + hjhProcessFlg + "," + id + "," +  amount + "," +  interest + "," +  serviceFee);
        //更新用户的账户表
        HjhAccede hjhAccede = new HjhAccede();
        
        //获取当前时间
        hjhAccede.setUpdateTime(GetDate.getNowTime10());
        hjhAccede.setUpdateUser(1);
        hjhAccede.setId(id);
        switch (hjhProcessFlg) {
            case CustomConstants.HJH_PROCESS_B:
                //计划订单-自动投标
                //amount=自动投标金额=b
                hjhAccede.setAlreadyInvest(amount);// 计划订单已出借金额 +b
            case CustomConstants.HJH_PROCESS_BF:
                //计划订单-自动复投
                //amount=自动投标金额=b
                hjhAccede.setAvailableInvestAccount(amount.negate()); // 计划订单可用余额  -b
                hjhAccede.setFrostAccount(amount); // 计划订单冻结金额 +b
                // add 汇计划三期 计划订单出借笔数累加 liubin 20180515 start
                hjhAccede.setInvestCounts(1);// 出借笔数 +1
                // add 汇计划三期 计划订单出借笔数累加 liubin 20180515 end
                break;
                
            case CustomConstants.HJH_PROCESS_D:
                //计划订单-自动承接(出借)
                //amount=自动投标金额=d
                hjhAccede.setAlreadyInvest(amount);// 计划订单已出借金额 +d
            case CustomConstants.HJH_PROCESS_DF:
                //计划订单-自动承接(出借/复投)
                //amount=自动投标金额=d
                hjhAccede.setAvailableInvestAccount(amount.negate()); // 计划订单可用余额  -d
                // add 汇计划三期 计划订单出借笔数累加 liubin 20180515 start
                hjhAccede.setInvestCounts(1); // 出借笔数 +1
                // add 汇计划三期 计划订单出借笔数累加 liubin 20180515 end
                break;
            case CustomConstants.HJH_PROCESS_F:
                //计划订单锁定期-债权回款（承接和还款，要复投）
                //amount=回款总额=f
                hjhAccede.setAvailableInvestAccount(amount); // 计划订单可用余额  +f
                // add 汇计划三期 汇计划自动出借(收债转服务费) liubin 20180515 start
                hjhAccede.setLqdServiceFee(serviceFee); // 债转服务费累计
                // add 汇计划三期 汇计划自动出借(收债转服务费) liubin 20180515 end
                break;
            case CustomConstants.HJH_PROCESS_H:
                //汇计划清算-债权回款（承接和还款，不复投）
                //amount=回款总额=h
                hjhAccede.setFrostAccount(amount); // 计划订单冻结金额 +h
                // add 汇计划三期 汇计划自动出借(收债转服务费) liubin 20180515 start
                hjhAccede.setLqdServiceFee(serviceFee); // 债转服务费累计
                // add 汇计划三期 汇计划自动出借(收债转服务费) liubin 20180515 end
                break;
            default:
                break;
        }
        
        Boolean accountFlag = this.hjhPlanCustomizeMapper.updateHjhAccedeForHjhProcess(hjhAccede) > 0 ? true : false;
        if (!accountFlag) {
            throw new RuntimeException("用户账户信息表更新失败");
        }
        
        return accountFlag;
    }
}
