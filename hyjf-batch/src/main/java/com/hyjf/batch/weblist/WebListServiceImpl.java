package com.hyjf.batch.weblist;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.AccountWebListExample;
import com.hyjf.mybatis.model.auto.BorrowRecover;
import com.hyjf.mybatis.model.auto.BorrowRecoverExample;
import com.hyjf.mybatis.model.auto.BorrowTender;
import com.hyjf.mybatis.model.auto.BorrowTenderExample;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.EmployeeCustomize;
import com.hyjf.mybatis.model.customize.WebListCustomize;

/**
 * 自动扣款(放款服务)
 *
 * @author Administrator
 *
 */
@Service
public class WebListServiceImpl extends BaseServiceImpl implements WebListService {


    /**
     * 债转服务费
     *
     * @return
     */
    public int creditFeeService() throws Exception {

        return 0;
    }

    /**
     * 线下充值
     *
     * @return
     */
    public int outLineService() throws Exception {

        return 0;
    }

    /**
     * 计算网站收到情况 （服务费）
     *
     * @return
     */
    public int incomeLoanService() throws Exception {

        // 取得出借列表(1000条)
        List<BorrowTender> borrowTenderList = getBorrowTenderList();

        if (borrowTenderList != null && borrowTenderList.size() > 0) {
            BorrowRecover borrowRecover = null;
            for (BorrowTender borrowTender : borrowTenderList) {
                // 取得还款明细
                borrowRecover = getBorrowRecoverByTenderId(borrowTender.getId());

                // 插入网站收支明细记录
                AccountWebList accountWebList = new AccountWebList();
                accountWebList.setOrdid(borrowTender.getNid());// 订单号
                accountWebList.setBorrowNid(borrowTender.getBorrowNid()); // 出借编号
                accountWebList.setUserId(borrowTender.getUserId()); // 出借者
                accountWebList.setAmount(borrowTender.getLoanFee()); // 服务费
                accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入 2支出
                accountWebList.setTrade(CustomConstants.TRADE_LOANFEE); // 服务费
                accountWebList.setTradeType(CustomConstants.TRADE_LOANFEE_NM); // 服务费
                accountWebList.setCreateTime(borrowRecover.getCreateTime());
                accountWebList.setRemark(borrowTender.getBorrowNid());
                int accountWebListCnt = insertAccountWebList(accountWebList);
                if (accountWebListCnt > 0) {
                    // 更新出借表
                    udpateBorrowTenderOfWeb(borrowTender.getId(), 1);
                }
            }
        }

        return 0;
    }

    /**
     * 计算网站收到情况 （管理费）
     *
     * @return
     */
    public int incomeFeeService() throws Exception {

        // 取得还款明细列表(1000条)
        List<WebListCustomize> webListCustomizesList = getBorrowRecoverByStatus();
        if (webListCustomizesList != null && webListCustomizesList.size() > 0) {
            for (WebListCustomize webListCustomize : webListCustomizesList) {
                // 插入网站收支明细记录
                AccountWebList accountWebList = new AccountWebList();
                accountWebList.setOrdid(webListCustomize.getOrdid());// 订单号
                accountWebList.setBorrowNid(webListCustomize.getBorrowNid()); // 出借编号
                accountWebList.setUserId(webListCustomize.getUserId()); // 出借者
                accountWebList.setAmount(webListCustomize.getAmount()); // 管理费
                accountWebList.setType(CustomConstants.TYPE_IN); // 类型1收入,2支出
                accountWebList.setTrade(CustomConstants.TRADE_REPAYFEE); // 管理费
                accountWebList.setTradeType(CustomConstants.TRADE_REPAYFEE_NM); // 账户管理费
                accountWebList.setRemark(webListCustomize.getBorrowNid()); // 出借编号
                accountWebList.setCreateTime(GetterUtil.getInteger(webListCustomize.getCreateTime()));
                insertAccountWebList(accountWebList);
            }
        }

        return 0;
    }

    /**
     * 充值手续费返还
     *
     * @return
     */
    public int rechargeFeeService() throws Exception {

        // 取得还款明细列表(1000条)
        List<AccountList> accountLists = getAccountListByWeb(CustomConstants.TRADE_RECHAFEE_REMARK);
        if (accountLists != null && accountLists.size() > 0) {
            for (AccountList accountList : accountLists) {
                // 插入网站收支明细记录
                AccountWebList accountWebList = new AccountWebList();
                accountWebList.setOrdid(accountList.getNid());// 订单号
                accountWebList.setUserId(accountList.getUserId()); // 出借者
                accountWebList.setAmount(accountList.getAmount()); // 管理费
                accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入 2支出
                accountWebList.setTrade(CustomConstants.TRADE_RECHAFEE); // 充值返现
                accountWebList.setTradeType(CustomConstants.TRADE_RECHAFEE_NM); // 充值返现
                accountWebList.setRemark(CustomConstants.TRADE_RECHAFEE_REMARK); // 充值手续费返还
                accountWebList.setCreateTime(GetterUtil.getInteger(accountList.getCreateTime()));
                int accountWebListCnt = insertAccountWebList(accountWebList);
                if (accountWebListCnt > 0) {
                    // 更新还款明细表
                    udpateAccountListOfWeb(accountList.getId(), 2);
                }
            }
        }

        return 0;
    }

    /**
     * 出借推广提成
     *
     * @return
     */
    public int promoteCommissionService() throws Exception {

        // 取得还款明细列表(1000条)
        List<AccountList> accountLists = getAccountListByWebLikeRemark(CustomConstants.TRADE_TGTC_REMARK);
        if (accountLists != null && accountLists.size() > 0) {
            for (AccountList accountList : accountLists) {
                // 插入网站收支明细记录
                AccountWebList accountWebList = new AccountWebList();
                accountWebList.setOrdid(accountList.getNid());// 订单号
                accountWebList.setUserId(accountList.getUserId()); // 出借者
                accountWebList.setAmount(accountList.getAmount()); // 管理费
                accountWebList.setType(CustomConstants.TYPE_OUT); // 类型1收入 2支出
                accountWebList.setTrade(CustomConstants.TRADE_TGTC); // 提成
                accountWebList.setTradeType(CustomConstants.TRADE_TGTC_NM); // 出借推广提成
                accountWebList.setRemark(getBorrowNidByOrdId(accountList.getNid())); // 出借推广提成
                accountWebList.setCreateTime(GetterUtil.getInteger(accountList.getCreateTime()));
                int accountWebListCnt = insertAccountWebList(accountWebList);
                if (accountWebListCnt > 0) {
                    // 更新还款明细表
                    udpateAccountListOfWeb(accountList.getId(), 1);
                }
            }
        }

        return 0;
    }

    /**
     * 其他
     *
     * @return
     */
    public int otherService() throws Exception {
        return 0;
    }

    /**
     * 取得出借列表
     *
     * @param tenderId
     * @return
     */
    private List<BorrowTender> getBorrowTenderList() {
        BorrowTenderExample borrowTenderExample = new BorrowTenderExample();
        // ApiStatus=已放款, web=0, LoanFee>0
        borrowTenderExample.createCriteria().andApiStatusEqualTo(1).andWebEqualTo(0).andLoanFeeGreaterThan(BigDecimal.ZERO);
        borrowTenderExample.setLimitEnd(0);
        borrowTenderExample.setLimitEnd(1000);
        borrowTenderExample.setOrderByClause(" id asc ");
        List<BorrowTender> borrowTenderList = borrowTenderMapper.selectByExample(borrowTenderExample);

        return borrowTenderList;
    }

    /**
     * 根据TenderId 取得BorrowRecover
     *
     * @param tenderId
     * @return
     */
    private BorrowRecover getBorrowRecoverByTenderId(Integer tenderId) {
        BorrowRecoverExample borrowRecoverExample = new BorrowRecoverExample();
        borrowRecoverExample.createCriteria().andTenderIdEqualTo(tenderId);
        List<BorrowRecover> borrowRecoverList = borrowRecoverMapper.selectByExample(borrowRecoverExample);
        if (borrowRecoverList != null && borrowRecoverList.size() > 0) {
            return borrowRecoverList.get(0);
        }

        return null;
    }

    /**
     * 取得还款明细列表
     *
     * @return
     */
    private List<WebListCustomize> getBorrowRecoverByStatus() {
        WebListCustomize customize = new WebListCustomize();
        customize.setLimitStart(0);
        customize.setLimitEnd(1000);
        return webListCustomizeMapper.selectBorrowRecoverOfWeb(customize);
    }

    /**
     * 取得收支明细列表
     *
     * @return
     */
    private List<AccountList> getAccountListByWeb(String remark) {
        AccountListExample accountListExample = new AccountListExample();
        // remark=充值手续费返还, web=0, RecoverFee>0
        accountListExample.createCriteria().andWebEqualTo(0).andRemarkEqualTo(remark);
        accountListExample.setLimitEnd(0);
        accountListExample.setLimitEnd(1000);
        accountListExample.setOrderByClause(" id asc ");
        return accountListMapper.selectByExample(accountListExample);
    }

    /**
     * 取得收支明细列表
     *
     * @return
     */
    private List<AccountList> getAccountListByWebLikeRemark(String remark) {
        AccountListExample accountListExample = new AccountListExample();
        // remark=充值手续费返还, web=0, RecoverFee>0
        accountListExample.createCriteria().andWebEqualTo(0).andRemarkLike("%" + remark + "%");
        accountListExample.setLimitEnd(0);
        accountListExample.setLimitEnd(1000);
        accountListExample.setOrderByClause(" id asc ");
        return accountListMapper.selectByExample(accountListExample);
    }

    /**
     * 判断网站收支是否存在
     *
     * @param nid
     * @return
     */
    private int countAccountWebList(String nid, String trade) {
        AccountWebListExample example = new AccountWebListExample();
        example.createCriteria().andOrdidEqualTo(nid).andTradeEqualTo(trade);
        return this.accountWebListMapper.countByExample(example);
    }

    /**
     * 插入网站收支记录
     *
     * @param nid
     * @return
     */
    private int insertAccountWebList(AccountWebList accountWebList) {
        if (countAccountWebList(accountWebList.getOrdid(), accountWebList.getTrade()) == 0) {
            // 设置部门名称
            setDepartments(accountWebList);
            // 插入
            return this.accountWebListMapper.insertSelective(accountWebList);
        }
        return 0;
    }

    /**
     * 设置部门名称
     *
     * @param accountWebList
     */
    private void setDepartments(AccountWebList accountWebList) {
        if (accountWebList != null) {
            Integer userId = accountWebList.getUserId();
            UsersInfo usersInfo = getUsersInfoByUserId(userId);

            if (usersInfo != null) {

                Integer attribute = usersInfo.getAttribute();

                if (attribute != null) {
                    // 查找用户的的推荐人
                    Users users = getUsersByUserId(userId);

                    Integer refUserId = users.getReferrer();
                    SpreadsUsersExample spreadsUsersExample = new SpreadsUsersExample();
                    SpreadsUsersExample.Criteria spreadsUsersExampleCriteria = spreadsUsersExample.createCriteria();
                    spreadsUsersExampleCriteria.andUserIdEqualTo(userId);
                    List<SpreadsUsers> sList = spreadsUsersMapper.selectByExample(spreadsUsersExample);
                    if (sList != null && !sList.isEmpty()) {
                        refUserId = sList.get(0).getSpreadsUserid();
                    }

                    // 如果是线上员工或线下员工，推荐人的userId和username不插
                    if (users != null && (attribute == 2 || attribute == 3)) {
                        // 查找用户信息
                        EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(userId);
                        if (employeeCustomize != null) {
                            accountWebList.setRegionName(employeeCustomize.getRegionName());
                            accountWebList.setBranchName(employeeCustomize.getBranchName());
                            accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
                        }
                    }
                    // 如果是无主单，全插
                    else if (users != null && (attribute == 1)) {
                        // 查找用户推荐人
                        EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
                        if (employeeCustomize != null) {
                            accountWebList.setRegionName(employeeCustomize.getRegionName());
                            accountWebList.setBranchName(employeeCustomize.getBranchName());
                            accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
                        }
                    }
                    // 如果是有主单
                    else if (users != null && (attribute == 0)) {
                        // 查找用户推荐人
                        EmployeeCustomize employeeCustomize = employeeCustomizeMapper.selectEmployeeByUserId(refUserId);
                        if (employeeCustomize != null) {
                            accountWebList.setRegionName(employeeCustomize.getRegionName());
                            accountWebList.setBranchName(employeeCustomize.getBranchName());
                            accountWebList.setDepartmentName(employeeCustomize.getDepartmentName());
                        }
                    }
                }
                accountWebList.setTruename(usersInfo.getTruename());
                accountWebList.setFlag(1);
            }
        }

    }

    /**
     * 更新出借表的web
     *
     * @param id
     * @param web
     * @return
     */
    private int udpateBorrowTenderOfWeb(Integer id, Integer web) {
        if (id != null) {
            BorrowTender borrowTender = new BorrowTender();
            borrowTender.setId(id);
            borrowTender.setWeb(web);
            return this.borrowTenderMapper.updateByPrimaryKeySelective(borrowTender);
        }
        return 0;
    }



    /**
     * 更新收支明细表的web
     *
     * @param id
     * @param web
     * @return
     */
    private int udpateAccountListOfWeb(Integer id, Integer web) {
        if (id != null) {
            AccountList accountList = new AccountList();
            accountList.setId(id);
            accountList.setWeb(web);
            return this.accountListMapper.updateByPrimaryKeySelective(accountList);
        }
        return 0;
    }

    /**
     * 根据出借订单号取出借编号
     *
     * @param ordId
     * @return
     */
    private String getBorrowNidByOrdId(String ordId) {
        BorrowTenderExample example = new BorrowTenderExample();
        example.createCriteria().andNidEqualTo(ordId);
        List<BorrowTender> list = this.borrowTenderMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0).getBorrowNid();
        }
        return null;
    }
}
